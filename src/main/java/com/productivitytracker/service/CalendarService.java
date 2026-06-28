package com.productivitytracker.service;

import com.productivitytracker.dao.PomodoroDAO;
import com.productivitytracker.dto.CalendarDayDTO;
import com.productivitytracker.dto.CalendarEventDTO;
import com.productivitytracker.dto.CalendarReminderDTO;
import com.productivitytracker.dto.CalendarViewDTO;
import com.productivitytracker.dto.GoalDTO;
import com.productivitytracker.model.Habit;
import com.productivitytracker.model.PomodoroSession;
import com.productivitytracker.model.Task;
import com.productivitytracker.util.Logger;

import java.sql.Date;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Aggregates dated user data for the calendar and reminder page.
 */
public class CalendarService {

    private static final DateTimeFormatter MONTH_LABEL = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.ENGLISH);
    private static final DateTimeFormatter SHORT_DATE = DateTimeFormatter.ofPattern("MMM d", Locale.ENGLISH);

    private final TaskService taskService;
    private final GoalService goalService;
    private final HabitService habitService;
    private final PomodoroDAO pomodoroDAO;

    public CalendarService() {
        this.taskService = new TaskServiceImpl();
        this.goalService = new GoalService();
        this.habitService = new HabitService();
        this.pomodoroDAO = new PomodoroDAO();
    }

    public CalendarViewDTO getMonthView(int userId, YearMonth month) {
        try {
            if (userId <= 0) {
                return emptyView(YearMonth.now());
            }

            YearMonth selectedMonth = month == null ? YearMonth.now() : month;
            LocalDate firstDay = selectedMonth.atDay(1);
            LocalDate lastDay = selectedMonth.atEndOfMonth();
            LocalDate today = LocalDate.now();

            List<Task> tasks = taskService.getTasks(userId);
            List<GoalDTO> goals = goalService.getGoals(userId);
            List<Habit> habits = habitService.getHabits(userId);
            List<PomodoroSession> sessions = pomodoroDAO.getSessionsBetween(userId, firstDay, lastDay);

            List<CalendarEventDTO> events = new ArrayList<>();
            events.addAll(taskEvents(tasks, firstDay, lastDay));
            events.addAll(goalEvents(goals, firstDay, lastDay));
            events.addAll(focusEvents(sessions));
            events.sort(Comparator.comparing(CalendarEventDTO::getDate)
                    .thenComparing(CalendarEventDTO::getSortOrder)
                    .thenComparing(CalendarEventDTO::getTitle, String.CASE_INSENSITIVE_ORDER));

            List<CalendarReminderDTO> reminders = reminderCards(tasks, goals, habits, today);
            int overdueCount = (int) reminders.stream().filter(item -> "overdue".equals(item.getUrgency())).count();
            int dueTodayCount = (int) reminders.stream().filter(item -> "today".equals(item.getUrgency())).count();
            int upcomingCount = (int) reminders.stream().filter(item -> "upcoming".equals(item.getUrgency())).count();
            int focusMinutes = sessions.stream().mapToInt(PomodoroSession::getTotalFocusMinutes).sum();

            return new CalendarViewDTO(
                    selectedMonth.toString(),
                    selectedMonth.minusMonths(1).toString(),
                    selectedMonth.plusMonths(1).toString(),
                    selectedMonth.format(MONTH_LABEL),
                    gridDays(selectedMonth, events, today),
                    reminders,
                    events.size(),
                    overdueCount,
                    dueTodayCount,
                    upcomingCount,
                    focusMinutes
            );
        } catch (RuntimeException ex) {
            Logger.logError("Service error in getMonthView", ex);
            return emptyView(month == null ? YearMonth.now() : month);
        }
    }

    private CalendarViewDTO emptyView(YearMonth month) {
        YearMonth selectedMonth = month == null ? YearMonth.now() : month;
        return new CalendarViewDTO(
                selectedMonth.toString(),
                selectedMonth.minusMonths(1).toString(),
                selectedMonth.plusMonths(1).toString(),
                selectedMonth.format(MONTH_LABEL),
                gridDays(selectedMonth, List.of(), LocalDate.now()),
                List.of(),
                0,
                0,
                0,
                0,
                0
        );
    }

    private List<CalendarEventDTO> taskEvents(List<Task> tasks, LocalDate firstDay, LocalDate lastDay) {
        return tasks.stream()
                .filter(task -> task.getDueDate() != null)
                .map(task -> {
                    LocalDate dueDate = task.getDueDate().toLocalDate();
                    String priority = task.getPriority() == null ? "Medium" : task.getPriority();
                    return new CalendarEventDTO(
                            dueDate,
                            "Task",
                            task.getName(),
                            priority + " priority",
                            task.getStatus(),
                            "event-task event-" + priority.toLowerCase(Locale.ENGLISH),
                            10
                    );
                })
                .filter(event -> isBetween(event.getDate(), firstDay, lastDay))
                .toList();
    }

    private List<CalendarEventDTO> goalEvents(List<GoalDTO> goals, LocalDate firstDay, LocalDate lastDay) {
        return goals.stream()
                .filter(goal -> goal.getEndDate() != null)
                .filter(goal -> isBetween(goal.getEndDate(), firstDay, lastDay))
                .map(goal -> new CalendarEventDTO(
                        goal.getEndDate(),
                        "Goal",
                        goal.getTitle(),
                        goal.getProgressPercentage() + "% complete",
                        goal.getStatus(),
                        "event-goal",
                        20
                ))
                .toList();
    }

    private List<CalendarEventDTO> focusEvents(List<PomodoroSession> sessions) {
        Map<LocalDate, List<PomodoroSession>> byDay = sessions.stream()
                .filter(session -> session.getCompletedAt() != null)
                .collect(Collectors.groupingBy(
                        session -> session.getCompletedAt().toLocalDate(),
                        LinkedHashMap::new,
                        Collectors.toList()
                ));

        List<CalendarEventDTO> events = new ArrayList<>();
        for (Map.Entry<LocalDate, List<PomodoroSession>> entry : byDay.entrySet()) {
            int minutes = entry.getValue().stream().mapToInt(PomodoroSession::getTotalFocusMinutes).sum();
            int sessionsCount = entry.getValue().size();
            events.add(new CalendarEventDTO(
                    entry.getKey(),
                    "Focus",
                    "Focus sessions",
                    sessionsCount + " sessions - " + minutes + "m",
                    "Logged",
                    "event-focus",
                    30
            ));
        }
        return events;
    }

    private List<CalendarDayDTO> gridDays(YearMonth month, List<CalendarEventDTO> events, LocalDate today) {
        Map<LocalDate, List<CalendarEventDTO>> eventsByDate = events.stream()
                .collect(Collectors.groupingBy(CalendarEventDTO::getDate));

        LocalDate firstDay = month.atDay(1);
        int sundayOffset = firstDay.getDayOfWeek().getValue() % 7;
        LocalDate gridStart = firstDay.minusDays(sundayOffset);

        List<CalendarDayDTO> days = new ArrayList<>();
        for (int i = 0; i < 42; i++) {
            LocalDate date = gridStart.plusDays(i);
            List<CalendarEventDTO> dayEvents = eventsByDate.getOrDefault(date, List.of()).stream()
                    .sorted(Comparator.comparing(CalendarEventDTO::getSortOrder)
                            .thenComparing(CalendarEventDTO::getTitle, String.CASE_INSENSITIVE_ORDER))
                    .toList();
            days.add(new CalendarDayDTO(date, YearMonth.from(date).equals(month), date.equals(today), dayEvents));
        }
        return days;
    }

    private List<CalendarReminderDTO> reminderCards(List<Task> tasks, List<GoalDTO> goals,
                                                    List<Habit> habits, LocalDate today) {
        LocalDate taskWindow = today.plusDays(7);
        LocalDate goalWindow = today.plusDays(14);
        List<CalendarReminderDTO> reminders = new ArrayList<>();

        for (Task task : tasks) {
            LocalDate dueDate = toLocalDate(task.getDueDate());
            if (dueDate == null || isComplete(task.getStatus()) || dueDate.isAfter(taskWindow)) {
                continue;
            }
            reminders.add(new CalendarReminderDTO(
                    "Task",
                    task.getName(),
                    (task.getPriority() == null ? "Medium" : task.getPriority()) + " priority",
                    dueLabel(dueDate, today),
                    urgency(dueDate, today),
                    "/TaskServlet"
            ));
        }

        for (GoalDTO goal : goals) {
            LocalDate endDate = goal.getEndDate();
            if (endDate == null || "COMPLETED".equalsIgnoreCase(goal.getStatus()) || endDate.isAfter(goalWindow)) {
                continue;
            }
            reminders.add(new CalendarReminderDTO(
                    "Goal",
                    goal.getTitle(),
                    goal.getProgressPercentage() + "% complete",
                    dueLabel(endDate, today),
                    urgency(endDate, today),
                    "/GoalServlet"
            ));
        }

        for (Habit habit : habits) {
            if (!isHabitDueToday(habit, today)) {
                continue;
            }
            reminders.add(new CalendarReminderDTO(
                    "Habit",
                    habit.getName(),
                    safeText(habit.getFrequency(), "Repeating") + " - streak " + habit.getStreak(),
                    "Due today",
                    "today",
                    "/HabitServlet"
            ));
        }

        return reminders.stream()
                .sorted(Comparator.comparingInt(this::urgencyOrder)
                        .thenComparing(CalendarReminderDTO::getType)
                        .thenComparing(CalendarReminderDTO::getTitle, String.CASE_INSENSITIVE_ORDER))
                .limit(12)
                .toList();
    }

    private boolean isHabitDueToday(Habit habit, LocalDate today) {
        if (habit == null || habit.getName() == null || habit.getName().isBlank()) {
            return false;
        }
        LocalDate lastDone = toLocalDate(habit.getLastDoneDate());
        if (today.equals(lastDone)) {
            return false;
        }

        String frequency = habit.getFrequency() == null ? "" : habit.getFrequency().toLowerCase(Locale.ENGLISH);
        if (frequency.contains("daily")) {
            return true;
        }
        if (frequency.contains("weekly")) {
            return lastDone == null || !lastDone.isAfter(today.minusDays(7));
        }
        String dayName = today.getDayOfWeek().name().toLowerCase(Locale.ENGLISH);
        String shortDayName = dayName.substring(0, 3);
        return frequency.contains(dayName) || frequency.contains(shortDayName);
    }

    private LocalDate toLocalDate(Date date) {
        return date == null ? null : date.toLocalDate();
    }

    private boolean isBetween(LocalDate date, LocalDate firstDay, LocalDate lastDay) {
        return date != null && !date.isBefore(firstDay) && !date.isAfter(lastDay);
    }

    private boolean isComplete(String status) {
        return status != null && "completed".equalsIgnoreCase(status.trim());
    }

    private String dueLabel(LocalDate dueDate, LocalDate today) {
        if (dueDate.isBefore(today)) {
            long days = today.toEpochDay() - dueDate.toEpochDay();
            return days == 1 ? "Overdue by 1 day" : "Overdue by " + days + " days";
        }
        if (dueDate.equals(today)) {
            return "Due today";
        }
        if (dueDate.equals(today.plusDays(1))) {
            return "Due tomorrow";
        }
        return "Due " + dueDate.format(SHORT_DATE);
    }

    private String urgency(LocalDate dueDate, LocalDate today) {
        if (dueDate.isBefore(today)) {
            return "overdue";
        }
        if (dueDate.equals(today)) {
            return "today";
        }
        return "upcoming";
    }

    private int urgencyOrder(CalendarReminderDTO reminder) {
        return switch (reminder.getUrgency()) {
            case "overdue" -> 0;
            case "today" -> 1;
            default -> 2;
        };
    }

    private String safeText(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value.trim();
    }
}
