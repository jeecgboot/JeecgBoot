<template>
  <div :style="{ padding: '0 0 32px 32px' }">
    <h4 :style="{ marginBottom: '20px' }">{{ title }}</h4>
    <v-chart
      height="254"
      :data="datasource"
      :forceFit="true"
      :padding="['auto', 'auto', '40', '50']">
      <v-tooltip />
      <v-axis />
      <v-bar position="x*y"/>
    </v-chart>
  </div>
</template>

<script>

  const data = []
  for (let i = 0; i < 12; i += 1) {
    data.push({
      x: `${i + 1}月`,
      y: Math.floor(Math.random() * 1000) + 200
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
    max: 22
  }]

  export default {
    name: "Bar",
    props: {
      title: {
        type: String,
        default: ''
      }
    },
    mounted(){
      this.datasource = data
    },
    data () {
      return {
        datasource:[],
        scale,
        tooltip
      }
    }
  }
</script>