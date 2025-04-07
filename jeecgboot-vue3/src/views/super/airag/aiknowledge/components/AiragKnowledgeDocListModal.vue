<!--知识库文档列表-->
<template>
  <div class="p-2">
    <BasicModal
      wrapClassName="airag-knowledge-doc"
      destroyOnClose
      @register="registerModal"
      :canFullscreen="false"
      defaultFullscreen
      :title="title"
      :footer="null"
    >
      <a-layout style="height: 100%">
        <a-layout-sider :style="siderStyle">
          <a-menu v-model:selectedKeys="selectedKeys" mode="vertical" style="border: none" :items="menuItems" @click="handleMenuClick" />
        </a-layout-sider>
        <a-layout-content :style="contentStyle">
          <div v-if="selectedKey === 'document'">
            <a-input v-model:value="searchText" placeholder="请输入文档名称，回车搜索" class="search-title" @pressEnter="reload"/>
            <a-row :span="24" class="knowledge-row">
              <a-col :xxl="4" :xl="6" :lg="6" :md="6" :sm="12" :xs="24">
                <a-card class="add-knowledge-card" :bodyStyle="cardBodyStyle">
                  <span style="line-height: 18px;font-weight: 500;color:#676f83;font-size: 12px">创建文档</span>
                  <div class="add-knowledge-doc" @click="handleCreateText">
                    <Icon icon="ant-design:form-outlined" size="13"></Icon><span>手动录入</span>
                  </div>
                  <div class="add-knowledge-doc" @click="handleCreateUpload">
                    <Icon icon="ant-design:cloud-upload-outlined" size="13"></Icon><span>文件上传</span>
                  </div>
                  <div class="add-knowledge-doc" @click="handleCreateUploadLibrary">
                    <a-upload
                        accept=".zip"
                        name="file"
                        :data="{ knowId: knowledgeId }"
                        :showUploadList="false"
                        :headers="headers"
                        :beforeUpload="beforeUpload"
                        :action="uploadUrl"
                        @change="handleUploadChange"
                    >
                      <Icon style="margin-left: 0" icon="ant-design:project-outlined" size="13"></Icon>
                      <span>文档库上传</span>
                    </a-upload>
                  </div>
                </a-card>
              </a-col>
              <a-col :xxl="4" :xl="6" :lg="6" :md="6" :sm="12" :xs="24" v-for="item in knowledgeDocDataList">
                <a-card class="knowledge-card pointer" @click="handleEdit(item)">
                  <div class="knowledge-header">
                    <div class="header-text flex">
                      <Icon v-if="item.type==='text'" icon="ant-design:file-text-outlined" size="32" color="#00a7d0"></Icon>
                      <Icon v-if="item.type==='file' && getFileSuffix(item.metadata) === 'pdf'" icon="ant-design:file-pdf-outlined" size="32" color="rgb(211, 47, 47)"></Icon>
                      <Icon v-if="item.type==='file' && getFileSuffix(item.metadata) === 'docx'" icon="ant-design:file-word-outlined" size="32" color="rgb(68, 138, 255)"></Icon>
                      <Icon v-if="item.type==='file' && getFileSuffix(item.metadata) === 'pptx'" icon="ant-design:file-ppt-outlined" size="32" color="rgb(245, 124, 0)"></Icon>
                      <Icon v-if="item.type==='file' && getFileSuffix(item.metadata) === 'xlsx'" icon="ant-design:file-excel-outlined" size="32" color="rgb(98, 187, 55)"></Icon>
                      <Icon v-if="item.type==='file' && getFileSuffix(item.metadata) === 'txt'" icon="ant-design:file-text-outlined" size="32" color="#00a7d0"></Icon>
                      <Icon v-if="item.type==='file' && getFileSuffix(item.metadata) === 'md'" icon="ant-design:file-markdown-outlined" size="32" color="#292929"></Icon>
                      <Icon v-if="item.type==='file' && getFileSuffix(item.metadata) === ''" icon="ant-design:file-unknown-outlined" size="32" color="#f5f5dc"></Icon>
                      <span class="ellipsis header-title">{{ item.title }}</span>
                    </div>
                  </div>
                  <div class="card-description">
                    <span>{{ item.content }}</span>
                  </div>
                  <div class="flex" style="justify-content: space-between">
                    <div class="card-text">
                      状态：
                      <div v-if="item.status==='complete'" class="card-text-status">
                        <Icon icon="ant-design:check-circle-outlined" size="16" color="#56D1A7"></Icon>
                        <span class="ml-2">已完成</span>
                      </div>
                      <div v-else-if="item.status==='building'" class="card-text-status">
                        <a-spin v-if="item.loading" :spinning="item.loading" :indicator="indicator"></a-spin>
                        <span class="ml-2">构建中</span>
                      </div>
                      <div v-else-if="item.status==='draft'" class="card-text-status">
                        <img src="../icon/draft.png" style="width: 16px;height: 16px" />
                        <span class="ml-2">草稿</span>
                      </div>
                    </div>
                    <a-dropdown placement="bottomRight" :trigger="['click']">
                      <div class="ant-dropdown-link pointer operation" @click.prevent.stop>
                        <Icon icon="ant-design:ellipsis-outlined" size="16"></Icon>
                      </div>
                      <template #overlay>
                        <a-menu>
                          <a-menu-item key="vectorization" @click="handleVectorization(item.id)">
                            <Icon icon="ant-design:retweet-outlined" size="16"></Icon>
                            向量化
                          </a-menu-item>
                          <a-menu-item key="edit" @click="handleEdit(item)">
                            <Icon icon="ant-design:edit-outlined" size="16"></Icon>
                            编辑
                          </a-menu-item>
                          <a-menu-item key="delete" @click="handleDelete(item.id)">
                            <Icon icon="ant-design:delete-outlined" size="16"></Icon>
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
              v-if="knowledgeDocDataList.length > 0"
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
          </div>

          <div v-if="selectedKey === 'hitTest'" style="padding: 16px">
            <a-spin :spinning="spinning">
              <div class="hit-test">
                <h4>命中测试</h4>
                <span>针对用户提问调试段落匹配情况，保障回答效果。</span>
              </div>
              <div class="content">
                <div class="content-title">
                  <Avatar v-if="hitShowSearchText" :size="35" :src="avatar" />
                  <span>{{ hitShowSearchText }}</span>
                </div>
                <div class="content-card">
                  <a-row :span="24" class="knowledge-row" v-if="hitTextList.length>0">
                    <a-col :xxl="6" :xl="6" :lg="6" :md="6" :sm="12" :xs="24" v-for="item in hitTextList">
                      <a-card class="hit-card pointer" style="border-color: #ffffff" @click="hitTextDescClick(item)">
                        <div class="card-title">
                          <div style="display: flex;">
                            <Icon icon="ant-design:appstore-outlined" size="14"></Icon>
                            <span style="margin-left: 4px">Chunk-{{item.chunk}}</span>
                            <span style="margin-left: 10px">{{ item.content.length }} 字符</span>
                          </div>
                          <a-tag class="card-title-tag" color="#a9c8ff">
                            <span>{{ getTagTxt(item.score) }}</span>
                          </a-tag>
                        </div>
                        <div class="card-description">
                          {{ item.content }}
                        </div>
                        <div class="card-footer">
                          {{item.docName}}
                        </div>
                      </a-card>
                    </a-col>
                  </a-row>
                  <div v-else-if="notHit">
                    <a-empty :image-style="{ margin: '0 auto', height: '160px', verticalAlign: 'middle', borderStyle: 'none' }">
                      <template #description>
                        <div style="margin-top: 26px; font-size: 20px; color: #000; text-align: center !important">
                          没有命中的分段
                        </div>
                      </template>
                    </a-empty>
                  </div>
                </div>
              </div>
              <div class="param">
                <span style="font-weight: bold; font-size: 16px">参数配置</span>
                <ul>
                  <li>
                    <span>条数：</span>
                    <a-input-number :min="1" v-model:value="topNumber"></a-input-number>
                  </li>
                  <li>
                    <span>Score阈值：</span>
                    <a-input-number :min="0" :step="0.01" :max="1" v-model:value="similarity"></a-input-number>
                  </li>
                </ul>
              </div>
              <div class="hit-test-footer">
                <a-input v-model:value="hitText" size="large" placeholder="请输入" style="width: 100%" @pressEnter="hitTestClick">
                  <template #suffix>
                    <Icon icon="ant-design:send-outlined" style="transform: rotate(-33deg); cursor: pointer" size="22" @click="hitTestClick"></Icon>
                  </template>
                </a-input>
              </div>
            </a-spin>
          </div>
        </a-layout-content>
      </a-layout>
    </BasicModal>

    <!--  手工录入文本  -->
    <AiragKnowledgeDocTextModal @register="docTextRegister" @success="handleSuccess"></AiragKnowledgeDocTextModal>
    <!--  文本明细  -->
    <AiTextDescModal @register="docTextDescRegister"></AiTextDescModal>
  </div>
