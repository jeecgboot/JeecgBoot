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
          'jvm.memory.max': { color: 'purple', text: 'JVM 最大内存', unit: 'MB' },
          'jvm.memory.committed': { color: 'purple', text: 'JVM 可用内存', unit: 'MB' },
          'jvm.memory.used': { color: 'purple', text: 'JVM 已用内存', unit: 'MB' },
          'jvm.buffer.memory.used': { color: 'cyan', text: 'JVM 缓冲区已用内存', unit: 'MB' },
          'jvm.buffer.count': { color: 'cyan', text: '当前缓冲区数量', unit: '个' },
          'jvm.threads.daemon': { color: 'green', text: 'JVM 守护线程数量', unit: '个' },
          'jvm.threads.live': { color: 'green', text: 'JVM 当前活跃线程数量', unit: '个' },
          'jvm.threads.peak': { color: 'green', text: 'JVM 峰值线程数量', unit: '个' },
          'jvm.classes.loaded': { color: 'orange', text: 'JVM 已加载 Class 数量', unit: '个' },
          'jvm.classes.unloaded': { color: 'orange', text: 'JVM 未加载 Class 数量', unit: '个' },
          'jvm.gc.memory.allocated': { color: 'pink', text: 'GC 时, 年轻代分配的内存空间', unit: 'MB' },
          'jvm.gc.memory.promoted': { color: 'pink', text: 'GC 时, 老年代分配的内存空间', unit: 'MB' },
          'jvm.gc.max.data.size': { color: 'pink', text: 'GC 时, 老年代的最大内存空间', unit: 'MB' },
          'jvm.gc.live.data.size': { color: 'pink', text: 'FullGC 时, 老年代的内存空间', unit: 'MB' },
          'jvm.gc.pause.count': { color: 'blue', text: '系统启动以来GC 次数', unit: '次' },
          'jvm.gc.pause.totalTime': { color: 'blue', text: '系统启动以来GC 总耗时', unit: '秒' }
        },
        // 当一条记录中需要取出多条数据的时候需要配置该字段
        moreInfo: {
          'jvm.gc.pause': ['.count', '.totalTime']
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
        ]).then((res) => {

          let info = []
          res.forEach((value, id) => {
            let more = this.moreInfo[value.name]
            if (!(more instanceof Array)) {
              more = ['']
            }
            more.forEach((item, idx) => {
              let param = value.name + item
              let val = value.measurements[idx].value

              if (param === 'jvm.memory.max'
                || param === 'jvm.memory.committed'
                || param === 'jvm.memory.used'
                || param === 'jvm.buffer.memory.used'
                || param === 'jvm.gc.memory.allocated'
                || param === 'jvm.gc.memory.promoted'
                || param === 'jvm.gc.max.data.size'
                || param === 'jvm.gc.live.data.size'
              ) {
                val = this.convert(val, Number)
              }
              info.push({ id: param + id, param, text: 'false value', value: val })
            })
          })
          this.dataSource = info


        }).catch((e) => {
          console.error(e)
          this.$message.error('获取JVM信息失败')
        }).finally(() => {
          this.loading = false
          this.tableLoading = false
        })
      },

      convert(value, type) {
        if (type === Number) {
          return Number(value / 1048576).toFixed(3)
        } else if (type === Date) {
          return moment(value * 1000).format('YYYY-MM-DD HH:mm:ss')
        }
        return value
      }
    }
  }
</script>
<style></style>
