<template>
  <BasicModal
    destroyOnClose
    @register="registerModal"
    :canFullscreen="false"
    width="1000px"
    @ok="handleOk"
    @cancel="handleCancel"
    okText="使用"
    wrapClassName="ai-rag-generate-prompt-modal"
    :confirmLoading="loading"
  >
    <div class="prompt-market-content">
      <!-- 搜索区域 -->
      <div class="search-section">
        <a-input-search
          v-model:value="searchText"
          placeholder="搜索提示词名称或描述"
          style="width: 300px"
          @search="handleSearch"
          @pressEnter="handleSearch"
        />
      </div>

      <!-- 提示词列表 -->
      <div class="prompt-list-section">
        <a-spin :spinning="loading">
          <template v-if="promptList.length > 0">
            <a-row :gutter="[24, 24]">
              <a-col v-for="item in promptList" :key="item.id" :xs="24" :sm="12" :md="8" :lg="8">
                <div class="prompt-card" @click="handleSelectPrompt(item)">
                  <a-card :class="['prompt-item-card', { selected: selectedPrompt?.id === item.id }]" :hoverable="true" size="small">
                    <template #title>
                      <div class="card-title">
                        <span class="title-text">{{ item.name }}</span>
                      </div>
                    </template>
                    <div class="card-content">
                      <p class="description" :title="item.description || item.desc">{{ item.description || item.desc }}</p>
                      <div class="card-footer" >
                        <span class="create-time">
                          {{ formatTime(item.createTime) }}
                        </span>
                      </div>
                    </div>
                  </a-card>
                </div>
              </a-col>
            </a-row>
          </template>

          <!-- 空状态 -->
          <empty v-else description="暂无提示词数据" class="empty-state" />
        </a-spin>
      </div>

      <!-- 分页区域 -->
      <div class="pagination-section">
        <Pagination
          v-model:current="pagination.current"
          v-model:pageSize="pagination.pageSize"
          :total="pagination.total"
          :show-size-changer="true"
          :page-size-options="['10', '20', '30', '50']"
          :show-quick-jumper="true"
          @change="handlePageChange"
          @showSizeChange="handleSizeChange"
        />
      </div>
    </div>
  </BasicModal>
</template>

<script lang="ts">
  import { ref, reactive } from 'vue';
  import { Empty } from 'ant-design-vue';
  import BasicModal from '@/components/Modal/src/BasicModal.vue';
  import { useModalInner } from '@/components/Modal';
  import { list } from '@/views/super/airag/aiprompts/AiragPrompts.api';
  import { formatToDateTime } from '@/utils/dateUtil';
  import { Pagination } from 'ant-design-vue';
  export default {
    name: 'AiAppPromptMarketModal',
    components: {
      BasicModal,
      Pagination,
      Empty,
    },
    emits: ['ok', 'register', 'select'],
    setup(props, { emit }) {
      // 提示词列表
      const promptList = ref<any[]>([]);
      // 加载状态
      const loading = ref<boolean>(false);
      // 搜索文本
      const searchText = ref<string>('');
      // 选中的提示词
      const selectedPrompt = ref<any>(null);

      // 分页配置
      const pagination = reactive({
        current: 1,
        pageSize: 12,
        total: 0,
      });

      // 注册modal
      const [registerModal, { closeModal, setModalProps }] = useModalInner(async () => {
        loading.value = false;
        resetField();
        await getPromptList();
        setModalProps({
          height: 600,
          bodyStyle: { padding: '24px' },
        });
      });

      /**
       *
       */
      function resetField() {
        promptList.value = [];
        selectedPrompt.value = null;
        searchText.value = '';
        pagination.current = 1;
      }

      /**
       * 获取提示词列表
       */
      async function getPromptList() {
        loading.value = true;
        try {
          const params = {
            pageNo: pagination.current,
            pageSize: pagination.pageSize,
            name: searchText.value ? `*${searchText.value}*` : '',
          };

          const res = await list(params);
          console.log('获取提示词列表成功:', res);
          if (res?.records) {
            promptList.value = res?.records || [];
            pagination.total = res?.total || 0;
          } else {
            promptList.value = [];
            pagination.total = 0;
          }
        } catch (error) {
          console.error('获取提示词列表失败:', error);
          promptList.value = [];
          pagination.total = 0;
        } finally {
          loading.value = false;
        }
      }

      /**
       * 处理搜索
       */
      function handleSearch() {
        pagination.current = 1;
        getPromptList();
      }

      /**
       * 处理页码变化
       */
      function handlePageChange(page: number, pageSize: number) {
        pagination.current = page;
        pagination.pageSize = pageSize;
        getPromptList();
      }

      /**
       * 处理页面大小变化
       */
      function handleSizeChange(current: number, size: number) {
        pagination.current = current;
        pagination.pageSize = size;
        getPromptList();
      }

      /**
       * 选择提示词
       */
      function handleSelectPrompt(item: any) {
        selectedPrompt.value = item;
      }
      /**
       * 格式化时间
       */
      function formatTime(time) {
        console.log('formatTime:', formatToDateTime(time));
        return formatToDateTime(time);
      }

      /**
       * 保存
       */
      async function handleOk() {
        if (selectedPrompt.value) {
          emit('ok', selectedPrompt.value.content);
        } else {
          emit('ok');
        }
        handleCancel();
      }

      /**
       * 取消
       */
      function handleCancel() {
        closeModal();
      }

      return {
        registerModal,
        handleOk,
        handleCancel,
        promptList,
        loading,
        searchText,
        selectedPrompt,
        pagination,
        handleSearch,
        handlePageChange,
        handleSizeChange,
        handleSelectPrompt,
        formatTime,
      };
    },
  };
