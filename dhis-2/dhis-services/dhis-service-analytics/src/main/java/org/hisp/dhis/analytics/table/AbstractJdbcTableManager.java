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

import static org.hisp.dhis.analytics.table.util.PartitionUtils.getEndDate;
import static org.hisp.dhis.analytics.table.util.PartitionUtils.getStartDate;
import static org.hisp.dhis.db.model.DataType.CHARACTER_11;
import static org.hisp.dhis.db.model.DataType.TEXT;
import static org.hisp.dhis.util.DateUtils.toLongDate;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hisp.dhis.analytics.AnalyticsTableHook;
import org.hisp.dhis.analytics.AnalyticsTableHookService;
import org.hisp.dhis.analytics.AnalyticsTableManager;
import org.hisp.dhis.analytics.AnalyticsTablePhase;
import org.hisp.dhis.analytics.AnalyticsTableType;
import org.hisp.dhis.analytics.AnalyticsTableUpdateParams;
import org.hisp.dhis.analytics.partition.PartitionManager;
import org.hisp.dhis.analytics.table.model.AnalyticsTable;
import org.hisp.dhis.analytics.table.model.AnalyticsTableColumn;
import org.hisp.dhis.analytics.table.model.AnalyticsTablePartition;
import org.hisp.dhis.analytics.table.setting.AnalyticsTableSettings;
import org.hisp.dhis.calendar.Calendar;
import org.hisp.dhis.category.CategoryService;
import org.hisp.dhis.common.IdentifiableObjectManager;
import org.hisp.dhis.common.ValueType;
import org.hisp.dhis.commons.collection.ListUtils;
import org.hisp.dhis.commons.collection.UniqueArrayList;
import org.hisp.dhis.commons.timer.SystemTimer;
import org.hisp.dhis.commons.timer.Timer;
import org.hisp.dhis.commons.util.TextUtils;
import org.hisp.dhis.dataapproval.DataApprovalLevelService;
import org.hisp.dhis.db.model.Collation;
import org.hisp.dhis.db.model.Index;
import org.hisp.dhis.db.model.Logged;
import org.hisp.dhis.db.model.Table;
import org.hisp.dhis.db.sql.SqlBuilder;
import org.hisp.dhis.organisationunit.OrganisationUnitGroupSet;
import org.hisp.dhis.organisationunit.OrganisationUnitLevel;
import org.hisp.dhis.organisationunit.OrganisationUnitService;
import org.hisp.dhis.period.PeriodDataProvider;
import org.hisp.dhis.period.PeriodType;
import org.hisp.dhis.resourcetable.ResourceTableService;
import org.hisp.dhis.setting.SettingKey;
import org.hisp.dhis.setting.SystemSettingManager;
import org.hisp.dhis.system.database.DatabaseInfoProvider;
import org.hisp.dhis.util.DateUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.Assert;

/**
 * @author Lars Helge Overland
 */
@Slf4j
@RequiredArgsConstructor
public abstract class AbstractJdbcTableManager implements AnalyticsTableManager {
  /**
   * Matches the following patterns:
   *
   * <ul>
   *   <li>1999-12-12
   *   <li>1999-12-12T
   *   <li>1999-12-12T10:10:10
   *   <li>1999-10-10 10:10:10
   *   <li>1999-10-10 10:10
   *   <li>2021-12-14T11:45:00.000Z
   *   <li>2021-12-14T11:45:00.000
   * </ul>
   */
  protected static final String DATE_REGEXP =
      "^\\d{4}-\\d{2}-\\d{2}(\\s|T)?((\\d{2}:)(\\d{2}:)?(\\d{2}))?(|.(\\d{3})|.(\\d{3})Z)?$";

  protected static final Set<ValueType> NO_INDEX_VAL_TYPES =
      Set.of(ValueType.TEXT, ValueType.LONG_TEXT);

  protected static final String PREFIX_ORGUNITLEVEL = "uidlevel";

  protected static final String PREFIX_ORGUNITNAMELEVEL = "namelevel";

  protected final IdentifiableObjectManager idObjectManager;

  protected final OrganisationUnitService organisationUnitService;

  protected final CategoryService categoryService;

  protected final SystemSettingManager systemSettingManager;

  protected final DataApprovalLevelService dataApprovalLevelService;

