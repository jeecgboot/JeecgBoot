<template>
  <div :class="disabled ? 'jeecg-form-container-disabled' : ''">
    <fieldset :disabled="disabled">
      <slot></slot>
    </fieldset>
  </div>
</template>

<script lang="ts">
  import { defineComponent } from 'vue';

  export default defineComponent({
    name: 'JFormContainer',
    props: {
      disabled: {
        type: Boolean,
        default: false,
        required: false,
      },
    },
  });
</script>

<style lang="less">
  .jeecg-form-container-disabled {
    cursor: not-allowed;

    fieldset[disabled] {
      -ms-pointer-events: none;
      pointer-events: none;
    }

    .ant-select {
      -ms-pointer-events: none;
      pointer-events: none;
    }

    .ant-upload-select {
      display: none;
    }

    .ant-upload-list {
      cursor: grabbing;
    }

    fieldset[disabled]{
      .anticon-delete{
        display: none !important;
      }
    }
  }

  .jeecg-form-container-disabled fieldset[disabled] .ant-upload-list {
    -ms-pointer-events: auto !important;
    pointer-events: auto !important;
  }

  .jeecg-form-container-disabled .ant-upload-list-item-actions .anticon-delete,
  .jeecg-form-container-disabled .ant-upload-list-item .anticon-close {
    display: none;
  }
</style>
