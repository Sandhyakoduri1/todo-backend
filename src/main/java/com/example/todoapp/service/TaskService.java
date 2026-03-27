package com.example.todoapp.service;

import com.example.todoapp.dto.TaskRequest;
import com.example.todoapp.exception.ResourceNotFoundException;
import com.example.todoapp.model.Task;
import com.example.todoapp.model.TaskPriority;
import com.example.todoapp.model.TaskStatus;
import com.example.todoapp.repository.TaskRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task createTask(TaskRequest request) {
        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(TaskStatus.PENDING);
        task.setPriority(request.getPriority() != null ? request.getPriority() : TaskPriority.MEDIUM);
        task.setDueDate(request.getDueDate());
        task.setCreatedAt(LocalDateTime.now());

        return taskRepository.save(task);
    }

    public Page<Task> getTasksWithPagination(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return taskRepository.findAll(pageable);
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
    }

    public Task updateTask(Long id, TaskRequest request) {
        Task task = getTaskById(id);
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setPriority(request.getPriority() != null ? request.getPriority() : TaskPriority.MEDIUM);
        task.setDueDate(request.getDueDate());

        return taskRepository.save(task);
    }

    public Task markTaskAsCompleted(Long id) {
        Task task = getTaskById(id);
        task.setStatus(TaskStatus.COMPLETED);

        return taskRepository.save(task);
    }

    public void deleteTask(Long id) {
        Task task = getTaskById(id);
        taskRepository.delete(task);
    }

    public List<Task> getTasksByStatus(TaskStatus status) {
        return taskRepository.findByStatus(status);
    }

    public List<Task> searchTasksByTitle(String title) {
        return taskRepository.findByTitleContainingIgnoreCase(title);
    }

    public List<Task> getTasksByPriority(TaskPriority priority) {
        return taskRepository.findByPriority(priority);
    }

    public List<Task> getTasksSortedByDueDate(String direction) {
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by("dueDate").descending()
                : Sort.by("dueDate").ascending();

        return taskRepository.findAll(sort);
    }

    public List<Task> getOverdueTasks() {
        return taskRepository.findByDueDateBeforeAndStatusNot(LocalDate.now(), TaskStatus.COMPLETED);
    }

    public List<Task> getTasksDueToday() {
        return taskRepository.findByDueDateAndStatusNot(LocalDate.now(), TaskStatus.COMPLETED);
    }

    public Task markTaskAsPending(Long id) {
        Task task = getTaskById(id);
        task.setStatus(TaskStatus.PENDING);
        return taskRepository.save(task);
    }

    public List<Task> getTasksDueThisWeek() {
        LocalDate today = LocalDate.now();
        LocalDate endOfWeek = today.plusDays(6);

        return taskRepository.findByDueDateBetweenAndStatusNot(today, endOfWeek, TaskStatus.COMPLETED);
    }
}