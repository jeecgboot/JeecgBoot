<template>
  <a-skeleton active :loading="loading" :paragraph="{rows: 17}">
    <a-card>
      <!-- Radis 信息实时监控 -->
      <a-row :gutter="8">
        <a-col :sm="24" :xl="12">
          <area-chart-ty v-bind="memory"/>
        </a-col>
        <a-col :sm="24" :xl="12">
          <area-chart-ty v-bind="key"/>
        </a-col>
      </a-row>

      <h3>Redis 详细信息</h3>
      <a-table
        :loading="tableLoading"
        :columns="columns"
        :dataSource="redisInfo"
        :pagination="false"/>

    </a-card>
  </a-skeleton>
</template>
<script>
  import moment from 'moment'
  import { getAction } from '@/api/manage'
  import AreaChartTy from '@/components/chart/AreaChartTy'

  export default {
    name: 'RedisInfo',
    components: {
      AreaChartTy
    },
    data() {
      return {
        loading: true,
        tableLoading: true,
        timer: null,
        millisec: 3000,
        key: {
          title: 'Radis Key 实时数量（个）',
          dataSource: [],
          y: '数量（个）',
          height: 340,
          max: 100,
          color: '#FF6987',
          lineSize: 8,
          lineColor: '#DC143C'
        },
        memory: {
          title: 'Radis 内存实时占用情况（KB）',
          dataSource: [],
          y: '内存（KB）',
          max: 3000,
          height: 340,
          lineSize: 8
        },
        redisInfo: [],
        columns: [{
          title: 'Key',
          align: 'center',
          dataIndex: 'key'
        }, {
          title: 'Description',
          align: 'left',
          dataIndex: 'description'
        }, {
          title: 'Value',
          align: 'center',
          dataIndex: 'value'
        }],
        url: {
          keysSize: '/actuator/redis/keysSize',
          memoryInfo: '/actuator/redis/memoryInfo',
          info: '/actuator/redis/info'
        }
      }
    },
    watch: {
      // '$route'(to, from) {
      //   console.log(to, from)
      //   let path = '/monitor/redis/info'
      //   if (to.path === path) this.openTimer(), console.log('to')
      //   if (from.path === path) this.closeTimer(), console.log('from'),
      //     this.$destroy()
      // }
    },
    mounted() {
      this.openTimer()
      this.loadRedisInfo()
      setTimeout(() => {
        this.loadData()
      }, 1000)
    },
    beforeDestroy() {
      this.closeTimer()//, console.log('beforeDestroy')
    },
    methods: {

      /** 开启定时器 */
      openTimer() {
        this.loadData()
        this.closeTimer()
        this.timer = setInterval(() => {
          if (this.$route.path === '/monitor/redis/info') {
            this.loadData()
          }
        }, this.millisec)
      },

      /** 关闭定时器 */
      closeTimer() {
        if (this.timer) clearInterval(this.timer)
      },

      /** 查询数据 */
      loadData() {
        Promise.all([
          getAction(this.url.keysSize),
          getAction(this.url.memoryInfo)
        ]).then((res) => {
          let time = moment().format('hh:mm:ss')

          let [{ dbSize: currentSize }, memoryInfo] = res
          let currentMemory = memoryInfo.used_memory / 1000

          // push 数据
          this.key.dataSource.push({ x: time, y: currentSize })
          this.memory.dataSource.push({ x: time, y: currentMemory })
          // 最大长度为6
          if (this.key.dataSource.length > 6) {
            this.key.dataSource.splice(0, 1)
            this.memory.dataSource.splice(0, 1)
          }

          // 计算最大值阈值
          let maxKey = this.getMaxValue(this.key.dataSource, 'y')
          this.key.max = Math.floor(maxKey) + 10
          let maxMemory = this.getMaxValue(this.memory.dataSource, 'y')
          this.memory.max = Math.floor(maxMemory) + 100

        }).catch((e) => {
          console.error(e)
          this.closeTimer()
          this.$message.error('获取 Redis 信息失败')
        }).finally(() => {
          this.loading = false
        })

      },

      getMaxValue(dataSource, f) {
        let maxValue = null
        dataSource.forEach(item => {
          let value = Number.parseInt(item[f])
          if (maxValue == null) {
            maxValue = value
          } else if (value > maxValue) {
            maxValue = value
          }
        })
        return maxValue
      },

      loadRedisInfo() {
        this.tableLoading = true
        getAction(this.url.info).then((res) => {
          this.redisInfo = res.result
        }).finally(() => {
          this.tableLoading = false
        })
      }

    }
  }
</script>
<style></style>
