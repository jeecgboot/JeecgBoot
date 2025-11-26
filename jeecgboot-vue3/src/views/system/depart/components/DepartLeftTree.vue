<template>
  <a-card :bordered="false" style="height: 100%">
    <div class="j-table-operator" style="width: 100%;display: flex;align-items: center">
      <a-button type="primary" preIcon="ant-design:plus-outlined" @click="onAddDepart">新增</a-button>
      <a-button type="primary" preIcon="ant-design:plus-outlined" @click="onAddChildDepart()">添加下级</a-button>
      <a-upload name="file" :showUploadList="false" :customRequest="onImportXls" v-if="!isTenantDepart">
        <a-button type="primary" preIcon="ant-design:import-outlined">导入</a-button>
      </a-upload>
      <a-button type="primary" preIcon="ant-design:export-outlined" @click="onExportXls" v-if="!isTenantDepart">导出</a-button>
      <template v-if="checkedKeys.length > 0">
        <a-dropdown>
          <template #overlay>
            <a-menu>
              <a-menu-item key="1" @click="onDeleteBatch">
                <icon icon="ant-design:delete-outlined" />
                <span>删除</span>
              </a-menu-item>
            </a-menu>
          </template>
          <a-button>
            <span>批量操作 </span>
            <icon icon="akar-icons:chevron-down" />
          </a-button>
        </a-dropdown>
      </template>
      <Icon icon="ant-design:question-circle-outlined" style="margin-left: 10px;cursor: pointer" @click="tipShow = true"></Icon>
      <div v-if="loginTenantName" style="margin-left: 10px;"
      >当前登录租户: <span class="tenant-name">{{ loginTenantName }}</span>
      </div>
    </div>
    <a-alert type="info" show-icon class="alert" style="margin-bottom: 8px">
      <template #message>
        <template v-if="checkedKeys.length > 0">
          <span>已选中 {{ checkedKeys.length }} 条记录</span>
          <a-divider type="vertical" />
          <a @click="checkedKeys = []">清空</a>
        </template>
        <template v-else>
          <span>未选中任何数据</span>
        </template>
      </template>
    </a-alert>
    <a-spin :spinning="loading">
      <a-input-search placeholder="按部门名称搜索…" style="margin-bottom: 10px" @search="onSearch" />
      <!--组织机构树-->
      <template v-if="treeData.length > 0">
        <a-tree
          v-if="!treeReloading"
          checkable
          :clickRowToExpand="false"
          :treeData="treeData"
          :selectedKeys="selectedKeys"
          :checkStrictly="checkStrictly"
          :load-data="loadChildrenTreeData"
          :checkedKeys="checkedKeys"
          v-model:expandedKeys="expandedKeys"
          @check="onCheck"
          @select="onSelect"
          draggable
          @drop="onDrop"
          @dragstart="onDragStart"
          style="overflow-y: auto;height: calc(100vh - 330px);"
        >
          <template #title="{ key: treeKey, title, dataRef, data }">
            <a-dropdown :trigger="['contextmenu']">
              <Popconfirm
                :open="visibleTreeKey === treeKey"
                title="确定要删除吗？"
                ok-text="确定"
                cancel-text="取消"
                placement="rightTop"
                @confirm="onDelete(dataRef)"
                @openChange="onVisibleChange"
              >
                <TreeIcon :orgCategory="dataRef.orgCategory" :title="title"></TreeIcon>
              </Popconfirm>

              <template #overlay>
                <a-menu @click="">
                  <a-menu-item key="1" @click="onAddChildDepart(dataRef)" v-if="data.orgCategory !== '3'">添加下级</a-menu-item>
                  <a-menu-item key="2" @click="visibleTreeKey = treeKey">
                    <span style="color: red">删除</span>
                  </a-menu-item>
                </a-menu>
              </template>
            </a-dropdown>
          </template>
        </a-tree>
      </template>
      <a-empty v-else description="暂无数据" />
    </a-spin>
    <DepartFormModal :rootTreeData="treeData" @register="registerModal" @success="loadRootTreeData" />
  </a-card>
  <a-modal v-model:open="tipShow" :footer="null" title="部门规则说明" :width="800">
      <ul class="departmentalRulesTip">
        <li>当前部门机构设置支持集团组织架构，第一级默认为公司，下级可创建子公司、部门和岗位。</li>
        <li><br/></li>
        <li>1、岗位下不能添加下级。</li>
        <li>2、部门下不能直接添加子公司。</li>
        <li>3、子公司下可继续添加子公司。</li>
        <li>4、岗位需配置职务级别，岗位的级别高低和上下级关系均以职务级别及上级岗位设置为准。</li>
        <li>5、董事长岗位仅可选择上级公司（子公司或总公司）各部门的所有岗位为上级岗位。</li>
        <li>6、非董事长岗位仅可选择当前父级部门及本部门内级别更高的岗位为上级岗位。</li>
        <li><br/></li>
        <li><b>特别说明：</b>董事长相关逻辑为固定写死，职务等级“董事长”的表述请勿修改。</li>
      </ul>
    <div style="height: 10px"></div>
  </a-modal>
