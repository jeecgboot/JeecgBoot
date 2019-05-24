<template>
  <div>
    <a-modal
      centered
      :title="title"
      :width="1000"
      :visible="visible"
      @ok="handleOk"
      @cancel="handleCancel"
      cancelText="关闭">
      <a-row :gutter="18">
        <a-col :span="16">
          <a-card title="选择人员" :bordered="true">
            <!-- 查询区域 -->
            <div class="table-page-search-wrapper">
              <a-form layout="inline">
                <a-row :gutter="24">

                  <a-col :span="10">
                    <a-form-item label="姓名">
                      <a-input placeholder="请输入姓名" v-model="queryParam.name"></a-input>
                    </a-form-item>
                  </a-col>
                  <a-col :span="8" >
                    <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
                      <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
                      <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
                    </span>
                  </a-col>

                </a-row>
              </a-form>
            </div>
            <!-- table区域-begin -->
            <div>
              <a-table
                size="small"
                bordered
                rowKey="id"
                :columns="columns1"
                :dataSource="dataSource1"
                :pagination="ipagination"
                :loading="loading"
                :scroll="{ y: 240 }"
                :rowSelection="{selectedRowKeys: selectedRowKeys,onSelectAll:onSelectAll,onSelect:onSelect,onChange: onSelectChange}"
                @change="handleTableChange">

              </a-table>
            </div>
            <!-- table区域-end -->
          </a-card>
        </a-col>
        <a-col :span="8">
          <a-card title="用户选择" :bordered="true">
            <!-- table区域-begin -->
            <div>
              <a-table
                size="small"
                bordered
                rowKey="id"
                :columns="columns2"
                :dataSource="dataSource2"
                :loading="loading"
                :scroll="{ y: 240 }"
              >
                <span slot="action" slot-scope="text, record">
                  <a-button type="primary" size="small" @click="handleDelete(record)" icon="delete">删除</a-button>
                </span>
              </a-table>
            </div>
            <!-- table区域-end -->
          </a-card>
        </a-col>
      </a-row>
    </a-modal>
  </div>
</template>

<script>
 import { filterObj } from '@/utils/util'
 import { getAction } from '@/api/manage'

  export default {
    name: "SelectDemoModal",
    data () {
      return {
        title: "用户列表",
        names: [],
        visible: false,
        placement: 'right',
        description: '人员管理页面',
        // 查询条件
        queryParam: {},
        // 表头
        columns1: [
          {
            title: '#',
            dataIndex: '',
            key:'rowIndex',
            width:50,
            align:"center",
            customRender:function (t,r,index) {
              return parseInt(index)+1;
            }
          },
          {
            title: '姓名',
            align:"center",
            width:113,
            dataIndex: 'name'
          },
          {
            title: '年龄',
            align:"center",
            width:100,
            dataIndex: 'age'
          },
          {
            title: '出生日期',
            align:"center",
            width:100,
            dataIndex: 'birthday'
          }
        ],
        columns2: [

          {
            title: '用户账号',
            align:"center",
            width:100,
            dataIndex: 'name'
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            width:100,
            scopedSlots: { customRender: 'action' },
          }
        ],
        //数据集
        dataSource1:[],
        dataSource2:[],
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
          list: "/test/jeecgDemo/list",
        },

      }
    },
    created() {
      this.loadData();
    },
    methods: {
      searchQuery(){
        this.loadData(1);
      },
      searchReset(){
        this.queryParam={};
        this.loadData(1);
      },
      handleCancel() {
        this.visible = false;
      },
      handleOk() {
          this.$emit("selectFinished",this.dataSource2);
          this.visible = false;
      },
      add() {
        this.visible = true;
      },
      loadData (arg){
        //加载数据 若传入参数1则加载第一页的内容
        if(arg===1){
          this.ipagination.current = 1;
        }
        var params = this.getQueryParams();//查询条件
        getAction(this.url.list,params).then((res)=>{
          if(res.success){
            this.dataSource1 = res.result.records;
            this.ipagination.total = res.result.total;
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
      },
      onSelectAll (selected, selectedRows, changeRows) {
        if(selected===true){
          for(var a = 0;a<changeRows.length;a++){
            this.dataSource2.push(changeRows[a]);
          }
        }else{
          for(var b = 0;b<changeRows.length;b++){
            this.dataSource2.splice(this.dataSource2.indexOf(changeRows[b]),1);
          }
        }
       // console.log(selected, selectedRows, changeRows);
      },
      onSelect (record,selected) {
        if(selected===true){
          this.dataSource2.push(record);
        }else{
          var index = this.dataSource2.indexOf(record);
          //console.log();
          if(index >=0 ){
            this.dataSource2.splice(this.dataSource2.indexOf(record),1);
          }

        }
      },
      onSelectChange (selectedRowKeys,selectedRows) {
        this.selectedRowKeys = selectedRowKeys;
        this.selectionRows = selectedRows;
      },
      onClearSelected(){
        this.selectedRowKeys = [];
        this.selectionRows = [];
      },
      handleDelete: function(record){
        this.dataSource2.splice(this.dataSource2.indexOf(record),1);
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
      }
    }
  }
</script>
<style lang="less" scoped>
  .ant-card-body .table-operator{
    margin-bottom: 18px;
  }
  .ant-table-tbody .ant-table-row td{
    padding-top:15px;
    padding-bottom:15px;
  }
  .anty-row-operator button{margin: 0 5px}
  .ant-btn-danger{background-color: #ffffff}

  .ant-modal-cust-warp{height: 100%}
  .ant-modal-cust-warp .ant-modal-body{height:calc(100% - 110px) !important;overflow-y: auto}
  .ant-modal-cust-warp .ant-modal-content{height:90% !important;overflow-y: hidden}
</style>