<template>
  <a-card :bordered="false">
    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" preIcon="ant-design:plus">新增</a-button>
      <!--      <a-button type="primary" preIcon="ant-design:download" @click="handleExportExcel('单表原生列表')">导出</a-button>-->
      <!--      <j-upload-button type="primary" preIcon="ant-design:import-outlined" @click="handleImportExcel">导入</j-upload-button>-->
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <template #overlay>
          <a-menu>
            <a-menu-item key="1" @click="batchDel">
              <Icon icon="ant-design:delete-outlined"></Icon>
              删除
            </a-menu-item>
          </a-menu>
        </template>
        <a-button
          >批量操作
          <Icon icon="mdi:chevron-down"></Icon>
        </a-button>
      </a-dropdown>
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{ selectedRowKeys.length }}</a
        >项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      <a-table
        ref="table"
        size="middle"
        :scroll="{ x: true }"
        bordered
        rowKey="id"
        class="j-table-force-nowrap"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
        @change="handleTableChange"
      >
        <template #bodyCell="{ column, text, record }">
          <template v-if="column.dataIndex==='tupian'">
            <span v-if="!text" style="font-size: 12px; font-style: italic">无图片</span>
            <img v-else :src="getImgView(text)" :preview="record.id" alt="" class="anty-img-wrap" />
          </template>
          <template v-else-if="column.dataIndex==='wenjian'">
            <span v-if="!text" style="font-size: 12px; font-style: italic">无文件</span>
            <a-button v-else :ghost="true" type="primary" preIcon="ant-design:download" size="small" @click="downloadFile(text)"> 下载 </a-button>
          </template>
          <template v-else-if="column.dataIndex==='action'">
            <a @click="handleEdit(record)">编辑</a>
            <a-divider type="vertical" />
            <a-dropdown>
              <!-- update-begin--author:liaozhiyang---date:20230803---for：【QQYUN-5838】图标改小保持一致 -->
              <a class="ant-dropdown-link">更多 <Icon icon="mdi-light:chevron-down"></Icon></a>
              <!-- update-end--author:liaozhiyang---date:20230803---for：【QQYUN-5838】图标改小保持一致 -->
              <template #overlay>
                <a-menu class="antd-more">
                  <a-menu-item>
                    <a @click="handleDetail(record)">详情</a>
                  </a-menu-item>
                  <a-menu-item>
                    <Popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                      <a>删除</a>
                    </Popconfirm>
                  </a-menu-item>
                </a-menu>
              </template>
            </a-dropdown>
          </template>
          <!-- <template v-else-if="column.dataIndex==='htmlSlot'">
            <div v-html="text"></div>
          </template>
          <template v-else-if="column.dataIndex==='pcaSlot'">
            <div>{{ getAreaTextByCode(text) }}</div>
          </template> -->
        </template>
      </a-table>
    </div>
    <OneNativeModal ref="oneProtogenesisModal" @ok="handleSuccess"></OneNativeModal>
  </a-card>
</template>

