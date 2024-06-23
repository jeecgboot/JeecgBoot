<template>
  <BasicModal @register="register" title="Modal Title" :helpMessage="['提示1', '提示2']" :okButtonProps="{ disabled: true }">
    <a-button type="primary" @click="closeModal" class="mr-2"> 从内部关闭弹窗 </a-button>
    <a-button type="primary" @click="setModalProps"> 从内部修改title </a-button>
  </BasicModal>
</template>
<script lang="ts">
  import { defineComponent } from 'vue';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  export default defineComponent({
    components: { BasicModal },
    setup() {
      const [register, { closeModal, setModalProps }] = useModalInner();
      return {
        register,
        closeModal,
        setModalProps: () => {
          setModalProps({ title: 'Modal New Title' });
        },
      };
    },
  });
</script>
