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
package org.hisp.dhis.dataapproval;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.hisp.dhis.category.CategoryCombo;
import org.hisp.dhis.common.BaseIdentifiableObject;
import org.hisp.dhis.common.DxfNamespaces;
import org.hisp.dhis.common.IdentifiableObject;
import org.hisp.dhis.common.MetadataObject;
import org.hisp.dhis.common.adapter.JacksonPeriodTypeDeserializer;
import org.hisp.dhis.common.adapter.JacksonPeriodTypeSerializer;
import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.period.PeriodType;
import org.hisp.dhis.schema.PropertyType;
import org.hisp.dhis.schema.annotation.Property;

/**
 * Identifies types of data to be approved, and the set of approval levels by which it is approved.
 *
 * <p>The types of data to be approved are identified by data sets (for aggregate data) and or
 * programs (for event/tracker data) that are related to a workflow.
 *
 * @author Jim Grace
 */
@JacksonXmlRootElement(localName = "dataApprovalWorkflow", namespace = DxfNamespaces.DXF_2_0)
public class DataApprovalWorkflow extends BaseIdentifiableObject implements MetadataObject {
  /** The period type for approving data with this workflow. */
  private PeriodType periodType;

  /** The category combination for approving data with this workflow. */
  private CategoryCombo categoryCombo;

  /** The data approval levels used in this workflow. */
  private Set<DataApprovalLevel> levels = new HashSet<>();

  /** The data sets part of this workflow. Inverse side. */
  private Set<DataSet> dataSets = new HashSet<>();

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  public DataApprovalWorkflow() {}

  public DataApprovalWorkflow(String name) {
    this.name = name;
  }

  public DataApprovalWorkflow(String name, PeriodType periodType, Set<DataApprovalLevel> levels) {
    this.name = name;
    this.periodType = periodType;
    this.levels = levels;
  }

  public DataApprovalWorkflow(
      String name,
      PeriodType periodType,
      CategoryCombo categoryCombo,
      Set<DataApprovalLevel> levels) {
    this.name = name;
    this.periodType = periodType;
    this.categoryCombo = categoryCombo;
    this.levels = levels;
  }

  // -------------------------------------------------------------------------
  // Logic
  // -------------------------------------------------------------------------

  /**
   * Returns the DataApprovalLevels for this workflow sorted by level.
   *
   * @return DataApprovalLevels sorted by level
   */
  public List<DataApprovalLevel> getSortedLevels() {
    Comparator<DataApprovalLevel> orderByLevelNumber =
        (DataApprovalLevel dal1, DataApprovalLevel dal2) -> dal1.getLevel() - dal2.getLevel();

    List<DataApprovalLevel> sortedLevels = new ArrayList<>(levels);

    Collections.sort(sortedLevels, orderByLevelNumber);

    return sortedLevels;
  }

  /**
   * Returns the SQL fragment to extend the category option end dates for this data approval
   * workflow. This will be based on the dataset belonging to this workflow with the longest
   * category option end date extension (if any).
   *
   * @return the SQL to extend the category option end dates, or an empty string if there is no such
   *     extension.
   */
  public String getSqlCoEndDateExtension() {
    String extension = "";
    int maxDays = 0;

    for (DataSet ds : getDataSets()) {
      if (ds.getOpenPeriodsAfterCoEndDate() != 0) {
        PeriodType pt = ds.getPeriodType();

        int periods = ds.getOpenPeriodsAfterCoEndDate();

        if (periods * pt.getFrequencyOrder() > maxDays) {
          maxDays = periods * pt.getFrequencyOrder();

          extension = " + " + periods + " * INTERVAL '+ " + pt.getSqlInterval() + "'";
        }
      }
    }

    return extension;
  }

  // -------------------------------------------------------------------------
  // Getters and Setters
  // -------------------------------------------------------------------------

  @JsonProperty
  @JsonSerialize(using = JacksonPeriodTypeSerializer.class)
  @JsonDeserialize(using = JacksonPeriodTypeDeserializer.class)
  @JacksonXmlProperty(namespace = DxfNamespaces.DXF_2_0)
  @Property(PropertyType.TEXT)
  public PeriodType getPeriodType() {
    return periodType;
  }

  public void setPeriodType(PeriodType periodType) {
    this.periodType = periodType;
  }

  @JsonProperty
  @JsonSerialize(as = IdentifiableObject.class)
  @JacksonXmlProperty(namespace = DxfNamespaces.DXF_2_0)
  public CategoryCombo getCategoryCombo() {
    return categoryCombo;
  }

  public void setCategoryCombo(CategoryCombo categoryCombo) {
    this.categoryCombo = categoryCombo;
  }

  @JsonProperty("dataApprovalLevels")
  @JsonSerialize(contentAs = BaseIdentifiableObject.class)
  @JacksonXmlElementWrapper(localName = "dataApprovalLevels", namespace = DxfNamespaces.DXF_2_0)
  @JacksonXmlProperty(localName = "dataApprovalLevel", namespace = DxfNamespaces.DXF_2_0)
  public Set<DataApprovalLevel> getLevels() {
    return levels;
  }

  public void setLevels(Set<DataApprovalLevel> levels) {
    this.levels = levels;
  }

  @JsonProperty
  @JsonSerialize(contentAs = BaseIdentifiableObject.class)
  @JacksonXmlElementWrapper(localName = "dataSets", namespace = DxfNamespaces.DXF_2_0)
  @JacksonXmlProperty(localName = "dataSet", namespace = DxfNamespaces.DXF_2_0)
  public Set<DataSet> getDataSets() {
    return dataSets;
  }

  public void setDataSets(Set<DataSet> dataSets) {
    this.dataSets = dataSets;
  }
}
