package com.productivitytracker.util;

import java.time.LocalDateTime;

public class Logger {
    public static void logError(String message, Throwable t) {
        System.err.println("[" + LocalDateTime.now() + "] ERROR: " + message);
        if (t != null) {
            t.printStackTrace(System.err);
        }
    }
    public static void logInfo(String message) {
        System.out.println("[" + LocalDateTime.now() + "] INFO: " + message);
    }
}
