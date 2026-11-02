<template>
  <div class="p-2">
    <a-spin :spinning="loading">
      <ErrorTip v-if="errorTip" :tip="errorTip" />
      <template v-else>
        <!--update-begin--author:liaozhiyang---date:20240316---for：【QQYUN-8426】图表查询区域适配暗黑色-->
        <Card v-if="showSearchField">
          <BasicForm @register="registerSearchForm" :model="searchFormModal" />
        </Card>
        <!--update-end--author:liaozhiyang---date:20240316---for：【QQYUN-8426】图表查询区域适配暗黑色-->
        <ErrorTip v-if="errors.resultIsEmpty" tip="没有查询到任何数据" />
        <ChartAutoRender v-else :chartsData="chartsData" />
      </template>
    </a-spin>
  </div>
</template>

<script lang="ts">
  import {computed, defineComponent, nextTick, provide, reactive, ref, watch} from 'vue';
  import { router } from '/@/router';
  import ErrorTip from './components/ErrorTip.vue';
  import ChartAutoRender from './components/render/ChartAutoRender.vue';
  import { BasicForm, useForm } from '/@/components/Form';
  import { onMountedOrActivated } from '/@/hooks/core/onMountedOrActivated';
  import { getChartsData, getParamsInfo } from '../graphreport.api';
  import { useParseFormSchemas } from './hooks/useParseFormSchemas';
  import { isArray } from '/@/utils/is';
  import { formatToDate } from '/@/utils/dateUtil';
  import { Card } from "ant-design-vue"

  // noinspection JSUnusedGlobalSymbols
  export default defineComponent({
    name: 'GraphreportAutoChart',
    components: { BasicForm, ErrorTip, ChartAutoRender, Card },
    props: {},
    setup() {
      provide('setErrorTip', setErrorTip);
      const $route = router.currentRoute;
      const loading = ref(false);
      const code = ref<string>();
      const errorTip = ref<Nullable<string>>(null);
      // 根据不同的运行时错误显示不同的提示信息
      const errors = reactive({ resultIsEmpty: false });
      const chartsData = ref();
      // 查询条件（map根据code隔离）
      const paramsMap = reactive({});
      // 用户自定义查询条件（map根据code隔离）
      const selfParamsMap = reactive({});
      // 查询条件form（map根据code隔离）
      const searchFormMap = reactive({});
      // 多页面params缓存
      const paramsRef = getPageComputed<string>(paramsMap, '');
      // 用户自定义查询条件
      const selfParamsRef = getPageComputed<Nullable<Recordable>>(selfParamsMap);
      // 查询条件form
      const searchFormModal = getPageComputed(searchFormMap, {});
      // 是否显示查询条件
      const showSearchField = ref(false);
      // 自适应列配置
      const adaptiveColProps = { xs: 24, sm: 12, md: 12, lg: 8, xl: 8, xxl: 6 };
      const [registerSearchForm, searchForm] = useForm({
        schemas: [],
        // 紧凑模式
        compact: true,
        // label默认宽度
        labelWidth: 120,
        // 按下回车后自动提交
        autoSubmitOnEnter: true,
        // 默认 row 配置
        rowProps: { gutter: 8 },
        // 默认 col 配置
        baseColProps: {
          ...adaptiveColProps,
        },
        labelCol: { xs: 24, sm: 8, md: 6, lg: 8, xl: 6, xxl: 6 },
        wrapperCol: {},
        // 是否显示 展开/收起 按钮
        showAdvancedButton: true,
        // 超过指定列数默认折叠
        autoAdvancedCol: 3,
        // 操作按钮配置
        actionColOptions: {
          ...adaptiveColProps,
          style: { textAlign: 'left' },
        },
        resetFunc: onReset,
        submitFunc: onSubmit,
      });

      // 【VUEN-1554】缓存路由走Activated，没缓存的走Mounted，均只走一次
      onMountedOrActivated(() => {
        code.value = $route.value.params.code as string;
      });

      // 刷新参数放到这里去触发，就可以刷新相同界面了
      watch(
        code,
        () => {
          selfParamsRef.value = null;
          loadChartsData();
        },
        { immediate: true }
      );

      function setErrorTip(tip: string | null) {
        errorTip.value = tip;
      }

      // 加载图表数据
      async function loadChartsData() {
        loading.value = true;
        if (!code.value) {
          loading.value = false;
          return false;
        }
        let params = { id: code.value, params: paramsRef.value };
        // 查询图表参数
        if (selfParamsRef.value == null) {
          selfParamsRef.value = {};
          try {
            let result = await getParamsInfo({ headId: params.id });
            if (result && result.length > 0) {
              for (let i of result) {
                selfParamsRef.value['self_' + i.paramName] = !$route.value.query[i.paramName] ? '' : $route.value.query[i.paramName];
              }
            }
          } catch {
            return;
          }
        }
        try {
          let result = await getChartsData({ ...params, ...selfParamsRef.value });
          let { head, data, items, dictOptions } = result;
          if (data && data.length === 0) {
            errors.resultIsEmpty = true;
          } else {
            errors.resultIsEmpty = false;
            chartsData.value = { head, data, items, dictOptions };
            parseChartsData();
          }
        } finally {
          loading.value = false;
        }
      }

      function getPageComputed<T = Recordable>(map, defVal?: T) {
        return computed<T>({
          get(): T {
            if (map[code.value!] == null) {
              map[code.value!] = defVal;
            }
            return map[code.value!];
          },
          set: (val: T) => (map[code.value!] = val),
        });
      }

      const { parseFormSchemas } = useParseFormSchemas(chartsData, showSearchField);

      // 解析图表数据
      async function parseChartsData() {
        let schemas = await parseFormSchemas();
        if (schemas && schemas.length>0) {
          nextTick(() => {
            searchForm.resetSchema(schemas);
          });
        }
      }

      async function onReset() {
        paramsRef.value = '';
        loadChartsData();
      }

      //表单提交
      async function onSubmit() {
        const data = await searchForm.validate();
        let { items } = chartsData.value;
        let params: Recordable[] = [];
        for (let field of items) {
          if (field.searchFlag !== 'Y') continue;
          let values: any = null;
          if (!field.searchMode) field.searchMode = 'single';
          if (field.searchMode === 'single') {
            // 单条件查询
            values = data[field.fieldName];
          } else {
            // 范围查询
            let _begin = data[field.fieldName];
            let _end = data[`${field.fieldName}_end`];
            values = [_begin, _end];
          }
          // 日期格式化
          if (field.fieldType === 'Date') {
            values = formatDate(values);
          }
          // 封装成 params
          if (values != null && (values[0] != null || values[1] != null)) {
            params.push({
              value: values,
              fieldTxt: field.fieldTxt,
              fieldName: field.fieldName,
              fieldType: field.fieldType,
              searchMode: field.searchMode,
            });
          }
        }
        paramsRef.value = encodeURIComponent(JSON.stringify(params));
        loadChartsData();
      }

      // 格式化日期
      function formatDate(values) {
        if (values) {
          if (isArray(values)) {
            if (values[0]) {
              values[0] = formatToDate(values[0]);
            }
            if (values[1]) {
              values[1] = formatToDate(values[1]);
            }
          } else {
            return formatToDate(values);
          }
        }
        return values;
      }

      return {
        loading,
        errors,
        errorTip,
        chartsData,
        showSearchField,
        searchFormModal,
        registerSearchForm,
      };
    },
  });
</script>

<style lang="less" scoped>
  .ant-card {
    margin-bottom: 20px;
    :deep(.ant-card-body) {
      padding: 10px 20px;
    }

    // update-begin--author:liaozhiyang---date:20260804---for：【LHZP-1033】图表查询日期/数字宽度适配
    :deep(.ant-picker),
    :deep(.ant-calendar-picker),
    :deep(.ant-input-number) {
      width: 100%;
    }

    // 范围查询：横向排列，左右各占一半
    :deep(.range-query) {
      .range-query-inner {
        display: flex;
        align-items: center;
        width: 100%;
        gap: 4px;

        .range-span {
          flex: none;
          width: 12px;
          text-align: center;
          line-height: 32px;
        }

        .ant-picker,
        .ant-calendar-picker,
        .ant-input,
        .ant-input-number {
          flex: 1;
          width: auto !important;
          min-width: 0;
        }
      }
    }
    // update-end--author:liaozhiyang---date:20260804---for：【LHZP-1033】图表查询日期/数字宽度适配
  }
</style>
