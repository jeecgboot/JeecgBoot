<!--popup组件-->
<template>
  <div class="JPopup components-input-demo-presuffix" v-if="avalid">
    <!--输入框-->
    <a-input @click="handleOpen" @update:value="handleValueChange" :value="isQueryingShowText ? '加载中...' : (innerShowText || showText)" :placeholder="placeholder" readOnly v-bind="attrs">
      <template #prefix>
        <Icon v-if="!isQueryingShowText" icon="ant-design:cluster-outlined"></Icon>
        <Icon v-else icon="ant-design:loading-outlined" spin></Icon>
      </template>
      <!-- <template #suffix>
                <Icon icon="ant-design:close-circle-outlined" @click="handleEmpty" title="清空" v-if="showText"></Icon>
            </template>-->
    </a-input>
    <a-form-item>
      <!--popup弹窗-->
      <JPopupOnlReportModal
        @register="regModal"
        :code="code"
        :multi="multi"
        :sorter="sorter"
        :groupId="uniqGroupId"
        :param="param"
        :showAdvancedButton="showAdvancedButton"
        :getContainer="getContainer"
        :getFormValues="getFormValues"
        :echoRows="echoRows"
        @ok="callBack"
      ></JPopupOnlReportModal>
    </a-form-item>
  </div>
