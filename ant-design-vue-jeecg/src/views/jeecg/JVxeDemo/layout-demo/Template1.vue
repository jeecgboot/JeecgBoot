<template>
  <a-card :bordered="false">
    <a-row :gutter="8">
      <!-- 这里是父级节点 -->
      <a-col :span="24" style="margin-bottom: 4px;">
        <j-vxe-table
          toolbar
          row-number
          row-selection
          click-select-row
          highlight-current-row
          :radio-config="{highlight: false}"
          :checkbox-config="{highlight: false}"
          :height="340"
          :loading="table1.loading"
          :columns="table1.columns"
          :dataSource="table1.dataSource"
          :pagination="table1.pagination"
          @pageChange="handleTable1PageChange"
          @selectRowChange="handleTable1SelectRowChange"
        />
      </a-col>
      <!-- 这里是子级节点 -->
      <a-col :span="12">
        <j-vxe-table
          toolbar
          row-number
          row-selection
          click-select-row
          highlight-current-row
          :radio-config="{highlight: false}"
          :checkbox-config="{highlight: false}"
          :height="340"
          :loading="table2.loading"
          :columns="table2.columns"
          :dataSource="table2.dataSource"
          :pagination="table2.pagination"
          @pageChange="handleTable2PageChange"
          @selectRowChange="handleTable2SelectRowChange"
        >
        </j-vxe-table>
      </a-col>
      <!-- 这里是孙级节点 -->
      <a-col :span="12">
        <j-vxe-table
          toolbar
          row-number
          row-selection
          :height="340"
          :loading="table3.loading"
          :columns="table3.columns"
          :dataSource="table3.dataSource"
          :pagination="table3.pagination"
          @pageChange="handleTable3PageChange"
        >
        </j-vxe-table>
      </a-col>
    </a-row>
  </a-card>
</template>

