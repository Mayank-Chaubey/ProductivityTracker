<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>


<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="theme-color" content="#2563eb">
    <title>Habits | Productivity Tracker</title>

    <!-- PWA & Icons -->
    <link rel="manifest" href="../manifest.json">
    <link rel="icon" type="image/svg+xml" href="../assets/images/icon-192.svg">
    <link rel="alternate icon" type="image/png" href="../assets/images/favicon.png">

    <!-- CSS -->
    <link rel="stylesheet" href="../assets/css/habits.css">
    <link rel="stylesheet" href="../assets/css/dark-mode.css">
    <link rel="stylesheet" href="../assets/css/animations.css">
    <link rel="stylesheet" href="../assets/css/effects.css">
</head>

<body>

<header class="header">
    <h1>🔥 Habits</h1>
    <p>Small actions, repeated — this is where momentum is built</p>
    <button id="theme-toggle" class="theme-toggle" aria-label="Toggle theme" title="Toggle theme">🌓</button>
</header>

<main class="content">

    <!-- Success and Error Alerts -->
    <div id="successMessage" class="alert alert-success">
        <strong>✓</strong> <span id="successText">Habit added successfully!</span>
    </div>

    <div id="errorMessage" class="alert alert-error">
        <strong>✕</strong> <span id="errorText">Please fill in all required fields.</span>
    </div>

    <!-- Error/Success Feedback -->
    <c:if test="${param.error == 'invalid_input'}">
        <div class="alert alert-error">Invalid input. Please check your entries.</div>
    </c:if>
    <c:if test="${param.error == 'save_failed'}">
        <div class="alert alert-error">Failed to save habit. Please try again.</div>
    </c:if>
    <c:if test="${param.success == '1'}">
        <div class="alert alert-success">Habit added successfully!</div>
    </c:if>

    <!-- Stats Summary -->
    <c:if test="${not empty habits}">
        <section class="card fade-in">
            <div class="stats-summary">
                <div class="stat-item">
                    <div class="stat-item-label">Total Habits</div>
                    <div class="stat-item-value" id="totalHabits">0</div>
                </div>
                <div class="stat-item">
                    <div class="stat-item-label">Done Today</div>
                    <div class="stat-item-value" id="doneToday">0</div>
                </div>
                <div class="stat-item">
                    <div class="stat-item-label">Avg Streak</div>
                    <div class="stat-item-value" id="avgStreak">0</div>
                </div>
                <div class="stat-item">
                    <div class="stat-item-label">Best Streak</div>
                    <div class="stat-item-value" id="bestStreak">0</div>
                </div>
            </div>
        </section>
    </c:if>

    <!-- Add Habit Form -->
    <section class="card fade-in">
        <h2>Add New Habit</h2>

        <form id="habitForm" action="../HabitServlet" method="post">
            <input type="hidden" name="csrfToken" value="${csrfToken}" />

            <div class="form-group">
                <label for="habit">Habit Name</label>
                <input type="text" id="habit" name="habitName"
                       placeholder="e.g. Meditation, Reading, Exercise" required maxlength="100" aria-label="Habit Name">
            </div>

            <div class="form-group">
                <label for="frequency">Frequency</label>
                <select id="frequency" name="frequency" required aria-label="Frequency">
                    <option value="">Select frequency</option>
                    <option value="Daily">Daily</option>
                    <option value="Weekly">Weekly (3-4 times)</option>
                    <option value="Monthly">Monthly</option>
                </select>
            </div>

            <button type="submit" class="habit-cta" id="addBtn" aria-label="Add Habit">
                + Add Habit
            </button>

        </form>
    </section>

    <!-- Habit List -->
    <section class="card fade-delay">
        <h2>Your Habits</h2>

        <!-- Empty State -->
        <c:if test="${empty habits}">
            <div class="empty-state">
                <div class="empty-state-icon">🌱</div>
                <p>No habits yet. Start small — consistency beats intensity.</p>
            </div>
        </c:if>

        <!-- Habits Table -->
        <c:if test="${not empty habits}">
            <table>
                <thead>
                    <tr>
                        <th>Habit Name</th>
                        <th>Frequency</th>
                        <th>Streak</th>
                        <th>Status</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody id="habitsTable">
                    <c:forEach var="habit" items="${habits}">
                        <tr class="habit-row" data-habit-id="${habit.id}">
                            <td><strong><c:out value="${habit.name}"/></strong></td>
                            <td>
                                <span class="frequency-badge"><c:out value="${habit.frequency}"/></span>
                            </td>
                            <td>
                                <span class="streak-badge">
                                    🔥 <c:out value="${habit.streak}"/> days
                                </span>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${habit.doneToday}">
                                        <span class="status-done">Done Today</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="status-pending">Pending</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <div class="habit-row-actions">
                                    <c:if test="${not habit.doneToday}">
                                        <button class="habit-btn btn-check" data-habit-id="${habit.id}" onclick="markHabitDone(event)" aria-label="Mark as done">
                                            ✓ Check
                                        </button>
                                    </c:if>
                                    <button class="habit-btn btn-delete" data-habit-id="${habit.id}" onclick="deleteHabit(event)" aria-label="Delete habit">
                                        ✕ Delete
                                    </button>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:if>
    </section>

