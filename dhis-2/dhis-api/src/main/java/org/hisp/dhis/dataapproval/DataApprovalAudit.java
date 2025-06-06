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
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import org.hisp.dhis.category.CategoryOptionCombo;
import org.hisp.dhis.common.DxfNamespaces;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.user.User;

/**
 * Records the approval of DataSet values for a given OrganisationUnit and Period.
 *
 * @author Jim Grace
 */
public class DataApprovalAudit implements Serializable {

  private static final long serialVersionUID = 4209187342531545619L;

  /** Identifies the data approval audit record (required). */
  private long id;

  /** The approval level for which this approval is defined. */
  private DataApprovalLevel level;

  /** The workflow for the values being approved (required). */
  private DataApprovalWorkflow workflow;

  /** The Period of the approval (required). */
  private Period period;

  /** The OrganisationUnit of the approval (required). */
  private OrganisationUnit organisationUnit;

  /** The attribute category option combo being approved (optional). */
  private CategoryOptionCombo attributeOptionCombo;

  /** Type of data approval action done. */
  private DataApprovalAction action;

  /** The Date (including time) when this approval was made (required). */
  private Date created;

  /** The User who made this approval (required). */
  private User creator;

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  public DataApprovalAudit() {}

  public DataApprovalAudit(DataApproval da, DataApprovalAction action) {
    this.id = da.getId();
    this.level = da.getDataApprovalLevel();
    this.workflow = da.getWorkflow();
    this.period = da.getPeriod();
    this.organisationUnit = da.getOrganisationUnit();
    this.attributeOptionCombo = da.getAttributeOptionCombo();
    this.action = action;
    this.created = da.getCreated();
    this.creator = da.getCreator();
  }

  // -------------------------------------------------------------------------
  // Getters and setters
  // -------------------------------------------------------------------------

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  @JsonProperty
  @JacksonXmlProperty(namespace = DxfNamespaces.DXF_2_0)
  public DataApprovalLevel getLevel() {
    return level;
  }

  public void setLevel(DataApprovalLevel level) {
    this.level = level;
  }

  @JsonProperty
  @JacksonXmlProperty(namespace = DxfNamespaces.DXF_2_0)
  public DataApprovalWorkflow getWorkflow() {
    return workflow;
  }

  public void setWorkflow(DataApprovalWorkflow workflow) {
    this.workflow = workflow;
  }

  @JsonProperty
  @JacksonXmlProperty(namespace = DxfNamespaces.DXF_2_0)
  public Period getPeriod() {
    return period;
  }

  public void setPeriod(Period period) {
    this.period = period;
  }

  @JsonProperty
  @JacksonXmlProperty(namespace = DxfNamespaces.DXF_2_0)
  public OrganisationUnit getOrganisationUnit() {
    return organisationUnit;
  }

  public void setOrganisationUnit(OrganisationUnit organisationUnit) {
    this.organisationUnit = organisationUnit;
  }

  @JsonProperty
  @JacksonXmlProperty(namespace = DxfNamespaces.DXF_2_0)
  public CategoryOptionCombo getAttributeOptionCombo() {
    return attributeOptionCombo;
  }

  public void setAttributeOptionCombo(CategoryOptionCombo attributeOptionCombo) {
    this.attributeOptionCombo = attributeOptionCombo;
  }

  @JsonProperty
  @JacksonXmlProperty(namespace = DxfNamespaces.DXF_2_0)
  public DataApprovalAction getAction() {
    return action;
  }

  public void setAction(DataApprovalAction action) {
    this.action = action;
  }

  @JsonProperty
  @JacksonXmlProperty(namespace = DxfNamespaces.DXF_2_0)
  public Date getCreated() {
    return created;
  }

  public void setCreated(Date created) {
    this.created = created;
  }

  @JsonProperty
  @JacksonXmlProperty(namespace = DxfNamespaces.DXF_2_0)
  public User getCreator() {
    return creator;
  }

  public void setCreator(User creator) {
    this.creator = creator;
  }

  // ----------------------------------------------------------------------
  // hashCode, equals, toString
  // ----------------------------------------------------------------------

  @Override
  public int hashCode() {
    return Objects.hash(
        id,
        level,
        workflow,
        period,
        organisationUnit,
        attributeOptionCombo,
        action,
        created,
        creator);
  }

  @Override
  public String toString() {
    return "DataApproval{"
        + "id="
        + id
        + ", level="
        + (level == null ? "(null)" : level.getLevel())
        + ", workflow='"
        + (workflow == null ? "(null)" : workflow.getName())
        + "'"
        + ", period="
        + (period == null ? "(null)" : period.getName())
        + ", organisationUnit='"
        + (organisationUnit == null ? "(null)" : organisationUnit.getName())
        + "'"
        + ", attributeOptionCombo='"
        + (attributeOptionCombo == null ? "(null)" : attributeOptionCombo.getName())
        + "'"
        + ", action="
        + action
        + ", created="
        + created
        + ", creator="
        + (creator == null ? "(null)" : creator.getName())
        + '}';
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (!(object instanceof DataApprovalAudit)) {
      return false;
    }

    DataApprovalAudit other = (DataApprovalAudit) object;

    return id == other.id
        && action == other.action
        && Objects.equals(level, other.level)
        && Objects.equals(workflow, other.workflow)
        && Objects.equals(period, other.period)
        && Objects.equals(organisationUnit, other.organisationUnit)
        && Objects.equals(attributeOptionCombo, other.attributeOptionCombo)
        && Objects.equals(created, other.created)
        && Objects.equals(creator, other.creator);
  }
}
