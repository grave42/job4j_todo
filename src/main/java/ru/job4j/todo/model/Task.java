package ru.job4j.todo.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Task {

    private int id;
    private String description;
    private String title;
    private LocalDateTime crated;
    private boolean done;
}
