<template>
  <a-input v-bind="getBindValue" v-model:value="showText" @input="backValue"></a-input>
</template>

<script lang="ts">
  import { defineComponent, PropType, ref, watchEffect, unref, watch, computed } from 'vue';
  import { useAttrs } from '/@/hooks/core/useAttrs';
  import { propTypes } from '/@/utils/propTypes';
  import { JInputTypeEnum } from '/@/enums/jeecgEnum.ts';
  import { omit } from 'lodash-es';

  export default defineComponent({
    name: 'JInput',
    inheritAttrs: false,
    props: {
      value: propTypes.string.def(''),
      type: propTypes.string.def(JInputTypeEnum.JINPUT_QUERY_LIKE),
      placeholder: propTypes.string.def(''),
      trim: propTypes.bool.def(false),
    },
    emits: ['change', 'update:value'],
    setup(props, { emit }) {
      const attrs = useAttrs();
      //表单值
      const showText = ref('');
      // update-begin--author:liaozhiyang---date:20231026---for：【issues/803】JIput updateSchema不生效
      //绑定属性
      const getBindValue = computed(() => {
        return omit(Object.assign({}, unref(props), unref(attrs)), ['value']);
      });
      // update-end--author:liaozhiyang---date:20231026---for：【issues/803】JIput updateSchema不生效
      //监听类型变化
      watch(
        () => props.type,
        (val) => {
          val && backValue({ target: { value: unref(showText) } });
        }
      );
      //监听value变化
      watch(
        () => props.value,
        () => {
          initVal();
        },
        { immediate: true }
      );

      /**
       * 初始化数值
       */
      function initVal() {
        if (!props.value) {
          showText.value = '';
        } else {
          let text = props.value;
          switch (props.type) {
            case JInputTypeEnum.JINPUT_QUERY_LIKE:
              //修复路由传参的值传送到jinput框被前后各截取了一位 #1336
              if (text.indexOf('*') != -1) {
                text = text.substring(1, text.length - 1);
              }
              break;
            case JInputTypeEnum.JINPUT_QUERY_NE:
              text = text.substring(1);
              break;
            case JInputTypeEnum.JINPUT_QUERY_GE:
              text = text.substring(2);
              break;
            case JInputTypeEnum.JINPUT_QUERY_LE:
              text = text.substring(2);
              break;
            default:
          }
          showText.value = text;
        }
      }

      /**
       * 返回值
       */
      function backValue(e) {
        let text = e?.target?.value ?? '';
        if (text && !!props.trim) {
          text = text.trim();
        }
        switch (props.type) {
          case JInputTypeEnum.JINPUT_QUERY_LIKE:
            text = '*' + text + '*';
            break;
          case JInputTypeEnum.JINPUT_QUERY_NE:
            text = '!' + text;
            break;
          case JInputTypeEnum.JINPUT_QUERY_GE:
            text = '>=' + text;
            break;
          case JInputTypeEnum.JINPUT_QUERY_LE:
            text = '<=' + text;
            break;
          default:
        }
        emit('change', text);
        emit('update:value', text);
      }

      return { showText, attrs, getBindValue, backValue };
    },
  });
</script>

<style scoped></style>
