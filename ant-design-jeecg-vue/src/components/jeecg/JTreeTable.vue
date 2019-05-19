<template>
  <a-table
    :rowKey="rowKey"
    :columns="columns"
    :dataSource="dataSource"
    v-bind="tableProps"
    @expand="handleExpand"/>
</template>

<script>
  import { getAction } from '@/api/manage'

  export default {
    name: 'JTreeTable',
    props: {
      rowKey: {
        type: String,
        default: 'id'
      },
      columns: {
        type: Array,
        required: true
      },
      url: {
        type: String,
        required: true
      },
      childrenUrl: {
        type: String,
        default: null
      },
      tableProps: {
        type: Object,
        default: () => {
        }
      }
    },
    data() {
      return {
        dataSource: []
      }
    },
    computed: {
      getChildrenUrl() {
        if (this.childrenUrl) {
          return this.childrenUrl
        } else {
          return this.url
        }
      }
    },
    created() {
      this.loadData()
    },
    methods: {

      /** 加载数据*/
      loadData(id = '0', first = true, url = this.url) {
        return getAction(url, { id }).then(res => {
          let dataSource = res.result.map(item => {
            // 判断是否标记了带有子级
            if (item.hasChildren === true) {
              // 定义默认展开时显示的loading子级，实际子级数据只在展开时加载
              let loadChild = { id: `${item.id}_loadChild`, name: 'loading...', isLoading: true }
              item.children = [loadChild]
            }
            return item
          })
          if (first) {
            this.dataSource = dataSource
          }
          return Promise.resolve(dataSource)
        })
      },

      /** 点击展开图标时触发 */
      handleExpand(expanded, record) {
        // 判断是否是展开状态
        if (expanded) {
          // 判断子级的首个项的标记是否是“正在加载中”，如果是就加载数据
          if (record.children[0].isLoading === true) {
            this.loadData(record.id, false, this.getChildrenUrl).then(dataSource => {
              // 处理好的数据可直接赋值给children
              record.children = dataSource
            })
          }
        }
      }

    }
  }
</script>

<style scoped>

</style>