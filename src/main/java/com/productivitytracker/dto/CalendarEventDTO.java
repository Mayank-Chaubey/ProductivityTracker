package com.productivitytracker.dto;

import java.time.LocalDate;

/**
 * Single item shown on the calendar grid.
 */
public class CalendarEventDTO {

    private final LocalDate date;
    private final String type;
    private final String title;
    private final String meta;
    private final String status;
    private final String cssClass;
    private final int sortOrder;

    public CalendarEventDTO(LocalDate date, String type, String title, String meta,
                            String status, String cssClass, int sortOrder) {
        this.date = date;
        this.type = type;
        this.title = title;
        this.meta = meta;
        this.status = status;
        this.cssClass = cssClass;
        this.sortOrder = sortOrder;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getDateValue() {
        return date == null ? "" : date.toString();
    }

    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getMeta() {
        return meta;
    }

    public String getStatus() {
        return status;
    }

    public String getCssClass() {
        return cssClass;
    }

    public int getSortOrder() {
        return sortOrder;
    }
}
