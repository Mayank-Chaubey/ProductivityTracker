<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Forgot Password | Productivity Tracker</title>
    <link rel="manifest" href="${pageContext.request.contextPath}/manifest.json">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/login.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/settings.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/effects.css">
</head>
<body>
<main id="mainContent">
    <div class="auth-container">
        <h2>Reset password</h2>
        <p class="auth-subtitle">Enter your email and we will send a reset link.</p>

        <c:if test="${not empty successMessage}">
            <div class="alert alert-success show"><c:out value="${successMessage}" /></div>
        </c:if>
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-error show"><c:out value="${errorMessage}" /></div>
        </c:if>
        <c:if test="${not empty resetLink}">
            <div class="alert alert-success show">
                Development reset link:
                <a href="${resetLink}"><c:out value="${resetLink}" /></a>
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/ForgotPasswordServlet" method="post">
            <input type="hidden" name="csrfToken" value="${csrfToken}">
            <div class="form-group">
                <label for="email">Email address</label>
                <input type="email" id="email" name="email" required placeholder="you@example.com">
            </div>
            <button type="submit">Send reset link</button>
        </form>
        <a class="auth-page-link" href="${pageContext.request.contextPath}/jsp/login.jsp">Back to login</a>
    </div>
</main>
<script src="${pageContext.request.contextPath}/assets/js/effects.js"></script>
</body>
</html>
