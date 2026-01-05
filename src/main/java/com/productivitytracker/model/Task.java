package com.productivitytracker.model;

import java.sql.Date;

public class Task {

    private int id;
    private int userId;
    private String name;
    private String priority;
    private String status;
    private Date createdDate;

    public Task() {}

    public Task(int id, int userId, String name,
                String priority, String status, Date createdDate) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.priority = priority;
        this.status = status;
        this.createdDate = createdDate;
    }

    // Convenience constructor (insert)
    public Task(int userId, String name, String priority) {
        this.userId = userId;
        this.name = name;
        this.priority = priority;
        this.status = "Pending";
    }

    public int getId() { return id; }
    public int getUserId() { return userId; }
    public String getName() { return name; }
    public String getPriority() { return priority; }
    public String getStatus() { return status; }
    public Date getCreatedDate() { return createdDate; }

    public void setId(int id) { this.id = id; }
    public void setUserId(int userId) { this.userId = userId; }
    public void setName(String name) { this.name = name; }
    public void setPriority(String priority) { this.priority = priority; }
    public void setStatus(String status) { this.status = status; }
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}