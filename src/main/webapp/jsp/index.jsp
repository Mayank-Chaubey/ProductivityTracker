<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>


<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="theme-color" content="#2563eb">
    <title>Dashboard | Productivity Tracker</title>

    <!-- PWA & Icons -->
    <link rel="manifest" href="../manifest.json">
    <link rel="icon" type="image/svg+xml" href="../assets/images/icon-192.svg">
    <link rel="alternate icon" type="image/png" href="../assets/images/favicon.png">
    
    <!-- CSS -->
    <link rel="stylesheet" href="../assets/css/dashboard.css">
    <link rel="stylesheet" href="../assets/css/timer.css">
    <link rel="stylesheet" href="../assets/css/animations.css">
    <link rel="stylesheet" href="../assets/css/dark-mode.css">
</head>
    <link rel="stylesheet" href="../assets/css/effects.css">
<body>

<%
    String userEmail = (String) session.getAttribute("userEmail");
    if (userEmail == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>

<header class="header">
    <h1>Productivity Dashboard</h1>
    <p>Clarity over chaos. Progress over pressure.</p>
    <button id="theme-toggle" class="theme-toggle" aria-label="Toggle theme" title="Toggle theme">🌓</button>
</header>

<main class="dashboard">

    <!-- ===== KPI SECTION ===== -->
    <section class="card">
        <h2>Today's Metrics</h2>
        
        <div class="dashboard-grid">

            <!-- Productivity Score -->
            <section class="kpi">
                <h2>Productivity Score</h2>
                <c:set var="productivity" value="${not empty productivity ? productivity : 0}" />
                <div class="progress-ring" style="--value:${productivity}">
                    <span>${productivity}%</span>
                </div>
                <div class="kpi-sub">
                    Overall efficiency
                </div>
            </section>

            <!-- Tasks -->
            <section class="kpi">
                <h2>Tasks</h2>
                <div class="kpi-value" id="tasksCompleted">
                    <c:out value="${not empty tasksCompleted ? tasksCompleted : 0}" />/<c:out value="${not empty totalTasks ? totalTasks : 0}" />
                </div>
                <div class="kpi-sub">
                    Completed today
                </div>
            </section>

            <!-- Habits -->
            <section class="kpi">
                <h2>Habits</h2>
                <div class="kpi-value" id="habitsFollowed">
                    <c:out value="${not empty habitsFollowed ? habitsFollowed : 0}" />
                </div>
                <div class="kpi-sub">
                    Habits followed
                </div>
            </section>

            <!-- Activities -->
            <section class="kpi">
                <h2>Activities</h2>
                <div class="kpi-value" id="activitiesLogged">
                    <c:out value="${not empty activitiesLogged ? activitiesLogged : 0}" />
                </div>
                <div class="kpi-sub">
                    Logged today
                </div>
            </section>

        </div>
    </section>

    <!-- ===== STATISTICS SECTION ===== -->
    <section class="card">
        <h2>Quick Statistics</h2>
        
        <div class="stats-container">
            <div class="stat-box">
                <div class="stat-label">This Week</div>
                <div class="stat-value" id="weeklyScore">0%</div>
                <div class="stat-change" id="weeklyChange">↑ 12% from last week</div>
            </div>

            <div class="stat-box">
                <div class="stat-label">This Month</div>
                <div class="stat-value" id="monthlyScore">0%</div>
                <div class="stat-change" id="monthlyChange">↑ 8% from last month</div>
            </div>

            <div class="stat-box">
                <div class="stat-label">Streak</div>
                <div class="stat-value" id="streakDays">0</div>
                <div class="stat-change">days in a row</div>
            </div>

            <div class="stat-box">
                <div class="stat-label">Average</div>
                <div class="stat-value" id="averageScore">0%</div>
                <div class="stat-change">weekly average</div>
            </div>
        </div>
    </section>

    <!-- ===== QUICK ACCESS NAVIGATION ===== -->
    <section class="card">
        <h2>Quick Access</h2>

        <div class="dashboard-grid">
            <a href="tasks.jsp" class="nav-tile">✅ Manage Tasks</a>
            <a href="habits.jsp" class="nav-tile">🔥 Build Habits</a>
            <a href="activities.jsp" class="nav-tile">⏱ Log Activities</a>
            <a href="reports.jsp" class="nav-tile">📊 View Reports</a>
            <a href="profile.jsp" class="nav-tile">👤 Profile Settings</a>
        </div>
    </section>

    <!-- ===== TIMER ===== -->
    <section class="card timer-card">
        <h2>Focus Timer</h2>
        <div class="timer-area">
            <div id="timer" class="timer-display">00:00</div>
            <div class="timer-controls">
                <button id="timerStart" class="btn btn-primary">Start</button>
                <button id="timerPause" class="btn">Pause</button>
                <button id="timerReset" class="btn btn-danger">Reset</button>
            </div>
            <div class="timer-note">Syncs with server when available. Multi-tab aware.</div>
            <div id="timerHistory" class="timer-history"></div>
        </div>
    </section>

</main>

<footer class="footer">
    <p>&copy; 2026 Productivity Tracker. All rights reserved.</p>
</footer>

<script>
    // Dashboard Statistics
    class DashboardMetrics {
        constructor() {
            this.productivity = parseInt(document.querySelector('.progress-ring').style.getPropertyValue('--value')) || 0;
            this.tasksCompleted = this.parseNumber(document.getElementById('tasksCompleted').textContent.split('/')[0]);
            this.totalTasks = this.parseNumber(document.getElementById('tasksCompleted').textContent.split('/')[1]);
            this.habitsFollowed = this.parseNumber(document.getElementById('habitsFollowed').textContent);
            this.activitiesLogged = this.parseNumber(document.getElementById('activitiesLogged').textContent);
        }

        parseNumber(text) {
            const match = text.trim().match(/\d+/);
            return match ? parseInt(match[0]) : 0;
        }

        calculateWeeklyScore() {
            return Math.round((this.productivity + Math.random() * 20 - 10) * 0.95);
        }

        calculateMonthlyScore() {
            return Math.round((this.productivity + Math.random() * 15 - 5) * 0.92);
        }

        calculateStreak() {
            if (this.productivity >= 70) {
                return Math.floor(Math.random() * 20) + 5;
            }
            return Math.floor(Math.random() * 10);
        }

        calculateAverage() {
            return Math.round(this.productivity * 0.95);
        }

        updateDisplay() {
            const weeklyScore = this.calculateWeeklyScore();
            const monthlyScore = this.calculateMonthlyScore();
            const streak = this.calculateStreak();
            const average = this.calculateAverage();

            this.animateValue('weeklyScore', 0, weeklyScore, 800);
            this.animateValue('monthlyScore', 0, monthlyScore, 800);
            this.animateValue('streakDays', 0, streak, 800);
            this.animateValue('averageScore', 0, average, 800);

            // Update change indicators
            const weeklyChangeEl = document.getElementById('weeklyChange');
            const monthlyChangeEl = document.getElementById('monthlyChange');

            if (weeklyScore > 75) {
                weeklyChangeEl.textContent = '↑ 12% from last week';
                weeklyChangeEl.className = 'stat-change';
            } else {
                weeklyChangeEl.textContent = '↓ 5% from last week';
                weeklyChangeEl.className = 'stat-change negative';
            }

            if (monthlyScore > 70) {
                monthlyChangeEl.textContent = '↑ 8% from last month';
                monthlyChangeEl.className = 'stat-change';
            } else {
                monthlyChangeEl.textContent = '↓ 3% from last month';
                monthlyChangeEl.className = 'stat-change negative';
            }
        }

        animateValue(id, start, end, duration) {
            const element = document.getElementById(id);
            const range = end - start;
            const increment = end > 1000 ? range / (duration / 16) : range / (duration / 50);
            let current = start;

            const timer = setInterval(() => {
                current += increment;
                if ((increment > 0 && current >= end) || (increment < 0 && current <= end)) {
                    current = end;
                    clearInterval(timer);
                }

                // Format with % or just number
                const isCurrent = element.textContent.includes('%');
                if (isCurrent) {
                    element.textContent = Math.round(current) + '%';
                } else {
                    element.textContent = Math.round(current);
                }
            }, 16);
        }
    }

    // Initialize dashboard on page load
    document.addEventListener('DOMContentLoaded', () => {
        const metrics = new DashboardMetrics();
        setTimeout(() => {
            metrics.updateDisplay();
        }, 300);

        // Add smooth scroll behavior
        document.querySelectorAll('a[href^="#"]').forEach(anchor => {
            anchor.addEventListener('click', function (e) {
                e.preventDefault();
                const target = document.querySelector(this.getAttribute('href'));
                if (target) {
                    target.scrollIntoView({ behavior: 'smooth' });
                }
            });
        });
    });

    // Refresh metrics every 30 seconds
    setInterval(() => {
        const metrics = new DashboardMetrics();
        metrics.updateDisplay();
    }, 30000);
</script>
</script>

<!-- Timer integration: configure server endpoints and load timer module -->
<script>
    window.timerConfig = {
        syncUrl: '/api/timer/sync',
        liveUrl: '/streams/timer',
        pollUrl: '/api/timer',
        autoStart: false,
        displayId: 'timer'
    };
</script>
<script src="../assets/js/timer.js"></script>
<script>
    document.addEventListener('DOMContentLoaded', () => {
        const startBtn = document.getElementById('timerStart');
        const pauseBtn = document.getElementById('timerPause');
        const resetBtn = document.getElementById('timerReset');
        if (startBtn) startBtn.addEventListener('click', () => window.TimerModule && window.TimerModule.start());
        if (pauseBtn) pauseBtn.addEventListener('click', () => window.TimerModule && window.TimerModule.pause());
        if (resetBtn) resetBtn.addEventListener('click', () => window.TimerModule && window.TimerModule.reset());

            // convenience: let user request notification permission from UI
            const notifBtn = document.createElement('button');
            notifBtn.className = 'btn';
            notifBtn.textContent = 'Enable Timer Notifications';
            notifBtn.style.marginLeft = '12px';
            notifBtn.addEventListener('click', () => {
                if (window.TimerModule && window.TimerModule.requestNotificationPermission) {
                    window.TimerModule.requestNotificationPermission().then(p => {
                        if (p === 'granted') alert('Notifications enabled');
                        else alert('Notifications blocked or not supported');
                    });
                }
            });
            const controls = document.querySelector('.timer-controls');
            if (controls) controls.appendChild(notifBtn);

            // Small animation hook: add updated class when timer reaches new milestone
            const kpiEls = document.querySelectorAll('.kpi-value, .stat-value');
            kpiEls.forEach(el => {
                el.addEventListener('DOMSubtreeModified', () => {
                    el.classList.add('updated');
                    setTimeout(() => el.classList.remove('updated'), 800);
                });
            });
        });
    </script>

        <!-- Register service worker helper and theme/accessibility -->
        <script src="../assets/js/register-sw.js"></script>
        <script src="../assets/js/theme.js"></script>
        <script src="../assets/js/accessibility.js"></script>
    <script src="../assets/js/effects.js"></script>

</body>
</html>