</script>

<style scoped lang="less">
.prompt-market-content {
  min-height: 400px;
  padding: 16px;

  .search-section {
    margin-bottom: 24px;
    display: flex;
    justify-content: flex-start;

    .ant-input-search {
      max-width: 400px;
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
      border-radius: 6px;
    }
  }

  .prompt-list-section {
    .prompt-card {
      transition:
          transform 0.2s ease,
          box-shadow 0.2s ease;

      &:hover {
        transform: translateY(-4px);
      }

      .prompt-item-card {
        border-radius: 8px;
        overflow: hidden;
        height: 150px; // 增加卡片高度
        transition: all 0.3s ease;
        border: 1px solid #e8e8e8;

        .ant-card-head {
          border-bottom: 1px solid #f0f0f0;
          padding: 12px 16px;
        }

        .ant-card-body {
          padding: 12px 16px;
          height: calc(100% - 48px); // 调整内容区域高度
          display: flex;
          flex-direction: column;
        }

        &.selected {
          border-color: #1890ff;
          background-color: #e6f7ff; // 添加选中背景色
          box-shadow: 0 0 0 2px rgba(24, 144, 255, 0.2);

          .card-title .title-text {
            color: #1890ff;
          }
        }

        .card-title {
          display: flex;
          align-items: center;

          .title-text {
            font-weight: 600;
            font-size: 16px;
            color: #333;
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
          }
        }

        .card-content {
          flex: 1; // 使用flex让内容区域自适应
          display: flex;
          flex-direction: column;
          justify-content: space-between; // 让内容和时间信息在垂直方向上分布

          .description {
            color: #666;
            font-size: 13px;
            line-height: 1.5;
            margin: 8px 0;
            display: -webkit-box;
            -webkit-line-clamp: 2;
            -webkit-box-orient: vertical;
            overflow: hidden;
            flex: 1; // 让描述区域自适应
          }

          .card-footer {
            margin-top: 8px; // 调整间距
            padding-top: 8px;
            border-top: 1px solid #f5f5f5;
            display: flex;
            justify-content: flex-end;

            .create-time {
              font-size: 12px;
              color: #999;
            }
          }
        }
      }
    }
  }

  .pagination-section {
    margin-top: 24px;
    padding-top: 16px;
    border-top: 1px solid #f0f0f0;
    display: flex;
    justify-content: flex-end;
  }

  .empty-state {
    margin-top: 40px;
  }
}

// 模态框整体样式
.ai-rag-generate-prompt-modal {
  .ant-modal-body {
    padding: 0 !important;
  }

  .ant-modal-header {
    border-radius: 8px 8px 0 0;
  }
}
</style>
