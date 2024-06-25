import type { JVxeVueComponent } from './types';
import { JVxeTypes } from './types/JVxeTypes';

import JVxeSlotCell from './components/cells/JVxeSlotCell';
import JVxeNormalCell from './components/cells/JVxeNormalCell.vue';
import JVxeDragSortCell from './components/cells/JVxeDragSortCell.vue';

import JVxeInputCell from './components/cells/JVxeInputCell.vue';
import JVxeDateCell from './components/cells/JVxeDateCell.vue';
import JVxeTimeCell from './components/cells/JVxeTimeCell.vue';
import JVxeSelectCell from './components/cells/JVxeSelectCell.vue';
import JVxeRadioCell from './components/cells/JVxeRadioCell.vue';
import JVxeCheckboxCell from './components/cells/JVxeCheckboxCell.vue';
import JVxeUploadCell from './components/cells/JVxeUploadCell.vue';
// import { TagsInputCell, TagsSpanCell } from './components/cells/JVxeTagsCell.vue'
import JVxeProgressCell from './components/cells/JVxeProgressCell.vue';
import JVxeTextareaCell from './components/cells/JVxeTextareaCell.vue';
// import JVxeDepartSelectCell from './components/cells/JVxeDepartSelectCell.vue'
// import JVxeUserSelectCell from './components/cells/JVxeUserSelectCell.vue'

let componentMap = new Map<JVxeTypes | string, JVxeVueComponent>();
// update-begin--author:liaozhiyang---date:20231208---for：【issues/860】生成的一对多代码，热更新之后点击新增卡死[暂时先解决]
const JVxeComponents = 'JVxeComponents__';
if (import.meta.env.DEV && componentMap.size === 0 && window[JVxeComponents] && window[JVxeComponents].size > 0) {
  componentMap = window[JVxeComponents];
}
// update-end--author:liaozhiyang---date:20231027---for：【issues/860】生成的一对多代码，热更新之后点击新增卡死[暂时先解决]
/** span 组件结尾 */
export const spanEnds: string = ':span';

/** 定义不能用于注册的关键字 */
export const excludeKeywords: Array<JVxeTypes> = [
  JVxeTypes.hidden,
  JVxeTypes.rowNumber,
  JVxeTypes.rowCheckbox,
  JVxeTypes.rowRadio,
  JVxeTypes.rowExpand,
];

/**
 * 注册组件
 *
 * @param type 组件 type
 * @param component Vue组件
 * @param spanComponent 显示组件，可空，默认为 JVxeNormalCell 组件
 */
export function addComponent(type: JVxeTypes, component: JVxeVueComponent, spanComponent?: JVxeVueComponent) {
  if (excludeKeywords.includes(type)) {
    throw new Error(`【addComponent】不能使用"${type}"作为组件的name，因为这是关键字。`);
  }
  if (componentMap.has(type)) {
    throw new Error(`【addComponent】组件"${type}"已存在`);
  }
  componentMap.set(type, component);
  if (spanComponent) {
    componentMap.set(type + spanEnds, spanComponent);
  }
  // update-begin--author:liaozhiyang---date:20231208---for：【issues/860】生成的一对多代码，热更新之后点击新增卡死[暂时先解决]
  import.meta.env.DEV && (window[JVxeComponents] = componentMap);
  // update-end--author:liaozhiyang---date:20231208---for：【issues/860】生成的一对多代码，热更新之后点击新增卡死[暂时先解决]
}

export function deleteComponent(type: JVxeTypes) {
  componentMap.delete(type);
  componentMap.delete(type + spanEnds);
  // update-begin--author:liaozhiyang---date:20231208---for：【issues/860】生成的一对多代码，热更新之后点击新增卡死[暂时先解决]
  import.meta.env.DEV && (window[JVxeComponents] = componentMap);
  // update-end--author:liaozhiyang---date:20231208---for：【issues/860】生成的一对多代码，热更新之后点击新增卡死[暂时先解决]
}

/** 定义内置自定义组件 */
export function definedComponent() {
  addComponent(JVxeTypes.slot, JVxeSlotCell);
  addComponent(JVxeTypes.normal, JVxeNormalCell);
  addComponent(JVxeTypes.rowDragSort, JVxeDragSortCell);

  addComponent(JVxeTypes.input, JVxeInputCell);
  addComponent(JVxeTypes.inputNumber, JVxeInputCell);
  addComponent(JVxeTypes.radio, JVxeRadioCell);
  addComponent(JVxeTypes.checkbox, JVxeCheckboxCell);
  addComponent(JVxeTypes.select, JVxeSelectCell);
  addComponent(JVxeTypes.selectSearch, JVxeSelectCell); // 下拉搜索
  addComponent(JVxeTypes.selectMultiple, JVxeSelectCell); // 下拉多选
  addComponent(JVxeTypes.date, JVxeDateCell);
  addComponent(JVxeTypes.datetime, JVxeDateCell);
  addComponent(JVxeTypes.time, JVxeTimeCell);
  addComponent(JVxeTypes.upload, JVxeUploadCell);
  addComponent(JVxeTypes.textarea, JVxeTextareaCell);

  // addComponent(JVxeTypes.tags, TagsInputCell, TagsSpanCell)
  addComponent(JVxeTypes.progress, JVxeProgressCell);

  // addComponent(JVxeTypes.departSelect, JVxeDepartSelectCell)
  // addComponent(JVxeTypes.userSelect, JVxeUserSelectCell)
}

export { componentMap };
