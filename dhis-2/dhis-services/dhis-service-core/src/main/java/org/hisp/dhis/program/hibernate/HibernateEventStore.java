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
package org.hisp.dhis.program.hibernate;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.query.Query;
import org.hisp.dhis.common.hibernate.SoftDeleteHibernateObjectStore;
import org.hisp.dhis.event.EventStatus;
import org.hisp.dhis.program.Event;
import org.hisp.dhis.program.EventStore;
import org.hisp.dhis.program.notification.NotificationTrigger;
import org.hisp.dhis.program.notification.ProgramNotificationTemplate;
import org.hisp.dhis.security.acl.AclService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * @author Abyot Asalefew
 */
@Repository("org.hisp.dhis.program.EventStore")
public class HibernateEventStore extends SoftDeleteHibernateObjectStore<Event>
    implements EventStore {
  private static final String EVENT_HQL_BY_UIDS = "from Event as ev where ev.uid in (:uids)";

  private static final Set<NotificationTrigger> SCHEDULED_EVENT_TRIGGERS =
      Sets.intersection(
          NotificationTrigger.getAllApplicableToEvent(),
          NotificationTrigger.getAllScheduledTriggers());

  public HibernateEventStore(
      EntityManager entityManager,
      JdbcTemplate jdbcTemplate,
      ApplicationEventPublisher publisher,
      AclService aclService) {
    super(entityManager, jdbcTemplate, publisher, Event.class, aclService, false);
  }

  @Override
  public long getEventCountLastUpdatedAfter(Date time) {
    CriteriaBuilder builder = getCriteriaBuilder();

    return getCount(
        builder,
        newJpaParameters()
            .addPredicate(root -> builder.greaterThanOrEqualTo(root.get("lastUpdated"), time))
            .count(builder::countDistinct));
  }

  @Override
  public boolean exists(String uid) {
    if (uid == null) {
      return false;
    }

    Query<?> query =
        nativeSynchronizedQuery(
            "select exists(select 1 from event where uid=:uid and deleted is false)");
    query.setParameter("uid", uid);

    return ((Boolean) query.getSingleResult()).booleanValue();
  }

  @Override
  public boolean existsIncludingDeleted(String uid) {
    if (uid == null) {
      return false;
    }

    Query<?> query = nativeSynchronizedQuery("select exists(select 1 from event where uid=:uid)");
    query.setParameter("uid", uid);

    return ((Boolean) query.getSingleResult()).booleanValue();
  }

  @Override
  public List<Event> getIncludingDeleted(List<String> uids) {
    List<Event> events = new ArrayList<>();
    List<List<String>> uidsPartitions = Lists.partition(Lists.newArrayList(uids), 20000);

    for (List<String> uidsPartition : uidsPartitions) {
      if (!uidsPartition.isEmpty()) {
        events.addAll(
            getSession()
                .createQuery(EVENT_HQL_BY_UIDS, Event.class)
                .setParameter("uids", uidsPartition)
                .list());
      }
    }

    return events;
  }

  @Override
  public List<Event> getWithScheduledNotifications(
      ProgramNotificationTemplate template, Date notificationDate) {
    if (notificationDate == null
        || !SCHEDULED_EVENT_TRIGGERS.contains(template.getNotificationTrigger())) {
      return Lists.newArrayList();
    }

    if (template.getRelativeScheduledDays() == null) {
      return Lists.newArrayList();
    }

    Date targetDate = DateUtils.addDays(notificationDate, template.getRelativeScheduledDays() * -1);

    String hql =
        "select distinct ev from Event as ev "
            + "inner join ev.programStage as ps "
            + "where :notificationTemplate in elements(ps.notificationTemplates) "
            + "and ev.scheduledDate is not null "
            + "and ev.occurredDate is null "
            + "and ev.status != :skippedEventStatus "
            + "and cast(:targetDate as date) = ev.scheduledDate "
            + "and ev.deleted is false";

    return getQuery(hql)
        .setParameter("notificationTemplate", template)
        .setParameter("skippedEventStatus", EventStatus.SKIPPED)
        .setParameter("targetDate", targetDate)
        .list();
  }

  @Override
  protected void preProcessPredicates(
      CriteriaBuilder builder, List<Function<Root<Event>, Predicate>> predicates) {
    predicates.add(root -> builder.equal(root.get("deleted"), false));
  }

  @Override
  protected Event postProcessObject(Event event) {
    return (event == null || event.isDeleted()) ? null : event;
  }
}
