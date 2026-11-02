<template>
  <BasicModal
    :title="title"
    :width="1200"
    :maskClosable="false"
    :confirmLoading="confirmLoading"
    defaultFullscreen
    v-bind="$attrs"
    @cancel="onCancel"
    @register="registerModal"
  >
    <a-spin :spinning="confirmLoading">
      <BasicForm @register="registerForm">
        <template #SQLAnalyzeButton>
          <div style="flex: 1; text-align: right">
            <a-button v-if="dataType === 'sql'" type="primary" @click="onSQLAnalyze">SQL解析</a-button>
            <a-button v-if="dataType === 'json'" type="primary" @click="onJSONAnalyze">JSON解析</a-button>
            <a-button v-if="dataType === 'api'" type="primary" @click="onAPIAnalyze">API解析</a-button>
          </div>
        </template>
      </BasicForm>
      <!-- 子表单区域 -->
      <a-tabs animated v-model:activeKey="activeKey">
        <a-tab-pane tab="列表字段" key="field" forceRender>
          <FieldTable ref="fieldTableRef" :dataSource="fieldTable.dataSource" :disableSearch="dataType === 'json'" />
        </a-tab-pane>
        <a-tab-pane tab="图表参数" key="params" forceRender>
          <ParamsTable ref="paramsTableRef" :dataSource="paramsTable.dataSource" />
        </a-tab-pane>
      </a-tabs>
    </a-spin>

    <template #footer>
      <a-button @click="onCancel">关闭</a-button>
      <a-button type="primary" :loading="confirmLoading" preIcon="ant-design:save" @click="onSubmit">保存</a-button>
    </template>
  </BasicModal>
</template>

