package ru.practicum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class StatClient {

    private static final String BASE_URL = "http://localhost:9090";

    public final WebClient webClient;

    @Autowired
    public StatClient() {
        webClient = WebClient.builder()
                .baseUrl(BASE_URL)
                .build();
    }

    public void postHit(EndpointHitDto endpointHitDto) {
        webClient.post()
                .uri("/hit")
                .bodyValue(endpointHitDto)
                .retrieve()
                .toBodilessEntity().block();
    }

    public List<ViewStatsDto> getViewStats(LocalDateTime start, LocalDateTime end, boolean unique) {
        return getViewStats(start, end, null, unique);
    }

    public List<ViewStatsDto> getViewStats(LocalDateTime start, LocalDateTime end, Collection<String> array) {
        return getViewStats(start, end, array, null);
    }

    public List<ViewStatsDto> getViewStats(LocalDateTime start, LocalDateTime end, Collection<String> array, Boolean unique) {
        ResponseEntity<List<ViewStatsDto>> response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/stats")
                        .queryParam("start", start.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                        .queryParam("end", end.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                        .queryParamIfPresent("uris", Optional.of(array))
                        .queryParamIfPresent("unique", Optional.of(unique))
                        .build())
                .retrieve()
                .toEntityList(ViewStatsDto.class)
                .block();

        return response != null ? response.getBody() : Collections.emptyList();
    }

}
