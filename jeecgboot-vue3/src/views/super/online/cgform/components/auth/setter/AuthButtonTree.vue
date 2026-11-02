<template>
  <div>
    <a-empty v-if="disabled" description="请先选中左侧角色/部门/用户" />
    <a-empty v-else-if="treeData.length === 0" description="无权限信息" />
    <template v-else>
      <div class="onl-auth-tree-btns">
        <a-button @click="onRefresh" size="small" type="primary" preIcon="ant-design:redo" ghost>刷新</a-button>
        <a-button @click="onSave" size="small" type="primary" preIcon="ant-design:save" ghost>保存</a-button>
        <a-button @click="onSelectAll" size="small" type="primary" ghost><CheckOutlined />全选</a-button>
        <a-button @click="onClearSelected" size="small" type="primary" ghost><UndoOutlined />重置</a-button>
      </div>
      <a-tree
        checkable
        v-model:checkedKeys="checkedKeys"
        :expandedKeys="expandedKeys"
        :autoExpandParent="autoExpandParent"
        :treeData="treeData"
        @expand="onExpand"
      />
    </template>
  </div>
</template>

<script lang="ts">
  import { defineComponent, ref, watch, computed } from 'vue';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { authButtonFixedList } from '../auth.data';
  import { authButtonLoadTree, loadRoleAuthChecked, saveAuthButton } from '../auth.api';
  import { CheckOutlined, UndoOutlined } from '@ant-design/icons-vue';

  export default defineComponent({
    name: 'AuthButtonTree',
    components: { CheckOutlined, UndoOutlined },
    props: {
      cgformId: { type: String, required: true },
    },
    setup(props) {
      const { createMessage: $message, createSuccessModal } = useMessage();
      const roleId = ref('');
      const authType = ref(2);
      const autoExpandParent = ref(true);
      const expandedKeys = ref<string[]>([]);
      const checkedKeys = ref<string[]>([]);

      const treeData = ref<Recordable[]>([]);
      const authMode = ref('');
      const disabled = computed(() => !roleId.value);
      watch(() => props.cgformId, loadTree, { immediate: true });

      async function loadTree() {
        if (!props.cgformId) return;
        let result = (await authButtonLoadTree(props.cgformId, authType.value)) as Recordable[];
        //1.遍历第一次 根据code设置默认按钮名称
        result.forEach((item) => {
          for (const btn of authButtonFixedList) {
            if (item.code == btn.code) {
              if (!item['title']) {
                item['title'] = btn.title;
              }
              break;
            }
          }
        });
        //2.拼树形数据
        let trees: Recordable[] = [];
        for (let item of result) {
          let title = getTreeNodeTitle(item);
          trees.push({ key: item.id, title });
        }
        treeData.value = trees;
      }

      function onRefresh() {
        loadTree();
        loadChecked(roleId.value, authMode.value);
      }

      // 给外部调用，加载当前选择的权限
      async function loadChecked($roleId, $authMode) {
        roleId.value = $roleId;
        authMode.value = $authMode;
        checkedKeys.value = [];
        await loadTree();
        let result = (await loadRoleAuthChecked({
          roleId: $roleId,
          cgformId: props.cgformId,
          type: authType.value,
          authMode: $authMode,
        })) as Recordable[];
        checkedKeys.value = result.map((item) => item.authId);
      }

      function clearChecked() {
        roleId.value = '';
        // QQYUN-4284 【online表单】权限管理 开启按钮后，在角色授权中显示，当关闭时，再打开角色权限仍然显示，需刷新页面才不显示
        loadTree();
      }

      async function onSave() {
        try {
          const {success, message, result} = await saveAuthButton(roleId.value, props.cgformId, {
            authId: JSON.stringify(checkedKeys.value),
            authMode: authMode.value,
          });
          if (success) {
            if (Array.isArray(result?.disabledNames)) {
              createSuccessModal({
                title: '保存成功',
                content: `由于以下按钮未激活，所以权限未生效。<br>${result.disabledNames.join('<br>')}`,
              });
            } else {
              $message.success('保存成功');
            }
          } else {
            $message.error(message);
          }
        } catch (e) {
          $message.error('保存出现异常');
          console.error(e);
        }
      }

      function getTreeNodeTitle(item) {
        let str = item.title + '-';
        if (item.code && item.code.includes('form_sub')) {
          // 【TV360X-2711】当 code 以 form_sub 开头时，说明是附表
          str += '表单可见（附表）'
        } else if (item.code === 'form_confirm') {
          str += '表单可见';
        } else if (item.page == 3) {
          str += '列表可见';
        } else if (item.page == 5) {
          str += '表单可见';
        }
        return str;
      }

      function onExpand($expandedKeys) {
        expandedKeys.value = $expandedKeys;
        autoExpandParent.value = false;
      }

      function clear() {
        roleId.value = '';
        checkedKeys.value = [];
      }

      function onClearSelected() {
        checkedKeys.value = [];
      }

      function onSelectAll() {
        checkedKeys.value = treeData.value.map((item) => item.key);
      }

      return {
        loadChecked,
        clear,
        expandedKeys,
        autoExpandParent,
        checkedKeys,
        treeData,
        disabled,
        onSave,
        onExpand,
        onRefresh,
        clearChecked,
        onClearSelected,
        onSelectAll,
      };
    },
  });
</script>

<style scoped lang="less">
  .onl-auth-tree-btns {
    margin-left: 10px;
  }

  .onl-auth-tree-btns button {
    margin: 0 5px 0 2px;
  }
</style>
