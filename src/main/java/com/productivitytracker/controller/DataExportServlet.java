package com.productivitytracker.controller;

import com.productivitytracker.model.Habit;
import com.productivitytracker.model.JournalEntry;
import com.productivitytracker.model.Reminder;
import com.productivitytracker.model.Task;
import com.productivitytracker.dto.GoalDTO;
import com.productivitytracker.service.GoalService;
import com.productivitytracker.service.HabitService;
import com.productivitytracker.service.JournalService;
import com.productivitytracker.service.ReminderService;
import com.productivitytracker.service.TaskService;
import com.productivitytracker.service.TaskServiceImpl;
import com.productivitytracker.util.JsonUtil;
import com.productivitytracker.util.SessionUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/DataExportServlet")
public class DataExportServlet extends HttpServlet {

    private final TaskService taskService = new TaskServiceImpl();
    private final HabitService habitService = new HabitService();
    private final GoalService goalService = new GoalService();
    private final ReminderService reminderService = new ReminderService();
    private final JournalService journalService = new JournalService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (!SessionUtil.isLoggedIn(session)) {
            response.sendRedirect(request.getContextPath() + "/jsp/login.jsp");
            return;
        }
        int userId = SessionUtil.getUserId(session);
        response.setContentType("application/json");
        response.setHeader("Content-Disposition", "attachment; filename=productivity-export-" + LocalDate.now() + ".json");
        response.getWriter().write(buildJson(userId));
    }

    private String buildJson(int userId) {
        StringBuilder json = new StringBuilder("{");
        appendTasks(json, taskService.getTasks(userId));
        json.append(',');
        appendHabits(json, habitService.getHabits(userId));
        json.append(',');
        appendGoals(json, goalService.getGoals(userId));
        json.append(',');
        appendReminders(json, reminderService.getReminders(userId));
        json.append(',');
        appendJournal(json, journalService.getEntries(userId, null));
        json.append('}');
        return json.toString();
    }

    private void appendTasks(StringBuilder json, List<Task> tasks) {
        json.append("\"tasks\":[");
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            if (i > 0) {
                json.append(',');
            }
            json.append("{\"id\":").append(task.getId())
                    .append(",\"name\":").append(JsonUtil.quote(task.getName()))
                    .append(",\"description\":").append(JsonUtil.quote(task.getDescription()))
                    .append(",\"priority\":").append(JsonUtil.quote(task.getPriority()))
                    .append(",\"status\":").append(JsonUtil.quote(task.getStatus()))
                    .append(",\"dueDate\":").append(JsonUtil.quote(task.getDueDate() == null ? "" : task.getDueDate().toString()))
                    .append(",\"tags\":").append(JsonUtil.quote(task.getTags()))
                    .append('}');
        }
        json.append(']');
    }

    private void appendHabits(StringBuilder json, List<Habit> habits) {
        json.append("\"habits\":[");
        for (int i = 0; i < habits.size(); i++) {
            Habit habit = habits.get(i);
            if (i > 0) {
                json.append(',');
            }
            json.append("{\"id\":").append(habit.getId())
                    .append(",\"name\":").append(JsonUtil.quote(habit.getName()))
                    .append(",\"frequency\":").append(JsonUtil.quote(habit.getFrequency()))
                    .append(",\"frequencyRule\":").append(JsonUtil.quote(habit.getFrequencyRule()))
                    .append(",\"category\":").append(JsonUtil.quote(habit.getCategory()))
                    .append(",\"streak\":").append(habit.getStreak())
                    .append('}');
        }
        json.append(']');
    }

    private void appendReminders(StringBuilder json, List<Reminder> reminders) {
        json.append("\"reminders\":[");
        for (int i = 0; i < reminders.size(); i++) {
            Reminder reminder = reminders.get(i);
            if (i > 0) {
                json.append(',');
            }
            json.append("{\"id\":").append(reminder.getId())
                    .append(",\"title\":").append(JsonUtil.quote(reminder.getTitle()))
                    .append(",\"type\":").append(JsonUtil.quote(reminder.getReminderType()))
                    .append(",\"remindAt\":").append(JsonUtil.quote(String.valueOf(reminder.getRemindAt())))
                    .append(",\"status\":").append(JsonUtil.quote(reminder.getStatus()))
                    .append('}');
        }
        json.append(']');
    }

    private void appendGoals(StringBuilder json, List<GoalDTO> goals) {
        json.append("\"goals\":[");
        for (int i = 0; i < goals.size(); i++) {
            GoalDTO goal = goals.get(i);
            if (i > 0) {
                json.append(',');
            }
            json.append("{\"id\":").append(goal.getId())
                    .append(",\"title\":").append(JsonUtil.quote(goal.getTitle()))
                    .append(",\"type\":").append(JsonUtil.quote(goal.getGoalType()))
                    .append(",\"target\":").append(goal.getTargetValue())
                    .append(",\"current\":").append(goal.getCurrentValue())
                    .append(",\"progress\":").append(goal.getProgressPercentage())
                    .append(",\"status\":").append(JsonUtil.quote(goal.getStatus()))
                    .append('}');
        }
        json.append(']');
    }

    private void appendJournal(StringBuilder json, List<JournalEntry> entries) {
        json.append("\"journal\":[");
        for (int i = 0; i < entries.size(); i++) {
            JournalEntry entry = entries.get(i);
            if (i > 0) {
                json.append(',');
            }
            json.append("{\"id\":").append(entry.getId())
                    .append(",\"title\":").append(JsonUtil.quote(entry.getTitle()))
                    .append(",\"content\":").append(JsonUtil.quote(entry.getContent()))
                    .append(",\"moodScore\":").append(entry.getMoodScore() == null ? "null" : entry.getMoodScore())
                    .append(",\"entryDate\":").append(JsonUtil.quote(String.valueOf(entry.getEntryDate())))
                    .append('}');
        }
        json.append(']');
    }
}
