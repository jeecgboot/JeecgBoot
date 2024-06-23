<!-- 操作表单schemas配置 -->
<template>
  <div style="margin: 20px auto; text-align: center">
    <a-button @click="updateFormSchemas" class="mr-2"> 更新字段属性 </a-button>
    <a-button @click="resetFormSchemas" class="mr-2"> 重置字段属性 </a-button>
  </div>
  <!-- 自定义表单 -->
  <BasicForm @register="registerForm" style="margin-top: 20px" />
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
      componentProps: {
        disabled: true,
      },
    },
    {
      field: 'accessed',
      label: '来访日期',
      component: 'DatePicker',
      ifShow: false,
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
   * updateSchema 更新字段属性，支持schemas里面所有的配置
   * updateSchema([{ field: 'visitor', componentProps: { disabled: false },}, ... ]);
   * resetSchema 重置字段属性，支持schemas里面所有的配置
   * resetSchema([{ field: 'visitor',label: '来访人员',component: 'Input',},... ]);
   */
  const [registerForm, { updateSchema, resetSchema }] = useForm({
    schemas: formSchemas,
    //隐藏操作按钮
    showActionButtonGroup: false,
    labelWidth: '150px',
    //默认聚焦第一个，只支持input
    autoFocusFirstItem: true,
  });

  /**
   * 清除表单配置
   */
  async function resetFormSchemas() {
    await resetSchema([
      {
        //字段必填
        field: 'visitor',
        label: '来访人员',
        component: 'Input',
      },
    ]);
  }

  /**
   * 更新表单配置
   */
  async function updateFormSchemas() {
    //支持更新schemas里面所有的配置
    await updateSchema([
      {
        //字段必填
        field: 'visitor',
        componentProps: {
          disabled: false,
        },
      },
      {
        field: 'accessed',
        ifShow: true,
      },
    ]);
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
