<template>
  <Card title="销售统计" :loading="loading">
    <div ref="chartRef" :style="{ width, height }"></div>
  </Card>
</template>
<script lang="ts" setup>
  import { Ref, ref, watch } from 'vue';
  import { Card } from 'ant-design-vue';
  import { useECharts } from '/@/hooks/web/useECharts';

  const props = defineProps({
    loading: Boolean,
    width: {
      type: String as PropType<string>,
      default: '100%',
    },
    height: {
      type: String as PropType<string>,
      default: '400px',
    },
  });

  const chartRef = ref<HTMLDivElement | null>(null);
  const { setOptions } = useECharts(chartRef as Ref<HTMLDivElement>);
  watch(
    () => props.loading,
    () => {
      if (props.loading) {
        return;
      }
      setOptions({
        legend: {
          bottom: 0,
          data: ['Visits', 'Sales'],
        },
        tooltip: {},
        radar: {
          radius: '60%',
          splitNumber: 8,
          indicator: [
            {
              name: '2017',
            },
            {
              name: '2017',
            },
            {
              name: '2018',
            },
            {
              name: '2019',
            },
            {
              name: '2020',
            },
            {
              name: '2021',
            },
          ],
        },
        series: [
          {
            type: 'radar' as 'custom',
            symbolSize: 0,
            areaStyle: {
              shadowBlur: 0,
              shadowColor: 'rgba(0,0,0,.2)',
              shadowOffsetX: 0,
              shadowOffsetY: 10,
              opacity: 1,
            },
            data: [
              {
                value: [90, 50, 86, 40, 50, 20],
                name: 'Visits',
                itemStyle: {
                  color: '#b6a2de',
                },
              },
              {
                value: [70, 75, 70, 76, 20, 85],
                name: 'Sales',
                itemStyle: {
                  color: '#67e0e3',
                },
              },
            ],
          },
        ],
      });
    },
    { immediate: true }
  );
</script>
