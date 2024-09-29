package ru.job4j.todo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.service.TaskService;

import java.util.Collection;
import java.util.Map;
import java.util.function.Supplier;

@Controller
@RequestMapping("/alltasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public String allTasks(@RequestParam(name = "filter", required = false, defaultValue = "all") String filter, Model model) {
        Map<String, Supplier<Collection<Task>>> filterActions = Map.of(
                "done", () -> taskService.findByDone(true),
                "new", () -> taskService.findByDone(false)
        );

        Collection<Task> tasks = filterActions.getOrDefault(filter, taskService::findAll).get();

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
    public String deleteTask(@PathVariable int id, Model model) {
        boolean isDeleted = taskService.deleteById(id);
        if (!isDeleted) {
            model.addAttribute("message", "Не удалось удалить задачу. Возможно, задача с таким ID не найдена.");
            return "errors/404";
        }
        return "redirect:/alltasks";
    }

    @PostMapping("/edit/{id}")
    public String editTask(@PathVariable int id, Task taskDetails, Model model, RedirectAttributes redirectAttributes) {
        taskDetails.setId(id);
        var isUpdated = taskService.update(taskDetails);
        if (!isUpdated) {
            redirectAttributes.addFlashAttribute("error", "Не удалось отредактировать задачу. Пожалуйста, попробуйте еще раз.");
            return "errors/404";
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
        return "errors/404";
    }

    @PostMapping("/updateDone/{id}")
    public String updateDone(@PathVariable int id, @RequestParam("done") boolean done, RedirectAttributes redirectAttributes) {
        boolean isUpdated = taskService.updateDoneById(id, done);

        if (!isUpdated) {
            redirectAttributes.addFlashAttribute("error", "Failed to update task.");
            return "errors/404";
        }

        return "redirect:/alltasks";
    }
}
