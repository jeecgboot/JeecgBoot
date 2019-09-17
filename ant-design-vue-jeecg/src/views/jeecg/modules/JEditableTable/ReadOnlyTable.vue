<template>
  <j-editable-table
    :columns="columns"
    :dataSource="dataSource"
    :rowNumber="true"
    :rowSelection="true"
    :maxHeight="400"
    :disabled="true"
  />
</template>

<script>
  import { FormTypes } from '@/utils/JEditableTableUtil'
  import JEditableTable from '@/components/jeecg/JEditableTable'

  export default {
    name: 'ReadOnlyTable',
    components: { JEditableTable },
    data() {
      return {

        columns: [
          {
            title: '输入框',
            key: 'input',
            type: FormTypes.input,
            placeholder: '清输入'
          },
          {
            title: '下拉框',
            key: 'select',
            type: FormTypes.select,
            options: [
              { title: 'String', value: 'string' },
              { title: 'Integer', value: 'int' },
              { title: 'Double', value: 'double' },
              { title: 'Boolean', value: 'boolean' }
            ],
            placeholder: '请选择'
          },
          {
            title: '多选框',
            key: 'checkbox',
            type: FormTypes.checkbox,
            customValue: [true, false]
          },
          {
            title: '日期',
            key: 'datetime',
            type: FormTypes.datetime
          }
        ],
        dataSource: [
          { input: 'hello', select: 'int', checkbox: true, datetime: '2019-6-17 14:50:48' },
          { input: 'world', select: 'string', checkbox: false, datetime: '2019-6-16 14:50:48' },
          { input: 'one', select: 'double', checkbox: true, datetime: '2019-6-17 15:50:48' },
          { input: 'two', select: 'boolean', checkbox: false, datetime: '2019-6-14 14:50:48' },
          { input: 'three', select: '', checkbox: false, datetime: '2019-6-13 14:50:48' }
        ]
      }
    },
    mounted() {

    }
  }
</script>

<style scoped>

</style>