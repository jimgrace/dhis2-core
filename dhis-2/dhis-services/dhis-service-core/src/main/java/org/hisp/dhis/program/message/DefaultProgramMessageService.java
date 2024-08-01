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
package org.hisp.dhis.program.message;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hisp.dhis.common.DeliveryChannel;
import org.hisp.dhis.common.IdentifiableObject;
import org.hisp.dhis.common.IdentifiableObjectManager;
import org.hisp.dhis.common.IllegalQueryException;
import org.hisp.dhis.common.UID;
import org.hisp.dhis.feedback.NotFoundException;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.outboundmessage.BatchResponseStatus;
import org.hisp.dhis.outboundmessage.OutboundMessageBatch;
import org.hisp.dhis.outboundmessage.OutboundMessageBatchService;
import org.hisp.dhis.program.Enrollment;
import org.hisp.dhis.program.Event;
import org.hisp.dhis.program.Program;
import org.hisp.dhis.program.ProgramService;
import org.hisp.dhis.security.acl.AclService;
import org.hisp.dhis.trackedentity.TrackedEntityService;
import org.hisp.dhis.user.CurrentUserUtil;
import org.hisp.dhis.user.User;
import org.hisp.dhis.user.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Zubair <rajazubair.asghar@gmail.com>
 */
@Slf4j
@RequiredArgsConstructor
@Service("org.hisp.dhis.program.message.ProgramMessageService")
public class DefaultProgramMessageService implements ProgramMessageService {
  private final IdentifiableObjectManager manager;

  private final ProgramMessageStore programMessageStore;

  private final OrganisationUnitService organisationUnitService;

  private final TrackedEntityService trackedEntityService;

  private final ProgramService programService;

  private final OutboundMessageBatchService messageBatchService;

  private final UserService userService;

  private final List<DeliveryChannelStrategy> strategies;

  private final List<MessageBatchCreatorService> batchCreators;

  private final AclService aclService;

  // -------------------------------------------------------------------------
  // Implementation methods
  // -------------------------------------------------------------------------

  @Override
  @Transactional(readOnly = true)
  public ProgramMessage getProgramMessage(long id) {
    return programMessageStore.get(id);
  }

  @Override
  @Transactional(readOnly = true)
  public ProgramMessage getProgramMessage(String uid) {
    return programMessageStore.getByUid(uid);
  }

  @Override
  @Transactional(readOnly = true)
  public List<ProgramMessage> getProgramMessages(ProgramMessageQueryParams params) {
    currentUserHasAccess(params);

    return programMessageStore.getProgramMessages(params);
  }

  @Override
  @Transactional
  public long saveProgramMessage(ProgramMessage programMessage) {
    programMessageStore.save(programMessage);
    return programMessage.getId();
  }

  @Override
  @Transactional
  public void updateProgramMessage(ProgramMessage programMessage) {
    programMessageStore.update(programMessage);
  }

  @Override
  @Transactional
  public void deleteProgramMessage(ProgramMessage programMessage) {
    programMessageStore.delete(programMessage);
  }

  @Override
  @Transactional
  public void deleteProgramMessage(UID uid) throws NotFoundException {
    ProgramMessage persisted = programMessageStore.getByUid(uid.getValue());

    if (persisted == null) {
      throw new NotFoundException(ProgramMessage.class, uid.getValue());
    }

    programMessageStore.delete(persisted);
  }

  @Override
  @Transactional
  public void updateProgramMessage(UID uid, ProgramMessageStatus status) throws NotFoundException {
    ProgramMessage persisted = programMessageStore.getByUid(uid.getValue());
    if (persisted == null) {
      throw new NotFoundException(ProgramMessage.class, uid.getValue());
    }

    persisted.setMessageStatus(status);
    programMessageStore.update(persisted);
  }

  @Override
  @Transactional
  public BatchResponseStatus sendMessages(List<ProgramMessage> programMessages) {
    List<ProgramMessage> populatedProgramMessages =
        programMessages.stream()
            .filter(this::hasDataWriteAccess)
            .map(this::setAttributesBasedOnStrategy)
            .collect(Collectors.toList());

    List<OutboundMessageBatch> batches = createBatches(populatedProgramMessages);

    BatchResponseStatus status = new BatchResponseStatus(messageBatchService.sendBatches(batches));

    saveProgramMessages(programMessages, status);

    return status;
  }

  private void currentUserHasAccess(ProgramMessageQueryParams params) {
    Enrollment enrollment = null;

    Set<Program> programs;

    if (params.hasEnrollment()) {
      enrollment = params.getEnrollment();
    }

    if (params.hasEvent()) {
      enrollment = params.getEvent().getEnrollment();
    }

    if (enrollment == null) {
      throw new IllegalQueryException("Enrollment or Event has to be provided");
    }

    programs = new HashSet<>(programService.getCurrentUserPrograms());

    User currentUser = userService.getUserByUsername(CurrentUserUtil.getCurrentUsername());
    if (currentUser != null && !programs.contains(enrollment.getProgram())) {
      throw new IllegalQueryException("User does not have access to the required program");
    }
  }

