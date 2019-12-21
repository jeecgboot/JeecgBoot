<template>
  <a-table
    :rowKey="rowKey"
    :columns="columns"
    :dataSource="dataSource"
    :expandedRowKeys="expandedRowKeys"
    v-bind="tableAttrs"
    v-on="$listeners"
    @expand="handleExpand"
    @expandedRowsChange="expandedRowKeys=$event">

    <template v-for="(slotItem) of slots" :slot="slotItem" slot-scope="text, record, index">
      <slot :name="slotItem" v-bind="{text,record,index}"></slot>
    </template>

  </a-table>
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
      // 根据什么查询，如果传递 id 就根据 id 查询
      queryKey: {
        type: String,
        default: 'parentId'
      },
      queryParams: {
        type: Object,
        default: () => ({})
      },
      // 查询顶级时的值，如果顶级为0，则传0
      topValue: {
        type: String,
        default: null
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
        default: () => ({})
      },
      /** 是否在创建组件的时候就查询数据 */
      immediateRequest: {
        type: Boolean,
        default: true
      },
      condition:{
        type:String,
        default:'',
        required:false
      }
    },
    data() {
      return {
        dataSource: [],
        expandedRowKeys: []
      }
    },
    computed: {
      getChildrenUrl() {
        if (this.childrenUrl) {
          return this.childrenUrl
        } else {
          return this.url
        }
      },
      slots() {
        let slots = []
        for (let column of this.columns) {
          if (column.scopedSlots && column.scopedSlots.customRender) {
            slots.push(column.scopedSlots.customRender)
          }
        }
        return slots
      },
      tableAttrs() {
        return Object.assign(this.$attrs, this.tableProps)
      }
    },
    watch: {
      queryParams: {
        deep: true,
        handler() {
          this.loadData()
        }
      }
    },
    created() {
      if (this.immediateRequest) this.loadData()
    },
    methods: {

      /** 加载数据*/
      loadData(id = this.topValue, first = true, url = this.url) {
        this.$emit('requestBefore', { first })

        if (first) {
          this.expandedRowKeys = []
        }

        let params = Object.assign({}, this.queryParams || {})
        params[this.queryKey] = id
        if(this.condition && this.condition.length>0){
          params['condition'] = this.condition
        }

        return getAction(url, params).then(res => {
          let list = []
          if (res.result instanceof Array) {
            list = res.result
          } else if (res.result.records instanceof Array) {
            list = res.result.records
          } else {
            throw '返回数据类型不识别'
          }
          let dataSource = list.map(item => {
            // 判断是否标记了带有子级
            if (item.hasChildren === true) {
              // 查找第一个带有dataIndex的值的列
              let firstColumn
              for (let column of this.columns) {
                firstColumn = column.dataIndex
                if (firstColumn) break
              }
              // 定义默认展开时显示的loading子级，实际子级数据只在展开时加载
              let loadChild = { id: `${item.id}_loadChild`, [firstColumn]: 'loading...', isLoading: true }
              item.children = [loadChild]
            }
            return item
          })
          if (first) {
            this.dataSource = dataSource
          }
          this.$emit('requestSuccess', { first, dataSource, res })
          return Promise.resolve(dataSource)
        }).finally(() => this.$emit('requestFinally', { first }))
      },

      /** 点击展开图标时触发 */
      handleExpand(expanded, record) {
        // 判断是否是展开状态
        if (expanded) {
          // 判断子级的首个项的标记是否是“正在加载中”，如果是就加载数据
          if (record.children[0].isLoading === true) {
            this.loadData(record.id, false, this.getChildrenUrl).then(dataSource => {
              // 处理好的数据可直接赋值给children
              if (dataSource.length === 0) {
                record.children = null
              } else {
                record.children = dataSource
              }
            })
          }
        }
      }

    }
  }
</script>

<style scoped>

</style>