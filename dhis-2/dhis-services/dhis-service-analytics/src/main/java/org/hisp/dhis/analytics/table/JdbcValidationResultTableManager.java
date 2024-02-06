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
package org.hisp.dhis.analytics.table;

import static org.hisp.dhis.analytics.table.model.AnalyticsValueType.FACT;
import static org.hisp.dhis.analytics.util.AnalyticsSqlUtils.quote;
import static org.hisp.dhis.db.model.DataType.CHARACTER_11;
import static org.hisp.dhis.db.model.DataType.DATE;
import static org.hisp.dhis.db.model.DataType.INTEGER;
import static org.hisp.dhis.db.model.DataType.TEXT;
import static org.hisp.dhis.db.model.DataType.TIMESTAMP;
import static org.hisp.dhis.db.model.constraint.Nullable.NOT_NULL;
import static org.hisp.dhis.db.model.constraint.Nullable.NULL;
import static org.hisp.dhis.util.DateUtils.getLongDateString;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.hisp.dhis.analytics.AnalyticsTableHookService;
import org.hisp.dhis.analytics.AnalyticsTableType;
import org.hisp.dhis.analytics.AnalyticsTableUpdateParams;
import org.hisp.dhis.analytics.partition.PartitionManager;
import org.hisp.dhis.analytics.table.model.AnalyticsTable;
import org.hisp.dhis.analytics.table.model.AnalyticsTableColumn;
import org.hisp.dhis.analytics.table.model.AnalyticsTablePartition;
import org.hisp.dhis.analytics.table.setting.AnalyticsTableExportSettings;
import org.hisp.dhis.category.Category;
import org.hisp.dhis.category.CategoryService;
import org.hisp.dhis.common.IdentifiableObjectManager;
import org.hisp.dhis.commons.util.TextUtils;
import org.hisp.dhis.dataapproval.DataApprovalLevelService;
import org.hisp.dhis.db.model.Logged;
import org.hisp.dhis.organisationunit.OrganisationUnitGroupSet;
import org.hisp.dhis.organisationunit.OrganisationUnitLevel;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.period.PeriodDataProvider;
import org.hisp.dhis.period.PeriodType;
import org.hisp.dhis.resourcetable.ResourceTableService;
import org.hisp.dhis.setting.SystemSettingManager;
import org.hisp.dhis.system.database.DatabaseInfoProvider;
import org.hisp.dhis.util.DateUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * @author Henning Håkonsen
 */
@Service("org.hisp.dhis.analytics.ValidationResultAnalyticsTableManager")
public class JdbcValidationResultTableManager extends AbstractJdbcTableManager {
  private static final List<AnalyticsTableColumn> FIXED_COLS =
      List.of(
          new AnalyticsTableColumn("dx", CHARACTER_11, NOT_NULL, "vr.uid"),
          new AnalyticsTableColumn("pestartdate", TIMESTAMP, "pe.startdate"),
          new AnalyticsTableColumn("peenddate", TIMESTAMP, "pe.enddate"),
          new AnalyticsTableColumn("year", INTEGER, NOT_NULL, "ps.year"));

  public JdbcValidationResultTableManager(
      IdentifiableObjectManager idObjectManager,
      OrganisationUnitService organisationUnitService,
      CategoryService categoryService,
      SystemSettingManager systemSettingManager,
      DataApprovalLevelService dataApprovalLevelService,
      ResourceTableService resourceTableService,
      AnalyticsTableHookService tableHookService,
      PartitionManager partitionManager,
      DatabaseInfoProvider databaseInfoProvider,
      @Qualifier("analyticsJdbcTemplate") JdbcTemplate jdbcTemplate,
      AnalyticsTableExportSettings analyticsExportSettings,
      PeriodDataProvider periodDataProvider) {
    super(
        idObjectManager,
        organisationUnitService,
        categoryService,
        systemSettingManager,
        dataApprovalLevelService,
        resourceTableService,
        tableHookService,
        partitionManager,
        databaseInfoProvider,
        jdbcTemplate,
        analyticsExportSettings,
        periodDataProvider);
  }

  @Override
  public AnalyticsTableType getAnalyticsTableType() {
    return AnalyticsTableType.VALIDATION_RESULT;
  }

  @Override
  public List<AnalyticsTable> getAnalyticsTables(AnalyticsTableUpdateParams params) {
    AnalyticsTable table =
        params.isLatestUpdate()
            ? new AnalyticsTable(AnalyticsTableType.VALIDATION_RESULT, List.of(), Logged.LOGGED)
            : getRegularAnalyticsTable(params, getDataYears(params), getColumns());

    return table.hasTablePartitions() ? List.of(table) : List.of();
  }

