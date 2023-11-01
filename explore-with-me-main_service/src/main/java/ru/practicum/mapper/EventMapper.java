package ru.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.model.event.Event;
import ru.practicum.model.event.dto.EventFullDto;
import ru.practicum.model.event.dto.EventShortDto;
import ru.practicum.model.event.dto.NewEventDto;
import ru.practicum.model.request.ParticipationRequestStatus;

import java.util.Collection;

@Mapper(uses = {UserMapper.class, CategoryMapper.class}, imports = {ParticipationRequestStatus.class})
public interface EventMapper {
    @Mapping(target = "initiator", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "paid", defaultValue = "false")
    @Mapping(target = "participantLimit", defaultValue = "0")
    @Mapping(target = "requestModeration", defaultValue = "true")
    @Mapping(source = "location.lat", target = "lat")
    @Mapping(source = "location.lon", target = "lon")
    Event toEvent(NewEventDto eventDto);

    @Mapping(target = "views", defaultValue = "0L")
    @Mapping(target = "confirmedRequests", expression = "java(event.getConfirmedRequests() != null ? " +
            "event.getConfirmedRequests().size() : 0L)")
    EventShortDto toEventShortDto(Event event);

    Collection<EventShortDto> toEventShortDto(Collection<Event> events);

    @Mapping(source = "lat", target = "location.lat")
    @Mapping(source = "lon", target = "location.lon")
    @Mapping(target = "views", defaultValue = "0L")
    @Mapping(target = "confirmedRequests", expression = "java(event.getConfirmedRequests() != null ? " +
            "event.getConfirmedRequests().size() : 0L)")
    EventFullDto toEventFullDto(Event event);

    Collection<EventFullDto> toEventFullDto(Collection<Event> events);

}
