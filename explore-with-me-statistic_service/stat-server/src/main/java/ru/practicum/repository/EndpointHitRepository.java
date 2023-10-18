package ru.practicum.repository;

import ru.practicum.model.EndpointHit;
import ru.practicum.model.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

public interface EndpointHitRepository {

    void save(EndpointHit endpointHit);

    List<ViewStats> findAllViewStats(LocalDateTime start, LocalDateTime end);

    List<ViewStats> findAllViewStatsByUniqueIp(LocalDateTime start, LocalDateTime end);

    List<ViewStats> findAllViewStats(LocalDateTime start, LocalDateTime end, String[] uris);

    List<ViewStats> findAllViewStatsByUniqueIp(LocalDateTime start, LocalDateTime end, String[] uris);

}
