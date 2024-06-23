<template>
  <div :class="prefixCls" :style="getStyle" v-if="showFooter || $slots.footer">
    <template v-if="!$slots.footer">
      <slot name="insertFooter"></slot>
      <a-button v-bind="cancelButtonProps" @click="handleClose" class="mr-2" v-if="showCancelBtn">
        {{ cancelText }}
      </a-button>
      <slot name="centerFooter"></slot>
      <a-button :type="okType" @click="handleOk" v-bind="okButtonProps" class="mr-2" :loading="confirmLoading" v-if="showOkBtn">
        {{ okText }}
      </a-button>
      <slot name="appendFooter"></slot>
    </template>

    <template v-else>
      <slot name="footer"></slot>
    </template>
  </div>
</template>
<script lang="ts">
  import type { CSSProperties } from 'vue';
  import { defineComponent, computed } from 'vue';
  import { useDesign } from '/@/hooks/web/useDesign';

  import { footerProps } from '../props';
  export default defineComponent({
    name: 'BasicDrawerFooter',
    props: {
      ...footerProps,
      height: {
        type: String,
        default: '60px',
      },
    },
    emits: ['ok', 'close'],
    setup(props, { emit }) {
      const { prefixCls } = useDesign('basic-drawer-footer');

      const getStyle = computed((): CSSProperties => {
        const heightStr = `${props.height}`;
        return {
          height: heightStr,
          lineHeight: heightStr,
        };
      });

      function handleOk() {
        emit('ok');
      }

      function handleClose() {
        emit('close');
      }
      return { handleOk, prefixCls, handleClose, getStyle };
    },
  });
</script>

<style lang="less">
  @prefix-cls: ~'@{namespace}-basic-drawer-footer';
  @footer-height: 60px;
  .@{prefix-cls} {
    position: absolute;
    bottom: 0;
    width: 100%;
    padding: 0 12px 0 20px;
    text-align: right;
    background-color: @component-background;
    border-top: 1px solid @border-color-base;

    > * {
      margin-right: 8px;
    }
  }
</style>
