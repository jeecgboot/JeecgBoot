<!-- 操作禁用表单 -->
<template>
  <div style="margin: 20px auto; text-align: center">
    <!-- all 触发或清空所有验证，visitor 只触发或者清空来访人员验证 -->
    <a-button @click="triggerFormRule('all')" class="mr-2"> 触发表单验证 </a-button>
    <a-button @click="cancelFormRule('all')" class="mr-2"> 清空表单验证 </a-button>
    <a-button @click="triggerFormRule('visitor')" class="mr-2"> 只验证来访人员 </a-button>
    <a-button @click="cancelFormRule('visitor')" class="mr-2"> 只清空来访人员验证 </a-button>
  </div>
  <!-- 自定义表单 -->
  <BasicForm @register="registerForm" style="margin-top: 20px;" />
</template>

<script lang="ts" setup>
  //引入依赖
  import { useForm, BasicForm, FormSchema } from '/@/components/Form';

  //自定义表单字段
  const formSchemas: FormSchema[] = [
    {
      field: 'visitor',
      label: '来访人员',
      component: 'Input',
      required: true,
    },
    {
      field: 'accessed',
      label: '来访日期',
      component: 'DatePicker',
      required: true,
    },
    {
      field: 'phone',
      label: '来访人手机号',
      component: 'Input',
      required: true,
    },
  ];

  /**
   * BasicForm绑定注册;
   * clearValidate 清除所有验证，支持取消验证其中几个字段 如 clearValidate(['visitor',...])
   * validate 验证所有,支持验证其中几个字段，validate(['visitor',...])
   * validateFields 只支持验证其中几个字段，如validateFields(['visitor',...])
   */
  const [registerForm, { clearValidate, validateFields, validate }] = useForm({
    schemas: formSchemas,
    labelWidth: '150px',
    //隐藏操作按钮
    showActionButtonGroup: false,
    //默认聚焦第一个，只支持input
    autoFocusFirstItem: true,
  });

  /**
   * 触发表单验证
   * @param type all 所有验证 visitor 只验证来访人员
   */
  async function triggerFormRule(type) {
    if (type == 'all') {
      //触发所有验证
      await validate();
    } else {
      //触发来访人员验证
      //visitor 来访人员的对应formSchemas field字段
      await validateFields(['visitor']);
    }
  }

  /**
   * 触发表单验证
   * @param type all 所有验证 visitor 只验证来访人员
   */
  async function cancelFormRule(type) {
    if (type == 'all') {
      //取消全部验证
      await clearValidate();
    } else {
      //只取消来访人员的验证
      //visitor 来访人员的对应formSchemas field字段
      await clearValidate(['visitor']);
    }
  }
</script>

<style scoped>
  /** 时间和数字输入框样式 */
  :deep(.ant-input-number) {
    width: 100%;
  }

  :deep(.ant-picker) {
    width: 100%;
  }
</style>
