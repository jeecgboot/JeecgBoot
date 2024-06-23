<!--套餐中使用的用户选择框-->
<template>
  <div>
    <BasicModal
      v-bind="$attrs"
      @register="register"
      :title="modalTitle"
      width="900px"
      wrapClassName="j-user-select-modal"
      @ok="handleOk"
      destroyOnClose
    >
      <BasicTable ref="tableRef" @register="registerTable" :rowSelection="rowSelection">
        <template #tableTitle></template>
      </BasicTable>
    </BasicModal>
  </div>
</template>
<script lang="ts">
  import { defineComponent, ref } from 'vue';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { getTenantUserList } from '../tenant.api';
  import { createAsyncComponent } from '/@/utils/factory/createAsyncComponent';
  import { useListPage } from '/@/hooks/system/useListPage';
  import { userColumns, userSearchFormSchema } from '../tenant.data';

  export default defineComponent({
    name: 'TenantUserSelectModal',
    components: {
      //此处需要异步加载BasicTable
      BasicModal,
      BasicTable: createAsyncComponent(() => import('/@/components/Table/src/BasicTable.vue'), {
        loading: true,
      }),
    },
    props: {
      //选择框标题
      modalTitle: {
        type: String,
        default: '选择用户',
      },
      //查询参数
      tenantId: {
        type: Number,
        default: 0,
      },
      //排除用户id的集合
      excludeUserIdList: {
        type: Array,
        default: [],
      },
    },
    emits: ['register', 'on-select'],
    setup(props, { emit, refs }) {
      const tableScroll = ref<any>({ x: false });
      const tableRef = ref();
      //注册弹框
      const [register, { closeModal }] = useModalInner(() => {
        if (window.innerWidth < 900) {
          tableScroll.value = { x: 900 };
        } else {
          tableScroll.value = { x: false };
        }
        setTimeout(() => {
          if (tableRef.value) {
            tableRef.value.setSelectedRowKeys([]);
          }
        }, 800);
      });

      //定义表格列
      const columns = [
        {
          title: '账号',
          dataIndex: 'username',
          width: 40,
          align: 'left',
        },
        {
          title: '姓名',
          dataIndex: 'realname',
          width: 40,
        },
        {
          title: '性别',
          dataIndex: 'sex_dictText',
          width: 20,
        },
        {
          title: '手机号码',
          dataIndex: 'phone',
          width: 30,
        },
        {
          title: '邮箱',
          dataIndex: 'email',
          width: 40,
        },
        {
          title: '状态',
          dataIndex: 'status_dictText',
          width: 20,
        },
      ];

      // 列表页面公共参数、方法
      const { prefixCls, tableContext } = useListPage({
        designScope: 'tenant-template',
        tableProps: {
          api: getTenantUserList,
          columns: userColumns,
          scroll: { y: 390 },
          rowKey: 'id',
          showActionColumn: false,
          formConfig: {
            schemas: userSearchFormSchema,
            labelWidth: 60,
            actionColOptions: {
              xs: 24,
              sm: 8,
              md: 8,
              lg: 8,
              xl: 8,
              xxl: 8,
            },
          },
          beforeFetch: (params) => {
            return Object.assign(params, { userTenantId: props.tenantId });
          },
        },
      });
      const [registerTable, { reload }, { rowSelection, selectedRowKeys }] = tableContext;

      /**
       * 确定选择
       */
      function handleOk() {
        //返回选中事件
        emit('on-select', rowSelection.selectedRows, rowSelection.selectedRowKeys);
      }

      return {
        handleOk,
        register,
        columns,
        rowSelection,
        tableScroll,
        tableRef,
        registerTable,
      };
    },
  });
</script>

<style scoped>
  :deep(.ant-input-suffix) {
    display: none;
  }
</style>
