<template>
  <a-spin :spinning="loading">
    <BasicForm @register="registerForm" />
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
  import { useBasicFormSchema, orgCategoryOptions } from '../depart.data';
  import { useDesign } from '/@/hooks/web/useDesign';

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

  //注册表单
  const [registerForm, { resetFields, setFieldsValue, validate, updateSchema }] = useForm({
    schemas: useBasicFormSchema().basicFormSchema,
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
      //提交表单
      await saveOrUpdateDepart(values, isUpdate.value);
      //刷新列表
      emit('success');
      Object.assign(model.value, values);
    } finally {
      loading.value = false;
    }
  }
</script>
<style lang="less">
  // update-begin-author:liusq date:20230625 for: [issues/563]暗色主题部分失效

  @prefix-cls: ~'@{namespace}-j-depart-form-content';
  /*begin 兼容暗夜模式*/
  .@{prefix-cls} {
    background: @component-background;
    border-top: 1px solid @border-color-base;
  }
  /*end 兼容暗夜模式*/
  // update-end-author:liusq date:20230625 for: [issues/563]暗色主题部分失效
</style>
