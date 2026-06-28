<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<c:set var="headerKicker" value="${empty param.kicker ? 'Productivity Tracker' : param.kicker}" />
<c:set var="avatarLetter" value="${empty sessionScope.userEmail ? 'U' : fn:toUpperCase(fn:substring(sessionScope.userEmail, 0, 1))}" />

<header class="app-header">
    <div>
        <p class="app-kicker"><c:out value="${headerKicker}" /></p>
        <h1><c:out value="${param.title}" /></h1>
        <p class="app-breadcrumb"><a href="${pageContext.request.contextPath}/DashboardServlet">Dashboard</a> / <span><c:out value="${param.title}" /></span></p>
        <p><c:out value="${param.subtitle}" /></p>
    </div>
    <div class="header-actions">
        <button type="button" id="command-open" class="icon-button" aria-label="Open command palette" title="Search">⌘</button>
        <button id="theme-toggle" class="theme-toggle" aria-label="Toggle theme" title="Toggle theme">🌓</button>
        <details class="user-menu">
            <summary aria-label="User menu">
                <span class="avatar"><c:out value="${avatarLetter}" /></span>
            </summary>
            <div class="user-menu-panel">
                <strong><c:out value="${sessionScope.userEmail}" /></strong>
                <a href="${pageContext.request.contextPath}/ProfileServlet">Profile</a>
                <a href="${pageContext.request.contextPath}/SettingsServlet">Settings</a>
                <a href="${pageContext.request.contextPath}/LogoutServlet">Logout</a>
            </div>
        </details>
    </div>
</header>
<script>
    document.body.setAttribute('data-context-path', '${pageContext.request.contextPath}');
</script>
