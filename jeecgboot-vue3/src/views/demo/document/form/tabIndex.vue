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
    BasicFunctionForm,
    BasicFormConAttribute,
    BasicFormFieldShow,
    BasicFormFieldTip,
    BasicFormRules,
    BasicFormDynamicsRules,
    BasicFormSlots,
    BasicFormCustomSlots,
    BasicFormRander,
    BasicFixedWidthForm,
    BasicFiledsLayotForm,
    BasicFormLayout,
    BasicFormBtn,
    BasicFormCompact,
    BasicFormCleanRule,
    BasicFormValue,
    BasicFormSchemas,
    BasicFormAdd,
    BasicFormFooter,
    BasicFormModal,
    BasicFormCustom,
    BasicFormSearch,
    BasicFormComponent,
    BasicFormCustomComponent,
  } from './index';
  export default defineComponent({
    name: 'document-table-demo',
    components: {
      BasicFunctionForm,
      BasicFormConAttribute,
      BasicFormFieldShow,
      BasicFormFieldTip,
      BasicFormRules,
      BasicFormDynamicsRules,
      BasicFormSlots,
      BasicFormCustomSlots,
      BasicFormRander,
      BasicFixedWidthForm,
      BasicFiledsLayotForm,
      BasicFormLayout,
      BasicFormBtn,
      BasicFormCompact,
      BasicFormCleanRule,
      BasicFormValue,
      BasicFormSchemas,
      BasicFormAdd,
      BasicFormFooter,
      BasicFormModal,
      BasicFormCustom,
      BasicFormSearch,
      BasicFormComponent,
      BasicFormCustomComponent,
    },
    setup() {
      //当前选中key
      const activeKey = ref('BasicFunctionForm');
      //组件集合
      const compList = ref([
        { key: 'BasicFunctionForm', label: '基础表单' },
        { key: 'BasicFormConAttribute', label: '字段控件属性' },
        { key: 'BasicFormComponent', label: 'Ant Design Vue自带控件' },
        { key: 'BasicFormCustomComponent', label: 'JEECG封装的控件' },
        { key: 'BasicFormFieldShow', label: '字段显示和隐藏' },
        { key: 'BasicFormFieldTip', label: '字段标题提示' },
        { key: 'BasicFormRules', label: '表单检验' },
        { key: 'BasicFormDynamicsRules', label: '自定义动态检验' },
        { key: 'BasicFormSlots', label: '字段插槽' },
        { key: 'BasicFormCustomSlots', label: '自定义组件(插槽)' },
        { key: 'BasicFormCustom', label: '自定义组件(component)' },
        { key: 'BasicFormRander', label: '自定义渲染' },
        { key: 'BasicFixedWidthForm', label: '固定label宽度' },
        { key: 'BasicFiledsLayotForm', label: '标题与字段布局' },
        { key: 'BasicFormLayout', label: '表单布局' },
        { key: 'BasicFormBtn', label: '操作按钮示例' },
        { key: 'BasicFormCompact', label: '表单紧凑' },
        { key: 'BasicFormCleanRule', label: '表单检验配置' },
        { key: 'BasicFormValue', label: '获取value值' },
        { key: 'BasicFormSchemas', label: '更新schemas表单配置' },
        { key: 'BasicFormAdd', label: '动态增减表单' },
        { key: 'BasicFormFooter', label: '自定义页脚' },
        { key: 'BasicFormModal', label: '弹出层表单' },
        { key: 'BasicFormSearch', label: '查询区域' },
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
