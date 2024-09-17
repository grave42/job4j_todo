package ru.job4j.todo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "ru.job4j.todo.model")
public class Job4jTodoApplication {

    public static void main(String[] args) {
        SpringApplication.run(Job4jTodoApplication.class, args);
    }
}
