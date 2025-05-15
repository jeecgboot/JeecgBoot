<template>
  <!--popup选择框-->
  <div>
    <BasicModal
      v-bind="$attrs"
      @register="register"
      :title="title"
      :width="1200"
      @ok="handleSubmit"
      @cancel="handleCancel"
      cancelText="关闭"
      wrapClassName="j-popup-modal"
      @visible-change="visibleChange"
    >
      <div class="jeecg-basic-table-form-container">
        <a-form ref="formRef" v-if="showSearchFlag" :model="queryParam" :label-col="labelCol" :wrapper-col="wrapperCol" @keyup.enter.native="searchQuery">
          <a-row :gutter="24">
            <template v-for="(item, index) in queryInfo">
              <template v-if="item.hidden === '1'">
                <a-col :md="8" :sm="24" :key="'query' + index" v-show="toggleSearchStatus">
                  <SearchFormItem :formElRef="formRef" :queryParam="queryParam" :item="item" :dictOptions="dictOptions"></SearchFormItem>
                </a-col>
              </template>
              <template v-else>
                <a-col :md="8" :sm="24" :key="'query' + index">
                  <SearchFormItem :formElRef="formRef" :queryParam="queryParam" :item="item" :dictOptions="dictOptions"></SearchFormItem>
                </a-col>
              </template>
            </template>

            <a-col :md="8" :sm="8" v-if="showAdvancedButton">
              <span style="float: left; overflow: hidden" class="table-page-search-submitButtons">
                <a-col :lg="6">
                  <a-button type="primary" preIcon="ant-design:search-outlined" @click="searchQuery">查询</a-button>
                  <a-button preIcon="ant-design:reload-outlined" @click="searchReset" style="margin-left: 8px">重置</a-button>
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
        :canResize="false"
        :bordered="true"
        :loading="loading"
        :rowKey="rowkey ? rowkey : combineRowKey"
        :columns="columns"
        :showIndexColumn="false"
        :dataSource="dataSource"
        :pagination="pagination"
        :rowSelection="rowSelection"
        @row-click="clickThenCheck"
        @change="handleChangeInTable"
      >
        <template #tableTitle></template>
         <template #bodyCell="{text, column}">
          <template v-if="column.fieldType === 'Image'">
            <span v-if="!text" style="font-size: 12px; font-style: italic">无图片</span>
            <img v-else :src="getImgView(text)" alt="图片不存在" class="cellIamge" @click="viewOnlineCellImage($event, text)" />
          </template>
        </template>
      </BasicTable>
    </BasicModal>
  </div>
</template>

