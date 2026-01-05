/* Dark Mode & Theme Management */

class ThemeManager {
    constructor() {
        this.STORAGE_KEY = 'pt-theme-preference';
        this.DEFAULT_THEME = 'auto';
        this.init();
    }

    init() {
        this.loadTheme();
        this.setupToggle();
        this.watchSystemPreference();
    }

    loadTheme() {
        const saved = localStorage.getItem(this.STORAGE_KEY) || this.DEFAULT_THEME;
        this.setTheme(saved);
    }

    setTheme(theme) {
        if (theme === 'auto') {
            const isDark = window.matchMedia('(prefers-color-scheme: dark)').matches;
            document.documentElement.setAttribute('data-theme', isDark ? 'dark' : 'light');
            document.body.className = isDark ? 'dark-mode' : 'light-mode';
            localStorage.setItem(this.STORAGE_KEY, 'auto');
        } else if (theme === 'dark') {
            document.documentElement.setAttribute('data-theme', 'dark');
            document.body.classList.remove('light-mode');
            document.body.classList.add('dark-mode');
            localStorage.setItem(this.STORAGE_KEY, 'dark');
        } else if (theme === 'light') {
            document.documentElement.setAttribute('data-theme', 'light');
            document.body.classList.remove('dark-mode');
            document.body.classList.add('light-mode');
            localStorage.setItem(this.STORAGE_KEY, 'light');
        }
        this.updateToggleIcon();
    }

    setupToggle() {
        // Create toggle button if not exists
        let toggle = document.getElementById('theme-toggle');
        if (!toggle) {
            toggle = document.createElement('button');
            toggle.id = 'theme-toggle';
            toggle.className = 'theme-toggle';
            toggle.type = 'button';
            toggle.setAttribute('aria-label', 'Toggle dark mode');
            toggle.setAttribute('title', 'Toggle dark mode (Press: S+D)');
            document.body.appendChild(toggle);
        }

        toggle.addEventListener('click', () => this.cycleTheme());

        // Keyboard shortcut: Shift+D
        document.addEventListener('keydown', (e) => {
            if (e.shiftKey && e.key === 'D') {
                e.preventDefault();
                this.cycleTheme();
            }
        });
    }

    cycleTheme() {
        const current = localStorage.getItem(this.STORAGE_KEY) || 'auto';
        const themes = ['auto', 'light', 'dark'];
        const next = themes[(themes.indexOf(current) + 1) % themes.length];
        this.setTheme(next);
    }

    updateToggleIcon() {
        const toggle = document.getElementById('theme-toggle');
        if (!toggle) return;
        
        const theme = localStorage.getItem(this.STORAGE_KEY) || 'auto';
        const icons = { auto: '🌓', light: '☀️', dark: '🌙' };
        toggle.textContent = icons[theme];
    }

    watchSystemPreference() {
        if (window.matchMedia) {
            const mq = window.matchMedia('(prefers-color-scheme: dark)');
            mq.addListener(() => {
                const saved = localStorage.getItem(this.STORAGE_KEY);
                if (saved === 'auto') this.setTheme('auto');
            });
        }
    }
}

// Initialize theme manager on DOM ready
document.addEventListener('DOMContentLoaded', () => {
    window.themeManager = new ThemeManager();
});
