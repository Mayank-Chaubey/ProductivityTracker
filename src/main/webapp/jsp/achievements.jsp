<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="theme-color" content="#2563eb">
    <title>Badges | Productivity Tracker</title>
    <link rel="manifest" href="${pageContext.request.contextPath}/manifest.json">
    <link rel="icon" type="image/svg+xml" href="${pageContext.request.contextPath}/assets/images/icon-192.svg">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/layout.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/dark-mode.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/animations.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/effects.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/apple.css">
</head>
<body>

<jsp:include page="partials/sidebar.jsp" />
<jsp:include page="partials/header.jsp">
    <jsp:param name="kicker" value="Milestones" />
    <jsp:param name="title" value="Achievements" />
    <jsp:param name="subtitle" value="Unlock badges as you complete tasks, build streaks, focus deeply, and finish goals." />
</jsp:include>

<main class="app-main workspace-shell">
    <section class="workspace-metrics">
        <article class="workspace-stat"><span>Unlocked</span><strong>${unlockedCount}</strong></article>
        <article class="workspace-stat"><span>Locked</span><strong>${lockedCount}</strong></article>
        <article class="workspace-stat"><span>Points</span><strong>${totalPoints}</strong></article>
    </section>

    <section class="achievement-grid">
        <c:forEach var="achievement" items="${achievements}">
            <article class="achievement-card ${achievement.unlocked ? 'is-unlocked' : 'is-locked'}">
                <div class="achievement-icon">${achievement.icon}</div>
                <div>
                    <span>${achievement.points} pts</span>
                    <h2><c:out value="${achievement.title}" /></h2>
                    <p><c:out value="${achievement.description}" /></p>
                    <c:choose>
                        <c:when test="${achievement.unlocked}">
                            <strong>Unlocked ${achievement.unlockedOn}</strong>
                        </c:when>
                        <c:otherwise>
                            <strong>Locked</strong>
                        </c:otherwise>
                    </c:choose>
                </div>
            </article>
        </c:forEach>
        <c:if test="${empty achievements}">
            <article class="workspace-empty">
                <h2>No badges available</h2>
                <p>Start completing tasks, habits, focus sessions, and goals to unlock achievements.</p>
            </article>
        </c:if>
    </section>
</main>

<script src="${pageContext.request.contextPath}/assets/js/theme.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/accessibility.js"></script>
</body>
</html>
