package com.productivitytracker.model;

public class TaskSubtask {

    private long id;
    private int taskId;
    private int userId;
    private String title;
    private boolean completed;
    private int positionIndex;

    public TaskSubtask() {}

    public TaskSubtask(long id, int taskId, int userId, String title, boolean completed, int positionIndex) {
        this.id = id;
        this.taskId = taskId;
        this.userId = userId;
        this.title = title;
        this.completed = completed;
        this.positionIndex = positionIndex;
    }

    public long getId() {
        return id;
    }

    public int getTaskId() {
        return taskId;
    }

    public int getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public boolean isCompleted() {
        return completed;
    }

    public int getPositionIndex() {
        return positionIndex;
    }
}
