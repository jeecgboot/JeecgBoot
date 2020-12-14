<template>
  <a-card title="即时保存示例" :bordered="false">
    <!--
      【即时保存大体思路】：
      1. JVxeTable 上必须加 keep-source 属性
      2. 监听 edit-closed事件，这个事件是在编辑完成后触发
      3. 在这个事件里面判断数据是否更改，如果更改了就调用接口进行保存操作
    -->
    <j-vxe-table
      toolbar
      :toolbarConfig="toolbarConfig"

      row-number
      row-selection
      keep-source
      async-remove

      :height="340"
      :loading="loading"
      :columns="columns"
      :dataSource="dataSource"
      :pagination="pagination"

      @save="handleTableSave"
      @remove="handleTableRemove"
      @edit-closed="handleEditClosed"
      @pageChange="handlePageChange"
      @selectRowChange="handleSelectRowChange"
    />
  </a-card>
</template>

<script>
  import { getAction, postAction, putAction } from '@api/manage'
  import { JVXETypes } from '@/components/jeecg/JVxeTable'

  // 即时保存示例
  export default {
    name: 'JSBCDemo',
    data() {
      return {
        // 工具栏的按钮配置
        toolbarConfig: {
          // add 新增按钮；remove 删除按钮；clearSelection 清空选择按钮
          btn: ['add', 'save', 'remove', 'clearSelection']
        },
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
          {key: 'call', title: '呼叫', width: '80px', type: JVXETypes.input},
          {key: 'len', title: '长', width: '80px', type: JVXETypes.input},
          {key: 'ton', title: '吨', width: '120px', defaultValue: 233, type: JVXETypes.input},
          {key: 'payer', title: '付款方', width: '120px', defaultValue: '张三', type: JVXETypes.input},
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
        // 查询url地址
        url: {
          getData: '/mock/vxe/getData',
          // 模拟保存单行数据（即时保存）
          saveRow: '/mock/vxe/immediateSaveRow',
          // 模拟保存整个表格的数据
          saveAll: '/mock/vxe/immediateSaveAll',
        },
      }
    },
    created() {
      this.loadData()
    },
    methods: {

      // 加载数据
      loadData() {
        // 封装查询条件
        let formData = {
          pageNo: this.pagination.current,
          pageSize: this.pagination.pageSize
        }
        // 调用查询数据接口
        this.loading = true
        getAction(this.url.getData, formData).then(res => {
          if (res.success) {
            // 后台查询回来的 total，数据总数量
            this.pagination.total = res.result.total
            // 将查询的数据赋值给 dataSource
            this.dataSource = res.result.records
            // 重置选择
            this.selectedRows = []
          } else {
            this.$error({title: '主表查询失败', content: res.message})
          }
        }).finally(() => {
          // 这里是无论成功或失败都会执行的方法，在这里关闭loading
          this.loading = false
        })
      },

      // 【整体保存】点击保存按钮时触发的事件
      handleTableSave({$table, target}) {
        // 校验整个表格
        $table.validate().then((errMap) => {
          // 校验通过
          if (!errMap) {
            // 获取所有数据
            let tableData = target.getTableData()
            console.log('当前保存的数据是：', tableData)
            // 获取新增的数据
            let newData = target.getNewData()
            console.log('-- 新增的数据：', newData)
            // 获取删除的数据
            let deleteData = target.getDeleteData()
            console.log('-- 删除的数据：', deleteData)

            // 【模拟保存】
            this.loading = true
            postAction(this.url.saveAll, tableData).then(res => {
              if (res.success) {
                this.$message.success(`保存成功！`)
              } else {
                this.$message.warn(`保存失败：` + res.message)
              }
            }).finally(() => {
              this.loading = false
            })
          }
        })
      },

      // 触发单元格删除事件
      handleTableRemove(event) {

        // 把 event.deleteRows 传给后台进行删除（注意：这里不会传递前端逻辑新增的数据，因为不需要请求后台删除）
        console.log('待删除的数据: ', event.deleteRows)
        // 也可以只传ID，因为可以根据ID删除
        let deleteIds = event.deleteRows.map(row => row.id)
        console.log('待删除的数据ids: ', deleteIds)

        // 模拟请求后台删除
        this.loading = true
        window.setTimeout(() => {
          this.loading = false
          this.$message.success('删除成功')
          // 假设后台返回删除成功，必须要调用 confirmRemove() 方法，才会真正在表格里移除（会同时删除选中的逻辑新增的数据）
          event.confirmRemove()
        }, 1000)
      },

      // 单元格编辑完成之后触发的事件
      handleEditClosed(event) {
        let {$table, row, column} = event

        let field = column.property
        let cellValue = row[field]
        // 判断单元格值是否被修改
        if ($table.isUpdateByRow(row, field)) {
          // 校验当前行
          $table.validate(row).then((errMap) => {
            // 校验通过
            if (!errMap) {
              // 【模拟保存】
              let hideLoading = this.$message.loading(`正在保存"${column.title}"`, 0)
              console.log('即时保存数据：', row)
              putAction(this.url.saveRow, row).then(res => {
                if (res.success) {
                  this.$message.success(`"${column.title}"保存成功！`)
                  // 局部更新单元格为已保存状态
                  $table.reloadRow(row, null, field)
                } else {
                  this.$message.warn(`"${column.title}"保存失败：` + res.message)
                }
              }).finally(() => {
                hideLoading()
              })
            }
          })
        }
      },

      // 当分页参数变化时触发的事件
      handlePageChange(event) {
        // 重新赋值
        this.pagination.current = event.current
        this.pagination.pageSize = event.pageSize
        // 查询数据
        this.loadData()
      },

      // 当选择的行变化时触发的事件
      handleSelectRowChange(event) {
        this.selectedRows = event.selectedRows
      },

    },
  }
</script>

<style scoped>

</style>