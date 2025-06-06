/*
 * Copyright (c) 2004-2022, University of Oslo
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
package org.hisp.dhis.webapi.controller.organisationunit;

import static org.hisp.dhis.dxf2.webmessage.WebMessageUtils.validateAndThrowErrors;
import static org.hisp.dhis.security.Authorities.F_ORG_UNIT_PROFILE_ADD;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import lombok.AllArgsConstructor;
import org.hisp.dhis.common.OpenApi;
import org.hisp.dhis.feedback.BadRequestException;
import org.hisp.dhis.feedback.ForbiddenException;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.orgunitprofile.OrgUnitProfile;
import org.hisp.dhis.orgunitprofile.OrgUnitProfileData;
import org.hisp.dhis.orgunitprofile.OrgUnitProfileService;
import org.hisp.dhis.security.RequiresAuthority;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@OpenApi.Document(
    entity = OrganisationUnit.class,
    classifiers = {"team:platform", "purpose:metadata"})
@RestController
@AllArgsConstructor
@RequestMapping("/api/organisationUnitProfile")
public class OrganisationUnitProfileController {
  private final OrgUnitProfileService orgUnitProfileService;

  @RequiresAuthority(anyOf = F_ORG_UNIT_PROFILE_ADD)
  @PostMapping(consumes = APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public void saveProfile(@RequestBody OrgUnitProfile profile)
      throws BadRequestException, ForbiddenException {
    validateAndThrowErrors(() -> orgUnitProfileService.validateOrgUnitProfile(profile));

    orgUnitProfileService.saveOrgUnitProfile(profile);
  }

  @GetMapping(produces = APPLICATION_JSON_VALUE)
  public OrgUnitProfile getProfile() throws ForbiddenException {
    return orgUnitProfileService.getOrgUnitProfile();
  }

  @GetMapping(value = "/{uid}/data", produces = APPLICATION_JSON_VALUE)
  public OrgUnitProfileData getProfileData(
      @PathVariable String uid, @RequestParam(required = false) String period)
      throws ForbiddenException {
    return orgUnitProfileService.getOrgUnitProfileData(uid, period);
  }
}
