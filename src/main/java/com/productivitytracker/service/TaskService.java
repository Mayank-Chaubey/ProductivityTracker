package com.productivitytracker.service;

import java.util.List;
import com.productivitytracker.model.Task;

public interface TaskService {

    boolean addTask(int userId, String taskName, String priority);

    boolean completeTask(int taskId, int userId);

    List<Task> getTasks(int userId);

    int countCompletedTasks(int userId);
}