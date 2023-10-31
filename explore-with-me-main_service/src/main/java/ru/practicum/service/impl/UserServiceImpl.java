package ru.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.PageableGenerator;
import ru.practicum.exception.NotFoundException;
import ru.practicum.model.user.User;
import ru.practicum.repository.UserRepository;
import ru.practicum.service.UserService;

import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User getUser(long userId) {
        log.debug("Invoked method getUser of class UserServiceImp " +
                "with parameters: userId = {};", userId);
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User", userId));
    }

    @Override
    public Collection<User> getUsers(Collection<Long> ids, int from, int size) {
        log.debug("Invoked method getUsers of class UserServiceImp " +
                "with parameters: ids = {}, from = {}, size = {};", ids, from, size);
        return ids != null ? userRepository.findAllById(ids) :
                userRepository.findAll(PageableGenerator.getPageable(from, size)).toList();
    }

    @Override
    @Transactional
    public User addUser(User user) {
        log.debug("Invoked method addUser of class UserServiceImp " +
                "with parameters: user = {};", user);
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void removeUser(long userId) {
        log.debug("Invoked method removeUser of class UserServiceImp " +
                "with parameters: userId = {};", userId);
        validateUserExistsById(userId);
        userRepository.deleteById(userId);
    }

    public void validateUserExistsById(long userId) {
        log.debug("Invoked method validateUserExistsById of class UserServiceImp " +
                "with parameters: userId = {};", userId);
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User", userId);
        }
    }

}
