import type { Plugin, ResolvedConfig } from 'vite';
import path from 'path';
import fs from 'fs-extra';
import { debug as Debug } from 'debug';
import chalk from 'chalk';
import { extractVariable, minifyCSS, createFileHash, formatCss } from './utils';
import { VITE_CLIENT_ENTRY, cssLangRE, cssVariableString, CLIENT_PUBLIC_ABSOLUTE_PATH } from './constants';
import { injectClientPlugin } from './injectClientPlugin';

export * from './client/colorUtils';
export { antdDarkThemePlugin } from './antdDarkThemePlugin';

export type ResolveSelector = (selector: string) => string;
export type InjectTo = 'head' | 'body' | 'body-prepend';

export interface ViteThemeOptions {
  colorVariables: string[];
  wrapperCssSelector?: string;
  resolveSelector?: ResolveSelector;
  customerExtractVariable?: (code: string) => string;
  fileName?: string;
  injectTo?: InjectTo;
  verbose?: boolean;
}

const debug = Debug('vite-plugin-theme');

export function viteThemePlugin(opt: ViteThemeOptions): Plugin[] {
  let isServer = false;
  let config: ResolvedConfig;
  let clientPath = '';
  const styleMap = new Map<string, string>();
  const extCssSet = new Set<string>();

  const emptyPlugin: Plugin = { name: 'vite:theme' };

  const options: ViteThemeOptions = Object.assign(
    {
      colorVariables: [],
      wrapperCssSelector: '',
      fileName: 'app-theme-style',
      injectTo: 'body',
      verbose: true,
    },
    opt,
  );

  debug('plugin options:', options);

  const {
    colorVariables,
    wrapperCssSelector,
    resolveSelector,
    customerExtractVariable,
    fileName,
    verbose,
  } = options;

  if (!colorVariables || colorVariables.length === 0) {
    console.error('colorVariables is not empty!');
    return [emptyPlugin];
  }

  const resolveSelectorFn =
    resolveSelector || ((s: string) => `${wrapperCssSelector} ${s}`);

  const cssOutputName = `${fileName}.${createFileHash()}.css`;

  let needSourcemap = false;

  return [
    injectClientPlugin('colorPlugin', {
      colorPluginCssOutputName: cssOutputName,
      colorPluginOptions: options,
    }),
    {
      ...emptyPlugin,
      enforce: 'post',
      configResolved(resolvedConfig) {
        config = resolvedConfig;
        isServer = resolvedConfig.command === 'serve';
        // dev 模式直接通过 /@fs/<absolute> 让浏览器加载内置 client.ts
        clientPath = JSON.stringify(
          isServer
            ? '/@fs/' + CLIENT_PUBLIC_ABSOLUTE_PATH
            : path.posix.join(config.base, CLIENT_PUBLIC_ABSOLUTE_PATH),
        );
        needSourcemap = !!resolvedConfig.build.sourcemap;
        debug('plugin config base:', resolvedConfig.base);
      },

      async transform(code, id) {
        if (!cssLangRE.test(id)) return null;

        const getResult = (content: string) => ({
          map: needSourcemap ? this.getCombinedSourcemap() : null,
          code: content,
        });

        const clientCode = isServer
          ? await getClientStyleString(code)
          : code.replace('export default', '').replace('"', '');

        const extractCssCodeTemplate =
          typeof customerExtractVariable === 'function'
            ? customerExtractVariable(clientCode)
            : extractVariable(clientCode, colorVariables, resolveSelectorFn);

        debug('extractCssCodeTemplate:', id, extractCssCodeTemplate?.slice(0, 100));

        if (!extractCssCodeTemplate) return null;

        if (isServer) {
          const retCode = [
            `import { addCssToQueue } from ${clientPath}`,
            `const themeCssId = ${JSON.stringify(id)}`,
            `const themeCssStr = ${JSON.stringify(formatCss(extractCssCodeTemplate))}`,
            `addCssToQueue(themeCssId, themeCssStr)`,
            code,
          ];
          return getResult(retCode.join('\n'));
        } else {
          if (!styleMap.has(id)) {
            extCssSet.add(extractCssCodeTemplate);
          }
          styleMap.set(id, extractCssCodeTemplate);
        }

        return null;
      },

      async writeBundle() {
        const {
          root,
          build: { outDir, assetsDir, minify },
        } = config;
        let extCssString = '';
        for (const css of extCssSet) {
          extCssString += css;
        }
        if (minify) {
          extCssString = await minifyCSS(extCssString, config);
        }
        const cssOutputPath = path.resolve(root, outDir, assetsDir, cssOutputName);
        fs.ensureDirSync(path.dirname(cssOutputPath));
        fs.writeFileSync(cssOutputPath, extCssString);
      },

      closeBundle() {
        if (verbose && !isServer) {
          const {
            build: { outDir, assetsDir },
          } = config;
          console.log(
            chalk.cyan('\n✨ [vite-plugin-theme]') +
              ` - extract css code file is successfully:`,
          );
          try {
            const { size } = fs.statSync(path.join(outDir, assetsDir, cssOutputName));
            console.log(
              chalk.dim(outDir + '/') +
                chalk.magentaBright(`${assetsDir}/${cssOutputName}`) +
                `\t\t${chalk.dim((size / 1024).toFixed(2) + 'kb')}` +
                '\n',
            );
          } catch {}
        }
      },
    },
  ];
}

async function getClientStyleString(code: string) {
  if (!code.includes(VITE_CLIENT_ENTRY)) return code;
  code = code.replace(/\\n/g, '');
  const cssPrefix = cssVariableString;
  const cssPrefixLen = cssPrefix.length;
  const cssPrefixIndex = code.indexOf(cssPrefix);
  const len = cssPrefixIndex + cssPrefixLen;
  const cssLastIndex = code.indexOf('\n', len + 1);
  if (cssPrefixIndex !== -1) {
    code = code.slice(len, cssLastIndex);
  }
  return code;
}
