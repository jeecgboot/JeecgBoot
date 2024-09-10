<template>
  <BasicModal v-bind="$attrs" @register="registerModal" title="用户选择列表" width="1000px" @ok="handleSubmit" destroyOnClose @openChange="handleOpenChange">
    <BasicTable @register="registerTable" :rowSelection="rowSelection" />
  </BasicModal>
</template>
<script lang="ts" setup>
  import { ref, unref, toRaw } from 'vue';
  import { BasicModal, useModalInner } from '/src/components/Modal';
  import { BasicTable, useTable, TableAction } from '/src/components/Table';
  import { userColumns, searchUserFormSchema } from '../role.data';
  import { list } from '../../user/user.api';
  // 声明Emits
  const emit = defineEmits(['select', 'register']);
  const checkedKeys = ref<Array<string | number>>([]);
  const [registerModal, { setModalProps, closeModal }] = useModalInner();
  //注册table数据
  const [registerTable, { reload }] = useTable({
    api: list,
    rowKey: 'id',
    columns: userColumns,
    formConfig: {
      labelWidth: 60,
      schemas: searchUserFormSchema,
      baseRowStyle: { maxHeight: '20px' },
      autoSubmitOnEnter: true,
    },
    striped: true,
    useSearchForm: true,
    showTableSetting: false,
    bordered: true,
    showIndexColumn: false,
    canResize: false,
  });
  /**
   * 选择列配置
   */
  const rowSelection = {
    type: 'checkbox',
    columnWidth: 50,
    selectedRowKeys: checkedKeys,
    onChange: onSelectChange,
  };
  /**
   * 选择事件
   */
  function onSelectChange(selectedRowKeys: (string | number)[]) {
    checkedKeys.value = selectedRowKeys;
  }

  const handleOpenChange = (visible) => {
    // -update-begin--author:liaozhiyang---date:20240702---for：【TV360X-1679】系统角色-角色用户再次打开弹窗重置之前选中的状态
    if (visible) {
      checkedKeys.value = [];
    }
    // -update-end--author:liaozhiyang---date:20240702---for：【TV360X-1679】系统角色-角色用户再次打开弹窗重置之前选中的状态
  };

  //提交事件
  function handleSubmit() {
    setModalProps({ confirmLoading: true });
    //关闭弹窗
    closeModal();
    //刷新列表
    emit('select', toRaw(unref(checkedKeys)));
    setModalProps({ confirmLoading: false });
  }
</script>
