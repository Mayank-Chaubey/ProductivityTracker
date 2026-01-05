# 🧪 Testing Guide - Productivity Tracker

## Quick Test Checklist

### ✅ Core Functionality
- [ ] Dashboard loads with metrics
- [ ] Timer starts/pauses/resets correctly
- [ ] Tasks can be created, edited, deleted
- [ ] Activities can be logged and filtered
- [ ] Habits can be tracked with streaks
- [ ] Reports show correct data

### ✅ Theme & Dark Mode
- [ ] Light mode loads by default
- [ ] Shift+D toggles dark mode
- [ ] Dark mode persists on reload
- [ ] System preference auto-detected
- [ ] All text has proper contrast in both modes
- [ ] Icons/images visible in both modes

### ✅ Animations & UX
- [ ] Page transitions are smooth
- [ ] Button hover/click effects work
- [ ] Loading states show animations
- [ ] Success/error messages appear smoothly
- [ ] Forms show validation feedback
- [ ] Timer has pulsing glow effect

### ✅ Keyboard Navigation
- [ ] Tab navigates all interactive elements
- [ ] Shift+Tab goes backward
- [ ] Enter/Space activates buttons
- [ ] Escape closes modals
- [ ] Focus indicators are visible
- [ ] ? shows help menu

### ✅ Accessibility
- [ ] Screen reader announces page title
- [ ] Form labels read correctly
- [ ] Buttons have accessible names
- [ ] Images have alt text
- [ ] Color not the only indicator
- [ ] High contrast mode works

### ✅ PWA Features
- [ ] Install prompt appears in Chrome
- [ ] Works offline (core features)
- [ ] Data persists in offline mode
- [ ] Syncs when back online
- [ ] Service Worker registered
- [ ] Theme persists across sessions

### ✅ Responsive Design
- [ ] Works on 320px width (mobile)
- [ ] Works on 768px width (tablet)
- [ ] Works on 1920px width (desktop)
- [ ] Touch targets are 44px minimum
- [ ] Landscape and portrait modes work
- [ ] No horizontal scroll needed

### ✅ Performance
- [ ] Page loads in < 3 seconds
- [ ] Timer updates are smooth
- [ ] No lag when typing
- [ ] Charts render quickly
- [ ] No memory leaks (check DevTools)
- [ ] Service Worker caches effectively

### ✅ Cross-Browser
- [ ] Chrome/Chromium: Full features
- [ ] Firefox: Full features
- [ ] Safari: Full features (except some PWA)
- [ ] Edge: Full features
- [ ] Mobile browsers: Core features

---

## Detailed Testing Procedures

### 1. Dark Mode Testing

#### Automatic Detection
```bash
# macOS
System Preferences → General → Appearance → Dark

# Windows
Settings → Personalization → Colors → Dark

# Linux
Varies by desktop environment
```
**Expected**: App automatically switches to dark mode

#### Manual Toggle
```
Steps:
1. Click theme button (☀️/🌙/🌓) in top-right
2. Verify UI updates immediately
3. Reload page
4. Verify theme persists
5. Press Shift+D
6. Verify theme toggles
```

#### Contrast Verification
```javascript
// In browser console
// Check WCAG AA compliance (4.5:1 for text)

// Light mode
getComputedStyle(document.querySelector('button')).backgroundColor
// Should be bright with dark text

// Dark mode
getComputedStyle(document.querySelector('button')).backgroundColor
// Should be dark with bright text
```

### 2. Keyboard Navigation Testing

#### Setup
```
1. Disable mouse trackpad/mouse
2. Only use keyboard
```

#### Test Plan
```
Tab through all elements:
- Buttons should receive focus (outline visible)
- Inputs should receive focus
- Links should receive focus
- Form should be submittable via Enter
- Modals should be closeable via Escape

Navigation order should be:
1. Header elements
2. Main content
3. Footer
```

#### Keyboard Shortcuts
```
Test each shortcut:
? → Opens help menu
Escape → Closes help menu
Shift+D → Toggles dark mode
Shift+T → Toggles timer
Shift+R → Resets timer
Ctrl+/ → Opens shortcuts panel
```

### 3. Accessibility Testing

#### Screen Reader (NVDA on Windows)
```
1. Download NVDA: https://www.nvaccess.org/
2. Start NVDA
3. Navigate with arrow keys
4. Verify all text is readable
5. Verify buttons have accessible names
6. Verify forms have labels
7. Verify dynamic content is announced
```

#### Screen Reader (JAWS)
```
1. Open JAWS
2. Use virtual cursor (arrow keys)
3. Use focus mode (Enter on buttons)
4. Navigate headings: H key
5. Navigate form fields: F key
6. Verify announcements make sense
```

