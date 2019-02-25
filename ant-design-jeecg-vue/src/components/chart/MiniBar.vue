<template>
  <div class="antv-chart-mini">
    <div class="chart-wrapper" :style="{ height: 46 }">
      <v-chart :force-fit="true" :height="height" :data="datasource" :padding="[36, 5, 18, 5]">
        <v-tooltip />
        <v-bar position="x*y" />
      </v-chart>
    </div>
  </div>
</template>

<script>
  import moment from 'dayjs'
  const data = []
  const beginDay = new Date().getTime()

  for (let i = 0; i < 10; i++) {
    data.push({
      x: moment(new Date(beginDay + 1000 * 60 * 60 * 24 * i)).format('YYYY-MM-DD'),
      y: Math.round(Math.random() * 10)
    })
  }

  const tooltip = [
    'x*y',
    (x, y) => ({
      name: x,
      value: y
    })
  ]

  const scale = [{
    dataKey: 'x',
    min: 2
  }, {
    dataKey: 'y',
    title: '时间',
    min: 1,
    max: 30
  }]

  export default {
    name: "MiniBar",
    props:{
      datasource:{
        type: Array,
        default:()=>[]
      }
    },
    created(){
      if(this.datasource.length==0){
        this.datasource = data;
      }
    },
    data () {
      return {
        data,
        tooltip,
        scale,
        height: 100
      }
    }
  }
</script>

<style lang="scss" scoped>
  @import "chart";
</style>