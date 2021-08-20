import XEUtils from 'xe-utils'
import PropTypes from 'ant-design-vue/es/_util/vue-types'
import { JVXETypes } from '@/components/jeecg/JVxeTable/jvxeTypes'
import VxeWebSocketMixins from '../mixins/vxe.web.socket.mixins'
import { initDictOptions } from '@/components/dict/JDictSelectUtil'

import { getRefPromise } from '../utils/vxeUtils'
import { getEnhancedMixins, replaceProps } from '../utils/cellUtils'

import JVxeToolbar from './JVxeToolbar'
import JVxeSubPopover from './JVxeSubPopover'
import JVxeDetailsModal from './JVxeDetailsModal'
import JVxePagination from './JVxePagination'
import { cloneObject, getVmParentByName, pushIfNotExist, randomString, simpleDebounce } from '@/utils/util'
import { UtilTools } from 'vxe-table/packages/tools/src/utils'
import { getNoAuthCols } from '@/utils/authFilter'

export default {
  name: 'JVxeTable',
  provide() {
    return {
      superTrigger: (name, event) => this.trigger(name, event)
    }
  },
  mixins: [VxeWebSocketMixins],
  components: {JVxeToolbar, JVxeSubPopover, JVxeDetailsModal, JVxePagination},
  props: {
    rowKey: PropTypes.string.def('id'),
    // 列信息
    columns: {
      type: Array,
      required: true
    },
    // 数据源
    dataSource: {
      type: Array,
      required: true
    },
    authPre: {
      type: String,
      required: false,
      default: ''
    },
    // 是否显示工具栏
    toolbar: PropTypes.bool.def(false),
    // 工具栏配置
    toolbarConfig: PropTypes.object.def(() => ({
      // prefix 前缀；suffix 后缀；
      slots: ['prefix', 'suffix'],
      // add 新增按钮；remove 删除按钮；clearSelection 清空选择按钮；collapse 展开收起
      btns: ['add', 'remove', 'clearSelection'],
    })),
    // 是否显示行号
    rowNumber: PropTypes.bool.def(false),
    // 是否可选择行
    rowSelection: PropTypes.bool.def(false),
    // 选择行类型
    rowSelectionType: PropTypes.oneOf(['checkbox', 'radio']).def('checkbox'),
    // 是否可展开行
    rowExpand: PropTypes.bool.def(false),
    // 展开行配置
    expandConfig: PropTypes.object.def(() => ({})),
    // 页面是否在加载中
    loading: PropTypes.bool.def(false),
    height: PropTypes.instanceOf([Number, String]).def('auto'),
    // 最大高度
    maxHeight: {
      type: Number,
      default: () => null,
    },
    // 要禁用的行 TODO 未实现
    disabledRows: PropTypes.object.def(() => ({})),
    // 是否禁用全部组件
    disabled: PropTypes.bool.def(false),
    // 是否可拖拽排序 TODO 仅实现上下排序，未实现拖拽排序（可能无法实现或较为困难）
    dragSort: PropTypes.bool.def(false),
    // 排序字段保存的Key
    dragSortKey: PropTypes.string.def('orderNum'),
    // 大小，可选值有：medium（中）、small（小）、mini（微）、tiny（非常小）
    size: PropTypes.oneOf(['medium', 'small', 'mini', 'tiny']).def('medium'),
    // 是否显示边框线
    bordered: PropTypes.bool.def(false),
    // 分页器参数，设置了即可显示分页器
    pagination: PropTypes.object.def(() => ({})),
    // 点击行时是否显示子表单
    clickRowShowSubForm: PropTypes.bool.def(false),
    // 点击行时是否显示主表单
    clickRowShowMainForm: PropTypes.bool.def(false),
    // 是否点击选中行，优先级最低
    clickSelectRow: PropTypes.bool.def(false),
    // 是否开启 reload 数据效果
    reloadEffect: PropTypes.bool.def(false),
    // 校验规则
    editRules: PropTypes.object.def(() => ({})),
    // 是否异步删除行，如果你要实现异步删除，那么需要把这个选项开启，
    // 在remove事件里调用confirmRemove方法才会真正删除（除非删除的全是新增的行）
    asyncRemove: PropTypes.bool.def(false),
    // 是否一直显示组件，如果为false则只有点击的时候才出现组件
    // 注：该参数不能动态修改；如果行、列字段多的情况下，会根据机器性能造成不同程度的卡顿。
    alwaysEdit: PropTypes.bool.def(false),
    // 联动配置，数组，详情配置见文档
    linkageConfig: PropTypes.array.def(() => []),
  },
  data() {
    return {
      isJVXETable: true,
      // caseId，表格唯一标识
      caseId: `_j-vxe-${randomString(8)}_`,
      // 内置columns
      _innerColumns: [],
      // 内置 EditRules
      _innerEditRules: [],
      // 记录滚动条位置
      scroll: {top: 0, left: 0},
      // 当前是否正在滚动
      scrolling: false,
      // vxe 默认配置
      defaultVxeProps: {
        'row-id': this.rowKey,
        // 高亮hover的行
        'highlight-hover-row': true,
        // 溢出隐藏并显示tooltip
        'show-overflow': true,
        // 表头溢出隐藏并显示tooltip
        'show-header-overflow': true,
        'show-footer-overflow': true,
        // 可编辑配置
        'edit-config': {trigger: 'click', mode: 'cell', showStatus: true},
        'expand-config': {
          iconClose: 'ant-table-row-expand-icon ant-table-row-collapsed',
          iconOpen: 'ant-table-row-expand-icon ant-table-row-expanded'
        },
        // 虚拟滚动配置，y轴大于30条数据时启用虚拟滚动
        // 'scroll-y': {
        //   gt: 30
        // },
        // 'scroll-x': {
        //   gt: 15
        // },
        'radio-config': {highlight: true},
        'checkbox-config': {highlight: true},
      },
      // 绑定左侧选择框
      selectedRows: [],
      // 绑定左侧选择框已选择的id
      selectedRowIds: [],
      // 统计列配置
      statistics: {
        has: false,
        sum: [],
        average: [],
      },
      // 允许执行刷新特效的行ID
      reloadEffectRowKeysMap: {},
      //配置了但是没有授权的按钮和列 集合
      excludeCode:[],
      // 联动下拉选项（用于隔离不同的下拉选项）
      // 内部联动配置，map
      _innerLinkageConfig: null,
    }
  },
  computed: {

    // vxe 最终 columns
    vxeColumns() {
      this._innerColumns.forEach(column => {
        let renderOptions = {
          caseId: this.caseId,
          bordered: this.bordered,
          disabled: this.disabled,
          scrolling: this.scrolling,
          reloadEffect: this.reloadEffect,
          reloadEffectRowKeysMap: this.reloadEffectRowKeysMap,
          listeners: this.cellListeners,
        }
        if (column.$type === JVXETypes.rowDragSort) {
          renderOptions.dragSortKey = this.dragSortKey
        }
        // slot 组件特殊处理
        if (column.$type === JVXETypes.slot) {
          if (this.$scopedSlots.hasOwnProperty(column.slotName)) {
            renderOptions.slot = this.$scopedSlots[column.slotName]
            renderOptions.target = this
          }
        }
        // 处理联动列，联动列只能作用于 select 组件
        if (column.$type === JVXETypes.select && this._innerLinkageConfig != null) {
          // 判断当前列是否是联动列
          if (this._innerLinkageConfig.has(column.key)) {
            renderOptions.linkage = {
              config: this._innerLinkageConfig.get(column.key),
              getLinkageOptionsSibling: this.getLinkageOptionsSibling,
              getLinkageOptionsAsync: this.getLinkageOptionsAsync,
              linkageSelectChange: this.linkageSelectChange,
            }
          }
        }
        if (column.editRender) {
          Object.assign(column.editRender, renderOptions)
        }
        if (column.cellRender) {
          Object.assign(column.cellRender, renderOptions)
        }
        // update--begin--autor:lvdandan-----date:20201019------for:LOWCOD-882 【新行编辑】列表上带按钮的遮挡问题
        if (column.$type === JVXETypes.file || column.$type === JVXETypes.image) {
          if (column.width && column.width.endsWith('px')) {
            column.width = Number.parseInt(column.width.substr(0,column.width.length-2))+Number.parseInt(1)+'px';
          }
        }
        // update--begin--autor:lvdandan-----date:20201019------for:LOWCOD-882 【新行编辑】列表上带按钮的遮挡问题

        // update--begin--autor:lvdandan-----date:20201211------for:JT-118 【online】 日期、时间控件长度较小
        if (column.$type === JVXETypes.datetime || column.$type === JVXETypes.userSelect || column.$type === JVXETypes.departSelect) {
          let width = column.width && column.width.endsWith('px')?Number.parseInt(column.width.substr(0,column.width.length-2)):0;
          if(width <= 190){
            column.width = '190px'
          }
        }
        if (column.$type === JVXETypes.date) {
          let width = column.width && column.width.endsWith('px')?Number.parseInt(column.width.substr(0,column.width.length-2)):0;
          if(width <= 135){
            column.width = '135px'
          }
        }
        // update--end--autor:lvdandan-----date:20201211------for:JT-118 【online】 日期、时间控件长度较小
      })
      return this._innerColumns
    },
    // vxe 最终 editRules
    vxeEditRules() {
      return Object.assign({}, this.editRules, this._innerEditRules)
    },
    // vxe 最终 props
    vxeProps() {
      let expandConfig = Object.assign({}, this.defaultVxeProps['expand-config'], this.expandConfig)

      return Object.assign({}, this.defaultVxeProps, {
        showFooter: this.statistics.has,
      }, this.$attrs, {
        loading: this.loading,
        columns: this.vxeColumns,
        editRules: this.vxeEditRules,
        // data: this.dataSource,
        height: this.height === 'auto' ? null : this.height,
        maxHeight: this.maxHeight,
        border: this.bordered,
        expandConfig: expandConfig,
        footerMethod: this.handleFooterMethod,
        // footerSpanMethod: this.handleFooterSpanMethod,
      })
    },
    // vxe 最终 events
    vxeEvents() {
      // 内置事件
      let events = {
        'scroll': this.handleVxeScroll,
        'cell-click': this.handleCellClick,
        'edit-closed': this.handleEditClosed,
        'edit-actived': this.handleEditActived,
        'radio-change': this.handleVxeRadioChange,
        'checkbox-all': this.handleVxeCheckboxAll,
        'checkbox-change': this.handleVxeCheckboxChange,
      }
      // 用户传递的事件，进行合并操作
      Object.keys(this.$listeners).forEach(key => {
        let listen = this.$listeners[key]
        if (events.hasOwnProperty(key)) {
          if (Array.isArray(listen)) {
            listen.push(events[key])
          } else {
            listen = [events[key], listen]
          }
        }
        events[key] = listen
      })
      return events
    },
    // 组件监听事件
    cellListeners() {
      return {
        trigger: (name, event) => this.trigger(name, event),
        valueChange: event => this.trigger('valueChange', event),
        /** 当前行向上移一位 */
        rowMoveUp: rowIndex => this.rowResort(rowIndex, rowIndex - 1),
        /** 当前行向下移一位 */
        rowMoveDown: rowIndex => this.rowResort(rowIndex, rowIndex + 1),
        /** 在当前行下面插入一行 */
        rowInsertDown: rowIndex => this.insertRows({}, rowIndex + 1),
      }
    },
  },
  watch: {
    dataSource: {
      //   deep: true,
      immediate: true,
      async handler() {
        let vxe = await getRefPromise(this, 'vxe')

        this.dataSource.forEach((data, idx) => {
          // 开启了排序就自动计算排序值
          if (this.dragSort) {
            this.$set(data, this.dragSortKey, idx + 1)
          }
          // 处理联动回显数据
          if (this._innerLinkageConfig != null) {
            for (let configItem of this._innerLinkageConfig.values()) {
              this.autoSetLinkageOptionsByData(data, '', configItem, 0)
            }
          }
        })

        // 阻断vue监听大数据，提高性能
        vxe.loadData(this.dataSource)

        // TODO 解析disabledRows
        // let disabled = false
        //
        // let disabledRowIds = (this.disabledRowIds || [])
        // // 解析disabledRows
        // Object.keys(this.disabledRows).forEach(disabledColKey => {
        //   // 判断是否有该属性
        //   if (data.hasOwnProperty(disabledColKey)) {
        //     if (disabled !== true) {
        //       let temp = this.disabledRows[disabledColKey]
        //       // 禁用规则可以是一个数组
        //       if (Array.isArray(temp)) {
        //         disabled = temp.includes(data[disabledColKey])
        //       } else {
        //         disabled = (temp === data[disabledColKey])
        //       }
        //       if (disabled) {
        //         disabledRowIds.push(row.id)
        //       }
        //     }
        //   }
        // })
      },
    },
    columns: {
      immediate: true,
      handler(columns) {
        //获取不需要显示列
        this.loadExcludeCode()
        let _innerColumns = []
        let _innerEditRules = {}
        let {rowNumber, rowSelection, rowExpand, dragSort} = this
        let expandColumn, seqColumn, checkboxColumn, radioColumn, dragSortColumn
        if (Array.isArray(columns)) {
          this.statistics.has = false
          this.statistics.sum = []
          this.statistics.average = []

          // 处理成vxe可识别的columns
          columns.forEach(column => {
            if(this.excludeCode.indexOf(column.key)>=0){
              return false
            }
            let col = {...column}
            let {type} = col
            const enhanced = getEnhancedMixins(type)
            if (type === JVXETypes.rowNumber) {
              seqColumn = col
            } else if (type === JVXETypes.rowCheckbox) {
              checkboxColumn = col
            } else if (type === JVXETypes.rowRadio) {
              radioColumn = col
            } else if (type === JVXETypes.rowExpand) {
              expandColumn = col
            } else if (type === JVXETypes.rowDragSort) {
              dragSortColumn = col
            } else {
              col.field = col.key
              // 防止和vxeTable自带的type起冲突
              col.$type = col.type
              delete col.type
              let renderName = 'cellRender', renderOptions = {name: JVXETypes._prefix + type}
              if (type) {
                // hidden 是特殊的组件
                if (type === JVXETypes.hidden) {
                  col.visible = false
                } else if (enhanced.switches.editRender) {
                  renderName = 'editRender'
                  renderOptions.type = (enhanced.switches.visible || this.alwaysEdit) ? 'visible' : 'default'
                }
              } else {
                renderOptions.name = JVXETypes._prefix + JVXETypes.normal
              }
              col[renderName] = renderOptions
              // 处理字典
              if (col.dictCode) {
                this._loadDictConcatToOptions(col)
              }
              // 处理校验
              if (col.validateRules) {
                let rules = []
                if (Array.isArray(col.validateRules)) {
                  for (let rule of col.validateRules) {
                    let replace = {
                      message: replaceProps(col, rule.message)
                    }
                    if (rule.unique || rule.pattern === 'only') {
                      // 唯一校验器
                      rule.validator = uniqueValidator.bind(this)
                    } else if (rule.pattern) {
                      // 非空
                      if (rule.pattern === fooPatterns[0].value) {
                        rule.required = true
                        delete rule.pattern
                      } else {
                        // 兼容Online表单的特殊规则
                        for (let foo of fooPatterns) {
                          if (foo.value === rule.pattern) {
                            rule.pattern = foo.pattern
                            break
                          }
                        }
                      }
                    } else if (typeof rule.handler === 'function') {
                      // 自定义函数校验
                      rule.validator = handlerConvertToValidator.bind(this)
                    }
                    rules.push(Object.assign({}, rule, replace))
                  }
                }
                _innerEditRules[col.key] = rules
              }
              // 处理统计列
              // sum = 求和、average = 平均值
              if (Array.isArray(col.statistics)) {
                this.statistics.has = true
                col.statistics.forEach(item => {
                  let arr = this.statistics[item.toLowerCase()]
                  if (Array.isArray(arr)) {
                    pushIfNotExist(arr, col.key)
                  }
                })
              }
              _innerColumns.push(col)
            }
          })
        }
        // 判断是否开启了序号
        if (rowNumber) {
          let col = {type: 'seq', title: '#', width: 60, fixed: 'left', align: 'center'}
          if (seqColumn) {
            col = Object.assign(col, seqColumn, {type: 'seq'})
          }
          _innerColumns.unshift(col)
        }
        // 判断是否开启了可选择行
        if (rowSelection) {
          let width = 40
          if (this.statistics.has && !rowExpand && !dragSort) {
            width = 60
          }
          let col = {type: this.rowSelectionType, width, fixed: 'left', align: 'center'}
          // radio
          if (this.rowSelectionType === 'radio' && radioColumn) {
            col = Object.assign(col, radioColumn, {type: 'radio'})
          }
          // checkbox
          if (this.rowSelectionType === 'checkbox' && checkboxColumn) {
            col = Object.assign(col, checkboxColumn, {type: 'checkbox'})
          }
          _innerColumns.unshift(col)
        }
        // 是否可展开行
        if (rowExpand) {
          let width = 40
          if (this.statistics.has && !dragSort) {
            width = 60
          }
          let col = {type: 'expand', title: '', width, fixed: 'left', align: 'center', slots: {content: 'expandContent'}}
          if (expandColumn) {
            col = Object.assign(col, expandColumn, {type: 'expand'})
          }
          _innerColumns.unshift(col)
        }
        // 是否可拖动排序
        if (dragSort) {
          let width = 40
          if (this.statistics.has) {
            width = 60
          }
          let col = {type: JVXETypes.rowDragSort, title: '', width, fixed: 'left', align: 'center', cellRender: {name: JVXETypes._prefix + JVXETypes.rowDragSort}}
          if (dragSortColumn) {
            col = Object.assign(col, dragSortColumn, {type: JVXETypes.rowDragSort})
          }
          _innerColumns.unshift(col)
        }

        this._innerColumns = _innerColumns
        this._innerEditRules = _innerEditRules
      }
    },
    // watch linkageConfig
    // 整理多级联动配置
    linkageConfig: {
      immediate: true,
      handler() {
        if (Array.isArray(this.linkageConfig) && this.linkageConfig.length > 0) {
          // 获取联动的key顺序
          let getLcKeys = (key, arr) => {
            let col = this._innerColumns.find(col => col.key === key)
            if (col) {
              arr.push(col.key)
              if (col.linkageKey) {
                return getLcKeys(col.linkageKey, arr)
              }
            }
            return arr
          }
          let configMap = new Map()
          this.linkageConfig.forEach(lc => {
            let keys = getLcKeys(lc.key, [])
            // 多个key共享一个，引用地址
            let configItem = {
              ...lc, keys,
              optionsMap: new Map()
            }
            keys.forEach(k => configMap.set(k, configItem))
          })
          this._innerLinkageConfig = configMap
        } else {
          this._innerLinkageConfig = null
        }
      }
    },
  },
  created() {
  },
  mounted() {
    this.handleTabsChange()
  },
  methods: {

    /**
     * 自动判断父级是否是 <a-tabs/> 组件，然后添加事件监听，自动重置表格
     */
    handleTabsChange() {
      // 获取父级
      const tabs = getVmParentByName(this, 'ATabs')
      const tabPane = getVmParentByName(this, 'ATabPane')
      if (tabs && tabPane) {
        // 用户自定义的 key
        const currentKey = tabPane.$vnode.key
        // 添加 activeKey 监听
        const unwatch = tabs.$children[0].$watch('$data._activeKey', async (key) => {
          // 切换到自己时重新计算
          if (currentKey === key) {
            await this.$nextTick()
            await this.refreshScroll()
            await this.recalculate()
          }
        })
        // 当前实例销毁时取消监听
        this.$on('beforeDestroy', () => unwatch())
      }
    },

    handleVxeScroll(event) {
      let {$refs, scroll} = this

      // 记录滚动条的位置
      scroll.top = event.scrollTop
      scroll.left = event.scrollLeft

      $refs.subPopover ? $refs.subPopover.close() : null
      this.scrolling = true
      this.closeScrolling()
    },
    // 当手动勾选单选时触发的事件
    handleVxeRadioChange(event) {
      let row = event.$table.getRadioRecord()
      this.selectedRows = row ? [row] : []
      this.handleSelectChange('radio', this.selectedRows, event)
    },
    // 当手动勾选全选时触发的事件
    handleVxeCheckboxAll(event) {
      this.selectedRows = event.$table.getCheckboxRecords()
      this.handleSelectChange('checkbox-all', this.selectedRows, event)
    },
    // 当手动勾选并且值发生改变时触发的事件
    handleVxeCheckboxChange(event) {
      this.selectedRows = event.$table.getCheckboxRecords()
      this.handleSelectChange('checkbox', this.selectedRows, event)
    },
    // 行选择change事件
    handleSelectChange(type, selectedRows, $event) {
      let action
      if (type === 'radio') {
        action = 'selected'
      } else if (type === 'checkbox') {
        action = selectedRows.includes($event.row) ? 'selected' : 'unselected'
      } else {
        action = 'selected-all'
      }

      this.selectedRowIds = selectedRows.map(row => row.id)
      this.trigger('selectRowChange', {
        type: type,
        action: action,
        $event: $event,
        row: $event.row,
        selectedRows: this.selectedRows,
        selectedRowIds: this.selectedRowIds
      })
    },

    // 点击单元格时触发的事件
    handleCellClick(event) {
      let {row, column, $event, $table} = event
      let {$refs} = this

      // 点击了可编辑的
      if (column.editRender) {
        $refs.subPopover ? $refs.subPopover.close() : null
        return
      }

      // 显示详细信息
      if (column.own.showDetails) {
        // 两个如果同时存在的话会出现死循环
        $refs.subPopover ? $refs.subPopover.close() : null
        $refs.detailsModal ? $refs.detailsModal.open(event) : null
      } else if ($refs.subPopover) {
        $refs.subPopover.toggle(event)
      } else if (this.clickSelectRow) {
        let className = $event.target.className || ''
        className = typeof className === 'string' ? className : className.toString()
        // 点击的是expand，不做处理
        if (className.includes('vxe-table--expand-btn')) {
          return
        }
        // 点击的是checkbox，不做处理
        if (className.includes('vxe-checkbox--icon') || className.includes('vxe-cell--checkbox')) {
          return
        }
        // 点击的是radio，不做处理
        if (className.includes('vxe-radio--icon') || className.includes('vxe-cell--radio')) {
          return
        }
        if (this.rowSelectionType === 'radio') {
          $table.setRadioRow(row)
          this.handleVxeRadioChange(event)
        } else {
          $table.toggleCheckboxRow(row)
          this.handleVxeCheckboxChange(event)
        }
      }
    },

    // 单元格编辑状态下被关闭时会触发该事件
    handleEditClosed({column}) {
      // 执行增强
      getEnhancedMixins(column.own.$type, 'aopEvents').editClosed.apply(this, arguments)
    },

    // 单元格被激活编辑时会触发该事件
    handleEditActived({column}) {
      // 执行增强
      getEnhancedMixins(column.own.$type, 'aopEvents').editActived.apply(this, arguments)
    },

    /** 表尾数据处理方法，用于显示统计信息 */
    handleFooterMethod({columns, data}) {
      const {statistics} = this
      let footers = []
      if (statistics.has) {
        if (statistics.sum.length > 0) {
          footers.push(this.getFooterStatisticsMap({
            columns: columns,
            title: '合计',
            checks: statistics.sum,
            method: (column) => XEUtils.sum(data, column.property)
          }))
        }
        if (statistics.average.length > 0) {
          footers.push(this.getFooterStatisticsMap({
            columns: columns,
            title: '平均',
            checks: statistics.average,
            method: (column) => XEUtils.mean(data, column.property)
          }))
        }
      }
      return footers
    },

    getFooterStatisticsMap({columns, title, checks, method}) {
      return columns.map((column, columnIndex) => {
        if (columnIndex === 0) {
          return title
        }
        if (checks.includes(column.property)) {
          return method(column, columnIndex)
        }
        return null
      })
    },

    /** 表尾单元格合并方法 */
    handleFooterSpanMethod(event) {
      if (event.columnIndex === 0) {
        return {colspan: 2}
      }
    },

    /*--- 外部可调用接口方法 ---*/

    /**
     * 重置滚动条Top位置
     * @param top 新top位置，留空则滚动到上次记录的位置，用于解决切换tab选项卡时导致白屏以及自动将滚动条滚动到顶部的问题
     */
    resetScrollTop(top) {
      this.scrollTo(null, (top == null || top === '') ? this.scroll.top : top)
    },

    /**
     * 加载新数据，和 loadData 不同的是，用该方法加载的数据都是相当于点新增按钮新增的数据。
     * 适用于不是数据库里查出来的没有id的临时数据
     * @param dataSource
     */
    async loadNewData(dataSource) {
      if (Array.isArray(dataSource)) {
        let {xTable} = this.$refs.vxe.$refs
        // issues/2784
        // 先清空所有数据
        xTable.loadData([])

        dataSource.forEach((data, idx) => {
          // 开启了排序就自动计算排序值
          if (this.dragSort) {
            this.$set(data, this.dragSortKey, idx + 1)
          }
          // 处理联动回显数据
          if (this._innerLinkageConfig != null) {
            for (let configItem of this._innerLinkageConfig.values()) {
              this.autoSetLinkageOptionsByData(data, '', configItem, 0)
            }
          }
        })
        // 再新增
        return xTable.insertAt(dataSource)
      }
      return []
    },

    // 校验table，失败返回errMap，成功返回null
    async validateTable() {
      const errMap = await this.validate().catch(errMap => errMap)
      return errMap ? errMap : null
    },
    // 完整校验
    async fullValidateTable() {
      const errMap = await this.fullValidate().catch(errMap => errMap)
      return errMap ? errMap : null
    },

    /** 设置某行某列的值 */
    setValues(values) {
      if (!Array.isArray(values)) {
        console.warn(`JVxeTable.setValues：必须传递数组`)
        return
      }
      values.forEach((item, idx) => {
        let {rowKey, values: record} = item
        let {row} = this.getIfRowById(rowKey)
        if (!row) {
          return
        }
        Object.keys(record).forEach(colKey => {
          let column = this.getColumnByKey(colKey)
          if (column) {
            let oldValue = row[colKey]
            let newValue = record[colKey]
            if (newValue !== oldValue) {
              this.$set(row, colKey, newValue)
              // 触发 valueChange 事件
              this.trigger('valueChange', {
                type: column.own.$type,
                value: newValue,
                oldValue: oldValue,
                col: column.own,
                column: column,
                isSetValues: true,
              })
            }
          } else {
            console.warn(`JVxeTable.setValues：没有找到key为"${colKey}"的列`)
          }
        })
      })
    },

    /** 获取所有的数据，包括values、deleteIds */
    getAll() {
      return {
        tableData: this.getTableData(),
        deleteData: this.getDeleteData()
      }
    },
    /** 获取表格表单里的值 */
    getValues(callback, rowIds) {
      let tableData = this.getTableData({rowIds: rowIds})
      callback('', tableData)
    },
    /** 获取表格数据 */
    getTableData(options = {}) {
      let {rowIds} = options
      let tableData
      // 仅查询指定id的行
      if (Array.isArray(rowIds) && rowIds.length > 0) {
        tableData = []
        rowIds.forEach(rowId => {
          let {row} = this.getIfRowById(rowId)
          if (row) {
            tableData.push(row)
          }
        })
      } else {
        // 查询所有行
        tableData = this.$refs.vxe.getTableData().fullData
      }
      return this.filterNewRows(tableData, false)
    },
    /** 仅获取新增的数据 */
    getNewData() {
      let newData = cloneObject(this.$refs.vxe.getInsertRecords())
      newData.forEach(row => delete row.id)
      return newData
    },
    /** 仅获取新增的数据,带有id */
    getNewDataWithId() {
      let newData = cloneObject(this.$refs.vxe.getInsertRecords())
      return newData
    },
    /** 根据ID获取行，新增的行也能查出来 */
    getIfRowById(id) {
      let row = this.getRowById(id), isNew = false
      if (!row) {
        row = this.getNewRowById(id)
        if (!row) {
          console.warn(`JVxeTable.getIfRowById：没有找到id为"${id}"的行`)
          return {row: null}
        }
        isNew = true
      }
      return {row, isNew}
    },
    /** 通过临时ID获取新增的行 */
    getNewRowById(id) {
      let records = this.getInsertRecords()
      for (let record of records) {
        if (record.id === id) {
          return record
        }
      }
      return null
    },
    /** 仅获取被删除的数据（新增又被删除的数据不会被获取到） */
    getDeleteData() {
      return cloneObject(this.$refs.vxe.getRemoveRecords())
    },
    /**
     * 添加一行或多行
     *
     * @param rows
     * @param isOnlJs 是否是onlineJS增强触发的
     * @return
     */
    async addRows(rows = {}, isOnlJs) {
      return this._addOrInsert(rows, -1, 'added', isOnlJs)
    },

    /**
     * 添加一行或多行
     *
     * @param rows
     * @param index 添加下标，数字，必填
     * @return
     */
    async insertRows(rows, index) {
      if (typeof index !== 'number' || index < 0) {
        console.warn(`【JVXETable】insertRows：index必须传递数字，且大于-1`)
        return
      }
      return this._addOrInsert(rows, index, 'inserted')
    },
    /**
     * 添加一行或多行临时数据，不会填充默认值，传什么就添加进去什么
     * @param rows
     * @param options 选项
     * @param options.setActive 是否激活最后一行的编辑模式
     */
    async pushRows(rows = {}, options = {}) {
      let {xTable} = this.$refs.vxe.$refs
      let {setActive, index} = options
      setActive = setActive == null ? false : !!setActive
      index = index == null ? -1 : index
      index = index === -1 ? index : xTable.tableFullData[index]
      // 插入行
      let result = await xTable.insertAt(rows, index)
      if (setActive) {
        // 激活最后一行的编辑模式
        xTable.setActiveRow(result.rows[result.rows.length - 1])
      }
      await this._recalcSortNumber()
      return result
    },

    /** 清空选择行 */
    clearSelection() {
      let event = {$table: this.$refs.vxe, target: this}
      if (this.rowSelectionType === JVXETypes.rowRadio) {
        this.$refs.vxe.clearRadioRow()
        this.handleVxeRadioChange(event)
      } else {
        this.$refs.vxe.clearCheckboxRow()
        this.handleVxeCheckboxChange(event)
      }
    },

    /** 删除一行或多行数据 */
    async removeRows(rows) {
      const res = await this._remove(rows)
      await this._recalcSortNumber()
      return res
    },

    /** 根据id删除一行或多行 */
    removeRowsById(rowId) {
      let rowIds
      if (Array.isArray(rowId)) {
        rowIds = rowId
      } else {
        rowIds = [rowId]
      }
      let rows = rowIds.map((id) => {
        let {row} = this.getIfRowById(id)
        if (!row) {
          return
        }
        if (row) {
          return row
        } else {
          console.warn(`【JVXETable】removeRowsById：${id}不存在`)
          return null
        }
      }).filter((row) => row != null)
      return this.removeRows(rows)
    },

    getColumnByKey() {
      return this.$refs.vxe.getColumnByField.apply(this.$refs.vxe, arguments)
    },

    /* --- 辅助方法 ---*/

    // 触发事件
    trigger(name, event = {}) {
      event.$target = this
      event.$table = this.$refs.vxe
      //online增强参数兼容
      event.target = this
      this.$emit(name, event)
    },

    /** 【多级联动】获取同级联动下拉选项 */
    getLinkageOptionsSibling(row, col, config, request) {
      // 如果当前列不是顶级列
      let key = ''
      if (col.key !== config.key) {
        // 就找出联动上级列
        let idx = config.keys.findIndex(k => col.key === k)
        let parentKey = config.keys[idx - 1]
        key = row[parentKey]
        // 如果联动上级列没有选择数据，就直接返回空数组
        if (key === '' || key == null) {
          return []
        }
      } else {
        key = 'root'
      }
      let options = config.optionsMap.get(key)
      if (!Array.isArray(options)) {
        if (request) {
          let parent = key === 'root' ? '' : key
          return this.getLinkageOptionsAsync(config, parent)
        } else {
          options = []
        }
      }
      return options
    },
    /** 【多级联动】获取联动下拉选项（异步） */
    getLinkageOptionsAsync(config, parent) {
      return new Promise(resolve => {
        let key = parent ? parent : 'root'
        let options
        if (config.optionsMap.has(key)) {
          options = config.optionsMap.get(key)
          if (options instanceof Promise) {
            options.then(opt => {
              config.optionsMap.set(key, opt)
              resolve(opt)
            })
          } else {
            resolve(options)
          }
        } else if (typeof config.requestData === 'function') {
          // 调用requestData方法，通过传入parent来获取子级
          let promise = config.requestData(parent)
          config.optionsMap.set(key, promise)
          promise.then(opt => {
            config.optionsMap.set(key, opt)
            resolve(opt)
          })
        } else {
          resolve([])
        }
      })
    },
    // 【多级联动】 用于回显数据，自动填充 optionsMap
    autoSetLinkageOptionsByData(data, parent, config, level) {
      if (level === 0) {
        this.getLinkageOptionsAsync(config, '')
      } else {
        this.getLinkageOptionsAsync(config, parent)
      }
      if (config.keys.length - 1 > level) {
        let value = data[config.keys[level]]
        if (value) {
          this.autoSetLinkageOptionsByData(data, value, config, level + 1)
        }
      }
    },
    // 【多级联动】联动组件change时，清空下级组件
    linkageSelectChange(row, col, config, value) {
      if (col.linkageKey) {
        this.getLinkageOptionsAsync(config, value)
        let idx = config.keys.findIndex(k => k === col.key)
        let values = {}
        for (let i = idx; i < config.keys.length; i++) {
          values[config.keys[i]] = ''
        }
        // 清空后几列的数据
        this.setValues([{rowKey: row.id, values}])
      }
    },

    /** 加载数据字典并合并到 options */
    _loadDictConcatToOptions(column) {
      initDictOptions(column.dictCode).then((res) => {
        if (res.success) {
          let newOptions = (column.options || [])// .concat(res.result)
          res.result.forEach(item => {
            // 过滤重复数据
            for (let option of newOptions) if (option.value === item.value) return
            newOptions.push(item)
          })
          this.$set(column, 'options', newOptions)
        } else {
          console.group(`JVxeTable 查询字典(${column.dictCode})发生异常`)
          console.warn(res.message)
          console.groupEnd()
        }
      })
    },
    //options自定义赋值 刷新
    virtualRefresh(){
      this.scrolling = true
      this.closeScrolling()
    },
    // 设置 this.scrolling 防抖模式
    closeScrolling: simpleDebounce(function () {
      this.scrolling = false
    }, 100),

    /**
     * 过滤添加的行
     * @param rows 要筛选的行数据
     * @param remove true = 删除新增，false=只删除id
     * @param handler function
     */
    filterNewRows(rows, remove = true, handler) {
      let insertRecords = this.$refs.vxe.getInsertRecords()
      let records = []
      for (let row of rows) {
        let item = cloneObject(row)
        if (insertRecords.includes(row)) {
          handler ? handler({item, row, insertRecords}) : null

          if (remove) {
            continue
          }
          delete item.id
        }
        records.push(item)
      }
      return records
    },

    // 删除选中的数据
    async removeSelection() {
      let res = await this._remove(this.selectedRows)
      this.clearSelection()
      await this._recalcSortNumber()
      return res
    },

    /**
     * 【删除指定行数据】（重写vxeTable的内部方法，添加了从keepSource中删除）
     * 如果传 row 则删除一行
     * 如果传 rows 则删除多行
     * 如果为空则删除所有
     */
    _remove(rows) {
      const xTable = this.$refs.vxe.$refs.xTable

      const {afterFullData, tableFullData, tableSourceData, editStore, treeConfig, checkboxOpts, selection, isInsertByRow, scrollYLoad} = xTable
      const {actived, removeList, insertList} = editStore
      const {checkField: property} = checkboxOpts
      let rest = []
      const nowData = afterFullData
      if (treeConfig) {
        throw new Error(UtilTools.getLog('vxe.error.noTree', ['remove']))
      }
      if (!rows) {
        rows = tableFullData
      } else if (!XEUtils.isArray(rows)) {
        rows = [rows]
      }
      // 如果是新增，则保存记录
      rows.forEach(row => {
        if (!isInsertByRow(row)) {
          removeList.push(row)
        }
      })
      // 如果绑定了多选属性，则更新状态
      if (!property) {
        XEUtils.remove(selection, row => rows.indexOf(row) > -1)
      }
      // 从数据源中移除
      if (tableFullData === rows) {
        rows = rest = tableFullData.slice(0)
        tableFullData.length = 0
        nowData.length = 0
      } else {
        rest = XEUtils.remove(tableFullData, row => rows.indexOf(row) > -1)
        XEUtils.remove(nowData, row => rows.indexOf(row) > -1)
      }
      // 【从keepSource中删除】
      if (xTable.keepSource) {
        let rowIdSet = new Set(rows.map(row => row.id))
        XEUtils.remove(tableSourceData, row => rowIdSet.has(row.id))
      }

      // 如果当前行被激活编辑，则清除激活状态
      if (actived.row && rows.indexOf(actived.row) > -1) {
        xTable.clearActived()
      }
      // 从新增中移除已删除的数据
      XEUtils.remove(insertList, row => rows.indexOf(row) > -1)
      xTable.handleTableData()
      xTable.updateFooter()
      xTable.updateCache()
      xTable.checkSelectionStatus()
      if (scrollYLoad) {
        xTable.updateScrollYSpace()
      }
      return xTable.$nextTick().then(() => {
        xTable.recalculate()
        return {row: rest.length ? rest[rest.length - 1] : null, rows: rest}
      })
    },

    /** 行重新排序 */
    async rowResort(oldIndex, newIndex) {
      const xTable = this.$refs.vxe.$refs.xTable
      window.xTable = xTable
      const sort = (array) => {
        // 存储旧数据，并删除旧项目
        let row = array.splice(oldIndex, 1)[0]
        // 向新项目里添加旧数据
        array.splice(newIndex, 0, row)
      }
      sort(xTable.tableFullData)
      if (xTable.keepSource) {
        sort(xTable.tableSourceData)
      }
      await this.$nextTick()
      await this._recalcSortNumber()
    },

    /** 重新计算排序字段的数值 */
    async _recalcSortNumber() {
      const xTable = this.$refs.vxe.$refs.xTable
      if (this.dragSort) {
        xTable.tableFullData.forEach((data, idx) => data[this.dragSortKey] = (idx + 1))
      }
      await xTable.updateCache(true)
      return await xTable.updateData()
    },

    async _addOrInsert(rows = {}, index, triggerName, isOnlJs) {
      let {xTable} = this.$refs.vxe.$refs
      let records
      if (Array.isArray(rows)) {
        records = rows
      } else {
        records = [rows]
      }
      // 遍历添加默认值
      records.forEach(record => this._createRow(record))
      let result = await this.pushRows(records, {index: index, setActive: true})
      // 遍历插入的行
      // update--begin--autor:lvdandan-----date:20201117------for:LOWCOD-987 【新行编辑】js增强附表内置方法调用问题 #1819
      // online js增强时以传过来值为准，不再赋默认值
      if (isOnlJs != true) {
        for (let i = 0; i < result.rows.length; i++) {
          let row = result.rows[i]
          this.trigger(triggerName, {
            row: row,
            $table: xTable,
            target: this,
          })
        }
      }
      // update--end--autor:lvdandan-----date:20201117------for:LOWCOD-987 【新行编辑】js增强附表内置方法调用问题 #1819
      return result
    },
    // 创建新行，自动添加默认值
    _createRow(record = {}) {
      let {xTable} = this.$refs.vxe.$refs
      // 添加默认值
      xTable.tableFullColumn.forEach(column => {
        let col = column.own
        if (col.key && (record[col.key] == null || record[col.key] === '')) {
          // 设置默认值
          let createValue = getEnhancedMixins(col.$type || col.type, 'createValue')
          record[col.key] = createValue({row: record, column, $table: xTable})
        }
        // update-begin--author:sunjianlei---date:20210819------for: 处理联动列，联动列只能作用于 select 组件
        if (col.$type === JVXETypes.select && this._innerLinkageConfig != null) {
          // 判断当前列是否是联动列
          if (this._innerLinkageConfig.has(col.key)) {
            let configItem = this._innerLinkageConfig.get(col.key)
            this.getLinkageOptionsAsync(configItem, '')
          }
        }
        // update-end--author:sunjianlei---date:20210819------for: 处理联动列，联动列只能作用于 select 组件
      })
      return record
    },

    /*--- 渲染函数 ---*/

    // 渲染 vxe
    renderVxeGrid(h) {
      return h('vxe-grid', {
        ref: 'vxe',
        class: ['j-vxe-table'],
        props: this.vxeProps,
        on: this.vxeEvents,
        // 作用域插槽的格式为
        scopedSlots: this.$scopedSlots,
      })
    },
    // 渲染工具栏
    renderToolbar(h) {
      if (this.toolbar) {
        return h('j-vxe-toolbar', {
          props: {
            toolbarConfig: this.toolbarConfig,
            excludeCode: this.excludeCode,
            size: this.size,
            disabled: this.disabled,
            disabledRows: this.disabledRows,
            selectedRowIds: this.selectedRowIds,
          },
          on: {
            // 新增事件
            add: () => this.addRows(),
            // 保存事件
            save: () => this.trigger('save', {
              $table: this.$refs.vxe,
              target: this,
            }),
            // 删除事件
            remove: () => {
              let $table = this.$refs.vxe
              let deleteRows = this.filterNewRows(this.selectedRows)
              // 触发删除事件
              if (deleteRows.length > 0) {
                let removeEvent = {deleteRows, $table, target: this}
                if (this.asyncRemove) {
                  // 确认删除，只有调用这个方法才会真删除
                  removeEvent.confirmRemove = () => this.removeSelection()
                } else {
                  this.removeSelection()
                }
                this.trigger('remove', removeEvent)
              } else {
                this.removeSelection()
              }
            },
            // 清除选择事件
            clearSelection: this.clearSelection
          },
          scopedSlots: {
            toolbarPrefix: this.$scopedSlots.toolbarPrefix,
            toolbarSuffix: this.$scopedSlots.toolbarSuffix,
          },
        })
      }
      return null
    },
    // 渲染 toolbarAfter 插槽
    renderToolbarAfterSlot() {
      if (this.$scopedSlots['toolbarAfter']) {
        return this.$scopedSlots['toolbarAfter']()
      }
      return null
    },
    // 渲染点击时弹出的子表
    renderSubPopover(h) {
      if (this.clickRowShowSubForm && this.$scopedSlots.subForm) {
        return h('j-vxe-sub-popover', {
          ref: 'subPopover',
          scopedSlots: {
            subForm: this.$scopedSlots.subForm,
          }
        })
      }
      return null
    },
    // 渲染点击时弹出的详细信息
    renderDetailsModal(h) {
      if (this.clickRowShowMainForm && this.$scopedSlots.mainForm) {
        return h('j-vxe-details-modal', {
          ref: 'detailsModal',
          scopedSlots: {
            subForm: this.clickRowShowSubForm ? this.$scopedSlots.subForm : null,
            mainForm: this.$scopedSlots.mainForm
          }
        })
      }
    },
    // 渲染分页器
    renderPagination(h) {
      if (this.pagination && Object.keys(this.pagination).length > 0) {
        return h('j-vxe-pagination', {
          props: {
            size: this.size,
            disabled: this.disabled,
            pagination: this.pagination
          },
          on: {
            change: (e) => this.trigger('pageChange', e)
          },
        })
      }
      return null
    },
    loadExcludeCode(){
      if(!this.authPre || this.authPre.length==0){
        this.excludeCode = []
      }else{
        let pre = this.authPre
        if(!pre.endsWith(':')){
          pre += ':'
        }
        this.excludeCode = getNoAuthCols(pre)
      }
    }

  },
  render(h) {
    return h('div', {
      class: ['j-vxe-table-box', `size--${this.size}`]
    }, [
      this.renderSubPopover(h),
      this.renderDetailsModal(h),
      this.renderToolbar(h),
      this.renderToolbarAfterSlot(),
      this.renderVxeGrid(h),
      this.renderPagination(h),
    ])
  },
  beforeDestroy() {
    this.$emit('beforeDestroy')
  }
}