#### Screen Reader (VoiceOver on Mac)
```
1. Enable: System Preferences → Accessibility → VoiceOver
2. Start typing VO (Control+Option)
3. Navigate with arrow keys
4. Interact with elements: VO+Space
5. Read form fields: VO+Down Arrow
6. Verify text is readable
```

### 4. PWA Testing

#### Installation (Chrome)
```
1. Open application in Chrome
2. Wait for install prompt (top-right icon)
3. Click "Install"
4. Verify app opens in app window
5. Verify app appears in taskbar
6. Close and reopen from app launcher
```

#### Offline Mode
```
1. Open DevTools (F12)
2. Network tab → Throttle → Offline
3. Navigate to Activities page
4. Verify page loads
5. Verify activities load (from cache)
6. Try to log new activity (should queue)
7. Go back online
8. Verify activity syncs
9. Verify no duplicate entries
```

#### Service Worker
```
1. Open DevTools → Application
2. Check Service Workers
3. Verify "sw.js" is registered and active
4. Check Cache Storage
5. Verify assets are cached
6. Check IndexedDB (if used)
7. Test unregistering and re-registering
```

### 5. Form Validation Testing

#### Email Validation
```
Valid emails:
- user@example.com ✓
- john.doe@company.co.uk ✓
- test+tag@example.com ✓

Invalid emails:
- user@example ✗
- @example.com ✗
- user @example.com ✗
```

#### Password Strength
```
Test cases:
1. "pass" → Weak (red)
2. "Password1" → Good (green)
3. "SecureP@ss123" → Strong (dark green)

Verify:
- Strength meter updates in real-time
- Correct color displayed
- Label shows strength level
```

#### Username Availability
```
Steps:
1. Type username slowly
2. After 3 characters, async check triggers
3. Observe loading state
4. Wait for server response
5. Verify error/success message
```

#### Real-time Feedback
```
1. Click email field
2. Type invalid email
3. Error appears immediately (red)
4. Fix the email
5. Error disappears (green)

Verify debounce works (300ms delay)
```

### 6. Responsive Design Testing

#### Mobile (320px)
```
1. DevTools → Device Toolbar
2. Set width to 320px
3. Verify:
   - No horizontal scroll
   - Text is readable
   - Buttons are tappable (44px+)
   - Menu works (hamburger or collapse)
   - Images scale properly
```

#### Tablet (768px)
```
1. Set width to 768px
2. Verify:
   - Two-column layout works
   - Grids adapt properly
   - Touch targets adequate
   - Landscape mode works
```

#### Desktop (1920px)
```
1. Maximize browser
2. Verify:
   - Multi-column layout
   - Maximum width constraint
   - Charts display properly
   - No excessive whitespace
```

#### Touch Testing (Mobile Device)
```
1. Access on actual phone/tablet
2. Test:
   - Tap buttons (no misclicks)
   - Swipe gestures (if implemented)
   - Pinch zoom (if enabled)
   - Portrait/Landscape rotation
```

### 7. Performance Testing

#### Load Time
```
1. Open DevTools → Lighthouse
2. Run audit for Performance
3. Check metrics:
   - First Contentful Paint (FCP) < 2s
   - Largest Contentful Paint (LCP) < 2.5s
   - Cumulative Layout Shift (CLS) < 0.1
   - Total Blocking Time (TBT) < 200ms
```

#### Memory Leaks
```
1. DevTools → Memory
2. Take heap snapshot
3. Perform repetitive actions (100x):
   - Open/close modals
   - Switch pages
   - Toggle theme
4. Take another snapshot
5. Compare snapshots (should be similar)
```

#### Animation Performance
```
1. DevTools → Performance
2. Record while:
   - Timer is running
   - Switching themes
   - Scrolling activities
3. Check FPS (should be 60 FPS)
4. Check for frame drops
```

### 8. Cross-Browser Testing

#### Chrome/Chromium
```bash
# Test installation prompt
# Test Service Worker
# Test advanced PWA features
# Expected: All features work
```

#### Firefox
```bash
# Test form validation
# Test keyboard navigation
# Test accessibility
# Note: Some PWA features limited
```

#### Safari (macOS)
```bash
# Test scrolling performance
# Test animations
# Note: PWA install differs
# Expected: All core features work
```

#### Safari (iOS)
```
Steps:
1. Open in iOS Safari
2. Tap Share → Add to Home Screen
3. Test offline mode
4. Test form validation
5. Note: Limited PWA support
```

#### Edge
```bash
# Test is same as Chrome (Chromium-based)
# Test all PWA features
# Test theme sync
```

### 9. Timer Testing

#### Start/Pause/Reset
```
1. Click start
2. Verify timer counts down
3. Click pause
4. Verify it stops
5. Click resume
6. Verify it continues
7. Click reset
8. Verify it goes to 00:00
```

