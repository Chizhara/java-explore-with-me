package ru.practicum.controller.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.annotation.RequestMarker;
import ru.practicum.mapper.EventMapper;
import ru.practicum.model.event.UpdateEventRequest;
import ru.practicum.model.event.dto.EventFullDto;
import ru.practicum.model.event.dto.EventShortDto;
import ru.practicum.model.event.dto.NewEventDto;
import ru.practicum.service.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

@Slf4j
@RestController
@RequestMapping(path = "/users/{userId}/events")
@RequiredArgsConstructor
@Validated
public class PrivateEventController {

    private final EventService eventService;
    private final EventMapper eventMapper;

    @GetMapping
    public Collection<EventShortDto> getEvents(@PathVariable Long userId,
                                               @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                               @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("Invoked method getEvents of class PrivateEventController " +
                "with parameters: userId = {}, from = {}, size = {};", userId, from, size);
        return eventMapper.toEventShortDto(
                eventService.getEvents(from, size));
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEvent(@PathVariable Long userId,
                                 @PathVariable Long eventId) {
        log.info("Invoked method getEvent of class PrivateEventController " +
                "with parameters: userId = {}, eventId = {};", userId, eventId);
        return eventMapper.toEventFullDto(
                eventService.getEvent(userId, eventId));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto postEvent(@PathVariable Long userId,
                                  @Valid @RequestBody NewEventDto eventDto) {
        log.info("Invoked method postEvent of class PrivateEventController " +
                "with parameters: userId = {}, eventDto = {};", userId, eventDto);
        return eventMapper.toEventFullDto(
                eventService.addEvent(userId, eventDto.getCategory(), eventMapper.toEvent(eventDto)));
    }

    @PatchMapping("/{eventId}")
    @Validated({RequestMarker.UserRequest.class})
    public EventFullDto patchEvent(@PathVariable Long userId,
                                   @PathVariable Long eventId,
                                   @Valid @RequestBody UpdateEventRequest eventDto) {
        log.info("Invoked method patchEvent of class PrivateEventController " +
                "with parameters: userId = {}, eventId = {}, eventDto = {};", userId, eventId, eventDto);
        return eventMapper.toEventFullDto(
                eventService.updateEventByUser(userId, eventId, eventDto));
    }

}
