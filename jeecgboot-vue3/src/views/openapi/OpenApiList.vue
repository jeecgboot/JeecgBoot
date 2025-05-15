<template>
  <div>
    <!--引用表格-->
   <BasicTable @register="registerTable" :rowSelection="rowSelection"  @expand="handleExpand">
      <!-- 内嵌table区域 begin -->
<!--           <template #expandedRowRender="{record}">-->
<!--             <a-tabs tabPosition="top">-->
<!--               <a-tab-pane tab="请求头表" key="openApiHeader" forceRender>-->
<!--                  <openApiHeaderSubTable v-if="expandedRowKeys.includes(record.id)" :id="record.id" />-->
<!--               </a-tab-pane>-->
<!--               <a-tab-pane tab="请求参数部分" key="openApiParam" forceRender>-->
<!--                  <openApiParamSubTable v-if="expandedRowKeys.includes(record.id)" :id="record.id" />-->
<!--               </a-tab-pane>-->
<!--             </a-tabs>-->
<!--           </template>-->
     <!-- 内嵌table区域 end -->
     <!--插槽:table标题-->
      <template #tableTitle>
          <a-button type="primary" v-auth="'openapi:open_api:add'"  @click="handleAdd" preIcon="ant-design:plus-outlined"> 新增</a-button>
          <a-button  type="primary" v-auth="'openapi:open_api:exportXls'"  preIcon="ant-design:export-outlined" @click="onExportXls"> 导出</a-button>
          <j-upload-button  type="primary" v-auth="'openapi:open_api:importExcel'"  preIcon="ant-design:import-outlined" @click="onImportXls">导入</j-upload-button>
          <a-dropdown v-if="selectedRowKeys.length > 0">
              <template #overlay>
                <a-menu>
                  <a-menu-item key="1" @click="batchHandleDelete">
                    <Icon icon="ant-design:delete-outlined"></Icon>
                    删除
                  </a-menu-item>
                </a-menu>
              </template>
              <a-button  v-auth="'openapi:open_api:deleteBatch'">批量操作
                <Icon icon="mdi:chevron-down"></Icon>
              </a-button>
        </a-dropdown>
        <!-- 高级查询 -->
        <super-query :config="superQueryConfig" @search="handleSuperQuery" />
      </template>
       <!--操作栏-->
      <template #action="{ record }">
        <TableAction :actions="getTableAction(record)" :dropDownActions="getDropDownAction(record)"/>
      </template>
      <!--字段回显插槽-->
      <template v-slot:bodyCell="{ column, record, index, text }">
      </template>
    </BasicTable>
    <!-- 表单区域 -->
    <OpenApiModal @register="registerModal" @success="handleSuccess"></OpenApiModal>
  </div>
</template>

<script lang="ts" name="openapi-openApi" setup>
  import {ref, reactive, computed, unref} from 'vue';
  import {BasicTable, useTable, TableAction} from '/@/components/Table';
  import { useListPage } from '/@/hooks/system/useListPage'
  import {useModal} from '/@/components/Modal';
  import OpenApiModal from './components/OpenApiModal.vue'
  import OpenApiHeaderSubTable from './subTables/OpenApiHeaderSubTable.vue'
  import OpenApiParamSubTable from './subTables/OpenApiParamSubTable.vue'
  import {columns, searchFormSchema, superQuerySchema} from './OpenApi.data';
  import {list, deleteOne, batchDelete, getImportUrl,getExportUrl} from './OpenApi.api';
  import {downloadFile} from '/@/utils/common/renderUtils';
  import { useUserStore } from '/@/store/modules/user';
  const queryParam = reactive<any>({});
   // 展开key
  const expandedRowKeys = ref<any[]>([]);
  //注册model
  const [registerModal, {openModal}] = useModal();
  const userStore = useUserStore();
   //注册table数据
  const { prefixCls,tableContext,onExportXls,onImportXls } = useListPage({
      tableProps:{
           title: '接口管理',
           api: list,
           columns,
           canResize:false,
           formConfig: {
                //labelWidth: 120,
                schemas: searchFormSchema,
                autoSubmitOnEnter:true,
                showAdvancedButton:true,
                fieldMapToNumber: [
                ],
                fieldMapToTime: [
                ],
            },
           actionColumn: {
               width: 120,
               fixed:'right'
           },
           beforeFetch: (params) => {
             return Object.assign(params, queryParam);
           },
        },
        exportConfig: {
            name:"接口管理",
            url: getExportUrl,
            params: queryParam,
        },
        importConfig: {
            url: getImportUrl,
            success: handleSuccess
        },
    })

  const [registerTable, {reload},{ rowSelection, selectedRowKeys }] = tableContext

   // 高级查询配置
   const superQueryConfig = reactive(superQuerySchema);

   /**
   * 高级查询事件
   */
   function handleSuperQuery(params) {
     Object.keys(params).map((k) => {
       queryParam[k] = params[k];
     });
     reload();
   }

   /**
     * 展开事件
     * */
   function handleExpand(expanded, record){
        expandedRowKeys.value=[];
        if (expanded === true) {
           expandedRowKeys.value.push(record.id)
        }
    }
   /**
    * 新增事件
    */
  function handleAdd() {
     openModal(true, {
       isUpdate: false,
       showFooter: true,
     });
  }
   /**
    * 编辑事件
    */
  function handleEdit(record: Recordable) {
     openModal(true, {
       record,
       isUpdate: true,
       showFooter: true,
     });
   }
   /**
    * 详情
   */
  function handleDetail(record: Recordable) {
     openModal(true, {
       record,
       isUpdate: true,
       showFooter: false,
     });
   }
   /**
    * 删除事件
    */
  async function handleDelete(record) {
     await deleteOne({id: record.id}, handleSuccess);
   }
   /**
    * 批量删除事件
    */
  async function batchHandleDelete() {
     await batchDelete({ids: selectedRowKeys.value},handleSuccess);
   }
   /**
    * 成功回调
    */
  function handleSuccess() {
      (selectedRowKeys.value = []) && reload();
   }
   /**
      * 操作栏
      */
  function getTableAction(record){
       return [
         {
           label: '编辑',
           onClick: handleEdit.bind(null, record),
           auth: 'openapi:open_api:edit'
         }
       ]
   }


  /**
   * 下拉操作栏
   */
  function getDropDownAction(record){
    return [
      {
        label: '详情',
        onClick: handleDetail.bind(null, record),
      }, {
        label: '删除',
        popConfirm: {
          title: '是否确认删除',
          confirm: handleDelete.bind(null, record),
          placement: 'topLeft'
        },
        auth: 'openapi:open_api:delete'
      }
    ]
  }


</script>

<style lang="less" scoped>
  :deep(.ant-picker),:deep(.ant-input-number){
    width: 100%;
  }
</style>
