package com.productivitytracker.model;

import java.sql.Date;

/**
 * Model for Habit entity. Represents a user habit record.
 */
public class Habit {

    private int id;
    private int userId;
    private String name;
    private String frequency;
    private int streak;
    private Date lastDoneDate;

    public Habit() {}

    public Habit(int id, int userId, String name,
                 String frequency, int streak, Date lastDoneDate) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.frequency = frequency;
        this.streak = streak;
        this.lastDoneDate = lastDoneDate;
    }

    // Convenience constructor (for insert)
    public Habit(int userId, String name, String frequency) {
        this.userId = userId;
        this.name = name;
        this.frequency = frequency;
        this.streak = 0;
    }

    public int getId() { return id; }
    public int getUserId() { return userId; }
    public String getName() { return name; }
    public String getFrequency() { return frequency; }
    public int getStreak() { return streak; }
    public Date getLastDoneDate() { return lastDoneDate; }

    public void setId(int id) { this.id = id; }
    public void setUserId(int userId) { this.userId = userId; }
    public void setName(String name) { this.name = name; }
    public void setFrequency(String frequency) { this.frequency = frequency; }
    public void setStreak(int streak) { this.streak = streak; }
    public void setLastDoneDate(Date lastDoneDate) {
        this.lastDoneDate = lastDoneDate;
    }
}