import type {App} from "vue";
import {router} from "@/router";
import {useGlobSetting} from "@/hooks/setting";

const glob = useGlobSetting();

const _PRELOAD_UTILS = '_ELECTRON_PRELOAD_UTILS_';

export const $electron = {
  // 当前是否为Electron平台
  isElectron: () => glob.isElectronPlatform,

  // 通过浏览器打开链接
  openInBrowser: bindUtils('openInBrowser') as (url: string) => void,

  resolveRoutePath,
}

function bindUtils(n: string) {
  const fn = window[_PRELOAD_UTILS]?.[n];
  if (typeof fn?.bind === 'function') {
    return fn.bind(null);
  }
  return () => console.warn(`Electron preload util ${n} is not a function`);
}

// 解析路由路径
function resolveRoutePath(path: string) {
  return window.location.origin + window.location.pathname + router.resolve(path).href;
}

/**
 * 配置Electron
 */
export function setupElectron(_: App) {
  // 非Electron平台不执行
  if (!$electron.isElectron()) {
    return;
  }
  hookWindowOpen();
}

function hookWindowOpen() {
  // 保存原生方法引用
  const originFunc = window.open;
  // 重写window.open方法
  window['open'] = function (url, windowName, windowFeatures) {
    url = typeof url === 'string' ? url.trim() : '';
    if (!url) {
      throw new Error('window.open: url is required');
    }
    // 判断是否以http或https开头
    if (/^https?:\/\//.test(url)) {
      // 判断是否为本地地址
      if (url.startsWith(window.location.origin) || url.startsWith(window['_CONFIG']['domianURL'])) {
        // 直接打开
        return originFunc(url, windowName, windowFeatures);
      }
      // 调用Electron进行外部浏览器打开
      return $electron.openInBrowser(url) as any;
    }
    // 自定义逻辑
    return originFunc(url, windowName, windowFeatures)
  }
}