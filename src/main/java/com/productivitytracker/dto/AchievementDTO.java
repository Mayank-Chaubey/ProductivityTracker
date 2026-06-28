package com.productivitytracker.dto;

import java.time.LocalDate;

/**
 * Represents a badge or achievement unlocked by a user.
 */
public class AchievementDTO {

    private final String code;
    private final String title;
    private final String description;
    private final String icon;
    private final int points;
    private final boolean unlocked;
    private final LocalDate unlockedOn;

    public AchievementDTO(String code, String title, String description, String icon,
                          int points, boolean unlocked, LocalDate unlockedOn) {
        this.code = code;
        this.title = title;
        this.description = description;
        this.icon = icon;
        this.points = points;
        this.unlocked = unlocked;
        this.unlockedOn = unlockedOn;
    }

    public String getCode() {
        return code;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getIcon() {
        return icon;
    }

    public int getPoints() {
        return points;
    }

    public boolean isUnlocked() {
        return unlocked;
    }

    public LocalDate getUnlockedOn() {
        return unlockedOn;
    }
}
