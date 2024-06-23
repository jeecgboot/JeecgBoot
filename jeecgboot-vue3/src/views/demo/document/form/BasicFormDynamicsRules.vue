<!-- 动态表单验证 -->
<template>
  <!-- 自定义表单 -->
  <BasicForm @register="registerForm" style="margin-top: 20px" @submit="handleSubmit" />
</template>

<script lang="ts" setup>
  //引入依赖
  import { useForm, BasicForm, FormSchema } from '/@/components/Form';
  import { duplicateCheck } from '/@/views/system/user/user.api';

  //自定义表单字段
  const formSchemas: FormSchema[] = [
    {
      field: 'visitor',
      label: '来访人员',
      component: 'Input',
      //自动触发检验，布尔类型
      required: true,
    },
    {
      field: 'accessed',
      label: '来访日期',
      component: 'DatePicker',
      //支持获取当前值判断触发 values代表当前表单的值
      required: ({ values }) => {
        return !values.accessed;
      },
    },
    {
      field: 'phone',
      label: '来访人手机号',
      component: 'Input',
      //动态自定义正则，values: 当前表单的所有值
      dynamicRules: ({ values }) => {
        //需要return
        return [
          {
            //默认开启表单检验
            required: true,
            // value 当前手机号输入的值
            validator: (_, value) => {
              //需要return 一个Promise对象
              return new Promise((resolve, reject) => {
                if (!value) {
                  reject('请输入手机号！');
                }
                //验证手机号是否正确
                let reg = /^1[3456789]\d{9}$/;
                if (!reg.test(value)) {
                  reject('请输入正确手机号！');
                }
                resolve();
              });
            },
          },
        ];
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

  /**
   * 提交事件
   */
  function handleSubmit(values: any) {}
</script>

<style scoped></style>
