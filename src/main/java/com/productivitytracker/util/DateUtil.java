package com.productivitytracker.util;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Objects;

public final class DateUtil {

    private DateUtil() {
        throw new AssertionError("Utility class should not be instantiated");
    }

    // ---------- TODAY ----------
    public static Date today() {
        return Date.valueOf(LocalDate.now());
    }

    // ---------- CONVERSIONS ----------
    public static Date toSqlDate(LocalDate localDate) {
        Objects.requireNonNull(localDate, "localDate must not be null");
        return Date.valueOf(localDate);
    }

    public static LocalDate toLocalDate(Date sqlDate) {
        Objects.requireNonNull(sqlDate, "sqlDate must not be null");
        return sqlDate.toLocalDate();
    }

    // ---------- COMPARISONS ----------
    public static boolean isSameDay(Date d1, Date d2) {
        if (d1 == null || d2 == null) {
            return false;
        }
        return d1.toLocalDate().isEqual(d2.toLocalDate());
    }

    public static boolean isSameDay(LocalDate d1, LocalDate d2) {
        if (d1 == null || d2 == null) {
            return false;
        }
        return d1.isEqual(d2);
    }
}