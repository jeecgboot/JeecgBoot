<template>
  <div ref="chartRef" :style="{ height, width }"></div>
</template>
<script lang="ts">
  import { defineComponent, PropType, ref, Ref, onMounted } from 'vue';

  import { useECharts } from '/@/hooks/web/useECharts';

  export default defineComponent({
    props: {
      width: {
        type: String as PropType<string>,
        default: '100%',
      },
      height: {
        type: String as PropType<string>,
        default: 'calc(100vh - 78px)',
      },
    },
    setup() {
      const chartRef = ref<HTMLDivElement | null>(null);
      const { setOptions } = useECharts(chartRef as Ref<HTMLDivElement>);
      const dataAll = [389, 259, 262, 324, 232, 176, 196, 214, 133, 370];
      const yAxisData = ['原因1', '原因2', '原因3', '原因4', '原因5', '原因6', '原因7', '原因8', '原因9', '原因10'];
      onMounted(() => {
        setOptions({
          backgroundColor: '#0f375f',
          title: [
            {
              text: '各渠道投诉占比',
              left: '2%',
              top: '1%',
              textStyle: {
                color: '#fff',
                fontSize: 14,
              },
            },
            {
              text: '投诉原因TOP10',
              left: '40%',
              top: '1%',
              textStyle: {
                color: '#fff',
                fontSize: 14,
              },
            },
            {
              text: '各级别投诉占比',
              left: '2%',
              top: '50%',
              textStyle: {
                color: '#fff',
                fontSize: 14,
              },
            },
          ],
          grid: [{ left: '50%', top: '7%', width: '45%', height: '90%' }],
          tooltip: {
            formatter: '{b} ({c})',
          },
          xAxis: [
            {
              gridIndex: 0,
              axisTick: { show: false },
              axisLabel: { show: false },
              splitLine: { show: false },
              axisLine: { show: false },
            },
          ],
          yAxis: [
            {
              gridIndex: 0,
              interval: 0,
              data: yAxisData.reverse(),
              axisTick: { show: false },
              axisLabel: { show: true },
              splitLine: { show: false },
              axisLine: { show: true, lineStyle: { color: '#6173a3' } },
            },
          ],
          series: [
            {
              name: '各渠道投诉占比',
              type: 'pie',
              radius: '30%',
              center: ['22%', '25%'],
              data: [
                { value: 335, name: '客服电话' },
                { value: 310, name: '奥迪官网' },
                { value: 234, name: '媒体曝光' },
                { value: 135, name: '质检总局' },
                { value: 105, name: '其他' },
              ],
              labelLine: { show: false },
              label: {
                show: true,
                formatter: '{b} \n ({d}%)',
                color: '#B1B9D3',
              },
            },
            {
              name: '各级别投诉占比',
              type: 'pie',
              radius: '30%',
              center: ['22%', '75%'],
              labelLine: { show: false },
              data: [
                { value: 335, name: 'A级' },
                { value: 310, name: 'B级' },
                { value: 234, name: 'C级' },
                { value: 135, name: 'D级' },
              ],
              label: {
                show: true,
                formatter: '{b} \n ({d}%)',
                color: '#B1B9D3',
              },
            },
            {
              name: '投诉原因TOP10',
              type: 'bar',
              xAxisIndex: 0,
              yAxisIndex: 0,
              barWidth: '45%',
              itemStyle: { color: '#86c9f4' },
              label: { show: true, position: 'right', color: '#9EA7C4' },
              data: dataAll.sort(),
            },
          ],
        });
      });
      return { chartRef };
    },
  });
</script>
