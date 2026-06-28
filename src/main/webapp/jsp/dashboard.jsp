<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="theme-color" content="#2563eb">
    <meta name="csrf-token" content="${csrfToken}">
    <title>Dashboard | Productivity Tracker</title>
    <link rel="manifest" href="${pageContext.request.contextPath}/manifest.json">
    <link rel="icon" type="image/svg+xml" href="${pageContext.request.contextPath}/assets/images/icon-192.svg">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/layout.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/dark-mode.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/animations.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/effects.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/dashboard.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/apple.css">
    <script src="https://cdn.jsdelivr.net/npm/chart.js@3.9.1/dist/chart.min.js"></script>
</head>
<body>

<jsp:include page="partials/sidebar.jsp" />
<jsp:include page="partials/header.jsp">
    <jsp:param name="title" value="Productivity Dashboard" />
    <jsp:param name="subtitle" value="Track streaks, focus sessions, and goals in one place" />
</jsp:include>

<main class="app-main dashboard-shell">
    <section class="dashboard-metrics">
        <article class="dashboard-card">
            <span>Productivity</span>
            <strong>${dashboard.productivity}%</strong>
            <div class="progress-track"><div class="progress-fill" style="--progress: ${dashboard.productivity}%"></div></div>
        </article>
        <article class="dashboard-card">
            <span>Tasks Done</span>
            <strong>${dashboard.completedTasks}/${dashboard.totalTasks}</strong>
        </article>
        <article class="dashboard-card">
            <span>Daily Streak</span>
            <strong>${dashboard.bestStreak}d</strong>
        </article>
        <article class="dashboard-card">
            <span>Weekly Streak</span>
            <strong>${dashboard.weeklyStreak}d</strong>
        </article>
        <article class="dashboard-card">
            <span>Freeze Tokens</span>
            <strong>${dashboard.freezeTokens}</strong>
        </article>
        <article class="dashboard-card">
            <span>Focus Today</span>
            <strong id="focusMinutes">${dashboard.focusMinutesToday}m</strong>
        </article>
    </section>

    <section class="dashboard-grid-two">
        <article class="dashboard-panel">
            <h2>Today</h2>
            <ul class="compact-list">
                <c:choose>
                    <c:when test="${empty dashboard.todayTasks}">
                        <li><span>No tasks for today</span><a href="${pageContext.request.contextPath}/TaskServlet">Add</a></li>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="task" items="${dashboard.todayTasks}">
                            <li>
                                <span><c:out value="${task.name}" /></span>
                                <strong><c:out value="${task.priority}" /></strong>
                            </li>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </ul>
        </article>

        <article class="dashboard-panel">
            <h2>Habit Streaks</h2>
            <ul class="compact-list">
                <c:choose>
                    <c:when test="${empty dashboard.streaks}">
                        <li><span>No streaks yet</span><a href="${pageContext.request.contextPath}/HabitServlet">Add Habit</a></li>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="streak" items="${dashboard.streaks}">
                            <li>
                                <span><c:out value="${streak.habitName}" /></span>
                                <strong>${streak.currentStreak}d</strong>
                            </li>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </ul>
        </article>
    </section>

    <section class="dashboard-grid-two" style="margin-top: 18px;">
        <article class="dashboard-panel">
            <h2>Pomodoro</h2>
            <div class="pomodoro-shell">
                <div id="pomodoroLabel">Work</div>
                <div id="pomodoroTimer" class="pomodoro-timer">25:00</div>
                <div class="pomodoro-controls">
                    <button id="pomodoroStart" class="pomodoro-btn">Start</button>
                    <button id="pomodoroPause" class="pomodoro-btn">Pause</button>
                    <button id="pomodoroReset" class="pomodoro-btn">Reset</button>
                </div>
                <p class="pomodoro-meta">Sessions today: <strong id="pomodoroSessions">${dashboard.pomodoroSessionsToday}</strong></p>
            </div>
        </article>

        <article class="dashboard-panel">
            <h2>Goals</h2>
            <form action="${pageContext.request.contextPath}/GoalServlet" method="post" class="goal-form">
                <input type="hidden" name="csrfToken" value="${csrfToken}">
                <input type="hidden" name="returnTo" value="/DashboardServlet">
                <input type="text" name="title" placeholder="Goal title" maxlength="120" required>
                <select name="goalType" required>
                    <option value="MONTHLY">Monthly</option>
                    <option value="QUARTERLY">Quarterly</option>
                </select>
                <input type="number" name="targetValue" min="1" max="1000" placeholder="Target" required>
                <button type="submit" class="pomodoro-btn">Add Goal</button>
            </form>
            <ul class="compact-list">
                <c:choose>
                    <c:when test="${empty dashboard.goals}">
                        <li><span>No active goals</span><strong>0%</strong></li>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="goal" items="${dashboard.goals}">
                            <li>
                                <span><c:out value="${goal.title}" /></span>
                                <strong>${goal.progressPercentage}%</strong>
                            </li>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </ul>
        </article>
    </section>

    <section class="dashboard-grid-two" style="margin-top: 18px;">
        <article class="dashboard-panel">
            <h2>Weekly Trend</h2>
            <canvas id="dashboardTrendChart" height="120"></canvas>
        </article>

        <article class="dashboard-panel">
            <h2>Achievements</h2>
            <ul class="compact-list">
                <c:choose>
                    <c:when test="${empty dashboard.achievements}">
                        <li><span>No achievements yet</span><strong>Locked</strong></li>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="achievement" items="${dashboard.achievements}">
                            <li>
                                <span>${achievement.icon} <c:out value="${achievement.title}" /></span>
                                <strong>${achievement.unlocked ? 'Unlocked' : 'Locked'}</strong>
                            </li>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </ul>
        </article>
    </section>
