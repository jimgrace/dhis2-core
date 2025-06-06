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
package org.hisp.dhis.category.hibernate;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import java.util.Collection;
import java.util.List;
import javax.annotation.Nonnull;
import org.hisp.dhis.category.CategoryOptionGroup;
import org.hisp.dhis.category.CategoryOptionGroupSet;
import org.hisp.dhis.category.CategoryOptionGroupStore;
import org.hisp.dhis.common.hibernate.HibernateIdentifiableObjectStore;
import org.hisp.dhis.hibernate.JpaQueryParameters;
import org.hisp.dhis.security.acl.AclService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * @author Lars Helge Overland
 */
@Repository("org.hisp.dhis.category.CategoryOptionGroupStore")
public class HibernateCategoryOptionGroupStore
    extends HibernateIdentifiableObjectStore<CategoryOptionGroup>
    implements CategoryOptionGroupStore {
  public HibernateCategoryOptionGroupStore(
      EntityManager entityManager,
      JdbcTemplate jdbcTemplate,
      ApplicationEventPublisher publisher,
      AclService aclService) {
    super(entityManager, jdbcTemplate, publisher, CategoryOptionGroup.class, aclService, true);
  }

  @Override
  public List<CategoryOptionGroup> getCategoryOptionGroups(CategoryOptionGroupSet groupSet) {
    CriteriaBuilder builder = getCriteriaBuilder();

    JpaQueryParameters<CategoryOptionGroup> parameters =
        newJpaParameters()
            .addPredicates(getSharingPredicates(builder))
            .addPredicate(
                root -> {
                  Join<Object, Object> groupSets = root.join("groupSets");

                  return builder.or(
                      builder.equal(groupSets.get("id"), groupSet.getId()),
                      builder.isNull(groupSets.get("id")));
                });

    return getList(builder, parameters);
  }

  @Override
  public List<CategoryOptionGroup> getByCategoryOption(
      @Nonnull Collection<String> categoryOptions) {
    if (categoryOptions.isEmpty()) return List.of();
    return getQuery(
            """
            select distinct cog from CategoryOptionGroup cog \
            join cog.members co \
            where co.uid in :categoryOptions""",
            CategoryOptionGroup.class)
        .setParameter("categoryOptions", categoryOptions)
        .getResultList();
  }
}
