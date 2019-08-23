<template>
  <a-card :bordered="false">
    <a-table
      rowKey="id"
      bordered
      :columns="columns"
      :dataSource="dataSource"
      :pagination="false"
    >
    </a-table>
  </a-card>
</template>

<script>
  export default {
    name: 'TableTotal',
    data() {
      return {
        columns: [
          {
            title: '#',
            width: '180px',
            align: 'center',
            dataIndex: 'rowIndex',
            customRender: function (text, r, index) {
              return (text !== '合计') ? (parseInt(index) + 1) : text
            }
          },
          {
            title: '姓名',
            dataIndex: 'name',
          },
          {
            title: '贡献点',
            dataIndex: 'point',
          },
          {
            title: '等级',
            dataIndex: 'level',
          },
          {
            title: '更新时间',
            dataIndex: 'updateTime',
          },
        ],
        dataSource: [
          { name: '张三', point: 23, level: 3, updateTime: '2019-8-14' },
          { name: '小王', point: 6, level: 1, updateTime: '2019-8-13' },
          { name: '李四', point: 53, level: 8, updateTime: '2019-8-12' },
          { name: '小红', point: 44, level: 5, updateTime: '2019-8-11' },
          { name: '王五', point: 97, level: 10, updateTime: '2019-8-10' },
          { name: '小明', point: 33, level: 2, updateTime: '2019-8-10' },
        ]
      }
    },
    mounted() {
      this.tableAddTotalRow(this.columns, this.dataSource)
    },
    methods: {

      /** 表格增加合计行 */
      tableAddTotalRow(columns, dataSource) {
        let numKey = 'rowIndex'
        let totalRow = { [numKey]: '合计' }
        columns.forEach(column => {
          let { key, dataIndex } = column
          if (![key, dataIndex].includes(numKey)) {

            let total = 0
            dataSource.forEach(data => {
              total += /^\d+\.?\d?$/.test(data[dataIndex]) ? Number.parseInt(data[dataIndex]) : Number.NaN
              console.log(data[dataIndex], ':', (/^\d+\.?\d?$/.test(data[dataIndex]) ? Number.parseInt(data[dataIndex]) : Number.NaN))
            })

            if (Number.isNaN(total)) {
              total = '-'
            }
            totalRow[dataIndex] = total
          }
        })

        dataSource.push(totalRow)
      }

    }
  }
</script>

<style scoped>

</style>