  @Override
  public Set<String> getExistingDatabaseTables() {
    return Set.of(getTableName());
  }

  @Override
  public String validState() {
    boolean hasData =
        jdbcTemplate
            .queryForRowSet("select validationresultid from validationresult limit 1")
            .next();

    if (!hasData) {
      return "No validation results exist, not updating validation result analytics tables";
    }

    return null;
  }

  @Override
  protected boolean hasUpdatedLatestData(Date startDate, Date endDate) {
    return false;
  }

  @Override
  protected List<String> getPartitionChecks(Integer year, Date endDate) {
    Objects.requireNonNull(year);
    return List.of("year = " + year);
  }

  @Override
  protected void populateTable(
      AnalyticsTableUpdateParams params, AnalyticsTablePartition partition) {
    String tableName = partition.getName();

    String sql = "insert into " + tableName + " (";

    List<AnalyticsTableColumn> columns = partition.getMasterTable().getAnalyticsTableColumns();

    for (AnalyticsTableColumn col : columns) {
      sql += col.getName() + ",";
    }

    sql = TextUtils.removeLastComma(sql) + ") select ";

    for (AnalyticsTableColumn col : columns) {
      sql += col.getSelectExpression() + ",";
    }

    sql = TextUtils.removeLastComma(sql) + " ";

    // Database legacy fix

    sql = sql.replace("organisationunitid", "sourceid");

    sql +=
        "from validationresult vrs "
            + "inner join period pe on vrs.periodid=pe.periodid "
            + "inner join _periodstructure ps on vrs.periodid=ps.periodid "
            + "inner join validationrule vr on vr.validationruleid=vrs.validationruleid "
            + "inner join _organisationunitgroupsetstructure ougs on vrs.organisationunitid=ougs.organisationunitid "
            + "and (cast(date_trunc('month', pe.startdate) as date)=ougs.startdate or ougs.startdate is null) "
            + "left join _orgunitstructure ous on vrs.organisationunitid=ous.organisationunitid "
            + "inner join _categorystructure acs on vrs.attributeoptioncomboid=acs.categoryoptioncomboid "
            + "where ps.year = "
            + partition.getYear()
            + " "
            + "and vrs.created < '"
            + getLongDateString(params.getStartTime())
            + "' "
            + "and vrs.created is not null";

    invokeTimeAndLog(sql, String.format("Populate %s", tableName));
  }

  private List<Integer> getDataYears(AnalyticsTableUpdateParams params) {
    String sql =
        "select distinct(extract(year from pe.startdate)) "
            + "from validationresult vrs "
            + "inner join period pe on vrs.periodid=pe.periodid "
            + "where pe.startdate is not null "
            + "and vrs.created < '"
            + getLongDateString(params.getStartTime())
            + "' ";

    if (params.getFromDate() != null) {
      sql += "and pe.startdate >= '" + DateUtils.getMediumDateString(params.getFromDate()) + "'";
    }

    return jdbcTemplate.queryForList(sql, Integer.class);
  }

  private List<AnalyticsTableColumn> getColumns() {
    List<AnalyticsTableColumn> columns = new ArrayList<>();

    List<OrganisationUnitGroupSet> orgUnitGroupSets =
        idObjectManager.getDataDimensionsNoAcl(OrganisationUnitGroupSet.class);

    List<OrganisationUnitLevel> levels = organisationUnitService.getFilledOrganisationUnitLevels();

    List<Category> attributeCategories = categoryService.getAttributeDataDimensionCategoriesNoAcl();

    for (OrganisationUnitGroupSet groupSet : orgUnitGroupSets) {
      columns.add(
          new AnalyticsTableColumn(
              groupSet.getUid(),
              CHARACTER_11,
              "ougs." + quote(groupSet.getUid()),
              groupSet.getCreated()));
    }

    for (OrganisationUnitLevel level : levels) {
      String column = PREFIX_ORGUNITLEVEL + level.getLevel();
      columns.add(
          new AnalyticsTableColumn(column, CHARACTER_11, "ous." + column, level.getCreated()));
    }

    for (Category category : attributeCategories) {
      columns.add(
          new AnalyticsTableColumn(
              category.getUid(),
              CHARACTER_11,
              "acs." + quote(category.getUid()),
              category.getCreated()));
    }

    for (PeriodType periodType : PeriodType.getAvailablePeriodTypes()) {
      String column = periodType.getName().toLowerCase();
      columns.add(new AnalyticsTableColumn(column, TEXT, "ps." + column));
    }

    columns.addAll(FIXED_COLS);
    columns.add(new AnalyticsTableColumn("value", DATE, NULL, FACT, "vrs.created as value"));

    return filterDimensionColumns(columns);
  }
}
