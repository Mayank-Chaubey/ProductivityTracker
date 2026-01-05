package com.productivitytracker.service;

import java.util.List;

import com.productivitytracker.dao.TaskDAO;
import com.productivitytracker.model.Task;
import com.productivitytracker.util.Logger;

public class TaskServiceImpl implements TaskService {

    private final TaskDAO taskDAO;

    public TaskServiceImpl() {
        this.taskDAO = new TaskDAO();
    }

    // ---------- ADD TASK ----------
    @Override
    public boolean addTask(int userId, String taskName, String priority) {
        try {
            if (userId <= 0) {
                throw new IllegalArgumentException("Invalid userId");
            }
            if (taskName == null || taskName.isBlank()) {
                return false;
            }
            if (priority == null || priority.isBlank()) {
                return false;
            }
            return taskDAO.addTask(userId, taskName, priority);
        } catch (Exception e) {
            Logger.logError("Service error in addTask", e);
            return false;
        }
    }

    // ---------- MARK TASK COMPLETED ----------
    @Override
    public boolean completeTask(int taskId, int userId) {
        try {
            if (taskId <= 0 || userId <= 0) {
                throw new IllegalArgumentException("Invalid taskId or userId");
            }
            return taskDAO.completeTask(taskId, userId);
        } catch (Exception e) {
            Logger.logError("Service error in completeTask", e);
            return false;
        }
    }

    // ---------- FETCH TASKS ----------
    @Override
    public List<Task> getTasks(int userId) {
        try {
            if (userId <= 0) {
                throw new IllegalArgumentException("Invalid userId");
            }
            return taskDAO.getTasks(userId);
        } catch (Exception e) {
            Logger.logError("Service error in getTasks", e);
            return List.of();
        }
    }

    // ---------- COUNT COMPLETED ----------
    @Override
    public int countCompletedTasks(int userId) {
        try {
            if (userId <= 0) {
                return 0;
            }
            return taskDAO.countCompletedTasks(userId);
        } catch (Exception e) {
            Logger.logError("Service error in countCompletedTasks", e);
            return 0;
        }
    }
}