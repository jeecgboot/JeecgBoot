<template>
  <a-radio-group v-if="compType === CompTypeEnum.Radio" v-bind="attrs" v-model:value="state" @change="handleChangeRadio">
    <template v-for="item in dictOptions" :key="`${item.value}`">
      <a-radio :value="item.value">
        <span :class="[useDicColor && item.color ? 'colorText' : '']" :style="{ backgroundColor: `${useDicColor && item.color}` }">
          {{ item.label }}
        </span>
      </a-radio>
    </template>
  </a-radio-group>

  <a-radio-group
    v-else-if="compType === CompTypeEnum.RadioButton"
    v-bind="attrs"
    v-model:value="state"
    buttonStyle="solid"
    @change="handleChangeRadio"
  >
    <template v-for="item in dictOptions" :key="`${item.value}`">
      <a-radio-button :value="item.value">
        {{ item.label }}
      </a-radio-button>
    </template>
  </a-radio-group>

  <template v-else-if="compType === CompTypeEnum.Select">
    <!-- 显示加载效果 -->
    <a-input v-if="loadingEcho" readOnly placeholder="加载中…">
      <template #prefix>
        <LoadingOutlined />
      </template>
    </a-input>
    <a-select
      v-else
      :placeholder="placeholder"
      v-bind="attrs"
      v-model:value="state"
      :filterOption="handleFilterOption"
      :getPopupContainer="getPopupContainer"
      :style="style"
      @change="handleChange"
    >
      <a-select-option v-if="showChooseOption" :value="null">请选择…</a-select-option>
      <template v-for="item in dictOptions" :key="`${item.value}`">
        <a-select-option :value="item.value">
          <span
            :class="[useDicColor && item.color ? 'colorText' : '']"
            :style="{ backgroundColor: `${useDicColor && item.color}` }"
            :title="item.label"
          >
            {{ item.label }}
          </span>
        </a-select-option>
      </template>
    </a-select>
  </template>
