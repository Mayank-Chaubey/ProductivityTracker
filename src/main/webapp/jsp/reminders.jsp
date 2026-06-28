<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="theme-color" content="#2563eb">
    <meta name="csrf-token" content="${csrfToken}">
    <title>Reminders | Productivity Tracker</title>
    <link rel="manifest" href="${pageContext.request.contextPath}/manifest.json">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/layout.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/dark-mode.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/effects.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/settings.css">
</head>
<body>
<jsp:include page="partials/sidebar.jsp" />
<jsp:include page="partials/header.jsp">
    <jsp:param name="title" value="Reminders" />
    <jsp:param name="subtitle" value="Schedule in-app and email nudges" />
</jsp:include>

<main class="app-main">
    <section class="page-grid">
        <article class="page-card">
            <h2>Create Reminder</h2>
            <form class="settings-form" action="${pageContext.request.contextPath}/ReminderServlet" method="post">
                <input type="hidden" name="csrfToken" value="${csrfToken}">
                <label>Title <input type="text" name="title" maxlength="160" required></label>
                <label>
                    Type
                    <select name="reminderType">
                        <option value="GENERAL">General</option>
                        <option value="TASK">Task</option>
                        <option value="HABIT">Habit</option>
                        <option value="GOAL">Goal</option>
                    </select>
                </label>
                <label>When <input type="datetime-local" name="remindAt" required></label>
                <label>
                    Channel
                    <select name="channel">
                        <option value="IN_APP">Browser/In-app</option>
                        <option value="EMAIL">Email</option>
                        <option value="BOTH">Both</option>
                    </select>
                </label>
                <button class="primary-btn" type="submit">Add reminder</button>
            </form>
        </article>

        <article class="page-card">
            <h2>Notification Permission</h2>
            <p>Browser reminders work while the app is open. Email reminders are processed by the background scheduler.</p>
            <button id="enableNotifications" class="secondary-btn" type="button">Enable browser notifications</button>
        </article>
    </section>

    <section class="page-card" style="margin-top: 18px;">
        <h2>Scheduled Reminders</h2>
        <ul class="muted-list">
            <c:forEach var="reminder" items="${reminders}">
                <li>
                    <strong><c:out value="${reminder.title}" /></strong>
                    <span class="status-pill"><c:out value="${reminder.status}" /></span><br>
                    <small><c:out value="${reminder.reminderType}" /> · <c:out value="${reminder.channel}" /> · <c:out value="${reminder.remindAt}" /></small>
                    <form action="${pageContext.request.contextPath}/ReminderServlet" method="post" style="margin-top: 8px;">
                        <input type="hidden" name="csrfToken" value="${csrfToken}">
                        <input type="hidden" name="action" value="delete">
                        <input type="hidden" name="reminderId" value="${reminder.id}">
                        <button class="danger-btn" type="submit" data-confirm="Delete this reminder?">Delete</button>
                    </form>
                </li>
            </c:forEach>
            <c:if test="${empty reminders}">
                <li>No reminders yet.</li>
            </c:if>
        </ul>
    </section>
</main>

<script>
(function () {
    const button = document.getElementById('enableNotifications');
    const csrfToken = document.querySelector('meta[name="csrf-token"]')?.content || '';

    button?.addEventListener('click', function () {
        if (!('Notification' in window)) {
            window.PTToast && window.PTToast('Browser notifications are not supported.', 'error');
            return;
        }
        Notification.requestPermission().then(function (permission) {
            window.PTToast && window.PTToast(permission === 'granted' ? 'Browser notifications enabled.' : 'Notification permission was not granted.', permission === 'granted' ? 'success' : 'error');
        });
    });

    function dismiss(id) {
        const body = new URLSearchParams();
        body.set('csrfToken', csrfToken);
        body.set('action', 'dismiss');
        body.set('reminderId', id);
        fetch('${pageContext.request.contextPath}/ReminderServlet', {
            method: 'POST',
            headers: {'Content-Type': 'application/x-www-form-urlencoded'},
            body: body.toString()
        }).catch(function () {});
    }

    function pollDueReminders() {
        fetch('${pageContext.request.contextPath}/ReminderServlet?action=due')
            .then(function (res) { return res.json(); })
            .then(function (payload) {
                (payload.data || []).forEach(function (reminder) {
                    if ('Notification' in window && Notification.permission === 'granted') {
                        new Notification('Productivity Tracker', { body: reminder.title });
                    }
                    window.PTToast && window.PTToast(reminder.title, 'info');
                    dismiss(reminder.id);
                });
            })
            .catch(function () {});
    }

    pollDueReminders();
    setInterval(pollDueReminders, 60000);
})();
</script>
<script src="${pageContext.request.contextPath}/assets/js/theme.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/effects.js"></script>
</body>
</html>
