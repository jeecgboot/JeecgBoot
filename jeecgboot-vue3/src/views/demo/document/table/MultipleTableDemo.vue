<template>
  <div>
    <a-alert
      message="多表格实例测试 (Issue #8792 修复)"
      description="此示例演示父子组件同时使用 BasicTable 时，列配置、列宽调整等功能正常工作，两个表格实例互不干扰"
      type="info"
      show-icon
      class="mb-4"
    />

    <a-card title="父组件的表格" class="mb-4">
      <BasicTable
        @register="registerParentTable"
        :columns="parentColumns"
        :dataSource="parentData"
        :pagination="false"
        showTableSetting
        :canResize="false"
      >
        <template #toolbar>
          <a-button type="primary" @click="testParentTable">测试父表格</a-button>
        </template>
      </BasicTable>
    </a-card>

    <a-card title="子组件的表格">
      <BasicTable
        @register="registerChildTable"
        :columns="childColumns"
        :dataSource="childData"
        :pagination="false"
        showTableSetting
        :canResize="false"
      >
        <template #toolbar>
          <a-button type="primary" @click="testChildTable">测试子表格</a-button>
        </template>
      </BasicTable>
    </a-card>
  </div>
</template>

<script lang="ts" setup>
  import { ref } from 'vue';
  import { BasicTable, useTable, BasicColumn } from '/@/components/Table';
  import { useMessage } from '/@/hooks/web/useMessage';

  const { createMessage } = useMessage();

  // 父表格配置
  const parentColumns: BasicColumn[] = [
    {
      title: 'ID',
      dataIndex: 'id',
      width: 80,
    },
    {
      title: '父表格-姓名',
      dataIndex: 'name',
      width: 150,
    },
    {
      title: '父表格-年龄',
      dataIndex: 'age',
      width: 100,
    },
    {
      title: '父表格-地址',
      dataIndex: 'address',
      width: 200,
    },
  ];

  const parentData = ref([
    { id: 1, name: '父表格-张三', age: 25, address: '北京市朝阳区' },
    { id: 2, name: '父表格-李四', age: 30, address: '上海市浦东新区' },
    { id: 3, name: '父表格-王五', age: 28, address: '广州市天河区' },
  ]);

  const [registerParentTable, { getColumns: getParentColumns }] = useTable({
    columns: parentColumns,
    dataSource: parentData.value,
  });

  // 子表格配置
  const childColumns: BasicColumn[] = [
    {
      title: 'ID',
      dataIndex: 'id',
      width: 80,
    },
    {
      title: '子表格-产品名称',
      dataIndex: 'product',
      width: 150,
    },
    {
      title: '子表格-价格',
      dataIndex: 'price',
      width: 100,
    },
    {
      title: '子表格-库存',
      dataIndex: 'stock',
      width: 100,
    },
    {
      title: '子表格-分类',
      dataIndex: 'category',
      width: 150,
    },
  ];

  const childData = ref([
    { id: 1, product: '子表格-商品A', price: 99, stock: 100, category: '电子产品' },
    { id: 2, product: '子表格-商品B', price: 199, stock: 50, category: '家居用品' },
    { id: 3, product: '子表格-商品C', price: 299, stock: 30, category: '服装鞋包' },
  ]);

  const [registerChildTable, { getColumns: getChildColumns }] = useTable({
    columns: childColumns,
    dataSource: childData.value,
  });

  function testParentTable() {
    const cols = getParentColumns();
    createMessage.success(`父表格列数: ${cols.length}，请查看控制台`);
    console.log('父表格列配置:', cols);
  }

  function testChildTable() {
    const cols = getChildColumns();
    createMessage.success(`子表格列数: ${cols.length}，请查看控制台`);
    console.log('子表格列配置:', cols);
  }
</script>

<style scoped>
</style>
