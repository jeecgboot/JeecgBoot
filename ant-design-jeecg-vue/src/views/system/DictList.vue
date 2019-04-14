<template>
  <a-card :bordered="false">

    <!-- 左侧面板 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <a-row :gutter="12">
          <a-col :md="7" :sm="8">
            <a-form-item label="字典名称" :labelCol="{span: 6}" :wrapperCol="{span: 14, offset: 1}">
              <a-input placeholder="请输入字典名称" v-model="queryParam.dictName"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="7" :sm="8">
            <a-form-item label="字典编号" :labelCol="{span: 6}" :wrapperCol="{span: 14, offset: 1}">
              <a-input placeholder="请输入字典编号" v-model="queryParam.dictCode"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="7" :sm="8">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
            </span>
          </a-col>
        </a-row>
        <a-row>
          <a-col :md="24" :sm="24">
            <a-button style="margin-bottom: 20px" @click="handleAdd" type="primary" icon="plus">添加</a-button>
            <a-button type="primary" icon="download" @click="handleExportXls">导出</a-button>
            <a-upload name="file" :showUploadList="false" :multiple="false" :action="importExcelUrl"
                      @change="handleImportExcel">
              <a-button type="primary" icon="import">导入</a-button>
            </a-upload>
          </a-col>
        </a-row>
      </a-form>

      <a-table
        ref="table"
        rowKey="id"
        size="middle"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        @change="handleTableChange">
          <span slot="action" slot-scope="text, record">
            <a @click="handleEdit(record)">
              <a-icon type="edit"/>
              编辑
            </a>
            <a-divider type="vertical"/>
            <a @click="editDictItem(record)"><a-icon type="setting"/> 字典配置</a>
            <a-divider type="vertical"/>
            <a @click="handleDelete(record.id)">删除</a>
          </span>
      </a-table>

    </div>
    <dict-modal ref="modalForm" @ok="modalFormOk"></dict-modal>  <!-- 字典类型 -->
    <dict-item-list ref="dictItemList"></dict-item-list>
  </a-card>
</template>

<script>
  import { filterObj } from '@/utils/util';
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import DictModal from './modules/DictModal'
  import DictItemList from './DictItemList'
  import {delDict} from '@/api/api'

  export default {
    name: "DictList",
    mixins:[JeecgListMixin],
    components: {DictModal, DictItemList},
    data() {
      return {
        description: '这是数据字典页面',
        visible: false,
        // 查询条件
        queryParam: {
          dictCode: "",
          dictName: "",
        },
        // 表头
        columns: [
          {
            title: '#',
            dataIndex: '',
            key: 'rowIndex',
            width: 120,
            align: "center",
            customRender: function (t, r, index) {
              return parseInt(index) + 1;
            }
          },
          {
            title: '字典名称',
            align: "left",
            dataIndex: 'dictName',
          },
          {
            title: '字典编号',
            align: "left",
            dataIndex: 'dictCode',
          },
          {
            title: '描述',
            align: "left",
            dataIndex: 'description',
          },
          {
            title: '操作',
            dataIndex: 'action',
            align: "center",
            scopedSlots: {customRender: 'action'},
          }
        ],
        dict: "",
        labelCol: {
          xs: {span: 8},
          sm: {span: 5},
        },
        wrapperCol: {
          xs: {span: 16},
          sm: {span: 19},
        },
        url: {
          list: "/sys/dict/list",
          delete: "/sys/dict/delete",
          exportXlsUrl: "sys/dict/exportXls",
          importExcelUrl: "sys/dict/importExcel",
        },
      }
    },
    computed: {
      importExcelUrl: function () {
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      }
    },
    methods: {
      getQueryParams() {
        var param = Object.assign({}, this.queryParam, this.isorter);
        param.field = this.getQueryField();
        param.pageNo = this.ipagination.current;
        param.pageSize = this.ipagination.pageSize;
        return filterObj(param);
      },
      //删除字典类型
      handleDel(record) {
        let that = this;
        that.$confirm({
          title: "确认删除",
          content: "您确定要删除 " + record.dictName + " 字典类型?",
          onOk: function () {
            delDict({id: record.id}).then((res) => {
              if (res.success) {
                that.$message.success(res.message);
                that.loadData();
                that.cancelDict();
              } else {
                that.$message.warning(res.message);
              }
            });
          },
        });
      },
      //取消选择
      cancelDict() {
        this.dict = "";
        this.visible = false;
        this.loadData();
      },
      //编辑字典数据
      editDictItem(record) {
        this.$refs.dictItemList.edit(record);
      },
      // 重置字典类型搜索框的内容
      searchReset() {
        var that = this;
        that.queryParam.dictName = "";
        that.queryParam.dictCode = "";
        that.loadData(this.ipagination.current);
      },
    },
    watch: {
      openKeys(val) {
        console.log('openKeys', val)
      },
    },
  }
</script>

<style scoped>
  /** Button按钮间距 */
  .ant-btn {
    margin-left: 3px
  }

  .table-operator {
    margin-bottom: 10px
  }

  .ant-tree li span.ant-tree-switcher, .ant-tree li span.ant-tree-iconEle {
    width: 0px
  }
</style>