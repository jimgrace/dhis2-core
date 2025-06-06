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
package org.hisp.dhis.predictor.hibernate;

import static com.google.common.base.Preconditions.checkNotNull;

import jakarta.persistence.EntityManager;
import java.util.Collection;
import java.util.List;
import javax.annotation.Nonnull;
import org.hisp.dhis.common.UID;
import org.hisp.dhis.common.hibernate.HibernateIdentifiableObjectStore;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.period.PeriodService;
import org.hisp.dhis.predictor.Predictor;
import org.hisp.dhis.predictor.PredictorStore;
import org.hisp.dhis.security.acl.AclService;
import org.hisp.dhis.system.util.SqlUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * @author Ken Haase
 */
@Repository("org.hisp.dhis.predictor.PredictorStore")
public class HibernatePredictorStore extends HibernateIdentifiableObjectStore<Predictor>
    implements PredictorStore {
  private final PeriodService periodService;

  public HibernatePredictorStore(
      EntityManager entityManager,
      JdbcTemplate jdbcTemplate,
      ApplicationEventPublisher publisher,
      AclService aclService,
      PeriodService periodService) {
    super(entityManager, jdbcTemplate, publisher, Predictor.class, aclService, false);

    checkNotNull(periodService);

    this.periodService = periodService;
  }

  // -------------------------------------------------------------------------
  // Implementation
  // -------------------------------------------------------------------------

  @Override
  public void save(@Nonnull Predictor predictor) {
    predictor.setPeriodType(periodService.reloadPeriodType(predictor.getPeriodType()));

    super.save(predictor);
  }

  @Override
  public void update(@Nonnull Predictor predictor) {
    predictor.setPeriodType(periodService.reloadPeriodType(predictor.getPeriodType()));

    super.update(predictor);
  }

  @Override
  public List<Predictor> getAllByDataElement(Collection<DataElement> dataElements) {
    return getQuery(
            """
            from Predictor p \
            where p.output in :dataElements""")
        .setParameter("dataElements", dataElements)
        .list();
  }

  @Override
  public List<Predictor> getAllWithGeneratorContainingDataElement(
      @Nonnull List<String> dataElementUids) {
    String multiLike = SqlUtils.likeAny("e.expression", dataElementUids);

    return getQuery(
            """
            select p from Predictor p \
            join p.generator as e \
            where %s \
            group by p"""
                .formatted(multiLike))
        .getResultList();
  }

  @Override
  public List<Predictor> getAllWithSampleSkipTestContainingDataElement(
      @Nonnull List<String> dataElementUids) {
    String multiLike = SqlUtils.likeAny("e.expression", dataElementUids);

    return getQuery(
            """
            select p from Predictor p \
            join p.sampleSkipTest as e \
            where %s \
            group by p"""
                .formatted(multiLike))
        .getResultList();
  }

  @Override
  public List<Predictor> getByCategoryOptionCombo(@Nonnull Collection<UID> uids) {
    if (uids.isEmpty()) return List.of();
    return getQuery(
            """
            select distinct p from  Predictor p \
            join p.outputCombo coc \
            where coc.uid in :uids""")
        .setParameter("uids", UID.toValueList(uids))
        .list();
  }
}
