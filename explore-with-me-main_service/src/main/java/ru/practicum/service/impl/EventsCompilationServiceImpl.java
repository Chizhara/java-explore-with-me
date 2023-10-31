package ru.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.PageableGenerator;
import ru.practicum.exception.NotFoundException;
import ru.practicum.model.compilation.EventsCompilation;
import ru.practicum.model.compilation.UpdateCompilationRequest;
import ru.practicum.model.event.Event;
import ru.practicum.repository.EventsCompilationRepository;
import ru.practicum.service.EventsCompilationService;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventsCompilationServiceImpl implements EventsCompilationService {
    private final EventsCompilationRepository compilationRepository;
    private final EventStatServiceImpl eventStatService;

    @Override
    public EventsCompilation getCompilation(long compId) {
        log.debug("Invoked method getCompilation of class EventsCompilationServiceImpl " +
                "with parameters: compId = {};", compId);
        EventsCompilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("EventsCompilation", compId));
        eventStatService.initViews(compilation.getEvents());
        return compilation;
    }

    @Override
    public Collection<EventsCompilation> getCompilations(Boolean pinned, int size, int from) {
        log.debug("Invoked method getCompilations of class EventsCompilationServiceImpl " +
                "with parameters: pinned = {}, size = {}, from = {};", pinned, size, from);
        List<EventsCompilation> compilations;
        if (pinned == null) {
            compilations = compilationRepository.findAll(PageableGenerator.getPageable(from, size)).getContent();
        } else {
            compilations = compilationRepository.findAllByPinned(pinned, PageableGenerator.getPageable(from, size));
        }

        Set<Event> events = new HashSet<>();
        compilations.forEach(compilation -> events.addAll(compilation.getEvents()));
        eventStatService.initViews(events);

        return compilations;
    }

    @Override
    @Transactional
    public EventsCompilation addCompilation(EventsCompilation compilation, Set<Long> eventsId) {
        log.debug("Invoked method addCompilation of class EventsCompilationServiceImpl " +
                "with parameters: compilation = {};", compilation);
        if (eventsId == null || eventsId.isEmpty()) {
            compilation.setEvents(Collections.emptyList());
            return compilationRepository.save(compilation);
        }
        Collection<Event> events = eventStatService.getEvents(eventsId);
        compilation.setEvents(events);
        return compilationRepository.save(compilation);
    }

    @Override
    @Transactional
    public EventsCompilation updateCompilation(long compId, UpdateCompilationRequest dataCompilation) {
        log.debug("Invoked method updateCompilation of class EventsCompilationServiceImpl " +
                "with parameters: compId = {}, dataCompilation = {};", compId, dataCompilation);
        EventsCompilation updatedCompilation = getCompilation(compId);
        updateCompilationFields(updatedCompilation, dataCompilation);
        return compilationRepository.save(updatedCompilation);
    }

    @Override
    @Transactional
    public void removeCompilation(long compId) {
        validateCompilationExisting(compId);
        compilationRepository.deleteById(compId);
    }

    private void updateCompilationFields(EventsCompilation updatedCompilation, UpdateCompilationRequest dataCompilation) {
        if (dataCompilation.getTitle() != null) {
            updatedCompilation.setTitle(dataCompilation.getTitle());
        }
        if (dataCompilation.getPinned() != null) {
            updatedCompilation.setPinned(dataCompilation.getPinned());
        }
        if (dataCompilation.getEvents() != null) {
            Collection<Event> events = eventStatService.getEvents(dataCompilation.getEvents());
            updatedCompilation.setEvents(events);
        }
    }

    private void validateCompilationExisting(long compId) {
        if (!compilationRepository.existsById(compId)) {
            throw new NotFoundException("EventsCompilation", compId);
        }
    }
}