  protected final ResourceTableService resourceTableService;

  protected final AnalyticsTableHookService tableHookService;

  protected final PartitionManager partitionManager;

  protected final DatabaseInfoProvider databaseInfoProvider;

  protected final JdbcTemplate jdbcTemplate;

  protected final AnalyticsTableSettings analyticsTableSettings;

  protected final PeriodDataProvider periodDataProvider;

  protected final SqlBuilder sqlBuilder;

  protected Boolean spatialSupport;

  protected boolean isSpatialSupport() {
    if (spatialSupport == null)
      spatialSupport = databaseInfoProvider.getDatabaseInfo().isSpatialSupport();
    return spatialSupport;
  }

  /**
   * Encapsulates the SQL logic to get the correct date column based on the event(program stage
   * instance) status. If new statuses need to be loaded into the analytics events tables, they have
   * to be supported/added into this logic.
   */
  protected final String eventDateExpression =
      "CASE WHEN 'SCHEDULE' = psi.status THEN psi.scheduleddate ELSE psi.occurreddate END";

  // -------------------------------------------------------------------------
  // Implementation
  // -------------------------------------------------------------------------

  @Override
  public Set<String> getExistingDatabaseTables() {
    return partitionManager.getAnalyticsPartitions(getAnalyticsTableType());
  }

  /** Override in order to perform work before tables are being generated. */
  @Override
  public void preCreateTables(AnalyticsTableUpdateParams params) {}

  /**
   * Removes data which was updated or deleted between the last successful analytics table update
   * and the start of this analytics table update process, excluding data which was created during
   * that time span.
   *
   * <p>Override in order to remove updated and deleted data for "latest" partition update.
   */
  @Override
  public void removeUpdatedData(List<AnalyticsTable> tables) {}

  @Override
  public void createTable(AnalyticsTable table) {
    createAnalyticsTable(table);
    createAnalyticsTablePartitions(table);

    if (analyticsTableSettings.isCitusExtensionEnabled()) {
      createDistributedCitusTable(table);
    }
  }

  /**
   * Create a distributed Citus table.
   *
   * @param table the {@link AnalyticsTable}.
   */
  private void createDistributedCitusTable(AnalyticsTable table) {
    if (!table.isTableTypeDistributed()) {
      log.warn(
          "No distribution column defined for table "
              + table.getMainName()
              + " so it won't be distributed");
      return;
    }

    String tableName = table.getName();
    String distributionColumn = table.getTableType().getDistributionColumn();

    try {
      jdbcTemplate.query(
          "select create_distributed_table( :1, :2 )",
          ps -> {
            ps.setString(1, tableName);
            ps.setString(2, distributionColumn);
          },
          rs -> {});
      log.info("Successfully distributed table " + tableName + " on column " + distributionColumn);
    } catch (Exception e) {
      log.warn(
          "Failed to distribute table " + table.getName() + " on column " + distributionColumn, e);
    }
  }

  /**
   * Drops and creates the given analytics table or table partition.
   *
   * @param table the {@link Table}.
   */
  private void createAnalyticsTable(Table table) {
    log.info("Creating table: '{}', columns: '{}'", table.getName(), table.getColumns().size());

    String sql = sqlBuilder.createTable(table);

    log.debug("Create table SQL: '{}'", sql);

    jdbcTemplate.execute(sql);
  }

  /**
   * Creates the table partitions for the given analytics table.
   *
   * @param table the {@link AnalyticsTable}.
   */
  private void createAnalyticsTablePartitions(AnalyticsTable table) {
    for (AnalyticsTablePartition partition : table.getTablePartitions()) {
      createAnalyticsTable(partition);
    }
  }

  @Override
  public void createIndex(Index index) {
    log.debug("Creating index: '{}'", index.getName());

    String sql = sqlBuilder.createIndex(index);

    log.debug("Create index SQL: '{}'", sql);

    jdbcTemplate.execute(sql);
  }

