<template>
  <div class="bg-white m-4 mr-0 overflow-hidden">
    <div v-if="userIdentity === '2'" class="j-table-operator" style="width: 100%">
      <a-button type="primary" preIcon="ant-design:plus-outlined" @click="onAddChildDepart">添加下级</a-button>
      <!--      <a-button type="primary" preIcon="ant-design:edit-outlined" @click="editDepart">编辑</a-button>-->
      <a-button :disabled="!(checkedKeys && checkedKeys.length > 0)" preIcon="ant-design:delete-outlined" @click="onDeleteBatch">删除</a-button>
    </div>
    <a-spin :spinning="loading">
      <template v-if="userIdentity === '2'">
        <a-input-search placeholder="按部门名称搜索…" style="margin-bottom: 10px" @search="onSearch" />
        <!--组织机构树-->
        <BasicTree
          v-if="!treeReloading"
          :toolbar="false"
          :search="false"
          :showLine="false"
          :clickRowToExpand="false"
          :multiple="false"
          :checkStrictly="true"
          :treeData="treeData"
          :checkedKeys="checkedKeys"
          :selectedKeys="selectedKeys"
          :expandedKeys="expandedKeys"
          :autoExpandParent="autoExpandParent"
          :beforeRightClick="getRightMenuList"
          @select="onSelect"
          @expand="onExpand"
          @check="onCheck"
        />
      </template>
      <a-empty v-else description="普通员工无此权限" />
    </a-spin>
    <DepartFormModal :rootTreeData="treeData" @register="registerModal" @success="handleSuccess" />
  </div>
</template>

