package ru.practicum.controller.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.annotation.RequestMarker;
import ru.practicum.mapper.EventMapper;
import ru.practicum.model.event.EventState;
import ru.practicum.model.event.UpdateEventRequest;
import ru.practicum.model.event.dto.EventFullDto;
import ru.practicum.service.EventService;

import javax.validation.Valid;
import javax.validation.ValidationException;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.Collection;

@Slf4j
@RestController
@RequestMapping(path = "/admin/events")
@RequiredArgsConstructor
@Validated
public class AdminEventController {

    private final EventService eventService;
    private final EventMapper eventMapper;

    @GetMapping
    public Collection<EventFullDto> getEvents(
            @RequestParam(name = "users", required = false) Collection<Long> usersId,
            @RequestParam(required = false) Collection<EventState> states,
            @RequestParam(name = "categories", required = false) Collection<Long> categoriesId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
            @RequestParam(defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("Invoked method getEvents of class AdminEventController " +
                        "with parameters: usersId = {}, states = {}, categoriesId = {}, " +
                        "rangeStart = {}, rangeEnd = {}, from = {}, size = {}",
                usersId, states, categoriesId, rangeStart, rangeEnd, from, size);

        if (rangeStart != null && rangeEnd != null && !rangeStart.isBefore(rangeEnd)) {
            throw new ValidationException(
                    String.format("Invalid request params values: range start = %s, rangeEnd = %s",
                            rangeStart, rangeEnd));
        }

        return eventMapper.toEventFullDto(
                eventService.searchEvents(usersId, states, categoriesId, rangeStart, rangeEnd, from, size));
    }

    @PatchMapping("/{eventId}")
    @Validated({RequestMarker.AdminRequest.class})
    public EventFullDto patchEvent(@PathVariable Long eventId,
                                   @Valid @RequestBody UpdateEventRequest eventDto) {
        log.info("Invoked method patchEvent of class AdminEventController " +
                "with parameters: eventId = {}, eventDto = {};", eventId, eventDto);
        return eventMapper.toEventFullDto(
                eventService.updateEventByAdmin(eventId, eventDto));
    }

}
