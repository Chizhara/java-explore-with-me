package ru.practicum.model.event.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.model.category.dto.CategoryDto;
import ru.practicum.model.event.EventState;
import ru.practicum.model.event.Location;
import ru.practicum.model.user.dto.UserShortDto;

import java.time.LocalDateTime;

@Data
@Builder
public class EventFullDto {

    private Long id;
    private String title;
    private String annotation;
    private CategoryDto category;
    private String description;
    private UserShortDto initiator;
    private Location location;
    private LocalDateTime eventDate;
    private Boolean paid;
    private Integer participantLimit;
    private LocalDateTime publishedOn;
    private LocalDateTime createdOn;
    private Boolean requestModeration;
    private Long confirmedRequests;
    private EventState state;
    private Long views;

}
