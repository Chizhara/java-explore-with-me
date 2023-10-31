package ru.practicum.model.request;

import lombok.*;
import ru.practicum.model.request.ParticipationRequestStatus;
import ru.practicum.model.user.User;
import ru.practicum.model.event.Event;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "participation_requests", schema = "public",
        uniqueConstraints = @UniqueConstraint(columnNames = {"requester_id","event_id"}))
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParticipationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id", nullable = false, updatable = false)
    private User requester;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false, updatable = false)
    private Event event;
    @Column(name = "status")
    private ParticipationRequestStatus status;
    @Column(name = "created")
    private LocalDateTime created;
}
