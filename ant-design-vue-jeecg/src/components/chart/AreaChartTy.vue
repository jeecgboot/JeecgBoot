<template>
  <div :style="{ padding: '0' }">
    <h4 :style="{ marginBottom: '20px' }">{{ title }}</h4>

    <v-chart ref="chart" :forceFit="true" :height="height" :data="dataSource" :scale="scale">
      <v-tooltip :shared="false"/>
      <v-axis/>
      <v-line position="x*y" :size="lineSize" :color="lineColor"/>
      <v-area position="x*y" :color="color"/>
    </v-chart>

  </div>
</template>

<script>
  import { triggerWindowResizeEvent } from '@/utils/util'

  export default {
    name: 'AreaChartTy',
    props: {
      // 图表数据
      dataSource: {
        type: Array,
        required: true
      },
      // 图表标题
      title: {
        type: String,
        default: ''
      },
      // x 轴别名
      x: {
        type: String,
        default: 'x'
      },
      // y 轴别名
      y: {
        type: String,
        default: 'y'
      },
      // Y轴最小值
      min: {
        type: Number,
        default: 0
      },
      // Y轴最大值
      max: {
        type: Number,
        default: null
      },
      // 图表高度
      height: {
        type: Number,
        default: 254
      },
      // 线的粗细
      lineSize: {
        type: Number,
        default: 2
      },
      // 面积的颜色
      color: {
        type: String,
        default: ''
      },
      // 线的颜色
      lineColor: {
        type: String,
        default: ''
      }
    },
    computed: {
      scale() {
        return [
          { dataKey: 'x', title: this.x, alias: this.x },
          { dataKey: 'y', title: this.y, alias: this.y, min: this.min, max: this.max }
        ]
      }
    },
    mounted() {
      triggerWindowResizeEvent()
    }
  }
</script>

<style lang="less" scoped>
  @import "chart";
</style>