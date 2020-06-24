<template>
  <div>
    <v-chart :forceFit="true" :height="height" :data="data">
      <v-coord type="rect" direction="LB" />
      <v-tooltip />
      <v-legend />
      <v-axis dataKey="State" :label="label" />
      <v-stack-bar position="State*流程数量"  color="流程状态" />
    </v-chart>
  </div>

</template>

<script>
  const DataSet = require('@antv/data-set');

  export default {
    name: 'StackBar',
    props: {
      dataSource: {
        type: Array,
        required: true,
        default: () => [
          { 'State': '请假', '流转中': 25, '已归档': 18 },
          { 'State': '出差', '流转中': 30, '已归档': 20 },
          { 'State': '加班', '流转中': 38, '已归档': 42},
          { 'State': '用车', '流转中': 51, '已归档': 67}
        ]
      },
      height: {
        type: Number,
        default: 254
      }
    },
    data() {
      return {
        label: { offset: 12 }
      }
    },
    computed: {
      data() {
        const dv = new DataSet.View().source(this.dataSource);
        dv.transform({
          type: 'fold',
          fields: ['流转中', '已归档'],
          key: '流程状态',
          value: '流程数量',
          retains: ['State'],
        });
       return dv.rows;
      }
    }
  }
</script>