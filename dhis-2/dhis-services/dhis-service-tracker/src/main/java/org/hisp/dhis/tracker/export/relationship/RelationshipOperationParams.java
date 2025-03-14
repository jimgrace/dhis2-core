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
package org.hisp.dhis.tracker.export.relationship;

import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.hisp.dhis.common.SortDirection;
import org.hisp.dhis.common.UID;
import org.hisp.dhis.program.Enrollment;
import org.hisp.dhis.program.Event;
import org.hisp.dhis.trackedentity.TrackedEntity;
import org.hisp.dhis.tracker.TrackerType;
import org.hisp.dhis.tracker.export.Order;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RelationshipOperationParams {
  private TrackerType type;

  private UID identifier;

  private List<Order> order;

  public static class RelationshipOperationParamsBuilder {

    private final List<Order> order = new ArrayList<>();

    // Do not remove this unused method. This hides the order field from the builder which Lombok
    // does not support. The repeated order field and private order method prevent access to order
    // via the builder.
    // Order should be added via the orderBy builder methods.
    private RelationshipOperationParamsBuilder order(List<Order> order) {
      return this;
    }

    public RelationshipOperationParamsBuilder orderBy(String field, SortDirection direction) {
      this.order.add(new Order(field, direction));
      return this;
    }

    public RelationshipOperationParamsBuilder identifier(UID uid) {
      this.identifier = uid;
      return this;
    }

    public RelationshipOperationParamsBuilder identifier(TrackedEntity trackedEntity) {
      this.identifier = UID.of(trackedEntity);
      return this;
    }

    public RelationshipOperationParamsBuilder identifier(Enrollment enrollment) {
      this.identifier = UID.of(enrollment);
      return this;
    }

    public RelationshipOperationParamsBuilder identifier(Event event) {
      this.identifier = UID.of(event);
      return this;
    }
  }

  private boolean includeDeleted;
}
