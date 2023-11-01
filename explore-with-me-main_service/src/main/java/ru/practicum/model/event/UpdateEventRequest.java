package ru.practicum.model.event;

import lombok.Builder;
import lombok.Data;
import ru.practicum.annotation.LocalDateTimeAfter;
import ru.practicum.annotation.RequestMarker;
import ru.practicum.annotation.StateActionOneOf;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Builder
public class UpdateEventRequest {
    @Size(min = 3, max = 120)
    private String title;
    @Size(min = 20, max = 2000)
    private String annotation;
    private Long category;
    @Size(min = 20, max = 7000)
    private String description;
    private Location location;
    @LocalDateTimeAfter(hours = 2)
    private LocalDateTime eventDate;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    @StateActionOneOf(anyOf = {StateAction.REJECT_EVENT, StateAction.PUBLISH_EVENT},
            groups = RequestMarker.AdminRequest.class)
    @StateActionOneOf(anyOf = {StateAction.CANCEL_REVIEW, StateAction.SEND_TO_REVIEW},
            groups = RequestMarker.UserRequest.class)
    private StateAction stateAction;
}
