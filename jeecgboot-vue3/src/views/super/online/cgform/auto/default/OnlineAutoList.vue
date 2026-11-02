<template>
  <div :class="['p-2', `online-list-${ID}`]">

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
      :clickToRowSelect="false"
      :scroll="tableScroll"
      @table-redo="reload"
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
          <span>{{cgBIBtnMap['add'].buttonName}}</span>
        </a-button>
        <a-button
            v-if="buttonSwitch.import && cgBIBtnMap['import'].enabled"
            type="primary"
            :preIcon="cgBIBtnMap['import'].buttonIcon"
            @click="onImportExcel"
        >
          <span>{{cgBIBtnMap['import'].buttonName}}</span>
        </a-button>
        <a-button
            v-if="buttonSwitch.export && cgBIBtnMap['export'].enabled"
            type="primary"
            :preIcon="cgBIBtnMap['export'].buttonIcon"
            :loading="exportLoading"
            @click="onExportExcelOverride"
        >
          <span>{{cgBIBtnMap['export'].buttonName}}</span>
        </a-button>
        <!-- update-begin--author:liaozhiyang---date:20250403---for：【QQYUN-11801】生成测试数据 -->
        <a-tooltip
          v-if="!isConfigCurRoute && (tableType == 1 || tableType == 2) && buttonSwitch.aigc_mock_data && cgBIBtnMap['aigc_mock_data']?.enabled && dataSource.length == 0 && !hasQueryCondition && !loading"
          :open="false"
          placement="bottom"
        >
          <template #title>
            <span>当有数据、有查询条件或生成测试数据后该按钮会隐藏</span>
          </template>
          <a-button preIcon="mdi:robot-love-outline" :loading="testDataLoading" @click="handleAddTestData(currentTableName, reload)">
            {{ cgBIBtnMap['aigc_mock_data'].buttonName }}
          </a-button>
        </a-tooltip>
        <!-- update-end--author:liaozhiyang---date:20250403---for：【QQYUN-11801】生成测试数据 -->
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
            @search="handleSuperQuery"
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

    <!-- 表单新增、修改弹框 -->
    <OnlineAutoModal
        @register="registerModal"
        :id="ID"
        :cgBIBtnMap="cgBIBtnMap"
        :buttonSwitch="buttonSwitch"
        :confirmBtnCfg="getFormConfirmButtonCfg"
        @success="reload"
        @formConfig="handleFormConfig"
    />

    <!-- 详情弹框 -->
    <online-detail-modal :id="ID" @register="registerDetailModal"/>

    <!-- 导入 -->
    <JImportModal @register="registerImportModal" :url="importUrl()" @ok="reload" online></JImportModal>

    <!-- 跳转Href的动态组件方式 -->
    <a-modal v-bind="hrefComponent.model" v-on="hrefComponent.on">
      <component :is="hrefComponent.is" v-bind="hrefComponent.params" />
    </a-modal>

    <!-- 自定义弹窗 -->
    <online-custom-modal @register="registerCustomModal" @success="reload" />

    <!-- 弹窗给href到另外一张表单用-详情表单 -->
    <online-detail-modal :id="hrefMainTableId" @register="registerOnlineHrefModal" :defaultFullscreen="false"/>

    <!-- 弹窗到另外一张表单用-可编辑表单-关联记录的字段可在列表上打开modal编辑数据 -->
    <online-pop-modal ref="onlinePopModalRef" :id="popTableId" @register="registerPopModal" @success="reload" request topTip></online-pop-modal>

    <!-- 流程图查看modal -->
    <BpmGraphicModal @register="registerBpmModal"></BpmGraphicModal>
    <!-- 页面loading[主要为了js增强使用] -->
    <Loading :loading="pageLoading" :absolute="true" />
  </div>
</template>

