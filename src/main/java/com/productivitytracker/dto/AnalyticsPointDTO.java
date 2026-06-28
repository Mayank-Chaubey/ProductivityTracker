package com.productivitytracker.dto;

/**
 * A single named metric point used by charts.
 */
public class AnalyticsPointDTO {

    private final String label;
    private final int value;

    public AnalyticsPointDTO(String label, int value) {
        this.label = label;
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public int getValue() {
        return value;
    }
}