  @Override
  public void swapTable(AnalyticsTableUpdateParams params, AnalyticsTable table) {
    boolean tableExists = tableExists(table.getMainName());
    boolean skipMasterTable =
        params.isPartialUpdate() && tableExists && table.getTableType().isLatestPartition();

    log.info(
        "Swapping table, master table exists: '{}', skip master table: '{}'",
        tableExists,
        skipMasterTable);
    List<Table> swappedPartitions = new UniqueArrayList<>();
    table.getTablePartitions().stream()
        .forEach(p -> swappedPartitions.add(swapTable(p, p.getMainName())));

    if (!skipMasterTable) {
      swapTable(table, table.getMainName());
    } else {
      swappedPartitions.forEach(
          partition -> swapInheritance(partition, table.getName(), table.getMainName()));
      dropTable(table);
    }
  }

  @Override
  public void dropTable(Table table) {
    dropTable(table.getName());
  }

  @Override
  public void dropTable(String name) {
    executeSilently(sqlBuilder.dropTableIfExistsCascade(name));
  }

  @Override
  public void analyzeTable(String name) {
    executeSilently(sqlBuilder.analyzeTable(name));
  }

  @Override
  public void vacuumTable(Table table) {
    executeSilently(sqlBuilder.vacuumTable(table));
  }

  @Override
  public void analyzeTable(Table table) {
    executeSilently(sqlBuilder.analyzeTable(table));
  }

  @Override
  public int invokeAnalyticsTableSqlHooks() {
    AnalyticsTableType type = getAnalyticsTableType();
    List<AnalyticsTableHook> hooks =
        tableHookService.getByPhaseAndAnalyticsTableType(
            AnalyticsTablePhase.ANALYTICS_TABLE_POPULATED, type);
    tableHookService.executeAnalyticsTableSqlHooks(hooks);
    return hooks.size();
  }

  /**
   * Swaps a database table, meaning drops the main table and renames the staging table to become
   * the main table.
   *
   * @param stagingTable the staging table.
   * @param mainTableName the main table name.
   */
  private Table swapTable(Table stagingTable, String mainTableName) {
    executeSilently(sqlBuilder.swapTable(stagingTable, mainTableName));
    return stagingTable.swapFromStaging();
  }

  /**
   * Updates table inheritance of a table partition from the staging master table to the main master
   * table.
   *
   * @param stagingMasterName the staging master table name.
   * @param mainMasterName the main master table name.
   */
  private void swapInheritance(Table partition, String stagingMasterName, String mainMasterName) {
    executeSilently(sqlBuilder.swapParentTable(partition, stagingMasterName, mainMasterName));
  }

  /**
   * Indicates if a table with the given name exists.
   *
   * @param name the table name.
   * @return true if a table with the given name exists.
   */
  private boolean tableExists(String name) {
    return !jdbcTemplate.queryForList(sqlBuilder.tableExists(name)).isEmpty();
  }

  // -------------------------------------------------------------------------
  // Abstract methods
  // -------------------------------------------------------------------------

  /**
   * Returns a list of table partition checks (constraints) for the given year and end date.
   *
   * @param year the year.
   * @param endDate the end date.
   * @return the list of table partition checks.
   */
  protected abstract List<String> getPartitionChecks(Integer year, Date endDate);

  // -------------------------------------------------------------------------
  // Protected supportive methods
  // -------------------------------------------------------------------------

  /** Returns the analytics table name. */
  protected String getTableName() {
    return getAnalyticsTableType().getTableName();
  }

  /**
   * Creates a {@link AnalyticsTable} with partitions based on a list of years with data.
   *
   * @param params the {@link AnalyticsTableUpdateParams}.
   * @param dataYears the list of years with data.
   * @param columns the list of {@link AnalyticsTableColumn}.
   */
  protected AnalyticsTable getRegularAnalyticsTable(
      AnalyticsTableUpdateParams params,
      List<Integer> dataYears,
      List<AnalyticsTableColumn> columns) {
    Calendar calendar = PeriodType.getCalendar();
    List<Integer> years = ListUtils.mutableCopy(dataYears);
    Logged logged = analyticsTableSettings.getTableLogged();

    Collections.sort(years);

    AnalyticsTable table = new AnalyticsTable(getAnalyticsTableType(), columns, logged);

    for (Integer year : years) {
      List<String> checks = getPartitionChecks(year, getEndDate(calendar, year));

      table.addTablePartition(
          checks, year, getStartDate(calendar, year), getEndDate(calendar, year));
    }

    return table;
  }

