import type { UserConfig, ConfigEnv } from 'vite';
import pkg from './package.json';
import dayjs from 'dayjs';
import { loadEnv } from 'vite';
import { resolve } from 'path';
import { generateModifyVars } from './build/generate/generateModifyVars';
import { createProxy } from './build/vite/proxy';
import { wrapperEnv } from './build/utils';
import { createVitePlugins } from './build/vite/plugin';
import { OUTPUT_DIR } from './build/constant';

function pathResolve(dir: string) {
  return resolve(process.cwd(), '.', dir);
}

const { dependencies, devDependencies, name, version } = pkg;
const __APP_INFO__ = {
  pkg: { dependencies, devDependencies, name, version },
  lastBuildTime: dayjs().format('YYYY-MM-DD HH:mm:ss'),
};

export default async ({ command, mode }: ConfigEnv): Promise<UserConfig> => {
  const root = process.cwd();

  const env = loadEnv(mode, root);

  // The boolean type read by loadEnv is a string. This function can be converted to boolean type
  const viteEnv = wrapperEnv(env);

  const { VITE_PORT, VITE_PUBLIC_PATH, VITE_PROXY } = viteEnv;

  const isBuild = command === 'build';

  const serverOptions: Recordable = {}

  // JEECG 作为乾坤子应用时，需要开启跨域并指定 origin
  const {VITE_GLOB_QIANKUN_MICRO_APP_NAME, VITE_GLOB_QIANKUN_MICRO_APP_ENTRY} = viteEnv;
  const isQiankunMicro = VITE_GLOB_QIANKUN_MICRO_APP_NAME != null && VITE_GLOB_QIANKUN_MICRO_APP_NAME !== '';
  if (isQiankunMicro && !isBuild) {
    serverOptions.cors = true;
    serverOptions.origin = VITE_GLOB_QIANKUN_MICRO_APP_ENTRY!.split('/').slice(0, 3).join('/');
  }

  console.log('[init] Start Port: ', VITE_PORT);
  console.debug('[init] Vite Proxy Config: ', VITE_PROXY);

  return {
    base: isQiankunMicro ? VITE_GLOB_QIANKUN_MICRO_APP_ENTRY : VITE_PUBLIC_PATH,
    root,
    resolve: {
      alias: [
        // 将 @rys-fe/vite-plugin-theme 客户端运行时重定向到内置版本（vite 8 适配），用 RegExp 精确匹配避免被父级别名误吞
        {
          find: /^@rys-fe\/vite-plugin-theme\/es\/client$/,
          replacement: pathResolve('build/vite/plugin/theme-plugin/client/client'),
        },
        {
          find: /^@rys-fe\/vite-plugin-theme\/es\/colorUtils$/,
          replacement: pathResolve('build/vite/plugin/theme-plugin/client/colorUtils'),
        },
        {
          find: /^@rys-fe\/vite-plugin-theme$/,
          replacement: pathResolve('build/vite/plugin/theme-plugin/index'),
        },
        {
          find: 'vue-i18n',
          replacement: 'vue-i18n/dist/vue-i18n.cjs.js',
        },
        // /@/xxxx => src/xxxx
        {
          find: /\/@\//,
          replacement: pathResolve('src') + '/',
        },
        // /#/xxxx => types/xxxx
        {
          find: /\/#\//,
          replacement: pathResolve('types') + '/',
        },
        {
          find: /@\//,
          replacement: pathResolve('src') + '/',
        },
        // /#/xxxx => types/xxxx
        {
          find: /#\//,
          replacement: pathResolve('types') + '/',
        },
      ],
    },
    server: {
      // Listening on all local IPs
      host: true,
      // @ts-ignore
      https: false,
      port: VITE_PORT,
      proxy: createProxy(VITE_PROXY),
      ...serverOptions,
      // update-begin--author:liaozhiyang---date:20260306---for:【QQYUN-14801】启动时预构建部分入口页面，访问时更快
      warmup: {
        clientFiles: [
          './src/main.ts',
          './src/App.vue',
          './src/views/system/loginmini/MiniLogin.vue',
          'src/layouts/default/index.vue'
        ],
      },
      // update-end--author:liaozhiyang---date:20260306---for:【QQYUN-14801】启动时预构建部分入口页面，访问时更快
    },
    build: {
      minify: 'esbuild',
      target: 'es2015',
      cssTarget: 'chrome80',
      outDir: OUTPUT_DIR,
      rollupOptions: {
        output: {
          chunkFileNames: 'js/[name]-[hash].js',
          entryFileNames: 'js/[name]-[hash].js',
          // update-begin--author:copilot---date:20260711---for:【vite8升级】rolldown不再支持对象形式的manualChunks，改为函数形式
          // 官方推荐写法：id.includes('node_modules') + split('node_modules/') 取出包名，再映射到 chunk 名（依赖包从大到小排列）
          manualChunks(id: string) {
            const normalizedId = id.replace(/\\/g, '/');
            if (!normalizedId.includes('node_modules/')) {
              return;
            }
            const segments = normalizedId.split('node_modules/').pop()!.split('/');
            const pkgName = segments[0].startsWith('@') ? `${segments[0]}/${segments[1]}` : segments[0];
            const packageToChunk: Record<string, string> = {
              vue: 'vue-vendor',
              'vue-router': 'vue-vendor',
              'emoji-mart-vue-fast': 'emoji-mart-vue-fast',
            };
            return packageToChunk[pkgName];
          },
          // update-end--author:copilot---date:20260711---for:【vite8升级】rolldown不再支持对象形式的manualChunks，改为函数形式
        },
      },
      // 关闭brotliSize显示可以稍微减少打包时间
      reportCompressedSize: false,
      // 提高超大静态资源警告大小
      chunkSizeWarningLimit: 2000,
    },
    esbuild: {
      //清除全局的console.log和debug
      drop: isBuild ? ['console', 'debugger'] : [],
    },
    define: {
      // setting vue-i18-next
      // Suppress warning
      __INTLIFY_PROD_DEVTOOLS__: false,
      __APP_INFO__: JSON.stringify(__APP_INFO__),
    },
    css: {
      preprocessorOptions: {
        less: {
          modifyVars: generateModifyVars(),
          javascriptEnabled: true,
        },
      },
    },

    // The vite plugin used by the project. The quantity is large, so it is separately extracted and managed
    plugins: await createVitePlugins(viteEnv, isBuild, isQiankunMicro),

    optimizeDeps: {
      esbuildOptions: {
        target: 'es2020',
      },
      // @iconify/iconify: The dependency is dynamically and virtually loaded by @purge-icons/generated, so it needs to be specified explicitly
      include: [
        // 强制预构建clipboard，解决Vite6对CommonJS模块的严格检查
        'clipboard',
        '@vue/shared',
        '@iconify/iconify',
        'ant-design-vue/es/locale/zh_CN',
        'ant-design-vue/es/locale/en_US',
        // update-begin--author:scott---date:20260427---for: 集成 @jeecg/aiflow（预编译 lib 在 node_modules）时，
        // Vite 默认不扫描 node_modules 里已打包的 mjs，导致 ant-design-vue/es/vc-picker/generate/dayjs.js
        // 引入的 dayjs 插件子路径（UMD/CJS）未被预打包，运行时报 "does not provide an export named 'default'"。
        // 显式列出 vc-picker 用到的全部 dayjs 插件，强制 esbuild 预打包成 ESM。
        'dayjs/plugin/advancedFormat',
        'dayjs/plugin/customParseFormat',
        'dayjs/plugin/weekday',
        'dayjs/plugin/localeData',
        'dayjs/plugin/weekOfYear',
        'dayjs/plugin/weekYear',
        'dayjs/plugin/quarterOfYear',
        // update-end--author:scott---date:20260427---for: 集成 @jeecg/aiflow 时 dayjs 插件 default 导出报错
      ],
      exclude: [
        //升级vite4后，需要排除online和aiflow依赖
        '@jeecg/aiflow',
      ],
    },
  };
};
