/*
 * Copyright (c) 2004-2022, University of Oslo
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * Neither the name of the HISP project nor the names of its contributors may
 * be used to endorse or promote products derived from this software without
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
package org.hisp.dhis.sms.listener;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import org.apache.commons.lang3.StringUtils;
import org.hisp.dhis.category.CategoryService;
import org.hisp.dhis.message.MessageSender;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.sms.command.SMSCommand;
import org.hisp.dhis.sms.command.code.SMSCode;
import org.hisp.dhis.sms.incoming.IncomingSms;
import org.hisp.dhis.sms.incoming.IncomingSmsService;
import org.hisp.dhis.system.util.SmsUtils;
import org.hisp.dhis.user.User;
import org.hisp.dhis.user.UserService;
import org.springframework.transaction.annotation.Transactional;

/** Created by zubair@dhis2.org on 11.08.17. */
@Transactional
public abstract class CommandSMSListener extends BaseSMSListener {
  private static final String DEFAULT_PATTERN = "([^\\s|=]+)\\s*\\=\\s*([^|=]+)\\s*(\\=|$)*+\\s*";

  protected static final int INFO = 1;

  protected static final int WARNING = 2;

  protected static final int ERROR = 3;

  protected final CategoryService dataElementCategoryService;

  protected final UserService userService;

  public CommandSMSListener(
      CategoryService dataElementCategoryService,
      UserService userService,
      IncomingSmsService incomingSmsService,
      MessageSender smsSender) {
    super(incomingSmsService, smsSender);
    checkNotNull(dataElementCategoryService);
    checkNotNull(userService);
    this.dataElementCategoryService = dataElementCategoryService;
    this.userService = userService;
  }

  @Override
  public boolean accept(@Nonnull IncomingSms sms) {
    return getSMSCommand(sms) != null;
  }

  @Override
  public void receive(@Nonnull IncomingSms sms, @Nonnull String username) {
    SMSCommand smsCommand = getSMSCommand(sms);

    Map<String, String> parsedMessage = this.parseMessageInput(sms, smsCommand);

    if (!hasCorrectFormat(sms, smsCommand)
        || !validateInputValues(parsedMessage, smsCommand, sms)) {
      return;
    }

    postProcess(sms, smsCommand, parsedMessage);
  }

  protected abstract void postProcess(
      IncomingSms sms, SMSCommand smsCommand, Map<String, String> parsedMessage);

  protected abstract SMSCommand getSMSCommand(IncomingSms sms);

  protected boolean hasCorrectFormat(IncomingSms sms, SMSCommand smsCommand) {
    String regexp = DEFAULT_PATTERN;

    if (smsCommand.getSeparator() != null && !smsCommand.getSeparator().trim().isEmpty()) {
      regexp = regexp.replaceAll("=", smsCommand.getSeparator());
    }

    Pattern pattern = Pattern.compile(regexp);

    Matcher matcher = pattern.matcher(sms.getText());

    if (!matcher.find()) {
      sendFeedback(
          StringUtils.defaultIfEmpty(
              smsCommand.getWrongFormatMessage(), SMSCommand.WRONG_FORMAT_MESSAGE),
          sms.getOriginator(),
          ERROR);
      return false;
    }

    return true;
  }

  protected Set<OrganisationUnit> getOrganisationUnits(IncomingSms sms) {
    User user = getUser(sms);

    if (user == null) {
      return new HashSet<>();
    }

    return SmsUtils.getOrganisationUnitsByPhoneNumber(
            sms.getOriginator(), Collections.singleton(user))
        .get(user.getUid());
  }

  protected User getUser(IncomingSms sms) {
    return userService.getUser(sms.getCreatedBy().getUid());
  }

  protected boolean validateInputValues(
      Map<String, String> commandValuePairs, SMSCommand smsCommand, IncomingSms sms) {
    if (!hasMandatoryParameters(commandValuePairs.keySet(), smsCommand.getCodes())) {
      sendFeedback(
          StringUtils.defaultIfEmpty(smsCommand.getDefaultMessage(), SMSCommand.PARAMETER_MISSING),
          sms.getOriginator(),
          ERROR);

      return false;
    }

    if (!hasOrganisationUnit(sms)) {
      sendFeedback(
          StringUtils.defaultIfEmpty(smsCommand.getNoUserMessage(), SMSCommand.NO_USER_MESSAGE),
          sms.getOriginator(),
          ERROR);

      return false;
    }

    if (hasMultipleOrganisationUnits(sms)) {
      sendFeedback(
          StringUtils.defaultIfEmpty(
              smsCommand.getMoreThanOneOrgUnitMessage(), SMSCommand.MORE_THAN_ONE_ORGUNIT_MESSAGE),
          sms.getOriginator(),
          ERROR);

      return false;
    }

    return true;
  }

  protected Map<String, String> parseMessageInput(IncomingSms sms, SMSCommand smsCommand) {
    HashMap<String, String> output = new HashMap<>();

    Pattern pattern = Pattern.compile(DEFAULT_PATTERN);

    if (!StringUtils.isBlank(smsCommand.getSeparator())) {
      String regex = DEFAULT_PATTERN.replaceAll("=", smsCommand.getSeparator());

      pattern = Pattern.compile(regex);
    }

    Matcher matcher = pattern.matcher(sms.getText());
    while (matcher.find()) {
      String key = matcher.group(1).trim();
      String value = matcher.group(2).trim();

      if (!StringUtils.isEmpty(key) && !StringUtils.isEmpty(value)) {
        output.put(key, value);
      }
    }

    return output;
  }

  private boolean hasMandatoryParameters(Set<String> keySet, Set<SMSCode> smsCodes) {
    for (SMSCode smsCode : smsCodes) {
      if (smsCode.isCompulsory() && !keySet.contains(smsCode.getCode())) {
        return false;
      }
    }

    return true;
  }

  private boolean hasOrganisationUnit(IncomingSms sms) {
    Collection<OrganisationUnit> orgUnits = getOrganisationUnits(sms);

    return !(orgUnits == null || orgUnits.isEmpty());
  }

  private boolean hasMultipleOrganisationUnits(IncomingSms sms) {
    List<User> users = userService.getUsersByPhoneNumber(sms.getOriginator());

    Set<OrganisationUnit> organisationUnits =
        users.stream()
            .flatMap(user -> user.getOrganisationUnits().stream())
            .collect(Collectors.toSet());

    return organisationUnits.size() > 1;
  }
}