  /**
   * Creates a {@link AnalyticsTable} with a partition for the "latest" data. The start date of the
   * partition is the time of the last successful full analytics table update. The end date of the
   * partition is the start time of this analytics table update process.
   *
   * @param params the {@link AnalyticsTableUpdateParams}.
   * @param columns the list of {@link AnalyticsTableColumn}.
   */
  protected AnalyticsTable getLatestAnalyticsTable(
      AnalyticsTableUpdateParams params, List<AnalyticsTableColumn> columns) {
    Date lastFullTableUpdate =
        systemSettingManager.getDateSetting(SettingKey.LAST_SUCCESSFUL_ANALYTICS_TABLES_UPDATE);
    Date lastLatestPartitionUpdate =
        systemSettingManager.getDateSetting(
            SettingKey.LAST_SUCCESSFUL_LATEST_ANALYTICS_PARTITION_UPDATE);
    Date lastAnyTableUpdate = DateUtils.getLatest(lastLatestPartitionUpdate, lastFullTableUpdate);

    Assert.notNull(
        lastFullTableUpdate,
        "A full analytics table update must be run prior to a latest partition update");

    Logged logged = analyticsTableSettings.getTableLogged();
    Date endDate = params.getStartTime();
    boolean hasUpdatedData = hasUpdatedLatestData(lastAnyTableUpdate, endDate);

    AnalyticsTable table = new AnalyticsTable(getAnalyticsTableType(), columns, logged);

    if (hasUpdatedData) {
      table.addTablePartition(
          List.of(), AnalyticsTablePartition.LATEST_PARTITION, lastFullTableUpdate, endDate);
      log.info(
          "Added latest analytics partition with start: '{}' and end: '{}'",
          toLongDate(lastFullTableUpdate),
          toLongDate(endDate));
    } else {
      log.info(
          "No updated latest data found with start: '{}' and end: '{}'",
          toLongDate(lastAnyTableUpdate),
          toLongDate(endDate));
    }

    return table;
  }

  /**
   * Filters out analytics table columns which were created after the time of the last successful
   * resource table update. This so that the create table query does not refer to columns not
   * present in resource tables.
   *
   * @param columns the analytics table columns.
   * @return a list of {@link AnalyticsTableColumn}.
   */
  protected List<AnalyticsTableColumn> filterDimensionColumns(List<AnalyticsTableColumn> columns) {
    Date lastResourceTableUpdate =
        systemSettingManager.getDateSetting(SettingKey.LAST_SUCCESSFUL_RESOURCE_TABLES_UPDATE);

    if (lastResourceTableUpdate == null) {
      return columns;
    }

    return columns.stream()
        .filter(c -> c.getCreated() == null || c.getCreated().before(lastResourceTableUpdate))
        .collect(Collectors.toList());
  }

  /**
   * Executes the given SQL statement. Logs and times the operation.
   *
   * @param sql the SQL statement.
   * @param logMessage the custom log message to include in the log statement.
   */
  protected void invokeTimeAndLog(String sql, String logPattern, Object... arguments) {
    Timer timer = new SystemTimer().start();

    log.debug("Populate table SQL: '{}'", sql);

    jdbcTemplate.execute(sql);

    String logMessage = TextUtils.format(logPattern, arguments);

    log.info("{} in: {}", logMessage, timer.stop().toString());
  }

  /**
   * Collects all the {@link PeriodType} as a list of {@link AnalyticsTableColumn}.
   *
   * @param prefix the prefix to use for the column name
   * @return a List of {@link AnalyticsTableColumn}
   */
  protected List<AnalyticsTableColumn> getPeriodTypeColumns(String prefix) {
    return PeriodType.getAvailablePeriodTypes().stream()
        .map(
            pt -> {
              String name = pt.getName().toLowerCase();
              return new AnalyticsTableColumn(name, TEXT, prefix + "." + quote(name));
            })
        .toList();
  }

  /**
   * Collects all the {@link OrganisationUnitLevel} as a list of {@link AnalyticsTableColumn}.
   *
   * @return a List of {@link AnalyticsTableColumn}
   */
  protected List<AnalyticsTableColumn> getOrganisationUnitLevelColumns() {
    return organisationUnitService.getFilledOrganisationUnitLevels().stream()
        .map(
            level -> {
              String name = PREFIX_ORGUNITLEVEL + level.getLevel();
              return new AnalyticsTableColumn(
                  name, CHARACTER_11, "ous." + quote(name), level.getCreated());
            })
        .toList();
  }

