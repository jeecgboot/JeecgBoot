<template>
  <div class="p-2">

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

    <!-- 列表-树形列表前五个配置不一样【树】 -->
    <BasicTable
      v-if="!tableReloading"
      ref="onlineTreeTableRef"
      :isTreeTable="true"
      :expandedRowKeys="expandedRowKeys"
      @expandedRowsChange="handleExpandedRowsChange"
      @expand="handleExpand"
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
            @click="onExportExcel"
        >
          <span>{{cgBIBtnMap['export'].buttonName}}</span>
        </a-button>
        <!-- update-begin--author:liusq---date:20261102---for：【QQYUN-11801】树表列表补充生成测试数据按钮 -->
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
        <!-- update-end--author:liusq---date:20261102---for：【QQYUN-11801】树表列表补充生成测试数据按钮 -->

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
        <TableAction :actions="getActions(record)" :dropDownActions="getTreeDropDownActions(record)" />
      </template>
    </BasicTable>

    <!-- 表单新增、修改弹框 -->
    <OnlineAutoModal
        @register="registerModal"
        :id="ID"
        :cgBIBtnMap="cgBIBtnMap"
        :buttonSwitch="buttonSwitch"
        :confirmBtnCfg="getFormConfirmButtonCfg"
        @success="handlerFormSuccess"
        @formConfig="handleFormConfig"
    />

    <!-- 导入 -->
    <JImportModal @register="registerImportModal" :url="importUrl()" @ok="reload" online></JImportModal>

    <!-- 跳转Href的动态组件方式 -->
    <a-modal v-bind="hrefComponent.model" v-on="hrefComponent.on">
      <component :is="hrefComponent.is" v-bind="hrefComponent.params" />
    </a-modal>

    <!-- 自定义弹窗 -->
    <online-custom-modal @register="registerCustomModal" @success="reload" />

    <!-- 详情弹框 -->
    <online-detail-modal :id="ID" @register="registerDetailModal"/>

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
  import { BasicTable, TableAction } from '/@/components/Table';
  import { useMessage } from '/@/hooks/web/useMessage';
  import OnlineAutoModal from '../default/OnlineAutoModal.vue';
  import OnlineDetailModal from '../default/OnlineDetailModal.vue';
  import OnlineCustomModal from '../default/OnlineCustomModal.vue';
  import { ref, watch } from 'vue';
  import JImportModal from '/@/components/Form/src/jeecg/components/JImportModal.vue';
  import { useOnlineTableContext } from '../../hooks/auto/useOnlineTableContext';
  import { useListButton } from '../../hooks/auto/useListButton';
  import { useTableColumns } from '../../hooks/auto/useTableColumns';
  import { useEnhance } from '../../hooks/auto/useEnhance';
  import { defHttp } from '/@/utils/http/axios';
  import { getRefPromise } from '../../hooks/auto/useAutoForm';
  import OnlineQueryForm from '../comp/OnlineQueryForm.vue';
  import OnlineSuperQuery from '../comp/superquery/SuperQuery.vue';
  import OnlinePopModal from '../comp/OnlinePopModal.vue';
  import { useOnlineListPopEvent } from '../../hooks/auto/useOnlinePopEvent';
  import { Tree } from "../../util/constant";
  import { Loading } from '/@/components/Loading';
  import { sanitizeRichText } from '/@/utils/htmlSanitizer';

  export default {
    name: 'DefaultOnlineList',
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
      const onlineTreeTableRef = ref();
      // update-begin--author:liusq---date:20261102---for：【QQYUN-11801】树表列表补充生成测试数据按钮
      const currentTableName = ref('');
      const tableType = ref('');
      // update-end--author:liusq---date:20261102---for：【QQYUN-11801】树表列表补充生成测试数据按钮

      // 树形列表增加了最后3个配置项【树】
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
        registerCustomModal,
        getTreeDataByResult,
        expandedRowKeys,
        handleExpandedRowsChange,
        tableReloading,
        onlineExtConfigJson,
        handleFormConfig,
        isConfigCurRoute,
        pageLoading,
      } = useOnlineTableContext();

      if (!ID.value) {
        $message.warning('地址错误, 配置ID不存在!');
        // update-begin--author:liaozhiyang---date:20230825---for：【QQYUN-6326】部分代码找不到引用
        throw new Error('地址错误, 配置ID不存在!');
        // update-end--author:liaozhiyang---date:20230825---for：【QQYUN-6326】部分代码找不到引用
      }
      // 树列表特定方法调用
      onlineTableContext.isTree(true);

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
      } = useListButton(onlineTableContext, onlineExtConfigJson);

      // 处理table的配置
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
        ID,
        () => {
          initAutoList();
        },
        { immediate: true }
      );

      async function initAutoList() {
        loading.value = true;
        // 1.列配置信息
        let columnResult: any = await getColumnList(Tree);
        handleTableConfig(columnResult);
        currentTableName.value = columnResult.currentTableName;
        tableType.value = columnResult.tableType;
        // 2.加载数据
        await loadData();
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
        //是否有子节点列-树列表特定【树】
        onlineTableContext['hasChildrenField'] = result.hasChildrenField;
        onlineTableContext['pidField'] = result.pidField;
      }

      /**
       * 查询控件 事件-执行查询
       * @param data
       */
      function queryWithCondition(data, status) {
        onlineTableContext['queryParam'] = data;
        if (status === true) {
          // 正常查询
          reload({mode:'search'});
        } else {
          //重置查询【树】
          searchReset();
        }
      }

      /**
       * 查询组件加载完成事件，获取高级查询需要的字段信息
       */
      async function onQueryFormLoaded(json) {
        await getRefPromise(superQueryButtonRef)
        superQueryButtonRef.value.init(json);
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

      /*-------------------------------以下方法是树列表专有的，或复写或新增-【树】----------------------------*/

      // 展开节点调用
      function handleExpand(expanded, record) {
        // 判断是否是展开状态
        let expandedRowKeysValue = expandedRowKeys.value;
        if (expanded) {
          addExpandedRowKey(record.id);
          if (record.children.length > 0 && record.children[0].isLoading === true) {
            let hasChildrenField = onlineTableContext.hasChildrenField;
            const { sortField, sortType } = onlineTableContext;
            let params = Object.assign({}, { column: sortField, order: sortType });
            params[onlineTableContext['pidField']] = record.id;
            params[hasChildrenField] = record[hasChildrenField];
            let url = `${onlineTableContext.onlineUrl.getTreeData}${onlineTableContext.ID}`;
            defHttp
              .get({ url, params }, { isTransformResponse: false })
              .then((res) => {
                console.log('handleExpand', res.result);
                if (res.success) {
                  if (Number(res.result.total) > 0) {
                    record.children = getTreeDataByResult(res.result.records);
                    // dataSource.value =
                  } else {
                    record.children = '';
                    record.hasChildrenField = '0';
                  }
                } else {
                  $message.warning(res.message);
                }
              })
              .catch(() => {
                $message.warning('加载子节点失败!');
              });
          }
        } else {
          let keyIndex = expandedRowKeysValue.indexOf(record.id);
          if (keyIndex >= 0) {
            expandedRowKeys.value = expandedRowKeysValue.splice(keyIndex, 1);
          }
        }
      }

      /**
       * 添加展开项的id
       * */
      function addExpandedRowKey(key) {
        let arr = expandedRowKeys.value;
        if (arr && arr.indexOf(key) < 0) {
          arr.push(key);
        }
        expandedRowKeys.value = arr;
      }

      // 重置查询条件
      async function searchReset() {
        if (onlineTableContext.isTree() === true) {
          expandedRowKeys.value = [];
          onlineTreeTableRef.value.collapseAll();
        }
        reload();
      }

      /**
       * 树表单新增、修改完成后 列表页面的回调事件
       */
      function handlerFormSuccess(formData) {
        console.log('expandedRowKeys.value', expandedRowKeys.value);
        if (loadParent.value === true) {
          let pid = formData[onlineTableContext.pidField];
          if (pid) {
            let arr = expandedRowKeys.value;
            if (arr.indexOf(pid) < 0) {
              arr.push(pid);
            }
            expandedRowKeys.value = arr;
          }
        }
        reload();
      }

      /**
       *
       * [更多]下拉项中的 [添加子节点]
       */
      const addChildButton = (record) => {
        return {
          label: '添加下级',
          onClick: handleAddChild.bind(null, record),
        };
      };

      const loadParent = ref(false);
      function handleAddChild(record) {
        loadParent.value = true;
        let param = {
          [onlineTableContext.pidField]: record['id'],
        };
        handleAdd(param);
      }

      /**
       *  操作列[更多下拉项]
       */
      function getTreeDropDownActions(record) {
        let arr = getDropDownActions(record, {'themeTemplate': Tree});
        if (buttonSwitch.addSub) {
          arr.unshift(addChildButton(record));
        }
        return arr;
      }

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
        actionColumn,
        dataSource,
        hasQueryCondition,
        pagination,
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
        currentTableName,
        tableType,
        isConfigCurRoute,

        // table-slot
        downloadRowFile,
        getImgView,
        getPcaText,
        getFormatDate,

        // 操作列
        getActions,
        getTreeDropDownActions,

        // 弹窗
        registerModal,
        registerCustomModal,
        registerImportModal,
        importUrl,
        handleFormConfig,
        onlinePopModalRef,
        hrefMainTableId,
        registerOnlineHrefModal,
        registerPopModal,
        popTableId,

        //其他
        tableReloading,
        handleSubmitFlow,
        hrefComponent,
        viewOnlineCellImage,

        //树特定的配置
        onlineTreeTableRef,
        handlerFormSuccess,
        searchReset,
        handleExpand,
        expandedRowKeys,
        handleExpandedRowsChange,
        registerDetailModal,
        handleClickFieldHref,
        registerBpmModal,

        pageLoading,
      };
      //useCompatibleOldVersion(that);
      return that;
    },

    // 1引入了loadsh   console.log(that.simpleDateFormat(new Date().getTime(),'yyyy-MM-dd'));
    // 2. value的问题
    // 3. 变量位置改变后需要 重写api

    // 1添加按钮的时候 预留出样式对象 然后js增强中设置样式对象
    // 2直接设置css字符串 然后通过js document 往head里面增加css片段 全局生效

    // TODO 清空高级查询
    // TODO 积木报表打印地址
    // const reportPrintUrl = ref('')
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
