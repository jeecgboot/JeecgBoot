import path from 'path';
import fs from 'fs';
import type { Alias, ResolvedConfig } from 'vite';
import { normalizePath } from 'vite';
import less from 'less';

export type ResolveFn = (
  id: string,
  importer?: string,
  aliasOnly?: boolean,
) => Promise<string | undefined>;

type CssUrlReplacer = (url: string, importer?: string) => string | Promise<string>;

export const externalRE = /^(https?:)?\/\//;
export const isExternalUrl = (url: string) => externalRE.test(url);

export const dataUrlRE = /^\s*data:/i;
export const isDataUrl = (url: string) => dataUrlRE.test(url);

const cssUrlRE = /url\(\s*('[^']+'|"[^"]+"|[^'")]+)\s*\)/;

let ViteLessManager: any;

function createViteLessPlugin(
  rootFile: string,
  alias: Alias[],
  resolvers: { less: ResolveFn },
): Less.Plugin {
  if (!ViteLessManager) {
    ViteLessManager = class ViteManager extends (less as any).FileManager {
      resolvers: any;
      rootFile: string;
      alias: Alias[];
      constructor(rootFile: string, resolvers: { less: ResolveFn }, alias: Alias[]) {
        super();
        this.rootFile = rootFile;
        this.resolvers = resolvers;
        this.alias = alias;
      }
      supports() {
        return true;
      }
      supportsSync() {
        return false;
      }
      async loadFile(filename: string, dir: string, opts: any, env: any) {
        const resolved = await this.resolvers.less(filename, path.join(dir, '*'));
        if (resolved) {
          const result = await rebaseUrls(resolved, this.rootFile, this.alias);
          let contents: string;
          if (result && 'contents' in result) {
            contents = result.contents as string;
          } else {
            contents = fs.readFileSync(resolved, 'utf-8');
          }
          return {
            filename: path.resolve(resolved),
            contents,
          };
        } else {
          return super.loadFile(filename, dir, opts, env);
        }
      }
    };
  }

  return {
    install(_: any, pluginManager: any) {
      pluginManager.addFileManager(new ViteLessManager(rootFile, resolvers, alias));
    },
    minVersion: [3, 0, 0],
  };
}

export function lessPlugin(id: string, config: ResolvedConfig) {
  const resolvers = createCSSResolvers(config);
  return createViteLessPlugin(id, (config.resolve as any).alias as Alias[], resolvers);
}

// vite 8 中 ResolvedConfig.createResolver 已经被环境化（依赖 config.environments），
// 直接调用会抛 "Cannot read properties of undefined (reading 'environments')"。
// antd dark theme 处理的是 less 源码，业务 less 中的 alias import 已由上游 vite 流水线处理过；
// 这里只用 less 默认 FileManager（相对路径），少数 alias import 失败时由 antdDark transform 的
// try/catch 捕获并跳过，不阻塞 build。
function createCSSResolvers(_config: ResolvedConfig): { less: ResolveFn } {
  return {
    less: (async (_id: string) => undefined) as ResolveFn,
  };
}

async function rebaseUrls(file: string, rootFile: string, alias: Alias[]): Promise<any> {
  file = path.resolve(file);
  const fileDir = path.dirname(file);
  const rootDir = path.dirname(rootFile);
  if (fileDir === rootDir) return { file };
  const content = fs.readFileSync(file, 'utf-8');
  if (!cssUrlRE.test(content)) return { file };
  const rebased = await rewriteCssUrls(content, (url) => {
    if (url.startsWith('/')) return url;
    for (const { find } of alias) {
      const matches = typeof find === 'string' ? url.startsWith(find) : (find as RegExp).test(url);
      if (matches) return url;
    }
    const absolute = path.resolve(fileDir, url);
    const relative = path.relative(rootDir, absolute);
    return normalizePath(relative);
  });
  return { file, contents: rebased };
}

function rewriteCssUrls(css: string, replacer: CssUrlReplacer): Promise<string> {
  return asyncReplace(css, cssUrlRE, async (match) => {
    const [matched, rawUrl] = match;
    return await doUrlReplace(rawUrl, matched, replacer);
  });
}

export async function asyncReplace(
  input: string,
  re: RegExp,
  replacer: (match: RegExpExecArray) => string | Promise<string>,
) {
  let match: RegExpExecArray | null;
  let remaining = input;
  let rewritten = '';
  while ((match = re.exec(remaining))) {
    rewritten += remaining.slice(0, match.index);
    rewritten += await replacer(match);
    remaining = remaining.slice(match.index + match[0].length);
  }
  rewritten += remaining;
  return rewritten;
}

async function doUrlReplace(rawUrl: string, matched: string, replacer: CssUrlReplacer) {
  let wrap = '';
  const first = rawUrl[0];
  if (first === `"` || first === `'`) {
    wrap = first;
    rawUrl = rawUrl.slice(1, -1);
  }
  if (isExternalUrl(rawUrl) || isDataUrl(rawUrl) || rawUrl.startsWith('#')) {
    return matched;
  }
  return `url(${wrap}${await replacer(rawUrl)}${wrap})`;
}
