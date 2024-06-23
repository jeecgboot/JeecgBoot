<template>
  <div :class="prefixCls">
    <span> {{ title }}</span>
    <InputNumber v-bind="$attrs" size="small" :class="`${prefixCls}-input-number`" @change="handleChange" />
  </div>
</template>
<script lang="ts">
  import { defineComponent, PropType } from 'vue';

  import { InputNumber } from 'ant-design-vue';
  import { useDesign } from '/@/hooks/web/useDesign';
  import { baseHandler } from '../handler';
  import { HandlerEnum } from '../enum';

  export default defineComponent({
    name: 'InputNumberItem',
    components: { InputNumber },
    props: {
      event: {
        type: Number as PropType<HandlerEnum>,
      },
      title: {
        type: String,
      },
    },
    setup(props) {
      const { prefixCls } = useDesign('setting-input-number-item');

      function handleChange(e) {
        props.event && baseHandler(props.event, e);
      }
      return {
        prefixCls,
        handleChange,
      };
    },
  });
</script>
<style lang="less" scoped>
  @prefix-cls: ~'@{namespace}-setting-input-number-item';

  .@{prefix-cls} {
    display: flex;
    justify-content: space-between;
    margin: 16px 0;

    &-input-number {
      width: 126px;
    }
  }
  html[data-theme='dark'] {
    .@{prefix-cls} {
      color: rgba(255, 255, 255, 0.8);
    }
  }
</style>
