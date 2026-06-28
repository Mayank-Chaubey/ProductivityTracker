package com.productivitytracker.mapper;

import com.productivitytracker.dto.TimeLogDTO;
import com.productivitytracker.model.TimeLog;

import java.sql.Date;
import java.util.List;

/**
 * Converts TimeLog model objects into report DTOs.
 */
public final class TimeLogMapper {

    private TimeLogMapper() {
        throw new AssertionError("TimeLogMapper should not be instantiated");
    }

    public static TimeLogDTO toDTO(TimeLog timeLog) {
        if (timeLog == null) {
            return null;
        }

        Date logDate = timeLog.getLogDate();
        return new TimeLogDTO(
                timeLog.getId(),
                timeLog.getReferenceType(),
                timeLog.getReferenceId(),
                timeLog.getDurationMinutes(),
                logDate == null ? "" : logDate.toString()
        );
    }

    public static List<TimeLogDTO> toDTOList(List<TimeLog> timeLogs) {
        return timeLogs.stream().map(TimeLogMapper::toDTO).toList();
    }
}
