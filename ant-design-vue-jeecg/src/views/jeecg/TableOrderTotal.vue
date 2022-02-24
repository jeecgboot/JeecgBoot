<template>
  <a-card :bordered="false">
    <a-row>
      <a-col>
        <a-switch v-bind="pageSwitchProps" v-model="pageSwitch"/>
      </a-col>
      <a-col>
        <a-table
            v-bind="tableProps"
            @change="handleTableChange"
            style="margin-top: 20px"
        >
        </a-table>
      </a-col>
    </a-row>
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
              return (text !== '总计') ? (parseInt(index) + 1) : text
            }
          },
          {
            title: '姓名',
            dataIndex: 'name',
            width: 180,
          },
          {
            title: '贡献点',
            dataIndex: 'point',
            width: 180,
          },
          {
            title: '等级',
            dataIndex: 'level',
            width: 180,
          },
          {
            title: '更新时间',
            dataIndex: 'updateTime',
            width: 180,
          },
        ],
        /* 分页参数 */
        ipagination:{
          current: 1,
          pageSize: 10,
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
        footerDataSource: [],
        pageSwitch:true
      }
    },
    computed:{
      // 数据表格的固定属性
      tableProps(){
        let tableProps = {
          size: 'middle',
          rowKey:'rowIndex',
          columns: this.columns,
          scroll: {x: true},
        }
        let renderFooter = this.footerDataSource.length === 0 ? null : () => this.renderTableFooter(tableProps)
        return {
          ...tableProps,
          ref: 'table',
          class: 'chart-data-list',
          pagination:this.pageSwitch?this.ipagination:false,
          columns: this.columns,
          dataSource: this.dataSource,
          footer: renderFooter,
        }
      },
      pageSwitchProps() {
        return {
          checkedChildren: '分页',
          unCheckedChildren: '分页',
          style: {
            position: 'absolute',
            right: '0px',
            top: '-10px'
          }
        }
      },
    },
    mounted() {
      // this.tableAddTotalRow(this.columns, this.dataSource)
      /*新增分页合计方法*/
      this.newDataSource=this.dataSource
      this.dataHandling(1,this.ipagination.pageSize)
    },
    watch:{
      //update-begin---author:wangshuai ---date:20220209  for：[JTC-494]常用示例->表格合计写法改成新的写法------------
      'pageSwitch':function(val){
         if(!val){
           this.dataHandling('-1',0)
         }else{
           this.dataHandling(1,this.ipagination.pageSize)
         }
      },
      'ipagination.current':function(val) {
        this.dataHandling(val,this.ipagination.pageSize)
      },
      //当合计行变化时，绑定滚动条
      'footerDataSource': {
        async handler(dataSource) {
          // 当底部合计行有值，并且显示出来时，再同步滚动条
          if (dataSource && dataSource.length > 0) {
            await this.$nextTick()
            // 同步表与footer滚动
            let dom = this.$refs.table.$el.querySelectorAll('.ant-table-body')[0]
            let footerDom = this.$refs.footerTable.$el.querySelectorAll('.ant-table-body')[0]
            dom.addEventListener(
                'scroll',
                () => {
                  footerDom.scrollLeft = dom.scrollLeft
                },
                true,
            )
          }
        },
        //update-end---author:wangshuai ---date:20220209  for：[JTC-494]常用示例->表格合计写法改成新的写法------------
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
      //update-begin---author:wangshuai ---date:20220209  for：[JTC-494]常用示例->表格合计写法改成新的写法------------
      /*如果分页走这个方法*/
      dataHandling(pageNo,pageSize) {
        //根据当前页数和每页显示条数分割数组
        let arrs = [];
        //如果pageNo不是-1（不分页）,那么需要对数据进行分页计算
        if(pageNo!=-1){
          arrs = this.newDataSource.slice((pageNo-1)*pageSize,pageNo*pageSize)
        }else{
          arrs = this.newDataSource
        }
        let newDataSource=[];
        let newArr= { };
        newArr.rowIndex="总计"
        let level=0;
        let point=0;
        //每一项的数值相加
        for (let j=0;j<arrs.length;j++){
          level+=arrs[j].level;
          point+=arrs[j].point;
        }
        newArr.level=level;
        newArr.point=point;
        newDataSource.push(newArr);
        //给foot底部数组赋值
        this.footerDataSource = newDataSource;
      },
      // 渲染表格底部合计行
      renderTableFooter(tableProps) {
        let h = this.$createElement
        return h('a-table', {
          ref: 'footerTable',
          props: {
            ...tableProps,
            pagination: false,
            dataSource: this.footerDataSource,
            showHeader: false,
          },
        })
        //update-end---author:wangshuai ---date:20220209  for：[JTC-494]常用示例->表格合计写法改成新的写法------------
      }
    }
  }
</script>

<style scoped lang="less">
/deep/ .chart-data-list .ant-table-footer .ant-table-body{
  overflow: hidden !important;
}
/deep/ .ant-table-footer{
  padding:0;
}
</style>