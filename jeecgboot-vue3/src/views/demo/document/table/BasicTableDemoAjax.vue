<template>
  <div class="p-4">
    <!--定义表格-->
    <BasicTable @register="registerTable">
      <!-- 搜索区域插槽自定义查询 -->
      <template #form-email="{ model, field }">
        <a-input placeholder="请输入邮箱" v-model:value="model[field]" addon-before="邮箱:" addon-after=".com"></a-input>
      </template>
      <!--操作栏-->
      <template #action="{ record }">
        <TableAction :actions="getTableAction(record)" />
      </template>
    </BasicTable>
  </div>
</template>

<script lang="ts" name="basic-table-demo" setup>
  import { ActionItem, BasicColumn, BasicTable, FormSchema, TableAction } from '/@/components/Table';
  import { useListPage } from '/@/hooks/system/useListPage';
  import { defHttp } from '/@/utils/http/axios';
  //定义表格列
  const columns: BasicColumn[] = [
    {
      title: '姓名',
      dataIndex: 'name',
      width: 170,
      align: 'left',
      resizable: true,
      sorter: {
        multiple: 1,
      },
    },
    {
      title: '关键词',
      dataIndex: 'keyWord',
      width: 130,
      resizable: true,
    },
    {
      title: '打卡时间',
      dataIndex: 'punchTime',
      width: 140,
      resizable: true,
    },
    {
      title: '工资',
      dataIndex: 'salaryMoney',
      width: 140,
      resizable: true,
      sorter: {
        multiple: 2,
      },
    },
    {
      title: '奖金',
      dataIndex: 'bonusMoney',
      width: 140,
      resizable: true,
    },
    {
      title: '性别',
      dataIndex: 'sex',
      sorter: {
        multiple: 3,
      },
      filters: [
        { text: '男', value: '1' },
        { text: '女', value: '2' },
      ],
      customRender: ({ record }) => {
        return record.sex ? (record.sex == '1' ? '男' : '女') : '';
      },
      width: 120,
      resizable: true,
    },
    {
      title: '生日',
      dataIndex: 'birthday',
      width: 120,
      resizable: true,
    },
    {
      title: '邮箱',
      dataIndex: 'email',
      width: 120,
      resizable: true,
    },
  ];
  //表单搜索字段
  const searchFormSchema: FormSchema[] = [
    {
      label: '姓名', //显示label
      field: 'name', //查询字段
      component: 'JInput', //渲染的组件
      defaultValue: '苏榕润', //设置默认值
    },
    {
      label: '性别',
      field: 'sex',
      component: 'JDictSelectTag',
      componentProps: {
        //渲染的组件的props
        dictCode: 'sex',
        placeholder: '请选择性别',
      },
    },
    {
      label: '邮箱',
      field: 'email',
      component: 'JInput',
      slot: 'email',
    },
    {
      label: '生日',
      field: 'birthday',
      component: 'DatePicker',
    },
  ];
  //ajax请求api接口
  const demoListApi = (params) => {
    return defHttp.get({ url: '/test/jeecgDemo/list', params });
  };
  // 列表页面公共参数、方法
  const { tableContext } = useListPage({
    designScope: 'basic-table-demo-filter',
    tableProps: {
      title: '用户列表',
      api: demoListApi,
      columns: columns,
      formConfig: {
        schemas: searchFormSchema,
      },
      useSearchForm: true,
    },
  });
  //BasicTable绑定注册
  const [registerTable, { getForm }] = tableContext;
  /**
   * 操作栏
   */
  function getTableAction(record): ActionItem[] {
    return [
      {
        label: '编辑',
        onClick: handleEdit.bind(null, record),
      },
    ];
  }

  function handleEdit(record) {
    let { getFieldsValue } = getForm();
    console.log('查询form的数据', getFieldsValue());
    console.log(record);
  }
</script>

<style scoped></style>
