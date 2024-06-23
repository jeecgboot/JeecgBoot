<template>
  <a-card :loading="loading" :bordered="false" :body-style="{ padding: '0' }">
    <div class="salesCard">
      <a-tabs default-active-key="1" size="large" :tab-bar-style="{ marginBottom: '24px', paddingLeft: '16px' }">
        <template #rightExtra>
          <div class="extra-wrapper">
            <div class="extra-item">
              <a>今日</a>
              <a>本周</a>
              <a>本月</a>
              <a>本年</a>
            </div>
            <a-range-picker :style="{ width: '256px' }" />
          </div>
        </template>
        <a-tab-pane loading="true" tab="受理监管" key="1">
          <a-row>
            <a-col :xl="16" :lg="12" :md="12" :sm="24" :xs="24">
              <Bar :chartData="barData" :option="{ title: { text: '', textStyle: { fontWeight: 'lighter' } } }" height="40vh" :seriesColor="seriesColor" />
            </a-col>
            <a-col :xl="8" :lg="12" :md="12" :sm="24" :xs="24">
              <QuickNav :loading="loading" class="enter-y" :bordered="false" :body-style="{ padding: 0 }" />
            </a-col>
          </a-row>
        </a-tab-pane>
        <a-tab-pane tab="交互监管" key="2">
          <a-row>
            <a-col :xl="16" :lg="12" :md="12" :sm="24" :xs="24">
              <BarMulti
                :seriesColor="interactiveColor"
                :chartData="barMultiData"
                :option="{ title: { text: '', textStyle: { fontWeight: 'lighter' } } }"
                height="40vh"
              />
            </a-col>
            <a-col :xl="8" :lg="12" :md="12" :sm="24" :xs="24">
              <QuickNav :loading="loading" class="enter-y" :bordered="false" :body-style="{ padding: 0 }" />
            </a-col>
          </a-row>
        </a-tab-pane>
        <a-tab-pane tab="存储监管" key="3">
          <a-row>
            <a-col :xl="16" :lg="12" :md="12" :sm="24" :xs="24" style="display: flex">
              <Gauge :seriesColor="seriesColor" :chartData="{ name: 'C盘', value: 70 }" height="30vh"></Gauge>
              <Gauge :seriesColor="seriesColor" :chartData="{ name: 'D盘', value: 50 }" height="30vh"></Gauge>
            </a-col>
            <a-col :xl="8" :lg="12" :md="12" :sm="24" :xs="24">
              <QuickNav :loading="loading" class="enter-y" :bordered="false" :body-style="{ padding: 0 }" />
            </a-col>
          </a-row>
        </a-tab-pane>
      </a-tabs>
    </div>
  </a-card>
</template>
<script lang="ts" setup>
  import { ref, computed } from 'vue';
  import Bar from '/@/components/chart/Bar.vue';
  import BarMulti from '/@/components/chart/BarMulti.vue';
  import Gauge from '/@/components/chart/Gauge.vue';
  import QuickNav from './QuickNav.vue';
  import { useRootSetting } from '/@/hooks/setting/useRootSetting';

  defineProps({
    loading: {
      type: Boolean,
    },
  });
  const { getThemeColor } = useRootSetting();
  const interactiveColor = ref();
  const rankList = [];
  for (let i = 0; i < 7; i++) {
    rankList.push({
      name: '白鹭岛 ' + (i + 1) + ' 号店',
      total: 1234.56 - i * 100,
    });
  }

  const barData = [];
  for (let i = 0; i < 12; i += 1) {
    barData.push({
      name: `${i + 1}月`,
      value: Math.floor(Math.random() * 1000) + 200,
    });
  }
  const barMultiData = [];
  for (let j = 0; j < 2; j++) {
    for (let i = 0; i < 12; i += 1) {
      barMultiData.push({
        type: j == 0 ? 'jeecg' : 'jeebt',
        name: `${i + 1}月`,
        value: Math.floor(Math.random() * 1000) + 200,
      });
    }
  }

  const seriesColor = computed(() => {
    interactiveColor.value = [
      { type: 'jeecg', color: getThemeColor.value },
      { type: 'jeebt', color: getRandomColor() },
    ];
    return getThemeColor.value;
  });
  function getRandomColor() {
    var letters = '0123456789ABCDEF';
    var color = '#';
    for (var i = 0; i < 6; i++) {
      color += letters[Math.floor(Math.random() * 16)];
    }
    return color;
  }
</script>

<style lang="less" scoped>
  .extra-wrapper {
    line-height: 55px;
    padding-right: 24px;

    .extra-item {
      display: inline-block;
      margin-right: 24px;

      a {
        margin-left: 24px;
      }
    }
  }
</style>
