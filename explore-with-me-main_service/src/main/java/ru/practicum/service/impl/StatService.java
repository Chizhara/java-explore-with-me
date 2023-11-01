package ru.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.StatClient;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatService {
    private static final String APP = "ewm-main-service";
    protected final HttpServletRequest request;
    protected final StatClient statClient;

    public void save() {
        log.debug("Invoked method getViews of class StatService " +
                "with parameters:  uri = {};", request.getRequestURI());

        EndpointHitDto endpointHitDto = EndpointHitDto.builder()
                .app(APP)
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now())
                .build();
        log.info("asd {}", endpointHitDto);
        statClient.postHit(endpointHitDto);
    }

    public Map<Long, Long> getViews(Collection<Long> eventsId, LocalDateTime rangeStart) {
        log.debug("Invoked method getViews of class StatService " +
                "with parameters: eventsId = {}, rangeStart = {};", eventsId, rangeStart);
        Collection<String> endpoints = eventsId.stream()
                .map((id) -> "/events/" + id)
                .collect(Collectors.toList());
        Collection<ViewStatsDto> viewStats = statClient
                .getViewStats(rangeStart, LocalDateTime.now().plusMinutes(1), endpoints, true);

        Map<Long, Long> idsViews = new HashMap<>(eventsId.size() + 1, 1);

        for (ViewStatsDto viewStatsDto : viewStats) {
            idsViews.put((long) Integer.parseInt(viewStatsDto.getUri().split("/")[2]), viewStatsDto.getHits());
        }
        return idsViews;
    }

    public Long getViews(long eventId, LocalDateTime rangeStart) {
        log.debug("Invoked method getViews of class StatService " +
                "with parameters: eventId = {}, rangeStart = {};", eventId, rangeStart);
        List<ViewStatsDto> viewStatsDto = statClient.getViewStats(rangeStart, LocalDateTime.now().plusMinutes(1),
                List.of("/events/" + eventId), true);
        if (viewStatsDto.isEmpty()) {
            return 0L;
        }
        return viewStatsDto.get(0).getHits();
    }
}
