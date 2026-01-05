package com.productivitytracker.model;

import java.sql.Date;

/**
 * Model for Activity entity. Represents a user activity record.
 */
public class Activity {

    private int id;
    private int userId;
    private String name;
    private int duration; // minutes
    private Date activityDate;

    /**
     * Default constructor (required for frameworks).
     */
    public Activity() {}

    /**
     * Full constructor (used when reading from DB).
     */
    public Activity(int id, int userId, String name, int duration, Date activityDate) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.duration = duration;
        this.activityDate = activityDate;
    }

    /**
     * Convenience constructor (used for inserts).
     */
    public Activity(int userId, String name, int duration) {
        this.userId = userId;
        this.name = name;
        this.duration = duration;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Date getActivityDate() {
        return activityDate;
    }

    public void setActivityDate(Date activityDate) {
        this.activityDate = activityDate;
    }

    @Override
    public String toString() {
        return "Activity{" +
                "id=" + id +
                ", userId=" + userId +
                ", name='" + name + '\'' +
                ", duration=" + duration +
                ", activityDate=" + activityDate +
                '}';
    }
}