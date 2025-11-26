<template>
  <BasicDrawer v-bind="$attrs" @register="registerDrawer" width="650px" destroyOnClose showFooter>
    <template #title>
      权限配置
      <a-dropdown>
        <a-button class="more-icon">
          更多操作
          <Icon icon="ant-design:down-outlined" size="14px" style="position: relative; top: 1px; right: 5px"></Icon>
        </a-button>
        <template #overlay>
          <a-menu @click="treeMenuClick">
            <a-menu-item key="checkAll">选择全部</a-menu-item>
            <a-menu-item key="cancelCheck">取消选择</a-menu-item>
            <div class="line"></div>
            <a-menu-item key="openAll">展开全部</a-menu-item>
            <a-menu-item key="closeAll">折叠全部</a-menu-item>
            <div class="line"></div>
            <a-menu-item key="relation">层级关联</a-menu-item>
            <a-menu-item key="standAlone">层级独立</a-menu-item>
          </a-menu>
        </template>
      </a-dropdown>
    </template>
    <BasicTree
      ref="treeRef"
      checkable
      :treeData="treeData"
      :checkedKeys="checkedKeys"
      :expandedKeys="expandedKeys"
      :selectedKeys="selectedKeys"
      :clickRowToExpand="false"
      :checkStrictly="true"
      title="所拥有的的权限"
      @check="onCheck"
    >
      <template #title="{ slotTitle, ruleFlag }">
        {{ slotTitle }}
        <Icon v-if="ruleFlag" icon="ant-design:align-left-outlined" style="margin-left: 5px; color: red"></Icon>
      </template>
    </BasicTree>
    <!--右下角按钮-->
    <template #footer>
      <!-- <PopConfirmButton title="确定放弃编辑？" @confirm="closeDrawer" okText="确定" cancelText="取消"></PopConfirmButton> -->
      <a-button @click="closeDrawer">取消</a-button>
      <a-button @click="handleSubmit(false)" type="primary" :loading="loading" ghost style="margin-right: 0.8rem">仅保存</a-button>
      <a-button @click="handleSubmit(true)" type="primary" :loading="loading">保存并关闭</a-button>
    </template>
  </BasicDrawer>
