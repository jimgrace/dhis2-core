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
package org.hisp.dhis.security.spring2fa;

import static org.hisp.dhis.security.twofa.TwoFactorAuthService.TWO_FACTOR_AUTH_REQUIRED_RESTRICTION_NAME;
import static org.hisp.dhis.security.twofa.TwoFactorAuthUtils.isValid2FACode;

import com.google.common.base.Strings;
import java.util.Set;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hisp.dhis.feedback.ConflictException;
import org.hisp.dhis.feedback.ErrorCode;
import org.hisp.dhis.security.ForwardedIpAwareWebAuthenticationDetails;
import org.hisp.dhis.security.twofa.TwoFactorAuthService;
import org.hisp.dhis.security.twofa.TwoFactorType;
import org.hisp.dhis.user.UserDetails;
import org.hisp.dhis.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedCredentialsNotFoundException;
import org.springframework.stereotype.Component;

/**
 * @author Henning Håkonsen
 * @author Morten Svanæs
 */
@Slf4j
@Component
public class TwoFactorAuthenticationProvider extends DaoAuthenticationProvider {
  private UserService userService;
  private TwoFactorAuthService twoFactorAuthService;

  @Autowired
  public TwoFactorAuthenticationProvider(
      @Qualifier("userDetailsService") UserDetailsService detailsService,
      PasswordEncoder passwordEncoder,
      @Lazy UserService userService,
      @Lazy TwoFactorAuthService twoFactorAuthService) {

    this.userService = userService;
    this.twoFactorAuthService = twoFactorAuthService;
    setUserDetailsService(detailsService);
    setPasswordEncoder(passwordEncoder);
  }

  @Override
  public Authentication authenticate(Authentication auth) throws AuthenticationException {
    String username = auth.getName();
    String ip = "";

    if (auth.getDetails() instanceof ForwardedIpAwareWebAuthenticationDetails details) {
      ip = details.getIp();
    }

    log.debug("Login attempt: {}", username);

    // If enabled, temporarily block user with too many failed attempts
    if (userService.isLocked(username)) {
      log.warn("Temporary lockout for user: '{}'", username);
      throw new LockedException(String.format("IP is temporarily locked: %s", ip));
    }

    // Calls the UserDetailsService#loadUserByUsername(), to create the UserDetails object,
    // after the password is validated.
    Authentication result = super.authenticate(auth);
    UserDetails userDetails = (UserDetails) result.getPrincipal();

    // Prevents other authentication methods (e.g., OAuth2/LDAP),
    // to use password login.
    if (userDetails.isExternalAuth()) {
      log.info(
          "User has external authentication enabled, password login attempt aborted: '{}'",
          username);
      throw new BadCredentialsException(
          "Invalid login method, user is using external authentication");
    }

    // If the user requires 2FA, and it's not enabled, redirect to
    // the enrolment page, (via the CustomAuthFailureHandler)
    boolean has2FARestrictionOnRole =
        userDetails.hasAnyRestrictions(Set.of(TWO_FACTOR_AUTH_REQUIRED_RESTRICTION_NAME));
    if (!userDetails.isTwoFactorEnabled() && has2FARestrictionOnRole) {
      throw new TwoFactorAuthenticationEnrolmentException(
          "User must setup two-factor authentication first before logging in");
    }

    boolean isHTTPBasicRequest = !(auth.getDetails() instanceof TwoFactorWebAuthenticationDetails);
    if (userDetails.isTwoFactorEnabled() && isHTTPBasicRequest) {
      // If the user has 2FA enabled and tries to authenticate with HTTP Basic
      throw new PreAuthenticatedCredentialsNotFoundException(
          "User has 2FA enabled, but attempted to authenticate with a non-form based login method: "
              + userDetails.getUsername());
    }

    if (userDetails.isTwoFactorEnabled()
        && auth.getDetails() instanceof TwoFactorWebAuthenticationDetails authDetails) {
      validate2FACode(authDetails.getCode(), userDetails);
    }

    return new UsernamePasswordAuthenticationToken(
        userDetails, result.getCredentials(), result.getAuthorities());
  }

  private void validate2FACode(@CheckForNull String code, @Nonnull UserDetails userDetails) {
    TwoFactorType type = userDetails.getTwoFactorType();

    // Send 2FA code via Email if the user has email 2FA enabled and the code is empty.
    if (TwoFactorType.EMAIL_ENABLED == type && Strings.isNullOrEmpty(code)) {
      try {
        twoFactorAuthService.sendEmail2FACode(userDetails.getUsername());
      } catch (ConflictException e) {
        throw new TwoFactorAuthenticationException(ErrorCode.E3049.getMessage());
      }
      throw new TwoFactorAuthenticationException(ErrorCode.E3051.getMessage());
    }

    if (Strings.isNullOrEmpty(code) || StringUtils.deleteWhitespace(code).isEmpty()) {
      throw new TwoFactorAuthenticationException(ErrorCode.E3023.getMessage());
    }

    if (!isValid2FACode(type, code, userDetails.getSecret())) {
      throw new TwoFactorAuthenticationException(ErrorCode.E3023.getMessage());
    }
    // All good, 2FA code is valid!
  }
}
