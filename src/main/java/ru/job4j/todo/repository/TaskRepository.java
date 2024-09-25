package ru.job4j.todo.repository;

import ru.job4j.todo.model.Task;

import javax.lang.model.type.TypeKind;
import java.util.Collection;
import java.util.Optional;

public interface TaskRepository {

    Task save(Task task);

    boolean deleteById(int id);

    boolean update(Task task);

    Optional<Task> findById(int id);

    Collection<Task> findByDone(boolean done);

    Collection<Task> findAll();

    boolean updateDoneById(int id, boolean done);
}
