package com.productivitytracker.dto;

/**
 * Due-soon reminder shown beside the calendar.
 */
public class CalendarReminderDTO {

    private final String type;
    private final String title;
    private final String meta;
    private final String dueLabel;
    private final String urgency;
    private final String actionPath;

    public CalendarReminderDTO(String type, String title, String meta, String dueLabel,
                               String urgency, String actionPath) {
        this.type = type;
        this.title = title;
        this.meta = meta;
        this.dueLabel = dueLabel;
        this.urgency = urgency;
        this.actionPath = actionPath;
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

    public String getDueLabel() {
        return dueLabel;
    }

    public String getUrgency() {
        return urgency;
    }

    public String getActionPath() {
        return actionPath;
    }
}
