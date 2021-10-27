// 组件类型
export default JVXETypes
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
  time: 'time',
  checkbox: 'checkbox',
  upload: 'upload',
  // 下拉搜索
  selectSearch: 'select-search',
  // 下拉多选
  selectMultiple: 'select-multiple',
  // 进度条
  progress: 'progress',
  //部门选择
  departSelect: 'sel_depart',
  //用户选择
  userSelect: 'sel_user',

  // 拖轮Tags（暂无用）
  tags: 'tags',

  slot: 'slot',
  normal: 'normal',
  hidden: 'hidden',
}
