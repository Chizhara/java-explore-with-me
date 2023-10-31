package ru.practicum.model.event.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.model.category.dto.CategoryDto;
import ru.practicum.model.user.dto.UserShortDto;

import java.time.LocalDateTime;

@Data
@Builder
public class EventShortDto {
    private Long id;
    private String title;
    private String annotation;
    private CategoryDto category;
    private UserShortDto initiator;
    private LocalDateTime eventDate;
    private Boolean paid;
    private Long confirmedRequests;
    private Long views;
}
