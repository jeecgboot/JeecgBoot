<template>
  <BasicModal
    @register="register"
    :getContainer="getContainer"
    :canFullscreen="false"
    destroyOnClose
    title="会签人员选择"
    :width="600"
    wrapClassName="j-filtered-user-select-modal"
  >
    <!-- 节点说明 -->
    <a-select 
      value="系统角色会签，请筛选参与人员" 
      disabled 
      style="width: 100%" 
      class="node-label-select"
    >
      <a-select-option value="系统角色会签，请筛选参与人员">
        系统角色会签，请筛选参与人员
      </a-select-option>
    </a-select>

    <div class="modal-content">
      <!-- 搜索框 -->
      <div :class="['search-wrapper', { 'search-expanded': searchInputStatus }]">
        <span v-show="!searchInputStatus" class="search-icon" @click="showSearchInput">
          <SearchOutlined />
        </span>
        <div v-show="searchInputStatus" class="search-input">
          <a-input 
            v-model:value="searchText" 
            placeholder="请输入用户名按回车搜索"
            @pressEnter="onSearchUser"
          >
            <template #prefix>
              <SearchOutlined />
            </template>
            <template #suffix>
              <CloseOutlined title="退出搜索" @click="clearSearch" />
            </template>
          </a-input>
        </div>
      </div>

      <!-- 用户列表 -->
      <div class="user-list-wrapper">
        <user-list 
          :multi="multi" 
          :dataList="userDataList" 
          :selectedIdList="selectedIdList" 
          @selected="onSelectUser" 
          @unSelect="unSelectUser" 
        />
      </div>

      <!-- 已选用户标签 -->
      <div v-if="selectedUserList.length > 0" class="selected-users">
        <SelectedUserItem 
          v-for="item in selectedUserList" 
          :key="item.id" 
          :info="item" 
          @unSelect="unSelectUser" 
        />
      </div>
    </div>

    <template #footer>
      <div class="modal-footer">
        <div class="pagination-wrapper">
          <a-pagination
            v-model:current="pageNo"
            size="small"
            :total="totalRecord"
            :pageSize="PAGE_SIZE"
            show-quick-jumper
            @change="onPageChange"
          />
        </div>
        <a-button 
          type="primary" 
          :disabled="!hasSelectedUser" 
          @click="handleOk"
        >
          确认提交
        </a-button>
      </div>
    </template>
  </BasicModal>
</template>

