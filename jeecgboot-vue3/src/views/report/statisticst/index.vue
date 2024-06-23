<template>
  <div class="p-4">
    <a-card :bordered="false" style="height: 100%">
      <a-tabs v-model:activeKey="activeKey" animated @change="tabChange">
        <a-tab-pane key="bar" tab="柱状图">
          <a-row>
            <a-col :span="10">
              <a-radio-group v-model:value="barType" @change="statisticst">
                <a-radio-button value="year">按年统计</a-radio-button>
                <a-radio-button value="month">按月统计</a-radio-button>
                <a-radio-button value="category">按类别统计</a-radio-button>
                <a-radio-button value="cabinet">按柜号统计</a-radio-button>
              </a-radio-group>
            </a-col>
          </a-row>
          <Bar :chartData="dataSource" height="50vh"></Bar>
        </a-tab-pane>
        <a-tab-pane key="pie" tab="饼状图" force-render>
          <a-row :gutter="24">
            <a-col :span="10">
              <a-radio-group v-model:value="pieType" @change="statisticst">
                <a-radio-button value="year">按年统计</a-radio-button>
                <a-radio-button value="month">按月统计</a-radio-button>
                <a-radio-button value="category">按类别统计</a-radio-button>
                <a-radio-button value="cabinet">按柜号统计</a-radio-button>
              </a-radio-group>
            </a-col>
            <Pie :chartData="dataSource" height="40vh"></Pie>
          </a-row>
        </a-tab-pane>
      </a-tabs>
    </a-card>
  </div>
</template>

<script lang="ts" setup>
  import { defHttp } from '/@/utils/http/axios';
  import { ref, unref, reactive } from 'vue';
  import Bar from '/@/components/chart/Bar.vue';
  import Pie from '/@/components/chart/Pie.vue';

  const activeKey = ref('bar');
  const barType = ref('year');
  const pieType = ref('year');
  const dataSource = ref([]);
  const url = reactive({
    getYearCountInfo: '/mock/api/report/getYearCountInfo',
    getMonthCountInfo: '/mock/api/report/getMonthCountInfo',
    getCntrNoCountInfo: '/mock/api/report/getCntrNoCountInfo',
    getCabinetCountInfo: '/mock/api/report/getCabinetCountInfo',
  });

  async function loadDate(url, type, params) {
    const res = await defHttp.get({ url, params }, { isTransformResponse: false, errorMessageMode: 'none' });
    if (res.success) {
      dataSource.value = [];
      switch (type) {
        case 'year':
          getYearCountSource(res.result);
          break;
        case 'month':
          getMonthCountSource(res.result);
          break;
        case 'category':
          getCategoryCountSource(res.result);
          break;
        case 'cabinet':
          getCabinetCountSource(res.result);
          break;
        default:
          break;
      }
    }
  }
  function getYearCountSource(data) {
    for (let i = 0; i < data.length; i++) {
      dataSource.value.push({
        name: `${data[i].year}年`,
        value: data[i].yearcount,
      });
    }
  }
  function getMonthCountSource(data) {
    for (let i = 0; i < data.length; i++) {
      dataSource.value.push({
        name: `${data[i].month}`,
        value: data[i].monthcount,
      });
    }
  }
  function getCategoryCountSource(data) {
    for (let i = 0; i < data.length; i++) {
      dataSource.value.push({
        name: `${data[i].classifyname}`,
        value: data[i].cntrnocount,
      });
    }
  }
  function getCabinetCountSource(data) {
    for (let i = 0; i < data.length; i++) {
      dataSource.value.push({
        name: `${data[i].cabinetname}`,
        value: data[i].cabinetcocunt,
      });
    }
  }
  // 选择统计类别
  function statisticst(e) {
    if (unref(activeKey) === 'pie') {
      loadDate(getUrl(unref(pieType)), unref(pieType), {});
    } else {
      loadDate(getUrl(unref(barType)), unref(barType), {});
    }
  }

  function getUrl(type) {
    if (type === 'year') {
      return url.getYearCountInfo;
    }
    if (type === 'month') {
      return url.getMonthCountInfo;
    }
    if (type === 'category') {
      return url.getCntrNoCountInfo;
    }
    if (type === 'cabinet') {
      return url.getCabinetCountInfo;
    }
  }
  //tab切换
  function tabChange(key) {
    console.log('切换的key:', key);
  }
  loadDate(url.getYearCountInfo, 'year', {});
</script>
