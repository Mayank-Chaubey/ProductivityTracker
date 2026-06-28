package com.productivitytracker.service;

import com.productivitytracker.config.AppConfig;
import com.productivitytracker.util.Logger;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@WebListener
public class ReminderSchedulerListener implements ServletContextListener {

    private ScheduledExecutorService executorService;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        if (!AppConfig.getBoolean("reminders.scheduler.enabled", true)) {
            return;
        }
        int interval = AppConfig.getInt("reminders.scheduler.interval.seconds", 60);
        NotificationService notificationService = new NotificationService();
        executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(notificationService::processDueEmailReminders,
                15, Math.max(15, interval), TimeUnit.SECONDS);
        Logger.logInfo("Reminder scheduler started.");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (executorService != null) {
            executorService.shutdownNow();
        }
    }
}
