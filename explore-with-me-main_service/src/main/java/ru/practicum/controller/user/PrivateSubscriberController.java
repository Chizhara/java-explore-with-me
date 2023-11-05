package ru.practicum.controller.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mapper.EventMapper;
import ru.practicum.mapper.UserMapper;
import ru.practicum.model.event.dto.EventShortDto;
import ru.practicum.model.user.dto.UserShortDto;
import ru.practicum.param.EventParam;
import ru.practicum.service.SubscriptionService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.Collection;

@Slf4j
@RestController
@RequestMapping(path = "/users/{userId}/subscriptions")
@RequiredArgsConstructor
@Validated
public class PrivateSubscriberController {

    private final SubscriptionService subscriptionService;
    private final UserMapper userMapper;
    private final EventMapper eventMapper;

    @GetMapping
    public Collection<UserShortDto> getSubscriptions(@PathVariable Long userId,
                                                     @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                     @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("Invoked method getUsersByCommonEvents of class PrivateSubscriberController " +
                "with parameters: userId = {}, from = {}, size = {};", userId, from, size);
        return userMapper.toUserShortDto(subscriptionService.getSubscriptions(userId, from, size));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserShortDto postSubscription(@PathVariable Long userId, @RequestParam("initiatorId") Long subId) {
        log.info("Invoked method postSubscription of class PrivateSubscriberController " +
                "with parameters: userId = {}, subId = {};", userId, subId);
        return userMapper.toUserShortDto(subscriptionService.addSubscription(userId, subId));
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSubscription(@PathVariable Long userId, @RequestParam("initiatorId") Long subId) {
        log.info("Invoked method deleteSubscription of class PrivateSubscriberController " +
                "with parameters: userId = {}, subId = {};", userId, subId);
        subscriptionService.removeSubscription(userId, subId);
    }

    @GetMapping("/events")
    public Collection<EventShortDto> getSubscriberEvents(
            @PathVariable Long userId,
            @RequestParam(name = "categories", required = false) Collection<Long> categoriesId,
            @RequestParam(name = "subscriptions", required = false) Collection<Long> subscriptionsId,
            @RequestParam(defaultValue = "true") Boolean onlyAvailable,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
            @RequestParam(defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("Invoked method getSubscriberEvents of class PrivateSubscriberController " +
                        "with parameters: subId = {}, categoriesId = {}, onlyAvailable = {}, " +
                        "rangeStart = {}, rangeEnd = {}, from = {}, size = {};",
                userId, categoriesId, onlyAvailable, rangeStart, rangeEnd, from, size);

        EventParam eventParam = new EventParam();
        eventParam.setCategoriesId(categoriesId);
        eventParam.setRangeStart(rangeStart);
        eventParam.setRangeEnd(rangeEnd);
        eventParam.setOnlyAvailable(onlyAvailable);
        eventParam.setInitiatorsId(subscriptionsId);

        return eventMapper.toEventShortDto(subscriptionService.getSubscriptionEvents(userId, eventParam, from, size));
    }
}
