/** 组件类型 */
export enum JVxeTypes {
  // 行号列
  rowNumber = 'row-number',
  // 选择列
  rowCheckbox = 'row-checkbox',
  // 单选列
  rowRadio = 'row-radio',
  // 展开列
  rowExpand = 'row-expand',
  // 上下排序
  rowDragSort = 'row-drag-sort',

  input = 'input',
  inputNumber = 'input-number',
  textarea = 'textarea',
  select = 'select',
  date = 'date',
  datetime = 'datetime',
  time = 'time',
  checkbox = 'checkbox',
  upload = 'upload',
  // 下拉搜索
  selectSearch = 'select-search',
  // 下拉多选
  selectMultiple = 'select-multiple',
  // 进度条
  progress = 'progress',
  //部门选择
  departSelect = 'depart-select',
  //用户选择
  userSelect = 'user-select',

  // 拖轮Tags（暂无用）
  tags = 'tags', // TODO 待实现

  slot = 'slot',
  normal = 'normal',
  hidden = 'hidden',

  // 以下为自定义组件
  popup = 'popup',
  selectDictSearch = 'selectDictSearch',
  radio = 'radio',
  image = 'image',
  file = 'file',
  // 省市区
  pca = 'pca',
}

// 为了防止和 vxe 内置的类型冲突，所以加上一个前缀
// 前缀是自动加的，代码中直接用就行（JVxeTypes.input）
export const JVxeTypePrefix = 'j-';

/** VxeTable 渲染类型 */
export enum JVxeRenderType {
  editer = 'editer',
  spaner = 'spaner',
  default = 'default',
}
