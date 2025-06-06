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
package org.hisp.dhis.system.notification;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;
import org.hisp.dhis.scheduling.JobType;
import org.junit.jupiter.api.Test;

/**
 * Tests properties of {@link Notification}s.
 *
 * @author Jan Bernitt
 */
class NotificationTest {
  @Test
  void testNotificationAreNaturallySortedNewestFirst() {
    Date now = new Date();

    Notification a = createNotificationWithTime(now);
    Notification b = createNotificationWithTime(new Date(now.getTime() - 1000L));
    Notification c = createNotificationWithTime(new Date(now.getTime() + 1000L));

    assertEquals(List.of(c, a, b), Stream.of(a, b, c).sorted().collect(toList()));
  }

  @Test
  void testCompletedNotificationsAreSortedAsLatestWhenSameTime() {
    Date now = new Date();

    Notification a = createNotificationWithTime(now);
    a.setMessage("message should be sorted 2nd");
    Notification b = createNotificationWithTime(now);
    b.setMessage("message should be sorted 1st");
    b.setCompleted(true);
    Notification c = createNotificationWithTime(new Date(now.getTime() - 1000L));
    c.setMessage("message should be sorted 3rd");

    List<Notification> notifications = new ArrayList<>(List.of(c, a, b));
    Collections.sort(notifications);

    Notification firstNotification = notifications.get(0);
    assertEquals("message should be sorted 1st", firstNotification.getMessage());
  }

  private Notification createNotificationWithTime(Date now) {
    Notification notification = new Notification();
    notification.setCategory(JobType.ACCOUNT_EXPIRY_ALERT);
    notification.setTimestamp(now.getTime());
    return notification;
  }
}
