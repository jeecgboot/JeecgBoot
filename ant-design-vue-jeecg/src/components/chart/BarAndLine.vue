<template>
  <div :style="{ padding: '0 50px 32px 0' }">
    <h4 :style="{ marginBottom: '20px' }">{{ title }}</h4>
    <v-chart :forceFit="true" :height="height" :data="data" :scale="scale" :padding=" padding">
      <v-tooltip/>
      <v-legend/>
      <v-axis/>
      <v-bar position="type*bar"/>
      <v-line position="type*line" color="#2fc25b" :size="3"/>
    </v-chart>
  </div>
</template>

<script>

  export default {
    name: 'BarMultid',
    props: {
      title: {
        type: String,
        default: ''
      },
      dataSource: {
        type: Array,
        default: () => [
          { type: '10:10', bar: 200, line: 1000 },
          { type: '10:15', bar: 600, line: 1000},
          { type: '10:20', bar: 200, line: 1000},
          { type: '10:25', bar: 900, line: 1000},
          { type: '10:30', bar: 200, line: 1000},
          { type: '10:35', bar: 200, line: 1000},
          { type: '10:40', bar: 100, line: 1000}
        ]
      },
      height: {
        type: Number,
        default: 400
      }
    },
    data() {
      return {
        padding: { top:50, right:50, bottom:100, left:50 },
        scale: [{
          dataKey: 'bar',
          min: 0
        }, {
          dataKey: 'line',
          min: 0
        }]
      }
    },
    computed: {
      data() {
        return this.dataSource
      }
    }
  }
</script>