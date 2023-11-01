package ru.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.practicum.model.event.Event;
import ru.practicum.model.event.EventSort;
import ru.practicum.model.event.EventState;
import ru.practicum.model.event.UpdateEventRequest;
import ru.practicum.service.EventService;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Primary
@Service
@RequiredArgsConstructor
public class EventStatServiceImpl implements EventService {

    private final EventServiceImpl eventService;
    private final StatService statService;

    @Override
    public Event getEvent(long eventId) {
        log.debug("Invoked method getEvent of class EventStatServiceImpl " +
                "with parameters: eventId = {};", eventId);
        Event event = eventService.getEvent(eventId);
        initViews(event);
        return event;
    }

    @Override
    public Event getEvent(long eventId, EventState state) {
        log.debug("Invoked method getEvent of class EventStatServiceImpl " +
                "with parameters: eventId = {}, state = {};", eventId, state);
        Event event = eventService.getEvent(eventId, state);
        initViews(event);
        return event;
    }

    @Override
    public Event getEvent(long userId, long eventId) {
        log.debug("Invoked method getEvent of class EventStatServiceImpl " +
                "with parameters: userId = {}, eventId = {};", userId, eventId);
        Event event = eventService.getEvent(userId, eventId);
        initViews(event);
        return event;
    }

    @Override
    public Collection<Event> getEvents(Set<Long> eventsId) {
        log.debug("Invoked method getEvents of class EventStatServiceImpl " +
                "with parameters: eventsId = {};", eventsId);
        Collection<Event> event = eventService.getEvents(eventsId);
        initViews(event);
        return event;
    }

    @Override
    public Collection<Event> getEvents(int from, int size) {
        log.debug("Invoked method getEvents of class EventStatServiceImpl " +
                "with parameters: from = {}, size = {};", from, size);
        Collection<Event> events = eventService.getEvents(from, size);
        initViews(events);
        return events;
    }

    @Override
    public Collection<Event> searchEvents(Collection<Long> users,
                                          Collection<EventState> states,
                                          Collection<Long> categories,
                                          LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                          int from, int size) {
        log.debug("Invoked method searchEvents of class EventStatServiceImpl " +
                        "with parameters: users = {}, states = {}, categories = {}, " +
                        "rangeStart = {}, rangeEnd = {}, from = {}, size = {}",
                users, states, categories, rangeStart, rangeEnd, from, size);

        Collection<Event> events =
                eventService.searchEvents(users, states, categories, rangeStart, rangeEnd, from, size);
        initViews(events);

        return events;
    }

    @Override
    public Collection<Event> searchEvents(String text,
                                          Collection<Long> categoriesId,
                                          Boolean paid,
                                          LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                          Boolean onlyAvailable,
                                          EventSort sort,
                                          int from, int size) {
        log.debug("Invoked method searchEvents of class EventStatServiceImpl " +
                        "with parameters: text = {}, categoriesId = {}, paid = {}, " +
                        "rangeStart = {}, rangeEnd = {}, " +
                        "onlyAvailable = {}, sort = {}, " +
                        "from = {}, size = {}",
                text, categoriesId, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);

        Collection<Event> events = eventService.searchEvents(text, categoriesId, paid, rangeStart, rangeEnd,
                onlyAvailable, sort, from, size);

        initViews(events);
        switch (sort) {
            case VIEWS:
                events = events.stream().sorted(Comparator.comparing(Event::getViews)).collect(Collectors.toList());
                break;
            case EVENT_DATE:
                events = events.stream().sorted(Comparator.comparing(Event::getEventDate)).collect(Collectors.toList());
                break;
        }
        return events;
    }

    @Override
    public Event addEvent(long userId, long catId, Event event) {
        log.debug("Invoked method addEvent of class EventServiceStatImpl " +
                "with parameters: userId = {}, catId = {}, event = {};", userId, catId, event);
        return eventService.addEvent(userId, catId, event);
    }

    @Override
    public Event updateEventByUser(long userId, long eventId, UpdateEventRequest dataEvent) {
        log.debug("Invoked method updateEventByUser of class EventStatServiceImpl " +
                "with parameters: userId = {}, eventId = {}, dataEvent = {};", userId, eventId, dataEvent);
        Event event = eventService.updateEventByUser(userId, eventId, dataEvent);
        initViews(event);
        return event;
    }

    @Override
    public Event updateEventByAdmin(long eventId, UpdateEventRequest dataEvent) {
        log.debug("Invoked method updateEventByAdmin of class EventStatServiceImpl " +
                "with parameters: eventId = {}, dataEvent = {};", eventId, dataEvent);
        Event event = eventService.updateEventByAdmin(eventId, dataEvent);
        initViews(event);
        return event;
    }

    public void initViews(Event event) {
        log.debug("Invoked method validateInitiator of class EventStatServiceImpl " +
                "with parameters: eventId = {};", event.getId());
        event.setViews(statService.getViews(event.getId(), event.getCreatedOn()));
    }

    public void initViews(Collection<Event> events) {
        log.debug("Invoked method initViews of class EventStatServiceImpl " +
                "with parameters: events = {};", events);
        if (events.isEmpty()) {
            return;
        }
        Map<Long, Long> viewsStat =
                statService.getViews(events.stream()
                                .map(Event::getId)
                                .collect(Collectors.toList()),
                        events.stream()
                                .map(Event::getCreatedOn)
                                .min(LocalDateTime::compareTo)
                                .get());

        events.forEach(event -> event.setViews(viewsStat.get(event.getId())));
    }
}
