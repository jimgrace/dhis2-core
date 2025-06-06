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
package org.hisp.dhis.dataapproval.hibernate;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.hisp.dhis.common.IdentifiableObjectUtils.getUids;
import static org.hisp.dhis.commons.util.TextUtils.getQuotedCommaDelimitedString;
import static org.hisp.dhis.util.DateUtils.toMediumDate;

import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Set;
import org.apache.commons.collections4.CollectionUtils;
import org.hisp.dhis.category.CategoryOptionCombo;
import org.hisp.dhis.commons.util.SqlHelper;
import org.hisp.dhis.commons.util.TextUtils;
import org.hisp.dhis.dataapproval.DataApprovalAudit;
import org.hisp.dhis.dataapproval.DataApprovalAuditQueryParams;
import org.hisp.dhis.dataapproval.DataApprovalAuditStore;
import org.hisp.dhis.hibernate.HibernateGenericStore;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.user.CurrentUserUtil;
import org.hisp.dhis.user.User;
import org.hisp.dhis.user.UserService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * @author Jim Grace
 */
@Repository("org.hisp.dhis.dataapproval.DataApprovalAuditStore")
public class HibernateDataApprovalAuditStore extends HibernateGenericStore<DataApprovalAudit>
    implements DataApprovalAuditStore {
  // -------------------------------------------------------------------------
  // Dependencies
  // -------------------------------------------------------------------------

  private final UserService userService;

  public HibernateDataApprovalAuditStore(
      EntityManager entityManager,
      JdbcTemplate jdbcTemplate,
      ApplicationEventPublisher publisher,
      UserService userService) {
    super(entityManager, jdbcTemplate, publisher, DataApprovalAudit.class, false);

    checkNotNull(userService);

    this.userService = userService;
  }

  // -------------------------------------------------------------------------
  // DataValueAuditStore implementation
  // -------------------------------------------------------------------------

  @Override
  public void deleteDataApprovalAudits(OrganisationUnit organisationUnit) {
    String hql = "delete from DataApprovalAudit d where d.organisationUnit = :unit";

    entityManager.createQuery(hql).setParameter("unit", organisationUnit).executeUpdate();
  }

  @Override
  public void deleteDataApprovalAudits(CategoryOptionCombo coc) {
    String hql = "delete from DataApprovalAudit d where d.attributeOptionCombo = :coc";
    entityManager.createQuery(hql).setParameter("coc", coc).executeUpdate();
  }

  @Override
  public List<DataApprovalAudit> getDataApprovalAudits(DataApprovalAuditQueryParams params) {
    SqlHelper hlp = new SqlHelper();

    String hql = "select a from DataApprovalAudit a ";

    if (params.hasWorkflows()) {
      hql +=
          hlp.whereAnd()
              + " a.workflow.uid in ("
              + getQuotedCommaDelimitedString(getUids(params.getWorkflows()))
              + ") ";
    }

    if (params.hasLevels()) {
      hql +=
          hlp.whereAnd()
              + " a.level.uid in ("
              + getQuotedCommaDelimitedString(getUids(params.getLevels()))
              + ") ";
    }

    if (params.hasOrganisationUnits()) {
      hql +=
          hlp.whereAnd()
              + " a.organisationUnit.uid in ("
              + getQuotedCommaDelimitedString(getUids(params.getOrganisationUnits()))
              + ") ";
    }

    if (params.hasAttributeOptionCombos()) {
      hql +=
          hlp.whereAnd()
              + " a.attributeOptionCombo.uid in ("
              + getQuotedCommaDelimitedString(getUids(params.getAttributeOptionCombos()))
              + ") ";
    }

    if (params.hasStartDate()) {
      hql +=
          hlp.whereAnd() + " a.period.startDate >= '" + toMediumDate(params.getStartDate()) + "' ";
    }

    if (params.hasEndDate()) {
      hql += hlp.whereAnd() + " a.period.endDate <= '" + toMediumDate(params.getEndDate()) + "' ";
    }

    User currentUser = userService.getUserByUsername(CurrentUserUtil.getCurrentUsername());
    Set<OrganisationUnit> userOrgUnits =
        currentUser != null ? currentUser.getOrganisationUnits() : null;

    if (!CollectionUtils.isEmpty(userOrgUnits)) {
      hql += hlp.whereAnd() + " (";

      for (OrganisationUnit userOrgUnit : userOrgUnits) {
        hql += "a.organisationUnit.path like '%" + userOrgUnit.getUid() + "%' or ";
      }

      hql = TextUtils.removeLastOr(hql) + ") ";
    }

    hql +=
        "order by a.workflow.name, a.organisationUnit.name, a.attributeOptionCombo.name, a.period.startDate, a.period.endDate, a.created";

    return getQuery(hql).list();
  }
}
