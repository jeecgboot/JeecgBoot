import * as jvxeTypes from './jvxeTypes'
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
import JVxeDepartSelectCell from './components/cells/JVxeDepartSelectCell'
import JVxeUserSelectCell from './components/cells/JVxeUserSelectCell'

//update--begin--autor:lvdandan-----date:20201216------for：JVxeTable--JVXETypes 【online】代码结构调整，便于online打包
// 组件类型
export const JVXETypes = jvxeTypes.JVXETypes
//update--end--autor:lvdandan-----date:20201216------for：JVxeTable--JVXETypes 【online】代码结构调整，便于online打包

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
  ...mapCell(JVXETypes.departSelect, JVxeDepartSelectCell),
  ...mapCell(JVXETypes.userSelect, JVxeUserSelectCell)

  /* hidden 是特殊的组件，不在这里注册 */
}

export { installCell, mapCell }

export default JVxeTable