<!-- JEditableTable -->
<!-- @version 1.3 -->
<!-- @author sjlei -->
<template>
  <a-spin :spinning="loading">

    <!-- 操作按钮 -->
    <div v-if="actionButton" class="action-button">
      <a-button type="primary" icon="plus" @click="handleClickAdd">新增</a-button>
      <span class="gap"/>
      <template v-if="selectedRowIds.length>0">
        <a-popconfirm
          :title="`确定要删除这 ${selectedRowIds.length} 项吗?`"
          @confirm="handleConfirmDelete">
          <a-button type="primary" icon="minus">删除</a-button>
        </a-popconfirm>
        <template v-if="showClearSelectButton">
          <span class="gap"/>
          <a-button icon="delete" @click="handleClickClearSelect">清空选择</a-button>
        </template>
      </template>
    </div>

    <div :id="`${caseId}inputTable`" class="input-table">
      <!-- 渲染表头 -->
      <div class="thead">
        <div class="tr">
          <!-- 左侧固定td  -->
          <div v-if="rowSelection" class="td td-cb" :style="style.tdLeft">
            <!--:indeterminate="true"-->
            <a-checkbox
              :checked="getSelectAll"
              :indeterminate="getSelectIndeterminate"
              @change="handleChangeCheckedAll"
            />
          </div>
          <div v-if="rowNumber" class="td td-num" :style="style.tdLeft">
            <span>#</span>
          </div>
          <!-- 右侧动态生成td -->
          <template v-for="col in columns">
            <div
              class="td"
              :key="col.key"
              :style="buildTdStyle(col)">

              <span>{{ col.title }}</span>
            </div>
          </template>
        </div>
      </div>
      <!-- 渲染主体 body -->
      <div :id="`${caseId}tbody`" class="tbody" :style="tbodyStyle">
        <!-- 扩展高度 -->
        <div class="tr-expand" :style="`height:${getExpandHeight}px; z-index:${loading?'11':'9'};`"></div>
        <!-- 无数据时显示 -->
        <div v-if="rows.length===0" class="tr-nodata">
          <span>暂无数据</span>
        </div>
        <!-- 动态生成tr -->
        <template v-for="(row,rowIndex) in rows">
          <!-- tr 只加载可见的和预加载的总共十条数据 -->
          <div
            v-if="
              rowIndex >= parseInt(`${(scrollTop-rowHeight) / rowHeight}`) &&
                (parseInt(`${scrollTop / rowHeight}`) + 9) > rowIndex
            "
            :id="`${caseId}tbody-tr-${rowIndex}`"
            :data-idx="rowIndex"
            class="tr"
            :class="selectedRowIds.indexOf(row.id) !== -1 ? 'tr-checked' : ''"
            :style="buildTrStyle(rowIndex)"
            :key="row.id">
            <!-- 左侧固定td  -->
            <div v-if="rowSelection" class="td td-cb" :style="style.tdLeft">
              <!-- 此 v-for 只是为了拼接 id 字符串 -->
              <template v-for="(id,i) in [`${row.id}`]">
                <a-checkbox
                  :id="id"
                  :key="i"
                  :checked="selectedRowIds.indexOf(id) !== -1"
                  @change="handleChangeLeftCheckbox"/>
              </template>
            </div>
            <div v-if="rowNumber" class="td td-num" :style="style.tdLeft">
              <span>{{ rowIndex+1 }}</span>
            </div>
            <!-- 右侧动态生成td -->
            <div
              class="td"
              v-for="col in columns"
              :key="col.key"
              :style="buildTdStyle(col)">

              <!-- 此 v-for 只是为了拼接 id 字符串 -->
              <template v-for="(id,i) in [`${col.key}${row.id}`]">

                <!-- native input -->
                <label :key="i" v-if="col.type === formTypes.input || col.type === formTypes.inputNumber">
                  <a-tooltip
                    :id="id"
                    placement="top"
                    :title="(tooltips[id] || {}).title"
                    :visible="(tooltips[id] || {}).visible || false"
                    :autoAdjustOverflow="true">

                    <input
                      :id="id"
                      v-bind="buildProps(row,col)"

                      :data-input-number="col.type === formTypes.inputNumber"
                      :placeholder="replaceProps(col, col.placeholder)"
                      @input="(e)=>{handleInputCommono(e.target,rowIndex,row,col)}"
                      @mouseover="()=>{handleMouseoverCommono(row,col)}"
                      @mouseout="()=>{handleMouseoutCommono(row,col)}"/>

                  </a-tooltip>

                </label>
                <!-- checkbox -->
                <template v-else-if="col.type === formTypes.checkbox">
                  <a-checkbox
                    :key="i"
                    :id="id"
                    v-bind="buildProps(row,col)"
                    :checked="checkboxValues[id]"
                    @change="handleChangeCheckboxCommon"
                  />
                </template>
                <!-- select -->
                <template v-else-if="col.type === formTypes.select">
                  <!-- select 真身 -->
                  <a-tooltip
                    :key="i"
                    :id="id"
                    placement="top"
                    :title="(tooltips[id] || {}).title"
                    :visible="(tooltips[id] || {}).visible || false"
                    :autoAdjustOverflow="true">

                    <span
                      @mouseover="()=>{handleMouseoverCommono(row,col)}"
                      @mouseout="()=>{handleMouseoutCommono(row,col)}">

                      <a-select
                        :id="id"
                        :key="i"
                        v-bind="buildProps(row,col)"
                        style="width: 100%;"
                        :value="selectValues[id]"
                        :placeholder="replaceProps(col, col.placeholder)"
                        @change="(v)=>handleChangeSelectCommon(v,id,row,col)">

                        <template v-for="(opt,optKey) in col.options">
                          <a-select-option :value="opt.value" :key="optKey">{{ opt.title }}</a-select-option>
                        </template>
                      </a-select>
                    </span>
                  </a-tooltip>
                </template>
                <!-- date -->
                <template v-else-if="col.type === formTypes.date || col.type === formTypes.datetime">
                  <a-tooltip
                    :key="i"
                    :id="id"
                    placement="top"
                    :title="(tooltips[id] || {}).title"
                    :visible="(tooltips[id] || {}).visible || false"
                    :autoAdjustOverflow="true">

                    <span
                      @mouseover="()=>{handleMouseoverCommono(row,col)}"
                      @mouseout="()=>{handleMouseoutCommono(row,col)}">

                      <j-date
                        :id="id"
                        :key="i"
                        v-bind="buildProps(row,col)"
                        style="width: 100%;"
                        :value="jdateValues[id]"
                        :placeholder="replaceProps(col, col.placeholder)"
                        :trigger-change="true"
                        :showTime="col.type === formTypes.datetime"
                        :dateFormat="col.type === formTypes.date? 'YYYY-MM-DD':'YYYY-MM-DD HH:mm:ss'"
                        @change="(v)=>handleChangeJDateCommon(v,id,row,col)"/>

                    </span>
                  </a-tooltip>
                </template>

                <!-- else (normal) -->
                <span v-else :key="i">{{ col.defaultValue }}</span>
              </template>
            </div>
          </div>
          <!-- -- tr end -- -->

        </template>


      </div>
    </div>
  </a-spin>
