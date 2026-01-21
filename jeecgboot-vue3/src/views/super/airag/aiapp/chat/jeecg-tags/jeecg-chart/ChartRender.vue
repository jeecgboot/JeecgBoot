<template>
  <div class="ai-chat-chart">
    <div v-if="isError" class="ai-chat-chart__error">{{ errorMessage }}</div>
    <div v-else-if="!resolvedType || !hasData" class="ai-chat-chart__error">
      <div v-if="loading"
           style="color: #999; padding: 12px 8px; border: 1px dashed #eee; margin: 8px 0; border-radius: 4px;">
        <span>图表渲染中...</span>
      </div>
      <span v-else>模型返回的图表渲染格式不正确，请优化提示词或重新尝试。</span>
    </div>
    <div v-else class="ai-chat-chart__body">
      <!-- 折线图 -->
      <LineMulti v-if="resolvedType === 'line'" v-bind="lineProps"/>
      <!-- 柱状图 -->
      <BarMulti v-else-if="resolvedType === 'bar'" v-bind="barProps"/>
      <!-- 饼图 -->
      <Pie v-else-if="resolvedType === 'pie'" v-bind="pieProps"/>
      <!-- 多列柱状图 -->
      <BarMulti v-else-if="resolvedType === 'multibar'" v-bind="multiBarProps"/>
      <!-- 多行折线图 -->
      <LineMulti v-else-if="resolvedType === 'multiline'" v-bind="multiLineProps"/>
      <!-- 折柱图 -->
      <BarAndLine v-else-if="resolvedType === 'barline'" v-bind="barLineProps"/>
      <!-- 面积图 -->
      <SingleLine v-else-if="resolvedType === 'area'" v-bind="areaLineProps"/>
      <!-- 雷达图 -->
      <Radar v-else-if="resolvedType === 'radar'" v-bind="radarProps"/>
      <!-- 仪表盘 -->
      <Gauge v-else-if="resolvedType === 'gauge'" v-bind="gaugeProps"/>

    </div>
  </div>
</template>

<script setup lang="ts">
import type { ChartType } from './types';
import { computed, ref, watchEffect } from 'vue';
import LineMulti from '/@/components/chart/LineMulti.vue';
import BarMulti from '/@/components/chart/BarMulti.vue';
import Pie from '/@/components/chart/Pie.vue';
import Radar from '/@/components/chart/Radar.vue';
import Gauge from '/@/components/chart/Gauge.vue';
import BarAndLine from '/@/components/chart/BarAndLine.vue';
import SingleLine from '/@/components/chart/SingleLine.vue';

const props = defineProps({
  /**
   * 图表配置字符串，示例：
   * {"type":"bar","data":[{"x":"数据项1","y":100},{"x":"数据项2","y":80}]}
   */
  data: {
    type: String,
    required: true,
  },
  loading: {
    type: Boolean,
    default: false,
  },
});

/**
 * 解析失败或类型错误的提示文本。
 */
const errorMessage = ref<string>('');
/**
 * 是否存在阻止渲染的错误。
 */
const isError = ref<boolean>(false);

/**
 * 将字符串解析为配置对象，失败时记录错误信息。
 */
const parsedConfig = computed<Recordable>(() => {
  try {
    errorMessage.value = '';
    isError.value = false;
    return JSON.parse(props.data || '{}');
  } catch (error) {
    errorMessage.value = '图表数据解析错误，无法渲染图表。';
    isError.value = true;
    return {};
  }
});

/**
 * 支持的图表类型集合。
 */
// 支持的类型覆盖常见图表，名称与提示词保持宽松映射
const supportedTypes: ChartType[] = ['bar', 'line', 'pie', 'radar', 'gauge', 'barline', 'multibar', 'multiline', 'area'];

/**
 * 解析得到的图表类型，统一小写后做合法性校验。
 */
