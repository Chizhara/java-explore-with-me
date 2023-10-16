package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.service.StatsService;

import java.time.LocalDateTime;
import java.util.Collection;

import static ru.practicum.mapper.EndpointHitMapper.toEndpointHit;
import static ru.practicum.mapper.EndpointHitMapper.toEndpointHitDto;
import static ru.practicum.mapper.StatsViewMapper.toViewStatsDto;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
public class StatsController {

    private final StatsService statsService;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public EndpointHitDto postEndpointHit(@RequestBody EndpointHitDto endpointHitDto) {
        log.info("Invoked method postEndpointHit of class StatsController " +
                "with parameters: endpointHitDto = {}", endpointHitDto);
        return toEndpointHitDto(statsService.addEndpointHit(toEndpointHit(endpointHitDto)));
    }

    @GetMapping("/stats")
    @ResponseStatus(HttpStatus.OK)
    public Collection<ViewStatsDto> getStatsView(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                                 @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                                 @RequestParam(required = false) String[] uris,
                                                 @RequestParam(required = false, defaultValue = "false") boolean unique) {
        log.info("Invoked method getStatsView of class StatsController " +
                "with parameters: start = {}, end = {}, uris = {}, unique = {}", start, end, uris, unique);
        return toViewStatsDto(statsService.getViewStats(start, end, uris, unique));
    }
}
