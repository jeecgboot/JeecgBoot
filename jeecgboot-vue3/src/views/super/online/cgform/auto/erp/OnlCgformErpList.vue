<template>
  <div ref="cgformErpListRef" class="p-2 cgformErpList">
    <div class="content">

      <!-- 加载中骨架屏 -->
      <a-skeleton v-if="tableReloading" active />

      <!-- 查询条件 -->
      <online-query-form
          v-show="!tableReloading"
          ref="onlineQueryFormOuter"
          :id="ID"
          :queryBtnCfg="getQueryButtonCfg"
          :resetBtnCfg="getResetButtonCfg"
          @search="queryWithCondition"
          @loaded="onQueryFormLoaded"
      />

      <!-- 列表 -->
      <BasicTable
        v-if="!tableReloading"
        ref="onlineTable"
        rowKey="jeecg_row_key"
        :canResize="true"
        :bordered="true"
        :showIndexColumn="false"
        :loading="loading"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="pagination"
        :rowSelection="rowSelection"
        :actionColumn="actionColumn"
        :showTableSetting="true"
        :clickToRowSelect="true"
        :scroll="scroll"
        @table-redo="reload"
        :tableSetting="tableSetting"
        :class="{ 'j-table-force-nowrap': enableScrollBar }"
        @change="handleChangeInTable"
      >
        <template #tableTitle>
          <a-button
              v-if="buttonSwitch.add && cgBIBtnMap['add'].enabled"
              type="primary"
              :preIcon="cgBIBtnMap['add'].buttonIcon"
              @click="handleAdd"
          >
            <span>{{ cgBIBtnMap['add'].buttonName }}</span>
          </a-button>
          <a-button
              v-if="buttonSwitch.import && cgBIBtnMap['import'].enabled"
              type="primary"
              :preIcon="cgBIBtnMap['import'].buttonIcon"
              @click="onImportExcel"
          >
            <span>{{ cgBIBtnMap['import'].buttonName }}</span>
          </a-button>
          <a-button
              v-if="buttonSwitch.export && cgBIBtnMap['export'].enabled"
              type="primary"
              :preIcon="cgBIBtnMap['export'].buttonIcon"
              :loading="exportLoading"
              @click="onExportExcelOverride"
          >
            <span>{{ cgBIBtnMap['export'].buttonName }}</span>
          </a-button>

          <!-- 自定义按钮 -->
          <template v-if="cgTopButtonList && cgTopButtonList.length > 0" v-for="(item, index) in cgTopButtonList">
            <a-button
              v-if="item.optType == 'js'"
              :key="'cgbtn' + index"
              @click="cgButtonJsHandler(item.buttonCode)"
              type="primary"
              :preIcon="item.buttonIcon ? 'ant-design:' + item.buttonIcon : ''"
            >
              {{ item.buttonName }}
            </a-button>
            <a-button
              v-else-if="item.optType == 'action'"
              :key="'cgbtn' + index"
              @click="cgButtonActionHandler(item.buttonCode)"
              type="primary"
              :preIcon="item.buttonIcon ? 'ant-design:' + item.buttonIcon : ''"
            >
              {{ item.buttonName }}
            </a-button>
          </template>

          <a-button
              v-show="selectedKeys.length > 0"
              v-if="buttonSwitch.batch_delete && cgBIBtnMap['batch_delete'].enabled"
              :preIcon="cgBIBtnMap['batch_delete'].buttonIcon"
              @click="handleBatchDelete"
          >
            <span>{{cgBIBtnMap['batch_delete'].buttonName}}</span>
          </a-button>

          <online-super-query
            v-if="buttonSwitch.super_query && cgBIBtnMap['super_query'].enabled"
            ref="superQueryButtonRef"
            online
            :status="superQueryStatus"
            :queryBtnCfg="cgBIBtnMap['super_query']"
            @search="handleSuperSearch"
          />
        </template>

        <template #fileSlot="{ text, record, column }">
          <span v-if="!text" style="font-size: 12px; font-style: italic">无文件</span>
          <a-button v-else :ghost="true" type="primary" preIcon="ant-design:download" size="small" @click="downloadRowFile(text, record, column, ID)">
            下载
          </a-button>
        </template>

        <template #imgSlot="{ text }">
          <span v-if="!text" style="font-size: 12px; font-style: italic">无图片</span>
          <img v-else :src="getImgView(text)" alt="图片不存在" class="online-cell-image" @click="viewOnlineCellImage(text)" />
        </template>

        <template #htmlSlot="{ text, column, record }">
          <!-- update-begin--author:liaozhiyang---date:20240517---for：【TV360X-129】增加富文本控件配置href跳转 -->
          <template v-if="column.fieldHref">
            <a v-html="sanitizeRichText(text)" @click="handleClickFieldHref(column.fieldHref, record)"></a>
          </template>
          <div v-else v-html="sanitizeRichText(text)"></div>
          <!-- update-end--author:liaozhiyang---date:20240517---for：【TV360X-129】增加富文本控件配置href跳转 -->
        </template>

        <template #pcaSlot="{ text, column }">
          <div :title="getPcaText(text, column)">{{ getPcaText(text, column) }}</div>
        </template>

        <template #dateSlot="{ text, column }">
          <span>{{ getFormatDate(text, column) }}</span>
        </template>

        <template #action="{ record }">
          <TableAction :actions="getActions(record)" :dropDownActions="getDropDownActions(record)" />
        </template>
      </BasicTable>
      <!--子表表格tab-->
      <a-tabs v-if="subTableData?.length" animated v-model:activeKey="tabActiveKey" style="margin: 10px">
        <a-tab-pane v-for="(item, index) in subTableData" :tab="item.description" :key="index" forceRender>
          <OnlCgformErpSubTable :data="item" :mainTableSelectedRowRcord="selectedRowRcord" @getSource="getSource" />
        </a-tab-pane>
      </a-tabs>

      <!-- 表单新增、修改弹框 -->
      <OnlineAutoModal
          @register="registerModal"
          :id="ID"
          :subTableSource="erpAllSubTableSource"
          :cgBIBtnMap="cgBIBtnMap"
          :buttonSwitch="buttonSwitch"
          :confirmBtnCfg="getFormConfirmButtonCfg"
          @success="reload"
          @formConfig="handleFormConfig"
      />

      <!-- 详情弹框 -->
      <online-detail-modal :id="ID" @register="registerDetailModal" />

      <!-- 导入 -->
      <JImportModal @register="registerImportModal" :url="importUrl()" @ok="reload" online></JImportModal>

      <!-- 跳转Href的动态组件方式 -->
      <a-modal v-bind="hrefComponent.model" v-on="hrefComponent.on">
        <component :is="hrefComponent.is" v-bind="hrefComponent.params" />
      </a-modal>

      <!-- 自定义弹窗 -->
      <online-custom-modal @register="registerCustomModal" @success="reload" />

      <!-- 弹窗给href到另外一张表单用-详情表单 -->
      <online-detail-modal :id="hrefMainTableId" @register="registerOnlineHrefModal" :defaultFullscreen="false" />

      <!-- 弹窗到另外一张表单用-可编辑表单-关联记录的字段可在列表上打开modal编辑数据 -->
      <online-pop-modal ref="onlinePopModalRef" :id="popTableId" @register="registerPopModal" @success="reload" request topTip></online-pop-modal>

      <!-- 流程图查看modal -->
      <BpmGraphicModal @register="registerBpmModal"></BpmGraphicModal>
      <!-- 页面loading[主要为了js增强使用] -->
      <Loading :loading="pageLoading" :absolute="true" />
    </div>
  </div>
