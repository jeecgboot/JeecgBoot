<template>
  <div class="p-2">
    <BasicModal destroyOnClose @register="registerModal" :canFullscreen="false" width="600px" :title="title" @ok="handleOk" @cancel="handleCancel">
      <div class="flex header">
        <a-input
          @pressEnter="loadFlowData"
          class="header-search"
          size="small"
          v-model:value="searchText"
          placeholder="请输入流程名称，回车搜索"
        ></a-input>
      </div>
      <a-row :span="24">
        <a-col :span="12" v-for="item in flowList" @click="handleSelect(item)">
          <a-card :style="item.id === flowId ? { border: '1px solid #3370ff' } : {}" hoverable class="checkbox-card" :body-style="{ width: '100%' }">
            <div style="display: flex; width: 100%;align-items:center">
              <img :src="getImage(item.icon)" class="flow-icon"/>
              <div style="display: grid;margin-left: 5px;align-items: center">
                <span class="checkbox-name ellipsis">{{ item.name }}</span>
                <div class="flex text-status" v-if="item.metadata && item.metadata.length>0">
                  <span class="tag-input">输入</span>
                  <div v-for="(metaItem, index) in item.metadata">
                    <a-tag color="rgba(87,104,161,0.08)" class="tags-meadata">
                      <span v-if="index<3" class="tag-text">{{ metaItem.field }}</span>
                    </a-tag>
                  </div>
                </div>
              </div>
            </div>
            <div class="text-desc mt-10">
              {{ item.descr || '暂无描述' }}
            </div>
          </a-card>
        </a-col>
      </a-row>
      <div v-if="flowId" class="use-select">
        已选择 <span class="ellipsis" style="max-width: 150px">{{flowData.name}}</span>
        <span style="margin-left: 8px; color: #3d79fb; cursor: pointer" @click="handleClearClick">清空</span>
      </div>
      <Pagination
        v-if="flowList.length > 0"
        :current="pageNo"
        :page-size="pageSize"
        :page-size-options="pageSizeOptions"
        :total="total"
        :showQuickJumper="true"
        :showSizeChanger="true"
        @change="handlePageChange"
        class="list-footer"
        size="small"
      />
    </BasicModal>
  </div>
</template>

<script lang="ts">
  import { ref, unref } from 'vue';
  import BasicModal from '@/components/Modal/src/BasicModal.vue';
  import { useModal, useModalInner } from '@/components/Modal';
  import { Pagination } from 'ant-design-vue';
  import { list } from '@/views/super/airag/aiknowledge/AiKnowledgeBase.api';
  // import {pageApi} from "@/views/super/airag/aiflow/pages/api";
  import { defHttp } from "@/utils/http/axios";
  import knowledge from '/@/views/super/airag/aiknowledge/icon/knowledge.png';
  import { cloneDeep } from 'lodash-es';
  import { getFileAccessHttpUrl } from "@/utils/common/compUtils";
  import defaultFlowImg from "@/assets/images/ai/aiflow.png";

  export default {
    name: 'AiAppAddFlowModal',
    components: {
      Pagination,
      BasicModal,
    },
    emits: ['success', 'register'],
    setup(props, { emit }) {
      const title = ref<string>('选择流程');
      //应用类型
      const flowId = ref<any>([]);
      //流程数据
      const flowList = ref<any>({});
      //选中的数据
      const flowData = ref<any>({})
      //当前页数
      const pageNo = ref<number>(1);
      //每页条数
      const pageSize = ref<number>(10);
      //总条数
      const total = ref<number>(0);
      //搜索文本
      const searchText = ref<string>('');
      //可选择的页数
      const pageSizeOptions = ref<any>(['10', '20', '30']);
      //注册modal
      const [registerModal, { closeModal, setModalProps }] = useModalInner(async (data) => {
        flowId.value = data.flowId ? cloneDeep(data.flowId) : '';
        flowData.value = data.flowData ? cloneDeep(data.flowData) : {};
        setModalProps({ minHeight: 500, bodyStyle: { padding: '10px' } });
        loadFlowData();
      });

      /**
       * 保存
       */
      async function handleOk() {
        emit('success',{ flowId: flowId.value, flowData: flowData.value });
        handleCancel();
      }

      /**
       * 取消
       */
      function handleCancel() {
        closeModal();
      }

      //复选框选中事件
      const handleSelect = (item) => {
        if(flowId.value === item.id){
          flowId.value = "";
          flowData.value = null;
          return;
        }
        flowId.value = item.id;
        flowData.value = item;
      };

      /**
       * 加载知识库
       */
      function loadFlowData() {
        let params = {
          pageNo: pageNo.value,
          pageSize: pageSize.value,
          column: 'createTime',
          order: 'desc',
          name: searchText.value,
          status:'enable'
        };
        getAiFlowList(params).then((res) =>{
          if(res){
            for (const data of res.records) {
              data.metadata = getMetadata(data.metadata);
            }
            flowList.value = res.records;
            total.value = res.total;
          } else {
            flowList.value = [];
            total.value = 0;
          }
        });
      }

      async function getAiFlowList(params?: any) {
        return defHttp.get({url: '/airag/flow/list', params});
      }

      /**
       * 分页改变事件
       * @param page
       * @param current
       */
      function handlePageChange(page, current) {
        pageNo.value = page;
        pageSize.value = current;
        loadFlowData();
      }

      /**
       * 清空选中状态
       */
      function handleClearClick() {
        flowId.value = "";
        flowData.value = null;
      }

      /**
       * 获取图标
       */
      function getImage(icon) {
        return icon ? getFileAccessHttpUrl(icon) : defaultFlowImg;
      }
      
      /**
       * 获取输入输出参入
       *
       * @param metadata
       */
      function getMetadata(metadata) {
        if (!metadata) {
          return [];
        }
        let parse = JSON.parse(metadata);
        let inputsArr = parse['inputs'];
        return [...inputsArr];
      }
      
      return {
        registerModal,
        title,
        handleOk,
        handleCancel,
        flowList,
        flowId,
        handleSelect,
        pageNo,
        pageSize,
        pageSizeOptions,
        total,
        handlePageChange,
        knowledge,
        searchText,
        loadFlowData,
        handleClearClick,
        flowData,
        getImage,
      };
    },
  };
