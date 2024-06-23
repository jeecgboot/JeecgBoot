<template>
  <a-checkbox-group v-bind="attrs" v-model:value="checkboxArray" :options="checkOptions" @change="handleChange">
    <template #label="{label, value}">
      <span :class="[useDicColor && getDicColor(value) ? 'colorText' : '']" :style="{ backgroundColor: `${getDicColor(value)}` }">{{ label }}</span>
    </template>
  </a-checkbox-group>
</template>

<script lang="ts">
  import { defineComponent, computed, watch, watchEffect, ref, unref } from 'vue';
  import { propTypes } from '/@/utils/propTypes';
  import { useAttrs } from '/@/hooks/core/useAttrs';
  import { initDictOptions } from '/@/utils/dict/index';

  export default defineComponent({
    name: 'JCheckbox',
    props: {
      value:propTypes.oneOfType([propTypes.string, propTypes.number]),
      dictCode: propTypes.string,
      useDicColor: propTypes.bool.def(false),
      options: {
        type: Array,
        default: () => [],
      },
    },
    emits: ['change', 'update:value'],
    setup(props, { emit }) {
      const attrs = useAttrs();
      //checkbox选项
      const checkOptions = ref<any[]>([]);
      //checkbox数值
      const checkboxArray = ref<any[]>([]);
      /**
       * 监听value
       */
      watchEffect(() => {
        //update-begin-author:taoyan date:2022-7-4 for:issues/I5E7YX AUTO在线表单进入功能测试之后一直卡在功能测试界面
        let temp = props.value;
        if(!temp && temp!==0){
          checkboxArray.value = []
        }else{
          temp = temp + '';
          checkboxArray.value = temp.split(',')
        }
        //update-end-author:taoyan date:2022-7-4 for:issues/I5E7YX AUTO在线表单进入功能测试之后一直卡在功能测试界面
        //update-begin-author:taoyan date:20220401 for: 调用表单的 resetFields不会清空当前信息，界面显示上一次的数据
        if (props.value === '' || props.value === undefined) {
          checkboxArray.value = [];
        }
        //update-end-author:taoyan date:20220401 for: 调用表单的 resetFields不会清空当前信息，界面显示上一次的数据
      });
      /**
       * 监听字典code
       */
      watchEffect(() => {
        props && initOptions();
      });

      /**
       * 初始化选项
       */
      async function initOptions() {
        //根据options, 初始化选项
        if (props.options && props.options.length > 0) {
          checkOptions.value = props.options;
          return;
        }
        //根据字典Code, 初始化选项
        if (props.dictCode) {
          const dictData = await initDictOptions(props.dictCode);
          checkOptions.value = dictData.reduce((prev, next) => {
            if (next) {
              const value = next['value'];
              prev.push({
                label: next['text'],
                value: value,
                color: next['color'],
              });
            }
            return prev;
          }, []);
        }
      }

      /**
       * change事件
       * @param $event
       */
      function handleChange($event) {
        emit('update:value', $event.join(','));
        emit('change', $event.join(','));
      }
      const getDicColor = (value) => {
        if (props.useDicColor) {
          const findItem = checkOptions.value.find((item) => item.value == value);
          if (findItem) {
            return findItem.color;
          }
        }
        return null;
      };
      return { checkboxArray, checkOptions, attrs, handleChange, getDicColor };
    },
  });
</script>
<style lang="less" scoped>
  // update-begin--author:liaozhiyang---date:20230110---for：【QQYUN-7799】字典组件（原生组件除外）加上颜色配置
  .colorText {
    display: inline-block;
    height: 20px;
    line-height: 20px;
    padding: 0 6px;
    border-radius: 8px;
    background-color: red;
    color: #fff;
    font-size: 12px;
  }
  // update-begin--author:liaozhiyang---date:20230110---for：【QQYUN-7799】字典组件（原生组件除外）加上颜色配置
</style>
