<template>
  <div>
    <BasicTable @register="registerTable" :rowSelection="rowSelection">
      <template #tableTitle>
        <a-button preIcon="ant-design:plus-outlined" type="primary" @click="handleAdd">新建</a-button>
<!--        <a-button type="primary" preIcon="ant-design:export-outlined" @click="onExportXls"> 导出</a-button>-->
<!--        <j-upload-button type="primary" preIcon="ant-design:import-outlined" @click="onImportXls">导入</j-upload-button>-->
        <a-dropdown v-if="selectedRowKeys.length > 0">
          <template #overlay>
            <a-menu>
              <a-menu-item key="1" @click="batchHandleDelete">
                <Icon icon="ant-design:delete-outlined"></Icon>
                删除
              </a-menu-item>
            </a-menu>
          </template>
          <a-button
            >批量操作
            <Icon style="fontsize: 12px" icon="ant-design:down-outlined"></Icon>
          </a-button>
        </a-dropdown>
      </template>
      <template #action="{ record }">
        <TableAction :actions="getActions(record)" :dropDownActions="getDropDownAction(record)" />
      </template>
    </BasicTable>
    <NoticeModal @register="registerModal" @success="reload" />
    <DetailModal @register="register" :frameSrc="iframeUrl" />
  </div>
</template>
<script lang="ts" name="system-notice" setup>
  import { ref, onMounted } from 'vue';
  import { BasicTable, TableAction } from '/@/components/Table';
  import { useModal } from '/@/components/Modal';
  import NoticeModal from './NoticeModal.vue';
  import DetailModal from './DetailModal.vue';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { useGlobSetting } from '/@/hooks/setting';
  import { getToken } from '/@/utils/auth';
  import { columns, searchFormSchema } from './notice.data';
  import { getList, deleteNotice, batchDeleteNotice,editIzTop, getExportUrl, getImportUrl, doReleaseData, doReovkeData } from './notice.api';
  import { useListPage } from '/@/hooks/system/useListPage';
  import { useAppStore } from '/@/store/modules/app';

  const appStore = useAppStore();
  const glob = useGlobSetting();
  const [registerModal, { openModal }] = useModal();
  const [register, { openModal: openDetail }] = useModal();
  const iframeUrl = ref('');
  const { createMessage, createConfirm } = useMessage();
  // 列表页面公共参数、方法
  const { prefixCls, onExportXls, onImportXls, tableContext, doRequest } = useListPage({
    designScope: 'notice-template',
    tableProps: {
      title: '消息通知',
      api: getList,
      columns: columns,
      formConfig: {
        schemas: searchFormSchema,
        fieldMapToTime: [['sendTime', ['sendTime_begin', 'sendTime_end'], 'YYYY-MM-DD']]
      }
    },
    exportConfig: {
      name: '消息通知列表',
      url: getExportUrl,
    },
    importConfig: {
      url: getImportUrl,
    },
  });

  const [registerTable, { reload }, { rowSelection, selectedRowKeys }] = tableContext;
  //流程编码
  const flowCode = 'dev_sys_announcement_001';
  /**
   * 新增事件
   */
  function handleAdd(record = {}) {
    openModal(true, {
      isUpdate: false,
      record,
    });
  }

  /**
   * 编辑事件
   */
  function handleEdit(record) {
    openModal(true, {
      record,
      isUpdate: true,
    });
  }

  /**
   * 删除事件
   */
  async function handleDelete(record) {
    await deleteNotice({ id: record.id }, reload);
  }
  /**
   * 置顶操作
   */
  async function handleTop(record, izTop) {
     await editIzTop({ id: record.id, izTop }, reload);
  }

  /**
   * 批量删除事件
   */
  async function batchHandleDelete() {
    doRequest(() => batchDeleteNotice({ ids: selectedRowKeys.value }));
  }
  /**
   * 发布
   */
  async function handleRelease(id) {
    await doReleaseData({ id });
    reload();
  }
  /**
   * 撤销
   */
  async function handleReovke(id) {
    await doReovkeData({ id });
    reload();
  }
  /**
   * 查看
   */
  function handleDetail(record) {
    iframeUrl.value = `${glob.uploadUrl}/sys/annountCement/show/${record.id}?token=${getToken()}`;
    openDetail(true, { record });
  }

  /**
   * 操作列定义
   * @param record
   */
  function getActions(record) {
    return [
      {
        label: '编辑',
        onClick: handleEdit.bind(null, record),
        ifShow: record.sendStatus == 0 || record.sendStatus == '2',
      },
      {
        label: '查看',
        onClick: handleDetail.bind(null, record),
        ifShow: record.sendStatus == 1,
      },
    ];
  }
  /**
   * 下拉操作栏
   */
  function getDropDownAction(record) {
    return [
      {
        label: '删除',
        ifShow: record.sendStatus != 1,
        popConfirm: {
          title: '是否确认删除',
          confirm: handleDelete.bind(null, record),
        },
      },
      {
        label: '发布',
        ifShow: (!record?.izApproval || record.izApproval == '0') && record.sendStatus == 0,
        onClick: handleRelease.bind(null, record.id),
      },
      {
        label: '撤销',
        ifShow: record.sendStatus == 1,
        popConfirm: {
          title: '确定要撤销吗？',
          confirm: handleReovke.bind(null, record.id),
        },
      },
      {
        label: '发布',
        ifShow: record.sendStatus == '2',
        popConfirm: {
          title: '确定要再次发布吗？',
          confirm: handleRelease.bind(null, record.id),
        },
      },
      {
        label: '置顶',
        onClick: handleTop.bind(null, record, 1),
        ifShow: record.sendStatus == 1 && record.izTop == 0,
      },
      {
        label: '取消置顶',
        onClick: handleTop.bind(null, record, 0),
        ifShow: record.sendStatus == 1 && record.izTop == 1,
      },
    ];
  }

  onMounted(() => {
    // 代码逻辑说明: 【JHHB-128】转公告
    const params = appStore.getMessageHrefParams;
    if (params?.add) {
      delete params.add;
      handleAdd(params);
      appStore.setMessageHrefParams('');
    }
  });
</script>
