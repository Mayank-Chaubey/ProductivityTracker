package com.productivitytracker.mapper;

import com.productivitytracker.dto.StreakDTO;
import com.productivitytracker.model.Streak;

import java.sql.Date;
import java.util.List;

/**
 * Converts Streak model objects into dashboard DTOs.
 */
public final class StreakMapper {

    private StreakMapper() {
        throw new AssertionError("StreakMapper should not be instantiated");
    }

    public static StreakDTO toDTO(Streak streak) {
        if (streak == null) {
            return null;
        }

        Date lastCompletedDate = streak.getLastCompletedDate();
        return new StreakDTO(
                streak.getHabitId(),
                streak.getHabitName(),
                streak.getWeeklyStreak(),
                streak.getCurrentStreak(),
                streak.getLongestStreak(),
                streak.getFreezeTokens(),
                lastCompletedDate == null ? "" : lastCompletedDate.toString()
        );
    }

    public static List<StreakDTO> toDTOList(List<Streak> streaks) {
        return streaks.stream().map(StreakMapper::toDTO).toList();
    }
}
