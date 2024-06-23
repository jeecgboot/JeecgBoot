<!-- 自定义页脚 -->
<template>
  <!-- 自定义表单 -->
  <BasicForm @register="registerForm" style="margin-top: 20px">
    <template #formHeader>
      <div style="margin: 0 auto 20px">
        <span>我是自定义按钮</span>
      </div>
    </template>
    <template #formFooter>
      <div style="margin: 0 auto">
        <a-button type="primary" @click="save" class="mr-2"> 保存 </a-button>
        <a-button type="primary" @click="saveDraft" class="mr-2"> 保存草稿 </a-button>
        <a-button type="error" @click="reset" class="mr-2"> 重置 </a-button>
      </div>
    </template>
  </BasicForm>
</template>

<script lang="ts" setup>
  //引入依赖
  import { useForm, BasicForm, FormSchema } from '/@/components/Form';

  //自定义表单字段
  const formSchemas: FormSchema[] = [
    {
      label: '员工姓名',
      field: 'name',
      component: 'Input',
    },
    {
      label: '性别',
      field: 'sex',
      component: 'Select',
      //填写组件的属性
      componentProps: {
        options: [
          { label: '男', value: 1 },
          { label: '女', value: 2 },
          { label: '未知', value: 3 },
        ],
      },
      //默认值
      defaultValue: 3,
    },
    {
      label: '年龄',
      field: 'age',
      component: 'Input',
    },
    {
      label: '入职时间',
      subLabel: '( 选填 )',
      field: 'entryTime',
      component: 'TimePicker',
    },
  ];

  /**
   * BasicForm绑定注册;
   */
  const [registerForm, { validate, resetFields }] = useForm({
    schemas: formSchemas,
    labelWidth: '150px',
    //隐藏操作按钮
    showActionButtonGroup: false,
  });

  /**
   * 保存
   */
  async function save() {
    //使用useForm方法获取表单值
    let values = await validate();
    console.log(values);
  }

  /**
   * 保存草稿
   */
  async function saveDraft() {
    //使用useForm方法validate获取表单值
    let values = await validate();
    console.log(values);
  }

  /**
   * 重置
   */
  async function reset() {
    //使用useForm方法resetFields清空值
    await resetFields();
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
