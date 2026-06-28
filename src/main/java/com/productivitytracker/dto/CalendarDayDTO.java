package com.productivitytracker.dto;

import java.time.LocalDate;
import java.util.List;

/**
 * Calendar grid cell with visible event data.
 */
public class CalendarDayDTO {

    private final LocalDate date;
    private final boolean inCurrentMonth;
    private final boolean today;
    private final List<CalendarEventDTO> events;

    public CalendarDayDTO(LocalDate date, boolean inCurrentMonth, boolean today,
                          List<CalendarEventDTO> events) {
        this.date = date;
        this.inCurrentMonth = inCurrentMonth;
        this.today = today;
        this.events = events == null ? List.of() : List.copyOf(events);
    }

    public LocalDate getDate() {
        return date;
    }

    public String getDateValue() {
        return date == null ? "" : date.toString();
    }

    public int getDayNumber() {
        return date == null ? 0 : date.getDayOfMonth();
    }

    public boolean isInCurrentMonth() {
        return inCurrentMonth;
    }

    public boolean isToday() {
        return today;
    }

    public List<CalendarEventDTO> getEvents() {
        return events;
    }

    public List<CalendarEventDTO> getVisibleEvents() {
        return events.stream().limit(3).toList();
    }

    public int getOverflowCount() {
        return Math.max(0, events.size() - 3);
    }
}
