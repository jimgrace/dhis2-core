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
package org.hisp.dhis.analytics.util;

import static org.apache.commons.lang3.StringUtils.EMPTY;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

/**
 * Utilities for analytics SQL operations.
 *
 * @author Lars Helge Overland
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AnalyticsSqlUtils {
  public static final String QUOTE = "\"";

  public static final String SINGLE_QUOTE = "'";

  public static final String ANALYTICS_TBL_ALIAS = "ax";

  public static final String OWNERSHIP_TBL_ALIAS = "own";

  public static final String DATE_PERIOD_STRUCT_ALIAS = "ps";

  public static final String ORG_UNIT_STRUCT_ALIAS = "ous";

  public static final String ORG_UNIT_GROUPSET_STRUCT_ALIAS = "ougs";

  private static final String SEPARATOR = ".";

  /**
   * Quotes the given relation (typically a column). Quotes part of the given relation are encoded
   * (replaced by double quotes that is).
   *
   * @param relation the relation (typically a column).
   * @return the quoted relation.
   */
  public static String quote(String relation) {
    Assert.notNull(relation, "Relation must be specified");

    String rel = relation.replace(QUOTE, (QUOTE + QUOTE));

    return QUOTE + rel + QUOTE;
  }

  /**
   * Quotes and qualifies the given relation (typically a column). Quotes part of the given relation
   * are encoded (replaced by double quotes that is).
   *
   * @param relation the relation (typically a column).
   * @return the quoted relation.
   */
  public static String quote(String alias, String relation) {
    Assert.notNull(alias, "Alias must be specified");

    return alias + SEPARATOR + quote(relation);
  }

  /**
   * Quotes and qualifies the given relation (typically a column). Quotes part of the given relation
   * are encoded (replaced by double quotes that is). The alias used is {@link
   * AnalyticsSqlUtils#ANALYTICS_TBL_ALIAS}.
   *
   * @return the quoted and qualified relation.
   */
  public static String quoteAlias(String relation) {
    return ANALYTICS_TBL_ALIAS + SEPARATOR + quote(relation);
  }

  /**
   * Returns a concatenated string of the given collection items separated by comma where each item
   * is quoted and aliased.
   *
   * @param items the collection of items.
   * @return a string.
   */
  public static String quoteAliasCommaSeparate(Collection<String> items) {
    return items.stream().map(AnalyticsSqlUtils::quoteAlias).collect(Collectors.joining(","));
  }

  /**
   * Encodes and quotes the given value.
   *
   * @param value the value.
   * @return the encoded and quoted value.
   */
  public static String encode(String value) {
    return encode(value, true);
  }

  /**
   * Encodes the given value.
   *
   * @param value the value.
   * @param quote whether to quote the value.
   * @return the encoded value.
   */
  public static String encode(String value, boolean quote) {
    value = value.replace(SINGLE_QUOTE, (SINGLE_QUOTE + SINGLE_QUOTE));

    return quote ? (SINGLE_QUOTE + value + SINGLE_QUOTE) : value;
  }

  /**
   * Returns a string containing closing parenthesis. The number of parenthesis is based on the
   * number of missing closing parenthesis in the argument string.
   *
   * <p>Example:
   *
   * <p>{@code} input: "((( ))" -> output: ")" {@code}
   *
   * @param str a string.
   * @return a String containing 0 or more "closing" parenthesis
   */
  public static String getClosingParentheses(String str) {
    if (StringUtils.isEmpty(str)) {
      return EMPTY;
    }

    int open = 0;

    for (int i = 0; i < str.length(); i++) {
      if (str.charAt(i) == '(') {
        open++;
      } else if (str.charAt(i) == ')') {
        if (open >= 1) {
          open--;
        }
      }
    }

    return StringUtils.repeat(")", open);
  }

  /**
   * The method creates the coalesce function for coordinates fallback.
   *
   * @param fields Collection of coordinate fields.
   * @param defaultColumnName Default coordinate field
   * @return Example: ST_AsGeoJSON(coalesce(ax."psigeometry",ax."pigeometry",ax."ougeometry"). or
   *     default coordinate field.
   */
  public static String getCoalesce(List<String> fields, String defaultColumnName) {
    if (fields == null) {
      return defaultColumnName;
    }

    String args =
        fields.stream()
            .filter(f -> f != null && !f.isBlank())
            .map(AnalyticsSqlUtils::quoteAlias)
            .collect(Collectors.joining(","));

    return args.isEmpty() ? defaultColumnName : "coalesce(" + args + ")";
  }
}
