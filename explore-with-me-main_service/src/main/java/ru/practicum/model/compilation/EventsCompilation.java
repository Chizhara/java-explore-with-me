package ru.practicum.model.compilation;

import lombok.*;
import ru.practicum.model.event.Event;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "compilations", schema = "public")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventsCompilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "title", length = 50, nullable = false)
    private String title;
    @Column(name = "pinned", nullable = false)
    private Boolean pinned;
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "compilation_id", table = "events_compilations")
    private Set<Event> events;
}
