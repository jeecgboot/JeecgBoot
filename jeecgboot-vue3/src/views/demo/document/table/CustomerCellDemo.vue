<template>
  <div class="p-4">
    <BasicTable @register="registerTable">
      <template #id="{ record }"> ID: {{ record.id }} </template>
      <template #bodyCell="{ column, record }">
        <Avatar v-if="column.key === 'avatar'" :size="60" :src="record.avatar" />
        <Tag v-if="column.key === 'no'" color="green">
          {{ record.no }}
        </Tag>
      </template>
      <template #img="{ text }">
        <TableImg :size="60" :simpleShow="true" :imgList="text" />
      </template>
      <template #imgs="{ text }"> <TableImg :size="60" :imgList="text" /> </template>

      <template #category="{ record }">
        <Tag color="green">
          {{ record.no }}
        </Tag>
      </template>
    </BasicTable>
  </div>
</template>
<script lang="ts">
  import { defineComponent } from 'vue';
  import { BasicTable, useTable, BasicColumn, TableImg } from '/@/components/Table';
  import { Tag, Avatar } from 'ant-design-vue';
  import { demoListApi } from '/@/api/demo/table';
  import { useListPage } from '/@/hooks/system/useListPage';
  const columns: BasicColumn[] = [
    {
      title: 'ID',
      dataIndex: 'id',
      slots: { customRender: 'id' },
    },
    {
      title: '头像',
      dataIndex: 'avatar',
      width: 100,
    },
    {
      title: '分类',
      dataIndex: 'category',
      width: 80,
      align: 'center',
      defaultHidden: true,
      slots: { customRender: 'category' },
    },
    {
      title: '姓名',
      dataIndex: 'name',
      width: 120,
    },
    {
      title: '图片列表1',
      dataIndex: 'imgArr',
      helpMessage: ['这是简单模式的图片列表', '只会显示一张在表格中', '但点击可预览多张图片'],
      width: 140,
      slots: { customRender: 'img' },
    },
    {
      title: '照片列表2',
      dataIndex: 'imgs',
      width: 160,
      slots: { customRender: 'imgs' },
    },
    {
      title: '地址',
      dataIndex: 'address',
    },
    {
      title: '编号',
      dataIndex: 'no',
    },
    {
      title: '开始时间',
      dataIndex: 'beginTime',
    },
    {
      title: '结束时间',
      dataIndex: 'endTime',
    },
  ];
  export default defineComponent({
    components: { BasicTable, TableImg, Tag, Avatar },
    setup() {
      const { tableContext } = useListPage({
        tableProps: {
          title: '自定义列内容',
          titleHelpMessage: '表格中所有头像、图片均为mock生成，仅用于演示图片占位',
          api: demoListApi,
          columns: columns,
          bordered: true,
          showTableSetting: false,
          showActionColumn: false,
          useSearchForm: false,
        },
      });
      //注册table数据
      const [registerTable] = tableContext;
      return {
        registerTable,
      };
    },
  });
</script>
