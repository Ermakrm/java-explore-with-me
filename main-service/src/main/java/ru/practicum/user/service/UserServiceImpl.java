package ru.practicum.user.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.exception.IllegalActionException;
import ru.practicum.exception.ObjectNotFoundException;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {
    UserRepository userRepository;

    @Override
    public User findUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new ObjectNotFoundException(String.format("User with id=%d not found", userId))
        );
    }

    @Override
    public User saveUser(User user) {
        if (userRepository.existsByName(user.getName())) {
            throw new IllegalActionException("User name is already used");
        }
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long userId) {
        existUserById(userId);
        userRepository.deleteById(userId);
    }

    @Override
    public List<User> findUsersByIds(List<Long> ids, int from, int size) {
        Pageable pr = PageRequest.of(from / size, size);
        return Objects.isNull(ids) ? userRepository.findAll(pr).getContent() : userRepository.findAllByIdIn(ids, pr);
    }

    @Override
    public void existUserById(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ObjectNotFoundException(String.format("User with id=%d not found", userId));
        }
    }
}
