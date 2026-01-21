<template>
  <BasicDrawer v-bind="getProps">
    <BasicTable @register="registerTable">
      <!--操作栏-->
      <template #action="{ record }">
        <TableAction :actions="getTableAction(record)" /> </template
    ></BasicTable>
  </BasicDrawer>
  <AiragTrackDetailModal @register="registerTrackDetailModal" />
</template>

<script lang="ts" setup>
  import type { DrawerProps } from '/@/components/Drawer';
  import { BasicDrawer, useDrawerInner } from '/@/components/Drawer';
  import { computed, unref, ref } from 'vue';
  import { useAttrs } from '@/hooks/core/useAttrs';
  import { BasicTable, TableAction } from '@/components/Table';
  import { useModal } from '@/components/Modal';
  import { getTrackList, deleteOne } from '@/views/super/airag/aiprompts/AiragExtData.api';
  import {useListPage} from "@/hooks/system/useListPage";
  import AiragTrackDetailModal from './AiragTrackDetailModal.vue';
  // Emits声明
  const attrs = useAttrs();
  const [registerTrackDetailModal, { openModal }] = useModal();
  //列配置
  const trackColumns = ref([
    { title: '问题', dataIndex: 'userQuery', key: 'userQuery' },
    { title: '提示词答案', dataIndex: 'promptAnswer', key: 'promptAnswer' },
    { title: '评估分数', dataIndex: 'answerScore', key: 'answerScore' },
    { title: '版本号', dataIndex: 'version', key: 'version' },
  ]);
  // 注册抽屉
  const [registerDrawer, { closeDrawer }] = useDrawerInner(open);
  // 表格定义
  const { tableContext } = useListPage({
    designScope: 'agent-config',
    tableProps: {
      title: '调用记录',
      api: getTrackList,
      columns: trackColumns.value,
      canResize: false,
      rowSelection: {
        columnWidth: 20,
      },
      immediate: false,
      beforeFetch: async (params) => {
        return Object.assign(params, { metadata: dataId.value });
      },
      afterFetch: async (res) => {
        if (res.length > 0) {
          res.forEach((item) => {
            if (item.dataValue) {
              let dataValue = JSON.parse(item.dataValue);
              item.answerScore = dataValue.answerScore;
              item.userQuery = dataValue.userQuery;
              item.promptAnswer = dataValue.promptAnswer;
            }
          });
        }
        return res;
      },
    },
  });
  const [registerTable, { reload }] = tableContext;
  // 抽屉最终props
  const getProps = computed(() => {
    let drawerProps: Partial<DrawerProps> = {
      width: 1000,
      title: '调用记录',
      destroyOnClose: true,
    };
    let finalProps: Recordable = {
      ...unref(attrs),
      ...drawerProps,
      onCancel: closeDrawer,
      onRegister: registerDrawer,
    };
    return finalProps;
  });
  /** dataId */
  const dataId = ref<any>('');
  /** 抽屉开启 */
  function open(data) {
    dataId.value = data.record?.id || '';
    reload();
  }
  /**
   * 删除
   */
  function handleDelete(record) {
    console.log(record, 'record');
    deleteOne({ id: record.id},reload)
  }

  /**
   * 详情
   * @param record
   */
  function handleDetail(record) {
    openModal(true, { dataValue: record.dataValue });
  }
  /*
   * 操作栏
   */
  function getTableAction(record) {
    return [
      {
        label: '详情',
        onClick: handleDetail.bind(null, record),
      },
      {
        label: '删除',
        popConfirm: {
          title: '是否确认删除',
          confirm: handleDelete.bind(null, record),
          placement: 'topLeft',
        },
      },
    ];
  }
</script>

<style scoped lang="less">
  .dynamic-column-config {
    max-width: 1200px;
    margin: 0 auto;
    max-height: calc(100vh - 100px); /* 设置最大高度为视口高度的70% */
    overflow-y: auto; /* 超出时显示垂直滚动条 */

    :deep(.ant-card-head) {
      border-bottom: 1px solid #f0f0f0;
    }

    :deep(.ant-card-body) {
      padding: 12px;
    }

    :deep(.ant-card-extra) {
      padding: 0;
    }
  }

  .column-list {
    margin-bottom: 24px;
  }

  .column-item {
    position: relative; /* 为绝对定位的操作区域提供参考 */
    display: flex;
    align-items: flex-start;
    gap: 12px;
    padding: 16px;
    margin-bottom: 16px;
    background: #fafafa;
    border: 1px solid #f0f0f0;
    border-radius: 6px;
    transition: all 0.3s;

    &:hover {
      border-color: #1890ff;
      background: #f6fbff;
    }

    /* 操作区域样式 */
    .action-area {
      position: absolute;
      top: 8px;
      right: 8px;

      :deep(.ant-btn) {
        width: 24px;
        height: 24px;
        min-width: 24px;
        padding: 0;

        .anticon {
          font-size: 14px;
        }
      }
    }
  }

  .column-form {
    flex: 1;
    width: 100%; /* 确保表单占满宽度 */
  }

  .form-row {
    display: flex;
    flex-direction: column;
    gap: 16px;
    margin-bottom: 16px;

    &:last-child {
      margin-bottom: 0;
    }
  }

  .form-item {
    flex: 1;

    &.full-width {
      flex: 3;
    }
  }

  .form-label {
    display: block;
    margin-bottom: 6px;
    font-weight: 500;
    color: rgba(0, 0, 0, 0.85);

    &::after {
      content: '：';
    }
  }

  .error-text {
    color: #ff4d4f;
    font-size: 12px;
    margin-top: 4px;
  }

  .has-error {
    :deep(.ant-input) {
      border-color: #ff4d4f;
    }
  }

  .form-actions {
    display: none; /* 隐藏原来的删除按钮区域 */
  }

  .fixed-columns {
    margin-top: 24px;
    padding-top: 24px;
    border-top: 1px dashed #d9d9d9;

    .tip-text {
      margin-bottom: 16px;
      color: #666;
      font-size: 14px;
    }
  }

  .fixed-column-item {
    margin-bottom: 16px;
    padding: 16px;
    background: #f5f5f5;
    border: 1px solid #e8e8e8;
    border-radius: 6px;

    &:last-child {
      margin-bottom: 0;
    }

    :deep(.ant-input-group-addon) {
      background: #e6f7ff;
      color: #1890ff;
      font-weight: 500;
    }
  }

  .footer-actions {
    display: flex;
    justify-content: center;
    margin-top: 32px;
    padding-top: 8px;
    border-top: 1px solid #f0f0f0;
    position: sticky;
    bottom: 0;
    background: #fff;
    padding-bottom: 8px;
  }
</style>
