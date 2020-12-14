import { installCell, mapCell } from './install'
import JVxeTable from './components/JVxeTable'

import JVxeSlotCell from './components/cells/JVxeSlotCell'
import JVxeNormalCell from './components/cells/JVxeNormalCell'
import JVxeInputCell from './components/cells/JVxeInputCell'
import JVxeDateCell from './components/cells/JVxeDateCell'
import JVxeSelectCell from './components/cells/JVxeSelectCell'
import JVxeCheckboxCell from './components/cells/JVxeCheckboxCell'
import JVxeUploadCell from './components/cells/JVxeUploadCell'
import { TagsInputCell, TagsSpanCell } from './components/cells/JVxeTagsCell'
import JVxeProgressCell from './components/cells/JVxeProgressCell'
import JVxeTextareaCell from './components/cells/JVxeTextareaCell'
import JVxeDragSortCell from './components/cells/JVxeDragSortCell'

// 组件类型
export const JVXETypes = {
  // 为了防止和 vxe 内置的类型冲突，所以加上一个前缀
  // 前缀是自动加的，代码中直接用就行（JVXETypes.input）
  _prefix: 'j-',

  // 行号列
  rowNumber: 'row-number',
  // 选择列
  rowCheckbox: 'row-checkbox',
  // 单选列
  rowRadio: 'row-radio',
  // 展开列
  rowExpand: 'row-expand',
  // 上下排序
  rowDragSort: 'row-drag-sort',

  input: 'input',
  inputNumber: 'inputNumber',
  textarea: 'textarea',
  select: 'select',
  date: 'date',
  datetime: 'datetime',
  checkbox: 'checkbox',
  upload: 'upload',
  // 下拉搜索
  selectSearch: 'select-search',
  // 下拉多选
  selectMultiple: 'select-multiple',
  // 进度条
  progress: 'progress',

  // 拖轮Tags（暂无用）
  tags: 'tags',

  slot: 'slot',
  normal: 'normal',
  hidden: 'hidden',
}

// 注册自定义组件
export const AllCells = {
  ...mapCell(JVXETypes.normal, JVxeNormalCell),
  ...mapCell(JVXETypes.input, JVxeInputCell),
  ...mapCell(JVXETypes.inputNumber, JVxeInputCell),
  ...mapCell(JVXETypes.checkbox, JVxeCheckboxCell),
  ...mapCell(JVXETypes.select, JVxeSelectCell),
  ...mapCell(JVXETypes.selectSearch, JVxeSelectCell),  // 下拉搜索
  ...mapCell(JVXETypes.selectMultiple, JVxeSelectCell),  // 下拉多选
  ...mapCell(JVXETypes.date, JVxeDateCell),
  ...mapCell(JVXETypes.datetime, JVxeDateCell),
  ...mapCell(JVXETypes.upload, JVxeUploadCell),
  ...mapCell(JVXETypes.textarea, JVxeTextareaCell),

  ...mapCell(JVXETypes.tags, TagsInputCell, TagsSpanCell),
  ...mapCell(JVXETypes.progress, JVxeProgressCell),

  ...mapCell(JVXETypes.rowDragSort, JVxeDragSortCell),
  ...mapCell(JVXETypes.slot, JVxeSlotCell),

  /* hidden 是特殊的组件，不在这里注册 */
}

export { installCell, mapCell }

export default JVxeTable