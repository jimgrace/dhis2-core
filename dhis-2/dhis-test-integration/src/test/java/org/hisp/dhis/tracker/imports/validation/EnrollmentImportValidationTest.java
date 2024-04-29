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
package org.hisp.dhis.tracker.imports.validation;

import static org.hisp.dhis.tracker.Assertions.assertHasErrors;
import static org.hisp.dhis.tracker.Assertions.assertHasOnlyErrors;
import static org.hisp.dhis.tracker.Assertions.assertNoErrors;
import static org.hisp.dhis.tracker.imports.validation.Users.USER_3;
import static org.hisp.dhis.tracker.imports.validation.Users.USER_4;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import org.hisp.dhis.program.EnrollmentService;
import org.hisp.dhis.tracker.TrackerTest;
import org.hisp.dhis.tracker.TrackerType;
import org.hisp.dhis.tracker.imports.TrackerIdSchemeParams;
import org.hisp.dhis.tracker.imports.TrackerImportParams;
import org.hisp.dhis.tracker.imports.TrackerImportService;
import org.hisp.dhis.tracker.imports.TrackerImportStrategy;
import org.hisp.dhis.tracker.imports.domain.TrackerObjects;
import org.hisp.dhis.tracker.imports.preheat.TrackerPreheat;
import org.hisp.dhis.tracker.imports.preheat.TrackerPreheatService;
import org.hisp.dhis.tracker.imports.report.ImportReport;
import org.hisp.dhis.tracker.imports.report.ValidationReport;
import org.hisp.dhis.user.User;
import org.hisp.dhis.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Morten Svanæs <msvanaes@dhis2.org>
 */
class EnrollmentImportValidationTest extends TrackerTest {

  @Autowired protected EnrollmentService enrollmentService;

  @Autowired private TrackerImportService trackerImportService;

  @Autowired private TrackerPreheatService trackerPreheatService;
  @Autowired protected UserService _userService;

  @Override
  protected void initTest() throws IOException {
    userService = _userService;
    setUpMetadata("tracker/tracker_basic_metadata.json");
    injectAdminUser();
    assertNoErrors(
        trackerImportService.importTracker(
            new TrackerImportParams(),
            fromJson("tracker/validations/enrollments_te_te-data.json")));
    manager.flush();
  }

  @Test
  void testEnrollmentValidationOkAll() throws IOException {
    TrackerObjects trackerObjects =
        fromJson("tracker/validations/enrollments_te_enrollments-data.json");
    ImportReport importReport =
        trackerImportService.importTracker(new TrackerImportParams(), trackerObjects);
    assertNoErrors(importReport);
  }

  @Test
  void testPreheatOwnershipForSubsequentEnrollment() throws IOException {
    TrackerObjects trackerObjects =
        fromJson("tracker/validations/enrollments_te_enrollments-data.json");
    ImportReport importReport =
        trackerImportService.importTracker(new TrackerImportParams(), trackerObjects);
    assertNoErrors(importReport);
    TrackerObjects secondTrackerObjects =
        fromJson("tracker/validations/enrollments_te_enrollments-data.json");
    TrackerPreheat preheat =
        trackerPreheatService.preheat(
            secondTrackerObjects, new TrackerIdSchemeParams(), userService.getUser(ADMIN_USER_UID));
    secondTrackerObjects
        .getEnrollments()
        .forEach(
            e -> {
              assertTrue(
                  e.getOrgUnit()
                      .isEqualTo(
                          preheat
                              .getProgramOwner()
                              .get(e.getTrackedEntity())
                              .get(e.getProgram().getIdentifier())
                              .getOrganisationUnit()));
            });
  }

  @Test
  void testDisplayIncidentDateTrueButDateValueIsInvalid() {
    assertThrows(
        IOException.class,
        () -> fromJson("tracker/validations/enrollments_error-displayIncident.json"));
  }

  @Test
  void testNoWriteAccessToOrg() throws IOException {
    TrackerObjects trackerObjects =
        fromJson("tracker/validations/enrollments_te_enrollments-data.json");
    User user = userService.getUser(USER_3);
    TrackerImportParams params = new TrackerImportParams();
    params.setUserId(user.getUid());

    ImportReport importReport = trackerImportService.importTracker(params, trackerObjects);

    assertHasErrors(importReport, 4, ValidationCode.E1040);
  }

