<template>
  <a-card :bordered="false">

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>

      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>
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
        size="default"
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
          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">
              更多 <a-icon type="down" />
            </a>
            <a-menu slot="overlay">
              <a-menu-item >
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
    <JeecgOrderTicket-modal ref="JeecgOrderTicketModal" @ok="modalFormOk"></JeecgOrderTicket-modal>
  </a-card>
</template>

<script>
  import { filterObj } from '@/utils/util'
  import { deleteAction,getAction } from '@/api/manage'
  import JeecgOrderTicketModal from './form/JeecgOrderTicketModal'

  export default {
    name: "JeecgOrderTicketList",
    components: {
      JeecgOrderTicketModal,
    },
    data () {
      return {
        description: '机票信息',
        // 查询条件
        queryParam: {
          orderId:null,
        },
        // 表头
        columns: [{
          title:'航班号',
          align:"center",
          dataIndex:'ticketCode'
        },{
          title:'航班时间',
          align:"center",
          dataIndex:'tickectDate'
        },{
          title:'订单号码',
          align:"center",
          dataIndex:'orderId',
        },{
          title:'创建人',
          align:"center",
          dataIndex:'createBy'
        },{
          title:'创建时间',
          align:"center",
          dataIndex:'createTime',
          sorter:true
        },{
            title: '操作',
            key: 'operation',
            fixed:'right',
            align:"center",
            width:130,
            scopedSlots: { customRender: 'action' },
          }],
        //数据集
        dataSource:[],
        // 分页参数
        ipagination:{
          current: 1,
          pageSize: 5,
          pageSizeOptions: ['5','10', '20'],
          showTotal: (total, range) => {
            return range[0] + "-" + range[1] + " 共" + total + "条"
          },
          showQuickJumper: true,
          showSizeChanger: true,
          total: 0
        },
        isorter:{
          column: 'createTime',
          order: 'asc',
        },
        loading:false,
        selectedRowKeys: [],
        selectedRows: [],
        url: {
          list: "/test/order/queryOrderTicketListByMainId",
          delete: "/test/order/deleteTicket",
          deleteBatch: "/test/order/deleteBatchTicket",
        },

      }
    },
    created() {
      this.loadData();
    },
    methods: {
      loadData (arg){
        //加载数据 若传入参数1则加载第一页的内容
        if(arg===1){
          this.ipagination.current = 1;
        }
        var params = this.getQueryParams();//查询条件
        getAction(this.url.list,{id:params.orderId}).then((res)=>{
          if(res.success){
            this.dataSource = res.result;
/*            this.ipagination.total = res.result.total;*/
          }else{
            this.dataSource = null;
/*            this.ipagination.total = 0;*/
          }
        })
      },
      getQueryParams(){
        var param = Object.assign({}, this.queryParam,this.isorter);
        param.field = this.getQueryField();
        param.pageNo = this.ipagination.current;
        param.pageSize = this.ipagination.pageSize;
        return filterObj(param);
      },
      getQueryField(){
        //TODO 字段权限控制
        var str = "id,";
        for(var a = 0;a<this.columns.length;a++){
          str+=","+this.columns[a].dataIndex;
        }
        return str;
      },
      onSelectChange (selectedRowKeys,selectionRows) {
        this.selectedRowKeys = selectedRowKeys;
        this.selectionRows = selectionRows;
      },
      onClearSelected(){
        this.selectedRowKeys = [];
        this.selectionRows = [];
      },
      searchQuery(){
        this.loadData(1);
      },
      searchReset(){
        var that = this;
        for(var a in that.queryParam){
          that.queryParam[a] = '';
        }
        that.loadData(1);
      },
      batchDel: function(){
        if(this.selectedRowKeys.length<=0){
          this.$message.warning('请选择一条记录！');
          return ;
        }else{
          var ids = "";
          for(var a =0;a<this.selectedRowKeys.length;a++){
            ids+=this.selectedRowKeys[a]+",";
          }
          var that = this;
          this.$confirm({
            title:"确认删除",
            content:"是否删除选中数据?",
            onOk: function(){
              deleteAction(that.url.deleteBatch,{ids: ids}).then((res)=>{
                if(res.success){
                  that.$message.success(res.message);
                  that.loadData();
                  that.onClearSelected();
                }else{
                  that.$message.warning(res.message);
                }
              });
            }
          });
        }
      },
      handleDelete: function(id){
        var that = this;
        this.$confirm({
          title:"确认删除",
          content:"是否删除选中数据?",
          onOk: function(){
            deleteAction(that.url.delete,{id: id}).then((res)=>{
              if(res.success){
                that.$message.success(res.message);
                that.loadData();
              }else{
                that.$message.warning(res.message);
              }
            });
          }
        });
      },
      handleEdit: function(record){
        this.$refs.JeecgOrderTicketModal.edit(record,'e');
        this.$refs.JeecgOrderTicketModal.title="新增机票信息";
      },
      handleAdd: function(){
        this.$refs.JeecgOrderTicketModal.add(this.queryParam.orderId);
        this.$refs.JeecgOrderTicketModal.title="新增机票信息";
      },
      handleDetail: function(record){
        this.$refs.JeecgOrderTicketModal.detail(record);
        this.$refs.JeecgOrderTicketModal.title="新增机票信息";
      },
      handleTableChange(pagination, filters, sorter){
        //分页、排序、筛选变化时触发
        console.log(sorter);
        //TODO 筛选
        if (Object.keys(sorter).length>0){
          this.isorter.column = sorter.field;
          this.isorter.order = "ascend"==sorter.order?"asc":"desc"
        }
        this.ipagination = pagination;
        this.loadData();
      },
      modalFormOk () {
        // 新增/修改 成功时，重载列表
        this.loadData();
      },
      getOrderMain(orderId){
        this.queryParam.orderId = orderId;
        this.loadData(1);
      }
    }
  }
</script>
<style scoped>
</style>