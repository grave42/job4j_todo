package ru.job4j.todo.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;

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
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.save(task);
            transaction.commit();
            return task;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Error saving task", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public boolean deleteById(final int id) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            Query query = session.createQuery("delete from Task where id = :id");
            query.setParameter("id", id);
            int result = query.executeUpdate();
            transaction.commit();
            return result > 0;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Error deleting task", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public boolean update(final Task task) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            Query query = session.createQuery(
                    "update Task set description = :description, title = :title, created = :created, done = :done where id = :id");
            query.setParameter("description", task.getDescription());
            query.setParameter("title", task.getTitle());
            query.setParameter("created", task.getCreated());
            query.setParameter("done", task.isDone());
            query.setParameter("id", task.getId());
            int result = query.executeUpdate();
            transaction.commit();
            return result > 0;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Error updating task", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public Optional<Task> findById(final int id) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Task task = session.find(Task.class, id);
            return Optional.ofNullable(task);
        } catch (Exception e) {
            throw new RuntimeException("Error finding task by ID", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public Collection<Task> findByDone(final boolean done) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Query<Task> query = session.createQuery("from Task where done = :done", Task.class);
            query.setParameter("done", done);
            return query.list();
        } catch (Exception e) {
            throw new RuntimeException("Error finding tasks by done status", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public Collection<Task> findAll() {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Query<Task> query = session.createQuery("from Task", Task.class);
            return query.list();
        } catch (Exception e) {
            throw new RuntimeException("Error finding all tasks", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public boolean updateDoneById(int id, boolean done) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            Query query = session.createQuery("update Task set done = :done where id = :id");
            query.setParameter("done", done);
            query.setParameter("id", id);

            int result = query.executeUpdate();

            transaction.commit();
            return result > 0;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            return false;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

}

