<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :width="800" title="用户代理" @ok="handleSubmit" destroyOnClose>
    <BasicForm @register="registerForm" />
    <template #insertFooter>
      <Popconfirm title="确定删除当前配置的代理吗？" @confirm="handleDel">
        <a-button v-if="agentData.id"><Icon icon="ant-design:clear-outlined" />删除代理</a-button>
      </Popconfirm>
    </template>
  </BasicModal>
</template>
<script lang="ts" setup>
  import { ref, computed, unref } from 'vue';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { BasicForm, useForm } from '/@/components/Form/index';
  import { formAgentSchema } from './user.data';
  import { deleteAgent, getUserAgent, saveOrUpdateAgent } from './user.api';
  import { Popconfirm } from 'ant-design-vue';
  // 声明Emits
  const emit = defineEmits(['success', 'register']);
  //表单配置
  const [registerForm, { resetFields, setFieldsValue, validate }] = useForm({
    schemas: formAgentSchema,
    showActionButtonGroup: false,
  });
  //表单数据
  const agentData = ref<any>({});
  //表单赋值
  const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
    //重置表单
    await resetFields();
    setModalProps({ confirmLoading: false });
    //查询获取表单数据
    const res = await getUserAgent({ userName: data.userName });
    data = res.result ? res.result : data;
    //代理数据赋值
    agentData.value = { ...data };
    //表单赋值
    await setFieldsValue({ ...data });
  });
  //表单提交事件
  async function handleSubmit() {
    try {
      const values = await validate();
      setModalProps({ confirmLoading: true });
      //提交表单
      await saveOrUpdateAgent(values);
      //关闭弹窗
      closeModal();
      //刷新列表
      emit('success');
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }

  /**
   * 删除代理
   */
  async function handleDel() {
    const reload = async () => {
      await resetFields();
      await setFieldsValue({ userName: agentData.value.userName });
      //关闭弹窗
      closeModal();
      emit('success');
    };
    if (agentData.value.id) {
      await deleteAgent({ id: agentData.value.id }, reload);
    }
  }
</script>
