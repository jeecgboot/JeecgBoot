<template>
  <Cascader v-bind="attrs" :value="cascaderValue" :options="getOptions" @change="handleChange" />
</template>
<script lang="ts">
  import { defineComponent, PropType, ref, reactive, watchEffect, computed, unref, watch, onMounted } from 'vue';
  import { Cascader } from 'ant-design-vue';
  import { provinceAndCityData, regionData, provinceAndCityDataPlus, regionDataPlus } from '../../utils/areaDataUtil';
  import { useRuleFormItem } from '/@/hooks/component/useFormItem';
  import { propTypes } from '/@/utils/propTypes';
  import { useAttrs } from '/@/hooks/core/useAttrs';
  import { isArray } from '/@/utils/is';

  export default defineComponent({
    name: 'JAreaLinkage',
    components: {
      Cascader,
    },
    inheritAttrs: false,
    props: {
      value: propTypes.oneOfType([propTypes.object, propTypes.array, propTypes.string]),
      //是否显示区县
      showArea: propTypes.bool.def(true),
      //是否是全部
      showAll: propTypes.bool.def(false),
      // 存储数据 
      saveCode: propTypes.oneOf(['province', 'city', 'region', 'all']).def('all'),
    },
    emits: ['options-change', 'change', 'update:value'],
    setup(props, { emit, refs }) {
      const emitData = ref<any[]>([]);
      const attrs = useAttrs();
      // const [state] = useRuleFormItem(props, 'value', 'change', emitData);
      const cascaderValue = ref([]);
      const getOptions = computed(() => {
        if (props.showArea && props.showAll) {
          return regionDataPlus;
        }
        if (props.showArea && !props.showAll) {
          return regionData;
        }
        if (!props.showArea && !props.showAll) {
          return provinceAndCityData;
        }
        if (!props.showArea && props.showAll) {
          return provinceAndCityDataPlus;
        }
      });
      /**
       * 监听value变化
       */
      watchEffect(() => {
        // update-begin--author:liaozhiyang---date:20240612--for：【TV360X-1223】省市区换新组件
        if (props.value) {
          initValue();
        } else {
          cascaderValue.value = [];
        }
        // update-end--author:liaozhiyang---date:20240612---for：【TV360X-1223】省市区换新组件
      });

      /**
       * 将字符串值转化为数组
       */
      function initValue() {
        let value = props.value ? props.value : [];
        // update-begin--author:liaozhiyang---date:20240607---for：【TV360X-501】省市区换新组件
        if (value && typeof value === 'string' && value != 'null' && value != 'undefined') {
          const arr = value.split(',');
          cascaderValue.value = transform(arr);
        } else if (isArray(value)) {
          if (value.length) {
            cascaderValue.value = transform(value);
          } else {
            cascaderValue.value = [];
          }
        }
        // update-end--author:liaozhiyang---date:20240607---for：【TV360X-501】省市区换新组件
      }
      function transform(arr) {
        let result: any = [];
        if (props.saveCode === 'region') {
          // 81 香港、82 澳门
          const regionCode = arr[0];
          if (['82', '81'].includes(regionCode.substring(0, 2))) {
            result = [`${regionCode.substring(0, 2)}0000`, regionCode];
          } else {
            result = [`${regionCode.substring(0, 2)}0000`, `${regionCode.substring(0, 2)}${regionCode.substring(2, 4)}00`, regionCode];
          }
        } else if (props.saveCode === 'city') {
          const cityCode = arr[0];
          result = [`${cityCode.substring(0, 2)}0000`, cityCode];
        } else if (props.saveCode === 'province') {
          const provinceCode = arr[0];
          result = [provinceCode];
        } else {
          result = arr;
        }
        return result;
      }
      function handleChange(arr, ...args) {
        // update-begin--author:liaozhiyang---date:20240607---for：【TV360X-501】省市区换新组件
        if (arr?.length) {
          let result: any = [];
          if (props.saveCode === 'region') {
            // 可能只有两位（选择香港时，只有省区）
            result = [arr[arr.length - 1]];
          } else if (props.saveCode === 'city') {
            result = [arr[1]];
          } else if (props.saveCode === 'province') {
            result = [arr[0]];
          } else {
            result = arr;
          }
          emit('change', result);
          emit('update:value', result);
        } else {
          emit('change', arr);
          emit('update:value', arr);
        }
        // update-end--author:liaozhiyang---date:20240607---for：【TV360X-501】省市区换新组件
        // emitData.value = args;
        //update-begin-author:taoyan date:2022-6-27 for: VUEN-1424【vue3】树表、单表、jvxe、erp 、内嵌子表省市县 选择不上
        // 上面改的v-model:value导致选中数据没有显示
        // state.value = result;
        //update-end-author:taoyan date:2022-6-27 for: VUEN-1424【vue3】树表、单表、jvxe、erp 、内嵌子表省市县 选择不上
      }
      return {
        cascaderValue,
        attrs,
        regionData,
        getOptions,
        handleChange,
      };
    },
  });
</script>
