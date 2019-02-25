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
    <jeecgOrderCustomer-modal ref="JeecgOrderCustomerModal" @ok="modalFormOk"></jeecgOrderCustomer-modal>
  </a-card>
</template>

<script>
  import JeecgOrderCustomerModal from './form/JeecgOrderCustomerModal'
  import { filterObj } from '@/utils/util'
  import { deleteAction,getAction } from '@/api/manage'
  import JeecgOrderDMainList from './JeecgOrderDMainList'

  export default {
    name: "JeecgOrderCustomerList",
    components: {
      JeecgOrderDMainList,
      JeecgOrderCustomerModal
    },
    data () {
      return {
        description: '订单客户信息',
        // 查询条件
        queryParam: {
          orderId:null,
        },
        // 表头
        columns: [
          {
            title: '客户名',
            align:"center",
            width: 100,
            dataIndex: 'name',
            key: 'name',
            fixed: 'left'
          },
          {
            title: '性别',
            align:"center",
            dataIndex: 'sex',
            customRender:function (text) {
              if(text==1){
                return "男";
              }else if(text==2){
                return "女";
              }else{
                return text;
              }
            }
          },
          {
            title: '身份证号码',
            align:"center",
            dataIndex: 'idcard',
          },
          {
            title: '电话',
            dataIndex: 'telphone',
            align:"center",
          },
          {
            title: '操作',
            key: 'operation',
            align: 'center',
            fixed: 'right',
            width: 130,
            scopedSlots: { customRender: 'action' },
          },
        ],
        //数据集
        dataSource:[],
        // 分页参数
        ipagination:{
          current: 1,
          pageSize: 10,
          pageSizeOptions: ['10', '20', '30'],
          showTotal: (total, range) => {
            return range[0] + "-" + range[1] + " 共" + total + "条"
          },
          showQuickJumper: true,
          showSizeChanger: true,
          total: 0
        },
        isorter:{
          column: 'createTime',
          order: 'desc',
        },
        loading:false,
        selectedRowKeys: [],
        selectedRows: [],
        url: {
          list: "/test/order/queryOrderCustomerListByMainId",
          delete: "/test/order/deleteCustomer",
          deleteBatch: "/test/order/deleteBatchCustomer",
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
/*
            this.ipagination.total = res.result.total;
*/
          }else{
            this.dataSource = null;
/*            this.ipagination.total = 0;*/
          }
        })
      },
      getQueryParams(){
        var param = Object.assign({}, this.queryParam,this.isorter);
        param.orderId = this.queryParam.orderId;
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
        this.$refs.JeecgOrderCustomerModal.edit(record,'e');
        this.$refs.JeecgOrderCustomerModal.title="编辑";
      },
      handleAdd: function(){
        this.$refs.JeecgOrderCustomerModal.add(this.queryParam.orderId);
        this.$refs.JeecgOrderCustomerModal.title="添加客户信息";
      },
      handleDetail: function(record){
        this.$refs.JeecgOrderCustomerModal.detail(record);
        this.$refs.JeecgOrderCustomerModal.title="添加客户信息";
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
        this.queryParam.orderId = orderId.toString();
        this.loadData(1);
      }
    }
  }
</script>
<style scoped>
  .ant-card{
    margin-left: -30px;
    margin-right: -30px;
  }
</style>