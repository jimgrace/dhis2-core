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
package org.hisp.dhis.program.variable;

import org.hisp.dhis.parser.expression.CommonExpressionVisitor;
import org.hisp.dhis.parser.expression.ProgramExpressionParams;
import org.hisp.dhis.program.AnalyticsType;

/**
 * @author Zubair Asghar
 */
public class vEventStatus implements ProgramVariable {

  // Placeholder format: FUNC_CTE_VAR( type='{type}', column='{column}', piUid='{piUid}',
  // psUid='{psUid|null}', offset='{offset}')
  private static final String PLACEHOLDER_FORMAT =
      "FUNC_CTE_VAR( type='vEventStatus', column='eventstatus', piUid='%s', psUid='null', offset='0')";

  @Override
  public Object getSql(CommonExpressionVisitor visitor) {
    if (!visitor.isUseExperimentalSqlEngine()) {
      return getSql2(visitor);
    }
    ProgramExpressionParams params = visitor.getProgParams();

    if (params != null
        && params.getProgramIndicator() != null
        && AnalyticsType.ENROLLMENT == params.getProgramIndicator().getAnalyticsType()) {
      String piUid = params.getProgramIndicator().getUid();
      return String.format(PLACEHOLDER_FORMAT, piUid);
    } else {
      return "eventstatus";
    }
  }

  @Override
  public Object defaultVariableValue() {
    return "COMPLETED";
  }

  public Object getSql2(CommonExpressionVisitor visitor) {
    ProgramExpressionParams params = visitor.getProgParams();

    return visitor
        .getStatementBuilder()
        .getProgramIndicatorEventColumnSql(
            null,
            "eventstatus",
            params.getReportingStartDate(),
            params.getReportingEndDate(),
            params.getProgramIndicator());
  }
}
