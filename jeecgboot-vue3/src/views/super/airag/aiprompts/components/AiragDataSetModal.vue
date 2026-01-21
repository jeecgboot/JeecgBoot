<template>
  <BasicModal v-bind="$attrs" :zIndex="999" @register="registerModal" :canFullscreen="true" :footer="null" defaultFullscreen destroyOnClose title="评测集">
    <div class="modal-content">
      <div class="top-section">
        <div class="header-actions">
          <a-button type="primary" v-if="columns && columns.length > 0" @click="handleAddData">
            <Icon icon="ant-design:plus-outlined" />
            新增数据
          </a-button>
          <a-button @click="columnConfig" class="config-btn">
            <Icon icon="ant-design:setting-outlined" />
            列配置
          </a-button>
        </div>
      </div>
      <div class="table-container">
        <a-table
            :columns="columns"
            :dataSource="dataSource"
            :pagination="pagination"
            rowKey="id"
            class="data-table"
            :scroll="{ x: 'max-content' }"
        >
          <template #action="{ record }">
            <div class="action-buttons">
              <a-button size="small" @click="handleEditDataset(record)" class="action-btn edit-btn">
                <Icon icon="ant-design:edit-outlined" />
              </a-button>
              <a-button size="small" @click="handleDelete(record)" class="action-btn delete-btn">
                <Icon icon="ant-design:delete-outlined" />
              </a-button>
            </div>
          </template>
        </a-table>
      </div>
    </div>
  </BasicModal>
  <!--自定义列配置  -->
  <AiragDataSetColumnModal @register="registerDataSetColumnModal" @success="reload" />
  <!--自定义数据配置  -->
  <AiragDataSetDataDrawer @register="registerDataSetDataDrawer" @success="reload" />
</template>

<script lang="ts" setup>
import { ref } from 'vue';
import { BasicModal, useModal, useModalInner } from '/@/components/Modal';
import AiragDataSetColumnModal from './AiragDataSetColumnModal.vue';
import AiragDataSetDataDrawer from './AiragDataSetDataDrawer.vue';
import {queryById, saveOrUpdate} from '../AiragExtData.api';
import { useDrawer } from '@/components/Drawer';
import { cloneDeep } from 'lodash-es';
import {useMessage} from "@/hooks/web/useMessage";
// Emits声明
const emit = defineEmits(['register', 'success']);
const { createMessage } = useMessage();
const dataId = ref<string>('');
const columns = ref<any>([]);
const dataSource = ref<any>([]);
const datasetValue = ref<any>({});
const pagination = ref({
  total: 0,
  current: 1,
  pageSize: 10,
});
//表单赋值
const [registerModal] = useModalInner(async (data) => {
  dataId.value = data.record?.id;
  initData(data.record)
});
//注册列配置
const [registerDataSetColumnModal, { openModal: openColumnModal }] = useModal();
//注册数据抽屉
const [registerDataSetDataDrawer, { openDrawer }] = useDrawer();

/**
 * 刷新数据
 */
function reload() {
  queryById({ id: dataId.value }).then((res) => {
    if (res.success && res.result) {
      initData(res.result)
    }
  });
  emit('success');
}
/**
 * 初始化数据
 */
function initData(result) {
  dataSource.value = []
  columns.value = []
  datasetValue.value = result?.datasetValue ? JSON.parse(result?.datasetValue) : {};
  if (datasetValue.value?.columns) {
    datasetValue.value?.columns.forEach((item) => {
      columns.value.push({
        title: item.name,
        dataIndex: item.name,
        key: item.name,
        fixed: 'center',
      });
    });
  }
  dataSource.value = datasetValue.value?.dataSource || [];
  //是否已经包含操作列
  const hasAction = columns.value.some(item=>item.key == 'action');
  if(!hasAction){
    columns.value.push({
      title: "操作",
      dataIndex: 'action',
      key: 'action',
      fixed: 'right',
      width: 120,
      slots: { customRender: 'action' },
    });
  }
}
/**
 * 打开列配置
 */
function columnConfig() {
  openColumnModal(true, {
    id: dataId.value,
    datasetValue: cloneDeep(datasetValue.value),
  });
}

/**
 * 新增数据点击事件
 */
function handleAddData() {
  if(!datasetValue.value?.columns || datasetValue.value?.columns.length == 0){
    createMessage.warning('请先配置列信息');
    return;
  }
  openDrawer(true, {
    id: dataId.value,
    dataSource:cloneDeep(datasetValue.value.dataSource),
    columns:cloneDeep(datasetValue.value.columns),
    isUpdate:false
  });
}

/**
 * 编辑数据点击事件
 * @param record
 */
function handleEditDataset(record) {
  openDrawer(true, {
    id: dataId.value,
    dataSource: cloneDeep(dataSource.value),
    columns: cloneDeep(datasetValue.value?.columns),
    record: cloneDeep(record),
    isUpdate:true
  });
}

/**
 * 删除数据点击事件
 */
function handleDelete(record) {
  //删除数据
  dataSource.value = dataSource.value.filter((item) => item.id !== record.id);
  refreshDataset()
}

/**
 *  表单提交事件
 */
async function refreshDataset() {
  //提交表单
  const submitData = {
    datasetValue: JSON.stringify({
      columns: datasetValue.value.columns,
      dataSource: dataSource.value,
    }),
    id: dataId.value,
  };

  console.log('提交数据:', submitData);
  await saveOrUpdate(submitData, true, false);
  reload()
}
</script>

<style lang="less" scoped>
.modal-content {
  display: flex;
  flex-direction: column;
  height: 100%;
}

.top-section {
  padding: 16px 0;
  border-bottom: 1px solid #f0f0f0;
  margin-bottom: 16px;
}

.header-actions {
  display: flex;
  gap: 12px;
  align-items: center;
}

.config-btn {
  color: #1890ff;
  border-color: #1890ff;
}

.table-container {
  flex: 1;
  overflow: auto;
}

.data-table {
  :deep(.ant-table-thead > tr > th) {
    background-color: #fafafa;
    font-weight: 600;
  }

  :deep(.ant-table-tbody > tr:hover) {
    background-color: #f5f5f5;
  }

  :deep(.ant-table-pagination) {
    margin: 16px 0;
  }
}

.action-buttons {
  display: flex;
  gap: 8px;
}

.action-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 4px 8px;
}

.edit-btn {
  color: #1890ff;
  border-color: #1890ff;

  &:hover {
    color: #40a9ff;
    border-color: #40a9ff;
  }
}

.delete-btn {
  color: #ff4d4f;
  border-color: #ff4d4f;

  &:hover {
    color: #ff7875;
    border-color: #ff7875;
  }
}

/** 时间和数字输入框样式 */
:deep(.ant-input-number) {
  width: 100%;
}

:deep(.ant-calendar-picker) {
  width: 100%;
}
</style>