</template>

<script lang="ts">
  import { onBeforeMount, reactive, ref, unref, h } from 'vue';
  import BasicModal from '@/components/Modal/src/BasicModal.vue';
  import { useModal, useModalInner } from '@/components/Modal';
  import { knowledgeDocList, knowledgeDeleteBatchDoc, knowledgeRebuildDoc, knowledgeEmbeddingHitTest } from '../AiKnowledgeBase.api';
  import { ActionItem, BasicTable, TableAction } from '@/components/Table';
  import { useListPage } from '@/hooks/system/useListPage';
  import AiragKnowledgeDocTextModal from './AiragKnowledgeDocTextModal.vue';
  import AiTextDescModal from './AiTextDescModal.vue';
  import { useMessage } from '@/hooks/web/useMessage';
  import { LoadingOutlined } from '@ant-design/icons-vue';
  import {Avatar, message, Modal, Pagination} from 'ant-design-vue';
  import { useUserStore } from '@/store/modules/user';
  import { getFileAccessHttpUrl, getHeaders } from '@/utils/common/compUtils';
  import defaultImg from '/@/assets/images/header.jpg';
  import Icon from "@/components/Icon";
  import { useGlobSetting } from '/@/hooks/setting';

  export default {
    name: 'AiragKnowledgeDocListModal',
    components: {
      Icon,
      Pagination,
      Avatar,
      LoadingOutlined,
      TableAction,
      BasicTable,
      BasicModal,
      AiragKnowledgeDocTextModal,
      AiTextDescModal,
    },
    emits: ['success', 'register'],
    setup(props, { emit }) {
      //标题
      const title = ref<string>('知识库详情');

      //保存或修改
      const knowledgeId = ref<string>('');

      //菜单初始化选中的值
      const selectedKeys = ref(['document']);
      //菜单点击选中的key，用于显示和隐藏table
      const selectedKey = ref<string>('document');
      //定向搜索的文本
      const hitText = ref<string>('');
      //定向显示的文本
      const hitShowSearchText = ref<string>('');
      //加载效果
      const spinning = ref<boolean>(false);
      //最小分数 0-1
      const similarity = ref<number>(0.65);
      //条数
      const topNumber = ref<number>(5);
      //定向返回结果集
      const hitTextList = ref<any>([]);
      //用户头像
      const avatar = ref<any>('');
      const userStore = useUserStore();
      //文档列表
      const knowledgeDocDataList = ref<any>([]);
      //当前页数
      const pageNo = ref<number>(1);
      //每页条数
      const pageSize = ref<number>(10);
      //总条数
      const total = ref<number>(0);
      //可选择的页数
      const pageSizeOptions = ref<any>(['10', '20', '30']);
      //查询参数
      const searchText = ref<string>('');
      //是否没有命中
      const notHit = ref<boolean>(false);
      //定时任务刷新列表
      const timer = ref<any>(null);
      //token
      const headers = getHeaders();
      const globSetting = useGlobSetting();
      //上传路径
      const uploadUrl = ref<string>(globSetting.domainUrl+"/airag/knowledge/doc/import/zip");
      
      //菜单项
      const menuItems = ref<any>([
        {
          key: 'document',
          icon: '',
          label: '文档',
          title: '文档',
        },
        {
          key: 'hitTest',
          icon: '',
          label: '命中测试',
          title: '命中测试',
        },
      ]);
      //注册modal
      const [docTextRegister, { openModal: docTextOpenModal }] = useModal();
      const [docTextDescRegister, { openModal: docTextDescOpenModal }] = useModal();

      //注册modal
      const [registerModal, { closeModal, setModalProps }] = useModalInner(async (data) => {
        knowledgeId.value = data.id;
        selectedKeys.value = ['document'];
        selectedKey.value = 'document';
        spinning.value = false;
        notHit.value = false;
        await reload();
        setModalProps({ confirmLoading: false });
      });

      const contentStyle = {
        textAlign: 'center',
        height: '100%',
        width: '80%',
        background: '#ffffff',
      };

      const siderStyle = {
        textAlign: 'center',
        width: '20%',
        background: '#ffffff',
        borderRight: '1px solid #cecece',
      };

      /**
       * 加载指示符
       */
      const indicator =  h(LoadingOutlined, {
        style: {
          fontSize: '16px',
          marginRight: '2px'
        },
        spin: true,
      });
      
      const { createMessage } = useMessage();

      /**
       * 手工录入文本
       */
      function handleCreateText() {
        docTextOpenModal(true, { knowledgeId: knowledgeId.value, type: "text" });
      }

      /**
       * 文件上传
       */
      function handleCreateUpload() {
        docTextOpenModal(true, { knowledgeId: knowledgeId.value, type: "file" });
      }

      /**
       * web网络地址
       */
      function handleCreateWeb() {
        createMessage.warning('功能正在完善中....');
      }

      /**
       * 编辑
       */
      function handleEdit(record) {
        if (record.type === 'text' || record.type === 'file') {
          docTextOpenModal(true, {
            record,
            isUpdate: true,
          });
        }
      }

      /**
       * 删除
       * @param id
       */
      function handleDelete(id) {
        Modal.confirm({
          title: '提示',
          content: '确认要删除该文档吗?',
          okText: '确认',
          cancelText: '取消',
          onOk: () => {
            knowledgeDeleteBatchDoc({ ids: id }, reload);
          }
        })
      }

      /**
       * 向量化
       *
       * @param id
       */
      async function handleVectorization(id) {
        await knowledgeRebuildDoc({ docIds: id }, handleSuccess);
      }

      /**
       * 文档新增和编辑成功回调
       */
      function handleSuccess() {
        if(!timer.value){
          reload();
        }
        clearInterval(timer.value);
        timer.value = null;
        triggeringTimer();
      }

      /**
       * 触发定时任务
       */
      function triggeringTimer() {
        timer.value = setInterval(() => {
          reload();
        },5000)
      }

      /**
       * 菜单点击事件
       * @param value
       */
      function handleMenuClick(value) {
        if (value.key === 'document') {
          setTimeout(() => {
            pageNo.value = 1;
            pageSize.value = 10;
            searchText.value = "";
            
            reload();
          });
        } else {
          hitTextList.value = [];
          hitShowSearchText.value = '';
          hitText.value = '';
          avatar.value = '';
          similarity.value = 0.65;
          topNumber.value = 5;
        }
        selectedKey.value = value.key;
      }

      /**
       * 命中测试
       */
      function hitTestClick() {
        if (hitText.value) {
          spinning.value = true;
          knowledgeEmbeddingHitTest({
            queryText: hitText.value,
            knowId: knowledgeId.value,
            topNumber: topNumber.value,
            similarity: similarity.value,
          }).then((res) => {
            if (res.success) {
              if (res.result) {
                hitTextList.value = res.result;
              } else {
                hitTextList.value = [];
              }
            }
            hitShowSearchText.value = hitText.value;
            avatar.value = userStore.getUserInfo.avatar ? getFileAccessHttpUrl(userStore.getUserInfo.avatar) : defaultImg;
            hitText.value = '';
            notHit.value = hitTextList.value.length == 0;
            spinning.value = false;
          }).catch(()=>{
            spinning.value = false;
          });
        }
      }

      /**
       * 获取文本
       * @param value
       */
      function getTagTxt(value) {
        return 'score' + ' ' + value.toFixed(2);
      }

      /**
       * 命中测试卡点击事件
       * @param values
       */
      function hitTextDescClick(values) {
        docTextDescOpenModal(true, { ...values });
      }

      /**
       * 加载表格
       */
      async function reload() {
        let params = {
          pageNo: pageNo.value,
          pageSize: pageSize.value,
          knowledgeId: knowledgeId.value,
          title: '*' + searchText.value + '*',
          column: 'createTime',
          order: 'desc'
        };
        await knowledgeDocList(params).then((res) => {
          if (res.success) {
            //update-begin---author:wangshuai---date:2025-03-21---for:【QQYUN-11636】向量化功能改成异步---
            if(res.result.records){
              let clearTimer = true;
              for (const item of res.result.records) {
                if(item.status && item.status === 'building' ){
                  clearTimer = false;
                  item.loading = true;
                }else{
                  item.loading = false;
                }
              }
              if(clearTimer){
                clearInterval(timer.value);
              }
            }
            //update-end---author:wangshuai---date:2025-03-21---for:【QQYUN-11636】向量化功能改成异步---
            knowledgeDocDataList.value = res.result.records;
            total.value = res.result.total;
          } else {
            knowledgeDocDataList.value = [];
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
       * 获取文件后缀
       */
      function getFileSuffix(metadata) {
        if(metadata){
          let filePath = JSON.parse(metadata).filePath;
          const index = filePath.lastIndexOf('.');
          return index > 0 ? filePath.substring(index + 1).toLowerCase() : '';
        }
        return '';
      }

      /**
       * 上传前事件
       */
      function beforeUpload(file) {
        let fileType = file.type;
        if (fileType !== 'application/x-zip-compressed') {
            createMessage.warning('请上传zip文件');
            return false;
        }
        return true;
      }

      /**
       * 文件上传回调事件
       * @param info
       */
      function handleUploadChange(info) {
        let { file } = info;
        if (file.status === 'error') {
          createMessage.error(file.response.message ||`${file.name} 上传失败.`);
        }
        if (file.status === 'done') {
          if(!file.response.success){
            createMessage.warning(file.response.message);
            return;
          }
          createMessage.success(file.response.message);
          handleSuccess();
        }
      }

      onBeforeMount(()=>{
        clearInterval(timer.value);
        timer.value = null;
      })
      
      return {
        registerModal,
        title,
        docTextRegister,
        handleCreateText,
        beforeUpload,
        handleCreateUpload,
        handleSuccess,
        contentStyle,
        siderStyle,
        selectedKeys,
        menuItems,
        handleMenuClick,
        selectedKey,
        hitTestClick,
        hitText,
        spinning,
        similarity,
        topNumber,
        hitShowSearchText,
        avatar,
        hitTextList,
        getTagTxt,
        docTextDescRegister,
        hitTextDescClick,
        knowledgeDocDataList,
        handleEdit,
        handleDelete,
        handleVectorization,
        pageNo,
        pageSize,
        pageSizeOptions,
        total,
        handlePageChange,
        searchText,
        reload,
        cardBodyStyle:{ textAlign: 'left', width: '100%' },
        getFileSuffix,
        notHit,
        indicator,
        headers,
        uploadUrl,
        handleUploadChange,
        knowledgeId,
      };
    },
  };
</script>

<style scoped lang="less">
  .pointer {
    cursor: pointer;
  }

  .hit-test {
    box-sizing: border-box;
    flex-wrap: wrap;
    text-align: left;
    display: flex;
    margin-bottom: 10px;

    h4 {
      font-weight: bold;
      font-size: 16px;
      align-self: center;
      margin-bottom: 0;
      color: #1f2329;
    }

    span {
      margin-left: 10px;
      color: #8f959e;
      font-weight: 400;
      align-self: center;
      margin-top: 2px;
    }
  }

  .hit-test-footer {
    margin-top: 10px;
    display: flex;
  }
  .param {
    text-align: left;
    margin-top: 10px;
    ul {
      margin-top: 10px;
      display: flex;
      li {
        align-items: center;
        margin-right: 10px;
        display: flex;
      }
    }
    border-bottom: 1px solid #cec6c6;
  }
  .content {
    height: calc(100vh - 300px);
    padding: 8px;
    border-radius: 10px;
    background-color: #f9fbfd;
    overflow-y: auto;
    .content-title {
      font-size: 16px;
      font-weight: 600;
      text-align: left;
      margin-left: 10px;
      display: flex;
      span {
        margin-left: 4px;
        font-size: 20px;
        align-self: center;
      }
    }
    .content-card {
      margin-top: 20px;
      margin-left: 10px;
      .hit-card {
        height: 160px;
        margin-bottom: 10px;
        margin-right: 10px;
        border-radius: 10px;
        background: #fcfcfd;
        border: 1px solid #f0f0f0;
        box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        transition: all 0.3s ease;
        .card-title {
          justify-content: space-between;
          color: #887a8b;
          font-size: 14px;
          display: flex;
        }
      }
    }
  }
  .hit-card:hover {
    box-shadow: 0 6px 12px rgba(0,0,0,0.15) !important;
  }
  .pointer {
    cursor: pointer;
  }
  
  .card-description {
    display: -webkit-box;
    -webkit-box-orient: vertical;
    -webkit-line-clamp: 4;
    height: 6em;
    overflow: hidden;
    text-overflow: ellipsis;
    line-height: 1.5;
    margin-top: 16px;
    text-align: left;
    font-size: 12px;
    color: #676F83;
  }
  
  .card-title-tag {
    color: #477dee;
  }
  
  .knowledge-row {
    padding: 16px;
    overflow-y: auto;
  }
  
  .add-knowledge-card {
    border-radius: 10px;
    margin-bottom: 20px;
    display: inline-flex;
    font-size: 16px;
    height: 166px;
    width: calc(100% - 20px);
    background: #fcfcfd;
    border: 1px solid #f0f0f0;
    box-shadow: 0 2px 4px rgba(0,0,0,0.1);
    transition: all 0.3s ease;
    .add-knowledge-card-icon {
      padding: 8px;
      margin-right: 12px;
    }
  }
  
  .knowledge-card {
    border-radius: 10px;
    margin-right: 20px;
    margin-bottom: 20px;
    height: 166px;
    background: #fcfcfd;
    border: 1px solid #f0f0f0;
    box-shadow: 0 2px 4px rgba(0,0,0,0.1);
    transition: all 0.3s ease;
    .knowledge-header {
      position: relative;
      font-size: 14px;
      height: 20px;
      text-align: left;
      .header-img {
        width: 40px;
        height: 40px;
        margin-right: 12px;
      }
      .header-title{
        font-weight: bold;
        color: #354052;
        margin-left: 4px;
        align-self: center;
      }
      
      .header-text {
        overflow: hidden;
        position: relative;
        display: flex;
        font-size: 16px;
      }
    }
  }

  .add-knowledge-card,.knowledge-card{
    transition: box-shadow 0.3s ease;
  }

  .add-knowledge-card:hover,.knowledge-card:hover{
    box-shadow: 0 6px 12px rgba(0,0,0,0.15);
  }
  
  .ellipsis {
    text-overflow: ellipsis;
    overflow: hidden;
    text-wrap: nowrap;
    width: calc(100% - 30px);
  }
  
  :deep(.ant-card .ant-card-body) {
    padding: 16px;
  }
  
  .card-text{
    font-size: 12px;
    display: flex;
    margin-top: 10px;
    align-items: center;
  }
  
  .search-title{
    width: 200px;
    margin-top: 10px;
    display: block;
    margin-left: 20px;
  }
  
  .operation{
    border: none;
    margin-top: 10px;
    align-items: end;
    display: none !important;
    bottom: 8px;
    right: 4px;
    position: absolute;
  }
  
  .knowledge-card:hover{
    .operation{
      display: block !important;
    }
  }
  
  .add-knowledge-doc{
    margin-top: 6px;
    color:#6F6F83;
    font-size: 13px;
    width: 100%;
    cursor: pointer;
    display: flex;
    span{
      margin-left: 4px;
      line-height: 28px;
    }
  }
  .add-knowledge-doc:hover{
    background: #c8ceda33;
  }
  .operation{
    background-color: unset;
    border: none;
    margin-right: 2px;
  }
  .operation:hover{
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
  .card-footer{
    margin-top: 4px;
    font-weight: 400;
    color: #1f2329;
    text-align: left;
    font-size: 12px;
  }
  .card-text-status{
    display: flex;
    align-items: center;
  }
  .ml-2{
    margin-left: 2px;
  }
</style>
<style lang="less">
  .airag-knowledge-doc .scroll-container {
    padding: 0 !important;
  }
</style>
