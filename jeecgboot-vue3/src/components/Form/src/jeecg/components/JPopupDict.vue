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
        :selected="selected"
        :rowkey="valueFiled"
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
      const selected = ref([]);
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
        openModal(true);
      }
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
      watch(
        () => props.value,
        () => {
          const callBack = () => {
            syncingFromProps = true;
            // update-begin--author:liusq---date:20260720---for：【LHZP-1003】单表pop字典，设置后，编辑打开，回显数据陷入循环查询的死循环
            // 代码逻辑说明: 必须使用实时的props.value而非闭包捕获的val，否则在异步回调时
            // defaultValue与记录值不同时，多个callBack会交替覆盖formModel，导致无限刷新
            const currentValue = props.value;
            if (props.multi) {
              showText.value = currentValue && currentValue.length > 0 ? currentValue.split(props.spliter) : [];
            } else {
              showText.value = currentValue ?? '';
            }
            // update-end--author:liusq---date:20260720---for：【LHZP-1003】单表pop字典，设置后，编辑打开，回显数据陷入循环查询的死循环
            // update-begin--author:liaozhiyang---date:20260714---for：【LHZP-392】pop字典外面删除了，弹窗里面还是勾选
            const currentValues = (props.multi ? showText.value : showText.value ? [showText.value] : []).map((v: any) => String(v));
            selected.value = (selected.value || []).filter((r: any) => currentValues.includes(String(r[valueFiled])));
            // update-end--author:liaozhiyang---date:20260714---for：【LHZP-392】pop字典外面删除了，弹窗里面还是勾选
            syncShowLabel();
            nextTick(() => {
              syncingFromProps = false;
            });
          };
          // 先回显已保存值，接口返回后再用加载到的 options 替换为显示文本，避免编辑弹窗短暂空白
          callBack();
          if (props.value || props.defaultValue) {
            if (cgRpConfigId.value) {
              loadData({ callBack });
            } else {
              loadColumnsInfo({ callBack });
            }
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
        const forceValue = props.value || props.defaultValue;
        const forceValues = forceValue ? String(forceValue).split(props.spliter).filter(Boolean) : [];
        defHttp
          .get(
            {
              url,
              params: {
                ['force_' + valueFiled]: forceValue,
                // 报表接口默认只返回10条，按已选值数量请求，确保全部值都能回显。
                pageSize: Math.max(forceValues.length, 10),
              },
            },
            { isTransformResponse: false, successMessageMode: 'none' }
          )
          .then((res) => {
            let data = res.result;
            if (data.records?.length) {
              options.value = data.records.map((item) => {
                return { value: item[valueFiled], text: item[labelFiled] };
              });
              selected.value = data.records;
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
        nextTick(() => {
          emit('change', result);
          emit('update:value', result);
          emit('options-change', options.value);
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
        selected,
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
