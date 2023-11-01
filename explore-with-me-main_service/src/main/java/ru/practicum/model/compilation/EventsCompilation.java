package ru.practicum.model.compilation;

import lombok.*;
import ru.practicum.model.event.Event;

import javax.persistence.*;
import java.util.Collection;

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
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "events_compilations", joinColumns = {@JoinColumn(name = "compilation_id")},
            inverseJoinColumns = @JoinColumn(name = "event_id"))
    private Collection<Event> events;

    @Override
    public String toString() {
        return "EventsCompilation{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", pinned=" + pinned +
                ", events=" + events +
                '}';
    }
}
