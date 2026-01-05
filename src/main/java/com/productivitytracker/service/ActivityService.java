package com.productivitytracker.service;

import java.util.List;

import com.productivitytracker.dao.ActivityDAO;
import com.productivitytracker.model.Activity;
import com.productivitytracker.util.Logger;

/**
 * Service layer for Activity business logic and validation.
 * All methods are user-scoped and input-validated.
 */
public class ActivityService {

    private final ActivityDAO activityDAO;

    public ActivityService() {
        this.activityDAO = new ActivityDAO();
    }

    /**
     * Add a new activity for a user. Returns true if successful.
     * @param userId the user ID (must be > 0)
     * @param activityName the activity name (validated)
     * @param duration duration in minutes (1-1440)
     * @return true if added, false otherwise
     */
    public boolean addActivity(int userId, String activityName, int duration) {
        try {
            if (userId <= 0 || activityName == null || activityName.isEmpty() || duration <= 0 || duration > 1440) {
                return false;
            }

            return activityDAO.addActivity(userId, activityName, duration);
        } catch (Exception e) {
            Logger.logError("Service error in addActivity", e);
            return false;
        }
    }

    /**
     * Get today's activities for a user.
     * @param userId the user ID
     * @return list of Activity objects (never null)
     */
    public List<Activity> getTodayActivities(int userId) {
        try {
            if (userId <= 0) {
                return List.of(); // safe empty list
            }

            return activityDAO.getTodayActivities(userId);
        } catch (Exception e) {
            Logger.logError("Service error in getTodayActivities", e);
            return List.of();
        }
    }

    /**
     * Get all activities for a user.
     * @param userId the user ID
     * @return list of Activity objects (never null)
     */
    public List<Activity> getAllActivities(int userId) {
        try {
            if (userId <= 0) {
                return List.of();
            }

            return activityDAO.getAllActivities(userId);
        } catch (Exception e) {
            Logger.logError("Service error in getAllActivities", e);
            return List.of();
        }
    }
}