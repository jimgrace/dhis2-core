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
package org.hisp.dhis.tracker.imports.validation;

/**
 * @author Morten Olav Hansen <mortenoh@gmail.com>
 * @author Morten Svanæs <msvanaes@dhis2.org>
 */
public enum ValidationCode {
  /* General */
  E1000("User: `{0}`, has no write access to OrganisationUnit: `{1}`."),
  E1001("User: `{0}`, has no data write access to TrackedEntityType: `{1}`."),
  E1002("TrackedEntity: `{0}`, already exists."),
  E1003("User: `{0}`, has no write access to TrackedEntity: `{1}`."),
  E1005("Could not find TrackedEntityType: `{0}`."),
  E1006("Attribute: `{0}`, does not exist."),
  E1007("Error validating attribute value type: `{0}`; Error: `{1}`."),
  E1008("Program stage `{0}` has no reference to a program. Check the program stage configuration"),
  E1009("File resource: `{0}`, has already been assigned to a different object."),
  E1010("Could not find Program: `{0}`, linked to Event."),
  E1011("Could not find OrganisationUnit: `{0}`, linked to Event."),
  E1012("Geometry does not conform to FeatureType: `{0}`."),
  E1013("Could not find ProgramStage: `{0}`, linked to Event."),
  E1014(
      "Provided Program: `{0}`, is a Program without registration. "
          + "An Enrollment cannot be created into Program without registration."),
  E1015("TrackedEntity: `{0}`, already has an active Enrollment in Program `{1}`."),
  E1016(
      "TrackedEntity: `{0}`, already has an enrollment in Program: `{1}`, and this "
          + "program only allows enrolling one time."),
  E1018("Attribute: `{0}`, is mandatory in program `{1}` but not declared in enrollment `{2}`."),
  E1019("Only Program attributes is allowed for enrollment; Non valid attribute: `{0}`."),
  E1020("Enrollment date: `{0}`, cannot be a future date."),
  E1021("Incident date: `{0}`, cannot be a future date."),
  E1022("TrackedEntity: `{0}`, must have same TrackedEntityType as Program `{1}`."),
  E1023("DisplayIncidentDate is true but property occurredAt is null."),
  E1025("Property enrolledAt is null."),
  E1029("Event OrganisationUnit: `{0}`, and Program: `{1}`, don't match."),
  E1030("Event: `{0}`, already exists."),
  E1031("Event occurredAt date is missing."),
  E1032("Event: `{0}`, do not exist."),
  E1033("Event: `{0}`, Enrollment value is null."),
  E1039("ProgramStage: `{0}`, is not repeatable and an event already exists."),
  E1041("Enrollment OrganisationUnit: `{0}`, and Program: `{1}`, don't match."),
  E1043("Event: `{0}`, completeness date has expired. Not possible to make changes to this event."),
  E1046("Event: `{0}`, needs to have at least one (event or schedule) date."),
  E1047(
      "Event: `{0}`, date belongs to an expired period. It is not possible to create such event."),
  E1049("Could not find OrganisationUnit: `{0}`, linked to Tracked Entity."),
  E1050("Event ScheduledAt date is missing."),
  E1051("Event: `{0}`, completedAt must be null when status is `{1}`"),
  E1052("Enrollment: `{0}`, completedAt must be null when status is `{1}`"),
  E1054("AttributeOptionCombo `{0}` is not in the event programs category combo `{1}`."),
  E1055("Default AttributeOptionCombo is not allowed since program has non-default CategoryCombo."),
  E1056("Event date: `{0}`, is before start date: `{1}`, for AttributeOption: `{2}`."),
  E1057(
      "Event date: `{0}`, is after end date: `{1}`, for AttributeOption: `{2}` in program: `{3}`."),
  E1063("TrackedEntity: `{0}`, does not exist."),
  E1064("Non-unique attribute value `{0}` for attribute `{1}`"),
  E1068("Could not find TrackedEntity: `{0}`, linked to Enrollment."),
  E1069("Could not find Program: `{0}`, linked to Enrollment."),
  E1070("Could not find OrganisationUnit: `{0}`, linked to Enrollment."),
  E1074("FeatureType is missing."),
  E1075("Attribute: `{0}`, is missing uid."),
  E1076("`{0}` `{1}` is mandatory and cannot be null"),
  E1077("Attribute: `{0}`, text value exceed the maximum allowed length: `{0}`."),
  E1079("Event: `{0}`, program: `{1}` is different from program defined in enrollment `{2}`."),
  E1080("Enrollment: `{0}`, already exists."),
  E1081("Enrollment: `{0}`, do not exist."),
  E1082("Event: `{0}`, is already deleted and cannot be modified."),
  E1083("User: `{0}`, is not authorized to modify completed events."),
  E1089("Event: `{0}`, references a Program Stage `{1}` that does not belong to Program `{2}`."),
  E1090(
      "Attribute: `{0}`, is mandatory in tracked entity type `{1}` but not declared in tracked entity `{2}`."),
  E1091("User: `{0}`, has no data write access to Program: `{1}`."),
  E1095("User: `{0}`, has no data write access to ProgramStage: `{1}`."),
  E1096("User: `{0}`, has no data read access to Program: `{1}`."),
  E1099("User: `{0}`, has no write access to CategoryOption: `{1}`."),
  E1100("User: `{0}`, is lacking 'F_TEI_CASCADE_DELETE' authority to delete TrackedEntity: `{1}`."),
  E1102(
      "User: `{0}`, does not have access to the tracked entity: `{1}`, Program: `{2}`, combination."),
  E1103(
      "User: `{0}`, is lacking 'F_ENROLLMENT_CASCADE_DELETE' authority to delete Enrollment : `{1}`."),
  E1104("User: `{0}`, has no data read access to program: `{1}`, TrackedEntityType: `{2}`."),
  E1112(
      "Attribute value: `{0}`, is set to confidential but systems encryption status is not ok. Reason: `{1}`"),
  E1113("Enrollment: `{0}`, is already deleted and cannot be modified."),
  E1114("TrackedEntity: `{0}`, is already deleted and cannot be modified."),
  E1115("Could not find CategoryOptionCombo: `{0}`."),
  E1116("Could not find CategoryOption: `{0}`."),
  E1117("CategoryOptionCombo does not exist for category combo `{0}` and category options `{1}`."),
  E1118("Assigned user `{0}` is not valid."),
  E1119("A Tracker Note with uid `{0}` already exists."),
  E1120("ProgramStage `{0}` does not allow user assignment"),
  E1121("Missing required trackedEntity property: `{0}`."),
  E1122("Missing required enrollment property: `{0}`."),
  E1123("Missing required event property: `{0}`."),
  E1124("Missing required relationship property: `{0}`."),
  E1125("Value `{0}` is not a valid option code in option set `{1}`"),
  E1126("Not allowed to update Tracked Entity property: {0}."),
  E1127("Not allowed to update Enrollment property: {0}."),
  E1128("Not allowed to update Event property: {0}."),
  E1300("Generated by program rule (`{0}`) - `{1}`"),
  E1301("Generated by program rule (`{0}`) - Mandatory DataElement `{1}` is not present"),
  E1302("DataElement `{0}` is not valid: `{1}`"),
  E1303("Mandatory DataElement `{0}` is not present"),
  E1304("DataElement `{0}` is not a valid data element"),
  E1305("DataElement `{0}` is not part of `{1}` program stage"),
  E1306("Generated by program rule (`{0}`) - Mandatory Attribute `{1}` is not present"),
  E1307(
      "Generated by program rule (`{0}`) - Unable to assign value to data element `{1}`. "
          + "The provided value must be empty or match the calculated value `{2}`"),
  E1308("Generated by program rule (`{0}`) - DataElement `{1}` is being replaced in event `{2}`"),
  E1309(
      "Generated by program rule (`{0}`) - Unable to assign value to attribute `{1}`. "
          + "The provided value must be empty or match the calculated value `{2}`"),
  E1310("Generated by program rule (`{0}`) - Attribute `{1}` is being replaced in te `{2}`"),
  E1313(
      "Event {0} of an enrollment does not point to an existing tracked entity. The data in your system might be corrupted"),
  E1314("Generated by program rule (`{0}`) - DataElement `{1}` is mandatory and cannot be deleted"),
  E1315(
      "Status `{0}` does not allow defining data values. Statuses that do allow defining data values are: {1}"),
  E1316("No event can transition from status `{0}` to status `{1}`."),
  E1317("Generated by program rule (`{0}`) - Attribute `{1}` is mandatory and cannot be deleted"),

  /* Relationship */
  E4000("Relationship: `{0}` cannot link to itself"),
  E4001(
      "Relationship item `{0}` for relationship `{1}` is invalid: an item must link exactly one of trackedEntity, enrollment, event."),
  E4006("Could not find relationship type: `{0}`."),
  E4010("Relationship type `{0}` constraint requires a {1} but a {2} was found."),
  E4012("Could not find `{0}`: `{1}`, linked to relationship."),
  E4014(
      "Relationship type `{0}` constraint requires a tracked entity having type `{1}` but `{2}` was found."),
  E4015("Relationship: `{0}`, already exists."),
  E4016("Relationship: `{0}`, do not exist."),
  E4017("Relationship: `{0}`, is already deleted and cannot be modified."),
  E4018("Relationship: `{0}`, linking {1}: `{2}` to {3}: `{4}` already exists."),
  E4020("User: `{0}`, has no write access to relationship: `{1}`."),
  E5000(
      "\"{0}\" `{1}` cannot be persisted because \"{2}\" `{3}` referenced by it cannot be persisted."),
  E9999("N/A");

  private final String message;

  ValidationCode(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }
}
