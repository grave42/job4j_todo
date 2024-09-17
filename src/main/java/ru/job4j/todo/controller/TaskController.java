package ru.job4j.todo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.service.TaskService;

import java.util.Collection;
import java.util.List;

@Controller
@RequestMapping("/alltasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public String allTasks(@RequestParam(name = "filter", required = false, defaultValue = "all") String filter, Model model) {
        Collection<Task> tasks;

        switch (filter) {
            case "done":
                tasks = taskService.findByDone(true); // Метод для поиска выполненных задач
                break;
            case "new":
                tasks = taskService.findByDone(false); // Метод для поиска новых задач
                break;
            default:
                tasks = taskService.findAll(); // Метод для поиска всех задач
                break;
        }

        model.addAttribute("alltasks", tasks);
        return "alltasks/list";
    }
}
