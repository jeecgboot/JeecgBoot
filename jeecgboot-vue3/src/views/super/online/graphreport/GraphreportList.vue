<template>
  <div :class="prefixCls">
    <BasicTable @register="registerTable" :rowSelection="rowSelection" v-bind="$attrs">
      <template #tableTitle>
        <a-button @click="onAdd" type="primary" preIcon="ant-design:plus">新增</a-button>
        <a-button preIcon="bxs:bot" type="primary" style="margin-right: 5px" @click="onCreateByAi">AI生成图表</a-button>
        <ExcelButton :config="excelConfig" />
        <a-dropdown v-if="selectedRowKeys.length > 0">
          <template #overlay>
            <a-menu>
              <a-menu-item key="1" @click="onBatchDelete">
                <a-icon type="delete" />
                <span>删除</span>
              </a-menu-item>
            </a-menu>
          </template>
          <a-button>
            <span>批量操作</span>
            <a-icon type="down" />
          </a-button>
        </a-dropdown>
      </template>
      <!--操作栏-->
      <template #action="{ record }">
        <TableAction :actions="getTableAction(record)" :dropDownActions="getDropDownAction(record)" />
      </template>
    </BasicTable>
  </div>
  <GraphreportModal @register="registerFormModal" @success="reload" />
  <!-- 通过Ai生成图表 -->
  <GraphreportAigcModal @register="registerAigcModal" @generate="onAigcGenerate" />
</template>

<script lang="ts">
  import { ref, defineComponent, h } from 'vue';
  import { router } from '/@/router';
  import { useListPage } from '/@/hooks/system/useListPage';
  import { doBatchDelete, list, Api, queryParamsList, doCopy } from './graphreport.api';
  import { ActionItem, BasicTable, TableAction } from '/@/components/Table';
  import ExcelButton from '/@/components/jeecg/ExcelButton.vue';
  import { useModal } from '/@/components/Modal';
  import { useCopyModal } from '../cgform/hooks/useCopyCgformModal';
  import GraphreportModal from './components/GraphreportModal.vue';
  import GraphreportAigcModal from "./components/GraphreportAigcModal.vue";

  // noinspection JSUnusedGlobalSymbols
  export default defineComponent({
    name: 'GraphreportList',
    components: {GraphreportAigcModal, ExcelButton, BasicTable, TableAction, GraphreportModal },
    setup() {
      // 列表页面公共参数、方法
      const { prefixCls, doRequest, doDeleteRecord, tableContext, createConfirm: $confirm } = useListPage({
        designScope: 'online-graphreport-list',
        tableProps: {
          api: list,
          columns: [
            { title: '图表名称', dataIndex: 'name' },
            { title: '编码', dataIndex: 'code' },
            { title: '数据', dataIndex: 'cgrSql' },
            {
              title: '数据源',
              dataIndex: 'dbSource',
              customRender: ({ text, record }) => {
                return record['dbSource_dictText'] ? record['dbSource_dictText'] : text;
              },
            },
            { title: 'Y轴字段', dataIndex: 'yaxisField' },
            { title: 'X轴字段', dataIndex: 'xaxisField' },
          ],
          formConfig: {
            //labelWidth: 200,
            schemas: [
              { label: '图表名称', field: 'name', component: 'JInput' },
              { label: '编码', field: 'code', component: 'JInput' },
            ],
          },
        },
      });
      const [registerTable, { reload }, { rowSelection, selectedRowKeys }] = tableContext;
      /**
       * excel导入导出配置
       */
      const excelConfig = {
        export: {
          name: 'Online图表',
          url: Api.exportXls,
        },
        import: {
          url: Api.importXls,
          success: reload,
        },
      };
      // 注册编辑弹窗
      const [registerFormModal, formModal] = useModal();
      // 注册Aigc弹窗
      const [registerAigcModal, aigcModal] = useModal();

      function onAdd() {
        formModal.openModal(true, { isUpdate: false });
      }

      function onCreateByAi() {
        aigcModal.openModal(true, {});
      }

      function onAigcGenerate(aigcResult: Recordable) {
        formModal.openModal(true, { isUpdate: false, aigc: aigcResult });
      }

      function onEdit(record) {
        formModal.openModal(true, { isUpdate: true, record });
      }

      // 批量删除事件
      async function onBatchDelete() {
        doRequest(() => doBatchDelete(selectedRowKeys.value));
      }

      // 功能测试
      function onGoToTest(record) {
        router.push('/online/graphreport/chart/' + record.id);
      }

      const { createCopyModal } = useCopyModal();

      // 显示配置地址弹窗
      async function onShowOnlineUrl(record) {
        let baseUrl = `/online/graphreport/chart/${record.id}`;
        let copyText = ref(baseUrl);
        createCopyModal({
          title: `配置地址【${record.name}】`,
          content: () => h('div', {}, copyText.value),
          copyText: copyText,
          copyTitle: `${record.name}`,
          componentName: 'GraphreportAutoChart'
        });
        let result = await queryParamsList(record.id);
        if (result && result.length > 0) {
          let str = '?';
          result.forEach((item) => {
            str += item.paramName + '=${' + item.paramName + '}&';
          });
          str = str.substring(0, str.length - 1);
          copyText.value = `${baseUrl}${str}`;
        }
      }

      /**
       * 复制图表
       * @param record
       */
      function onCopyReport(record) {
        $confirm({
          title: '复制图表',
          content: '是否确认复制该图表？',
          iconType: 'warning',
          closable: true,
          onOk() {
            return doCopy(record.id)
              .then(() => {
                // 成功提示由 defHttp 自动弹出（后端 Result 的 message），这里不再手动弹，避免重复提示
                // 强制跳到第 1 页再刷新：新复制的图表落在第 1 页，普通 reload() 沿用 current 会看不到新数据
                reload({ page: 1 });
              })
              .catch((err) => {
                return Promise.reject(err);
              });
          },
        });
      }

      /**
       * 操作栏
       */
      function getTableAction(record) {
        return [
          {
            label: '编辑',
            onClick: () => onEdit(record),
          },
        ];
      }

      /**
       * 下拉操作栏
       */
      function getDropDownAction(record): ActionItem[] {
        return [
          {
            label: '功能测试',
            class: ['low-app-hide'],
            onClick: () => onGoToTest(record),
          },
          {
            label: '配置地址',
            class: ['low-app-hide'],
            onClick: () => onShowOnlineUrl(record),
          },
          {
            label: '复制',
            onClick: () => onCopyReport(record),
          },
          {
            label: '删除',
            popConfirm: {
              title: '确定删除吗？',
              placement: 'left',
              confirm: () => doDeleteRecord(() => doBatchDelete([record.id])),
            },
          },
        ];
      }

      return {
        reload,
        prefixCls,
        rowSelection,
        selectedRowKeys,
        onAdd,
        getTableAction,
        getDropDownAction,
        onBatchDelete,
        registerTable,
        registerFormModal,
        excelConfig,
        registerAigcModal,
        onCreateByAi,
        onAigcGenerate,
      };
    },
  });
</script>
<style lang="less" scoped></style>
