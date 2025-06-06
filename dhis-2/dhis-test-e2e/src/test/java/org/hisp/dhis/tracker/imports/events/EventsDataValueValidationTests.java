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
package org.hisp.dhis.tracker.imports.events;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.google.gson.JsonObject;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.hisp.dhis.test.e2e.Constants;
import org.hisp.dhis.test.e2e.actions.metadata.DataElementActions;
import org.hisp.dhis.test.e2e.actions.metadata.ProgramActions;
import org.hisp.dhis.test.e2e.actions.metadata.SharingActions;
import org.hisp.dhis.test.e2e.dto.ApiResponse;
import org.hisp.dhis.test.e2e.dto.TrackerApiResponse;
import org.hisp.dhis.test.e2e.helpers.JsonObjectBuilder;
import org.hisp.dhis.test.e2e.helpers.QueryParamsBuilder;
import org.hisp.dhis.tracker.TrackerApiTest;
import org.hisp.dhis.tracker.imports.databuilder.EventDataBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * @author Gintare Vilkelyte <vilkelyte.gintare@gmail.com>
 */
public class EventsDataValueValidationTests extends TrackerApiTest {
  private static final String OU_ID = Constants.ORG_UNIT_IDS[0];

  private ProgramActions programActions;

  private SharingActions sharingActions;

  private DataElementActions dataElementActions;

  private String programId;

  private String programStageId;

  private String mandatoryDataElementId;

  private String notMandatoryDataElementId;

  @BeforeAll
  public void beforeAll() {
    programActions = new ProgramActions();
    sharingActions = new SharingActions();
    dataElementActions = new DataElementActions();

    loginActions.loginAsSuperUser();

    setupData();
  }

  @ParameterizedTest
  @CsvSource({"ON_COMPLETE,ACTIVE"})
  public void shouldNotValidateWhenDataValueExists(String validationStrategy, String eventStatus) {
    programActions.programStageActions.setValidationStrategy(programStageId, validationStrategy);

    JsonObject events = createEventBodyWithStatus(eventStatus);

    TrackerApiResponse response = trackerImportExportActions.postAndGetJobReport(events);

    response
        .validateSuccessfulImport()
        .validateEvents()
        .body("stats.created", Matchers.equalTo(1))
        .body("objectReports", notNullValue())
        .body("objectReports[0].errorReports", empty());
  }

  @ParameterizedTest
  @CsvSource({
    "ON_COMPLETE,COMPLETED",
    "ON_UPDATE_AND_INSERT,ACTIVE",
    "ON_UPDATE_AND_INSERT,COMPLETED"
  })
  public void shouldValidateWhenNoDataValue(String validationStrategy, String eventStatus) {
    programActions.programStageActions.setValidationStrategy(programStageId, validationStrategy);

    JsonObject event = createEventBodyWithStatus(eventStatus);

    TrackerApiResponse response = trackerImportExportActions.postAndGetJobReport(event);

    response
        .validate()
        .body("status", equalTo("ERROR"))
        .body("bundleReport.typeReportMap.EVENT", nullValue());

    response.validateErrorReport().body("errorCode", hasItem("E1303"));
  }

  @ParameterizedTest
  @CsvSource({"ON_COMPLETE,ACTIVE"})
  public void shouldRemoveMandatoryDataValue(String validationStrategy, String eventStatus) {
    programActions.programStageActions.setValidationStrategy(programStageId, validationStrategy);

    JsonObject event = createEventBodyWithStatus(eventStatus);

    addDataValue(
        event.getAsJsonArray("events").get(0).getAsJsonObject(),
        mandatoryDataElementId,
        "TEXT VALUE");

    String eventId =
        trackerImportExportActions
            .postAndGetJobReport(event)
            .validateSuccessfulImport()
            .extractImportedEvents()
            .get(0);

    event = trackerImportExportActions.get("/events/" + eventId).validateStatus(200).getBody();

    event =
        JsonObjectBuilder.jsonObject(event)
            .addPropertyByJsonPath("dataValues[0].value", null)
            .wrapIntoArray("events");

    trackerImportExportActions.postAndGetJobReport(event).validateSuccessfulImport();
  }