</template>
<script lang="ts">
  import { defineComponent, PropType, ref, reactive, watchEffect, computed, unref, watch, onMounted, nextTick } from 'vue';
  import { propTypes } from '/@/utils/propTypes';
  import { useAttrs } from '/@/hooks/core/useAttrs';
  import { initDictOptions } from '/@/utils/dict';
  import { get, omit } from 'lodash-es';
  import { useRuleFormItem } from '/@/hooks/component/useFormItem';
  import { CompTypeEnum } from '/@/enums/CompTypeEnum';
  import { LoadingOutlined } from '@ant-design/icons-vue';

  export default defineComponent({
    name: 'JDictSelectTag',
    inheritAttrs: false,
    components: { LoadingOutlined },
    props: {
      value: propTypes.oneOfType([propTypes.string, propTypes.number, propTypes.array]),
      dictCode: propTypes.string,
      type: propTypes.string,
      placeholder: propTypes.string,
      stringToNumber: propTypes.bool,
      useDicColor: propTypes.bool.def(false),
      getPopupContainer: {
        type: Function,
        default: (node) => node?.parentNode,
      },
      // 是否显示【请选择】选项
      showChooseOption: propTypes.bool.def(true),
      // 下拉项-online使用
      options: {
        type: Array,
        default: [],
        required: false,
      },
      style: propTypes.any,
    },
    emits: ['options-change', 'change','update:value'],
    setup(props, { emit, refs }) {
      const dictOptions = ref<any[]>([]);
      const attrs = useAttrs();
      const [state, , , formItemContext] = useRuleFormItem(props, 'value', 'change');
      const getBindValue = Object.assign({}, unref(props), unref(attrs));
      // 是否正在加载回显数据
      const loadingEcho = ref<boolean>(false);
      // 是否是首次加载回显，只有首次加载，才会显示 loading
      let isFirstLoadEcho = true;

      //组件类型
      const compType = computed(() => {
        return !props.type || props.type === 'list' ? 'select' : props.type;
      });
      /**
       * 监听字典code
       */
      watchEffect(() => {
        if (props.dictCode) {
          loadingEcho.value = isFirstLoadEcho;
          isFirstLoadEcho = false;
          initDictData().finally(() => {
            loadingEcho.value = isFirstLoadEcho;
          });
        }
        //update-begin-author:taoyan date: 如果没有提供dictCode 可以走options的配置--
        if (!props.dictCode) {
          dictOptions.value = props.options;
        }
        //update-end-author:taoyan date: 如果没有提供dictCode 可以走options的配置--
      });

      //update-begin-author:taoyan date:20220404 for: 使用useRuleFormItem定义的value，会有一个问题，如果不是操作设置的值而是代码设置的控件值而不能触发change事件
      // 此处添加空值的change事件,即当组件调用地代码设置value为''也能触发change事件
      watch(
        () => props.value,
        () => {
          if (props.value === '') {
            emit('change', '');
            nextTick(() => formItemContext.onFieldChange());
          }
        }
      );
      //update-end-author:taoyan date:20220404 for: 使用useRuleFormItem定义的value，会有一个问题，如果不是操作设置的值而是代码设置的控件值而不能触发change事件

      async function initDictData() {
        let { dictCode, stringToNumber } = props;
        //根据字典Code, 初始化字典数组
        const dictData = await initDictOptions(dictCode);
        dictOptions.value = dictData.reduce((prev, next) => {
          if (next) {
            const value = next['value'];
            prev.push({
              label: next['text'] || next['label'],
              value: stringToNumber ? +value : value,
              color: next['color'],
              ...omit(next, ['text', 'value', 'color']),
            });
          }
          return prev;
        }, []);
      }

      function handleChange(e) {
        const { mode } = unref<Recordable>(getBindValue);
        let changeValue:any;
        // 兼容多选模式
        
        //update-begin---author:wangshuai ---date:20230216  for：[QQYUN-4290]公文发文：选择机关代字报错,是因为值改变触发了change事件三次，导致数据发生改变------------
        //采用一个值，不然的话state值变换触发多个change
        if (mode === 'multiple') {
          changeValue = e?.target?.value ?? e;
          // 过滤掉空值
          if (changeValue == null || changeValue === '') {
            changeValue = [];
          }
          if (Array.isArray(changeValue)) {
            changeValue = changeValue.filter((item) => item != null && item !== '');
          }
        } else {
          changeValue = e?.target?.value ?? e;
        }
        state.value = changeValue;

        //update-begin---author:wangshuai ---date:20230403  for：【issues/4507】JDictSelectTag组件使用时，浏览器给出警告提示：Expected Function, got Array------------
        emit('update:value',changeValue)
        //update-end---author:wangshuai ---date:20230403  for：【issues/4507】JDictSelectTag组件使用时，浏览器给出警告提示：Expected Function, got Array述------------
        //update-end---author:wangshuai ---date:20230216  for：[QQYUN-4290]公文发文：选择机关代字报错,是因为值改变触发了change事件三次，导致数据发生改变------------
        
        // nextTick(() => formItemContext.onFieldChange());
      }

      /** 单选radio的值变化事件 */
      function handleChangeRadio(e) {
        state.value = e?.target?.value ?? e;
        //update-begin---author:wangshuai ---date:20230504  for：【issues/506】JDictSelectTag 组件 type="radio" 没有返回值------------
        emit('update:value',e?.target?.value ?? e)
        //update-end---author:wangshuai ---date:20230504  for：【issues/506】JDictSelectTag 组件 type="radio" 没有返回值------------
      }

      /** 用于搜索下拉框中的内容 */
      function handleFilterOption(input, option) {
        // update-begin--author:liaozhiyang---date:20230914---for：【QQYUN-6514】 配置的时候，Y轴不能输入多个字段了，控制台报错
        if (typeof option.children === 'function') {
          // 在 label 中搜索
          let labelIf = option.children()[0]?.children.toLowerCase().indexOf(input.toLowerCase()) >= 0;
          if (labelIf) {
            return true;
          }
        }
        // update-end--author:liaozhiyang---date:20230914---for：【QQYUN-6514】 配置的时候，Y轴不能输入多个字段了，控制台报错
        // 在 value 中搜索
        return (option.value || '').toString().toLowerCase().indexOf(input.toLowerCase()) >= 0;
      }

      return {
        state,
        compType,
        attrs,
        loadingEcho,
        getBindValue,
        dictOptions,
        CompTypeEnum,
        handleChange,
        handleChangeRadio,
        handleFilterOption,
      };
    },
  });
</script>
<style scoped lang="less">
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