#### Notifications
```
1. Grant notification permission
2. Start short timer (10 seconds)
3. Wait for completion
4. Verify browser notification appears
5. Click notification
6. Verify app is focused
```

#### Multi-Tab Sync
```
1. Open app in 2 browser tabs
2. Start timer in tab 1
3. Switch to tab 2
4. Verify timer is running in tab 2
5. Pause in tab 2
6. Switch to tab 1
7. Verify timer is paused in tab 1
```

### 10. Data Sync Testing

#### Real-time Updates
```
1. Open app in 2 tabs
2. In tab 1: Add a task
3. Check tab 2: Task appears immediately
4. In tab 2: Complete the task
5. Check tab 1: Task shows as complete
```

#### Offline to Online
```
1. Go offline (DevTools)
2. Add task
3. Log activity
4. Go back online
5. Verify changes sync
6. Check no duplicates
```

---

## Test Case Templates

### Template: Feature Test
```
Feature: [Feature Name]
Scenario: [Scenario Description]

Given: [Initial state]
When: [User action]
Then: [Expected result]

Example:
Feature: Dark Mode Toggle
Scenario: User toggles dark mode via keyboard

Given: User is on dashboard in light mode
When: User presses Shift+D
Then: UI switches to dark mode and preference is saved
```

### Template: Accessibility Test
```
Feature: [Feature Name]
Accessibility Requirement: [WCAG Criterion]

Steps:
1. [Step]
2. [Step]

Expected:
- Screen reader announces [text]
- Keyboard navigation works
- Focus is visible
- Color contrast is 4.5:1
```

---

## Tools & Resources

### Browser DevTools
- **Lighthouse Audit**: Performance, Accessibility, Best Practices
- **Accessibility Tree**: DevTools → Accessibility panel
- **Color Contrast**: Inspect element → Colors
- **Performance Monitor**: DevTools → Rendering

### Accessibility Testing Tools
- **NVDA**: Free screen reader for Windows
- **JAWS**: Premium screen reader (trial available)
- **VoiceOver**: Built-in Mac screen reader
- **axe DevTools**: Chrome extension for accessibility audits
- **WAVE**: Web accessibility evaluation tool

### Performance Testing Tools
- **Lighthouse**: Built into Chrome DevTools
- **WebPageTest**: https://www.webpagetest.org/
- **GTmetrix**: https://gtmetrix.com/
- **Google PageSpeed Insights**: https://pagespeed.web.dev/

### PWA Testing
- **Chrome DevTools**: Application tab
- **PWA Builder**: https://www.pwabuilder.com/
- **Lighthouse PWA Audit**: Chrome DevTools > Lighthouse

### Cross-Browser Testing
- **BrowserStack**: https://www.browserstack.com/
- **CrossBrowserTesting**: https://crossbrowsertesting.com/
- **Responsively**: https://responsively.app/

---

## Known Issues & Workarounds

### Issue: Dark mode not persisting
**Workaround**: Clear localStorage and try again
```javascript
localStorage.clear();
location.reload();
```

### Issue: Timer not working offline
**Workaround**: Ensure Service Worker is registered
```javascript
navigator.serviceWorker.getRegistrations().then(regs => {
    if (regs.length === 0) {
        navigator.serviceWorker.register('/assets/js/sw.js');
    }
});
```

### Issue: Form validation errors not showing
**Workaround**: Verify JavaScript is enabled and validation.js is loaded
```javascript
// Check if FormValidator exists
if (window.FormValidator) {
    console.log('Validation loaded');
} else {
    console.error('Validation not loaded');
}
```

### Issue: Accessibility announcements not working
**Workaround**: Check if screen reader is active
```javascript
// Manually test with window.announceMessage
window.announceMessage('Test announcement');
```

---

## Continuous Testing

### Before Each Release
1. ✅ Run Lighthouse audit (target: 90+ score)
2. ✅ Test keyboard navigation (full app)
3. ✅ Test screen reader (at least one)
4. ✅ Test offline mode
5. ✅ Test all major browsers
6. ✅ Test on mobile device
7. ✅ Run unit tests
8. ✅ Check console for errors

### Post-Deployment
1. ✅ Monitor error logs
2. ✅ Check real user metrics (RUM)
3. ✅ Test in production environment
4. ✅ Verify PWA installation
5. ✅ Check analytics

---

## Test Coverage Goals

- **Functionality**: 100% of features tested
- **Accessibility**: WCAG 2.1 AA compliance
- **Performance**: 90+ Lighthouse score
- **Browser Support**: Latest 2 versions of major browsers
- **Mobile**: iOS Safari and Android Chrome

---

**Version**: 2.0 Enterprise Edition
**Last Updated**: 2026
**Status**: Production Ready ✓
