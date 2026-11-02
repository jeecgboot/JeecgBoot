<!--popup组件-->
<template>
  <div class="JPopupDict components-input-demo-presuffix">
    <!--输入框-->
    <a-input v-if="useInput" v-model:value="showLabel" v-bind="attrs" :disabled="disabled" @click="handleOpen" readonly :loading="loading" />
    <a-select v-else v-model:value="showText" v-bind="attrs" :disabled="disabled" :mode="multi ? 'multiple' : ''" @click="handleOpen" readOnly :loading="loading" :open="false">
      <a-select-option v-for="item in options" :value="item.value">{{ item.text }}</a-select-option>
    </a-select>
    <a-form-item>
      <!--popup弹窗-->
      <JPopupOnlReportModal
        @register="regModal"
        :code="code"
        :multi="multi"
        :echoRows="echoRows"
        :sorter="sorter"
        :groupId="''"
        :param="param"
        :getFormValues="getFormValues"
        :getContainer="getContainer"
        :showAdvancedButton="showAdvancedButton"
        @ok="callBack"
      />
    </a-form-item>
  </div>
</template>
<script lang="ts">
  import JPopupOnlReportModal from './modal/JPopupOnlReportModal.vue';
  import { defineComponent, ref, nextTick, watch, reactive, unref } from 'vue';
  import { useModal } from '/@/components/Modal';
  import { propTypes } from '/@/utils/propTypes';
  import { useAttrs } from '/@/hooks/core/useAttrs';
  import { defHttp } from '/@/utils/http/axios';
  import { useMessage } from '/@/hooks/web/useMessage';
  //定义请求url信息
  const configUrl = reactive({
    getColumns: '/online/cgreport/api/getRpColumns/',
    getData: '/online/cgreport/api/getData/',
  });

  export default defineComponent({
    name: 'JPopupDict',
    components: {
      JPopupOnlReportModal,
    },
    inheritAttrs: false,
    props: {
      /**
       * 示例：demo,name,id
       * demo: online报表编码
       * name: online报表的字段，用户显示的label
       * id: online报表的字段，用于存储key
       */
      dictCode: propTypes.string.def(''),
      value: propTypes.string.def(''),
      sorter: propTypes.string.def(''),
      disabled: propTypes.bool.def(false),
      multi: propTypes.bool.def(false),
      param: propTypes.object.def({}),
      spliter: propTypes.string.def(','),
      getFormValues: propTypes.func,
      getContainer: propTypes.func,
      showAdvancedButton: propTypes.bool.def(true),
      useInput: propTypes.bool.def(false),
    },
    emits: ['update:value', 'register', 'change', 'options-change'],
    setup(props, { emit }) {
      const { createMessage } = useMessage();
      const attrs = useAttrs();
      const showText = ref<any>(props.multi ? [] : '');
      const showLabel = ref<string>('');
      const options = ref<any>([]);
      const cgRpConfigId = ref('');
      const loading = ref(false);
      let syncingFromProps = false;
      const code = props.dictCode.split(',')[0];
      const labelFiled = props.dictCode.split(',')[1];
      const valueFiled = props.dictCode.split(',')[2];
      if (!code || !valueFiled || !labelFiled) {
        createMessage.error('popupDict参数未正确配置!');
      }
      //注册model
      const [regModal, { openModal }] = useModal();

      /**
       * 打开pop弹出框
       */
      function handleOpen() {
        // 代码逻辑说明: 【TV360X-317】禁用后JPopup和JPopupdic还可以点击出弹窗
        //update-begin---wangshuai---date:20260104  for：[issue/1560]动态列后面无法接其它列，如动态列最后加汇总就有问题------------
        //update-end---author:wangshuai ---date:20260104  for：[issue/1560]动态列后面无法接其它列，如动态列最后加汇总就有问题------------
        if (props.disabled) {
          return;
        }
        echoRows.value = buildEchoRows();
        openModal(true);
      }

      // update-begin--author:liaozhiyang---date:20261029---for：【LHZP-3】popup字典编辑时弹窗勾选回显
      const echoRows = ref<any[]>([]);
      /**
       * 当前值转成字符串数组（存储字段可能是数字类型，统一按字符串比较）
       */
      function getValues(): string[] {
        const value = props.value;
        if (value === undefined || value === null || value === '') {
          return [];
        }
        const values = props.multi ? String(value).split(props.spliter) : [String(value)];
        return values.filter((v) => v !== '');
      }
      /**
       * 根据当前值生成弹窗回显的待匹配行 [{存储字段: 值}]，弹窗中多条匹配时勾选第一条
       */
      function buildEchoRows() {
        return getValues().map((v) => ({ [valueFiled]: v }));
      }
      /**
       * 按存储值反查显示文本（存储字段不唯一时取第一条，与列表后端翻译一致）
       */
      function translate(callBack) {
        cgRpConfigId.value ? loadData({ callBack }) : loadColumnsInfo({ callBack });
      }
      // update-end--author:liaozhiyang---date:20261029---for：【LHZP-3】popup字典编辑时弹窗勾选回显
      /**
       * liaozhiyang
       * 20260522
       * 【QQYUN-15493】新增a-input模式下
       * */
      function syncShowLabel() {
        if (!props.useInput) return;
        const currentValue = showText.value;
        if (props.multi) {
          const values = Array.isArray(currentValue) ? currentValue : currentValue ? currentValue.split(props.spliter) : [];
          showLabel.value = values
            .map((v) => {
              const opt = options.value.find((o: any) => String(o.value) === String(v));
              return opt?.text || v;
            })
            .join(props.spliter);
        } else {
          const opt = options.value.find((o: any) => String(o.value) === String(currentValue));
          showLabel.value = opt?.text || currentValue || '';
        }
      }
      /**
       * 监听value数值
       */
      /**
       * 根据 props.value 同步显示值（单向同步，不触发 update:value）
       */
      function syncFromProps() {
        syncingFromProps = true;
        // update-begin--author:liusq---date:20260720---for：【LHZP-1003】单表pop字典，设置后，编辑打开，回显数据陷入循环查询的死循环
        // 代码逻辑说明: 必须使用实时的props.value而非闭包捕获的val，否则在异步回调时
        // defaultValue与记录值不同时，多个callBack会交替覆盖formModel，导致无限刷新
        // update-begin--author:liaozhiyang---date:20261029---for：【LHZP-3】存储字段为数字时，显示值与options的value类型不一致导致不翻译
        const values = getValues();
        showText.value = props.multi ? values : values[0] ?? '';
        // update-end--author:liaozhiyang---date:20261029---for：【LHZP-3】存储字段为数字时，显示值与options的value类型不一致导致不翻译
        // update-end--author:liusq---date:20260720---for：【LHZP-1003】单表pop字典，设置后，编辑打开，回显数据陷入循环查询的死循环
        syncShowLabel();
        nextTick(() => {
          syncingFromProps = false;
        });
      }
      watch(
        () => props.value,
        () => {
          // 先回显已保存值，接口返回后再用加载到的 options 替换为显示文本，避免编辑弹窗短暂空白
          syncFromProps();
          if (props.value || props.defaultValue) {
            translate(syncFromProps);
          }
        },
        { immediate: true }
      );
      watch(
        () => showText.value,
        (val) => {
          // props 回显属于单向同步，不能再作为用户操作反向清空或覆盖表单值
          if (syncingFromProps) return;
          let result;
          if (props.multi) {
            result = val.join(',');
          } else {
            result = val;
          }
          nextTick(() => {
            emit('change', result);
            emit('update:value', result);
          });
        }
      );
      /**
       * 加载列信息
       */
      function loadColumnsInfo({ callBack }) {
        loading.value = true;
        let url = `${configUrl.getColumns}${code}`;
        defHttp
          .get({ url }, { isTransformResponse: false, successMessageMode: 'none' })
          .then((res) => {
            if (res.success) {
              cgRpConfigId.value = res.result.cgRpConfigId;
              loadData({ callBack });
            }
          })
          .catch((err) => {
            loading.value = false;
            callBack?.();
          });
      }
      function loadData({ callBack }) {
        loading.value = true;
        let url = `${configUrl.getData}${unref(cgRpConfigId)}`;
        defHttp
          .get(
            { url, params: { ['force_' + valueFiled]: props.value || props.defaultValue } },
            { isTransformResponse: false, successMessageMode: 'none' }
          )
          .then((res) => {
            let data = res.result;
            if (data.records?.length) {
              // update-begin--author:liaozhiyang---date:20261029---for：【LHZP-3】popup字典编辑时弹窗勾选回显
              // 存储字段不唯一时反查会返回多条，同一个值只取第一条的显示文本；value统一转字符串，与显示值类型一致
              const map = new Map();
              data.records.forEach((item) => {
                const key = String(item[valueFiled]);
                !map.has(key) && map.set(key, { value: key, text: item[labelFiled] });
              });
              options.value = [...map.values()];
              // update-end--author:liaozhiyang---date:20261029---for：【LHZP-3】popup字典编辑时弹窗勾选回显
            }
          })
          .finally(() => {
            loading.value = false;
            callBack?.();
          });
      }
      /**
       * 传值回调
       */
      function callBack(rows) {
        const dataOptions: any = [];
        const dataValue: any = [];
        let result;
        rows.forEach((item) => {
          dataOptions.push({ value: item[valueFiled], text: item[labelFiled] });
          dataValue.push(item[valueFiled]);
        });
        options.value = dataOptions;
        if (props.multi) {
          showText.value = dataValue;
          result = dataValue.join(props.spliter);
          //update-begin author:liaozhiyang date:20260522 for：【QQYUN-15493】新增a-input模式下
          if (props.useInput) {
            showLabel.value = dataOptions.map((o: any) => o.text).join(props.spliter);
          }
          //update-end author:liaozhiyang date:20260522 for：【QQYUN-15493】新增a-input模式下
        } else {
          showText.value = dataValue[0];
          result = dataValue[0];
          //update-begin author:liaozhiyang date:20260522 for：【QQYUN-15493】新增a-input模式下
          if (props.useInput) {
            showLabel.value = dataOptions[0]?.text || '';
          }
          //update-end author:liaozhiyang date:20260522 for：【QQYUN-15493】新增a-input模式下
        }
        // update-begin--author:liaozhiyang---date:20261029---for：【LHZP-3】popup字典编辑时弹窗勾选回显
        // 选择后需按存储值重新翻译
        const needTranslate = result != null && result !== '' && String(props.value ?? '') === String(result);
        // update-end--author:liaozhiyang---date:20261029---for：【LHZP-3】popup字典编辑时弹窗勾选回显
        nextTick(() => {
          emit('change', result);
          emit('update:value', result);
          emit('options-change', options.value);
          needTranslate && translate(syncFromProps);
        });
      }

      return {
        showText,
        showLabel,
        attrs,
        regModal,
        handleOpen,
        callBack,
        code,
        options,
        loading,
        echoRows,
        valueFiled,
      };
    },
  });
</script>
<style lang="less" scoped>
  // 代码逻辑说明: 【QQYUN-9260】必填模式下会影响到弹窗内antd组件的样式
  .JPopupDict {
    > .ant-form-item {
      display: none;
    }
  }
  .components-input-demo-presuffix {
    :deep(.ant-select-dropdown) {
      display: none !important;
    }
  }
  .components-input-demo-presuffix .anticon-close-circle {
    cursor: pointer;
    color: #ccc;
    transition: color 0.3s;
    font-size: 12px;
  }

  .components-input-demo-presuffix .anticon-close-circle:hover {
    color: #f5222d;
  }

  .components-input-demo-presuffix .anticon-close-circle:active {
    color: #666;
  }
</style>
