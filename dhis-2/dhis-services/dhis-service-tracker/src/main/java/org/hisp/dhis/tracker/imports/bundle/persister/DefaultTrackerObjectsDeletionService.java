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
package org.hisp.dhis.tracker.imports.bundle.persister;

import static org.hisp.dhis.changelog.ChangeLogType.READ;
import static org.hisp.dhis.user.CurrentUserUtil.getCurrentUsername;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.hisp.dhis.common.BaseIdentifiableObject;
import org.hisp.dhis.common.IdentifiableObjectManager;
import org.hisp.dhis.feedback.NotFoundException;
import org.hisp.dhis.program.Enrollment;
import org.hisp.dhis.program.Event;
import org.hisp.dhis.program.UserInfoSnapshot;
import org.hisp.dhis.program.notification.ProgramNotificationInstance;
import org.hisp.dhis.program.notification.ProgramNotificationInstanceParam;
import org.hisp.dhis.program.notification.ProgramNotificationInstanceService;
import org.hisp.dhis.relationship.Relationship;
import org.hisp.dhis.trackedentity.TrackedEntity;
import org.hisp.dhis.trackedentity.TrackedEntityChangeLogService;
import org.hisp.dhis.trackedentityattributevalue.TrackedEntityAttributeValue;
import org.hisp.dhis.trackedentityattributevalue.TrackedEntityAttributeValueChangeLogService;
import org.hisp.dhis.trackedentitydatavalue.TrackedEntityDataValueChangeLogService;
import org.hisp.dhis.tracker.TrackerType;
import org.hisp.dhis.tracker.export.relationship.RelationshipQueryParams;
import org.hisp.dhis.tracker.export.relationship.RelationshipStore;
import org.hisp.dhis.tracker.imports.report.Entity;
import org.hisp.dhis.tracker.imports.report.TrackerTypeReport;
import org.hisp.dhis.tracker.trackedentityattributevalue.TrackedEntityAttributeValueService;
import org.hisp.dhis.user.CurrentUserUtil;
import org.springframework.stereotype.Service;

/**
 * @author Zubair Asghar
 */
@Service
@RequiredArgsConstructor
public class DefaultTrackerObjectsDeletionService implements TrackerObjectDeletionService {
  private final IdentifiableObjectManager manager;

  private final TrackedEntityChangeLogService trackedEntityChangeLogService;

  private final RelationshipStore relationshipStore;

  private final TrackedEntityAttributeValueService attributeValueService;

  private final TrackedEntityDataValueChangeLogService dataValueChangeLogService;

  private final TrackedEntityAttributeValueChangeLogService attributeValueAuditService;

  private final ProgramNotificationInstanceService programNotificationInstanceService;

  @Override
  public TrackerTypeReport deleteEnrollments(List<String> enrollments) throws NotFoundException {
    UserInfoSnapshot userInfoSnapshot =
        UserInfoSnapshot.from(CurrentUserUtil.getCurrentUserDetails());
    TrackerTypeReport typeReport = new TrackerTypeReport(TrackerType.ENROLLMENT);

    for (String uid : enrollments) {
      Entity objectReport = new Entity(TrackerType.ENROLLMENT, uid);

      Enrollment enrollment = manager.get(Enrollment.class, uid);
      if (enrollment == null) {
        throw new NotFoundException(Enrollment.class, uid);
      }
      enrollment.setLastUpdatedByUserInfo(userInfoSnapshot);

      List<String> events =
          enrollment.getEvents().stream()
              .filter(event -> !event.isDeleted())
              .map(BaseIdentifiableObject::getUid)
              .toList();
      deleteEvents(events);

      RelationshipQueryParams params = RelationshipQueryParams.builder().entity(enrollment).build();
      List<String> relationships =
          relationshipStore.getByEnrollment(enrollment, params).stream()
              .map(BaseIdentifiableObject::getUid)
              .toList();

      deleteRelationships(relationships);

      List<ProgramNotificationInstance> notificationInstances =
          programNotificationInstanceService.getProgramNotificationInstances(
              ProgramNotificationInstanceParam.builder().enrollment(enrollment).build());

      notificationInstances.forEach(programNotificationInstanceService::delete);

      TrackedEntity te = enrollment.getTrackedEntity();
      te.setLastUpdatedByUserInfo(userInfoSnapshot);

      manager.delete(enrollment);

      manager.update(te);

      typeReport.getStats().incDeleted();
      typeReport.addEntity(objectReport);
    }

    return typeReport;
  }

