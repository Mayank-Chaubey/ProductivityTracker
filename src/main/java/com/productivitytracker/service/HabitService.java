package com.productivitytracker.service;

import java.util.List;

import com.productivitytracker.dao.HabitDAO;
import com.productivitytracker.model.Habit;
import com.productivitytracker.util.Logger;

/**
 * Service layer for Habit business logic and validation.
 * All methods are user-scoped and input-validated.
 */
public class HabitService {

    private final HabitDAO habitDAO;

    public HabitService() {
        this.habitDAO = new HabitDAO();
    }

    // ---------- ADD HABIT ----------
    public boolean addHabit(int userId, String habitName, String frequency) {
        try {
            if (userId <= 0) {
                return false;
            }

            if (habitName == null || habitName.isEmpty()) {
                return false;
            }

            if (frequency == null || frequency.isEmpty()) {
                return false;
            }

            return habitDAO.addHabit(userId, habitName, frequency);
        } catch (Exception e) {
            Logger.logError("Service error in addHabit", e);
            return false;
        }
    }

    // ---------- MARK HABIT AS DONE ----------
    public boolean markHabitDone(int habitId, int userId) {
        try {
            if (habitId <= 0 || userId <= 0) {
                return false;
            }

            return habitDAO.markHabitDone(habitId, userId);
        } catch (Exception e) {
            Logger.logError("Service error in markHabitDone", e);
            return false;
        }
    }

    // ---------- GET USER HABITS ----------
    public List<Habit> getHabits(int userId) {
        try {
            if (userId <= 0) {
                return List.of(); // safe empty list
            }

            return habitDAO.getHabits(userId);
        } catch (Exception e) {
            Logger.logError("Service error in getHabits", e);
            return List.of();
        }
    }

    // ---------- COUNT HABITS ----------
    public int countHabits(int userId) {
        try {
            if (userId <= 0) {
                return 0;
            }

            return habitDAO.countHabits(userId);
        } catch (Exception e) {
            Logger.logError("Service error in countHabits", e);
            return 0;
        }
    }
}