package com.productivitytracker.dto;

/**
 * Habit data prepared for JSP rendering.
 */
public class HabitDTO {

    private final int id;
    private final String name;
    private final String frequency;
    private final String frequencyRule;
    private final String category;
    private final int streak;
    private final String lastDoneDate;
    private final boolean doneToday;

    public HabitDTO(int id, String name, String frequency, String category, int streak,
                    String lastDoneDate, boolean doneToday) {
        this(id, name, frequency, "", category, streak, lastDoneDate, doneToday);
    }

    public HabitDTO(int id, String name, String frequency, String frequencyRule, String category, int streak,
                    String lastDoneDate, boolean doneToday) {
        this.id = id;
        this.name = name;
        this.frequency = frequency;
        this.frequencyRule = frequencyRule;
        this.category = category;
        this.streak = streak;
        this.lastDoneDate = lastDoneDate;
        this.doneToday = doneToday;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFrequency() {
        return frequency;
    }

    public String getFrequencyRule() {
        return frequencyRule;
    }

    public String getCategory() {
        return category;
    }

    public int getStreak() {
        return streak;
    }

    public String getLastDoneDate() {
        return lastDoneDate;
    }

    public boolean isDoneToday() {
        return doneToday;
    }
}
