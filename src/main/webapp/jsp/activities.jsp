<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="theme-color" content="#2563eb">
    <title>Log Activities | Productivity Tracker</title>

    <!-- PWA & Icons -->
    <link rel="manifest" href="../manifest.json">
    <link rel="icon" type="image/svg+xml" href="../assets/images/icon-192.svg">
    <link rel="alternate icon" type="image/png" href="../assets/images/favicon.png">

    <!-- CSS -->
    <link rel="stylesheet" href="../assets/css/activities.css">
    <link rel="stylesheet" href="../assets/css/animations.css">
    <link rel="stylesheet" href="../assets/css/dark-mode.css">
    <link rel="stylesheet" href="../assets/css/effects.css">
</head>

<body>

<header class="header">
    <h1>Activity Logger</h1>
    <p>Track what you're working on. Stay focused. Build momentum.</p>
    <button id="theme-toggle" class="theme-toggle" aria-label="Toggle theme" title="Toggle theme">🌓</button>
</header>

<main class="content">

    <!-- ===== ACTIVITY INPUT CARD ===== -->
    <section class="card">
        <h2>Log New Activity</h2>
        
        <form id="activityForm">
            <div class="form-group">
                <label for="activityName">Activity Name</label>
                <input type="text" id="activityName" placeholder="e.g., Design mockup, Code review" required />
                <div class="form-message"></div>
            </div>

            <div class="form-group" style="grid-template-columns: 1fr 1fr;">
                <div>
                    <label for="activityCategory">Category</label>
                    <select id="activityCategory" required>
                        <option value="">Select category</option>
                        <option value="coding">💻 Coding</option>
                        <option value="design">🎨 Design</option>
                        <option value="writing">✍️ Writing</option>
                        <option value="meeting">📞 Meeting</option>
                        <option value="learning">📚 Learning</option>
                        <option value="admin">📋 Admin</option>
                        <option value="break">🌟 Break</option>
                        <option value="other">📌 Other</option>
                    </select>
                </div>

                <div>
                    <label for="activityDuration">Duration (minutes)</label>
                    <input type="number" id="activityDuration" placeholder="e.g., 30" min="1" max="480" value="30" />
                </div>
            </div>

            <div class="form-group" style="grid-template-columns: 1fr;">
                <label for="activityNotes">Notes (optional)</label>
                <textarea id="activityNotes" placeholder="Add any notes about this activity..." style="grid-column: 1 / -1;"></textarea>
            </div>

            <div class="form-actions">
                <button type="submit" class="activity-cta">+ Log Activity</button>
                <button type="button" class="activity-cta secondary" id="quickLogBtn" title="Use timer session duration">Quick Log (Last Session)</button>
            </div>
        </form>
    </section>

    <!-- ===== STATISTICS CARD ===== -->
    <section class="card">
        <h2>Today's Activity Stats</h2>
        
        <div class="stats-grid">
            <div class="stat-item">
                <div class="stat-label">Total Activities</div>
                <div class="stat-value" id="totalActivities">0</div>
            </div>

            <div class="stat-item">
                <div class="stat-label">Total Time (hours)</div>
                <div class="stat-value" id="totalTime">0.0</div>
            </div>

            <div class="stat-item">
                <div class="stat-label">Most Active</div>
                <div class="stat-value" id="mostActive">—</div>
            </div>

            <div class="stat-item">
                <div class="stat-label">Avg Duration (min)</div>
                <div class="stat-value" id="avgDuration">0</div>
            </div>
        </div>
    </section>

    <!-- ===== FILTER & SORT ===== -->
    <section class="card">
        <h2>Recent Activities</h2>

        <div class="filter-controls">
            <div class="filter-group">
                <label for="filterCategory">Filter by:</label>
                <select id="filterCategory">
                    <option value="">All Categories</option>
                    <option value="coding">Coding</option>
                    <option value="design">Design</option>
                    <option value="writing">Writing</option>
                    <option value="meeting">Meeting</option>
                    <option value="learning">Learning</option>
                    <option value="admin">Admin</option>
                    <option value="break">Break</option>
                    <option value="other">Other</option>
                </select>
            </div>

            <div class="filter-group">
                <label for="sortBy">Sort by:</label>
                <select id="sortBy">
                    <option value="recent">Most Recent</option>
                    <option value="oldest">Oldest</option>
                    <option value="duration">Duration (High to Low)</option>
                    <option value="category">Category</option>
                </select>
            </div>

            <button id="exportBtn" class="btn-export">⬇️ Export CSV</button>
        </div>

        <div id="activitiesContainer" class="activities-list">
            <div class="empty-state">
                <div class="empty-state-icon">📌</div>
                <div class="empty-state-title">No activities logged yet</div>
                <div class="empty-state-text">Start logging your activities above to track your productivity.</div>
            </div>
        </div>
    </section>

    <!-- ===== QUICK TIMER ACCESS ===== -->
    <section class="card" style="background: linear-gradient(135deg, rgba(37,99,235,0.08) 0%, rgba(22,163,74,0.08) 100%);">
        <h2>Quick Timer</h2>
        <div style="display: flex; align-items: center; justify-content: space-between; gap: 20px; flex-wrap: wrap;">
            <div id="timerQuickAccess" style="display: flex; align-items: center; gap: 12px;">
                <span style="font-size: 2rem; font-weight: 700; color: #2563eb;">00:00</span>
                <div style="display: flex; gap: 8px;">
                    <button class="btn" id="qStartBtn" style="flex: 0 1 auto; min-width: 70px;">Start</button>
                    <button class="btn" id="qPauseBtn" style="flex: 0 1 auto; min-width: 70px;">Pause</button>
                    <button class="btn" id="qResetBtn" style="flex: 0 1 auto; min-width: 70px;">Reset</button>
                </div>
            </div>
            <a href="index.jsp" class="btn-secondary" style="text-decoration: none; padding: 10px 16px; border-radius: 8px; border: 1px solid #e5e7eb; color: #2563eb; font-weight: 600;">Go to Dashboard</a>
        </div>
    </section>

