<template>

  <a-card :borderd="false">


    <a-button @click="handleTableCheck" type="primary">表单验证</a-button>
    <span style="padding-left:8px;"></span>
    <a-tooltip placement="top" title="获取值，忽略表单验证" :autoAdjustOverflow="true">
      <a-button @click="handleTableGet" type="primary">获取值</a-button>
    </a-tooltip>
    <span style="padding-left:8px;"></span>
    <a-tooltip placement="top" title="模拟加载1000条数据" :autoAdjustOverflow="true">
      <a-button @click="handleTableSet" type="primary">设置值</a-button>
    </a-tooltip>


    <j-editable-table
      ref="editableTable"
      :loading="loading"
      :columns="columns"
      :dataSource="dataSource"
      :rowNumber="true"
      :rowSelection="true"
      :actionButton="true"
      style="margin-top: 8px;"
      @selectRowChange="handleSelectRowChange"/>

  </a-card>

</template>

<script>


  import JEditableTable from '@/components/jeecg/JEditableTable'
  import { FormTypes } from '@/utils/JEditableTableUtil'
  import { randomUUID, randomString, randomNumber } from '@/utils/util'

  export default {
    name: 'JeecgEditableTableExample',
    components: {
      JEditableTable
    },
    data() {
      return {
        loading: false,
        columns: [
          {
            title: '字段名称',
            key: 'dbFieldName',
            width: '19%',
            type: FormTypes.input,
            defaultValue: '',
            placeholder: '请输入${title}',
            validateRules: [
              {
                required: true, // 必填
                message: '请输入${title}' // 显示的文本
              },
              {
                pattern: /^[a-z|A-Z][a-z|A-Z\d_-]{0,}$/, // 正则
                message: '${title}必须以字母开头，可包含数字、下划线、横杠'
              }
            ]
          },
          {
            title: '字段备注',
            key: 'dbFieldTxt',
            width: '19%',
            type: FormTypes.input,
            defaultValue: '',
            placeholder: '请输入${title}',
            validateRules: [{ required: true, message: '请输入${title}' }]
          },
          {
            title: '字段类型',
            key: 'dbFieldType',
            width: '18%',
            type: FormTypes.select,
            options: [ // 下拉选项
              { title: 'String', value: 'string' },
              { title: 'Integer', value: 'int' },
              { title: 'Double', value: 'double' },
              { title: 'Boolean', value: 'boolean' }
            ],
            defaultValue: '',
            placeholder: '请选择${title}',
            validateRules: [{ required: true, message: '请选择${title}' }]
          },
          {
            title: '字段长度',
            key: 'dbLength',
            width: '8%',
            type: FormTypes.inputNumber,
            defaultValue: 32,
            placeholder: '${title}',
            validateRules: [{ required: true, message: '请输入${title}' }]
          },
          {
            title: '默认值',
            key: 'dbDefaultVal',
            width: '22%',
            type: FormTypes.input,
            defaultValue: '',
            placeholder: '请输入${title}',
            validateRules: [{ required: true, message: '请输入${title}' }]
          },
          {
            title: '可以为空',
            key: 'isNull',
            width: '8%',
            type: FormTypes.checkbox,
            customValue: ['Y', 'N'], // true ,false
            defaultChecked: false
          }

        ],
        dataSource: [],
        selectedRowIds: []

      }
    },
    created() {

    },
    mounted() {
      this.randomData(23, false)
    },
    methods: {

      /** 表单验证 */
      handleTableCheck() {
        this.$refs.editableTable.getValues((error) => {
          if (error === 0) {
            this.$message.success('验证通过')
          } else {
            this.$message.error('验证未通过')
          }
        })
      },
      /** 获取值，忽略表单验证 */
      handleTableGet() {
        this.$refs.editableTable.getValues((error, values) => {
          console.log('values:', values)
        }, false)
        console.log('deleteIds:', this.$refs.editableTable.getDeleteIds())

        this.$message.info('获取值成功，请看控制台输出')

      },
      /** 模拟加载1000条数据 */
      handleTableSet() {
        this.randomData(1000, true)
      },

      handleSelectRowChange(selectedRowIds) {
        this.selectedRowIds = selectedRowIds
      },

      /* 随机生成数据 */
      randomData(size, loading = false) {
        if (loading) {
          this.loading = true
          setTimeout(() => {
            this.loading = false
          }, 3000)
        }

        let values = []
        for (let i = 0; i < size; i++) {
          values.push({
            id: randomUUID(),
            dbFieldName: `name_${i + 1}`,
            dbFieldTxt: randomString(10),
            dbFieldType: ['string', 'int', 'double', 'boolean'][randomNumber(0, 3)],
            dbLength: randomNumber(0, 233),
            dbDefaultVal: randomString(8),
            isNull: ['Y', 'N'][randomNumber(0, 1)]
          })
        }
        this.dataSource = values
      }


    }
  }
</script>

<style scoped>

</style>