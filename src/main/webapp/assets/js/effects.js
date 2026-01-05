/**
 * Enhanced Animations & Effects Module
 * - Carousel slider functionality
 * - Animated counters
 * - Roller effects
 * - Particle generators
 */

(function(window, document) {
    'use strict';

    // ===== CAROUSEL / SLIDER =====
    class Carousel {
        constructor(containerSelector) {
            this.container = document.querySelector(containerSelector);
            if (!this.container) return;

            this.track = this.container.querySelector('.carousel-track');
            this.slides = (this.track ? this.track.querySelectorAll('.carousel-slide') : []);
            this.dots = this.container.querySelectorAll('.carousel-dot');
            this.prevBtn = this.container.querySelector('.carousel-nav.prev');
            this.nextBtn = this.container.querySelector('.carousel-nav.next');

            this.currentSlide = 0;
            this.slideCount = this.slides.length;

            if (this.slideCount === 0) return;

            this.init();
        }

        init() {
            this.attachEventListeners();
            this.autoPlay();
        }

        attachEventListeners() {
            if (this.prevBtn) this.prevBtn.addEventListener('click', () => this.prevSlide());
            if (this.nextBtn) this.nextBtn.addEventListener('click', () => this.nextSlide());

            this.dots.forEach((dot, index) => {
                dot.addEventListener('click', () => this.goToSlide(index));
            });

            // Keyboard navigation
            document.addEventListener('keydown', (e) => {
                if (e.key === 'ArrowLeft') this.prevSlide();
                if (e.key === 'ArrowRight') this.nextSlide();
            });
        }

        updateSlide() {
            if (!this.track) return;
            const offset = -this.currentSlide * 100;
            this.track.style.transform = `translateX(${offset}%)`;

            // Update dots
            this.dots.forEach((dot, index) => {
                dot.classList.toggle('active', index === this.currentSlide);
            });
        }

        nextSlide() {
            this.currentSlide = (this.currentSlide + 1) % this.slideCount;
            this.updateSlide();
        }

        prevSlide() {
            this.currentSlide = (this.currentSlide - 1 + this.slideCount) % this.slideCount;
            this.updateSlide();
        }

        goToSlide(index) {
            this.currentSlide = Math.max(0, Math.min(index, this.slideCount - 1));
            this.updateSlide();
        }

        autoPlay() {
            setInterval(() => this.nextSlide(), 5000);
        }
    }

    // ===== ANIMATED COUNTER =====
    class AnimatedCounter {
        constructor(elementSelector, endValue, duration = 1000) {
            this.element = document.querySelector(elementSelector);
            if (!this.element) return;

            this.endValue = endValue;
            this.duration = duration;
            this.startValue = 0;
            this.currentValue = 0;

            this.startTime = null;
            this.requestId = null;
        }

        easeOutQuad(t) {
            return t * (2 - t);
        }

        animate(timestamp) {
            if (!this.startTime) this.startTime = timestamp;
            const elapsed = timestamp - this.startTime;
            const progress = Math.min(elapsed / this.duration, 1);

            this.currentValue = Math.floor(
                this.startValue + (this.endValue - this.startValue) * this.easeOutQuad(progress)
            );

            this.element.textContent = this.currentValue.toLocaleString();

            if (progress < 1) {
                this.requestId = requestAnimationFrame((ts) => this.animate(ts));
            } else {
                this.element.textContent = this.endValue.toLocaleString();
            }
        }

        start() {
            if (this.requestId) cancelAnimationFrame(this.requestId);
            this.startTime = null;
            this.requestId = requestAnimationFrame((ts) => this.animate(ts));
        }
    }

    // ===== PROGRESS SLIDER =====
    class ProgressSlider {
        constructor(containerSelector, duration = 1200) {
            this.container = document.querySelector(containerSelector);
            if (!this.container) return;

            this.fill = this.container.querySelector('.slider-progress-fill');
            this.duration = duration;
            this.animate();
        }

        animate() {
            if (!this.fill) return;
            this.fill.style.animation = `slideProgress ${this.duration}ms cubic-bezier(0.4, 0, 0.2, 1) forwards`;
        }

        reset() {
            if (!this.fill) return;
            this.fill.style.animation = 'none';
            setTimeout(() => {
                this.animate();
            }, 50);
        }
    }

    // ===== ROLLER / TICKER EFFECT =====
    class RollerEffect {
        constructor(elementSelector, values = [], interval = 2000) {
            this.element = document.querySelector(elementSelector);
            if (!this.element) return;

            this.values = values;
            this.interval = interval;
            this.currentIndex = 0;

            if (this.values.length > 0) {
                this.element.textContent = this.values[0];
                this.startRolling();
            }
        }

        startRolling() {
            setInterval(() => {
                this.currentIndex = (this.currentIndex + 1) % this.values.length;
                this.element.textContent = this.values[this.currentIndex];
            }, this.interval);
        }
    }

    // ===== SCROLL REVEAL =====
    class ScrollReveal {
        constructor() {
            this.elements = document.querySelectorAll('.scroll-reveal');
            this.observerOptions = {
                threshold: 0.1,
                rootMargin: '0px 0px -50px 0px'
            };

            this.observer = new IntersectionObserver(
                (entries) => this.handleIntersection(entries),
                this.observerOptions
            );

            this.elements.forEach(el => this.observer.observe(el));
        }

        handleIntersection(entries) {
            entries.forEach(entry => {
                if (entry.isIntersecting) {
                    entry.target.style.animation = 'scroll-reveal-up 0.8s ease forwards';
                    this.observer.unobserve(entry.target);
                }
            });
        }
    }

    // ===== PARTICLE GENERATOR =====
    class ParticleGenerator {
        constructor(containerSelector) {
            this.container = document.querySelector(containerSelector);
            if (!this.container) return;
        }

        generateParticles(x, y, count = 8) {
            for (let i = 0; i < count; i++) {
                const particle = document.createElement('div');
                particle.className = 'particle particle-dot';

                const angle = (i / count) * Math.PI * 2;
                const velocity = 50 + Math.random() * 50;
                const tx = Math.cos(angle) * velocity;
                const ty = Math.sin(angle) * velocity;

                particle.style.left = x + 'px';
                particle.style.top = y + 'px';
                particle.style.setProperty('--tx', tx + 'px');
                particle.style.setProperty('--ty', ty + 'px');

                this.container.appendChild(particle);

                setTimeout(() => particle.remove(), 3000);
            }
        }
    }

    // ===== STAGGER LIST ANIMATION =====
    class StaggerAnimation {
        constructor(containerSelector) {
            this.container = document.querySelector(containerSelector);
            if (!this.container) return;

            this.items = this.container.querySelectorAll('.stagger-item');
            this.showItems();
        }

        showItems() {
            this.items.forEach(item => {
                item.classList.add('show');
            });
        }

        reset() {
            this.items.forEach(item => {
                item.classList.remove('show');
            });
            setTimeout(() => this.showItems(), 100);
        }
    }

    // ===== BOUNCE ANIMATION TRIGGER =====
    class BounceAnimator {
        static bounce(elementSelector) {
            const element = document.querySelector(elementSelector);
            if (!element) return;

            element.classList.remove('bounce-in');
            void element.offsetWidth; // Trigger reflow
            element.classList.add('bounce-in');
        }
    }

    // ===== SHAKE ANIMATION (Error/Alert) =====
    class ShakeAnimator {
        static shake(elementSelector) {
            const element = document.querySelector(elementSelector);
            if (!element) return;

            element.classList.remove('shake');
            void element.offsetWidth;
            element.classList.add('shake');

            setTimeout(() => {
                element.classList.remove('shake');
            }, 500);
        }
    }

    // ===== FLIP CARD SETUP =====
    class FlipCard {
        constructor(containerSelector) {
            this.cards = document.querySelectorAll(containerSelector + ' .flip-card');
            this.cards.forEach(card => {
                card.addEventListener('click', () => this.toggle(card));
            });
        }

        toggle(card) {
            const inner = card.querySelector('.flip-card-inner');
            if (inner) {
                inner.style.transform =
                    inner.style.transform === 'rotateY(180deg)'
                        ? 'rotateY(0deg)'
                        : 'rotateY(180deg)';
            }
        }
    }

    // ===== PUBLIC API =====
    window.Carousel = Carousel;
    window.AnimatedCounter = AnimatedCounter;
    window.ProgressSlider = ProgressSlider;
    window.RollerEffect = RollerEffect;
    window.ScrollReveal = ScrollReveal;
    window.ParticleGenerator = ParticleGenerator;
    window.StaggerAnimation = StaggerAnimation;
    window.BounceAnimator = BounceAnimator;
    window.ShakeAnimator = ShakeAnimator;
    window.FlipCard = FlipCard;

    // ===== AUTO-INITIALIZE ON DOM READY =====
    document.addEventListener('DOMContentLoaded', () => {
        // Auto-init carousels
        new Carousel('.carousel');

        // Auto-init scroll reveals
        if (document.querySelectorAll('.scroll-reveal').length > 0) {
            new ScrollReveal();
        }

        // Auto-init stagger animations
        document.querySelectorAll('.stagger-list').forEach(list => {
            new StaggerAnimation(list);
        });

        // Auto-init flip cards
        document.querySelectorAll('.flip-cards').forEach(container => {
            new FlipCard(container);
        });
    });

})(window, document);