const resolvedType = computed<ChartType>(() => {
  const rawType = String((parsedConfig.value as any).type || '').toLowerCase();
  const normalizedType = (rawType || '').replace(/[-_\s]/g, '');
  const typeAliasMap: Record<string, ChartType> = {
    bar: 'bar',
    line: 'line',
    pie: 'pie',
    radar: 'radar',
    gauge: 'gauge',
    barline: 'barline',
    barandline: 'barline',
    linebar: 'barline',
    multiline: 'multiline',
    multibar: 'multibar',
    area: 'area',
    arealine: 'area',
  };
  const mapped = typeAliasMap[normalizedType] || '';
  if (mapped && supportedTypes.includes(mapped)) {
    return mapped as ChartType;
  }
  return '';
});

/**
 * 当类型不被支持时，给出错误提示。
 */
watchEffect(() => {
  if (isError.value) {
    return;
  }
  if (resolvedType.value === '') {
    const typeValue = (parsedConfig.value as any).type;
    if (typeValue) {
      errorMessage.value = '当前仅支持 bar、line、pie、radar、gauge、barline、multibar、multiline、area 类型图表。';
      isError.value = true;
    }
  } else {
    errorMessage.value = '';
    isError.value = false;
  }
});

/**
 * 原始数据列表。
 */
const rawData = computed<unknown>(() => (parsedConfig.value as any).data);

/**
 * 判断是否存在可用的数据数组。
 */
const hasData = computed<boolean>(() => {
  if (resolvedType.value === 'gauge') {
    return Boolean(rawData.value);
  }
  return Array.isArray(rawData.value) && rawData.value.length > 0;
});

/**
 * 将原始数据标准化为多序列图表所需的结构。
 */
const multiSeriesData = computed<Recordable[]>(() => {
  if (!Array.isArray(rawData.value)) {
    return [];
  }
  return rawData.value.map((item: Recordable) => buildMultiSeriesItem(item));
});

/**
 * 面积折线与多行折线共享的数据结构，透传 seriesType 控制折线类型。
 */
const areaSeriesData = computed<Recordable[]>(() => {
  if (!Array.isArray(rawData.value)) {
    return [];
  }
  return rawData.value.map((item: Recordable) => {
    return {
      ...buildMultiSeriesItem(item),
      seriesType: item && item.seriesType ? String(item.seriesType) : 'line',
      areaStyle: {},
    };
  });
});

/**
 * 将原始数据标准化为饼图所需的结构。
 */
const pieSeriesData = computed<Recordable[]>(() => {
  if (!Array.isArray(rawData.value)) {
    return [];
  }
  return rawData.value.map((item: Recordable) => {
    return {
      name: resolveName(item),
      value: resolveNumber(item),
    };
  });
});

/**
 * 构建多序列图表需要的标准化数据项。
 */
function buildMultiSeriesItem(item: Recordable): Recordable {
  const seriesName = resolveSeriesName(item);
  const name = resolveName(item);
  const value = resolveNumber(item);
  return {type: seriesName, name, value};
}

/**
 * 解析系列名称，优先使用 series，其次使用 type，最后回退为“数据”。
 */
function resolveSeriesName(item: Recordable): string {
  if (item && item.series !== undefined) {
    return String(item.series);
  }
  if (item && item.type !== undefined) {
    return String(item.type);
  }
  return '数据';
}

/**
 * 解析横轴名称，支持 x 与 name 字段。
 */
function resolveName(item: Recordable): string {
  if (item && item.x !== undefined) {
    return String(item.x);
  }
  if (item && item.name !== undefined) {
    return String(item.name);
  }
  return '';
}

/**
 * 解析数值字段，支持 y 与 value，非数字时回退为 0。
 */
function resolveNumber(item: Recordable): number {
  const rawValue = item ? item.y ?? item.value : null;
  const num = Number(rawValue);
  if (Number.isFinite(num)) {
    return num;
  }
  return 0;
}

/**
 * 解析序列类型，折柱图需要区分 bar / line。
 */
function resolveSeriesType(item: Recordable): string {
  if (item && item.seriesType !== undefined) {
    return String(item.seriesType);
  }
  return 'bar';
}

