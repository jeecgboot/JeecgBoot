<template>
  <a-card :bordered="false">
    <j-tree-table :url="url" :columns="columns" :tableProps="tableProps"/>
  </a-card>
</template>

<script>
  import JTreeTable from '@/components/jeecg/JTreeTable'

  export default {
    name: 'JeecgTreeTable',
    components: { JTreeTable },
    data() {
      return {
        url: '/api/asynTreeList',
        columns: [
          {
            title: '菜单名称',
            dataIndex: 'name'
          },
          {
            title: '组件',
            dataIndex: 'component'
          },
          {
            title: '排序',
            dataIndex: 'orderNum'
          }
        ],
        selectedRowKeys: []
      }
    },
    computed: {
      tableProps() {
        let _this = this
        return {
          // 列表项是否可选择
          // https://vue.ant.design/components/table-cn/#rowSelection
          rowSelection: {
            selectedRowKeys: _this.selectedRowKeys,
            onChange: (selectedRowKeys) => _this.selectedRowKeys = selectedRowKeys
          }
        }
      }
    }
  }
</script>

<style scoped></style>
