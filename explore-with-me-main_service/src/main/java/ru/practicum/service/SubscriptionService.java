package ru.practicum.service;

import ru.practicum.model.event.Event;
import ru.practicum.model.user.User;
import ru.practicum.param.EventParam;

import java.util.Collection;

public interface SubscriptionService {

    User addSubscription(long subscriberId, long userId);

    void removeSubscription(long subscriberId, long userId);

    Collection<User> getSubscriptions(long subscriberId, int from, int size);

    Collection<User> getSubscribers(long userId, int from, int size);

    Collection<Event> getSubscriptionEvents(long subscriberId, EventParam eventParam, int from, int size);

    Collection<User> getInitiators(long userId, boolean onlyAliens, int from, int size);
}
