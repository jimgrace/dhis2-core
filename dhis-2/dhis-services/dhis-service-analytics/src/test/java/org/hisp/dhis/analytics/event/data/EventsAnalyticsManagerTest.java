/*
 * Copyright (c) 2004-2019, University of Oslo
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

package org.hisp.dhis.analytics.event.data;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hisp.dhis.DhisConvenienceTest.*;
import static org.hisp.dhis.common.DimensionalObjectUtils.getList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.hisp.dhis.analytics.AggregationType;
import org.hisp.dhis.analytics.event.EventQueryParams;
import org.hisp.dhis.common.*;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.jdbc.StatementBuilder;
import org.hisp.dhis.jdbc.statementbuilder.PostgreSQLStatementBuilder;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.program.Program;
import org.hisp.dhis.program.ProgramIndicatorService;
import org.hisp.dhis.system.grid.ListGrid;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

/**
 * @author Luciano Fiandesio
 */
public class EventsAnalyticsManagerTest
{
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private ProgramIndicatorService programIndicatorService;

    private JdbcEventAnalyticsManager subject;

    @Mock
    private SqlRowSet rowSet;

    @Before
    public void setUp()
    {
        mockRowSet();

        StatementBuilder statementBuilder = new PostgreSQLStatementBuilder();

        subject = new JdbcEventAnalyticsManager( jdbcTemplate, statementBuilder, programIndicatorService );

        when( jdbcTemplate.queryForRowSet( anyString() ) ).thenReturn( this.rowSet );

    }

    @Test
    public void verifyLowerFunctionIsUsedAsColumnName()
    {
        when( rowSet.getString( "lower" ) ).thenReturn( "alfa" );

        Grid resultGrid = subject.getAggregatedEventData( createRequestParams(ValueType.TEXT), createGrid(), 200000 );

        assertThat( resultGrid.getRows(), hasSize( 1 ) );
        assertThat( resultGrid.getRow( 0 ), hasSize( 4 ) );
        assertThat( resultGrid.getRow( 0 ).get( 0 ), is( "alfa" ) );
        assertThat( resultGrid.getRow( 0 ).get( 1 ), is( "201701" ) );
        assertThat( resultGrid.getRow( 0 ).get( 2 ), is( "Sierra Leone" ) );
        assertThat( resultGrid.getRow( 0 ).get( 3 ), is( 100 ) );
    }

    @Test
    public void verifyLowerFunctionIsNotUsedAsColumnNameWhenQueryItemIsNotText()
    {
        when( rowSet.getString( "fWIAEtYVEGk" ) ).thenReturn( "2000" );

        Grid resultGrid = subject.getAggregatedEventData( createRequestParams(ValueType.INTEGER), createGrid(), 200000 );

        assertThat( resultGrid.getRows(), hasSize( 1 ) );
        assertThat( resultGrid.getRow( 0 ), hasSize( 4 ) );
        assertThat( resultGrid.getRow( 0 ).get( 0 ), is( "2000" ) );
        assertThat( resultGrid.getRow( 0 ).get( 1 ), is( "201701" ) );
        assertThat( resultGrid.getRow( 0 ).get( 2 ), is( "Sierra Leone" ) );
        assertThat( resultGrid.getRow( 0 ).get( 3 ), is( 100 ) );
    }

    private EventQueryParams createRequestParams(ValueType queryItemValueType)
    {

        DataElement deA = createDataElement( 'A', ValueType.INTEGER, AggregationType.SUM );
        deA.setUid( "fWIAEtYVEGk" );

        OrganisationUnit ouA = createOrganisationUnit( 'A' );

        DimensionalItemObject dio = new BaseDimensionalItemObject( deA.getUid() );

        EventQueryParams.Builder params = new EventQueryParams.Builder();

        params.withPeriods( getList( createPeriod( "2000Q1" ) ), "monthly" );
        params.withOrganisationUnits( getList( ouA ) );
        params.withTableName( "analytics_event_ebayegv0exc" );

        Program p = createProgram( 'A' );
        params.withProgram( p );
        params.withProgramStage( createProgramStage( 'B', p ) );

        QueryItem queryItem = new QueryItem( dio );
        queryItem.setValueType( queryItemValueType );

        params.addItem( queryItem );

        return params.build();
    }

    private Grid createGrid()
    {
        Grid grid = new ListGrid();
        grid.addHeader(
                new GridHeader( "fWIAEtYVEGk", "Mode of discharge", ValueType.TEXT, "java.lang.String", false, true ) );
        grid.addHeader( new GridHeader( "pe", "Period", ValueType.TEXT, "java.lang.String", false, true ) );
        grid.addHeader( new GridHeader( "value", "Value", ValueType.NUMBER, "java.lang.Double", false, true ) );
        return grid;
    }

    private void mockRowSet()
    {
        // simulate one row only
        when( rowSet.next() ).thenReturn( true ).thenReturn( false );

        when( rowSet.getString( "monthly" ) ).thenReturn( "201701" );
        when( rowSet.getString( "ou" ) ).thenReturn( "Sierra Leone" );
        when( rowSet.getInt( "value" ) ).thenReturn( 100 );
    }
}