  @ParameterizedTest
  @CsvSource({"ON_UPDATE_AND_INSERT,ACTIVE"})
  public void shouldNotRemoveMandatoryDataValue(String validationStrategy, String eventStatus) {
    programActions.programStageActions.setValidationStrategy(programStageId, validationStrategy);

    JsonObject event = createEventBodyWithStatus(eventStatus);

    addDataValue(
        event.getAsJsonArray("events").get(0).getAsJsonObject(),
        mandatoryDataElementId,
        "TEXT VALUE");

    String eventId =
        trackerImportExportActions
            .postAndGetJobReport(event)
            .validateSuccessfulImport()
            .extractImportedEvents()
            .get(0);

    event = trackerImportExportActions.get("/events/" + eventId).getBody();

    event =
        JsonObjectBuilder.jsonObject(event)
            .addPropertyByJsonPath("dataValues[0].value", null)
            .wrapIntoArray("events");

    trackerImportExportActions
        .postAndGetJobReport(event)
        .validateErrorReport()
        .body("message", hasItem(CoreMatchers.containsString("DataElement")))
        .body("message", hasItem(CoreMatchers.containsString(mandatoryDataElementId)));
  }

  @Test
  public void shouldRemoveNotMandatoryDataValue() {
    JsonObject event = createEventBodyWithStatus("ACTIVE");

    addDataValue(
        event.getAsJsonArray("events").get(0).getAsJsonObject(),
        notMandatoryDataElementId,
        "TEXT VALUE");

    String eventId =
        trackerImportExportActions
            .postAndGetJobReport(event)
            .validateSuccessfulImport()
            .extractImportedEvents()
            .get(0);

    event = trackerImportExportActions.get("/events/" + eventId).getBody();

    event =
        JsonObjectBuilder.jsonObject(event)
            .addPropertyByJsonPath("dataValues[0].value", null)
            .wrapIntoArray("events");

    trackerImportExportActions.postAndGetJobReport(event).validateSuccessfulImport();
  }

  @Test
  public void shouldImportEventsWithCompulsoryDataValues() {
    JsonObject events =
        new EventDataBuilder()
            .addDataValue(mandatoryDataElementId, "TEXT value")
            .array(OU_ID, programId, programStageId);

    TrackerApiResponse response = trackerImportExportActions.postAndGetJobReport(events);

    response
        .validateSuccessfulImport()
        .validateEvents()
        .body("stats.created", Matchers.equalTo(1))
        .body("objectReports", notNullValue())
        .body("objectReports[0].errorReports", empty());

    String eventId = response.extractImportedEvents().get(0);

    trackerImportExportActions
        .get("/events/" + eventId)
        .validate()
        .statusCode(200)
        .body("dataValues", not(Matchers.emptyArray()));
  }

  private JsonObject createEventBodyWithStatus(String status) {
    EventDataBuilder builder = new EventDataBuilder().setStatus(status);

    if (status.equalsIgnoreCase("SCHEDULE")) {
      builder.setScheduledDate(Instant.now().plus(1, ChronoUnit.DAYS).toString());
    }

    return builder.array(OU_ID, programId, programStageId);
  }

  private void setupData() {
    ApiResponse response = programActions.createEventProgram(OU_ID);
    programId = response.extractUid();
    assertNotNull(programId, "Failed to create a program");

    sharingActions.setupSharingForConfiguredUserGroup("program", programId);

    programStageId =
        programActions
            .get(programId, new QueryParamsBuilder().add("fields=*"))
            .extractString("programStages.id[0]");

    assertNotNull(programStageId, "Failed to create a programStage");

    ApiResponse dataelements =
        dataElementActions.get(
            "?fields=id&filter=domainType:eq:TRACKER&filter=valueType:in:[TEXT,LONG_TEXT]&pageSize=2");
    dataelements.validate().body("dataElements", hasSize(2));

    mandatoryDataElementId = dataelements.extractString("dataElements.id[0]");
    notMandatoryDataElementId = dataelements.extractString("dataElements.id[1]");

    programActions
        .addDataElement(programStageId, mandatoryDataElementId, true)
        .validate()
        .statusCode(200);
    programActions
        .addDataElement(programStageId, notMandatoryDataElementId, false)
        .validate()
        .statusCode(200);
  }

  private void addDataValue(JsonObject body, String dataElementId, String value) {
    new JsonObjectBuilder(body)
        .addOrAppendToArray(
            "dataValues",
            new JsonObjectBuilder()
                .addProperty("dataElement", dataElementId)
                .addProperty("value", value)
                .build())
        .build();
  }
}
