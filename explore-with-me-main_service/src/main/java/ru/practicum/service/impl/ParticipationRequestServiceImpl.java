package ru.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.InvalidActionException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.model.event.Event;
import ru.practicum.model.event.EventState;
import ru.practicum.model.request.EventRequestStatusUpdateRequest;
import ru.practicum.model.request.EventRequestStatusUpdateResult;
import ru.practicum.model.request.ParticipationRequest;
import ru.practicum.model.request.ParticipationRequestStatus;
import ru.practicum.repository.ParticipationRequestRepository;
import ru.practicum.service.ParticipationRequestService;
import ru.practicum.service.UserService;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParticipationRequestServiceImpl implements ParticipationRequestService {

    private final EventServiceImpl eventService;
    private final UserService userService;
    private final ParticipationRequestRepository participationRequestRepository;

    @Override
    public ParticipationRequest getRequest(long userId, long requestId) {
        log.debug("Invoked method getRequest of class ParticipationRequestServiceImpl " +
                "with parameters: userId = {}, requestId = {};", userId, requestId);
        return participationRequestRepository.findByIdAndRequesterId(requestId, userId).orElseThrow(
                () -> new NotFoundException("ParticipationRequest", requestId));
    }

    @Override
    public Collection<ParticipationRequest> getRequests(long userId) {
        log.debug("Invoked method getRequests of class ParticipationRequestServiceImpl " +
                "with parameters: userId = {};", userId);
        return participationRequestRepository.findAllByRequesterId(userId);
    }

    @Override
    public Collection<ParticipationRequest> getRequests(long userId, long eventId) {
        log.debug("Invoked method getRequests of class ParticipationRequestServiceImpl " +
                "with parameters: userId = {}, eventId = {};", userId, eventId);
        return participationRequestRepository.findAllByEventIdAndEventInitiatorId(eventId, userId);
    }

    @Override
    @Transactional
    public ParticipationRequest addRequest(long userId, long eventId) {
        log.debug("Invoked method addRequest of class ParticipationRequestServiceImpl " +
                "with parameters: userId = {}, eventId = {};", userId, eventId);
        ParticipationRequest request = initRequest(userId, eventId);
        validateParticipationRequest(request);
        initRequestStatus(request);
        return participationRequestRepository.save(request);
    }

    @Override
    @Transactional
    public ParticipationRequest updateRequestStatus(long userId, long eventId, ParticipationRequestStatus status) {
        log.debug("Invoked method updateRequestStatus of class ParticipationRequestServiceImpl " +
                "with parameters: userId = {}, eventId = {}, status = {};", userId, eventId, status);
        ParticipationRequest request = getRequest(userId, eventId);
        request.setStatus(status);
        return participationRequestRepository.save(request);
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResult updateRequests(long userId, long eventId,
                                                         EventRequestStatusUpdateRequest requestUpdateRequest) {
        log.debug("Invoked method updateRequests of class ParticipationRequestServiceImpl " +
                        "with parameters: userId = {}, eventId = {}, requestUpdateRequest = {};",
                userId, eventId, requestUpdateRequest);

        Event event = eventService.getEvent(userId, eventId);
        validateEventRequestsModeration(event);

        List<ParticipationRequest> requests = participationRequestRepository
                .findAllByEventIdAndEventInitiatorIdAndStatusNot(eventId, userId, ParticipationRequestStatus.CANCELED);

        Collection<ParticipationRequest> confirmedRequests = new LinkedList<>();
        Collection<ParticipationRequest> rejectedRequests = new LinkedList<>();
        Collection<ParticipationRequest> pendingRequests = new LinkedList<>();
        Collection<ParticipationRequest> tempRequests = new LinkedList<>();

        Collection<ParticipationRequest> updatedRequests =
                new ArrayList<>(requestUpdateRequest.getRequestIds().size() + 1);

        requests.forEach(request -> {
            switch (request.getStatus()) {
                case CONFIRMED:
                    validateRequestIdsNotIn(request, requestUpdateRequest.getRequestIds());
                    confirmedRequests.add(request);
                    break;
                case REJECTED:
                    validateRequestIdsNotIn(request, requestUpdateRequest.getRequestIds());
                    rejectedRequests.add(request);
                    break;
                case PENDING:
                    if (requestUpdateRequest.getRequestIds().contains(request.getId())) {
                        request.setStatus(requestUpdateRequest.getStatus());
                        updatedRequests.add(request);
                        tempRequests.add(request);
                    } else {
                        pendingRequests.add(request);
                    }
                    break;
            }
        });

        switch (requestUpdateRequest.getStatus()) {
            case CONFIRMED:
                confirmedRequests.addAll(updatedRequests);
            case REJECTED:
                rejectedRequests.addAll(updatedRequests);
        }

        if (event.getParticipantLimit() != 0 && confirmedRequests.size() >= event.getParticipantLimit()) {
            pendingRequests.forEach(pendingRequest -> pendingRequest.setStatus(ParticipationRequestStatus.REJECTED));
            tempRequests.addAll(pendingRequests);
        }

        participationRequestRepository.saveAll(tempRequests);

        return new EventRequestStatusUpdateResult(confirmedRequests, rejectedRequests);
    }

    private ParticipationRequest initRequest(long userId, long eventId) {
        log.trace("Invoked method initRequest of class ParticipationRequestServiceImpl " +
                "with parameters: userId = {}, eventId = {};", userId, eventId);
        Optional<ParticipationRequest> requestOptional = checkCanceledRequest(userId, eventId);
        if (requestOptional.isPresent()) {
            ParticipationRequest request = requestOptional.get();
            request.setStatus(ParticipationRequestStatus.PENDING);
            return request;
        }

        return ParticipationRequest.builder()
                .requester(userService.getUser(userId))
                .event(eventService.getEvent(eventId))
                .created(LocalDateTime.now())
                .build();
    }

    private void initRequestStatus(ParticipationRequest request) {
        log.trace("Invoked method initRequestStatus of class ParticipationRequestServiceImpl " +
                "with parameters: request = {};", request);
        if (request.getEvent().getRequestModeration() && request.getEvent().getParticipantLimit() != 0) {
            request.setStatus(ParticipationRequestStatus.PENDING);
        } else {
            request.setStatus(ParticipationRequestStatus.CONFIRMED);
        }
    }

    private Optional<ParticipationRequest> checkCanceledRequest(long userId, long eventId) {
        log.trace("Invoked method checkCanceledRequest of class ParticipationRequestServiceImpl " +
                "with parameters: userId = {}, eventId = {};", userId, eventId);
        return participationRequestRepository.findByEventIdAndEventInitiatorIdAndStatus(eventId, userId,
                ParticipationRequestStatus.CANCELED);
    }

    private void validateRequestIdsNotIn(ParticipationRequest request, Collection<Long> reqsId) {
        log.trace("Invoked method validateRequestIdsNotIn of class ParticipationRequestServiceImpl " +
                "with parameters: request = {}, reqsId = {};", request, reqsId);
        if (!reqsId.contains(request.getId())) {
            throw new InvalidActionException("Request must have status PENDING");
        }
    }

    private void validateParticipationRequest(ParticipationRequest request) {
        log.trace("Invoked method validateParticipationRequest of class ParticipationRequestServiceImpl " +
                "with parameters: request = {};", request);
        if (request.getEvent().getState() != EventState.PUBLISHED) {
            throw new InvalidActionException(
                    String.format("Cannot create a participation request for an event with id = %d",
                            request.getEvent().getId()));
        }
        if (request.getRequester().getId().equals(request.getEvent().getInitiator().getId())) {
            throw new InvalidActionException(
                    String.format("Initiator with id = %d cannot apply for participation in event: eventId = %d",
                            request.getRequester().getId(), request.getEvent().getInitiator().getId()));
        }
        validateEventRequestsCount(request.getEvent());
    }

    private void validateEventRequestsModeration(Event event) {
        log.trace("Invoked method validateEventRequestsModeration of class ParticipationRequestServiceImpl " +
                "with parameters: event = {};", event);
        if (event.getState() != EventState.PUBLISHED) {
            throw new InvalidActionException(
                    String.format("Event with id = %d should be PUBLISHED", event.getId()));
        }
        if (!event.getRequestModeration()) {
            throw new InvalidActionException(
                    String.format("Moderation is disabled at the event with id = %d", event.getId()));
        }
        validateEventRequestsCount(event);
    }


    private void validateEventRequestsCount(Event event) {
        log.trace("Invoked method validateEventRequestsCount of class ParticipationRequestServiceImpl " +
                "with parameters: event = {};", event);
        event.getConfirmedRequests().forEach(request -> System.out.println(request.getId()));
        if (event.getParticipantLimit() != 0 && event.getConfirmedRequests().size() >= event.getParticipantLimit()) {
            throw new InvalidActionException("The participant limit has been reached");
        }
    }

}
