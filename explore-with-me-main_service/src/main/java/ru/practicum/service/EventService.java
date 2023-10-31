package ru.practicum.service;

import org.springframework.stereotype.Service;
import ru.practicum.model.event.Event;
import ru.practicum.model.event.EventSort;
import ru.practicum.model.event.UpdateEventRequest;
import ru.practicum.model.event.EventState;

import java.time.LocalDateTime;
import java.util.Collection;

@Service

public interface EventService {
    Event getEvent(long eventId);

    Event getEvent(long eventId, EventState state);

    Event getEvent(long userId, long eventId);

    Collection<Event> getEvents(int from, int size);

    Collection<Event> searchEvents(Collection<Long> users,
                                   Collection<EventState> states,
                                   Collection<Long> categories,
                                   LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                   int from, int size);

    Collection<Event> searchEvents(String text,
                                   Collection<Long> states,
                                   Boolean paid,
                                   LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                   Boolean onlyAvailable,
                                   EventSort sort,
                                   int from, int size);

    Event addEvent(long userId, long catId, Event category);

    Event updateEventByUser(long userId, long eventId, UpdateEventRequest event);

    Event updateEventByAdmin(long eventId, UpdateEventRequest dataEvent);

}
