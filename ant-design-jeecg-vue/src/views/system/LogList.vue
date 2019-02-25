<template>
  <a-card :bordered="false">
    <!--导航区域-->
    <div>
      <a-tabs defaultActiveKey="1" @change="callback">
        <a-tab-pane tab="登陆日志" key="1"></a-tab-pane>
        <a-tab-pane tab="操作日志" key="2"></a-tab-pane>
      </a-tabs>
    </div>

    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <a-row :gutter="24">

          <a-col :span="6">
            <a-form-item label="搜索日志">
              <a-input placeholder="请输入搜索关键词" v-model="queryParam.keyWord"></a-input>
            </a-form-item>
          </a-col>

          <a-col :span="10">
            <a-form-item label="创建时间">
              <a-range-picker
                :showTime="{ format: 'HH:mm' }"
                v-model="queryParam.createTimeRange"
                format="YYYY-MM-DD HH:mm"
                :placeholder="['开始时间', '结束时间']"
                @change="onDateChange"
                @ok="onDateOk"
              />
            </a-form-item>
          </a-col>

          <a-col :span="8" >
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchByquery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
            </span>
          </a-col>

        </a-row>
      </a-form>
    </div>

    <!-- table区域-begin -->
      <a-table
        ref="table"
        size="middle"
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        @change="handleTableChange">

        <p v-show="queryParam.logType==2" slot="expandedRowRender" slot-scope="record" style="margin: 0">
          请求方法： {{ record.method }}
          <a-divider dashed/>
          请求参数： {{ record.requestParam }}
        </p>

      </a-table>
    </div>
    <!-- table区域-end -->
  </a-card>
</template>

<script>
  import { filterObj } from '@/utils/util';
  import {getLogList,deleteLog,deleteLogList} from '@/api/api'

  export default {
    name: "LogList",
    data () {
      return {
        description: '这是日志管理页面',
        // 查询条件
        queryParam: {
          ipInfo:'',
          createTimeRange:[],
          logType:'1',
          keyWord:'',
        },
        // 表头
        columns: [
          {
            title: '#',
            dataIndex: '',
            key:'rowIndex',
            fixed:'center',
            align:"center",
            customRender:function (t,r,index) {
              return parseInt(index)+1;
            }
          },
          {
            title: '日志内容',
            align:"left",
            dataIndex: 'logContent',
            fixed:'center',
            sorter: true
          },
          {
            title: '操作人ID',
            dataIndex: 'userid',
            align:"center",
            sorter: true
          },
          {
            title: '操作人名称',
            dataIndex: 'username',
            align:"center",
            sorter: true
          },
          {
            title: 'IP',
            dataIndex: 'ip',
            align:"center",
            sorter: true
          },
          {
            title: '耗时(毫秒)',
            dataIndex: 'costTime',
            align:"center",
            sorter: true
          },
          {
            title: '日志类型',
            dataIndex: 'logType',
            customRender:function (text) {
              if(text==1){
                return "登录日志";
              }else if(text==2){
                return "操作日志";
              }else{
                return text;
              }
            },
            align:"center",
          },
          {
            title: '创建时间',
            dataIndex: 'createTime',
            align:"center",
            sorter: true
          }
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
        this.loading=true;
        getLogList(params).then((res)=>{
          if(res.success){
            this.dataSource = res.result.records;
            this.ipagination.total = res.result.total;
            this.loading=false;
            this.loadrefresh = false;
          }
        })
      },
      getQueryParams(){
        console.log(this.queryParam.createTimeRange)
        var param = Object.assign({}, this.queryParam,this.isorter);
        param.field = this.getQueryField();
        param.pageNo = this.ipagination.current;
        param.pageSize = this.ipagination.pageSize;
        delete param.createTimeRange; // 时间参数不传递后台
        return filterObj(param);
      },
      getQueryField(){
        var str = "id,";
        for(var a = 0;a<this.columns.length;a++){
          str+=","+this.columns[a].dataIndex;
        }
        return str;
      },
      // 查询
      searchByquery(){
        this.loadData(1);
      },
      // 重置
      searchReset(){
        var that = this;
        var logType = that.queryParam.logType;
        that.queryParam = {}; //清空查询区域参数
        that.queryParam.logType = logType;
        that.loadData(this.ipagination.current);
      },
      // 日志类型
      callback(key){
        let that=this;
        that.queryParam.logType=key;
        that.loadData();
      },
      onDateChange: function (value, dateString) {
        console.log(dateString[0],dateString[1]);
        this.queryParam.createTime_begin=dateString[0];
        this.queryParam.createTime_end=dateString[1];
      },
      onDateOk(value) {
        console.log(value);
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
    }
  }
</script>
<style>
  .ant-card-body .table-operator{
    margin-bottom: 18px;
  }
  .ant-layout-content{
    margin:12px 16px 0 !important;
  }
  .ant-table-tbody .ant-table-row td{
    padding-top:15px;
    padding-bottom:15px;
  }
  .anty-row-operator button{margin: 0 5px}
  .ant-btn-danger{background-color: #ffffff}
  .ant-divider-horizontal{margin: 10px 0;}
</style>