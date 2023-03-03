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

import static org.hisp.dhis.utils.Assertions.assertLessOrEqual;
import static org.hisp.dhis.web.WebClientUtils.assertStatus;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.commons.codec.digest.DigestUtils;
import org.hisp.dhis.jsontree.JsonObject;
import org.hisp.dhis.web.HttpStatus;
import org.hisp.dhis.webapi.DhisControllerConvenienceTest;
import org.junit.jupiter.api.Test;

/**
 * Tests the {@link org.hisp.dhis.webapi.openapi.QueryController} with Mock MVC
 * tests.
 *
 * @author Austin McGee
 */
class QueryControllerTest extends DhisControllerConvenienceTest
{
    private static final String testTargetUrl = "/api/me";

    private static final String testTargetUrlHash = DigestUtils.sha1Hex( testTargetUrl );

    @Test
    void testGetUnusedShortenedQuery()
    {
        assertStatus( HttpStatus.NOT_FOUND, GET( "/query/shortened/" + testTargetUrlHash ) );
    }

    @Test
    void testGetOpenApiDocument_PathFilter()
    {
        JsonObject doc = GET( "/openapi/openapi.json?path=/users" ).content();
        assertTrue( doc.isObject() );
        assertTrue(
            doc.getObject( "paths" ).has( "/users/gist", "/users/invite", "/users/invites", "/users/sharing" ) );
        assertLessOrEqual( 25, doc.getObject( "paths" ).size() );
        assertLessOrEqual( 35, doc.getObject( "components.schemas" ).size() );
    }

    @Test
    void testGetOpenApiDocument_TagFilter()
    {
        JsonObject doc = GET( "/openapi/openapi.json?tag=user" ).content();
        assertTrue( doc.isObject() );
        assertTrue(
            doc.getObject( "paths" ).has( "/users/gist", "/users/invite", "/users/invites", "/users/sharing" ) );
        assertLessOrEqual( 130, doc.getObject( "paths" ).size() );
        assertLessOrEqual( 60, doc.getObject( "components.schemas" ).size() );
    }

}
