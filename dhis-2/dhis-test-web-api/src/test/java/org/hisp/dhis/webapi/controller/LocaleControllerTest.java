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

import static org.hisp.dhis.webapi.utils.WebClientUtils.assertStatus;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.stream.Collectors;
import org.hisp.dhis.jsontree.JsonObject;
import org.hisp.dhis.webapi.DhisControllerConvenienceTest;
import org.hisp.dhis.webapi.json.domain.JsonWebLocale;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

/**
 * Tests the {@link LocaleController} using (mocked) REST requests.
 *
 * @author Jan Bernitt
 */
class LocaleControllerTest extends DhisControllerConvenienceTest {

  @Test
  void testAddLocale() {
    assertWebMessage(
        "Created",
        201,
        "OK",
        "Locale created successfully",
        POST("/locales/dbLocales?language=en&country=GB").content(HttpStatus.CREATED));
  }

  @Test
  void testAddLocale_InvalidCountry() {
    assertWebMessage(
        "Conflict",
        409,
        "ERROR",
        "Invalid country or language code.",
        POST("/locales/dbLocales?language=en&country=").content(HttpStatus.CONFLICT));
  }

  @Test
  void testAddLocale_InvalidLanguage() {
    assertWebMessage(
        "Conflict",
        409,
        "ERROR",
        "Invalid country or language code.",
        POST("/locales/dbLocales?language=&country=GB").content(HttpStatus.CONFLICT));
  }

  @Test
  void testAddLocale_AlreadyExists() {
    assertStatus(HttpStatus.CREATED, POST("/locales/dbLocales?language=en&country=GB"));
    assertWebMessage(
        "Conflict",
        409,
        "ERROR",
        "Locale code existed.",
        POST("/locales/dbLocales?language=en&country=GB").content(HttpStatus.CONFLICT));
  }

  @Test
  @DisplayName("Indonesian ui locales are returned with the expected locale codes")
  void indonesianUiLocaleCodesTest() {
    // when
    JsonObject response = GET("/locales/ui").content();

    // then
    List<String> localeCodes =
        response.asList(JsonWebLocale.class).stream()
            .map(JsonWebLocale::getLocale)
            .collect(Collectors.toList());

    assertTrue(
        localeCodes.containsAll(List.of("id", "id_ID")),
        "Locales 'id' and 'id_ID' should be present");
    assertFalse(
        localeCodes.containsAll(List.of("in", "in_ID")),
        "Locales 'in' and 'in_ID' should not be present");
  }
}
