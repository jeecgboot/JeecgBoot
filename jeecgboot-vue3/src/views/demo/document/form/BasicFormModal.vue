<!-- 弹出层表单 -->
<template>
  <div style="margin: 20px auto">
    <a-button type="primary" @click="openPopup" class="mr-2"> 打开弹窗 </a-button>
  </div>
  <!-- 自定义弹窗组件 -->
  <BasicModal @register="registerModal" title="弹出层表单">
    <!-- 自定义表单 -->
    <BasicForm @register="registerForm" />
  </BasicModal>
</template>

<script lang="ts" setup>
  //引入依赖
  import { useForm, BasicForm, FormSchema } from '/@/components/Form';
  import { BasicModal } from '/@/components/Modal';
  import { useModal } from '/@/components/Modal';

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

  //BasicModal绑定注册;
  const [registerModal, { openModal }] = useModal();

  /**
   * BasicForm绑定注册;
   */
  const [registerForm, { validate, resetFields }] = useForm({
    schemas: formSchemas,
    //隐藏操作按钮
    showActionButtonGroup: false,
  });

  /**
   * 打开弹窗
   */
  async function openPopup() {
    //详见 BasicModal模块
    openModal(true, {});
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