</template>

<script setup name="CgformErpList">
  import { ref, nextTick, computed, watchEffect } from 'vue';
  import { BasicTable, TableAction } from '/@/components/Table';
  import { useMessage } from '/@/hooks/web/useMessage';
  import OnlineAutoModal from '../default/OnlineAutoModal.vue';
  import OnlineCustomModal from '../default/OnlineCustomModal.vue';
  import OnlineDetailModal from '../default/OnlineDetailModal.vue';
  import { watch } from 'vue';
  import JImportModal from '/@/components/Form/src/jeecg/components/JImportModal.vue';
  import { useOnlineTableContext } from '../../hooks/auto/useOnlineTableContext';
  import { useListButton } from '../../hooks/auto/useListButton';
  import { useTableColumns } from '../../hooks/auto/useTableColumns';
  import { useEnhance } from '../../hooks/auto/useEnhance';
  import { getRefPromise } from '../../hooks/auto/useAutoForm';
  import OnlineQueryForm from '../comp/OnlineQueryForm.vue';
  import OnlineSuperQuery from '../comp/superquery/SuperQuery.vue';
  import { useOnlineListPopEvent } from '../../hooks/auto/useOnlinePopEvent';
  import OnlinePopModal from '../comp/OnlinePopModal.vue';
  import OnlCgformErpSubTable from './OnlCgformErpSubTable.vue';
  import { ERP } from "../../util/constant";
  import { cloneDeep } from 'lodash-es';
  import { Loading } from '/@/components/Loading';
  import { isObject } from '/@/utils/is';
  import { useDebounceFn } from '@vueuse/core';
  import { sanitizeRichText } from '/@/utils/htmlSanitizer';
  const subTableData = ref([]);
  const tabActiveKey = ref(0);
  const selectedRowRcord = ref(null);
  const tableSetting = ref({});
  const { createMessage: $message } = useMessage();
  const cgformErpListRef = ref(null);
  const onlineTable = ref(null);
  // update-begin--author:liaozhiyang---date:20231128---for：【QQYUN-7260】erp主表编辑时保存子表记录
  const erpAllSubTableSource = ref({});
  const getSource = (tableName, data) => {
    erpAllSubTableSource.value[tableName] = data;
  };
  // update-end--author:liaozhiyang---date:20231128---for：【QQYUN-7260】erp主表编辑时保存子表记录
  // 这行代码应该在每次进入新的路由都会走，不管该路由有没有被缓存--
  const {
    ID,
    onlineTableContext,
    onlineQueryFormOuter,
    loading,
    reload,
    dataSource,
    pagination,
    handleSpecialConfig,
    getColumnList,
    handleChangeInTable,
    loadData,
    superQueryButtonRef,
    superQueryStatus,
    handleSuperQuery,
    onlineExtConfigJson,
    handleFormConfig,
    registerCustomModal,
    tableReloading,
    pageLoading,
  } = useOnlineTableContext({ themeTemplate: ERP });

  const scroll = computed(() => {
    const reuslt = { y: 300 };
    if (isObject(tableScroll.value)) {
      return { ...tableScroll.value, ...reuslt };
    }
    return reuslt;
  });

  // 监听数据源变化
  // update-begin--author:liaozhiyang---date:20260715---for：【LHZP-593】点击分页上一页选中的不显示了
  const handleDataSourceChange = useDebounceFn((value) => {
    // update-begin--author:liusq---date:20260713---for：【LHZP-118】ERP主子表查询结果为空时子表残留数据
    // 主表无数据时强制清空选中行，触发子表 watch 走 else 分支清空子表
    if (!Array.isArray(value) || value.length === 0) {
      if (selectedKeys.value.length > 0) {
        selectedKeys.value = [];
      }
      if (selectedRowRcord.value !== null) {
        selectedRowRcord.value = null;
      }
      return;
    }
    // update-end--author:liusq---date:20260713---for：【LHZP-118】ERP主子表查询结果为空时子表残留数据
    if (selectedKeys.value.length > 0) {
      // 【TV360X-2701】选中的数据在当前页数据中不存在就清空选中
      // selectedKeys.value = selectedKeys.value.filter((key) => value.some((item) => item['jeecg_row_key'] === key));
    }
    // update-begin--author:liaozhiyang---date:20250722---for：【issues/8575】erp默认选中第一个及没选中主表时子表不查询
    if (pagination.value.current == 1 && selectedKeys.value.length === 0) {
        const tableTbodyElem = document.querySelector('.ant-table-wrapper .ant-table-tbody');
        tableTbodyElem?.querySelector('.ant-table-row')?.click();
    }
    // update-end--author:liaozhiyang---date:20250722---for：【issues/8575】erp默认选中第一个及没选中主表时子表不查询
  }, 100);
  watch(dataSource, (value) => handleDataSourceChange(value));
  // update-end--author:liaozhiyang---date:20260715---for：【LHZP-593】点击分页上一页选中的不显示了

  // 判断 若ID不存在就终止后续逻辑
  if (!ID.value) {
    $message.warning('地址错误, 配置ID不存在!');
    // update-end--author:liaozhiyang---date:20230825---for：【QQYUN-6326】部分代码找不到引用
    throw new Error('地址错误, 配置ID不存在!');
    // update-end--author:liaozhiyang---date:20230825---for：【QQYUN-6326】部分代码找不到引用

  }
  // 处理增强
  let { initCgEnhanceJs } = useEnhance(onlineTableContext);
  // 处理列表button
  const {
    buttonSwitch,
    cgLinkButtonList,
    cgBIBtnMap,
    getQueryButtonCfg,
    getResetButtonCfg,
    getFormConfirmButtonCfg,
    cgTopButtonList,
    importUrl,
    registerModal,
    handleAdd,
    handleEdit,
    handleBatchDelete,
    registerImportModal,
    onImportExcel,
    onExportExcel,
    cgButtonJsHandler,
    cgButtonActionHandler,
    cgButtonLinkHandler,
    handleSubmitFlow,
    getDropDownActions,
    getActions,
    initButtonList,
    initButtonSwitch,
    registerDetailModal,
    registerBpmModal,
  } = useListButton(onlineTableContext, onlineExtConfigJson, {
    singleDelCallback: (id) => {
      // 删除的正好是选中的数据需清除选中的key
      if (Array.isArray(onlineTableContext['selectedRowKeys']) && onlineTableContext['selectedRowKeys'].includes(id)) {
        onlineTableContext['clearSelectedRow']();
      }
    },
    editClickCallback: (id, e) => {
      // 当前数据被选中了就得阻止冒泡，防止反选
      if (Array.isArray(onlineTableContext['selectedRowKeys']) && onlineTableContext['selectedRowKeys'].includes(id)) {
        e.stopPropagation();
      }
    },
  });
  const exportLoading = ref(false);

  // 重写导出方法，防止频繁点击
  async function onExportExcelOverride() {
    try {
      exportLoading.value = true;
      await onExportExcel();
    } finally {
      // 防止频繁点击，延迟1.5s关闭loading
      setTimeout(() => (exportLoading.value = false), 1500);
    }
  }

  // 处理 BasicTable 的配置
  const {
    columns,
    actionColumn,
    selectedKeys,
    rowSelection,
    enableScrollBar,
    tableScroll,
    downloadRowFile,
    getImgView,
    getPcaText,
    getFormatDate,
    handleColumnResult,
    hrefComponent,
    viewOnlineCellImage,
    hrefMainTableId,
    registerOnlineHrefModal,
    registerPopModal,
    openPopModal,
    onlinePopModalRef,
    popTableId,
    handleClickFieldHref,
  } = useTableColumns(onlineTableContext, onlineExtConfigJson);

  watch(
    selectedKeys,
    (value) => {
      if (selectedKeys.value?.length) {
        selectedRowRcord.value = dataSource.value.find((item) => item['id'] === selectedKeys.value[0]);
      }else{
        selectedRowRcord.value = null;
      }
    },
    {
      immediate: true,
    }
  );

  // 监听表单配置ID
  watch(
    ID,
    () => {
      console.log('watched id is change...');
      initAutoList();
    },
    { immediate: true }
  );

  /**重新加载online配置*/
  async function initAutoList() {
    loading.value = true;
    // 1.列配置信息
    let columnResult = await getColumnList(ERP);
    handleTableConfig(columnResult.main);
    subTableData.value = columnResult.subList;
    // 2.加载数据
    // update-begin--author:liaozhiyang---date:20260715---for：【LHZP-219】修复有默认值查询的场景下会调用两次接口，让只调用最后一次查询接口
    let hasDefaultQuery = false;
    try {
      const queryForm = await getRefPromise(onlineQueryFormOuter);
      if (queryForm && typeof queryForm.whenFirstQueryDecided === 'function') {
        hasDefaultQuery = await queryForm.whenFirstQueryDecided();
      }
    } catch (e) {
      hasDefaultQuery = false;
    }
    if (!hasDefaultQuery) {
      await loadData();
    }
    // update-end--author:liaozhiyang---date:20260715---for：【LHZP-219】修复有默认值查询的场景下会调用两次接口，让只调用最后一次查询接口
    loading.value = false;
    // 3.执行js增强 setup
    onlineTableContext.execButtonEnhance('setup');
  }

  // 将查询结果转成 table渲染需要的配置
  function handleTableConfig(result) {
    // js增强初始化
    let EnhanceJS = initCgEnhanceJs(result.enhanceJs);
    onlineTableContext['EnhanceJS'] = EnhanceJS;
    // 自定义按钮设置
    initButtonList(result.cgButtonList);
    // 页面按钮显示隐藏状态设置
    initButtonSwitch(result.hideColumns);
    // 列配置
    handleColumnResult(result, 'radio');
    // 表配置
    handleSpecialConfig(result);
    // update-begin--author:liaozhiyang---date:20240611---for：【TV360X-1004】erp风格列设置之后再次刷新展示内容不对
    tableSetting.value = {
      cacheKey: `online_erp_mainTable_${result.currentTableName}`,
    };
    // update-end--author:liaozhiyang---date:20240611---for：【TV360X-1004】erp风格列设置之后再次刷新展示内容不对
  }

  /**
   * 查询控件 事件-执行查询
   * @param data
   */
  function queryWithCondition(data) {
    onlineTableContext['queryParam'] = data;
    reload({mode:'search'});
  }

  /**
   * 查询组件加载完成事件，获取高级查询需要的字段信息
   */
  async function onQueryFormLoaded(json = {}) {
    console.log('onQueryFormLoaded', json);
    await getRefPromise(superQueryButtonRef);
    // update-begin--author:liaozhiyang---date:20230904---for：【QQYUN-6425】erp风格 高级查询 未查询出数据时 子表数据未清空
    const cJson = cloneDeep(json),
      { properties = {} } = cJson;
    Object.entries(properties).forEach(([key, item]) => {
      if (item.view == 'table') {
        delete properties[key];
      }
    });
    superQueryButtonRef.value.init(cJson);
    // update-end--author:liaozhiyang---date:20230904---for：【QQYUN-6425】erp风格 高级查询 未查询出数据时 子表数据未清空
  }

  /**
   * list页面打开 其他表单弹窗
   * @param params
   */
  function openOnlinePopModal(params) {
    console.log('openOnlinePopModal', params);
    popTableId.value = params.id;
    let data = {
      title: params.describe,
    };
    if (params.record && params.record.id) {
      data['record'] = params.record;
      data['isUpdate'] = true;
    }
    openPopModal(true, data);
  }
  //绑定弹窗事件
  useOnlineListPopEvent(openOnlinePopModal);
  console.log('111111111-------onlineTableContext====111111111111111111', onlineTableContext);

  const handleSuperSearch = (params, matchType) => {
    handleSuperQuery(params, matchType);
    selectedKeys.value = [];
  };
  // 解决erp主表5条时在笔记本上会出现滚动条问题
  watchEffect(() => {
    const size = onlineTable?.value?.getBindValuesRef().value?.size
    setTimeout(() => {
      const tableTbodyElem = cgformErpListRef.value.querySelector('.ant-table-wrapper .ant-table-body');
      if (tableTbodyElem) {
        tableTbodyElem.style.height = 'auto';
        if (dataSource.value.length > 0) {
          if (tableTbodyElem.offsetHeight < 300) {
            tableTbodyElem.style.height = `${tableTbodyElem.offsetHeight + 1}px`;
            console.log(size);
          }
        }
      }
    }, 0);
  }, {
    flush: 'post'
  });
  // 1引入了loadsh   console.log(that.simpleDateFormat(new Date().getTime(),'yyyy-MM-dd'));
  // 2. value的问题
  // 3. 变量位置改变后需要 重写api
  // 1添加按钮的时候 预留出样式对象 然后js增强中设置样式对象
  // 2直接设置css字符串 然后通过js document 往head里面增加css片段 全局生效
