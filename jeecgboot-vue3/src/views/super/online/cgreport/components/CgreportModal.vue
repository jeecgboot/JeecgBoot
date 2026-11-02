<template>
  <BasicModal
    v-bind="$attrs"
    @register="registerModal"
    :title="title"
    :width="1200"
    :maskClosable="false"
    :defaultFullscreen="true"
    :confirmLoading="confirmLoading"
    @ok="handleSubmit"
  >
    <!-- update-begin--author:liaozhiyang---date:20260824---for：【LHZP-984】online报表新增定位功能 -->
    <template #insertFooter>
      <div class="positioning-area">
        <a-input v-model:value="positioning" placeholder="搜索字段或备注" allowClear @pressEnter="handlePositioning" />
        <a-button type="primary" ghost @click="handlePositioning">定位</a-button>
      </div>
    </template>
    <!-- update-end--author:liaozhiyang---date:20260824---for：【LHZP-984】online报表新增定位功能 -->
    <BasicForm @register="registerForm" ref="formRef">
      <template #analyseButton>
        <div style="flex: 1; text-align: left">
          <a-popover title="使用指南" trigger="hover" style="margin: 0 10px 0 6px">
            <template #content>
              您可以键入“”作为一个参数，这里abc是参数的名称。例如：<br />
              select * from table where id = ${abc}。<br />
              select * from table where id like concat('%',${abc},'%')。(mysql模糊查询)<br />
              select * from table where id like '%'||${abc}||'%'。(oracle模糊查询)<br />
              select * from table where id like '%'+${abc}+'%'。(sqlserver模糊查询)<br />
              <span style="color: red">注：参数只支持动态报表，popup暂不支持</span>
            </template>
            <a-icon type="question-circle" />
          </a-popover>
          <a-button style="margin-left: 10px" type="primary" @click="handleSQLAnalyze">SQL解析</a-button>
        </div>
      </template>
    </BasicForm>
    <a-divider style="margin: 1px 0" class="cust-divider"></a-divider>
    <!-- 子表单区域 -->
    <a-tabs v-model:activeKey="activeKey" animated @change="handleChangeTabs">
      <a-tab-pane tab="动态报表配置明细" :key="refKeys[0]" :forceRender="true">
        <JVxeTable
          keep-source
          dragSort
          resizable
          ref="onlCgreportItem"
          :loading="onlCgreportItemTable.loading"
          :columns="onlCgreportItemTable.columns"
          :dataSource="onlCgreportItemTable.dataSource"
          :height="390"
          :rowNumber="true"
          :rowSelection="true"
          dragSortFixed="none"
          rowNumberFixed="none"
          rowSelectionFixed="none"
          :toolbar="true"
          :scrollX="{ enabled: true, gt: 0 }"
          :scrollY="{ enabled: true, gt: 10 }"
        />
      </a-tab-pane>

      <a-tab-pane tab="报表参数" :key="refKeys[1]" :forceRender="true">
        <JVxeTable
          keep-source
          resizable
          dragSort
          ref="onlCgreportParam"
          :loading="onlCgreportParamTable.loading"
          :columns="onlCgreportParamTable.columns"
          :dataSource="onlCgreportParamTable.dataSource"
          :height="390"
          :rowNumber="true"
          :rowSelection="true"
          dragSortFixed="none"
          rowNumberFixed="none"
          rowSelectionFixed="none"
          :toolbar="true"
          :scrollX="{ enabled: true, gt: 0 }"
          :scrollY="{ enabled: true, gt: 10 }"
        />
      </a-tab-pane>
    </a-tabs>
  </BasicModal>
</template>

