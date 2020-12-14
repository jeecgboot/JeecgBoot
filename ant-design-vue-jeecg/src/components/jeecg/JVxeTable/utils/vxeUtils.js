import { getVmParentByName } from '@/utils/util'
import { JVXETypes } from '@comp/jeecg/JVxeTable/index'

export const VALIDATE_FAILED = Symbol()

/**
 * 获取指定的 $refs 对象
 * 有时候可能会遇到组件未挂载到页面中的情况，导致无法获取 $refs 中的某个对象
 * 这个方法可以等待挂载完成之后再返回 $refs 的对象，避免报错
 * @author sunjianlei
 **/
export function getRefPromise(vm, name) {
  return new Promise((resolve) => {
    (function next() {
      let ref = vm.$refs[name]
      if (ref) {
        resolve(ref)
      } else {
        setTimeout(() => {
          next()
        }, 10)
      }
    })()
  })
}

/** 获取某一数字输入框列中的最大的值 */
export function getInputNumberMaxValue(col, rowsValues) {
  let maxNum = 0
  Object.values(rowsValues).forEach((rowValue, index) => {
    let val = rowValue[col.key], num
    try {
      num = Number.parseFloat(val)
    } catch {
      num = 0
    }
    // 把首次循环的结果当成最大值
    if (index === 0) {
      maxNum = num
    } else {
      maxNum = (num > maxNum) ? num : maxNum
    }
  })
  return maxNum
}

/**
 *
 * 根据 tagName 获取父级节点
 *
 * @param dom 一级dom节点
 * @param tagName 标签名，不区分大小写
 * @return {HTMLElement | NULL}
 */
export function getParentNodeByTagName(dom, tagName = 'body') {
  if (tagName === 'body') {
    return document.body
  }
  if (dom.parentNode) {
    if (dom.parentNode.tagName.toLowerCase() === tagName.trim().toLowerCase()) {
      return dom.parentNode
    } else {
      return getParentNodeByTagName(dom.parentNode, tagName)
    }
  } else {
    return null
  }
}

/**
 * vxe columns 封装成高级查询可识别的选项
 * @param columns
 * @param handler 单独处理方法
 */
export function vxePackageToSuperQuery(columns, handler) {
  if (Array.isArray(columns)) {
    // 高级查询所需要的参数
    let fieldList = []
    // 遍历列
    for (let i = 0; i < columns.length; i++) {
      let col = columns[i]
      if (col.type === JVXETypes.rowCheckbox ||
        col.type === JVXETypes.rowRadio ||
        col.type === JVXETypes.rowExpand ||
        col.type === JVXETypes.rowNumber
      ) {
        continue
      }
      let field = {
        type: 'string',
        value: col.key,
        text: col.title,
        dictCode: col.dictCode || col.dict,
      }
      if (col.type === JVXETypes.date || col.type === JVXETypes.datetime) {
        field.type = col.type
        field.format = col.format
      }
      if (col.type === JVXETypes.inputNumber) {
        field.type = 'int'
      }
      if (Array.isArray(col.options)) {
        field.options = col.options
      }
      if (typeof handler === 'function') {
        Object.assign(field, handler(col, idx))
      }
      fieldList.push(field)
    }
    return fieldList
  } else {
    console.error('columns必须是一个数组')
  }
  return null
}

/**
 * 一次性验证主表单和所有的次表单
 * @param form 主表单 form 对象
 * @param cases 接收一个数组，每项都是一个JVxeTable实例
 * @param autoJumpTab
 * @returns {Promise<any>}
 * @author sunjianlei
 */
export async function validateFormAndTables(form, cases, autoJumpTab) {
  if (!(form && typeof form.validateFields === 'function')) {
    throw `form 参数需要的是一个form对象，而传入的却是${typeof form}`
  }
  let dataMap = {}
  let values = await new Promise((resolve, reject) => {
    // 验证主表表单
    form.validateFields((err, values) => {
      err ? reject({error: VALIDATE_FAILED, originError: err}) : resolve(values)
    })
  })
  Object.assign(dataMap, {formValue: values})
  // 验证所有子表的表单
  let subData = await validateTables(cases, autoJumpTab)
  // 合并最终数据
  dataMap = Object.assign(dataMap, {tablesValue: subData})
  return dataMap
}

/**
 * 验证并获取一个或多个表格的所有值
 *
 * @param cases 接收一个数组，每项都是一个JVxeTable实例
 * @param autoJumpTab 校验失败后，是否自动跳转tab选项
 */
export function validateTables(cases, autoJumpTab = true) {
  if (!Array.isArray(cases)) {
    throw `'validateTables'函数的'cases'参数需要的是一个数组，而传入的却是${typeof cases}`
  }
  return new Promise((resolve, reject) => {
    let tablesData = []
    let index = 0
    if (!cases || cases.length === 0) {
      resolve()
    }
    (function next() {
      let vm = cases[index]
      vm.validateTable().then(errMap => {
        // 校验通过
        if (!errMap) {
          tablesData[index] = vm.getAll()
          // 判断校验是否全部完成，完成返回成功，否则继续进行下一步校验
          if (++index === cases.length) {
            resolve(tablesData)
          } else (
            next()
          )
        } else {
          // 尝试获取tabKey，如果在ATab组件内即可获取
          let paneKey
          let tabPane = getVmParentByName(vm, 'ATabPane')
          if (tabPane) {
            paneKey = tabPane.$vnode.key
            // 自动跳转到该表格
            if (autoJumpTab) {
              let tabs = getVmParentByName(tabPane, 'Tabs')
              tabs && tabs.setActiveKey && tabs.setActiveKey(paneKey)
            }
          }
          // 出现未验证通过的表单，不再进行下一步校验，直接返回失败
          reject({error: VALIDATE_FAILED, index, paneKey, errMap})
        }
      })
    })()
  })
}