</main>

<script>
    const trendLabels = [<c:forEach var="point" items="${dashboard.weeklyProductivity}" varStatus="status">'${point.label}'<c:if test="${!status.last}">,</c:if></c:forEach>];
    const trendValues = [<c:forEach var="point" items="${dashboard.weeklyProductivity}" varStatus="status">${point.value}<c:if test="${!status.last}">,</c:if></c:forEach>];

    const trendChartCanvas = document.getElementById('dashboardTrendChart');
    if (trendChartCanvas && window.Chart) {
        new Chart(trendChartCanvas, {
            type: 'line',
            data: {
                labels: trendLabels,
                datasets: [{
                    data: trendValues,
                    borderColor: '#2563eb',
                    backgroundColor: 'rgba(37,99,235,0.12)',
                    fill: true,
                    tension: 0.35,
                    pointRadius: 4
                }]
            },
            options: {
                responsive: true,
                plugins: { legend: { display: false } },
                scales: { y: { beginAtZero: true, max: 100 } }
            }
        });
    }

    const csrfToken = document.querySelector('meta[name="csrf-token"]')?.getAttribute('content') || '';
    const workSeconds = 25 * 60;
    const breakSeconds = 5 * 60;
    let running = false;
    let onBreak = false;
    let remaining = workSeconds;
    let timerRef = null;

    const timerEl = document.getElementById('pomodoroTimer');
    const labelEl = document.getElementById('pomodoroLabel');
    const focusEl = document.getElementById('focusMinutes');
    const sessionsEl = document.getElementById('pomodoroSessions');

    function renderTimer() {
        const mm = String(Math.floor(remaining / 60)).padStart(2, '0');
        const ss = String(remaining % 60).padStart(2, '0');
        timerEl.textContent = mm + ':' + ss;
        labelEl.textContent = onBreak ? 'Break' : 'Work';
    }

    function playBeep() {
        const ctx = new (window.AudioContext || window.webkitAudioContext)();
        const oscillator = ctx.createOscillator();
        const gain = ctx.createGain();
        oscillator.type = 'sine';
        oscillator.frequency.setValueAtTime(880, ctx.currentTime);
        gain.gain.setValueAtTime(0.16, ctx.currentTime);
        oscillator.connect(gain);
        gain.connect(ctx.destination);
        oscillator.start();
        oscillator.stop(ctx.currentTime + 0.2);
    }

    function logPomodoroSession() {
        fetch('${pageContext.request.contextPath}/PomodoroServlet', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
                'X-CSRF-Token': csrfToken
            },
            body: 'workMinutes=25&breakMinutes=5&cycles=1&csrfToken=' + encodeURIComponent(csrfToken)
        }).then(() => {
            const sessions = parseInt(sessionsEl.textContent, 10) || 0;
            const focus = parseInt((focusEl.textContent || '0').replace('m', ''), 10) || 0;
            sessionsEl.textContent = sessions + 1;
            focusEl.textContent = (focus + 25) + 'm';
        }).catch(() => {});
    }

    function tick() {
        if (!running) {
            return;
        }
        remaining -= 1;
        if (remaining <= 0) {
            playBeep();
            if (!onBreak) {
                logPomodoroSession();
            }
            onBreak = !onBreak;
            remaining = onBreak ? breakSeconds : workSeconds;
        }
        renderTimer();
    }

    document.getElementById('pomodoroStart').addEventListener('click', function () {
        if (running) {
            return;
        }
        running = true;
        timerRef = window.setInterval(tick, 1000);
    });

    document.getElementById('pomodoroPause').addEventListener('click', function () {
        running = false;
        if (timerRef) {
            window.clearInterval(timerRef);
            timerRef = null;
        }
    });

    document.getElementById('pomodoroReset').addEventListener('click', function () {
        running = false;
        onBreak = false;
        remaining = workSeconds;
        if (timerRef) {
            window.clearInterval(timerRef);
            timerRef = null;
        }
        renderTimer();
    });

    renderTimer();
</script>
<script src="${pageContext.request.contextPath}/assets/js/theme.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/accessibility.js"></script>
</body>
</html>