<script lang="ts">
  import { defineComponent, computed, ref, toRaw, PropType } from 'vue';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { SearchOutlined, CloseOutlined } from '@ant-design/icons-vue';
  import { Pagination } from 'ant-design-vue';
  import { defHttp } from '/@/utils/http/axios';
  import UserList from './UserList.vue';
  import SelectedUserItem from './SelectedUserItem.vue';

  // 用户数据接口
  interface UserInfo {
    id: string;
    username: string;
    realname: string;
    [key: string]: any;
  }

  // 弹窗打开参数接口
  interface ModalData {
    usernames?: string[];
    list?: UserInfo[];
  }

  // 常量定义
  const PAGE_SIZE = 10;
  const API_URL = '/sys/user/selectUserList';

  export default defineComponent({
    name: 'FilteredUserSelectModal',
    components: {
      BasicModal,
      SearchOutlined,
      CloseOutlined,
      SelectedUserItem,
      UserList,
      APagination: Pagination,
    },
    props: {
      // 是否多选
      multi: {
        type: Boolean,
        default: true,
      },
      // 容器函数
      getContainer: {
        type: Function as PropType<() => HTMLElement>,
        default: undefined,
      },
      // 传入的用户账号列表（用于过滤）
      usernames: {
        type: Array as PropType<string[]>,
        default: () => [],
      },
    },
    emits: ['selected', 'register'],
    setup(props, { emit }) {
      // ==================== 状态定义 ====================
      const selectedUserList = ref<UserInfo[]>([]);
      const actualUsernames = ref<string[]>([]);
      const searchInputStatus = ref(false);
      const searchText = ref('');
      const pageNo = ref(1);
      const totalRecord = ref(0);
      const userDataList = ref<UserInfo[]>([]);

      // ==================== 计算属性 ====================
      const selectedIdList = computed(() => 
        selectedUserList.value.map((item) => item.id)
      );

      const hasSelectedUser = computed(() => 
        selectedUserList.value.length > 0
      );

      // ==================== 弹窗事件 ====================
      const [register] = useModalInner((data: ModalData) => {
        initUsernames(data);
        initSelectedUsers(data);
        resetSearchState();
        loadUserList();
      });

      /**
       * 初始化用户名列表
       */
      function initUsernames(data: ModalData) {
        if (data?.usernames?.length) {
          actualUsernames.value = [...data.usernames];
        } else if (props.usernames?.length) {
          actualUsernames.value = [...props.usernames];
        } else {
          actualUsernames.value = [];
        }
      }

      /**
       * 初始化已选用户
       */
      function initSelectedUsers(data: ModalData) {
        selectedUserList.value = data?.list?.length ? [...data.list] : [];
      }

      /**
       * 重置搜索状态
       */
      function resetSearchState() {
        searchText.value = '';
        searchInputStatus.value = false;
        pageNo.value = 1;
      }

      // ==================== 用户操作 ====================
      /**
       * 确认选择
       */
      function handleOk() {
        if (!hasSelectedUser.value) return;
        emit('selected', toRaw(selectedUserList.value));
      }

      /**
       * 选中用户
       */
      function onSelectUser(info: UserInfo) {
        if (!props.multi) {
          selectedUserList.value = [{ ...info }];
          return;
        }
        
        if (!selectedIdList.value.includes(info.id)) {
          selectedUserList.value.push({ ...info });
        }
      }

      /**
       * 取消选中用户
       */
      function unSelectUser(id: string) {
        const index = selectedUserList.value.findIndex((item) => item.id === id);
        if (index >= 0) {
          selectedUserList.value.splice(index, 1);
        }
      }

      // ==================== 搜索功能 ====================
      /**
       * 显示搜索框
       */
      function showSearchInput(e?: Event) {
        e?.preventDefault();
        e?.stopPropagation();
        searchInputStatus.value = true;
      }

      /**
       * 执行搜索
       */
      function onSearchUser() {
        pageNo.value = 1;
        loadUserList();
      }

      /**
       * 清除搜索
       */
      function clearSearch(e?: Event) {
        e?.preventDefault();
        e?.stopPropagation();
        searchText.value = '';
        searchInputStatus.value = false;
        pageNo.value = 1;
        loadUserList();
      }

      // ==================== 分页功能 ====================
      /**
       * 翻页处理
       */
      function onPageChange() {
        loadUserList();
      }

      // ==================== 数据加载 ====================
      /**
       * 加载用户列表
       */
      async function loadUserList() {
        if (!actualUsernames.value?.length) {
          userDataList.value = [];
          totalRecord.value = 0;
          return;
        }

        try {
          const params: Record<string, any> = {
            pageNo: pageNo.value,
            pageSize: PAGE_SIZE,
            includeUsernameList: actualUsernames.value.join(','),
          };
          
          if (searchText.value.trim()) {
            params.keyword = searchText.value.trim();
          }

          const { success, result } = await defHttp.get(
            { url: API_URL, params }, 
            { isTransformResponse: false }
          );
          
          if (success && result) {
            userDataList.value = result.records || [];
            totalRecord.value = result.total || 0;
          } else {
            resetUserData();
          }
        } catch (error) {
          console.error('加载用户列表失败:', error);
          resetUserData();
        }
      }

      /**
       * 重置用户数据
       */
      function resetUserData() {
        userDataList.value = [];
        totalRecord.value = 0;
      }

      // ==================== 返回值 ====================
      return {
        PAGE_SIZE,
        register,
        handleOk,
        searchText,
        searchInputStatus,
        showSearchInput,
        onSearchUser,
        clearSearch,
        pageNo,
        totalRecord,
        onPageChange,
        userDataList,
        selectedUserList,
        selectedIdList,
        hasSelectedUser,
        onSelectUser,
        unSelectUser,
      };
    },
  });
</script>

<style lang="less" scoped>
  .j-filtered-user-select-modal {
    // 节点说明标签
    .node-label-select {
      margin-bottom: 10px;
      
      :deep(.ant-select-selector) {
        color: #fff !important;
        background-color: #409eff !important;
        border-radius: 5px !important;
        cursor: not-allowed !important;
      }
      
      :deep(.ant-select-selection-item),
      :deep(.ant-select-arrow) {
        color: #fff !important;
      }
    }

    // 弹窗内容区
    .modal-content {
      position: relative;
      min-height: 350px;
    }

    // 搜索框容器
    .search-wrapper {
      position: absolute;
      top: 14px;
      z-index: 1;
      
      &.search-expanded {
        width: 100%;
      }

      .search-icon {
        margin-left: 10px;
        color: #c0c0c0;
        cursor: pointer;
        transition: color 0.3s;
        
        &:hover {
          color: #0a8fe9;
        }
      }

      .search-input {
        width: 100%;
        
        :deep(.anticon) {
          color: #c0c0c0;
          cursor: pointer;
          transition: color 0.3s;
          
          &:hover {
            color: #0a8fe9;
          }
        }
      }
    }

    // 用户列表
    .user-list-wrapper {
      padding-top: 50px;
    }

    // 已选用户标签
    .selected-users {
      display: flex;
      flex-wrap: wrap;
      gap: 8px;
      padding-top: 15px;
      width: 100%;
      overflow-x: hidden;
    }

    // 底部容器
    .modal-footer {
      display: flex;
      justify-content: space-between;
      align-items: center;
      width: 100%;
      gap: 16px;
      
      .pagination-wrapper {
        flex: 1;
        min-width: 0;
        
        :deep(.ant-pagination) {
          display: flex;
          justify-content: flex-start;
        }
      }
      
      .ant-btn {
        flex-shrink: 0;
      }
    }

    // 滚动容器优化
    :deep(.scroll-container) {
      padding-bottom: 0 !important;
    }
  }
</style>
