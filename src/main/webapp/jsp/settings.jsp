<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="theme-color" content="#2563eb">
    <title>Settings | Productivity Tracker</title>
    <link rel="manifest" href="${pageContext.request.contextPath}/manifest.json">
    <link rel="icon" type="image/svg+xml" href="${pageContext.request.contextPath}/assets/images/icon-192.svg">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/layout.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/dark-mode.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/effects.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/settings.css">
</head>
<body>
<jsp:include page="partials/sidebar.jsp" />
<jsp:include page="partials/header.jsp">
    <jsp:param name="title" value="Settings" />
    <jsp:param name="subtitle" value="Account, notifications, security, and data controls" />
</jsp:include>

<main class="app-main">
    <section class="settings-grid">
        <div class="settings-card">
            <h2>Account</h2>
            <p><strong><c:out value="${user.name}" /></strong><br><c:out value="${user.email}" /></p>
            <p>
                Email status:
                <span class="status-pill ${user.emailVerified ? 'good' : 'warn'}">
                    ${user.emailVerified ? 'Verified' : 'Not verified'}
                </span>
            </p>
            <c:if test="${not user.emailVerified}">
                <form class="settings-form" action="${pageContext.request.contextPath}/SettingsServlet" method="post">
                    <input type="hidden" name="csrfToken" value="${csrfToken}">
                    <input type="hidden" name="action" value="sendVerification">
                    <button class="secondary-btn" type="submit">Send verification email</button>
                </form>
            </c:if>
        </div>

        <div class="settings-card">
            <h2>Notifications</h2>
            <form class="settings-form" action="${pageContext.request.contextPath}/SettingsServlet" method="post">
                <input type="hidden" name="csrfToken" value="${csrfToken}">
                <input type="hidden" name="action" value="preferences">
                <label class="inline-check">
                    <input type="checkbox" name="emailNotifications" ${settings.emailNotifications ? 'checked' : ''}>
                    Email notifications
                </label>
                <label class="inline-check">
                    <input type="checkbox" name="browserNotifications" ${settings.browserNotifications ? 'checked' : ''}>
                    Browser notifications
                </label>
                <label>
                    Reminder lead time
                    <input type="number" name="reminderLeadMinutes" min="0" max="1440" value="${settings.reminderLeadMinutes}">
                </label>
                <label>
                    Timezone
                    <input type="text" name="timezone" value="${fn:escapeXml(settings.timezone)}">
                </label>
                <button class="primary-btn" type="submit">Save preferences</button>
            </form>
        </div>

        <div class="settings-card">
            <h2>Change Password</h2>
            <form class="settings-form" action="${pageContext.request.contextPath}/SettingsServlet" method="post">
                <input type="hidden" name="csrfToken" value="${csrfToken}">
                <input type="hidden" name="action" value="password">
                <label>Current password <input type="password" name="currentPassword" required></label>
                <label>New password <input type="password" name="newPassword" minlength="8" required></label>
                <button class="primary-btn" type="submit">Change password</button>
            </form>
        </div>

        <div class="settings-card">
            <h2>Security</h2>
            <form class="settings-form" action="${pageContext.request.contextPath}/SettingsServlet" method="post">
                <input type="hidden" name="csrfToken" value="${csrfToken}">
                <input type="hidden" name="action" value="logoutAll">
                <button class="secondary-btn" type="submit">Logout from all devices</button>
            </form>
            <ul class="muted-list" style="margin-top: 16px;">
                <c:forEach var="event" items="${securityEvents}">
                    <li>
                        <strong><c:out value="${event.eventType}" /></strong><br>
                        <small><c:out value="${event.createdAt}" /> · <c:out value="${event.ipAddress}" /></small>
                    </li>
                </c:forEach>
                <c:if test="${empty securityEvents}">
                    <li>No security events yet.</li>
                </c:if>
            </ul>
        </div>

        <div class="settings-card">
            <h2>Data</h2>
            <div class="settings-form">
                <a class="secondary-btn" href="${pageContext.request.contextPath}/DataExportServlet">Export all data</a>
            </div>
        </div>

        <div class="settings-card">
            <h2>Delete Account</h2>
            <form class="settings-form" action="${pageContext.request.contextPath}/SettingsServlet" method="post">
                <input type="hidden" name="csrfToken" value="${csrfToken}">
                <input type="hidden" name="action" value="deleteAccount">
                <label>Type DELETE <input type="text" name="deleteConfirm" required></label>
                <label>Current password <input type="password" name="deletePassword" required></label>
                <button class="danger-btn" type="submit" data-confirm="This permanently deletes your account and data. Continue?">Delete account</button>
            </form>
        </div>
    </section>
</main>
<script src="${pageContext.request.contextPath}/assets/js/theme.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/effects.js"></script>
</body>
</html>
