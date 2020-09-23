<template>
  <a-card :bordered="false">
    <a-table
      rowKey="rowIndex"
      bordered
      :columns="columns"
      :dataSource="dataSource"
      :pagination="ipagination"
      @change="handleTableChange"
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
        /* 分页参数 */
        ipagination:{
          current: 1,
          pageSize: 10,
          pageSizeOptions: ['10', '20', '30'],
          showTotal: (total, range) => {
            return range[0] + "-" + range[1] + " 共" + total + "条"
          },
          showQuickJumper: true,
          showSizeChanger: true,
          total: 0
        },
        dataSource: [
          { id:"1",name: '张三', point: 23, level: 3, updateTime: '2019-8-14' },
          { name: '小王', point: 6, level: 1, updateTime: '2019-8-13' },
          { name: '李四', point: 53, level: 8, updateTime: '2019-8-12' },
          { name: '小红', point: 44, level: 5, updateTime: '2019-8-11' },
          { name: '王五', point: 97, level: 10, updateTime: '2019-8-10' },
          { name: '小明', point: 33, level: 2, updateTime: '2019-8-10' },
          { name: '小张', point: 33, level: 2, updateTime: '2019-8-10' },
          { name: '小六', point: 33, level: 2, updateTime: '2019-8-10' },
          { name: '小五', point: 33, level: 2, updateTime: '2019-8-10' },
          { name: '小赵', point: 33, level: 2, updateTime: '2019-8-10' },
          { name: '李华', point: 33, level: 2, updateTime: '2019-8-10' },
          { name: '小康', point: 33, level: 2, updateTime: '2019-8-10' },
          { name: '小鹿', point: 33, level: 2, updateTime: '2019-8-10' },
        ],
        newArr:[],
        newDataSource:[],
      }
    },
    mounted() {
      // this.tableAddTotalRow(this.columns, this.dataSource)
      /*新增分页合计方法*/
      this.newDataSource=this.dataSource
      this.dataHandling(this.ipagination.pageSize-1)
    },
    watch:{
      'ipagination.pageSize':function(val) {
        this.dataHandling(val-1)
      }
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
      },
      handleTableChange(pagination, filters, sorter) {
        this.ipagination = pagination;
      },
      /*如果分页走这个方法*/
      dataHandling(num) {
        this.newArr=[];
        let arrLength = this.newDataSource.length; // 数组长度;
        let index = 0;
        for (let i = 0; i < arrLength; i++) {
          if (i % num === 0 && i !== 0) {
            this.newArr.push(this.newDataSource.slice(index, i));
            index = i;
          }
          if ((i + 1) === arrLength) {
            this.newArr.push(this.newDataSource.slice(index, (i + 1)));
          }
        }
        var arrs=this.newArr;
        for (let i =0;i<arrs.length;i++){
          let arr = arrs[i];
          let newArr= { };
          newArr.name="-";
          newArr.updateTime="-";
          newArr.rowIndex="合计"
          var level=0;
          var point=0;
          for (let j=0;j<arr.length;j++){
            level+=arr[j].level;
            point+=arr[j].point;
          }
          newArr.level=level;
          newArr.point=point;
          arrs[i].push(newArr);
        }
        var newDataSource=[];
        for (let i =0;i<arrs.length;i++){
          let arr = arrs[i];
          for(var j in arr){
            newDataSource.push(arr[j]);
          }
        }
        console.log(this.dataSource)
        this.dataSource = Object.values(newDataSource);
        console.log(this.dataSource)
      }
    }
  }
</script>

<style scoped>

</style>