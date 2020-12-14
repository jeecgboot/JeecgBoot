<template>
  <a-skeleton active :loading="loading" :paragraph="{rows: 17}">
    <a-card :bordered="false">

      <a-alert type="info" :showIcon="true">
        <div slot="message">
          上次更新时间：{{ this.time }}
          <a-divider type="vertical"/>
          <a @click="handleClickUpdate">立即更新</a>
        </div>
      </a-alert>

      <a-table
        rowKey="id"
        size="middle"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="false"
        :loading="tableLoading"
        style="margin-top: 20px;">

        <template slot="param" slot-scope="text, record">
          <a-tag :color="textInfo[record.param].color">{{ text }}</a-tag>
        </template>

        <template slot="text" slot-scope="text, record">
          {{ textInfo[record.param].text }}
        </template>

        <template slot="value" slot-scope="text, record">
          {{ text }} {{ textInfo[record.param].unit }}
        </template>

      </a-table>

    </a-card>
  </a-skeleton>
</template>
<script>
  import moment from 'moment'
  import { getAction } from '@/api/manage'

  moment.locale('zh-cn')

  export default {
    data() {
      return {
        time: '',
        loading: true,
        tableLoading: true,
        columns: [{
          title: '参数',
          width: '30%',
          dataIndex: 'param',
          scopedSlots: { customRender: 'param' }
        }, {
          title: '描述',
          width: '40%',
          dataIndex: 'text',
          scopedSlots: { customRender: 'text' }
        }, {
          title: '当前值',
          width: '30%',
          dataIndex: 'value',
          scopedSlots: { customRender: 'value' }
        }],
        dataSource: [],
        // 列表通过 textInfo 渲染出颜色、描述和单位
        textInfo: {
          'tomcat.sessions.created': { color: 'green', text: 'tomcat 已创建 session 数', unit: '个' },
          'tomcat.sessions.expired': { color: 'green', text: 'tomcat 已过期 session 数', unit: '个' },
          'tomcat.sessions.active.current': { color: 'green', text: 'tomcat 当前活跃 session 数', unit: '个' },
          'tomcat.sessions.active.max': { color: 'green', text: 'tomcat 活跃 session 数峰值', unit: '个' },
          'tomcat.sessions.rejected': { color: 'green', text: '超过session 最大配置后，拒绝的 session 个数', unit: '个' },

          'tomcat.global.sent': { color: 'purple', text: '发送的字节数', unit: 'bytes' },
          'tomcat.global.request.max': { color: 'purple', text: 'request 请求最长耗时', unit: '秒' },
          'tomcat.global.request.count': { color: 'purple', text: '全局 request 请求次数', unit: '次' },
          'tomcat.global.request.totalTime': { color: 'purple', text: '全局 request 请求总耗时', unit: '秒' },

          'tomcat.servlet.request.max': { color: 'cyan', text: 'servlet 请求最长耗时', unit: '秒' },
          'tomcat.servlet.request.count': { color: 'cyan', text: 'servlet 总请求次数', unit: '次' },
          'tomcat.servlet.request.totalTime': { color: 'cyan', text: 'servlet 请求总耗时', unit: '秒' },

          'tomcat.threads.current': { color: 'pink', text: 'tomcat 当前线程数（包括守护线程）', unit: '个' },
          'tomcat.threads.config.max': { color: 'pink', text: 'tomcat 配置的线程最大数', unit: '个' }
        },
        // 当一条记录中需要取出多条数据的时候需要配置该字段
        moreInfo: {
          'tomcat.global.request': ['.count', '.totalTime'],
          'tomcat.servlet.request': ['.count', '.totalTime']
        }
      }
    },
    mounted() {
      this.loadTomcatInfo()
    },
    methods: {

      handleClickUpdate() {
        this.loadTomcatInfo()
      },

      loadTomcatInfo() {
        this.tableLoading = true
        this.time = moment().format('YYYY年MM月DD日 HH时mm分ss秒')
        Promise.all([
          getAction('actuator/metrics/tomcat.sessions.created'),
          getAction('actuator/metrics/tomcat.sessions.expired'),
          getAction('actuator/metrics/tomcat.sessions.active.current'),
          getAction('actuator/metrics/tomcat.sessions.active.max'),
          getAction('actuator/metrics/tomcat.sessions.rejected'),
          // 2.3.5.RELEASE 无此API
          // getAction('actuator/metrics/tomcat.global.sent'),
          // getAction('actuator/metrics/tomcat.global.request.max'),
          // getAction('actuator/metrics/tomcat.global.request'),
          // getAction('actuator/metrics/tomcat.threads.current'),
          // getAction('actuator/metrics/tomcat.threads.config.max')
          // 2.1.3.RELEASE 无此API
          //getAction('actuator/metrics/tomcat.servlet.request'),
          // getAction('actuator/metrics/tomcat.servlet.request.max'),
        ]).then((res) => {
          let tomcatInfo = []
          res.forEach((value, id) => {
            let more = this.moreInfo[value.name]
            if (!(more instanceof Array)) {
              more = ['']
            }
            more.forEach((item, idx) => {
              let param = value.name + item
              tomcatInfo.push({
                id: param + id, param,
                text: 'false value',
                value: value.measurements[idx].value
              })
            })
          })
          this.dataSource = tomcatInfo
        }).catch((e) => {
          console.error(e)
          this.$message.error('获取Tomcat信息失败')
        }).finally(() => {
          this.loading = false
          this.tableLoading = false
        })
      }
    }
  }
</script>
<style></style>
