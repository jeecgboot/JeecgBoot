<template>
  <a-card :bordered="false">

    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <a-row :gutter="24">

          <a-col :md="6" :sm="24">
            <a-form-item label="订单号">
              <a-input placeholder="请输入订单号" v-model="queryParam.orderCode"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="24">
            <a-form-item label="订单类型">
              <a-select placeholder="请输入订单类型" v-model="queryParam.ctype">
                <a-select-option value="1">国内订单</a-select-option>
                <a-select-option value="2">国际订单</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>

          <a-col :md="6" :sm="24">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
            </span>
          </a-col>

        </a-row>
      </a-form>
    </div>

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
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="false"
        :expandedRowKeys= "expandedRowKeys"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        @change="handleTableChange"
        @expand="handleExpand"
      >

        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>
          <a-divider type="vertical"/>
          <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
            <a>删除</a>
          </a-popconfirm>
        </span>
        <a-table
          slot="expandedRowRender"
          slot-scope="text"
          :columns="innerColumns"
          :dataSource="innerData"
          size="middle"
          bordered
          rowKey="id"
          :pagination="false"
          :loading="loading"
          >
        </a-table>
      </a-table>
    </div>
    <!-- table区域-end -->

    <!-- 表单区域 -->
    <jeecgOrderDMain-modal ref="modalForm" @ok="modalFormOk"></jeecgOrderDMain-modal>
  </a-card>
</template>

<script>
  import { getAction } from '@/api/manage'
  import JeecgOrderDMainModal from '@/views/jeecg/tablist/form/JeecgOrderDMainModal'
  import {JeecgListMixin} from '@/mixins/JeecgListMixin'

  export default {
    name: "TableDemo",
    mixins: [JeecgListMixin],
    components: {
      JeecgOrderDMainModal
    },
    data() {
      return {
        // 子表表头
        innerColumns: [
          {
            title: '客户名',
            align: "center",
            width: 100,
            dataIndex: 'name',
            key: 'name',
          },
          {
            title: '性别',
            align: "center",
            dataIndex: 'sex',
            customRender: function (text) {
              if (text == 1) {
                return "男";
              } else if (text == 2) {
                return "女";
              } else {
                return text;
              }
            }
          },
          {
            title: '身份证号码',
            align: "center",
            dataIndex: 'idcard',
          },
          {
            title: '电话',
            dataIndex: 'telphone',
            align: "center",
          },

        ],
        innerData: [],
        expandedRowKeys: [],
        id: ' ',
        description: '列表展开子表Demo',
        // 列表表头
        columns: [{
          title: '#',
          dataIndex: '',
          key: 'rowIndex',
          width: 60,
          align: "center",
          customRender: function (t, r, index) {
            return parseInt(index) + 1;
          }
        },
          {
            title: '订单号',
            align: "center",
            dataIndex: 'orderCode'
          },
          {
            title: '订单类型',
            align: "center",
            dataIndex: 'ctype',
            customRender: (text) => {
              let re = "";
              if (text === '1') {
                re = "国内订单";
              } else if (text === '2') {
                re = "国际订单";
              }
              return re;
            }
          },
          {
            title: '订单日期',
            align: "center",
            dataIndex: 'orderDate'
          },
          {
            title: '订单金额',
            align: "center",
            dataIndex: 'orderMoney'
          },
          {
            title: '订单备注',
            align: "center",
            dataIndex: 'content'
          },
          {
            title: '操作',
            dataIndex: 'action',
            align: "center",
            scopedSlots: {customRender: 'action'},
          }],
        // 分页参数
        type: "radio",
        url: {
          list: "/test/order/orderList",
          delete: "/test/order/delete",
          deleteBatch: "/test/order/deleteBatch",
          customerListByMainId: "/test/order/listOrderCustomerByMainId",
        },
      }
    },
    computed: {
      currentId(){
        return this.id;
      }
    },
    methods: {
      handleExpand(expanded, record){
        this.expandedRowKeys=[];
        this.innerData=[];
        if(expanded===true){
          this.loading = true;
          this.expandedRowKeys.push(record.id);
          getAction(this.url.customerListByMainId, {mainId: record.id}).then((res) => {
            if (res.success) {
              this.loading = false;
              this.innerData = res.result.records;
            }
          });
        }
      },
    }
  }
</script>
<style scoped>
  .ant-card-body .table-operator {
    margin-bottom: 18px;
  }

  .ant-table-tbody .ant-table-row td {
    padding-top: 15px;
    padding-bottom: 15px;
  }

  .anty-row-operator button {
    margin: 0 5px
  }

  .ant-btn-danger {
    background-color: #ffffff
  }

  .ant-modal-cust-warp {
    height: 100%
  }

  .ant-modal-cust-warp .ant-modal-body {
    height: calc(100% - 110px) !important;
    overflow-y: auto
  }

  .ant-modal-cust-warp .ant-modal-content {
    height: 90% !important;
    overflow-y: hidden
  }
</style>