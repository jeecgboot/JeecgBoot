<template>
  <div :style="{ padding: '0 0 32px 32px' }">
    <h4 :style="{ marginBottom: '20px' }">{{ title }}</h4>
    <v-chart :force-fit="true" :height="height" :data="data" :scale="scale" :padding="['auto', 'auto', '40', '50']">
      <v-tooltip />
      <v-axis />
      <v-legend />
      <v-line position="type*y" color="x" />
      <v-point position="type*y" color="x" :size="4" :v-style="style" :shape="'circle'" />
    </v-chart>
  </div>
</template>

<script>
  import { DataSet } from '@antv/data-set'

  const sourceDataConst = [
    { type: 'Jan', jeecg: 7.0, jeebt: 3.9 },
    { type: 'Feb', jeecg: 6.9, jeebt: 4.2 },
    { type: 'Mar', jeecg: 9.5, jeebt: 5.7 },
    { type: 'Apr', jeecg: 14.5, jeebt: 8.5 },
    { type: 'May', jeecg: 18.4, jeebt: 11.9 },
    { type: 'Jun', jeecg: 21.5, jeebt: 15.2 },
    { type: 'Jul', jeecg: 25.2, jeebt: 17.0 },
    { type: 'Aug', jeecg: 26.5, jeebt: 16.6 },
    { type: 'Sep', jeecg: 23.3, jeebt: 14.2 },
    { type: 'Oct', jeecg: 18.3, jeebt: 10.3 },
    { type: 'Nov', jeecg: 13.9, jeebt: 6.6 },
    { type: 'Dec', jeecg: 9.6, jeebt: 4.8 }
  ];



  export default {
    name: 'LineChartMultid',
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
        default: () => ['jeecg', 'jeebt']
      },
      height:{
        type:Number,
        default:254
      }
    },
    data() {
      return {
        data:"",
        scale: [{
          dataKey: 'x',
          min: 0,
          max: 1
        }],
        style: { stroke: '#fff', lineWidth: 1 },
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
        if (this.dataSource && this.dataSource.length > 0) {
          temp = this.dataSource.map(item => {
            // 为了防止直接修改源数据导致报错
            let obj = Object.assign({}, item)
            obj.type = obj.x
            return obj
          })
        }
        const dv = new DataSet.View().source(temp);
        dv.transform({
          type: 'fold',
          fields: this.fields,
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