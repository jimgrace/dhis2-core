/*
 * Copyright (c) 2004-2024, University of Oslo
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors 
 * may be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.hisp.dhis.pushanalysis.scheduling;

import static org.hisp.dhis.scheduling.JobProgress.FailurePolicy.SKIP_ITEM;

import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.hisp.dhis.email.EmailService;
import org.hisp.dhis.scheduling.Job;
import org.hisp.dhis.scheduling.JobConfiguration;
import org.hisp.dhis.scheduling.JobProgress;
import org.hisp.dhis.scheduling.JobType;
import org.hisp.dhis.scheduling.parameters.HtmlPushAnalyticsJobParameters;
import org.hisp.dhis.scheduling.parameters.HtmlPushAnalyticsJobParameters.ViewMode;
import org.hisp.dhis.setting.SystemSettings;
import org.hisp.dhis.setting.SystemSettingsProvider;
import org.hisp.dhis.user.User;
import org.hisp.dhis.user.UserService;
import org.hisp.dhis.user.UserSettingsService;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Sends HTML push analytics (a dashboard page) to users of the specified user group.
 *
 * <p>An email contains the HTML of a dashboard given by UID. This HTML is generated by an external
 * service. The URL (template) for this service is configured via {@link
 * SystemSettings#getHtmlPushAnalyticsUrl()}. This service uses a dedicated account to log in and
 * impersonate the user that should view the dashboard. The content is then "scraped" and send back
 * to this job. Therefore, the user that is viewing and the dashboard that is viewed are given as
 * parameters in the called URL. By convention a placeholder named <code>{username}</code> and
 * <code>{id}</code> (for the dashboard) are expected to exist in the URL template configured via
 * the setting.
 *
 * <p>The job has 2 view modes: Either the dashboard is viewed as the user receiving the email or as
 * the user running the job.
 *
 * @author Jan Bernitt
 * @since 2.41
 */
@Component
@RequiredArgsConstructor
public class HtmlPushAnalyticsJob implements Job {

  private final EmailService emailService;
  private final SystemSettingsProvider settingsProvider;
  private final UserService userService;
  private final UserSettingsService userSettingsService;
  private final RestTemplate restTemplate;

  @Override
  public JobType getJobType() {
    return JobType.HTML_PUSH_ANALYTICS;
  }

  @Override
  public void execute(JobConfiguration config, JobProgress progress) {
    HtmlPushAnalyticsJobParameters params =
        (HtmlPushAnalyticsJobParameters) config.getJobParameters();
    String urlTemplate = settingsProvider.getCurrentSettings().getHtmlPushAnalyticsUrl();

    progress.startingProcess("HTML push analytics");

    Map<String, String> receiversEmailsByUsername = stageComputeReceivers(progress, params);

    if (!stageValidateConfiguration(progress, params, urlTemplate, receiversEmailsByUsername)) {
      progress.failedProcess("Validation failed, job aborted");
      return;
    }

    String url = urlTemplate.replace("{id}", params.getDashboard());
    String subject = config.getName();
    if (params.getMode() == ViewMode.EXECUTOR) {
      String viewerId = config.getExecutedBy();
      User viewer = userService.getUser(viewerId);
      String viewerName = viewer.getUsername();
      String viewerUrl = substituteUrl(url, viewerName);
      progress.startingStage(
          "Fetching push analytics HTML for user %s (%s)".formatted(viewerName, viewerUrl));
      String body = progress.runStage(() -> getPushAnalyticsHtmlBody(viewerUrl));
      progress.startingStage(
          "Sending push analytics to %d receivers as viewed by %s"
              .formatted(receiversEmailsByUsername.size(), viewerName));
      progress.runStage(
          () ->
              emailService.sendEmail(
                  subject, body, Set.copyOf(receiversEmailsByUsername.values())));
    } else {
      progress.startingStage(
          "Sending push analytics to %d receivers as viewed by themselves"
              .formatted(receiversEmailsByUsername.size()),
          SKIP_ITEM);
      progress.runStageInParallel(
          8,
          receiversEmailsByUsername.entrySet(),
          e -> "For user %s (%s)".formatted(e.getKey(), substituteUrl(url, e.getKey())),
          e -> {
            String body =
                progress.runStage(() -> getPushAnalyticsHtmlBody(substituteUrl(url, e.getKey())));
            emailService.sendEmail(subject, body, Set.of(e.getValue()));
          });
    }
    progress.completedProcess(null);
  }

  @Nonnull
  private String substituteUrl(String urlTemplate, String username) {
    String url = urlTemplate.replace("{username}", username);
    if (url.contains("{locale}")) {
      Locale locale = userSettingsService.getUserSettings(username, true).evalUserLocale();
      url = url.replace("{locale}", locale == null ? "" : locale.toLanguageTag());
    }
    return url;
  }

  private Map<String, String> stageComputeReceivers(
      JobProgress progress, HtmlPushAnalyticsJobParameters params) {
    progress.startingStage("Computing receiving users");
    return progress.runStage(
        Map.of(),
        receivers -> "Found %d receivers".formatted(receivers.size()),
        () -> userService.getUserGroupUserEmailsByUsername(params.getReceivers()));
  }

  /**
   * Check all preconditions to be able to call the external service and send the emails.
   *
   * <ul>
   *   <li>email must be configured
   *   <li>the URL (template) for the external service must be configured
   *   <li>there receiver group must exist and have users with email addresses
   * </ul>
   *
   * @return true, if all preconditions are met and the job can start its work
   */
  private boolean stageValidateConfiguration(
      JobProgress progress,
      HtmlPushAnalyticsJobParameters params,
      String urlTemplate,
      Map<String, String> receiversEmailsByUsername) {
    progress.startingStage("Validating configuration");
    if (!emailService.emailConfigured()) {
      progress.failedStage("EMAIL gateway configuration does not exist");
      return false;
    }
    if (urlTemplate.isEmpty()) {
      progress.failedStage(
          "System setting for push analytics template %s is not configured"
              .formatted("keyHtmlPushAnalyticsUrl"));
      return false;
    }
    if (receiversEmailsByUsername.isEmpty()) {
      progress.failedStage(
          "User group %s of receivers does not exist, is empty or does not have a user with known email address"
              .formatted(params.getReceivers()));
      return false;
    }
    progress.completedStage(null);
    return true;
  }

  private String getPushAnalyticsHtmlBody(String url) {
    ResponseEntity<String> response =
        restTemplate.exchange(url, HttpMethod.GET, null, String.class);
    if (!response.getStatusCode().is2xxSuccessful())
      throw new RuntimeException(
          "Fetching push analytics was not successful, return status %s"
              .formatted(response.getStatusCode()));
    return response.getBody();
  }
}
