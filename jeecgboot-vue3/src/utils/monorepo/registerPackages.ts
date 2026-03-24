import type { App } from 'vue';
import { warn } from '/@/utils/log';
import { registerDynamicRouter } from '/@/utils/monorepo/dynamicRouter';
import { createAsyncComponent } from "@/utils/factory/createAsyncComponent";

// 懒加载模块配置（按需加载，访问相关路由时才加载对应包）
const lazyPackages = [
  { name: '@jeecg/online', importer: () => import('@jeecg/online') },
  { name: '@jeecg/aiflow', importer: () => import('@jeecg/aiflow') },
];

let appInstance: App | null = null;

// noinspection JSUnusedGlobalSymbols
const installOptions = {
  baseImport,
};

export function registerPackages(app: App) {
  // 仅保存 app 实例，不立即加载模块
  appInstance = app;
  app.component(
    'SuperQuery',
    createAsyncComponent(() => import('@jeecg/online').then(mod => mod.SuperQuery))
  );
  app.component(
    'JOnlineSearchSelect',
    createAsyncComponent(() => import('@jeecg/online').then(mod => mod.JOnlineSearchSelect))
  );
}

/** 已加载的包缓存 */
const loadedPackages = new Map<string, any>();
/** 正在加载的包 Promise 缓存（防止重复加载） */
const loadingPromises = new Map<string, Promise<any>>();

/**
 * 按需加载包并注册
 */
async function ensurePackageLoaded(pkgConfig: typeof lazyPackages[number]) {
  const { name, importer } = pkgConfig;
  if (loadedPackages.has(name)) {
    return loadedPackages.get(name);
  }
  if (!loadingPromises.has(name)) {
    const promise = importer().then((pkg) => {
      const mod = pkg.default || pkg;
      if (appInstance) {
        appInstance.use(mod, installOptions);
        registerDynamicRouter(mod.getViews);
      }
      loadedPackages.set(name, mod);
      loadingPromises.delete(name);
      return mod;
    });
    loadingPromises.set(name, promise);
  }
  return loadingPromises.get(name);
}

/**
 * 根据 component 路径关键字匹配优先加载的包
 */
function getMatchedPackage(component: string): typeof lazyPackages[number] | null {
  const lc = component.toLowerCase();
  for (const pkgConfig of lazyPackages) {
    // 从包名中提取关键字，如 @jeecg/online -> online, @jeecg/aiflow -> aiflow
    const keyword = pkgConfig.name.split('/').pop()!;
    if (lc.includes(keyword)) {
      return pkgConfig;
    }
  }
  return null;
}

/**
 * 从指定包中查找组件
 */
async function findComponentInPackage(pkgConfig: typeof lazyPackages[number], component: string): Promise<(() => Promise<Recordable>) | null> {
  try {
    const mod = await ensurePackageLoaded(pkgConfig);
    const views = mod.getViews();
    for (const key of Object.keys(views)) {
      const k = key.replace('./src/views', '');
      const startFlag = component.startsWith('/');
      const endFlag = component.endsWith('.vue') || component.endsWith('.tsx');
      const startIndex = startFlag ? 0 : 1;
      const lastIndex = endFlag ? k.length : k.lastIndexOf('.');
      if (k.substring(startIndex, lastIndex) === component) {
        return views[key];
      }
    }
  } catch (e) {
    // 包不存在或加载失败，跳过
  }
  return null;
}

/**
 * 按需加载包组件：当路由匹配不到本地组件时调用
 * 根据 component 路径中的关键字优先匹配对应包，避免无意义的遍历
 */
export async function loadPackageComponent(component: string): Promise<(() => Promise<Recordable>) | null> {
  // 优先根据关键字精准匹配包
  const matched = getMatchedPackage(component);
  if (matched) {
    return findComponentInPackage(matched, component);
  }
  // 未匹配到关键字，依次尝试所有包
  for (const pkgConfig of lazyPackages) {
    const result = await findComponentInPackage(pkgConfig, component);
    if (result) return result;
  }
  return null;
}

// 模块里可使用的import
const importGlobs = [import.meta.glob('../../utils/**/*.{ts,js,tsx}'), import.meta.glob('../../hooks/**/*.{ts,js,tsx}')];

/**
 * 基础项目导包
 * 目前支持导入如下
 * /@/utils/**
 * /@/hooks/**
 *
 * @param path 文件路径，ts无需输入后缀名。如：/@/utils/common/compUtils
 */
async function baseImport(path: string) {
  if (path) {
    // 将 /@/ 替换成 ../../
    path = path.replace(/^\/@\//, '../../');
    for (const glob of importGlobs) {
      for (const key of Object.keys(glob)) {
        if (path === key || `${path}.ts` === key || `${path}.tsx` === key) {
          return glob[key]();
        }
      }
    }
    warn(`引入失败：${path} 不存在`);
  }
  return null;
}
