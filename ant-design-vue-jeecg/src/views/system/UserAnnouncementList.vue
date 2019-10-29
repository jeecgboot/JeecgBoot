<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">

          <a-col :span="6">
            <a-form-item label="标题">
              <a-input placeholder="请输入标题" v-model="queryParam.titile"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item label="发布人">
              <a-input placeholder="请输入发布人" v-model="queryParam.sender"></a-input>
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

    <div class="table-operator">
      <a-button type="primary" @click="readAll" icon="book">全部标注已读</a-button>
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
      @change="handleTableChange">
      <span slot="action" slot-scope="text, record">
        <a @click="showAnnouncement(record)">查看</a>
      </span>
    </a-table>
    <show-announcement ref="ShowAnnouncement"></show-announcement>
  </a-card>
</template>

<script>
  import { filterObj } from '@/utils/util'
  import { getAction,putAction } from '@/api/manage'
  import ShowAnnouncement from '@/components/tools/ShowAnnouncement'
  import {JeecgListMixin} from '@/mixins/JeecgListMixin'

  export default {
    name: "UserAnnouncementList",
    mixins: [JeecgListMixin],
    components: {
      ShowAnnouncement
    },
    data () {
      return {
        description: '系统通告表管理页面',
        queryParam: {},
        columns: [{
          title: '标题',
          align:"center",
          dataIndex: 'titile'
        },{
          title: '消息类型',
          align: "center",
          dataIndex: 'msgCategory',
          customRender: function (text) {
            if (text == '1') {
              return "通知公告";
            } else if (text == "2") {
              return "系统消息";
            } else {
              return text;
            }
          }
        },{
          title: '发布人',
          align:"center",
          dataIndex: 'sender'
        },{
          title: '发布时间',
          align:"center",
          dataIndex: 'sendTime'
        },{
          title: '优先级',
          align:"center",
          dataIndex: 'priority',
          customRender:function (text) {
            if(text=='L'){
              return "低";
            }else if(text=="M"){
              return "中";
            }else if(text=="H"){
              return "高";
            } else {
              return text;
            }
          }
        },{
          title: '阅读状态',
          align:"center",
          dataIndex: 'readFlag',
          customRender:function (text) {
            if(text=='0'){
              return "未读";
            }else if(text=="1"){
              return "已读";
            } else {
              return text;
            }
          }
        },{
          title: '操作',
          dataIndex: 'action',
          align:"center",
          scopedSlots: { customRender: 'action' },
        }],
		    url: {
          list: "/sys/sysAnnouncementSend/getMyAnnouncementSend",
          editCementSend:"sys/sysAnnouncementSend/editByAnntIdAndUserId",
          readAllMsg:"sys/sysAnnouncementSend/readAll",
        },
        loading:false,
      }
    },
    methods: {
      handleDetail: function(record){
        this.$refs.sysAnnouncementModal.detail(record);
        this.$refs.sysAnnouncementModal.title="查看";
      },
      showAnnouncement(record){
        putAction(this.url.editCementSend,{anntId:record.anntId}).then((res)=>{
          if(res.success){
            this.loadData();
          }
        });
        this.$refs.ShowAnnouncement.detail(record);
      },
      readAll(){
        var that = this;
        that.$confirm({
          title:"确认操作",
          content:"是否全部标注已读?",
          onOk: function(){
            putAction(that.url.readAllMsg).then((res)=>{
              if(res.success){
                that.$message.success(res.message);
                that.loadData();
              }
            });
          }
        });
      },
    }
  }
</script>
<style scoped>
  .ant-card-body .table-operator{
    margin-bottom: 18px;
  }
  .anty-row-operator button{margin: 0 5px}
  .ant-btn-danger{background-color: #ffffff}z

  .ant-modal-cust-warp{height: 100%}
  .ant-modal-cust-warp .ant-modal-body{height:calc(100% - 110px) !important;overflow-y: auto}
  .ant-modal-cust-warp .ant-modal-content{height:90% !important;overflow-y: hidden}
</style>