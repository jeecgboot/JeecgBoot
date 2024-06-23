<template>
  <a-row>
    <a-col :span="12">
      <div :style="containerStyle">
        <a-tree v-if="treeData.length > 0" showIcon :treeData="treeData" :selectedKeys="selectedKeys" @select="onSelect">
          <template #title="{ title, key }">
            <UserOutlined style="color: #9e9e9e"/><span style="margin-left: 5px">{{ title }}</span>
          </template>
        </a-tree>
      </div>
    </a-col>
    <a-col :span="12" style="padding-left: 10px">
      <div :style="containerStyle">
        <user-list :multi="multi" :excludeUserIdList="excludeUserIdList" :dataList="userDataList" :selectedIdList="selectedIdList" @selected="onSelectUser" @unSelect="unSelectUser" />
      </div>
    </a-col>
  </a-row>
</template>

<script lang="ts">
  import { computed, ref, watch } from 'vue';
  import { defHttp } from '/@/utils/http/axios';
  import UserList from './UserList.vue';
  import { UserOutlined } from '@ant-design/icons-vue';

  export default {
    name: 'RoleUserList',
    components: {
      UserList,
      UserOutlined
    },
    props: {
      searchText: {
        type: String,
        default: '',
      },
      selectedIdList: {
        type: Array,
        default: () => [],
      },
      excludeUserIdList:{
        type: Array,
        default: () => [],
      },
      multi: {
        type: Boolean,
        default: false,
      }
    },
    emits: ['selected', 'unSelect'],
    setup(props, { emit }) {
      const treeData = ref<any[]>([]);
      async function loadRoleList() {
        const url = '/sys/role/listByTenant';
        let params = {
          order: 'desc',
          column: 'createTime',
          pageSize: 200,
        };
        let arr = [];
        const data = await defHttp.get({ url, params }, { isTransformResponse: false });
        if (data.success) {
          const { records } = data.result;
          arr = records.map((k) => {
            return {
              title: k.roleName,
              id: k.id,
              key: k.id,
            };
          });
        }
        console.log('loadRoleList', data);
        treeData.value = arr;
      }
      loadRoleList();

      const selectedKeys = ref<any[]>([]);
      const selectedRoleId = ref('');
      function onSelect(ids, e) {
        let record = e.node.dataRef;
        selectedKeys.value = [record.key];

        let id = ids[0];
        selectedRoleId.value = id;
        loadUserList();
      }

      const userDataList = ref<any[]>([]);
      async function loadUserList() {
        const url = '/sys/user/selectUserList';
        let params = {
          pageNo: 1,
          pageSize: 99,
        };
        if (props.searchText) {
          params['keyword'] = props.searchText;
        }
        if (selectedRoleId.value) {
          params['roleId'] = selectedRoleId.value;
        }
        //update-begin---author:wangshuai---date:2024-02-02---for:【QQYUN-8239】用户角色，添加用户 返回2页数据，实际只显示一页---
        if(props.excludeUserIdList && props.excludeUserIdList.length>0){
          params['excludeUserIdList'] = props.excludeUserIdList.join(",");
        }
        //update-end---author:wangshuai---date:2024-02-02---for:【QQYUN-8239】用户角色，添加用户 返回2页数据，实际只显示一页---
        const data = await defHttp.get({ url, params }, { isTransformResponse: false });
        if (data.success) {
          const { records } = data.result;
          userDataList.value = records;
        } else {
          console.error(data.message);
        }
        console.log('role-loadUserList', data);
      }
      watch(
        () => props.searchText,
        () => {
          loadUserList();
        }
      );

      function onSelectUser(info) {
        emit('selected', info);
      }
      function unSelectUser(id) {
        emit('unSelect', id);
      }

      const maxHeight = ref(300);
      maxHeight.value = window.innerHeight - 300;
      const containerStyle = computed(() => {
        return {
          'overflow-y': 'auto',
          'max-height': maxHeight.value + 'px',
        };
      });

      return {
        containerStyle,
        treeData,
        selectedKeys,
        onSelect,
        onSelectUser,
        unSelectUser,
        userDataList,
      };
    },
  };
</script>

<style scoped></style>