<script lang="ts" setup>
  import '../less/TableExpand.less';
  import { onMounted, ref, reactive } from 'vue';
  import { defHttp } from '/@/utils/http/axios';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { filterMultiDictText } from '/@/utils/dict/JDictSelectUtil.js';
  import { getAreaTextByCode } from '/@/components/Form/src/utils/Area';
  import OneNativeModal from './components/OneNativeModal.vue';
  import { Modal, Popconfirm } from 'ant-design-vue';
  import { JSelectUserByDept, JDictSelectTag, JSelectDept, JSearchSelect } from '/@/components/Form';
  import Icon from '/@/components/Icon/index';
  import { filterObj, getFileAccessHttpUrl } from '/@/utils/common/compUtils';
  import { loadCategoryData } from '/@/api/common/api';
  import { getToken } from '/@/utils/auth';
  import { useMethods } from '/@/hooks/system/useMethods';
  import { downloadFile } from '/@/utils/common/renderUtils';
  import { initDictOptions } from '/@/utils/dict';

  const { handleExportXls, handleImportXls } = useMethods();
  const modalVisible = ref<boolean>(false);
  const queryParam = ref<any>({});
  const loading = ref<boolean>(false);
  const dictOptions = ref<any>([]);
  const oneProtogenesisModal = ref();
  const tokenHeader = { 'X-Access-Token': getToken() };
  //表头
  const columns = ref<any>([
    {
      title: '文本',
      align: 'center',
      dataIndex: 'name',
    },
    {
      title: '字典下拉',
      align: 'center',
      dataIndex: 'xiala',
      customRender: ({ text }) => (text ? filterMultiDictText(dictOptions.value['xiala'], text) : ''),
    },
    {
      title: '字典单选',
      align: 'center',
      dataIndex: 'danxuan',
      customRender: ({ text }) => (text ? filterMultiDictText(dictOptions.value['danxuan'], text) : ''),
    },
    {
      title: '字典多选',
      align: 'center',
      dataIndex: 'duoxuan',
      customRender: ({ text }) => (text ? filterMultiDictText(dictOptions.value['duoxuan'], text) : ''),
    },
    {
      title: '开关',
      align: 'center',
      dataIndex: 'kaiguan',
      customRender: ({ text }) => (text ? filterMultiDictText(dictOptions.value['kaiguan'], text) : ''),
    },
    {
      title: '日期',
      align: 'center',
      dataIndex: 'riqi',
      customRender: function ({ text }) {
        return !text ? '' : text.length > 10 ? text.substr(0, 10) : text;
      },
    },
    {
      title: '年月日时分秒',
      align: 'center',
      dataIndex: 'nyrsfm',
    },
    {
      title: '时间',
      align: 'center',
      dataIndex: 'shijian',
    },
    {
      title: '文件',
      align: 'center',
      dataIndex: 'wenjian',
    },
    {
      title: '图片',
      align: 'center',
      dataIndex: 'tupian',
    },
    {
      title: '操作',
      dataIndex: 'action',
      align: 'center',
      fixed: 'right',
      width: 147,
    },
  ]);

  const Api = reactive<any>({
    list: '/test/jeecgDemo/oneNative/list',
    delete: '/test/jeecgDemo/oneNative/delete',
    exportXls: '/test/jeecgDemo/oneNative/exportXls',
    importExcel: 'test/jeecgDemo/oneNative/importExcel',
  });

  const dataSource = ref<any>([]);
  const toggleSearchStatus = ref<boolean>(false);
  const ipagination = ref<any>({
    current: 1,
    pageSize: 10,
    pageSizeOptions: ['10', '20', '30'],
    showTotal: (total, range) => {
      return range[0] + '-' + range[1] + ' 共' + total + '条';
    },
    showQuickJumper: true,
    showSizeChanger: true,
    total: 0,
  });

  const selectedRowKeys = ref<any>([]);
  const selectionRows = ref<any>([]);
  const iSorter = ref<any>({ column: 'createTime', order: 'desc' });
  const iFilters = ref<any>({});
  const { createMessage } = useMessage();

  /**
   * 复选框选中事件
   * @param rowKeys
   * @param rows
   */
  function onSelectChange(rowKeys, rows) {
    selectedRowKeys.value = rowKeys;
    selectionRows.value = rows;
  }

  /**
   * 表格改变事件
   */
  function handleTableChange({ pagination, filters, sorter }) {
    ipagination.value = pagination;
    iSorter.value = sorter;
    iFilters.value = { ...filters };
  }

  /**
   * 新增
   */
  function handleAdd() {
    oneProtogenesisModal.value.disableSubmit = false;
    oneProtogenesisModal.value.add();
  }

  /**
   * 清除选中行
   */
  function onClearSelected() {
    selectedRowKeys.value = [];
    selectionRows.value = [];
  }

  /**
   * 批量删除
   */
  function batchDel() {
    Modal.confirm({
      title: '确认删除',
      content: '是否删除选中数据',
      okText: '确认',
      cancelText: '取消',
      onOk: () => {
        defHttp.delete({ url: Api.delete, data: { ids: selectedRowKeys.value } }, { joinParamsToUrl: true }).then(() => {
          handleSuccess();
        });
      },
    });
  }

  /**
   * 导出excel
   */
  function handleExportExcel(title) {
    let paramsForm = getQueryParams();
    if (selectedRowKeys.value && selectedRowKeys.value.length > 0) {
      paramsForm['selections'] = selectedRowKeys.join(',');
    }
    handleExportXls(title, Api.exportXls, filterObj(paramsForm));
  }

  /**
   * 导入excel
   */
  function handleImportExcel(file) {
    handleImportXls(file, Api.importExcel, '').then(() => {
      handleSuccess();
    });
  }

  /**
   * 获取查询参数
   */
  function getQueryParams() {
    let params = Object.assign(queryParam.value, iSorter.value, iFilters.value);
    params.field = getQueryField();
    params.pageNo = ipagination.value.current;
    params.pageSize = ipagination.value.pageSize;
    return filterObj(params);
  }

  /**
   * 字段权限控制
   */
  function getQueryField() {
    let str = 'id,';
    columns.value.forEach(function (value) {
      str += ',' + value.dataIndex;
    });
    return str;
  }

  /**
   * 初始化数据
   */
  function loadData(arg?) {
    if (arg === 1) {
      ipagination.value.current = 1;
    }
    loading.value = true;
    let params = getQueryParams();
    defHttp
      .get({ url: Api.list, params }, { isTransformResponse: false })
      .then((res) => {
        if (res.success) {
          dataSource.value = res.result.records;
          if (res.result && res.result.total) {
            ipagination.value.total = res.result.total;
          } else {
            ipagination.value.total = 0;
          }
        } else {
          createMessage.warning(res.message);
        }
      })
      .finally(() => {
        loading.value = false;
      });
  }

  //查询
  function searchQuery() {
    loadData(1);
    selectedRowKeys.value = [];
    selectionRows.value = [];
  }

  /**
   * 查询区域展开关闭
   */
  function handleToggleSearch() {
    toggleSearchStatus.value = !toggleSearchStatus.value;
  }

  /**
   * 重置按钮
   */
  function searchReset() {
    queryParam.value = {};
    loadData(1);
  }

  /**
   * 获取预览图片
   */
  function getImgView(text) {
    if (text && text.indexOf(',') > 0) {
      text = text.substring(0, text.indexOf(','));
    }
    return getFileAccessHttpUrl(text);
  }

  /**
   * 编辑
   * @param record
   */
  function handleEdit(record) {
    oneProtogenesisModal.value.disableSubmit = false;
    oneProtogenesisModal.value.edit(record);
  }

  /**
   * 详情
   * @param record
   */
  function handleDetail(record) {
    oneProtogenesisModal.value.disableSubmit = true;
    oneProtogenesisModal.value.edit(record);
  }

  /**
   * 删除
   * @param id
   */
  function handleDelete(id) {
    defHttp.delete({ url: Api.delete, data: { ids: id } }, { joinParamsToUrl: true }).then((res) => {
      handleSuccess();
    });
  }

  /**
   * 初始化字典选项
   */
  async function initDictConfig() {
    dictOptions.value['flzds'] = await loadCategoryData({ code: 'B01' });
    dictOptions.value['xiala'] = await initDictOptions('sex');
    dictOptions.value['danxuan'] = await initDictOptions('sex');
    dictOptions.value['duoxuan'] = await initDictOptions('urgent_level');
  }

  /**
   * 保存表单后回调事件
   */
  function handleSuccess() {
    selectedRowKeys.value = [];
    selectionRows.value = [];
    loadData(1);
  }
  onMounted(() => {
    dictOptions.value['kaiguan'] = [
      { text: '是', value: '1' },
      { text: '否', value: '2' },
    ];
    //初始加载页面
    loadData();
    //初始化字典选项
    initDictConfig();
  });
</script>
