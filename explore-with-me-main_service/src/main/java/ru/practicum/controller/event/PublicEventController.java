package ru.practicum.controller.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mapper.EventMapper;
import ru.practicum.model.event.EventSort;
import ru.practicum.model.event.EventState;
import ru.practicum.model.event.dto.EventFullDto;
import ru.practicum.model.event.dto.EventShortDto;
import ru.practicum.param.EventParam;
import ru.practicum.service.EventService;
import ru.practicum.service.impl.StatService;

import javax.validation.ValidationException;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
@Validated
public class PublicEventController {

    private final EventService eventService;
    private final EventMapper eventMapper;
    private final StatService statService;

    @GetMapping
    public Collection<EventShortDto> getEvents(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) Collection<Long> categoriesId,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
            @RequestParam(defaultValue = "false") Boolean onlyAvailable,
            @RequestParam(defaultValue = "NONE") EventSort sort,
            @RequestParam(defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("Invoked method getEvents of class PublicEventController " +
                        "with parameters: text = {}, categoriesId = {}, paid = {}, " +
                        "rangeStart = {}, rangeEnd = {}, " +
                        "onlyAvailable = {}, sort = {}, " +
                        "from = {}, size = {}",
                text, categoriesId, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);

        if (rangeStart == null) {
            rangeStart = LocalDateTime.now();
        }

        if (rangeEnd != null && !rangeStart.isBefore(rangeEnd)) {
            throw new ValidationException(
                    String.format("Invalid request params values: range start = %s, rangeEnd = %s",
                            rangeStart, rangeEnd));
        }

        EventParam eventParam = new EventParam();
        eventParam.setSearchingText(text);
        eventParam.setPaid(paid);
        eventParam.setOnlyAvailable(onlyAvailable);
        eventParam.setSort(sort);
        eventParam.setCategoriesId(categoriesId);
        eventParam.setRangeStart(rangeStart);
        eventParam.setRangeEnd(rangeEnd);

        Collection<EventShortDto> events = eventMapper.toEventShortDto(
                eventService.searchEvents(eventParam, from, size));

        statService.save();
        return events;
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEvent(@PathVariable Long eventId) {
        log.info("Invoked method getEvent of class PublicEventController " +
                "with parameters: eventId = {};", eventId);

        EventFullDto event = eventMapper.toEventFullDto(
                eventService.getEvent(eventId, EventState.PUBLISHED));

        statService.save();
        return event;
    }
}
