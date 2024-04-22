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
package org.hisp.dhis.webapi.controller;

import java.util.List;
import org.hisp.dhis.common.DhisApiVersion;
import org.hisp.dhis.common.IllegalQueryException;
import org.hisp.dhis.common.OpenApi;
import org.hisp.dhis.programstagefilter.EventFilter;
import org.hisp.dhis.programstagefilter.EventFilterService;
import org.hisp.dhis.webapi.mvc.annotation.ApiVersion;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Ameen Mohamed <ameen@dhis2.org>
 */
@OpenApi.Tags("tracker")
@RestController
@RequestMapping("/api/eventFilters")
@ApiVersion(include = {DhisApiVersion.ALL, DhisApiVersion.DEFAULT})
public class EventFilterController extends AbstractCrudController<EventFilter> {
  private final EventFilterService eventFilterService;

  public EventFilterController(EventFilterService eventFilterService) {
    this.eventFilterService = eventFilterService;
  }

  @Override
  public void preCreateEntity(EventFilter eventFilter) {
    List<String> errors = eventFilterService.validate(eventFilter);
    if (!errors.isEmpty()) {
      throw new IllegalQueryException(errors.toString());
    }
  }

  @Override
  public void preUpdateEntity(EventFilter oldEventFilter, EventFilter newEventFilter) {
    List<String> errors = eventFilterService.validate(newEventFilter);
    if (!errors.isEmpty()) {
      throw new IllegalQueryException(errors.toString());
    }
  }
}
