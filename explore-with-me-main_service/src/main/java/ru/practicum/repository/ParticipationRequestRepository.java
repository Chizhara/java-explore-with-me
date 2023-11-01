package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.model.request.ParticipationRequest;
import ru.practicum.model.request.ParticipationRequestStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Long> {
    List<ParticipationRequest> findAllByRequesterId(long userId);

    Optional<ParticipationRequest> findByIdAndRequesterId(long eventId, long userId);

    List<ParticipationRequest> findAllByEventIdAndEventInitiatorId(long eventId, long userId);

    Optional<ParticipationRequest> findByEventIdAndEventInitiatorIdAndStatus(long eventId, long userId,
                                                                             ParticipationRequestStatus status);

    List<ParticipationRequest> findAllByEventIdAndEventInitiatorIdAndStatusNot(long eventId, long userId,
                                                                               ParticipationRequestStatus status);
}
