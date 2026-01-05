# 🔧 Developer Documentation - Productivity Tracker API

## Table of Contents
1. [JavaScript Modules](#javascript-modules)
2. [CSS System](#css-system)
3. [API Integration](#api-integration)
4. [Custom Events](#custom-events)
5. [Best Practices](#best-practices)

---

## JavaScript Modules

### ThemeManager (`theme.js`)
Manages light/dark theme switching with system preference detection.

#### Usage
```javascript
// Automatically initialized on DOMContentLoaded
window.themeManager  // Global access

// Set theme (auto/light/dark)
themeManager.setTheme('dark');

// Cycle through themes
themeManager.cycleTheme(); // auto → light → dark → auto

// Check current theme
localStorage.getItem('pt-theme-preference'); // Returns: 'auto', 'light', 'dark'
```

#### Events
```javascript
// Listen for theme changes
document.addEventListener('DOMContentLoaded', () => {
    console.log('Current theme:', localStorage.getItem('pt-theme-preference'));
});
```

#### CSS Variables Available
```css
/* Light Mode (default) */
:root {
    --bg-primary: #ffffff;
    --bg-secondary: #f9fafb;
    --text-primary: #1f2937;
    --text-secondary: #6b7280;
    --accent-primary: #2563eb;
    --accent-secondary: #16a34a;
    --accent-danger: #dc2626;
    --border-color: #e5e7eb;
    --shadow-sm: 0 1px 2px 0 rgba(0, 0, 0, 0.05);
    --shadow-md: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
    --shadow-lg: 0 10px 15px -3px rgba(0, 0, 0, 0.1);
}

/* Dark Mode (auto via @media prefers-color-scheme: dark) */
@media (prefers-color-scheme: dark) {
    :root {
        --bg-primary: #111827;
        --bg-secondary: #1f2937;
        --text-primary: #f3f4f6;
        /* ... */
    }
}

/* Manual override */
.dark-mode {
    --bg-primary: #111827;
    /* ... */
}
```

---

### AccessibilityManager (`accessibility.js`)
Provides keyboard navigation, focus management, and accessible announcements.

#### Usage
```javascript
// Automatically initialized on DOMContentLoaded
window.a11y  // Global access

// Make announcement to screen readers
window.announceMessage('Task saved successfully', 'assertive');

// Success/Error/Info announcements
window.a11y.announceSuccess('Profile updated');
window.a11y.announceError('Failed to save changes');
window.a11y.announceInfo('15 tasks due today');
```

#### Keyboard Shortcuts
```javascript
// Already configured globally:
// ? - Show shortcuts help
// Escape - Close modals
// Tab - Navigate elements
// Ctrl+/ - Toggle shortcuts panel
```

#### ARIA Live Region
```html
<!-- Automatically created -->
<div id="accessibility-announcements" role="status" aria-live="polite"></div>

<!-- Announcements are read by screen readers -->
```

#### Skip Links
```html
<!-- Link is auto-created on page load -->
<a href="#main">Skip to main content</a>
```

#### Focus Management
```javascript
// Focus indicators show keyboard navigation
// .focus-visible automatically applied when using keyboard

// CSS handles it:
button:focus-visible {
    outline: 2px solid var(--accent-primary);
    outline-offset: 2px;
}
```

---

### FormValidator (`validation.js`)
Real-time form validation with async checks and password strength.

#### Usage
```javascript
// Auto-initialize on all forms at DOMContentLoaded
// Uses form data-validate="true" attribute

// Manual initialization
const validator = new FormValidator(formElement);

// Validate single field
validator.validateField(fieldName);

// Validate entire form
const isValid = validator.validateForm();

// Get validation errors
const errors = validator.getErrors(); // Returns: { fieldName: 'Error message', ... }
```

#### Validation Rules
```javascript
// Available built-in validators:
ValidationRules.email   // RFC 5322 email validation
ValidationRules.username // 3-32 alphanumeric + underscore
ValidationRules.password // At least 8 chars with uppercase, lowercase, digit
ValidationRules.phone  // International format
ValidationRules.url    // HTTP/HTTPS URLs

// Add custom rule:
ValidationRules.customField = /your-regex/;
```

#### Password Strength Meter
```javascript
// Update password strength meter
validator.updatePasswordStrength(passwordValue);

// Returns strength level:
// 0: Weak (red)
// 1: Fair (orange)
// 2: Good (green)
// 3: Strong (dark green)

// Visual indicator updates automatically
// Color bar: <div class="strength-meter"></div>
// Label: <span class="strength-label"></span>
```

#### Async Validation
```javascript
// Check username availability (mock)
validator.checkUsernameAvailability('username');

// In production, modify:
// fetch('/api/check-username?username=' + username)
```

#### Form HTML
```html
<form data-validate="true">
    <div class="form-group">
        <label for="email">Email</label>
        <input 
            type="email" 
            id="email" 
            name="email"
            required
            pattern="[email-pattern]"
            data-validator="email"
        >
        <span class="error-message"></span>
    </div>

    <div class="form-group">
        <label for="password">Password</label>
        <input 
            type="password" 
            id="password" 
            name="password"
            data-validator="password"
        >
        <div class="strength-meter"></div>
        <span class="strength-label"></span>
    </div>
</form>
```

---

### TimerModule (`timer.js`)
Enterprise timer with notifications, multi-tab sync, and session logging.

#### Usage
```javascript
// Automatically initialized at DOMContentLoaded
window.TimerModule  // Global access

// Start timer (seconds)
TimerModule.start(1500); // 25 minutes Pomodoro

// Pause/Resume
TimerModule.pause();
TimerModule.resume();

// Reset
TimerModule.reset();

// Get current state
const state = TimerModule.getState();
// Returns: { isRunning, remaining, total, session }

// Request notifications
TimerModule.requestNotificationPermission();

// Sync across tabs
TimerModule.syncNow();
```

#### Browser Notifications
```javascript
// Auto-enabled after permission
// Shows notification when timer completes
// Click notification to focus app

// Events triggered:
// 'timerStart' - Timer started
// 'timerPause' - Timer paused
// 'timerReset' - Timer reset
// 'timerComplete' - Timer finished
```

#### Multi-Tab Sync
```javascript
// Changes sync instantly via BroadcastChannel
// All open tabs show same timer state
// Browser localStorage for persistence

// Check sync status:
TimerModule.getState().synced // true/false
```

#### Fallback Methods
```javascript
// Tries in order:
// 1. BroadcastChannel (modern browsers)
// 2. Server-Sent Events (WebSocket alternative)
// 3. Polling (fallback, every 5 seconds)
```

---

### ActivityManager (embedded in `activities.jsp`)
Manages activity logging with filtering, sorting, and export.

#### Usage
```javascript
// Available after activities.jsp loads
window.ActivityManager  // Global access

// Add activity
ActivityManager.add({
    category: 'work',
    duration: 45,
    notes: 'Developed timer feature',
    date: new Date()
});

// Delete activity
ActivityManager.delete(activityId);

// Get all activities
const activities = ActivityManager.getAll();

// Filter
const filtered = ActivityManager.filter({
    category: 'work',
    dateFrom: new Date('2024-01-01'),
    dateTo: new Date('2024-01-31')
});

// Sort
const sorted = ActivityManager.sort('duration', 'desc');

// Get statistics
const stats = ActivityManager.getStats();
// Returns: { totalTime, avgDaily, busyCategory, thisWeek }

// Export to CSV
ActivityManager.exportCSV();
```

#### Activity Object
```javascript
{
    id: 'uuid',
    category: 'work|learning|break|personal',
    duration: 45, // minutes
    notes: 'What I did',
    date: '2024-01-15',
    time: '14:30',
    created: Date
}
```

---

### DashboardModule (`dashboard.js`)
Real-time dashboard metrics with SSE/WS/polling fallback.

#### Usage
```javascript
// Initialize with server endpoint
DashboardModule.init({
    apiEndpoint: '/api/dashboard/metrics',
    streamEndpoint: '/streams/dashboard',
    fallbackInterval: 5000 // ms
});

// Get current metrics
const metrics = DashboardModule.getMetrics();

// Listen for updates
DashboardModule.on('metricsUpdate', (metrics) => {
    console.log('New metrics:', metrics);
});

// Reconnect after connection loss
DashboardModule.reconnect();

// Force update
DashboardModule.refresh();
```

#### Metrics Object
```javascript
{
    productivityScore: 75,
    tasksCompleted: 8,
    tasksTotal: 12,
    timeSpent: 420, // minutes
    habitsCompleted: 5,
    habitsTotal: 7,
    activitiesLogged: 12
}
```

---

### ChartsModule (`charts.js`)
Analytics and visualization with live data streaming.

#### Usage
```javascript
// Initialize charts
ChartsModule.init({
    apiEndpoint: '/api/charts/data',
    elements: {
        productivity: '#productivity-chart',
        timeAllocation: '#time-allocation-chart',
        habits: '#habits-chart'
    }
});

// Render chart
ChartsModule.render('productivity', {
    type: 'line',
    period: 'monthly'
});

// Update chart data
ChartsModule.update('productivity', newData);

// Export chart as image
ChartsModule.export('productivity', 'png');
```

---

### ServiceWorker (`sw.js`)
Offline caching with intelligent cache strategy.

#### Strategy
```
Network-First for APIs:
1. Try network request
2. Fall back to cache if offline
3. Cache response for next time

Cache-First for Assets:
1. Check cache first
2. If not found, fetch from network
3. Cache for offline use
```

#### Cached Files
```
/assets/css/dashboard.css
/assets/css/timer.css
/assets/css/animations.css
/assets/css/dark-mode.css
/assets/js/timer.js
/assets/js/dashboard.js
/assets/js/charts.js
/assets/js/validation.js
/assets/js/theme.js
/assets/js/accessibility.js
```

#### Listening to Service Worker
```javascript
// Check registration status
navigator.serviceWorker.getRegistrations().then(regs => {
    regs.forEach(reg => {
        console.log('SW Scope:', reg.scope);
        console.log('SW State:', reg.installing ? 'installing' : 
                                reg.waiting ? 'waiting' : 
                                reg.active ? 'active' : 'unknown');
    });
});

// Listen for updates
navigator.serviceWorker.ready.then(reg => {
    reg.addEventListener('controllerchange', () => {
        console.log('Service Worker updated');
    });
});
```

---

## CSS System

### Custom Properties (Variables)

#### Light Mode (Default)
```css
:root {
    /* Colors */
    --bg-primary: #ffffff;
    --bg-secondary: #f9fafb;
    --text-primary: #1f2937;
    --text-secondary: #6b7280;
    --text-tertiary: #9ca3af;
    --accent-primary: #2563eb;
    --accent-secondary: #16a34a;
    --accent-danger: #dc2626;
    --accent-warning: #f59e0b;
    --border-color: #e5e7eb;
    
    /* Shadows */
    --shadow-sm: 0 1px 2px 0 rgba(0, 0, 0, 0.05);
    --shadow-md: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
    --shadow-lg: 0 10px 15px -3px rgba(0, 0, 0, 0.1);
    --shadow-xl: 0 20px 25px -5px rgba(0, 0, 0, 0.1);
    
    /* Transitions */
    --transition-fast: 0.12s ease-in-out;
    --transition-normal: 0.25s ease-in-out;
    --transition-slow: 0.6s ease-in-out;
    
    /* Spacing */
    --spacing-xs: 4px;
    --spacing-sm: 8px;
    --spacing-md: 16px;
    --spacing-lg: 24px;
    --spacing-xl: 32px;
    
    /* Border Radius */
    --radius-sm: 4px;
    --radius-md: 8px;
    --radius-lg: 12px;
    --radius-full: 9999px;
}
```

#### Dark Mode
```css
@media (prefers-color-scheme: dark) {
    :root {
        --bg-primary: #111827;
        --bg-secondary: #1f2937;
        --text-primary: #f3f4f6;
        --text-secondary: #d1d5db;
        --text-tertiary: #9ca3af;
        --accent-primary: #3b82f6;
        --accent-secondary: #4ade80;
        --accent-danger: #f87171;
        --accent-warning: #facc15;
        --border-color: #374151;
        /* Shadows adjusted for dark background */
        --shadow-md: 0 4px 6px -1px rgba(0, 0, 0, 0.3);
        /* ... */
    }
}
```

#### Manual Override
```css
body.dark-mode {
    /* Same as @media (prefers-color-scheme: dark) */
}

body.light-mode {
    /* Force light mode regardless of system preference */
}
```

### Using Variables in CSS
```css
.button {
    background-color: var(--accent-primary);
    color: white;
    border-radius: var(--radius-md);
    box-shadow: var(--shadow-md);
    transition: background-color var(--transition-normal);
}
```

### Using Variables in JavaScript
```javascript
// Get variable value
const primaryColor = getComputedStyle(document.documentElement)
    .getPropertyValue('--accent-primary').trim();

// Set variable value
document.documentElement.style.setProperty('--accent-primary', '#ff6b6b');
```

---

## API Integration

### Expected Backend Endpoints

#### Dashboard Metrics
```
GET /api/dashboard/metrics
Response: {
    productivityScore: 75,
    tasksCompleted: 8,
    tasksTotal: 12,
    timeSpent: 420,
    habitsCompleted: 5,
    habitsTotal: 7,
    activitiesLogged: 12
}
```

#### Stream Endpoint (SSE)
```
GET /streams/dashboard
Content-Type: text/event-stream

data: {"productivityScore": 75, ...}\n\n
data: {"productivityScore": 76, ...}\n\n
```

#### Charts Data
```
GET /api/charts/data?period=monthly
Response: {
    productivity: [75, 76, 74, 78, 80, ...],
    timeAllocation: { work: 60, break: 20, learn: 20 },
    habits: { habit1: 28, habit2: 30, habit3: 25 }
}
```

#### Check Username Availability
```
GET /api/check-username?username=john_doe
Response: { available: true }
```

#### Timer Sync
```
GET /api/timer/sync
Response: {
    isRunning: true,
    remaining: 1200,
    total: 1500,
    sessionId: 'uuid'
}
```

---

## Custom Events

### Window Events
```javascript
// Theme changed
window.addEventListener('theme-changed', (e) => {
    console.log('New theme:', e.detail.theme);
});

// Timer state changed
window.addEventListener('timer-state-changed', (e) => {
    console.log('Timer state:', e.detail);
});

// Form validation error
window.addEventListener('form-validation-error', (e) => {
    console.log('Field:', e.detail.field, 'Error:', e.detail.message);
});

// Accessibility announcement
window.addEventListener('a11y-announce', (e) => {
    console.log('Announcement:', e.detail.message);
});
```

### Dispatch Custom Events
```javascript
// Theme changed
window.dispatchEvent(new CustomEvent('theme-changed', {
    detail: { theme: 'dark' }
}));

// Timer update
window.dispatchEvent(new CustomEvent('timer-state-changed', {
    detail: {
        isRunning: true,
        remaining: 1200,
        total: 1500
    }
}));
```

---

## Best Practices

### JavaScript
```javascript
// ✅ DO: Use const/let, avoid var
const timer = new TimerModule();

// ✅ DO: Use debounce for real-time validation
function debounce(fn, delay) {
    let timeout;
    return (...args) => {
        clearTimeout(timeout);
        timeout = setTimeout(() => fn(...args), delay);
    };
}

// ✅ DO: Check browser support
if ('serviceWorker' in navigator) {
    navigator.serviceWorker.register('sw.js');
}

// ✅ DO: Handle errors gracefully
try {
    await fetch('/api/endpoint');
} catch (error) {
    window.announceError('Connection failed. Using cached data.');
}

// ❌ DON'T: Use global variables
// ❌ DON'T: Modify DOM directly without checking
// ❌ DON'T: Ignore console errors
```

### CSS
```css
/* ✅ DO: Use CSS variables for theming */
color: var(--text-primary);

/* ✅ DO: Support dark mode */
@media (prefers-color-scheme: dark) { /* ... */ }

/* ✅ DO: Respect motion preferences */
@media (prefers-reduced-motion: reduce) {
    * { animation: none !important; }
}

/* ✅ DO: Use proper contrast ratios */
/* WCAG AA: 4.5:1 for text, 3:1 for graphics */

/* ❌ DON'T: Use inline styles */
/* ❌ DON'T: Hardcode colors (use variables) */
/* ❌ DON'T: Remove focus indicators */
```

### Accessibility
```javascript
// ✅ DO: Add ARIA labels
<button aria-label="Close dialog">×</button>

// ✅ DO: Use semantic HTML
<button>Action</button>  // not <div onclick="...">

// ✅ DO: Announce dynamic changes
window.announceMessage('3 tasks added');

// ✅ DO: Test keyboard navigation only
// Disable mouse and navigate with Tab

// ❌ DON'T: Rely on color alone
// ❌ DON'T: Use low contrast text
// ❌ DON'T: Set tabindex > 0
```

### Performance
```javascript
// ✅ DO: Lazy load images
<img loading="lazy" src="...">

// ✅ DO: Debounce scroll/resize events
window.addEventListener('resize', debounce(() => { /* ... */ }, 250));

// ✅ DO: Use CSS transforms instead of layout changes
transform: translateX(10px);  // Fast
left: 10px;                   // Slow

// ✅ DO: Minimize DOM reflows
// Batch DOM updates together

// ❌ DON'T: Poll for data constantly
// ❌ DON'T: Load all assets immediately
// ❌ DON'T: Create memory leaks (listeners, intervals)
```

---

## Deployment Checklist

- [ ] Minify CSS/JS for production
- [ ] Set HTTPS for PWA features
- [ ] Configure CORS for API endpoints
- [ ] Enable gzip compression
- [ ] Set proper cache headers
- [ ] Test Service Worker registration
- [ ] Verify all redirects work
- [ ] Test PWA installation on mobile
- [ ] Run Lighthouse audit
- [ ] Test keyboard navigation
- [ ] Test screen reader compatibility
- [ ] Test on multiple browsers
- [ ] Monitor error logs

---

## Resources

- [MDN Web Docs](https://developer.mozilla.org/)
- [Web.dev Best Practices](https://web.dev/)
- [WCAG 2.1 Guidelines](https://www.w3.org/WAI/WCAG21/quickref/)
- [PWA Checklist](https://web.dev/pwa-checklist/)
- [CSS Variables Spec](https://www.w3.org/TR/css-variables-1/)

---

**Version**: 2.0 Enterprise Edition
**Last Updated**: 2026
**Status**: Production Ready ✓
