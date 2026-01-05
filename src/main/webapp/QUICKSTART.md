# 🎯 Quick Start Guide - Productivity Tracker

## First Time Setup

### 1️⃣ Access the Application
- **URL**: `http://localhost:8080/ProductivityTracker/`
- **Recommended Browsers**: Chrome, Firefox, Safari, or Edge (latest versions)

### 2️⃣ Create Your Account
1. Click **"Get Started Free"** or navigate to `/jsp/register.jsp`
2. Fill in your details:
   - **Email**: Real-time validation ensures it's valid
   - **Username**: Must be unique (checked against server)
   - **Password**: 
     - 🔴 Weak: Too simple
     - 🟡 Fair: Basic security
     - 🟢 Good: Strong password
     - 🟢 Strong: Very secure (green background)
   - **Phone**: Optional, validates format
3. Click **"Create Account"**

### 3️⃣ Log In
1. Visit `/jsp/login.jsp`
2. Enter your credentials
3. Click **"Sign In"**

### 4️⃣ Explore the Dashboard
- **Dashboard** (`/jsp/index.jsp`): Overview of your productivity metrics
- **Tasks** (`/jsp/tasks.jsp`): Manage your daily tasks
- **Activities** (`/jsp/activities.jsp`): Log your work sessions
- **Habits** (`/jsp/habits.jsp`): Track your habit streaks
- **Reports** (`/jsp/reports.jsp`): View detailed analytics

---

## 🎨 Personalizing Your Experience

### Dark Mode
**Automatic**: Your system preference is automatically detected
- **macOS**: System Preferences → General → Appearance
- **Windows**: Settings → Personalization → Colors
- **Linux**: Varies by desktop environment

**Manual Toggle**:
- Click the theme button (☀️/🌙/🌓) in the **top-right corner**
- Or press **Shift + D** on keyboard
- Your preference is saved automatically

