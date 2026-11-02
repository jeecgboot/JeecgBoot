<template>
  <BasicModal
    wrapClassName="field-extend-config-modal"
    v-bind="$attrs"
    title="字段扩展配置项"
    @register="registerModal"
    keyboard
    :canFullscreen="false"
    cancelText="关闭"
    @ok="handleSubmit"
  >
    <a-spin :spinning="spinningLoading">
      <BasicForm @register="registerForm">
        <template #switchOptions="{ model, field }">
          <SetSwitchOptions v-model:value="model[field]" />
        </template>
      </BasicForm>
    </a-spin>
  </BasicModal>
</template>

<script lang="ts">
  import { defineComponent, h, reactive, toRaw, toRefs } from 'vue';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import SetSwitchOptions from './SetSwitchOptions.vue';
  import { ref } from 'vue';
  import { BasicForm, useForm } from '/@/components/Form/index';
  import { pick } from 'lodash-es';
  import { LABELLENGTH } from '../util/constant';
  import { isArray } from '/@/utils/is';
  import { message, Tooltip } from 'ant-design-vue';
  export default defineComponent({
    name: 'FieldExtendJsonModal',
    components: {
      BasicModal,
      BasicForm,
      SetSwitchOptions,
    },
    emits: ['success', 'register'],
    setup(_p, { emit }) {
      const spinningLoading = ref(false);

      function initExtendConfig() {
        extendConfig.uploadnum = 0;
        extendConfig.showLength = '';
        extendConfig.popupMulti = true;
        extendConfig.multiple = false;
        extendConfig.multiSelect = true;
        extendConfig.store = '';
        extendConfig.text = '';
        extendConfig.orderRule = '';
        extendConfig.validateError = '';
        extendConfig.labelLength = LABELLENGTH;
        extendConfig.displayLevel = 'all';
        //是否标题字段
        extendConfig.isTitleField = 0;
      }

      const extendConfig = reactive({
        uploadnum: 0,
        showLength: '',
        popupMulti: true,
        multiple: false,
        store: '',
        text: '',
        multiSelect: true,
        orderRule: '',
        validateError: '',
        labelLength: LABELLENGTH,
        displayLevel: 'all',
        isTitleField: 0,
      });
      const fieldShowType = ref('');
      const rowKey = ref('');
      const sortFlag = ref('0');
      const dbType = ref('');
      //是否子表
      const isSubTable = ref(false);

      const formSchemas: any[] = [
        {
          label: 'rowKey',
          field: 'rowKey',
          component: 'Input',
          show: false,
        },
        {
          label: '文件上传数量',
          field: 'uploadnum',
          component: 'InputNumber',
          componentProps: {
            style: {
              width: '100%',
            },
          },
          ifShow: () => {
            return fieldShowType.value === 'file' || fieldShowType.value === 'image';
          },
        },
        {
          label: '是否多选',
          field: 'popupMulti',
          component: 'RadioGroup',
          defaultValue: true,
          componentProps: {
            options: [
              { label: '否', value: false },
              { label: '是', value: true },
            ],
          },
          ifShow: () => {
            return fieldShowType.value === 'popup' || fieldShowType.value === 'popup_dict';
          },
        },
        // update-begin--author:liaozhiyang---date:20260826---for:【LHZP-261】online下拉搜索支持多选
        {
          label: '是否多选',
          field: 'multiple',
          component: 'RadioGroup',
          defaultValue: false,
          componentProps: {
            options: [
              { label: '否', value: false },
              { label: '是', value: true },
            ],
          },
          ifShow: () => {
            return fieldShowType.value === 'sel_search';
          },
        },
        // update-end--author:liaozhiyang---date:20260826---for:【LHZP-261】online下拉搜索支持多选
        {
          label: '是否多选',
          field: 'multiSelect',
          component: 'RadioGroup',
          defaultValue: true,
          componentProps: {
            options: [
              { label: '否', value: false },
              { label: '是', value: true },
            ],
          },
          ifShow: () => {
            return fieldShowType.value === 'sel_user' || fieldShowType.value === 'sel_depart';
          },
        },
        {
          label: '存储字段',
          field: 'store',
          component: 'Input',
          ifShow: () => {
            return fieldShowType.value === 'sel_user' || fieldShowType.value === 'sel_depart';
          },
        },
        {
          label: '展示字段',
          field: 'text',
          component: 'Input',
          ifShow: () => {
            return fieldShowType.value === 'sel_user' || fieldShowType.value === 'sel_depart';
          },
        },
        {
          label: '默认排序',
          field: 'orderRule',
          component: 'RadioGroup',
          defaultValue: '',
          componentProps: {
            options: [
              { label: '降序', value: 'desc' },
              { label: '升序', value: 'asc' },
              { label: '不默认排序', value: '' },
            ],
          },
          ifShow: () => {
            // update-begin--author:liaozhiyang---date:20240523---for：【TV360X-308】Datetime、string、Date,int、BigDecimal、double类型默认展示排序功能
            return sortFlag.value === '1' || ['Datetime', 'string', 'Date', 'int', 'BigDecimal', 'double', 'long'].includes(dbType.value);
            // update-end--author:liaozhiyang---date:20240523---for：【TV360X-308】Datetime、string、Date,int、BigDecimal、double类型默认展示排序功能
          },
        },
        {
          label: '校验提示',
          field: 'validateError',
          component: 'Input',
          componentProps: {
            placeholder: '请输入校验提示文本',
          },
        },
        {
          label: () => h(Tooltip, { title: '显示文字的个数' }, { default: () => '查询label长度' }),
          field: 'labelLength',
          component: 'InputNumber',
          componentProps: {
            placeholder: '请输入label长度',
          },
        },
        {
          label: '是否固定',
          field: 'isFixed',
          component: 'RadioGroup',
          defaultValue: 0,
          componentProps: {
            options: [
              { label: '是', value: 1 },
              { label: '否', value: 0 },
            ],
          },
        },
        // update-begin--author:liaozhiyang---date:20240105---for：【QQYUN-7499】多列风格富文本、markdown增加独占一行功能
        {
          label: '是否独占一行',
          field: 'isOneRow',
          component: 'RadioGroup',
          defaultValue: false,
          componentProps: {
            options: [
              { label: '否', value: false },
              { label: '是', value: true },
            ],
          },
          ifShow: () => {
            return fieldShowType.value === 'markdown' || fieldShowType.value === 'umeditor';
          },
        },
        // update-end--author:liaozhiyang---date:20240105---for：【QQYUN-7499】多列风格富文本、markdown增加独占一行功能
        // update-begin--author:liaozhiyang---date:20240430---for：【issues/6094】online 日期(年月日)控件增加年、年月，年周，年季度等格式
        {
          label: '日期格式',
          field: 'picker',
          component: 'RadioGroup',
          defaultValue: 'default',
          componentProps: {
            options: [
              { label: '年', value: 'year' },
              { label: '年月', value: 'month' },
              { label: '年周', value: 'week' },
              { label: '年季度', value: 'quarter' },
              { label: '年月日', value: 'default' },
            ],
          },
          ifShow: () => {
            return fieldShowType.value === 'date';
          },
        },
        // update-end--author:liaozhiyang---date:20240430---for：【issues/6094】online 日期(年月日)控件增加年、年月，年周，年季度等格式
        // update-begin--author:liaozhiyang---date:20260204---for:【QQYUN-14694】online支持配置独立的省、市、县
        {
          label: '显示级别',
          field: 'displayLevel',
          component: 'RadioGroup',
          defaultValue: 'all',
          componentProps: {
            options: [
              { label: '省市区', value: 'all' },
              { label: '省', value: 'province' },
              { label: '市', value: 'city' },
              { label: '区', value: 'region' },
            ],
          },
          ifShow: () => {
            return fieldShowType.value === 'pca';
          },
        },
        // update-end--author:liaozhiyang---date:20260204---for:【QQYUN-14694】online支持配置独立的省、市、县
        //update-begin---author:liusq ---date:20260519  for：【QQYUN-13645】online表单允许设置某个字段是标题字段
        {
          label: '是否标题字段',
          field: 'isTitleField',
          component: 'RadioGroup',
          defaultValue: 0,
          componentProps: {
            options: [
              { label: '是', value: 1 },
              { label: '否', value: 0 },
            ],
            onChange: (e) => {
              const newValue = e?.target?.value ?? e;
              if (newValue === 1) {
                message.info('标题字段唯一，将覆盖其他已设置项');
              }
            },
          },
          ifShow: () => {
            //子表不配置标题字段
            return !isSubTable.value;
          },
        },
        //update-end---author:liusq ---date:20260519  for：【QQYUN-13645】online表单允许设置某个字段是标题字段
        // update-begin--author:liaozhiyang---date:20240308---for：【TV360X-25】扩展参数配置中增加开关是否选项配置
        {
          label: '开关值',
          field: 'switchOptions',
          component: 'Input',
          slot: 'switchOptions',
          ifShow: () => {
            return fieldShowType.value === 'switch';
          },
        },
        // update-end--author:liaozhiyang---date:20240308---for：【TV360X-25】扩展参数配置中增加开关是否选项配置
      ];

      // 表单配置
      const [registerForm, { validate, setFieldsValue, resetFields }] = useForm({
        schemas: formSchemas,
        showActionButtonGroup: false,
        labelAlign: 'right',
        labelWidth: 100,
      });

      const [registerModal, { closeModal }] = useModalInner(async (data) => {
        console.log('extend json', data);
        initExtendConfig();
        if (data.jsonStr) {
          let json = JSON.parse(data.jsonStr);
          // update-begin--author:liaozhiyang---date:20260826---for:【LHZP-261】online下拉搜索支持多选
          if (data.fieldShowType === 'sel_search' && json.multiple == null) {
            extendConfig.multiple = false;
          }
          // update-end--author:liaozhiyang---date:20260826---for:【LHZP-261】online下拉搜索支持多选
          // update-begin--author:liaozhiyang---date:20240524---for：【TV360X-519】开关设置默认值及兼容数组写法
          if (data.fieldShowType === 'switch') {
            // 兼容[0,1]写法
            if (isArray(json) && json.length == 2) {
              extendConfig.switchOptions = json;
            }
          }
          // update-end--author:liaozhiyang---date:20240524---for：【TV360X-519】开关设置默认值及兼容数组写法
          Object.keys(json).map((k) => {
            console.log('扩展参数:' + k + '=' + json[k]);
            extendConfig[k] = json[k];
          });
        } else {
          // update-begin--author:liaozhiyang---date:20240524---for：【TV360X-519】开关设置默认值及兼容数组写法
          // 开关设置默认值
          if (data.fieldShowType === 'switch') {
            extendConfig.switchOptions = ['Y', 'N'];
          }
          // update-end--author:liaozhiyang---date:20240524---for：【TV360X-519】开关设置默认值及兼容数组写法
        }
        fieldShowType.value = data.fieldShowType;
        rowKey.value = data.id;
        sortFlag.value = data.sortFlag;
        dbType.value = data.dbType;
        isSubTable.value = !!data.isSubTable;
        let temp = toRaw(extendConfig);
        await resetFields();
        await setFieldsValue({
          ...temp,
          rowKey: data.id,
        });
      });

      async function handleSubmit() {
        let data = await validate();
        console.log('datga', data);
        let type = fieldShowType.value;
        let obj: any = {};
        if (type === 'file' || type === 'image') {
          if (data.uploadnum && data.uploadnum > 0) {
            obj.uploadnum = data.uploadnum;
          }
        } else if (type === 'textarea' || type === 'text') {
          if (data.showLength && data.showLength > 0) {
            obj.showLength = data.showLength;
          }
        } else if (type === 'sel_user' || type === 'sel_depart') {
          obj = pick(data, 'store', 'text', 'multiSelect');
        } else if (type === 'popup'||type === 'popup_dict') {
          obj.popupMulti = data.popupMulti;
        } else if (type === 'sel_search') {
          obj.multiple = data.multiple;
        }
        // update-begin--author:liaozhiyang---date:20240523---for：【TV360X-308】Datetime、string、Date,int、BigDecimal、double类型默认展示排序功能
        if (data.orderRule) {
          obj.orderRule = data.orderRule;
        }
        // update-end--author:liaozhiyang---date:20240523---for：【TV360X-308】Datetime、string、Date,int、BigDecimal、double类型默认展示排序功能
        if (data.validateError) {
          obj.validateError = data.validateError;
        }
        if (data.labelLength) {
          obj.labelLength = data.labelLength;
        }
        // update-begin--author:liaozhiyang---date:20230818---for：【QQYUN-4161】列支持固定功能
        if (data.isFixed) {
          obj.isFixed = data.isFixed;
        }
        // update-end--author:liaozhiyang---date:20230818---for：【QQYUN-4161】列支持固定功能
        //update-begin---author:liusq ---date:20260519  for：【QQYUN-13645】online表单允许设置某个字段是标题字段
        if (!isSubTable.value && data.isTitleField) {
          obj.isTitleField = data.isTitleField;
        }
        //update-end---author:liusq ---date:20260519  for：【QQYUN-13645】online表单允许设置某个字段是标题字段
        // update-begin--author:liaozhiyang---date:20230105---for：【QQYUN-7499】多列风格富文本、markdown增加独占一行功能
        // update-begin--author:liaozhiyang---date:20260827---for：【LHZP-371】富文本和Markdown独占一行选择否后正确保存
        if ((type === 'markdown' || type === 'umeditor') && typeof data.isOneRow === 'boolean') {
          obj.isOneRow = data.isOneRow;
        }
        // update-end--author:liaozhiyang---date:20260827---for：【LHZP-371】富文本和Markdown独占一行选择否后正确保存
        // update-end--author:liaozhiyang---date:20230105---for：【QQYUN-7499】多列风格富文本、markdown增加独占一行功能
        if (data.picker) {
          obj.picker = data.picker;
        }
        // update-begin--author:liaozhiyang---date:20240522---for：【TV360X-25】扩展参数配置中增加开关是否选项配置
        if (data.switchOptions) {
          const arr = data.switchOptions.split(',');
          let Y = Number(arr[0]);
          let N = Number(arr[1]);
          // 都能转成number就转，否则就是string
          if (Number.isNaN(Y) || Number.isNaN(N)) {
            Y = arr[0];
            N = arr[1];
          }
          obj.switchOptions = [Y, N];
        }
        // update-end--author:liaozhiyang---date:20240522---for：【TV360X-25】扩展参数配置中增加开关是否选项配置
        if (data.displayLevel) {
          obj.displayLevel = data.displayLevel;
        }
        console.log('obj....', obj);
        for (let key in obj) {
          if (obj[key] === '') {
            delete obj[key];
          }
        }
        console.log(obj);
        emit('success', obj, data.rowKey);
        closeModal();
      }

      return {
        spinningLoading,
        registerModal,
        registerForm,
        fieldShowType,
        rowKey,
        isSubTable,
        handleSubmit,
        ...toRefs(extendConfig),
      };
    },
  });
</script>

<style scoped></style>
