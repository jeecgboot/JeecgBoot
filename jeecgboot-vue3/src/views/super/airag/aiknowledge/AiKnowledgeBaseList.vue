<!--知识库添加页面-->
<template>
  <div class="knowledge">
    <!--查询区域-->
    <div class="jeecg-basic-table-form-container">
      <a-form
        ref="formRef"
        @keyup.enter.native="searchQuery"
        :model="queryParam"
        :label-col="labelCol"
        :wrapper-col="wrapperCol"
        style="background-color: #f7f8fc"
      >
        <a-row :gutter="24">
          <a-col :xl="7" :lg="7" :md="8" :sm="24">
            <a-form-item name="name" label="知识库名称">
              <JInput v-model:value="queryParam.name" placeholder="请输入知识库名称" />
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: left; overflow: hidden" class="table-page-search-submitButtons">
              <a-col :lg="6">
                <a-button type="primary" preIcon="ant-design:search-outlined" @click="searchQuery">查询</a-button>
                <a-button type="primary" preIcon="ant-design:reload-outlined" @click="searchReset" style="margin-left: 8px">重置</a-button>
              </a-col>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>

    <a-row :span="24" class="knowledge-row">
      <a-col :xxl="4" :xl="6" :lg="6" :md="6" :sm="12" :xs="24">
        <a-card class="add-knowledge-card" @click="handleAddKnowled">
          <div class="flex">
            <Icon icon="ant-design:plus-outlined" class="add-knowledge-card-icon" size="20"></Icon>
            <span class="add-knowledge-card-title">创建知识库</span>
          </div>
        </a-card>
      </a-col>
      <a-col v-if="knowledgeList && knowledgeList.length>0" :xxl="4" :xl="6" :lg="6" :md="6" :sm="12" :xs="24" v-for="item in knowledgeList">
        <a-card class="knowledge-card pointer" @click="handleDocClick(item.id)">
          <div class="knowledge-header">
            <div class="flex">
              <img class="header-img" src="./icon/knowledge.png" />
              <div class="header-text">
                <span class="header-text-top header-name ellipsis" :title="item.name"> {{ item.name }} </span>
                <span class="header-text-top"> 创建者：{{ item.createBy }} </span>
              </div>
            </div>
          </div>
          <div class="mt-10 text-desc">
            <span>{{ item.descr || '暂无描述' }}</span>
          </div>
          <div class="knowledge-footer">
            <Icon class="knowledge-footer-icon" icon="ant-design:deployment-unit-outlined" size="14"></Icon>
            <span>{{ item.embedId_dictText }}</span>
          </div>
          <div class="knowledge-btn">
            <a-dropdown placement="bottomRight" :trigger="['click']" :getPopupContainer="(node) => node.parentNode">
              <div class="ant-dropdown-link pointer model-icon" @click.prevent.stop>
                <Icon icon="ant-design:ellipsis-outlined" size="16"></Icon>
              </div>
              <template #overlay>
                <a-menu>
                  <a-menu-item key="vectorization" @click.prevent.stop="handleVectorization(item.id)">
                    <Icon icon="ant-design:retweet-outlined" size="16"></Icon>
                    向量化
                  </a-menu-item>
                  <a-menu-item key="text" @click.prevent.stop="handleEditClick(item)">
                    <Icon class="pointer" icon="ant-design:edit-outlined" size="16"></Icon>
                    编辑
                  </a-menu-item>
                  <a-menu-item key="file" @click.prevent.stop="handleDelete(item)">
                    <Icon class="pointer" icon="ant-design:delete-outlined" size="16"></Icon>
                    删除
                  </a-menu-item>
                </a-menu>
              </template>
            </a-dropdown>
          </div>
        </a-card>
      </a-col>
    </a-row>
    <Pagination
      v-if="knowledgeList.length > 0"
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
    <!--添加知识库弹窗-->
    <KnowledgeBaseModal @register="registerModal" @success="reload"></KnowledgeBaseModal>
    <!-- 知识库文档弹窗 -->
    <AiragKnowledgeDocListModal @register="docListRegister"></AiragKnowledgeDocListModal>
  </div>
