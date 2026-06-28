<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Journal | Productivity Tracker</title>
    <link rel="manifest" href="${pageContext.request.contextPath}/manifest.json">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/layout.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/dark-mode.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/effects.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/settings.css">
</head>
<body>
<jsp:include page="partials/sidebar.jsp" />
<jsp:include page="partials/header.jsp">
    <jsp:param name="title" value="Journal" />
    <jsp:param name="subtitle" value="Capture notes, reflections, and mood" />
</jsp:include>

<main class="app-main">
    <section class="page-grid">
        <article class="page-card">
            <h2>New Entry</h2>
            <form class="settings-form" action="${pageContext.request.contextPath}/JournalServlet" method="post">
                <input type="hidden" name="csrfToken" value="${csrfToken}">
                <label>Title <input type="text" name="title" maxlength="160" required></label>
                <label>Date <input type="date" name="entryDate"></label>
                <label>
                    Mood
                    <select name="moodScore">
                        <option value="">Not set</option>
                        <option value="1">1 - Low</option>
                        <option value="2">2</option>
                        <option value="3">3 - Neutral</option>
                        <option value="4">4</option>
                        <option value="5">5 - Great</option>
                    </select>
                </label>
                <label>Notes <textarea name="content" rows="8" required></textarea></label>
                <button class="primary-btn" type="submit">Save entry</button>
            </form>
        </article>

        <article class="page-card">
            <h2>Search</h2>
            <form class="settings-form" action="${pageContext.request.contextPath}/JournalServlet" method="get">
                <label>Keyword <input type="search" name="q" value="${fn:escapeXml(q)}"></label>
                <button class="secondary-btn" type="submit">Search journal</button>
            </form>
        </article>
    </section>

    <section class="page-card" style="margin-top: 18px;">
        <h2>Entries</h2>
        <ul class="muted-list">
            <c:forEach var="entry" items="${entries}">
                <li class="journal-entry">
                    <strong><c:out value="${entry.title}" /></strong>
                    <small><c:out value="${entry.entryDate}" /> · Mood <c:out value="${empty entry.moodScore ? 'Not set' : entry.moodScore}" /></small>
                    <div class="journal-content"><c:out value="${entry.content}" /></div>
                    <form action="${pageContext.request.contextPath}/JournalServlet" method="post">
                        <input type="hidden" name="csrfToken" value="${csrfToken}">
                        <input type="hidden" name="action" value="delete">
                        <input type="hidden" name="entryId" value="${entry.id}">
                        <button class="danger-btn" type="submit" data-confirm="Delete this journal entry?">Delete</button>
                    </form>
                </li>
            </c:forEach>
            <c:if test="${empty entries}">
                <li>No journal entries yet.</li>
            </c:if>
        </ul>
    </section>
</main>
<script src="${pageContext.request.contextPath}/assets/js/theme.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/effects.js"></script>
</body>
</html>
