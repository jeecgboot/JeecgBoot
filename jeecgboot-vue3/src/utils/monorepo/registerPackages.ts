import type { App } from 'vue';
import { warn } from '/@/utils/log';
import { registerDynamicRouter } from '/@/utils/monorepo/dynamicRouter';
// 引入模块
import PACKAGE_TEST_JEECG_ONLINE from '@jeecg/online';

export function registerPackages(app: App) {
  use(app, PACKAGE_TEST_JEECG_ONLINE);
}

// noinspection JSUnusedGlobalSymbols
const installOptions = {
  baseImport,
};

/** 注册模块 */
function use(app: App, pkg) {
  app.use(pkg, installOptions);
  registerDynamicRouter(pkg.getViews);
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
