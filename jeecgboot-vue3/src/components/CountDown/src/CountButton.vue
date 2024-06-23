<template>
  <Button v-bind="$attrs" :disabled="isStart" @click="handleStart" :loading="loading">
    {{ getButtonText }}
  </Button>
  <!-- 图片验证码弹窗 -->
  <CaptchaModal @register="captchaRegisterModal" @ok="handleStart" />
</template>
<script lang="ts">
  import { defineComponent, ref, watchEffect, computed, unref } from 'vue';
  import { Button } from 'ant-design-vue';
  import { useCountdown } from './useCountdown';
  import { isFunction } from '/@/utils/is';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { useModal } from "@/components/Modal";
  import { createAsyncComponent } from "@/utils/factory/createAsyncComponent";
  import { ExceptionEnum } from "@/enums/exceptionEnum";
  const CaptchaModal = createAsyncComponent(() => import('/@/components/jeecg/captcha/CaptchaModal.vue'));
  const [captchaRegisterModal, { openModal: openCaptchaModal }] = useModal();

  const props = {
    value: { type: [Object, Number, String, Array] },
    count: { type: Number, default: 60 },
    beforeStartFunc: {
      type: Function as PropType<() => Promise<boolean>>,
      default: null,
    },
  };

  export default defineComponent({
    name: 'CountButton',
    components: { Button, CaptchaModal },
    props,
    setup(props) {
      const loading = ref(false);

      const { currentCount, isStart, start, reset } = useCountdown(props.count);
      const { t } = useI18n();

      const getButtonText = computed(() => {
        return !unref(isStart) ? t('component.countdown.normalText') : t('component.countdown.sendText', [unref(currentCount)]);
      });

      watchEffect(() => {
        props.value === undefined && reset();
      });

      /**
       * @description: Judge whether there is an external function before execution, and decide whether to start after execution
       */
      async function handleStart() {
        const { beforeStartFunc } = props;
        if (beforeStartFunc && isFunction(beforeStartFunc)) {
          loading.value = true;
          try {
            //update-begin---author:wangshuai---date:2024-04-18---for:【QQYUN-9005】同一个IP，1分钟超过5次短信，则提示需要验证码---
            const canStart = await beforeStartFunc().catch((res) =>{
              if(res.code === ExceptionEnum.PHONE_SMS_FAIL_CODE){
                openCaptchaModal(true, {});
              }
            });
            //update-end---author:wangshuai---date:2024-04-18---for:【QQYUN-9005】同一个IP，1分钟超过5次短信，则提示需要验证码---
            canStart && start();
          } finally {
            loading.value = false;
          }
        } else {
          start();
        }
      }
      return { handleStart, currentCount, loading, getButtonText, isStart, captchaRegisterModal };
    },
  });
</script>
