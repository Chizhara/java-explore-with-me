package ru.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.model.request.dto.EventRequestStatusUpdateResultDto;
import ru.practicum.model.request.EventRequestStatusUpdateResult;
import ru.practicum.model.request.dto.ParticipationRequestDto;
import ru.practicum.model.request.ParticipationRequest;

import java.util.Collection;

@Mapper
public interface ParticipationRequestMapper {

    @Mapping(source = "request.event.id", target = "event")
    @Mapping(source = "request.requester.id", target = "requester")
    ParticipationRequestDto toParticipationRequestDto(ParticipationRequest request);

    Collection<ParticipationRequestDto> toParticipationRequestDto(Collection<ParticipationRequest> request);

    EventRequestStatusUpdateResultDto toEventRequestStatusUpdateResultDto(EventRequestStatusUpdateResult result);
}
