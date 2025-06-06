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
package org.hisp.dhis.analytics.event.data;

import static org.hisp.dhis.analytics.AnalyticsConstants.ANALYTICS_TBL_ALIAS;
import static org.hisp.dhis.analytics.AnalyticsConstants.ORG_UNIT_GROUPSET_STRUCT_ALIAS;
import static org.hisp.dhis.analytics.AnalyticsConstants.ORG_UNIT_STRUCT_ALIAS;
import static org.hisp.dhis.analytics.AnalyticsConstants.OWNERSHIP_TBL_ALIAS;
import static org.hisp.dhis.analytics.OrgUnitFieldType.OWNER_AT_START;
import static org.hisp.dhis.system.util.SqlUtils.quote;
import static org.hisp.dhis.util.DateUtils.plusOneDay;
import static org.hisp.dhis.util.DateUtils.toMediumDate;

import java.util.Date;
import org.hisp.dhis.analytics.AnalyticsTableType;
import org.hisp.dhis.analytics.event.EventQueryParams;
import org.hisp.dhis.analytics.table.model.AnalyticsTable;
import org.hisp.dhis.program.AnalyticsType;

/**
 * Joiner of organisation unit tables for event/enrollment analytics query.
 *
 * @author Jim Grace
 */
public final class OrgUnitTableJoiner {
  private OrgUnitTableJoiner() {
    throw new UnsupportedOperationException("util");
  }

  /**
   * Generates SQL to join any needed organisation unit tables.
   *
   * @param params a {@see EventQueryParams}
   * @param analyticsType a {@see AnalyticsType}
   */
  public static String joinOrgUnitTables(EventQueryParams params, AnalyticsType analyticsType) {
    String sql = "";

    if (params.getOrgUnitField().getType().isOwnership()) {
      sql += joinOwnershipTable(params);
    }

    if (params.getOrgUnitField().isJoinOrgUnitTables(analyticsType)) {
      sql += joinOrgUnitStructureTables(params, analyticsType);
    }

    return sql;
  }

  // -------------------------------------------------------------------------
  // Supportive methods
  // -------------------------------------------------------------------------

  /**
   * Joins the ownership table.
   *
   * <p>The date ranges in the ownership table are based on the ownership at the start of each day.
   * To get the ownership at the end of a day, we must add one to the date, to get the ownership at
   * the start of the next day.
   *
   * <p>If we get here, the {@link DefaultEventQueryPlanner} will have separated any period
   * dimensions to one period per query. Therefore, we can use the earliest start date and latest
   * end date to get the date range for either that one period or any collection of filter periods.
   */
  private static String joinOwnershipTable(EventQueryParams params) {
    Date compareDate =
        (params.getOrgUnitField().getType() == OWNER_AT_START)
            ? params.getEarliestStartDate()
            : plusOneDay(params.getLatestEndDate());

    String ownershipTable =
        AnalyticsTable.getTableName(AnalyticsTableType.OWNERSHIP, params.getProgram());

    return "left join "
        + ownershipTable
        + " as "
        + OWNERSHIP_TBL_ALIAS
        + " on "
        + quote(ANALYTICS_TBL_ALIAS, "trackedentity")
        + " = "
        + quote(OWNERSHIP_TBL_ALIAS, "teuid")
        + " and '"
        + toMediumDate(compareDate)
        + "' between "
        + quote(OWNERSHIP_TBL_ALIAS, "startdate")
        + " and "
        + quote(OWNERSHIP_TBL_ALIAS, "enddate")
        + " ";
  }

  /**
   * Joins the analytics_rs_orgunitstructure table and, if needed, the
   * analytics_rs_orgunitgroupsetstructure table.
   */
  private static String joinOrgUnitStructureTables(
      EventQueryParams params, AnalyticsType analyticsType) {
    String orgUnitJoinCol = params.getOrgUnitField().getOrgUnitJoinCol(analyticsType);

    String sql =
        "left join analytics_rs_orgunitstructure as "
            + ORG_UNIT_STRUCT_ALIAS
            + " on "
            + orgUnitJoinCol
            + " = "
            + quote(ORG_UNIT_STRUCT_ALIAS, "organisationunituid")
            + " ";

    if (params.hasOrganisationUnitGroupSets()) {
      sql +=
          "left join analytics_rs_organisationunitgroupsetstructure as "
              + ORG_UNIT_GROUPSET_STRUCT_ALIAS
              + " on "
              + quote(ORG_UNIT_STRUCT_ALIAS, "organisationunitid")
              + " = "
              + quote(ORG_UNIT_GROUPSET_STRUCT_ALIAS, "organisationunitid")
              + " ";
    }

    return sql;
  }
}