<script lang="ts" setup>
  import { ref, computed, unref, reactive, nextTick } from 'vue';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { BasicForm, useForm } from '/@/components/Form/index';
  import { useJvxeMethod } from '/@/hooks/system/useJvxeMethods';
  import { formSchema, onlCgreportParamColumns, onlCgreportItemColumns } from '../cgreport.data';
  import { saveOrUpdate, onlCgreportParamList, onlCgreportItemList, analyzeSql } from '../cgreport.api';
  import { useMessage } from '/@/hooks/web/useMessage';
  const { createMessage, createInfoModal } = useMessage();
  // Emits声明
  const emit = defineEmits(['register', 'success']);
  const isUpdate = ref(true);
  // 当前是否正在加载中
  const confirmLoading = ref(true);
  const refKeys = ref(['onlCgreportItem', 'onlCgreportParam']);
  const activeKey = ref('onlCgreportItem');
  const positioning = ref('');
  const onlCgreportParam = ref();
  const onlCgreportItem = ref();
  const tableRefs = { onlCgreportItem, onlCgreportParam };
  // 行编辑表格未就绪时，每次重试的等待间隔（毫秒）
  const TABLE_READY_RETRY_INTERVAL = 50;
  // 行编辑表格初始化的最大重试次数，避免无限等待
  const TABLE_READY_MAX_RETRY_COUNT = 50;
  const onlCgreportParamTable = reactive({
    loading: false,
    dataSource: <any[]>[],
    columns: onlCgreportParamColumns,
  });
  const onlCgreportItemTable = reactive({
    loading: false,
    dataSource: <any[]>[],
    columns: onlCgreportItemColumns,
  });
  //表单配置
  const [registerForm, { setProps, resetFields, setFieldsValue, validate, validateFields }] = useForm({
    // labelWidth: 150,
    schemas: formSchema,
    showActionButtonGroup: false,
    // update-begin--author:liaozhiyang---date:20240509---for：【QQYUN-9230】报表图表弹窗样式调整
    labelWidth: 100,
    wrapperCol: null
    // update-end--author:liaozhiyang---date:20240509---for：【QQYUN-9230】报表图表弹窗样式调整
  });
  //表单赋值
  const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
    //重置表单
    await reset();
    setModalProps({ confirmLoading: false, showCancelBtn: data?.showFooter, showOkBtn: data?.showFooter });
    isUpdate.value = !!data?.isUpdate;
    if (unref(isUpdate)) {
      //表单赋值
      await setFieldsValue({
        ...data.record,
      });
      requestSubTableData(onlCgreportParamList, { headId: data?.record?.id }, onlCgreportParamTable);
      requestSubTableData(onlCgreportItemList, { headId: data?.record?.id }, onlCgreportItemTable);
    }
    // 隐藏底部时禁用整个表单
    setProps({ disabled: !data?.showFooter });

    // 当传递了 aigc 时，说明是通过 ai 生成的报表
    if (data?.aigc) {
      const hideWaitLoading = createMessage.loading('正在生成报表字段，请稍后...', 0);
      try {
        await setFieldsValue({
          ...data.aigc,
        });
        // 等待懒加载的行编辑表格初始化完成后，自动执行SQL解析
        if (await waitForTableReady()) {
          handleSQLAnalyze();
        }
      } finally {
        hideWaitLoading();
      }
    }
  });
  //方法配置
  const [handleChangeTabs, handleSubmit, requestSubTableData, formRef] = useJvxeMethod(
    requestAddOrEdit,
    classifyIntoFormData,
    tableRefs,
    activeKey,
    refKeys
  );

  //设置标题
  const title = computed(() => (!unref(isUpdate) ? '新增' : '编辑'));

  async function reset() {
    await resetFields();
    activeKey.value = 'onlCgreportItem';
    positioning.value = '';
    onlCgreportParamTable.dataSource = [];
    onlCgreportItemTable.dataSource = [];
  }

  // update-begin--author:liaozhiyang---date:20260824---for：【LHZP-984】online报表新增定位功能
  /** 根据字段名或字段文本定位报表字段 */
  async function handlePositioning() {
    const value = positioning.value.trim();
    if (!value) {
      createMessage.warning('请输入字段或备注');
      return;
    }
    const xTable = onlCgreportItem.value?.getXTable?.();
    const fullData = xTable?.getTableData?.().fullData || [];
    const matches = (item, exact = false) => {
      const fieldName = String(item.fieldName || '');
      const fieldTxt = String(item.fieldTxt || '');
      return exact ? fieldName === value || fieldTxt === value : fieldName.includes(value) || fieldTxt.includes(value);
    };
    const row = fullData.find((item) => matches(item, true)) || fullData.find((item) => matches(item));
    if (!row) {
      createMessage.warning('未找到匹配字段');
      return;
    }
    await xTable.scrollToRow(row);
    const tableBody = xTable.getRefMaps?.().refTableBody?.value;
    const rowElement = tableBody?.$el?.querySelector(`[rowid="${xTable.getRowid(row)}"]`);
    if (rowElement) {
      rowElement.classList.add('customHighlight');
      setTimeout(() => rowElement.classList.remove('customHighlight'), 1000);
    }
  }
  // update-end--author:liaozhiyang---date:20260824---for：【LHZP-984】online报表新增定位功能

  /** 等待懒加载的行编辑表格实例初始化完成 */
  async function waitForTableReady() {
    for (let retryCount = 0; retryCount < TABLE_READY_MAX_RETRY_COUNT; retryCount++) {
      await nextTick();
      if (onlCgreportItem.value?.getTableData && onlCgreportParam.value?.getTableData) {
        return true;
      }
      console.log('【AI生成报表】等待行编辑表格实例初始化完成，重试中...', retryCount + 1);
      await new Promise((resolve) => setTimeout(resolve, TABLE_READY_RETRY_INTERVAL));
    }
    console.warn('【AI生成报表】等待行编辑表格实例初始化完成超时，请手动执行SQL解析');
    createInfoModal({
      title: '报表字段暂未显示',
      content: '系统未能自动加载报表字段，请点击上方“SQL解析”按钮重试。',
      okText: '知道了',
    });
    return false;
  }
  function classifyIntoFormData(allValues) {
    let main = Object.assign({}, allValues.formValue);
    return {
      ...main, // 展开
      onlCgreportParamList: allValues.tablesValue[1].tableData,
      onlCgreportItemList: allValues.tablesValue[0].tableData,
    };
  }
  //表单提交事件
  async function requestAddOrEdit(values) {
    try {
      setModalProps({ confirmLoading: true });
      //提交表单
      let params = [],
        items = [],
        head = {};
      Object.keys(values).map((k) => {
        if (k == 'onlCgreportItemList') {
          items = values[k];
        } else if (k == 'onlCgreportParamList') {
          params = values[k];
        } else {
          head[k] = values[k];
        }
      });
      let obj = { head, params, items };
      console.log('报表配置保存请求参数', obj);
      await saveOrUpdate(obj, isUpdate.value);
      //关闭弹窗
      closeModal();
      //刷新列表
      emit('success');
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }

  //解析SQL
  function handleSQLAnalyze() {
    setModalProps({ confirmLoading: true });
    validateFields(['cgrSql', 'dbSource'])
      .then((values) => {
        let { cgrSql, dbSource } = values;
        let urlParam = 'sql=' + encodeURIComponent(cgrSql);
        if (dbSource) {
          urlParam += '&dbKey=' + dbSource;
        }
        console.log('urlParam----', urlParam);
        analyzeSql(urlParam).then((res) => {
          if (res) {
            createMessage.success('解析成功');
            let { fields, params } = res;
            let newFields = fields.filter((item) => item.fieldName != '__row_number__');
            // 从vxeTable中获取当前数据
            let itemDataSource = onlCgreportItem.value.getTableData()
            // 合并后台数据
            let newItemDataList = getTabData(itemDataSource, newFields || [], 'fieldName');
            // 排序数据
            newItemDataList = newItemDataList.sort((a, b) => a.orderNum - b.orderNum);
            onlCgreportItemTable.dataSource = newItemDataList;

            let paramDataSource = onlCgreportParam.value.getTableData()
            let newParamList = getTabData(paramDataSource, params || [], 'paramName');
            newParamList = newParamList.sort((a, b) => a.orderNum - b.orderNum);
            onlCgreportParamTable.dataSource = newParamList;
          }
        });
      })
      .catch(() => {
        console.log('解析失败');
      })
      .finally(() => {
        setModalProps({ confirmLoading: false });
      });
  }

  /**
   * 获取tab的table的datasource
   * @param old_arr 原datasource
   * @param new_arr 从数据库查询出来的datasource
   * @param columnName 字段列
   */
  function getTabData(old_arr: any[], new_arr: any[], columnName: string): any[] {
    if (old_arr.length > 0) {
      let need: any[] = [],
        field_arr: any[] = [],
        max_order = 1;
      for (let t of new_arr) {
        for (let o of old_arr) {
          if (o[columnName] == t[columnName]) {
            need.push(o);
            field_arr.push(t[columnName]);
            if (o.orderNum > max_order) {
              max_order = o.orderNum;
            }
            break;
          }
        }
      }
      for (let t of new_arr) {
        if (field_arr.indexOf(t[columnName]) < 0) {
          t.orderNum = ++max_order;
          need.push(t);
        }
      }
      return need;
    } else {
      let max_order = 0;
      for (let t of new_arr) {
        if (!t.orderNum) {
          t.orderNum = ++max_order;
        }
      }
      return new_arr;
    }
  }

  // 手动设置table的数据 解析后调用
  async function setDataSource(tableRef, data) {
    await nextTick();
    await tableRef.value!.getXTable().insert(data);
    await nextTick();
  }
</script>
<style lang='less' scoped>
  // update-begin--author:liaozhiyang---date:20240509---for：【QQYUN-9230】报表图表弹窗样式调整
  form {
    padding-right: 22px;
  }
  :deep(.ant-tabs-content-holder) {
    padding: 0 10px;
  }
  // update-end--author:liaozhiyang---date:20240509---for：【QQYUN-9230】报表图表弹窗样式调整
  
  // ==================== 滚动条优化 - 开始 ====================
  // 目的：美化表格滚动条，减少视觉干扰
  // 效果：更细的滚动条，更美观的样式
  // 时间：2024年优化
  :deep(.vxe-table--body-wrapper) {
    // 自定义滚动条样式
    &::-webkit-scrollbar {
      width: 6px;
      height: 6px;
    }
    
    &::-webkit-scrollbar-track {
      background: #f1f1f1;
      border-radius: 3px;
    }
    
    &::-webkit-scrollbar-thumb {
      background: #c1c1c1;
      border-radius: 3px;
      
      &:hover {
        background: #a8a8a8;
      }
    }
    
    &::-webkit-scrollbar-corner {
      background: #f1f1f1;
    }
  }
  
  // 优化表格容器，确保内容不会溢出
  :deep(.vxe-table) {
    .vxe-body--row.customHighlight {
      background-color: var(--vxe-ui-table-row-hover-background-color);
    }
    .vxe-table--body-wrapper {
      overflow: auto;
    }
  }

  .positioning-area {
    width: 280px;
    display: flex;
    gap: 8px;
    float: left;
  }
  // ==================== 滚动条优化 - 结束 ====================
</style>
