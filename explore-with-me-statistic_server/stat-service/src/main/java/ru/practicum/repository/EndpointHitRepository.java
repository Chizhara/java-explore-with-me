package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.EndpointHit;
import ru.practicum.model.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

public interface EndpointHitRepository extends JpaRepository<EndpointHit, Long> {

    @Query("SELECT new ru.practicum.model.ViewStats(eh.app, eh.uri, count(*)) " +
            "FROM EndpointHit as eh " +
            "WHERE eh.timestamp between ?1 AND ?2 " +
            "GROUP BY eh.app, eh.uri " +
            "ORDER BY count(*) DESC ")
    List<ViewStats> findAllViewStats(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.model.ViewStats(eh.app, eh.uri, count(distinct eh.ip)) " +
            "FROM EndpointHit as eh " +
            "WHERE eh.timestamp BETWEEN ?1 AND ?2 " +
            "GROUP BY eh.app, eh.uri " +
            "ORDER BY count(distinct eh.ip) DESC ")
    List<ViewStats> findAllViewStatsByUniqueIp(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.model.ViewStats(eh.app, eh.uri, count(*)) " +
            "FROM EndpointHit as eh " +
            "WHERE eh.timestamp BETWEEN ?1 AND ?2 AND eh.uri IN ?3 " +
            "GROUP BY eh.app, eh.uri " +
            "ORDER BY count(*) DESC ")
    List<ViewStats> findAllViewStats(LocalDateTime start, LocalDateTime end, String[] uris);

    @Query("SELECT new ru.practicum.model.ViewStats(eh.app, eh.uri, count(distinct eh.ip) ) " +
            "FROM EndpointHit as eh " +
            "WHERE eh.timestamp BETWEEN ?1 AND ?2 AND eh.uri IN ?3 " +
            "GROUP BY eh.app, eh.uri " +
            "ORDER BY count(distinct eh.ip) DESC ")
    List<ViewStats> findAllViewStatsByUniqueIp(LocalDateTime start, LocalDateTime end, String[] uris);
    
}
