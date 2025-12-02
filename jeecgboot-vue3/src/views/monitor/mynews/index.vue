<template>
  <div>
    <BasicTable @register="registerTable" :searchInfo="searchInfo" :rowSelection="rowSelection">
      <template #tableTitle>
        <a-button type="primary" @click="handlerReadAllMsg">全部标注已读</a-button>
        <a-dropdown v-if="selectedRowKeys.length > 0">
          <template #overlay>
            <a-menu>
              <a-menu-item key="1" @click="batchHandleDelete">
                <Icon icon="ant-design:delete-outlined"></Icon>
                删除
              </a-menu-item>
            </a-menu>
          </template>
          <a-button>
            批量操作
            <Icon icon="mdi:chevron-down"></Icon>
          </a-button>
        </a-dropdown>
      </template>
      <template #action="{ record }">
        <TableAction :actions="getActions(record)" />
      </template>
    </BasicTable>
    <DetailModal @register="register" />
    <keep-alive>
      <component v-if="currentModal" v-bind="bindParams" :key="currentModal" :is="currentModal" @register="modalRegCache[currentModal].register" />
    </keep-alive>
  </div>
</template>
<script lang="ts" name="monitor-mynews" setup>
import {ref, onMounted, unref} from 'vue';
  import { BasicTable, useTable, TableAction } from '/@/components/Table';
  import DetailModal from './DetailModal.vue';
  import { getMyNewsList, editCementSend, syncNotic, readAllMsg, getOne, deleteAnnSend, deleteBatchAnnSend } from './mynews.api';
  import { columns, searchFormSchema } from './mynews.data';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { getToken } from '/@/utils/auth';
  import { useModal } from '/@/components/Modal';
  import { useGlobSetting } from '/@/hooks/setting';
  const glob = useGlobSetting();
  const { createMessage } = useMessage();
  const checkedKeys = ref<Array<string | number>>([]);
  const content = ref({});
  const searchInfo = { logType: '1' };
  const [register, { openModal: openDetail }] = useModal();
  import { useListPage } from '/@/hooks/system/useListPage';
  import { getLogList } from '/@/views/monitor/log/log.api';
  import { useRouter } from 'vue-router';
  import { useAppStore } from '/@/store/modules/app';
  import { useMessageHref } from '/@/views/system/message/components/useSysMessage';
  const appStore = useAppStore();
  const router = useRouter();
  const { currentRoute } = useRouter();
  const { goPage, currentModal, modalRegCache, bindParams } = useMessageHref();
  // 代码逻辑说明: 【QQYUN-13058】我的消息区分类型且支持根据url参数查询类型
  const querystring = currentRoute.value.query;
  const findItem: any = searchFormSchema.find((item: any) => item.field === 'msgCategory');
  if (findItem) {
    if (querystring?.msgCategory) {
      findItem.componentProps.defaultValue = querystring.msgCategory
    } else if (querystring.noticeType) {
      findItem.componentProps.defaultValue = querystring.noticeType;
    } else {
      findItem.componentProps.defaultValue = null
    }
  }
  const { prefixCls, tableContext } = useListPage({
    designScope: 'mynews-list',
    tableProps: {
      title: '我的消息',
      api: getMyNewsList,
      columns: columns,
      formConfig: {
        schemas: searchFormSchema,
        // 代码逻辑说明: 【TV360X-545】我的消息列表不能通过时间范围查询---
        fieldMapToTime: [['sendTime', ['sendTimeBegin', 'sendTimeEnd'], 'YYYY-MM-DD']],
      },
      beforeFetch: (params) => {
        // 代码逻辑说明: 【QQYUN-13058】我的消息区分类型且支持根据url参数查询类型
        if (params.msgCategory) {
          if (['1', '2'].includes(params.msgCategory)) {
            params.msgCategory = params.msgCategory;
          } else {
            params.noticeType = params.msgCategory;
            delete params.msgCategory;
          }
        } else {
          if (querystring?.msgCategory) {
            params.msgCategory = querystring.msgCategory;
          } else if (querystring.noticeType) {
            params.noticeType = querystring.noticeType;
          }
        }
        return params;
      },
    },
  });
  const [registerTable, { reload }, { rowSelection, selectedRows, selectedRowKeys }] = tableContext;
  /**
   * 操作列定义
   * @param record
   */
  function getActions(record) {
    return [
      {
        label: '查看',
        onClick: handleDetail.bind(null, record),
      },
      {
        label: '删除',
        popConfirm: {
          title: '是否确认删除',
          confirm: handleDelete.bind(null, record.id),
        },
        ifShow: record.readFlag === 1
      }
    ];
  }

  /**
   * 查看
   */
  function handleDetail(record) {
    let anntId = record.anntId;
    editCementSend({ anntId: anntId }).then((res) => {
      reload();
      syncNotic({ anntId: anntId });
    });
    const openModalFun = ()=>{
      openDetail(true, {
        record,
        isUpdate: true,
      });
    }
    goPage(record, openModalFun);

  }
  // 日志类型
  function callback(key) {
    searchInfo.logType = key;
    reload();
  }

  //全部标记已读
  function handlerReadAllMsg() {
    readAllMsg({}, reload);
  }

  /**
   * 选择事件
   */
  function onSelectChange(selectedRowKeys: (string | number)[]) {
    checkedKeys.value = selectedRowKeys;
  }

  // 代码逻辑说明: 消息跳转，打开详情表单
  onMounted(()=>{
    initHrefModal();
  });
  function initHrefModal(){
    let params = appStore.getMessageHrefParams;
    if(params){
      let anntId = params.id;
      if(anntId){
        editCementSend({ anntId: anntId }).then(() => {
          reload();
          syncNotic({ anntId: anntId });
        });
      }
      let detailId = params.detailId;
      if(detailId){
        getOne(detailId).then(data=>{
          console.log('getOne', data)
          openDetail(true, {
            record: data,
            isUpdate: true,
          });
          appStore.setMessageHrefParams('')
        })
      }
    }
  }

  function handleSuccess() {
    selectedRowKeys.value = [];
    reload();
  }
  
  /**
   * 删除我的消息
   * 
   * @param id
   */
  async function handleDelete(id) {
    await deleteAnnSend({ id: id }, handleSuccess);
  }

  /**
   * 批量删除我的消息
   */
  async function batchHandleDelete() {
    let unRead = unref(selectedRows).filter((item) => item.readFlag == 0);
    if (unref(unRead).length > 0) {
      createMessage.warning('未阅读的消息禁止删除！');
      return;
    }
    await deleteBatchAnnSend({ ids: selectedRowKeys.value }, handleSuccess);
  }
</script>
