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

import static org.hisp.dhis.tracker.Assertions.assertHasError;
import static org.hisp.dhis.tracker.Assertions.assertHasOnlyErrors;
import static org.hisp.dhis.tracker.Assertions.assertNoErrors;
import static org.hisp.dhis.tracker.imports.validation.Users.USER_3;
import static org.hisp.dhis.tracker.imports.validation.Users.USER_4;
import static org.hisp.dhis.tracker.imports.validation.Users.USER_5;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import org.hisp.dhis.common.IdentifiableObjectManager;
import org.hisp.dhis.common.ValueType;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.event.EventStatus;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.program.Enrollment;
import org.hisp.dhis.program.Event;
import org.hisp.dhis.program.Program;
import org.hisp.dhis.program.ProgramService;
import org.hisp.dhis.program.ProgramStage;
import org.hisp.dhis.program.ProgramStageDataElement;
import org.hisp.dhis.program.ProgramStageDataElementService;
import org.hisp.dhis.program.ProgramType;
import org.hisp.dhis.security.acl.AccessStringHelper;
import org.hisp.dhis.trackedentity.TrackedEntity;
import org.hisp.dhis.trackedentity.TrackedEntityType;
import org.hisp.dhis.trackedentity.TrackedEntityTypeService;
import org.hisp.dhis.tracker.TrackerTest;
import org.hisp.dhis.tracker.acl.TrackedEntityProgramOwnerService;
import org.hisp.dhis.tracker.acl.TrackerOwnershipManager;
import org.hisp.dhis.tracker.imports.TrackerImportParams;
import org.hisp.dhis.tracker.imports.TrackerImportService;
import org.hisp.dhis.tracker.imports.TrackerImportStrategy;
import org.hisp.dhis.tracker.imports.domain.TrackerObjects;
import org.hisp.dhis.tracker.imports.report.ImportReport;
import org.hisp.dhis.user.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Morten Svanæs <msvanaes@dhis2.org>
 */
class EventSecurityImportValidationTest extends TrackerTest {

  @Autowired private TrackerImportService trackerImportService;

  @Autowired private TrackedEntityProgramOwnerService trackedEntityProgramOwnerService;

  @Autowired private ProgramService programService;

  @Autowired private IdentifiableObjectManager manager;

  @Autowired private ProgramStageDataElementService programStageDataElementService;

  @Autowired private TrackedEntityTypeService trackedEntityTypeService;

  @Autowired private OrganisationUnitService organisationUnitService;

  @Autowired private TrackerOwnershipManager trackerOwnershipAccessManager;

  private TrackedEntity maleA;

  private TrackedEntity maleB;

  private TrackedEntity femaleA;

  private TrackedEntity femaleB;

  private OrganisationUnit organisationUnitA;

  private OrganisationUnit organisationUnitB;

  private Program programA;

  private DataElement dataElementA;

  private DataElement dataElementB;

  private ProgramStage programStageA;

  private ProgramStage programStageB;

  private TrackedEntityType trackedEntityType;

  private User importUser;

  @BeforeAll
  void setUp() throws IOException {
    setUpMetadata("tracker/tracker_basic_metadata.json");

    importUser = userService.getUser("tTgjgobT1oS");
    injectSecurityContextUser(importUser);

    TrackerImportParams params = TrackerImportParams.builder().build();
    assertNoErrors(
        trackerImportService.importTracker(
            params, fromJson("tracker/validations/enrollments_te_te-data.json")));
    assertNoErrors(
        trackerImportService.importTracker(
            params, fromJson("tracker/validations/enrollments_te_enrollments-data.json")));
    manager.flush();

    organisationUnitA = createOrganisationUnit('A');
    organisationUnitB = createOrganisationUnit('B');
    manager.save(organisationUnitA);
    manager.save(organisationUnitB);
    organisationUnitA.setPublicAccess(AccessStringHelper.FULL);
    manager.update(organisationUnitA);
    importUser.addOrganisationUnit(organisationUnitA);
    manager.update(importUser);
    dataElementA = createDataElement('A');
    dataElementB = createDataElement('B');
    dataElementA.setValueType(ValueType.INTEGER);
    dataElementB.setValueType(ValueType.INTEGER);
    manager.save(dataElementA);
    manager.save(dataElementB);
    programStageA = createProgramStage('A', 0);
    programStageB = createProgramStage('B', 0);
    programStageB.setRepeatable(true);
    manager.save(programStageA);
    manager.save(programStageB);
    programA = createProgram('A', new HashSet<>(), organisationUnitA);
    programA.setProgramType(ProgramType.WITH_REGISTRATION);
    trackedEntityType = createTrackedEntityType('A');
    trackedEntityTypeService.addTrackedEntityType(trackedEntityType);
    TrackedEntityType trackedEntityTypeFromProgram = createTrackedEntityType('C');
    trackedEntityTypeService.addTrackedEntityType(trackedEntityTypeFromProgram);
    manager.save(programA);
    ProgramStageDataElement programStageDataElement = new ProgramStageDataElement();
    programStageDataElement.setDataElement(dataElementA);
    programStageDataElement.setProgramStage(programStageA);
    programStageDataElementService.addProgramStageDataElement(programStageDataElement);
    programStageA.getProgramStageDataElements().add(programStageDataElement);
    programStageA.setProgram(programA);
    programStageDataElement = new ProgramStageDataElement();
    programStageDataElement.setDataElement(dataElementB);
    programStageDataElement.setProgramStage(programStageB);
    programStageDataElementService.addProgramStageDataElement(programStageDataElement);
    programStageB.getProgramStageDataElements().add(programStageDataElement);
    programStageB.setProgram(programA);
    programStageB.setMinDaysFromStart(2);
    programA.getProgramStages().add(programStageA);
    programA.getProgramStages().add(programStageB);
    programA.setTrackedEntityType(trackedEntityType);
    trackedEntityType.setPublicAccess(AccessStringHelper.DATA_READ_WRITE);
    manager.update(programStageA);
    manager.update(programStageB);
    manager.update(programA);
    maleA = createTrackedEntity('A', organisationUnitA);
    maleB = createTrackedEntity(organisationUnitB);
    femaleA = createTrackedEntity(organisationUnitA);
    femaleB = createTrackedEntity(organisationUnitB);
    maleA.setTrackedEntityType(trackedEntityType);
    maleB.setTrackedEntityType(trackedEntityType);
    femaleA.setTrackedEntityType(trackedEntityType);
    femaleB.setTrackedEntityType(trackedEntityType);
    manager.save(maleA);
    manager.save(maleB);
    manager.save(femaleA);
    manager.save(femaleB);

    int testYear = Calendar.getInstance().get(Calendar.YEAR) - 1;
    Date dateMar20 = getDate(testYear, 3, 20);
    Date dateApr10 = getDate(testYear, 4, 10);

    Enrollment enrollmentA = createEnrollment(programA, maleA, organisationUnitA);
    enrollmentA.setEnrollmentDate(dateMar20);
    enrollmentA.setOccurredDate(dateApr10);
    enrollmentA.setUid("MNWZ6hnuhSX");
    manager.save(enrollmentA);
    maleA.getEnrollments().add(enrollmentA);
    manager.update(maleA);
    trackerOwnershipAccessManager.assignOwnership(maleA, programA, organisationUnitA, false, false);

    trackedEntityProgramOwnerService.updateTrackedEntityProgramOwner(
        maleA, programA, organisationUnitA);
    manager.update(programA);
    OrganisationUnit qfUVllTs6cS = organisationUnitService.getOrganisationUnit("QfUVllTs6cS");
    importUser.addOrganisationUnit(organisationUnitA);
    Program p = programService.getProgram("prabcdefghA");
    p.addOrganisationUnit(qfUVllTs6cS);
    programService.updateProgram(p);
    manager.update(importUser);
  }

