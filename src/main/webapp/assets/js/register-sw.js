// Service worker registration helper
(function registerSW(){
  if (!('serviceWorker' in navigator)) return;
  window.addEventListener('load', () => {
    navigator.serviceWorker.register('/assets/js/sw.js')
      .then(reg => {
        console.log('Service Worker registered (helper):', reg.scope);
      })
      .catch(err => {
        console.warn('Service Worker registration failed (helper):', err);
      });
  });
})();
