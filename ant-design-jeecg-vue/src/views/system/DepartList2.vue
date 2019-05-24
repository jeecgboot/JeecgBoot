<template>
  <a-card :bordered="false">

    <!-- 查询区域 -->
    <!--
   -->
    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>

      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel">
            <a-icon type="delete"/>
            删除
          </a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作
          <a-icon type="down"/>
        </a-button>
      </a-dropdown>
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>
      <a-table
        ref="table"
        size="middle"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        @change="handleTableChange">

        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical"/>
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down"/></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a href="javascript:;" @click="handleDetail(record)">详情</a>
              </a-menu-item>
              <a-menu-item>
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>
    <!-- table区域-end -->

    <!-- 表单区域 -->
    <sysDepart-modal ref="sysDepartModal" @ok="modalFormOk"></sysDepart-modal>
  </a-card>
</template>

<script>
  import SysDepartModal from './modules/DepartModal'
  /*  import { filterObj } from '@/utils/util'
    , queryByFactories*/
  import {queryDepartTreeList} from '@/api/api'
  import {deleteAction} from '@/api/manage'

  // 表头
  const columns = [
    {
      title: '机构名称',
      dataIndex: 'departName',
    },
    {
      title: '机构类型',
      align: "center",
      dataIndex: 'orgType'
    },
    {
      title: '机构编码',
      dataIndex: 'orgCode'
    },
    {
      title: '手机号',
      dataIndex: 'mobile'
    },
    {
      title: '传真',
      dataIndex: 'fax'
    },
    {
      title: '地址',
      dataIndex: 'address'
    },
    {
      title: '排序',
      align: 'center',
      dataIndex: 'departOrder'
    },
    {
      title: '操作',
      align: "center",
      dataIndex: 'action',
      scopedSlots: {customRender: 'action'},
    }
  ];

  export default {
    name: "DepartList2",
    components: {
      SysDepartModal
    },
    data() {
      return {
        description: 'jeecg 生成SysDepart代码管理页面',
        // 查询条件
        queryParam: {},
        //数据集
        factories: '',
        dataSource: [],
        columns: columns,
        // 分页参数
        /*        ipagination:{
                  current: 1,
                  pageSize: 5,
                  pageSizeOptions: ['5', '10', '20'],
                  showTotal: (total, range) => {
                    return range[0] + "-" + range[1] + " 共" + total + "条"
                  },
                  showQuickJumper: true,
                  showSizeChanger: true,
                  total: 0
                },*/
        isorter: {
          column: 'createTime',
          order: 'desc',
        },
        loading: false,
        selectedRowKeys: [],
        selectedRows: [],
        url: {
          list: "/sysdepart/sysDepart/list",
          delete: "/sysdepart/sysDepart/delete",
          deleteBatch: "/sysdepart/sysDepart/deleteBatch",
        },

      }
    },
    created() {
      this.loadData();
    },
    methods: {
      loadData() {
        this.dataSource = [];
        queryDepartTreeList().then((res) => {
          if (res.success) {
            this.dataSource = res.result;
          }
        })

      },

      getQueryField() {
        //TODO 字段权限控制
        var str = "id,";
        for (var a = 0; a < this.columns.length; a++) {
          str += "," + this.columns[a].dataIndex;
        }
        return str;
      },
      onSelectChange(selectedRowKeys, selectionRows) {
        this.selectedRowKeys = selectedRowKeys;
        this.selectionRows = selectionRows;
      },
      onClearSelected() {
        this.selectedRowKeys = [];
        this.selectionRows = [];
      },
//TODO getQueryParams
      handleDelete: function (id) {
        var that = this;
        deleteAction(that.url.delete, {id: id}).then((res) => {
          if (res.success) {
            that.$message.success(res.message);
            that.loadData();
          } else {
            that.$message.warning(res.message);
          }
        });
      },
      handleDetail(record) {
        this.$refs.sysDepartModal.edit(record);
        this.$refs.sysDepartModal.title = "详情";
        this.$refs.sysDepartModal.disableSubmit = true;
      },
      batchDel: function () {
        if (this.selectedRowKeys.length <= 0) {
          this.$message.warning('请选择一条记录！');
          return;
        } else {
          var ids = "";
          for (var a = 0; a < this.selectedRowKeys.length; a++) {
            ids += this.selectedRowKeys[a] + ",";
          }
          var that = this;
          this.$confirm({
            title: "确认删除",
            content: "是否删除选中数据?",
            onOk: function () {
              deleteAction(that.url.deleteBatch, {ids: ids}).then((res) => {
                if (res.success) {
                  that.$message.success(res.message);
                  that.loadData();
                  that.onClearSelected();
                } else {
                  that.$message.warning(res.message);
                }
              });
            }
          });
        }
      },
      handleEdit: function (record) {
        this.$refs.sysDepartModal.edit(record);
        this.$refs.sysDepartModal.title = "编辑";
      },
      handleAdd() {
        this.$refs.sysDepartModal.add();
        this.$refs.sysDepartModal.title = "新增";
      },
      handleTableChange(pagination, filters, sorter) {
        //分页、排序、筛选变化时触发
        console.log(sorter);
        //TODO 筛选
        if (Object.keys(sorter).length > 0) {
          this.isorter.column = sorter.field;
          this.isorter.order = "ascend" == sorter.order ? "asc" : "desc"
        }
        /*this.ipagination = pagination;*/
        this.loadData();
      },
      modalFormOk() {
        // 新增/修改 成功时，重载列表
        this.loadData();
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less'
</style>