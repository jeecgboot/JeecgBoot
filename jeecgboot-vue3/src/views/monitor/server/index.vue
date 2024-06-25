<template>
  <div class="p-4">
    <a-card :bordered="false" style="height: 100%">
      <a-tabs v-model:activeKey="activeKey" @change="tabChange">
        <a-tab-pane key="1" tab="服务器信息"></a-tab-pane>
        <a-tab-pane key="2" tab="JVM信息" force-render></a-tab-pane>
        <a-tab-pane key="3" tab="Tomcat信息"></a-tab-pane>
        <a-tab-pane key="4" tab="磁盘监控">
          <DiskInfo v-if="activeKey == 4" style="height: 100%"></DiskInfo>
        </a-tab-pane>
        <a-tab-pane key="5" tab="内存信息" />
      </a-tabs>
      <!--  update-begin---author:wangshuai ---date: 20230829 for：性能监控切换到磁盘监控再切回来报错列为空，不能用if判断------------>
      <BasicTable @register="registerTable" :searchInfo="searchInfo" :dataSource="dataSource" v-show="activeKey != 4">
      <!--  update-end---author:wangshuai ---date: 20230829 for：性能监控切换到磁盘监控再切回来报错列为空，不能用if判断------------>
        <template #tableTitle>
          <div slot="message"
            >上次更新时间：{{ lastUpdateTime }}
            <a-divider type="vertical" />
            <a @click="handleUpdate">立即更新</a></div
          >
        </template>
        <template #param="{ record, text }">
          <a-tag :color="textInfo[record.param].color">{{ text }}</a-tag>
        </template>
        <template #text="{ record }">
          {{ textInfo[record.param].text }}
        </template>
        <template #value="{ record, text }"> {{ text }} {{ textInfo[record.param].unit }} </template>
      </BasicTable>
    </a-card>
  </div>
</template>
<script lang="ts" name="monitor-server" setup>
  import { onMounted, ref, unref } from 'vue';
  import { BasicTable, useTable, TableAction } from '/@/components/Table';
  import DiskInfo from '../disk/DiskInfo.vue';
  import { getServerInfo, getTextInfo, getMoreInfo } from './server.api';
  import { columns } from './server.data';
  import { useMessage } from '/@/hooks/web/useMessage';
  import dayjs from 'dayjs';

  const dataSource = ref([]);
  const activeKey = ref('1');
  const moreInfo = ref({});
  const lastUpdateTime = ref({});
  let textInfo = ref({});
  const { createMessage } = useMessage();
  const checkedKeys = ref<Array<string | number>>([]);

  const searchInfo = { logType: '1' };
  const [registerTable, { reload }] = useTable({
    columns,
    showIndexColumn: false,
    bordered: true,
    pagination: false,
    canResize: false,
    tableSetting: { fullScreen: true },
    rowKey: 'id',
  });

  //tab切换
  function tabChange(key) {
    if (key != 4) {
      getInfoList(key);
    }
  }

  //加载信息
  function getInfoList(infoType) {
    lastUpdateTime.value = dayjs().format('YYYY年MM月DD日 HH时mm分ss秒');
    getServerInfo(infoType).then((res) => {
      textInfo.value = getTextInfo(infoType);
      moreInfo.value = getMoreInfo(infoType);
      let info = [];
      if (infoType === '5') {
        for (let param in res[0].result) {
          let data = res[0].result[param];
          let val = convert(data, unref(textInfo)[param].valueType);
          info.push({ id: param, param, text: 'false value', value: val });
        }
      } else {
        res.forEach((value, id) => {
          let more = unref(moreInfo)[value.name];
          if (!(more instanceof Array)) {
            more = [''];
          }
          more.forEach((item, idx) => {
            let param = value.name + item;
            let val = convert(value.measurements[idx].value, unref(textInfo)[param].valueType);
            info.push({ id: param + id, param, text: 'false value', value: val });
          });
        });
      }
      dataSource.value = info;
    });
  }

  function handleUpdate() {
    getInfoList(activeKey.value);
  }

  //单位转换
  function convert(value, type) {
    if (type === 'Number') {
      return Number(value * 100).toFixed(2);
    } else if (type === 'Date') {
      return dayjs(value * 1000).format('YYYY-MM-DD HH:mm:ss');
    } else if (type === 'RAM') {
      return Number(value / 1048576).toFixed(3);
    }
    return value;
  }

  onMounted(() => {
    getInfoList(activeKey.value);
  });
</script>
