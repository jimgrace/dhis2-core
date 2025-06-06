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
package org.hisp.dhis.webapi.controller;

import static org.hisp.dhis.http.HttpAssertions.assertStatus;
import static org.hisp.dhis.test.webapi.Assertions.assertWebMessage;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.hisp.dhis.http.HttpStatus;
import org.hisp.dhis.jsontree.JsonObject;
import org.hisp.dhis.test.webapi.H2ControllerIntegrationTestBase;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

/**
 * Tests the {@link org.hisp.dhis.webapi.controller.mapping.MapController} using (mocked) REST
 * requests.
 *
 * @author Jan Bernitt
 */
@Transactional
class MapControllerTest extends H2ControllerIntegrationTestBase {
  @Test
  void testPutJsonObject() {
    String mapId = assertStatus(HttpStatus.CREATED, POST("/maps/", "{'name':'My map'}"));

    JsonObject map = GET("/maps/{uid}", mapId).content();

    // The default merge method is REPLACE, so we must set the mandatory attributes from the created
    // object.
    String mandatoryProperties =
        "'lastUpdated':'"
            + map.get("lastUpdated").node().value()
            + "', 'created':'"
            + map.get("created").node().value()
            + "'";

    assertStatus(
        HttpStatus.OK,
        PUT("/maps/" + mapId, "{'name':'My updated map'," + mandatoryProperties + "}"));

    map = GET("/maps/{uid}", mapId).content();

    assertEquals("My updated map", map.get("name").node().value());
  }

  @Test
  void testPutJsonObject_NotFound() {
    assertWebMessage(
        "Not Found",
        404,
        "ERROR",
        "Map does not exist: xyz",
        PUT("/maps/xyz", "{'name':'My updated map'}").content(HttpStatus.NOT_FOUND));
  }

  @Test
  void testGetWithMapViewAndOrgUnitField() {
    String attrId =
        assertStatus(
            HttpStatus.CREATED,
            POST(
                "/attributes",
                "{  'name':'GeoJsonAttribute', "
                    + "'valueType':'GEOJSON', "
                    + "'organisationUnit':true}"));

    String mapId =
        assertStatus(
            HttpStatus.CREATED,
            POST(
                "/maps/",
                "{\"name\":\"My map\", \"mapViews\":[ { \"orgUnitField\": \""
                    + attrId
                    + "\", "
                    + "\"layer\": \"thematic1\",\"renderingStrategy\": \"SINGLE\" } ]}"));

    JsonObject map = GET("/maps/{uid}", mapId).content();
    assertNotNull(map.getArray("mapViews"));
    assertEquals(1, map.getArray("mapViews").size());

    JsonObject mapView = map.getArray("mapViews").get(0).as(JsonObject.class);
    assertEquals(attrId, mapView.getString("orgUnitField").string());
    assertEquals("GeoJsonAttribute", mapView.getString("orgUnitFieldDisplayName").string());
  }
}
