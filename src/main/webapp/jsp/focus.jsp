<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="theme-color" content="#2563eb">
    <meta name="csrf-token" content="${csrfToken}">
    <title>Focus | Productivity Tracker</title>
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
    <jsp:param name="kicker" value="Deep Work" />
    <jsp:param name="title" value="Focus Timer" />
    <jsp:param name="subtitle" value="Run structured work sessions, log focus minutes, and review recent momentum." />
</jsp:include>

<main class="app-main workspace-shell">
    <section class="workspace-metrics">
        <article class="workspace-stat"><span>Sessions Today</span><strong id="focusSessions">${summary.sessionsToday}</strong></article>
        <article class="workspace-stat"><span>Focus Today</span><strong id="focusMinutes">${summary.focusMinutesToday}m</strong></article>
        <article class="workspace-stat"><span>Lifetime Sessions</span><strong>${summary.totalSessionsLifetime}</strong></article>
    </section>

    <section class="focus-stage workspace-panel">
        <div class="focus-label" id="focusLabel">Work</div>
        <div class="focus-timer" id="focusTimer">25:00</div>
        <div class="focus-presets">
            <button type="button" class="pomodoro-btn" data-work="25" data-break="5">25 / 5</button>
            <button type="button" class="pomodoro-btn" data-work="50" data-break="10">50 / 10</button>
            <button type="button" class="pomodoro-btn" data-work="90" data-break="15">90 / 15</button>
        </div>
        <div class="pomodoro-controls">
            <button type="button" id="focusStart" class="pomodoro-btn">Start</button>
            <button type="button" id="focusPause" class="pomodoro-btn">Pause</button>
            <button type="button" id="focusReset" class="pomodoro-btn">Reset</button>
        </div>
    </section>

    <section class="workspace-panel">
        <div class="workspace-panel-header">
            <div>
                <h2>Recent Sessions</h2>
                <p>Your last completed focus blocks.</p>
            </div>
        </div>
        <div class="workspace-table-wrap">
            <table class="reports-table">
                <thead>
                    <tr>
                        <th>Completed</th>
                        <th>Work</th>
                        <th>Break</th>
                        <th>Cycles</th>
                        <th>Focus</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="session" items="${summary.recentSessions}">
                        <tr>
                            <td>${session.completedAt}</td>
                            <td>${session.workMinutes}m</td>
                            <td>${session.breakMinutes}m</td>
                            <td>${session.cycles}</td>
                            <td>${session.totalFocusMinutes}m</td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty summary.recentSessions}">
                        <tr><td colspan="5">No completed focus sessions yet.</td></tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </section>
</main>

<script>
    const csrfToken = document.querySelector('meta[name="csrf-token"]')?.getAttribute('content') || '';
    let workMinutes = 25;
    let breakMinutes = 5;
    let remaining = workMinutes * 60;
    let running = false;
    let onBreak = false;
    let timerRef = null;

    const timerEl = document.getElementById('focusTimer');
    const labelEl = document.getElementById('focusLabel');
    const sessionsEl = document.getElementById('focusSessions');
    const minutesEl = document.getElementById('focusMinutes');

    function renderTimer() {
        const mm = String(Math.floor(remaining / 60)).padStart(2, '0');
        const ss = String(remaining % 60).padStart(2, '0');
        timerEl.textContent = mm + ':' + ss;
        labelEl.textContent = onBreak ? 'Break' : 'Work';
        timerEl.classList.toggle('timer-active', running);
    }

    function logSession() {
        fetch('${pageContext.request.contextPath}/PomodoroServlet', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
                'X-CSRF-Token': csrfToken
            },
            body: 'workMinutes=' + encodeURIComponent(workMinutes) +
                '&breakMinutes=' + encodeURIComponent(breakMinutes) +
                '&cycles=1&csrfToken=' + encodeURIComponent(csrfToken)
        }).then(function () {
            const sessions = parseInt(sessionsEl.textContent, 10) || 0;
            const minutes = parseInt((minutesEl.textContent || '0').replace('m', ''), 10) || 0;
            sessionsEl.textContent = sessions + 1;
            minutesEl.textContent = (minutes + workMinutes) + 'm';
        }).catch(function () {});
    }

    function tick() {
        if (!running) {
            return;
        }
        remaining -= 1;
        if (remaining <= 0) {
            if (!onBreak) {
                logSession();
            }
            onBreak = !onBreak;
            remaining = (onBreak ? breakMinutes : workMinutes) * 60;
        }
        renderTimer();
    }

    function start() {
        if (running) {
            return;
        }
        running = true;
        timerRef = window.setInterval(tick, 1000);
        renderTimer();
    }

    function pause() {
        running = false;
        if (timerRef) {
            window.clearInterval(timerRef);
            timerRef = null;
        }
        renderTimer();
    }

    function reset() {
        pause();
        onBreak = false;
        remaining = workMinutes * 60;
        renderTimer();
    }

    document.querySelectorAll('.focus-presets button').forEach(function (button) {
        button.addEventListener('click', function () {
            workMinutes = parseInt(button.dataset.work, 10);
            breakMinutes = parseInt(button.dataset.break, 10);
            reset();
        });
    });

    document.getElementById('focusStart').addEventListener('click', start);
    document.getElementById('focusPause').addEventListener('click', pause);
    document.getElementById('focusReset').addEventListener('click', reset);
    renderTimer();
</script>
<script src="${pageContext.request.contextPath}/assets/js/theme.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/accessibility.js"></script>
</body>
</html>
