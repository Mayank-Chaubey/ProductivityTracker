<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="theme-color" content="#2563eb">
    <title>Goals | Productivity Tracker</title>
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
    <jsp:param name="kicker" value="Targets" />
    <jsp:param name="title" value="Goals" />
    <jsp:param name="subtitle" value="Set monthly or quarterly targets and move them forward one focused step at a time." />
</jsp:include>

<main class="app-main workspace-shell">
    <section class="workspace-metrics">
        <article class="workspace-stat"><span>Active</span><strong>${activeGoalsCount}</strong></article>
        <article class="workspace-stat"><span>Completed</span><strong>${completedGoalsCount}</strong></article>
        <article class="workspace-stat"><span>Total</span><strong>${totalGoalsCount}</strong></article>
    </section>

    <section class="workspace-panel">
        <div class="workspace-panel-header">
            <div>
                <h2>Create Goal</h2>
                <p>Choose a realistic target and update progress as you complete work.</p>
            </div>
        </div>
        <form action="${pageContext.request.contextPath}/GoalServlet" method="post" class="workspace-form">
            <input type="hidden" name="csrfToken" value="${csrfToken}">
            <input type="hidden" name="returnTo" value="/GoalServlet">
            <input type="text" name="title" placeholder="Goal title" maxlength="120" required>
            <select name="goalType" required>
                <option value="MONTHLY">Monthly</option>
                <option value="QUARTERLY">Quarterly</option>
            </select>
            <input type="number" name="targetValue" min="1" max="1000" placeholder="Target" required>
            <button type="submit" class="pomodoro-btn">Create</button>
        </form>
    </section>

    <section class="goal-grid">
        <c:choose>
            <c:when test="${empty goals}">
                <article class="workspace-empty">
                    <h2>No goals yet</h2>
                    <p>Create your first target to start tracking long-term progress.</p>
                </article>
            </c:when>
            <c:otherwise>
                <c:forEach var="goal" items="${goals}">
                    <article class="goal-card ${goal.status == 'COMPLETED' ? 'is-complete' : ''}">
                        <div class="goal-card-top">
                            <span>${goal.goalType}</span>
                            <strong>${goal.status}</strong>
                        </div>
                        <h2><c:out value="${goal.title}" /></h2>
                        <p>${goal.currentValue}/${goal.targetValue} completed by ${goal.endDate}</p>
                        <div class="progress-track">
                            <div class="progress-fill" style="--progress: ${goal.progressPercentage}%"></div>
                        </div>
                        <div class="goal-actions">
                            <strong>${goal.progressPercentage}%</strong>
                            <form action="${pageContext.request.contextPath}/GoalServlet" method="post">
                                <input type="hidden" name="csrfToken" value="${csrfToken}">
                                <input type="hidden" name="returnTo" value="/GoalServlet">
                                <input type="hidden" name="action" value="increment">
                                <input type="hidden" name="goalId" value="${goal.id}">
                                <input type="number" name="increment" value="1" min="1" max="100" aria-label="Progress increment">
                                <button type="submit" class="pomodoro-btn" ${goal.status == 'COMPLETED' ? 'disabled' : ''}>Update</button>
                            </form>
                        </div>
                    </article>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </section>
</main>

<script src="${pageContext.request.contextPath}/assets/js/theme.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/accessibility.js"></script>
</body>
</html>