</template>
<script lang="ts" setup>
  import { ref, unref } from 'vue';
  import { BasicDrawer, useDrawerInner } from '/@/components/Drawer';
  import { BasicTree, TreeItem } from '/@/components/Tree';
  import { queryPremTreeList, editPackPermission } from '../tenant.api';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { PACK_AUTH_CONFIG_KEY } from '/@/enums/cacheEnum';
  const emit = defineEmits(['register','success']);
  //树的信息
  const treeData = ref<TreeItem[]>([]);
  //树的全部节点信息
  const allTreeKeys = ref([]);
  //树的选择节点信息
  const checkedKeys = ref<any>([]);
  //树的选中的节点信息
  const selectedKeys = ref([]);
  const packId = ref('');
  //树的实例
  const treeRef = ref(null);
  const loading = ref(false);

  //展开折叠的key
  const expandedKeys = ref<any>([]);
  //父子节点选中状态是否关联 true不关联，false关联
  const checkStrictly = ref<boolean>(false);
  const [registerDrawer, { setDrawerProps, closeDrawer }] = useDrawerInner(async (data) => {
    await reset();
    setDrawerProps({ confirmLoading: false, loading: true });
    packId.value = data.packId;
    //初始化数据
    const roleResult = await queryPremTreeList();
    treeData.value = translateTitle(roleResult.treeList);
    allTreeKeys.value = roleResult.ids;
    const localData = localStorage.getItem(PACK_AUTH_CONFIG_KEY);
    if (localData) {
      const obj = JSON.parse(localData);
      obj.level && treeMenuClick({ key: obj.level });
      obj.expand && treeMenuClick({ key: obj.expand });
    } else {
      expandedKeys.value = roleResult.ids;
    }

    //初始化角色菜单数据
    if(data.permissionIds){
      checkedKeys.value = data.permissionIds.split(",");
    }

    setDrawerProps({ loading: false });
  });
  /**
   * 翻译菜单名称
   */
  function translateTitle(data) {
    if (data?.length) {
      data.forEach((item) => {
        if (item.slotTitle) {
          const { t } = useI18n();
          if (item.slotTitle.includes("t('") && t) {
            item.slotTitle = new Function('t', `return ${item.slotTitle}`)(t);
          }
        }
        if (item.children?.length) {
          translateTitle(item.children);
        }
      });
    }
    return data;
  }
  /**
   * 点击选中
   */
  function onCheck(o, e) {
    // checkStrictly: true=>层级独立，false=>层级关联.
    if (checkStrictly.value) {
      checkedKeys.value = o.checked ? o.checked : o;
    } else {
      const keys = getNodeAllKey(e.node, 'children', 'key');
      if (e.checked) {
        // 反复操作下可能会有重复的keys，得用new Set去重下
        checkedKeys.value = [...new Set([...checkedKeys.value, ...keys])];
      } else {
        const result = removeMatchingItems(checkedKeys.value, keys);
        checkedKeys.value = result;
      }
    }
  }
  /**
   * 删除相匹配数组的项
   */
  function removeMatchingItems(arr1, arr2) {
    // 使用哈希表记录 arr2 中的元素
    const hashTable = {};
    for (const item of arr2) {
      hashTable[item] = true;
    }
    // 使用 filter 方法遍历第一个数组，过滤出不在哈希表中存在的项
    return arr1.filter((item) => !hashTable[item]);
  }
  /**
   * 获取当前节点及以下所有子孙级的key
   */
  function getNodeAllKey(node: any, children: any, key: string) {
    const result: any = [];
    result.push(node[key]);
    const recursion = (data) => {
      data.forEach((item: any) => {
        result.push(item[key]);
        if (item[children]?.length) {
          recursion(item[children]);
        }
      });
    };
    node[children]?.length && recursion(node[children]);
    return result;
  }
  
  /**
   * 数据重置
   */
  function reset() {
    treeData.value = [];
    allTreeKeys.value = [];
    checkedKeys.value = [];
    selectedKeys.value = [];
    packId.value = '';
  }
  /**
   * 获取tree实例
   */
  function getTree() {
    const tree = unref(treeRef);
    if (!tree) {
      throw new Error('tree is null!');
    }
    return tree;
  }
  /**
   * 提交
   */
  async function handleSubmit(exit) {
    let params = {
      id: unref(packId),
      permissionIds: unref(getTree()?.getCheckedKeys()).join(','),
    };
    if (loading.value === false) {
      await doSave(params);
    } else {
      console.log('请等待上次执行完毕!');
    }
    if (exit) {
      // 如果关闭
      closeDrawer();
    }
  }

  // VUE角色授权重复保存 #352
  async function doSave(params) {
    loading.value = true;
    try {
      await editPackPermission(params);
      emit("success")
    } catch (e) {
      loading.value = false;
    }
    setTimeout(() => {
      loading.value = false;
    }, 500);
  }

  /**
   * 树菜单选择
   * @param key
   */
  function treeMenuClick({ key }) {
    if (key === 'checkAll') {
      checkedKeys.value = allTreeKeys.value;
    } else if (key === 'cancelCheck') {
      checkedKeys.value = [];
    } else if (key === 'openAll') {
      expandedKeys.value = allTreeKeys.value;
      saveLocalOperation('expand', 'openAll');
    } else if (key === 'closeAll') {
      expandedKeys.value = [];
      saveLocalOperation('expand', 'closeAll');
    } else if (key === 'relation') {
      checkStrictly.value = false;
      saveLocalOperation('level', 'relation');
    } else {
      checkStrictly.value = true;
      saveLocalOperation('level', 'standAlone');
    }
  }
  /**
   * 角色授权弹窗操作缓存
   */
  const saveLocalOperation = (key, value) => {
    const localData = localStorage.getItem(PACK_AUTH_CONFIG_KEY);
    const obj = localData ? JSON.parse(localData) : {};
    obj[key] = value;
    localStorage.setItem(PACK_AUTH_CONFIG_KEY, JSON.stringify(obj));
  };
</script>

<style lang="less" scoped>
  /** 固定操作按钮 */
  .jeecg-basic-tree {
    position: absolute;
    width: 618px;
  }
  .line {
    height: 1px;
    width: 100%;
    border-bottom: 1px solid #f0f0f0;
  }
  .more-icon {
    float: right;
    margin-right: 2px;
    cursor: pointer;
  }
  :deep(.jeecg-tree-header) {
    border-bottom: none;
  }
</style>
