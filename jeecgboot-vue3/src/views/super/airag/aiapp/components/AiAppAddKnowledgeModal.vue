<template>
  <div class="p-2">
    <BasicModal destroyOnClose @register="registerModal" :canFullscreen="false" width="600px" :title="title" @ok="handleOk" @cancel="handleCancel">
      <div class="flex header">
        <span>所选知识库必须使用相同的 Embedding 模型</span>
        <a-input
          @pressEnter="loadKnowledgeData"
          class="header-search"
          size="small"
          v-model:value="searchText"
          placeholder="请输入知识库名称，回车搜索"
        ></a-input>
      </div>
      <a-row :span="24">
        <a-col :span="12" v-for="item in appKnowledgeOption" @click="handleSelect(item)">
          <a-card :style="item.checked ? { border: '1px solid #3370ff' } : {}" hoverable class="checkbox-card" :body-style="{ width: '100%' }">
            <div style="display: flex; width: 100%; justify-content: space-between">
              <div>
                <img class="checkbox-img" :src="knowledge" />
                <span class="checkbox-name">{{ item.name }}</span>
              </div>
              <a-checkbox v-model:checked="item.checked" @click.stop class="quantum-checker"> </a-checkbox>
            </div>
          </a-card>
        </a-col>
      </a-row>
      <div v-if="knowledgeIds.length > 0" class="use-select">
        已选择 {{ knowledgeIds.length }} 知识库
        <span style="margin-left: 8px; color: #3d79fb; cursor: pointer" @click="handleClearClick">清空</span>
      </div>
      <Pagination
        v-if="appKnowledgeOption.length > 0"
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
  import knowledge from '/@/views/super/airag/aiknowledge/icon/knowledge.png';
  import { cloneDeep } from 'lodash-es';

  export default {
    name: 'AiAppAddKnowledgeModal',
    components: {
      Pagination,
      BasicModal,
    },
    emits: ['success', 'register'],
    setup(props, { emit }) {
      const title = ref<string>('添加关联知识库');

      //app知识库
      const appKnowledgeOption = ref<any>([]);
      //应用类型
      const knowledgeIds = ref<any>([]);
      //应用数据
      const knowledgeData = ref<any>([]);
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
        knowledgeIds.value = data.knowledgeIds ? cloneDeep(data.knowledgeIds.split(',')) : [];
        knowledgeData.value = data.knowledgeDataList ? cloneDeep(data.knowledgeDataList) : [];
        setModalProps({ minHeight: 500, bodyStyle: { padding: '10px' } });
        loadKnowledgeData();
      });

      /**
       * 保存
       */
      async function handleOk() {
        emit('success', knowledgeIds.value, knowledgeData.value);
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
        let id = item.id;
        const target = appKnowledgeOption.value.find((item) => item.id === id);
        if (target) {
          target.checked = !target.checked;
        }
        //存放选中的知识库的id
        if (knowledgeIds.value.length == 0) {
          knowledgeIds.value.push(id);
          knowledgeData.value.push(item);
          return;
        }
        let findIndex = knowledgeIds.value.findIndex((item) => item === id);
        if (findIndex === -1) {
          knowledgeIds.value.push(id);
          knowledgeData.value.push(item);
        } else {
          knowledgeIds.value.splice(findIndex, 1);
          knowledgeData.value.splice(findIndex, 1);
        }
      };

      /**
       * 加载知识库
       */
      function loadKnowledgeData() {
        let params = {
          pageNo: pageNo.value,
          pageSize: pageSize.value,
          name: searchText.value,
        };
        list(params).then((res) => {
          if (res.success) {
            if (knowledgeIds.value.length > 0) {
              for (const item of res.result.records) {
                if (knowledgeIds.value.includes(item.id)) {
                  item.checked = true;
                }
              }
              appKnowledgeOption.value = res.result.records;
            } else {
              appKnowledgeOption.value = res.result.records;
            }
            total.value = res.result.total;
          } else {
            appKnowledgeOption.value = [];
            total.value = 0;
          }
        });
      }

      /**
       * 分页改变事件
       * @param page
       * @param current
       */
      function handlePageChange(page, current) {
        pageNo.value = page;
        pageSize.value = current;
        loadKnowledgeData();
      }

      /**
       * 清空选中状态
       */
      function handleClearClick() {
        knowledgeIds.value = [];
        knowledgeData.value = [];
        appKnowledgeOption.value.forEach((item) => {
          item.checked = false;
        });
      }

      return {
        registerModal,
        title,
        handleOk,
        handleCancel,
        appKnowledgeOption,
        knowledgeIds,
        handleSelect,
        pageNo,
        pageSize,
        pageSizeOptions,
        total,
        handlePageChange,
        knowledge,
        searchText,
        loadKnowledgeData,
        handleClearClick,
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
  .checkbox-img {
    width: 30px;
    height: 30px;
  }
  .checkbox-name {
    margin-left: 4px;
  }
  .use-select {
    color: #646a73;
    position: absolute;
    bottom: 0;
    left: 20px;
  }
</style>
