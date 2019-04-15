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
          <td>
            <a-tag color="purple">jvm.memory.max</a-tag>
          </td>
          <td>JVM 最大内存</td>
          <td>{{jvm.memory.max}} MB</td>
        </tr>
        <tr>
          <td>
            <a-tag color="purple">jvm.memory.committed</a-tag>
          </td>
          <td>JVM 可用内存</td>
          <td>{{jvm.memory.committed}} MB</td>
        </tr>
        <tr>
          <td>
            <a-tag color="purple">jvm.memory.used</a-tag>
          </td>
          <td>JVM 已用内存</td>
          <td>{{jvm.memory.used}} MB</td>
        </tr>
        <tr>
          <td>
            <a-tag color="cyan">jvm.buffer.memory.used</a-tag>
          </td>
          <td>JVM 缓冲区已用内存</td>
          <td>{{jvm.buffer.memory.used}} MB</td>
        </tr>
        <tr>
          <td>
            <a-tag color="cyan">jvm.buffer.count</a-tag>
          </td>
          <td>当前缓冲区数量</td>
          <td>{{jvm.buffer.count}} 个</td>
        </tr>
        <tr>
          <td>
            <a-tag color="green">jvm.threads.daemon</a-tag>
          </td>
          <td>JVM 守护线程数量</td>
          <td>{{jvm.threads.daemon}} 个</td>
        </tr>
        <tr>
          <td>
            <a-tag color="green">jvm.threads.live</a-tag>
          </td>
          <td>JVM 当前活跃线程数量</td>
          <td>{{jvm.threads.live}} 个</td>
        </tr>
        <tr>
          <td>
            <a-tag color="green">jvm.threads.peak</a-tag>
          </td>
          <td>JVM 峰值线程数量</td>
          <td>{{jvm.threads.peak}} 个</td>
        </tr>
        <tr>
          <td>
            <a-tag color="orange">jvm.classes.loaded</a-tag>
          </td>
          <td>JVM 已加载 Class 数量</td>
          <td>{{jvm.classes.loaded}} 个</td>
        </tr>
        <tr>
          <td>
            <a-tag color="orange">jvm.classes.unloaded</a-tag>
          </td>
          <td>JVM 未加载 Class 数量</td>
          <td>{{jvm.classes.unloaded}} 个</td>
        </tr>
        <tr>
          <td>
            <a-tag color="pink">jvm.gc.memory.allocated</a-tag>
          </td>
          <td>GC 时, 年轻代分配的内存空间</td>
          <td>{{jvm.gc.memory.allocated}} MB</td>
        </tr>
        <tr>
          <td>
            <a-tag color="pink">jvm.gc.memory.promoted</a-tag>
          </td>
          <td>GC 时, 老年代分配的内存空间</td>
          <td>{{jvm.gc.memory.promoted}} MB</td>
        </tr>
        <tr>
          <td>
            <a-tag color="pink">jvm.gc.max.data.size</a-tag>
          </td>
          <td>GC 时, 老年代的最大内存空间</td>
          <td>{{jvm.gc.maxDataSize}} MB</td>
        </tr>
        <tr>
          <td>
            <a-tag color="pink">jvm.gc.live.data.size</a-tag>
          </td>
          <td>FullGC 时, 老年代的内存空间</td>
          <td>{{jvm.gc.liveDataSize}} MB</td>
        </tr>
        <tr>
          <td>
            <a-tag color="blue">jvm.gc.pause.count</a-tag>
          </td>
          <td>系统启动以来GC 次数</td>
          <td>{{jvm.gc.pause.count}} 次</td>
        </tr>
        <tr>
          <td>
            <a-tag color="blue">jvm.gc.pause.totalTime</a-tag>
          </td>
          <td>系统启动以来GC 总耗时</td>
          <td>{{jvm.gc.pause.totalTime}} 秒</td>
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
    data() {
      return {
        time: '',
        loading: true,
        jvm: {
          memory: {
            max: 0,
            committed: 0,
            used: 0
          },
          buffer: {
            memory: {
              used: 0
            },
            count: 0
          },
          threads: {
            daemon: 0,
            live: 0,
            peak: 0
          },
          classes: {
            loaded: 0,
            unloaded: 0
          },
          gc: {
            memory: {
              allocated: 0,
              promoted: 0
            },
            maxDataSize: 0,
            liveDataSize: 0,
            pause: {
              totalTime: 0,
              count: 0
            }
          }
        }
      }
    },
    mounted() {
      this.create()
    },
    methods: {
      create() {
        this.time = moment().format('YYYY年MM月DD日 HH时mm分ss秒')
        axios.all([
          getAction('actuator/metrics/jvm.memory.max'),
          getAction('actuator/metrics/jvm.memory.committed'),
          getAction('actuator/metrics/jvm.memory.used'),
          getAction('actuator/metrics/jvm.buffer.memory.used'),
          getAction('actuator/metrics/jvm.buffer.count'),
          getAction('actuator/metrics/jvm.threads.daemon'),
          getAction('actuator/metrics/jvm.threads.live'),
          getAction('actuator/metrics/jvm.threads.peak'),
          getAction('actuator/metrics/jvm.classes.loaded'),
          getAction('actuator/metrics/jvm.classes.unloaded'),
          getAction('actuator/metrics/jvm.gc.memory.allocated'),
          getAction('actuator/metrics/jvm.gc.memory.promoted'),
          getAction('actuator/metrics/jvm.gc.max.data.size'),
          getAction('actuator/metrics/jvm.gc.live.data.size'),
          getAction('actuator/metrics/jvm.gc.pause')
        ]).then((r) => {
          this.jvm.memory.max = this.convert(r[0].measurements[0].value)
          this.jvm.memory.committed = this.convert(r[1].measurements[0].value)
          this.jvm.memory.used = this.convert(r[2].measurements[0].value)
          this.jvm.buffer.memory.used = this.convert(r[3].measurements[0].value)
          this.jvm.buffer.count = r[4].measurements[0].value
          this.jvm.threads.daemon = r[5].measurements[0].value
          this.jvm.threads.live = r[6].measurements[0].value
          this.jvm.threads.peak = r[7].measurements[0].value
          this.jvm.classes.loaded = r[8].measurements[0].value
          this.jvm.classes.unloaded = r[9].measurements[0].value
          this.jvm.gc.memory.allocated = this.convert(r[10].measurements[0].value)
          this.jvm.gc.memory.promoted = this.convert(r[11].measurements[0].value)
          this.jvm.gc.maxDataSize = this.convert(r[12].measurements[0].value)
          this.jvm.gc.liveDataSize = this.convert(r[13].measurements[0].value)
          this.jvm.gc.pause.count = r[14].measurements[0].value
          this.jvm.gc.pause.totalTime = r[14].measurements[1].value
          this.loading = false
        }).catch((r) => {
          console.error(r)
          this.$message.error('获取JVM信息失败')
        })
      },
      convert(value) {
        return Number(value / 1048576).toFixed(3)
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
