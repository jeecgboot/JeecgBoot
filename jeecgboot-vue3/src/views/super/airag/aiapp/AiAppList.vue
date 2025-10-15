<!--知识库文档列表-->
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
            <a-form-item name="name" label="应用名称">
              <JInput v-model:value="queryParam.name" placeholder="请输入应用名称" />
            </a-form-item>
          </a-col>
          <a-col :xl="7" :lg="7" :md="8" :sm="24">
            <a-form-item name="type" label="应用类型">
              <j-dict-select-tag v-model:value="queryParam.type" dict-code="ai_app_type" placeholder="请选择应用类型" />
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
        <a-card class="add-knowledge-card" @click="handleCreateApp">
          <div class="flex">
            <Icon icon="ant-design:plus-outlined" class="add-knowledge-card-icon" size="20"></Icon>
            <span class="add-knowledge-card-title">创建应用</span>
          </div>
        </a-card>
      </a-col>
      <a-col :xxl="4" :xl="6" :lg="6" :md="6" :sm="12" :xs="24" v-for="item in knowledgeAppDataList">
        <a-card class="knowledge-card pointer" @click="handleEditClick(item)">
          <div class="flex">
            <img class="header-img" :src="getImage(item.icon)" />
            <div class="header-text">
              <span class="header-text-top header-name ellipsis"> {{ item.name }} </span>
              <span class="header-text-top header-create ellipsis">
                <a-tag v-if="item.status === 'release'" color="green">已发布</a-tag>
                <a-tag v-if="item.status === 'disable'">已禁用</a-tag>
                <span>创建者：{{ item.createBy_dictText || item.createBy }}</span>
              </span>
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
            <template v-if="item.status !== 'release'">
              <a-divider type="vertical" style="float: left" />
              <a-tooltip title="删除">
                <div class="card-footer-icon" @click.prevent.stop="handleDeleteClick(item)">
                  <Icon icon="ant-design:delete-outlined" class="operation" size="18" color="#1F2329"></Icon>
                </div>
              </a-tooltip>
            </template>
            <a-divider type="vertical" style="float: left" />
            <a-tooltip title="发布">
              <a-dropdown class="card-footer-icon" placement="bottomRight" :trigger="['click']">
                <div @click.prevent.stop>
                  <Icon style="position: relative;top: 1px" icon="ant-design:send-outlined" size="16" color="#1F2329"></Icon>
                </div>
                <template #overlay>
                  <a-menu>
                    <template v-if="item.status === 'enable'">
                      <a-menu-item key="release" @click.prevent.stop="handleSendClick(item,'release')">
                        <Icon icon="lineicons:rocket-5" size="16"></Icon>
                        发布
                      </a-menu-item>
                      <a-menu-divider/>
                    </template>
                    <template v-else-if="item.status === 'release'">
                      <a-menu-item key="un-release" @click.prevent.stop="handleSendClick(item,'un-release')">
                        <Icon icon="tabler:rocket-off" size="16"></Icon>
                        取消发布
                      </a-menu-item>
                      <a-menu-divider/>
                    </template>
                    <a-menu-item key="web" @click.prevent.stop="handleSendClick(item,'web')">
                      <Icon icon="ant-design:dribbble-outlined" size="16"></Icon>
                      嵌入网站
                    </a-menu-item>
                    <a-menu-item v-if="isShowMenu" key="menu" @click.prevent.stop="handleSendClick(item,'menu')">
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
      :show-total="() => `共${total}条` "
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
  import { ref, reactive, onMounted } from 'vue';
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
  import { $electron } from "@/electron";
  import { appList, deleteApp, releaseApp } from './AiApp.api';
  import { useMessage } from '@/hooks/web/useMessage';
  import JInput from '@/components/Form/src/jeecg/components/JInput.vue';
  import JDictSelectTag from '@/components/Form/src/jeecg/components/JDictSelectTag.vue';
  import { useRouter } from "vue-router";

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
      const { createMessage, createConfirmSync } = useMessage();
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
      function handleViewClick(id: string) {
        let url = '/ai/app/chat/' + id;

        // update-begin--author:sunjianlei---date:20250411---for：【QQYUN-9685】构建 electron 桌面应用
        if ($electron.isElectron()) {
          url = $electron.resolveRoutePath(url);
          window.open(url, '_blank', 'width=1200,height=800');
          return
        }
        // update-end----author:sunjianlei---date:20250411---for：【QQYUN-9685】构建 electron 桌面应用

        window.open(url, '_blank');
      }

      /**
       * 删除
       */
      function handleDeleteClick(item) {
        if(knowledgeAppDataList.value.length == 1 && pageNo.value > 1) {
          pageNo.value = pageNo.value - 1;
        }
        deleteApp({ id: item.id, name: item.name }, reload);
      }

      /**
       * 发布点击事件
       * @param item 数据
       * @param type 类别
       */
      function handleSendClick(item,type) {
        if (type === 'release' || type === 'un-release') {
          return onRelease(item);
        }

        openAiAppSendModal(true,{
          type: type,
          data: item
        })
      }

      async function onRelease(item) {
        const toRelease = item.status === 'enable';
        let flag = await createConfirmSync({
          title: toRelease ? '发布应用' : '取消发布应用',
          content: toRelease ? '确定要发布应用吗？发布后将不允许修改应用。' : '确定要取消发布应用吗？',
          okText: '确定',
          cancelText: '取消',
        });
        if (!flag) {
          return
        }
        doRelease(item, item.status === 'enable');
      }

      /**
       * 发布
       */
      async function doRelease(item, release: boolean) {
        let success: boolean = await releaseApp(item.id, release);
        if (success) {
          // 发布成功
          if (release) {
            item.status = 'release'
          } else {
            item.status = 'enable'
          }
        }
      }

      /**
       * 重置
       */
      function searchReset() {
        pageNo.value = 1;
        formRef.value.resetFields();
        queryParam.name = '';
        //刷新数据
        reload();
      }

      /**
       * 查询
       */
      function searchQuery(){
        pageNo.value = 1; 
        //刷新数据
        reload();
      }

      const router = useRouter();
      //是否显示菜单配置选项
      const isShowMenu = ref<boolean>(false);
      onMounted((()=>{
        let fullPath = router.currentRoute.value.fullPath;
        console.log(fullPath)
        if(fullPath === '/myapps/ai/app'){
          isShowMenu.value = false;
        } else {
          isShowMenu.value = true;
        }
      }))

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
        isShowMenu,
        searchQuery,
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
  }

  .add-knowledge-card {
    margin-bottom: 20px;
    background: #fcfcfd;
    border: 1px solid #f0f0f0;
    box-shadow: 0 2px 4px #e6e6e6;
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

  .knowledge-card {
    border-radius: 10px;
    margin-right: 20px;
    margin-bottom: 20px;
    height: 152px;
    background: #fcfcfd;
    border: 1px solid #f0f0f0;
    box-shadow: 0 2px 4px #e6e6e6;
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
    box-shadow: 0 6px 12px #d0d3d8;
  }

  .knowledge-row {
    max-height: calc(100% - 100px);
    margin-top: 20px;
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
    background-color: #e9ecf2;
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
