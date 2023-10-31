package ru.practicum.controller.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mapper.ParticipationRequestMapper;
import ru.practicum.model.request.dto.ParticipationRequestDto;
import ru.practicum.model.request.ParticipationRequestStatus;
import ru.practicum.service.ParticipationRequestService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping(path = "/users/{userId}/requests")
@RequiredArgsConstructor
@Validated
public class PrivateRequestController {

    private final ParticipationRequestService participationRequestService;
    private final ParticipationRequestMapper participationRequestMapper;

    @GetMapping
    public Collection<ParticipationRequestDto> getRequests(@PathVariable Long userId) {
        log.info("Invoked method getRequests of class PrivateRequestController " +
                "with parameters: userI d= {};", userId);
        return participationRequestMapper.toParticipationRequestDto(
                participationRequestService.getRequests(userId));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ParticipationRequestDto postRequest(@PathVariable Long userId,
                                               @RequestParam Long eventId) {
        log.info("Invoked method postRequest of class PrivateRequestController " +
                "with parameters: userId = {}, eventId = {};", userId, eventId);
        return participationRequestMapper.toParticipationRequestDto(
                participationRequestService.addRequest(userId, eventId));
    }

    @PatchMapping("{eventId}/cancel")
    public ParticipationRequestDto rejectEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        log.info("Invoked method rejectEvent of class PrivateRequestController " +
                "with parameters: userId = {}, eventId = {};", userId, eventId);
        return participationRequestMapper.toParticipationRequestDto(
                participationRequestService.updateRequestStatus(userId, eventId, ParticipationRequestStatus.CANCELED));
    }
}
