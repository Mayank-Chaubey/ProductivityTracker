/* Accessibility & Keyboard Navigation Manager */

class AccessibilityManager {
    constructor() {
        this.setupKeyboardNavigation();
        this.setupFocusIndicators();
        this.setupAriaLive();
        this.setupSkipLinks();
        this.setupAnnouncements();
    }

    setupKeyboardNavigation() {
        // Global keyboard shortcuts help
        const shortcuts = {
            '?': () => this.showShortcutsHelp(),
            'Escape': () => this.closeFocusedModals(),
            'Tab': () => this.enableFocusTrap(),
            'Control+/': () => this.toggleShortcutPanel()
        };

        document.addEventListener('keydown', (e) => {
            // Enable focus visible class when Tab is pressed
            if (e.key === 'Tab') {
                document.body.classList.add('keyboard-nav');
                document.addEventListener('mousedown', () => {
                    document.body.classList.remove('keyboard-nav');
                }, { once: true });
            }

            // Global shortcuts
            if (e.key === '?') {
                e.preventDefault();
                this.showShortcutsHelp();
            }
            if (e.key === 'Escape') {
                e.preventDefault();
                this.closeFocusedModals();
            }
        });

        // Skip to main content link
        this.createSkipLink();
    }

    setupFocusIndicators() {
        // CSS-based focus indicators are in dark-mode.css and animations.css
        // This just ensures they're active
        const style = document.createElement('style');
        style.textContent = `
            /* Enhanced focus indicators */
            button:focus-visible, 
            input:focus-visible, 
            select:focus-visible, 
            textarea:focus-visible, 
            a:focus-visible {
                outline: 2px solid var(--accent-primary, #2563eb);
                outline-offset: 2px;
                box-shadow: 0 0 0 4px rgba(37, 99, 235, 0.1);
            }
            
            .keyboard-nav *:focus-visible {
                outline: 2px dashed var(--accent-primary, #2563eb);
            }
        `;
        document.head.appendChild(style);
    }

    setupAriaLive() {
        // Create live region for announcements
        if (!document.querySelector('[role="status"][aria-live="polite"]')) {
            const liveRegion = document.createElement('div');
            liveRegion.id = 'accessibility-announcements';
            liveRegion.setAttribute('role', 'status');
            liveRegion.setAttribute('aria-live', 'polite');
            liveRegion.setAttribute('aria-atomic', 'true');
            liveRegion.style.cssText = 'position:absolute; left:-10000px; width:1px; height:1px; overflow:hidden;';
            document.body.appendChild(liveRegion);
        }
    }

    setupSkipLinks() {
        // Skip links are created per page; this ensures they're functional
        const skipLinks = document.querySelectorAll('a[href="#main"], a[href="#content"]');
        skipLinks.forEach(link => {
            link.addEventListener('click', (e) => {
                const target = document.querySelector(link.getAttribute('href'));
                if (target) {
                    target.setAttribute('tabindex', '-1');
                    target.focus();
                    target.addEventListener('blur', () => {
                        target.removeAttribute('tabindex');
                    }, { once: true });
                    e.preventDefault();
                }
            });
        });
    }

    setupAnnouncements() {
        // Globally accessible announce function
        window.announceMessage = (message, priority = 'polite') => {
            const liveRegion = document.getElementById('accessibility-announcements');
            if (liveRegion) {
                liveRegion.setAttribute('aria-live', priority);
                liveRegion.textContent = message;
                setTimeout(() => {
                    liveRegion.setAttribute('aria-live', 'off');
                }, 1000);
            }
        };
    }

    createSkipLink() {
        if (document.querySelector('a[href="#main"]')) return; // Already exists

        const skipLink = document.createElement('a');
        skipLink.href = '#main';
        skipLink.textContent = 'Skip to main content';
        skipLink.style.cssText = `
            position: absolute;
            top: -40px;
            left: 0;
            background: var(--accent-primary, #2563eb);
            color: white;
            padding: 8px 16px;
            text-decoration: none;
            z-index: 100;
            border-radius: 4px;
            font-weight: 500;
        `;
        skipLink.addEventListener('focus', () => {
            skipLink.style.top = '10px';
        });
        skipLink.addEventListener('blur', () => {
            skipLink.style.top = '-40px';
        });
        document.body.insertBefore(skipLink, document.body.firstChild);
    }

    showShortcutsHelp() {
        const shortcuts = [
            { key: '?', description: 'Show this help' },
            { key: 'S+D', description: 'Toggle dark mode' },
            { key: 'S+T', description: 'Start/Pause timer' },
            { key: 'S+R', description: 'Reset timer' },
            { key: 'Tab', description: 'Focus next element' },
            { key: 'Shift+Tab', description: 'Focus previous element' },
            { key: 'Escape', description: 'Close modals' },
            { key: 'Enter', description: 'Activate buttons' },
            { key: 'Space', description: 'Activate buttons' }
        ];

        const message = shortcuts
            .map(s => `${s.key}: ${s.description}`)
            .join('\n');

        window.announceMessage(`Keyboard shortcuts: ${message}`);
        
        // Also show as alert for prominence
        console.table(shortcuts);
    }

    closeFocusedModals() {
        const modals = document.querySelectorAll('[role="dialog"][open], .modal[open]');
        modals.forEach(modal => {
            if (modal.hasAttribute('open')) {
                modal.removeAttribute('open');
            }
            modal.style.display = 'none';
        });
    }

    toggleShortcutPanel() {
        this.showShortcutsHelp();
    }

    announceSuccess(message = 'Success') {
        window.announceMessage(`✓ ${message}`, 'assertive');
    }

    announceError(message = 'Error occurred') {
        window.announceMessage(`✗ ${message}`, 'assertive');
    }

    announceInfo(message) {
        window.announceMessage(`ℹ ${message}`, 'polite');
    }
}

// Initialize on DOM ready
document.addEventListener('DOMContentLoaded', () => {
    window.a11y = new AccessibilityManager();
});
