package ru.job4j.todo.service;

import org.springframework.stereotype.Service;
import ru.job4j.todo.model.User;
import ru.job4j.todo.repository.UserStore;

import java.util.Optional;

@Service
public class UserStoreServiceImpl implements UserStoreService {

    private final UserStore userRepository;

    public UserStoreServiceImpl(UserStore userStore) {
        this.userRepository = userStore;
    }

    @Override
    public Optional<User> save(User user) {
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findByLoginAndPassword(String email, String password) {
        return userRepository.findByLoginAndPassword(email, password);
    }
}
