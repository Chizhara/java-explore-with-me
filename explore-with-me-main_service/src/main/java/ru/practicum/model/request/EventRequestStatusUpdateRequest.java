package ru.practicum.model.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
public class EventRequestStatusUpdateRequest {
    @NotNull
    private Set<Long> requestIds;
    @NotNull
    private ParticipationRequestStatus status;
}
