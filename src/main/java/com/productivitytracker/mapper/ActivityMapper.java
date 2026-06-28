package com.productivitytracker.mapper;

import com.productivitytracker.dto.ActivityDTO;
import com.productivitytracker.model.Activity;

import java.sql.Date;
import java.util.List;

/**
 * Converts Activity model objects into view DTOs.
 */
public final class ActivityMapper {

    private ActivityMapper() {
        throw new AssertionError("ActivityMapper should not be instantiated");
    }

    public static ActivityDTO toDTO(Activity activity) {
        if (activity == null) {
            return null;
        }

        Date activityDate = activity.getActivityDate();
        return new ActivityDTO(
                activity.getId(),
                activity.getName(),
                activity.getCategory() == null || activity.getCategory().isBlank() ? "other" : activity.getCategory(),
                activity.getDuration(),
                activity.getNotes() == null ? "" : activity.getNotes(),
                activityDate == null ? "" : activityDate.toString()
        );
    }

    public static List<ActivityDTO> toDTOList(List<Activity> activities) {
        return activities.stream().map(ActivityMapper::toDTO).toList();
    }
}
