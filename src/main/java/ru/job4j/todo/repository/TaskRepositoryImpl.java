package ru.job4j.todo.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@Repository
public class TaskRepositoryImpl implements TaskRepository {

    private final CrudRepository crudRepository;

    public TaskRepositoryImpl(final CrudRepository crudRepository) {
        this.crudRepository = crudRepository;
    }

    @Override
    public Task save(final Task task) {
        crudRepository.run(session -> session.save(task));
        return task;
    }

    @Override
    public boolean deleteById(final int id) {
        String query = "delete from Task where id = :id";
        Map<String, Object> params = Map.of("id", id);
        crudRepository.run(query, params);
        return true;
    }

    @Override
    public boolean update(final Task task) {
        String query = "update Task set description = :description, title = :title, created = :created, done = :done where id = :id";
        Map<String, Object> params = Map.of(
                "description", task.getDescription(),
                "title", task.getTitle(),
                "created", task.getCreated(),
                "done", task.isDone(),
                "id", task.getId()
        );
        crudRepository.run(query, params);
        return true;
    }

    @Override
    public Optional<Task> findById(final int id) {
        String query = "from Task where id = :id";
        Map<String, Object> params = Map.of("id", id);
        return crudRepository.optional(query, Task.class, params);
    }

    @Override
    public Collection<Task> findByDone(final boolean done) {
        String query = "from Task where done = :done";
        Map<String, Object> params = Map.of("done", done);
        return crudRepository.query(query, Task.class, params);
    }

    @Override
    public Collection<Task> findAll() {
        String query = "from Task";
        return crudRepository.query(query, Task.class);
    }

    @Override
    public boolean updateDoneById(int id, boolean done) {
        String query = "update Task set done = :done where id = :id";
        Map<String, Object> params = Map.of("done", done, "id", id);
        crudRepository.run(query, params);
        return true;
    }
}

