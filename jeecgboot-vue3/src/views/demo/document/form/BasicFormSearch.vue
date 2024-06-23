<!-- 查询区域 -->
<template>
  <!-- 自定义表单 -->
  <BasicForm @register="registerForm" @submit="handleSubmit" style="margin-top: 20px" />
</template>

<script lang="ts" setup>
  //引入依赖
  import { useForm, BasicForm, FormSchema } from '/@/components/Form';

  //自定义表单字段
  const formSchemas: FormSchema[] = [
    {
      field: 'name',
      label: '姓名',
      component: 'Input',
    },
    {
      field: 'hobby',
      label: '爱好',
      component: 'Input',
    },
    {
      field: 'birthday',
      label: '生日',
      component: 'DatePicker',
    },
    {
      field: 'joinTime',
      label: '入职时间',
      component: 'RangePicker',
      componentProps: {
        valueType: 'Date',
      },
    },
    {
      field: 'workYears',
      label: '工作年份',
      component: 'JRangeNumber',
    },
    {
      field: 'sex',
      label: '性别',
      component: 'Select',
      componentProps: {
        options: [
          {
            label: '男',
            value: '1',
          },
          {
            label: '女',
            value: '2',
          },
        ],
      },
    },
    {
      field: 'marital',
      label: '婚姻状况',
      component: 'RadioGroup',
      componentProps: {
        options: [
          {
            label: '未婚',
            value: '1',
          },
          {
            label: '已婚',
            value: '2',
          },
        ],
      },
    },
  ];

  /**
   * BasicForm绑定注册;
   */
  const [registerForm] = useForm({
    //将表单内时间区域的值映射成 2个字段, 'YYYY-MM-DD'日期格式化
    fieldMapToTime: [['joinTime', ['joinTime_begin', 'joinTime_end'], 'YYYY-MM-DD']],
    //注册表单列
    schemas: formSchemas,
    //是否显示展开收起按钮，默认false
    showAdvancedButton: true,
    //超过指定行数折叠，默认3行
    autoAdvancedCol: 3,
    //折叠时默认显示行数，默认1行
    alwaysShowLines: 2,

    //将表单内数值类型区域的值映射成 2个字段
    fieldMapToNumber: [['workYears', ['workYears_begin', 'workYears_end']]],
    //每列占比，默认一行为24
    baseColProps: { span: 12 },
  });

  /**
   * 点击提交按钮的value值
   * @param values
   */
  function handleSubmit(values: any) {
    console.log('提交按钮数据::::', values);
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
