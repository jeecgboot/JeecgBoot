<template>
  <div :style="{ padding: '0 0 32px 32px' }">
    <h4 :style="{ marginBottom: '20px' }">{{ title }}</h4>
    <v-chart :forceFit="true" :height="height" :data="data" :scale="scale">
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
          { type: '10:10', bar: 2, line: 2 },
          { type: '10:15', bar: 6, line: 3 },
          { type: '10:20', bar: 2, line: 5 },
          { type: '10:25', bar: 9, line: 1 },
          { type: '10:30', bar: 2, line: 3 },
          { type: '10:35', bar: 2, line: 1 },
          { type: '10:40', bar: 1, line: 2 }
        ]
      },
      height: {
        type: Number,
        default: 400
      }
    },
    data() {
      return {
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