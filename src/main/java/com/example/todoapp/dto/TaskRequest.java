package com.example.todoapp.dto;

import com.example.todoapp.model.TaskPriority;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TaskRequest {

    @NotBlank(message = "Title cannot be empty")
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    private String title;

    @Size(max = 300, message = "Description cannot exceed 300 characters")
    private String description;

    private TaskPriority priority;

    @FutureOrPresent(message = "Due date cannot be in the past")
   private LocalDate dueDate;
}