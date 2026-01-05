<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="theme-color" content="#2563eb">
    <title>Reports | Productivity Tracker</title>

    <!-- PWA & Icons -->
    <link rel="manifest" href="../manifest.json">
    <link rel="icon" type="image/svg+xml" href="../assets/images/icon-192.svg">
    <link rel="alternate icon" type="image/png" href="../assets/images/favicon.png">

    <!-- CSS -->
    <link rel="stylesheet" href="../assets/css/style.css">
    <link rel="stylesheet" href="../assets/css/reports.css">
    <link rel="stylesheet" href="../assets/css/dark-mode.css">
    <link rel="stylesheet" href="../assets/css/animations.css">
    <link rel="stylesheet" href="../assets/css/effects.css">

    <!-- Charts Library -->
    <script src="https://cdn.jsdelivr.net/npm/chart.js@3.9.1/dist/chart.min.js"></script>
    <script src="../assets/js/charts.js" defer></script>

</head>

<body>

<header class="header">
    <h1>📊 Productivity Reports</h1>
    <p>Track your patterns. Understand your progress. Improve consistently.</p>
    <button id="theme-toggle" class="theme-toggle" aria-label="Toggle theme" title="Toggle theme">🌓</button>
</header>

<main class="content">

    <!-- ===== DATE RANGE FILTER ===== -->
    <section class="card filter-section fade-in">
        <h2>📅 Select Period</h2>
        <div class="filter-controls">
            <div class="filter-group">
                <label for="periodSelect">Report Period:</label>
                <select id="periodSelect" onchange="updateReportPeriod(this.value)">
                    <option value="week">This Week</option>
                    <option value="month">This Month</option>
                    <option value="quarter">This Quarter</option>
                    <option value="year">This Year</option>
                </select>
            </div>
            <button class="btn-export" onclick="exportReport()">📥 Export Report</button>
        </div>
    </section>

    <!-- ===== KEY METRICS ===== -->
    <section class="metrics-grid fade-in">

        <div class="metric-card">
            <div class="metric-icon">📈</div>
            <h3>Overall Productivity</h3>
            <div class="metric-value" id="productivityValue">${productivity}%</div>
            <div class="metric-label">
                <span id="productivityTrend" class="trend-badge trend-up">↑ Consistent performance</span>
            </div>
            <div class="metric-bar">
                <div class="bar-fill" style="width: ${productivity}%"></div>
            </div>
        </div>

        <div class="metric-card">
            <div class="metric-icon">✅</div>
            <h3>Tasks Completed</h3>
            <div class="metric-value" id="tasksValue">${completedTasks}/${totalTasks}</div>
            <div class="metric-label">
                <c:set var="taskPercent" value="${(completedTasks / totalTasks) * 100}"/>
                <span id="tasksTrend" class="trend-badge">
                    <c:choose>
                        <c:when test="${taskPercent >= 80}">↑ Excellent progress</c:when>
                        <c:when test="${taskPercent >= 50}">→ On track</c:when>
                        <c:otherwise>↓ Keep pushing</c:otherwise>
                    </c:choose>
                </span>
            </div>
            <div class="metric-bar">
                <div class="bar-fill" style="width: ${(completedTasks / totalTasks) * 100}%"></div>
            </div>
        </div>

        <div class="metric-card">
            <div class="metric-icon">🔥</div>
            <h3>Habit Consistency</h3>
            <div class="metric-value" id="habitValue">${habitConsistency}%</div>
            <div class="metric-label">
                <span id="habitTrend" class="trend-badge trend-neutral">Stable over last week</span>
            </div>
            <div class="metric-bar">
                <div class="bar-fill" style="width: ${habitConsistency}%"></div>
            </div>
        </div>

        <div class="metric-card">
            <div class="metric-icon">⏱️</div>
            <h3>Time Invested</h3>
            <div class="metric-value" id="timeValue">${totalTime}<span style="font-size: 0.6em;">min</span></div>
            <div class="metric-label">
                <span id="timeTrend" class="trend-badge">Logged this week</span>
            </div>
            <div class="metric-bar">
                <div class="bar-fill" style="width: 75%"></div>
            </div>
        </div>

    </section>

    <!-- ===== PERFORMANCE CHARTS ===== -->
    <section class="card fade-in">
        <h2>📉 Weekly Performance</h2>
        <div class="chart-container">
            <canvas id="performanceChart"></canvas>
        </div>
    </section>

    <!-- ===== PRODUCTIVITY TREND ===== -->
    <section class="card fade-in">
        <h2>📈 Productivity Trend</h2>
        <div class="chart-container">
            <canvas id="trendChart"></canvas>
        </div>
    </section>

    <!-- ===== DAILY BREAKDOWN ===== -->
    <section class="card fade-in">
        <h2>📋 Daily Breakdown</h2>
        <p class="table-note">
            📌 Your daily productivity snapshot for the selected period
        </p>

        <div class="table-responsive">
            <table>
                <thead>
                    <tr>
                        <th>Day</th>
                        <th>Productivity</th>
                        <th>Tasks Done</th>
                        <th>Time Logged</th>
                        <th>Status</th>
                    </tr>
                </thead>
                <tbody id="reportTableBody">

                    <!-- Empty state -->
                    <c:if test="${empty weeklyReports}">
                        <tr>
                            <td colspan="5" class="empty-state">
                                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z" />
                                </svg>
                                <div>No data available yet. Start logging activities to see your productivity breakdown!</div>
                            </td>
                        </tr>
                    </c:if>

                    <!-- Dynamic rows -->
                    <c:forEach var="day" items="${weeklyReports}">
                        <tr class="fade-in">
                            <td><strong>${day.day}</strong></td>
                            <td>
                                <div class="productivity-bar">
                                    <div class="bar-inline" style="width: ${day.productivity}%"></div>
                                    <span>${day.productivity}%</span>
                                </div>
                            </td>
                            <td>
                                <span class="badge badge-info">${day.tasksCompleted}</span>
                            </td>
                            <td>
                                <span class="badge badge-time">${day.timeLogged} min</span>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${day.productivity >= 80}">
                                        <span class="status-badge status-excellent">🌟 Excellent</span>
                                    </c:when>
                                    <c:when test="${day.productivity >= 60}">
                                        <span class="status-badge status-good">👍 Good</span>
                                    </c:when>
                                    <c:when test="${day.productivity >= 40}">
                                        <span class="status-badge status-fair">→ Fair</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="status-badge status-low">📍 Low</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>

                </tbody>
            </table>
        </div>
    </section>

    <!-- ===== INSIGHTS ===== -->
    <section class="card insights-section fade-in">
        <h2>💡 Key Insights</h2>
        <div class="insights-grid">
            <div class="insight-card">
                <div class="insight-icon">🎯</div>
                <h3>Your Strongest Day</h3>
                <p id="strongestDay">Monday</p>
                <p class="insight-detail">Keep this momentum going!</p>
            </div>

            <div class="insight-card">
                <div class="insight-icon">⚡</div>
                <h3>Peak Time</h3>
                <p id="peakTime">9 AM - 12 PM</p>
                <p class="insight-detail">Your most productive hours</p>
            </div>

            <div class="insight-card">
                <div class="insight-icon">📊</div>
                <h3>Average Daily</h3>
                <p id="averageDaily">6 tasks</p>
                <p class="insight-detail">Per day completion rate</p>
            </div>

            <div class="insight-card">
                <div class="insight-icon">🚀</div>
                <h3>This Week's Goal</h3>
                <p id="weeklyGoal">42 tasks</p>
                <p class="insight-detail">You've completed 38 (90%)</p>
            </div>
        </div>
    </section>