  @Test
  void testOnlyProgramAttributesAllowedOnEnrollments() throws IOException {
    TrackerObjects trackerObjects =
        fromJson("tracker/validations/enrollments_error_non_program_attr.json");

    ImportReport importReport =
        trackerImportService.importTracker(new TrackerImportParams(), trackerObjects);

    assertHasErrors(importReport, 3, ValidationCode.E1019);
  }

  @Test
  void testAttributesOk() throws IOException {
    TrackerObjects trackerObjects = fromJson("tracker/validations/enrollments_te_attr-data.json");

    ImportReport importReport =
        trackerImportService.importTracker(new TrackerImportParams(), trackerObjects);

    assertNoErrors(importReport);
    assertEquals(
        1,
        importReport
            .getPersistenceReport()
            .getTypeReportMap()
            .get(TrackerType.ENROLLMENT)
            .getEntityReport()
            .size());
  }

  @Test
  void testDeleteCascadeEnrollments() throws IOException {

    TrackerObjects trackerObjects = fromJson("tracker/validations/enrollments_te_attr-data.json");
    TrackerImportParams params = new TrackerImportParams();
    ImportReport importReport = trackerImportService.importTracker(params, trackerObjects);

    assertNoErrors(importReport);
    manager.flush();
    importEvents();
    manager.flush();
    trackerObjects = fromJson("tracker/validations/enrollments_te_attr-data.json");
    User user2 = userService.getUser(USER_4);
    params.setUserId(user2.getUid());
    params.setImportStrategy(TrackerImportStrategy.DELETE);

    ImportReport trackerImportDeleteReport =
        trackerImportService.importTracker(params, trackerObjects);

    assertHasOnlyErrors(trackerImportDeleteReport, ValidationCode.E1103, ValidationCode.E1040);
  }

  protected void importEvents() throws IOException {
    TrackerObjects trackerObjects = fromJson("tracker/validations/events-with-registration.json");
    TrackerImportParams params = new TrackerImportParams();
    params.setImportStrategy(TrackerImportStrategy.CREATE);

    ImportReport importReport = trackerImportService.importTracker(params, trackerObjects);

    assertNoErrors(importReport);
  }

  @Test
  void testActiveEnrollmentAlreadyExists() throws IOException {
    TrackerObjects trackerObjects =
        fromJson("tracker/validations/enrollments_double-tei-enrollment_part1.json");

    ImportReport importReport =
        trackerImportService.importTracker(new TrackerImportParams(), trackerObjects);

    assertNoErrors(importReport);

    TrackerObjects trackerObjects2 =
        fromJson("tracker/validations/enrollments_double-tei-enrollment_part2.json");

    importReport = trackerImportService.importTracker(new TrackerImportParams(), trackerObjects2);

    ValidationReport validationResult = importReport.getValidationReport();

    assertHasOnlyErrors(validationResult, ValidationCode.E1015);
  }

  @Test
  void testEnrollmentDeleteOk() throws IOException {
    TrackerObjects trackerObjects =
        fromJson("tracker/validations/enrollments_te_enrollments-data.json");
    TrackerImportParams params = new TrackerImportParams();
    ImportReport importReport = trackerImportService.importTracker(params, trackerObjects);
    assertNoErrors(importReport);

    manager.flush();
    manager.clear();

    TrackerObjects deleteTrackerObjects =
        fromJson("tracker/validations/enrollments_te_enrollments-data-delete.json");
    params.setImportStrategy(TrackerImportStrategy.DELETE);

    ImportReport importReportDelete =
        trackerImportService.importTracker(params, deleteTrackerObjects);

    assertNoErrors(importReportDelete);
    assertEquals(1, importReportDelete.getStats().getDeleted());
  }

  /** Notes with no value are ignored */
  @Test
  void testBadEnrollmentNoteNoValue() throws IOException {
    TrackerObjects trackerObjects =
        fromJson("tracker/validations/enrollments_bad-note-no-value.json");

    ImportReport importReport =
        trackerImportService.importTracker(new TrackerImportParams(), trackerObjects);

    assertNoErrors(importReport);
  }
}
