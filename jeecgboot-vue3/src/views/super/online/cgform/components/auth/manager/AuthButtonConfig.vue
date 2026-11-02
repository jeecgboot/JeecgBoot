<template>
  <div class="auth-field-config">
    <BasicTable @register="registerTable" :loading="tableLoading">
      <template #headerCell="{ column }">
        <template v-if="column.dataIndex === 'switch'">
          <a-switch
            size="small"
            :checked="allSwitch"
            :loading="allSwitchLoading"
            :disabled="!buttonDataSource.length"
            @change="handleChangeAllSwitch"
          />
          启用
        </template>
        <template v-else>
          {{ column.customTitle }}
        </template>
      </template>
      <template #switch="{ record }">
        <a-switch size="small" :checked="record.status === 1" :disabled="allSwitchLoading" @change="(flag) => onUpdateStatus(flag, record)" />
      </template>

      <template #control> 可见 </template>
    </BasicTable>
  </div>
</template>

<script lang="ts">
  import { defineComponent, ref, watch } from 'vue';
  import { cloneDeep } from 'lodash-es';
  import { BasicTable, useTable } from '/@/components/Table';
  import { authButtonLoadData, authButtonEnable, authButtonDisable, batchAuthButtonUpdateStatus } from '../auth.api';
  import { authButtonColumns, authButtonFixedList } from '../auth.data';

  export default defineComponent({
    name: 'AuthButtonConfig',
    components: { BasicTable },
    props: {
      headId: {
        type: String,
        default: '',
        required: true,
      },
      // 1单表 2主表 3附表
      tableType: {
        type: Number,
        default: 1,
      },
      isTree: {
        type: String,
        default: 'N',
      },
    },
    setup(props) {
      const cgformId = ref('');
      const pageType = ref(2);
      const pageControlList = ref(3);
      const pageControlForm = ref(5);
      const allSwitch = ref(false);
      const allSwitchLoading = ref(false);
      const tableLoading = ref(false);
      const buttonDataSource = ref<Recordable[]>([]);
      const [registerTable, { reload, getTableRef, setPagination }] = useTable({
        api: loadData,
        rowKey: 'code',
        bordered: true,
        columns: authButtonColumns,
        showIndexColumn: false,
      });

      watch(
        () => props.headId,
        (headId) => {
          cgformId.value = headId.split('?')[0];
          // update-begin--author:liaozhiyang---date:20240520---for：【TV360X-149】点击权限控制进入页面后，分页没有重置
          getTableRef().value && setPagination({ current: 1, pageSize: 10 });
          // update-end--author:liaozhiyang---date:20240520---for：【TV360X-149】点击权限控制进入页面后，分页没有重置
          reload().catch(() => null);
        },
        { immediate: true }
      );

      /**
       * 获取内置按钮
       * liaozhiyang
       * 2024-06-14
       * 【TV360X-1017】子表权限管理按钮权限根据主表主题模版只显示所需按钮
       * */
      const getButtonList = (res) => {
        const buttons = cloneDeep(authButtonFixedList).filter((item) => item.code !== 'addSub' || props.isTree === 'Y');
        if (res.mainRelationType != null && res.mainThemeTemplate != null && props.tableType == 3) {
          // 子表（一对一、一对多）
          let result: any = [];
          switch (res.mainThemeTemplate) {
            case 'normal':
            case 'innerTable':
            case 'tab':
              if (res.mainRelationType == 1) {
                // 一对一
                result = [];
              } else {
                // 一对多：add(新增)、popup_add(弹窗新增)、update(子表编辑)、batch_delete(批量删除)
                result = buttons.filter((item) => ['add', 'popup_add', 'update', 'batch_delete'].includes(item.code));
              }
              break;
            case 'erp':
              // ERP 附表不展示高级查询、提交流程
              result = buttons.filter((item) => !['super_query', 'bpm'].includes(item.code));
              break;
          }
          return result;
        } else {
          // 主表\单表：过滤掉子表专用的弹窗按钮权限code（popup_add弹窗新增、popup_update弹窗编辑、update弹窗编辑）
          // update也是列表页编辑按钮的老code（useListButton.ts:187双重门控判断），单表/主表上启用会导致编辑按钮被隐藏
          return buttons.filter((item) => !['popup_add', 'popup_update', 'update'].includes(item.code));
        }
      };

      // 加载数据
      async function loadData(params) {
        let result = await authButtonLoadData(cgformId.value, params);
        let { authList, buttonList } = result;
        let dataSource: Recordable[] = [];
        // concat 固定按钮
        // -update-begin--author:liaozhiyang---date:20240614---for：【TV360X-1017】子表权限管理按钮权限根据主表主题模版只显示所需按钮
        // 获取内置按钮
        const buttons = getButtonList(result);
        // -update-end--author:liaozhiyang---date:20240614---for：【TV360X-1017】子表权限管理按钮权限根据主表主题模版只显示所需按钮
        for (let btn of buttons) {

          // 去除重复数据
          const findBtnIdx = buttonList.findIndex((item) => item.buttonCode === btn.code);
          const findBtn: Recordable = {}
          if (findBtnIdx !== -1) {
            findBtn.title = buttonList[findBtnIdx].buttonName;
            buttonList.splice(findBtnIdx, 1);
          }

          let item = {
            status: 0,
            page: pageControlList.value,
          };
          let auth = authList.find((auth) => auth.code == btn.code);
          Object.assign(btn, item, auth, findBtn);
          dataSource.push(btn);
        }
        // update-begin--author:liaozhiyang---date:20240520---for：【TV360X-187】去掉子表权限管理中按钮权限的高级查询
        if (props.tableType == 3) {
          const findIndex = dataSource.findIndex((item) => item.code === 'super_query');
          if (findIndex != -1) {
            dataSource.splice(findIndex, 1);
          }
        }
        // update-end--author:liaozhiyang---date:20240520---for：【TV360X-187】去掉子表权限管理中按钮权限的高级查询
        // update-begin--author:liaozhiyang---date:20250403---for：【QQYUN-11801】生成测试数据
        if ([2, 3].includes(+props.tableType)) {
          const findIndex = dataSource.findIndex((item) => item.code == 'aigc_mock_data');
          if (findIndex != -1) {
            dataSource.splice(findIndex, 1);
          }
        }
        // update-end--author:liaozhiyang---date:20250403---for：【QQYUN-11801】生成测试数据
        // concat 查询的自定义按钮
        const resultData = concatCustomButton(authList, buttonList, dataSource);
        buttonDataSource.value = resultData;
        syncAllSwitchStatus();
        return resultData;
      }

      function concatCustomButton(authList: any[], buttonList: any[], dataSource: any[]) {
        for (let btn of buttonList) {
          //update-begin-author:taoyan date:2022-5-25 for: VUEN-1103 自定义按钮，开启权限控制后，再打开未保存上
          let auth = authList.find((auth) => auth.code == btn.buttonCode);
          //update-end-author:taoyan date:2022-5-25 for: VUEN-1103 自定义按钮，开启权限控制后，再打开未保存上
          let item = {
            code: btn.buttonCode,
            title: btn.buttonName,
            status: 0,
            page: btn.buttonStyle == 'form' ? pageControlForm.value : pageControlList.value,
          };
          dataSource.push(Object.assign(item, auth));
        }
        return dataSource;
      }

      async function onUpdateStatus(flag, record) {
        await (flag ? doEnableAuthButton(record) : doDisableAuthButton(record));
        syncAllSwitchStatus();
      }

      function syncAllSwitchStatus() {
        allSwitch.value = buttonDataSource.value.length > 0 && buttonDataSource.value.every((item) => item.status === 1);
      }

      async function handleChangeAllSwitch(checked) {
        const changedButtons = buttonDataSource.value.filter((item) => (checked ? item.status !== 1 : item.status === 1));
        if (!changedButtons.length) return;
        allSwitchLoading.value = true;
        tableLoading.value = true;
        try {
          await batchAuthButtonUpdateStatus(
            changedButtons.map((item) => ({
              id: item.id,
              cgformId: cgformId.value,
              code: item.code,
              control: 5,
              page: item.page,
              status: checked ? 1 : 0,
            }))
          );
        } finally {
          await reload().catch(() => null);
          allSwitchLoading.value = false;
          tableLoading.value = false;
        }
      }

      // 启用按钮权限
      async function doEnableAuthButton(record: Recordable) {
        let result = await authButtonEnable({
          id: record.id,
          code: record.code,
          page: record.page,
          cgformId: cgformId.value,
          type: pageType.value,
          control: 5,
          status: 1,
        });
        record.id = result.id;
        record.status = 1;
      }

      // 禁用按钮权限
      async function doDisableAuthButton(record: Recordable) {
        await authButtonDisable(record.id);
        record.status = 0;
      }

      return { registerTable, onUpdateStatus, allSwitch, allSwitchLoading, tableLoading, buttonDataSource, handleChangeAllSwitch };
    },
  });
</script>

<style lang="less" scoped></style>
