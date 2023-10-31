package ru.practicum.service;

import ru.practicum.model.request.EventRequestStatusUpdateRequest;
import ru.practicum.model.request.EventRequestStatusUpdateResult;
import ru.practicum.model.request.ParticipationRequest;
import ru.practicum.model.request.ParticipationRequestStatus;

import java.util.Collection;


public interface ParticipationRequestService {
    ParticipationRequest addRequest(long userId, long eventId);

    Collection<ParticipationRequest> getRequests(long userId);

    ParticipationRequest updateRequestStatus(long userId, long eventId, ParticipationRequestStatus status);

    ParticipationRequest getRequest(long userId, long eventId);

    Collection<ParticipationRequest> getRequests(long userId, long eventId);

    EventRequestStatusUpdateResult updateRequests(long userId, long eventId,
                                                  EventRequestStatusUpdateRequest requestUpdateRequest);
}