</script>

<style lang="less">
 // update-begin--author:liaozhiyang---date:20240313---for：【QQYUN-8493】修正暗黑模式online表单Erp和编辑页面显示不正确
  html[data-theme='light'] {
    .cgformErpList {
      height: 100%;
      .content {
        background-color: #fff;
        height: 100%;
      }
    }
  }
  // update-end--author:liaozhiyang---date:20240313---for：【QQYUN-8493】修正暗黑模式online表单Erp和编辑页面显示不正确
  /** [表格主题样式一] 表格强制列不换行 */
  .j-table-force-nowrap {
    td,
    th {
      white-space: nowrap;
    }

    .ant-table-selection-column {
      padding: 12px 22px !important;
    }

    /** 列自适应，弊端会导致列宽失效 */
    &.ant-table-wrapper .ant-table-content {
      overflow-x: auto;
    }
  }
  .online-cell-image {
    height: 25px !important;
    margin: 0 auto;
    max-width: 80px;
    font-size: 12px;
    font-style: italic;
    cursor: pointer;
  }
</style>
<style lang="less" scoped>
  // 为了解决笔记本上计算出浮点数导致5条也出现滚动条
  :deep(.ant-table-body) {
    line-height: 22px;
  }

  // update-begin--author:liaozhiyang---date:20260831---for：【LHZP-1073】erp查询区域每行第一个字段的 label 取消固定宽度
  @media (min-width: 992px) {
    :deep(.online-query-form .ant-row > .ant-col:nth-child(4n + 1) .ant-form-item-label) {
      flex: none;
      width: auto !important;
    }

    :deep(.online-query-form .ant-row > .ant-col:nth-child(4n + 1) .ant-form-item-control) {
      flex: 1;
      min-width: 0;
    }
  }

  @media (min-width: 768px) and (max-width: 991px) {
    :deep(.online-query-form .ant-row > .ant-col:nth-child(2n + 1) .ant-form-item-label) {
      flex: none;
      width: auto !important;
    }

    :deep(.online-query-form .ant-row > .ant-col:nth-child(2n + 1) .ant-form-item-control) {
      flex: 1;
      min-width: 0;
    }
  }

  @media (max-width: 767px) {
    :deep(.online-query-form .ant-form-item-label) {
      flex: none;
      width: auto !important;
    }

    :deep(.online-query-form .ant-form-item-control) {
      flex: 1;
      min-width: 0;
    }
  }
  // update-end--author:liaozhiyang---date:20260831---for：【LHZP-1073】erp查询区域每行第一个字段的 label 取消固定宽度
</style>