<script lang="ts">
  import { ref, reactive, computed, nextTick, defineComponent } from 'vue';

  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { BasicForm, useForm } from '/@/components/Form/index';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { useFormSchemas } from '../graphreport.data';
  import FieldTable from './tables/FieldTable.vue';
  import ParamsTable from './tables/ParamsTable.vue';
  import { defHttp } from '/@/utils/http/axios';
  import { isArray } from '/@/utils/is';
  import { parseField } from '/@/views/super/online/graphreport/graphreport.api';

  enum Api {
    add = '/online/graphreport/head/add',
    edit = '/online/graphreport/head/edit',
    sql = '/online/cgreport/head/parseSql',
  }

  enum FieldApi {
    list = '/online/graphreport/head/queryOnlGraphreportItemByMainId',
  }

  enum ParamsApi {
    list = '/online/graphreport/params/listByHeadId',
  }

  export default defineComponent({
    name: 'GraphreportModal',
    components: { ParamsTable, FieldTable, BasicModal, BasicForm },
    emits: ['register', 'success'],
    setup(props, { emit }) {
      const { createMessage: $message } = useMessage();
      // 是否是更新模式
      const isUpdate = ref(false);
      const fieldTableRef = ref<InstanceType<typeof FieldTable>>();
      const paramsTableRef = ref<InstanceType<typeof ParamsTable>>();
      // 编辑时存储的值
      let model: Recordable = {};
      const title = computed(() => (isUpdate.value ? '编辑' : '新增'));
      // tabs当前活动的页面
      const activeKey = ref('field');
      // 当前是否正在加载中
      const confirmLoading = ref(false);
      // 数据源类型
      const dbSourceOptions = ref<any[]>([]);

      const fieldTable = reactive({
        dataSource: [],
      });
      const paramsTable = reactive({
        dataSource: [],
      });

      //  注册弹窗
      const [registerModal, { closeModal }] = useModalInner(async (data) => {
        isUpdate.value = data?.isUpdate ?? false;
        if (isUpdate.value) {
          await edit(data?.record);
        } else {
          await add();
        }

        // 当传递了 aigc 时，说明是通过 ai 生成的报表
        if (data?.aigc) {
          console.log('top - data.aigc :', data.aigc)
          await setFieldsValue({
            ...data.aigc,
          });
          // 等待 0.5s 后，自动执行SQL解析
          setTimeout(() => onSQLAnalyze())
        }
      });
      const { formSchemas, dataType, isCombination } = useFormSchemas(props, {
        dbSourceOptions,
      });
      // 注册表单
      const [registerForm, { resetFields, clearValidate, setFieldsValue, validate, validateFields }] = useForm({
        schemas: formSchemas,
        showActionButtonGroup: false,
        labelAlign: 'right',
      });

      async function add() {
        // 清空数据
        fieldTable.dataSource = [];
        paramsTable.dataSource = [];
        await nextTick();
        // 默认添加一行
        fieldTableRef.value?.tableRef?.addRows({}, { setActive: false });
        edit({});
      }

      async function edit(record) {
        confirmLoading.value = false;
        activeKey.value = 'field';
        // 重置表单
        await resetFields();
        model = Object.assign({}, record);
        model.dbSource = model.dbSource == null ? '' : model.dbSource;
        // 如果是组合模式，那么就分割字符串成数组
        if (isCombination.value === 'combination' && typeof model.graphType === 'string') {
          model.graphType = model.graphType.split(',');
        }
        if (typeof model.yaxisField === 'string') {
          model.yaxisField = model.yaxisField.split(',');
        }
        // 给表单赋值
        await setFieldsValue(model);
        await clearValidate();
        // 处理数据
        dataType.value = model.dataType ?? 'sql';
        // isCombination.value = model.isCombination ?? 'combination'
        // 加载子表数据
        if (model.id) {
          requestSubData(model.id);
        }
      }

      /** 查询子表数据 */
      function requestSubData(headId) {
        confirmLoading.value = true;
        requestTabData(FieldApi.list, { id: headId }, fieldTable).finally(() => {
          confirmLoading.value = false;
        });
        requestTabData(ParamsApi.list, { headId }, paramsTable);
      }

      /** 查询某个tab的数据 */
      function requestTabData(url, params, tab) {
        return defHttp.get({ url, params }).then((result) => {
          tab.dataSource = result || [];
        });
      }

      /** SQL 解析 */
      async function onSQLAnalyze() {
        confirmLoading.value = true;
        try {
          let { cgrSql, dbSource } = await validate(['cgrSql', 'dbSource']);
          let result = await defHttp.get({ url: Api.sql, params: { sql: cgrSql, dbKey: dbSource || '' } });
          $message.success('解析成功');
          let newData = (result.fields || []).map((item) => {
            item.isShow = item.isShow === 1 ? 'Y' : 'N';
            return item;
          });
          let oldData = fieldTableRef.value?.tableRef?.getTableData();
          fieldTable.dataSource = getTableData(oldData, newData, 'fieldName');
          // 解析参数
          paramsTable.dataSource = getTableData(paramsTable.dataSource, result.params || [], 'paramName');
        } finally {
          confirmLoading.value = false;
        }
      }

      /** JSON 解析 */
      async function onJSONAnalyze() {
        await doBackAnalyze('JSON');
      }

      /** API 解析 */
      async function onAPIAnalyze() {
        await doBackAnalyze('API');
      }

      /**
       * 后台解析字段
       *
       * @param type 解析类型
       */
      async function doBackAnalyze(type: string) {
        let { cgrSql } = await validateFields(['cgrSql']);
        let result = await parseField(type, cgrSql);
        let fields = Array.isArray(result) ? result: result.fields;
        let newData = (fields || []).map((item) => {
          item.isShow = item.isShow === 1 ? 'Y' : 'N';
          return item;
        });
        let oldData = fieldTableRef.value?.tableRef?.getTableData();
        fieldTable.dataSource = getTableData(oldData, newData, 'fieldName');
        // 解析参数
        if (Array.isArray(result?.params)) {
          paramsTable.dataSource = getTableData(paramsTable.dataSource, result.params, 'paramName');
        }
      }

      /** 【重写】确定按钮点击事件 */
      async function onSubmit() {
        try {
          // 校验主表
          let mainValues = await validate();
          // 校验字段表
          let errMap = await fieldTableRef.value?.tableRef?.validateTable();
          if (errMap) {
            activeKey.value = 'field';
            return;
          }
          // 校验参数表
          errMap = await paramsTableRef.value?.tableRef?.validateTable();
          if (errMap) {
            activeKey.value = 'params';
            return;
          }
          // 整理数据
          let formData = classifyIntoFormData(mainValues);
          // 发起请求
          confirmLoading.value = true;
          await request(formData);
          emit('success');
          closeModal();
        } finally {
          confirmLoading.value = false;
        }
      }

      /** 发起请求，自动判断是执行新增还是修改操作 */
      function request(params) {
        if (isUpdate.value) {
          return defHttp.put({ url: Api.edit, params });
        } else {
          return defHttp.post({ url: Api.add, params });
        }
      }

      /** 整理成formData */
      function classifyIntoFormData(mainValues) {
        let main = Object.assign(model, mainValues);
        // update-begin--author:liaozhiyang---date:20260824---for：【LHZP-1055】online图表JSON方式禁用查询
        const fieldData = fieldTableRef.value?.tableRef?.getTableData() || [];
        if (main.dataType === 'json') {
          fieldData.forEach((item) => (item.searchFlag = 'N'));
        }
        // update-end--author:liaozhiyang---date:20260824---for：【LHZP-1055】online图表JSON方式禁用查询
        if (isArray(main.graphType)) {
          main.graphType = main.graphType.join(',');
        }
        if (isArray(main.yaxisField)) {
          main.yaxisField = main.yaxisField.join(',');
        }
        return {
          ...main,
          onlGraphreportItemList: fieldData,
          paramsList: paramsTableRef.value?.tableRef?.getTableData(),
        };
      }

      function onCancel() {
        closeModal();
      }

      // 查询数据源类型
      async function queryDataSourceOptions() {
        let url = '/sys/dataSource/options';
        try {
          let result = await defHttp.get({ url });
          dbSourceOptions.value = result || [];
          dbSourceOptions.value.unshift({ label: '请选择', value: '' });
        } catch {
          dbSourceOptions.value = [{ label: '加载失败', value: undefined }];
        }
      }

      queryDataSourceOptions();

      return {
        fieldTableRef,
        paramsTableRef,
        title,
        confirmLoading,
        dataType,
        activeKey,
        fieldTable,
        paramsTable,
        onSubmit,
        onCancel,
        onSQLAnalyze,
        onAPIAnalyze,
        onJSONAnalyze,
        registerModal,
        registerForm,
      };
    },
  });

  /**
   * 获取tab的table的datasource
   * @param oldData 原datasource
   * @param newData 从数据库查询出来的datasource
   * @param columnName 字段列，用于比较数据是否相同
   */
  function getTableData(oldData, newData, columnName) {
    newData.forEach((newItem) => {
      for (let oldItem of oldData) {
        if (oldItem[columnName] === newItem[columnName]) {
          Object.assign(newItem, oldItem);
          break;
        }
      }
    });
    return newData;
  }
</script>

<style lang="less" scoped>
  // update-begin--author:liaozhiyang---date:20240509---for：【QQYUN-9230】报表图表弹窗样式调整
  form {
    padding-right: 3.5%;
  }
  :deep(.ant-tabs-content-holder) {
    padding: 0 10px;
  }
  // update-begin--author:liaozhiyang---date:20240509---for：【QQYUN-9230】报表图表弹窗样式调整
</style>