</script>

<style scoped lang="less">
  .header {
    color: #646a73;
    width: 100%;
    justify-content: space-between;
    margin-bottom: 10px;
    .header-search {
      width: 200px;
    }
  }
  .pointer {
    cursor: pointer;
  }
  .type-title {
    color: #1d2025;
    margin-bottom: 4px;
  }
  .type-desc {
    color: #8f959e;
    font-weight: 400;
  }
  .list-footer {
    position: absolute;
    bottom: 0;
    left: 260px;
  }
  .checkbox-card {
    margin-bottom: 10px;
    margin-right: 10px;
  }
  .checkbox-name {
    font-size: 14px;
    font-weight: bold;
    color: #354052;
    width: 100%;
    overflow: hidden;
    align-content: center;
    text-overflow: ellipsis;
    white-space: nowrap;
    display: grid;
  }
  .use-select {
    color: #646a73;
    position: absolute;
    bottom: 0;
    left: 20px;
    display: flex;
  }
  .ellipsis {
    display: block;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
  .flow-icon{
    width: 34px;
    height: 34px;
    border-radius: 10px;
  }
  :deep(.ant-card .ant-card-body){
    padding:16px !important;
  }
  .header-create-by{
    font-size: 12px;
    color: #646a73;
  }
  .text-desc {
    width: 100%;
    font-weight: 400;
    display: inline-block;
    text-overflow: ellipsis;
    overflow: hidden;
    text-wrap: nowrap;
    font-size: 12px;
    color: #676F83;
  }
  .mt-10{
    margin-top: 10px;
  }
  .flex{
    display: flex;
  }
  .text-status{
    font-size: 12px;
    color: #676F83;
  }
  .tag-text {
    display: flow;
    max-width: 48px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    height: 20px;
    font-size: 12px;
    color: rgba(15, 21, 40,0.82);
  }
  .tag-input{
    align-self: center;
    color: rgba(55,67,106,0.7);
    font-size: 12px;
    font-style: normal;
    font-weight: 500;
    line-height: 16px;
    margin-right: 6px;
    text-align: right;
    white-space: nowrap;
  }
  .tags-meadata{
    padding-inline: 2px;
    border-radius: 4px;
    display: flex;
    font-weight: 500;
    max-width: 100%;
  }
</style>