</template>

<script lang="ts">
  import { reactive, ref } from 'vue';
  import { useModal } from '/@/components/Modal';
  import { deleteModel, list, rebuild } from './AiKnowledgeBase.api';
  import { Pagination } from 'ant-design-vue';
  import JInput from '@/components/Form/src/jeecg/components/JInput.vue';
  import KnowledgeBaseModal from './components/AiKnowledgeBaseModal.vue';
  import JSelectUser from '@/components/Form/src/jeecg/components/JSelectUser.vue';
  import JDictSelectTag from '@/components/Form/src/jeecg/components/JDictSelectTag.vue';
  import AiragKnowledgeDocListModal from './components/AiragKnowledgeDocListModal.vue';
  import Icon from '@/components/Icon';
  import { useMessage } from "@/hooks/web/useMessage";

  export default {
    name: 'KnowledgeBaseList',
    components: {
      Icon,
      AiragKnowledgeDocListModal,
      KnowledgeBaseModal,
      JDictSelectTag,
      JSelectUser,
      JInput,
      Pagination,
    },
    setup() {
      //模型列表
      const knowledgeList = ref([]);

      //注册modal
      const [registerModal, { openModal }] = useModal();
      const [docListRegister, { openModal: openDocModal }] = useModal();

      //当前页数
      const pageNo = ref<number>(1);
      //每页条数
      const pageSize = ref<number>(10);
      //总条数
      const total = ref<number>(0);
      //可选择的页数
      const pageSizeOptions = ref<any>(['10', '20', '30']);
      //查询参数
      const queryParam = reactive<any>({});
      //查询区域label宽度
      const labelCol = reactive({
        xs: 24,
        sm: 4,
        xl: 6,
        xxl: 6,
      });
      //查询区域组件宽度
      const wrapperCol = reactive({
        xs: 24,
        sm: 20,
      });
      //查询区域表单的ref
      const formRef = ref();
      const { createMessage } = useMessage();

      //页面初始化执行列表查询
      reload();

      /**
       * 新增
       */
      async function handleAddKnowled() {
        openModal(true, {});
      }

      /**
       * 编辑
       *
       * @param item
       */
      function handleEditClick(item) {
        console.log(item);
        openModal(true, {
          id: item.id,
          isUpdate: true,
        });
      }

      /**
       * 重新加载数据
       */
      function reload() {
        let params = {
          pageNo: pageNo.value,
          pageSize: pageSize.value,
          column: 'createTime',
          order: 'desc'
        };
        Object.assign(params, queryParam);
      
        list(params).then((res) => {
          if (res.success) {
            knowledgeList.value = res.result.records;
            total.value = res.result.total;
          } else {
            knowledgeList.value = [];
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
        reload();
      }

      /**
       * 删除模型
       * @param item
       */
      async function handleDelete(item) {
        await deleteModel({ id: item.id, name: item.name }, reload);
      }

      /**
       * 查询
       */
      function searchQuery() {
        reload();
      }

      /**
       * 重置
       */
      function searchReset() {
        formRef.value.resetFields();
        queryParam.createBy = '';
        //刷新数据
        reload();
      }

      /**
       * 参数配置点击事件
       *
       * @param id
       */
      function handleDocClick(id) {
        openDocModal(true, { id });
      }

      /**
       * 知识库向量化
       * @param id
       */
      async function handleVectorization(id) {
        rebuild({ knowIds: id }).then((res) =>{
          if(res.success){
            createMessage.success("向量化成功！");
            reload();
          }else{
            createMessage.warning("向量化失败！");
          }
        }).catch(err=>{
          createMessage.warning("向量化失败！");
        });
      }

      return {
        handleAddKnowled,
        handleEditClick,
        registerModal,
        knowledgeList,
        reload,
        pageNo,
        pageSize,
        pageSizeOptions,
        total,
        handlePageChange,
        handleDelete,
        searchQuery,
        searchReset,
        queryParam,
        labelCol,
        wrapperCol,
        formRef,
        handleDocClick,
        docListRegister,
        handleVectorization,
      };
    },
  };
</script>

<style scoped lang="less">
  .knowledge {
    height: calc(100vh - 115px);
    background: #f7f8fc;
    padding: 24px;
    overflow-y: auto;

    .knowledge-row {
      max-height: calc(100% - 100px);
      margin-top: 20px;
      overflow-y: auto;
      .knowledge-header {
        position: relative;
        font-size: 14px;
        height: 40px;
        .header-img {
          width: 34px;
          height: 34px;
          margin-right: 6px;
        }
        .header-text {
          width: calc(100% - 80px);
          overflow: hidden;
          position: relative;
          display: grid;
          .header-name {
            font-size: 14px !important;
            font-weight: bold;
            color: #354052 !important;
          }
          .header-text-top {
            height: 22px;
            font-size: 12px;
          }
        }
      }
    }
  }

  .text-desc {
    width: 100%;
    font-weight: 400;
    display: inline-block;
    text-overflow: ellipsis;
    display: -webkit-box;
    -webkit-box-orient: vertical;
    -webkit-line-clamp: 2;
    height: 40px;
    overflow: hidden;
    font-size: 12px;
    color: #676f83;
  }
  
  .knowledge-footer{
    font-size: 12px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    margin-top: 16px;
    .knowledge-footer-icon{
      position: relative;
      top: 2px
    }
    span{
      margin-left: 2px;
    }
  }

  .flex {
    display: flex;
    align-items: center;
  }

  :deep(.ant-card .ant-card-body) {
    padding: 16px;
  }

  .mt-10 {
    margin-top: 10px;
  }

  .ml-4 {
    margin-left: 4px;
  }

  .knowledge-btn {
    position: absolute;
    right: 4px;
    top: 6px;
    height: auto;
    display: none;
  }
  .add-knowledge-card {
    margin-bottom: 20px;
    background: #fcfcfd;
    border: 1px solid #f0f0f0;
    box-shadow: 0 2px 4px rgba(0,0,0,0.1);
    transition: all 0.3s ease;
    border-radius: 10px;
    display: inline-flex;
    justify-content: center;
    align-items: center;
    font-size: 16px;
    cursor: pointer;
    height: 152px;
    width: calc(100% - 20px);
    .add-knowledge-card-icon {
      padding: 8px;
      color: #1f2329;
      background-color: #f5f6f7;
      margin-right: 12px;
    }
    .add-knowledge-card-title {
      font-size: 16px;
      color:#1f2329;
      font-weight: 400;
      align-self: center;
    }
  }

  .add-knowledge-card:hover {
    box-shadow: 0 6px 12px rgba(0,0,0,0.15);
  }

  .knowledge-card {
    margin-right: 20px;
    margin-bottom: 20px;
    height: 152px;
    border-radius: 10px;
    background: #fcfcfd;
    border: 1px solid #f0f0f0;
    box-shadow: 0 2px 4px rgba(0,0,0,0.1);
    transition: all 0.3s ease;
  }
  .knowledge-card:hover {
    box-shadow: 0 6px 12px rgba(0,0,0,0.15);
    .knowledge-btn {
      display: block;
    }
  }
  .pointer {
    cursor: pointer;
  }
  .list-footer {
    text-align: right;
    margin-top: 5px;
  }
  .jeecg-basic-table-form-container {
    padding: 0;
    :deep(.ant-form) {
      background-color: transparent;
    }
    .table-page-search-submitButtons {
      display: block;
      margin-bottom: 24px;
      white-space: nowrap;
    }
  }
  
  .model-icon{
    background-color: unset;
    border: none;
    margin-right: 2px;
  }
  .model-icon:hover{
    color: #000000;
    background-color: rgba(0,0,0,0.05);
    border: none;
  }
  .ant-dropdown-link{
    font-size: 14px;
    height: 24px;
    padding: 0 7px;
    border-radius: 4px;
    align-content: center;
    text-align: center;
  }
  
  .ellipsis{
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
</style>
