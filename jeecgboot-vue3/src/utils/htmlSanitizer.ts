import xss, { getDefaultWhiteList, safeAttrValue, type ICSSFilter, type IFilterXSSOptions, type IWhiteList } from 'xss';

const GLOBAL_RICH_TEXT_ATTRS = ['class', 'style', 'title'];
const DANGEROUS_TAG_BODIES = ['script', 'style', 'iframe', 'object', 'embed', 'form'];

function mergeAttrs(...attrs: Array<string[] | undefined>): string[] {
  return Array.from(new Set(attrs.flatMap((item) => item || [])));
}

function createRichTextWhiteList(): IWhiteList {
  const whiteList = getDefaultWhiteList();
  Object.keys(whiteList).forEach((tag) => {
    whiteList[tag] = mergeAttrs(whiteList[tag], GLOBAL_RICH_TEXT_ATTRS);
  });

  whiteList.a = mergeAttrs(whiteList.a, ['rel']);
  whiteList.img = mergeAttrs(whiteList.img, ['class', 'style']);
  whiteList.table = mergeAttrs(whiteList.table, ['cellspacing', 'cellpadding', 'height']);
  whiteList.td = mergeAttrs(whiteList.td, ['height']);
  whiteList.th = mergeAttrs(whiteList.th, ['height']);
  whiteList.span = mergeAttrs(whiteList.span, ['aria-hidden']);

  // Markdown 数学公式由 KaTeX 生成 MathML；仅开放展示所需标签和属性。
  const mathTags = [
    'semantics',
    'mrow',
    'mi',
    'mn',
    'mo',
    'ms',
    'mtext',
    'mspace',
    'mfrac',
    'msqrt',
    'mroot',
    'msup',
    'msub',
    'msubsup',
    'mover',
    'munder',
    'munderover',
    'mtable',
    'mtr',
    'mtd',
  ];
  whiteList.math = ['class', 'style', 'xmlns', 'display'];
  mathTags.forEach((tag) => {
    whiteList[tag] = ['class', 'style'];
  });
  whiteList.annotation = ['encoding'];

  // AI 对话的自定义标签只保留组件挂载所需结构，不开放事件属性。
  whiteList['jeecg-tool-exec'] = ['class'];
  whiteList['jeecg-chart'] = ['class'];
  whiteList.render = ['class', 'style'];
  whiteList.data = ['class'];
  return whiteList;
}

function sanitizeUrlAttr(tag: string, name: string, value: string, cssFilter: ICSSFilter): string {
  const sanitizedValue = safeAttrValue(tag, name, value, cssFilter);
  if (/^data:/i.test(sanitizedValue)) {
    // 排除 SVG 等可携带主动内容的格式，data URL 仅允许常见位图作为图片源。
    const isSafeImage = tag === 'img' && name === 'src' && /^data:image\/(?:png|jpe?g|gif|webp|bmp);base64,/i.test(sanitizedValue);
    return isSafeImage ? sanitizedValue : '';
  }
  return sanitizedValue;
}

const COMMENT_STYLE_RULES: Record<string, RegExp> = {
  position: /^absolute$/,
  top: /^-?\d+(?:\.\d+)?px$/,
  left: /^-?\d+(?:\.\d+)?px$/,
  width: /^\d+(?:\.\d+)?px$/,
  height: /^\d+(?:\.\d+)?px$/,
  'background-position': /^\d+(?:\.\d+)?%\s+\d+(?:\.\d+)?%$/,
};

function sanitizeCommentAttr(tag: string, name: string, value: string, cssFilter: ICSSFilter): string {
  if (name !== 'style') {
    return safeAttrValue(tag, name, value, cssFilter);
  }
  return value
    .split(';')
    .map((declaration) => declaration.split(':', 2).map((item) => item.trim()))
    .filter(([property, styleValue]) => property && styleValue && COMMENT_STYLE_RULES[property.toLowerCase()]?.test(styleValue))
    .map(([property, styleValue]) => `${property.toLowerCase()}:${styleValue}`)
    .join(';');
}

const richTextOptions: IFilterXSSOptions = {
  whiteList: createRichTextWhiteList(),
  stripIgnoreTag: true,
  stripIgnoreTagBody: DANGEROUS_TAG_BODIES,
  safeAttrValue: sanitizeUrlAttr,
};

const commentOptions: IFilterXSSOptions = {
  whiteList: {
    br: [],
    span: ['class', 'style'],
  },
  stripIgnoreTag: true,
  stripIgnoreTagBody: DANGEROUS_TAG_BODIES,
  safeAttrValue: sanitizeCommentAttr,
};

function normalizeHtml(value: unknown): string {
  if (value == null) {
    return '';
  }
  return typeof value === 'string' ? value : String(value);
}

/** 净化 Markdown 渲染结果或业务富文本，仅保留展示所需的安全标签和属性。 */
export function sanitizeRichText(value: unknown): string {
  return xss(normalizeHtml(value), richTextOptions);
}

/**
 * 转义 HTML 特殊字符，用于把纯文本安全地拼进 v-html
 * @author scott
 * @since 2026-10-31 【GHSA-867f-2922-wj26】Online 表单详情多行文本存储型 XSS
 */
export function escapeHtml(value: unknown): string {
  return normalizeHtml(value)
    .replace(/&/g, '&amp;')
    .replace(/</g, '&lt;')
    .replace(/>/g, '&gt;')
    .replace(/"/g, '&quot;')
    .replace(/'/g, '&#39;');
}

/** 净化评论预览和评论列表，只允许换行及系统生成的 emoji 标签。 */
export function sanitizeCommentHtml(value: unknown): string {
  //console.log("xss filter value:", value);
  return xss(normalizeHtml(value), commentOptions);
}
