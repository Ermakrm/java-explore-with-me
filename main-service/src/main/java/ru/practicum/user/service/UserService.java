package ru.practicum.user.service;

import ru.practicum.user.model.User;

import java.util.List;

public interface UserService {
    User saveUser(User user);

    User findUserById(Long userId);

    void existUserById(Long userId);

    void deleteUser(Long userId);

    List<User> findUsersByIds(List<Long> ids, int from, int size);
}
