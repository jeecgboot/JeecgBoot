<!-- 操作表单值 -->
<template>
  <div style="margin: 20px auto; text-align: center">
    <a-button @click="getFormValue" class="mr-2"> 获取表单值 </a-button>
    <a-button @click="clearFormValue" class="mr-2"> 清空表单值 </a-button>
    <a-button @click="updateFormValue" class="mr-2"> 更新表单值 </a-button>
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
   * getFieldsValue 获取所有表单值
   * validate 验证通过之后的表单值,支持验证其中几个字段，validate(['visitor',...])
   * setFieldsValue 更新表单值，如 setFieldsValue({'visitor':'李四',...})
   * resetFields 清除所有表单值
   */
  const [registerForm, { getFieldsValue, setFieldsValue, resetFields, validate }] = useForm({
    schemas: formSchemas,
    //隐藏操作按钮
    showActionButtonGroup: false,
    labelWidth: '150px',
    //默认聚焦第一个，只支持input
    autoFocusFirstItem: true,
  });

  /**
   * 获取表单值
   */
  async function getFormValue() {
    //获取所有值
    let fieldsValue = await getFieldsValue();
    console.log('fieldsValue:::', fieldsValue);
    //表单验证通过后获取所有字段值
    fieldsValue = await validate();
    console.log('fieldsValue:::', fieldsValue);
    //表单验`visitor来访人员`通过后获取的值
    fieldsValue = await validate(['visitor']);
    console.log('fieldsValue:::', fieldsValue);
  }

  /**
   * 清空表单值
   */
  async function clearFormValue() {
    await resetFields();
  }

  /**
   * 更新表单值
   */
  async function updateFormValue() {
    console.log('我进来了');
    await setFieldsValue({ visitor: '李四' });
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
