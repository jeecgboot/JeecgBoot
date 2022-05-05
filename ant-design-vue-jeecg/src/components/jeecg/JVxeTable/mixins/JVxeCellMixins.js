import PropTypes from 'ant-design-vue/es/_util/vue-types'
import { filterDictText } from '@/components/dict/JDictSelectUtil'
import { getEnhancedMixins, JVXERenderType, replaceProps } from '@/components/jeecg/JVxeTable/utils/cellUtils'

// noinspection JSUnusedLocalSymbols
export default {
  inject: {
    getParentContainer: {default: () => ((node) => node.parentNode)},
  },
  props: {
    value: PropTypes.any,
    row: PropTypes.object,
    column: PropTypes.object,
    // 组件参数
    params: PropTypes.object,
    // 渲染选项
    renderOptions: PropTypes.object,
    // 渲染类型
    renderType: PropTypes.string.def('default'),
  },
  data() {
    return {
      innerValue: null,
    }
  },
  computed: {
    caseId() {
      return this.renderOptions.caseId
    },
    originColumn() {
      return this.column.own
    },
    $type() {
      return this.originColumn.$type
    },
    rows() {
      return this.params.data
    },
    fullDataLength() {
      return this.params.$table.tableFullData.length
    },
    rowIndex() {
      return this.params.rowIndex
    },
    columnIndex() {
      return this.params.columnIndex
    },
    cellProps() {
      let {originColumn: col, renderOptions} = this

      let props = {}

      // 输入占位符
      props['placeholder'] = replaceProps(col, col.placeholder)

      // 解析props
      if (typeof col.props === 'object') {
        Object.keys(col.props).forEach(key => {
          props[key] = replaceProps(col, col.props[key])
        })
      }

      // 判断是否是禁用的列
      props['disabled'] = (typeof col['disabled'] === 'boolean' ? col['disabled'] : props['disabled'])

      // TODO 判断是否是禁用的行
      // if (props['disabled'] !== true) {
      //   props['disabled'] = ((this.disabledRowIds || []).indexOf(row.id) !== -1)
      // }

      // 判断是否禁用所有组件
      if (renderOptions.disabled === true) {
        props['disabled'] = true
      }

      return props
    },
  },
  watch: {
    $type: {
      immediate: true,
      handler($type) {
        this.enhanced = getEnhancedMixins($type)
        this.listeners = getListeners.call(this)
      },
    },
    value: {
      immediate: true,
      handler(val) {
        let value = val

        // 验证值格式
        let originValue = this.row[this.column.property]
        let getValue = this.enhanced.getValue.call(this, originValue)
        if (originValue !== getValue) {
          // 值格式不正确，重新赋值
          value = getValue
          vModel.call(this, value)
        }

        this.innerValue = this.enhanced.setValue.call(this, value)

        // 判断是否启用翻译
        if (this.renderType === JVXERenderType.spaner && this.enhanced.translate.enabled) {
          let res = this.enhanced.translate.handler.call(this, value)
          // 异步翻译，目前仅【多级联动】使用
          if (res instanceof Promise) {
            res.then(value => this.innerValue = value)
          } else {
            this.innerValue = res
          }
        }
      },
    },
  },
  created() {
  },
  methods: {

    /** 通用处理change事件 */
    handleChangeCommon(value) {
      let handle = this.enhanced.getValue.call(this, value)
      this.trigger('change', {value: handle})
      // 触发valueChange事件
      this.parentTrigger('valueChange', {
        type: this.$type,
        value: handle,
        oldValue: this.value,
        col: this.originColumn,
        rowIndex: this.params.rowIndex,
        columnIndex: this.params.columnIndex,
      })
    },
    /** 通用处理blur事件 */
    handleBlurCommon(value) {
      this.trigger('blur', {value})
    },

    /**
     *  如果事件存在的话，就触发
     * @param name 事件名
     * @param event 事件参数
     * @param args 其他附带参数
     */
    trigger(name, event, args = []) {
      let listener = this.listeners[name]
      if (typeof listener === 'function') {
        if (typeof event === 'object') {
          event = this.packageEvent(name, event)
        }
        listener(event, ...args)
      }
    },
    parentTrigger(name, event, args = []) {
      args.unshift(this.packageEvent(name, event))
      this.trigger('trigger', name, args)
    },
    packageEvent(name, event = {}) {
      event.row = this.row
      event.column = this.column
      //online增强参数兼容
      event.column['key'] = this.column['property']
      event.cellTarget = this
      if (!event.type) {
        event.type = name
      }
      if (!event.cellType) {
        event.cellType = this.$type
      }
      // 是否校验表单，默认为true
      if (typeof event.validate !== 'boolean') {
        event.validate = true
      }
      return event
    },

  },
  model: {
    prop: 'value',
    event: 'change'
  },
  /**
   * 【自定义增强】用于实现一些增强事件
   * 【注】这里只是定义接口，具体功能需要到各个组件内实现（也有部分功能实现）
   * 【注】该属性不是Vue官方属性，是JVxeTable组件自定义的
   *      所以方法内的 this 指向并不是当前组件，而是方法自身，
   *      也就是说并不能 this 打点调实例里的任何方法
   */
  enhanced: {
    // 注册参数（详见：https://xuliangzhan_admin.gitee.io/vxe-table/#/table/renderer/edit）
    installOptions: {
      // 自动聚焦的 class 类名
      autofocus: '',
    },
    // 事件拦截器（用于兼容）
    interceptor: {
      // 已实现：event.clearActived
      // 说明：比如点击了某个组件的弹出层面板之后，此时被激活单元格不应该被自动关闭，通过返回 false 可以阻止默认的行为。
      ['event.clearActived'](params, event, target) {
        return true
      },
      // 自定义：event.clearActived.className
      // 说明：比原生的多了一个参数：className，用于判断点击的元素的样式名（递归到顶层）
      ['event.clearActived.className'](params, event, target) {
        return true
      },
    },
    // 【功能开关】
    switches: {
      // 是否使用 editRender 模式（仅当前组件，并非全局）
      // 如果设为true，则表头上方会出现一个可编辑的图标
      editRender: true,
      // false = 组件触发后可视）；true = 组件一直可视
      visible: false,
    },
    // 【切面增强】切面事件处理，一般在某些方法执行后同步执行
    aopEvents: {
      // 单元格被激活编辑时会触发该事件
      editActived() {
      },
      // 单元格编辑状态下被关闭时会触发该事件
      editClosed() {
      },
    },
    // 【翻译增强】可以实现例如select组件保存的value，但是span模式下需要显示成text
    translate: {
      // 是否启用翻译
      enabled: false,
      /**
       * 【翻译处理方法】如果handler留空，则使用默认的翻译方法
       * (this指向当前组件)
       *
       * @param value 需要翻译的值
       * @returns{*} 返回翻译后的数据
       */
      handler(value,) {
        // 默认翻译方法
        return filterDictText(this.column.own.options, value)
      },
    },
    /**
     * 【获取值增强】组件抛出的值
     * (this指向当前组件)
     *
     * @param value 保存到数据库里的值
     * @returns{*} 返回处理后的值
     */
    getValue(value) {
      return value
    },
    /**
     * 【设置值增强】设置给组件的值
     * (this指向当前组件)
     *
     * @param value 组件触发的值
     * @returns{*} 返回处理后的值
     */
    setValue(value) {
      return value
    },
    /**
     * 【新增行增强】在用户点击新增时触发的事件，返回新行的默认值
     *
     * @param row 行数据
     * @param column 列配置，.own 是用户配置的参数
     * @param $table vxe 实例
     * @param renderOptions 渲染选项
     * @param params 可以在这里获取 $table
     *
     * @returns 返回新值
     */
    createValue({row, column, $table, renderOptions, params}) {
      return column.own.defaultValue
    },
  }
}

function getListeners() {
  let listeners = Object.assign({}, (this.renderOptions.listeners || {}))
  if (!listeners.change) {
    listeners.change = async (event) => {
      vModel.call(this, event.value)
      await this.$nextTick()
      // 处理 change 事件相关逻辑（例如校验）
      this.params.$table.updateStatus(this.params)
    }
  }
  return listeners
}

export function vModel(value, row, property) {
  if (!row) {
    row = this.row
  }
  if (!property) {
    property = this.column.property
  }
  this.$set(row, property, value)
}

/** 模拟触发事件 */
export function dispatchEvent({cell, $event}, className, handler) {
  // alwaysEdit 下不模拟触发事件，否者会导致触发两次
  if (this && this.alwaysEdit) {
    return
  }
  window.setTimeout(() => {
    let element = cell.getElementsByClassName(className)
    if (element && element.length > 0) {
      if (typeof handler === 'function') {
        handler(element[0])
      } else {
        // 模拟触发点击事件
        if($event){
          element[0].dispatchEvent($event)
        }
      }
    }
  }, 10)
}