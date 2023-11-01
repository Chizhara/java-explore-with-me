package ru.practicum.controller.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mapper.EventsCompilationMapper;
import ru.practicum.model.compilation.UpdateCompilationRequest;
import ru.practicum.model.compilation.dto.CompilationDto;
import ru.practicum.model.compilation.dto.NewCompilationDto;
import ru.practicum.service.EventsCompilationService;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(path = "/admin/compilations")
@RequiredArgsConstructor
public class AdminEventCompilationController {

    private final EventsCompilationService compilationService;
    private final EventsCompilationMapper compilationMapper;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto postCompilation(@Valid @RequestBody NewCompilationDto compilationDto) {
        log.info("Invoked method postCompilation of class AdminEventCompilationController " +
                "with parameters: compilationDto = {};", compilationDto);
        return compilationMapper.toCompilationDto(
                compilationService.addCompilation(
                        compilationMapper.toEventsCompilation(compilationDto), compilationDto.getEvents()));
    }

    @PatchMapping("/{compId}")
    public CompilationDto patchCompilation(@PathVariable Long compId,
                                           @Valid @RequestBody UpdateCompilationRequest compilationRequestDto) {
        log.info("Invoked method patchCompilation of class AdminEventCompilationController " +
                "with parameters: compId = {}, compilationRequestDto = {};", compId, compilationRequestDto);
        return compilationMapper.toCompilationDto(
                compilationService.updateCompilation(compId, compilationRequestDto));
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable Long compId) {
        log.info("Invoked method deleteCompilation of class AdminEventCompilationController " +
                "with parameters: compId = {};", compId);
        compilationService.removeCompilation(compId);
    }
}
