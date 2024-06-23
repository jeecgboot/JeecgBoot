<template>
  <div style="padding: 5px">
    <vxe-toolbar>
      <template #buttons>
        <vxe-button @click="allAlign = 'left'">新增</vxe-button>
      </template>
    </vxe-toolbar>

    <vxe-table :align="allAlign" :data="tableData1">
      <vxe-table-column type="seq" width="60"></vxe-table-column>
      <vxe-table-column field="name" title="Name"></vxe-table-column>
      <vxe-table-column field="sex" title="Sex"></vxe-table-column>
      <vxe-table-column field="age" title="Age"></vxe-table-column>
    </vxe-table>
  </div>
</template>
<script lang="ts">
  import { defineComponent, ref } from 'vue';
  import { CollapseContainer } from '/@/components/Container/index';

  export default defineComponent({
    components: { CollapseContainer },
    setup() {
      const allAlign = ref(null);
      const tableData1 = ref([
        { id: 10001, name: 'Test1', role: 'Develop', sex: 'Man', age: 28, address: 'vxe-table 从入门到放弃' },
        { id: 10002, name: 'Test2', role: 'Test', sex: 'Women', age: 22, address: 'Guangzhou' },
        { id: 10003, name: 'Test3', role: 'PM', sex: 'Man', age: 32, address: 'Shanghai' },
        { id: 10004, name: 'Test4', role: 'Designer', sex: 'Women', age: 24, address: 'Shanghai' },
      ]);
      return {
        allAlign,
        tableData1,
      };
    },
  });
</script>

<style lang="css" scoped></style>
