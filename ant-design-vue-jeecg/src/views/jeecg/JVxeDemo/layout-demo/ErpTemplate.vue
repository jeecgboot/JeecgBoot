<template>

  <a-card :bordered="false">
    <j-vxe-table
      toolbar
      :toolbarConfig="toolbarConfig"

      row-number
      row-selection
      row-selection-type="radio"
      highlight-current-row
      click-select-row
      :height="tableHeight"
      :loading="table1.loading"
      :columns="table1.columns"
      :dataSource="table1.dataSource"
      :pagination="table1.pagination"
      :expand-config="expandConfig"
      style="margin-bottom: 8px"

      @pageChange="handleTable1PageChange"
      @selectRowChange="handleTable1SelectRowChange"
    ></j-vxe-table>

    <a-tabs v-show="subTabs.show" :class="{'sub-tabs':true, 'un-expand': !subTabs.expand}">
      <a-tab-pane tab="子表1" key="1">
        <j-vxe-table
          toolbar
          row-number
          row-selection
          height="auto"
          :maxHeight="350"
          :loading="table2.loading"
          :columns="table2.columns"
          :dataSource="table2.dataSource"
          :pagination="table2.pagination"
          @pageChange="handleTable2PageChange"
          @selectRowChange="handleTable2SelectRowChange"
        />
      </a-tab-pane>
      <a-tab-pane tab="子表2" key="2">
        <h1>这里是子表2</h1>
        <h1>这里是子表2</h1>
        <h1>这里是子表2</h1>
        <h1>这里是子表2</h1>
        <h1>这里是子表2</h1>
        <h1>这里是子表2</h1>
      </a-tab-pane>
    </a-tabs>

  </a-card>
</template>

<script>
  import { JVXETypes } from '@/components/jeecg/JVxeTable'
  import { getAction } from '@api/manage'

  export default {
    name: 'ErpTemplate',
    data() {
      return {
        toolbarConfig: {
          // prefix 前缀；suffix 后缀
          slot: ['prefix', 'suffix'],
          // add 新增按钮；remove 删除按钮；clearSelection 清空选择按钮
          btn: ['add', 'remove', 'clearSelection']
        },

        expandConfig: {
          // 是否只能同时展开一行
          accordion: true
        },

        // 子表 tabs
        subTabs: {
          show: false,
          // 是否展开
          expand: true,
          // 是否自动展开
          autoExpand: true,
        },

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
            showTotal: (total, range) => {
              // 此处为 jsx 语法
              let text = <span>{range[0] + '-' + range[1] + ' 共 ' + total + ' 条'}</span>
              // 判断子表是否显示，如果显示就渲染展开收起按钮
              if (this.subTabs.show) {
                let expand = (<span>
                <a-button type="link" onClick={this.handleToggleTabs}>
                  <a-icon type={this.subTabs.expand ? 'up' : 'down'}/>
                  <span>{this.subTabs.expand ? '收起' : '展开'}</span>
                </a-button>
                <a-checkbox vModel={this.subTabs.autoExpand}>自动展开</a-checkbox>
              </span>)
                // 返回多个dom用数组
                return [expand, text]
              } else {
                // 直接返回单个dom
                return text
              }
            },
          },
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
              type: JVXETypes.input
            },
            {key: 'call', title: '呼叫', width: '990px', type: JVXETypes.input},
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
          currentRowId: null,
          loading: false,
          pagination: {current: 1, pageSize: 10, pageSizeOptions: ['5', '10', '20', '30'], total: 0},
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
        currentSubRow: null,
        // 查询url地址
        url: {
          getData: '/mock/vxe/getData',
        },
      }
    },
    computed: {
      tableHeight() {
        let {show, expand} = this.subTabs
        return show ? (expand ? 350 : 482) : 482
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

      // 查询子表数据
      loadSubData(row) {
        if (row) {
          // 这里一定要做限制，限制不能重复查询，否者会出现死循环
          if (this.table2.currentRowId === row.id) {
            return true
          }
          this.table2.currentRowId = row.id
          this.loadTable2Data()
          return true
        } else {
          return false
        }
      },
      // 查询子表数据
      loadTable2Data() {
        let table2 = this.table2
        let formData = {
          parentId: table2.currentRowId,
          pageNo: this.table2.pagination.current,
          pageSize: this.table2.pagination.pageSize
        }
        table2.loading = true
        getAction(this.url.getData, formData).then(res => {
          if (res.success) {
            // 将查询的数据赋值给 dataSource
            table2.selectedRows = []
            table2.dataSource = res.result.records
            table2.pagination.total = res.result.total
          } else {
            this.$error({title: '子表查询失败', content: res.message})
          }
        }).finally(() => {
          // 这里是无论成功或失败都会执行的方法，在这里关闭loading
          table2.loading = false
        })
      },


      // table1【主表】当选择的行变化时触发的事件
      handleTable1SelectRowChange(event) {
        this.table1.selectedRows = event.selectedRows
        this.subTabs.show = true
        if (this.subTabs.autoExpand) {
          this.subTabs.expand = true
        }
        this.loadSubData(event.selectedRows[0])
      },
      // table2【子表】当选择的行变化时触发的事件
      handleTable2SelectRowChange(event) {
        this.table2.selectedRows = event.selectedRows
      },

      handleTable1PageChange(event) {
        // 重新赋值
        this.table1.pagination.current = event.current
        this.table1.pagination.pageSize = event.pageSize
        // 查询数据
        this.loadTable1Data()
      },
      // 当table2【子表】分页参数变化时触发的事件
      handleTable2PageChange(event) {
        // 重新赋值
        this.table2.pagination.current = event.current
        this.table2.pagination.pageSize = event.pageSize
        // 查询数据
        this.loadTable2Data()
      },

      // 展开或收起子表tabs
      handleToggleTabs() {
        this.subTabs.expand = !this.subTabs.expand
      },

    },

  }
</script>

<style lang="less" scoped>
  .sub-tabs {
    &.un-expand {
      /deep/ .ant-tabs-content {
        height: 0 !important;
      }

      /deep/ .ant-tabs-bar {
        border-color: transparent !important;
      }

      /deep/ .ant-tabs-ink-bar {
        background-color: transparent !important;
      }

      /deep/ .ant-tabs-tab {
        display: none !important;
      }
    }
  }
</style>