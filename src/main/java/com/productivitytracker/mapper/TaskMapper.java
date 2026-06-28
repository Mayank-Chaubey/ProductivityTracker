package com.productivitytracker.mapper;

import com.productivitytracker.dto.TaskDTO;
import com.productivitytracker.model.Task;

import java.sql.Date;
import java.util.List;

/**
 * Converts Task model objects into view DTOs.
 */
public final class TaskMapper {

    private TaskMapper() {
        throw new AssertionError("TaskMapper should not be instantiated");
    }

    public static TaskDTO toDTO(Task task) {
        if (task == null) {
            return null;
        }

        Date createdDate = task.getCreatedDate();
        Date dueDate = task.getDueDate();
        return new TaskDTO(
                task.getId(),
                task.getName(),
                task.getDescription() == null ? "" : task.getDescription(),
                task.getPriority(),
                task.getStatus(),
                createdDate == null ? "" : createdDate.toString(),
                dueDate == null ? "" : dueDate.toString(),
                task.getTags() == null ? "" : task.getTags(),
                task.isRecurring(),
                task.getRecurrenceRule() == null ? "" : task.getRecurrenceRule(),
                task.getSubtasks()
        );
    }

    public static List<TaskDTO> toDTOList(List<Task> tasks) {
        return tasks.stream().map(TaskMapper::toDTO).toList();
    }
}
