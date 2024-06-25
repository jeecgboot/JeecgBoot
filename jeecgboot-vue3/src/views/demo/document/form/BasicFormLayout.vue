<!-- 表单布局 -->
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
      label: '会议名称',
      field: 'name',
      component: 'Input',
    },
    {
      label: '参会地点',
      field: 'meetingLocation',
      component: 'Input',
    },
    {
      label: '参与人数',
      field: 'numberOfPart',
      component: 'InputNumber',
    },
    {
      label: '会议纪要',
      field: 'meetingMinutes',
      component: 'JUpload',
    },
  ];

  /**
   * BasicForm绑定注册;
   */
  const [registerForm] = useForm({
    //表单布局属性，支持（vertical,inline），默认为inline
    layout: 'inline',
    //注册表单列
    schemas: formSchemas,
    //不显示查询和重置按钮
    showActionButtonGroup: false,
    //默认row行配置,当 layout 为 inline 生效
    rowProps: { gutter: 24, justify: 'center', align: 'middle' },
    //全局col列占比(每列显示多少位)，和schemas中的colProps属性一致
    baseColProps: { span: 12 },
    //row行的样式
    baseRowStyle: { width: '100%' },
  });
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
