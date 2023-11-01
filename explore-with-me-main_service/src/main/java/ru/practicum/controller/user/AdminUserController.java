package ru.practicum.controller.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mapper.UserMapper;
import ru.practicum.model.user.dto.NewUserRequest;
import ru.practicum.model.user.dto.UserDto;
import ru.practicum.service.UserService;

import javax.validation.Valid;
import java.util.Collection;

@Slf4j
@RestController
@RequestMapping(path = "/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping
    public Collection<UserDto> getUsers(@RequestParam(required = false) Collection<Long> ids,
                                        @RequestParam(defaultValue = "0") int from,
                                        @RequestParam(defaultValue = "10") int size) {
        log.info("Invoked method getUsers of class AdminUsersController " +
                "with parameters: ids = {}, from = {}, size = {};", ids, from, size);
        return userMapper.toUserDto(userService.getUsers(ids, from, size));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto postUser(@Valid @RequestBody NewUserRequest userRequest) {
        log.info("Invoked method postUser of class AdminUsersController " +
                "with parameters: userRequest = {};", userRequest);
        return userMapper.toUserDto(userService.addUser(userMapper.toUser(userRequest)));
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long userId) {
        log.info("Invoked method deleteUser of class AdminUsersController " +
                "with parameters userId: userId;");
        userService.removeUser(userId);
    }

}
