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
package org.hisp.dhis.eventvisualization;

import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.join;
import static org.hisp.dhis.common.AnalyticsType.EVENT;
import static org.hisp.dhis.common.DimensionalObjectUtils.TITLE_ITEM_SEP;
import static org.hisp.dhis.common.DimensionalObjectUtils.getPrettyFilter;
import static org.hisp.dhis.common.DimensionalObjectUtils.setDimensionItemsForFilters;
import static org.hisp.dhis.common.DxfNamespaces.DXF_2_0;
import static org.hisp.dhis.common.IdentifiableObjectUtils.join;
import static org.hisp.dhis.eventvisualization.Attribute.COLUMN;
import static org.hisp.dhis.eventvisualization.Attribute.FILTER;
import static org.hisp.dhis.eventvisualization.Attribute.ROW;
import static org.hisp.dhis.schema.PropertyType.CONSTANT;
import static org.hisp.dhis.schema.annotation.Property.Value.TRUE;
import static org.hisp.dhis.util.ObjectUtils.firstNonNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.hisp.dhis.analytics.EventDataType;
import org.hisp.dhis.analytics.EventOutputType;
import org.hisp.dhis.analytics.Sorting;
import org.hisp.dhis.common.AnalyticsType;
import org.hisp.dhis.common.BaseAnalyticalObject;
import org.hisp.dhis.common.BaseDimensionalItemObject;
import org.hisp.dhis.common.BaseIdentifiableObject;
import org.hisp.dhis.common.DimensionalItemObject;
import org.hisp.dhis.common.DimensionalObject;
import org.hisp.dhis.common.DisplayDensity;
import org.hisp.dhis.common.DxfNamespaces;
import org.hisp.dhis.common.EventAnalyticalObject;
import org.hisp.dhis.common.FontSize;
import org.hisp.dhis.common.Grid;
import org.hisp.dhis.common.MetadataObject;
import org.hisp.dhis.common.RegressionType;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.event.EventStatus;
import org.hisp.dhis.i18n.I18nFormat;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.program.EnrollmentStatus;
import org.hisp.dhis.program.Program;
import org.hisp.dhis.program.ProgramStage;
import org.hisp.dhis.schema.annotation.Property;
import org.hisp.dhis.schema.annotation.PropertyRange;
import org.hisp.dhis.trackedentity.TrackedEntityAttribute;
import org.hisp.dhis.trackedentity.TrackedEntityType;
import org.hisp.dhis.translation.Translatable;
import org.hisp.dhis.user.User;

