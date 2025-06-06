/*
 * Copyright (c) 2004-2024, University of Oslo
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
package org.hisp.dhis.tracker.export.singleevent;

import static org.hisp.dhis.changelog.ChangeLogType.CREATE;
import static org.hisp.dhis.changelog.ChangeLogType.DELETE;
import static org.hisp.dhis.changelog.ChangeLogType.UPDATE;
import static org.hisp.dhis.external.conf.ConfigurationKey.CHANGELOG_TRACKER;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.hisp.dhis.changelog.ChangeLogType;
import org.hisp.dhis.common.UID;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.external.conf.DhisConfigurationProvider;
import org.hisp.dhis.feedback.NotFoundException;
import org.hisp.dhis.program.Event;
import org.hisp.dhis.tracker.Page;
import org.hisp.dhis.tracker.PageParams;
import org.locationtech.jts.geom.Geometry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("org.hisp.dhis.tracker.export.singleevent.SingleEventChangeLogService")
@RequiredArgsConstructor
public class DefaultSingleEventChangeLogService implements SingleEventChangeLogService {

  private final SingleEventService singleEventService;
  private final HibernateSingleEventChangeLogStore hibernateSingleEventChangeLogStore;
  private final DhisConfigurationProvider config;

  @Nonnull
  @Override
  @Transactional(readOnly = true)
  public Page<SingleEventChangeLog> getEventChangeLog(
      UID event, SingleEventChangeLogOperationParams operationParams, PageParams pageParams)
      throws NotFoundException {
    // check existence and access
    singleEventService.getEvent(event);

    return hibernateSingleEventChangeLogStore.getEventChangeLogs(
        event, operationParams, pageParams);
  }

  @Transactional
  @Override
  public void deleteEventChangeLog(Event event) {
    hibernateSingleEventChangeLogStore.deleteEventChangeLog(event);
  }

  @Transactional
  @Override
  public void deleteEventChangeLog(DataElement dataElement) {
    hibernateSingleEventChangeLogStore.deleteEventChangeLog(dataElement);
  }

  @Override
  @Transactional
  public void addEventChangeLog(
      Event event,
      DataElement dataElement,
      String previousValue,
      String value,
      ChangeLogType changeLogType,
      String userName) {
    if (config.isDisabled(CHANGELOG_TRACKER)) {
      return;
    }

    SingleEventChangeLog singleEventChangeLog =
        new SingleEventChangeLog(
            event, dataElement, null, previousValue, value, changeLogType, new Date(), userName);

    hibernateSingleEventChangeLogStore.addEventChangeLog(singleEventChangeLog);
  }

  @Override
  @Transactional
  public void addFieldChangeLog(
      @Nonnull Event currentEvent, @Nonnull Event event, @Nonnull String username) {
    if (config.isDisabled(CHANGELOG_TRACKER)) {
      return;
    }

    logIfChanged(
        "occurredAt", Event::getOccurredDate, this::formatDate, currentEvent, event, username);
    logIfChanged(
        "scheduledAt", Event::getScheduledDate, this::formatDate, currentEvent, event, username);
    logIfChanged(
        "geometry", Event::getGeometry, this::formatGeometry, currentEvent, event, username);
  }

  @Override
  @Transactional(readOnly = true)
  public Set<String> getOrderableFields() {
    return hibernateSingleEventChangeLogStore.getOrderableFields();
  }

  @Override
  public Set<Pair<String, Class<?>>> getFilterableFields() {
    return hibernateSingleEventChangeLogStore.getFilterableFields();
  }

  private <T> void logIfChanged(
      String field,
      Function<Event, T> valueExtractor,
      Function<T, String> formatter,
      Event currentEvent,
      Event event,
      String userName) {

    String currentValue = formatter.apply(valueExtractor.apply(currentEvent));
    String newValue = formatter.apply(valueExtractor.apply(event));

    if (!Objects.equals(currentValue, newValue)) {
      ChangeLogType changeLogType = getChangeLogType(currentValue, newValue);

      SingleEventChangeLog singleEventChangeLog =
          new SingleEventChangeLog(
              event, null, field, currentValue, newValue, changeLogType, new Date(), userName);

      hibernateSingleEventChangeLogStore.addEventChangeLog(singleEventChangeLog);
    }
  }

  private ChangeLogType getChangeLogType(String oldValue, String newValue) {
    if (isFieldCreated(oldValue, newValue)) {
      return CREATE;
    } else if (isFieldUpdated(oldValue, newValue)) {
      return UPDATE;
    } else {
      return DELETE;
    }
  }

  private boolean isFieldCreated(String originalValue, String payloadValue) {
    return originalValue == null && payloadValue != null;
  }

  private boolean isFieldUpdated(String originalValue, String payloadValue) {
    return originalValue != null && payloadValue != null;
  }

  private String formatDate(Date date) {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    return date != null ? formatter.format(date) : null;
  }

  private String formatGeometry(Geometry geometry) {
    if (geometry == null) {
      return null;
    }

    return Stream.of(geometry.getCoordinates())
        .map(c -> String.format("(%f, %f)", c.x, c.y))
        .collect(Collectors.joining(", "));
  }
}
