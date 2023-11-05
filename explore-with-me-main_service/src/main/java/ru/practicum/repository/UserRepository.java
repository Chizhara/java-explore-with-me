package ru.practicum.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.event.EventState;
import ru.practicum.model.user.User;

import java.util.Collection;

public interface UserRepository extends JpaRepository<User, Long> {
    Page<User> findAllBySubscriptionsContaining(User user, Pageable page);

    Page<User> findAllBySubscribersContainingOrderBySubscriptions(User user, Pageable page);

    @Query("SELECT u FROM User u " +
            "WHERE u.id <> ?2 AND 0 < (SELECT COUNT(e) FROM Event as e " +
            "WHERE e.initiator.id = u.id AND e.state = ?1) " +
            "ORDER BY u.subscribers.size DESC ")
    Page<User> findAllInitiatorsByStateOrderBySubscribers(EventState state, long userId, Pageable page);

    @Query("SELECT u FROM User u " +
            "WHERE u.id <> ?2 AND NOT u IN ?3 AND 0 < (SELECT COUNT(e) FROM Event as e " +
            "WHERE e.initiator.id = u.id AND e.state = ?1) " +
            "ORDER BY u.subscribers.size DESC ")
    Page<User> findAllAlienInitiatorsByStateOrderBySubscribers(EventState state, long user,
                                                               Collection<User> subscriptions,
                                                               Pageable page);
}
