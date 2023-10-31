package ru.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.model.compilation.EventsCompilation;
import ru.practicum.model.compilation.dto.CompilationDto;
import ru.practicum.model.compilation.dto.NewCompilationDto;
import ru.practicum.model.request.ParticipationRequestStatus;

@Mapper(uses = {EventMapper.class})
public interface EventsCompilationMapper {

    @Mapping(target = "events", ignore = true)
    EventsCompilation toEventsCompilation(NewCompilationDto compilationDto);

    CompilationDto toCompilationDto(EventsCompilation compilation);

}
