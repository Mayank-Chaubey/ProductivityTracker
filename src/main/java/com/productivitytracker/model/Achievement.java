package com.productivitytracker.model;

import java.time.LocalDate;

/**
 * Achievement unlocked by a user.
 */
public class Achievement {

    private String code;
    private int userId;
    private LocalDate unlockedOn;

    public Achievement() {
    }

    public Achievement(String code, int userId, LocalDate unlockedOn) {
        this.code = code;
        this.userId = userId;
        this.unlockedOn = unlockedOn;
    }

    public String getCode() {
        return code;
    }

    public int getUserId() {
        return userId;
    }

    public LocalDate getUnlockedOn() {
        return unlockedOn;
    }
}