/**
 * 仪表盘数据，允许数组或对象输入，确保返回 name、value。
 */
const gaugeData = computed(() => {
  const dataSource = rawData.value as any;
  if (Array.isArray(dataSource) && dataSource.length > 0) {
    const item = dataSource[0];
    return { name: resolveName(item) || '仪表盘', value: resolveNumber(item) };
  }
  if (dataSource && typeof dataSource === 'object') {
    return { name: resolveName(dataSource) || '仪表盘', value: resolveNumber(dataSource as Recordable) };
  }
  return { name: '仪表盘', value: 0 };
});

/**
 * 雷达图数据，支持 max 字段定义指标上限。
 */
const radarSeriesData = computed<Recordable[]>(() => {
  if (!Array.isArray(rawData.value)) {
    return [];
  }
  return rawData.value.map((item: Recordable) => {
    return {
      type: resolveSeriesName(item),
      name: resolveName(item),
      value: resolveNumber(item),
      max: item && item.max !== undefined ? Number(item.max) : undefined,
    };
  });
});

/**
 * 折柱图数据，附带 seriesType 用于区分柱/线。
 */
const barLineSeriesData = computed<Recordable[]>(() => {
  if (!Array.isArray(rawData.value)) {
    return [];
  }
  return rawData.value.map((item: Recordable) => {
    return {
      ...buildMultiSeriesItem(item),
      seriesType: resolveSeriesType(item),
    };
  });
});

/**
 * 折线图的渲染属性。
 */
const lineProps = computed(() => {
  return {
    type: 'line',
    height: '360px',
    width: '100%',
    chartData: multiSeriesData.value,
  };
});

/**
 * 柱状图的渲染属性。
 */
const barProps = computed(() => {
  return {
    height: '360px',
    width: '100%',
    chartData: multiSeriesData.value,
  };
});

/**
 * 饼图的渲染属性。
 */
const pieProps = computed(() => {
  return {
    height: '360px',
    width: '100%',
    chartData: pieSeriesData.value,
  };
});

/**
 * 多列柱状图配置，与 bar 相同但允许区分类型前缀。
 */
const multiBarProps = computed(() => {
  return {
    height: '360px',
    width: '100%',
    chartData: multiSeriesData.value,
    option: parsedConfig.value.option || {},
  };
});

/**
 * 多行折线图配置。
 */
const multiLineProps = computed(() => {
  return {
    type: 'line',
    height: '360px',
    width: '100%',
    chartData: multiSeriesData.value,
    option: parsedConfig.value.option || {},
  };
});

/**
 * 面积折线图配置，开启面积样式。
 */
const areaLineProps = computed(() => {
  return {
    type: 'line',
    height: '360px',
    width: '100%',
    chartData: areaSeriesData.value,
    option: { ...(parsedConfig.value.option || {}), areaStyle: {} },
  };
});

/**
 * 雷达图配置。
 */
const radarProps = computed(() => {
  return {
    height: '420px',
    width: '100%',
    chartData: radarSeriesData.value,
    option: parsedConfig.value.option || {},
  };
});

/**
 * 仪表盘配置。
 */
const gaugeProps = computed(() => {
  return {
    height: '360px',
    width: '100%',
    chartData: gaugeData.value,
    option: parsedConfig.value.option || {},
    seriesColor: (parsedConfig.value as any).seriesColor || undefined,
  };
});

/**
 * 折柱图配置，支持自定义颜色。
 */
const barLineProps = computed(() => {
  return {
    height: '360px',
    width: '100%',
    chartData: barLineSeriesData.value,
    customColor: (parsedConfig.value as any).colors || [],
    option: parsedConfig.value.option || {},
  };
});
</script>

<style scoped lang="less">

.ai-chat-chart {
  width: 100%;
  min-width: 360px;
  padding: 12px 0;
}

.ai-chat-chart__body {
  width: 100%;
}

.ai-chat-chart__error {
  color: #ff4d4f;
  font-size: 14px;
  line-height: 20px;
}

</style>
