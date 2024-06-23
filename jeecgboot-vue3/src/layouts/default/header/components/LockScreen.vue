<template>
  <Tooltip :title="t('layout.header.tooltipLock')" placement="bottom" :mouseEnterDelay="0.5" @click="handleLock">
    <LockOutlined />
  </Tooltip>
  <LockModal ref="modalRef" v-if="lockModalVisible" @register="register" />
</template>
<script lang="ts">
  import { defineComponent, computed, ref } from 'vue';
  import { createAsyncComponent } from '/@/utils/factory/createAsyncComponent';
  import { Tooltip } from 'ant-design-vue';
  import { LockOutlined } from '@ant-design/icons-vue';
  import Icon from '/@/components/Icon';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { useModal } from '/@/components/Modal';
  import { getRefPromise } from '/@/utils/index';

  export default defineComponent({
    name: 'LockScreen',
    inheritAttrs: false,
    components: {
      Icon,
      Tooltip,
      LockOutlined,
      LockModal: createAsyncComponent(() => import('./lock/LockModal.vue')),
    },
    setup() {
      const { t } = useI18n();
      const [register, { openModal }] = useModal();
      // update-begin--author:liaozhiyang---date:20230901---for：【QQYUN-6333】空路由问题—首次访问资源太大
      const lockModalVisible = ref(false);
      const modalRef = ref(null);
      async function handleLock() {
        lockModalVisible.value = true;
        await getRefPromise(modalRef);
        openModal(true);
      }
      // update-end--author:liaozhiyang---date:20230901---for：【QQYUN-6333】空路由问题—首次访问资源太大
      return {
        t,
        register,
        handleLock,
        lockModalVisible,
        modalRef,
      };
    },
  });
</script>
