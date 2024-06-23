<template>
  <div ref="chartRef" :style="{ height, width }"></div>
</template>
<script lang="ts">
  import { defineComponent, PropType, ref, Ref, reactive, watchEffect } from 'vue';
  import { useECharts } from '/@/hooks/web/useECharts';
  import { GaugeChart } from 'echarts/charts';
  import { cloneDeep } from 'lodash-es';
  export default defineComponent({
    name: 'Gauge',
    props: {
      chartData: {
        type: Object as PropType<Object>,
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
        series: [
          {
            type: 'gauge',
            progress: {
              show: true,
              width: 12,
            },
            axisLine: {
              lineStyle: {
                width: 12,
              },
            },
            axisTick: {
              show: false,
            },
            splitLine: {
              length: 12,
              lineStyle: {
                width: 1,
                color: '#999',
              },
            },
            axisLabel: {
              distance: 25,
              color: '#999',
              fontSize: 12,
            },
            anchor: {
              show: true,
              showAbove: true,
              size: 20,
              itemStyle: {
                borderWidth: 5,
              },
            },
            title: {},
            detail: {
              valueAnimation: true,
              fontSize: 25,
              formatter: '{value}%',
              offsetCenter: [0, '80%'],
            },
            data: [
              {
                value: 70,
                name: '本地磁盘',
              },
            ],
          },
        ],
      });

      watchEffect(() => {
        props.chartData && initCharts();
      });

      function initCharts() {
        echarts.use(GaugeChart);
        if (props.option) {
          Object.assign(option, cloneDeep(props.option));
        }
        option.series[0].data[0].name = props.chartData.name;
        option.series[0].data[0].value = props.chartData.value;
        // update-begin--author:liaozhiyang---date:20240407---for：【QQYUN-8762】首页默认及echars颜色调整
        option.series[0].color = props.seriesColor;
        // update-end--author:liaozhiyang---date:20240407---for：【QQYUN-8762】首页默认及echars颜色调整
        setOptions(option);
      }
      return { chartRef };
    },
  });
</script>
