package com.example.todoapp.repository;

import com.example.todoapp.model.Task;
import com.example.todoapp.model.TaskStatus;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByUserEmail(String email);

    List<Task> findByUserEmail(String email, Sort sort);

    Optional<Task> findByIdAndUserEmail(Long id, String email);

    List<Task> findByUserEmailAndDueDateBeforeAndStatusNot(
            String email,
            LocalDate dueDate,
            TaskStatus status
    );

    List<Task> findByUserEmailAndDueDateAndStatusNot(
            String email,
            LocalDate dueDate,
            TaskStatus status
    );

    List<Task> findByUserEmailAndDueDateBetweenAndStatusNot(
            String email,
            LocalDate startDate,
            LocalDate endDate,
            TaskStatus status
    );
}