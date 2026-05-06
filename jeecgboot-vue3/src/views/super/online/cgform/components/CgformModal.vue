<template>
  <BasicModal
    ref="modalRef"
    :title="title"
    :width="1200"
    :maskClosable="false"
    :defaultFullscreen="true"
    :confirmLoading="confirmLoading"
    v-bind="$attrs"
    wrapClassName="onlForm-config-modal"
    @cancel="onCancel"
    @register="registerModal"
  >
    <div ref="onlFormContentRef" class="onlForm-content">
      <a-spin ref="spinRef" wrapperClassName="pl-2 pr-2" :spinning="confirmLoading">
        <div ref="onlFormContentFormRef" class="onlForm-content-form">
          <CgformHeadForm
            ref="cgformHeadFormRef"
            @tableTypeChange="onTableTypeChange"
            @relationTypeChange="onRelationTypeChange"
            @isTreeChange="onIsTreeChange"
            @extConfigSaved="onExtConfigSaved"
          />
        </div>
        <a-spin :spinning="tableLoading || hideTabs">
          <a-tabs v-show="!hideTabs" v-model:activeKey="activeKey" @change="onTabsChange">
            <a-tab-pane tab="数据库属性" key="dbTable" forceRender>
              <DBAttributeTable
                ref="dbTable"
                :isUpdate="isUpdate"
                :tableName="oldTableName"
                :actionButton="actionButton"
                @added="onTableAdded"
                @removed="onTableRemoved"
                @dragged="onTableDragged"
                @inserted="onTableInserted"
                @syncDbType="onTableSyncDbType"
                @syncDbIsPersist="onTableSyncDbIsPersist"
                @syncDbIsNull="onTableSyncDbIsNull"
              />
            </a-tab-pane>
            <a-tab-pane tab="页面属性" key="pageTable" forceRender>
              <PageAttributeTable ref="pageTable" />
            </a-tab-pane>
            <a-tab-pane tab="校验字段" key="checkTable" forceRender>
              <CheckDictTable ref="checkTable" />
            </a-tab-pane>
            <a-tab-pane tab="外键" key="fkTable" forceRender>
              <ForeignKeyTable ref="fkTable" :actionButton="actionButton" />
            </a-tab-pane>
            <a-tab-pane tab="索引" key="idxTable" forceRender>
              <IndexTable ref="idxTable" :actionButton="actionButton" />
            </a-tab-pane>
            <a-tab-pane key="queryTable" forceRender>
              <template #tab>
                <span>
                  个性查询配置
                  <a-tooltip>
                    <template #title>允许自定义，查询表单字段控件类型！</template>
                    <Icon icon="bx:help-circle"></Icon>
                  </a-tooltip>
                </span>
              </template>
              <QueryTable ref="queryTable" @query="onTableQuery" />
            </a-tab-pane>
          </a-tabs>
        </a-spin>
      </a-spin>
    </div>

    <template #footer>
      <div class="footer-area">
        <div class="rightArea">
          <a-button @click="onCancel">关闭</a-button>
          <a-button type="primary" :loading="confirmLoading" preIcon="ant-design:save" @click="onSubmit">保存</a-button>
        </div>
        <div class="leftArea">
          <div v-if="aiTestMode && !isUpdate">
            <a-select
              v-model:value="aiTestTable"
              placeholder="请选择测试的数据模型"
              :getPopupContainer="(n) => n?.parentElement"
              style="width: 300px; margin: 0 10px 0 0;text-align: left;"
            >
              <template v-for="(item, index) in aiTableList" :key="index">
                <a-select-option :value="item.name">{{ item.title + '（' + item.name + '）' }}</a-select-option>
              </template>
            </a-select>
            <a-button type="primary" ghost @click="initVirtualData">生成数据&gt;&gt;</a-button>
          </div>
          <div v-if="isUpdate" class="positioning-area">
            <a-input v-model:value="positioning" placeholder="请输入字段名称或字段备注" allowClear @pressEnter="handlePositioning"></a-input>
            <a-button type="primary" ghost @click="handlePositioning">定位</a-button>
          </div>
        </div>
      </div>
    </template>
  </BasicModal>
</template>

