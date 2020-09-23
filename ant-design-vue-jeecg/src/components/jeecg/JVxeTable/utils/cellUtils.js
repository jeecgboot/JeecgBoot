import { AllCells, JVXETypes } from '@/components/jeecg/JVxeTable'
import JVxeCellMixins from '../mixins/JVxeCellMixins'

export const JVXERenderType = {
  editer: 'editer',
  spaner: 'spaner',
  default: 'default',
}

/** 安装所有vxe组件 */
export function installAllCell(VXETable) {
  // 遍历所有组件批量注册
  Object.keys(AllCells).forEach(type => installOneCell(VXETable, type))
}

/** 安装单个vxe组件 */
export function installOneCell(VXETable, type) {
  const switches = getEnhancedMixins(type, 'switches')
  if (switches.editRender === false) {
    installCellRender(VXETable, type, AllCells[type])
  } else {
    installEditRender(VXETable, type, AllCells[type])
  }
}

/** 注册可编辑组件 */
export function installEditRender(VXETable, type, comp, spanComp) {
  // 获取当前组件的增强
  const enhanced = getEnhancedMixins(type)
  // span 组件
  if (!spanComp && AllCells[type + ':span']) {
    spanComp = AllCells[type + ':span']
  } else {
    spanComp = AllCells[JVXETypes.normal]
  }
  // 添加渲染
  VXETable.renderer.add(JVXETypes._prefix + type, {
    // 可编辑模板
    renderEdit: createRender(comp, enhanced, JVXERenderType.editer),
    // 显示模板
    renderCell: createRender(spanComp, enhanced, JVXERenderType.spaner),
    // 增强注册
    ...enhanced.installOptions,
  })
}

/** 注册普通组件 */
export function installCellRender(VXETable, type, comp = AllCells[JVXETypes.normal]) {
  // 获取当前组件的增强
  const enhanced = getEnhancedMixins(type)
  VXETable.renderer.add(JVXETypes._prefix + type, {
    // 默认显示模板
    renderDefault: createRender(comp, enhanced, JVXERenderType.default),
    // 增强注册
    ...enhanced.installOptions,
  })
}

function createRender(comp, enhanced, renderType) {
  return function (h, renderOptions, params) {
    return [h(comp, {
      props: {
        value: params.row[params.column.property],
        row: params.row,
        column: params.column,
        params: params,
        renderOptions: renderOptions,
        renderType: renderType,
      }
    })]
  }
}

// 已混入的组件增强
const AllCellsMixins = new Map()

/** 获取某个组件的增强 */
export function getEnhanced(type) {
  let cell = AllCells[type]
  if (cell && cell.enhanced) {
    return cell.enhanced
  }
  return null
}

/**
 * 获取某个组件的增强（混入默认值）
 *
 * @param type JVXETypes
 * @param name 可空，增强名称，留空返回所有增强
 */
export function getEnhancedMixins(type, name) {
  const getByName = (e) => name ? e[name] : e
  if (AllCellsMixins.has(type)) {
    return getByName(AllCellsMixins.get(type))
  }
  let defEnhanced = JVxeCellMixins.enhanced
  let enhanced = getEnhanced(type)
  if (enhanced) {
    Object.keys(defEnhanced).forEach(key => {
      let def = defEnhanced[key]
      if (enhanced.hasOwnProperty(key)) {
        // 方法如果存在就不覆盖
        if (typeof def !== 'function' && typeof def !== 'string') {
          enhanced[key] = Object.assign({}, def, enhanced[key])
        }
      } else {
        enhanced[key] = def
      }
    })
    AllCellsMixins.set(type, enhanced)
    return getByName(enhanced)
  }
  AllCellsMixins.set(type, defEnhanced)
  return getByName(defEnhanced)
}


/** 辅助方法：替换${...}变量 */
export function replaceProps(col, value) {
  if (value && typeof value === 'string') {
    let text = value
    text = text.replace(/\${title}/g, col.title)
    text = text.replace(/\${key}/g, col.key)
    text = text.replace(/\${defaultValue}/g, col.defaultValue)
    return text
  }
  return value
}
