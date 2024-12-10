/*
 * Copyright (c) 2004-2024, University of Oslo
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
package org.hisp.dhis.merge.category.optioncombo;

import static org.hisp.dhis.datavalue.DataValue.dataValueWithNewAttrOptionCombo;
import static org.hisp.dhis.datavalue.DataValue.dataValueWithNewCatOptionCombo;
import static org.hisp.dhis.merge.DataMergeStrategy.DISCARD;
import static org.hisp.dhis.merge.DataMergeStrategy.LAST_UPDATED;

import jakarta.persistence.EntityManager;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hisp.dhis.category.CategoryOptionCombo;
import org.hisp.dhis.common.BaseIdentifiableObject;
import org.hisp.dhis.common.UID;
import org.hisp.dhis.dataapproval.DataApproval;
import org.hisp.dhis.dataapproval.DataApprovalAuditStore;
import org.hisp.dhis.dataapproval.DataApprovalStore;
import org.hisp.dhis.dataset.CompleteDataSetRegistration;
import org.hisp.dhis.dataset.CompleteDataSetRegistrationStore;
import org.hisp.dhis.datavalue.DataValue;
import org.hisp.dhis.datavalue.DataValueAudit;
import org.hisp.dhis.datavalue.DataValueAuditStore;
import org.hisp.dhis.datavalue.DataValueStore;
import org.hisp.dhis.merge.CommonDataMergeHandler;
import org.hisp.dhis.merge.CommonDataMergeHandler.DataValueMergeParams;
import org.hisp.dhis.merge.MergeRequest;
import org.hisp.dhis.program.Event;
import org.hisp.dhis.program.EventStore;
import org.springframework.stereotype.Component;

/**
 * Merge handler for data types.
 *
 * @author david mackessy
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataCategoryOptionComboMergeHandler {

  private final DataValueStore dataValueStore;
  private final DataValueAuditStore dataValueAuditStore;
  private final DataApprovalAuditStore dataApprovalAuditStore;
  private final DataApprovalStore dataApprovalStore;
  private final EventStore eventStore;
  private final CompleteDataSetRegistrationStore completeDataSetRegistrationStore;
  private final CommonDataMergeHandler commonDataMergeHandler;
  private final EntityManager entityManager;

  public void handleDataValues(
      @Nonnull List<CategoryOptionCombo> sources,
      @Nonnull CategoryOptionCombo target,
      @Nonnull MergeRequest mergeRequest) {

    if (DISCARD == mergeRequest.getDataMergeStrategy()) {
      log.info(
          mergeRequest.getDataMergeStrategy()
              + " dataMergeStrategy being used, deleting source data values");
      dataValueStore.deleteDataValuesByCategoryOptionCombo(sources);
      dataValueStore.deleteDataValuesByAttributeOptionCombo(sources);
    } else {
      // ----------------------
      // category option combos
      // ----------------------
      List<DataValue> sourceCocDataValues =
          dataValueStore.getAllDataValuesByCatOptCombo(
              UID.of(sources.stream().map(BaseIdentifiableObject::getUid).toList()));
      log.info(
          "{} data values retrieved for source categoryOptionCombos", sourceCocDataValues.size());

      // get map of target data values, using the duplicate key constraints as the key
      Map<String, DataValue> targetCocDataValues =
          dataValueStore.getAllDataValuesByCatOptCombo(UID.of(List.of(target.getUid()))).stream()
              .collect(Collectors.toMap(getCocDataValueKey, dv -> dv));
      log.info(
          "{} data values retrieved for target categoryOptionCombo", targetCocDataValues.size());

      commonDataMergeHandler.handleDataValues(
          new DataValueMergeParams<>(
              mergeRequest,
              sources,
              target,
              sourceCocDataValues,
              targetCocDataValues,
              dataValueStore::deleteDataValuesByCategoryOptionCombo,
              cocDataValueDuplicates,
              dataValueWithNewCatOptionCombo,
              getCocDataValueKey));

      // -----------------------
      // attribute option combos
      // -----------------------
      List<DataValue> sourceAocDataValues =
          dataValueStore.getAllDataValuesByAttrOptCombo(
              UID.of(sources.stream().map(BaseIdentifiableObject::getUid).toList()));
      log.info(
          "{} data values retrieved for source attributeOptionCombos", sourceAocDataValues.size());

      // get map of target data values, using the duplicate key constraints as the key
      Map<String, DataValue> targetAocDataValues =
          dataValueStore.getAllDataValuesByAttrOptCombo(UID.of(List.of(target.getUid()))).stream()
              .collect(Collectors.toMap(getAocDataValueKey, dv -> dv));
      log.info(
          "{} data values retrieved for target attributeOptionCombo", targetAocDataValues.size());

      commonDataMergeHandler.handleDataValues(
          new DataValueMergeParams<>(
              mergeRequest,
              sources,
              target,
              sourceAocDataValues,
              targetAocDataValues,
              dataValueStore::deleteDataValuesByAttributeOptionCombo,
              aocDataValueDuplicates,
              dataValueWithNewAttrOptionCombo,
              getAocDataValueKey));
    }
  }

  /**
   * All {@link DataValueAudit}s will either be deleted or left as is, based on whether the source
   * {@link CategoryOptionCombo}s are being deleted or not.
   */
  public void handleDataValueAudits(
      @Nonnull List<CategoryOptionCombo> sources,
      CategoryOptionCombo target,
      @Nonnull MergeRequest mergeRequest) {
    if (mergeRequest.isDeleteSources()) {
      log.info(
          "Deleting source data value audit records as source CategoryOptionCombos are being deleted");
      sources.forEach(dataValueAuditStore::deleteDataValueAudits);
    } else {
      log.info(
          "Leaving source data value audit records as is, source CategoryOptionCombos are not being deleted");
    }
  }

  public void handleDataApprovals(
      List<CategoryOptionCombo> sources, CategoryOptionCombo target, MergeRequest mergeRequest) {
    if (DISCARD == mergeRequest.getDataMergeStrategy()) {
      dataApprovalStore.deleteByCategoryOptionCombo(
          UID.of(sources.stream().map(BaseIdentifiableObject::getUid).toList()));
    } else {
      List<DataApproval> sourceDas =
          dataApprovalStore.getByCategoryOptionCombo(
              UID.of(sources.stream().map(BaseIdentifiableObject::getUid).toList()));

      // sort into duplicate & non-duplicates
      // get map of target data approvals, using the duplicate key constraints as the key
      Map<String, DataApproval> targetDas =
          dataApprovalStore.getByCategoryOptionCombo(List.of(UID.of(target.getUid()))).stream()
              .collect(Collectors.toMap(getDataApprovalKey, da -> da));
      log.info("{} data approvals retrieved for target categoryOptionCombo", targetDas.size());

      Map<Boolean, List<DataApproval>> sourceDuplicateList =
          sourceDas.stream()
              .collect(Collectors.partitioningBy(dv -> dataApprovalDuplicates.test(dv, targetDas)));

      if (!sourceDuplicateList.get(false).isEmpty())
        handleDaNonDuplicates(sourceDuplicateList.get(false), target);
      if (!sourceDuplicateList.get(true).isEmpty())
        handleDaDuplicates(sourceDuplicateList.get(true), targetDas, target, sources);
    }
  }

  /** */
  public void handleDataApprovalAudits(
      List<CategoryOptionCombo> sources, CategoryOptionCombo target, MergeRequest mergeRequest) {
    if (mergeRequest.isDeleteSources()) {
      log.info(
          "Deleting source data approval audit records as source CategoryOptionCombos are being deleted");
      sources.forEach(dataApprovalAuditStore::deleteDataApprovalAudits);
    } else {
      log.info(
          "Leaving source data approval audit records as is, source CategoryOptionCombos are not being deleted");
    }
  }

  /** */
  public void handleEvents(
      @Nonnull List<CategoryOptionCombo> sources,
      @Nonnull CategoryOptionCombo target,
      @Nonnull MergeRequest mergeRequest) {
    List<Event> sourceEvents =
        eventStore.getAllByAttributeOptionCombo(
            UID.of(sources.stream().map(BaseIdentifiableObject::getUid).toList()));

    sourceEvents.forEach(e -> e.setAttributeOptionCombo(target));
  }

  /** */
  public void handleCompleteDataSetRegistrations(
      List<CategoryOptionCombo> sources,
      CategoryOptionCombo target,
      @Nonnull MergeRequest mergeRequest) {
    if (DISCARD == mergeRequest.getDataMergeStrategy()) {
      completeDataSetRegistrationStore.deleteByCategoryOptionCombo(sources);
    } else if (LAST_UPDATED == mergeRequest.getDataMergeStrategy()) {
      // get DVs from sources
      List<CompleteDataSetRegistration> sourceCdsr =
          completeDataSetRegistrationStore.getAllByCategoryOptionCombo(
              UID.of(sources.stream().map(BaseIdentifiableObject::getUid).toList()));

      // get map of target cdsr, using the duplicate key constraints as the key
      Map<String, CompleteDataSetRegistration> targetcdsr =
          completeDataSetRegistrationStore
              .getAllByCategoryOptionCombo(UID.of(List.of(target.getUid())))
              .stream()
              .collect(Collectors.toMap(getCdsrKey, cdsr -> cdsr));

      Map<Boolean, List<CompleteDataSetRegistration>> sourceDuplicateList =
          sourceCdsr.stream()
              .collect(Collectors.partitioningBy(cdsr -> cdsrDuplicates.test(cdsr, targetcdsr)));

      if (!sourceDuplicateList.get(false).isEmpty()) {
        handleCdsrNonDuplicates(sourceDuplicateList.get(false), target);
      }
      if (!sourceDuplicateList.get(true).isEmpty()) {
        handleCdsrDuplicates(sourceDuplicateList.get(true), targetcdsr, target, sources);
      }
    }
  }

  private void handleCdsrDuplicates(
      List<CompleteDataSetRegistration> sourceCdsrDuplicates,
      Map<String, CompleteDataSetRegistration> targetCdsr,
      CategoryOptionCombo target,
      List<CategoryOptionCombo> sources) {
    // group CompleteDataSetRegistration by key, so we can deal with each duplicate correctly
    Map<String, List<CompleteDataSetRegistration>> sourceCdsrGroupedByKey =
        sourceCdsrDuplicates.stream().collect(Collectors.groupingBy(getCdsrKey));

    // filter groups down to single CDSR with latest date
    List<CompleteDataSetRegistration> filtered =
        sourceCdsrGroupedByKey.values().stream()
            .map(
                ls ->
                    Collections.max(
                        ls, Comparator.comparing(CompleteDataSetRegistration::getLastUpdated)))
            .toList();

    for (CompleteDataSetRegistration source : filtered) {
      CompleteDataSetRegistration matchingTargetCdsr = targetCdsr.get(getCdsrKey.apply(source));

      if (matchingTargetCdsr.getLastUpdated().before(source.getLastUpdated())) {
        completeDataSetRegistrationStore.deleteCompleteDataSetRegistration(matchingTargetCdsr);

        CompleteDataSetRegistration copyWithNewRef =
            CompleteDataSetRegistration.copyWithNewAttributeOptionCombo(source, target);
        completeDataSetRegistrationStore.saveWithoutUpdatingLastUpdated(copyWithNewRef);
      }
    }

    // delete the rest of the source CDSRs after handling the last update duplicate
    completeDataSetRegistrationStore.deleteByCategoryOptionCombo(sources);
  }

  /**
   * Handles non duplicate CompleteDataSetRegistrations. As CompleteDataSetRegistration has a
   * composite primary key which includes CategoryOptionCombo, this cannot be updated. A new copy of
   * the CompleteDataSetRegistration is required, which uses the target CompleteDataSetRegistration
   * as the new ref.
   *
   * @param sourceCdsr sources to handle
   * @param target target to use as new ref in copy
   */
  private void handleCdsrNonDuplicates(
      List<CompleteDataSetRegistration> sourceCdsr, CategoryOptionCombo target) {
    sourceCdsr.forEach(
        cdsr -> {
          CompleteDataSetRegistration copyWithNewAoc =
              CompleteDataSetRegistration.copyWithNewAttributeOptionCombo(cdsr, target);
          completeDataSetRegistrationStore.saveWithoutUpdatingLastUpdated(copyWithNewAoc);
        });

    sourceCdsr.forEach(completeDataSetRegistrationStore::deleteCompleteDataSetRegistration);
  }

  /**
   * Method to handle merging duplicate {@link DataApproval}s. There may be multiple potential
   * {@link DataApproval} duplicates. The {@link DataApproval} with the latest `lastUpdated` value
   * is filtered out, the rest are then deleted at the end of the process (We can only have one of
   * these entries due to the unique key constraint). The filtered-out {@link DataApproval} will be
   * compared with the target {@link DataApproval} lastUpdated date.
   *
   * <p>If the target date is later, no action is required.
   *
   * <p>If the source date is later, the source {@link DataApproval} has its {@link
   * CategoryOptionCombo} set as the target. The matching target {@link DataApproval}s will then be
   * deleted.
   *
   * @param sourceDaDuplicates {@link DataApproval}s to merge
   * @param targetDaMap target map of {@link DataApproval}s to check duplicates against
   * @param target target {@link CategoryOptionCombo}
   */
  private void handleDaDuplicates(
      @Nonnull Collection<DataApproval> sourceDaDuplicates,
      @Nonnull Map<String, DataApproval> targetDaMap,
      @Nonnull CategoryOptionCombo target,
      @Nonnull List<CategoryOptionCombo> sources) {
    log.info(
        "Handling "
            + sourceDaDuplicates.size()
            + " duplicate data approvals, keeping later lastUpdated value");

    // group Data approvals by key, so we can deal with each duplicate correctly
    Map<String, List<DataApproval>> sourceDataApprovalsGroupedByKey =
        sourceDaDuplicates.stream().collect(Collectors.groupingBy(getDataApprovalKey));

    // filter groups down to single DA with latest date
    List<DataApproval> filtered =
        sourceDataApprovalsGroupedByKey.values().stream()
            .map(ls -> Collections.max(ls, Comparator.comparing(DataApproval::getLastUpdated)))
            .toList();

    for (DataApproval source : filtered) {
      DataApproval matchingTargetDataApproval = targetDaMap.get(getDataApprovalKey.apply(source));

      if (matchingTargetDataApproval.getLastUpdated().before(source.getLastUpdated())) {
        dataApprovalStore.deleteDataApproval(matchingTargetDataApproval);
        // flush is required here as it's not possible to update a source DataApproval with
        // essentially the same unique constraint key as the target DataApproval until it is removed
        // from the Hibernate session
        entityManager.flush();

        source.setAttributeOptionCombo(target);
      }
    }

    // delete the rest of the source data values after handling the last update duplicate
    dataApprovalStore.deleteByCategoryOptionCombo(
        UID.of(sources.stream().map(BaseIdentifiableObject::getUid).toList()));
  }

  /**
   * Method to handle merging non-duplicate {@link DataApproval}s. Source {@link DataApproval}s will
   * be assigned the target {@link CategoryOptionCombo} ref.
   *
   * @param dataApprovals {@link DataApproval}s to merge
   * @param target target {@link CategoryOptionCombo}
   */
  private void handleDaNonDuplicates(
      @Nonnull List<DataApproval> dataApprovals, @Nonnull CategoryOptionCombo target) {
    log.info(
        "Handling "
            + dataApprovals.size()
            + " non duplicate data values. Each will have their attribute option combo set as the target");

    dataApprovals.forEach(sourceDataApproval -> sourceDataApproval.setAttributeOptionCombo(target));
  }

  private static final Function<DataValue, String> getCocDataValueKey =
      dv ->
          String.valueOf(dv.getPeriod().getId())
              + dv.getSource().getId()
              + dv.getDataElement().getId()
              + dv.getAttributeOptionCombo().getId();

  private static final Function<DataApproval, String> getDataApprovalKey =
      da ->
          String.valueOf(da.getPeriod().getId())
              + da.getDataApprovalLevel().getId()
              + da.getWorkflow().getId()
              + da.getOrganisationUnit().getId();

  private static final BiPredicate<DataValue, Map<String, DataValue>> cocDataValueDuplicates =
      (sourceDv, targetDvs) -> targetDvs.containsKey(getCocDataValueKey.apply(sourceDv));

  private static final BiPredicate<DataApproval, Map<String, DataApproval>> dataApprovalDuplicates =
      (sourceDa, targetDas) -> targetDas.containsKey(getDataApprovalKey.apply(sourceDa));

  private static final Function<DataValue, String> getAocDataValueKey =
      dv ->
          String.valueOf(dv.getPeriod().getId())
              + dv.getSource().getId()
              + dv.getDataElement().getId()
              + dv.getCategoryOptionCombo().getId();

  private static final BiPredicate<DataValue, Map<String, DataValue>> aocDataValueDuplicates =
      (sourceDv, targetDvs) -> targetDvs.containsKey(getAocDataValueKey.apply(sourceDv));

  private static final Function<CompleteDataSetRegistration, String> getCdsrKey =
      cdsr ->
          String.valueOf(cdsr.getPeriod().getId())
              + cdsr.getSource().getId()
              + cdsr.getDataSet().getId();

  private static final BiPredicate<
          CompleteDataSetRegistration, Map<String, CompleteDataSetRegistration>>
      cdsrDuplicates =
          (sourceCdsr, targetCdsr) -> targetCdsr.containsKey(getCdsrKey.apply(sourceCdsr));
}