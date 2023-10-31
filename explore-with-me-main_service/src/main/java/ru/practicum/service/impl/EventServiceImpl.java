package ru.practicum.service.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.PageableGenerator;
import ru.practicum.exception.*;
import ru.practicum.model.event.*;
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
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event", eventId));
    }

    @Override
    public Event getEvent(long eventId, EventState state) {
        return eventRepository.findByIdAndState(eventId, state)
                .orElseThrow(() -> new NotFoundException("Event", eventId));
    }

    @Override
    public Event getEvent(long userId, long eventId) {
        Event event = getEvent(eventId);
        validateInitiator(event, userId);
        return event;
    }

    @Override
    public Collection<Event> getEvents(int from, int size) {
        return eventRepository.findAll(PageableGenerator.getPageable(from, size)).getContent();
    }

    @Override
    public Collection<Event> getEvents(Set<Long> eventsId) {
        return eventRepository.findAllById(eventsId);
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<Event> searchEvents(Collection<Long> users,
                                          Collection<EventState> states,
                                          Collection<Long> categoriesId,
                                          LocalDateTime rangeStart,
                                          LocalDateTime rangeEnd,
                                          int from, int size) {
        BooleanExpression booleanExpression = QEvent.event.isNotNull();
        if (users != null) {
            booleanExpression = booleanExpression.and(QEvent.event.initiator.id.in(users));
        }
        if (states != null) {
            booleanExpression = booleanExpression.and(QEvent.event.state.in(states));
        }
        if (categoriesId != null) {
            booleanExpression = booleanExpression.and(QEvent.event.category.id.in(categoriesId));
        }

        return eventRepository.findAll(booleanExpression,
                PageableGenerator.getPageable(from, size)).getContent();
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<Event> searchEvents(String text,
                                          Collection<Long> categoriesId,
                                          Boolean paid,
                                          LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                          Boolean onlyAvailable,
                                          EventSort sort,
                                          int from, int size) {
        log.debug("Invoked method searchEvents of class EventServiceImpl " +
                        "with parameters: text = {}, categoriesId = {}, paid = {}, " +
                        "rangeStart = {}, rangeEnd = {}, " +
                        "onlyAvailable = {}, sort = {}, " +
                        "from = {}, size = {}",
                text, categoriesId, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);

        BooleanExpression booleanExpression = QEvent.event.state.eq(EventState.PUBLISHED);
        if (text != null) {
            booleanExpression = booleanExpression.andAnyOf(QEvent.event.annotation.containsIgnoreCase(text)
                    .or(QEvent.event.description.containsIgnoreCase(text)));
        }
        if (categoriesId != null) {
            booleanExpression = booleanExpression.and(QEvent.event.category.id.in(categoriesId));
        }
        if (paid != null) {
            booleanExpression = booleanExpression.and(QEvent.event.paid.eq(paid));
        }
        if (onlyAvailable != null && onlyAvailable) {
            booleanExpression = booleanExpression
                    .and(QEvent.event.participantLimit.eq(0)
                            .or(QEvent.event.confirmedRequests.size().loe(QEvent.event.participantLimit)));
        }
        if (rangeEnd != null) {
            booleanExpression = booleanExpression.and(QEvent.event.eventDate.before(rangeEnd));
        }

        booleanExpression = booleanExpression.and(QEvent.event.eventDate.after(rangeStart));

        return eventRepository.findAll(booleanExpression, PageableGenerator.getPageable(from, size)).getContent();
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
        event.setCreatedOn(LocalDateTime.now());
        event.setState(EventState.PENDING);
        initEventForeignDependencies(event, userId, catId);
    }

    private void initEventForeignDependencies(Event event, long userId, long catId) {
        event.setInitiator(userService.getUser(userId));
        event.setCategory(categoryService.getCategory(catId));
    }

    private void validateInitiator(Event event, long userId) {
        if (event.getInitiator().getId() != userId) {
            throw new ForbiddenAccessException(userId, event.getId());
        }
    }

    private void validateEventDate(LocalDateTime eventDate) {
        if (eventDate.isBefore(LocalDateTime.now().plusHours(2))) {
            throw new InvalidValueException("eventDate", eventDate);
        }
    }

    private void isEventUpdatable(Event event) {
        if (event.getState() == EventState.PUBLISHED) {
            throw new InvalidActionException("Event must not be PUBLISHED");
        }
    }

    private void updateEventFields(Event updatedEvent, UpdateEventRequest dataEvent) {
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
