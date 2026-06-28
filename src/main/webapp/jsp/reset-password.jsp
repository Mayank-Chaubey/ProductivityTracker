<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Reset Password | Productivity Tracker</title>
    <link rel="manifest" href="${pageContext.request.contextPath}/manifest.json">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/login.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/settings.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/effects.css">
</head>
<body>
<main id="mainContent">
    <div class="auth-container">
        <h2>Choose new password</h2>
        <p class="auth-subtitle">Use at least 8 characters.</p>
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-error show"><c:out value="${errorMessage}" /></div>
        </c:if>
        <form action="${pageContext.request.contextPath}/ResetPasswordServlet" method="post">
            <input type="hidden" name="csrfToken" value="${csrfToken}">
            <input type="hidden" name="token" value="${fn:escapeXml(token)}">
            <div class="form-group">
                <label for="password">New password</label>
                <input type="password" id="password" name="password" required minlength="8">
            </div>
            <div class="form-group">
                <label for="confirmPassword">Confirm password</label>
                <input type="password" id="confirmPassword" name="confirmPassword" required minlength="8">
            </div>
            <button type="submit">Reset password</button>
        </form>
        <a class="auth-page-link" href="${pageContext.request.contextPath}/jsp/login.jsp">Back to login</a>
    </div>
</main>
<script src="${pageContext.request.contextPath}/assets/js/effects.js"></script>
</body>
</html>
