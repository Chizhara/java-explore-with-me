package ru.practicum.service;

import ru.practicum.model.EndpointHit;
import ru.practicum.model.ViewStats;

import java.time.LocalDateTime;
import java.util.Collection;

public interface StatsService {

    EndpointHit addEndpointHit(EndpointHit endpointHit);

    Collection<ViewStats> getViewStats(LocalDateTime start, LocalDateTime end, String[] uris, boolean unique);

}
