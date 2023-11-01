package ru.practicum.service;

import ru.practicum.model.compilation.EventsCompilation;
import ru.practicum.model.compilation.UpdateCompilationRequest;

import java.util.Collection;
import java.util.Set;

public interface EventsCompilationService {
    EventsCompilation getCompilation(long compId);

    Collection<EventsCompilation> getCompilations(Boolean pinned, int size, int from);

    EventsCompilation addCompilation(EventsCompilation compilation, Set<Long> eventsId);

    EventsCompilation updateCompilation(long compId, UpdateCompilationRequest compilation);

    void removeCompilation(long compId);
}
