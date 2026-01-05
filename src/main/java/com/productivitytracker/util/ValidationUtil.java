package com.productivitytracker.util;

import java.util.Objects;
import java.util.regex.Pattern;

public final class ValidationUtil {

    // ---------- CONSTANTS ----------
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    private ValidationUtil() {
        throw new AssertionError("ValidationUtil should not be instantiated");
    }

    // ---------- STRING ----------
    public static boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    // ---------- NUMBER ----------
    public static boolean isPositive(int value) {
        return value > 0;
    }

    // ---------- EMAIL ----------
    public static boolean isEmailValid(String email) {
        if (email == null) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }

    // ---------- PASSWORD ----------
    public static boolean passwordsMatch(String p1, String p2) {
        return Objects.equals(p1, p2);
    }
}