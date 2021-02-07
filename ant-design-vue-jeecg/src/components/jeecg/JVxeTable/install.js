import Vue from 'vue'
import { getEventPath } from '@/utils/util'
import JVxeTable, { AllCells, JVXETypes } from './index'
import './less/j-vxe-table.less'
// 引入 vxe-table
import 'xe-utils'
import VXETable, { Grid } from 'vxe-table'
import VXETablePluginAntd from 'vxe-table-plugin-antd'
import 'vxe-table/lib/index.css'
import 'vxe-table-plugin-antd/dist/style.css'
import { getEnhancedMixins, installAllCell, installOneCell } from '@/components/jeecg/JVxeTable/utils/cellUtils'

// VxeGrid所有的方法映射
const VxeGridMethodsMap = {}
Object.keys(Grid.methods).forEach(key => {
  // 使用eval可以避免闭包（但是要注意不要写es6的代码）
  VxeGridMethodsMap[key] = eval(`(function(){return this.$refs.vxe.${key}.apply(this.$refs.vxe,arguments)})`)
})
// 将Grid所有的方法都映射（继承）到JVxeTable上
JVxeTable.methods = Object.assign({}, VxeGridMethodsMap, JVxeTable.methods)

// VXETable 全局配置
const VXETableSettings = {
  // z-index 起始值
  zIndex: 1000,
  table: {
    validConfig: {
      // 校验提示方式：强制使用tooltip
      message: 'tooltip'
    }
  }
}

// 执行注册方法
Vue.use(VXETable, VXETableSettings)
VXETable.use(VXETablePluginAntd)
Vue.component(JVxeTable.name, JVxeTable)

// 注册自定义组件
installAllCell(VXETable)

// 添加事件拦截器 event.clearActived
// 比如点击了某个组件的弹出层面板之后，此时被激活单元格不应该被自动关闭，通过返回 false 可以阻止默认的行为。
VXETable.interceptor.add('event.clearActived', function (params, event, target) {
  // 获取组件增强
  let col = params.column.own
  const interceptor = getEnhancedMixins(col.$type, 'interceptor')
  // 执行增强
  let flag = interceptor['event.clearActived'].apply(this, arguments)
  if (flag === false) {
    return false
  }

  let path = getEventPath(event)
  for (let p of path) {
    let className = p.className || ''
    className = typeof className === 'string' ? className : className.toString()

    /* --- 特殊处理以下组件，点击以下标签时不清空编辑状态 --- */

    // 点击的标签是JInputPop
    if (className.includes('j-input-pop')) {
      return false
    }
    // 点击的标签是JPopup的弹出层、部门选择、用户选择
    if (className.includes('j-popup-modal') || className.includes('j-depart-select-modal') || className.includes('j-user-select-modal')) {
      return false
    }
    // 执行增强
    let flag = interceptor['event.clearActived.className'].apply(this, [className, ...arguments])
    if (flag === false) {
      return false
    }
  }
})

/**
 * 注册map
 * @param type 类型
 * @param cell 输入组件
 * @param span 显示组件，可空，默认为 JVxeNormalCell 组件
 */
export function mapCell(type, cell, span) {
  let cells = {[type]: cell}
  if (span) {
    cells[type + ':span'] = span
  }
  return cells
}

/**
 * 注册自定义组件
 *
 * @param type 类型
 * @param cell 输入组件
 * @param span 显示组件，可空，默认为 JVxeNormalCell 组件
 */
export function installCell(type, cell, span) {
  let exclude = [JVXETypes.rowNumber, JVXETypes.rowCheckbox, JVXETypes.rowRadio, JVXETypes.rowExpand, JVXETypes.rowDragSort]
  if (exclude.includes(type)) {
    throw new Error(`【installCell】不能使用"${type}"作为组件的type，因为这是关键字。`)
  }
  Object.assign(AllCells, mapCell(type, cell, span))
  installOneCell(VXETable, type)
}
