<template>
  <BasicModal @register="registerModal" title="自定义按钮" :width="1200" defaultFullscreen @cancel="onCancel">
    <template #footer>
      <a-button @click="onCancel">关闭</a-button>
      <div v-if="aiTestMode" style="float: left">
        <a-button :loading="generating" @click="onGenButtons">生成测试数据</a-button>
      </div>
    </template>

    <BasicTable @register="registerTable" :rowSelection="rowSelection">
      <template #tableTitle>
        <a-button @click="onAdd" type="primary" preIcon="ant-design:plus">新增</a-button>
        <a-button @click="onOpenBIButton" preIcon="ant-design:setting">管理内置按钮</a-button>

        <a-dropdown v-if="selectedRowKeys.length > 0">
          <template #overlay>
            <a-menu>
              <a-menu-item key="1" @click="onBatchDelete">
                <a-icon type="delete" />
                删除
              </a-menu-item>
            </a-menu>
          </template>
          <a-button style="margin-left: 8px">
            批量操作
            <a-icon type="down" />
          </a-button>
        </a-dropdown>
      </template>

      <!--操作栏-->
      <template #action="{ record }">
        <TableAction :actions="getTableAction(record)" :dropDownActions="getDropDownAction(record)" />
      </template>
    </BasicTable>

    <!-- 表单区域 -->
    <BasicModal v-bind="formModalProps">
      <a-spin :spinning="formModalProps.confirmLoading">
        <BasicForm @register="registerForm" />
      </a-spin>
    </BasicModal>
  </BasicModal>

  <!-- 管理内置按钮 -->
  <BuiltInButtonList @register="registerBIButtonModal" :record="record"/>

</template>

<script lang="ts">
  import { ref, reactive, computed, nextTick, defineComponent } from 'vue';

  import { useOnlineTest } from '../../hooks/aitest/useOnlineTest';
  import { useListPage } from '/@/hooks/system/useListPage';

  import { ActionItem, BasicTable, TableAction } from '/@/components/Table';
  import { BasicModal, useModal, useModalInner } from '/@/components/Modal';
  import { BasicForm, useForm } from '/@/components/Form';
  import { list, doBatchDelete, saveOrUpdate } from './button.api';
  import { columns, formSchemas } from './button.data';
  import BuiltInButtonList from './BuiltInButtonList.vue';
  import {useMessage} from "@/hooks/web/useMessage";

  export default defineComponent({
    name: 'CustomButtonList',
    components: { BasicModal, BasicTable, TableAction, BasicForm, BuiltInButtonList },
    emits: ['register'],
    setup() {
      const {createMessage: $message} = useMessage();

      const code = ref('');
      const record = ref()
      // 列表页面公共参数、方法
      const { doRequest, doDeleteRecord, tableContext } = useListPage({
        tableProps: {
          api: (params) => list(code.value, params),
          columns,
          canResize: false,
          useSearchForm: false,
          beforeFetch(params) {
            return Object.assign(params, { column: 'orderNum', order: 'asc' });
          },
        },
      });
      // 注册table数据
      const [registerTable, { reload, getDataSource }, { rowSelection, selectedRowKeys }] = tableContext;
      // 注册弹窗
      const [registerModal, { closeModal }] = useModalInner(async (data) => {
        code.value = data.row.id;
        record.value = data.row
        reload();
      });
      // useOnlineAiTest
      const { aiTestMode, genButtons } = useOnlineTest({}, { reload }, null);
      const generating = ref(false);
      // 注册 form 弹窗
      const [registerFormModal, formModal] = useModal();
      const isUpdate = ref(false);
      const formModalProps = reactive({
        onRegister: registerFormModal,
        title: computed(() => (isUpdate?.value ? '修改' : '新增')),
        width: 800,
        confirmLoading: false,
        onOk: onSubmit,
        onCancel: formModal.closeModal,
      });
      let formRecord = {};
      // 注册 form
      const [registerForm, { resetFields, setFieldsValue, validate }] = useForm({
        // update-begin--author:liaozhiyang---date:20240618---for：【TV360X-1306】自定义按钮弹窗按钮样式切换是重置弹窗高度
        schemas: formSchemas({ redoModalHeight: formModal.redoModalHeight }),
        // update-end--author:liaozhiyang---date:20240618---for：【TV360X-1306】自定义按钮弹窗按钮样式切换是重置弹窗高度
        showActionButtonGroup: false,
      });

      async function openFormModal(data) {
        isUpdate.value = data.isUpdate;
        formRecord = { ...(data.record ?? {}) };
        formModal.openModal();
        await nextTick();
        await resetFields();
        setFieldsValue(formRecord);
      }

      // 新增按钮
      function onAdd() {
        openFormModal({ isUpdate: false });
      }

      // 编辑按钮
      function onEdit(record) {
        openFormModal({ isUpdate: true, record });
      }

      function onCancel() {
        closeModal();
      }

      // update-begin--author:liaozhiyang---date:20260824---for：【LHZP-195】过滤已添加的自定义按钮并防止重复提交
      async function onGenButtons() {
        if (generating.value) return;
        generating.value = true;
        try {
          const existingButtonCodes = (getDataSource?.() || []).map((item) => item.buttonCode).filter(Boolean);
          await genButtons(code.value, existingButtonCodes);
        } finally {
          generating.value = false;
        }
      }
      // update-end--author:liaozhiyang---date:20260824---for：【LHZP-195】过滤已添加的自定义按钮并防止重复提交

      // 批量删除事件
      async function onBatchDelete() {
        doRequest(() => doBatchDelete(selectedRowKeys.value));
      }

      async function onSubmit() {
        try {
          formModalProps.confirmLoading = true;
          let values = await validate();
          values = Object.assign({ cgformHeadId: code.value }, formRecord, values);
          await saveOrUpdate(values, isUpdate.value);
          reload();
          formModal.closeModal();
        } finally {
          formModalProps.confirmLoading = false;
        }
      }

      // -------------------- [Begin] 内置按钮弹窗 ------------------

      const [registerBIButtonModal, bIButtonModal] = useModal()

      /**
       * 打开管理内置按钮弹窗
       */
      function onOpenBIButton() {
        if (record.value?.tableType == 3) {
          $message.warn('附表不支持管理内置按钮，请选择对应主表')
          return
        }
        bIButtonModal.openModal(true, {});
      }

      // ---------------------- [End] 内置按钮弹窗 ------------------

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
        code,
        record,
        onAdd,
        onEdit,
        onBatchDelete,
        aiTestMode,
        generating,
        onGenButtons,
        registerModal,
        registerTable,
        selectedRowKeys,
        rowSelection,
        onCancel,
        getTableAction,
        getDropDownAction,
        registerForm,
        formModalProps,
        registerBIButtonModal,
        onOpenBIButton,
      };
    },
  });
</script>
<style lang="less" scoped></style>