  @Override
  @Transactional(readOnly = true)
  public void validatePayload(ProgramMessage message) {
    List<String> violations = new ArrayList<>();

    // Check if message text is provided
    if (message.getText() == null) {
      violations.add("Message content must be provided.");
    }

    // Check if delivery channels are specified
    if (message.getDeliveryChannels() == null || message.getDeliveryChannels().isEmpty()) {
      violations.add("Delivery channel must be specified.");
    }

    // Check if either enrollment or event is specified
    if (message.getEnrollment() == null && message.getEvent() == null) {
      violations.add("Enrollment or event must be specified.");
    }

    // Validate enrollment if it exists
    if (message.getEnrollment() != null) {
      String uid = message.getEnrollment().getUid();
      if (!manager.exists(Enrollment.class, uid)) {
        violations.add(String.format("Enrollment: %s does not exist.", uid));
      }
    }

    // Validate event if it exists
    if (message.getEvent() != null) {
      String uid = message.getEvent().getUid();
      if (!manager.exists(Event.class, uid)) {
        violations.add(String.format("Event: %s does not exist.", uid));
      }
    }

    ProgramMessageRecipients recipients = message.getRecipients();

    // Validate tracked entity if it exists
    if (recipients.getTrackedEntity() != null
        && trackedEntityService.getTrackedEntity(recipients.getTrackedEntity().getUid()) == null) {
      violations.add("Tracked entity does not exist.");
    }

    // Validate orgUnit if it exists
    if (recipients.getOrganisationUnit() != null
        && organisationUnitService.getOrganisationUnit(recipients.getOrganisationUnit().getUid())
            == null) {
      violations.add("Organisation unit does not exist.");
    }

    // If there are any violations, log them and throw an exception
    if (!violations.isEmpty()) {
      String violationMessage = String.join(", ", violations);
      log.info("Message validation failed: " + violationMessage);
      throw new IllegalQueryException(violationMessage);
    }
  }

  // ---------------------------------------------------------------------
  // Supportive Methods
  // ---------------------------------------------------------------------

  private boolean hasDataWriteAccess(ProgramMessage message) {
    IdentifiableObject object = null;

    boolean isAuthorized;

    if (message.hasEnrollment()) {
      object = message.getEnrollment().getProgram();
    } else if (message.hasEvent()) {
      object = message.getEvent().getProgramStage();
    }

    if (object != null) {
      isAuthorized = aclService.canDataWrite(CurrentUserUtil.getCurrentUserDetails(), object);

      if (!isAuthorized) {
        log.error(
            String.format(
                "Sending message failed. User does not have write access for %s.",
                object.getName()));

        return false;
      }
    }

    return true;
  }

  private void saveProgramMessages(List<ProgramMessage> messageBatch, BatchResponseStatus status) {
    messageBatch.parallelStream()
        .map(pm -> setParameters(pm, status))
        .forEach(this::saveProgramMessage);
  }

  private ProgramMessage setParameters(ProgramMessage message, BatchResponseStatus status) {
    message.setEnrollment(getEnrollment(message));
    message.setEvent(getEvent(message));
    message.setProcessedDate(new Date());
    message.setMessageStatus(
        status.isOk() ? ProgramMessageStatus.SENT : ProgramMessageStatus.FAILED);

    return message;
  }

  private List<OutboundMessageBatch> createBatches(List<ProgramMessage> programMessages) {
    return batchCreators.stream()
        .map(bc -> bc.getMessageBatch(programMessages))
        .filter(bc -> !bc.getMessages().isEmpty())
        .toList();
  }

  private Enrollment getEnrollment(ProgramMessage programMessage) {
    if (programMessage.getEnrollment() != null) {
      return manager.get(Enrollment.class, programMessage.getEnrollment().getUid());
    }

    return null;
  }

  private Event getEvent(ProgramMessage programMessage) {
    if (programMessage.getEvent() != null) {
      return manager.get(Event.class, programMessage.getEvent().getUid());
    }

    return null;
  }

  private ProgramMessage setAttributesBasedOnStrategy(ProgramMessage message) {
    Set<DeliveryChannel> channels = message.getDeliveryChannels();

    for (DeliveryChannel channel : channels) {
      for (DeliveryChannelStrategy strategy : strategies) {
        if (strategy.getDeliveryChannel().equals(channel)) {
          strategy.setAttributes(message);
        }
      }
    }

    return message;
  }
}
