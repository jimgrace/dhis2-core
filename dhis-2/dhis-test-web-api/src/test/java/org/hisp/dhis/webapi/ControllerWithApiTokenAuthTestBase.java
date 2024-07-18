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
package org.hisp.dhis.webapi;

import static org.hisp.dhis.test.web.WebClientUtils.failOnException;

import javax.persistence.EntityManager;
import org.hisp.dhis.test.DhisConvenienceTest;
import org.hisp.dhis.test.config.H2DhisConfiguration;
import org.hisp.dhis.test.utils.TestUtils;
import org.hisp.dhis.user.User;
import org.hisp.dhis.user.UserService;
import org.hisp.dhis.webapi.security.config.WebMvcConfig;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * Base class for convenient testing of the web API on basis of {@link
 * org.hisp.dhis.jsontree.JsonMixed} responses, with API tokens.
 *
 * <p>This class differs from {@link H2ControllerIntegrationTestBase} in that this base class also
 * includes the {@link FilterChainProxy} so that we can authenticate the request like it would in a
 * normal running server.
 *
 * @author Morten Svanæs
 */
@ContextConfiguration(
    inheritLocations = false,
    classes = {H2DhisConfiguration.class, WebMvcConfig.class})
public abstract class ControllerWithApiTokenAuthTestBase extends H2ControllerIntegrationTestBase {
  @Autowired private UserService _userService;

  @Autowired private EntityManager entityManager;

  @Autowired private FilterChainProxy springSecurityFilterChain;

  @Override
  @BeforeEach
  void setup() {
    userService = _userService;
    clearSecurityContext();

    User randomAdminUser =
        DhisConvenienceTest.createRandomAdminUserWithEntityManager(entityManager);
    injectSecurityContextUser(randomAdminUser);

    adminUser = createAndAddAdminUser("ALL");

    mvc =
        MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .addFilter(springSecurityFilterChain)
            .build();

    injectSecurityContextUser(adminUser);

    TestUtils.executeStartupRoutines(webApplicationContext);
  }

  @Override
  protected final HttpResponse webRequest(MockHttpServletRequestBuilder request) {
    return failOnException(
        () -> new HttpResponse(toResponse(mvc.perform(request).andReturn().getResponse())));
  }
}
