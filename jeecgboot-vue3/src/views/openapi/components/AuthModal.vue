<template>
  <!--  <j-modal :title="title" :width="width" :visible="visible" @ok="handleOk" :okButtonProps="{ class: { 'jee-hidden': disableSubmit } }" @cancel="handleCancel" cancelText="关闭">-->
  <div style="position: relative;">
    <a-drawer
      v-model:open="authDrawerOpen"
      class="custom-class"
      root-class-name="root-class-name"
      :root-style="{ color: 'blue' }"
      style="color: red"
      title="Basic Drawer"
      placement="right"
      :width="800"
      @after-open-change="authDrawerOpenChange"
    >
      <AuthForm ref="registerForm" @ok="submitCallback" :formDisabled="disableSubmit" :formBpm="false"></AuthForm>
      <a-button type="primary" style="position: absolute;bottom: 2%;right: 2%" @click="handleOk">确定</a-button>
    </a-drawer>
  </div>
  <!--  </j-modal>-->
</template>

<script lang="ts" setup>
  import { ref, nextTick, defineExpose } from 'vue';
  import AuthForm from './AuthForm.vue';
  import JModal from '/@/components/Modal/src/JModal/JModal.vue';

  const title = ref<string>('');
  const width = ref<number>(800);
  const visible = ref<boolean>(false);
  const disableSubmit = ref<boolean>(false);
  const registerForm = ref();
  const emit = defineEmits(['register', 'success']);

  const authDrawerOpen = ref(false);
  const authDrawerOpenChange = (val: any) => {
    if(!val)
      registerForm.value.cleanData()
  };

  /**
   * 新增
   */
  function add() {
    title.value = '新增';
    visible.value = true;
    nextTick(() => {
      registerForm.value.add();
    });
  }

  /**
   * 授权
   * @param record
   */
  function edit(record) {
    title.value = disableSubmit.value ? '详情' : '授权';
    visible.value = true;
    authDrawerOpen.value = true;
    nextTick(() => {
      registerForm.value.edit(record);
    });
  }

  /**
   * 确定按钮点击事件
   */
  function handleOk() {
    registerForm.value.submitForm();
  }

  /**
   * form保存回调事件
   */
  function submitCallback() {
    handleCancel();
    emit('success');
  }

  /**
   * 取消按钮回调事件
   */
  function handleCancel() {
    visible.value = false;
    authDrawerOpen.value = false;
  }

  defineExpose({
    add,
    edit,
    disableSubmit,
  });
</script>

<style lang="less">
  /**隐藏样式-modal确定按钮 */
  .jee-hidden {
    display: none !important;
  }
</style>
<style lang="less" scoped></style>
