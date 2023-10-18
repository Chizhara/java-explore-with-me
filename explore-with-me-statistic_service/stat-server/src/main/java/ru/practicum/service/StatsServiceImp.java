package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.model.EndpointHit;
import ru.practicum.model.ViewStats;
import ru.practicum.repository.EndpointHitRepository;

import java.time.LocalDateTime;
import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatsServiceImp implements StatsService {

    private final EndpointHitRepository endpointHitRepository;

    @Override
    public void addEndpointHit(EndpointHit endpointHit) {
        log.debug("Invoked method addEndpointHit of class StatsServiceImp " +
                "with parameters: endpointHit = {}", endpointHit);
        endpointHitRepository.save(endpointHit);
    }

    @Override
    public Collection<ViewStats> getViewStats(LocalDateTime start, LocalDateTime end, String[] uris, boolean unique) {
        log.debug("Invoked method getViewStats of class StatsServiceImp " +
                "with parameters: start = {}, end = {}, uris = {}, unique = {}", start, end, uris, unique);

        if (uris == null) {
            if (unique) {
                return endpointHitRepository.findAllViewStatsByUniqueIp(start, end);
            } else {
                return endpointHitRepository.findAllViewStats(start, end);
            }
        } else {
            if (unique) {
                return endpointHitRepository.findAllViewStatsByUniqueIp(start, end, uris);
            } else {
                return endpointHitRepository.findAllViewStats(start, end, uris);
            }
        }
    }

}
