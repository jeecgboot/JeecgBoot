<template>
  <div :style="{ padding: '0 0 32px 32px' }">
    <h4 :style="{ marginBottom: '20px' }">{{ title }}</h4>

    <v-chart ref="chart" :forceFit="true" :height="height" :data="dataSource" :scale="scale">
      <v-tooltip/>
      <v-axis/>
      <v-line position="x*y" :size="lineSize"/>
      <v-area position="x*y"/>
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
      // 图表高度
      height: {
        type: Number,
        default: 254
      },
      // 线的粗细
      lineSize: {
        type: Number,
        default: 2
      }
    },
    computed: {
      scale() {
        return [
          { dataKey: 'x', title: this.x, alias: this.x },
          { dataKey: 'y', title: this.y, alias: this.y }
        ]
      }
    },
    mounted() {
      triggerWindowResizeEvent()
    }
  }
</script>

<style lang="scss" scoped>
  @import "chart";
</style>