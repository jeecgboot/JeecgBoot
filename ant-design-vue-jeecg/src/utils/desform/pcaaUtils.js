/*
 * 省市区联动组件通用工具类。
 * 列表翻译、组件反推、表单设计器组件反推等功能都可以用该工具类实现。
 *
 * 1. leafName字段的意义：
 *      AntdvUI和ElementUI的叶级节点名字是不一样的，
 *      AntdvUI的名字是 isLeaf，ElementUI是 leaf，
 *      默认是AntdvUI的名字，在表单设计器那边需要手动 setLeafName
 */

import { pcaa } from 'area-data'

export { pcaa }

/** 根节点Code = 86 */
export const ROOT_CODE = '86'

/** 叶级节点的名字 */
let leafName = 'isLeaf'

/**
 * set leafName
 * @param $leafName
 */
export function setLeafName($leafName = 'isLeaf') {
  leafName = $leafName
}

/**
 *  将地区数据转换成下拉框等组件可识别的Options数组
 * @param data 地区data
 * @param labelName label标签名字
 * @return
 */
export function transToOptions(data, labelName = 'label') {
  if (data) {
    return Object.keys(data).map(key => ({ value: key, [labelName]: data[key] }))
  } else {
    return []
  }
}

/**
 * 获取子级Data对象
 *
 * @param code 父级地区Code
 */
export function getChildrenDataByCode(code) {
  return pcaa[code]
}

/**
 * 获取子级Options对象
 *
 * @param code 父级地区Code
 * @return {Array} 返回的值一定是一个数组，如果数组length===0，则代表没有子级
 */
export function getChildrenOptionsByCode(code) {
  let options = []
  let data = getChildrenDataByCode(code)
  if (data) {
    for (let key in data) {
      if (data.hasOwnProperty(key)) {
        options.push({ value: key, label: data[key], })
      }
    }
    return options
  } else {
    return []
  }
}

/**
 * 获取兄弟Data对象
 *
 * @param code 地区Code
 */
export function getSiblingsDataByCode(code) {
  if (typeof code === 'string' && code.length === 6) {
    // 父级节点Code
    let parentCode = `${code.substring(0, 4)}00`
    return getChildrenDataByCode(parentCode)
  } else {
    console.warn('[getSiblingsByCode]: code不合法')
    return null
  }
}

/**
 * 获取对应的 Label
 *
 * @param code 地区Code
 */
export function getLabelByCode(code) {
  if (code) {
    // 获取当前code所有的兄弟节点
    let siblingsData = getSiblingsDataByCode(code)
    // 然后取出自己的值
    return siblingsData[code]
  } else {
    return code
  }
}

/**
 * 获取所有的 Label，包括自身和所有父级
 *
 * @param code 地区Code
 * @param joinText 合并文本
 */
export function getAllLabelByCode(code, joinText = ' / ') {
  if (code) {
    let { labels } = getAllParentByCode(code)
    return labels.join(joinText)
  } else {
    return code
  }
}

/**
 * 通过子级 code 反推所有父级的 code
 *
 * @param code 子级地区Code
 * @returns {Object} options: 所有父级的选项；codes: 所有父级的code；labels: 所有父级的显示名
 */
export function getAllParentByCode(code) {
  code = (typeof code === 'string' ? code : '').trim()
  if (code.length === 0) {
    return { options: [], codes: [], labels: [] }
  }
  // 获取第一级数据
  let rootOptions = getChildrenOptionsByCode(ROOT_CODE)
  hasChildren(rootOptions)
  // 父级code数组，code长度
  let parentCodes = [code], length = code.length
  // 父级label数组
  let parentLabels = [getLabelByCode(code)]
  // 级别，位数，是否继续循环
  let level = 1, num = 2, flag = true

  let options = rootOptions
  do {
    let endIndex = num * level++
    // 末尾补零个数
    let zeroPadding = [...new Array(length - endIndex)].map(i => '0').join('')
    // 裁剪并补零（获取上级的方式就是将当前code的后两位变成 00）
    let parentCode = code.substring(0, endIndex) + zeroPadding
    // 是否找到在选项中的位置
    let findIt = false
    for (let option of options) {
      if (option.value === parentCode) {
        if (option[leafName]) {
          flag = false
        } else {
          let children = getChildrenOptionsByCode(option.value)
          hasChildren(children)
          option.children = children
          options = children
          parentCodes.splice(parentCodes.length - 1, 0, option.value)
          parentLabels.splice(parentLabels.length - 1, 0, option.label)
        }
        findIt = true
        break
      }
    }

    if (findIt) {
      findIt = false
    } else {
      flag = false
    }
  } while (flag)
  return { options: rootOptions, codes: parentCodes, labels: parentLabels }
}

/**
 * 判断所有的项是否有子节点
 *
 * @param options
 */
export function hasChildren(options) {
  options.forEach(option => {
    option[leafName] = getChildrenOptionsByCode(option.value).length === 0
  })
}
