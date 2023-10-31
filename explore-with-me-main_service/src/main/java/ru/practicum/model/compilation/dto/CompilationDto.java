package ru.practicum.model.compilation.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.model.event.dto.EventShortDto;

import java.util.Collection;
import java.util.LinkedList;

@Data
@Builder
public class CompilationDto {
    private Long id;
    private String title;
    @Builder.Default
    private Collection<EventShortDto> events = new LinkedList<>();
    private Boolean pinned;
}
