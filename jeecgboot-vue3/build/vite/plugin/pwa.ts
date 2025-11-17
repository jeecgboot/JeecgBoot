/**
 * PWA 插件配置
 * 适配按需加载：只预缓存关键资源，按需加载的 chunk 使用运行时缓存
 */
import { VitePWA } from 'vite-plugin-pwa';
import type { VitePWAOptions } from 'vite-plugin-pwa';
import type { PluginOption } from 'vite';

export function configPwaPlugin(isBuild: boolean): PluginOption | PluginOption[] {
  if (!isBuild) {
    console.log('非生产环境不启用 PWA 插件!');
    return [];
  }

  const pwaOptions: Partial<VitePWAOptions> = {
    registerType: 'manual',
    injectRegister: 'inline', // 将 Service Worker 注册代码内联到 HTML 中，避免缓存问题
    includeAssets: ['favicon.ico', 'logo.png'],
    manifest: {
      name: 'JeecgBoot',
      short_name: 'Jeecg',
      theme_color: '#ffffff',
      icons: [
        {
          src: '/logo.png',
          sizes: '192x192',
          type: 'image/png',
        },
        {
          src: '/logo.png',
          sizes: '512x512',
          type: 'image/png',
        },
      ],
    },
    workbox: {
      // 增加文件大小限制到 10MB
      maximumFileSizeToCacheInBytes: 10 * 1024 * 1024, // 10 MB
      cleanupOutdatedCaches: true,
      
      // 预缓存策略：只缓存关键资源，按需加载的 chunk 通过运行时缓存
      // 预缓存入口文件、CSS 和静态资源，以及核心 JS（入口和 vendor）
      globPatterns: [
        'index.html',
        'manifest.webmanifest',
        '**/*.css',
        '**/*.{ico,png,svg,woff2}',
        // 预缓存入口 JS 和核心 vendor chunk
        'js/index-*.js',
        'js/*-vendor-*.js',
      ],
      // 注意：不预缓存按需加载的路由组件 chunk
      // 这些 chunk 将通过运行时缓存策略按需加载和缓存
      
      // 运行时缓存策略：处理按需加载的资源
      runtimeCaching: [
        // 按需加载的 JS chunk：优先网络，失败后使用缓存
        {
          urlPattern: /\/js\/.*\.js$/i,
          handler: 'NetworkFirst',
          options: {
            cacheName: 'js-chunks-cache',
            networkTimeoutSeconds: 3,
            expiration: {
              maxEntries: 100,
              maxAgeSeconds: 60 * 60 * 24 * 7, // 7天
            },
            cacheableResponse: {
              statuses: [0, 200],
            },
          },
        },
        // CSS 文件：优先缓存
        {
          urlPattern: /\/css\/.*\.css$/i,
          handler: 'CacheFirst',
          options: {
            cacheName: 'css-cache',
            expiration: {
              maxEntries: 50,
              maxAgeSeconds: 60 * 60 * 24 * 30, // 30天
            },
            cacheableResponse: {
              statuses: [0, 200],
            },
          },
        },
        // Google Fonts
        {
          urlPattern: /^https:\/\/fonts\.googleapis\.com\/.*/i,
          handler: 'CacheFirst',
          options: {
            cacheName: 'google-fonts-cache',
            expiration: {
              maxEntries: 10,
              maxAgeSeconds: 60 * 60 * 24 * 365,
            },
            cacheableResponse: {
              statuses: [0, 200],
            },
          },
        },
        // 图片资源
        {
          urlPattern: /\.(?:png|jpg|jpeg|svg|gif|webp)$/,
          handler: 'CacheFirst',
          options: {
            cacheName: 'image-cache',
            expiration: {
              maxEntries: 100,
              maxAgeSeconds: 60 * 60 * 24 * 30,
            },
          },
        },
        // API 请求
        {
          urlPattern: /\/api\/.*/i,
          handler: 'NetworkFirst',
          options: {
            cacheName: 'api-cache',
            networkTimeoutSeconds: 10,
            expiration: {
              maxEntries: 50,
              maxAgeSeconds: 60 * 5,
            },
            cacheableResponse: {
              statuses: [0, 200],
            },
          },
        },
      ],
      skipWaiting: false,
      clientsClaim: false,
    },
    devOptions: {
      enabled: false,
    },
  };

  return VitePWA(pwaOptions);
}

