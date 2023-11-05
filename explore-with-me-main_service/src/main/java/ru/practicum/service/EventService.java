package ru.practicum.service;

import org.springframework.stereotype.Service;
import ru.practicum.model.event.Event;
import ru.practicum.model.event.EventState;
import ru.practicum.model.event.UpdateEventRequest;
import ru.practicum.param.EventParam;

import java.util.Collection;
import java.util.Set;

@Service

public interface EventService {
    Event getEvent(long eventId);

    Event getEvent(long eventId, EventState state);

    Collection<Event> getEvents(Set<Long> eventsId);

    Event getEvent(long userId, long eventId);

    Collection<Event> getEvents(int from, int size);

    Collection<Event> searchEvents(EventParam eventParam,
                                   int from, int size);

    Event addEvent(long userId, long catId, Event category);

    Event updateEventByUser(long userId, long eventId, UpdateEventRequest event);

    Event updateEventByAdmin(long eventId, UpdateEventRequest dataEvent);

}
