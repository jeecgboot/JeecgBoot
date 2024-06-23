<template>
  <div :class="formDisabled ? 'jeecg-form-container-disabled jeecg-form-detail-effect' : ''">
    <fieldset :disabled="formDisabled">
      <slot name="detail"></slot>
    </fieldset>
    <slot name="edit"> </slot>
    <fieldset disabled>
      <slot></slot>
    </fieldset>
  </div>
</template>
<script lang="ts">
  import { defineComponent, ref, watch } from 'vue';

  export default defineComponent({
    name: 'JForm',
    props: {
      disabled: {
        type: Boolean,
        default: false,
        required: false,
      },
    },
    setup(props, { emit }) {
      const formDisabled = ref<boolean>(props.disabled);

      watch(
        () => props.disabled,
        (value) => {
          formDisabled.value = value;
        }
      );

      return {
        formDisabled,
      };
    },
  });
</script>

<style scoped lang="less">
  .jeecg-form-container-disabled {
    cursor: not-allowed;
  }
  .jeecg-form-container-disabled fieldset[disabled] {
    -ms-pointer-events: none;
    pointer-events: none;
  }
  .jeecg-form-container-disabled :deep(.ant-select) {
    -ms-pointer-events: none;
    pointer-events: none;
  }
  // update-begin--author:liaozhiyang---date:20240605---for：【TV360X-857】online代码生成详情样式调整
  // begin antdv 禁用样式
  // .jeecg-form-container-disabled :deep(.ant-input-number),
  // .jeecg-form-container-disabled :deep(.ant-input),
  // .jeecg-form-container-disabled :deep(.ant-input-password),
  // .jeecg-form-container-disabled :deep(.ant-select-single .ant-select-selector),
  // .jeecg-form-container-disabled :deep(.ant-radio-wrapper .ant-radio-checked .ant-radio-inner),
  // .jeecg-form-container-disabled :deep(.ant-switch),
  // .jeecg-form-container-disabled :deep(.ant-picker),
  // .jeecg-form-container-disabled :deep(.ant-select:not(.ant-select-customize-input) .ant-select-selector),
  // .jeecg-form-container-disabled :deep(.ant-input-affix-wrapper),
  // .jeecg-form-container-disabled :deep(.tox .tox-toolbar__group),
  // .jeecg-form-container-disabled :deep(.tox .tox-edit-area__iframe),
  // .jeecg-form-container-disabled :deep(.vditor-toolbar),
  // .jeecg-form-container-disabled :deep(.vditor-preview),
  // .jeecg-form-container-disabled :deep(.jeecg-tinymce-img-upload) {
  //   background: rgba(51, 51, 51, 0.04);
  // }

  // .jeecg-form-container-disabled :deep(.ant-radio-wrapper),
  // .jeecg-form-container-disabled :deep(.ant-checkbox-wrapper),
  // .jeecg-form-container-disabled :deep(.ant-btn) {
  //   color: rgba(0, 0, 0, 0.65);
  // }
  // .jeecg-form-container-disabled :deep(.ant-radio-wrapper .ant-radio-inner:after),
  // .jeecg-form-container-disabled :deep(.ant-checkbox-checked .ant-checkbox-inner) {
  //   background-color: rgba(51, 51, 51, 0.25);
  // }
  // .jeecg-form-container-disabled :deep(.ant-radio-inner),
  // .jeecg-form-container-disabled :deep(.ant-checkbox-inner) {
  //   border-color: rgba(51, 51, 51, 0.25) !important;
  // }
  // .jeecg-form-container-disabled :deep(.ant-input-password > .ant-input),
  // .jeecg-form-container-disabled :deep(.ant-input-affix-wrapper .ant-input) {
  //   background: none;
  // }
  html[data-theme='light'] {
    .jeecg-form-detail-effect {
      :deep(.ant-select-selector),
      :deep(.ant-btn),
      :deep(.ant-input),
      :deep(.ant-input-affix-wrapper),
      :deep(.ant-picker),
      :deep(.ant-input-number) {
        color: #606266 !important;
      }
      :deep(.ant-select) {
        color: #606266 !important;
      }
      :deep(.ant-select-selection-item-content),:deep(.ant-select-selection-item),:deep(input) {
        color: #606266 !important;
      }

      :deep(.ant-radio-wrapper),
      :deep(.ant-checkbox-wrapper),
      :deep(.ant-btn) {
        color: rgba(0, 0, 0, 0.65);
      }
      :deep(.ant-radio-wrapper .ant-radio-inner:after),
      :deep(.ant-checkbox-checked .ant-checkbox-inner) {
        color: #606266 !important;
      }
      :deep(.ant-radio-inner),
      :deep(.ant-checkbox-inner) {
        border-color: rgba(51, 51, 51, 0.25) !important;
        background-color: rgba(51, 51, 51, 0.04) !important;
      }
      :deep(.ant-checkbox-checked .ant-checkbox-inner::after), :deep(.ant-tree-checkbox-checked .ant-tree-checkbox-inner::after){
        border-color: rgba(51, 51, 51, 0.25) !important;
      }
      :deep(.ant-switch) {
        background-color: rgba(51, 51, 51, 0.25);
      }
      :deep(.tox .tox-toolbar__group),
      :deep(.tox .tox-edit-area__iframe),
      :deep(.vditor-toolbar),
      :deep(.vditor-preview),
      :deep(.jeecg-tinymce-img-upload) {
        background: rgba(51, 51, 51, 0.04);
      }
    }
  }

  html[data-theme='dark'] {
    .jeecg-form-detail-effect {
      :deep(.ant-select-selector),
      :deep(.ant-btn),
      :deep(.ant-input),
      :deep(.ant-input-affix-wrapper),
      :deep(.ant-picker),
      :deep(.ant-input-number) {
        color: rgba(255, 255, 255, 0.25) !important;
        //background-color: rgba(255, 255, 255, 0.08) !important;
      }
      :deep(.ant-select) {
        color: rgba(255, 255, 255, 0.25) !important;
      }
      :deep(.ant-select-selection-item-content),:deep(.ant-select-selection-item),:deep(input) {
        color: rgba(255, 255, 255, 0.25) !important;
      }

      :deep(.ant-radio-wrapper),
      :deep(.ant-checkbox-wrapper){
        color: rgba(255, 255, 255, 0.25);
      }
      :deep(.ant-radio-wrapper .ant-radio-inner:after),
      :deep(.ant-checkbox-checked .ant-checkbox-inner) {
        background-color: rgba(255, 255, 255, 0.08);
      }
      :deep(.ant-radio-inner),
      :deep(.ant-checkbox-inner) {
        border-color: #424242 !important;
        background-color: rgba(255, 255, 255, 0.08);
      }
      :deep(.ant-switch) {
        background-color: rgba(51, 51, 51, 0.25);
        opacity: 0.65;
      }
      :deep(.tox .tox-toolbar__group),
      :deep(.tox .tox-edit-area__iframe),
      :deep(.vditor-toolbar),
      :deep(.vditor-preview),
      :deep(.jeecg-tinymce-img-upload) {
        background: rgba(51, 51, 51, 0.04);
      }
    }
  }
  // end antdv 禁用样式
  // update-begin--author:liaozhiyang---date:20240605---for：【TV360X-857】online代码生成详情样式调整
  .jeecg-form-container-disabled :deep(.ant-upload-select) {
    cursor: grabbing;
  }
  .jeecg-form-container-disabled :deep(.ant-upload-list) {
    cursor: grabbing;
  }
  .jeecg-form-container-disabled fieldset[disabled] :deep(.ant-upload-list){
    // -ms-pointer-events: auto !important;
    // pointer-events: auto !important;
  }
  .jeecg-form-container-disabled fieldset[disabled] iframe {
    -ms-pointer-events: auto !important;
    pointer-events: auto !important;
  }
  .jeecg-form-container-disabled :deep(.ant-upload-list-item-actions .anticon-delete),
  .jeecg-form-container-disabled :deep(.ant-upload-list-item .anticon-close) {
    display: none;
  }
  .jeecg-form-container-disabled :deep(.vditor-sv) {
    display: none !important;
    background: rgba(51, 51, 51, 0.04);
  }
</style>
