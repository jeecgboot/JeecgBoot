<!-- 操作按钮 -->
<template>
  <div style="margin: 20px auto; text-align: center">
    <!-- 通过setProps 可以设置 userForm 中的属性 -->
    <!--  showActionButtonGroup 显示或者隐藏查询、重置按钮  -->
    <a-button @click="setProps({ showActionButtonGroup: false })" class="mr-2"> 隐藏操作按钮 </a-button>
    <a-button @click="setProps({ showActionButtonGroup: true })" class="mr-2"> 显示操作按钮 </a-button>
    <!--  showActionButtonGroup 显示或者隐藏重置按钮  -->
    <a-button @click="setProps({ showResetButton: false })" class="mr-2"> 隐藏重置按钮 </a-button>
    <a-button @click="setProps({ showResetButton: true })" class="mr-2"> 显示重置按钮 </a-button>
    <!--  showActionButtonGroup 显示或者隐藏查询按钮  -->
    <a-button @click="setProps({ showSubmitButton: false })" class="mr-2"> 隐藏查询按钮 </a-button>
    <a-button @click="setProps({ showSubmitButton: true })" class="mr-2"> 显示查询按钮 </a-button>
  </div>
  <!-- 自定义表单 -->
  <BasicForm @register="registerForm" @submit="handleSubmit" style="margin-top: 50px; margin-left: 50px" />
</template>

<script lang="ts" setup>
  //引入依赖
  import { useForm, BasicForm, FormSchema } from '/@/components/Form';
  import { CollapseContainer } from '/@/components/Container';

  /**
   * BasicForm绑定注册;
   * setProps方法可以动态设置useForm中的属性
   */
  const [registerForm, { setProps }] = useForm({
    //自定义查询按钮的文本和图标
    submitButtonOptions: { text: '查询', preIcon: '' },
    //自定义重置按钮的文本和图标
    resetButtonOptions: { text: '重置', preIcon: '' },
    //操作按钮的位置
    actionColOptions: { span: 17 },
    //提交按钮的自定义事件
    submitFunc: customSubmitFunc,
    //重置按钮的自定义时间
    resetFunc: customSubmitFunc,
    //显示操作按钮
    showActionButtonGroup: true,
  });

  /**
   * 查询按钮点击事件
   */
  async function customSubmitFunc() {
    console.log('查询按钮点击事件，此处处理查询按钮的逻辑');
  }

  /**
   * 重置按钮点击事件
   */
  async function customResetFunc() {
    console.log('重置按钮点击事件，此处处理重置按钮的逻辑');
  }

  /**
   * 点击提交按钮的value值
   * @param values
   */
  function handleSubmit(values: any) {
    console.log('提交按钮数据::::', values);
  }
</script>

<style scoped></style>
