package com.productivitytracker.dto;

/**
 * Task data prepared for JSP rendering.
 */
public class TaskDTO {

    private final int id;
    private final String name;
    private final String description;
    private final String priority;
    private final String status;
    private final String createdDate;
    private final String dueDate;
    private final String tags;
    private final boolean recurring;
    private final String recurrenceRule;
    private final java.util.List<com.productivitytracker.model.TaskSubtask> subtasks;

    public TaskDTO(int id, String name, String description, String priority,
                   String status, String createdDate, String dueDate) {
        this(id, name, description, priority, status, createdDate, dueDate, "", false, "", java.util.List.of());
    }

    public TaskDTO(int id, String name, String description, String priority,
                   String status, String createdDate, String dueDate, String tags,
                   boolean recurring, String recurrenceRule,
                   java.util.List<com.productivitytracker.model.TaskSubtask> subtasks) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.priority = priority;
        this.status = status;
        this.createdDate = createdDate;
        this.dueDate = dueDate;
        this.tags = tags;
        this.recurring = recurring;
        this.recurrenceRule = recurrenceRule;
        this.subtasks = subtasks == null ? java.util.List.of() : subtasks;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPriority() {
        return priority;
    }

    public String getStatus() {
        return status;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public String getDueDate() {
        return dueDate;
    }

    public String getTags() {
        return tags;
    }

    public boolean isRecurring() {
        return recurring;
    }

    public String getRecurrenceRule() {
        return recurrenceRule;
    }

    public java.util.List<com.productivitytracker.model.TaskSubtask> getSubtasks() {
        return subtasks;
    }
}
