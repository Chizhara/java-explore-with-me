package ru.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.PageableGenerator;
import ru.practicum.exception.*;
import ru.practicum.model.event.Event;
import ru.practicum.model.event.EventState;
import ru.practicum.model.event.StateAction;
import ru.practicum.model.event.UpdateEventRequest;
import ru.practicum.param.EventParam;
import ru.practicum.repository.EventRepository;
import ru.practicum.service.CategoryService;
import ru.practicum.service.EventService;
import ru.practicum.service.UserService;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final UserService userService;
    private final CategoryService categoryService;

    private final EventRepository eventRepository;

    @Override
    public Event getEvent(long eventId) {
        log.debug("Invoked method getEvent of class EventServiceImpl " +
                "with parameters: eventId = {};", eventId);
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event", eventId));
    }

    @Override
    public Event getEvent(long eventId, EventState state) {
        log.debug("Invoked method getEvent of class EventServiceImpl " +
                "with parameters: eventId = {}, state = {};", eventId, state);
        return eventRepository.findByIdAndState(eventId, state)
                .orElseThrow(() -> new NotFoundException("Event", eventId));
    }

    @Override
    public Event getEvent(long userId, long eventId) {
        log.debug("Invoked method getEvent of class EventServiceImpl " +
                "with parameters: eventId = {}, eventId = {};", eventId, eventId);
        Event event = getEvent(eventId);
        validateInitiator(event, userId);
        return event;
    }

    @Override
    public Collection<Event> getEvents(int from, int size) {
        log.debug("Invoked method getEvents of class EventServiceImpl " +
                "with parameters: from = {}, size = {};", from, size);
        return eventRepository.findAll(PageableGenerator.getPageable(from, size)).getContent();
    }

    @Override
    public Collection<Event> getEvents(Set<Long> eventsId) {
        log.debug("Invoked method getEvents of class EventServiceImpl " +
                "with parameters: eventsId = {};", eventsId);
        return eventRepository.findAllById(eventsId);
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<Event> searchEvents(EventParam eventParam,
                                          int from, int size) {
        log.debug("Invoked method searchEvents of class EventServiceImpl " +
                "with parameters: eventParam {}, from = {}, size = {}", eventParam, from, size);

        return eventRepository.findAll(eventParam.getExpression(),
                PageableGenerator.getPageable(from, size)).getContent();
    }

    @Override
    @Transactional
    public Event addEvent(long userId, long catId, Event event) {
        log.debug("Invoked method addEvent of class EventServiceImpl " +
                "with parameters: userId = {}, catId = {}, event = {};", userId, catId, event);
        initEventFields(event, userId, catId);
        return eventRepository.save(event);
    }

    @Override
    @Transactional
    public Event updateEventByUser(long userId, long eventId, UpdateEventRequest dataEvent) {
        log.debug("Invoked method updateEventByUser of class EventServiceImpl " +
                "with parameters: userId = {}, eventId = {}, dataEvent = {};", userId, eventId, dataEvent);
        Event updatedEvent = getEvent(eventId);
        validateInitiator(updatedEvent, userId);
        isEventUpdatable(updatedEvent);
        updateEventFields(updatedEvent, dataEvent);
        validateEventDate(updatedEvent.getEventDate());
        return eventRepository.save(updatedEvent);
    }

    @Override
    @Transactional
    public Event updateEventByAdmin(long eventId, UpdateEventRequest dataEvent) {
        log.debug("Invoked method updateEventByAdmin of class EventServiceImpl " +
                "with parameters: eventId = {}, dataEvent = {};", eventId, dataEvent);
        Event updatedEvent = getEvent(eventId);
        updateEventFields(updatedEvent, dataEvent);
        validateEventDate(updatedEvent.getEventDate());
        return eventRepository.save(updatedEvent);
    }

    private void initEventFields(Event event, long userId, long catId) {
        log.trace("Invoked method initEventFields of class EventServiceImpl " +
                "with parameters: event = {}, userId = {}, catId = {};", event, userId, catId);
        event.setCreatedOn(LocalDateTime.now());
        event.setState(EventState.PENDING);
        initEventForeignDependencies(event, userId, catId);
    }

    private void initEventForeignDependencies(Event event, long userId, long catId) {
        log.trace("Invoked method initEventForeignDependencies of class EventServiceImpl " +
                "with parameters: event = {}, userId = {}, catId = {};", event, userId, catId);
        event.setInitiator(userService.getUser(userId));
        event.setCategory(categoryService.getCategory(catId));
    }

    private void validateInitiator(Event event, long userId) {
        log.trace("Invoked method validateInitiator of class EventServiceImpl " +
                "with parameters: event = {}, userId = {};", event, userId);
        if (event.getInitiator().getId() != userId) {
            throw new ForbiddenAccessException(userId, event.getId());
        }
    }

    private void validateEventDate(LocalDateTime eventDate) {
        log.trace("Invoked method validateEventDate of class EventServiceImpl " +
                "with parameters: eventDate = {};", eventDate);
        if (eventDate.isBefore(LocalDateTime.now().plusHours(2))) {
            throw new InvalidValueException("eventDate", eventDate);
        }
    }

    private void isEventUpdatable(Event event) {
        log.trace("Invoked method isEventUpdatable of class EventServiceImpl " +
                "with parameters: event = {};", event);
        if (event.getState() == EventState.PUBLISHED) {
            throw new InvalidActionException("Event must not be PUBLISHED");
        }
    }

    private void updateEventFields(Event updatedEvent, UpdateEventRequest dataEvent) {
        log.trace("Invoked method updateEventFields of class EventServiceImpl " +
                "with parameters: updatedEvent = {}, dataEvent = {};", updatedEvent, dataEvent);
        if (dataEvent.getTitle() != null) {
            updatedEvent.setTitle(dataEvent.getTitle());
        }
        if (dataEvent.getAnnotation() != null) {
            updatedEvent.setAnnotation(dataEvent.getAnnotation());
        }
        if (dataEvent.getCategory() != null) {
            updatedEvent.setCategory(categoryService.getCategory(dataEvent.getCategory()));
        }
        if (dataEvent.getDescription() != null) {
            updatedEvent.setDescription(dataEvent.getDescription());
        }
        if (dataEvent.getLocation() != null) {
            updatedEvent.setLat(dataEvent.getLocation().getLat());
            updatedEvent.setLon(dataEvent.getLocation().getLon());
        }
        if (dataEvent.getEventDate() != null) {
            updatedEvent.setEventDate(dataEvent.getEventDate());
        }
        if (dataEvent.getPaid() != null) {
            updatedEvent.setPaid(dataEvent.getPaid());
        }
        if (dataEvent.getParticipantLimit() != null) {
            updatedEvent.setParticipantLimit(dataEvent.getParticipantLimit());
        }
        if (dataEvent.getRequestModeration() != null) {
            updatedEvent.setRequestModeration(dataEvent.getRequestModeration());
        }
        if (dataEvent.getStateAction() != null) {
            updateEventState(updatedEvent, dataEvent.getStateAction());
        }
    }

    private void updateEventState(Event updatedEvent, StateAction stateAction) {
        log.trace("Invoked method updateEventFields of class EventServiceImpl " +
                "with parameters: updatedEvent = {}, stateAction = {};", updatedEvent, stateAction);

        switch (stateAction) {
            case PUBLISH_EVENT:
                if (updatedEvent.getState() != EventState.PENDING) {
                    throw new EventIncorrectException("publish", updatedEvent.getState());
                }
                if (updatedEvent.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
                    throw new InvalidValueException("eventDate", updatedEvent.getEventDate());
                }
                updatedEvent.setState(EventState.PUBLISHED);
                break;
            case REJECT_EVENT:
                if (updatedEvent.getState() == EventState.PUBLISHED) {
                    throw new EventIncorrectException("reject", updatedEvent.getState());
                }
                updatedEvent.setState(EventState.CANCELED);
                break;
            case CANCEL_REVIEW:
                isEventUpdatable(updatedEvent);
                updatedEvent.setState(EventState.CANCELED);
                break;
            case SEND_TO_REVIEW:
                isEventUpdatable(updatedEvent);
                updatedEvent.setState(EventState.PENDING);
                break;
        }
    }
}
