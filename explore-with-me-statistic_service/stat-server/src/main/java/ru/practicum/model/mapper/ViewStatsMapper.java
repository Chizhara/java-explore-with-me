package ru.practicum.model.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.model.ViewStats;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.stream.Collectors;

@UtilityClass
public class ViewStatsMapper {

    public static ViewStatsDto toViewStatsDto(ViewStats viewStats) {
        return ViewStatsDto.builder()
                .uri(viewStats.getUri())
                .app(viewStats.getApp())
                .hits(viewStats.getHits())
                .build();
    }

    public static Collection<ViewStatsDto> toViewStatsDto(Collection<ViewStats> viewStats) {
        return viewStats.stream()
                .map(ViewStatsMapper::toViewStatsDto)
                .collect(Collectors.toList());
    }

    public static ViewStats toViewStats(ResultSet rs) throws SQLException {
        return ViewStats.builder()
                .app(rs.getString("app"))
                .uri(rs.getString("uri"))
                .hits(rs.getLong("hits"))
                .build();
    }

}