### Keyboard Shortcuts
| Action | Shortcut |
|--------|----------|
| Show Help | **?** |
| Toggle Dark Mode | **Shift + D** |
| Start/Pause Timer | **Shift + T** |
| Reset Timer | **Shift + R** |
| Close Modals | **Escape** |
| Navigate Elements | **Tab** / **Shift + Tab** |
| Activate Button | **Enter** or **Space** |
| Shortcuts Panel | **Ctrl + /** |

---

## 📋 Using the Dashboard

### Today's Metrics
- **Productivity Score**: Overall efficiency percentage
- **Tasks**: Completed vs. Total tasks
- **Time Spent**: Total hours logged today
- **Habits**: Streaks and completions
- **Activities**: Number of activities logged

### Quick Actions
1. **Start Timer**: Click the blue timer button
2. **Add Task**: Click "+ Add Task"
3. **Log Activity**: Click "Log Activity" on the timer
4. **View Reports**: Click "View All Reports"

### Timer Features
- ⏱️ **Start/Pause**: Click the timer or press **Shift + T**
- 🔄 **Reset**: Click reset or press **Shift + R**
- 📢 **Notifications**: Browser notification when time's up
- 📝 **Quick Log**: Automatically log this session as activity

---

## ✅ Managing Tasks

### Add a New Task
1. Click **"+ Add Task"** button
2. Fill in task details:
   - **Title**: What needs to be done
   - **Description**: Additional notes
   - **Priority**: High/Medium/Low
   - **Due Date**: When it's due
   - **Category**: Task category
3. Click **"Save Task"**

### Edit a Task
1. Click on the task card
2. Make changes
3. Click **"Update"**

### Complete a Task
- Click the checkbox ✓ on the task card
- Task moves to completed section
- Contributes to productivity score

### Delete a Task
1. Click the ⋮ menu on the task card
2. Click **"Delete"**
3. Confirm deletion

### Filter & Search
- **Filter**: By priority, status, category, or due date
- **Search**: Use the search box to find tasks
- **Sort**: By due date, priority, or creation date

---

## 🎬 Logging Activities

### Quick Log
1. Go to **Activities** page (`/jsp/activities.jsp`)
2. Click **"Quick Log from Timer"** (if you have an active session)
3. Review auto-filled details
4. Add notes if needed
5. Click **"Save Activity"**

### Manual Log
1. Click **"+ New Activity"** button
2. Fill in:
   - **Category**: Work, Break, Learning, etc.
   - **Duration**: Time spent (in minutes)
   - **Notes**: What you did
   - **Date**: When it happened
3. Click **"Save Activity"**

### View & Manage Activities
- **Filter**: By category, date range
- **Sort**: By date, duration, or category
- **Delete**: Click ⋮ menu → Delete
- **Export**: Click **"Export as CSV"** for data analysis

### Statistics
- **Total Time**: Sum of all activities
- **Busiest Category**: What you spend most time on
- **Daily Average**: Time logged per day
- **Weekly Trend**: How your logging changes over time

---

## 🔥 Building Habits

### Create a Habit
1. Go to **Habits** page (`/jsp/habits.jsp`)
2. Click **"+ New Habit"**
3. Enter habit details:
   - **Name**: What habit to build
   - **Description**: Why it's important
   - **Frequency**: Daily/Weekly/Monthly
   - **Goal**: Target completions
4. Click **"Create Habit"**

### Track Daily
- Mark habit as complete for the day
- Streak counter increments automatically
- Your best streak is recorded

### View Progress
- **Current Streak**: Days in a row completed
- **Best Streak**: Longest streak achieved
- **Completion Rate**: % of days completed
- **Chart**: Visual streak history

### Tips for Success
- ✅ Complete at the same time each day
- ✅ Track immediately after completion
- ✅ Don't break the chain!
- ✅ Celebrate milestones (7-day, 30-day streaks)

---

## 📊 Analyzing Your Data

### View Reports
1. Go to **Reports** page (`/jsp/reports.jsp`)
2. Choose analysis period:
   - Last 7 days
   - Last 30 days
   - Last 90 days
   - Custom range

### Key Metrics
- **Productivity Trend**: How your efficiency changes over time
- **Task Completion**: Rate of task completion
- **Time Allocation**: How time is distributed
- **Habit Consistency**: Habit streak performance
- **Peak Hours**: When you're most productive

### Export Data
1. Click **"Export as CSV"**
2. Choose date range
3. Select metrics to include
4. Click **"Download"**
5. Open in Excel, Google Sheets, etc.

### Share Reports
- Take screenshots of charts
- Export PDF (future feature)
- Share insights with team members

---

## 🔐 Account & Privacy

### Update Profile
1. Click your avatar in top-right
2. Click **"Profile"**
3. Update:
   - **Name**: Full name
   - **Email**: Contact email
   - **Avatar**: Profile picture
   - **Bio**: Short description
4. Click **"Save Changes"**

### Change Password
1. Go to **Profile**
2. Click **"Change Password"**
3. Enter current password
4. Enter new password (must meet strength requirements)
5. Confirm new password
6. Click **"Update Password"**

### Privacy Settings
- Your data is encrypted and private
- We never share data with third parties
- You can export or delete all your data anytime
- GDPR compliant

### Logout
- Click your avatar
- Click **"Logout"**
- You'll be redirected to login page

---

## 📱 Mobile & PWA Usage

### Install as App
**iPhone (Safari)**:
1. Tap the **Share** button
2. Tap **"Add to Home Screen"**
3. Name the app
4. Tap **"Add"**
5. App appears on home screen

**Android (Chrome)**:
1. Tap the **⋮** (menu) button
2. Tap **"Install app"** or **"Add to Home Screen"**
3. App appears in app drawer

**Desktop (Chrome/Edge)**:
1. Look for the **install** prompt in address bar
2. Click **"Install"**
3. App opens in window mode

### Offline Access
- Core features work offline
- Changes sync when you're back online
- Activity history is available offline
- Timer works without internet

### Mobile Tips
- 📱 Use landscape mode for charts
- 👆 Tap and hold for additional options
- 🔄 Pull down to refresh
- 🌐 Periodic sync in background

---

## ❓ Frequently Asked Questions

### Q: How is my data stored?
**A**: Your data is stored securely on our servers with AES-256 encryption. Local copies are cached in your browser for offline access.

### Q: Can I use the same account on multiple devices?
**A**: Yes! Your data syncs across all devices automatically. Changes appear instantly.

### Q: Is my productivity data private?
**A**: Absolutely. We never sell or share your data. You own your information completely.

### Q: Can I export my data?
**A**: Yes. Go to Reports → Export as CSV. You can also download all your data from Settings.

### Q: Does it work offline?
**A**: Core features work offline. You can track time, manage tasks, and log activities without internet. Data syncs when you reconnect.

### Q: What browsers are supported?
**A**: Chrome 90+, Firefox 88+, Safari 14+, Edge 90+. Mobile browsers supported too.

### Q: How do I reset my password?
**A**: Go to login page → "Forgot Password" → Enter email → Follow reset link in your inbox.

### Q: Can I use it on my phone?
**A**: Yes! Install as an app (see PWA instructions above) or use the mobile website.

### Q: Is there a teams/collaboration feature?
**A**: Currently in development. Coming in v2.1!

### Q: What's the difference between tasks and activities?
**A**: **Tasks** are things you plan to do. **Activities** are things you actually did (and how long you spent).

### Q: Why is the timer starting instead of pausing?
**A**: Check that JavaScript is enabled. Clear cache and refresh if issues persist.

---

## 🚨 Troubleshooting

### "Page Won't Load"
- ✅ Refresh the page (Cmd+R or Ctrl+R)
- ✅ Clear browser cache (DevTools → Application → Clear storage)
- ✅ Try a different browser
- ✅ Check your internet connection

### "Dark Mode Not Working"
- ✅ Verify `dark-mode.css` is loaded (DevTools → Network)
- ✅ Check system dark mode is enabled
- ✅ Try pressing Shift+D to manually toggle
- ✅ Clear localStorage: `localStorage.clear()`

### "Validations Not Showing"
- ✅ Ensure `validation.js` is loaded
- ✅ Check browser console for errors
- ✅ Refresh the page
- ✅ Disable browser extensions temporarily

### "Timer Not Working"
- ✅ Ensure notifications permission is granted
- ✅ Check browser tab is active (some browsers pause background timers)
- ✅ Refresh the page
- ✅ Check browser supports Service Workers

### "Data Not Syncing Across Tabs"
- ✅ Ensure `theme.js` and `timer.js` are loaded
- ✅ Check both tabs are on the same domain
- ✅ Verify BroadcastChannel is supported (modern browsers only)

### "PWA Installation Not Available"
- ✅ Use Chrome, Edge, or Firefox
- ✅ Ensure you're using HTTPS (in production)
- ✅ Wait a few seconds - install prompt appears after initial load
- ✅ Try adding manually via browser menu

---

## 💡 Pro Tips

### Maximize Productivity
1. **Set realistic goals**: Small, achievable tasks build momentum
2. **Use categories**: Organize activities by project or context
3. **Review daily**: Check your dashboard each morning
4. **Track habits**: Consistency compounds over time
5. **Analyze trends**: Weekly reviews help optimize your workflow

### Get Most From Analytics
- 📊 Use weekly reviews to identify patterns
- 📈 Track your productivity score over 30 days
- 🎯 Compare time allocation to priorities
- 🔥 Focus on habit streaks for motivation

### Keyboard Power User
- Use **Shift+D** to toggle dark mode
- Use **Shift+T/R** for timer control
- Use **Tab** for quick navigation
- Use **?** to see all shortcuts

### Mobile Optimization
- Install as PWA for faster access
- Use offline mode for uninterrupted tracking
- Enable notifications for timer reminders
- Bookmark frequently used pages

---

## 🎓 Learning Resources

### Getting Help
- **Keyboard Shortcuts**: Press **?** on any page
- **Accessibility Features**: Press **Ctrl+/** to see all options
- **Browser Compatibility**: Check DevTools for warnings

### Browser DevTools Tips
- **F12** or **Cmd+Option+I**: Open DevTools
- **Network tab**: Check if files load properly
- **Console tab**: View any error messages
- **Application tab**: Check offline data and service workers

### Keyboard Navigation Testing
- Disable your mouse
- Navigate using only Tab, Enter, and arrow keys
- All features should be accessible

---

## 🌟 What's New?

### Latest Updates
- ✨ Enterprise-grade form validation
- 🌙 System-aware dark mode
- ⚡ 20+ smooth animations
- 📱 PWA with offline support
- ♿ Full accessibility support
- 🔄 Multi-tab real-time sync
- 📊 Advanced analytics

### Coming Soon
- 👥 Team collaboration
- 🤖 AI-powered insights
- 📲 Native mobile apps
- 🔗 Integrations (Slack, Calendar, Jira)
- 🎯 Advanced goal tracking

---

## 📞 Support

Need help? 
- 📧 Email: support@productivitytracker.com
- 💬 Chat: Available in-app (future)
- 📚 Docs: Full documentation at `/README.md`
- 🐛 Report Issues: Send feedback from Settings → Help

---

**Version**: 2.0 Enterprise Edition
**Last Updated**: 2026
**Status**: Production Ready ✓

Happy tracking! 🚀
