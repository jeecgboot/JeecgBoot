<template>
  <div :style="{ padding: '0 0 32px 32px' }">
    <h4 :style="{ marginBottom: '20px' }">{{ title }}</h4>
    <v-chart :forceFit="true" :height="height" :data="data" :padding="['auto', 'auto', '40', '50']">
      <v-tooltip />
      <v-axis />
      <v-legend />
      <v-bar position="x*y" color="type" :adjust="adjust" />
    </v-chart>
  </div>
</template>

<script>
  import { DataSet } from '@antv/data-set'

  const sourceDataConst = [
    { type: 'Jeecg', 'Jan.': 18.9, 'Feb.': 28.8, 'Mar.': 39.3, 'Apr.': 81.4, 'May': 47, 'Jun.': 20.3, 'Jul.': 24, 'Aug.': 35.6 },
    { type: 'Jeebt', 'Jan.': 12.4, 'Feb.': 23.2, 'Mar.': 34.5, 'Apr.': 99.7, 'May': 52.6, 'Jun.': 35.5, 'Jul.': 37.4, 'Aug.': 42.4 }
  ];
  const fieldsConst = ['Jan.', 'Feb.', 'Mar.', 'Apr.', 'May', 'Jun.', 'Jul.', 'Aug.'];
  export default {
    name: 'BarMultid',
    props: {
      title: {
        type: String,
        default: ''
      },
      dataSource:{
        type:Array,
        default:()=>[]
      },
      fields:{
        type:Array,
        default:()=>[]
      },
      height: {
        type: Number,
        default: 254
      }
    },
    data() {
      return {
        data:"",
        adjust: [{
          type: 'dodge',
          marginRatio: 1 / 32,
        }],
      };
    },
    watch: {
      'dataSource': function () {
        this.drawChart();
      }
    },
    mounted(){
      this.drawChart()
    },
    methods:{
      drawChart(){
        let temp = sourceDataConst;
        if(this.dataSource && this.dataSource.length>0){
          temp = this.dataSource
        }
        const dv = new DataSet.View().source(temp);
        dv.transform({
          type: 'fold',
          fields:(!this.fields||this.fields.length==0)?fieldsConst:this.fields,
          key: 'x',
          value: 'y',
        });
        this.data=dv.rows;
      }
    }
  }
</script>

<style scoped>

</style>