// 兼容 online 的规则
const fooPatterns = [
  {title: '非空', value: '*', pattern: /^.+$/},
  {title: '6到16位数字', value: 'n6-16', pattern: /^\d{6,16}$/},
  {title: '6到16位任意字符', value: '*6-16', pattern: /^.{6,16}$/},
  {title: '6到18位字母', value: 's6-18', pattern: /^[a-z|A-Z]{6,18}$/},
  {title: '网址', value: 'url', pattern: /^(?:([A-Za-z]+):)?(\/{0,3})([0-9.\-A-Za-z]+)(?::(\d+))?(?:\/([^?#]*))?(?:\?([^#]*))?(?:#(.*))?$/},
  {title: '电子邮件', value: 'e', pattern: /^([\w]+\.*)([\w]+)@[\w]+\.\w{3}(\.\w{2}|)$/},
  {title: '手机号码', value: 'm', pattern: /^1[3456789]\d{9}$/},
  {title: '邮政编码', value: 'p', pattern: /^[1-9]\d{5}$/},
  {title: '字母', value: 's', pattern: /^[A-Z|a-z]+$/},
  {title: '数字', value: 'n', pattern: /^-?\d+(\.?\d+|\d?)$/},
  {title: '整数', value: 'z', pattern: /^-?\d+$/},
  {title: '金额', value: 'money', pattern: /^(([1-9][0-9]*)|([0]\.\d{0,2}|[1-9][0-9]*\.\d{0,2}))$/},
]

/** 旧版handler转为新版Validator */
function handlerConvertToValidator(event) {
  const {column, rule} = event
  return new Promise((resolve, reject) => {
    rule.handler(event, (flag, msg) => {
      let message = rule.message
      if (typeof msg === 'string') {
        message = replaceProps(column.own, msg)
      }
      if (flag == null) {
        resolve(message)
      } else if (!!flag) {
        resolve(message)
      } else {
        reject(new Error(message))
      }
    }, this, event)
  })
}

/** 唯一校验器 */
function uniqueValidator(event) {
  const {cellValue, column, rule} = event
  let tableData = this.getTableData()
  let findCount = 0
  for (let rowData of tableData) {
    if (rowData[column.own.key] === cellValue) {
      if (++findCount >= 2) {
        return Promise.reject(new Error(rule.message))
      }
    }
  }
  return Promise.resolve()
}