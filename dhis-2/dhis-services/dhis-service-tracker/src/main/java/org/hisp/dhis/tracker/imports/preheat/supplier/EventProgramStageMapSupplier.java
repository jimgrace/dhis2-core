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
package org.hisp.dhis.tracker.imports.preheat.supplier;

import java.util.List;
import java.util.Objects;
import org.hisp.dhis.common.UID;
import org.hisp.dhis.program.ProgramStage;
import org.hisp.dhis.tracker.imports.domain.Event;
import org.hisp.dhis.tracker.imports.domain.TrackerObjects;
import org.hisp.dhis.tracker.imports.preheat.TrackerPreheat;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

/**
 * This supplier adds to the pre-heat object a List of all Program Stages UIDs that have at least
 * ONE event that is not logically deleted ('deleted = true') and the status is not 'SKIPPED' among
 * the Program Stages and the enrollments present in the payload.
 *
 * @author Luciano Fiandesio
 */
@Component
public class EventProgramStageMapSupplier extends JdbcAbstractPreheatSupplier {
  private static final String PS_UID = "programStageUid";

  private static final String PI_UID = "enrollmentUid";

  private static final String SQL =
      "select distinct ps.uid as "
          + PS_UID
          + ", en.uid as "
          + PI_UID
          + " "
          + " from enrollment as en "
          + " join programstage as ps on en.programid = ps.programid "
          + " join event as ev on en.enrollmentid = ev.enrollmentid "
          + " where ev.deleted = false "
          + " and ev.status != 'SKIPPED' "
          + " and ps.programstageid = ev.programstageid "
          + " and ps.uid in (:programStageUids) "
          + " and en.uid in (:enrollmentUids) ";

  protected EventProgramStageMapSupplier(JdbcTemplate jdbcTemplate) {
    super(jdbcTemplate);
  }

  @Override
  public void preheatAdd(TrackerObjects trackerObjects, TrackerPreheat preheat) {
    if (trackerObjects.getEvents().isEmpty()) {
      return;
    }

    List<String> notRepeatableProgramStageUids =
        trackerObjects.getEvents().stream()
            .map(Event::getProgramStage)
            .filter(Objects::nonNull)
            .map(preheat::getProgramStage)
            .filter(Objects::nonNull)
            .filter(ps -> ps.getProgram().isRegistration())
            .filter(ps -> !ps.getRepeatable())
            .map(ProgramStage::getUid)
            .distinct()
            .toList();

    List<UID> enrollmentUids =
        trackerObjects.getEvents().stream()
            .map(Event::getEnrollment)
            .filter(Objects::nonNull)
            .distinct()
            .toList();

    if (!notRepeatableProgramStageUids.isEmpty() && !enrollmentUids.isEmpty()) {
      MapSqlParameterSource parameters = new MapSqlParameterSource();
      parameters.addValue("programStageUids", notRepeatableProgramStageUids);
      parameters.addValue("enrollmentUids", UID.toValueList(enrollmentUids));
      jdbcTemplate.query(
          SQL,
          parameters,
          (RowCallbackHandler)
              rs -> preheat.addProgramStageWithEvents(rs.getString(PS_UID), rs.getString(PI_UID)));
    }
  }
}
