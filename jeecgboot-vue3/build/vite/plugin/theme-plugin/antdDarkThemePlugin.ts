import type { Plugin, ResolvedConfig } from 'vite';
import path from 'path';
import fs from 'fs-extra';
import less from 'less';
import { createFileHash, minifyCSS, extractVariable } from './utils';
import chalk from 'chalk';
import { colorRE, linkID } from './constants';
import { injectClientPlugin } from './injectClientPlugin';
import { lessPlugin } from './preprocessor/less';

export interface AntdDarkThemeOption {
  darkModifyVars?: any;
  fileName?: string;
  verbose?: boolean;
  selector?: string;
  filter?: (id: string) => boolean;
  extractCss?: boolean;
  preloadFiles?: string[];
  loadMethod?: 'link' | 'ajax';
}

export function antdDarkThemePlugin(options: AntdDarkThemeOption): Plugin[] {
  const {
    darkModifyVars,
    verbose = true,
    fileName = 'app-antd-dark-theme-style',
    selector,
    filter,
    extractCss = true,
    preloadFiles = [],
    loadMethod = 'link',
  } = options;
  let isServer = false;
  let needSourcemap = false;
  let config: ResolvedConfig;
  let extCssString = '';

  const styleMap = new Map<string, string>();
  const codeCache = new Map<string, { code: string; css: string }>();

  const cssOutputName = `${fileName}.${createFileHash()}.css`;

  const hrefProtocals = ['http://'];

  const getCss = (css: string) => {
    return `[${selector || 'data-theme="dark"'}] {${css}}`;
  };

  async function preloadLess() {
    if (!preloadFiles || !preloadFiles.length) return;
    for (const id of preloadFiles) {
      const code = fs.readFileSync(id, 'utf-8');
      less
        .render(code, {
          javascriptEnabled: true,
          modifyVars: darkModifyVars,
          filename: path.resolve(id),
          plugins: [lessPlugin(id, config)],
        })
        .then(({ css }) => {
          const colors = css.match(colorRE);
          if (colors) {
            css = extractVariable(css, colors.concat(['transparent']));
            codeCache.set(id, { code, css });
          }
        })
        .catch(() => void 0);
    }
  }

  function getProtocal(p: string): string | undefined {
    let protocal: string | undefined;
    hrefProtocals.forEach((hrefProtocal) => {
      if (p.startsWith(hrefProtocal)) protocal = hrefProtocal;
    });
    return protocal;
  }

  return [
    injectClientPlugin('antdDarkPlugin', {
      antdDarkCssOutputName: cssOutputName,
      antdDarkExtractCss: extractCss,
      antdDarkLoadLink: loadMethod === 'link',
    }),
    {
      name: 'vite:antd-dark-theme',
      enforce: 'pre',
      configResolved(resolvedConfig) {
        config = resolvedConfig;
        isServer = resolvedConfig.command === 'serve';
        needSourcemap = !!resolvedConfig.build.sourcemap;
        if (isServer) preloadLess();
      },
      transformIndexHtml: {
        order: 'pre',
        handler(html) {
          let href: string;
          const protocal = getProtocal(config.base);

          if (isServer || loadMethod !== 'link') return html;

          if (protocal) {
            href =
              protocal +
              path.posix.join(
                config.base.slice(protocal.length),
                config.build.assetsDir,
                cssOutputName,
              );
          } else {
            href = path.posix.join(config.base, config.build.assetsDir, cssOutputName);
          }

          return {
            html,
            tags: [
              {
                tag: 'link',
                attrs: {
                  disabled: true,
                  id: linkID,
                  rel: 'alternate stylesheet',
                  href,
                },
                injectTo: 'head',
              },
            ],
          };
        },
      },

      async transform(code, id) {
        if (!id.endsWith('.less') || !code.includes('@')) return null;
        if (typeof filter === 'function' && !filter(id)) return null;

        const getResult = (content: string) => ({
          map: needSourcemap ? this.getCombinedSourcemap() : null,
          code: content,
        });

        let processCss = '';
        const cache = codeCache.get(id);
        const isUpdate = !cache || cache.code !== code;

        if (isUpdate) {
          let renderedCss = '';
          try {
            const { css } = await less.render(code, {
              javascriptEnabled: true,
              modifyVars: darkModifyVars,
              filename: path.resolve(id),
              plugins: [lessPlugin(id, config)],
            });
            renderedCss = css;
          } catch {
            // less 解析失败（典型场景：业务 less 用了 vite alias / virtual import），
            // 静默跳过该文件 —— 不影响主流程，也不参与 dark theme 提取。
            return null;
          }
          const colors = renderedCss.match(colorRE);
          if (colors) {
            processCss = extractVariable(renderedCss, colors.concat(['transparent']));
          }
        } else {
          processCss = cache!.css;
        }

        if (isServer || !extractCss) {
          if (isUpdate) codeCache.set(id, { code, css: processCss });
          return getResult(`${getCss(processCss)}\n` + code);
        } else {
          if (!styleMap.has(id) && processCss) {
            try {
              const { css } = await less.render(getCss(processCss), {
                filename: path.resolve(id),
                plugins: [lessPlugin(id, config)],
              });
              extCssString += `${css}\n`;
            } catch {
              // 同上：拼出来的 selector wrapper less 解析失败也跳过
            }
          }
          styleMap.set(id, processCss);
        }

        return null;
      },

      async writeBundle() {
        if (!extractCss) return;
        const {
          root,
          build: { outDir, assetsDir, minify },
        } = config;
        if (minify) {
          extCssString = await minifyCSS(extCssString, config);
        }
        const cssOutputPath = path.resolve(root, outDir, assetsDir, cssOutputName);
        fs.writeFileSync(cssOutputPath, extCssString);
      },

      closeBundle() {
        if (verbose && !isServer && extractCss) {
          const {
            build: { outDir, assetsDir },
          } = config;
          console.log(
            chalk.cyan('\n✨ [vite-plugin-theme:antd-dark]') +
              ` - extract antd dark css code file is successfully:`,
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
