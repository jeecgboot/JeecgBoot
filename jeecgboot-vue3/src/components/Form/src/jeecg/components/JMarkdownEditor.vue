<template>
  <MarkDown v-bind="bindProps" @change="onChange" @get="onGetVditor" />
</template>

<script lang="ts">
  import { computed, defineComponent, watch, nextTick } from 'vue';
  import { MarkDown } from '/@/components/Markdown';
  import { propTypes } from '/@/utils/propTypes';
  import { Form } from 'ant-design-vue';
  export default defineComponent({
    name: 'JMarkdownEditor',
    // 不将 attrs 的属性绑定到 html 标签上
    inheritAttrs: false,
    components: { MarkDown },
    props: {
      value: propTypes.string.def(''),
      disabled: propTypes.bool.def(false),
    },
    emits: ['change', 'update:value'],
    setup(props, { emit, attrs }) {
      // markdown 组件实例
      let mdRef: any = null;
      // vditor 组件实例
      let vditorRef: any = null;
      // 合并 props 和 attrs
      const bindProps = computed(() => Object.assign({}, props, attrs));
      const formItemContext = Form.useInjectFormItemContext();
      // 相当于 onMounted
      function onGetVditor(instance) {
        mdRef = instance;
        vditorRef = mdRef.getVditor();

        // 监听禁用，切换编辑器禁用状态
        watch(
          () => props.disabled,
          (disabled) => (disabled ? vditorRef.disabled() : vditorRef.enable()),
          { immediate: true }
        );
      }

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
        onGetVditor,
      };
    },
  });
</script>

<style lang="less" scoped></style>
