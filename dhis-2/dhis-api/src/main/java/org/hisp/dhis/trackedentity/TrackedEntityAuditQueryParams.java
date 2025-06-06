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
package org.hisp.dhis.trackedentity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hisp.dhis.audit.AuditOperationType;
import org.hisp.dhis.common.Pager;

/**
 * @author Abyot Asalefew Gizaw abyota@gmail.com
 */
@Data
@Accessors(chain = true)
public class TrackedEntityAuditQueryParams {
  private List<String> trackedEntities = new ArrayList<>();

  private List<String> users = new ArrayList<>();

  private List<AuditOperationType> auditTypes = new ArrayList<>();

  private Date startDate = null;

  private Date endDate = null;

  private Pager pager;

  // -------------------------------------------------------------------------
  // Logic
  // -------------------------------------------------------------------------

  public boolean hasTrackedEntities() {
    return trackedEntities != null && !trackedEntities.isEmpty();
  }

  public boolean hasUsers() {
    return users != null && !users.isEmpty();
  }

  public boolean hasAuditTypes() {
    return auditTypes != null && !auditTypes.isEmpty();
  }

  public boolean hasStartDate() {
    return startDate != null;
  }

  public boolean hasEndDate() {
    return endDate != null;
  }

  public boolean hasPaging() {
    return pager != null;
  }
}