@JacksonXmlRootElement(localName = "eventVisualization", namespace = DXF_2_0)
public class EventVisualization extends BaseAnalyticalObject
    implements MetadataObject, EventAnalyticalObject {

  protected EventVisualizationType type;

  // -------------------------------------------------------------------------
  // Dimensional properties
  // -------------------------------------------------------------------------

  /** Stores the sorting state in the current object. */
  private List<Sorting> sorting = new ArrayList<>();

  // -------------------------------------------------------------------------
  // Transient properties
  // -------------------------------------------------------------------------

  protected transient List<Period> relativePeriods = new ArrayList<>();

  /** Program. Required. */
  private Program program;

  /** Program stage. */
  private ProgramStage programStage;

  /** Tracked entity type associated. */
  private TrackedEntityType trackedEntityType;

  /** Data element value dimension. */
  private DataElement dataElementValueDimension;

  /** Attribute value dimension. */
  private TrackedEntityAttribute attributeValueDimension;

  /** Dimensions to crosstabulate / use as columns. */
  private List<String> columnDimensions = new ArrayList<>();

  /** Programs used in dimensions (columns, rows, filters) */
  private List<Program> programDimensions = new ArrayList<>();

  /** Dimensions to use as rows. */
  private List<String> rowDimensions = new ArrayList<>();

  /** The non-typed dimensions for this event visualization. */
  private List<SimpleDimension> simpleDimensions = new ArrayList<>();

  /** The list of existing event repetitions in this event visualization. */
  private List<EventRepetition> eventRepetitions = new ArrayList<>();

  /** Indicates output type. */
  private EventOutputType outputType;

  /** Indicates whether to collapse all data dimensions into a single dimension. */
  private boolean collapseDataDimensions;

  /** Indicates whether to hide n/a data. */
  private boolean hideNaData;

  /**
   * Indicates whether this is a legacy row (EventChart or EventReport).
   *
   * <p>- EventVisualizations created through the new App will never be legacy.
   *
   * <p>- Legacy EventVisualizations updated through the new App will always become non-legacy.
   */
  private boolean legacy;

  /** The enrollment status. */
  private EnrollmentStatus enrollmentStatus;

  /** The event status. */
  private EventStatus eventStatus;

  // -------------------------------------------------------------------------
  // Analytical properties
  // -------------------------------------------------------------------------

  /** Value dimension. */
  private transient DimensionalItemObject value;

  private EventDataType dataType;

  public EventVisualization() {}

  public EventVisualization(String name) {
    super.name = name;
  }

  @Override
  public void init(
      User user,
      Date date,
      OrganisationUnit organisationUnit,
      List<OrganisationUnit> organisationUnitsAtLevel,
      List<OrganisationUnit> organisationUnitsInGroups,
      I18nFormat format) {
    this.relativeUser = user;
    this.format = format;
  }

  public boolean isTargetLine() {
    return targetLineValue != null;
  }

  public boolean isBaseLine() {
    return baseLineValue != null;
  }

  public String generateTitle() {
    List<String> titleItems = new ArrayList<>();

    for (String filter : filterDimensions) {
      DimensionalObject object =
          getDimensionalObject(
              filter,
              relativePeriodDate,
              relativeUser,
              true,
              organisationUnitsAtLevel,
              organisationUnitsInGroups,
              format);

      if (object != null) {
        String item = join(object.getItems());
        String prettyFilter = getPrettyFilter(object.getFilter());

        if (item != null) {
          titleItems.add(item);
        }

        if (prettyFilter != null) {
          titleItems.add(prettyFilter);
        }
      }
    }

    return join(titleItems, TITLE_ITEM_SEP);
  }

  @Override
  protected void clearTransientStateProperties() {
    format = null;
    relativePeriods = new ArrayList<>();
    relativeUser = null;
    organisationUnitsAtLevel = new ArrayList<>();
    organisationUnitsInGroups = new ArrayList<>();
    dataItemGrid = null;
    value = null;
  }

  public boolean isRegression() {
    return regressionType == null || RegressionType.NONE != regressionType;
  }

  // -------------------------------------------------------------------------
  // Getters and setters for transient properties
  // -------------------------------------------------------------------------

  @JsonIgnore
  public void setFormat(I18nFormat format) {
    this.format = format;
  }

  @JsonIgnore
  public List<Period> getRelativePeriods() {
    return relativePeriods;
  }

  @JsonIgnore
  public void setRelativePeriods(List<Period> relativePeriods) {
    this.relativePeriods = relativePeriods;
  }

  @JsonIgnore
  public void setDataItemGrid(Grid dataItemGrid) {
    this.dataItemGrid = dataItemGrid;
  }

  // -------------------------------------------------------------------------
  // Getters and setters
  // -------------------------------------------------------------------------

  @JsonProperty
  @JacksonXmlProperty(namespace = DXF_2_0)
  public String getDomainAxisLabel() {
    return domainAxisLabel;
  }

  @JsonProperty
  @JacksonXmlProperty(namespace = DXF_2_0)
  @Translatable(propertyName = "domainAxisLabel")
  public String getDisplayDomainAxisLabel() {
    return getTranslation("domainAxisLabel", getDomainAxisLabel());
  }

  @JsonProperty
  @JacksonXmlProperty(namespace = DXF_2_0)
  public String getRangeAxisLabel() {
    return rangeAxisLabel;
  }

  @JsonProperty
  @JacksonXmlProperty(namespace = DXF_2_0)
  @Translatable(propertyName = "rangeAxisLabel")
  public String getDisplayRangeAxisLabel() {
    return getTranslation("rangeAxisLabel", getRangeAxisLabel());
  }

  @JsonProperty
  @JacksonXmlProperty(namespace = DXF_2_0)
  @Property(value = CONSTANT, required = TRUE)
  public EventVisualizationType getType() {
    return type;
  }

  public void setType(EventVisualizationType type) {
    this.type = type;
  }

  @JsonProperty
  @JacksonXmlProperty(namespace = DXF_2_0)
  public Double getTargetLineValue() {
    return targetLineValue;
  }

  @JsonProperty
  @JacksonXmlProperty(namespace = DXF_2_0)
  public String getTargetLineLabel() {
    return targetLineLabel;
  }

  @JsonProperty
  @JacksonXmlProperty(namespace = DXF_2_0)
  public Double getBaseLineValue() {
    return baseLineValue;
  }

  @JsonProperty
  @JacksonXmlProperty(namespace = DXF_2_0)
  public String getBaseLineLabel() {
    return baseLineLabel;
  }

  @JsonProperty
  @JacksonXmlProperty(namespace = DXF_2_0)
  public Double getRangeAxisMaxValue() {
    return rangeAxisMaxValue;
  }

  @JsonProperty
  @JacksonXmlProperty(namespace = DXF_2_0)
  @PropertyRange(min = Integer.MIN_VALUE)
  public Double getRangeAxisMinValue() {
    return rangeAxisMinValue;
  }

  @JsonProperty
  @JacksonXmlProperty(namespace = DXF_2_0)
  public Integer getRangeAxisSteps() {
    return rangeAxisSteps;
  }

  @JsonProperty
  @JacksonXmlProperty(namespace = DXF_2_0)
  public Integer getRangeAxisDecimals() {
    return rangeAxisDecimals;
  }

  @JsonProperty("sorting")
  @JacksonXmlElementWrapper(localName = "sorting", namespace = DXF_2_0)
  @JacksonXmlProperty(localName = "sortingItem", namespace = DXF_2_0)
  public List<Sorting> getSorting() {
    return sorting;
  }

  public void setSorting(List<Sorting> sorting) {
    if (sorting != null) {
      this.sorting = sorting.stream().distinct().collect(toList());
    }
  }

  // -------------------------------------------------------------------------
  // AnalyticalObject
  // -------------------------------------------------------------------------

  /**
   * Some EventVisualization's may not have columnDimensions.
   *
   * <p>PIE, GAUGE and others do not have rowDimensions.
   */
  @Override
  public void populateAnalyticalProperties() {
    super.populateDimensions(columnDimensions, columns, COLUMN, this);
    super.populateDimensions(rowDimensions, rows, ROW, this);
    super.populateDimensions(filterDimensions, filters, FILTER, this);

    value = firstNonNull(dataElementValueDimension, attributeValueDimension);
  }

  public void associateSimpleDimensions() {
    new SimpleDimensionHandler(this).associateDimensions();
  }

  public List<DimensionalItemObject> series() {
    String series = columnDimensions.get(0);
    return getItems(series);
  }

  public List<DimensionalItemObject> category() {
    String category = rowDimensions.get(0);
    return getItems(category);
  }

  private List<DimensionalItemObject> getItems(String dimension) {
    DimensionalObject object =
        getDimensionalObject(
            dimension,
            relativePeriodDate,
            relativeUser,
            true,
            organisationUnitsAtLevel,
            organisationUnitsInGroups,
            format);

    setDimensionItemsForFilters(object, dataItemGrid, true);

    return object != null ? object.getItems() : null;
  }

  public boolean isMultiProgram() {
    return trackedEntityType != null;
  }

  public AnalyticsType getAnalyticsType() {
    return EVENT;
  }

  // -------------------------------------------------------------------------
  // Getters and setters properties
  // -------------------------------------------------------------------------

  @Override
  @JsonProperty
  @JsonSerialize(as = BaseIdentifiableObject.class)
  @JacksonXmlProperty(namespace = DXF_2_0)
  public Program getProgram() {
    return program;
  }

  public void setProgram(Program program) {
    this.program = program;
  }

  @Override
  @JsonProperty
  @JsonSerialize(as = BaseIdentifiableObject.class)
  @JacksonXmlProperty(namespace = DXF_2_0)
  public ProgramStage getProgramStage() {
    return programStage;
  }

  public void setProgramStage(ProgramStage programStage) {
    this.programStage = programStage;
  }

  @JsonProperty
  @JsonSerialize(as = BaseIdentifiableObject.class)
  @JacksonXmlProperty(namespace = DXF_2_0)
  public TrackedEntityType getTrackedEntityType() {
    return trackedEntityType;
  }

  public void setTrackedEntityType(TrackedEntityType trackedEntityType) {
    this.trackedEntityType = trackedEntityType;
  }

  @JsonProperty
  @JsonSerialize(as = BaseIdentifiableObject.class)
  @JacksonXmlProperty(namespace = DXF_2_0)
  public DataElement getDataElementValueDimension() {
    return dataElementValueDimension;
  }

  @Override
  public void setDataElementValueDimension(DataElement dataElementValueDimension) {
    this.dataElementValueDimension = dataElementValueDimension;
  }

  @JsonProperty
  @JsonSerialize(as = BaseIdentifiableObject.class)
  @JacksonXmlProperty(namespace = DXF_2_0)
  public TrackedEntityAttribute getAttributeValueDimension() {
    return attributeValueDimension;
  }

  @Override
  public void setAttributeValueDimension(TrackedEntityAttribute attributeValueDimension) {
    this.attributeValueDimension = attributeValueDimension;
  }

  @JsonProperty
  @JacksonXmlElementWrapper(localName = "columnDimensions", namespace = DXF_2_0)
  @JacksonXmlProperty(localName = "columnDimension", namespace = DXF_2_0)
  public List<String> getColumnDimensions() {
    return columnDimensions;
  }

  public void setColumnDimensions(List<String> columnDimensions) {
    this.columnDimensions = columnDimensions;
  }

  @JsonProperty
  @JacksonXmlElementWrapper(localName = "programDimensions", namespace = DXF_2_0)
  @JacksonXmlProperty(localName = "programDimension", namespace = DXF_2_0)
  public List<Program> getProgramDimensions() {
    return programDimensions;
  }

  public void setProgramDimensions(List<Program> programDimensions) {
    this.programDimensions = programDimensions;
  }

  @JsonProperty
  @JacksonXmlElementWrapper(localName = "rowDimensions", namespace = DXF_2_0)
  @JacksonXmlProperty(localName = "rowDimension", namespace = DXF_2_0)
  public List<String> getRowDimensions() {
    return rowDimensions;
  }

  public void setRowDimensions(List<String> rowDimensions) {
    this.rowDimensions = rowDimensions;
  }

  @Override
  @JsonProperty
  @JacksonXmlProperty(namespace = DXF_2_0)
  public List<SimpleDimension> getSimpleDimensions() {
    return simpleDimensions;
  }

  public void setSimpleDimensions(List<SimpleDimension> simpleDimensions) {
    this.simpleDimensions = simpleDimensions;
  }

  @JsonProperty("repetitions")
  @JacksonXmlElementWrapper(localName = "repetitions", namespace = DxfNamespaces.DXF_2_0)
  @JacksonXmlProperty(localName = "repetition", namespace = DxfNamespaces.DXF_2_0)
  public List<EventRepetition> getEventRepetitions() {
    return eventRepetitions;
  }

  public void setEventRepetitions(List<EventRepetition> eventRepetitions) {
    this.eventRepetitions = eventRepetitions;
  }

  @Override
  @JsonProperty
  @JacksonXmlProperty(namespace = DXF_2_0)
  public EventOutputType getOutputType() {
    return outputType;
  }

  public void setOutputType(EventOutputType outputType) {
    this.outputType = outputType;
  }

  @JsonProperty
  @JacksonXmlProperty(namespace = DXF_2_0)
  public boolean isCollapseDataDimensions() {
    return collapseDataDimensions;
  }

  public void setCollapseDataDimensions(boolean collapseDataDimensions) {
    this.collapseDataDimensions = collapseDataDimensions;
  }

  @JsonProperty
  @JacksonXmlProperty(namespace = DXF_2_0)
  public EnrollmentStatus getProgramStatus() {
    return enrollmentStatus;
  }

  public void setProgramStatus(EnrollmentStatus enrollmentStatus) {
    this.enrollmentStatus = enrollmentStatus;
  }

  @JsonProperty
  @JacksonXmlProperty(namespace = DXF_2_0)
  public EventStatus getEventStatus() {
    return eventStatus;
  }

  public void setEventStatus(EventStatus eventStatus) {
    this.eventStatus = eventStatus;
  }

  @JsonProperty
  @JacksonXmlProperty(namespace = DXF_2_0)
  public boolean isHideNaData() {
    return hideNaData;
  }

  public void setHideNaData(boolean hideNaData) {
    this.hideNaData = hideNaData;
  }

  // -------------------------------------------------------------------------
  // Analytical properties
  // -------------------------------------------------------------------------

  @Override
  @JsonProperty
  @JsonDeserialize(as = BaseDimensionalItemObject.class)
  @JacksonXmlProperty(namespace = DXF_2_0)
  public DimensionalItemObject getValue() {
    return value;
  }

  public void setValue(DimensionalItemObject value) {
    this.value = value;
  }

  /**
   * This attribute and its accessors were replaced by "type", but will remain here for
   * backward-compatibility with EventReport.
   *
   * @return the current {@link EventDataType}.
   */
  @JsonProperty
  @JacksonXmlProperty(namespace = DXF_2_0)
  public EventDataType getDataType() {
    return dataType;
  }

  public void setDataType(EventDataType dataType) {
    this.dataType = dataType;
  }

  @JsonProperty
  @JacksonXmlProperty(namespace = DXF_2_0)
  public DisplayDensity getDisplayDensity() {
    return displayDensity;
  }

  @JsonProperty
  @JacksonXmlProperty(namespace = DXF_2_0)
  public FontSize getFontSize() {
    return fontSize;
  }

  @JsonProperty
  @JacksonXmlProperty(namespace = DXF_2_0)
  public boolean isLegacy() {
    return legacy;
  }

  public void setLegacy(boolean legacy) {
    this.legacy = legacy;
  }
}
