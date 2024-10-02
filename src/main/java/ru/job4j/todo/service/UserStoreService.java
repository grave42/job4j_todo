package ru.job4j.todo.service;

import ru.job4j.todo.model.User;

import java.util.Optional;

public interface UserStoreService {

    Optional<User> save(User user);

    Optional<User> findByLoginAndPassword(String email, String password);
}
