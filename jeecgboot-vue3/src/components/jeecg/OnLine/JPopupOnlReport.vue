<template>
  <div>
    <div class="jeecg-basic-table-form-container" v-if="showSearchFlag">
      <a-form ref="formRef" :model="queryParam" :label-col="labelCol" :wrapper-col="wrapperCol" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <template v-for="(item, index) in queryInfo">
            <template v-if="item.hidden === '1'">
              <a-col :md="6" :sm="24" :key="'query' + index" v-show="toggleSearchStatus">
                <SearchFormItem :formElRef="formRef" :queryParam="queryParam" :item="item" :dictOptions="dictOptions"></SearchFormItem>
              </a-col>
            </template>
            <template v-else>
              <a-col :md="6" :sm="24" :key="'query' + index">
                <SearchFormItem :formElRef="formRef" :queryParam="queryParam" :item="item" :dictOptions="dictOptions"></SearchFormItem>
              </a-col>
            </template>
          </template>

          <a-col :md="6" :sm="8">
            <span style="float: left; overflow: hidden" class="table-page-search-submitButtons">
              <a-col :lg="6">
                <a-button type="primary" preIcon="ant-design:search-outlined" @click="searchQuery">查询</a-button>
                <a-button type="primary" preIcon="ant-design:reload-outlined" @click="searchReset" style="margin-left: 8px">重置</a-button>
                <a @click="handleToggleSearch" style="margin-left: 8px">
                  {{ toggleSearchStatus ? '收起' : '展开' }}
                  <Icon :icon="toggleSearchStatus ? 'ant-design:up-outlined' : 'ant-design:down-outlined'" />
                </a>
              </a-col>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <BasicTable
      ref="tableRef"
      :canResize="true"
      :bordered="true"
      :loading="loading"
      :rowKey="combineRowKey"
      :columns="columns"
      :showIndexColumn="false"
      :dataSource="dataSource"
      :pagination="pagination"
      :rowSelection="rowSelection"
      @row-click="clickThenCheck"
      @change="handleChangeInTable"
    >
      <template #tableTitle>
        <a-button type="primary" preIcon="ant-design:export-outlined" @click="onExportXls"> 导出</a-button>
      </template>
    </BasicTable>

    <!-- 跳转Href的动态组件方式 -->
    <a-modal v-bind="hrefComponent.model" v-on="hrefComponent.on">
      <component :is="hrefComponent.is" v-bind="hrefComponent.params" />
    </a-modal>
  </div>
</template>

<script lang="ts">
  import { defineComponent, unref, ref, watch, watchEffect, reactive, computed } from 'vue';
  import { createAsyncComponent } from '/@/utils/factory/createAsyncComponent';
  import { useAttrs } from '/@/hooks/core/useAttrs';
  import { usePopBiz } from '/@/components/jeecg/OnLine/hooks/usePopBiz';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { useRoute } from 'vue-router';
  
  export default defineComponent({
    name: 'JPopupOnlReport',
    components: {
      SearchFormItem: createAsyncComponent(() => import('/@/components/jeecg/OnLine/SearchFormItem.vue'), { loading: true }),
      BasicTable: createAsyncComponent(() => import('/@/components/Table/src/BasicTable.vue'), {
        loading: true,
      }),
    },
    props: ['multi', 'code', 'id', 'sorter', 'groupId', 'param', 'clickToRowSelect'],
    emits: ['ok', 'register'],
    setup(props, { emit, refs }) {
      const { createMessage } = useMessage();
      const labelCol = reactive({
        xs: { span: 24 },
        sm: { span: 6 },
      });
      const wrapperCol = reactive({
        xs: { span: 24 },
        sm: { span: 18 },
      });
      const formRef = ref();
      const tableRef = ref();
      const toggleSearchStatus = ref(false);
      const attrs = useAttrs();
      const tableScroll = ref({ x: true });
      const route = useRoute();
      console.log('route.query = ',route.query)
      const getBindValue = Object.assign({}, {routeQuery: route.query}, unref(props), unref(attrs));
      
      const [
        {
          visibleChange,
          loadColumnsInfo,
          dynamicParamHandler,
          loadData,
          loadColumnsAndData,
          handleChangeInTable,
          combineRowKey,
          clickThenCheck,
          filterUnuseSelect,
          handleExport,
        },
        {
          hrefComponent,
          visible,
          rowSelection,
          checkedKeys,
          selectRows,
          pagination,
          dataSource,
          columns,
          loading,
          title,
          iSorter,
          queryInfo,
          queryParam,
          dictOptions,
        },
      ] = usePopBiz(getBindValue, tableRef);

      pagination.pageSizeOptions = ['10', '100', '300'];

      const showSearchFlag = computed(() => unref(queryInfo) && unref(queryInfo).length > 0);
      /**
       *监听code
       */
      watch(
        () => props.code,
        () => {
          loadColumnsAndData();
        },
        { immediate: true }
      );
      /**
       *监听popup动态参数 支持系统变量语法
       */
      watch(
        () => props.param,
        () => {
          if (visible) {
            dynamicParamHandler();
            //loadData();
          }
        }
      );
      /**
       *监听sorter排序字段
       */
      watchEffect(() => {
        if (props.sorter) {
          let arr = props.sorter.split('=');
          if (arr.length === 2 && ['asc', 'desc'].includes(arr[1].toLowerCase())) {
            iSorter.value = { column: arr[0], order: arr[1].toLowerCase() };
            // 排序字段受控
            unref(columns).forEach((col) => {
              if (col.dataIndex === unref(iSorter).column) {
                col['sortOrder'] = unref(iSorter).order === 'asc' ? 'ascend' : 'descend';
              } else {
                col['sortOrder'] = false;
              }
            });
          } else {
            console.warn('【JPopup】sorter参数不合法');
          }
        }
      });

      function handleToggleSearch() {
        toggleSearchStatus.value = !unref(toggleSearchStatus);
      }

      /**
       * 导出excel
       */
      function onExportXls() {
        handleExport!();
      }

      /**
       * 查询
       */
      function searchQuery() {
        loadData(1);
      }

      /**
       * 重置
       */
      function searchReset() {
        queryParam.value = {};
        loadData(1);
      }

      return {
        attrs,

        tableScroll,
        dataSource,
        pagination,
        columns,
        rowSelection,
        checkedKeys,
        loading,
        title,
        hrefComponent,

        clickThenCheck,
        loadData,
        combineRowKey,
        handleChangeInTable,
        visibleChange,
        queryInfo,
        queryParam,
        tableRef,
        formRef,
        labelCol,
        wrapperCol,
        dictOptions,
        showSearchFlag,
        toggleSearchStatus,
        handleToggleSearch,
        searchQuery,
        searchReset,
        onExportXls,
      };
    },
  });
</script>

<style lang="less" scoped>
  .jeecg-basic-table-form-container {
    padding: 0px;

    .table-page-search-submitButtons {
      display: block;
      margin-bottom: 0;
      white-space: nowrap;
    }
  }

  :deep(.jeecg-basic-table .ant-table-wrapper .ant-table-title){
    min-height: 0;
  }
  :deep(.ant-select-selector){
    min-width: 95px;
  }
</style>
