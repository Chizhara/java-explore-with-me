package ru.practicum.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Collection;

@Data
@Builder
@AllArgsConstructor
public class EventRequestStatusUpdateResult {

    private Collection<ParticipationRequest> confirmedRequests;
    private Collection<ParticipationRequest> rejectedRequests;

}