  /**
   * Organisation unit name hierarchy delivery.
   *
   * @return a table column {@link AnalyticsTableColumn}
   */
  protected AnalyticsTableColumn getOrganisationUnitNameHierarchyColumn() {
    String columnExpression =
        "concat_ws(' / ',"
            + organisationUnitService.getFilledOrganisationUnitLevels().stream()
                .map(lv -> "ous." + PREFIX_ORGUNITNAMELEVEL + lv.getLevel())
                .collect(Collectors.joining(","))
            + ") as ounamehierarchy";
    return new AnalyticsTableColumn("ounamehierarchy", TEXT, Collation.C, columnExpression);
  }

  /**
   * Collects all the {@link OrganisationUnitGroupSet} as a list of {@link AnalyticsTableColumn}.
   *
   * @return a List of {@link AnalyticsTableColumn}
   */
  protected List<AnalyticsTableColumn> getOrganisationUnitGroupSetColumns() {
    return idObjectManager.getDataDimensionsNoAcl(OrganisationUnitGroupSet.class).stream()
        .map(
            ougs -> {
              String name = ougs.getUid();
              return new AnalyticsTableColumn(
                  name, CHARACTER_11, "ougs." + quote(name), ougs.getCreated());
            })
        .toList();
  }

  protected List<AnalyticsTableColumn> getAttributeCategoryOptionGroupSetColumns() {
    return categoryService.getAttributeCategoryOptionGroupSetsNoAcl().stream()
        .map(
            cogs -> {
              String name = cogs.getUid();
              return new AnalyticsTableColumn(
                  name, CHARACTER_11, "acs." + quote(name), cogs.getCreated());
            })
        .toList();
  }

  protected List<AnalyticsTableColumn> getAttributeCategoryColumns() {
    return categoryService.getAttributeDataDimensionCategoriesNoAcl().stream()
        .map(
            category -> {
              String name = category.getUid();
              return new AnalyticsTableColumn(
                  name, CHARACTER_11, "acs." + quote(name), category.getCreated());
            })
        .toList();
  }

  /**
   * Indicates whether the table with the given name is not empty, i.e. has at least one row.
   *
   * @param name the table name.
   * @return true if the table is not empty.
   */
  protected boolean tableIsNotEmpty(String name) {
    String sql = String.format("select 1 from %s limit 1;", sqlBuilder.quote(name));
    return jdbcTemplate.queryForRowSet(sql).next();
  }

  /**
   * Quotes the given relation.
   *
   * @param relation the relation to quote, e.g. a table or column name.
   * @return a double quoted relation.
   */
  protected String quote(String relation) {
    return sqlBuilder.quote(relation);
  }

  /**
   * Returns a quoted and comma delimited string.
   *
   * @param items the items to join.
   * @return a string representing the comma delimited and quoted item values.
   */
  protected String quotedCommaDelimitedString(Collection<String> items) {
    return sqlBuilder.singleQuotedCommaDelimited(items);
  }

  /**
   * Qualifies the given table name.
   *
   * @param name the table name.
   * @return a fully qualified and quoted table reference which specifies the catalog, database and
   *     table.
   */
  protected String qualify(String name) {
    return sqlBuilder.qualifyTable(name);
  }

  /**
   * Replaces variables in the given template string with the given variables to qualify and the
   * given map of variable keys and values.
   *
   * @param template the template string.
   * @param qualifyVariables the list of variables to qualify.
   * @param variables the map of variables and values.
   * @return a resolved string.
   */
  protected String replaceQualify(
      String template, List<String> qualifyVariables, Map<String, String> variables) {
    Map<String, String> map = new HashMap<>(variables);
    qualifyVariables.forEach(v -> map.put(v, qualify(v)));
    return TextUtils.replace(template, map);
  }

  // -------------------------------------------------------------------------
  // Private supportive methods
  // -------------------------------------------------------------------------

  /**
   * Executes a SQL statement silently without throwing any exceptions. Instead exceptions are
   * logged.
   *
   * @param sql the SQL statement to execute.
   */
  private void executeSilently(String sql) {
    try {
      jdbcTemplate.execute(sql);
    } catch (DataAccessException ex) {
      log.error(ex.getMessage());
    }
  }
}