<script lang="ts">
  import { ref } from 'vue'
  import { BasicTable, TableAction } from '/@/components/Table';
  import { useMessage } from '/@/hooks/web/useMessage';
  import OnlineAutoModal from './OnlineAutoModal.vue';
  import OnlineCustomModal from './OnlineCustomModal.vue';
  import OnlineDetailModal from './OnlineDetailModal.vue'
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
  import { NORMAL } from "../../util/constant";
  import { Loading } from '/@/components/Loading';
  import { sanitizeRichText } from '/@/utils/htmlSanitizer';

  export default {
    name: 'OnlineAutoList',
    components: {
      BasicTable,
      TableAction,
      OnlineAutoModal,
      JImportModal,
      OnlineQueryForm,
      OnlineSuperQuery,
      OnlineCustomModal,
      OnlineDetailModal,
      OnlinePopModal,
      Loading,
    },
    setup() {
      const { createMessage: $message } = useMessage();
      const currentTableName = ref('');
      const tableType = ref('');
      // 这行代码应该在每次进入新的路由都会走，不管该路由有没有被缓存--
      const {
        ID,
        onlineTableContext,
        onlineQueryFormOuter,
        loading,
        reload,
        dataSource,
        hasQueryCondition,
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
        isConfigCurRoute,
        pageLoading,
      } = useOnlineTableContext();

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
        handleAddTestData,
        testDataLoading,
        testDataBtnShow,
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
        registerBpmModal
      } = useListButton(onlineTableContext, onlineExtConfigJson);

      const exportLoading = ref(false)

      // 重写导出方法，防止频繁点击
      async function onExportExcelOverride() {
        try {
          exportLoading.value = true
          await onExportExcel()
        } finally {
          // 防止频繁点击，延迟1.5s关闭loading
          setTimeout(() => exportLoading.value = false, 1500)
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
        let columnResult: any = await getColumnList(NORMAL);
        handleTableConfig(columnResult);
        // update-begin--author:liaozhiyang---date:20250403---for：【QQYUN-11801】生成测试数据
        currentTableName.value = columnResult.currentTableName;
        tableType.value = columnResult.tableType;
        // update-end--author:liaozhiyang---date:20250403---for：【QQYUN-11801】生成测试数据
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
        handleColumnResult(result);
        // 表配置
        handleSpecialConfig(result);
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
      async function onQueryFormLoaded(json) {
        console.log('onQueryFormLoaded', json)
        await getRefPromise(superQueryButtonRef)
        superQueryButtonRef.value.init(json);
      }

      /**
       * list页面打开 其他表单弹窗
       * @param params
       */
      function openOnlinePopModal(params){
        console.log('openOnlinePopModal', params)
        popTableId.value = params.id;
        let data = {
          title: params.describe
        }
        if(params.record && params.record.id){
          data['record'] = params.record
          data['isUpdate'] = true;
        }
        openPopModal(true, data);
      }
      //绑定弹窗事件
      useOnlineListPopEvent(openOnlinePopModal);
      
      const that = {
        sanitizeRichText,
        ID,
        // 查询区域
        onlineQueryFormOuter,
        queryWithCondition,
        onQueryFormLoaded,
        reload,

        //高级查询
        superQueryButtonRef,
        superQueryStatus,
        handleSuperQuery,

        // table区域
        loading,
        columns,
        dataSource,
        hasQueryCondition,
        pagination,
        actionColumn,
        rowSelection,
        selectedKeys,
        tableScroll,
        enableScrollBar,
        handleChangeInTable,

        //按钮
        buttonSwitch,
        handleAdd,
        handleEdit,
        onImportExcel,
        onExportExcel,
        exportLoading,
        onExportExcelOverride,
        cgBIBtnMap,
        getQueryButtonCfg,
        getResetButtonCfg,
        getFormConfirmButtonCfg,
        cgTopButtonList,
        cgLinkButtonList,
        cgButtonJsHandler,
        cgButtonActionHandler,
        cgButtonLinkHandler,
        handleBatchDelete,
        handleAddTestData,
        testDataLoading,
        testDataBtnShow,
        currentTableName,
        loadData,
        tableType,
        isConfigCurRoute,

        // table-slot
        downloadRowFile,
        getImgView,
        getPcaText,
        getFormatDate,

        // 操作列
        getActions,
        getDropDownActions,

        // 弹窗
        registerModal,
        registerCustomModal,
        registerImportModal,
        registerDetailModal,
        importUrl,
        handleFormConfig,
        onlinePopModalRef,

        //其他
        tableReloading,
        handleSubmitFlow,
        hrefComponent,
        viewOnlineCellImage,
        hrefMainTableId,
        onlineExtConfigJson,
        registerOnlineHrefModal,
        registerPopModal,
        popTableId,
        registerBpmModal,
        handleClickFieldHref,
        pageLoading,
      };
      return that;
    },

    // 1引入了loadsh   console.log(that.simpleDateFormat(new Date().getTime(),'yyyy-MM-dd'));
    // 2. value的问题
    // 3. 变量位置改变后需要 重写api
    // 1添加按钮的时候 预留出样式对象 然后js增强中设置样式对象
    // 2直接设置css字符串 然后通过js document 往head里面增加css片段 全局生效
  };
</script>

<style lang="less">
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
