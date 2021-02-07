<template>
  <div class="antv-chart-mini">
    <div class="chart-wrapper" :style="{ height: 46 }">
      <v-chart :force-fit="true" :height="height" :data="data" :scale="scale" :padding="[36, 0, 18, 0]">
        <v-tooltip/>
        <v-smooth-area position="x*y"/>
      </v-chart>
    </div>
  </div>
</template>

<script>
  import moment from 'dayjs'

  const sourceData = []
  const beginDay = new Date().getTime()

  for (let i = 0; i < 10; i++) {
    sourceData.push({
      x: moment(new Date(beginDay + 1000 * 60 * 60 * 24 * i)).format('YYYY-MM-DD'),
      y: Math.round(Math.random() * 10)
    })
  }

  export default {
    name: 'MiniArea',
    props: {
      dataSource: {
        type: Array,
        default: () => []
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
      }
    },
    data() {
      return {
        data: [],
        height: 100
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
    created() {
      if (this.dataSource.length === 0) {
        this.data = sourceData
      } else {
        this.data = this.dataSource
      }
    }
  }
</script>

<style lang="less" scoped>
  @import "chart";
</style>