  @BeforeEach
  void setUpUser() {
    injectSecurityContextUser(importUser);
  }

  @Test
  void testNoWriteAccessToProgramStage() throws IOException {
    TrackerObjects trackerObjects =
        fromJson("tracker/validations/events_error-no-programStage-access.json");
    TrackerImportParams params = new TrackerImportParams();
    User user = userService.getUser(USER_3);
    user.addOrganisationUnit(organisationUnitA);
    manager.update(user);
    injectSecurityContextUser(user);

    ImportReport importReport = trackerImportService.importTracker(params, trackerObjects);

    assertHasOnlyErrors(importReport, ValidationCode.E1095, ValidationCode.E1096);
  }

  @Test
  void testNoUncompleteEventAuth() throws IOException {
    TrackerObjects trackerObjects = fromJson("tracker/validations/events_error-no-uncomplete.json");
    TrackerImportParams params = TrackerImportParams.builder().build();
    params.setImportStrategy(TrackerImportStrategy.CREATE);

    ImportReport importReport = trackerImportService.importTracker(params, trackerObjects);

    assertNoErrors(importReport);

    // Change just inserted Event to status COMPLETED...
    Event zwwuwNp6gVd = manager.get(Event.class, "ZwwuwNp6gVd");
    zwwuwNp6gVd.setStatus(EventStatus.COMPLETED);
    manager.update(zwwuwNp6gVd);
    programA.setPublicAccess(AccessStringHelper.FULL);
    manager.update(programA);
    programStageA.setPublicAccess(AccessStringHelper.FULL);
    manager.update(programStageA);
    maleA.setPublicAccess(AccessStringHelper.FULL);
    manager.update(maleA);
    User user = userService.getUser(USER_4);
    user.addOrganisationUnit(organisationUnitA);
    manager.update(user);
    injectSecurityContextUser(user);
    manager.flush();
    manager.clear();
    params.setImportStrategy(TrackerImportStrategy.UPDATE);
    importReport = trackerImportService.importTracker(params, trackerObjects);
    assertHasOnlyErrors(importReport, ValidationCode.E1083);
  }

  @Test
  void shouldSucceedWhenCreatingScheduledEventFromInsideSearchOrgUnit() throws IOException {
    TrackerObjects trackerObjects =
        fromJson("tracker/validations/events-scheduled-with-registration.json");
    TrackerImportParams params = TrackerImportParams.builder().build();
    params.setImportStrategy(TrackerImportStrategy.CREATE);
    OrganisationUnit orgUnit = organisationUnitService.getOrganisationUnit("QfUVllTs6cS");
    User user = userService.getUser(USER_5);
    user.setTeiSearchOrganisationUnits(Set.of(orgUnit));
    manager.update(user);
    injectSecurityContextUser(user);
    ImportReport importReport = trackerImportService.importTracker(params, trackerObjects);

    assertNoErrors(importReport);
  }

  @Test
  void shouldFailWhenCreatingScheduledEventFromOutsideSearchOrgUnit() throws IOException {
    TrackerObjects trackerObjects =
        fromJson("tracker/validations/events-scheduled-with-registration.json");
    TrackerImportParams params = TrackerImportParams.builder().build();
    params.setImportStrategy(TrackerImportStrategy.CREATE);
    injectSecurityContextUser(userService.getUser(USER_5));
    ImportReport importReport = trackerImportService.importTracker(params, trackerObjects);

    assertHasError(importReport, ValidationCode.E1000);
  }
}