</template>

<script lang="ts" setup>
  import { inject, nextTick, ref, unref, defineEmits, h } from 'vue';
  import { useModal } from '/@/components/Modal';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { useMethods } from '/@/hooks/system/useMethods';
  import { Api, deleteBatchDepart, queryDepartAndPostTreeSync, updateChangeDepart } from '../depart.api';
  import { searchByKeywords } from '/@/views/system/departUser/depart.user.api';
  import DepartFormModal from '/@/views/system/depart/components/DepartFormModal.vue';
  import { Modal, Popconfirm } from 'ant-design-vue';
  import TreeIcon from "@/components/Form/src/jeecg/components/TreeIcon/TreeIcon.vue";

  const prefixCls = inject('prefixCls');
  const emit = defineEmits(['select', 'rootTreeData']);
  const { createMessage } = useMessage();
  const { handleImportXls, handleExportXls } = useMethods();
  const props = defineProps({
    //是否为租户部门
    isTenantDepart: { default: false, type: Boolean },
    //当前登录租户
    loginTenantName: { default: "", type: String },
  })

  const loading = ref<boolean>(false);
  // 部门树列表数据
  const treeData = ref<any[]>([]);
  // 当前选中的项
  const checkedKeys = ref<any[]>([]);
  // 当前展开的项
  const expandedKeys = ref<any[]>([]);
  // 当前选中的项
  const selectedKeys = ref<any[]>([]);
  // 树组件重新加载
  const treeReloading = ref<boolean>(false);
  // 树父子是否关联
  const checkStrictly = ref<boolean>(true);
  // 当前选中的部门
  const currentDepart = ref<any>(null);
  // 控制确认删除提示框是否显示
  const visibleTreeKey = ref<any>(null);
  // 搜索关键字
  const searchKeyword = ref('');
  // 提示弹窗是否显示
  const tipShow = ref<boolean>(false);

  // 注册 modal
  const [registerModal, { openModal }] = useModal();

  // 加载顶级部门信息
  async function loadRootTreeData() {
    try {
      loading.value = true;
      treeData.value = [];
      const result = await queryDepartAndPostTreeSync();
      if (Array.isArray(result)) {
        treeData.value = result;
      }
      if (expandedKeys.value.length === 0) {
        autoExpandParentNode();
      } else {
        if (selectedKeys.value.length === 0) {
          let item = treeData.value[0];
          if (item) {
            // 默认选中第一个
            setSelectedKey(item.id, item);
          }
        } else {
          emit('select', currentDepart.value);
        }
      }
      emit('rootTreeData', treeData.value);
    } finally {
      loading.value = false;
    }
  }

  loadRootTreeData();

  // 加载子级部门信息
  async function loadChildrenTreeData(treeNode) {
    try {
      const result = await queryDepartAndPostTreeSync({
        pid: treeNode.dataRef.id,
      });
      if (result.length == 0) {
        treeNode.dataRef.isLeaf = true;
      } else {
        treeNode.dataRef.children = result;
        if (expandedKeys.value.length > 0) {
          // 判断获取的子级是否有当前展开的项
          let subKeys: any[] = [];
          for (let key of expandedKeys.value) {
            if (result.findIndex((item) => item.id === key) !== -1) {
              subKeys.push(key);
            }
          }
          if (subKeys.length > 0) {
            expandedKeys.value = [...expandedKeys.value];
          }
        }
      }
      treeData.value = [...treeData.value];
      emit('rootTreeData', treeData.value);
    } catch (e) {
      console.error(e);
    }
    return Promise.resolve();
  }

  // 自动展开父节点，只展开一级
  function autoExpandParentNode() {
    let item = treeData.value[0];
    if (item) {
      if (!item.isLeaf) {
        expandedKeys.value = [item.key];
      }
      // 默认选中第一个
      setSelectedKey(item.id, item);
      reloadTree();
    } else {
      emit('select', null);
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
    if (data) {
      currentDepart.value = data;
      emit('select', data);
    }
  }

  // 添加一级部门
  function onAddDepart() {
    openModal(true, { isUpdate: false, isChild: false });
  }

  // 添加子级部门
  function onAddChildDepart(data = currentDepart.value) {
    if (data == null) {
      createMessage.warning('请先选择一个部门');
      return;
    }
    if(data.orgCategory === '3'){
      createMessage.warning('岗位下无法添加子级！');
      return;
    }
    const record = { parentId: data.id, orgCategory: data.orgCategory };
    openModal(true, { isUpdate: false, isChild: true, record });
  }

  // 搜索事件
  async function onSearch(value: string) {
    if (value) {
      try {
        loading.value = true;
        treeData.value = [];
        let result = await searchByKeywords({ keyWord: value, orgCategory: "1,2,3,4" });
        if (Array.isArray(result)) {
          treeData.value = result;
        }
        autoExpandParentNode();
      } finally {
        loading.value = false;
      }
    } else {
      loadRootTreeData();
    }
    searchKeyword.value = value;
  }

  // 树复选框选择事件
  function onCheck(e) {
    if (Array.isArray(e)) {
      checkedKeys.value = e;
    } else {
      checkedKeys.value = e.checked;
    }
  }

  // 树选择事件
  function onSelect(selKeys, event) {
    console.log('select: ', selKeys, event);
    if (selKeys.length > 0 && selectedKeys.value[0] !== selKeys[0]) {
      setSelectedKey(selKeys[0], event.selectedNodes[0]);
    } else {
      // 这样可以防止用户取消选择
      setSelectedKey(selectedKeys.value[0]);
    }
  }

  /**
   * 根据 ids 删除部门
   * @param idListRef array
   * @param confirm 是否显示确认提示框
   */
  async function doDeleteDepart(idListRef, confirm = true) {
    const idList = unref(idListRef);
    if (idList.length > 0) {
      try {
        loading.value = true;
        await deleteBatchDepart({ ids: idList.join(',') }, confirm);
        await loadRootTreeData();
      } finally {
        loading.value = false;
      }
    }
  }

  // 删除单个部门
  async function onDelete(data) {
    if (data) {
      onVisibleChange(false);
      doDeleteDepart([data.id], false);
    }
  }

  // 批量删除部门
  async function onDeleteBatch() {
    try {
      await doDeleteDepart(checkedKeys);
      checkedKeys.value = [];
    } finally {
    }
  }

  function onVisibleChange(visible) {
    if (!visible) {
      visibleTreeKey.value = null;
    }
  }

  function onImportXls(d) {
    handleImportXls(d, Api.importExcelUrl, () => {
      loadRootTreeData();
    });
  }

  function onExportXls() {
    // 代码逻辑说明: 【TV360X-1671】部门管理不支持选中的记录导出---
    let params = {}
    if(checkedKeys.value && checkedKeys.value.length > 0) {
      params['selections'] = checkedKeys.value.join(',')
    }
    handleExportXls('部门信息', Api.exportXlsUrl,params);
  }

  /**
   * 拖拽开始时，只关闭被拖拽的当前节点
   * 
   * @param info
   */
  function onDragStart(info: any) {
    const dragKey = info.node?.key;
    if (!dragKey){
      return;
    }
    // 只关闭被拖拽的当前节点，不关闭其子节点
    if (expandedKeys.value.includes(dragKey)) {
      expandedKeys.value = expandedKeys.value.filter(key => key !== dragKey);
    }
  }
  
  /**
   * 拖拽结束
   * @param info
   */
  function onDrop (info){
    const dropKey = info.node.key;
    const dragKey = info.dragNode.key;
    const dropPos = info.node.pos.split('-');
    const dropPosition = info.dropPosition - Number(dropPos[dropPos.length - 1]);
    const dropTitle = info.node.title;
    const dragTitle = info.dragNode.title;
    //禁止拖拽到子节点
    if (isDescendant(info.dragNode, info.node.key)) {
      createMessage.warning('不能拖拽到自身后代');
      return;
    }
    if(dropKey === dragKey){
      createMessage.warning('不能自身拖拽到自身');
      return;
    }
    let pos = "中";
    if(dropPosition === -1){
      pos = "上方";
    }else if (dropPosition === 1){
      pos = "下方";
    }
    let text = "将【" + dragTitle + "】移动到【" + dropTitle + "】" + pos + "？";
    Modal.confirm({
      title: '确认移动',
      content: h('div', {}, [
        h('p', { style: { marginBottom: '12px', fontSize: '14px' } }, text),
        h('p', { 
          style: { 
            color: '#ff4d4f', 
            fontSize: '13px',
            margin: '0'
          } 
        }, '移动后：机构编码会改变，历史业务数据保留原机构编码，此操作不可撤销！')
      ]),
      okText: '确认',
      cancelText: '取消',
      onOk: () => {
        updateChangeDepart({ dragId: dragKey, dropId: dropKey, dropPosition: dropPosition, sort: info.dropPosition }).then(res=>{
          if(res.success){
            createMessage.success('部门顺序调整成功');
            //重新加载树
            treeData.value = [];
            selectedKeys.value = [];
            loadRootTreeData();
          } else {
            createMessage.error(res.message);
          }
        }).catch(e=>{
          createMessage.error(e.message);
        })
      }
    })
  }

  /**
   * 判断目标节点是否在拖拽节点的子树中（避免循环引用）
   * 
   * @param dragNode
   * @param targetKey
   */
  function isDescendant(dragNode, targetKey) {
    const stack = [...(dragNode.children ?? [])];
    while (stack.length) {
      const node = stack.pop()!;
      if (node.key === targetKey){
        return true;
      }
      if (node.children){
        stack.push(...node.children);
      }
    }
    return false;
  }
  
  defineExpose({
    loadRootTreeData,
  });
</script>

<style lang="less" scoped>
  .departmentalRulesTip{
    margin: 20px;
    background-color: #f8f9fb;
    color: #99a1a9;
    border-radius: 4px;
    padding: 12px;
  }
  .tenant-name {
    text-decoration: underline;
    margin: 5px;
    font-size: 15px;
  }
</style>
