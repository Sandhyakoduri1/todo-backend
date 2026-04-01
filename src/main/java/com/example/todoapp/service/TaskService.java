package com.example.todoapp.service;

import com.example.todoapp.dto.TaskRequest;
import com.example.todoapp.exception.ResourceNotFoundException;
import com.example.todoapp.model.Task;
import com.example.todoapp.model.TaskPriority;
import com.example.todoapp.model.TaskStatus;
import com.example.todoapp.model.User;
import com.example.todoapp.repository.TaskRepository;
import com.example.todoapp.repository.UserRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public Task createTask(TaskRequest request, String email) {
        Task task = new Task();

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(TaskStatus.PENDING);
        task.setPriority(request.getPriority() != null ? request.getPriority() : TaskPriority.MEDIUM);
        task.setDueDate(request.getDueDate());

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        task.setUser(user);

        return taskRepository.save(task);
    }

    public List<Task> getAllTasks(String email) {
        return taskRepository.findByUserEmail(email);
    }

    public Task getTaskById(Long id, String email) {
        return taskRepository.findByIdAndUserEmail(id, email)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
    }

    public Task updateTask(Long id, TaskRequest request, String email) {
        Task task = getTaskById(id, email);
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setPriority(request.getPriority() != null ? request.getPriority() : TaskPriority.MEDIUM);
        task.setDueDate(request.getDueDate());

        return taskRepository.save(task);
    }

    public Task markComplete(Long id, String email) {
        Task task = getTaskById(id, email);
        task.setStatus(TaskStatus.COMPLETED);
        return taskRepository.save(task);
    }

    public Task markPending(Long id, String email) {
        Task task = getTaskById(id, email);
        task.setStatus(TaskStatus.PENDING);
        return taskRepository.save(task);
    }

    public void deleteTask(Long id, String email) {
        Task task = getTaskById(id, email);
        taskRepository.delete(task);
    }

    public List<Task> getOverdueTasks(String email) {
        return taskRepository.findByUserEmailAndDueDateBeforeAndStatusNot(
                email,
                LocalDate.now(),
                TaskStatus.COMPLETED
        );
    }

    public List<Task> getTasksDueToday(String email) {
        return taskRepository.findByUserEmailAndDueDateAndStatusNot(
                email,
                LocalDate.now(),
                TaskStatus.COMPLETED
        );
    }

    public List<Task> getTasksDueThisWeek(String email) {
        LocalDate today = LocalDate.now();
        LocalDate endOfWeek = today.plusDays(6);

        return taskRepository.findByUserEmailAndDueDateBetweenAndStatusNot(
                email,
                today,
                endOfWeek,
                TaskStatus.COMPLETED
        );
    }

    public List<Task> getTasksSortedByDueDate(String email, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by("dueDate").descending()
                : Sort.by("dueDate").ascending();

        return taskRepository.findByUserEmail(email, sort);
    }
}