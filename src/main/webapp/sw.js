// Context-aware service worker for the deployed webapp root.
const CACHE_NAME = 'pt-static-v9';
const APP_ROOT = self.registration.scope;
const CORE_ASSETS = [
  'index.html',
  'manifest.json',
  'assets/css/style.css',
  'assets/css/layout.css',
  'assets/css/dashboard.css',
  'assets/css/tasks.css',
  'assets/css/habits.css',
  'assets/css/activities.css',
  'assets/css/reports.css',
  'assets/css/dark-mode.css',
  'assets/css/animations.css',
  'assets/css/apple.css',
  'assets/js/theme.js',
  'assets/js/accessibility.js',
  'assets/js/register-sw.js'
].map(path => new URL(path, APP_ROOT).toString());

self.addEventListener('install', event => {
  event.waitUntil(
    caches.open(CACHE_NAME)
      .then(cache => cache.addAll(CORE_ASSETS))
      .catch(() => {})
  );
  self.skipWaiting();
});

self.addEventListener('activate', event => {
  event.waitUntil(
    caches.keys()
      .then(keys => Promise.all(keys.filter(key => key !== CACHE_NAME).map(key => caches.delete(key))))
      .then(() => self.clients.claim())
  );
});

self.addEventListener('fetch', event => {
  if (event.request.method !== 'GET') {
    return;
  }

  const requestUrl = new URL(event.request.url);
  if (!requestUrl.href.startsWith(APP_ROOT)) {
    return;
  }

  const relativePath = requestUrl.href.slice(APP_ROOT.length);
  const isDynamicRoute =
    relativePath === '' ||
    relativePath.endsWith('Servlet') ||
    relativePath.startsWith('jsp/') ||
    relativePath.startsWith('api/') ||
    relativePath.startsWith('streams/');

  if (isDynamicRoute) {
    event.respondWith(fetch(event.request));
    return;
  }

  event.respondWith(
    caches.match(event.request).then(cached => {
      if (cached) {
        return cached;
      }

      return fetch(event.request).then(response => {
        const copy = response.clone();
        caches.open(CACHE_NAME).then(cache => cache.put(event.request, copy));
        return response;
      }).catch(() => caches.match(new URL('index.html', APP_ROOT).toString()));
    })
  );
});

self.addEventListener('notificationclick', event => {
  event.notification.close();
  event.waitUntil(
    clients.matchAll({ type: 'window', includeUncontrolled: true }).then(clientList => {
      for (const client of clientList) {
        if (client.url.startsWith(APP_ROOT) && 'focus' in client) {
          return client.focus();
        }
      }
      if (clients.openWindow) {
        return clients.openWindow(new URL('CalendarServlet', APP_ROOT).toString());
      }
      return undefined;
    })
  );
});
