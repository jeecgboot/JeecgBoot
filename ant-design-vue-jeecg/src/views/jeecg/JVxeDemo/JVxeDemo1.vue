<template>
  <j-vxe-table
    ref="vTable"
    toolbar
    row-number
    row-selection
    drag-sort
    keep-source
    :height="580"
    :loading="loading"
    :dataSource="dataSource"
    :columns="columns"
    style="margin-top: 8px;"
    @valueChange="handleValueChange"
  >

    <template v-slot:toolbarSuffix>
      <a-button @click="handleTableCheck">表单验证</a-button>
      <a-tooltip placement="top" title="获取值，忽略表单验证" :autoAdjustOverflow="true">
        <a-button @click="handleTableGet">获取值</a-button>
      </a-tooltip>
      <a-tooltip placement="top" title="模拟加载1000条数据" :autoAdjustOverflow="true">
        <a-button @click="handleTableSet">设置值</a-button>
      </a-tooltip>
    </template>

    <template v-slot:action="props">
      <a @click="handleCK(props)">查看</a>
      <a-divider type="vertical"/>
      <a-popconfirm title="确定删除吗？" @confirm="handleDL(props)">
        <a>删除</a>
      </a-popconfirm>
    </template>

  </j-vxe-table>
</template>

<script>
  import moment from 'moment'
  import { pushIfNotExist, randomNumber, randomUUID } from '@/utils/util'
  import { JVXETypes } from '@/components/jeecg/JVxeTable'

  export default {
    name: 'JVxeDemo1',
    data() {
      return {
        loading: false,
        columns: [
          {
            title: '不可编辑',
            key: 'normal',
            type: JVXETypes.normal,
            width: '180px',
            fixed: 'left',
            defaultValue: 'normal-new',
          },
          {
            title: '单行文本',
            key: 'input',
            type: JVXETypes.input,
            width: '180px',
            defaultValue: '',
            placeholder: '请输入${title}',
            validateRules: [
              {
                required: true, // 必填
                message: '请输入${title}' // 显示的文本
              },
              {
                pattern: /^[a-z|A-Z][a-z|A-Z\d_-]*$/, // 正则
                message: '${title}必须以字母开头，可包含数字、下划线、横杠'
              },
              {
                unique: true,
                message: '${title}不能重复'
              },
              {
                handler({cellValue, row, column}, callback, target) {
                  // cellValue 当前校验的值
                  // callback(flag, message) 方法必须执行且只能执行一次
                  //          flag = 是否通过了校验，不填写或者填写 null 代表不进行任何操作
                  //          message = 提示的类型，默认使用配置的 message
                  // target 行编辑的实例对象
                  if (cellValue === 'abc') {
                    callback(false, '${title}不能是abc')  // false = 未通过校验
                  } else {
                    callback(true) // true = 通过验证
                  }
                },
                message: '${title}默认提示'
              }
            ]
          },
          {
            title: '多行文本',
            key: 'textarea',
            type: JVXETypes.textarea,
            width: '200px',
          },
          {
            title: '数字',
            key: 'number',
            type: JVXETypes.inputNumber,
            width: '80px',
            defaultValue: 32,
            // 【统计列】sum = 求和、average = 平均值
            statistics: ['sum', 'average'],
          },
          {
            title: '下拉框',
            key: 'select',
            type: JVXETypes.select,
            width: '180px',
            // 下拉选项
            options: [
              {title: 'String', value: 'string'},
              {title: 'Integer', value: 'int'},
              {title: 'Double', value: 'double'},
              {title: 'Boolean', value: 'boolean'}
            ],
            allowInput: true,
            placeholder: '请选择'
          },
          {
            title: '下拉框_字典',
            key: 'select_dict',
            type: JVXETypes.select,
            width: '180px',
            options: [],
            dictCode: 'sex',
            placeholder: '请选择',
          },
          {
            title: '下拉框_多选',
            key: 'select_multiple',
            type: JVXETypes.selectMultiple,
            width: '205px',
            options: [
              {title: 'String', value: 'string'},
              {title: 'Integer', value: 'int'},
              {title: 'Double', value: 'double'},
              {title: 'Boolean', value: 'boolean'}
            ],
            defaultValue: ['int', 'boolean'], // 多个默认项
            // defaultValue: 'string,double,int', // 也可使用这种方式
            placeholder: '多选',
          },

          {
            title: '下拉框_搜索',
            key: 'select_search',
            type: JVXETypes.selectSearch,
            width: '180px',
            options: [
              {title: 'String', value: 'string'},
              {title: 'Integer', value: 'int'},
              {title: 'Double', value: 'double'},
              {title: 'Boolean', value: 'boolean'}
            ],
          },
          {
            title: '日期时间',
            key: 'datetime',
            type: JVXETypes.datetime,
            width: '200px',
            defaultValue: '2019-4-30 14:52:22',
            placeholder: '请选择',
          },
          {
            title: '复选框',
            key: 'checkbox',
            type: JVXETypes.checkbox,
            width: '100px',
            customValue: ['Y', 'N'], // true ,false
            defaultChecked: false,
          },
          {
            title: '操作',
            key: 'action',
            type: JVXETypes.slot,
            fixed: 'right',
            minWidth: '100px',
            align: 'center',
            slotName: 'action',
          }
        ],
        dataSource: [],
      }

    },

    created() {
      this.randomPage(0, 20, true)
    },
    methods: {

      handleCK(props) {
        this.$message.success('请在控制台查看输出')
        // 参数介绍：
        // props.value          当前单元格的值
        // props.row            当前行的数据
        // props.rowId          当前行ID
        // props.rowIndex       当前行下标
        // props.column         当前列的配置
        // props.columnIndex    当前列下标
        // props.$table         vxe实例，可以调用vxe内置方法
        // props.target         JVXE实例，可以调用JVXE内置方法
        // props.caseId         JVXE实例唯一ID
        // props.scrolling      是否正在滚动
        // props.triggerChange  触发change事件，用于更改slot的值
        console.log('查看: ', {props})
      },

      handleDL(props) {
        // 调用删除方法
        props.target.removeRows(props.row)
      },

      handleValueChange(event) {
        console.log('handleValueChange.event: ', event)
      },

      /** 表单验证 */
      handleTableCheck() {
        this.$refs.vTable.validateTable().then(errMap => {
          if (errMap) {
            console.log('表单验证未通过：', {errMap})
            this.$message.error('验证未通过，请在控制台查看详细')
          } else {
            this.$message.success('验证通过')
          }
        })
      },

      /** 获取值，忽略表单验证 */
      handleTableGet() {
        const values = this.$refs.vTable.getTableData()
        console.log('获取值:', {values})
        this.$message.success('获取值成功，请看控制台输出')
      },

      /** 模拟加载1000条数据 */
      handleTableSet() {
        this.randomPage(1, 1000, true)
      },

      /* 随机生成数据 */
      randomPage(current, pageSize, loading = false) {
        if (loading) {
          this.loading = true
        }

        let randomDatetime = () => {
          let time = randomNumber(1000, 9999999999999)
          return moment(new Date(time)).format('YYYY-MM-DD HH:mm:ss')
        }

        let limit = (current - 1) * pageSize

        let options = ['string', 'int', 'double', 'boolean']

        let begin = Date.now()
        let values = []
        for (let i = 0; i < pageSize; i++) {
          values.push({
            id: randomUUID(),
            normal: `normal-${(limit + i) + 1}`,
            input: `text-${(limit + i) + 1}`,
            textarea: `textarea-${(limit + i) + 1}`,
            number: randomNumber(0, 233),
            select: options[randomNumber(0, 3)],
            select_dict: randomNumber(1, 2).toString(),
            select_multiple: (() => {
              let length = randomNumber(1, 4)
              let arr = []
              for (let j = 0; j < length; j++) {
                pushIfNotExist(arr, options[randomNumber(0, 3)])
              }
              return arr
            })(),
            select_search: options[randomNumber(0, 3)],
            datetime: randomDatetime(),
            checkbox: ['Y', 'N'][randomNumber(0, 1)]
          })
        }

        this.dataSource = values
        let end = Date.now()
        let diff = end - begin

        if (loading && diff < pageSize) {
          setTimeout(() => {
            this.loading = false
          }, pageSize - diff)
        }

      }
    }
  }
</script>

<style scoped>

</style>