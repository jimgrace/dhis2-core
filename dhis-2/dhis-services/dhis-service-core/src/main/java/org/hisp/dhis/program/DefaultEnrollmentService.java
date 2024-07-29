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
package org.hisp.dhis.program;

import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hisp.dhis.common.CodeGenerator;
import org.hisp.dhis.common.IdentifiableObjectManager;
import org.hisp.dhis.common.IllegalQueryException;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.program.notification.event.ProgramEnrollmentNotificationEvent;
import org.hisp.dhis.trackedentity.TrackedEntity;
import org.hisp.dhis.trackedentity.TrackedEntityService;
import org.hisp.dhis.trackedentity.TrackerOwnershipManager;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Abyot Asalefew
 */
@Slf4j
@RequiredArgsConstructor
@Service("org.hisp.dhis.program.EnrollmentService")
public class DefaultEnrollmentService implements EnrollmentService {
  private final TrackedEntityService trackedEntityService;

  private final ApplicationEventPublisher eventPublisher;

  private final TrackerOwnershipManager trackerOwnershipAccessManager;

  private final IdentifiableObjectManager manager;

  private Enrollment prepareEnrollment(
      TrackedEntity trackedEntity,
      Program program,
      Date enrollmentDate,
      Date occurredDate,
      OrganisationUnit organisationUnit,
      String uid) {
    if (program.getTrackedEntityType() != null
        && !program.getTrackedEntityType().equals(trackedEntity.getTrackedEntityType())) {
      throw new IllegalQueryException(
          "Tracked entity must have same tracked entity as program: " + program.getUid());
    }

    Enrollment enrollment = new Enrollment();
    enrollment.setUid(CodeGenerator.isValidUid(uid) ? uid : CodeGenerator.generateUid());
    enrollment.setOrganisationUnit(organisationUnit);
    enrollment.enrollTrackedEntity(trackedEntity, program);

    if (enrollmentDate != null) {
      enrollment.setEnrollmentDate(enrollmentDate);
    } else {
      enrollment.setEnrollmentDate(new Date());
    }

    if (occurredDate != null) {
      enrollment.setOccurredDate(occurredDate);
    } else {
      enrollment.setOccurredDate(new Date());
    }

    enrollment.setStatus(EnrollmentStatus.ACTIVE);

    return enrollment;
  }

  @Override
  @Transactional
  public Enrollment enrollTrackedEntity(
      TrackedEntity trackedEntity,
      Program program,
      Date enrollmentDate,
      Date occurredDate,
      OrganisationUnit organisationUnit) {
    return enrollTrackedEntity(
        trackedEntity,
        program,
        enrollmentDate,
        occurredDate,
        organisationUnit,
        CodeGenerator.generateUid());
  }

  @Override
  @Transactional
  public Enrollment enrollTrackedEntity(
      TrackedEntity trackedEntity,
      Program program,
      Date enrollmentDate,
      Date occurredDate,
      OrganisationUnit organisationUnit,
      String uid) {
    Enrollment enrollment =
        prepareEnrollment(
            trackedEntity, program, enrollmentDate, occurredDate, organisationUnit, uid);
    manager.save(enrollment);
    trackerOwnershipAccessManager.assignOwnership(
        trackedEntity, program, organisationUnit, true, true);
    eventPublisher.publishEvent(new ProgramEnrollmentNotificationEvent(this, enrollment.getId()));
    manager.update(enrollment);
    trackedEntityService.updateTrackedEntity(trackedEntity);
    return enrollment;
  }
}
