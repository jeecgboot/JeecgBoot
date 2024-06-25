<!-- 动态增减表单 -->
<template>
  <!-- 自定义表单 -->
  <BasicForm @register="registerForm" style="margin-top: 20px;" @submit="handleSubmit">
    <!--  添加input的插槽  -->
    <template #addForm="{ field }">
      <a-button v-if="Number(field) === 0" @click="addField" style="width: 50px">+</a-button>
      <a-button v-if="Number(field) > 0" @click="delField(field)" style="width: 50px">-</a-button>
    </template>
  </BasicForm>
  <!--  <div style="margin: 20px auto; text-align: center">
    <a-button @click="addField">添加表单项</a-button>
  </div>-->
</template>

<script lang="ts" setup>
  //引入依赖
  import { useForm, BasicForm, FormSchema } from '/@/components/Form';
  import { CollapseContainer } from '/@/components/Container';
  import { ref } from 'vue';

  //自定义表单字段
  const formSchemas: FormSchema[] = [
    {
      field: 'name1',
      label: '姓名1',
      component: 'Input',
      // ifShow:false,
      colProps: {
        span: 8,
      },
    },
    {
      field: 'age1',
      label: '年龄1',
      component: 'InputNumber',
      // ifShow:false,
      colProps: {
        span: 8,
      },
    },
    {
      field: '0',
      component: 'Input',
      // ifShow:false,
      label: ' ',
      colProps: {
        span: 8,
      },
      slot: 'addForm',
    },
  ];

  /**
   * BasicForm绑定注册;
   * appendSchemaByField:增加表单项（字段）
   *
   * removeSchemaByFiled:减少表单项（字段）
   */
  const [registerForm, { appendSchemaByField, removeSchemaByFiled }] = useForm({
    schemas: formSchemas,
    showResetButton: false,
    labelWidth: '150px',
    // showSubmitButton:false
    submitButtonOptions: { text: '提交', preIcon: '' },
    actionColOptions: { span: 17 },
  });

  //组件个数
  let n = ref<number>(2);

  /**
   * 添加字段
   * appendSchemaByField类型: ( schema: FormSchema, prefixField: string | undefined, first?: boolean | undefined ) => Promise<void>
   * 说明: 插入到指定 filed 后面，如果没传指定 field，则插入到最后,当 first = true 时插入到第一个位置
   */
  async function addField() {
    //添加表单字段，里面为schemas对应的属性，可自行配置
    await appendSchemaByField(
      {
        field: `name${n.value}`,
        component: 'Input',
        label: '字段' + n.value,
        colProps: {
          span: 8,
        },
      },
      ''
    );
    await appendSchemaByField(
      {
        field: `sex${n.value}`,
        component: 'InputNumber',
        label: '字段' + n.value,
        colProps: {
          span: 8,
        },
      },
      ''
    );

    await appendSchemaByField(
      {
        field: `${n.value}`,
        component: 'Input',
        label: ' ',
        colProps: {
          span: 8,
        },
        slot: 'addForm',
      },
      ''
    );
    n.value++;
  }

  /**
   * 删除字段
   * 类型: (field: string | string[]) => Promise<void>
   * 说明: 根据 field 删除 Schema
   * @param field 当前字段名称
   */
  function delField(field) {
    //移除指定字段
    removeSchemaByFiled([`name${field}`, `sex${field}`, `${field}`]);
    n.value--;
  }

  /**
   * 点击提交按钮的value值
   * @param values
   */
  function handleSubmit(values: any) {
    console.log('提交按钮数据::::', values);
  }
</script>

<style scoped>
  /** 数字输入框样式 */
  :deep(.ant-input-number) {
    width: 100%;
  }
</style>
