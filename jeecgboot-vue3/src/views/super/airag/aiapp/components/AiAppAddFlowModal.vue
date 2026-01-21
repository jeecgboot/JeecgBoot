<template>
  <div class="p-2">
    <BasicModal destroyOnClose @register="registerModal" :canFullscreen="false" width="600px" :title="title" @ok="handleOk" @cancel="handleCancel">
      <div class="flex header">
        <JInput
          @pressEnter="loadFlowData"
          class="header-search"
          size="small"
          v-model:value="searchText"
          placeholder="请输入流程名称，回车搜索"
        />
      </div>
      <a-row :span="24">
        <a-col :span="12" v-for="item in flowList" @click="handleSelect(item)">
          <!-- begin 流程选择支持单选和多选 -->
          <a-card :style="getCardStyle(item)" hoverable class="checkbox-card" :body-style="{ width: '100%' }">
            <div style="display: flex; width: 100%;align-items:center; justify-content: space-between">
              <div style="display: flex; align-items:center; flex: 1; overflow: hidden; margin-right: 10px;">
                <img :src="getImage(item.icon)" class="flow-icon"/>
                <div style="display: grid;margin-left: 5px;align-items: center">
                  <span class="checkbox-name ellipsis">{{ item.name }}</span>
                  <div class="flex text-status" v-if="item.metadata && item.metadata.length>0">
                    <span class="tag-input">输入</span>
                    <div v-for="(metaItem, index) in item.metadata">
                      <a-tag color="#f2f3f8" class="tags-meadata">
                        <span v-if="index<3" class="tag-text">{{ metaItem.field }}</span>
                      </a-tag>
                    </div>
                  </div>
                </div>
              </div>
              <a-checkbox v-if="multiple" v-model:checked="item.checked" @click.stop @change="(e)=>handleChange(e,item)"></a-checkbox>
              <!-- end 流程选择支持单选和多选 -->
            </div>
            <div class="text-desc mt-10">
              {{ item.descr || '暂无描述' }}
            </div>
          </a-card>
        </a-col>
      </a-row>
      <div v-if="showFooterSelection" class="use-select">
        <template v-if="!multiple">
          已选择 <span class="ellipsis" style="max-width: 100px">{{flowData.name}}</span>
        </template>
        <template v-else>
          已选择 {{ flowId.length }} 个流程
        </template>
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
  import { ref, unref, computed } from 'vue';
  import BasicModal from '@/components/Modal/src/BasicModal.vue';
  import { useModal, useModalInner } from '@/components/Modal';
  import { Pagination } from 'ant-design-vue';
  import {JInput} from "@/components/Form";
  import { list } from '@/views/super/airag/aiknowledge/AiKnowledgeBase.api';
  import knowledge from '/@/views/super/airag/aiknowledge/icon/knowledge.png';
  import { cloneDeep } from 'lodash-es';
  // import {pageApi} from "@/views/super/airag/aiflow/pages/api";
  import { defHttp } from "@/utils/http/axios";
  import { getFileAccessHttpUrl } from "@/utils/common/compUtils";
  import defaultFlowImg from "@/assets/images/ai/aiflow.png";

  export default {
    name: 'AiAppAddFlowModal',
    components: {
      Pagination,
      BasicModal,
      JInput,
    },
    emits: ['success', 'register'],
    props: {
      multiple:{ type: Boolean, default: false },
      // 排除的流程ID，多个逗号分隔
      excludedIds: { type: String, default: '' },
    },
    setup(props, { emit }) {
      const title = ref<string>('选择流程');
      //应用类型
      const flowId = ref<any>([]);
      //流程数据
      const flowList = ref<any>([]);
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
        //update-begin---author:wangshuai---date:2025-12-24---for: 流程选择支持单选和多选 ---
        if (props.multiple) {
          flowId.value = data.flowId ? (Array.isArray(data.flowId) ? cloneDeep(data.flowId) : data.flowId.split(',')) : [];
          flowData.value = data.flowData ? cloneDeep(data.flowData) : [];
        } else {
          flowId.value = data.flowId ? cloneDeep(data.flowId) : '';
          flowData.value = data.flowData ? cloneDeep(data.flowData) : {};
        }
        setModalProps({ minHeight: 500, bodyStyle: { padding: '10px', height: 'calc(100% - 20px)', overflowY: 'auto' } });
        //update-end---author:wangshuai---date:2025-12-24---for:流程选择支持单选和多选---
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
        //update-begin---author:wangshuai---date:2025-12-24---for: 流程选择支持单选和多选 ---
        if(!props.multiple) {
          if (flowId.value === item.id) {
            flowId.value = "";
            flowData.value = null;
            return;
          }
          flowId.value = item.id;
          flowData.value = item;
        } else {
          item.checked = !item.checked;
          updateMultipleSelection(item);
        }
        //update-end---author:wangshuai---date:2025-12-24---for: 流程选择支持单选和多选 ---
      };

      /**
       * 加载AI流程
       */
      function loadFlowData() {
        let params: Recordable = {
          pageNo: pageNo.value,
          pageSize: pageSize.value,
          column: 'createTime',
          order: 'desc',
          name: searchText.value,
          status: 'enable,release'
        };

        // 排除的流程ID，多个逗号分隔
        if (props.excludedIds) {
          params.excludedIds = props.excludedIds;
        }

        getAiFlowList(params).then((res) =>{
          if(res){
            for (const data of res.records) {
              data.metadata = getMetadata(data.metadata);
              //update-begin---author:wangshuai---date:2025-12-24---for: 流程选择支持单选和多选 ---
              if (props.multiple && Array.isArray(flowId.value) && flowId.value.includes(data.id)) {
                data.checked = true;
              }
              //update-end---author:wangshuai---date:2025-12-24---for: 流程选择支持单选和多选 ---
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
        //update-begin---author:wangshuai---date:2025-12-24---for: 流程选择支持单选和多选 ---
        if (!props.multiple) {
          flowId.value = "";
          flowData.value = null;
        } else {
          flowId.value = [];
          flowData.value = [];
          if (flowList.value && Array.isArray(flowList.value)) {
            flowList.value.forEach(item => item.checked = false);
          }
        }
        //update-end---author:wangshuai---date:2025-12-24---for: 流程选择支持单选和多选 ---
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

      /*===========begin 流程选择支持多选 ===========*/
      function handleChange(e, item) {
        updateMultipleSelection(item);
      }

      function updateMultipleSelection(item) {
        if (item.checked) {
          if (!flowId.value.includes(item.id)) {
            flowId.value.push(item.id);
            flowData.value.push(item);
          }
        } else {
          const index = flowId.value.indexOf(item.id);
          if (index > -1) {
            flowId.value.splice(index, 1);
            flowData.value.splice(index, 1);
          }
        }
      }
      
      const showFooterSelection = computed(() => {
        if (props.multiple) {
          return flowId.value && flowId.value.length > 0;
        }
        return !!flowId.value;
      });

      function getCardStyle(item) {
        if (props.multiple) {
          return item.checked ? { border: '1px solid #3370ff' } : {};
        }
        return item.id === flowId.value ? { border: '1px solid #3370ff' } : {};
      }
      /*===========end 流程选择支持多选 ===========*/
      
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
        handleChange,
        getCardStyle,
        showFooterSelection,
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
    left: 210px;
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
    color: #3a3f4f;
  }
  .tag-input{
    align-self: center;
    color: #707a97;
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

  :deep(.jeecg-modal-wrapper){
    height: calc(100% - 20px);
  }

  .scroll-container {
    height: 480px;
    overflow-y: auto;
    padding-bottom: 20px;
  }
</style>
