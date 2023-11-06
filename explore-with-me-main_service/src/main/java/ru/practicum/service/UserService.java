package ru.practicum.service;

import ru.practicum.model.user.User;

import java.util.Collection;

public interface UserService {
    User getUser(long id);

    Collection<User> getUsers(Collection<Long> ids, int from, int size);

    User addUser(User user);

    void removeUser(long userId);

    void validateUserExistsById(long userId);
}
