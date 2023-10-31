package ru.practicum.model.request.dto;

import lombok.Data;

import java.util.Collection;

@Data
public class EventRequestStatusUpdateResultDto {
    private Collection<ParticipationRequestDto> confirmedRequests;
    private Collection<ParticipationRequestDto> rejectedRequests;
}
