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
package org.hisp.dhis.webapi.controller.mapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.hisp.dhis.common.DisplayProperty;
import org.hisp.dhis.common.OpenApi;
import org.hisp.dhis.render.RenderService;
import org.hisp.dhis.webapi.service.GeoFeatureService;
import org.hisp.dhis.webapi.utils.ContextUtils;
import org.hisp.dhis.webapi.webdomain.GeoFeature;
import org.hisp.dhis.webapi.webdomain.WebOptions;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Lars Helge Overland
 */
@OpenApi.Document(
    entity = GeoFeature.class,
    classifiers = {"team:analytics", "purpose:metadata"})
@Controller
@RequestMapping("/api/geoFeatures")
public class GeoFeatureController {

  private static final CacheControl GEOFEATURE_CACHE =
      CacheControl.maxAge(2, TimeUnit.HOURS).cachePrivate();

  private final RenderService renderService;

  private final GeoFeatureService geoFeatureService;

  public GeoFeatureController(RenderService renderService, GeoFeatureService geoFeatureService) {
    this.renderService = renderService;
    this.geoFeatureService = geoFeatureService;
  }

  // -------------------------------------------------------------------------
  // Resources
  // -------------------------------------------------------------------------

  @GetMapping
  @ResponseBody
  public ResponseEntity<List<GeoFeature>> getGeoFeaturesJson(
      @RequestParam(required = false) String ou,
      @RequestParam(required = false) String oug,
      @RequestParam(required = false) DisplayProperty displayProperty,
      @RequestParam(required = false) Date relativePeriodDate,
      @RequestParam(required = false) String userOrgUnit,
      @RequestParam(required = false) String coordinateField,
      @RequestParam(defaultValue = "false", value = "includeGroupSets") boolean rpIncludeGroupSets,
      @RequestParam Map<String, String> parameters,
      HttpServletRequest request,
      HttpServletResponse response) {
    WebOptions options = new WebOptions(parameters);
    boolean includeGroupSets = "detailed".equals(options.getViewClass()) || rpIncludeGroupSets;

    List<GeoFeature> features =
        geoFeatureService.getGeoFeatures(
            GeoFeatureService.Parameters.builder()
                .displayProperty(displayProperty)
                .includeGroupSets(includeGroupSets)
                .request(request)
                .response(response)
                .organisationUnit(ou)
                .userOrgUnit(userOrgUnit)
                .organisationUnitGroupId(oug)
                .relativePeriodDate(relativePeriodDate)
                .coordinateField(coordinateField)
                .build());

    return ResponseEntity.ok()
        .header(HttpHeaders.CACHE_CONTROL, GEOFEATURE_CACHE.getHeaderValue())
        .contentType(MediaType.APPLICATION_JSON)
        .body(features);
  }

  @GetMapping(produces = "application/javascript")
  public void getGeoFeaturesJsonP(
      @RequestParam(required = false) String ou,
      @RequestParam(required = false) String oug,
      @RequestParam(required = false) DisplayProperty displayProperty,
      @RequestParam(required = false) Date relativePeriodDate,
      @RequestParam(required = false) String userOrgUnit,
      @RequestParam(required = false) String coordinateField,
      @RequestParam(defaultValue = "callback") String callback,
      @RequestParam(defaultValue = "false", value = "includeGroupSets") boolean rpIncludeGroupSets,
      @RequestParam Map<String, String> parameters,
      HttpServletRequest request,
      HttpServletResponse response)
      throws IOException {
    WebOptions options = new WebOptions(parameters);
    boolean includeGroupSets = "detailed".equals(options.getViewClass()) || rpIncludeGroupSets;

    List<GeoFeature> features =
        geoFeatureService.getGeoFeatures(
            GeoFeatureService.Parameters.builder()
                .displayProperty(displayProperty)
                .includeGroupSets(includeGroupSets)
                .request(request)
                .response(response)
                .userOrgUnit(userOrgUnit)
                .organisationUnitGroupId(oug)
                .relativePeriodDate(relativePeriodDate)
                .coordinateField(coordinateField)
                .build());

    if (features == null) {
      return;
    }

    ContextUtils.setCacheControl(response, GEOFEATURE_CACHE);
    response.setContentType("application/javascript");
    renderService.toJsonP(response.getOutputStream(), features, callback);
  }
}
