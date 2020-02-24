<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">

        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->
    
    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('报销单基本信息')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload>
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
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange, type:'radio'}"
        :customRow="clickThenSelect"
        @change="handleTableChange">

        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>
        <template slot="imgSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无此图片</span>
          <img v-else :src="getImgView(text)" height="25px" alt="图片不存在" style="max-width:80px;font-size: 12px;font-style: italic;"/>
        </template>
        <template slot="fileSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无此文件</span>
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
          <a @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
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

    <a-tabs defaultActiveKey="1">
      <a-tab-pane tab="报销单基本明细" key="1" >
        <reimburse-biz-base-detail-info-list :mainId="selectedMainId" :costType="costType" :rmbsTemplateCode="rmbsTemplateCode" :rmbsTemplateName="rmbsTemplateName"/>
      </a-tab-pane>
      <a-tab-pane tab="报销单抵扣凭证" key="2" forceRender :mainId="selectedMainId" v-if="existOffsetFlag=='Y'">
        <reimburse-biz-vat-deduction-vouchers-list :mainId="selectedMainId" />
      </a-tab-pane>
      <a-tab-pane tab="报销单付款清单" key="3" forceRender>
        <reimburse-biz-payment-list-list :mainId="selectedMainId" :rmbsTemplateCode="rmbsTemplateCode" :rmbsTemplateName="rmbsTemplateName" :vendorCode="vendorCode" :vendorName="vendorName"/>
      </a-tab-pane>
    </a-tabs>
    <reimburseBizMainInfo-modal ref="modalForm" @ok="modalFormOk" ></reimburseBizMainInfo-modal>
  </a-card>
  </template>

<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import ReimburseBizMainInfoModal from './modules/ReimburseBizMainInfoModal'
  import { getAction } from '@/api/manage'
  import ReimburseBizBaseDetailInfoList from './ReimburseBizBaseDetailInfoList'
  import ReimburseBizVatDeductionVouchersList from './ReimburseBizVatDeductionVouchersList'
  import ReimburseBizPaymentListList from './ReimburseBizPaymentListList'
  import pick from 'lodash.pick'
  export default {
    name: "ReimburseBizMainInfoList",
    mixins:[JeecgListMixin],
    components: {
      ReimburseBizBaseDetailInfoList,
      ReimburseBizVatDeductionVouchersList,
      ReimburseBizPaymentListList,
      ReimburseBizMainInfoModal
    },
    data () {
      return {
        description: '报销单基本信息管理页面',
        // 表头
        columns: [
          {
            title:'报账单编号',
            align:"center",
            dataIndex: 'applyNo'
          },
          {
            title:'报销事由',
            align:"center",
            dataIndex: 'reimbursementItem'
          },
          {
            title:'备注',
            align:"center",
            dataIndex: 'remark'
          },
          {
            title:'支付方式',
            align:"center",
            //dataIndex: 'paymentType'
            dataIndex: 'paymentType_dictText'
          },
          {
            title:'发票金额(价税总额)',
            align:"center",
            dataIndex: 'invoiceAmount'
          },
          {
            title:'付款金额',
            align:"center",
            dataIndex: 'paymentAmount'
          },
          {
            title:'供应商编号',
            align:"center",
            dataIndex: 'vendorCode'
          },
          {
            title:'供应商名称',
            align:"center",
            dataIndex: 'vendorName'
          },
          {
            title:'流程审批状态',
            align:"center",
            dataIndex: 'procState'
          },

          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            scopedSlots: { customRender: 'action' },
          }
        ],
        url: {
          list: "/biz/reimburseBizMainInfo/list",
          delete: "/biz/reimburseBizMainInfo/delete",
          deleteBatch: "/biz/reimburseBizMainInfo/deleteBatch",
          exportXlsUrl: "/biz/reimburseBizMainInfo/exportXls",
          importExcelUrl: "biz/reimburseBizMainInfo/importExcel",
        },
        dictOptions:{
        },
        /* 分页参数 */
        ipagination:{
          current: 1,
          pageSize: 5,
          pageSizeOptions: ['5', '10', '50'],
          showTotal: (total, range) => {
            return range[0] + "-" + range[1] + " 共" + total + "条"
          },
          showQuickJumper: true,
          showSizeChanger: true,
          total: 0
        },
        selectedMainId:'',
        costType:'',
        rmbsTemplateCode:'',
        rmbsTemplateName:'',
        vendorCode:'',
        vendorName:'',
        existOffsetFlag:'N',
        model: {}
      }
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      }
    },
    methods: {
      initDictConfig(){
      },
      clickThenSelect(record) {
        return {
          on: {
            click: () => {
              this.onSelectChange(record.id.split(","), [record]);
            }
          }
        }
      },
      onClearSelected() {
        this.selectedRowKeys = [];
        this.selectionRows = [];
        this.selectedMainId ='';
        this.existOffsetFlag ='N';
        this.costType = '';
        this.vendorCode = '';
        this.vendorName = '';
      },
      onSelectChange(selectedRowKeys, selectionRows) {
        //this.selectedMainId=selectedRowKeys[0];
        this.selectedMainId = selectionRows[0].applyNo;
        this.selectedRowKeys = selectedRowKeys;
        this.selectionRows = selectionRows;
        this.costType=selectionRows[0].costType;
        this.rmbsTemplateCode= selectionRows[0].reimbursementTemplateCode;
        this.rmbsTemplateName= selectionRows[0].reimbursementTemplateName;
        this.vendorCode= selectionRows[0].vendorCode;
        this.vendorName = selectionRows[0].vendorName;
        this.existOffsetFlag = selectionRows[0].existOffsetFlag;
        //console.log("Current_rmbsTemplateName=="+this.rmbsTemplateName);
      },
      loadData(arg) {
        if(!this.url.list){
          this.$message.error("请设置url.list属性!")
          return
        }
        //加载数据 若传入参数1则加载第一页的内容
        if (arg === 1) {
          this.ipagination.current = 1;
        }
        this.onClearSelected()
        var params = this.getQueryParams();//查询条件
        this.loading = true;
        getAction(this.url.list, params).then((res) => {
          if (res.success) {
            this.dataSource = res.result.records;
            this.ipagination.total = res.result.total;
          }
          if(res.code===510){
            this.$message.warning(res.message)
          }
          this.loading = false;
        })
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>