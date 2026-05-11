import path from 'path';
import { normalizePath } from 'vite';

export const VITE_CLIENT_ENTRY = '/@vite/client';

// 客户端入口（指向项目内置的 client.ts —— 预构建后会落到 node_modules/.vite 缓存里）
// 这里给一个虚拟绝对路径，injectClientPlugin 用它来匹配 transform 的 id
export const VITE_PLUGIN_THEME_CLIENT_ENTRY = normalizePath(
  path.resolve(process.cwd(), 'build/vite/plugin/theme-plugin/client'),
);

export const CLIENT_PUBLIC_ABSOLUTE_PATH = normalizePath(
  VITE_PLUGIN_THEME_CLIENT_ENTRY + '/client.ts',
);

// 业务 import 时使用的相对路径片段（按 normalizePath 规范化），用于宽匹配
export const CLIENT_PUBLIC_PATH_FRAGMENT = 'theme-plugin/client/client';

export const commentRE = /\\\\?n|\n|\\\\?r|\/\*[\s\S]+?\*\//g;

const cssLangs = `\\.(css|less|sass|scss|styl|stylus|postcss)($|\\?)`;

export const colorRE =
  /#([a-fA-F0-9]{6}|[a-fA-F0-9]{3})|rgba?\((.*),\s*(.*),\s*(.*)(?:,\s*(.*(?:.*)?))?\)/gi;

export const cssVariableString = `const css = "`;

export const cssBlockRE = /[^}]*\{[^{]*\}/g;

export const cssLangRE = new RegExp(cssLangs);
export const ruleRE = /(\w+-)*\w+:/;
export const cssValueRE = /(\s?[a-z0-9]+\s)*/;
export const safeEmptyRE = /\s?/;
export const importSafeRE = /(\s*!important)?/;

export const linkID = '__VITE_PLUGIN_THEME-ANTD_DARK_THEME_LINK__';