<script lang="ts">
  import { defineComponent, unref, ref, watch, watchEffect, reactive, computed } from 'vue';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { createAsyncComponent } from '/@/utils/factory/createAsyncComponent';
  import { useAttrs } from '/@/hooks/core/useAttrs';
  import { usePopBiz } from '/@/components/jeecg/OnLine/hooks/usePopBiz';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { getFileAccessHttpUrl } from '/@/utils/common/compUtils';
  import { createImgPreview } from '/@/components/Preview/index';

  export default defineComponent({
    name: 'JPopupOnlReportModal',
    components: {
      //此处需要异步加载BasicTable
      BasicModal,
      SearchFormItem: createAsyncComponent(() => import('/@/components/jeecg/OnLine/SearchFormItem.vue'), { loading: false }),
      BasicTable: createAsyncComponent(() => import('/@/components/Table/src/BasicTable.vue'), {
        loading: true,
      }),
    },
    props: ['multi', 'code', 'sorter', 'groupId', 'param','showAdvancedButton', 'getFormValues', 'selected', 'rowkey'],
    emits: ['ok', 'register'],
    setup(props, { emit }) {
      const { createMessage } = useMessage();
      const labelCol = reactive({
        xs: { span: 24 },
        sm: { span: 6 },
      });
      const wrapperCol = reactive({
        xs: { span: 24 },
        sm: { span: 18 },
      });
      //注册弹框
      const [register, { closeModal }] = useModalInner();
      const formRef = ref();
      const tableRef = ref();
      const toggleSearchStatus = ref(false);
      const attrs = useAttrs();
      const tableScroll = ref({ x: true });
      // update-begin--author:liaozhiyang---date:20230811---for：【issues/675】子表字段Popup弹框数据不更新
      const getBindValue = computed(() => {
        return Object.assign({}, unref(props), unref(attrs));
      });
      // update-end--author:liaozhiyang---date:20230811---for：【issues/675】子表字段Popup弹框数据不更新
      const [
        {
          visibleChange,
          loadColumnsInfo,
          dynamicParamHandler,
          loadData,
          handleChangeInTable,
          combineRowKey,
          clickThenCheck,
          filterUnuseSelect,
          getOkSelectRows,
        },
        {
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
      ] = usePopBiz(getBindValue,tableRef);

      const showSearchFlag = computed(() => unref(queryInfo) && unref(queryInfo).length > 0);
      /**
       *监听code
       */
      watch(
        () => props.code,
        () => {
          loadColumnsInfo();
        }
      );
      /**
       *监听popup动态参数 支持系统变量语法
       */
      watch(
        () => props.param,
        () => {
          // update-begin--author:liaozhiyang---date:20231213---for：【issues/901】JPopup组件配置param参数后异常
          if (visible.value) {
            dynamicParamHandler();
            loadData();
          }
          // update-end--author:liaozhiyang---date:20231213---for：【issues/901】JPopup组件配置param参数后异常
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

      //update-begin-author:taoyan date:2022-5-31 for: VUEN-1156 popup 多数据有分页时，选中其他页，关闭popup 再点开，分页仍然选中上一次点击的分页，但数据是第一页的数据 未刷新
      watch(
        () => pagination.current,
        (current) => {
          if (current) {
            tableRef.value.setPagination({
              current: current,
            });
          }
        }
      );
      //update-end-author:taoyan date:2022-5-31 for: VUEN-1156 popup 多数据有分页时，选中其他页，关闭popup 再点开，分页仍然选中上一次点击的分页，但数据是第一页的数据 未刷新

      function handleToggleSearch() {
        toggleSearchStatus.value = !unref(toggleSearchStatus);
      }
      /**
       * 取消/关闭
       */
      function handleCancel() {
        closeModal();
        checkedKeys.value = [];
        selectRows.value = [];
        // update-begin--author:liaozhiyang---date:20230908---for：【issues/742】选择后删除默认仍然存在
        tableRef.value.clearSelectedRowKeys();
        // update-end--author:liaozhiyang---date:20230908---for：【issues/742】选择后删除默认仍然存在
      }

      /**
       *确认提交
       */
      function handleSubmit() {
        filterUnuseSelect();
        if (!props.multi && unref(selectRows) && unref(selectRows).length > 1) {
          createMessage.warning('只能选择一条记录');
          return false;
        }
        if (!unref(selectRows) || unref(selectRows).length == 0) {
          createMessage.warning('至少选择一条记录');
          return false;
        }
        //update-begin-author:taoyan date:2022-5-31 for: VUEN-1155 popup 选择数据时，会选择多条重复数据
        let rows = getOkSelectRows!();
        emit('ok', rows);
        //update-end-author:taoyan date:2022-5-31 for: VUEN-1155 popup 选择数据时，会选择多条重复数据
        handleCancel();
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

      /**
       * 2024-07-24
       * liaozhiyang
       * 【TV360X-1756】报表添加图片类型
       * 图片
       * @param text
       */
      function getImgView(text) {
        if (text && text.indexOf(',') > 0) {
          text = text.substring(0, text.indexOf(','));
        }
        return getFileAccessHttpUrl(text);
      }
      /**
       * 2024-07-24
       * liaozhiyang
       * 【TV360X-1756】报表添加图片类型
       * 预览列表 cell 图片
       * @param text
       */
      function viewOnlineCellImage(e, text) {
        e.stopPropagation();
        if (text) {
          let imgList: any = [];
          let arr = text.split(',');
          for (let str of arr) {
            if (str) {
              imgList.push(getFileAccessHttpUrl(str));
            }
          }
          createImgPreview({ imageList: imgList });
        }
      }
      // update-begin--author:liaozhiyang---date:20250415--for：【issues/3656】popupdict回显
      watchEffect(() => {
        if (props.selected && props.rowkey) {
          const selected = props.multi ? props.selected : [props.selected];
          checkedKeys!.value = selected.map((item) => item[props.rowkey]);
          selectRows!.value = selected;
        }
      });
      // update-end--author:liaozhiyang---date:20250415--for：【issues/3656】popupdict回显
      return {
        attrs,
        register,
        tableScroll,
        dataSource,
        pagination,
        columns,
        rowSelection,
        checkedKeys,
        loading,
        title,
        handleCancel,
        handleSubmit,
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
        getImgView,
        viewOnlineCellImage,
      };
    },
  });
</script>

<style lang="less" scoped>
  .jeecg-basic-table-form-container {
    padding: 5px;

    .table-page-search-submitButtons {
      display: block;
      margin-bottom: 0;
      white-space: nowrap;
    }
  }
  :deep(.jeecg-basic-table .ant-table-wrapper .ant-table-title){
    min-height: 0;
  }
  .cellIamge {
    height: 25px !important;
    margin: 0 auto;
    max-width: 80px;
    font-size: 12px;
    font-style: italic;
    cursor: pointer;
  }
</style>
