<template>
  <Result status="success" title="更改密码成功" :sub-title="getSubTitle">
    <template #extra>
      <a-button key="console" type="primary" @click="finish"> 返回登录 </a-button>
    </template>
  </Result>
</template>
<script lang="ts">
  import { defineComponent, ref, computed, unref, onMounted, watchEffect, watch } from 'vue';
  import { Form, Input, Button, Result } from 'ant-design-vue';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { useLoginState } from '../login/useLogin';
  import { useCountdown } from '/@/components/CountDown/src/useCountdown';
  import { propTypes } from '/@/utils/propTypes';

  export default defineComponent({
    name: 'step3',
    components: {
      Button,
      Form,
      FormItem: Form.Item,
      Input,
      Result,
    },
    props: {
      accountInfo: {
        type: Object,
        default: () => ({}),
      },
      count: propTypes.number.def(5),
    },
    emits: ['finish'],
    setup(props, { emit }) {
      const { t } = useI18n();
      const { accountInfo } = props;
      const { handleBackLogin } = useLoginState();

      const { currentCount, start } = useCountdown(props.count);
      const getSubTitle = computed(() => {
        return t('sys.login.subTitleText', [unref(currentCount)]);
      });
      /**
       * 倒计时
       */
      watchEffect(() => {
        if (unref(currentCount) === 1) {
          setTimeout(() => {
            finish();
          }, 500);
        }
      });

      /**
       * 结束回调
       */
      function finish() {
        handleBackLogin();
        emit('finish');
      }

      onMounted(() => {
        start();
      });

      return {
        getSubTitle,
        finish,
      };
    },
  });
</script>
