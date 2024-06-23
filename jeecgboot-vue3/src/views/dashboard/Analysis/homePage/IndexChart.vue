<template>
  <div class="p-4">
    <ChartGroupCard class="enter-y" :loading="loading" type="chart" />
    <SaleTabCard class="!my-4 enter-y" :loading="loading" />
    <a-row>
      <a-col :span="24">
        <a-card :loading="loading" :bordered="false" title="最近一周访问量统计">
          <div class="infoArea">
            <HeadInfo title="今日IP" :iconColor="ipColor" :content="loginfo.todayIp" icon="environment"></HeadInfo>
            <HeadInfo title="今日访问" :iconColor="visitColor" :content="loginfo.todayVisitCount" icon="team"></HeadInfo>
            <HeadInfo title="总访问量" :iconColor="seriesColor" :content="loginfo.totalVisitCount" icon="rise"></HeadInfo>
          </div>
          <LineMulti :chartData="lineMultiData" height="33vh" type="line" :option="{ legend: { top: 'bottom' } }"></LineMulti>
        </a-card>
      </a-col>
    </a-row>
  </div>
</template>
<script lang="ts" setup>
  import { ref, watch } from 'vue';
  import ChartGroupCard from '../components/ChartGroupCard.vue';
  import SaleTabCard from '../components/SaleTabCard.vue';
  import LineMulti from '/@/components/chart/LineMulti.vue';
  import HeadInfo from '/@/components/chart/HeadInfo.vue';
  import { getLoginfo, getVisitInfo } from '../api.ts';
  import { useRootSetting } from '/@/hooks/setting/useRootSetting';

  const loading = ref(true);
  const { getThemeColor } = useRootSetting();

  setTimeout(() => {
    loading.value = false;
  }, 500);

  const loginfo = ref({});
  const lineMultiData = ref([]);

  function initLogInfo() {
    getLoginfo(null).then((res) => {
      if (res.success) {
        Object.keys(res.result).forEach((key) => {
          res.result[key] = res.result[key] + '';
        });
        loginfo.value = res.result;
      }
    });
    getVisitInfo(null).then((res) => {
      if (res.success) {
        lineMultiData.value = [];
        res.result.forEach((item) => {
          lineMultiData.value.push({ name: item.type, type: 'ip', value: item.ip, color: ipColor.value });
          lineMultiData.value.push({ name: item.type, type: 'visit', value: item.visit, color: visitColor.value });
        });
      }
    });
  }

  const ipColor = ref();
  const visitColor = ref();
  const seriesColor = ref();
  watch(
    () => getThemeColor.value,
    () => {
      seriesColor.value = getThemeColor.value;
      visitColor.value = '#67B962';
      ipColor.value = getThemeColor.value;
      initLogInfo();
    },
    { immediate: true }
  );

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
   .infoArea {
    display: flex;
    justify-content: space-between;
    padding: 0 10%;
    .head-info.center {
      padding: 0;
    }
    .head-info {
      min-width: 0;
    }
  }
  .circle-cust {
    position: relative;
    top: 28px;
    left: -100%;
  }

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

  /* 首页访问量统计 */
  .head-info {
    position: relative;
    text-align: left;
    padding: 0 32px 0 0;
    min-width: 125px;

    &.center {
      text-align: center;
      padding: 0 32px;
    }

    span {
      color: rgba(0, 0, 0, 0.45);
      display: inline-block;
      font-size: 0.95rem;
      line-height: 42px;
      margin-bottom: 4px;
    }

    p {
      line-height: 42px;
      margin: 0;

      a {
        font-weight: 600;
        font-size: 1rem;
      }
    }
  }
  .ant-card {
    ::v-deep(.ant-card-head-title) {
      font-weight: normal;
    }
  }
</style>
