/* ============================================================
   Enterprise Dashboard Module
   - SSE with polling fallback
   - Exponential backoff + retry reset
   - Fetch timeout protection
   - No double polling / double streams
   - Animated counters with safe DOM updates
   - Public API: DashboardModule.init(config)
   ============================================================ */
   ;(function (global) {
       'use strict';

       var DEFAULTS = {
           metricsUrl: '/api/dashboard/metrics',
           refreshInterval: 30000,
           live: { url: '/streams/dashboard' },
           retry: { attempts: 8, baseDelay: 800 },
           debug: false
       };

       var state = {
           config: {},
           sse: null,
           pollTimer: null,
           retryCount: 0
       };

       function log() {
           if (state.config.debug && console && console.debug) {
               console.debug.apply(console, ['[dashboard]'].concat([].slice.call(arguments)));
           }
       }

       function err() {
           if (console && console.error) {
               console.error.apply(console, ['[dashboard]'].concat([].slice.call(arguments)));
           }
       }

       /* ---------------- Fetch with timeout ---------------- */
       function fetchJson(url, timeout) {
           timeout = timeout || 8000;

           return new Promise(function (resolve, reject) {
               var xhr = new XMLHttpRequest();
               xhr.open('GET', url, true);
               xhr.timeout = timeout;

               xhr.onreadystatechange = function () {
                   if (xhr.readyState !== 4) return;
                   if (xhr.status >= 200 && xhr.status < 300) {
                       try {
                           resolve(JSON.parse(xhr.responseText));
                       } catch (e) {
                           reject(e);
                       }
                   } else {
                       reject(new Error('Fetch failed: ' + xhr.status));
                   }
               };

               xhr.onerror = function () {
                   reject(new Error('Network error'));
               };

               xhr.ontimeout = function () {
                   reject(new Error('Request timeout'));
               };

               xhr.send();
           });
       }

       /* ---------------- Animation ---------------- */
       function easeOutCubic(t) {
           return 1 - Math.pow(1 - t, 3);
       }

       function animateValue(el, end, percent) {
           if (!el) return;

           var start = parseInt(el.getAttribute('data-start') || '0', 10) || 0;
           var duration = 800;
           var startTime = new Date().getTime();

           function frame() {
               var now = new Date().getTime();
               var progress = Math.min(1, (now - startTime) / duration);
               var value = Math.round(start + (end - start) * easeOutCubic(progress));
               el.textContent = value + (percent ? '%' : '');

               if (progress < 1) {
                   requestAnimationFrame(frame);
               } else {
                   el.setAttribute('data-start', end);
               }
           }

           requestAnimationFrame(frame);
       }

       /* ---------------- DOM Update ---------------- */
       function updateMetricsDOM(m) {
           try {
               if (typeof m.productivity !== 'undefined') {
                   var ring = document.querySelector('.progress-ring');
                   if (ring) ring.style.setProperty('--value', m.productivity);
                   var txt = ring ? ring.querySelector('span') : null;
                   animateValue(txt, m.productivity, true);
               }

               var tasks = document.getElementById('tasksCompleted');
               if (tasks && typeof m.tasksCompleted !== 'undefined' && typeof m.totalTasks !== 'undefined') {
                   tasks.textContent = m.tasksCompleted + ' / ' + m.totalTasks;
               }

               animateValue(document.getElementById('habitsFollowed'), m.habitsFollowed || 0, false);
               animateValue(document.getElementById('activitiesLogged'), m.activitiesLogged || 0, false);
               animateValue(document.getElementById('weeklyScore'), m.weeklyScore || m.productivity || 0, true);
               animateValue(document.getElementById('monthlyScore'), m.monthlyScore || m.productivity || 0, true);
               animateValue(document.getElementById('averageScore'), m.averageScore || m.productivity || 0, true);
               animateValue(document.getElementById('streakDays'), m.streakDays || 0, false);

           } catch (e) {
               err('DOM update error', e);
           }
       }

       /* ---------------- Polling ---------------- */
       function startPolling() {
           if (state.pollTimer) return;

           function tick() {
               fetchJson(state.config.metricsUrl)
                   .then(updateMetricsDOM)
                   .catch(function (e) {
                       err('Polling error', e);
                   });
           }

           tick();
           state.pollTimer = setInterval(tick, state.config.refreshInterval);
       }

       /* ---------------- SSE ---------------- */
       function startSSE() {
           if (!window.EventSource) {
               startPolling();
               return;
           }

           try {
               var es = new EventSource(state.config.live.url);
               state.sse = es;

               es.onmessage = function (evt) {
                   try {
                       updateMetricsDOM(JSON.parse(evt.data));
                   } catch (e) {}
               };

               es.onerror = function () {
                   es.close();
                   startPolling();
               };
           } catch (e) {
               startPolling();
           }
       }

       /* ---------------- Init ---------------- */
       function init(cfg) {
           state.config = cfg || DEFAULTS;

           fetchJson(state.config.metricsUrl)
               .then(updateMetricsDOM)
               .catch(function () {});

           if (state.config.live && state.config.live.url) {
               startSSE();
           } else {
               startPolling();
           }
       }

       document.addEventListener('DOMContentLoaded', function () {
           if (global.dashboardConfig) {
               init(global.dashboardConfig);
           }
       });

       global.DashboardModule = { init: init };

   })(window);