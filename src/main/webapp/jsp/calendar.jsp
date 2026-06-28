<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="theme-color" content="#2563eb">
    <title>Calendar | Productivity Tracker</title>
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
    <jsp:param name="kicker" value="Timeline" />
    <jsp:param name="title" value="Calendar" />
    <jsp:param name="subtitle" value="Review deadlines, goal targets, focus history, and reminders from one view." />
</jsp:include>

<main class="app-main workspace-shell">
    <section class="calendar-toolbar workspace-panel">
        <div>
            <h2><c:out value="${calendar.monthLabel}" /></h2>
            <p>Tasks use due dates, goals use target dates, and focus sessions are grouped by day.</p>
        </div>
        <div class="calendar-month-actions" aria-label="Calendar month navigation">
            <a class="pomodoro-btn" href="${pageContext.request.contextPath}/CalendarServlet?month=${calendar.previousMonthValue}">Previous</a>
            <a class="pomodoro-btn" href="${pageContext.request.contextPath}/CalendarServlet">Today</a>
            <a class="pomodoro-btn" href="${pageContext.request.contextPath}/CalendarServlet?month=${calendar.nextMonthValue}">Next</a>
        </div>
    </section>

    <section class="workspace-metrics">
        <article class="workspace-stat"><span>Month Events</span><strong>${calendar.totalEvents}</strong></article>
        <article class="workspace-stat"><span>Due Today</span><strong>${calendar.dueTodayCount}</strong></article>
        <article class="workspace-stat"><span>Overdue</span><strong>${calendar.overdueCount}</strong></article>
        <article class="workspace-stat"><span>Focus Minutes</span><strong>${calendar.focusMinutes}m</strong></article>
    </section>

    <section class="calendar-layout">
        <div class="workspace-panel calendar-board">
            <div class="calendar-scroll" role="region" aria-label="Monthly calendar" tabindex="0">
                <div class="calendar-weekdays" aria-hidden="true">
                    <span>Sun</span>
                    <span>Mon</span>
                    <span>Tue</span>
                    <span>Wed</span>
                    <span>Thu</span>
                    <span>Fri</span>
                    <span>Sat</span>
                </div>
                <div class="calendar-grid">
                    <c:forEach var="day" items="${calendar.gridDays}">
                        <article class="calendar-day ${day.inCurrentMonth ? '' : 'is-muted'} ${day.today ? 'is-today' : ''}" data-date="${day.dateValue}">
                            <div class="calendar-day-number">${day.dayNumber}</div>
                            <c:forEach var="event" items="${day.visibleEvents}">
                                <div class="calendar-event ${event.cssClass}">
                                    <span><c:out value="${event.type}" /></span>
                                    <strong><c:out value="${event.title}" /></strong>
                                    <small><c:out value="${event.meta}" /></small>
                                </div>
                            </c:forEach>
                            <c:if test="${day.overflowCount > 0}">
                                <div class="calendar-overflow">+${day.overflowCount} more</div>
                            </c:if>
                        </article>
                    </c:forEach>
                </div>
            </div>
        </div>

        <aside class="workspace-panel reminder-panel" aria-label="Due reminders">
            <div class="workspace-panel-header">
                <div>
                    <h2>Reminders</h2>
                    <p>${calendar.upcomingCount} upcoming this week</p>
                </div>
                <button type="button" class="pomodoro-btn" id="enableReminderNotifications">Notify</button>
            </div>

            <div class="reminder-list">
                <c:choose>
                    <c:when test="${empty calendar.reminders}">
                        <article class="workspace-empty compact-empty">
                            <h2>Nothing due soon</h2>
                            <p>Add due dates to tasks or goal targets to see reminders here.</p>
                        </article>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="reminder" items="${calendar.reminders}">
                            <article class="reminder-card is-${reminder.urgency}">
                                <div>
                                    <span><c:out value="${reminder.type}" /></span>
                                    <strong class="reminder-title"><c:out value="${reminder.title}" /></strong>
                                    <small><c:out value="${reminder.meta}" /></small>
                                </div>
                                <div class="reminder-card-actions">
                                    <em class="reminder-due"><c:out value="${reminder.dueLabel}" /></em>
                                    <a href="${pageContext.request.contextPath}${reminder.actionPath}">Open</a>
                                </div>
                            </article>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </div>
        </aside>
    </section>
</main>

<script>
    (function () {
        const button = document.getElementById('enableReminderNotifications');
        const reminderCards = Array.from(document.querySelectorAll('.reminder-card'));
        const dueCards = reminderCards.filter(card => card.classList.contains('is-overdue') || card.classList.contains('is-today'));
        const iconUrl = '${pageContext.request.contextPath}/assets/images/icon-192.svg';

        if (!button) {
            return;
        }

        if (!('Notification' in window)) {
            button.disabled = true;
            button.textContent = 'Unavailable';
            return;
        }

        if (!dueCards.length) {
            button.disabled = true;
        }

        async function showNotification(title, body, tag) {
            if ('serviceWorker' in navigator) {
                try {
                    const registration = await Promise.race([
                        navigator.serviceWorker.ready,
                        new Promise(resolve => window.setTimeout(() => resolve(null), 700))
                    ]);
                    if (registration && registration.showNotification) {
                        registration.showNotification(title, { body: body, icon: iconUrl, tag: tag });
                        return;
                    }
                } catch (ignored) {}
            }
            new Notification(title, { body: body, icon: iconUrl, tag: tag });
        }

        async function notifyDueCards() {
            let permission = Notification.permission;
            if (permission === 'default') {
                permission = await Notification.requestPermission();
            }
            if (permission !== 'granted') {
                button.textContent = 'Blocked';
                return;
            }

            const todayKey = new Date().toISOString().slice(0, 10);
            const storageKey = 'pt-calendar-reminders-' + todayKey;
            let sentValues = [];
            try {
                sentValues = JSON.parse(window.localStorage.getItem(storageKey) || '[]');
            } catch (ignored) {}
            const sent = new Set(sentValues);

            dueCards.forEach(function (card, index) {
                const title = card.querySelector('.reminder-title')?.textContent.trim() || 'Productivity reminder';
                const due = card.querySelector('.reminder-due')?.textContent.trim() || 'Due today';
                const meta = card.querySelector('small')?.textContent.trim() || '';
                const key = title + '|' + due;
                if (sent.has(key)) {
                    return;
                }
                showNotification(title, [due, meta].filter(Boolean).join(' - '), 'pt-reminder-' + index);
                sent.add(key);
            });

            window.localStorage.setItem(storageKey, JSON.stringify(Array.from(sent)));
            button.textContent = 'Enabled';
        }

        button.addEventListener('click', notifyDueCards);
    })();
</script>
<script src="${pageContext.request.contextPath}/assets/js/register-sw.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/theme.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/accessibility.js"></script>
</body>
</html>
