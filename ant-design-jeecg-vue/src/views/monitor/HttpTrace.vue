<template>
  <a-card :bordered="false" class="card-area">
    <div>
      <div class="alert">
        <a-alert type="success" :show-icon="true">
          <div slot="message">
            共追踪到 {{dataSource.length}} 条近期HTTP请求记录
            <a style="margin-left: 24px" @click="search">点击刷新</a>
          </div>
        </a-alert>
      </div>
      <!-- 表格区域 -->
      <a-table :columns="columns"
               :dataSource="dataSource"
               :pagination="pagination"
               :loading="loading"
               :scroll="{ x: 900 }"
               @change="handleTableChange">
      </a-table>
    </div>
  </a-card>
</template>

<script>
import moment from 'moment'
moment.locale('zh-cn')
import {getAction} from '@/api/manage'

export default {
  data () {
    return {
      advanced: false,
      dataSource: [],
      pagination: {
        defaultPageSize: 10,
        defaultCurrent: 1,
        pageSizeOptions: ['10', '20', '30', '40', '100'],
        showQuickJumper: true,
        showSizeChanger: true,
        showTotal: (total, range) => `显示 ${range[0]} ~ ${range[1]} 条记录，共 ${total} 条记录`
      },
      loading: false
    }
  },
  computed: {
    columns () {
      return [{
        title: '请求时间',
        dataIndex: 'timestamp',
        customRender: (text, row, index) => {
          return moment(text).format('YYYY-MM-DD HH:mm:ss')
        }
      }, {
        title: '请求方法',
        dataIndex: 'request.method',
        customRender: (text, row, index) => {
          switch (text) {
            case 'GET':
              return <a-tag color="#87d068">{text}</a-tag>
            case 'POST':
              return <a-tag color="#2db7f5">{text}</a-tag>
            case 'PUT':
              return <a-tag color="#ffba5a">{text}</a-tag>
            case 'DELETE':
              return <a-tag color="#f50">{text}</a-tag>
            default:
              return text
          }
        },
        filters: [
          { text: 'GET', value: 'GET' },
          { text: 'POST', value: 'POST' },
          { text: 'PUT', value: 'PUT' },
          { text: 'DELETE', value: 'DELETE' }
        ],
        filterMultiple: true,
        onFilter: (value, record) => record.request.method.includes(value)
      }, {
        title: '请求URL',
        dataIndex: 'request.uri',
        customRender: (text, row, index) => {
          return text.split('?')[0]
        }
      }, {
        title: '响应状态',
        dataIndex: 'response.status',
        customRender: (text, row, index) => {
          if (text < 200) {
            return <a-tag color="pink">{text}</a-tag>
          } else if (text < 201) {
            return <a-tag color="green">{text}</a-tag>
          } else if (text < 399) {
            return <a-tag color="cyan">{text}</a-tag>
          } else if (text < 403) {
            return <a-tag color="orange">{text}</a-tag>
          } else if (text < 501) {
            return <a-tag color="red">{text}</a-tag>
          } else {
            return text
          }
        }
      }, {
        title: '请求耗时',
        dataIndex: 'timeTaken',
        customRender: (text, row, index) => {
          if (text < 500) {
            return <a-tag color="green">{text} ms</a-tag>
          } else if (text < 1000) {
            return <a-tag color="cyan">{text} ms</a-tag>
          } else if (text < 1500) {
            return <a-tag color="orange">{text} ms</a-tag>
          } else {
            return <a-tag color="red">{text} ms</a-tag>
          }
        }
      }]
    }
  },
  mounted () {
    this.fetch()
  },
  methods: {
    search () {
      this.fetch()
    },
    handleTableChange (pagination, filters, sorter) {
      this.fetch()
    },
    fetch () {
      this.loading = true
      getAction('actuator/httptrace').then((data) => {
        this.loading = false
        let filterData = []
        for (let d of data.traces) {
          if (d.request.method !== 'OPTIONS' && d.request.uri.indexOf('httptrace') === -1) {
            filterData.push(d)
          }
        }
        this.dataSource = filterData
      })
    }
  }
}
</script>

<style lang="less" scoped>
  .alert {
    margin-bottom: .5rem;
  }
</style>
