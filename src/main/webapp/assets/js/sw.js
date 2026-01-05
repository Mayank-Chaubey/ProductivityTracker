// Simple service worker: precache core assets and provide network-first for API
const CACHE_NAME = 'pt-static-v1';
const CORE_ASSETS = [
  '/',
  '/index.html',
  '/jsp/index.jsp',
  '/assets/css/dashboard.css',
  '/assets/css/timer.css',
  '/assets/css/animations.css',
  '/assets/js/timer.js',
  '/assets/js/charts.js',
  '/assets/js/dashboard.js'
];

self.addEventListener('install', (e) => {
  e.waitUntil(
    caches.open(CACHE_NAME).then(cache => cache.addAll(CORE_ASSETS)).catch(() => {})
  );
  self.skipWaiting();
});

self.addEventListener('activate', (e) => {
  e.waitUntil(self.clients.claim());
});

self.addEventListener('fetch', (e) => {
  const url = new URL(e.request.url);
  // Simple strategy: network-first for API, cache-first for others
  if (url.pathname.startsWith('/api/') || url.pathname.startsWith('/streams/')) {
    e.respondWith(fetch(e.request).catch(() => caches.match(e.request)));
    return;
  }
  e.respondWith(caches.match(e.request).then(resp => resp || fetch(e.request).then(r => {
    return caches.open(CACHE_NAME).then(cache => { cache.put(e.request, r.clone()); return r; });
  }).catch(() => {
    // fallback to root
    return caches.match('/');
  })));
});