</template>
<script lang="ts">
  import JPopupOnlReportModal from './modal/JPopupOnlReportModal.vue';
  import { defineComponent, ref, reactive, onMounted, watchEffect, watch, computed, unref } from 'vue';
  import { useModal } from '/@/components/Modal';
  import { propTypes } from '/@/utils/propTypes';
  import { useAttrs } from '/@/hooks/core/useAttrs';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { defHttp } from '/@/utils/http/axios';

  // 定义请求 url 信息
  const configUrl = reactive({
    getColumns: '/online/cgreport/api/getRpColumns/',
    getData: '/online/cgreport/api/getData/',
  });

  export default defineComponent({
    name: 'JPopup',
    components: {
      JPopupOnlReportModal,
    },
    inheritAttrs: false,
    props: {
      code: propTypes.string.def(''),
      value: propTypes.oneOfType([propTypes.string, propTypes.number]).def(''),
      sorter: propTypes.string.def(''),
      width: propTypes.number.def(1200),
      placeholder: propTypes.string.def('请选择'),
      multi: propTypes.bool.def(false),
      param: propTypes.object.def({}),
      spliter: propTypes.string.def(','),
      groupId: propTypes.string.def(''),
      formElRef: propTypes.object,
      setFieldsValue: propTypes.func,
      getFormValues: propTypes.func,
      getContainer: propTypes.func,
      fieldConfig: {
        type: Array,
        default: () => [],
      },
      showAdvancedButton: propTypes.bool.def(true),
      // 是否是在 筛选（search） 中使用
      inSearch: propTypes.bool.def(false),
    },
    emits: ['update:value', 'register', 'popUpChange', 'focus'],
    setup(props, { emit, refs }) {
      const { createMessage } = useMessage();
      const attrs = useAttrs();
      //pop是否展示
      const avalid = ref(true);
      const showText = ref('');
      const innerShowText = ref('')
      const loading = ref(false);
      const isQueryingShowText = ref(false);
      const cgRpConfigId = ref('');
      // 编辑回显：弹窗待匹配的已选行
      const echoRows = ref<any[]>([]);
      //注册model
      const [regModal, { openModal }] = useModal();
      //表单值
      let {code, fieldConfig } = props;
      //唯一分组groupId
      const uniqGroupId = computed(() => (props.groupId ? `${props.groupId}_${code}_${fieldConfig[0]['source']}_${fieldConfig[0]['target']}` : ''));
      /**
       * 判断popup配置项是否正确
       */
      onMounted(() => {
        if (props.fieldConfig.length == 0) {
          createMessage.error('popup参数未正确配置!');
          avalid.value = false;
        }
      });
      // update-begin--author:liaozhiyang---date:20261029---for：【LHZP-2558】popup存储的值如果是数值不回显
      /**
       * value 转成显示文本（单选回填时会保留数字类型）
       */
      function getValueText(val) {
        if (val === undefined || val === null || val === '') {
          return '';
        }
        return String(val).split(props.spliter).join(',');
      }
      // update-end--author:liaozhiyang---date:20261029---for：【LHZP-2558】popup存储的值如果是数值不回显
      /**
       * 监听value数值
       */
      watch(
        () => props.value,
        (val) => {
          const text = getValueText(val);
          if (!text) {
            innerShowText.value = '';
          }
          // 反查期间先不显示 id，等接口返回后再决定显示文本或回退显示 value
          if (!isQueryingShowText.value) {
            showText.value = text;
          }
          // 筛选场景下组件重建后 innerShowText 会丢失，需要根据 value 反查 online 报表文本回显
          if (props.inSearch && text && !innerShowText.value) {
            queryShowTextByValue(String(val));
          }
        },
        { immediate: true }
      );

      /**
       * 根据 value 反查显示文本
       */
      function queryShowTextByValue(val) {
        if (!props.code || props.fieldConfig.length === 0) {
          return;
        }
        const sourceField = props.fieldConfig[0]['source'];
        const labelField = props.fieldConfig[0]['label'];
        if (!sourceField || !labelField) {
          return;
        }
        // 反查期间显示 loading，并先清空 showText 避免闪现 id
        isQueryingShowText.value = true;
        showText.value = '';
        const doQuery = () => {
          loadDataForShowText(val, sourceField, labelField);
        };
        if (cgRpConfigId.value) {
          doQuery();
        } else {
          loadColumnsInfo({ callBack: doQuery });
        }
      }

      /**
       * 加载列信息获取 cgRpConfigId
       */
      function loadColumnsInfo({ callBack }) {
        loading.value = true;
        let url = `${configUrl.getColumns}${props.code}`;
        defHttp
          .get({ url }, { isTransformResponse: false, successMessageMode: 'none' })
          .then((res) => {
            if (res.success) {
              cgRpConfigId.value = res.result.cgRpConfigId;
              callBack?.();
            }
          })
          .catch((err) => {
            console.error('【JPopup】加载报表列信息失败', err);
          })
          .finally(() => {
            loading.value = false;
            // 列信息加载失败时恢复显示 value，并关闭 loading
            if (!cgRpConfigId.value) {
              isQueryingShowText.value = false;
              showText.value = getValueText(props.value);
            }
          });
      }

      /**
       * 根据 value 加载显示文本
       */
      function loadDataForShowText(val, sourceField, labelField) {
        loading.value = true;
        let url = `${configUrl.getData}${unref(cgRpConfigId)}`;
        defHttp
          .get(
            { url, params: { ['force_' + sourceField]: val } },
            { isTransformResponse: false, successMessageMode: 'none' }
          )
          .then((res) => {
            let data = res.result;
            if (data.records?.length) {
              const labels = data.records.map((item) => item[labelField]);
              innerShowText.value = labels.join(',');
            }
          })
          .catch((err) => {
            console.error('【JPopup】根据 value 反查显示文本失败', err);
          })
          .finally(() => {
            loading.value = false;
            // 反查结束关闭 loading；若未查到文本则回退显示 value
            isQueryingShowText.value = false;
            if (!innerShowText.value) {
              showText.value = getValueText(props.value);
            }
          });
      }

      /**
       * 打开pop弹出框
       */
      function handleOpen() {
        emit('focus');
        // 代码逻辑说明: 【TV360X-317】禁用后JPopup和JPopupdic还可以点击出弹窗
        if (attrs.value.disabled) {
          return;
        }
        // update-begin--author:liaozhiyang---date:20261029---for：【LHZP-3】popup编辑时弹窗勾选回显
        echoRows.value = buildEchoRows();
        // update-end--author:liaozhiyang---date:20261029---for：【LHZP-3】popup编辑时弹窗勾选回显
        openModal(true);
      }

      /**
       * 根据表单中各回填字段的值，按位置还原出已选行 [{源字段: 值}]，用于弹窗勾选回显
       */
      function buildEchoRows() {
        let formValues: Recordable = {};
        try {
          formValues = props.getFormValues?.() ?? props.formElRef?.getFieldsValue?.() ?? {};
        } catch (e) {
          formValues = {};
        }
        // 取不到表单值时（如高级查询），至少用自身的值
        const codeField = attrs.value.codeField;
        if (codeField && formValues[codeField] === undefined) {
          formValues = { ...formValues, [codeField]: props.value };
        }
        const columns: { source: string; values: string[] }[] = [];
        props.fieldConfig.forEach((item: any) => {
          const target = item.target.split(',')[0];
          const val = formValues[target];
          // 表单中没有该字段（如无权限隐藏）则不参与匹配
          if (val === undefined) {
            return;
          }
          const text = val === null ? '' : String(val);
          // 单选时整体作为一个值，多选时按分隔符拆开
          columns.push({ source: item.source, values: props.multi ? text.split(props.spliter) : [text] });
        });
        // 所有字段都为空说明没有已选数据
        if (!columns.some((col) => col.values.some((v) => v !== ''))) {
          return [];
        }
        const count = Math.max(...columns.map((col) => col.values.length));
        const rows: Recordable[] = [];
        for (let i = 0; i < count; i++) {
          const row = {};
          columns.forEach((col) => {
            // 值为空的字段不参与匹配（如回填字段被清空，只剩存储字段）
            if (col.values[i] !== undefined && col.values[i] !== '') {
              row[col.source] = col.values[i];
            }
          });
          // 整行都为空的不参与匹配
          Object.keys(row).length && rows.push(row);
        }
        return rows;
      }

      /**
       * TODO 清空
       */
      function handleEmpty() {
        showText.value = '';
      }

      /**
       * 输入框值改变事件（只读模式下用于处理 allowClear）
       */
      function handleValueChange(value) {
        emit('update:value', value);
        if (value) {
          return;
        }
        innerShowText.value = '';
        const values = {};
        props.fieldConfig.forEach((item) => {
          item.target.split(',').forEach((target) => {
            values[target] = '';
          });
        });
        props.formElRef && props.formElRef.setFieldsValue(values);
        props.setFieldsValue && props.setFieldsValue(values);
        emit('popUpChange', values);
      }

      /**
       * 传值回调
       */
      function callBack(rows) {
        let { fieldConfig } = props;
        //匹配popup设置的回调值
        let values = {};
        let labels = []
        for (let item of fieldConfig) {
          let val = rows.map((row) => row[item.source]);
          // 代码逻辑说明: 【QQYUN-7535】数组只有一个且是number类型，join会改变值的类型为string
          val = val.length == 1 ? val[0] : val.join(',');
          item.target.split(',').forEach((target) => {
            values[target] = val;
          });

          if (props.inSearch) {
            // 处理显示值
            if (item.label) {
              let txt = rows.map((row) => row[item.label]);
              txt = txt.length == 1 ? txt[0] : txt.join(',');
              labels.push(txt);
            } else {
              labels.push(val);
            }
          }

        }
        innerShowText.value = labels.join(',');
        //传入表单示例方式赋值
        props.formElRef && props.formElRef.setFieldsValue(values);
        //传入赋值方法方式赋值
        props.setFieldsValue && props.setFieldsValue(values);
        // 代码逻辑说明: 【issues/5213】JPopup抛出change事件
        emit('popUpChange', values);
      }

      return {
        showText,
        innerShowText,
        avalid,
        uniqGroupId,
        loading,
        isQueryingShowText,
        attrs,
        regModal,
        echoRows,
        handleOpen,
        handleEmpty,
        handleValueChange,
        callBack,
      };
    },
  });
</script>
<style lang="less" scoped>
  // 代码逻辑说明: 【QQYUN-9260】必填模式下会影响到弹窗内antd组件的样式
  .JPopup {
    > .ant-form-item {
      display: none;
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
