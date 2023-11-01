package ru.practicum.model.request;

import lombok.*;
import ru.practicum.model.event.Event;
import ru.practicum.model.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "participation_requests", schema = "public",
        uniqueConstraints = @UniqueConstraint(columnNames = {"requester_id", "event_id"}))
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
    @Enumerated(EnumType.STRING)
    private ParticipationRequestStatus status;
    @Column(name = "created")
    private LocalDateTime created;

    @Override
    public String toString() {
        return "ParticipationRequest{" +
                "id=" + id +
                ", status=" + status +
                ", created=" + created +
                '}';
    }
}
