<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :width="800" title="离职交接" @ok="handleSubmit">
    <BasicForm @register="registerForm" />
  </BasicModal>
</template>
<script lang="ts" setup name="user-quit-agent-modal">
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { BasicForm, useForm } from '/@/components/Form/index';
  import { formQuitAgentSchema } from './user.data';
  import { getUserAgent, userQuitAgent } from './user.api';
  import dayjs from 'dayjs';
  // 声明Emits
  const emit = defineEmits(['success', 'register']);
  //表单配置
  const [registerForm, { resetFields, setFieldsValue, validate, clearValidate, updateSchema }] = useForm({
    schemas: formQuitAgentSchema,
    showActionButtonGroup: false,
  });
  //表单赋值
  const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
    //重置表单
    await resetFields();
    setModalProps({ confirmLoading: true });
    
    let userId = data.userId;
    //查询获取表单数据
    const res = await getUserAgent({ userName: data.userName });
    data = res.result ? res.result : data;
    let date = new Date();
    if (!data.startTime) {
      data.startTime = dayjs(date).format('YYYY-MM-DD HH:mm:ss');
    }
    if (!data.endTime) {
      data.endTime = getYear(date);
    }

    //update-begin---author:wangshuai ---date:20230703  for：【QQYUN-5685】5、离职人员可以选自己------------
    await updateSchema(
      [{
        field:'agentUserName',
        componentProps:{
          excludeUserIdList:[userId]
        }
      }]
    )
    //update-end---author:wangshuai ---date:20230703  for：【QQYUN-5685】5、离职人员可以选自己------------
    //表单赋值
    await setFieldsValue({ ...data });
    setModalProps({ confirmLoading: false });
  });
  //表单提交事件
  async function handleSubmit() {
    try {
      const values = await validate();
      setModalProps({ confirmLoading: true });
      //提交表单
      await userQuitAgent(values);
      //关闭弹窗
      closeModal();
      //刷新列表
      emit('success',values.userName);
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }

  /**
   * 获取后30年
   */
  function getYear(date) {
    //update-begin---author:wangshuai ---date:20221207  for：[QQYUN-3285]交接人设置 结束时间有问题------------
    //这是一个数值
    let y = date.getFullYear() + 30;
    let m = dayjs(date).format('MM');
    let d = dayjs(date).format('DD');
    let hour = dayjs(date).format('HH:mm:ss');
    console.log('年月日', y + '-' + m + '-' + d);
    return dayjs(y + '-' + m + '-' + d + ' ' + hour).format('YYYY-MM-DD HH:mm:ss');
    //update-end---author:wangshuai ---date:20221207  for：[QQYUN-3285]交接人设置 结束时间有问题--------------
  }
</script>
