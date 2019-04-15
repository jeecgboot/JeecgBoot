<template>
  <div style="width: 100%;margin-top: 1rem;background-color: #ffffff">
    <a-row :gutter="8">
      <a-col :span="12">
        <apexchart ref="memoryInfo" type=area height=350 :options="memory.chartOptions" :series="memory.series" />
      </a-col>
      <a-col :span="12">
        <apexchart ref="keySize" type=area height=350  :options="key.chartOptions" :series="key.series" />
      </a-col>
    </a-row>
    <a-row :gutter="8">
      <a-divider orientation="left">Redis详细信息</a-divider>
      <table style="border-bottom: 1px solid #f1f1f1;">
        <tr v-for="(info, index) in redisInfo" :key="index" style="border-top: 1px solid #f1f1f1;">
          <td style="padding: .7rem 1rem">{{info.key}}</td>
          <td style="padding: .7rem 1rem">{{info.description}}</td>
          <td style="padding: .7rem 1rem">{{info.value}}</td>
        </tr>
      </table>
    </a-row>
  </div>
</template>
<script>
import axios from 'axios'
import moment from 'moment'
import {getAction} from '@/api/manage'

export default {
  name: 'RedisInfo',
  data () {
    return {
      loading: true,
      memory: {
        series: [],
        chartOptions: {
          chart: {
            animations: {
              enabled: true,
              easing: 'linear',
              dynamicAnimation: {
                speed: 3000
              }
            },
            toolbar: {
              show: false
            },
            zoom: {
              enabled: false
            }
          },
          dataLabels: {
            enabled: false
          },
          stroke: {
            curve: 'smooth'
          },
          title: {
            text: 'Redis内存实时占用情况（kb）',
            align: 'left'
          },
          markers: {
            size: 0
          },
          xaxis: {
          },
          yaxis: {},
          legend: {
            show: false
          }
        },
        data: [],
        xdata: []
      },
      key: {
        series: [],
        chartOptions: {
          chart: {
            animations: {
              enabled: true,
              easing: 'linear',
              dynamicAnimation: {
                speed: 3000
              }
            },
            toolbar: {
              show: false
            },
            zoom: {
              enabled: false
            }
          },
          dataLabels: {
            enabled: false
          },
          colors: ['#f5564e'],
          stroke: {
            curve: 'smooth'
          },
          title: {
            text: 'Redis key实时数量（个）',
            align: 'left'
          },
          markers: {
            size: 0
          },
          xaxis: {
          },
          yaxis: {},
          legend: {
            show: false
          }
        },
        data: [],
        xdata: []
      },
      redisInfo: [],
      timer: null
    }
  },
  beforeDestroy () {
    if (this.timer) {
      clearInterval(this.timer)
    }
  },
  mounted () {
    let minMemory = 1e10
    let minSize = 1e10
    let maxMemory = -1e10
    let maxSize = -1e10
    this.timer = setInterval(() => {
      if (this.$route.path.indexOf('redis') !== -1) {
        axios.all([
          getAction('redis/keysSize'),
          getAction('redis/memoryInfo')
        ]).then((r) => {
          console.log(r)
          let currentMemory = r[1].used_memory / 1000
          let currentSize = r[0].dbSize
          if (currentMemory < minMemory) {
            minMemory = currentMemory
          }
          if (currentMemory > maxMemory) {
            maxMemory = currentMemory
          }
          if (currentSize < minSize) {
            minSize = currentSize
          }
          if (currentSize > maxSize) {
            maxSize = currentSize
          }
          let time = moment().format('hh:mm:ss')
          this.memory.data.push(currentMemory)
          this.memory.xdata.push(time)
          this.key.data.push(currentSize)
          this.key.xdata.push(time)
          if (this.memory.data.length >= 6) {
            this.memory.data.shift()
            this.memory.xdata.shift()
          }
          if (this.key.data.length >= 6) {
            this.key.data.shift()
            this.key.xdata.shift()
          }
          this.$refs.memoryInfo.updateSeries([
            {
              name: '内存(kb)',
              data: this.memory.data.slice()
            }
          ])
          this.$refs.memoryInfo.updateOptions({
            xaxis: {
              categories: this.memory.xdata.slice()
            },
            yaxis: {
              min: minMemory,
              max: maxMemory
            }
          }, true, true)
          this.$refs.keySize.updateSeries([
            {
              name: 'key数量',
              data: this.key.data.slice()
            }
          ])
          this.$refs.keySize.updateOptions({
            xaxis: {
              categories: this.key.xdata.slice()
            },
            yaxis: {
              min: minSize - 2,
              max: maxSize + 2
            }
          }, true, true)
          if (this.loading) {
            this.loading = false
          }
        }).catch((r) => {
          console.error(r)
          this.$message.error('获取Redis信息失败')
          if (this.timer) {
            clearInterval(this.timer)
          }
        })
      }
    }, 3000)
    getAction('redis/info').then((r) => {
      console.log('redis/info')
      console.log(r)
      this.redisInfo = r.result
    })
  }
}
</script>
<style>

</style>
