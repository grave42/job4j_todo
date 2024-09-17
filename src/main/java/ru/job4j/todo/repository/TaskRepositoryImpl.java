package ru.job4j.todo.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

@Repository
public class TaskRepositoryImpl implements TaskRepository {

    private final SessionFactory sessionFactory;

    public TaskRepositoryImpl(final SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Task save(final Task task) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.save(task);
            transaction.commit();
            return task;
        }
    }

    @Override
    public boolean deleteById(final int id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            Task task = session.find(Task.class, id);
            if (task != null) {
                session.delete(task);
                transaction.commit();
                return true;
            }
            transaction.commit();
            return false;
        }
    }

    @Override
    public boolean update(final Task task) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            Task existingTask = session.find(Task.class, task.getId());
            if (existingTask != null) {
                existingTask.setDescription(task.getDescription());
                existingTask.setTitle(task.getTitle());
                existingTask.setCreated(task.getCreated());
                existingTask.setDone(task.isDone());
                session.update(existingTask);
                transaction.commit();
                return true;
            }
            transaction.commit();
            return false;
        }
    }

    @Override
    public Optional<Task> findById(final int id) {
        try (Session session = sessionFactory.openSession()) {
            Task task = session.find(Task.class, id);
            return Optional.ofNullable(task);
        }
    }

    @Override
    public Collection<Task> findByDone(final boolean done) {
        try (Session session = sessionFactory.openSession()) {
            Query<Task> query = session.createQuery("from Task where done = :done", Task.class);
            query.setParameter("done", done);
            return query.list();
        }
    }

    @Override
    public Collection<Task> findAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<Task> query = session.createQuery("from Task", Task.class);
            return query.list();
        }
    }
}
