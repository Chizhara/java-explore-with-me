package ru.practicum.controller.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mapper.ParticipationRequestMapper;
import ru.practicum.model.request.dto.EventRequestStatusUpdateResultDto;
import ru.practicum.model.request.EventRequestStatusUpdateRequest;
import ru.practicum.model.request.dto.ParticipationRequestDto;
import ru.practicum.service.ParticipationRequestService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping(path = "/users/{userId}/events/{eventId}/requests")
@RequiredArgsConstructor
@Validated
public class ModerationRequestController {
    private final ParticipationRequestMapper requestMapper;
    private final ParticipationRequestService requestService;
    @GetMapping
    public Collection<ParticipationRequestDto> getRequests(@PathVariable Long userId,
                                                           @PathVariable Long eventId) {
        log.info("Invoked method getRequests of class ModerationRequestController " +
                "with parameters: userId = {}, eventId = {};", userId, eventId);
        return requestMapper.toParticipationRequestDto(
                requestService.getRequests(userId, eventId));
    }

    @PatchMapping
    public EventRequestStatusUpdateResultDto patchRequests(@PathVariable Long userId,
                                                           @PathVariable Long eventId,
                                                           @RequestBody EventRequestStatusUpdateRequest requestUpdateRequest) {
        log.info("Invoked method patchRequests of class ModerationRequestController " +
                "with parameters: userId = {}, eventId = {}, requestUpdateRequest = {};", userId, eventId, requestUpdateRequest);

        return requestMapper.toEventRequestStatusUpdateResultDto(requestService.updateRequests(userId, eventId, requestUpdateRequest));
    }
}
