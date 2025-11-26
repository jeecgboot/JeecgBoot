<template>
  <BasicModal
    v-bind="$attrs"
    @register="registerModal"
    :title="getTitle"
    :width="500"
    @ok="handleSubmit"
    :keyboard="false"
    :maskClosable="false"
    :closable="false"
    :bodyStyle="{ padding: '10px 20px 20px 20px' }"
    :ok-button-props="{ loading: confirmLoading }"
    :cancel-button-props="{ style: { display: 'none' } }"
    destroyOnClose
  >
    <div>
      <span class="pwd-topbar">您当前密码和系统密码一致，请修改密码！</span>
      <BasicForm @register="registerForm" />
    </div>
  </BasicModal>
</template>

<script lang="ts" setup>
  import { ref, computed, } from 'vue';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { BasicForm, useForm } from '/@/components/Form';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { rules } from '@/utils/helper/validator';
  import { defHttp } from '@/utils/http/axios';
  import { useUserStore } from '@/store/modules/user';

  const emit = defineEmits(['success', 'register']);

  const { t } = useI18n();
  const { createMessage } = useMessage();
  const confirmLoading = ref(false);
  const oldPassword = ref('');

  // 判断密码是否为连续的数字或字母
  function lxStr (password) {
    let arr = password.split('');
    let flag = true;
    for (let i = 1; i < arr.length - 1; i++) {
      let firstIndex = arr[i - 1].charCodeAt();
      let secondIndex = arr[i].charCodeAt();
      let thirdIndex = arr[i + 1].charCodeAt();
      thirdIndex - secondIndex == 1;
      secondIndex - firstIndex == 1;
      if ((thirdIndex - secondIndex === 1) && (secondIndex - firstIndex === 1)) {
        flag = false;
      }
    }
    if (!flag) {
      return flag
    }
    return flag
  }
  
  // 表单配置
  const [registerForm, { resetFields, setFieldsValue, validate }] = useForm({
    labelWidth: 100,
    schemas: [
      {
        label: t('layout.changePassword.newPassword'),
        field: 'password',
        component: 'StrengthMeter',
        componentProps: {
          placeholder: t('layout.changePassword.pleaseEnterNewPassword'),
        },
        rules: [
          {
            required: true,
            message: t('layout.changePassword.pleaseEnterNewPassword'),
          },
          {
            validator:(_, value)=>{
              if(!value){
                return Promise.resolve();
              }
              //不能使用系统密码作为新密码
              if(value === oldPassword.value){
                return Promise.reject('不能使用系统密码作为新密码!');
              }
              //密码由8位数字、大小写字母和特殊符号组成
              let reg = /^(?=.*[a-zA-Z])(?=.*\d)(?=.*[~!@#$%^&*()_+`\-={}:";'<>?,./]).{8,}$/;
              if(!reg.test(value)){
                return Promise.reject('密码由 8 位及以上数字、大小写字母和特殊符号组成！');
              }
              return Promise.resolve();
            }
          },
          /* {
             // 使用一个函数来自定义验证逻辑，客户使用这个
             validator: (_, value) => {
               // 如果密码为空，直接返回 resolve() 让 required 规则去处理
               if (!value) {
                 return Promise.resolve();
               }
               //不能使用系统密码作为新密码
               if(value === oldPassword.value){
                 return Promise.reject('不能使用系统密码作为新密码!');
               }
               // 1. 长度不低于8位
               if (value.length < 8) {
                 return Promise.reject('密码长度不能少于8位');
               }
               // 2. 必须包含数字、字母大小写、特殊字符
               // 使用正则表达式进行匹配
               const hasNumber = /[0-9]/.test(value);
               const hasLowercase = /[a-z]/.test(value);
               const hasUppercase = /[A-Z]/.test(value);
               const hasSpecialChar = /[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?~`]/.test(value);
               if (!(hasNumber && hasLowercase && hasUppercase && hasSpecialChar)) {
                 return Promise.reject('密码必须包含数字、大小写字母和特殊字符');
               }
               if(!lxStr(value)){
                 return Promise.reject('密码不能出现3位及3为以上的数字或字母');
               }
               //不能相同字符（如111、aaa）连续3位或3位以上
               const reg2 = /(\w)*(\w)\2{2}(\w)*!/g
               if (reg2.test(value)) {
                 console.log('密码不能出现相同字符连续3位或3位以上')
                 return false
               }
               // 如果所有规则都通过，返回 resolve()
               return Promise.resolve();
             }
           }*/
        ],
      },
      {
        label: t('layout.changePassword.confirmNewPassword'),
        field: 'confirmpassword',
        component: 'InputPassword',
        dynamicRules: ({ values }) => rules.confirmPassword(values, true),
      },
    ],
    showActionButtonGroup: false,
    actionColOptions: {
      span: 24,
    },
  });

  const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
    resetFields();
    setModalProps({ confirmLoading: false });
    oldPassword.value = decodeURIComponent(data.oldPassword)
  });

  const getTitle = computed(() => '修改密码');
  const userStore = useUserStore();
  const { createMessage: $message } = useMessage();

  
  
  // 提交处理
  async function handleSubmit() {
    try {
      const values = await validate();
      setModalProps({ confirmLoading: true });
      //提交表单
      let params = Object.assign({ username: userStore.getUserInfo.username, oldpassword: oldPassword.value }, values);
      defHttp.put({ url: '/sys/user/updatePassword', params }, { isTransformResponse: false }).then((res) => {
        if (res.success) {
            $message.info({
              content:'密码修改成功，请重新登录！2s后自动退出登录',
              duration: 2
            })
            //3s后返回登录页面
            setTimeout(()=>{
              userStore.logout(true);
            },2000)
            //关闭弹窗
            closeModal();
          //关闭弹窗
          closeModal();
        } else {
          $message.warning(res.message);
        }
      });
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }
</script>

<style lang="less" scoped>
  :deep(.ant-form-item) {
    margin-bottom: 20px;
  }
  .pwd-topbar{
    width: 400px;
    display: flex;
    align-items: center;
    gap: 8px;
    padding: 8px 12px;
    background: linear-gradient(90deg, #fff7e6, #fff1b8);
    border: 1px solid #ffd591;
    border-left: 0;
    border-right: 0;
    color: #ad6800;
    position: relative;
    margin-bottom: 10px;
    left: 36px;
  }
</style>
