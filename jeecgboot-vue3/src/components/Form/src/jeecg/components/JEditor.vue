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
        // update-begin--author:liaozhiyang---date:20240429---for：【QQYUN-9110】组件有值校验没消失
        nextTick(() => {
          formItemContext?.onFieldChange();
        });
        // update-end--author:liaozhiyang---date:20240429---for：【QQYUN-9110】组件有值校验没消失
      }

      return {
        bindProps,
        onChange,
      };
    },
  });
</script>

<style lang="less" scoped></style>
