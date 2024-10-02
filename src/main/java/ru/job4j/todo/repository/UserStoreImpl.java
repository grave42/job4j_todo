package ru.job4j.todo.repository;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.User;

import java.util.Optional;

@Slf4j
@Repository
public class UserStoreImpl implements UserStore {

    private final SessionFactory sessionFactory;

    @Autowired
    public UserStoreImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Optional<User> save(User user) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.save(user);
            transaction.commit();
            return Optional.of(user);
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            log.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findByLoginAndPassword(final String login, final String password) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "FROM User u WHERE u.login = :login AND u.password = :password";
            User user = session.createQuery(hql, User.class)
                    .setParameter("login", login)
                    .setParameter("password", password)
                    .uniqueResult();
            return Optional.ofNullable(user);
        } catch (HibernateException e) {
            log.error(e.getMessage(), e);
        }
        return Optional.empty();
    }
}
