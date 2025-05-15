<template>
  <BasicModal
    wrapClassName="JSelectUserByDepartmentModal"
    v-bind="$attrs"
    @register="register"
    :title="modalTitle"
    width="800px"
    @ok="handleOk"
    destroyOnClose
    @visible-change="visibleChange"
  >
    <div class="j-select-user-by-dept">
      <div class="modal-content">
        <!-- 左侧搜索和组织列表 -->
        <div class="left-content">
          <!-- 搜索框 -->
          <div class="search-box">
            <a-input v-model:value.trim="searchText" placeholder="搜索" @change="handleSearch" @pressEnter="handleSearch" allowClear />
          </div>
          <!-- 组织架构 -->
          <div class="tree-box">
            <template v-if="searchText.length">
              <template v-if="searchResult.depart.length || searchResult.user.length">
                <div class="search-result">
                  <template v-if="searchResult.user.length">
                    <div class="search-user">
                      <p class="search-user-title">人员</p>
                      <template v-for="item in searchResult.user" :key="item.id">
                        <div class="search-user-item" @click="handleSearchUserCheck(item)">
                          <a-checkbox v-model:checked="item.checked" />
                          <div class="right">
                            <div class="search-user-item-circle">
                              <img v-if="item.avatar" :src="getFileAccessHttpUrl(item.avatar)" alt="avatar" />
                            </div>
                            <div class="search-user-item-info">
                              <div class="search-user-item-name">{{ item.realname }}</div>
                              <div class="search-user-item-org">{{ item.orgCodeTxt }}</div>
                            </div>
                          </div>
                        </div>
                      </template>
                    </div>
                  </template>
                  <template v-if="searchResult.depart.length">
                    <div class="search-depart">
                      <p class="search-depart-title">部门</p>
                      <template v-for="item in searchResult.depart" :key="item.id">
                        <div class="search-depart-item" @click="handleSearchDepartClick(item)">
                          <a-checkbox v-model:checked="item.checked" @click.stop @change="($event) => handleSearchDepartCheck($event, item)" />
                          <div class="search-depart-item-name">{{ item.departName }}</div>
                          <RightOutlined />
                        </div>
                      </template>
                    </div>
                  </template>
                </div>
              </template>
              <template v-else>
                <div class="no-data">
                  <a-empty description="暂无数据" />
                </div>
              </template>
            </template>
            <template v-else>
              <a-breadcrumb v-if="breadcrumb.length">
                <a-breadcrumb-item @click="handleBreadcrumbClick()">
                  <HomeOutlined />
                </a-breadcrumb-item>
                <template v-for="item in breadcrumb" :key="item?.id">
                  <a-breadcrumb-item @click="handleBreadcrumbClick(item)">
                    <span>{{ item.departName }}</span>
                  </a-breadcrumb-item>
                </template>
              </a-breadcrumb>
              <div v-if="currentDepartUsers.length">
                <!-- 当前部门用户树 -->
                <div class="depart-users-tree">
                  <div v-if="!currentDepartTree.length" class="allChecked">
                    <a-checkbox v-model:checked="currentDepartAllUsers" @change="handleAllUsers">全选</a-checkbox>
                  </div>
                  <template v-for="item in currentDepartUsers" :key="item.id">
                    <div class="depart-users-tree-item" @click="handleDepartUsersTreeCheck(item)">
                      <a-checkbox v-model:checked="item.checked" />
                      <div class="right">
                        <div class="depart-users-tree-item-circle">
                          <img v-if="item.avatar" :src="getFileAccessHttpUrl(item.avatar)" alt="avatar" />
                        </div>
                        <div class="depart-users-tree-item-name">{{ item.realname }}</div>
                      </div>
                    </div>
                  </template>
                </div>
              </div>
              <!-- 部门树 -->
              <div v-if="currentDepartTree.length" class="depart-tree">
                <template v-for="item in currentDepartTree" :key="item.id">
                  <div class="depart-tree-item" @click="handleDepartTreeClick(item)">
                    <a-checkbox v-model:checked="item.checked" @click.stop @change="($event) => handleDepartTreeCheck($event, item)" />
                    <div class="depart-tree-item-name">{{ item.departName }}</div>
                    <RightOutlined />
                  </div>
                </template>
              </div>
              <div v-if="currentDepartTree.length === 0 && currentDepartUsers.length === 0" class="no-data">
                <a-empty description="暂无数据" />
              </div>
            </template>
          </div>
        </div>
        <!-- 右侧已选人员展示 -->
        <div class="right-content">
          <div class="selected-header"> 已选人员：{{ selectedUsers.length }}人 </div>
          <div class="selected-users">
            <div class="content">
              <div v-for="user in selectedUsers" :key="user.id" class="user-avatar" @click="handleDelUser(user)">
                <div class="avatar-circle">
                  <img v-if="user.avatar" :src="getFileAccessHttpUrl(user.avatar)" alt="avatar" />
                  <div class="mask">
                    <CloseOutlined></CloseOutlined>
                  </div>
                </div>
                <div class="user-name">{{ user.realname }}</div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </BasicModal>
