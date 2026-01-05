<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>


<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="theme-color" content="#2563eb">
    <title>Tasks | Productivity Tracker</title>

    <!-- PWA & Icons -->
    <link rel="manifest" href="../manifest.json">
    <link rel="icon" type="image/svg+xml" href="../assets/images/icon-192.svg">
    <link rel="alternate icon" type="image/png" href="../assets/images/favicon.png">

    <!-- CSS -->
    <link rel="stylesheet" href="../assets/css/style.css">
    <link rel="stylesheet" href="../assets/css/tasks.css">
    <link rel="stylesheet" href="../assets/css/dark-mode.css">
    <link rel="stylesheet" href="../assets/css/animations.css">
    <link rel="stylesheet" href="../assets/css/effects.css">
</head>

<body>

<header class="header">
    <h1>📋 Tasks</h1>
    <p>Plan it. Track it. Complete it. Celebrate it.</p>
    <button id="theme-toggle" class="theme-toggle" aria-label="Toggle theme" title="Toggle theme">🌓</button>
</header>

<main class="content">

    <!-- ===== TASK STATISTICS ===== -->
    <section class="task-stats fade-in">
        <div class="stat-card">
            <div class="stat-number" id="totalTasks">0</div>
            <div class="stat-label">Total Tasks</div>
        </div>
        <div class="stat-card">
            <div class="stat-number" id="completedTasks">0</div>
            <div class="stat-label">Completed</div>
        </div>
        <div class="stat-card">
            <div class="stat-number" id="pendingTasks">0</div>
            <div class="stat-label">Pending</div>
        </div>
        <div class="stat-card">
            <div class="stat-number" id="highPriorityTasks">0</div>
            <div class="stat-label">High Priority</div>
        </div>
    </section>

    <!-- ===== ADD TASK ===== -->
    <section class="card fade-in">
        <h2>➕ Add New Task</h2>

        <form action="../TaskServlet" method="post" id="taskForm">

            <div class="form-group">
                <label for="task">Task Name</label>
                <input type="text" id="task" name="taskName"
                       placeholder="e.g. Finish project assignment" required>
            </div>

            <div class="form-group">
                <label for="description">Description</label>
                <textarea id="description" name="description"
                          placeholder="Add details about your task..."></textarea>
            </div>

            <div class="form-group">
                <label for="priority">Priority Level</label>
                <select id="priority" name="priority" required>
                    <option value="">-- Select Priority --</option>
                    <option value="Low">🟢 Low</option>
                    <option value="Medium">🟡 Medium</option>
                    <option value="High">🔴 High</option>
                </select>
            </div>

            <div class="form-group">
                <label for="dueDate">Due Date</label>
                <input type="date" id="dueDate" name="dueDate">
            </div>

            <button type="submit" class="task-cta">
                ✓ Add Task
            </button>

        </form>
    </section>

    <!-- ===== TASK LIST ===== -->
    <section class="card fade-in">
        <h2>📝 Your Tasks</h2>

        <table>
            <thead>
                <tr>
                    <th>Task Name</th>
                    <th>Priority</th>
                    <th>Due Date</th>
                    <th>Status</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>

                <!-- Empty state -->
                <c:if test="${empty tasks}">
                    <tr>
                        <td colspan="5" class="empty-state">
                            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2" />
                            </svg>
                            <div>No tasks yet. Create your first task to get started!</div>
                        </td>
                    </tr>
                </c:if>

                <!-- Dynamic tasks -->
                <c:forEach var="task" items="${tasks}">
                    <tr class="fade-in">
                        <td>
                            <strong>${task.name}</strong>
                            <c:if test="${not empty task.description}">
                                <br><small style="color: #94a3b8;">${task.description}</small>
                            </c:if>
                        </td>

                        <td>
                            <c:choose>
                                <c:when test="${task.priority == 'High'}">
                                    <span class="priority-high">🔴 High</span>
                                </c:when>
                                <c:when test="${task.priority == 'Medium'}">
                                    <span class="priority-medium">🟡 Medium</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="priority-low">🟢 Low</span>
                                </c:otherwise>
                            </c:choose>
                        </td>

                        <td>
                            <c:choose>
                                <c:when test="${not empty task.dueDate}">
                                    ${task.dueDate}
                                </c:when>
                                <c:otherwise>
                                    <span style="color: #cbd5e1;">No date</span>
                                </c:otherwise>
                            </c:choose>
                        </td>

                        <td>
                            <c:choose>
                                <c:when test="${task.status == 'Completed'}">
                                    <span class="status-done">✓ Completed</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="status-pending">⏳ Pending</span>
                                </c:otherwise>
                            </c:choose>
                        </td>

                        <td>
                            <div class="action-buttons">
                                <c:if test="${task.status != 'Completed'}">
                                    <form action="../TaskServlet" method="post" style="display: inline;">
                                        <input type="hidden" name="action" value="complete">
                                        <input type="hidden" name="taskId" value="${task.id}">
                                        <button type="submit" class="btn-small btn-complete" title="Mark as Complete">
                                            Mark Done
                                        </button>
                                    </form>
                                </c:if>
                                <form action="../TaskServlet" method="post" style="display: inline;">
                                    <input type="hidden" name="action" value="delete">
                                    <input type="hidden" name="taskId" value="${task.id}">
                                    <button type="submit" class="btn-small btn-delete" title="Delete Task" onclick="return confirm('Are you sure?')">
                                        Delete
                                    </button>
                                </form>
                            </div>
                        </td>
                    </tr>
                </c:forEach>

            </tbody>
        </table>
    </section>

