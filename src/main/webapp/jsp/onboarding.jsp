<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Welcome | Productivity Tracker</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/login.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/settings.css">
</head>
<body>
<main id="mainContent">
    <div class="auth-container">
        <h2>Set up your workspace</h2>
        <p class="auth-subtitle">Choose notification defaults. You can change these later.</p>
        <form action="${pageContext.request.contextPath}/OnboardingServlet" method="post">
            <input type="hidden" name="csrfToken" value="${csrfToken}">
            <label class="inline-check"><input type="checkbox" name="emailNotifications" checked> Email reminders</label>
            <label class="inline-check"><input type="checkbox" name="browserNotifications" checked> Browser reminders</label>
            <button type="submit">Continue</button>
        </form>
    </div>
</main>
</body>
</html>
