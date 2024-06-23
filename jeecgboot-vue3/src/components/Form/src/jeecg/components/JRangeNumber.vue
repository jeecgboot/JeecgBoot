<template>
  <a-input-group>
    <a-input-number v-bind="attrs" :value="beginValue" style="width: calc(50% - 15px)" placeholder="最小值" @change="handleChangeBegin" />
    <a-input style="width: 30px; border-left: 0; pointer-events: none; background-color: #fff" placeholder="~" disabled />
    <a-input-number v-bind="attrs" :value="endValue" style="width: calc(50% - 15px); border-left: 0" placeholder="最大值" @change="handleChangeEnd" />
  </a-input-group>
</template>

<script>
  /**
   * 查询条件用-数值范围查询
   */
  import { ref, watch } from 'vue';
  import { Form } from 'ant-design-vue';
  import { propTypes } from '/@/utils/propTypes';
  import { useAttrs } from '/@/hooks/core/useAttrs';
  
  export default {
    name: 'JRangeNumber',
    props: {
      value: propTypes.oneOfType([propTypes.string, propTypes.array]),
    },
    emits: ['change', 'update:value', 'blur'],
    setup(props, { emit }) {
      const beginValue = ref('');
      const endValue = ref('');
      const attrs = useAttrs();
      const formItemContext = Form.useInjectFormItemContext();
      
      function handleChangeBegin(e) {
        beginValue.value = e;
        emitArray();
      }

      function handleChangeEnd(e) {
        endValue.value = e;
        emitArray();
      }

      function emitArray() {
        let arr = [];
        let begin = beginValue.value || '';
        let end = endValue.value || '';
        arr.push(begin);
        arr.push(end);
        emit('change', arr);
        emit('update:value', arr);
        formItemContext.onFieldChange();
      }

      watch(
        () => props.value,
        (val) => {
          if (val && val.length == 2) {
            beginValue.value = val[0];
            endValue.value = val[1];
          } else {
            beginValue.value = '';
            endValue.value = '';
          }
        }, {immediate: true}
      );
      
      return {
        beginValue,
        endValue,
        handleChangeBegin,
        handleChangeEnd,
        attrs,
      };
    },
  };
</script>

<style lang="less" scoped>
  // update-begin--author:liaozhiyang---date:20240607---for：【TV360X-214】范围查询控件没有根据配置格式化
  .ant-input-group {
    display: flex;
    .ant-input-number {
      &:first-child {
        border-top-right-radius: 0;
        border-bottom-right-radius: 0;
      }
      &:last-child {
        border-top-left-radius: 0;
        border-bottom-left-radius: 0;
      }
    }
  }
  // update-end--author:liaozhiyang---date:20240607---for：【TV360X-214】范围查询控件没有根据配置格式化
</style>
