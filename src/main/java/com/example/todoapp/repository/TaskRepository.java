package com.example.todoapp.repository;

import com.example.todoapp.model.Task;
import com.example.todoapp.model.TaskPriority;
import com.example.todoapp.model.TaskStatus;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByStatus(TaskStatus status);

    List<Task> findByTitleContainingIgnoreCase(String title);

    List<Task> findByPriority(TaskPriority priority);

    List<Task> findByDueDateBeforeAndStatusNot(LocalDate date, TaskStatus status);

    List<Task> findByDueDateAndStatusNot(LocalDate date, TaskStatus status);

    List<Task> findByDueDateBetweenAndStatusNot(LocalDate startDate, LocalDate endDate, TaskStatus status);

    List<Task> findAll(Sort sort);
}