<template>
  <div class="p-4">
    <BasicTable @register="registerTable" :dataSource="dataSource" @change="handlerTableChange">
      <template #tableTitle>
        <div slot="message">
          共追踪到 {{ dataSource.length }} 条近期HTTP请求记录
          <a-divider type="vertical" />
          <a @click="loadDate">立即刷新</a>
        </div>
      </template>
      <template #toolbar>
        <a-radio-group class="http-status-choose" size="small" v-model:value="query" @change="loadDate">
          <a-radio-button value="all">全部</a-radio-button>
          <a-radio-button value="success">成功</a-radio-button>
          <a-radio-button value="error">错误</a-radio-button>
        </a-radio-group>
      </template>
    </BasicTable>
  </div>
</template>
<script lang="ts" name="monitor-trace" setup>
  import { onMounted, ref, reactive } from 'vue';
  import { BasicTable, useTable, TableAction } from '/@/components/Table';
  import { getActuatorList } from './trace.api';
  import { columns } from './trace.data';
  import { useMessage } from '/@/hooks/web/useMessage';

  const dataSource = ref([]);
  const { createMessage } = useMessage();
  const query = ref('all');
  const order = ref('');

  const [registerTable, { reload }] = useTable({
    columns,
    showIndexColumn: false,
    bordered: true,
    rowKey: 'id',
  });

  function loadDate() {
    getActuatorList(query.value,order.value).then((res) => {
      let filterData = [];
      for (let d of res.traces) {
        if (d.request.method !== 'OPTIONS' && d.request.uri.indexOf('httptrace') === -1) {
          filterData.push(d);
        }
      }
      dataSource.value = filterData;
    });
  }

  const handlerTableChange = (args, arg1, sort, action) => {
    if ('sort' == action.action && sort.field) {
      order.value = sort.field;
      if (sort.order) {
        order.value += sort.order == 'ascend' ? '/asc' : '/desc';
      } else {
        order.value = '';
      }
    }
    loadDate();
  };

  onMounted(() => {
    loadDate();
  });
</script>
<style scoped>
  :deep(.jeecg-basic-table-header__toolbar) {
    width: 150px;
  }
</style>
