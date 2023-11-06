package ru.practicum.controller.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mapper.UserMapper;
import ru.practicum.model.user.dto.UserShortDto;
import ru.practicum.service.SubscriptionService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

@Slf4j
@RestController
@RequestMapping(path = "/users/{userId}")
@RequiredArgsConstructor
public class PrivateUserController {

    private final SubscriptionService subscriptionService;
    private final UserMapper userMapper;

    @GetMapping("/subscribers")
    public Collection<UserShortDto> getSubscribers(@PathVariable Long userId,
                                                   @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                   @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("Invoked method getSubscribers of class PrivateUserController " +
                "with parameters: userId = {}, from = {}, size = {};", userId, from, size);
        return userMapper.toUserShortDto(subscriptionService.getSubscribers(userId, from, size));
    }

    @GetMapping("/initiators")
    public Collection<UserShortDto> getInitiators(@PathVariable Long userId,
                                                  @RequestParam(defaultValue = "true") Boolean onlyAliens,
                                                  @RequestParam(defaultValue = "0") int from,
                                                  @RequestParam(defaultValue = "10") int size) {
        log.info("Invoked method getInitiators of class PrivateUserController " +
                "with parameters: userId = {}, onlyAliens = {}, from = {}, size = {};", userId, onlyAliens, from, size);
        return userMapper.toUserShortDto(subscriptionService.getInitiators(userId, onlyAliens, from, size));
    }


}
