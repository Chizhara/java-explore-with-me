package ru.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.model.compilation.EventsCompilation;
import ru.practicum.model.compilation.dto.CompilationDto;
import ru.practicum.model.compilation.dto.NewCompilationDto;

import java.util.Collection;

@Mapper(uses = {EventMapper.class})
public interface EventsCompilationMapper {

    @Mapping(target = "events", ignore = true)
    @Mapping(target = "pinned", defaultValue = "false")
    EventsCompilation toEventsCompilation(NewCompilationDto compilationDto);

    Collection<CompilationDto> toCompilationDto(Collection<EventsCompilation> compilation);

    CompilationDto toCompilationDto(EventsCompilation compilation);

}
