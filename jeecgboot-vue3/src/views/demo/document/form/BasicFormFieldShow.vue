<!-- 字段显示与隐藏 -->
<template>
  <!-- 自定义表单 -->
  <BasicForm @register="registerForm" style="margin-top: 20px" />
</template>

<script lang="ts" setup>
  //引入依赖
  import { useForm, BasicForm, FormSchema } from '/@/components/Form';

  //自定义表单字段
  const formSchemas: FormSchema[] = [
    {
      field: 'id',
      label: '编号',
      component: 'Input',
      //隐藏id，css 控制，不会删除 dom（支持布尔类型 true和false。支持动态值判断，详情请见ifShow）
      show: false,
    },
    {
      field: 'evaluate',
      label: '对公司整体评价',
      component: 'RadioGroup',
      componentProps: {
        options: [
          { label: '满意', value: '0' },
          { label: '不满意', value: '1' },
        ],
      },
      defaultValue: '0',
    },
    {
      field: 'describe',
      label: '不满意原因说明',
      component: 'InputTextArea',
      //ifShow和show属性一致，使用其中一个即可，values代表当前表单的值，js 控制，会删除 dom
      ifShow: ({ values }) => {
        return values.evaluate == '1';
      },
    },
    {
      field: 'satisfiedLevel',
      label: '满意度',
      component: 'Slider',
      componentProps: {
        tipFormatter: (value) => {
          return value + '%';
        },
      },
      //动态禁用，values代表当前表单的值，返回 true或false
      dynamicDisabled: ({ values }) => {
        return values.evaluate == '1';
      },
    },
  ];

  /**
   * BasicForm绑定注册;
   */
  const [registerForm] = useForm({
    //注册表单列
    schemas: formSchemas,
    showResetButton: false,
    labelWidth: '150px',
    submitButtonOptions: { text: '提交', preIcon: '' },
    actionColOptions: { span: 17 },
  });
</script>

<style scoped></style>
