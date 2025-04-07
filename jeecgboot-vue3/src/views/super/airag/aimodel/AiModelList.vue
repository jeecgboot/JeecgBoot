<template>
  <div class="model">
    <!--查询区域-->
    <div class="jeecg-basic-table-form-container">
      <a-form ref="formRef" @keyup.enter.native="searchQuery" :model="queryParam" :label-col="labelCol" :wrapper-col="wrapperCol" style="background-color: #f7f8fc !important;">
        <a-row :gutter="24">
          <a-col :lg="6">
            <a-form-item name="name" label="模板名称">
              <JInput v-model:value="queryParam.name" />
            </a-form-item>
          </a-col>
          <a-col :lg="6">
            <a-form-item name="modelType" label="模板类型">
              <JDictSelectTag v-model:value="queryParam.modelType" dict-code="model_type" />
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
    <a-row :span="24" class="model-row">
      <a-col :xxl="4" :xl="6" :lg="6" :md="6" :sm="12" :xs="24">
        <a-card class="add-knowledge-card" @click="handleAdd">
          <div class="flex">
            <Icon icon="ant-design:plus-outlined" class="add-knowledge-card-icon" size="20"></Icon>
            <span class="add-knowledge-card-title">添加模型</span>
          </div>
        </a-card>
      </a-col>
      <a-col :xxl="4" :xl="6" :lg="6" :md="6" :sm="12" :xs="24" v-for="item in modalList" v-if="modalList && modalList.length>0">
        <a-card class="model-card" @click="handleEditClick(item)">
          <div class="model-header">
            <div class="flex">
              <img :src="getImage(item.provider)" class="header-img" />
              <div class="header-text">{{ item.name }}</div>
            </div>
          </div>
          <div class="mt-6">
            <ul>
              <li class="flex mr-14">
                <span class="label">模型类型</span>
                <span class="described">{{ item.modelType_dictText }}</span>
              </li>
              <li class="flex mr-14 mt-6">
                <span class="label">基础模型</span>
                <span class="described">{{ item.modelName }}</span>
              </li>
              <li class="flex mr-14 mt-6">
                <span class="label">创建者</span>
                <span class="described">{{ item.createBy }}</span>
              </li>
            </ul>
          </div>
          <div class="model-btn">
            <a-button class="model-icon" size="small" @click.prevent.stop="handleEditClick(item)">
              <Icon icon="ant-design:edit-outlined"></Icon>
            </a-button>
            <a-dropdown placement="bottomRight" :trigger="['click']" :getPopupContainer="(node) => node.parentNode">
              <div class="ant-dropdown-link pointer model-icon" @click.prevent.stop>
                <Icon icon="ant-design:ellipsis-outlined"></Icon>
              </div>
              <template #overlay>
                <a-menu>
                  <!--<a-menu-item key="param" @click="handleParamClick(item.id)">
                    <Icon icon="ant-design:setting-outlined" size="16"></Icon>
                    <span class="ml-4">模型参数配置</span>
                  </a-menu-item>-->
                  <a-menu-item key="delete" @click.prevent.stop="handleDeleteClick(item)">
                    <Icon icon="ant-design:delete-outlined" size="16"></Icon> 删除
                  </a-menu-item>
                </a-menu>
              </template>
            </a-dropdown>
          </div>
        </a-card>
      </a-col>
    </a-row>
    <Pagination
      v-if="modalList.length > 0"
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

  <!--添加模型弹窗-->
  <AiModelModal @register="registerModal" @success="reload"></AiModelModal>
</template>

<script lang="ts">
  import { reactive, ref } from 'vue';
  import AiModelModal from './components/AiModelModal.vue';
  import { useModal } from '@/components/Modal';
  import { deleteModel, list } from './model.api';
  import { imageList } from './model.data';
  import { Pagination } from 'ant-design-vue';
  import JInput from '@/components/Form/src/jeecg/components/JInput.vue';
  import JSelectUser from '@/components/Form/src/jeecg/components/JSelectUser.vue';
  import JDictSelectTag from '@/components/Form/src/jeecg/components/JDictSelectTag.vue';

  export default {
    name: 'ModelList',
    components: {
      JDictSelectTag,
      JSelectUser,
      JInput,
      AiModelModal,
      Pagination,
    },
    setup() {
      //模型列表
      const modalList = ref([]);

      const [registerModal, { openModal }] = useModal();

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

      //页面初始化执行列表查询
      reload();

      /**
       * 新增
       */
      async function handleAdd() {
        openModal(true, {});
      }

      /**
       * 编辑
       *
       * @param item
       */
      function handleEditClick(item) {
        openModal(true, {
          id: item.id,
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
            modalList.value = res.result.records;
            total.value = res.result.total;
          } else {
            modalList.value = [];
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
       * 获取图片
       * @param name
       */
      const getImage = (name) => {
        return imageList.value[name];
      };

      /**
       * 删除模型
       * @param item
       */
      async function handleDeleteClick(item) {
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
      function handleParamClick(id) {}

      return {
        handleAdd,
        handleEditClick,
        registerModal,
        modalList,
        reload,
        pageNo,
        pageSize,
        pageSizeOptions,
        total,
        handlePageChange,
        getImage,
        handleDeleteClick,
        searchQuery,
        searchReset,
        queryParam,
        labelCol,
        wrapperCol,
        formRef,
        handleParamClick,
      };
    },
  };
</script>

<style scoped lang="less">
  .model {
    height: calc(100vh - 115px);
    background: #f7f8fc;
    padding: 24px;
    overflow-y: auto;
    .model-row {
      max-height: calc(100% - 100px);
      margin-top: 20px;
      overflow-y: auto;
      .model-header {
        position: relative;
        font-size: 14px;
        .header-img {
          width: 32px;
          height: 32px;
          margin-right: 12px;
        }
        .header-text {
          font-size: 16px;
          font-weight: bold;
          color: #354052;
          width: calc(100% - 80px);
          overflow: hidden;
          align-content: center;
          text-overflow: ellipsis;
          white-space: nowrap;
        }
      }
    }
  }

  .label {
    font-weight: 400;
    font-size: 12px;
    align-self: center;
    color: #8a8f98;
    overflow-wrap: break-word;
  }
  .described {
    font-weight: 400;
    margin-left: 14px;
    display: inline-block;
    text-overflow: ellipsis;
    white-space: nowrap;
    overflow: hidden;
    font-size: 12px;
  }

  .flex {
    display: flex;
  }

  :deep(.ant-card .ant-card-body) {
    padding: 16px;
  }

  .mr-14 {
    margin-right: 14px;
  }
  .mt-6 {
    margin-top: 6px;
  }

  .ml-4 {
    margin-left: 4px;
  }

  .model-btn {
    position: absolute;
    right: 4px;
    top: 6px;
    height: auto;
    display: none;
  }
  .model-card {
    margin-right: 20px;
    margin-bottom: 20px;
    background: #fcfcfd;
    border: 1px solid #f0f0f0;
    box-shadow: 0 2px 4px rgba(0,0,0,0.1);
    transition: all 0.3s ease;
    border-radius: 10px;
    height: 152px;
    cursor: pointer;
  }
  .model-card:hover {
    box-shadow: 0 6px 12px rgba(0,0,0,0.15);
    .model-btn {
      display: flex;
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
</style>