  @Override
  public TrackerTypeReport deleteEvents(List<String> events) throws NotFoundException {
    UserInfoSnapshot userInfoSnapshot =
        UserInfoSnapshot.from(CurrentUserUtil.getCurrentUserDetails());
    TrackerTypeReport typeReport = new TrackerTypeReport(TrackerType.EVENT);
    for (String uid : events) {
      Entity objectReport = new Entity(TrackerType.EVENT, uid);

      Event event = manager.get(Event.class, uid);
      event.setLastUpdatedByUserInfo(userInfoSnapshot);

      RelationshipQueryParams params = RelationshipQueryParams.builder().entity(event).build();
      List<String> relationships =
          relationshipStore.getByEvent(event, params).stream()
              .map(BaseIdentifiableObject::getUid)
              .toList();

      deleteRelationships(relationships);

      // This is needed until deprecated method
      // TrackedEntityDataValueChangeLogService.getTrackedEntityDataValueChangeLogs is removed.
      dataValueChangeLogService.deleteTrackedEntityDataValueChangeLog(event);

      List<ProgramNotificationInstance> notificationInstances =
          programNotificationInstanceService.getProgramNotificationInstances(
              ProgramNotificationInstanceParam.builder().event(event).build());

      notificationInstances.forEach(programNotificationInstanceService::delete);

      manager.delete(event);

      if (event.getProgramStage().getProgram().isRegistration()) {
        TrackedEntity entity = event.getEnrollment().getTrackedEntity();
        entity.setLastUpdatedByUserInfo(userInfoSnapshot);

        manager.update(entity);

        Enrollment enrollment = event.getEnrollment();
        enrollment.setLastUpdatedByUserInfo(userInfoSnapshot);

        manager.update(enrollment);
      }

      typeReport.getStats().incDeleted();
      typeReport.addEntity(objectReport);
    }

    return typeReport;
  }

  @Override
  public TrackerTypeReport deleteTrackedEntities(List<String> trackedEntities)
      throws NotFoundException {
    UserInfoSnapshot userInfoSnapshot =
        UserInfoSnapshot.from(CurrentUserUtil.getCurrentUserDetails());
    TrackerTypeReport typeReport = new TrackerTypeReport(TrackerType.TRACKED_ENTITY);

    for (String uid : trackedEntities) {
      Entity objectReport = new Entity(TrackerType.TRACKED_ENTITY, uid);

      TrackedEntity entity = manager.get(TrackedEntity.class, uid);
      if (entity == null) {
        throw new NotFoundException(TrackedEntity.class, uid);
      }
      trackedEntityChangeLogService.addTrackedEntityChangeLog(entity, getCurrentUsername(), READ);

      entity.setLastUpdatedByUserInfo(userInfoSnapshot);

      Set<Enrollment> daoEnrollments = entity.getEnrollments();

      List<String> enrollments =
          daoEnrollments.stream()
              .filter(enrollment -> !enrollment.isDeleted())
              .map(BaseIdentifiableObject::getUid)
              .toList();
      deleteEnrollments(enrollments);

      RelationshipQueryParams params = RelationshipQueryParams.builder().entity(entity).build();
      List<String> relationships =
          relationshipStore.getByTrackedEntity(entity, params).stream()
              .map(BaseIdentifiableObject::getUid)
              .toList();

      deleteRelationships(relationships);

      Collection<TrackedEntityAttributeValue> attributeValues =
          attributeValueService.getTrackedEntityAttributeValues(entity);

      for (TrackedEntityAttributeValue attributeValue : attributeValues) {
        attributeValueService.deleteTrackedEntityAttributeValue(attributeValue);
      }

      attributeValueAuditService.deleteTrackedEntityAttributeValueChangeLogs(entity);

      manager.delete(entity);

      typeReport.getStats().incDeleted();
      typeReport.addEntity(objectReport);
    }

    return typeReport;
  }

  @Override
  public TrackerTypeReport deleteRelationships(List<String> relationships)
      throws NotFoundException {
    TrackerTypeReport typeReport = new TrackerTypeReport(TrackerType.RELATIONSHIP);

    for (String uid : relationships) {
      Entity objectReport = new Entity(TrackerType.RELATIONSHIP, uid);

      Relationship relationship = manager.get(Relationship.class, uid);
      if (relationship == null) {
        throw new NotFoundException(Relationship.class, uid);
      }
      manager.delete(relationship);

      typeReport.getStats().incDeleted();
      typeReport.addEntity(objectReport);
    }

    return typeReport;
  }
}
