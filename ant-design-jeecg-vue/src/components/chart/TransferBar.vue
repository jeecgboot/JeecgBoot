<template>
  <div :style="{ padding: '0 0 32px 32px' }">
    <h4 :style="{ marginBottom: '20px' }">{{ title }}</h4>
    <v-chart
      :height="height"
      :data="data"
      :scale="scale"
      :forceFit="true"
      :padding="['auto', 'auto', '40', '50']">
      <v-tooltip/>
      <v-axis/>
      <v-bar position="x*y"/>
    </v-chart>
  </div>
</template>

<script>

  export default {
    name: 'Bar',
    props: {
      title: {
        type: String,
        default: ''
      },
      x: {
        type: String,
        default: 'x'
      },
      y: {
        type: String,
        default: 'y'
      },
      data: {
        type: Array,
        default: () => []
      },
      height: {
        type: Number,
        default: 254
      }
    },
    data() {
      return {}
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
      // this.getMonthBar()
    },
    methods: {
      // getMonthBar() {
      //   this.$http.get('/analysis/month-bar')
      //     .then(res => {
      //       this.data = res.result
      //     })
      // }
    }
  }
</script>