package ru.practicum.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.practicum.model.EndpointHit;
import ru.practicum.model.ViewStats;
import ru.practicum.model.mapper.ViewStatsMapper;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class EndpointHitRepositoryImp implements EndpointHitRepository {

    private final NamedParameterJdbcOperations jdbcOperations;

    public void save(EndpointHit endpointHit) {
        final String sql = "INSERT INTO endpoint_hits(app, uri, ip, hit_date) VALUES (:app, :uri, :ip, :hit_date)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource map = makeMap(endpointHit);
        jdbcOperations.update(sql, map, keyHolder);
    }

    public List<ViewStats> findAllViewStats(LocalDateTime start, LocalDateTime end) {
        final String sql = "SELECT eh.app AS app, eh.uri AS uri, count(*) AS hits " +
                "FROM endpoint_hits AS eh " +
                "WHERE eh.hit_date between :start AND :end " +
                "GROUP BY eh.app, eh.uri " +
                "ORDER BY count(*) DESC ";

        return jdbcOperations.query(sql, Map.of("start", start, "end", end),
                (rs, rowNum) -> ViewStatsMapper.toViewStats(rs));
    }

    public List<ViewStats> findAllViewStatsByUniqueIp(LocalDateTime start, LocalDateTime end) {
        final String sql = "SELECT eh.app AS app, eh.uri AS uri, count(DISTINCT eh.ip) AS hits " +
                "FROM endpoint_hits AS eh " +
                "WHERE eh.hit_date between :start AND :end " +
                "GROUP BY eh.app, eh.uri " +
                "ORDER BY count(DISTINCT eh.ip) DESC ";

        return jdbcOperations.query(sql, Map.of("start", start, "end", end),
                (rs, rowNum) -> ViewStatsMapper.toViewStats(rs));
    }

    public List<ViewStats> findAllViewStats(LocalDateTime start, LocalDateTime end, String[] uris) {
        final String sql = "SELECT eh.app AS app, eh.uri AS uri, count(*) AS hits " +
                "FROM endpoint_hits AS eh " +
                "WHERE eh.hit_date between :start AND :end AND eh.uri IN (:uris) " +
                "GROUP BY eh.app, eh.uri " +
                "ORDER BY count(*) DESC ";

        return jdbcOperations.query(sql, Map.of("start", start, "end", end, "uris", Arrays.asList(uris)),
                (rs, rowNum) -> ViewStatsMapper.toViewStats(rs));
    }

    public List<ViewStats> findAllViewStatsByUniqueIp(LocalDateTime start, LocalDateTime end, String[] uris) {
        final String sql = "SELECT eh.app AS app, eh.uri AS uri, count(DISTINCT eh.ip) AS hits " +
                "FROM endpoint_hits AS eh " +
                "WHERE eh.hit_date between :start AND :end AND eh.uri IN (:uris) " +
                "GROUP BY eh.app, eh.uri " +
                "ORDER BY count(DISTINCT eh.ip) DESC ";

        return jdbcOperations.query(sql, Map.of("start", start, "end", end, "uris", Arrays.asList(uris)),
                (rs, rowNum) -> ViewStatsMapper.toViewStats(rs));
    }

    private MapSqlParameterSource makeMap(EndpointHit endpointHit) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("app", endpointHit.getApp());
        map.addValue("uri", endpointHit.getUri());
        map.addValue("ip", endpointHit.getIp());
        map.addValue("hit_date", endpointHit.getTimestamp());
        return map;
    }

}
