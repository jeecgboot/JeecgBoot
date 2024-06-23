<template>
  <div :id="formItemId" style="flex: 1; width: 100%">
    <slot></slot>
  </div>
</template>

<script setup>
  import { Form } from 'ant-design-vue';
  import { computed } from 'vue';
  const formItemContext = Form.useInjectFormItemContext();
  const formItemId = computed(() => {
    return formItemContext.id.value;
  });
</script>

<style lang="scss" scoped></style>
