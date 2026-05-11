import path from 'path';
import { normalizePath } from 'vite';
import type { Plugin, ResolvedConfig } from 'vite';
import type { ViteThemeOptions } from './index';
import { CLIENT_PUBLIC_ABSOLUTE_PATH, CLIENT_PUBLIC_PATH_FRAGMENT } from './constants';

type PluginType = 'colorPlugin' | 'antdDarkPlugin';

/**
 * 通过 transform hook 直接对内置 client.ts 中的 6 个占位符做字符串替换。
 *
 * vite 8 的 `define` 在 dev 模式下并未对项目源码（.ts）中的纯标识符占位做编译期替换，
 * 因此回到 transform-based 替换 —— 与原 @rys-fe/vite-plugin-theme 行为一致。
 *
 * 同名 plugin 注册多次（colorPlugin + antdDarkPlugin）时，两次 transform 都会跑，
 * 各自只替换自己负责的占位，互不干扰。
 */
export function injectClientPlugin(
  type: PluginType,
  {
    colorPluginOptions,
    colorPluginCssOutputName,
    antdDarkCssOutputName,
    antdDarkExtractCss,
    antdDarkLoadLink,
  }: {
    colorPluginOptions?: ViteThemeOptions;
    antdDarkCssOutputName?: string;
    colorPluginCssOutputName?: string;
    antdDarkExtractCss?: boolean;
    antdDarkLoadLink?: boolean;
  },
): Plugin {
  let config: ResolvedConfig;
  let isServer = false;

  return {
    name: `vite:inject-vite-plugin-theme-client(${type})`,
    enforce: 'pre',
    configResolved(resolvedConfig) {
      config = resolvedConfig;
      isServer = resolvedConfig.command === 'serve';
    },

    transform(code, id) {
      const nid = normalizePath(id);
      const isClientFile =
        nid === CLIENT_PUBLIC_ABSOLUTE_PATH ||
        nid.endsWith(CLIENT_PUBLIC_PATH_FRAGMENT + '.ts') ||
        nid.endsWith(CLIENT_PUBLIC_PATH_FRAGMENT + '.js') ||
        nid.includes(CLIENT_PUBLIC_PATH_FRAGMENT.replace(/\//gi, '_'));

      if (!isClientFile) return;

      const assetsDir = config.build?.assetsDir ?? 'assets';
      const base = config.base ?? '/';
      const getOutputFile = (name?: string) =>
        JSON.stringify(`${base}${assetsDir}/${name}`);

      // __PROD__: 所有 plugin 实例都注入
      code = code.replace(/\b__PROD__\b/g, JSON.stringify(!isServer));

      if (type === 'colorPlugin') {
        code = code
          .replace(/\b__COLOR_PLUGIN_OUTPUT_FILE_NAME__\b/g, getOutputFile(colorPluginCssOutputName))
          .replace(/\b__COLOR_PLUGIN_OPTIONS__\b/g, JSON.stringify(colorPluginOptions ?? {}));
      }

      if (type === 'antdDarkPlugin') {
        code = code.replace(
          /\b__ANTD_DARK_PLUGIN_OUTPUT_FILE_NAME__\b/g,
          getOutputFile(antdDarkCssOutputName),
        );
        if (typeof antdDarkExtractCss === 'boolean') {
          code = code.replace(
            /\b__ANTD_DARK_PLUGIN_EXTRACT_CSS__\b/g,
            JSON.stringify(antdDarkExtractCss),
          );
        }
        if (typeof antdDarkLoadLink === 'boolean') {
          code = code.replace(
            /\b__ANTD_DARK_PLUGIN_LOAD_LINK__\b/g,
            JSON.stringify(antdDarkLoadLink),
          );
        }
      }

      return { code, map: null };
    },
  };
}

// 防止 path 被 tree-shake
export const _PATH = path.posix;
