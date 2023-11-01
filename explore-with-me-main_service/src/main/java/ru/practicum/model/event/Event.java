package ru.practicum.model.event;

import lombok.*;
import org.hibernate.annotations.Where;
import ru.practicum.model.category.Category;
import ru.practicum.model.request.ParticipationRequest;
import ru.practicum.model.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "events", schema = "public")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "title", length = 120)
    private String title;
    @Column(name = "annotation", length = 2000)
    private String annotation;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false, updatable = false)
    private Category category;
    @Column(name = "description", length = 7000)
    private String description;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "initiator_id", nullable = false, updatable = false)
    private User initiator;
    @Column(name = "location_lat")
    private Float lat;
    @Column(name = "location_lon")
    private Float lon;
    @Column(name = "event_date")
    private LocalDateTime eventDate;
    private Boolean paid;
    @Column(name = "participant_limit")
    private Integer participantLimit;
    @Column(name = "published_date")
    private LocalDateTime publishedOn;
    @Column(name = "created_date")
    private LocalDateTime createdOn;
    @Column(name = "request_moderation")
    private Boolean requestModeration;
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    @Where(clause = "status = 'CONFIRMED'")
    private List<ParticipationRequest> confirmedRequests;
    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private EventState state;
    @Transient
    private Long views;

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", annotation='" + annotation + '\'' +
                ", description='" + description + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
                ", eventDate=" + eventDate +
                ", paid=" + paid +
                ", participantLimit=" + participantLimit +
                ", publishedOn=" + publishedOn +
                ", createdOn=" + createdOn +
                ", requestModeration=" + requestModeration +
                ", state=" + state +
                ", views=" + views +
                '}';
    }
}
