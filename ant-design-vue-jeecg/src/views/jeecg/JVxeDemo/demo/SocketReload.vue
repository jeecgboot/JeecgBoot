<template>
  <a-card title="无痕刷新示例" :bordered="false">

    <div style="margin-bottom: 8px;">
      <span>启用数据变动特效：</span>
      <a-switch v-model="reloadEffect"/>
    </div>

    <!--
      【即时保存大体思路】：
      1. 该功能依赖于【即时保存】功能，请先看即时保存示例
      2. 必须要有 socket-reload 属性，且设为 true
      3. 必须要有 socket-key 属性，该属性为当前表格的唯一标识，
         系统会自动更新所有 socket-key 相同的表格
      4. 在局部保存 edit-closed 事件中，
         保存成功后调用 socketSendUpdateRow 方法，将当前 row 传递过去即可 （见第 108 行）
    -->
    <j-vxe-table
      ref="table"
      row-number
      row-selection

      keep-source
      socket-reload
      socket-key="demo-socket-reload"
      :reload-effect="reloadEffect"

      :height="340"
      :loading="loading"
      :columns="columns"
      :dataSource="dataSource"
      @edit-closed="handleEditClosed"
    />
  </a-card>
</template>

<script>
  import { getAction } from '@api/manage'
  import { JVXETypes } from '@/components/jeecg/JVxeTable'

  // 无痕刷新示例
  export default {
    name: 'SocketReload',
    data() {
      return {
        loading: false,
        dataSource: [],
        columns: [
          {key: 'num', title: '序号', width: '80px'},
          {key: 'ship_name', title: '船名', width: '180px', type: JVXETypes.input},
          {key: 'call', title: '呼叫', width: '80px', type: JVXETypes.input},
          {key: 'len', title: '长', width: '80px', type: JVXETypes.input},
          {key: 'ton', title: '吨', width: '120px', type: JVXETypes.input},
          {key: 'payer', title: '付款方', width: '120px', type: JVXETypes.input},
          {key: 'count', title: '数', width: '40px'},
          {key: 'company', title: '公司', minWidth: '180px', type: JVXETypes.input},
          {key: 'trend', title: '动向', width: '120px', type: JVXETypes.input},
        ],
        // 查询url地址
        url: {
          getData: '/mock/vxe/getData',
        },
        // 是否启用日历刷新效果
        reloadEffect: false,
      }
    },
    created() {
      this.loadData()
    },
    methods: {
      // 加载数据
      loadData() {
        let formData = {pageNo: 1, pageSize: 200}
        this.loading = true
        getAction(this.url.getData, formData).then(res => {
          if (res.success) {
            this.dataSource = res.result.records
          } else {
            this.$error({title: '主表查询失败', content: res.message})
          }
        }).finally(() => {
          this.loading = false
        })
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
              // 【模拟保存】（此处需要替换成真实的请求）
              let hideLoading = this.$message.loading(`正在保存"${column.title}"`, 0)
              setTimeout(() => {
                hideLoading()
                this.$message.success(`"${column.title}"保存成功！`)
                // 局部更新单元格为已保存状态
                $table.reloadRow(row, null, field)
                // 发送更新消息
                this.$refs.table.socketSendUpdateRow(row)
              }, 555)
            }
          })
        }
      },

    },
  }
</script>

<style scoped>

</style>