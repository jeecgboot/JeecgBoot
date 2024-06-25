<!--角色选择框-->
<template>
  <div>
    <BasicModal v-bind="$attrs" @register="register" :title="modalTitle" width="800px" @ok="handleOk" destroyOnClose @visible-change="visibleChange">
      <BasicTable
        :columns="columns"
        v-bind="config"
        :useSearchForm="true"
        :formConfig="formConfig"
        :api="getRoleList"
        :searchInfo="searchInfo"
        :rowSelection="rowSelection"
        :indexColumnProps="indexColumnProps"
      ></BasicTable>
    </BasicModal>
  </div>
</template>
<script lang="ts">
  import { defineComponent, ref, unref } from 'vue';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { getRoleList } from '/@/api/common/api';
  import { createAsyncComponent } from '/@/utils/factory/createAsyncComponent';
  import { useSelectBiz } from '/@/components/Form/src/jeecg/hooks/useSelectBiz';
  import { selectProps } from '/@/components/Form/src/jeecg/props/props';
  import { useAttrs } from '/@/hooks/core/useAttrs';

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
        default: '角色选择',
      },
    },
    emits: ['register', 'getSelectResult'],
    setup(props, { emit, refs }) {
      //注册弹框
      const [register, { closeModal }] = useModalInner();
      const attrs = useAttrs();
      //表格配置
      const config = {
        canResize: false,
        bordered: true,
        size: 'small',
        rowKey: unref(props).rowKey,
      };
      const getBindValue = Object.assign({}, unref(props), unref(attrs), config);
      const [{ rowSelection, indexColumnProps, visibleChange, getSelectResult }] = useSelectBiz(getRoleList, getBindValue);
      const searchInfo = ref(props.params);
      //查询form
      const formConfig = {
        //labelWidth: 220,
        baseColProps: {
          xs: 24,
          sm: 24,
          md: 24,
          lg: 14,
          xl: 14,
          xxl: 14,
        },
        //update-begin-author:liusq date:2023-10-30 for: [issues/5514]组件页面显示错位
        actionColOptions: {
            xs: 24,
            sm: 8,
            md: 8,
            lg: 8,
            xl: 8,
            xxl: 8,
        },
        //update-end-author:liusq date:2023-10-30 for: [issues/5514]组件页面显示错位
        schemas: [
          {
            label: '角色名称',
            field: 'roleName',
            component: 'Input',
          },
        ],
      };
      //定义表格列
      const columns = [
        {
          title: '角色名称',
          dataIndex: 'roleName',
          width: 240,
          align: 'left',
        },
        {
          title: '角色编码',
          dataIndex: 'roleCode',
          // width: 40,
        },
      ];

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

      return {
        config,
        handleOk,
        searchInfo,
        register,
        indexColumnProps,
        visibleChange,
        getRoleList,
        formConfig,
        getBindValue,
        columns,
        rowSelection,
      };
    },
  });
</script>
