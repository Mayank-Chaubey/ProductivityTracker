package com.productivitytracker.mapper;

import com.productivitytracker.dto.ReportDTO;
import com.productivitytracker.model.Report;

import java.util.List;

/**
 * Converts legacy Report model objects into report DTOs.
 */
public final class ReportMapper {

    private ReportMapper() {
        throw new AssertionError("ReportMapper should not be instantiated");
    }

    public static ReportDTO toDTO(Report report) {
        if (report == null) {
            return new ReportDTO(0, 0, 0, 0, 0, 0, 0, 0, List.of());
        }

        return new ReportDTO(
                report.getTotalTasks(),
                report.getCompletedTasks(),
                report.getTotalHabits(),
                0,
                report.getTotalActivities(),
                report.getTotalTimeMinutes(),
                report.getProductivityPercentage(),
                0,
                List.of()
        );
    }
}
