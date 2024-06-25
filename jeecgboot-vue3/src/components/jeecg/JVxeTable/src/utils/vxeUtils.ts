/**
 *
 * 根据 tagName 获取父级节点
 *
 * @param dom 一级dom节点
 * @param tagName 标签名，不区分大小写
 */
export function getParentNodeByTagName(dom: HTMLElement, tagName: string = 'body'): HTMLElement | null {
  if (tagName === 'body') {
    return document.body;
  }
  if (dom.parentElement) {
    if (dom.parentElement.tagName.toLowerCase() === tagName.trim().toLowerCase()) {
      return dom.parentElement;
    } else {
      return getParentNodeByTagName(dom.parentElement, tagName);
    }
  } else {
    return null;
  }
}
