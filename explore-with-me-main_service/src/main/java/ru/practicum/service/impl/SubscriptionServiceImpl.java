package ru.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.PageableGenerator;
import ru.practicum.exception.InvalidActionException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.model.event.Event;
import ru.practicum.model.event.EventState;
import ru.practicum.model.user.User;
import ru.practicum.param.EventParam;
import ru.practicum.repository.UserRepository;
import ru.practicum.service.EventService;
import ru.practicum.service.SubscriptionService;
import ru.practicum.service.UserService;

import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final UserService userService;
    private final EventService eventService;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public User addSubscription(long subscriberId, long userId) {
        log.debug("Invoked method addSubscription of class SubscriptionServiceImpl " +
                "with parameters: subscriberId = {}, userId = {};", subscriberId, userId);

        User subscriber = userService.getUser(subscriberId);
        User user = userService.getUser(userId);

        validateSubscriptionExists(subscriber, user);
        subscriber.getSubscriptions().add(user);
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void removeSubscription(long subscriberId, long userId) {
        log.debug("Invoked method addSubscription of class SubscriptionServiceImpl " +
                "with parameters: subscriberId = {}, userId = {};", subscriberId, userId);
        User subscriber = userService.getUser(subscriberId);
        User user = userService.getUser(userId);

        if (!subscriber.getSubscriptions().remove(user)) {
            throw new NotFoundException("Subscription", userId);
        }

        userRepository.save(user);
    }

    @Override
    public Collection<User> getSubscriptions(long subscriberId, int from, int size) {
        log.debug("Invoked method getSubscriptions of class SubscriptionServiceImpl " +
                "with parameters: subscriberId = {}, from = {}, size = {};", subscriberId, from, size);
        User subscriber = userService.getUser(subscriberId);

        return userRepository.findAllBySubscribersContainingOrderBySubscriptions(subscriber,
                PageableGenerator.getPageable(from, size)).toSet();
    }

    @Override
    public Collection<User> getSubscribers(long userId, int from, int size) {
        log.debug("Invoked method getSubscribers of class SubscriptionServiceImpl " +
                "with parameters: userId = {}, from = {}, size = {};", userId, from, size);
        User user = userService.getUser(userId);
        return userRepository.findAllBySubscriptionsContaining(user, PageableGenerator.getPageable(from, size)).toSet();
    }

    @Override
    public Collection<Event> getSubscriptionEvents(long subscriberId, EventParam eventParam, int from, int size) {
        log.debug("Invoked method getSubscribers of class SubscriptionServiceImpl " +
                        "with parameters: subscriberId = {}, eventParam = {}, from = {}, size = {};",
                subscriberId, eventParam, from, size);

        User subscriber = userService.getUser(subscriberId);
        validateSubscriptions(subscriber, eventParam.getInitiatorsId());

        eventParam.setBySubscriber(subscriber);
        eventParam.setStates(EventState.PUBLISHED);

        return eventService.searchEvents(eventParam, from, size);
    }

    @Override
    public Collection<User> getInitiators(long userId, boolean onlyAliens, int from, int size) {
        log.debug("Invoked method getInitiators of class SubscriptionServiceImpl " +
                "with parameters: userId = {}, onlyAliens ={}, from = {}, size = {};", userId, onlyAliens, from, size);

        if (onlyAliens) {
            return getAlienInitiators(userId, from, size);
        } else {
            userService.validateUserExistsById(userId);
            return getInitiators(userId, from, size);
        }
    }

    private Collection<User> getAlienInitiators(long userId, int from, int size) {
        User user = userService.getUser(userId);
        if (user.getSubscriptions().isEmpty()) {
            return getInitiators(userId, from, size);
        }
        return userRepository.findAllAlienInitiatorsByStateOrderBySubscribers(EventState.PUBLISHED,
                user.getId(), user.getSubscriptions(), PageableGenerator.getPageable(from, size)).toList();
    }

    private Collection<User> getInitiators(long userId, int from, int size) {
        return userRepository.findAllInitiatorsByStateOrderBySubscribers(EventState.PUBLISHED, userId,
                PageableGenerator.getPageable(from, size)).toList();
    }

    private void validateSubscriptionExists(User subscriber, User user) {
        if (subscriber.getSubscriptions().contains(user)) {
            throw new InvalidActionException(
                    String.format("User with id = %d already subscribed to user with id = %d",
                            subscriber.getId(), user.getId()));
        }
    }

    private void validateSubscriptions(User subscriber, Collection<Long> users) {
        if (users == null) {
            return;
        }
        Collection<Long> subscriptions = subscriber.getSubscriptions().stream()
                .map(User::getId)
                .collect(Collectors.toList());

        for (long userId : users) {
            if (!subscriptions.contains(userId)) {
                throw new NotFoundException("Subscription", userId);
            }
        }
    }
}