</template>

<script setup lang="ts">
  import { ref, reactive } from 'vue';
  import { RightOutlined, HomeOutlined, CloseOutlined } from '@ant-design/icons-vue';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { queryTreeList, getTableList as getTableListOrigin } from '/@/api/common/api';
  import { getFileAccessHttpUrl } from '/@/utils/common/compUtils';
  import { isArray } from '/@/utils/is';
  import { defHttp } from '/@/utils/http/axios';

  defineOptions({ name: 'JSelectUserByDepartmentModal' });
  const props = defineProps({
    // ...selectProps,
    //回传value字段名
    rowKey: {
      type: String,
      default: 'id',
    },
    //回传文本字段名
    labelKey: {
      type: String,
      default: 'name',
    },
    modalTitle: {
      type: String,
      default: '部门用户选择',
    },
    selectedUser: {
      type: Array,
      default: () => [],
    },
    //查询参数
    params: {
      type: Object,
      default: () => {},
    },
    //最大选择数量
    maxSelectCount: {
      type: Number,
      default: 0,
    },
    // 是否单选
    isRadioSelection: {
      type: Boolean,
      default: false,
    },
  });
  const emit = defineEmits(['close', 'register', 'change']);
  import { useMessage } from '/@/hooks/web/useMessage';
  const { createMessage } = useMessage();
  // 搜索文本
  const searchText = ref('');
  const breadcrumb = ref<any[]>([]);
  // 部门树(整颗树)
  const departTree = ref([]);
  // 当前部门树
  const currentDepartTree = ref<any[]>([]);
  // 选中的部门节点
  const checkedDepartIds = ref<string[]>([]);
  // 当前部门用户
  const currentDepartUsers = ref([]);
  // 已选用户
  const selectedUsers = ref<any[]>([]);
  // 全选
  const currentDepartAllUsers = ref(false);
  // 搜索结构
  const searchResult: any = reactive({
    depart: [],
    user: [],
  });
  // 映射部门和人员的关系
  const cacheDepartUser = {};
  //注册弹框
  const [register, { closeModal }] = useModalInner(async (data) => {
    // 初始化
    if (props.selectedUser.length) {
      // 编辑时，传进来已选中的数据
      selectedUsers.value = props.selectedUser;
    }
    getQueryTreeList();
  });
  const visibleChange = (visible) => {
    if (visible === false) {
      setTimeout(() => {
        emit('close');
      }, 300);
    }
  };
  const handleOk = () => {
    if (selectedUsers.value.length == 0) {
      createMessage.warning('请选择人员');
      return;
    }
    if (props.isRadioSelection && selectedUsers.value.length > 1) {
      createMessage.warning('只允许选择一个用户');
      return;
    }
    if (props.maxSelectCount && selectedUsers.value.length > props.maxSelectCount) {
      createMessage.warning(`最多只能选择${props.maxSelectCount}个用户`);
      return;
    }
    emit('change', selectedUsers.value);
    closeModal();
  };
  // 搜索人员/部门
  const handleSearch = () => {
    if (searchText.value) {
      defHttp
        .get({
          url: `/sys/user/listAll`,
          params: {
            column: 'createTime',
            order: 'desc',
            pageNo: 1,
            pageSize: 100,
            realname: `*${searchText.value}*`,
          },
        })
        .then((result: any) => {
          result.records?.forEach((item) => {
            const findItem = selectedUsers.value.find((user) => user.id == item.id);
            if (findItem) {
              // 能在右侧找到说明选中了，左侧同样需要选中。
              item.checked = true;
            } else {
              item.checked = false;
            }
          });
          searchResult.user = result.records ?? [];
        });
      searchResult.depart = getDepartByName(searchText.value) ?? [];
    } else {
      searchResult.user = [];
      searchResult.depart = [];
    }
  };
  // 面包屑
  const handleBreadcrumbClick = (item?) => {
    // 先清空
    currentDepartUsers.value = [];
    if (item) {
      const findIndex = breadcrumb.value.findIndex((o) => o.id === item.id);
      if (findIndex != -1) {
        breadcrumb.value = breadcrumb.value.filter((item, index) => {
          console.log(item);
          return index <= findIndex;
        });
      }
      const data = getDepartTreeNodeById(item.id, departTree.value);
      currentDepartTree.value = data.children;
    } else {
      // 根节点
      currentDepartTree.value = departTree.value;
      breadcrumb.value = [];
    }
  };
  // 点击部门树复选框触发
  const handleDepartTreeCheck = (e, item) => {
    const { target } = e;
    if (target.checked) {
      // 选中
      getUsersByDeptId(item['id']).then((users) => {
        addUsers(users);
      });
      checkedDepartIds.value.push((item as any).id);
      // 检查父节点下所有子节点是否选中
      const parentItem = getDepartTreeParentById(item.id);
      if (parentItem?.children) {
        const isChildAllChecked = parentItem.children.every((item) => item.checked);
        if (isChildAllChecked) {
          parentItem.checked = true;
        } else {
          parentItem.checked = false;
        }
      }
    } else {
      // 取消选中
      const findIndex = checkedDepartIds.value.findIndex((o: any) => o.id === item.id);
      if (findIndex != -1) {
        checkedDepartIds.value.splice(findIndex, 1);
      }
      // 如果父节点是选中，则需要取消
      const parentItem = getDepartTreeParentById(item.id);
      if (parentItem) {
        parentItem.checked = false;
      }
      getUsersByDeptId(item['id']).then((users) => {
        users.forEach((item) => {
          const findIndex = selectedUsers.value.findIndex((user) => user.id === item.id);
          if (findIndex != -1) {
            selectedUsers.value.splice(findIndex, 1);
          }
        });
      });
    }
  };
  // 点击部门树节点触发
  const handleDepartTreeClick = (item) => {
    breadcrumb.value = [...breadcrumb.value, item];
    if (item.children) {
      // 有子节点，则显示部门
      if (item.checked) {
        // 父节点勾选，则子节点全部勾选
        item.children.forEach((item) => {
          item.checked = true;
        });
      }
      currentDepartTree.value = item.children;
      defHttp
        .get({
          url: '/sys/sysDepart/getUsersByDepartId',
          params: {
            id: item['id'],
          },
        })
        .then((res: any) => {
          const result = res ?? [];
          if (item.checked) {
            // 父节点勾选，则默认勾选
            result.forEach((item) => {
              item.checked = true;
            });
          }
          // 右侧勾选了，则默认勾选（用户存在多部门，在别的部门被选中了）
          if (selectedUsers.value.length) {
            result.forEach((item) => {
              const findItem = selectedUsers.value.find((user) => user.id === item.id);
              if (findItem) {
                // 说明在selectedUsers中被找到
                item.checked = true;
              }
            });
          }
          currentDepartUsers.value = result;
        });
    } else {
      // 没有子节点，则显示用户
      currentDepartTree.value = [];
      getTableList({
        departId: item['id'],
      }).then((res: any) => {
        if (res?.records) {
          let checked = true;
          res.records.forEach((item) => {
            const findItem = selectedUsers.value.find((user) => user.id == item.id);
            if (findItem) {
              // 能在右侧找到说明选中了，左侧同样需要选中。
              item.checked = true;
            } else {
              item.checked = false;
              checked = false;
            }
          });
          currentDepartAllUsers.value = checked;
          currentDepartUsers.value = res.records;
        }
      });
    }
  };
  // 点击部门用户树复选框触发
  const handleDepartUsersTreeCheck = (item) => {
    item.checked = !item.checked;
    if (item.checked) {
      addUsers(item);
    } else {
      selectedUsers.value = selectedUsers.value.filter((user) => user.id !== item.id);
    }
    if (item.checked == false) {
      // 有一个是false，则全选false
      currentDepartAllUsers.value = false;
    }
  };
  // 全选
  const handleAllUsers = ({ target }) => {
    const { checked } = target;
    if (checked) {
      currentDepartUsers.value.forEach((item: any) => (item.checked = true));
      addUsers(currentDepartUsers.value);
    } else {
      currentDepartUsers.value.forEach((item: any) => (item.checked = false));
      selectedUsers.value = selectedUsers.value.filter((user) => {
        const userId = user.id;
        const findItem = currentDepartUsers.value.find((item: any) => item.id === userId);
        if (findItem) {
          return false;
        } else {
          return true;
        }
      });
    }
  };
  // 删除人员
  const handleDelUser = (item) => {
    const findIndex = selectedUsers.value.findIndex((user) => user.id === item.id);
    if (findIndex != -1) {
      selectedUsers.value.splice(findIndex, 1);
    }
    const findItem: any = currentDepartUsers.value.find((user: any) => user.id === item.id);
    if (findItem) {
      findItem.checked = false;
      currentDepartAllUsers.value = false;
    }
  };
  // 点击搜索用户复选框
  const handleSearchUserCheck = (item) => {
    item.checked = !item.checked;
    if (item.checked) {
      addUsers(item);
    } else {
      selectedUsers.value = selectedUsers.value.filter((user) => user.id !== item.id);
    }
  };
  // 点击搜索部门复选框
  const handleSearchDepartCheck = (e, item) => {
    handleDepartTreeCheck(e, item);
  };
  // 点击搜索部门
  const handleSearchDepartClick = (item) => {
    searchResult.depart = [];
    searchResult.user = [];
    breadcrumb.value = getPathToNodeById(item.id);
    handleDepartTreeClick(item);
  };
  // 添加人员到右侧
  const addUsers = (users) => {
    let newUsers: any = [];
    if (isArray(users)) {
      // selectedUsers里面没有才添加（防止重复）
      newUsers = users.filter((user: any) => !selectedUsers.value.find((item) => item.id === user.id));
    } else {
      if (!selectedUsers.value.find((user) => user.id === users.id)) {
        // selectedUsers里面没有才添加（防止重复）
        newUsers = [users];
      }
    }
    selectedUsers.value = [...selectedUsers.value, ...newUsers];
    const result = currentDepartUsers.value.every((item: any) => !!item.checked);
    currentDepartAllUsers.value = result;
  };
  // 解析参数
  const parseParams = (params) => {
    if (props?.params) {
      return {
        ...params,
        ...props.params,
      };
    }
    return params;
  };
  const getQueryTreeList = (params?) => {
    params = parseParams(params);
    queryTreeList({ ...params }).then((res) => {
      if (res) {
        departTree.value = res;
        currentDepartTree.value = res;
      }
    });
  };
  // 根据部门id获取用户
  const getTableList = (params) => {
    params = parseParams(params);
    return getTableListOrigin({ ...params });
  };
  const getUsersByDeptId = (id) => {
    return new Promise<any[]>((resolve) => {
      if (cacheDepartUser[id]) {
        resolve(cacheDepartUser[id]);
      } else {
        getTableList({
          departId: id,
        }).then((res: any) => {
          cacheDepartUser[id] = res.records ?? [];
          if (res?.records?.length) {
            resolve(res.records ?? []);
          }
        });
      }
    });
  };
  // 根据id获取根节点到当前节点路径
  const getPathToNodeById = (id: string, tree = departTree.value, path = []): any[] => {
    for (const node of tree) {
      if ((node as any).id === id) {
        return [...path];
      }
      if ((node as any).children) {
        const foundPath = getPathToNodeById(id, (node as any).children, [...path, node]);
        if (foundPath.length) {
          return foundPath;
        }
      }
    }
    return [];
  };
  // 根据id获取部门树父节点数据
  const getDepartTreeParentById = (id: string, tree = departTree.value, parent = null): any => {
    for (const node of tree) {
      if ((node as any).id === id) {
        return parent;
      }
      if ((node as any).children) {
        const found = getDepartTreeParentById(id, (node as any).children, node);
        if (found) {
          return found;
        }
      }
    }
    return null;
  };
  // 通过名称搜索部门支持模糊
  const getDepartByName = (name: string, tree = departTree.value): any[] => {
    const result: any[] = [];
    const search = (nodes: any[]) => {
      for (const node of nodes) {
        if (node.departName?.toLowerCase().includes(name.toLowerCase())) {
          result.push(node);
        }
        if (node.children?.length) {
          search(node.children);
        }
      }
    };
    search(tree);
    return result;
  };
  // 根据id获取部门树当前节点数据
  const getDepartTreeNodeById = (id: string, tree = departTree.value): any => {
    for (const node of tree) {
      if ((node as any).id === id) {
        return node;
      }
      if ((node as any).children) {
        const found = getDepartTreeNodeById(id, (node as any).children);
        if (found) {
          return found;
        }
      }
    }
    return null;
  };
