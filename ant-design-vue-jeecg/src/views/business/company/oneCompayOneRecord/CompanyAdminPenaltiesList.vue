<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="申报状态">
              <j-dict-select-tag placeholder="请选择申报状态" v-model="queryParam.status" dictCode="statue"/>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24" v-if="role === 'monitor'">
            <a-form-item label="企业名称">
              <a-input placeholder="请输入企业名称" v-model="queryParam.companyName"></a-input>
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">
            <a-col :xl="10" :lg="11" :md="12" :sm="24" v-if="role === 'monitor'">
              <a-form-item label="发文日期">
                <j-date placeholder="请选择开始日期" class="query-group-cust" v-model="queryParam.reportDate_begin"></j-date>
                <span class="query-group-split-cust"></span>
                <j-date placeholder="请选择结束日期" class="query-group-cust" v-model="queryParam.reportDate_end"></j-date>
              </a-form-item>
            </a-col>
          </template>
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
<!--      <a-button type="primary" icon="download" @click="handleExportXls('行政处罚信息')">导出</a-button>-->
<!--      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">-->
<!--        <a-button type="primary" icon="import">导入</a-button>-->
<!--      </a-upload>-->
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>
      </a-dropdown>
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;" v-if="role === 'monitor'">
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
          <a @click="tohandleEdit(record)" v-if="role === 'monitor'">编辑</a>
          <a @click="tohandleEdit(record)" v-else>查看</a>
          <a-divider type="vertical" v-if="role === 'monitor'" />
          <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
            <a v-if="role === 'monitor'">删除</a>
          </a-popconfirm>
        </span>

      </a-table>
    </div>

    <companyAdminPenalties-modal ref="modalForm" @ok="modalFormOk" :companyId="companyId" :monitor="role === 'monitor'"></companyAdminPenalties-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import CompanyAdminPenaltiesModal from './routeView/CompanyAdminPenaltiesModal'
  import JDate from '@/components/jeecg/JDate.vue'
  import {filterMultiDictText} from '@/components/dict/JDictSelectUtil'

  export default {
    name: "CompanyAdminPenaltiesList",
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      JDate,
      CompanyAdminPenaltiesModal
    },
    props: {
      companyId:'',
      role:''
    },
    data () {
      return {
        description: '行政处罚信息管理页面',
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
            title:'文件名称',
            align:"center",
            dataIndex: 'documentName'
          },
          {
            title:'文件编号',
            align:"center",
            dataIndex: 'documentNo'
          },
          {
            title:'发文日期',
            align:"center",
            dataIndex: 'reportDate',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
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
          // {
          //   title:'文件上传',
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
          //   title:'审核人',
          //   align:"center",
          //   dataIndex: 'updateBy'
          // },
          // {
          //   title:'审核时间',
          //   align:"center",
          //   dataIndex: 'updateTime',
          //   customRender:function (text) {
          //     return !text?"":(text.length>10?text.substr(0,10):text)
          //   }
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
          list: "/cap/companyAdminPenalties/list",
          delete: "/cap/companyAdminPenalties/delete",
          deleteBatch: "/cap/companyAdminPenalties/deleteBatch",
          // exportXlsUrl: "/cap/companyAdminPenalties/exportXls",
          // importExcelUrl: "cap/companyAdminPenalties/importExcel",
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
      tohandleEdit:function (record) {
        this.$refs.modalForm.value = record.companyId;
        this.handleEdit(record);
        this.$refs.modalForm.title="行政处罚信息";
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

    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>