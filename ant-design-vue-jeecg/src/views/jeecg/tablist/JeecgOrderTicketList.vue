<template>
  <a-card :bordered="false">

    <!-- 操作按钮区域 -->
    <div class="table-operator" :md="24" :sm="24" style="margin: -25px 0px 10px 2px">
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
            <a class="ant-dropdown-link">
              更多 <a-icon type="down"/>
            </a>
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
    <JeecgOrderTicket-modal ref="modalForm" @ok="modalFormOk"></JeecgOrderTicket-modal>
  </a-card>
</template>

<script>
  import JeecgOrderTicketModal from './form/JeecgOrderTicketModal'
  import {JeecgListMixin} from '@/mixins/JeecgListMixin'
  import {getAction} from '@/api/manage'

  export default {
    name: "JeecgOrderTicketList",
    mixins: [JeecgListMixin],
    components: {
      JeecgOrderTicketModal,
    },
    data() {
      return {
        description: '机票信息',
        // 表头
        columns: [{
          title: '航班号',
          align: "center",
          dataIndex: 'ticketCode'
        }, {
          title: '航班时间',
          align: "center",
          dataIndex: 'tickectDate'
        }, {
          title: '订单号码',
          align: "center",
          dataIndex: 'orderId',
        }, {
          title: '创建人',
          align: "center",
          dataIndex: 'createBy'
        }, {
          title: '创建时间',
          align: "center",
          dataIndex: 'createTime',
          sorter: true
        }, {
          title: '操作',
          key: 'operation',
          align: "center",
          width: 130,
          scopedSlots: {customRender: 'action'},
        }],
        url: {
          list: "/test/order/listOrderTicketByMainId",
          delete: "/test/order/deleteTicket",
          deleteBatch: "/test/order/deleteBatchTicket",
        }
      }
    },
    methods: {
      loadData(arg) {
        if (arg === 1) {
          this.ipagination.current = 1;
        }
        var params = this.getQueryParams();
        //update-begin--Author:kangxiaolin  Date:20190905 for：[442]主子表分开维护，生成的代码子表的分页改为真实的分页--------------------
        getAction(this.url.list, {orderId: params.mainId ,pageNo : this.ipagination.current,
          pageSize :this.ipagination.pageSize}).then((res) => {
          if (res.success) {
            this.dataSource = res.result.records;
            this.ipagination.total = res.result.total;
          } else {
            this.dataSource = null;
          }
        })
        //update-end--Author:kangxiaolin  Date:20190905 for：[442]主子表分开维护，生成的代码子表的分页改为真实的分页--------------------
      },
      getOrderMain(orderId) {
        this.queryParam.mainId = orderId;
        this.loadData(1);
      },
      handleAdd: function () {
        this.$refs.modalForm.add(this.queryParam.mainId);
        this.$refs.modalForm.title = "添加机票信息";
      },
    }
  }
</script>
<style scoped>
  .ant-card {
    margin-left: -30px;
    margin-right: -30px;
  }
</style>