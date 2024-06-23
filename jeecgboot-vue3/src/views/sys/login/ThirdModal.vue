<template>
  <!-- 第三方登录绑定账号密码输入弹框 -->
  <a-modal title="请输入密码" v-model:open="thirdPasswordShow" @ok="thirdLoginCheckPassword" @cancel="thirdLoginNoPassword">
    <a-input-password placeholder="请输入密码" v-model:value="thirdLoginPassword" style="margin: 15px; width: 80%" />
  </a-modal>

  <!-- 第三方登录提示是否绑定账号弹框 -->
  <a-modal :footer="null" :closable="false" v-model:open="thirdConfirmShow" :class="'ant-modal-confirm'">
    <div class="ant-modal-confirm-body-wrapper">
      <div class="ant-modal-confirm-body">
        <QuestionCircleFilled style="color: #faad14" />
        <span class="ant-modal-confirm-title">提示</span>
        <div class="ant-modal-confirm-content"> 已有同名账号存在,请确认是否绑定该账号？ </div>
      </div>
      <div class="ant-modal-confirm-btns">
        <a-button @click="thirdLoginUserCreate" :loading="thirdCreateUserLoding">创建新账号</a-button>
        <a-button @click="thirdLoginUserBind" type="primary">确认绑定</a-button>
      </div>
    </div>
  </a-modal>

  <!-- 第三方登录绑定手机号 -->
  <a-modal title="绑定手机号" v-model:open="bindingPhoneModal" :maskClosable="false">
    <Form class="p-4 enter-x" style="margin: 15px 10px">
      <FormItem class="enter-x">
        <a-input size="large" placeholder="请输入手机号" v-model:value="thirdPhone" class="fix-auto-fill">
          <template #prefix>
            <Icon icon="ant-design:mobile-outlined" :style="{ color: 'rgba(0,0,0,.25)' }"></Icon>
          </template>
        </a-input>
      </FormItem>
      <FormItem name="sms" class="enter-x">
        <CountdownInput size="large" class="fix-auto-fill" v-model:value="thirdCaptcha" placeholder="请输入验证码" :sendCodeApi="sendCodeApi">
          <template #prefix>
            <Icon icon="ant-design:mail-outlined" :style="{ color: 'rgba(0,0,0,.25)' }"></Icon>
          </template>
        </CountdownInput>
      </FormItem>
    </Form>
    <template #footer>
      <a-button type="primary" @click="thirdHandleOk">确定</a-button>
    </template>
  </a-modal>
</template>
<script lang="ts">
  import { defineComponent } from 'vue';
  import { Form, Input } from 'ant-design-vue';
  import { CountdownInput } from '/@/components/CountDown';
  import { useThirdLogin } from '/@/hooks/system/useThirdLogin';
  import { QuestionCircleFilled } from '@ant-design/icons-vue';

  const FormItem = Form.Item;
  const InputPassword = Input.Password;

  export default defineComponent({
    name: 'ThirdModal',
    components: { FormItem, Form, InputPassword, CountdownInput, QuestionCircleFilled },
    setup() {
      return {
        ...useThirdLogin(),
      };
    },
  });
</script>
