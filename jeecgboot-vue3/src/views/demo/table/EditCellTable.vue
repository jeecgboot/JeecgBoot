<template>
  <div class="p-4">
    <BasicTable @register="registerTable" @edit-end="handleEditEnd" @edit-cancel="handleEditCancel" :beforeEditSubmit="beforeEditSubmit" />
  </div>
</template>
<script lang="ts">
  import { defineComponent } from 'vue';
  import { BasicTable, useTable, BasicColumn } from '/@/components/Table';
  import { optionsListApi } from '/@/api/demo/select';

  import { demoListApi } from '/@/api/demo/table';
  import { treeOptionsListApi } from '/@/api/demo/tree';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { mapTableTotalSummary } from "@/utils/common/compUtils";
  const columns: BasicColumn[] = [
    {
      title: '输入框',
      dataIndex: 'name',
      edit: true,
      editComponentProps: {
        prefix: '$',
      },
      width: 200,
    },
    {
      title: '默认输入状态',
      dataIndex: 'name7',
      edit: true,
      editable: true,
      width: 200,
    },
    {
      title: '输入框校验',
      dataIndex: 'name1',
      edit: true,
      // 默认必填校验
      editRule: true,
      width: 200,
    },
    {
      title: '输入框函数校验',
      dataIndex: 'name2',
      edit: true,
      editRule: async (text) => {
        if (text === '2') {
          return '不能输入该值';
        }
        return '';
      },
      width: 200,
    },
    {
      title: '数字输入框',
      dataIndex: 'id',
      edit: true,
      editRule: true,
      editComponent: 'InputNumber',
      width: 200,
    },
    {
      title: '下拉框',
      dataIndex: 'name3',
      edit: true,
      editComponent: 'Select',
      editComponentProps: {
        options: [
          {
            label: 'Option1',
            value: '1',
          },
          {
            label: 'Option2',
            value: '2',
          },
        ],
      },
      width: 200,
    },
    {
      title: '远程下拉',
      dataIndex: 'name4',
      edit: true,
      editComponent: 'ApiSelect',
      editComponentProps: {
        api: optionsListApi,
        resultField: 'list',
        labelField: 'name',
        valueField: 'id',
      },
      width: 200,
    },
    {
      title: '远程下拉树',
      dataIndex: 'name71',
      edit: true,
      editComponent: 'ApiTreeSelect',
      editRule: false,
      editComponentProps: {
        api: treeOptionsListApi,
        resultField: 'list',
      },
      width: 200,
    },
    {
      title: '日期选择',
      dataIndex: 'date',
      edit: true,
      editComponent: 'DatePicker',
      editComponentProps: {
        valueFormat: 'YYYY-MM-DD',
        format: 'YYYY-MM-DD',
      },
      width: 200,
    },
    {
      title: '时间选择',
      dataIndex: 'time',
      edit: true,
      editComponent: 'TimePicker',
      editComponentProps: {
        valueFormat: 'HH:mm',
        format: 'HH:mm',
      },
      width: 200,
    },
    {
      title: '勾选框',
      dataIndex: 'name5',
      edit: true,
      editComponent: 'Checkbox',
      editValueMap: (value) => {
        return value ? '是' : '否';
      },
      width: 200,
    },
    {
      title: '开关',
      dataIndex: 'name6',
      edit: true,
      editComponent: 'Switch',
      editValueMap: (value) => {
        return value ? '开' : '关';
      },
      width: 200,
    },
  ];
  export default defineComponent({
    components: { BasicTable },
    setup() {
      const [registerTable] = useTable({
        title: '可编辑单元格示例',
        api: demoListApi,
        columns: columns,
        showIndexColumn: false,
        bordered: true,
        showSummary: true,
        summaryFunc: onSummary
      }); 

      const { createMessage } = useMessage();

      function handleEditEnd({ record, index, key, value }: Recordable) {
        console.log(record, index, key, value);
        return false;
      }

      // 模拟将指定数据保存
      function feakSave({ value, key, id }) {
        createMessage.loading({
          content: `正在模拟保存${key}`,
          key: '_save_fake_data',
          duration: 0,
        });
        return new Promise((resolve) => {
          setTimeout(() => {
            if (value === '') {
              createMessage.error({
                content: '保存失败：不能为空',
                key: '_save_fake_data',
                duration: 2,
              });
              resolve(false);
            } else {
              createMessage.success({
                content: `记录${id}的${key}已保存`,
                key: '_save_fake_data',
                duration: 2,
              });
              resolve(true);
            }
          }, 2000);
        });
      }

      async function beforeEditSubmit({ record, index, key, value }) {
        console.log('单元格数据正在准备提交', { record, index, key, value });
        return await feakSave({ id: record.id, key, value });
      }

      function handleEditCancel() {
        console.log('cancel');
      }

      function onSummary(tableData: Recordable[]) {
        // 可用工具方法自动计算合计
        const totals = mapTableTotalSummary(tableData, ['id']);
        return [
          totals
        ];
      }
      return {
        registerTable,
        handleEditEnd,
        handleEditCancel,
        beforeEditSubmit,
      };
    },
  });
</script>
