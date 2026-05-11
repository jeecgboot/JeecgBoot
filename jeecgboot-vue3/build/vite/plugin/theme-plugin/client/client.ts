// @ts-nocheck
// 以下 6 个 placeholder 标识符由 build/vite/plugin/theme-plugin/injectClientPlugin.ts
// 在 transform hook 中以正则单词边界字符串替换为最终值。
// 不要写 `declare const __X__: T` —— 替换会破坏 declare 语句结构。
// vite 用 oxc/esbuild 编译 .ts 不做严格类型检查，未声明标识符能编过。

export const globalField = '__VITE_THEME__';
export const styleTagId = '__VITE_PLUGIN_THEME__';
export const darkStyleTagId = '__VITE_PLUGIN_DARK_THEME__';
export const linkID = '__VITE_PLUGIN_THEME-ANTD_DARK_THEME_LINK__';

const colorPluginOutputFileName = __COLOR_PLUGIN_OUTPUT_FILE_NAME__;
const isProd = __PROD__;
const colorPluginOptions = __COLOR_PLUGIN_OPTIONS__;
const injectTo = colorPluginOptions && colorPluginOptions.injectTo;

const debounceThemeRender = debounce(200, renderTheme);

export let darkCssIsReady = false;

(() => {
  if (!(window as any)[globalField]) {
    (window as any)[globalField] = {
      styleIdMap: new Map(),
      styleRenderQueueMap: new Map(),
    };
  }
  setGlobalOptions('replaceStyleVariables', replaceStyleVariables);
  if (!getGlobalOptions('defaultOptions')) {
    setGlobalOptions('defaultOptions', colorPluginOptions);
  }
})();

export function addCssToQueue(id: string, styleString: string) {
  const styleIdMap: Map<string, string> = getGlobalOptions('styleIdMap');
  if (!styleIdMap.get(id)) {
    (window as any)[globalField].styleRenderQueueMap.set(id, styleString);
    debounceThemeRender();
  }
}

function renderTheme() {
  const variables = getGlobalOptions('colorVariables');
  if (!variables) return;
  const styleRenderQueueMap: Map<string, string> = getGlobalOptions('styleRenderQueueMap');
  const styleDom = getStyleDom(styleTagId);
  let html = styleDom.innerHTML;
  for (const [id, css] of styleRenderQueueMap.entries()) {
    html += css;
    (window as any)[globalField].styleRenderQueueMap.delete(id);
    (window as any)[globalField].styleIdMap.set(id, css);
  }
  replaceCssColors(html, variables).then((processCss) => {
    appendCssToDom(styleDom, processCss, injectTo);
  });
}

export async function replaceStyleVariables({
  colorVariables,
  customCssHandler,
}: {
  colorVariables: string[];
  customCssHandler?: (css: string) => string;
}) {
  setGlobalOptions('colorVariables', colorVariables);
  const styleIdMap: Map<string, string> = getGlobalOptions('styleIdMap');
  const styleRenderQueueMap: Map<string, string> = getGlobalOptions('styleRenderQueueMap');
  if (!isProd) {
    for (const [id, css] of styleIdMap.entries()) {
      styleRenderQueueMap.set(id, css);
    }
    renderTheme();
  } else {
    try {
      const cssText = await fetchCss(colorPluginOutputFileName);
      const styleDom = getStyleDom(styleTagId);
      const processCss = await replaceCssColors(cssText, colorVariables, customCssHandler);
      appendCssToDom(styleDom, processCss, injectTo);
    } catch (error: any) {
      throw new Error(error);
    }
  }
}

export async function loadDarkThemeCss() {
  if (darkCssIsReady || !__ANTD_DARK_PLUGIN_EXTRACT_CSS__) return;
  if (__ANTD_DARK_PLUGIN_LOAD_LINK__) {
    const linkTag = document.getElementById(linkID);
    if (linkTag) {
      linkTag.removeAttribute('disabled');
      linkTag.setAttribute('rel', 'stylesheet');
    }
  } else {
    const cssText = await fetchCss(__ANTD_DARK_PLUGIN_OUTPUT_FILE_NAME__);
    const styleDom = getStyleDom(darkStyleTagId);
    appendCssToDom(styleDom, cssText, injectTo);
  }
  darkCssIsReady = true;
}

export async function replaceCssColors(
  css: string,
  colors: string[],
  customCssHandler?: (css: string) => string,
) {
  let retCss = css;
  const defaultOptions = getGlobalOptions('defaultOptions');
  const colorVariables: string[] = defaultOptions ? defaultOptions.colorVariables || [] : [];
  colorVariables.forEach(function (color, index) {
    const reg = new RegExp(
      color.replace(/,/g, ',\\s*').replace(/\s/g, '').replace('(', `\\(`).replace(')', `\\)`) +
        '([\\da-f]{2})?(\\b|\\)|,|\\s)?',
      'ig',
    );
    retCss = retCss.replace(reg, colors[index] + '$1$2').replace('$1$2', '');
    if (customCssHandler && typeof customCssHandler === 'function') {
      retCss = customCssHandler(retCss) || retCss;
    }
  });
  return retCss;
}

export function setGlobalOptions(key: string, value: any) {
  (window as any)[globalField][key] = value;
}

export function getGlobalOptions(key: string) {
  return (window as any)[globalField][key];
}

export function getStyleDom(id: string): HTMLStyleElement {
  let style = document.getElementById(id) as HTMLStyleElement | null;
  if (!style) {
    style = document.createElement('style');
    style.setAttribute('id', id);
  }
  return style;
}

export async function appendCssToDom(
  styleDom: HTMLStyleElement,
  cssText: string,
  appendTo: 'head' | 'body' | 'body-prepend' = 'body',
) {
  styleDom.innerHTML = cssText;
  if (appendTo === 'head') {
    document.head.appendChild(styleDom);
  } else if (appendTo === 'body') {
    document.body.appendChild(styleDom);
  } else if (appendTo === 'body-prepend') {
    const firstChildren = document.body.firstChild;
    document.body.insertBefore(styleDom, firstChildren);
  }
}

function fetchCss(fileName: string): Promise<string> {
  return new Promise((resolve, reject) => {
    const append = getGlobalOptions('appended');
    if (append) {
      setGlobalOptions('appended', false);
      resolve('');
      return;
    }
    const xhr = new XMLHttpRequest();
    xhr.onload = function () {
      if (xhr.readyState === 4) {
        if (xhr.status === 200) resolve(xhr.responseText);
        else reject(xhr.status);
      }
    };
    xhr.onerror = function (e) {
      reject(e);
    };
    xhr.ontimeout = function (e) {
      reject(e);
    };
    xhr.open('GET', fileName, true);
    xhr.send();
  });
}

function debounce<T extends (...args: any[]) => any>(delay: number, fn: T) {
  let timer: ReturnType<typeof setTimeout> | undefined;
  return function (this: any, ...args: any[]) {
    const ctx = this;
    if (timer) clearTimeout(timer);
    timer = setTimeout(function () {
      fn.apply(ctx, args);
    }, delay);
  };
}
