import type { ResolvedConfig } from 'vite';
import { createHash } from 'crypto';
import CleanCSS from 'clean-css';
import {
  commentRE,
  cssBlockRE,
  ruleRE,
  cssValueRE,
  safeEmptyRE,
  importSafeRE,
} from './constants';

export type ResolveSelector = (selector: string) => string;

export function getVariablesReg(colors: string[]) {
  return new RegExp(
    colors
      .map(
        (i) =>
          `(${i
            .replace(/\s/g, ' ?')
            .replace(/\(/g, `\\(`)
            .replace(/\)/g, `\\)`)
            .replace(/0?\./g, `0?\\.`)})`,
      )
      .join('|'),
  );
}

export function combineRegs(decorator = '', joinString = '', ...args: any[]) {
  const regString = args
    .map((item) => {
      const str = item.toString();
      return `(${str.slice(1, str.length - 1)})`;
    })
    .join(joinString);
  return new RegExp(regString, decorator);
}

export function formatCss(s: string) {
  s = s.replace(/\s*([{}:;,])\s*/g, '$1');
  s = s.replace(/;\s*;/g, ';');
  s = s.replace(/,[\s.#\d]*{/g, '{');
  s = s.replace(/([^\s])\{([^\s])/g, '$1 {\n\t$2');
  s = s.replace(/([^\s])\}([^\n]*)/g, '$1\n}\n$2');
  s = s.replace(/([^\s]);([^\s}])/g, '$1;\n\t$2');
  return s;
}

export function createFileHash() {
  return createHash('sha256').update(Date.now().toString()).digest('hex').substr(0, 8);
}

/**
 * Compress the generated css.
 * vite 8 已移除 config.build.cleanCssOptions，这里直接使用 CleanCSS 默认配置。
 */
export async function minifyCSS(css: string, config: ResolvedConfig) {
  const res = new CleanCSS({ rebase: false }).minify(css);

  if (res.errors && res.errors.length) {
    console.error(`error when minifying css:\n${res.errors}`);
    throw res.errors[0];
  }

  if (res.warnings && res.warnings.length) {
    config.logger.warn(`warnings when minifying css:\n${res.warnings}`);
  }

  return res.styles;
}

// Used to extract relevant color configuration in css
export function extractVariable(
  code: string,
  colorVariables: string[],
  resolveSelector?: ResolveSelector,
  colorRE?: RegExp,
): string {
  colorVariables = Array.from(new Set(colorVariables));
  code = code.replace(commentRE, '');

  const cssBlocks = code.match(cssBlockRE);
  if (!cssBlocks || cssBlocks.length === 0) {
    return '';
  }

  let allExtractedVariable = '';

  const variableReg = getVariablesReg(colorVariables);

  for (let index = 0; index < cssBlocks.length; index++) {
    const cssBlock = cssBlocks[index];
    if (!variableReg.test(cssBlock) || !cssBlock) continue;

    const cssSelector = cssBlock.match(/[^{]*/)?.[0] ?? '';
    if (!cssSelector) continue;

    if (/^@.*keyframes/.test(cssSelector)) {
      allExtractedVariable += `${cssSelector}{${extractVariable(
        cssBlock.replace(/[^{]*\{/, '').replace(/}$/, ''),
        colorVariables,
        resolveSelector,
        colorRE,
      )}}`;
      continue;
    }

    const colorReg = combineRegs(
      'g',
      '',
      ruleRE,
      cssValueRE,
      safeEmptyRE,
      variableReg,
      importSafeRE,
    );

    const colorReplaceTemplates = cssBlock.match(colorRE || colorReg);

    if (!colorReplaceTemplates) continue;

    allExtractedVariable += `${
      resolveSelector ? resolveSelector(cssSelector) : cssSelector
    } {${colorReplaceTemplates.join(';')}}`;
  }

  return allExtractedVariable;
}
