<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24" v-if="role === 'monitor'">
            <a-form-item label="申报状态">
              <j-dict-select-tag placeholder="请选择申报状态" v-model="queryParam.status" dictCode="statue"/>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24" v-if="role === 'monitor'">
            <a-form-item label="企业名称">
              <a-input placeholder="请输入企业名称" v-model="queryParam.companyName"></a-input>
            </a-form-item>
          </a-col>

            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="申报年份">
                <a-input placeholder="请输入申报年份" v-model="queryParam.reportYear" ></a-input>
              </a-form-item>
            </a-col>

          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="toSearchReset" icon="reload" style="margin-left: 8px">重置</a-button>
              <a @click="handleToggleSearch" style="margin-left: 8px" v-if="role === 'monitor'">
                {{ toggleSearchStatus ? '收起' : '展开' }}
                <a-icon :type="toggleSearchStatus ? 'up' : 'down'"/>
              </a>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->
    
    <!-- 操作按钮区域 -->

    <div class="table-operator" v-if="role === 'monitor'">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <!--
      <a-button type="primary" icon="download" @click="handleExportXls('企业年度动态监管')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload>
      -->
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay" >
          <a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>
      </a-dropdown>

    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;" v-if="role==='monitor'">
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
        class="j-table-force-nowrap"
        @change="handleTableChange">

        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>
        <template slot="imgSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无图片</span>
          <img v-else :src="getImgView(text)" height="25px" alt="" style="max-width:80px;font-size: 12px;font-style: italic;"/>
        </template>
        <template slot="fileSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无文件</span>
          <a-button
            v-else
            :ghost="true"
            type="primary"
            icon="download"
            size="small"
            @click="uploadFile(text)">
            下载
          </a-button>
        </template>

        <span slot="action" slot-scope="text, record">
          <a @click="toHandleEdit(record)" v-if="role === 'monitor'">编辑</a>
          <a @click="toHandleEdit(record)" v-else>查看</a>
          <a-divider type="vertical" v-if="role === 'monitor'"/>
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a v-if="role === 'monitor'">删除</a>
                </a-popconfirm>
        </span>


      </a-table>
    </div>

    <companyDynamicSupervision-modal ref="modalForm" @ok="modalFormOk" :companyId="companyid" :monitor="role === 'monitor'"></companyDynamicSupervision-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import CompanyDynamicSupervisionModal from './CompanyDynamicSupervisionModal'
  import store from '@/store/'

  export default {
    name: "CompanyDynamicSupervisionList",
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      CompanyDynamicSupervisionModal
    },
    props:{
      companyId:'',
      role:''
    },
    data () {
      return {
        description: '企业年度动态监管管理页面',
        companyid:this.companyId,
        queryParam: {
          companyId: this.companyId
        },
        // 表头
        columns: [
          {
            title: '序号',
            dataIndex: '',
            key:'rowIndex',
            width:60,
            align:"center",
            customRender:function (t,r,index) {
              return parseInt(index)+1;
            }
          },
          {
            title:'材料名称',
            align:"center",
            dataIndex: 'documentName'
          },
          {
            title:'材料类型',
            align:"center",
            dataIndex: 'documentType_dictText'
          },
          {
            title:'申报年份',
            align:"center",
            dataIndex: 'reportYear'
          },

          // {
          //   title:'数据状态',
          //   align:"center",
          //   dataIndex: 'status_dictText'
          // },
          // {
          //   title:'企业id',
          //   align:"center",
          //   dataIndex: 'companyId'
          // },
          //
          //
          // {
          //   title:'内容',
          //   align:"center",
          //   dataIndex: 'content',
          //   scopedSlots: {customRender: 'fileSlot'}
          // },
          // {
          //   title:'申报人',
          //   align:"center",
          //   dataIndex: 'createBy'
          // },
          // {
          //   title:'申报时间',
          //   align:"center",
          //   dataIndex: 'createTime',
          //   customRender:function (text) {
          //     return !text?"":(text.length>10?text.substr(0,10):text)
          //   }
          // },
          // {
          //   title:'审核时间',
          //   align:"center",
          //   dataIndex: 'updateTime',
          //   customRender:function (text) {
          //     return !text?"":(text.length>10?text.substr(0,10):text)
          //   }
          // },
          // {
          //   title:'审核人',
          //   align:"center",
          //   dataIndex: 'updateBy'
          // },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            // fixed:"right",
            width:147,
            scopedSlots: { customRender: 'action' }
          }
        ],
        url: {
          list: "/cds/companyDynamicSupervision/list",
          delete: "/cds/companyDynamicSupervision/delete",
          deleteBatch: "/cds/companyDynamicSupervision/deleteBatch",
          // exportXlsUrl: "/cds/companyDynamicSupervision/exportXls",
          // importExcelUrl: "cds/companyDynamicSupervision/importExcel",
        },
        dictOptions:{},
      }
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      },
    },
    methods: {
      initDictConfig(){
      },
      toHandleEdit:function(record){
        this.$refs.modalForm.value = record.companyId;
        this.handleEdit(record);
        this.$refs.modalForm.title="年度动态监管";
      },
      toSearchReset() {
        this.queryParam = {companyId:this.companyId};
        this.loadData(1);
      },
    },
    created(){
      if(this.companyid==null) {
        this.companyid = this.$store.getters.userInfo.companyIds[0]
      }
      //
      // let that = this;
      // var params = this.getQueryParams();//查询条件
      // queryDynamicSupervision(params).then((res)=>{
      //   if(res.success) {
      //     console.log(res.result);
      //     that.data = res.result;
      //   }else{
      //     this.$message.error(res.message);
      //   }
      // })
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>