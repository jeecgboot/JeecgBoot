<template>
  <div>
    <div ref="chartRef" style="width: 100%; height: 400px"></div>
  </div>
</template>
<script lang="ts" setup>
  import { onMounted, ref, reactive, Ref } from 'vue';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { useECharts } from '/@/hooks/web/useECharts';
  import { GaugeChart } from 'echarts/charts';

  const props = defineProps({ data: {} });
  const dataSource = ref([]);
  const chartRef = ref<HTMLDivElement | null>(null);
  const { setOptions, echarts } = useECharts(chartRef as Ref<HTMLDivElement>);
  const loading = ref(false);
  const { createMessage } = useMessage();
  const option = reactive({
    series: [
      {
        type: 'gauge',
        progress: {
          show: true,
          width: 18,
        },
        axisLine: {
          lineStyle: {
            width: 18,
          },
        },
        axisTick: {
          show: true,
        },
        splitLine: {
          length: 15,
          lineStyle: {
            width: 2,
            color: '#999',
          },
        },
        axisLabel: {
          distance: 25,
          color: '#999',
          fontSize: 15,
        },
        anchor: {
          show: true,
          showAbove: true,
          size: 25,
          itemStyle: {
            borderWidth: 10,
          },
        },
        title: {},
        detail: {
          valueAnimation: true,
          fontSize: 50,
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

  function initCharts() {
    option.series[0].data[0].name = props.data.name;
    option.series[0].data[0].value = props.data.restPPT;
    setOptions(option);
  }

  onMounted(() => {
    console.info(props.data);
    echarts.use(GaugeChart);
    initCharts();
  });
</script>
