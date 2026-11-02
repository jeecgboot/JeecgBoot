<template>
  <Tinymce v-bind="bindProps" @change="onChange" />
</template>

<script lang="ts">
  import { computed, defineComponent, nextTick } from 'vue';

  import { Tinymce } from '/@/components/Tinymce';
  import { propTypes } from '/@/utils/propTypes';
  import { Form } from 'ant-design-vue';

  export default defineComponent({
    name: 'JEditor',
    // 不将 attrs 的属性绑定到 html 标签上
    inheritAttrs: false,
    components: { Tinymce },
    props: {
      value: propTypes.string.def(''),
      disabled: propTypes.bool.def(false),
      //是否聚焦
      autoFocus: propTypes.bool.def(true),
    },
    emits: ['change', 'update:value'],
    setup(props, { emit, attrs }) {
      // 合并 props 和 attrs
      const bindProps = computed(() => Object.assign({}, props, attrs));
      const formItemContext = Form.useInjectFormItemContext();
      // value change 事件
      function onChange(value) {
        emit('change', value);
        emit('update:value', value);
        // 代码逻辑说明: 【QQYUN-9110】组件有值校验没消失
        nextTick(() => {
          formItemContext?.onFieldChange();
        });
      }

      return {
        bindProps,
        onChange,
      };
    },
  });
</script>

<style lang="less">
  .tox .tox-dialog {
    width: min(680px, calc(100vw - 32px));
    border-radius: 6px;
    box-shadow: 0 8px 28px rgb(0 0 0 / 16%);
    overflow: hidden;

    .tox-dialog__header {
      padding: 16px;
    }

    .tox-dialog__title {
      font-size: 16px;
      font-weight: 500;
      line-height: 24px;
      color: #262626;
    }

    .tox-dialog__body {
      display: block;
    }

    .tox-dialog__body-nav {
      flex-direction: row;
      gap: 24px;
      padding: 0 20px;
      background: #fafafa;
      border-bottom: 1px solid #f0f0f0;
    }

    .tox-dialog__body-nav-item {
      padding: 9px 4px 8px;
      margin-bottom: -1px;
      font-size: 13px;
      line-height: 20px;
    }

    .tox-dialog__body-content {
      height: 270px !important;
      min-height: 270px;
      max-height: calc(100vh - 220px);
      flex-basis: 270px !important;
      padding: 20px 20px 24px;
      overflow-y: auto;
    }

    .tox-label {
      padding-bottom: 5px;
      font-size: 13px;
      line-height: 20px;
      color: #595959;
    }

    .tox-form__group:not(:last-child) {
      margin-bottom: 10px;
    }

    .tox-textfield {
      min-height: 36px;
      padding: 6px 10px;
      border-color: #d9d9d9;
      border-radius: 4px;
      font-size: 13px;
      line-height: 22px;
      transition:
        border-color 0.2s,
        box-shadow 0.2s;
    }

    .tox-textfield:hover {
      border-color: #40a9ff;
    }

    .tox-textfield:focus {
      border-color: #409eff;
      box-shadow: 0 0 0 2px rgb(64 158 255 / 12%);
    }

    .tox-form__controls-h-stack .tox-form__grid {
      flex: 1;
    }

    .tox-form__controls-h-stack .tox-lock {
      margin-left: 10px;
    }

    .tox-dialog__footer {
      padding: 12px 20px;
      background: #fafafa;
    }

    .tox-button {
      min-width: 60px;
      padding: 4px 16px;
      border-radius: 4px;
      font-size: 13px;
      line-height: 22px;
      .tox-icon {
        display: flex;
        justify-content: center;
      }
    }
  }
</style>
