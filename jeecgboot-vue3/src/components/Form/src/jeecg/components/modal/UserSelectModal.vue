<!--用户选择框-->
<template>
  <div>
    <BasicModal
      v-bind="$attrs"
      @register="register"
      :title="modalTitle"
      :width="showSelected ? '1200px' : '900px'"
      :wrapClassName="modalWrapClassName"
      @ok="handleOk"
      @cancel="handleCancel"
      :maxHeight="maxHeight"
      :centered="true"
      destroyOnClose
      @visible-change="visibleChange"
      
    >
      <a-row>
        <a-col :span="showSelected ? 18 : 24">
          <BasicTable
            ref="tableRef"
            :columns="columns"
            :scroll="tableScroll"
            v-bind="getBindValue"
            :useSearchForm="true"
            :formConfig="formConfig"
            :api="hasCustomApi ? customListApi : getUserList"
            :searchInfo="searchInfo"
            :rowSelection="rowSelection"
            :indexColumnProps="indexColumnProps"
            :afterFetch="afterFetch"
            :beforeFetch="beforeFetch"
            :defSort="{ column: '', order: '' }"
          >
            <template #tableTitle></template>
          </BasicTable>
        </a-col>
        <a-col :span="showSelected ? 6 : 0">
          <BasicTable
            v-bind="selectedTable"
            :dataSource="selectRows"
            :useSearchForm="true"
            :formConfig="{ showActionButtonGroup: false, baseRowStyle: { minHeight: '40px' } }"
          >
            <!--操作栏-->
            <template #action="{ record }">
              <a href="javascript:void(0)" @click="handleDeleteSelected(record)"><Icon icon="ant-design:delete-outlined"></Icon></a>
            </template>
          </BasicTable>
        </a-col>
      </a-row>
    </BasicModal>
  </div>
