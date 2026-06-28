<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<c:set var="currentUri" value="${pageContext.request.requestURI}" />

<aside class="app-sidebar" aria-label="Primary navigation">
    <a class="sidebar-brand" href="${pageContext.request.contextPath}/DashboardServlet">ProductivityTracker</a>
    <nav class="sidebar-nav">
        <a class="${fn:contains(currentUri, 'DashboardServlet') or fn:contains(currentUri, 'dashboard.jsp') ? 'is-active' : ''}" href="${pageContext.request.contextPath}/DashboardServlet">Dashboard</a>
        <a class="${fn:contains(currentUri, 'TaskServlet') or fn:contains(currentUri, 'tasks.jsp') ? 'is-active' : ''}" href="${pageContext.request.contextPath}/TaskServlet">Tasks</a>
        <a class="${fn:contains(currentUri, 'HabitServlet') or fn:contains(currentUri, 'habits.jsp') ? 'is-active' : ''}" href="${pageContext.request.contextPath}/HabitServlet">Habits</a>
        <a class="${fn:contains(currentUri, 'ActivityServlet') or fn:contains(currentUri, 'activities.jsp') ? 'is-active' : ''}" href="${pageContext.request.contextPath}/ActivityServlet">Activities</a>
        <a class="${fn:contains(currentUri, 'CalendarServlet') or fn:contains(currentUri, 'calendar.jsp') ? 'is-active' : ''}" href="${pageContext.request.contextPath}/CalendarServlet">Calendar</a>
        <a class="${fn:contains(currentUri, 'ReminderServlet') or fn:contains(currentUri, 'reminders.jsp') ? 'is-active' : ''}" href="${pageContext.request.contextPath}/ReminderServlet">Reminders</a>
        <a class="${fn:contains(currentUri, 'GoalServlet') or fn:contains(currentUri, 'goals.jsp') ? 'is-active' : ''}" href="${pageContext.request.contextPath}/GoalServlet">Goals</a>
        <a class="${fn:contains(currentUri, 'PomodoroServlet') or fn:contains(currentUri, 'focus.jsp') ? 'is-active' : ''}" href="${pageContext.request.contextPath}/PomodoroServlet">Focus</a>
        <a class="${fn:contains(currentUri, 'JournalServlet') or fn:contains(currentUri, 'journal.jsp') ? 'is-active' : ''}" href="${pageContext.request.contextPath}/JournalServlet">Journal</a>
        <a class="${fn:contains(currentUri, 'ReportServlet') or fn:contains(currentUri, 'reports.jsp') ? 'is-active' : ''}" href="${pageContext.request.contextPath}/ReportServlet">Reports</a>
        <a class="${fn:contains(currentUri, 'AchievementServlet') or fn:contains(currentUri, 'achievements.jsp') ? 'is-active' : ''}" href="${pageContext.request.contextPath}/AchievementServlet">Badges</a>
        <a class="${fn:contains(currentUri, 'ProfileServlet') or fn:contains(currentUri, 'profile.jsp') ? 'is-active' : ''}" href="${pageContext.request.contextPath}/ProfileServlet">Profile</a>
        <a class="${fn:contains(currentUri, 'SettingsServlet') or fn:contains(currentUri, 'settings.jsp') ? 'is-active' : ''}" href="${pageContext.request.contextPath}/SettingsServlet">Settings</a>
        <a class="${fn:contains(currentUri, 'help.jsp') ? 'is-active' : ''}" href="${pageContext.request.contextPath}/jsp/help.jsp">Help</a>
    </nav>
    <a class="sidebar-logout" href="${pageContext.request.contextPath}/LogoutServlet">Logout</a>
</aside>

<nav class="mobile-bottom-nav" aria-label="Mobile navigation">
    <a class="${fn:contains(currentUri, 'DashboardServlet') or fn:contains(currentUri, 'dashboard.jsp') ? 'is-active' : ''}" href="${pageContext.request.contextPath}/DashboardServlet">Home</a>
    <a class="${fn:contains(currentUri, 'TaskServlet') or fn:contains(currentUri, 'tasks.jsp') ? 'is-active' : ''}" href="${pageContext.request.contextPath}/TaskServlet">Tasks</a>
    <a class="${fn:contains(currentUri, 'HabitServlet') or fn:contains(currentUri, 'habits.jsp') ? 'is-active' : ''}" href="${pageContext.request.contextPath}/HabitServlet">Habits</a>
    <a class="${fn:contains(currentUri, 'ReminderServlet') or fn:contains(currentUri, 'reminders.jsp') ? 'is-active' : ''}" href="${pageContext.request.contextPath}/ReminderServlet">Alerts</a>
    <a class="${fn:contains(currentUri, 'SettingsServlet') or fn:contains(currentUri, 'settings.jsp') ? 'is-active' : ''}" href="${pageContext.request.contextPath}/SettingsServlet">More</a>
</nav>
