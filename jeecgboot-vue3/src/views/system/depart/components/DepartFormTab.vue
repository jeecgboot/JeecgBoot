<template>
  <a-spin :spinning="loading">
    <BasicForm @register="registerForm" >
      <template #depPostParentId="{ model, field }">
        <a-tree-select v-model:value="depPostValue" :treeData="treeData" allowClear treeCheckable @select="treeSelect">
          <template #title="{ orgCategory, title }">
            <TreeIcon :orgCategory="orgCategory" :title="title"></TreeIcon>
          </template>
          <template #tagRender="{ option }">
            <span style="margin-left: 10px">{{ orgNameMap[option.id] }}</span>
          </template>
        </a-tree-select>
      </template>
    </BasicForm>
    <div class="j-box-bottom-button offset-20" style="margin-top: 30px">
      <div class="j-box-bottom-button-float" :class="[`${prefixCls}`]">
        <a-button preIcon="ant-design:sync-outlined" @click="onReset">重置</a-button>
        <a-button type="primary" preIcon="ant-design:save-filled" @click="onSubmit">保存</a-button>
      </div>
    </div>
  </a-spin>
</template>

<script lang="ts" setup>
  import { watch, computed, inject, ref, unref, onMounted } from 'vue';
  import { BasicForm, useForm } from '/@/components/Form/index';
  import { saveOrUpdateDepart } from '../depart.api';
  import { useBasicFormSchema, orgCategoryOptions, positionChange } from '../depart.data';
  import { useDesign } from '/@/hooks/web/useDesign';
  import TreeIcon from "@/components/Form/src/jeecg/components/TreeIcon/TreeIcon.vue";
  import { getDepartPathNameByOrgCode } from '@/utils/common/compUtils';

  const { prefixCls } = useDesign('j-depart-form-content');

  const emit = defineEmits(['success']);
  const props = defineProps({
    data: { type: Object, default: () => ({}) },
    rootTreeData: { type: Array, default: () => [] },
  });
  const loading = ref<boolean>(false);
  // 当前是否是更新模式
  const isUpdate = ref<boolean>(true);
  // 当前的弹窗数据
  const model = ref<object>({});
  const treeData = ref<any>([]);
  //上级岗位
  const depPostValue = ref<any>([]);
  //上级岗位名称映射
  const orgNameMap = ref<Record<string, string>>({});

  //注册表单
  const [registerForm, { resetFields, setFieldsValue, validate, updateSchema }] = useForm({
    schemas: useBasicFormSchema(treeData).basicFormSchema,
    showActionButtonGroup: false,
  });

  const categoryOptions = computed(() => {
    if (!!props?.data?.parentId) {
      return orgCategoryOptions.child;
    } else {
      return orgCategoryOptions.root;
    }
  });

  onMounted(() => {
    // 禁用字段
    updateSchema([
      { field: 'parentId', componentProps: { disabled: true } },
      { field: 'orgCode', componentProps: { disabled: true } },
    ]);
    // data 变化，重填表单
    watch(
      () => props.data,
      async () => {
        let record = unref(props.data);
        if (typeof record !== 'object') {
          record = {};
        }
        model.value = record;
        if (record.depPostParentId) {
          orgNameMap.value[record.depPostParentId] = await getDepartPathNameByOrgCode('', '', record.depPostParentId);
          depPostValue.value = [record.depPostParentId];
        }
        positionChange(record.positionId, record, treeData);
        await resetFields();
        await setFieldsValue({ ...record });
      },
      { deep: true, immediate: true }
    );
    // 更新 父部门 选项
    watch(
      () => props.rootTreeData,
      async () => {
        updateSchema([
          {
            field: 'parentId',
            componentProps: { treeData: props.rootTreeData },
          },
        ]);
      },
      { deep: true, immediate: true }
    );
    // 监听并更改 orgCategory options
    watch(
      categoryOptions,
      async () => {
        updateSchema([
          {
            field: 'orgCategory',
            componentProps: { options: categoryOptions.value },
          },
        ]);
      },
      { immediate: true }
    );
  });

  // 重置表单
  async function onReset() {
    await resetFields();
    await setFieldsValue({ ...model.value });
  }

  // 提交事件
  async function onSubmit() {
    try {
      loading.value = true;
      let values = await validate();
      values = Object.assign({}, model.value, values);
      if (depPostValue.value && depPostValue.value.length > 0) {
        values.depPostParentId = depPostValue.value[0];
      } else {
        values.depPostParentId = '';
      }
      //提交表单
      await saveOrUpdateDepart(values, isUpdate.value);
      //刷新列表
      emit('success');
      Object.assign(model.value, values);
    } finally {
      loading.value = false;
    }
  }

  /**
   * 树选中事件
   *
   * @param info
   * @param keys
   */
  async function treeSelect(keys, info) {
    if (info.checkable) {
      orgNameMap.value[info.id] = '';
      depPostValue.value = [info.value];
      orgNameMap.value[info.id] = await getDepartPathNameByOrgCode(info.orgCode, info.label, info.id);
    } else {
      depPostValue.value = [];
    }
  }
</script>
<style lang="less">

  @prefix-cls: ~'@{namespace}-j-depart-form-content';
  /*begin 兼容暗夜模式*/
  .@{prefix-cls} {
    background: @component-background;
    border-top: 1px solid @border-color-base;
  }
  /*end 兼容暗夜模式*/
</style>
<style lang="less" scoped>
  :deep(.ant-select-selector .ant-select-selection-item){
    svg{
      display: none !important;
    }
  }
</style>