</template>
<script lang="ts">
  import { defineComponent, unref, ref, watch, computed } from 'vue';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { getUserList } from '/@/api/common/api';
  import { createAsyncComponent } from '/@/utils/factory/createAsyncComponent';
  import { useSelectBiz } from '/@/components/Form/src/jeecg/hooks/useSelectBiz';
  import { useAttrs } from '/@/hooks/core/useAttrs';
  import { selectProps } from '/@/components/Form/src/jeecg/props/props';

  export default defineComponent({
    name: 'UserSelectModal',
    components: {
      //此处需要异步加载BasicTable
      BasicModal,
      BasicTable: createAsyncComponent(() => import('/@/components/Table/src/BasicTable.vue'), {
        loading: true,
      }),
    },
    props: {
      ...selectProps,
      //选择框标题
      modalTitle: {
        type: String,
        default: '选择用户',
      },
      //排除用户id的集合
      excludeUserIdList: {
        type: Array,
        default: [],
      },

      // wrap类名
      modalWrapClassName: {
        type: String,
        default: 'j-user-select-modal',
      },
      // 查询table数据使用的自定义接口
      customListApi: {type: Function},
      // 自定义接口的查询条件是否使用 JInput
      customApiJInput: {type: Boolean, default: true},
      // 自定义表单配置条件
      customFormConfig: {type: Object},
      // 自定义表格列
      customTableColumns: {type: Array},
    },
    emits: ['register', 'getSelectResult', 'close'],
    setup(props, { emit, refs }) {
      // 代码逻辑说明: VUEN-1086 【移动端】用户选择 查询按钮 效果不好 列表展示没有滚动条
      const tableScroll = ref<any>({ x: false });
      const tableRef = ref();
      const maxHeight = ref(600);

      const hasCustomApi = computed(() => typeof props.customListApi === 'function');

      //注册弹框
      const [register, { closeModal }] = useModalInner(() => {
        if (window.innerWidth < 900) {
          tableScroll.value = { x: 900 };
        } else {
          tableScroll.value = { x: false };
        }
        // 代码逻辑说明: VUEN-1112 一对多 用户选择 未显示选择条数，及清空
        setTimeout(() => {
          if (tableRef.value) {
            tableRef.value.setSelectedRowKeys(selectValues['value'] || []);
          }
        }, 800);
      });
      const attrs = useAttrs();
      //表格配置
      const config = {
        canResize: false,
        bordered: true,
        size: 'small',
      };
      const getBindValue = Object.assign({}, unref(props), unref(attrs), config);
      const [{ rowSelection, visibleChange, selectValues, indexColumnProps, getSelectResult, handleDeleteSelected, selectRows }] = useSelectBiz(
        getUserList,
        getBindValue,
        emit
      );
      const searchInfo = ref(props.params);
      // 代码逻辑说明: 【issues/657】右侧选中列表删除无效
      watch(rowSelection.selectedRowKeys, (newVal) => {
        // 代码逻辑说明: null指针异常导致控制台报错页面不显示------------
        if(tableRef.value){
          tableRef.value.setSelectedRowKeys(newVal);
        }
      });
      //查询form
      const formConfig = {
        baseColProps: {
          xs: 24,
          sm: 8,
          md: 6,
          lg: 8,
          xl: 6,
          xxl: 6,
        },
        // 代码逻辑说明: VUEN-1086 【移动端】用户选择 查询按钮 效果不好 列表展示没有滚动条---查询表单按钮的栅格布局和表单的保持一致
        actionColOptions: {
          xs: 24,
          sm: 8,
          md: 8,
          lg: 8,
          xl: 8,
          xxl: 8,
        },
        schemas: [
          {
            label: '账号',
            field: 'username',
            component: (hasCustomApi.value && !props.customApiJInput) ? 'Input' : 'JInput',
          },
          {
            label: '姓名',
            field: 'realname',
            component: (hasCustomApi.value && !props.customApiJInput) ? 'Input' : 'JInput',
          },
        ],
        autoSubmitOnEnter: true,

        ...props.customFormConfig,
      };
      //定义表格列
      const columns = props.customTableColumns?.length ? props.customTableColumns : [
        {
          title: '用户账号',
          dataIndex: 'username',
          width: 120,
          align: 'left',
        },
        {
          title: '用户姓名',
          dataIndex: 'realname',
          width: 120,
        },
        {
          title: '性别',
          dataIndex: 'sex_dictText',
          width: 50,
        },
        {
          title: '手机号码',
          dataIndex: 'phone',
          width: 120,
          customRender:( { record, text })=>{
            if(record.izHideContact && record.izHideContact === '1'){
              return '/';
            }
            return text;
          }
        },
        {
          title: '邮箱',
          dataIndex: 'email',
          customRender:( { record, text })=>{
            if(record.izHideContact && record.izHideContact === '1'){
              return text?'/':'';
            }
            return text;
          }
          // width: 40,
        },
        {
          title: '状态',
          dataIndex: 'status_dictText',
          width: 80,
        },
      ];
      //已选择的table信息
      const selectedTable = {
        pagination: false,
        showIndexColumn: false,
        scroll: { y: 390 },
        size: 'small',
        canResize: false,
        bordered: true,
        rowKey: 'id',
        columns: [
          {
            title: '用户姓名',
            dataIndex: 'realname',
            width: 40,
          },
          {
            title: '操作',
            dataIndex: 'action',
            align: 'center',
            width: 40,
            slots: { customRender: 'action' },
          },
        ],
      };
      /**
       * 确定选择
       */
      function handleOk() {
        getSelectResult((options, values) => {
          //回传选项和已选择的值
          emit('getSelectResult', options, values);
          //关闭弹窗
          closeModal();
        });
      }
      
      /**
       * 用户返回结果逻辑查询
       */
      function afterFetch(record) {
        let excludeList = props.excludeUserIdList;
        if(!excludeList){
          return record;
        }
        let arr:any[] = [];
        //如果存在过滤用户id集合，并且后台返回的数据不为空
        if(excludeList.length>0 && record && record.length>0){
          for(let item of record){
            if(excludeList.indexOf(item.id)<0){
              arr.push({...item})
            }
          }
          return arr;
        }
        return record;
      }
      // 代码逻辑说明: 【QQYUN-9366】用户选择组件取消和关闭会把选择数据带入
      const handleCancel = () => {
        emit('close');
      };

      // 代码逻辑说明: 【TV360X-305】小屏幕展示10条
      const clientHeight = document.documentElement.clientHeight * 200;
      maxHeight.value = clientHeight > 600 ? 600 : clientHeight;

      /**
       * 请求之前根据创建时间排序
       *
       * @param params
       */
      function beforeFetch(params) {
        return Object.assign({ column: 'createTime', order: 'desc' }, params);
      }

      return {
        //config,
        handleOk,
        searchInfo,
        register,
        indexColumnProps,
        visibleChange,
        getBindValue,
        getUserList,
        formConfig,
        columns,
        rowSelection,
        selectRows,
        selectedTable,
        handleDeleteSelected,
        tableScroll,
        tableRef,
        afterFetch,
        handleCancel,
        maxHeight,
        beforeFetch,
        hasCustomApi,
      };
    },
  });
</script>
