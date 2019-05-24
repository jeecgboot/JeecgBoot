<template>
  <a-card :bordered="false">

    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <a-row :gutter="24">
        
          <a-col :md="6" :sm="8" class="1">
            <a-form-item label="请假人">
              <a-input placeholder="请输入请假人" v-model="queryParam.name"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8" class="1">
            <a-form-item label="请假天数">
              <a-input placeholder="请输入请假天数" v-model="queryParam.days"></a-input>
            </a-form-item>
          </a-col>
        <template v-if="toggleSearchStatus">
        <a-col :md="6" :sm="8">
            <a-form-item label="开始时间">
              <a-input placeholder="请输入开始时间" v-model="queryParam.beginDate"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="请假结束时间">
              <a-input placeholder="请输入请假结束时间" v-model="queryParam.endDate"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="请假原因">
              <a-input placeholder="请输入请假原因" v-model="queryParam.reason"></a-input>
            </a-form-item>
          </a-col>
        </template>
          <a-col :md="6" :sm="8" >
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
              <a @click="handleToggleSearch" style="margin-left: 8px">
                {{ toggleSearchStatus ? '收起' : '展开' }}
                <a-icon :type="toggleSearchStatus ? 'up' : 'down'"/>
              </a>
            </span>
          </a-col>

        </a-row>
      </a-form>
    </div>

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload>
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
          <template v-if="record.bpmStatus === '1'">
            <a @click="handleEdit(record)">编辑</a>
            <a-divider type="vertical"/>
            <a @click="startProcess(record)">提交流程</a>
            <a-divider type="vertical"/>
          </template>
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item v-if="record.bpmStatus === '1'">
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
              <a-menu-item v-else @click="handlePreviewPic(record)">审批进度</a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>
    <!-- table区域-end -->

    <!-- 表单区域 -->
    <form-common-modal ref="modalForm" :path="path" @ok="modalFormOk"></form-common-modal>
    <!-- 审批流程 -->
    <process-inst-pic-modal ref="processInstPicModal"></process-inst-pic-modal>

  </a-card>
</template>

<script>
  import { postAction } from '@/api/manage'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import JDate from '@/components/jeecg/JDate.vue'
  import FormCommonModal from "@/components/bpm/FormCommonModal";
  import ProcessInstPicModal from "@/components/bpm/ProcessInstPicModal";

  export default {
    name: "JoaDemoList",
    mixins:[JeecgListMixin],
    components: {
      FormCommonModal,
      ProcessInstPicModal,
      JDate
    },
    data () {
      return {
        description: '流程测试管理页面',
        // 表头
        columns: [
          {
            title: '#',
            dataIndex: '',
            key:'rowIndex',
            width:60,
            align:"center",
            customRender:function (t,r,index) {
              return parseInt(index)+1;
            }
          },
          {
            title: '请假人',
            align:"center",
            dataIndex: 'name'
          },
          {
            title: '请假天数',
            align:"center",
            dataIndex: 'days'
          },
          {
            title: '开始时间',
            align:"center",
            dataIndex: 'beginDate'
          },
          {
            title: '结束时间',
            align:"center",
            dataIndex: 'endDate'
          },
          {
            title: '请假原因',
            align:"center",
            dataIndex: 'reason'
          },
          {
            title: '流程状态',
            align:"center",
            dataIndex: 'bpmStatus',
            customRender:function (text) {
              if(text=='1'){
                return "待提交";
              }else if(text=='2'){
                return "处理中";
              }else if(text=='3'){
                return "已完成";
              }else if(text=='4'){
                return "已作废";
              }else{
                return text;
              }
            }
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            scopedSlots: { customRender: 'action' },
          }
        ],
        flowCode:"dev_joa_demo_001",
        path:"jeecg/modules/JoaDemoForm",
		    url: {
          list: "/test/joaDemo/list",
          delete: "/test/joaDemo/delete",
          deleteBatch: "/test/joaDemo/deleteBatch",
          exportXlsUrl: "test/joaDemo/exportXls",
          importExcelUrl: "test/joaDemo/importExcel",
          startProcess: "/process/extActProcess/startMutilProcess",
       },
    }
  },
  computed: {
    importExcelUrl: function(){
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
    }
  },
    methods: {
      startProcess: function(record){
        var that = this;
        this.$confirm({
          title:"提示",
          content:"确认提交流程吗?",
          onOk: function(){
            var param = {
              flowCode:that.flowCode,
              id:record.id,
              formUrl:that.path,
              formUrlMobile:that.path
            }
            postAction(that.url.startProcess,param).then((res)=>{
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
      },
      handlePreviewPic: function(record){
        var flowCode = this.flowCode;
        var dataId = record.id;
        this.$refs.processInstPicModal.preview(flowCode,dataId);
        this.$refs.processInstPicModal.title="流程图";
      },
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less'
</style>