<template>
  <BasicModal @register="registerModel" title="详细信息" :width="1200" :keyboard="true" @ok="handleOk" @cancel="close">
    <transition name="fade">
      <div v-if="getVisible">
        <slot name="mainForm" :row="row" :column="column" />
      </div>
    </transition>
  </BasicModal>
</template>
<script lang="ts">
  import { ref, defineComponent } from 'vue';
  import { cloneDeep } from 'lodash-es';
  import { useModal } from '/@/components/Modal/src/hooks/useModal';
  import { createAsyncComponent } from '/@/utils/factory/createAsyncComponent';

  export default defineComponent({
    components: {
      BasicModal: createAsyncComponent(() => import('/@/components/Modal/src/BasicModal.vue'), {
        loading: true,
      }),
    },
    props: {
      trigger: {
        type: Function,
        required: true,
      },
    },
    setup(props) {
      const row = ref(null);
      const column = ref(null);

      const [registerModel, { openModal, closeModal, getVisible }] = useModal();

      function open(event) {
        let { row: $row, column: $column } = event;
        row.value = cloneDeep($row);
        column.value = $column;
        openModal();
      }

      function close() {
        closeModal();
      }

      function handleOk() {
        props.trigger('detailsConfirm', {
          row: row.value,
          column: column.value,
          callback: (success) => {
            success ? closeModal() : openModal();
          },
        });
      }

      return {
        getVisible,
        row,
        column,
        open,
        close,
        handleOk,
        registerModel,
      };
    },
  });
</script>
<style scoped lang="less">
  .fade-enter-active,
  .fade-leave-active {
    opacity: 1;
    transition: opacity 0.5s;
  }

  .fade-enter,
  .fade-leave-to {
    opacity: 0;
  }
</style>
