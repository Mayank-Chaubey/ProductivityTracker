package com.productivitytracker.dto;

import java.util.List;

/**
 * Complete view model for the calendar page.
 */
public class CalendarViewDTO {

    private final String monthValue;
    private final String previousMonthValue;
    private final String nextMonthValue;
    private final String monthLabel;
    private final List<CalendarDayDTO> gridDays;
    private final List<CalendarReminderDTO> reminders;
    private final int totalEvents;
    private final int overdueCount;
    private final int dueTodayCount;
    private final int upcomingCount;
    private final int focusMinutes;

    public CalendarViewDTO(String monthValue, String previousMonthValue, String nextMonthValue,
                           String monthLabel, List<CalendarDayDTO> gridDays,
                           List<CalendarReminderDTO> reminders, int totalEvents,
                           int overdueCount, int dueTodayCount, int upcomingCount,
                           int focusMinutes) {
        this.monthValue = monthValue;
        this.previousMonthValue = previousMonthValue;
        this.nextMonthValue = nextMonthValue;
        this.monthLabel = monthLabel;
        this.gridDays = gridDays == null ? List.of() : List.copyOf(gridDays);
        this.reminders = reminders == null ? List.of() : List.copyOf(reminders);
        this.totalEvents = totalEvents;
        this.overdueCount = overdueCount;
        this.dueTodayCount = dueTodayCount;
        this.upcomingCount = upcomingCount;
        this.focusMinutes = focusMinutes;
    }

    public String getMonthValue() {
        return monthValue;
    }

    public String getPreviousMonthValue() {
        return previousMonthValue;
    }

    public String getNextMonthValue() {
        return nextMonthValue;
    }

    public String getMonthLabel() {
        return monthLabel;
    }

    public List<CalendarDayDTO> getGridDays() {
        return gridDays;
    }

    public List<CalendarReminderDTO> getReminders() {
        return reminders;
    }

    public int getTotalEvents() {
        return totalEvents;
    }

    public int getOverdueCount() {
        return overdueCount;
    }

    public int getDueTodayCount() {
        return dueTodayCount;
    }

    public int getUpcomingCount() {
        return upcomingCount;
    }

    public int getFocusMinutes() {
        return focusMinutes;
    }
}
