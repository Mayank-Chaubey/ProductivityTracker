package com.productivitytracker.dto;

/**
 * Activity data prepared for JSP rendering and analytics.
 */
public class ActivityDTO {

    private final int id;
    private final String name;
    private final String category;
    private final int duration;
    private final String notes;
    private final String activityDate;

    public ActivityDTO(int id, String name, int duration, String activityDate) {
        this(id, name, "other", duration, "", activityDate);
    }

    public ActivityDTO(int id, String name, String category, int duration, String notes, String activityDate) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.duration = duration;
        this.notes = notes;
        this.activityDate = activityDate;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public int getDuration() {
        return duration;
    }

    public String getNotes() {
        return notes;
    }

    public String getActivityDate() {
        return activityDate;
    }
}
