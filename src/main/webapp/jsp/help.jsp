<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Help | Productivity Tracker</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/layout.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/settings.css">
</head>
<body>
<main class="app-main" style="margin-left:0;">
    <section class="page-card">
        <h1>Help, About, and FAQ</h1>
        <p>Productivity Tracker helps you manage tasks, habits, focus sessions, goals, reminders, and notes from one workspace.</p>
        <div class="faq-list">
            <details open>
                <summary>How do reminders work?</summary>
                <p>In-app reminders appear while the app is open. Email reminders are sent by the background scheduler when SMTP is configured.</p>
            </details>
            <details>
                <summary>How do recurring tasks work?</summary>
                <p>When a recurring task is completed, the app creates the next pending task using the selected repeat rule.</p>
            </details>
            <details>
                <summary>Can I export my data?</summary>
                <p>Use Settings > Export all data to download a JSON export for your account.</p>
            </details>
        </div>
        <p><a class="auth-page-link" href="${pageContext.request.contextPath}/DashboardServlet">Back to app</a></p>
    </section>
</main>
</body>
</html>
