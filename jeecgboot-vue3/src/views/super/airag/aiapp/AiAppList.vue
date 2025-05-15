<!--知识库文档列表-->
<template>
  <div class="p-2 knowledge">
    <!--查询区域-->
    <div class="jeecg-basic-table-form-container">
      <a-form
        ref="formRef"
        @keyup.enter.native="reload"
        :model="queryParam"
        :label-col="labelCol"
        :wrapper-col="wrapperCol"
        style="background-color: #f7f8fc"
      >
        <a-row :gutter="24">
          <a-col :lg="6">
            <a-form-item name="name" label="应用名称">
              <JInput v-model:value="queryParam.name" placeholder="请输入应用名称" />
            </a-form-item>
          </a-col>
          <a-col :lg="6">
            <a-form-item name="type" label="应用类型">
              <j-dict-select-tag v-model:value="queryParam.type" dict-code="ai_app_type" placeholder="请选择应用类型" />
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: left; overflow: hidden" class="table-page-search-submitButtons">
              <a-col :lg="6">
                <a-button type="primary" preIcon="ant-design:search-outlined" @click="reload">查询</a-button>
                <a-button type="primary" preIcon="ant-design:reload-outlined" @click="searchReset" style="margin-left: 8px">重置</a-button>
              </a-col>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <a-row :span="24" class="knowledge-row">
      <a-col :xxl="4" :xl="6" :lg="6" :md="6" :sm="12" :xs="24">
        <a-card class="add-knowledge-card" :bodyStyle="cardBodyStyle">
          <span style="line-height: 18px; font-weight: 500; color: #676f83; font-size: 12px">创建应用</span>
          <div class="add-knowledge-doc" @click="handleCreateApp">
            <Icon icon="ant-design:form-outlined" size="13"></Icon>
            <span>创建空白应用</span>
          </div>
        </a-card>
      </a-col>
      <a-col :xxl="4" :xl="6" :lg="6" :md="6" :sm="12" :xs="24" v-for="item in knowledgeAppDataList">
        <a-card class="knowledge-card pointer" @click="handleEditClick(item)">
          <div class="flex">
            <img class="header-img" :src="getImage(item.icon)" />
            <div class="header-text">
              <span class="header-text-top header-name ellipsis"> {{ item.name }} </span>
              <span class="header-text-top header-create ellipsis"> 创建者：{{ item.createBy }} </span>
            </div>
          </div>
          <div class="header-tag">
            <a-tag color="#EBF1FF" style="margin-right: 0" v-if="item.type === 'chatSimple'">
              <span style="color: #3370ff">简单配置</span>
            </a-tag>
            <a-tag color="#FDF6EC" style="margin-right: 0" v-if="item.type === 'chatFLow'">
              <span style="color: #e6a343">高级编排</span>
            </a-tag>
          </div>
          <div class="card-description">
            <span>{{ item.descr || '暂无描述' }}</span>
          </div>
          <div class="card-footer">
            <a-tooltip title="演示">
              <div class="card-footer-icon" @click.prevent.stop="handleViewClick(item.id)">
                <Icon class="operation" icon="ant-design:youtube-outlined" size="20" color="#1F2329"></Icon>
              </div>
            </a-tooltip>
            <a-divider type="vertical" style="float: left" />
            <a-tooltip title="删除">
              <div class="card-footer-icon" @click.prevent.stop="handleDeleteClick(item)">
                <Icon icon="ant-design:delete-outlined" class="operation" size="20" color="#1F2329"></Icon>
              </div>
            </a-tooltip>
            <a-divider type="vertical" style="float: left" />
            <a-tooltip title="发布">
              <a-dropdown class="card-footer-icon" placement="bottomRight" :trigger="['click']">
                <div @click.prevent.stop>
                  <Icon icon="ant-design:send-outlined"></Icon>
                </div>
                <template #overlay>
                  <a-menu>
                    <a-menu-item key="web" @click.prevent.stop="handleSendClick(item,'web')">
                      <Icon icon="ant-design:dribbble-outlined" size="16"></Icon>
                      嵌入网站
                    </a-menu-item>
                    <a-menu-item key="menu" @click.prevent.stop="handleSendClick(item,'menu')">
                      <Icon icon="ant-design:menu-outlined" size="16"></Icon> 配置菜单
                    </a-menu-item>
                  </a-menu>
                </template>
              </a-dropdown>
            </a-tooltip>
          </div>
        </a-card>
      </a-col>
    </a-row>
    <Pagination
      v-if="knowledgeAppDataList.length > 0"
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
    <!-- Ai新增弹窗  -->
    <AiAppModal @register="registerModal" @success="handleSuccess"></AiAppModal>
    <!-- Ai设置弹窗 -->
    <AiAppSettingModal @register="registerSettingModal" @success="reload"></AiAppSettingModal>
    <!-- 发布弹窗 -->
    <AiAppSendModal @register="registerAiAppSendModal"/>
  </div>
