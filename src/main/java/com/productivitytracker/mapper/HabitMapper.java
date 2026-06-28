package com.productivitytracker.mapper;

import com.productivitytracker.dto.HabitDTO;
import com.productivitytracker.model.Habit;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

/**
 * Converts Habit model objects into view DTOs.
 */
public final class HabitMapper {

    private HabitMapper() {
        throw new AssertionError("HabitMapper should not be instantiated");
    }

    public static HabitDTO toDTO(Habit habit) {
        if (habit == null) {
            return null;
        }

        Date lastDoneDate = habit.getLastDoneDate();
        boolean doneToday = lastDoneDate != null && LocalDate.now().equals(lastDoneDate.toLocalDate());
        return new HabitDTO(
                habit.getId(),
                habit.getName(),
                habit.getFrequency(),
                habit.getFrequencyRule() == null ? "" : habit.getFrequencyRule(),
                habit.getCategory() == null ? "General" : habit.getCategory(),
                habit.getStreak(),
                lastDoneDate == null ? "" : lastDoneDate.toString(),
                doneToday
        );
    }

    public static List<HabitDTO> toDTOList(List<Habit> habits) {
        return habits.stream().map(HabitMapper::toDTO).toList();
    }
}