</main>

<footer class="footer">
    <a href="index.jsp">← Back to Dashboard</a>
</footer>

<script>
    // Dynamic statistics calculation
    function updateStats() {
        const rows = document.querySelectorAll('table tbody tr');
        const tasks = [];
        
        rows.forEach(row => {
            if (!row.querySelector('.empty-state')) {
                tasks.push(row);
            }
        });

        let total = tasks.length;
        let completed = 0;
        let pending = 0;
        let highPriority = 0;

        tasks.forEach(row => {
            const statusCell = row.cells[3]?.textContent || '';
            const priorityCell = row.cells[1]?.textContent || '';

            if (statusCell.includes('Completed')) {
                completed++;
            } else {
                pending++;
            }

            if (priorityCell.includes('High')) {
                highPriority++;
            }
        });

        document.getElementById('totalTasks').textContent = total;
        document.getElementById('completedTasks').textContent = completed;
        document.getElementById('pendingTasks').textContent = pending;
        document.getElementById('highPriorityTasks').textContent = highPriority;
    }

    // Initialize stats on page load
    document.addEventListener('DOMContentLoaded', updateStats);
    
    // Form validation
    document.getElementById('taskForm').addEventListener('submit', function(e) {
        const taskName = document.getElementById('task').value.trim();
        if (!taskName) {
            e.preventDefault();
            alert('Please enter a task name');
            return false;
        }
    });

    // Add ripple effect to buttons
    document.querySelectorAll('.task-cta, .btn-small').forEach(button => {
        button.addEventListener('click', function(e) {
            const rect = this.getBoundingClientRect();
            const x = e.clientX - rect.left;
            const y = e.clientY - rect.top;
            
            const ripple = document.createElement('span');
            ripple.style.position = 'absolute';
            ripple.style.left = x + 'px';
            ripple.style.top = y + 'px';
            ripple.style.width = '0';
            ripple.style.height = '0';
            ripple.style.borderRadius = '50%';
            ripple.style.background = 'rgba(255, 255, 255, 0.6)';
            ripple.style.pointerEvents = 'none';
            ripple.style.animation = 'ripple 0.6s ease-out';
        });
    });
    </script>

    <!-- Register service worker helper and Theme & Accessibility -->
    <script src="../assets/js/register-sw.js"></script>
    <script src="../assets/js/theme.js"></script>
    <script src="../assets/js/accessibility.js"></script>
    <script src="../assets/js/effects.js"></script>

</body>
</html>