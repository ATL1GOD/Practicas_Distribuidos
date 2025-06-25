const CACHE_NAME = 'cineboletos-cache-v1';
const urlsToCache = [
  './',
  './index.html',
  './style.css',
  './script.js',
  './icons/favicon-192x192.png',
  './icons/favicon-512x512.png',
];

// Instalación del Service Worker
self.addEventListener('install', (e) => {
  console.log('Service Worker instalado');
  e.waitUntil(
    caches.open(CACHE_NAME).then((cache) => {
      return cache.addAll(urlsToCache);
    })
  );
});

// Activación del Service Worker - Limpieza de caches viejos
self.addEventListener('activate', (e) => {
  const cacheWhitelist = [CACHE_NAME];
  console.log('Service Worker activado');
  e.waitUntil(
    caches.keys().then((cacheNames) => {
      return Promise.all(
        cacheNames.map((cacheName) => {
          if (!cacheWhitelist.includes(cacheName)) {
            console.log('Cache antiguo eliminado:', cacheName);
            return caches.delete(cacheName);
          }
        })
      );
    })
  );
});

// Manejo de peticiones - Cache y actualización en segundo plano
self.addEventListener('fetch', (event) => {
  // Solo manejar peticiones GET
  if (event.request.method !== 'GET') return;

  event.respondWith(
    caches.match(event.request).then((cachedResponse) => {
      const fetchRequest = event.request.clone();

      // Si hay en caché, devolver y actualizar en segundo plano
      if (cachedResponse) {
        fetch(fetchRequest).then((response) => {
          if (
            response &&
            response.status === 200 &&
            response.type === 'basic'
          ) {
            caches.open(CACHE_NAME).then((cache) => {
              cache.put(event.request, response.clone());
            });
          }
        }).catch((err) => {
          console.warn('Fallo al actualizar caché:', err);
        });

        return cachedResponse;
      }

      // Si no está en caché, intentar obtenerlo de la red
      return fetch(fetchRequest)
        .then((response) => {
          if (
            response &&
            response.status === 200 &&
            response.type === 'basic'
          ) {
            const responseToCache = response.clone();
            caches.open(CACHE_NAME).then((cache) => {
              cache.put(event.request, responseToCache);
            });
          }
          return response;
        })
        .catch(() => {
          // Si falla la red, mostrar página de respaldo
          return caches.match('./offline.html');
        });
    })
  );
});
