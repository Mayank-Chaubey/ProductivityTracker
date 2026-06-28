package com.productivitytracker.dto;

import java.time.LocalDate;

/**
 * One day cell for calendar heatmap.
 */
public class HeatmapCellDTO {

    private final LocalDate date;
    private final int intensity;

    public HeatmapCellDTO(LocalDate date, int intensity) {
        this.date = date;
        this.intensity = intensity;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getIntensity() {
        return intensity;
    }
}