<script lang="ts">
  import { ref, computed, nextTick, provide, defineComponent, toRaw } from 'vue';
  import { useResizeObserver } from '@vueuse/core';
  import { Icon } from '/@/components/Icon';
  import { BasicModal, useModal, useModalInner } from '/@/components/Modal';
  import { useMessage } from '/@/hooks/web/useMessage';
  import DBAttributeTable from './tables/DBAttributeTable.vue';
  import CgformHeadForm from './CgformHeadForm.vue';
  import PageAttributeTable from './tables/PageAttributeTable.vue';
  import CheckDictTable from './tables/CheckDictTable.vue';
  import ForeignKeyTable from './tables/ForeignKeyTable.vue';
  import IndexTable from './tables/IndexTable.vue';
  import QueryTable from './tables/QueryTable.vue';
  import { CgformModal } from '../types';
  import { useInitialData, VALIDATE_FAILED, useTreeNeedFields } from '../cgform.data';
  import { formApi } from '../cgform.api';
  import { simpleDebounce } from '/@/utils/common/compUtils';
  import { useOnlineTest } from '../hooks/aitest/useOnlineTest';
  import { buildUUID } from '/@/utils/uuid';
  import { sleep } from '/@/utils';

  export default defineComponent({
    name: 'CgformModal',
    components: {
      BasicModal,
      CgformHeadForm,
      DBAttributeTable,
      PageAttributeTable,
      CheckDictTable,
      ForeignKeyTable,
      IndexTable,
      QueryTable,
      Icon,
    },
    emits: ['success', 'register'],
    props: {
      actionButton: {
        type: Boolean,
        default: true,
        required: false,
      },
    },
    setup(props, { emit }) {
      const { createMessage: $message } = useMessage();
      const modalRef = ref();
      const spinRef = ref();
      // 是否是更新模式
      const isUpdate = ref(false);
      // 编辑时存储的值
      let model: Recordable = {};
      const title = computed(() => (isUpdate.value ? '编辑' : '新增'));
      // 当前是否正在加载中
      const confirmLoading = ref(true);
      // 表格区域正在加载中
      const tableLoading = ref(false);
      // tabs当前活动的页面
      const activeKey = ref('dbTable');
      // 解决打开弹窗速度缓慢的问题
      const hideTabs = ref(true);
      // 标记是否已经初始化过（v-show模式下组件不会被销毁）
      let tabsInitialized = false;
      // table refs
      const tables: CgformModal.TablesRef = {
        dbTable: ref<CgformModal.DBAttributeTableType>(),
        pageTable: ref<CgformModal.PageAttributeTableType>(),
        checkTable: ref<CgformModal.CheckDictTableType>(),
        fkTable: ref<CgformModal.ForeignKeyTableType>(),
        idxTable: ref<CgformModal.IndexTableType>(),
        queryTable: ref<CgformModal.QueryTableType>(),
      };
      // 当前是否是全屏状态
      const fullScreenRef = computed(() => modalRef.value?.fullScreenRef ?? false);
      provide('tables', tables);
      provide('fullScreenRef', fullScreenRef);
      const positioning = ref('');
      const onlFormContentRef = ref<HTMLElement>();
      const onlFormContentFormRef = ref<HTMLElement>();
      /** `.onlForm-content-form` 当前高度（随布局/展开收起等变化） */
      const onlFormContentFormHeight = ref(0);
      const vxetableHeight = ref(0);
      provide('vxetableHeight', vxetableHeight);
      useResizeObserver(onlFormContentFormRef, (entries) => {
        const entry = entries[0];
        if (entry) {
          onlFormContentFormHeight.value = entry.contentRect.height;
          vxetableHeight.value = onlFormContentRef.value!.offsetHeight - onlFormContentFormHeight.value - 128;
        }
      });

      // 头部表单子组件 ref
      const cgformHeadFormRef = ref<InstanceType<typeof CgformHeadForm>>();
      // 代理 formAction 供 ExtendConfigModal 和 useOnlineTest 使用
      const formAction = {
        getFieldsValue: (fields?: string[]) => cgformHeadFormRef.value?.getFieldsValue(fields) ?? {},
        setFieldsValue: (values: Recordable) => cgformHeadFormRef.value?.setFieldsValue(values),
        validateFields: (_fields?: string[]) => Promise.resolve(),
        resetFields: () => cgformHeadFormRef.value?.resetFields(),
      };
      // 表单赋值
      const [registerModal, { closeModal }] = useModalInner((data) => {
        isUpdate.value = data?.isUpdate ?? false;
        if (isUpdate.value) {
          edit(data?.record);
        } else {
          add();
        }
      });
      // 旧表名
      const oldTableName = ref('');
      // 立即同步所有 table（防抖版）
      const syncAllTableNowDebounce = simpleDebounce(() => syncAllTableNowPromise(), 150);
      // 临时数据ID，不提交到后台
      let fieldTempIds: string[] = [];
      // 是否显示附表字段
      let showSubTableStr = false;
      // 是否已添加树表单字段
      let treeFieldAdded = false;
      // 已添加的树表单字段ID
      let treeFieldIds: string[] = [];
      let interval: any = null;

      // hook OnlineTest
      const { aiTestMode, aiTestTable, aiTableList, initVirtualData, tableJsonGetHelper, refreshCacheTableName } = useOnlineTest(
        {
          oldTableName,
          tables,
        },
        {
          initialAllShowItem,
          setAllTableData,
        },
        formAction
      );

      function add() {
        edit({});
      }

      async function edit(record) {
        confirmLoading.value = false;
        activeKey.value = 'dbTable';
        // 重置表单
        cgformHeadFormRef.value?.resetFields();
        model = Object.assign({}, record);
        initialAllShowItem(model);
        // 重置定位内容
        positioning.value = '';
        
        // 便于 ai test data 复制
        tableJsonGetHelper(model);

        cgformHeadFormRef.value?.initialExtConfigJson(model);
        cgformHeadFormRef.value?.setFieldsValue(model);
        oldTableName.value = model.tableName;
        // update-begin--author:liaozhiyang---date:20260330---for:【QQYUN-13610】解决online打开慢的问题
        // 解决打开弹窗速度缓慢的问题（第一次延迟显示，后续直接显示）
        if (tabsInitialized) {
          hideTabs.value = false;
          // v-show模式下组件不会被销毁，需要先清空所有表的旧数据
          clearAllTableData();
        } else {
          sleep(1, () => {
            hideTabs.value = false;
            tabsInitialized = true;
          });
        }
        // update-end--author:liaozhiyang---date:20260330---for:【QQYUN-13610】解决online打开慢的问题
        // update模式，加载数据库中的数据
        if (isUpdate.value) {
          // update-begin--author:liaozhiyang---date:20260210---for:【QQYUN-13658】Jvxetable、vxetable按需加载
          await getRefPromise(tables.dbTable);
          // update-end--author:liaozhiyang---date:20260209---for:【QQYUN-13658】Jvxetable、vxetable按需加载
          tables.dbTable.value?.setDataSource([]);
          await loadFields(model.id);
          // 加载index的数据，由于默认不可见，所以可以分开加载，提升加载效率
          await loadIndexes(model.id);
          // 设置子表下拉选择
          getRefPromise(tables.pageTable).then(() => {
            tables.pageTable.value!.changePageType(model.tableType == 3, model.relationType);
          });
        } else {
          // 添加初始数据
          let { initialData, tempIds } = useInitialData();
          await setAllTableData(initialData, true);
          fieldTempIds = tempIds;
        }
      }

      // 加载表字段配置
      async function loadFields(headId) {
        tableLoading.value = true;
        try {
          let fields = await formApi.doQueryField(headId);
          // 便于 ai test data 复制
          console.log("online fields:", fields)
          tableLoading.value = false;
          await setAllTableData(fields);
        } finally {
          tableLoading.value = false;
        }
      }

      // 加载表索引配置
      async function loadIndexes(headId) {
        let indexes = await formApi.doQueryIndexes(headId);
        // 便于 ai test data 复制
        console.log("online indexs:", indexes)
        // update-begin--author:liaozhiyang---date:20260210---for:【QQYUN-13658】Jvxetable、vxetable按需加载
        await getRefPromise(tables.idxTable);
        // update-end--author:liaozhiyang---date:20260209---for:【QQYUN-13658】Jvxetable、vxetable按需加载
        tables.idxTable.value!.setDataSource(indexes);
      }

      function initialAllShowItem(model) {
        treeFieldAdded = model.isTree == 'Y';
        showSubTableStr = model.tableType === 2;
      }
      // update-begin--author:liaozhiyang---date:20260330---for:【QQYUN-13610】解决online打开慢的问题
      // 清空所有表的数据（v-show模式下切换数据前调用，避免旧ID匹配报错）
      function clearAllTableData() {
        const { dbTable, pageTable, checkTable, fkTable, idxTable, queryTable } = tables;
        dbTable.value?.setDataSource([]);
        pageTable.value?.setDataSource([]);
        checkTable.value?.setDataSource([]);
        fkTable.value?.setDataSource([]);
        idxTable.value?.setDataSource([]);
        queryTable.value?.setDataSource([]);
      }
      // update-end--author:liaozhiyang---date:20260330---for:【QQYUN-13610】解决online打开慢的问题

      // 设置除索引配置之外所有的JVxeTable的数据
      async function setAllTableData(data: Recordable[], insert?) {
        const { dbTable, pageTable, checkTable, fkTable, queryTable } = tables;
        // update-begin--author:liaozhiyang---date:20260210---for:【QQYUN-13658】Jvxetable、vxetable按需加载
        await getRefPromise(dbTable);
        // update-end--author:liaozhiyang---date:20260209---for:【QQYUN-13658】Jvxetable、vxetable按需加载
        dbTable.value!.setDataSource(data, insert);
        // 先加载第一个tab的数据，延时加载其他tab，可以使打开速度的视觉效果更好
        // update-begin--author:liaozhiyang---date:20260210---for:【QQYUN-13658】Jvxetable、vxetable按需加载
        setTimeout(async () => {
          await Promise.all([
            getRefPromise(pageTable),
            getRefPromise(checkTable),
            getRefPromise(fkTable),
            getRefPromise(queryTable),
          ]);
          // update-end--author:liaozhiyang---date:20260209---for:【QQYUN-13658】Jvxetable、vxetable按需加载
          pageTable.value!.setDataSource(data, insert);
          checkTable.value!.setDataSource(data, insert);
          fkTable.value!.setDataSource(data, insert);
          queryTable.value!.setDataSource(data, insert);
        }, 10);
      }

      /** ATab切换事件 */
      function onTabsChange(activeKey) {
        // 当切换了选项卡的时候只同步修改当前所能看到的table
        if (['pageTable', 'checkTable', 'fkTable', 'idxTable', 'queryTable'].indexOf(activeKey) !== -1) {
          const dbTable = tables.dbTable;
          const table = tables[activeKey];
          dbTable.value!.tableRef!.resetScrollTop();
          // update-begin--author:liaozhiyang---date:20260316---for:【QQYUN-13751】jVxetable优化
          clearInterval(interval);
          interval = setTimeout(() => {
            table.value.syncTable(dbTable);
          }, 200);
          // update-end--author:liaozhiyang---date:20260316---for:【QQYUN-13751】jVxetable优化
        }
      }

      // 表类型tableType字段change事件（由子组件 emit 触发，formModel 已在子组件内更新）
      function onTableTypeChange(value) {
        const relationType = cgformHeadFormRef.value?.formModel.relationType ?? 0;
        tables.pageTable.value!.changePageType(value == 3, relationType);
      }
      // update-begin--author:liaozhiyang---date:20260317---for:【QQYUN-9441】online一对多加上关联记录和他表字段
      // 关联类型relationType字段change事件
      function onRelationTypeChange(value) {
        const tableType = cgformHeadFormRef.value?.formModel.tableType ?? 1;
        tables.pageTable.value!.changePageType(tableType == 3, value);
      }
      // update-end--author:liaozhiyang---date:20260317---for:【QQYUN-9441】online一对多加上关联记录和他表字段

      // 是否树isTree字段change事件
      function onIsTreeChange(value) {
        value === 'Y' ? addTreeNeedField() : deleteTreeNeedField();
      }

      /** 立即主动同步所有table */
      function syncAllTableNow() {
        syncAllTableNowDebounce();
      }

      // 立即同步所有 table
      async function syncAllTableNowPromise() {
        let { dbTable, pageTable, checkTable, fkTable, queryTable } = tables;
        await pageTable.value!.syncTable(dbTable);
        await checkTable.value!.syncTable(dbTable);
        await fkTable.value!.syncTable(dbTable);
        await queryTable.value!.syncTable(dbTable);
      }

      // update-begin--author:liaozhiyang---date:20260414---for：【QQYUN-15128】新增字段时系统字段永远在最下面
      const SYS_BUILT_IN_FIELDS = ['create_by', 'create_time', 'update_by', 'update_time', 'sys_org_code'];
      // update-end--author:liaozhiyang---date:20260414---for：【QQYUN-15128】新增字段时系统字段永远在最下面

      // update-begin--author:liaozhiyang---date:20260506---for:【issues/9593】树表的页面属性中子节点和父节点的属性串了
      let onTableAddedQueue: Promise<unknown> = Promise.resolve();
      // update-end--author:liaozhiyang---date:20260506---for:【issues/9593】树表的页面属性中子节点和父节点的属性串了

      /** 当新增了的时候应立即同步 */
      function onTableAdded() {
        // update-begin--author:liaozhiyang---date:20260506---for: 【issues/9593】树表的页面属性中子节点和父节点的属性串了
        onTableAddedQueue = onTableAddedQueue.then(async () => {
          // update-begin--author:liaozhiyang---date:20260414---for：【QQYUN-15128】新增行时系统字段永远在最下面（系统字段被删除时退化为正常追加）
          const dbJVxeRef = tables.dbTable.value?.tableRef;
          if (dbJVxeRef) {
            const fullData = dbJVxeRef.getXTable().internalData.tableFullData;
            const sysIndex = fullData.findIndex((row) => SYS_BUILT_IN_FIELDS.includes(row.dbFieldName));
            const lastIndex = fullData.length - 1;
            // 末尾是新增行，且在系统字段后面，且本身不是系统字段
            if (sysIndex !== -1 && lastIndex > sysIndex && !SYS_BUILT_IN_FIELDS.includes(fullData[lastIndex]?.dbFieldName)) {
              const newRowData = { ...fullData[lastIndex] };
              // 在 dbTable 中把末尾新行移到第一个系统字段前面
              await dbJVxeRef.rowResort(lastIndex, sysIndex);
              // 对其他 tables 直接在系统字段前面插入，不走 syncAllTableNow 的追加逻辑
              const { pageTable, checkTable, fkTable, queryTable } = tables;
              for (const t of [pageTable, checkTable, fkTable, queryTable]) {
                const jvxeRef = t.value?.tableRef;
                if (!jvxeRef) continue;
                const tFullData = jvxeRef.getXTable().internalData.tableFullData;
                const tSysIndex = tFullData.findIndex((row) => SYS_BUILT_IN_FIELDS.includes(row.dbFieldName));
                // 串行 await 附表 insertRows/addRows，确保下一次队列任务读到的是完全收尾后的状态
                if (tSysIndex !== -1) {
                  await jvxeRef.insertRows(newRowData, tSysIndex);
                } else {
                  await jvxeRef.addRows(newRowData);
                }
              }
              return;
            }
          }
          // update-end--author:liaozhiyang---date:20260414---for：【QQYUN-15128】新增行时系统字段永远在最下面（系统字段被删除时退化为正常追加）
          syncAllTableNow();
        });
        // 兜底：队列异常不阻塞后续调用
        onTableAddedQueue = onTableAddedQueue.catch((e) => {
          console.error('[onTableAdded] queue error:', e);
        });
        // update-end--author:liaozhiyang---date:20260506---for: 【issues/9593】树表的页面属性中子节点和父节点的属性串了
      }

      /** 当删除的时候也应立即同步 */
      function onTableRemoved() {
        syncAllTableNow();
      }

      /** 当拖动后立即同步 */
      function onTableDragged(event) {
        let { oldIndex, newIndex } = event;
        syncAllOrderNumNow(oldIndex, newIndex);
      }

      /** 当插入后立即同步 */
      async function onTableInserted(event) {
        let { insertIndex, row } = event;
        let { pageTable, checkTable, fkTable, queryTable } = tables;
        pageTable.value!.tableRef!.insertRows(row, insertIndex);
        checkTable.value!.tableRef!.insertRows(row, insertIndex);
        fkTable.value!.tableRef!.insertRows(row, insertIndex);
        queryTable.value!.tableRef!.insertRows(row, insertIndex);
      }

      /** 立即同步所有的表的排序顺序 */
      function syncAllOrderNumNow(oldIndex: number, newIndex: number) {
        let { pageTable, checkTable, fkTable, queryTable } = tables;
        pageTable.value!.tableRef!.rowResort(oldIndex, newIndex);
        checkTable.value!.tableRef!.rowResort(oldIndex, newIndex);
        fkTable.value!.tableRef!.rowResort(oldIndex, newIndex);
        queryTable.value!.tableRef!.rowResort(oldIndex, newIndex);
      }

      /** 当value变化时同步 date */
      function onTableSyncDbType(event) {
        tables.pageTable.value!.syncFieldShowType(event.row);
      }
      // update-begin--author:liaozhiyang---date:20240313---for：【QQYUN-8485】不同步数据库的字段则去掉对应查询勾选
      /** 当dbIsPersist（同步数据库） value变化时同步 查询去掉勾选 */
      function onTableSyncDbIsPersist(event) {
        tables.pageTable.value!.syncIsQuery(event.row);
      }
      // update-end--author:liaozhiyang---date:20240313---for：【QQYUN-8485】不同步数据库的字段则去掉对应查询勾选
      // update-begin--author:liaozhiyang---date:20240313---for：【QQYUN-8485】数据库不允许为空，校验默认相应勾上
      /** 当dbIsNull(不允许空值) value变化时同步 校验必填 */
      function onTableSyncDbIsNull(event) {
        tables.checkTable.value!.syncFieldMustInput(event.row);
      }
      // update-end--author:liaozhiyang---date:20240313---for：【QQYUN-8485】数据库不允许为空，校验默认相应勾上

      function onTableQuery(id) {
        tables.pageTable.value!.enableQuery(id);
      }

      /** 添加树字段 */
      function addTreeNeedField() {
        if (!treeFieldAdded) {
          let { dbTable, pageTable, checkTable } = tables;
          let treeFields = useTreeNeedFields();
          treeFields = treeFields.filter((item: any) => {
            let nameList = dbTable.value!.tableRef!.getTableData().map((o) => o.dbFieldName);
            return !nameList.includes(item.dbFieldName);
          });
          treeFieldIds = [];
          treeFields.forEach((newData: any) => {
            let uuidTemp = buildUUID() + '__tempId';
            treeFieldIds.push(uuidTemp);
            newData.id = uuidTemp;
          });
          dbTable.value!.tableRef!.addRows(treeFields, { setActive: false });
          // update-begin--author:liaozhiyang---date:20260506---for: 【issues/9593】树表的页面属性中子节点和父节点的属性串了
          // pageTable.value!.tableRef!.addRows(treeFields, { setActive: false });
          // checkTable.value!.tableRef!.addRows(treeFields, { setActive: false });
          // update-end--author:liaozhiyang---date:20260506---for: 【issues/9593】树表的页面属性中子节点和父节点的属性串了
          nextTick(() => syncAllTableNow());
          treeFieldAdded = true;
        }
        nextTick(() => {
          if (cgformHeadFormRef.value) {
            cgformHeadFormRef.value.formModel.treeIdField = 'has_child';
            cgformHeadFormRef.value.formModel.treeParentIdField = 'pid';
          }
        });
      }

      /** 删除树字段 */
      function deleteTreeNeedField() {
        if (treeFieldIds && treeFieldIds.length > 0) {
          let { dbTable } = tables;
          dbTable.value!.tableDeleteLines(treeFieldIds);
          treeFieldIds = [];
          treeFieldAdded = false;
        }
      }

      // 触发所有表单验证
      function validateAll() {
        let options = {};
        return new Promise((resolve, reject) => {
          // 验证主表表单
          cgformHeadFormRef.value!.validate().then(
            (values) => resolve({ values }),
            (errMsg) => {
              if (errMsg) $message.warning(errMsg);
              reject(VALIDATE_FAILED);
            }
          );
        })
          .then((result) => {
            Object.assign(options, result);
            return validateTableFields();
          })
          .then((allTableData) => {
            Object.assign(options, allTableData);
            let formData = classifyIntoFormData(options);
            return validateForeignKey(formData);
          })
          .catch((e) => {
            if (e === VALIDATE_FAILED || e?.code === VALIDATE_FAILED) {
              // 表单校验失败时消息已在 validate() 内提示，此处不重复
            } else {
              // update-begin--author:liaozhiyang---date:20231226---for：【QQYUN-7503】附表配置多个外键，保存失败没提示
              e?.msg ? $message.warning(e.msg) : console.error(e);
              // update-end--author:liaozhiyang---date:20231226---for：【QQYUN-7503】附表配置多个外键，保存失败没提示
            }
            return Promise.reject(null);
          });
      }

      /** 验证并获取所有表的数据 */
      function validateTableFields() {
        return new Promise(async (resolve, reject) => {
          let tableKeys = Object.keys(tables);
          let allTableData: any = {};
          for (let i = 0; i < tableKeys.length; i++) {
            let key = tableKeys[i];
            let table = tables[key];
            try {
              allTableData[key] = await table.value!.validateData(key);
            } catch (e: any) {
              if (e.code === VALIDATE_FAILED) {
                // 未通过就跳转到相应的tab选项卡
                activeKey.value = e.activeKey;
              } else {
                console.error(e);
              }
              reject(e);
              return;
            }
          }
          resolve(allTableData);
        });
      }

      /** 将所有表的数据整理整合成后台识别的formData */
      function classifyIntoFormData(options) {
        // 整理数据
        let formData = {
          head: {} as Recordable,
          fields: [] as any[],
          indexs: [] as any[],
          deleteFieldIds: [] as any[],
          deleteIndexIds: [] as any[],
        };
        formData.head = Object.assign(model, options.values);
        // update-begin--author:liaozhiyang---date:20260401---for：【QQYUN-14949】online配置中尽可能多的显示vxetable字段
        formData.head.formCategory = 'temp';
        formData.head.idType = 'UUID';
        // update-end--author:liaozhiyang---date:20260401---for：【QQYUN-14949】online配置中尽可能多的显示vxetable字段
        // 整理online表单扩展JSON
        const extConfigJson = { ...cgformHeadFormRef.value?.getExtConfigJson() };
        formData.head.isDesForm = extConfigJson.isDesForm;
        formData.head.desFormCode = extConfigJson.desFormCode;
        delete extConfigJson.isDesForm;
        delete extConfigJson.desFormCode;
        formData.head.extConfigJson = JSON.stringify(extConfigJson);
        // 整理 fields
        options.dbTable.tableData.forEach((item, index) => {
          // ID 以 dbTable 的 ID 为准
          let rowId = item.id;
          let fields = Object.assign({}, item);

          let pageTable = options.pageTable.tableData[index];
          fields = Object.assign(pageTable, fields);

          let checkTable = options.checkTable.tableData[index];
          fields = Object.assign(checkTable, fields);

          let fkTable = options.fkTable.tableData[index];
          fields = Object.assign(fkTable, fields);

          let queryTable = options.queryTable.tableData[index];
          fields = Object.assign(queryTable, fields);

          // 如果 dbTable 没有返回id，则代表是新增的数据
          if (rowId == null || rowId === '') {
            delete fields.id;
          } else {
            fields.id = rowId;
          }
          // 去掉临时ID
          let tempIds = ([] as string[]).concat(fieldTempIds, treeFieldIds);
          if (tempIds.includes(fields.id)) {
            delete fields.id;
          }
          formData.fields.push(fields);
        });
        formData.deleteFieldIds = options.dbTable.deleteIds;
        // 整理 index
        formData.indexs = options.idxTable.tableData;
        formData.deleteIndexIds = options.idxTable.deleteIds;
        return formData;
      }

      /** 外键配置校验 只能配置一个 */
      function validateForeignKey(formData) {
        // 1.配置两个 校验
        // 2.配置一个后，保存，再配置新的 删除老的 校验
        // 3.配置一个后，保存,修改当前为新的 校验
        return new Promise((resolve, reject) => {
          let fields = formData.fields;
          let saved = true;
          if (fields && fields.length > 0) {
            let hasForeignKey = 0;
            for (let i = 0; i < fields.length; i++) {
              if (fields[i].mainField || fields[i].mainTable) {
                hasForeignKey += 1;
              }
              if (hasForeignKey > 1) {
                saved = false;
                break;
              }
            }
          }
          if (saved) {
            resolve(formData);
          } else {
            reject({
              code: -1,
              msg: '外键只允许配置一个!',
              error: VALIDATE_FAILED,
            });
          }
        });
      }

      // 表单提交事件
      function onSubmit() {
        confirmLoading.value = true;
        validateAll()
          .then(
            async (formData: any) => {
              // 表字段转小写
              if (formData.fields && formData.fields.length > 0) {
                for (let field of formData.fields) {
                  field.dbFieldName = field.dbFieldName.toLowerCase().trim();
                }
              }
              if (formData.head?.tableName) {
                formData.head.tableName = formData.head.tableName.toLowerCase().trim();
              }
              // 发起请求
              await formApi.doSaveOrUpdate(formData, isUpdate.value);
              refreshCacheTableName(oldTableName.value, formData.head['tableName']);
              emit('success');
              // 解决关闭弹窗时会闪一下的问题，因为同时加载多个JVxeTable造成的卡顿影响了弹窗关闭效果
              sleep(1, () => onCancel());
            },
            (e) => {
              console.error(e);
            }
          )
          .finally(() => {
            confirmLoading.value = false;
          });
      }

      //update-begin-author:taoyan date:2022-8-15 for: VUEN-1891 online表单编辑时 修改了扩展配置能否 确认即保存，不用再点整个表单得确定
      async function onExtConfigSaved(values) {
        if (isUpdate.value) {
          const params = {
            id: model.id,
            extConfigJson: JSON.stringify(toRaw(values)),
          };
          await formApi.editHead(params);
          emit('success');
        }
      }
      //update-end-author:taoyan date:2022-8-15 for: VUEN-1891 online表单编辑时 修改了扩展配置能否 确认即保存，不用再点整个表单得确定

      function onCancel() {
        hideTabs.value = true;
        // 解决关闭弹窗时会闪一下的问题，因为同时加载多个JVxeTable造成的卡顿影响了弹窗关闭效果
        sleep(1, () => closeModal());
      }
      /**
       * 2024-07-17
       * liaozhiyang
       * 【TV360X-829】根据字典名称和字段备注快速定位到行
       * */
      const handlePositioning = () => {
        const val = positioning.value.trim();
        if (val.length) {
          const jVxe_instance = tables[activeKey.value].value.tableRef;
          const vxe_instance = jVxe_instance.getXTable();
          const fullData = vxe_instance.getTableData().fullData;
          // 先精确，再模糊
          const preciseIndex = fullData.findIndex((item) => val === item.dbFieldName || val === item.dbFieldTxt);
          let index = -1;
          if (preciseIndex == -1) {
            // 模糊
            const dimIndex = fullData.findIndex((item) => item.dbFieldName.includes(positioning.value) || item.dbFieldTxt.includes(positioning.value));
            index = dimIndex;
          } else {
            index = preciseIndex;
          }
          if(index != -1) {
            const row = fullData[index];
            vxe_instance.scrollToRow(row).then(() => {
              const { refTableBody } = vxe_instance.getRefMaps();
              const tableBody = refTableBody.value;
              const bodyElem = tableBody ? tableBody.$el : null;
              if (bodyElem) {
                const trElem = bodyElem.querySelector(`[rowid="${vxe_instance.getRowid(row)}"]`);
                if (trElem) {
                  trElem.classList.add('customHighlight');
                  setTimeout(() => {
                    trElem?.classList.remove('customHighlight');
                  }, 1e3);
                }
              }
              // update-begin--author:liaozhiyang---date:20260330---for：【QQYUN-15058】解决字段定位在小屏幕上看不见的问题
              // 将弹窗竖向滚动条滚动到最底部
              const spinEl = spinRef.value?.$el || spinRef.value;
              const scrollContainer = spinEl?.closest('.scrollbar__wrap');
              if (scrollContainer) {
                scrollContainer.scrollTop = scrollContainer.scrollHeight;
              }
              // update-end--author:liaozhiyang---date:20260330---for：【QQYUN-15058】解决字段定位在小屏幕上看不见的问题
            });
          } else {
            $message.warning('没搜到相关字段名称或字段备注~');
          }
        } else {
          $message.warning('请输入字段名称或字段备注~');
        }
      };
      function getRefPromise(componentRef) {
        return new Promise((resolve) => {
          function next() {
            let ref = componentRef.value;
            if (ref?.tableRef?.getXTable) {
              resolve(ref);
            } else {
              requestAnimationFrame(next);
            }
          }
          next();
        });
      }
      return {
        ...tables,
        modalRef,
        onlFormContentRef,
        onlFormContentFormRef,
        onlFormContentFormHeight,
        spinRef,
        title,
        confirmLoading,
        tableLoading,
        activeKey,
        onCancel,
        formAction,
        hideTabs,
        onSubmit,
        onTabsChange,
        onTableAdded,
        onTableRemoved,
        onTableDragged,
        onTableInserted,
        onTableSyncDbType,
        onTableQuery,
        onExtConfigSaved,
        registerModal,
        // hook OnlineTest
        aiTestMode,
        aiTestTable,
        aiTableList,
        initVirtualData,
        onTableSyncDbIsPersist,
        onTableSyncDbIsNull,
        isUpdate,
        positioning,
        handlePositioning,
        oldTableName,
        onIsTreeChange,
        // 头部表单子组件
        cgformHeadFormRef,
        onTableTypeChange,
        onRelationTypeChange,
      };
    },
  });
</script>

<style lang="less" scoped>
  .onlForm-content {
    height: 100%;
  }
  // update-begin--author:liaozhiyang---date:20240717---for：【TV360X-829】根据字典名称和字段备注快速定位到行
  .footer-area {
    display: flex;
    justify-content: space-between;
    flex-direction: row-reverse;
    .leftArea {
      display: flex;
      > * {
        &:not(:first-child) {
          margin-left: 16px;
        }
      }
    }
  }

  .positioning-area {
    width: 280px;
    display: flex;
    > :first-child {
      margin-right: 8px;
    }
  }
  :deep(.vxe-table) {
    .vxe-body--row.customHighlight {
      background-color: var(--vxe-ui-table-row-hover-background-color);
    }
  }
  // update-begin--author:liaozhiyang---date:20240717---for：【TV360X-829】根据字典名称和字段备注快速定位到行
</style>
<style>
  .onlForm-config-modal {
    .scroll-container .scrollbar__wrap {
      margin-bottom: 0 !important;
    }
  }
</style>