</main>

<footer class="footer">
    <a href="index.jsp">← Back to Dashboard</a>
    <a href="logout.jsp">Logout</a>
</footer>

<script>
    // Habits Manager
    class HabitsManager {
        constructor() {
            this.form = document.getElementById('habitForm');
            this.habitInput = document.getElementById('habit');
            this.frequencySelect = document.getElementById('frequency');
            this.addBtn = document.getElementById('addBtn');
            this.successMessage = document.getElementById('successMessage');
            this.errorMessage = document.getElementById('errorMessage');
            this.errorText = document.getElementById('errorText');
            this.habitsTable = document.getElementById('habitsTable');
            this.init();
        }

        init() {
            this.form.addEventListener('submit', (e) => this.handleAddHabit(e));
            this.updateStats();
        }

        validateForm() {
            const habitName = this.habitInput.value.trim();
            const frequency = this.frequencySelect.value;

            if (!habitName || !frequency) {
                this.errorText.textContent = 'Please enter a habit name and select a frequency.';
                this.showError();
                return false;
            }

            if (habitName.length < 2) {
                this.errorText.textContent = 'Habit name must be at least 2 characters.';
                this.showError();
                return false;
            }

            return true;
        }

        handleAddHabit(e) {
            e.preventDefault();

            if (!this.validateForm()) {
                return;
            }

            // Show loading state
            this.addBtn.classList.add('loading');
            this.addBtn.disabled = true;

            // Simulate form submission
            setTimeout(() => {
                this.successMessage.classList.add('show');
                this.addBtn.classList.remove('loading');
                this.addBtn.disabled = false;

                // Clear form
                this.habitInput.value = '';
                this.frequencySelect.value = '';

                // Hide message after 3 seconds
                setTimeout(() => {
                    this.successMessage.classList.remove('show');
                }, 3000);

                // Submit actual form
                setTimeout(() => {
                    this.form.submit();
                }, 600);
            }, 1200);
        }

        showError() {
            this.errorMessage.classList.add('show');
            setTimeout(() => {
                this.errorMessage.classList.remove('show');
            }, 4000);
        }

        updateStats() {
            const rows = document.querySelectorAll('.habit-row');
            if (rows.length === 0) return;

            let doneCount = 0;
            let totalStreak = 0;
            let maxStreak = 0;

            rows.forEach(row => {
                const status = row.querySelector('.status-done');
                const streakText = row.querySelector('.streak-badge').textContent;
                const streak = parseInt(streakText.match(/\d+/)[0]);

                if (status) doneCount++;
                totalStreak += streak;
                if (streak > maxStreak) maxStreak = streak;
            });

            document.getElementById('totalHabits').textContent = rows.length;
            document.getElementById('doneToday').textContent = doneCount;
            document.getElementById('avgStreak').textContent = Math.round(totalStreak / rows.length);
            document.getElementById('bestStreak').textContent = maxStreak;
        }
    }

    // Mark habit as done
    function markHabitDone(event) {
        const btn = event.target;
        const habitId = btn.dataset.habitId;

        btn.disabled = true;
        btn.classList.add('loading');

        // Simulate API call
        setTimeout(() => {
            btn.style.display = 'none';
            
            const row = btn.closest('.habit-row');
            const statusCell = row.querySelector('td:nth-child(4)');
            statusCell.innerHTML = '<span class="status-done">Done Today</span>';

            const manager = window.habitsManager;
            manager.updateStats();
        }, 800);
    }

    // Delete habit
    function deleteHabit(event) {
        const btn = event.target;
        const habitId = btn.dataset.habitId;

        if (confirm('Are you sure you want to delete this habit? This action cannot be undone.')) {
            btn.disabled = true;
            btn.classList.add('loading');

            // Simulate API call
            setTimeout(() => {
                const row = btn.closest('.habit-row');
                row.style.opacity = '0';
                row.style.transform = 'translateX(-20px)';
                
                setTimeout(() => {
                    row.remove();
                    
                    // If no habits left, show empty state
                    const remaining = document.querySelectorAll('.habit-row');
                    if (remaining.length === 0) {
                        location.reload();
                    }
                }, 300);
            }, 800);
        }
    }

    // Initialize on page load
    document.addEventListener('DOMContentLoaded', () => {
        window.habitsManager = new HabitsManager();
    });
</script>

    <!-- Register service worker helper and Theme & Accessibility -->
    <script src="../assets/js/register-sw.js"></script>
    <script src="../assets/js/theme.js"></script>
    <script src="../assets/js/accessibility.js"></script>
    <script src="../assets/js/effects.js"></script>

</body>
</html>
