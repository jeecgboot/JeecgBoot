<template>
  <a-skeleton active :loading="loading" :paragraph="{rows: 17}">
    <div class="jvm-info" style="background-color: #ffffff">
      <div class="alert">
        <a-alert type="success" :show-icon="true">
          <div slot="message">
            数据获取时间 {{this.time}}
            <a style="margin-left: 24px" @click="create">点击刷新</a>
          </div>
        </a-alert>
      </div>
      <table>
        <tr>
          <th>参数</th>
          <th>描述</th>
          <th>当前值</th>
        </tr>
        <tr>
          <td><a-tag color="green">tomcat.sessions.created</a-tag></td>
          <td>tomcat 已创建 session 数</td>
          <td>{{tomcat.sessions.created}} 个</td>
        </tr>
        <tr>
          <td><a-tag color="green">tomcat.sessions.expired</a-tag></td>
          <td>tomcat 已过期 session 数</td>
          <td>{{tomcat.sessions.expired}} 个</td>
        </tr>
        <tr>
          <td><a-tag color="green">tomcat.sessions.active.current</a-tag></td>
          <td>tomcat 当前活跃 session 数</td>
          <td>{{tomcat.sessions.active.current}} 个</td>
        </tr>
        <tr>
          <td><a-tag color="green">tomcat.sessions.active.max</a-tag></td>
          <td>tomcat 活跃 session 数峰值</td>
          <td>{{tomcat.sessions.active.max}} 个</td>
        </tr>
        <tr>
          <td><a-tag color="green">tomcat.sessions.rejected</a-tag></td>
          <td>超过session 最大配置后，拒绝的 session 个数</td>
          <td>{{tomcat.sessions.rejected}} 个</td>
        </tr>
        <tr>
          <td><a-tag color="purple">tomcat.global.sent</a-tag></td>
          <td>发送的字节数</td>
          <td>{{tomcat.global.sent}} bytes</td>
        </tr>
        <tr>
          <td><a-tag color="purple">tomcat.global.request.max</a-tag></td>
          <td>request 请求最长耗时</td>
          <td>{{tomcat.global.request.max}} 秒</td>
        </tr>
        <tr>
          <td><a-tag color="purple">tomcat.global.request.count</a-tag></td>
          <td>全局 request 请求次数</td>
          <td>{{tomcat.global.request.count}} 次</td>
        </tr>
        <tr>
          <td><a-tag color="purple">tomcat.global.request.totalTime</a-tag></td>
          <td>全局 request 请求总耗时</td>
          <td>{{tomcat.global.request.totalTime}} 秒</td>
        </tr>
        <tr>
          <td><a-tag color="cyan">tomcat.servlet.request.max</a-tag></td>
          <td>servlet 请求最长耗时</td>
          <td>{{tomcat.servlet.request.max}} 秒</td>
        </tr>
        <tr>
          <td><a-tag color="cyan">tomcat.servlet.request.count</a-tag></td>
          <td>servlet 总请求次数</td>
          <td>{{tomcat.servlet.request.count}} 次</td>
        </tr>
        <tr>
          <td><a-tag color="cyan">tomcat.servlet.request.totalTime</a-tag></td>
          <td>servlet 请求总耗时</td>
          <td>{{tomcat.servlet.request.totalTime}} 秒</td>
        </tr>
        <tr>
          <td><a-tag color="pink">tomcat.threads.current</a-tag></td>
          <td>tomcat 当前线程数（包括守护线程）</td>
          <td>{{tomcat.threads.current}} 个</td>
        </tr>
        <tr>
          <td><a-tag color="pink">tomcat.threads.configMax</a-tag></td>
          <td>tomcat 配置的线程最大数</td>
          <td>{{tomcat.threads.configMax}} 个</td>
        </tr>
      </table>
    </div>
  </a-skeleton>
</template>
<script>
import axios from 'axios'
import moment from 'moment'
import {getAction} from '@/api/manage'
moment.locale('zh-cn')

export default {
  data () {
    return {
      time: '',
      loading: true,
      tomcat: {
        sessions: {
          created: 0,
          expired: 0,
          active: {
            current: 0,
            max: 0
          },
          rejected: 0
        },
        global: {
          sent: 0,
          request: {
            count: 0,
            max: 0,
            totalTime: 0
          }
        },
        servlet: {
          request: {
            count: 0,
            totalTime: 0,
            max: 0
          }
        },
        threads: {
          current: 0,
          configMax: 0
        }
      }
    }
  },
  mounted () {
    this.create()
  },
  methods: {
    create () {
      this.time = moment().format('YYYY年MM月DD日 HH时mm分ss秒')
      axios.all([
        getAction('actuator/metrics/tomcat.sessions.created'),
        getAction('actuator/metrics/tomcat.sessions.expired'),
        getAction('actuator/metrics/tomcat.sessions.active.current'),
        getAction('actuator/metrics/tomcat.sessions.active.max'),
        getAction('actuator/metrics/tomcat.sessions.rejected'),
        getAction('actuator/metrics/tomcat.global.sent'),
        getAction('actuator/metrics/tomcat.global.request.max'),
        getAction('actuator/metrics/tomcat.global.request'),
        getAction('actuator/metrics/tomcat.servlet.request'),
        getAction('actuator/metrics/tomcat.servlet.request.max'),
        getAction('actuator/metrics/tomcat.threads.current'),
        getAction('actuator/metrics/tomcat.threads.config.max')
      ]).then((r) => {
        this.tomcat.sessions.created = r[0].measurements[0].value
        this.tomcat.sessions.expired = r[1].measurements[0].value
        this.tomcat.sessions.active.current = r[2].measurements[0].value
        this.tomcat.sessions.active.max = r[3].measurements[0].value
        this.tomcat.sessions.rejected = r[4].measurements[0].value
        this.tomcat.global.sent = r[5].measurements[0].value
        this.tomcat.global.request.max = r[6].measurements[0].value
        this.tomcat.global.request.count = r[7].measurements[0].value
        this.tomcat.global.request.totalTime = r[7].measurements[1].value
        this.tomcat.servlet.request.count = r[8].measurements[0].value
        this.tomcat.servlet.request.totalTime = r[8].measurements[1].value
        this.tomcat.servlet.request.max = r[9].measurements[0].value
        this.tomcat.threads.current = r[10].measurements[0].value
        this.tomcat.threads.configMax = r[11].measurements[0].value
        this.loading = false
      }).catch((r) => {
        console.error(r)
        this.$message.error('获取Tomcat信息失败')
      })
    }
  }
}
</script>
<style lang="less">
  .jvm-info {
    width: 100%;
    table {
      width: 100%;
      tr {
        line-height: 1.5rem;
        border-bottom: 1px solid #f1f1f1;
        th {
          background: #fafafa;
          padding: .5rem;
        }
        td {
          padding: .5rem;
          .ant-tag {
            font-size: .8rem !important;
          }
        }
      }
    }
    .alert {
      margin-bottom: .5rem;
    }
  }
</style>
