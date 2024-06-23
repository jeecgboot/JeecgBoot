<template>
  <div ref="chartRef" :style="{ height, width }"></div>
</template>
<script lang="ts">
  import { defineComponent, PropType, ref, Ref, watchEffect, reactive, watch } from 'vue';
  import { useECharts } from '/@/hooks/web/useECharts';
  import { cloneDeep } from 'lodash-es';
  export default defineComponent({
    name: 'Pie',
    props: {
      chartData: {
        type: Array,
        default: () => [],
      },
      size: {
        type: Object,
        default: () => {},
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
    emits: ['click'],
    setup(props, { emit }) {
      const chartRef = ref<HTMLDivElement | null>(null);
      const { setOptions, getInstance, resize } = useECharts(chartRef as Ref<HTMLDivElement>);
      const option = reactive({
        tooltip: {
          formatter: '{b} ({c})',
        },
        series: [
          {
            type: 'pie',
            radius: '72%',
            center: ['50%', '55%'],
            data: [],
            labelLine: { show: true },
            label: {
              show: true,
              formatter: '{b} \n ({d}%)',
              color: '#B1B9D3',
            },
          },
        ],
      });

      watchEffect(() => {
        props.chartData && initCharts();
      });
      /**
       * 监听拖拽大小变化
       */
      watch(
        () => props.size,
        () => {
          resize();
        },
        {
          immediate: true,
        }
      );
      function initCharts() {
        if (props.option) {
          Object.assign(option, cloneDeep(props.option));
        }
        option.series[0].data = props.chartData;
        setOptions(option);
        resize();
        getInstance()?.off('click', onClick);
        getInstance()?.on('click', onClick);
      }

      function onClick(params) {
        emit('click', params);
      }

      return { chartRef };
    },
  });
</script>