<script>
  import { getAction } from '@api/manage'
  import { JVXETypes } from '@/components/jeecg/JVxeTable'

  // 【多种布局模板】上面父、左下子、右下孙
  export default {
    name: 'Template1',
    data() {
      return {
        table1: {
          // 是否正在加载
          loading: false,
          // 分页器参数
          pagination: {
            // 当前页码
            current: 1,
            // 每页的条数
            pageSize: 200,
            // 可切换的条数
            pageSizeOptions: ['10', '20', '30', '100', '200'],
            // 数据总数（目前并不知道真实的总数，所以先填写0，在后台查出来后再赋值）
            total: 0,
          },
          // 最后选中的行
          lastRow: null,
          // 选择的行
          selectedRows: [],
          // 数据源，控制表格的数据
          dataSource: [],
          // 列配置，控制表格显示的列
          columns: [
            {key: 'num', title: '序号', width: '80px'},
            {
              // 字段key，跟后台数据的字段名匹配
              key: 'ship_name',
              // 列的标题
              title: '船名',
              // 列的宽度
              width: '180px',
              // 如果加上了该属性，就代表当前单元格是可编辑的，type就是表单的类型，input就是简单的输入框
              type: JVXETypes.input,
              formatter({cellValue, row, column}) {
                let foo = ''
                if (row.company === '佧伒侾佯有限公司') {
                  foo += '-233'
                }
                return cellValue + foo
              },
            },
            {key: 'call', title: '呼叫', width: '80px', type: JVXETypes.input},
            {key: 'len', title: '长', width: '80px', type: JVXETypes.inputNumber},
            {key: 'ton', title: '吨', width: '120px', type: JVXETypes.inputNumber},
            {key: 'payer', title: '付款方', width: '120px', type: JVXETypes.input},
            {key: 'count', title: '数', width: '40px'},
            {
              key: 'company',
              title: '公司',
              // 最小宽度，与宽度不同的是，这个不是固定的宽度，如果表格有多余的空间，会平均分配给设置了 minWidth 的列
              // 如果要做占满表格的列可以这么写
              minWidth: '180px',
              type: JVXETypes.input
            },
            {key: 'trend', title: '动向', width: '120px', type: JVXETypes.input},
          ],
        },
        // 子级表的配置信息 （配置和主表的完全一致，就不写冗余的注释了）
        table2: {
          loading: false,
          pagination: {current: 1, pageSize: 200, pageSizeOptions: ['100', '200'], total: 0},
          // 最后选中的行
          lastRow: null,
          selectedRows: [],
          dataSource: [],
          columns: [
            {key: 'dd_num', title: '调度序号', width: '120px'},
            {key: 'tug', title: '拖轮', width: '180px', type: JVXETypes.input},
            {key: 'work_start_time', title: '作业开始时间', width: '180px', type: JVXETypes.input},
            {key: 'work_stop_time', title: '作业结束时间', width: '180px', type: JVXETypes.input},
            {key: 'type', title: '船舶分类', width: '120px', type: JVXETypes.input},
            {key: 'port_area', title: '所属港区', width: '120px', type: JVXETypes.input},
          ],
        },
        // 孙级表的配置信息 （配置和主表的完全一致，就不写冗余的注释了）
        table3: {
          loading: false,
          pagination: {current: 1, pageSize: 200, pageSizeOptions: ['100', '200'], total: 0},
          selectedRows: [],
          dataSource: [],
          columns: [
            {key: 'dd_num', title: '调度序号', width: '120px'},
            {key: 'tug', title: '拖轮', width: '120px', type: JVXETypes.input},
            {key: 'power', title: '马力', width: '120px', type: JVXETypes.input},
            {key: 'nature', title: '性质', width: '120px', type: JVXETypes.input},
            {key: 'departure_time', title: '发船时间', width: '180px', type: JVXETypes.input},
          ],
        },
        // 查询url地址
        url: {
          getData: '/mock/vxe/getData',
        },
      }
    },
    // 监听器
    watch: {
      // 监听table1 【主表】选择的数据发生了变化
      ['table1.lastRow'](row) {
        this.loadTable2Data()
      },
      // 监听table2 【子表】选择的数据发生了变化
      ['table2.lastRow']() {
        this.loadTable3Data()
      },
    },
    created() {
      this.loadTable1Data()
    },
    methods: {

      // 加载table1【主表】的数据
      loadTable1Data() {
        // 封装查询条件
        let formData = {
          pageNo: this.table1.pagination.current,
          pageSize: this.table1.pagination.pageSize
        }
        // 调用查询数据接口
        this.table1.loading = true
        getAction(this.url.getData, formData).then(res => {
          if (res.success) {
            // 后台查询回来的 total，数据总数量
            this.table1.pagination.total = res.result.total
            // 将查询的数据赋值给 dataSource
            this.table1.dataSource = res.result.records
            // 重置选择
            this.table1.selectedRows = []
          } else {
            this.$error({title: '主表查询失败', content: res.message})
          }
        }).finally(() => {
          // 这里是无论成功或失败都会执行的方法，在这里关闭loading
          this.table1.loading = false
        })
      },

      // 当table1【主表】分页参数变化时触发的事件
      handleTable1PageChange(event) {
        // 重新赋值
        this.table1.pagination.current = event.current
        this.table1.pagination.pageSize = event.pageSize
        // 查询数据
        this.loadTable1Data()
      },

      // table1【主表】当选择的行变化时触发的事件
      handleTable1SelectRowChange(event) {
        this.handleTableSelectRowChange(this.table1, event)
      },

      // 加载table2【子表】的数据，根据主表的id进行查询
      loadTable2Data() {
        // 如果主表没有选择，则不查询
        let selectedRows = this.table1.selectedRows
        if (!selectedRows || selectedRows.length === 0) {
          this.table2.pagination.total = 0
          this.table2.dataSource = []
          this.table2.selectedRows = []
          return
        } else if (this.table1.lastRow == null) {
          this.table1.lastRow = selectedRows[selectedRows.length - 1]
        }
        let formData = {
          parentId: this.table1.lastRow.id,
          pageNo: this.table2.pagination.current,
          pageSize: this.table2.pagination.pageSize
        }
        this.table2.loading = true
        getAction(this.url.getData, formData).then(res => {
          if (res.success) {
            this.table2.pagination.total = res.result.total
            this.table2.dataSource = res.result.records
            this.table2.selectedRows = []
          } else {
            this.$error({title: '子表查询失败', content: res.message})
          }
        }).finally(() => {
          this.table2.loading = false
        })
      },

      // table2【子表】当选择的行变化时触发的事件
      handleTable2SelectRowChange(event) {
        this.handleTableSelectRowChange(this.table2, event)
      },

      // 当table2【子表】分页参数变化时触发的事件
      handleTable2PageChange(event) {
        // 重新赋值
        this.table2.pagination.current = event.current
        this.table2.pagination.pageSize = event.pageSize
        // 查询数据
        this.loadTable2Data()
      },

      // 加载table3【孙表】的数据，根据子表的id进行查询
      loadTable3Data() {
        // 如果主表没有选择，则不查询
        let selectedRows = this.table2.selectedRows
        if (!selectedRows || selectedRows.length === 0) {
          this.table3.pagination.total = 0
          this.table3.dataSource = []
          this.table3.selectedRows = []
          return
        } else if (this.table2.lastRow == null) {
          this.table2.lastRow = selectedRows[selectedRows.length - 1]
        }
        let formData = {
          parentId: this.table2.lastRow.id,
          pageNo: this.table3.pagination.current,
          pageSize: this.table3.pagination.pageSize
        }
        this.table3.loading = true
        getAction(this.url.getData, formData).then(res => {
          if (res.success) {
            this.table3.pagination.total = res.result.total
            this.table3.dataSource = res.result.records
          } else {
            this.$error({title: '子表查询失败', content: res.message})
          }
        }).finally(() => {
          this.table3.loading = false
        })
      },
      // 当table3【孙表】分页参数变化时触发的事件
      handleTable3PageChange(event) {
        // 重新赋值
        this.table3.pagination.current = event.current
        this.table3.pagination.pageSize = event.pageSize
        // 查询数据
        this.loadTable3Data()
      },

      /** 公共方法：处理表格选中变化事件 */
      handleTableSelectRowChange(table, event) {
        let {row, action, selectedRows, $table} = event
        // 获取最后一个选中的
        let lastSelected = selectedRows[selectedRows.length - 1]
        if (action === 'selected') {
          table.lastRow = row
        } else if (action === 'selected-all') {
          // 取消全选
          if (selectedRows.length === 0) {
            table.lastRow = null
          } else if (!table.lastRow) {
            table.lastRow = lastSelected
          }
        } else if (action === 'unselected' && row === table.lastRow) {
          table.lastRow = lastSelected
        }
        $table.setCurrentRow(table.lastRow)
        table.selectedRows = selectedRows
      },

    }
  }
</script>

<style lang="less">

</style>