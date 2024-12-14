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
package org.hisp.dhis.dxf2.deprecated.tracker.importer.shared.postprocess;

import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dxf2.deprecated.tracker.event.DataValue;
import org.hisp.dhis.dxf2.deprecated.tracker.event.Event;
import org.hisp.dhis.dxf2.deprecated.tracker.importer.Processor;
import org.hisp.dhis.dxf2.deprecated.tracker.importer.context.WorkContext;
import org.hisp.dhis.fileresource.FileResource;
import org.hisp.dhis.fileresource.FileResourceService;
import org.springframework.stereotype.Component;

/**
 * @author Abyot Asalefew Gizaw <abyota@gmail.com>
 */
@Component
public class EventFileResourcePostProcessor implements Processor {
  @Override
  public void process(Event event, WorkContext ctx) {
    FileResourceService fileResourceService = ctx.getServiceDelegator().getFileResourceService();

    for (DataValue dataValue : event.getDataValues()) {
      DataElement dataElement = ctx.getDataElementMap().get(dataValue.getDataElement());

      if (dataElement.isFileType()) {
        FileResource fileResource = fileResourceService.getFileResource(dataValue.getValue());

        if (fileResource != null
            && (!fileResource.isAssigned() || fileResource.getFileResourceOwner() == null)) {
          fileResource.setAssigned(true);
          fileResource.setFileResourceOwner(event.getEvent());
          fileResourceService.updateFileResource(fileResource);
        }
      }
    }
  }
}
