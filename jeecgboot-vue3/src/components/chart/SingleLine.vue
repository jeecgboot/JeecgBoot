<template>
  <div ref="chartRef" :style="{ height, width }"></div>
</template>
<script lang="ts">
  import { defineComponent, PropType, ref, Ref, reactive, watchEffect } from 'vue';
  import { useECharts } from '/@/hooks/web/useECharts';
  import { cloneDeep } from 'lodash-es';
  export default defineComponent({
    name: 'single-line',
    props: {
      chartData: {
        type: Array,
        default: () => [],
      },
      option: {
        type: Object,
        default: () => ({}),
      },
      width: {
        type: String as PropType<string>,
        default: '100%',
      },
      height: {
        type: String as PropType<string>,
        default: 'calc(100vh - 78px)',
      },
      // update-begin--author:liaozhiyang---date:20240407---for：【QQYUN-8762】首页默认及echars颜色调整
      seriesColor: {
        type: String,
        default: '#1890ff',
      },
      // update-end--author:liaozhiyang---date:20240407---for：【QQYUN-8762】首页默认及echars颜色调整
    },
    setup(props) {
      const chartRef = ref<HTMLDivElement | null>(null);
      const { setOptions, echarts } = useECharts(chartRef as Ref<HTMLDivElement>);
      const option = reactive({
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow',
            label: {
              show: true,
              backgroundColor: '#333',
            },
          },
        },
        xAxis: {
          type: 'category',
          data: [],
        },
        yAxis: {
          type: 'value',
        },
        series: [
          {
            type: 'line',
            showSymbol: false,
            smooth: true,
            areaStyle: {},
            data: [],
            color: props.seriesColor,
          },
        ],
      });

      watchEffect(() => {
        props.chartData && initCharts();
      });
      
      function initCharts() {
        if (props.option) {
          Object.assign(option, cloneDeep(props.option));
        }
        let seriesData = props.chartData.map((item) => {
          return item.value;
        });
        let xAxisData = props.chartData.map((item) => {
          return item.name;
        });
        option.series[0].data = seriesData;
        // update-begin--author:liaozhiyang---date:20240407---for：【QQYUN-8762】首页默认及echars颜色调整
        option.series[0].color = props.seriesColor;
        // update-end--author:liaozhiyang---date:20240407---for：【QQYUN-8762】首页默认及echars颜色调整
        option.xAxis.data = xAxisData;
        setOptions(option);
      }
      return { chartRef };
    },
  });
</script>
