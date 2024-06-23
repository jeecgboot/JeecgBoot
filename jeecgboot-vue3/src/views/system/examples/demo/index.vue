<template>
  <div>
    <!--自定义查询区域-->
    <div class="jeecg-basic-table-form-container" @keyup.enter="searchQuery" v-if="customSearch">
      <a-form ref="formRef" :model="queryParam" :label-col="labelCol" :wrapper-col="wrapperCol">
        <a-row :gutter="24">
          <a-col :lg="8">
            <a-form-item label="用户名">
              <a-input placeholder="请输入名称模糊查询" v-model:value="queryParam.name"></a-input>
            </a-form-item>
          </a-col>
          <a-col :lg="8">
            <a-form-item label="年龄">
              <a-input placeholder="最小年龄" type="ge" v-model:value="queryParam.age_begin" style="width: calc(50% - 15px)"></a-input>
              <span>~</span>
              <a-input placeholder="最大年龄" type="le" v-model:value="queryParam.age_end" style="width: calc(50% - 15px)"></a-input>
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">
            <a-col :lg="8">
              <a-form-item label="性别">
                <JDictSelectTag v-model:value="queryParam.sex" placeholder="请选择性别" dictCode="sex" />
              </a-form-item>
            </a-col>
            <a-col :lg="8">
              <a-form-item label="选择用户">
                <JDictSelectTag v-model:value="queryParam.id" placeholder="请选择用户" dictCode="demo,name,id" />
              </a-form-item>
            </a-col>
          </template>
          <span style="float: left; overflow: hidden" class="table-page-search-submitButtons">
            <a-col :lg="6">
              <a-button type="primary" preIcon="ant-design:search-outlined" @click="searchQuery">查询</a-button>
              <a-button type="primary" preIcon="ant-design:reload-outlined" @click="searchReset" style="margin-left: 8px">重置</a-button>
              <a @click="toggleSearchStatus = !toggleSearchStatus" style="margin-left: 8px">
                {{ toggleSearchStatus ? '收起' : '展开' }}
                <Icon :icon="toggleSearchStatus ? 'ant-design:up-outlined' : 'ant-design:down-outlined'" />
              </a>
            </a-col>
          </span>
        </a-row>
      </a-form>
    </div>
    <BasicTable @register="registerTable" :rowSelection="rowSelection" :class="{ 'p-4': customSearch }">
      <template #form-age="{ model, field }">
        <a-input placeholder="最小年龄" type="ge" v-model:value="min" style="width: calc(50% - 15px)" @change="ageChange(model, field)"></a-input>
        <span>~</span>
        <a-input placeholder="最大年龄" type="le" v-model:value="max" style="width: calc(50% - 15px)" @change="ageChange(model, field)"></a-input>
      </template>
      <template #tableTitle>
        <a-button preIcon="ant-design:plus-outlined" type="primary" @click="handleAdd">新增</a-button>
        <a-upload name="file" :showUploadList="false" :customRequest="(file) => handleImportXls(file, getImportUrl, reload)">
          <a-button preIcon="ant-design:import-outlined" type="primary">导入</a-button>
        </a-upload>
        <a-button preIcon="ant-design:export-outlined" type="primary" @click="handleExportXls('单表示例', getExportUrl,exportParams)">导出</a-button>
        <a-button preIcon="ant-design:filter" type="primary" @click="">高级查询</a-button>
        <a-button preIcon="ant-design:plus-outlined" type="primary" @click="openTab">打开Tab页</a-button>
        <a-button preIcon="ant-design:retweet-outlined" type="primary" @click="customSearch = !customSearch">{{
          customSearch ? '表单配置查询' : '自定义查询'
        }}</a-button>
        <a-button preIcon="ant-design:import-outlined" type="primary" @click="handleImport">弹窗导入</a-button>

        <super-query :config="superQueryConfig" @search="handleSuperQuery"/>

        <a-dropdown v-if="checkedKeys.length > 0">
          <template #overlay>
            <a-menu>
              <a-menu-item key="1" @click="batchHandleDelete">
                <Icon icon="ant-design:delete-outlined"></Icon>
                删除
              </a-menu-item>
            </a-menu>
          </template>
          <a-button
            >批量操作
            <Icon style="fontsize: 12px" icon="ant-design:down-outlined"></Icon>
          </a-button>
        </a-dropdown>
      </template>
      <template #action="{ record }">
        <TableAction :actions="getActions(record)" />
      </template>
    </BasicTable>
    <DemoModal @register="registerModal" @success="reload" :isDisabled="isDisabled"/>
    <JImportModal @register="registerModalJimport" :url="getImportUrl" online />
  </div>
