package ru.practicum.model.compilation.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.model.event.dto.EventShortDto;

import java.util.Collection;

@Data
@Builder
public class CompilationDto {
    private Long id;
    private String title;
    private Collection<EventShortDto> events;
    private Boolean pinned;
}
