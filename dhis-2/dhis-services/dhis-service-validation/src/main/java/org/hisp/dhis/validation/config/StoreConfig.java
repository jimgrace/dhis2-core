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
package org.hisp.dhis.validation.config;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hisp.dhis.common.hibernate.HibernateIdentifiableObjectStore;
import org.hisp.dhis.security.acl.AclService;
import org.hisp.dhis.user.CurrentUserService;
import org.hisp.dhis.validation.ValidationRuleGroup;
import org.hisp.dhis.validation.notification.ValidationNotificationTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author Luciano Fiandesio
 */
@Configuration("validationStoreConfig")
public class StoreConfig {
  @PersistenceContext private EntityManager entityManager;

  @Autowired private JdbcTemplate jdbcTemplate;

  @Autowired private ApplicationEventPublisher publisher;

  @Autowired private CurrentUserService currentUserService;

  @Autowired private AclService aclService;

  @Bean("org.hisp.dhis.validation.notification.ValidationNotificationTemplateStore")
  public HibernateIdentifiableObjectStore<ValidationNotificationTemplate>
      programNotificationInstanceStore() {
    return new HibernateIdentifiableObjectStore<ValidationNotificationTemplate>(
        entityManager,
        jdbcTemplate,
        publisher,
        ValidationNotificationTemplate.class,
        currentUserService,
        aclService,
        true);
  }

  @Bean("org.hisp.dhis.validation.ValidationRuleGroupStore")
  public HibernateIdentifiableObjectStore<ValidationRuleGroup> validationRuleGroupStore() {
    return new HibernateIdentifiableObjectStore<ValidationRuleGroup>(
        entityManager,
        jdbcTemplate,
        publisher,
        ValidationRuleGroup.class,
        currentUserService,
        aclService,
        true);
  }
}
