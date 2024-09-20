package ru.job4j.todo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.service.TaskService;

import java.util.Collection;

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
                tasks = taskService.findByDone(true);
                break;
            case "new":
                tasks = taskService.findByDone(false);
                break;
            default:
                tasks = taskService.findAll();
                break;
        }

        model.addAttribute("alltasks", tasks);
        return "alltasks/list";
    }

    @GetMapping("/{id}")
    public String getById(Model model, @PathVariable int id) {
        var taskOptional = taskService.findById(id);
        if (taskOptional.isEmpty()) {
            model.addAttribute("message", "Кандидат с указанным идентификатором не найден");
            return "errors/404";
        }
        model.addAttribute("task", taskOptional.get());
        return "alltasks/one";
    }

    @PostMapping("/complete/{id}")
    public String completeTask(@PathVariable int id) {
        var taskOptional = taskService.findById(id);
        if (taskOptional.isPresent()) {
            var task = taskOptional.get();
            task.setDone(true);
            taskService.update(task);
        }
        return "redirect:/alltasks";
    }

    @GetMapping("/delete/{id}")
    public String deleteTask(@PathVariable int id) {
        taskService.deleteById(id);
        return "redirect:/alltasks";
    }

    @PostMapping("/edit/{id}")
    public String editTask(@PathVariable int id, Task taskDetails) {
        var taskOptional = taskService.findById(id);
        if (taskOptional.isPresent()) {
            var task = taskOptional.get();
            task.setTitle(taskDetails.getTitle());
            task.setDescription(taskDetails.getDescription());
            taskService.update(task);
        }
        return "redirect:/alltasks";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        var taskOptional = taskService.findById(id);
        if (taskOptional.isPresent()) {
            model.addAttribute("task", taskOptional.get());
            return "alltasks/edit";
        }
        return "redirect:/alltasks";
    }
}
