/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.xwiki.notifications.script;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.xwiki.bridge.DocumentAccessBridge;
import org.xwiki.component.annotation.Component;
import org.xwiki.eventstream.Event;
import org.xwiki.eventstream.EventStatus;
import org.xwiki.eventstream.EventStatusManager;
import org.xwiki.eventstream.internal.DefaultEvent;
import org.xwiki.eventstream.internal.DefaultEventStatus;
import org.xwiki.model.reference.EntityReferenceSerializer;
import org.xwiki.notifications.NotificationException;
import org.xwiki.notifications.NotificationManager;
import org.xwiki.notifications.NotificationRenderer;
import org.xwiki.notifications.internal.ModelBridge;
import org.xwiki.rendering.block.Block;
import org.xwiki.script.service.ScriptService;
import org.xwiki.stability.Unstable;

/**
 * Script services for the notifications.
 *
 * @version $Id$
 * @since 9.2RC1
 */
@Component
@Singleton
@Named("notification")
@Unstable
public class NotificationScriptService implements ScriptService
{
    @Inject
    private NotificationManager notificationManager;

    @Inject
    private NotificationRenderer notificationRenderer;

    @Inject
    private EventStatusManager eventStatusManager;

    @Inject
    private DocumentAccessBridge documentAccessBridge;

    @Inject
    private EntityReferenceSerializer<String> entityReferenceSerializer;

    @Inject
    private ModelBridge modelBridge;

    /**
     * @param offset the offset
     * @param limit the number of events to get
     * @return a list of events concerning the current user and to display as notifications
     * @throws NotificationException if error happens
     */
    public List<Event> getEvents(int offset, int limit) throws NotificationException
    {
        return notificationManager.getEvents(offset, limit);
    }

    /**
     * Return the number of events to display as notifications concerning the current user.
     *
     * @param onlyUnread either if only unread events should be counted or all events
     * @return the list of events to display as notifications
     * @throws NotificationException if an error happens
     */
    public long getEventsCount(boolean onlyUnread) throws NotificationException
    {
        return notificationManager.getEventsCount(onlyUnread);
    }

    /**
     * @param userId id of the user
     * @param offset the offset
     * @param limit the number of events to get
     * @return a list of events concerning a given user and to display as notifications
     * @throws NotificationException if error happens
     */
    public List<Event> getEvents(String userId, int offset, int limit) throws NotificationException
    {
        return notificationManager.getEvents(userId, offset, limit);
    }

    /**
     * Generate a rendering Block for a given event to display as notification.
     * @param event the event to render
     * @return a rendering block ready to display the event
     * @throws NotificationException if an error happens
     */
    public Block render(Event event) throws NotificationException
    {
        return notificationRenderer.render(event);
    }

    /**
     * Get the list of statuses concerning the given events and the current user.
     *
     * @param events a list of events
     * @return the list of statuses corresponding to each pair or event/entity
     *
     * @throws Exception if an error occurs
     */
    public List<EventStatus> getEventStatuses(List<Event> events) throws Exception
    {
        return eventStatusManager.getEventStatus(events,
                Arrays.asList(entityReferenceSerializer.serialize(documentAccessBridge.getCurrentUserReference())));
    }

    /**
     * Save a status for the current user.
     * @param eventId id of the event
     * @param isRead either or not the current user has read the given event
     * @throws Exception if an error occurs
     */
    public void saveEventStatus(String eventId, boolean isRead) throws Exception
    {
        DefaultEvent event = new DefaultEvent();
        event.setId(eventId);
        String userId = entityReferenceSerializer.serialize(documentAccessBridge.getCurrentUserReference());
        eventStatusManager.saveEventStatus(new DefaultEventStatus(event, userId, isRead));
    }

    /**
     * Set the start date for the current user.
     *
     * @param startDate the date before which we ignore notifications
     * @throws NotificationException if an error occurs
     */
    public void setStartDate(Date startDate) throws NotificationException
    {
        modelBridge.setStartDateForUser(documentAccessBridge.getCurrentUserReference(), startDate);
    }

    /**
     * Set the start date for the given user.
     *
     * @param userId id of the user
     * @param startDate the date before which we ignore notifications
     * @throws NotificationException if an error occurs
     */
    public void setStartDate(String userId, Date startDate) throws NotificationException
    {
        notificationManager.setStartDate(userId, startDate);
    }
}
