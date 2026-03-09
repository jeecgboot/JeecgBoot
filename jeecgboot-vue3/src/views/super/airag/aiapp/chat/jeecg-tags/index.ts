import type { JeecgTag } from './types';
import { shallowRef } from 'vue';

import ToolExecTag from './tool-exec';
import JeecgChart from "./jeecg-chart";

export const jeecgTagMap: Map<string, JeecgTag> = new Map();
// 所有 jeecg 标签名称列表
export const tagNames: string[] = [];

// 注册 工具调用 标签
useJeecgTag(ToolExecTag);
// 注册 图表渲染 标签
useJeecgTag(JeecgChart);

// jeecg 标签统一的 class 名称
export const JEECG_TAG_CLASS = 'jeecg-tag';

/**
 * 忽略 jeecg 自定义标签的解析
 * @param md
 */
export function mdPluginJeecgTag(md: any) {
  // 保存原始的 html_block  渲染规则
  const htmlBlockOrigin =
    md.renderer.rules.html_block ||
    function (tokens, idx) {
      return tokens[idx].content;
    };

  // 覆盖 html_block  渲染规则
  md.renderer.rules.html_block = function (tokens, idx) {
    const token = tokens[idx];
    const content = token.content;

    let isJeecgTag = false;
    let tagName = '';

    for (const name of tagNames) {
      // 检查内容是否包含自定义标签的起始或结束标签
      const startTag = new RegExp(`<${name}(\\s|>)`, 'i');
      const endTag = new RegExp(`</${name}>`, 'i');
      if (startTag.test(content) || endTag.test(content)) {
        isJeecgTag = true;
        tagName = name;
        break;
      }
    }

    // jeecg 自定义标签
    if (isJeecgTag) {
      const box = document.createElement('div');
      box.innerHTML = content;
      const tag = box.firstElementChild!;
      tag.classList.add(JEECG_TAG_CLASS);
      return tag.outerHTML;
    }

    // 其他 HTML 标签按默认方式渲染
    return htmlBlockOrigin(tokens, idx);
  };
}

export function useJeecgTag(tag: JeecgTag) {
  if (jeecgTagMap.has(tag.name)) {
    return;
  }
  tag.component = shallowRef(tag.component);
  jeecgTagMap.set(tag.name, tag);
  tagNames.push(tag.name);
}
