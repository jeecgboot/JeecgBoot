<template>
  <j-vxe-table
    ref="vTable"
    toolbar
    row-number
    row-selection
    keep-source
    :height="484"
    :dataSource="dataSource"
    :columns="columns"
    @valueChange="handleValueChange"
  />
</template>

<script>
  import moment from 'moment'
  import { randomNumber, randomUUID } from '@/utils/util'
  import { JVXETypes } from '@/components/jeecg/JVxeTable'

  export default {
    name: 'JVxeDemo2',
    data() {
      return {
        columns: [
          {
            title: '省/直辖市/自治区',
            key: 's1',
            type: JVXETypes.select,
            width: '240px',
            options: [],
            placeholder: '请选择${title}'
          },
          {
            title: '市',
            key: 's2',
            type: JVXETypes.select,
            width: '240px',
            options: [],
            placeholder: '请选择${title}'
          },
          {
            title: '县/区',
            key: 's3',
            type: JVXETypes.select,
            width: '240px',
            options: [],
            placeholder: '请选择${title}'
          }
        ],
        dataSource: [],
    
        mockData: [
          { text: '北京市', value: '110000', parent: null },
          { text: '天津市', value: '120000', parent: null },
          { text: '河北省', value: '130000', parent: null },
          { text: '上海市', value: '310000', parent: null },
      
          { text: '北京市', value: '110100', parent: '110000' },
          { text: '天津市市', value: '120100', parent: '120000' },
          { text: '石家庄市', value: '130100', parent: '130000' },
          { text: '唐山市', value: '130200', parent: '130000' },
          { text: '秦皇岛市', value: '130300', parent: '130000' },
          { text: '上海市', value: '310100', parent: '310000' },
      
          { text: '东城区', value: '110101', parent: '110100' },
          { text: '西城区', value: '110102', parent: '110100' },
          { text: '朝阳区', value: '110105', parent: '110100' },
          { text: '和平区', value: '120101', parent: '120100' },
          { text: '河东区', value: '120102', parent: '120100' },
          { text: '河西区', value: '120103', parent: '120100' },
          { text: '黄浦区', value: '310101', parent: '310100' },
          { text: '徐汇区', value: '310104', parent: '310100' },
          { text: '长宁区', value: '310105', parent: '310100' },
          { text: '长安区', value: '130102', parent: '130100' },
          { text: '桥西区', value: '130104', parent: '130100' },
          { text: '新华区', value: '130105', parent: '130100' },
          { text: '路南区', value: '130202', parent: '130200' },
          { text: '路北区', value: '130203', parent: '130200' },
          { text: '古冶区', value: '130204', parent: '130200' },
          { text: '海港区', value: '130302', parent: '130300' },
          { text: '山海关区', value: '130303', parent: '130300' },
          { text: '北戴河区', value: '130304', parent: '130300' },
        ]
      }

    },
    created() {
      // 初始化数据
      this.columns[0].options = this.request(null)
    },
    methods: {
      
      request(parentId) {
        return this.mockData.filter(i => i.parent === parentId)
      },
  
      /** 当选项被改变时，联动其他组件 */
      handleValueChange(event) {
        const { type, row, column, value, target } = event
        console.log("event",event)
        if (type === JVXETypes.select) {
      
          // 第一列
          if (column.key === 's1') {
            // 设置第二列的 options
            console.log('this.request(value)::',this.request(value))
            target.$refs.vxe.columns[3].options = this.request(value)
            // 清空后两列的数据
            target.setValues([{
              rowKey: row.id,
              values: { s2: '', s3: '' }
            }])
            target.$refs.vxe.columns[4].options = []
          } else
          // 第二列
          if (column.key === 's2') {
            target.$refs.vxe.columns[4].options = this.request(value)
            target.setValues([{
              rowKey: row.id,
              values: { s3: '' }
            }])
          }
        }
    
      }
    }
  }
</script>

<style scoped>

</style>