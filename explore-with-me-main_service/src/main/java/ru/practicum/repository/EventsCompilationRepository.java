package ru.practicum.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.compilation.EventsCompilation;

import java.util.List;

public interface EventsCompilationRepository extends JpaRepository<EventsCompilation, Long> {
    List<EventsCompilation> findAllByPinned(boolean pinned, Pageable page);
}