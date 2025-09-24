<template>
  <BasicTable @register="registerTable" style="padding-top: 10px">
    <template #departNames="{ text, record }">
      <template v-if="text && text.length > 0">
        {{ getName(text) }}
      </template>
    </template>
    <template #positionNames="{ text, record }">
      <template v-if="text && text.length > 0">
        {{ getName(text) }}
      </template>
    </template>
    <template #tableTitle>
      <a-button preIcon="ant-design:usergroup-add-outlined" type="primary" @click="addUser" :disabled="tenantPackData.izSysn == '1'">邀请成员</a-button>
      <a-button preIcon="ant-design:rollback-outlined" @click="cancel">关闭</a-button>
    </template>
    <!--操作栏-->
    <template #action="{ record }">
      <TableAction :actions="getTableAction(record)" />
    </template>
  </BasicTable>
  <tenant-user-select-modal :multi="true" @register="registerUserModal" @on-select="onSelected" :tenantId="getTenantId"></tenant-user-select-modal>
</template>

<script lang="ts">
  import { defineComponent, defineEmits } from 'vue';
  import { BasicModal } from '@/components/Modal';
  import { BasicTable, TableAction } from '@/components/Table';
  import TenantUserSelectModal from '@/views/system/tenant/components/TenantUserSelectModal.vue';
  import { addTenantPackUser, deleteTenantPackUser } from '@/views/system/tenant/tenant.api';
  //注册table数据
  import { useListPage } from '@/hooks/system/useListPage';
  import { queryTenantPackUserList } from '@/views/system/tenant/tenant.api';
  import { tenantPackUserColumns } from '@/views/system/tenant/tenant.data';
  import { useModal } from '@/components/Modal';
  import { computed, reactive } from 'vue';

  export default defineComponent({
    name: 'TenantUserRightList',
    components: { BasicModal, BasicTable, TableAction, TenantUserSelectModal },
    emits: ['cancel'],
    setup(props, { emit }) {
      const [registerUserModal, { openModal: openUserModal, closeModal: closeUserModal }] = useModal();
      //获取租户id
      const getTenantId = computed(() => {
        return tenantPackData.tenantId;
      });

      //套餐包信息
      const tenantPackData = reactive<any>({});
      const { tableContext } = useListPage({
        tableProps: {
          api: queryTenantPackUserList,
          immediate: false,
          columns: tenantPackUserColumns,
          canResize: false,
          useSearchForm: false,
          beforeFetch: (params) => {
            params.tenantId = tenantPackData.tenantId;
            params.packId = tenantPackData.id;
            params.status = 1;
            return params;
          },
          actionColumn: {
            width: 120,
            fixed: 'right',
          },
        },
      });

      const [registerTable, { reload }, { rowSelection, selectedRowKeys }] = tableContext;

      /**
       * 初始化数据
       * @param value
       */
      function initData(value) {
        console.log('value：：', value);
        Object.assign(tenantPackData, value);
        reload();
      }

      /**
       * 表格操作列
       * @param record
       */
      function getTableAction(record) {
        return [
          {
            label: '移除',
            popConfirm: {
              title: '是否确认移除',
              confirm: handleDelete.bind(null, record),
            },
            ifShow:() => tenantPackData.izSysn != '1'
          },
        ];
      }

      /**
       * 删除
       */
      async function handleDelete(record) {
        let params = {
          packId: record.packId,
          packName: record.packName,
          tenantId: tenantPackData.tenantId,
          userId: record.id,
          realname: record.realname,
        };
        await deleteTenantPackUser(params);
        await reload();
      }

      /**
       * 添加用户弹窗
       */
      function addUser() {
        openUserModal(true, {
          list: [],
        });
      }

      /**
       * 邀请人回调事件
       * @param arr
       */
      async function onSelected(arr) {
        if (arr && arr.length > 0) {
          let names: any[] = [];
          let ids: any[] = [];
          for (let u of arr) {
            names.push(u.realname);
            ids.push(u.id);
          }
          let params = {
            packId: tenantPackData.id,
            packName: tenantPackData.packName,
            tenantId: tenantPackData.tenantId,
            userId: ids.join(','),
            realname: names.join(','),
          };
          await addTenantPackUser(params);
          await reload();
          closeUserModal();
        }
      }

      /**
       * 获取部门/职务名称
       * @param value
       */
      function getName(value) {
        return value.join(',');
      }

      /**
       * 关闭
       */
      function cancel() {
        emit('cancel');
      }

      return {
        getTableAction,
        onSelected,
        registerTable,
        registerUserModal,
        getTenantId,
        rowSelection,
        addUser,
        initData,
        getName,
        cancel,
        tenantPackData,
      };
    },
  });
</script>

<style scoped lang="less"></style>
