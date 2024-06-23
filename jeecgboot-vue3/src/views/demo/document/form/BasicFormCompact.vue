<!-- 操作禁用表单 -->
<template>
  <div style="margin: 20px auto; text-align: center">
    <!-- 通过setProps 可以设置 userForm 中的属性 -->
    <!-- 表单大小，默认为 default   -->
    <a-button @click="setProps({ size: 'large' })" class="mr-2"> 更改大小为最大 </a-button>
    <a-button @click="setProps({ size: 'default' })" class="mr-2"> 还原大小 </a-button>
    <a-button @click="setProps({ size: 'small' })" class="mr-2"> 更改大小为最小 </a-button>
    <!--  disabled表单禁用  -->
    <a-button @click="setProps({ disabled: true })" class="mr-2"> 禁用表单 </a-button>
    <a-button @click="setProps({ disabled: false })" class="mr-2"> 解除禁用 </a-button>
    <!--  compact 是否为紧凑表单  -->
    <a-button @click="setProps({ compact: true })" class="mr-2"> 紧凑表单 </a-button>
    <a-button @click="setProps({ compact: false })" class="mr-2"> 还原正常间距 </a-button>
  </div>
  <!-- 自定义表单 -->
  <BasicForm @register="registerForm" style="margin-top: 20px;" />
</template>

<script lang="ts" setup>
  //引入依赖
  import { useForm, BasicForm, FormSchema } from '/@/components/Form';
  import { CollapseContainer } from '/@/components/Container';

  //自定义表单字段
  const formSchemas: FormSchema[] = [
    {
      field: 'visitor',
      label: '来访人员',
      component: 'Input',
    },
    {
      field: 'accessed',
      label: '来访日期',
      component: 'DatePicker',
    },
    {
      field: 'phone',
      label: '来访人手机号',
      component: 'Input',
    },
  ];

  /**
   * BasicForm绑定注册;
   * setProps方法可以动态设置useForm中的属性
   */
  const [registerForm, { setProps }] = useForm({
    schemas: formSchemas,
    labelWidth: '150px',
    //隐藏操作按钮
    showActionButtonGroup: false,
    //默认聚焦第一个，只支持input
    autoFocusFirstItem: true,
  });
</script>

<style scoped></style>