</template>
<script lang="ts" setup>
  import { ref, unref, reactive, toRaw, watch,computed } from 'vue';
  import { BasicTable, useTable, TableAction } from '/@/components/Table';
  import { useModal } from '/@/components/Modal';
  import DemoModal from './DemoModal.vue';
  import JImportModal from '/@/components/Form/src/jeecg/components/JImportModal.vue';
  import JDictSelectTag from '/@/components/Form/src/jeecg/components/JDictSelectTag.vue';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { useMethods } from '/@/hooks/system/useMethods';
  import { getDemoList, deleteDemo, batchDeleteDemo, getExportUrl, getImportUrl } from './demo.api';
  import { columns, searchFormSchema } from './demo.data';
  import { useGo } from '/@/hooks/web/usePage';
  import { router } from '/@/router';
  import { filterObj } from '/@/utils/common/compUtils';
  
  const go = useGo();
  const checkedKeys = ref<Array<string | number>>([]);
  const [registerModal, { openModal }] = useModal();
  const [registerModalJimport, { openModal: openModalJimport }] = useModal();
  const { handleExportXls, handleImportXls } = useMethods();
  const min = ref();
  const max = ref();
  const isDisabled = ref(false);
  
  const [registerTable, { reload, setProps }] = useTable({
    title: '单表示例',
    api: getDemoList,
    columns,
    formConfig: {
      //labelWidth: 120,
      schemas: searchFormSchema,
      fieldMapToTime: [['birthday', ['birthday_begin', 'birthday_end'], 'YYYY-MM-DD']],
      fieldMapToNumber: [['age', ['age_begin', 'age_end']]],
      autoAdvancedCol: 2,
      actionColOptions: {
        style: { textAlign: 'left' },
      },
    },
    //自定义默认排序
    defSort: {
      column: 'createTime,sex',
      order: 'desc',
    },
    striped: true,
    useSearchForm: true,
    showTableSetting: true,
    clickToRowSelect: false,
    bordered: true,
    showIndexColumn: false,
    tableSetting: { fullScreen: true },
    canResize: false,
    rowKey: 'id',
    actionColumn: {
      width: 180,
      title: '操作',
      dataIndex: 'action',
      slots: { customRender: 'action' },
      fixed: undefined,
    },
  });
  /**
   * 选择列配置
   */
  const rowSelection = {
    type: 'checkbox',
    columnWidth: 40,
    selectedRowKeys: checkedKeys,
    onChange: onSelectChange,
  };

  function handleImport() {
    openModalJimport(true);
  }

  const exportParams = computed(()=>{
    let paramsForm = {};
    if (checkedKeys.value && checkedKeys.value.length > 0) {
      paramsForm['selections'] = checkedKeys.value.join(',');
    }
    return filterObj(paramsForm)
  })
  /**
   * 操作列定义
   * @param record
   */
  function getActions(record) {
    return [
      {
        label: '编辑',
        onClick: handleEdit.bind(null, record),
      },
      {
        label: '详情',
        onClick: handleDetail.bind(null, record),
      },
      {
        label: '删除',
        popConfirm: {
          title: '是否确认删除',
          confirm: handleDelete.bind(null, record),
        },
      },
    ];
  }

  /**
   * 选择事件
   */
  function onSelectChange(selectedRowKeys: (string | number)[]) {
    console.log("checkedKeys------>",checkedKeys)
    checkedKeys.value = selectedRowKeys;
  }

  /**
   * 新增事件
   */
  function handleAdd() {
    isDisabled.value = false;
    openModal(true, {
      isUpdate: false,
    });
  }

  /**
   * 编辑事件
   */
  function handleEdit(record) {
    isDisabled.value = false;
    openModal(true, {
      record,
      isUpdate: true,
    });
  }

  /**
   * 详情页面
   */
  function handleDetail(record) {
    isDisabled.value = true;
    openModal(true, {
      record,
      isUpdate: true,
    });
  }

  /**
   * 删除事件
   */
  async function handleDelete(record) {
    await deleteDemo({ id: record.id }, reload);
  }

  /**
   * 批量删除事件
   */
  async function batchHandleDelete() {
    await batchDeleteDemo({ ids: checkedKeys.value }, reload);
  }
  /**
   * 年龄修改事件
   */
  function ageChange(model, field) {
    model[field] = [unref(min), unref(max)];
  }

  /**
   * 打开tab页面
   */
  function openTab() {
    go(`/comp/jeecg/basic`);
  }
  //-----自定义查询----begin--------
  const formElRef = ref();
  const labelCol = reactive({
    xs: { span: 24 },
    sm: { span: 7 },
  });
  const wrapperCol = reactive({
    xs: { span: 24 },
    sm: { span: 16 },
  });
  const toggleSearchStatus = ref(false);
  const customSearch = ref(false);
  const queryParam = reactive({
    name: '',
    age_begin: '',
    age_end: '',
    sex: '',
    id: '',
  });
  watch(customSearch, () => {
    setProps({ useSearchForm: !unref(customSearch) });
  });
  function searchQuery() {
    setProps({ searchInfo: toRaw(queryParam) });
    reload();
  }
  function searchReset() {
    Object.assign(queryParam, { name: '', age_begin: '', age_end: '', sex: '', id: '' });
    reload();
  }
  //自定义查询----end---------

  const superQueryConfig = reactive({
    name:{ title: "名称", view: "text", type: "string", order: 1 },
    sex:{ title: "性别", view: "list", type: "string", dictCode:'sex', order: 2 },
  });
  
  function handleSuperQuery(params) {
    Object.keys(params).map(k=>{
      queryParam[k] = params[k]
    });
    searchQuery();
  }
</script>
<style lang="less" scoped>
  .jeecg-basic-table-form-container {
    .table-page-search-submitButtons {
      display: block;
      margin-bottom: 24px;
      white-space: nowrap;
    }
  }
</style>
