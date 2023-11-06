package ru.practicum.model.user;

import lombok.*;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "users", schema = "public")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;
    @Column(name = "name", unique = true)
    String name;
    @Column(name = "email", unique = true)
    String email;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "subscriptions", joinColumns = {@JoinColumn(name = "subscriber_id")},
            inverseJoinColumns = @JoinColumn(name = "recipient_id"))
    Collection<User> subscriptions;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "subscriptions", joinColumns = {@JoinColumn(name = "recipient_id")},
            inverseJoinColumns = @JoinColumn(name = "subscriber_id"))
    Collection<User> subscribers;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
