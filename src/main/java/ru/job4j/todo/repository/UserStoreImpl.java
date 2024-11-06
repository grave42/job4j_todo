package ru.job4j.todo.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.User;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
public class UserStoreImpl implements UserStore {

    private final CrudRepository crudRepository;

    @Autowired
    public UserStoreImpl(CrudRepository crudRepository) {
        this.crudRepository = crudRepository;
    }

    @Override
    public Optional<User> save(User user) {
        try {
            crudRepository.run(session -> session.save(user));
            return Optional.of(user);
        } catch (Exception e) {
            log.error("Error saving user: {}", e.getMessage(), e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findByLoginAndPassword(final String login, final String password) {
        String hql = "FROM User u WHERE u.login = :login AND u.password = :password";
        Map<String, Object> params = Map.of("login", login, "password", password);
        try {
            return crudRepository.optional(hql, User.class, params);
        } catch (Exception e) {
            log.error("Error finding user by login and password: {}", e.getMessage(), e);
            return Optional.empty();
        }
    }
}