</template>

<script>

  import { FormTypes, VALIDATE_NO_PASSED } from '@/utils/JEditableTableUtil'
  import { cloneObject, randomString } from '@/utils/util'
  import JDate from '@/components/jeecg/JDate'

  // 行高，需要在实例加载完成前用到
  let rowHeight = 61

  export default {
    name: 'JEditableTable',
    components: { JDate },
    props: {
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
      // 是否显示操作按钮
      actionButton: {
        type: Boolean,
        default: false
      },
      // 是否显示行号
      rowNumber: {
        type: Boolean,
        default: false
      },
      // 是否可选择行
      rowSelection: {
        type: Boolean,
        default: false
      },
      // 页面是否在加载中
      loading: {
        type: Boolean,
        default: false
      },
      // 页面是否在加载中
      maxHeight: {
        type: Number,
        default: 400
      },
      // 要禁用的行
      disabledRows: {
        type: Object,
        default() {
          return {}
        }
      }
    },
    data() {
      return {
        // caseId，用于防止有多个实例的时候会冲突
        caseId: `_jet-${randomString(6)}-`,
        // 存储document element 对象
        el: {
          inputTable: null,
          tbody: null
        },
        // 存储各个div的style
        style: {
          tbody: { left: '0px', 'max-height': '400px' },
          // 左侧固定td的style
          tdLeft: { 'min-width': '4%', 'max-width': '45px' }
        },
        // 表单的类型
        formTypes: FormTypes,
        // 行数据
        rows: [],
        // 行高，height + padding + border
        rowHeight,
        // 滚动条顶部距离
        scrollTop: 0,
        // 绑定 select 的值
        selectValues: {},
        // 绑定 checkbox 的值
        checkboxValues: {},
        // 绑定 jdate 的值
        jdateValues: {},
        // 绑定左侧选择框已选择的id
        selectedRowIds: [],
        // 存储被删除行的id
        deleteIds: [],
        // 存储显示tooltip的信息
        tooltips: {},
        // 存储没有通过验证的inputId
        notPassedIds: []
      }
    },
    created() {
      // 当前显示的tr
      this.visibleTrEls = []
      // 用来存储input表单的值
      // 数组里的每项都是一个对象，对象里每个key都是input的rowKey，值就是input的值，其中有个id的字段来区分
      // 示例：
      // [{
      //    id: "_jet-4sp0iu-15541771111770"
      //    dbDefaultVal: "aaa",
      //    dbFieldName: "bbb",
      //    dbFieldTxt: "ccc",
      //    dbLength: 32
      // }]
      this.inputValues = []
      this.disabledRowIds = (this.disabledRowIds || [])
    },
    // 计算属性
    computed: {
      // expandHeight = rows.length * rowHeight
      getExpandHeight() {
        return this.rows.length * this.rowHeight
      },
      // 获取是否选择了部分
      getSelectIndeterminate() {
        return (this.selectedRowIds.length > 0 &&
          this.selectedRowIds.length < this.rows.length)
      },
      // 获取是否选择了全部
      getSelectAll() {
        return (this.selectedRowIds.length === this.rows.length) && this.rows.length > 0
      },
      tbodyStyle() {
        let style = Object.assign({}, this.style.tbody)
        style['max-height'] = `${this.maxHeight}px`
        return style
      },
      showClearSelectButton() {
        let count = 0
        for (let key in this.disabledRows) {
          if (this.disabledRows.hasOwnProperty(key)) count++
        }
        return count > 0
      }
    },
    // 侦听器
    watch: {
      dataSource: function(newValue) {
        this.initialize()

        let rows = []
        let checkboxValues = {}
        let selectValues = {}
        let jdateValues = {}
        // 禁用行的id
        let disabledRowIds = (this.disabledRowIds || [])
        newValue.forEach(data => {
          let value = { id: this.caseId + data.id }
          let row = { id: value.id }
          let disabled = false
          this.columns.forEach(column => {
            let inputId = column.key + value.id
            let sourceValue = (data[column.key] == null ? '' : data[column.key]).toString()
            if (column.type === FormTypes.checkbox) {

              // 判断是否设定了customValue（自定义值）
              if (column.customValue instanceof Array) {
                let customValue = (column.customValue[0] || '').toString()
                checkboxValues[inputId] = (sourceValue === customValue)
              } else {
                checkboxValues[inputId] = sourceValue
              }

            } else if (column.type === FormTypes.select) {
              selectValues[inputId] = sourceValue ? sourceValue : undefined

            } else if (column.type === FormTypes.date || column.type === FormTypes.datetime) {
              jdateValues[inputId] = sourceValue

            } else {
              value[column.key] = sourceValue
            }

            // 解析disabledRows
            for (let columnKey in this.disabledRows) {
              // 判断是否有该属性
              if (this.disabledRows.hasOwnProperty(columnKey) && data.hasOwnProperty(columnKey)) {
                // row[columnKey] =

                if (disabled !== true) {
                  disabled = this.disabledRows[columnKey] === data[columnKey]
                  if (disabled) {
                    disabledRowIds.push(row.id)
                  }
                }

              }
            }
          })
          this.inputValues.push(value)
          rows.push(row)
        })
        this.disabledRowIds = disabledRowIds
        this.checkboxValues = checkboxValues
        this.selectValues = selectValues
        this.jdateValues = jdateValues
        this.rows = rows

        // 更新form表单的值
        this.$nextTick(() => {
          this.updateFormValues()
        })

      },
      // 当selectRowIds改变时触发事件
      selectedRowIds(newValue) {
        this.$emit('selectRowChange', cloneObject(newValue))
      }
    },
    mounted() {
      // 获取document element对象
      let elements = {};
      ['inputTable', 'tbody'].forEach(id => {
        elements[id] = document.getElementById(this.caseId + id)
      })
      this.el = elements

      let vm = this
      /** 监听滚动条事件 */
      this.el.inputTable.onscroll = function(event) {
        vm.syncScrollBar(event.target.scrollLeft)
      }
      this.el.tbody.onscroll = function(event) {
        vm.recalcTrHiddenItem(event.target.scrollTop)
      }
    },
    methods: {

      /** 初始化列表 */
      initialize() {
        this.visibleTrEls = []
        this.rows = []
        this.deleteIds = []
        this.inputValues = []
        this.selectValues = {}
        this.checkboxValues = {}
        this.jdateValues = {}
        this.selectedRowIds = []
        this.tooltips = {}
        this.notPassedIds = []
        this.scrollTop = 0
        this.$nextTick(() => {
          this.el.tbody.scrollTop = 0
        })
      },

      /** 同步滚动条状态 */
      syncScrollBar(scrollLeft) {
        this.style.tbody.left = `${scrollLeft}px`
        this.el.tbody.scrollLeft = scrollLeft
      },
      /** 重置滚动条位置，参数留空则滚动到上次记录的位置 */
      resetScrollTop(top) {
        if (top != null && typeof top === 'number') {
          this.el.tbody.scrollTop = top
        } else {
          this.el.tbody.scrollTop = this.scrollTop
        }
      },
      /** 重新计算需要隐藏或显示的tr */
      recalcTrHiddenItem(top) {
        let diff = top - this.scrollTop
        if (diff < 0) {
          diff = this.scrollTop - top
        }
        // 只有在滚动了百分之三十的行高的距离时才进行更新
        if (diff >= this.rowHeight * 0.3) {
          this.scrollTop = top
          // 更新form表单的值
          this.$nextTick(() => {
            this.updateFormValues()
          })
        }
      },
      /** push 一条数据 */
      push(record, update = true, rows) {
        if (!(rows instanceof Array)) {
          rows = cloneObject(this.rows) || []
        }


        if (record.id == null) {
          let timestamp = new Date().getTime()
          record.id = `${this.caseId}${timestamp}${rows.length}`
        }
        if (record.id.indexOf(this.caseId) === -1) {
          record.id = this.caseId + record.id
        }
        let row = { id: record.id }
        let value = { id: row.id }
        let checkboxValues = Object.assign({}, this.checkboxValues)
        let selectValues = Object.assign({}, this.selectValues)
        let jdateValues = Object.assign({}, this.jdateValues)
        this.columns.forEach(column => {
          let key = column.key
          let inputId = key + row.id
          // record中是否有该列的值
          let recordHasValue = record[key] != null
          if (column.type === FormTypes.input) {
            value[key] = recordHasValue ? record[key] : column.defaultValue

          } else if (column.type === FormTypes.inputNumber) {
            // 判断是否是排序字段，如果是就赋最大值
            if (column.isOrder === true) {
              value[key] = this.getInputNumberMaxValue(column) + 1
            } else {
              value[key] = recordHasValue ? record[key] : column.defaultValue
            }

          } else if (column.type === FormTypes.checkbox) {
            checkboxValues[inputId] = recordHasValue ? record[key] : column.defaultChecked

          } else if (column.type === FormTypes.select) {
            let selected = column.defaultValue
            if (selected !== 0 && !selected) {
              selected = undefined
            }
            selectValues[inputId] = recordHasValue ? record[key] : selected

          } else if (column.type === FormTypes.date || column.type === FormTypes.datetime) {
            jdateValues[inputId] = recordHasValue ? record[key] : column.defaultValue

          } else {
            value[key] = recordHasValue ? record[key] : ''
          }
        })
        rows.push(row)
        this.inputValues.push(value)
        this.checkboxValues = checkboxValues
        this.selectValues = selectValues
        this.jdateValues = jdateValues

        if (update) {
          this.rows = rows
          this.$nextTick(() => {
            this.updateFormValues()
          })
        }
        return rows
      },
      /** 获取某一数字输入框列中的最大的值 */
      getInputNumberMaxValue(column) {
        let maxNum = 0
        this.inputValues.forEach((item, index) => {
          let val = item[column.key], num
          try {
            num = parseInt(val)
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
      },
      /** 添加一行 */
      add(num = 1, forceScrollToBottom = false) {
        let timestamp = new Date().getTime()
        let rows = this.rows
        for (let i = 0; i < num; i++) {
          let row = { id: `${this.caseId}${timestamp}${rows.length}` }
          rows = this.push(row, false, rows)
        }
        this.rows = rows

        this.$nextTick(() => {
          this.updateFormValues()
        })
        // 设置滚动条位置
        let tbody = this.el.tbody
        let offsetHeight = tbody.offsetHeight
        let realScrollTop = tbody.scrollTop + offsetHeight
        if (forceScrollToBottom === false) {
          // 只有滚动条在底部的时候才自动滚动
          if (!((tbody.scrollHeight - realScrollTop) <= 10)) {
            return
          }
        }
        this.$nextTick(() => {
          tbody.scrollTop = tbody.scrollHeight
        })
        // 触发add事件
        this.$emit('added')
      },
      /** 删除被选中的行 */
      removeSelectedRows() {
        this.removeRows(this.selectedRowIds)
        this.selectedRowIds = []
      },
      /** 删除一行或多行 */
      removeRows(id) {
        let ids = id
        if (!(id instanceof Array)) {
          if (typeof id === 'string') {
            ids = [id]
          } else {
            throw  `InputTable.removeRows() 函数需要的参数可以是string或Array类型，但提供的却是${typeof id}`
          }
        }

        let rows = cloneObject(this.rows)
        ids.forEach(removeId => {
          // 找到每个id对应的真实index并删除
          const findAndDelete = (arr) => {
            for (let i = 0; i < arr.length; i++) {
              if (arr[i].id === removeId) {
                arr.splice(i, 1)
                return true
              }
            }
          }
          // 找到rows对应的index，并删除
          if (findAndDelete(rows)) {
            // 将caseId去除
            this.deleteIds.push(removeId.split(this.caseId)[1])
          }
          // 找到values对应的index，并删除
          findAndDelete(this.inputValues)
        })
        this.rows = rows
        this.$emit('deleted', this.getDeleteIds())
        return true
      },

      /** 获取表格表单里的值 */
      getValues(callback, validate = true) {
        let error = 0
        let valueArray = cloneObject(this.inputValues)
        let tooltips = Object.assign({}, this.tooltips)
        let notPassedIds = cloneObject(this.notPassedIds)
        valueArray.forEach(value => {
          this.columns.forEach(column => {
            let inputId = column.key + value.id
            if (column.type === FormTypes.checkbox) {
              let checked = this.checkboxValues[inputId]
              if (column.customValue instanceof Array) {
                value[column.key] = checked ? column.customValue[0] : column.customValue[1]
              } else {
                value[column.key] = checked
              }

            } else if (column.type === FormTypes.select) {
              value[column.key] = this.selectValues[inputId]

            } else if (column.type === FormTypes.date || column.type === FormTypes.datetime) {
              value[column.key] = this.jdateValues[inputId]

            }
            // 检查表单验证
            if (validate) {
              let results = this.validateOneInput(value[column.key], value, column, notPassedIds, false)
              tooltips[inputId] = results[0]
              if (tooltips[inputId].visible) {
                if (error++ === 0) {
                  // let element = document.getElementById(inputId)
                  // while (element.className !== 'tr') {
                  //   element = element.parentElement
                  // }
                  // this.jumpToId(inputId, element)
                }
              }
              tooltips[inputId].visible = false
              notPassedIds = results[1]
            }
          })
          // 将caseId去除
          value.id = value.id.split(this.caseId)[1]
        })
        this.tooltips = tooltips
        this.notPassedIds = notPassedIds
        if (typeof callback === 'function') callback(error, valueArray)
      },
      /** getValues的Promise版 */
      getValuesPromise(validate = true) {
        return new Promise((resolve, reject) => {
          this.getValues((error, values) => {
            if (error === 0) {
              resolve(values)
            } else {
              reject(VALIDATE_NO_PASSED)
            }
          }, validate)
        })
      },
      /** 获取被删除项的id */
      getDeleteIds() {
        return cloneObject(this.deleteIds)
      },
      /** 获取所有的数据，包括values、deleteIds */
      getAll(validate) {
        return new Promise((resolve, reject) => {
          let deleteIds = this.getDeleteIds()
          this.getValuesPromise(validate).then((values) => {
            resolve({ values, deleteIds })
          }).catch(error => {
            reject(error)
          })
        })
      },
      /** 设置某行某列的值 */
      setValues(values) {

        values.forEach(item => {
          let { rowKey, values: newValues } = item
          for (let newValueKey in newValues) {
            if (newValues.hasOwnProperty(newValueKey)) {
              let newValue = newValues[newValueKey]
              let edited = false // 已被修改
              this.inputValues.forEach(value => {
                // 在inputValues中找到了该字段
                if (`${this.caseId}${rowKey}` === value.id) {
                  edited = true
                  value[newValueKey] = newValue
                }
              })
              let modelKey = `${newValueKey}${this.caseId}${rowKey}`
              // 在 selectValues 中寻找值
              if (!edited && this.selectValues.hasOwnProperty(modelKey)) {
                if (newValue !== 0 && !newValue) {
                  this.selectValues[modelKey] = undefined
                } else {
                  this.selectValues[modelKey] = newValue
                }
                edited = true
              }
              // 在 checkboxValues 中寻找值
              if (!edited && this.checkboxValues.hasOwnProperty(modelKey)) {
                this.checkboxValues[modelKey] = newValue
                edited = true
              }
              // 在 jdateValues 中寻找值
              if (!edited && this.jdateValues.hasOwnProperty(modelKey)) {
                this.jdateValues[modelKey] = newValue
                edited = true
              }
            }
          }
        })
        // 强制更新formValues
        this.forceUpdateFormValues()
      },

      /** 跳转到指定位置 */
      // jumpToId(id, element) {
      //   if (element == null) {
      //     element = document.getElementById(id)
      //   }
      //   if (element != null) {
      //     console.log(this.el.tbody.scrollTop, element.offsetTop)
      //     this.el.tbody.scrollTop = element.offsetTop
      //     console.log(this.el.tbody.scrollTop, element.offsetTop)
      //   }
      // },
      /** 验证单个表单 */
      validateOneInput(value, row, column, notPassedIds, update = false) {
        let tooltips = Object.assign({}, this.tooltips)
        // let notPassedIds = cloneObject(this.notPassedIds)
        let inputId = column.key + row.id
        let [passed, message] = this.validateValue(column.validateRules, value)
        tooltips[inputId] = tooltips[inputId] ? tooltips[inputId] : {}
        tooltips[inputId].visible = !passed
        let index = notPassedIds.indexOf(inputId)
        let borderColor = null, boxShadow = null
        if (!passed) {
          tooltips[inputId].title = this.replaceProps(column, message)
          borderColor = 'red'
          boxShadow = `0 0 0 2px rgba(255, 0, 0, 0.2)`
          if (index === -1) notPassedIds.push(inputId)
        } else {
          if (index !== -1) notPassedIds.splice(index, 1)
        }

        let element = document.getElementById(inputId)
        if (element != null) {
          // select 在 .ant-select-selection 上设置 border-color
          if (column.type === FormTypes.select) {
            element = element.getElementsByClassName('ant-select-selection')[0]
          }
          // jdate 在 input 上设置 border-color
          if (column.type === FormTypes.date || column.type === FormTypes.datetime) {
            element = element.getElementsByTagName('input')[0]
          }
          element.style.borderColor = borderColor
          element.style.boxShadow = boxShadow
        }
        // 是否更新到data
        if (update) {
          this.tooltips = tooltips
          this.notPassedIds = notPassedIds
        }
        return [tooltips[inputId], notPassedIds]
      },
      /** 通过规则验证值是否正确 */
      validateValue(rules, value) {
        if (!(rules instanceof Array)) {
          return [true, ''] // 没有验证规则，或验证规则格式不正确，默认通过
        }

        let passed = true
        let message = ''
        for (let rule of rules) {
          let isNull = (value == null || value === '')
          // 非空验证
          if (rule.required === true) {
            if (isNull) {
              passed = false
              message = rule.message
              break
            }
          } else // 使用 else-if 是为了防止一个 rule 中出现两个规则
          // 正则表达式验证
          if (rule.pattern && !isNull) {
            passed = new RegExp(rule.pattern).test(value)
            message = rule.message
            break
          }

        }
        return [passed, message]
      },

      /** 动态更新表单的值 */
      updateFormValues() {
        let trs = this.el.tbody.getElementsByClassName('tr')
        let trEls = []
        for (let tr of trs) {
          trEls.push(tr)
        }
        // 获取新增的 tr
        let newTrEls = trEls
        if (this.visibleTrEls.length > 0) {
          newTrEls = []
          for (let tr of trEls) {
            let isNewest = true
            for (let vtr of this.visibleTrEls) {
              if (vtr.id === tr.id) {
                isNewest = false
                break
              }
            }
            if (isNewest) {
              newTrEls.push(tr)
            }
          }
        }
        this.visibleTrEls = trEls
        // 向新增的tr中赋值
        newTrEls.forEach(tr => {
          let { idx } = tr.dataset
          let value = this.inputValues[idx]
          for (let key in value) {
            if (value.hasOwnProperty(key)) {
              let elid = `${key}${value.id}`
              let el = document.getElementById(elid)
              if (el) {
                el.value = value[key]
              }
            }
          }
        })
      },
      /** 强制更新FormValues */
      forceUpdateFormValues() {
        this.visibleTrEls = []
        this.updateFormValues()
      },

      /** 全选或取消全选 */
      handleChangeCheckedAll() {
        let selectedRowIds = []
        if (!this.getSelectAll) {
          this.rows.forEach(row => {
            if ((this.disabledRowIds || []).indexOf(row.id) === -1) {
              selectedRowIds.push(row.id)
            }
          })
        }
        this.selectedRowIds = selectedRowIds
      },
      /** 左侧行选择框change事件 */
      handleChangeLeftCheckbox(event) {
        let { id } = event.target

        if ((this.disabledRowIds || []).indexOf(id) !== -1) {
          return
        }

        let index = this.selectedRowIds.indexOf(id)
        if (index !== -1) {
          this.selectedRowIds.splice(index, 1)
        } else {
          this.selectedRowIds.push(id)
        }

      },
      handleClickAdd() {
        this.add()
      },
      handleConfirmDelete() {
        this.removeSelectedRows()
      },
      handleClickClearSelect() {
        this.selectedRowIds = []
      },


      /* --- common function begin --- */

      /** 鼠标移入 */
      handleMouseoverCommono(row, column) {
        let inputId = column.key + row.id
        if (this.notPassedIds.indexOf(inputId) !== -1) {
          this.showOrHideTooltip(inputId, true)
        }
      },
      /** 鼠标移出 */
      handleMouseoutCommono(row, column) {
        let inputId = column.key + row.id
        this.showOrHideTooltip(inputId, false)
      },
      /** input事件 */
      handleInputCommono(target, index, row, column) {
        let { value, dataset, selectionStart } = target

        if (`${dataset.inputNumber}` === 'true') {
          let replace = value.replace(/[^0-9]/g, '')
          if (value !== replace) {
            value = replace
            target.value = replace
            if (typeof  selectionStart === 'number') {
              target.selectionStart = selectionStart - 1
              target.selectionEnd = selectionStart - 1
            }
          }
        }
        // 存储输入的值
        this.inputValues[index][column.key] = value
        // 做单个表单验证
        this.validateOneInput(value, row, column, this.notPassedIds, true)
      },
      handleChangeCheckboxCommon(event) {
        let { id, checked } = event.target
        this.checkboxValues = this.bindValuesChange(checked, id, 'checkboxValues')
      },
      handleChangeSelectCommon(value, id, row, column) {
        this.selectValues = this.bindValuesChange(value, id, 'selectValues')
        // 做单个表单验证
        this.validateOneInput(value, row, column, this.notPassedIds, true)
      },
      handleChangeJDateCommon(value, id, row, column) {
        this.jdateValues = this.bindValuesChange(value, id, 'jdateValues')
        this.validateOneInput(value, row, column, this.notPassedIds, true)
      },
      /** 记录用到数据绑定的组件的值 */
      bindValuesChange(value, id, key) {
        let values = Object.assign({}, this[key])
        values[id] = value
        return values
      },

      /** 显示或隐藏tooltip */
      showOrHideTooltip(inputId, show) {
        let tooltips = Object.assign({}, this.tooltips)
        tooltips[inputId] = tooltips[inputId] ? tooltips[inputId] : {}
        tooltips[inputId].visible = show
        this.tooltips = tooltips
      },

      /* --- common function end --- */

      /* --- 以下是辅助方法，多用于动态构造页面中的数据 --- */

      /** 辅助方法：打印日志 */
      log: console.log,

      /** 辅助方法：替换${...}变量 */
      replaceProps(col, value) {
        if (value && typeof value === 'string') {
          value = value.replace(/\${title}/g, col.title)
          value = value.replace(/\${key}/g, col.key)
          value = value.replace(/\${defaultValue}/g, col.defaultValue)
        }
        return value
      },

      /** view辅助方法：构建 tr style */
      buildTrStyle(index) {
        return {
          'top': `${rowHeight * index}px`
        }
      },
      /** view辅助方法：构建 td style */
      buildTdStyle(col) {
        let style = {}
        // 计算宽度
        if (col.width) {
          style['width'] = col.width
        } else if (this.columns) {
          style['width'] = `${(100 - 4 * 2) / this.columns.length}%`
        } else {
          style['width'] = '120px'
        }
        // checkbox 居中显示
        let isCheckbox = col.type === FormTypes.checkbox
        if (isCheckbox) {
          style['align-items'] = 'center'
          style['text-align'] = 'center'
          style['padding-left'] = '0'
          style['padding-right'] = '0'
        }
        return style
      },
      /** view辅助方法：构造props */
      buildProps(row, col) {
        let props = {}
        // 解析props
        if (typeof col.props === 'object') {
          for (let prop in col.props) {
            if (col.props.hasOwnProperty(prop)) {
              props[prop] = this.replaceProps(col, col.props[prop])
            }
          }
        }
        // 判断是否为禁用的行
        if (props['disabled'] !== true) {
          props['disabled'] = ((this.disabledRowIds || []).indexOf(row.id) !== -1)
        }
        return props
      }

    }
  }
</script>

<style lang="less" scoped>

  .action-button {
    margin-bottom: 8px;

    .gap {
      padding-left: 8px;
    }

  }

  @scrollBarSize: 12px;

  /* 定义滚动条高宽及背景 高宽分别对应横竖滚动条的尺寸*/
  ::-webkit-scrollbar {
    width: @scrollBarSize;
    height: @scrollBarSize;
    background-color: transparent;
  }

  /* 定义滚动条轨道 */
  ::-webkit-scrollbar-track {
    background-color: #f0f0f0;
  }

  /* 定义滑块 */
  ::-webkit-scrollbar-thumb {
    border-radius: @scrollBarSize;
    background-color: #eee;
    box-shadow: inset 0 0 6px rgba(0, 0, 0, 0.3);

    &:hover {
      background-color: #bbb;
    }

    &:active {
      background-color: #888;
    }

  }

  /* 设定边框参数 */
  @borderColor: #e8e8e8;
  @border: 1px solid @borderColor;
  /* tr & td 之间的间距 */
  @spacing: 8px;

  .input-table {
    max-width: 100%;
    overflow-x: auto;
    overflow-y: hidden;
    position: relative;
    border: @border;

    .thead, .tbody {

      .tr, .td {
        display: flex;
      }

      .td {
        /*color: white;*/
        /*background-color: black;*/
        /*margin-right: @spacing !important;*/

        padding-left: @spacing;
        flex-direction: column;

        &.td-cb, &.td-num {
          min-width: 4%;
          max-width: 45px;
          margin-right: 0;
          padding-left: 0;
          padding-right: 0;
          justify-content: center;
          align-items: center;
        }

      }

    }

    .thead {
      overflow-y: scroll;
      border-bottom: @border;

      .td {
        padding: 8px @spacing;
        justify-content: center;
      }

    }

    .tbody {
      position: relative;
      top: 0;
      left: 0;
      overflow-x: hidden;
      overflow-y: scroll;
      min-height: 61px;
      max-height: 400px;

      .tr-nodata {
        color: #999;
        line-height: 61px;
        text-align: center;
      }

      .tr {
        /*line-height: 50px;*/

        border-bottom: @border;
        transition: background-color 300ms;
        width: 100%;
        position: absolute;
        left: 0;
        z-index: 10;

        &.tr-checked {
          background-color: #fafafa;
        }

        &:hover {
          background-color: #E6F7FF;
        }

      }

      .tr-expand {
        position: relative;
        z-index: 9;
        background-color: white;
      }

      .td {
        padding: 14px 0 14px @spacing;

        justify-content: center;

        &:last-child {
          padding-right: @spacing;
        }

        input {
          font-variant: tabular-nums;
          box-sizing: border-box;
          margin: 0;
          list-style: none;
          position: relative;
          display: inline-block;
          padding: 4px 11px;
          width: 100%;
          height: 32px;
          font-size: 14px;
          line-height: 1.5;
          color: rgba(0, 0, 0, 0.65);
          background-color: #fff;
          border: 1px solid #d9d9d9;
          border-radius: 4px;
          transition: all 0.3s;
          outline: none;

          &:hover {
            border-color: #4D90FE
          }

          &:focus {
            border-color: #40a9ff;
            box-shadow: 0 0 0 2px rgba(24, 144, 255, 0.2);
            border-right-width: 1px !important;
          }

          &:disabled {
            color: rgba(0, 0, 0, 0.25);
            background: #f5f5f5;
            cursor: not-allowed;
          }

          /* 设置placeholder的颜色 */
          &::-webkit-input-placeholder { /* WebKit browsers */
            color: #ccc;
          }
          &:-moz-placeholder { /* Mozilla Firefox 4 to 18 */
            color: #ccc;
          }
          &::-moz-placeholder { /* Mozilla Firefox 19+ */
            color: #ccc;
          }
          &:-ms-input-placeholder { /* Internet Explorer 10+ */
            color: #ccc;
          }

        }

      }

    }

  }

</style>