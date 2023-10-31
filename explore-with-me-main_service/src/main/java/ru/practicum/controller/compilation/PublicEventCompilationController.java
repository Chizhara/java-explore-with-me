package ru.practicum.controller.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mapper.EventsCompilationMapper;
import ru.practicum.model.compilation.dto.CompilationDto;
import ru.practicum.service.EventsCompilationService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

@Slf4j
@RestController
@RequestMapping(path = "/compilations")
@RequiredArgsConstructor
@Validated
public class PublicEventCompilationController {

    private final EventsCompilationService compilationService;
    private final EventsCompilationMapper compilationMapper;

    @GetMapping("/{compId}")
    public CompilationDto getCompilation(@PathVariable Long compId) {
        log.info("Invoked method patchCompilation of class PublicEventCompilationController " +
                "with parameters: compId = {};", compId);
        return compilationMapper.toCompilationDto(
                compilationService.getCompilation(compId));
    }

    @GetMapping
    public Collection<CompilationDto> getCompilations(@RequestParam(required = false) Boolean pinned,
                                                      @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                      @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("Invoked method postCompilation of class PublicEventCompilationController " +
                "with parameters: pinned = {}, from = {}, size = {};", pinned, from, size);
        return compilationMapper.toCompilationDto(
                compilationService.getCompilations(pinned, size, from));
    }

}