</main>

<footer class="footer">
    <a href="index.jsp">← Back to Dashboard</a>
</footer>

<script>
    // Initialize charts when DOM is ready
    document.addEventListener('DOMContentLoaded', function() {
        initializeCharts();
        generateInsights();
    });

    // Initialize performance and trend charts
    function initializeCharts() {
        // Weekly Performance Chart
        const performanceCtx = document.getElementById('performanceChart');
        if (performanceCtx) {
            new Chart(performanceCtx, {
                type: 'bar',
                data: {
                    labels: ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'],
                    datasets: [{
                        label: 'Productivity %',
                        data: [75, 82, 68, 90, 85, 70, 78],
                        backgroundColor: [
                            'rgba(37, 99, 235, 0.8)',
                            'rgba(37, 99, 235, 0.8)',
                            'rgba(37, 99, 235, 0.8)',
                            'rgba(37, 99, 235, 0.8)',
                            'rgba(37, 99, 235, 0.8)',
                            'rgba(37, 99, 235, 0.8)',
                            'rgba(37, 99, 235, 0.8)'
                        ],
                        borderColor: '#2563eb',
                        borderWidth: 1,
                        borderRadius: 6
                    }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: true,
                    plugins: {
                        legend: {
                            display: false
                        }
                    },
                    scales: {
                        y: {
                            beginAtZero: true,
                            max: 100,
                            ticks: {
                                callback: function(value) {
                                    return value + '%';
                                }
                            }
                        }
                    }
                }
            });
        }

        // Productivity Trend Chart
        const trendCtx = document.getElementById('trendChart');
        if (trendCtx) {
            new Chart(trendCtx, {
                type: 'line',
                data: {
                    labels: ['Week 1', 'Week 2', 'Week 3', 'Week 4', 'Week 5', 'Current'],
                    datasets: [{
                        label: 'Productivity Trend',
                        data: [65, 72, 68, 78, 82, 85],
                        borderColor: '#2563eb',
                        backgroundColor: 'rgba(37, 99, 235, 0.1)',
                        borderWidth: 3,
                        fill: true,
                        tension: 0.4,
                        pointBackgroundColor: '#2563eb',
                        pointBorderColor: '#fff',
                        pointBorderWidth: 2,
                        pointRadius: 5,
                        pointHoverRadius: 7
                    }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: true,
                    plugins: {
                        legend: {
                            display: false
                        }
                    },
                    scales: {
                        y: {
                            beginAtZero: true,
                            max: 100,
                            ticks: {
                                callback: function(value) {
                                    return value + '%';
                                }
                            }
                        }
                    }
                }
            });
        }
    }

    // Generate insights dynamically
    function generateInsights() {
        // This would be populated from server-side data
        const insights = {
            strongestDay: 'Thursday',
            peakTime: '10 AM - 1 PM',
            averageDaily: 7,
            weeklyGoal: '45 tasks (89% complete)'
        };

        document.getElementById('strongestDay').textContent = insights.strongestDay;
        document.getElementById('peakTime').textContent = insights.peakTime;
        document.getElementById('averageDaily').textContent = insights.averageDaily + ' tasks';
        document.getElementById('weeklyGoal').textContent = insights.weeklyGoal;
    }

    // Update report based on period
    function updateReportPeriod(period) {
        console.log('Updating report for period:', period);
        // Reload page or fetch new data via AJAX
        location.reload();
    }

    // Export report functionality
    function exportReport() {
        alert('Report exported successfully! Check your downloads folder.');
        // In production, this would trigger a file download
    }

    // Animate numbers on page load
    function animateNumber(element, target, duration = 1000) {
        const increment = target / (duration / 16);
        let current = 0;

        const timer = setInterval(() => {
            current += increment;
            if (current >= target) {
                current = target;
                clearInterval(timer);
            }
            element.textContent = Math.floor(current);
        }, 16);
    }
</script>

    <!-- Register service worker helper and Theme & Accessibility -->
    <script src="../assets/js/register-sw.js"></script>
    <script src="../assets/js/theme.js"></script>
    <script src="../assets/js/accessibility.js"></script>
    <script src="../assets/js/effects.js"></script>

</body>
</html>