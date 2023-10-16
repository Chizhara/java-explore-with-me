package ru.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.model.ViewStats;

import java.util.Collection;
import java.util.stream.Collectors;

@UtilityClass
public class StatsViewMapper {

    public static ViewStatsDto toViewStatsDto(ViewStats viewStats) {
        return ViewStatsDto.builder()
                .uri(viewStats.getUri())
                .app(viewStats.getApp())
                .hits(viewStats.getHits())
                .build();
    }

    public static Collection<ViewStatsDto> toViewStatsDto(Collection<ViewStats> viewStats) {
        return viewStats.stream()
                .map(StatsViewMapper::toViewStatsDto)
                .collect(Collectors.toList());
    }

}
