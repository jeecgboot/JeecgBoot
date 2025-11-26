<!-- 部门负责人页面 -->
<template>
  <!--引用表格-->
  <BasicTable @register="registerTable"></BasicTable>
</template>

<script setup lang="ts" name="DepartmentHeadList">
  import { BasicTable } from '@/components/Table';
  import { useListPage } from '@/hooks/system/useListPage';
  import { userColumns } from '@/views/system/depart/depart.data';
  import { getDepartmentHead } from '../depart.api';
  import { computed, watch } from 'vue';
  const props = defineProps({
    data: { require: true, type: Object },
  });
  // 当前部门id
  const departId = computed(() => props.data?.id);
 
  // 列表页面公共参数、方法
  const { tableContext } = useListPage({
    tableProps: {
      api: getDepartmentHead,
      columns: userColumns,
      canResize: false,
      rowKey: 'id',
      tableSetting: { cacheKey: 'depart_head_list' },
      // 请求之前对参数做处理
      beforeFetch(params) {
        return Object.assign(params, { departId: departId.value });
      },
      showActionColumn: false,
      immediate: !!departId.value,
    },
  });
  // 注册 ListTable
  const [registerTable, { reload }] = tableContext;

  watch(
    () => props.data,
    () => reload()
  );
</script>

<style scoped lang="less"></style>
