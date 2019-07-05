<template>
  <a-card :bordered="false">
    <j-tree-table
      :url="url"
      topValue="0"
      queryKey="id"
      :columns="columns"
      :tableProps="tableProps">

      <template v-slot:action="props">
        <!-- 可使用的参数： -->
        <!-- props.text -->
        <!-- props.record -->
        <!-- props.index -->
        <a @click="()=>handleEdit(props.record)">编辑</a>
      </template>

    </j-tree-table>
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
          },
          {
            title: '操作',
            dataIndex: 'action',
            scopedSlots: { customRender: 'action' }
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
    },
    methods: {
      handleEdit(record) {
        this.$info({
          width: 600,
          title: '编辑',
          content: '编辑ID：' + record.id+"；名称："+record.name,
          okText: '确定',
          maskClosable: true
        })
      }
    }
  }
</script>

<style scoped></style>