</template>

<script lang="ts">
  import { ref, reactive } from 'vue';
  import BasicModal from '@/components/Modal/src/BasicModal.vue';
  import { useModal, useModalInner } from '@/components/Modal';
  import { LoadingOutlined } from '@ant-design/icons-vue';
  import { Avatar, Modal, Pagination } from 'ant-design-vue';
  import { getFileAccessHttpUrl } from '@/utils/common/compUtils';
  import defaultImg from './img/ailogo.png';
  import AiAppModal from './components/AiAppModal.vue';
  import AiAppSettingModal from './components/AiAppSettingModal.vue';
  import AiAppSendModal from './components/AiAppSendModal.vue';
  import Icon from '@/components/Icon';
  import { appList, deleteApp } from './AiApp.api';
  import { useMessage } from '@/hooks/web/useMessage';
  import JInput from '@/components/Form/src/jeecg/components/JInput.vue';
  import JDictSelectTag from '@/components/Form/src/jeecg/components/JDictSelectTag.vue';

  export default {
    name: 'AiAppList',
    components: {
      JDictSelectTag,
      JInput,
      AiAppSendModal,
      Icon,
      Pagination,
      Avatar,
      LoadingOutlined,
      BasicModal,
      AiAppModal,
      AiAppSettingModal,
    },
    emits: ['success', 'register'],
    setup(props, { emit }) {
      /**
       * 创建应用的集合
       */
      const knowledgeAppDataList = ref<any>([]);
      //当前页数
      const pageNo = ref<number>(1);
      //每页条数
      const pageSize = ref<number>(10);
      //总条数
      const total = ref<number>(0);
      //可选择的页数
      const pageSizeOptions = ref<any>(['10', '20', '30']);
      //注册modal
      const [registerModal, { openModal }] = useModal();
      const [registerSettingModal, { openModal: openAppModal }] = useModal();
      const [registerAiAppSendModal, { openModal: openAiAppSendModal }] = useModal();
      const { createMessage } = useMessage();
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
      //表单的ref
      const formRef = ref();
      
      reload();

      /**
       * 加载数据
       */
      function reload() {
        let params = {
          pageNo: pageNo.value,
          pageSize: pageSize.value,
          column: 'createTime',
          order: 'desc',
        };
        Object.assign(params, queryParam);
        appList(params).then((res) => {
          if (res.success) {
            knowledgeAppDataList.value = res.result.records;
            total.value = res.result.total;
          } else {
            knowledgeAppDataList.value = [];
            total.value = 0;
          }
        });
      }

      /**
       * 创建应用
       */
      function handleCreateApp() {
        openModal(true, {});
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
       * 成功
       */
      function handleSuccess(id) {
        reload();
        //打开编辑弹窗
        openAppModal(true, {
          isUpdate: false,
          id: id,
        });
      }

      /**
       * 获取图片
       * @param url
       */
      function getImage(url) {
        return url ? getFileAccessHttpUrl(url) : defaultImg;
      }

      /**
       * 编辑
       * @param item
       */
      function handleEditClick(item) {
        console.log('item:::', item);
        openAppModal(true, {
          isUpdate: true,
          ...item,
        });
      }

      /**
       * 演示
       */
      function handleViewClick(id) {
        window.open('/ai/app/chat/' + id , '_blank');
      }

      /**
       * 删除
       */
      function handleDeleteClick(item) {
        deleteApp({ id: item.id, name: item.name }, reload);
      }

      /**
       * 发布点击事件
       * @param item 数据
       * @param type 类别
       */
      function handleSendClick(item,type) {
        openAiAppSendModal(true,{
          type: type,
          data: item
        })
      }

      /**
       * 重置
       */
      function searchReset() {
        formRef.value.resetFields();
        queryParam.name = '';
        //刷新数据
        reload();
      }

      return {
        handleCreateApp,
        knowledgeAppDataList,
        pageNo,
        pageSize,
        total,
        pageSizeOptions,
        handlePageChange,
        cardBodyStyle: { textAlign: 'left', width: '100%' },
        registerModal,
        handleSuccess,
        getImage,
        handleEditClick,
        handleViewClick,
        handleDeleteClick,
        registerSettingModal,
        reload,
        queryParam,
        labelCol,
        wrapperCol,
        handleSendClick,
        registerAiAppSendModal,
        searchReset,
        formRef,
      };
    },
  };
</script>

<style scoped lang="less">
  .knowledge {
    display: grid;
    height: 100%;
    background: #f7f8fc;
    padding: 24px;
    overflow-y: auto;
  }

  .add-knowledge-card {
    border-radius: 10px;
    margin-bottom: 20px;
    display: inline-flex;
    font-size: 16px;
    height: 152px;
    width: calc(100% - 20px);
    background: #fcfcfd;
    border: 1px solid #f0f0f0;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    transition: all 0.3s ease;
    .add-knowledge-card-icon {
      padding: 8px;
      color: #1f2329;
      background-color: #f5f6f7;
      margin-right: 12px;
    }
  }

  .knowledge-card {
    border-radius: 10px;
    margin-right: 20px;
    margin-bottom: 20px;
    height: 152px;
    background: #fcfcfd;
    border: 1px solid #f0f0f0;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    transition: all 0.3s ease;
    .header-img {
      width: 40px;
      height: 40px;
      border-radius: 0.5rem;
    }
    .header-text {
      margin-left: 5px;
      position: relative;
      font-size: 14px;
      display: grid;
      width: calc(100% - 100px);
      .header-name {
        font-weight: bold;
        color: #354052;
      }
      .header-create {
        font-size: 12px;
        color: #646a73;
      }
    }
    .header-tag {
      position: absolute;
      right: 4px;
      top: 6px;
    }
  }

  .add-knowledge-card,
  .knowledge-card {
    transition: box-shadow 0.3s ease;
  }

  .add-knowledge-card:hover,
  .knowledge-card:hover {
    box-shadow: 0 6px 12px rgba(0, 0, 0, 0.15);
  }

  .knowledge-row {
    overflow-y: auto;
  }

  .add-knowledge-doc {
    margin-top: 6px;
    color: #6f6f83;
    font-size: 13px;
    width: 100%;
    cursor: pointer;
    display: flex;
    span {
      margin-left: 4px;
      line-height: 28px;
    }
  }
  .add-knowledge-doc:hover {
    background: #c8ceda33;
  }
  .card-description {
    display: -webkit-box;
    -webkit-box-orient: vertical;
    -webkit-line-clamp: 3;
    height: 4.5em;
    overflow: hidden;
    text-overflow: ellipsis;
    line-height: 1.5;
    margin-top: 10px;
    text-align: left;
    font-size: 12px;
    color: #676f83;
  }
  .card-footer {
    position: absolute;
    bottom: 8px;
    left: 0;
    min-height: 30px;
    padding: 0 16px;
    width: 100%;
    align-items: center;
    display: flex;
  }

  .card-footer-icon {
    font-size: 14px;
    height: 24px;
    padding: 0 7px;
    border-radius: 4px;
    text-align: center;
    align-content: center;
    float: left;
    width: 36px;
  }

  .card-footer-icon:hover {
    color: #000000;
    background-color: rgba(0, 0, 0, 0.05);
    border: none;
  }

  .operation {
    position: relative;
    top: 2px;
  }
  .list-footer {
    text-align: right;
    margin-top: 5px;
  }
  :deep(.ant-card .ant-card-body) {
    padding: 16px;
  }
  .ellipsis{
    overflow: hidden;
    text-wrap: nowrap;
    text-overflow: ellipsis;
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
</style>
<style lang="less">
  .airag-knowledge-doc .scroll-container {
    padding: 0 !important;
  }
</style>
