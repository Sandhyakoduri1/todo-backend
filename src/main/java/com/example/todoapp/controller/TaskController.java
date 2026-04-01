package com.example.todoapp.controller;

import com.example.todoapp.dto.TaskRequest;
import com.example.todoapp.model.Task;
import com.example.todoapp.service.TaskService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public List<Task> getAllTasks(Authentication authentication) {
        String email = authentication.getName();
        return taskService.getAllTasks(email);
    }

    @PostMapping
    public Task createTask(@RequestBody TaskRequest request, Authentication authentication) {
        String email = authentication.getName();
        return taskService.createTask(request, email);
    }

    @PutMapping("/{id}")
    public Task updateTask(@PathVariable Long id,
                           @RequestBody TaskRequest request,
                           Authentication authentication) {
        String email = authentication.getName();
        return taskService.updateTask(id, request, email);
    }

    @PutMapping("/{id}/complete")
    public Task markComplete(@PathVariable Long id, Authentication authentication) {
        String email = authentication.getName();
        return taskService.markComplete(id, email);
    }

    @PutMapping("/{id}/pending")
    public Task markPending(@PathVariable Long id, Authentication authentication) {
        String email = authentication.getName();
        return taskService.markPending(id, email);
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id, Authentication authentication) {
        String email = authentication.getName();
        taskService.deleteTask(id, email);
    }

    @GetMapping("/overdue")
    public List<Task> getOverdueTasks(Authentication authentication) {
        return taskService.getOverdueTasks(authentication.getName());
    }

    @GetMapping("/due-today")
    public List<Task> getTasksDueToday(Authentication authentication) {
        return taskService.getTasksDueToday(authentication.getName());
    }

    @GetMapping("/due-this-week")
    public List<Task> getTasksDueThisWeek(Authentication authentication) {
        return taskService.getTasksDueThisWeek(authentication.getName());
    }

    @GetMapping("/sort-by-due-date")
    public List<Task> getTasksSortedByDueDate(
            @RequestParam(defaultValue = "asc") String direction,
            Authentication authentication) {
        return taskService.getTasksSortedByDueDate(authentication.getName(), direction);
    }
}