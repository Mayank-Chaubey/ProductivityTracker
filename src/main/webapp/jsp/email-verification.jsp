<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Email Verification | Productivity Tracker</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/login.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/settings.css">
</head>
<body>
<main id="mainContent">
    <div class="auth-container">
        <c:choose>
            <c:when test="${verified}">
                <h2>Email verified</h2>
                <p class="auth-subtitle">Your email is verified. You can continue using the app.</p>
            </c:when>
            <c:otherwise>
                <h2>Verification failed</h2>
                <p class="auth-subtitle">The verification link is invalid or expired.</p>
            </c:otherwise>
        </c:choose>
        <a class="auth-page-link" href="${pageContext.request.contextPath}/jsp/login.jsp">Go to login</a>
    </div>
</main>
</body>
</html>
