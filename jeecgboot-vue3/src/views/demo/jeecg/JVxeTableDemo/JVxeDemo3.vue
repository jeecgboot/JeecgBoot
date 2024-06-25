<template>
  <div>
    <ol style="border: 1px solid #cccccc; width: 600px; padding: 8px">
      <li>1. 开启 dragSort 属性之后即可实现上下拖拽排序。</li>
      <li>2. 使用 sortKey 属性可以自定义排序保存的 key，默认为 orderNum。</li>
      <li>3. 使用 sortBegin 属性可以自定义排序的起始值，默认为 0。</li>
      <li>4. sortKey 定义的字段不需要定义在 columns 中也能正常获取到值。</li>
      <li>5. 当存在 fixed 列时，拖拽排序将会失效，仅能上下排序。</li>
    </ol>

    <p> 以下示例开启了拖拽排序，排序值保存字段为 sortNum，排序起始值为 3<br /> </p>

    <JVxeTable
      ref="tableRef1"
      toolbar
      dragSort
      sortKey="sortNum"
      :sortBegin="3"
      rowSelection
      dragSortFixed="none"
      rowSelectionFixed="none"
      :maxHeight="580"
      :columns="table1.columns"
      :dataSource="table1.data"
    >
      <template #toolbarSuffix>
        <a-button @click="onGetData1">获取数据</a-button>
      </template>
    </JVxeTable>

    <br />
    <p>以下 fixed 表格不支持拖拽排序，仅支持点击上下排序</p>

    <JVxeTable ref="tableRef2" toolbar dragSort rowSelection :maxHeight="580" :columns="table2.columns" :dataSource="table2.data">
      <template #toolbarSuffix>
        <a-button @click="onGetData2">获取数据</a-button>
      </template>
    </JVxeTable>
  </div>
</template>

<script lang="ts" setup>
  import { ref, reactive } from 'vue';
  import { JVxeTypes, JVxeColumn, JVxeTableInstance } from '/@/components/jeecg/JVxeTable/types';
  import { useMessage } from '/@/hooks/web/useMessage';

  const tableRef1 = ref<JVxeTableInstance>();
  const tableRef2 = ref<JVxeTableInstance>();
  const table1 = reactive({
    columns: [
      {
        title: 'ID',
        key: 'id',
        width: 120,
        type: JVxeTypes.normal,
      },
      {
        title: '姓名',
        key: 'name',
        width: 240,
        type: JVxeTypes.input,
        defaultValue: 'new name',
      },
      {
        title: '字段长度',
        key: 'dbLength',
        width: 2400,
        type: JVxeTypes.inputNumber,
        defaultValue: 32,
      },
      {
        title: 'sortNum',
        key: 'sortNum',
        width: 120,
        type: JVxeTypes.normal,
      },
    ] as JVxeColumn[],
    data: [
      { id: 'uuid-0001', name: '张三', dbLength: 123 },
      { id: 'uuid-0002', name: '李四', dbLength: 777 },
      { id: 'uuid-0003', name: '王五', dbLength: 666 },
      { id: 'uuid-0004', name: '赵六', dbLength: 233 },
    ],
  });

  const table2 = reactive({
    columns: [
      {
        title: 'ID',
        key: 'id',
        width: 320,
        fixed: 'left',
        type: JVxeTypes.normal,
      },
      {
        title: '姓名',
        key: 'name',
        width: 720,
        type: JVxeTypes.input,
        defaultValue: 'new name',
      },
      {
        title: '字段长度',
        key: 'dbLength',
        width: 720,
        type: JVxeTypes.inputNumber,
        defaultValue: 32,
      },
    ] as JVxeColumn[],
    data: [
      { id: 'uuid-0001', name: '张三', dbLength: 123 },
      { id: 'uuid-0002', name: '李四', dbLength: 777 },
      { id: 'uuid-0003', name: '王五', dbLength: 666 },
      { id: 'uuid-0004', name: '赵六', dbLength: 233 },
    ],
  });

  const { createMessage } = useMessage();

  function onGetData1() {
    createMessage.info('请看控制台');
    console.log(tableRef1.value!.getTableData());
  }

  function onGetData2() {
    createMessage.info('请看控制台');
    console.log(tableRef2.value!.getTableData());
  }
</script>
