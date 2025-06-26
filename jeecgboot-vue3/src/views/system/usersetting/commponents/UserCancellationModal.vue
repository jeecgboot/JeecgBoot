<template>
  <BasicModal v-bind="$attrs" @register="registerModal" title="注销" @ok="handleSubmit" destroyOnClose :width="400">
    <a-form class="antd-modal-form" ref="formRef" :model="formState" :rules="validatorRules">
      <div class="cancellation-tip">
        <p style="color: red;margin-bottom: 10px !important;">注销后账号会保留10天，如需恢复请QQ管理员 </p>
      </div>
      <a-form-item label="" name="phone" class="cancellation-tip">
        <div class="black font-size-13" style="margin-bottom: 6px">验证方式</div>
        <div class="pass-padding">
          <a-input placeholder="请输入手机号" v-model:value="formState.phone" />
        </div>
      </a-form-item>
      <a-form-item label="" name="smscode" class="cancellation-tip">
        <CountdownInput v-model:value="formState.smscode" placeholder="请输入6位验证码" :sendCodeApi="sendCodeApi" />
      </a-form-item>
    </a-form>
  </BasicModal>
</template>
<script lang="ts" name="UserCancellationModal" setup>
  import { ref, unref, reactive } from 'vue';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { Rule } from '/@/components/Form/index';
  import { userLogOff } from '../UserSetting.api';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { useUserStore } from '/@/store/modules/user';
  import { CountdownInput } from '/@/components/CountDown';
  import { defHttp } from '@/utils/http/axios';

  const { createMessage, createErrorModal } = useMessage();
  //用户名
  const username = ref<string>('');
  const formRef = ref();
  const formState = reactive({
    smscode: '',
    phone: '',
  });
  // 声明Emits
  const emit = defineEmits(['success', 'register']);
  //表单赋值
  const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
    setModalProps({ confirmLoading: false });
    username.value = data.record.username;
    Object.assign(formState, { password: '', smscode: '', phone: '' });
  });
  const userStore = useUserStore();
  const validatorRules: Record<string, Rule[]> = {
    phone: [{ required: true, message: '请输入手机号' }],
    smscode: [{ required: true, message: '请输入6位验证码' }],
  };

  //表单提交事件
  async function handleSubmit() {
    try {
      let values = await formRef.value.validateFields();
      setModalProps({ confirmLoading: true });
      //提交表单
      values.username = unref(username);
      await userLogOff(values).then((res) => {
        if (res.success) {
          createMessage.info({
            content: '注销成功！',
            duration: 1,
          });
          //返回登录页面
          setTimeout(() => {
            userStore.logout(true);
          }, 1000);
          //关闭弹窗
          closeModal();
        } else {
          createMessage.warn(res.message);
        }
      });
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }

  /**
   * 倒计时执行前的函数
   */
  function sendCodeApi() {
    return new Promise((resolve, reject) => {
      if(!formState.phone){
        createErrorModal({ title: '错误提示', content: '请输入手机号！' });
        reject();
        return;
      }
      let params = { phone: formState.phone, username: username.value };
      defHttp
        .post({ url: '/sys/user/sendLogOffPhoneSms', params }, { isTransformResponse: false })
        .then((res) => {
          if (res.success) {
            resolve(true);
          } else {
            createErrorModal({ title: '错误提示', content: res.message || '未知问题' });
            reject();
          }
        })
        .catch((res) => {
          createErrorModal({ title: '错误提示', content: res.message || '未知问题' });
          reject();
        });
    });
  }
</script>
<style lang="less" scoped>
  .black {
    color: @text-color;
  }
  .font-size-13 {
    font-size: 13px;
    line-height: 15px;
  }
  .cancellation-tip {
    padding: 0 10px;
  }
</style>