<script lang="ts" setup>
  import { inject, nextTick, ref } from 'vue';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { BasicTree, ContextMenuItem } from '/@/components/Tree';
  import { queryMyDepartTreeList, searchByKeywords } from '../depart.user.api';
  import DepartFormModal from '@/views/system/depart/components/DepartFormModal.vue';
  import { useModal } from '@/components/Modal';
  import { deleteBatchDepart } from '@/views/system/depart/depart.api';

  const prefixCls = inject('prefixCls');
  const emit = defineEmits(['select']);
  const { createMessage } = useMessage();

  let loading = ref<boolean>(false);
  // 负责部门ID
  let myDepIds = ref<any[]>([]);
  // 部门树列表数据
  let treeData = ref<any[]>([]);
  // 当前展开的项
  let expandedKeys = ref<any[]>([]);
  // 当前选中的项
  let selectedKeys = ref<any[]>([]);
  // 当前选中的项
  let selectedNode = ref<any>({});
  // 当前选中的项
  let checkedKeys = ref<any[]>([]);
  // 是否自动展开父级
  let autoExpandParent = ref<boolean>(true);
  // 用户身份(1:普通员工  2:上级)
  let userIdentity = ref<string>('2');
  // 树组件重新加载
  let treeReloading = ref<boolean>(false);
  // 注册 modal
  const [registerModal, { openModal }] = useModal();
  // 加载部门信息
  function loadDepartTreeData() {
    loading.value = true;
    treeReloading.value = true;
    treeData.value = [];
    queryMyDepartTreeList()
      .then((res) => {
        if (res.success) {
          if (Array.isArray(res.result)) {
            treeData.value = res.result;
            myDepIds.value = res.result.map((item) => item.id);
            userIdentity.value = res.message;
            autoExpandParentNode();
          }
        } else {
          createMessage.warning(res.message);
        }
      })
      .finally(async () => {
        await nextTick();
        loading.value = false;
        treeReloading.value = false;
      });
  }

  loadDepartTreeData();

  // 自动展开父节点，只展开一级
  function autoExpandParentNode() {
    let keys: Array<any> = [];
    treeData.value.forEach((item, index) => {
      if (item.children && item.children.length > 0) {
        keys.push(item.key);
      }
      if (index === 0) {
        // 默认选中第一个
        setSelectedKey(item.id, item);
      }
    });
    if (keys.length > 0) {
      reloadTree();
      expandedKeys.value = keys;
    }
  }

  // 添加子级部门
  function onAddChildDepart() {
    if (selectedKeys.value && selectedKeys.value.length === 0) {
      createMessage.warning('请先选择一个部门');
      return;
    }
    const record = { parentId: selectedKeys.value[0] };
    openModal(true, { isUpdate: false, isChild: true, record });
  }

  // 编辑部门
  function editDepart() {
    if (selectedKeys.value && selectedKeys.value.length === 0) {
      createMessage.warning('请先选择一个部门');
      return;
    }
    if (myDepIds.value.includes(selectedKeys.value[0])) {
      createMessage.warning('不能编辑负责部门');
      return;
    }
    console.log('selectedNode', selectedNode.value);
    openModal(true, { isUpdate: false, isChild: true, record: { ...selectedNode.value } });
  }

  // 删除部门
  async function onDeleteBatch() {
    const idList = checkedKeys.value;
    if (myDepIds.value.includes(idList[0])) {
      createMessage.warning('不能删除负责部门');
      return;
    }
    if (idList.length > 0) {
      try {
        loading.value = true;
        await deleteBatchDepart({ ids: idList.join(',') }, true);
        await loadDepartTreeData();
      } finally {
        loading.value = false;
      }
    }
  }
  // 重新加载树组件，防止无法默认展开数据
  async function reloadTree() {
    await nextTick();
    treeReloading.value = true;
    await nextTick();
    treeReloading.value = false;
  }

  /**
   * 设置当前选中的行
   */
  function setSelectedKey(key: string, data?: object) {
    selectedKeys.value = [key];
    checkedKeys.value = [key];
    if (data) {
      selectedNode.value = { ...data };
      emit('select', data);
    }
  }

  // 搜索事件
  function onSearch(value: string) {
    if (value) {
      loading.value = true;
      searchByKeywords({ keyWord: value, myDeptSearch: '1' })
        .then((result) => {
          if (Array.isArray(result)) {
            treeData.value = result;
          } else {
            createMessage.warning('未查询到部门信息');
            treeData.value = [];
          }
        })
        .finally(() => (loading.value = false));
    } else {
      loadDepartTreeData();
    }
  }

  // 树选择事件
  function onSelect(selKeys, event) {
    if (selKeys.length > 0 && selectedKeys.value[0] !== selKeys[0]) {
      setSelectedKey(selKeys[0], event.selectedNodes[0]);
    } else {
      // 这样可以防止用户取消选择
      setSelectedKey(selectedKeys.value[0]);
    }
    checkedKeys.value = [selectedKeys.value[0]];
  }

  // 树选中事件
  function onCheck(keys) {
    if (keys.checked && keys.checked.length > 0) {
      checkedKeys.value = [...keys.checked];
    } else {
      checkedKeys.value = [];
    }
  }

  // 树展开事件
  function onExpand(keys) {
    expandedKeys.value = keys;
    autoExpandParent.value = false;
  }

  //成功回调
  async function handleSuccess() {
    await loadDepartTreeData();
  }
  /**
   *
   * @param node
   */
  function getRightMenuList(node: any): ContextMenuItem[] {
    return [
      {
        label: '添加下级',
        disabled: myDepIds.value.includes(node.key),
        handler: () => {
          setSelectedKey(node.key);
          onAddChildDepart();
        },
        icon: 'ant-design:plus-outlined',
      },
      {
        label: '编辑',
        disabled: myDepIds.value.includes(node.key),
        handler: () => {
          setSelectedKey(node.key);
          const record = { ...node.dataRef };
          openModal(true, { isUpdate: true, record, isChild: true });
        },
        icon: 'ant-design:edit-outlined',
      },
    ];
  }
</script>
<style lang="less" scoped>
  /*升级antd3后，查询框与树贴的太近，样式优化*/
  :deep(.jeecg-tree-header) {
    margin-bottom: 6px;
  }
</style>
