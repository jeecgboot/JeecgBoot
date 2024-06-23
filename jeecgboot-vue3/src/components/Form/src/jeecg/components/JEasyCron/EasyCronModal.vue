<template>
  <BasicModal @register="registerModal" title="Cron表达式" width="800px" @ok="onOk">
    <EasyCron v-bind="attrs" />
  </BasicModal>
</template>

<script lang="ts">
  import { defineComponent } from 'vue';
  import { useAttrs } from '/@/hooks/core/useAttrs';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import EasyCron from './EasyCronInner.vue';

  export default defineComponent({
    name: 'EasyCronModal',
    inheritAttrs: false,
    components: { BasicModal, EasyCron },
    setup() {
      const attrs = useAttrs();
      const [registerModal, { closeModal }] = useModalInner();

      function onOk() {
        closeModal();
      }

      return { attrs, registerModal, onOk };
    },
  });
</script>
