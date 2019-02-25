<template>
  <div class="standard-table">
    <div class="alert">
      <a-alert type="info" :show-icon="true">
        <div slot="message">
          已选择&nbsp;<a style="font-weight: 600">{{ selectedRows.length }}</a>&nbsp;&nbsp;
          <template v-for="(item, index) in needTotalList" v-if="item.needTotal">
            {{ item.title }} 总计&nbsp;
            <a :key="index" style="font-weight: 600">
              {{ item.customRender ? item.customRender(item.total) : item.total }}
            </a>&nbsp;&nbsp;
          </template>
          <a style="margin-left: 24px" @click="onClearSelected">清空</a>
        </div>
      </a-alert>
    </div>
    <a-table
      :size="size"
      :bordered="bordered"
      :loading="loading"
      :columns="columns"
      :dataSource="current"
      :rowKey="rowKey"
      :pagination="pagination"
      :rowSelection="{ selectedRowKeys: selectedRowKeys, onChange: updateSelect }"
    >
    </a-table>
  </div>
</template>

<script>
  export default {
    name: "StandardTable",
    // props: ['bordered', 'loading', 'columns', 'data', 'rowKey', 'pagination', 'selectedRows'],
    props: {

      /**
       * 数据加载函数，返回值必须是 Promise
       * 默认情况下必须传递 data 参数；
       *    如果使用本地数据渲染表格，业务代码中将获取本地数据包装为 Promise 即可。
       *
       * currentData 用于向外暴露表格当前渲染的数据，
       * 业务开发中也可以直接修改 currentData，从而重新渲染表格（仅推荐用于客户端排序、数据过滤等场景）
       */
      data: {
        type: Function,
        required: true
      },
      dataSource: {
        type: Array,
        default () {
          return []
        }
      },
      columns: {
        type: Array,
        required: true
      },
/*      pagination: {
        type: Object,
        default () {
          return {}
        }
      },*/
      pageSize: {
        type: Number,
        default: 10
      },
      pageNum: {
        type: Number,
        default: 1
      },
      pageSizeOptions: {
        type: Array,
        default () {
          return ['10', '20', '30', '40', '50']
        }
      },
      responseParamsName: {
        type: Object,
        default () {
          return {}
        }
      },
      bordered: {
        type: Boolean,
        default: false
      },
      /**
       * 表格大小风格，default, middle, small
       */
      size: {
        type: String,
        default: 'default'
      },
      rowKey: {
        type: String,
        default: ''
      },
      selectedRows: {
        type: Array,
        default: null
      }
    },
    data () {
      return {
        needTotalList: [],
        selectedRowKeys: [],

        loading: true,

        total: 0,
        pageNumber: this.pageNum,
        currentPageSize: this.pageSize,
        defaultCurrent: 1,
        sortParams: {},

        current: [],
        pagination: {},
        paramsName: {},
      }
    },
    created () {
      //数据请求参数配置
      this.paramsName = Object.assign(
        {},
        {
          pageNumber: "pageNo",
          pageSize: "pageSize",
          total: "totalCount",
          results: "data",
          sortColumns: "sortColumns"
        },
        this.responseParamsName
      );

      this.needTotalList = this.initTotalList(this.columns)

      // load data
      this.loadData( { pageNum: this.pageNumber } )
    },
    methods: {
      updateSelect (selectedRowKeys, selectedRows) {
        this.selectedRowKeys = selectedRowKeys
        let list = this.needTotalList
        this.needTotalList = list.map(item => {
          return {
            ...item,
            total: selectedRows.reduce((sum, val) => {
              return sum + val[item.dataIndex]
            }, 0)
          }
        })
        this.$emit('change', selectedRowKeys, selectedRows)
      },
      initTotalList (columns) {
        const totalList = []
        columns.forEach(column => {
          if (column.needTotal) {
            totalList.push({ ...column, total: 0 })
          }
        })
        return totalList
      },

      loadData (params) {
        let that = this
        that.loading = true
        params = Object.assign({}, params)
        const remoteParams = Object.assign({}, that.sortParams)
        remoteParams[that.paramsName.pageNumber] = params.pageNum || that.pageNumber
        remoteParams[that.paramsName.pageSize] = params.pageSize || that.currentPageSize

        if (params.pageNum) {
          that.pageNumber = params.pageNum
        }
        if (params.pageSize) {
          that.currentPageSize = params.pageSize
        }

        let dataPromise = that.data(remoteParams)

        dataPromise.then( response => {
          if (!response) {
            that.loading = false
            return
          }
          let results = response[that.paramsName.results]
          results = (results instanceof Array && results) || []

          that.current = results

          that.$emit("update:currentData", that.current.slice())
          that.$emit("dataloaded", that.current.slice())

          that.total = response[that.paramsName.total] * 1
          that.pagination = that.pager()
          that.loading = false
        }, () => {
          // error callback
          that.loading = false
        })
      },
      // eslint-disable-next-line
      onPagerChange (page, pageSize) {
        this.pageNumber = page
        this.loadData({ pageNum: page })
      },
      onPagerSizeChange (current, size) {
        this.currentPageSize = size
        /*
        if (current === this.pageNumber) this.loadData()
        console.log('page-size-change', current, size)
        */
      },
      onClearSelected () {
        this.selectedRowKeys = []
        this.updateSelect([], [])
      },
      pager () {
        return {
          total: this.total,
          showTotal: total => `共有 ${total} 条`,
          showSizeChanger: true,
          pageSizeOptions: this.pageSizeOptions,
          pageSize: this.pageSize,
          defaultCurrent: this.defaultCurrent,
          onChange: this.onPagerChange,
          onShowSizeChange: this.onPagerSizeChange
        }
      }
    },
    watch: {
      'selectedRows': function (selectedRows) {
        this.needTotalList = this.needTotalList.map(item => {
          return {
            ...item,
            total: selectedRows.reduce( (sum, val) => {
              return sum + val[item.dataIndex]
            }, 0)
          }
        })
      }
    }
  }
</script>

<style scoped>
    .alert {
        margin-bottom: 16px;
    }
</style>