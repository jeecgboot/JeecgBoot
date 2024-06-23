<template>
  <div ref="chartRef" :style="{ height, width }"></div>
</template>
<script lang="ts">
  import { defineComponent, PropType, ref, Ref, reactive, watchEffect } from 'vue';
  import { useECharts } from '/@/hooks/web/useECharts';
  import { cloneDeep } from 'lodash-es';
  export default defineComponent({
    name: 'barAndLine',
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
            name: 'bar',
            type: 'bar',
            data: [],
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
        //图例类型
        let typeArr = Array.from(new Set(props.chartData.map((item) => item.type)));
        //轴数据
        let xAxisData = Array.from(new Set(props.chartData.map((item) => item.name)));
        let seriesData = [];
        typeArr.forEach((type) => {
          let obj = { name: type };
          let chartArr = props.chartData.filter((item) => type === item.type);
          //data数据
          obj['data'] = chartArr.map((item) => item.value);
          obj['type'] = chartArr[0].seriesType;
          seriesData.push(obj);
        });
        option.series = seriesData;
        option.xAxis.data = xAxisData;
        setOptions(option);
      }
      return { chartRef };
    },
  });
</script>
