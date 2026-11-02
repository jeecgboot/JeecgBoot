<template>
  <BasicModal @register="registerModal" title="内置按钮" :width="1200" @cancel="onCancel">

    <BasicTable @register="registerTable" :rowSelection="rowSelection">
      <template #action="{ record }">
        <TableAction :actions="getTableAction(record)"/>
      </template>
    </BasicTable>

    <template #footer>
      <a-button @click="onCancel">关闭</a-button>
    </template>

    <!-- 表单区域 -->
    <BasicModal v-bind="formModalProps">
      <a-spin :spinning="formModalProps.confirmLoading">
        <div style="margin-top: 20px;">
          <BasicForm @register="registerForm"/>
        </div>
      </a-spin>
    </BasicModal>
  </BasicModal>
</template>

<script setup lang="ts">
import {computed, nextTick, reactive, ref} from 'vue';
import {useListPage} from '/@/hooks/system/useListPage';

import {BasicTable, TableAction} from '/@/components/Table';
import {BasicModal, useModal, useModalInner} from '/@/components/Modal';
import {BasicForm, useForm} from '/@/components/Form';
import {builtInList, saveOrUpdate} from './button.api';
import {columns, formSchemas} from './button.data';

const props = defineProps({
  record: {
    type: Object,
    required: false,
    default: () => ({}),
  },
})
const emit = defineEmits(['register']);

const code = computed(() => props.record?.id);
// 1单表、2主表、3附表
const tableType = computed(() => props.record?.tableType);

const isSingleTable = computed(() => tableType.value === 1);

// 列表页面公共参数、方法
const {tableContext} = useListPage({
  tableProps: {
    api: async (params) => {
      const list = await builtInList(code.value, params)
      if (isSingleTable.value) {
        // 过滤包含 sub_ 的按钮，说明是附表按钮，单表时不显示
        return list.filter(item => !item.buttonCode.includes('sub_'));
      }
      return list;
    },
    columns: columns.filter(item => !['buttonStyle', 'optType', 'orderNum', 'exp'].includes(item.dataIndex as string)),
    canResize: false,
    useSearchForm: false,
    pagination: false,
  },
});
// 注册table数据
const [registerTable, {reload}, {rowSelection}] = tableContext;
// 注册弹窗
const [registerModal, {closeModal}] = useModalInner(() => reload());

// 注册 form 弹窗
const [registerFormModal, formModal] = useModal();
const isUpdate = ref(false);
const formModalProps = reactive({
  onRegister: registerFormModal,
  title: computed(() => (isUpdate?.value ? '修改' : '新增')),
  width: 600,
  centered: true,
  confirmLoading: false,
  onOk: onSubmit,
  onCancel: formModal.closeModal,
});
let formRecord = {};

const schemas = [
  ...formSchemas({redoModalHeight: formModal.redoModalHeight}),
].filter((schema) => {
  return ['buttonCode', 'buttonName', 'buttonIcon', 'buttonStatus'].includes(schema.field);
}).map(schema => {
  if ('buttonCode' === schema.field) {
    return {
      ...schema,
      dynamicRules: () => [],
      dynamicDisabled: () => true,
    }
  } else if ('buttonIcon' === schema.field) {
    return {
      ...schema,
      ifShow: () => true,
      componentProps: ({formModel}) => ({
        clearSelect: true,
        iconPrefixSave: false,
        iconPrefix: formModel.buttonCode === 'aigc_mock_data' ? 'mdi' : 'ant-design',
      }),
      // 以下按钮不允许设置图标
      dynamicDisabled: ({values}) => ['bpm', 'edit', 'detail', 'delete'].includes(values.buttonCode),
    }
  }
  return schema
});

// 注册 form
const [registerForm, {resetFields, setFieldsValue, validate}] = useForm({
  // update-begin--author:liaozhiyang---date:20240618---for：【TV360X-1306】自定义按钮弹窗按钮样式切换是重置弹窗高度
  schemas: schemas,
  // update-end--author:liaozhiyang---date:20240618---for：【TV360X-1306】自定义按钮弹窗按钮样式切换是重置弹窗高度
  showActionButtonGroup: false,
});

async function openFormModal(data: Recordable) {
  isUpdate.value = data.isUpdate;
  formRecord = {...(data.record ?? {})};
  formModal.openModal();
  await nextTick();
  await resetFields();
  setFieldsValue(formRecord);
}

// 编辑按钮
function onEdit(record: Recordable) {
  openFormModal({isUpdate: true, record});
}

function onCancel() {
  closeModal();
}

async function onSubmit() {
  try {
    formModalProps.confirmLoading = true;
    let values = await validate();
    values = Object.assign({cgformHeadId: code.value}, formRecord, values);

    console.log('onSubmit - values :', values)
    const isUpdate = values.id != null

    await saveOrUpdate(values, isUpdate);
    reload();
    formModal.closeModal();
  } finally {
    formModalProps.confirmLoading = false;
  }
}

/**
 * 操作栏
 */
function getTableAction(record: Recordable) {
  return [
    {
      label: '编辑',
      onClick: () => onEdit(record),
    },
  ];
}


</script>
<style lang="less" scoped></style>