</script>
<style lang="less">
  .JSelectUserByDepartmentModal {
    .scroll-container {
      padding: 0;
    }
  }
</style>
<style lang="less" scoped>
  .j-select-user-by-dept {
    background: #fff;
    border-radius: 4px;
  }
  .modal-content {
    display: flex;
    padding: 20px;
    padding-bottom: 0;
    padding-left: 0;
    height: 400px;
    font-size: 12px;
  }
  .left-content {
    display: flex;
    flex-direction: column;
    flex: 1;
    border-right: 1px solid #e8e8e8;
    .search-box {
      margin: 0 16px 16px 16px;
    }
    :deep(.ant-breadcrumb) {
      font-size: 12px;
      margin-left: 16px;
      color: inherit;
      cursor: pointer;
      li {
        .ant-breadcrumb-link {
          cursor: pointer;
          &:hover {
            color: @primary-color;
          }
        }
        &:last-child {
          .ant-breadcrumb-link {
            pointer-events: none;
          }
        }
      }
    }
    .tree-box {
      display: flex;
      flex-direction: column;
      flex: 1;
      overflow-y: auto;
      .no-data {
        flex: 1;
        display: flex;
        align-items: center;
        justify-content: center;
      }
    }
    .depart-tree {
      .depart-tree-item {
        padding: 0 16px;
        line-height: 40px;
        display: flex;
        align-items: center;
        justify-content: space-between;
        cursor: pointer;
        &:hover {
          background-color: #f4f6fa;
        }
      }
      .depart-tree-item-name {
        flex: 1;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
        margin: 0 8px;
      }
    }
  }
  .depart-users-tree {
    .allChecked {
      padding: 0 16px;
      margin-bottom: 16px;
      padding-top: 12px;
      :deep(.ant-checkbox-wrapper) {
        font-size: 12px;
      }
    }
    .depart-users-tree-item {
      line-height: 50px;
      padding: 0 16px;
      display: flex;
      cursor: pointer;
      &:hover {
        background-color: #f4f6fa;
      }
      .right {
        flex: 1;
        display: flex;
        align-items: center;
        margin: 0 8px;
      }
      .depart-users-tree-item-circle {
        width: 36px;
        height: 36px;
        border-radius: 50%;
        background-color: #aaa;
        overflow: hidden;
        img {
          display: block;
          width: 100%;
          height: 100%;
        }
      }
      .depart-users-tree-item-name {
        margin-left: 8px;
      }
    }
  }
  .search-depart {
    margin-bottom: 8px;
    .search-depart-title {
      padding-left: 16px;
      font-size: 14px;
      font-weight: 500;
      margin-bottom: 8px;
    }
    .search-depart-item {
      display: flex;
      align-items: center;
      padding: 8px 16px;
      cursor: pointer;
      &:hover {
        background-color: #f4f6fa;
      }
      .search-depart-item-name {
        margin-left: 8px;
        flex: 1;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }
    }
  }

  .search-user {
    margin-bottom: 8px;
    .search-user-title {
      font-size: 14px;
      font-weight: 500;
      margin-bottom: 8px;
      padding-left: 16px;
    }
    .search-user-item {
      display: flex;
      align-items: center;
      padding: 8px 16px;
      cursor: pointer;
      &:hover {
        background-color: #f4f6fa;
      }
      .right {
        flex: 1;
        display: flex;
        align-items: center;
        margin: 0 8px;
      }
      .search-user-item-info {
        display: flex;
        flex-direction: column;
        justify-content: center;
        margin-left: 8px;
      }
      .search-user-item-circle {
        width: 36px;
        height: 36px;
        border-radius: 50%;
        overflow: hidden;
        background-color: #aaa;
      }
      .search-user-item-name {
        flex: 1;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }
      .search-user-item-org {
        color: #999;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }
    }
  }
  .right-content {
    width: 400px;
    display: flex;
    flex-direction: column;
    padding-left: 16px;
    .selected-header {
      margin-bottom: 16px;
    }
    .selected-users {
      flex: 1;
      overflow-y: auto;
    }
    .content {
      display: grid;
      grid-template-columns: repeat(5, 1fr);
      gap: 8px;
    }
    .user-avatar {
      text-align: center;
      width: 70px;
      cursor: pointer;
    }
    .avatar-circle {
      width: 40px;
      height: 40px;
      border-radius: 50%;
      overflow: hidden;
      display: flex;
      align-items: center;
      justify-content: center;
      color: white;
      margin: 0 auto 8px;
      position: relative;
      background-color: rgba(0, 0, 0, 0.5);
      img {
        width: 100%;
        height: 100%;
      }
      &:hover {
        .mask {
          opacity: 1;
        }
      }
      .mask {
        opacity: 0;
        transition: opacity;
        width: 100%;
        height: 100%;
        position: absolute;
        top: 0;
        left: 0;
        background-color: rgba(0, 0, 0, 0.5);
        font-size: 16px;
        display: flex;
        align-items: center;
        justify-content: center;
      }
    }
    .user-name {
      text-align: center;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
      width: 100%;
    }
  }
</style>