</main>

<footer class="footer">
    <p>&copy; 2026 Productivity Tracker. All rights reserved.</p>
</footer>

<script>
    // ===== ACTIVITY MANAGER ===== 
    class ActivityManager {
        constructor() {
            this.STORAGE_KEY = 'activities';
            this.load();
        }

        load() {
            try {
                const raw = localStorage.getItem(this.STORAGE_KEY);
                this.activities = raw ? JSON.parse(raw) : [];
            } catch (e) {
                this.activities = [];
            }
        }

        save() {
            try {
                localStorage.setItem(this.STORAGE_KEY, JSON.stringify(this.activities));
            } catch (e) { /* ignore */ }
        }

        add(activity) {
            activity.id = Date.now();
            activity.timestamp = new Date().toISOString();
            this.activities.unshift(activity);
            this.save();
            return activity;
        }

        delete(id) {
            this.activities = this.activities.filter(a => a.id !== id);
            this.save();
        }

        getToday() {
            const today = new Date().toDateString();
            return this.activities.filter(a => new Date(a.timestamp).toDateString() === today);
        }

        getStats(list) {
            if (!list.length) return { total: 0, hours: 0, avg: 0, categories: {} };
            const total = list.length;
            const hours = list.reduce((sum, a) => sum + (a.duration || 0), 0) / 60;
            const avg = Math.round(list.reduce((sum, a) => sum + (a.duration || 0), 0) / total);
            const categories = {};
            list.forEach(a => {
                categories[a.category] = (categories[a.category] || 0) + 1;
            });
            const mostActive = Object.keys(categories).sort((a, b) => categories[b] - categories[a])[0];
            return { total, hours: hours.toFixed(1), avg, mostActive: mostActive || '—' };
        }

        filter(category) {
            if (!category) return this.getToday();
            return this.getToday().filter(a => a.category === category);
        }

        sort(list, sortBy) {
            const copy = [...list];
            switch (sortBy) {
                case 'oldest': return copy.reverse();
                case 'duration': return copy.sort((a, b) => (b.duration || 0) - (a.duration || 0));
                case 'category': return copy.sort((a, b) => (a.category || '').localeCompare(b.category || ''));
                default: return copy;
            }
        }
    }

    const activityMgr = new ActivityManager();

    // ===== UI RENDERING =====
    function renderActivities(list) {
        const container = document.getElementById('activitiesContainer');
        if (!list.length) {
            container.innerHTML = '<div class="empty-state"><div class="empty-state-icon">📌</div><div class="empty-state-title">No activities found</div></div>';
            return;
        }

        const html = list.map(a => {
            const time = new Date(a.timestamp).toLocaleTimeString();
            const cat = a.category || 'other';
            const icons = { coding: '💻', design: '🎨', writing: '✍️', meeting: '📞', learning: '📚', admin: '📋', break: '🌟', other: '📌' };
            return `
                <div class="activity-item fade-in">
                    <div class="activity-header">
                        <div class="activity-title">
                            <span class="activity-icon">${icons[cat] || '📌'}</span>
                            <div>
                                <strong>${a.name || 'Untitled'}</strong>
                                <span class="activity-time">${time}</span>
                            </div>
                        </div>
                        <button class="activity-delete" data-id="${a.id}" title="Delete">✕</button>
                    </div>
                    ${a.notes ? `<div class="activity-notes">${a.notes}</div>` : ''}
                    <div class="activity-meta">
                        <span class="activity-duration">${a.duration || 30}m</span>
                        <span class="activity-category">${cat}</span>
                    </div>
                </div>
            `;
        }).join('');
        container.innerHTML = html;

        // Delete handlers
        container.querySelectorAll('.activity-delete').forEach(btn => {
            btn.addEventListener('click', (e) => {
                const id = parseInt(btn.dataset.id);
                activityMgr.delete(id);
                refresh();
            });
        });
    }

    function updateStats() {
        const today = activityMgr.getToday();
        const stats = activityMgr.getStats(today);
        document.getElementById('totalActivities').textContent = stats.total;
        document.getElementById('totalTime').textContent = stats.hours;
        document.getElementById('avgDuration').textContent = stats.avg;
        document.getElementById('mostActive').textContent = stats.mostActive;
    }

    function refresh() {
        const category = document.getElementById('filterCategory').value;
        const sortBy = document.getElementById('sortBy').value;
        let list = activityMgr.filter(category);
        list = activityMgr.sort(list, sortBy);
        renderActivities(list);
        updateStats();
    }

    // ===== FORM HANDLING =====
    document.getElementById('activityForm').addEventListener('submit', (e) => {
        e.preventDefault();
        const name = document.getElementById('activityName').value.trim();
        const category = document.getElementById('activityCategory').value;
        const duration = parseInt(document.getElementById('activityDuration').value) || 30;
        const notes = document.getElementById('activityNotes').value.trim();

        if (!name || !category) {
            alert('Please fill in all required fields');
            return;
        }

        activityMgr.add({ name, category, duration, notes });
        document.getElementById('activityForm').reset();
        refresh();
    });

    // Quick log from last timer session
    document.getElementById('quickLogBtn').addEventListener('click', () => {
        const SESSIONS_KEY = 'focusTimerState:sessions';
        try {
            const raw = localStorage.getItem(SESSIONS_KEY);
            const sessions = raw ? JSON.parse(raw) : [];
            if (!sessions.length) { alert('No timer sessions found'); return; }
            const lastSession = sessions[0];
            const duration = Math.round(lastSession.duration / 1000 / 60);
            document.getElementById('activityName').focus();
            document.getElementById('activityName').value = 'Focus Session';
            document.getElementById('activityCategory').value = 'coding';
            document.getElementById('activityDuration').value = duration;
            document.getElementById('activityNotes').value = `Auto-logged from timer: ${new Date(lastSession.start).toLocaleTimeString()}`;
        } catch (e) {
            alert('Could not load last session');
        }
    });

    // Filter & sort
    document.getElementById('filterCategory').addEventListener('change', refresh);
    document.getElementById('sortBy').addEventListener('change', refresh);

    // Export CSV
    document.getElementById('exportBtn').addEventListener('click', () => {
        const today = activityMgr.getToday();
        if (!today.length) { alert('No activities to export'); return; }
        const headers = ['Name', 'Category', 'Duration (min)', 'Notes', 'Time'];
        const rows = today.map(a => [
            a.name || '',
            a.category || '',
            a.duration || 30,
            (a.notes || '').replace(/"/g, '""'),
            new Date(a.timestamp).toLocaleTimeString()
        ]);
        const csv = [headers, ...rows].map(r => r.map(v => `"${v}"`).join(',')).join('\n');
        const blob = new Blob([csv], { type: 'text/csv' });
        const url = URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `activities-${new Date().toISOString().split('T')[0]}.csv`;
        a.click();
        URL.revokeObjectURL(url);
    });

    // ===== QUICK TIMER =====
    function initQuickTimer() {
        const display = document.querySelector('#timerQuickAccess span');
        if (!window.TimerModule) return;
        const update = () => {
            const s = window.TimerModule.getState();
            const mins = Math.floor(s.seconds / 60);
            const secs = s.seconds % 60;
            display.textContent = `${mins.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}`;
        };
        setInterval(update, 1000);
        document.getElementById('qStartBtn').addEventListener('click', () => window.TimerModule.start());
        document.getElementById('qPauseBtn').addEventListener('click', () => window.TimerModule.pause());
        document.getElementById('qResetBtn').addEventListener('click', () => window.TimerModule.reset());
    }

    // ===== INIT =====
    document.addEventListener('DOMContentLoaded', () => {
        refresh();
        initQuickTimer();
    });
</script>

<!-- Load timer module -->
<script src="../assets/js/timer.js"></script>
<script>
    window.timerConfig = {
        syncUrl: '/api/timer/sync',
        pollUrl: '/api/timer',
        autoStart: false,
        displayId: 'timer'
    };
</script>
    </script>

    <!-- Register service worker helper and Theme & Accessibility -->
    <script src="../assets/js/register-sw.js"></script>
    <script src="../assets/js/theme.js"></script>
    <script src="../assets/js/accessibility.js"></script>
    <script src="../assets/js/effects.js"></script>

</body>
</html>
