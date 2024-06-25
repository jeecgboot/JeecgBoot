<template>
  <div class="p-4">
    <a-card :bordered="false" style="height: 100%">
      <a-tabs v-model:activeKey="activeKey" @change="tabChange">
        <a-tab-pane :key="item.key" :tab="item.label" v-for="item in compList" />
      </a-tabs>
      <component :is="currentComponent" />
    </a-card>
  </div>
</template>
<script lang="ts">
  import { defineComponent, ref, computed } from 'vue';
  import {
    AuthColumnDemo,
    BasicTableBorder,
    BasicTableDemo,
    BasicTableDemoAjax,
    CustomerCellDemo,
    EditCellTableDemo,
    EditRowTableDemo,
    ExpandTableDemo,
    ExportTableDemo,
    FixedHeaderColumn,
    InnerTableDemo,
    MergeHeaderDemo,
    MergeTableDemo,
    SelectTableDemo,
    TreeTableDemo,
  } from './index';
  export default defineComponent({
    name: 'document-table-demo',
    components: {
      AuthColumnDemo,
      BasicTableBorder,
      BasicTableDemo,
      BasicTableDemoAjax,
      CustomerCellDemo,
      EditCellTableDemo,
      EditRowTableDemo,
      ExpandTableDemo,
      ExportTableDemo,
      FixedHeaderColumn,
      InnerTableDemo,
      MergeHeaderDemo,
      MergeTableDemo,
      SelectTableDemo,
      TreeTableDemo,
    },
    setup() {
      //当前选中key
      const activeKey = ref('BasicTableDemo');
      //组件集合
      const compList = ref([
        { key: 'BasicTableDemo', label: '基础静态表格' },
        { key: 'BasicTableDemoAjax', label: '常规AJAX表格' },
        { key: 'BasicTableBorder', label: '边框表格' },
        { key: 'CustomerCellDemo', label: '自定义列内容' },
        { key: 'EditCellTableDemo', label: '可编辑单元格' },
        { key: 'EditRowTableDemo', label: '可编辑行' },
        { key: 'ExpandTableDemo', label: '可展开表格' },
        { key: 'ExportTableDemo', label: '导入导出' },
        { key: 'FixedHeaderColumn', label: '固定头和列示例' },
        { key: 'InnerTableDemo', label: '内嵌表格' },
        { key: 'MergeHeaderDemo', label: '分组表头示例' },
        { key: 'MergeTableDemo', label: '合并行列' },
        { key: 'SelectTableDemo', label: '可选择表格' },
        { key: 'TreeTableDemo', label: '树形表格' },
        { key: 'AuthColumnDemo', label: '权限列设置' },
      ]);
      //当前选中组件
      const currentComponent = computed(() => {
        return activeKey.value;
      });

      //使用component动态切换tab
      function tabChange(key) {
        activeKey.value = key;
      }
      return {
        activeKey,
        currentComponent,
        tabChange,
        compList,
      };
    },
  });
</script>
