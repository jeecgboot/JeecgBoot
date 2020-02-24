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
        <reimburse-biz-base-detail-info-list :mainId="selectedMainId" />
      </a-tab-pane>
      <a-tab-pane tab="报销单抵扣凭证" key="2" forceRender>
        <reimburse-biz-vat-deduction-vouchers-list :mainId="selectedMainId" />
      </a-tab-pane>
      <a-tab-pane tab="报销单付款清单" key="3" forceRender>
        <reimburse-biz-payment-list-list :mainId="selectedMainId" />
      </a-tab-pane>
    </a-tabs>

    <reimburseBizMainInfo-modal ref="modalForm" @ok="modalFormOk"></reimburseBizMainInfo-modal>
  </a-card>
</template>

<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import ReimburseBizMainInfoModal from './modules/ReimburseBizMainInfoModal'
  import { getAction } from '@/api/manage'
  import ReimburseBizBaseDetailInfoList from './ReimburseBizBaseDetailInfoList'
  import ReimburseBizVatDeductionVouchersList from './ReimburseBizVatDeductionVouchersList'
  import ReimburseBizPaymentListList from './ReimburseBizPaymentListList'

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
            title:'报账单模板名称',
            align:"center",
            dataIndex: 'reimbursementTemplateName'
          },
          {
            title:'员工姓名',
            align:"center",
            dataIndex: 'userName'
          },
          {
            title:'员工所属公司名称',
            align:"center",
            dataIndex: 'userCompanyName'
          },
          {
            title:'员工所属部门名称',
            align:"center",
            dataIndex: 'userDepartName'
          },
          {
            title:'员工职务',
            align:"center",
            dataIndex: 'userPosition'
          },
          {
            title:'员工编号',
            align:"center",
            dataIndex: 'userNo'
          },
          {
            title:'员工邮箱',
            align:"center",
            dataIndex: 'userEmail'
          },
          {
            title:'员工手机号',
            align:"center",
            dataIndex: 'userMobile'
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
            title:'支出成本类型',
            align:"center",
            dataIndex: 'costType'
          },
          {
            title:'币种',
            align:"center",
            dataIndex: 'currency'
          },
          {
            title:'发票张数',
            align:"center",
            dataIndex: 'attachmentNum'
          },
          {
            title:'支付方式：转账（银企直联），结清（银行托收），网银支付',
            align:"center",
            dataIndex: 'paymentType'
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
            title:'发票价款',
            align:"center",
            dataIndex: 'invoicePriceAmount'
          },
          {
            title:'发票税款',
            align:"center",
            dataIndex: 'invoiceTaxAmount'
          },
          {
            title:'付款金额-价款',
            align:"center",
            dataIndex: 'paymentPriceAmount'
          },
          {
            title:'付款金额-税款',
            align:"center",
            dataIndex: 'paymentTaxAmount'
          },
          {
            title:'本次冲减金额',
            align:"center",
            dataIndex: 'strikeAmount'
          },
          {
            title:'审批确认金额',
            align:"center",
            dataIndex: 'auditConfirmAmount'
          },
          {
            title:'审批差异金额',
            align:"center",
            dataIndex: 'auditDiscrepancyAmount'
          },
          {
            title:'审批确认说明',
            align:"center",
            dataIndex: 'auditConfirmRemark'
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
            title:'供应商地点',
            align:"center",
            dataIndex: 'vendorAddress'
          },
          {
            title:'供应商纳税人类型: 一般纳税人/ 小规模纳税人/非增值税纳税人',
            align:"center",
            dataIndex: 'vendorVatFlag'
          },
          {
            title:'结算支付方式',
            align:"center",
            dataIndex: 'balSeqPayType'
          },
          {
            title:'来源系统名称',
            align:"center",
            dataIndex: 'sourceSystemName'
          },
          {
            title:'月度预算期间',
            align:"center",
            dataIndex: 'monthBdgtPeriod',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'审核会计姓名',
            align:"center",
            dataIndex: 'auditUserName'
          },
          {
            title:'实物单流程审批状态',
            align:"center",
            dataIndex: 'docProcessState'
          },
          {
            title:'税务审核会计姓名',
            align:"center",
            dataIndex: 'vatAuditUserName'
          },
          {
            title:'抵扣联实物单流程状态',
            align:"center",
            dataIndex: 'vatDocProcessState'
          },
          {
            title:'流程模板',
            align:"center",
            dataIndex: 'procTemplateName'
          },
          {
            title:'流程提交时间',
            align:"center",
            dataIndex: 'submitTime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'审批完毕时间',
            align:"center",
            dataIndex: 'endTime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'流程审批状态',
            align:"center",
            dataIndex: 'procState'
          },
          {
            title:'是否借款：N否，Y是',
            align:"center",
            dataIndex: 'isLoan'
          },
          {
            title:'是否冲减：N否，Y是',
            align:"center",
            dataIndex: 'isStrike'
          },
          {
            title:'是否预付：N否，Y是',
            align:"center",
            dataIndex: 'isPrepay'
          },
          {
            title:'是否有合同：N否，Y是',
            align:"center",
            dataIndex: 'existContract'
          },
          {
            title:'是否关联交易:N否，Y是',
            align:"center",
            dataIndex: 'isConntrans'
          },
          {
            title:'关联交易类型',
            align:"center",
            dataIndex: 'conntransType'
          },
          {
            title:'是否包含订单：N-否/Y-是',
            align:"center",
            dataIndex: 'existOrder'
          },
          {
            title:'是否不动产：Y-是/N-否',
            align:"center",
            dataIndex: 'isImmovable'
          },
          {
            title:'是否电子发票：Y-是/N-否',
            align:"center",
            dataIndex: 'isEleInvoice'
          },
          {
            title:'是否包含进项抵扣凭证：Y-是/N-否',
            align:"center",
            dataIndex: 'existOffsetFlag'
          },
          {
            title:'抵扣联是否认证通过 Y-认证通过、N-未认证或认证失败',
            align:"center",
            dataIndex: 'certified'
          },
          {
            title:'是否成本下分：Y-是/N-否',
            align:"center",
            dataIndex: 'isPayDivide'
          },
          {
            title:'是否包含视同销售：N-否/Y-是',
            align:"center",
            dataIndex: 'isSales'
          },
          {
            title:'是否需要起草进项发票报账单：N-否，Y-是',
            align:"center",
            dataIndex: 'isInputVatInvoice'
          },
          {
            title:'是否已经预匹配订单：Y-是，N-否',
            align:"center",
            dataIndex: 'isPreMatchPo'
          },
          {
            title:'归档时间',
            align:"center",
            dataIndex: 'archivingTime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'归档人',
            align:"center",
            dataIndex: 'archivingMan'
          },
          {
            title:'创建时间',
            align:"center",
            dataIndex: 'createTime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'创建人',
            align:"center",
            dataIndex: 'createBy'
          },
          {
            title:'更新时间',
            align:"center",
            dataIndex: 'updateTime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'更新人',
            align:"center",
            dataIndex: 'updateBy'
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
        selectedMainId:''

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
        this.selectedMainId=''
      },
      onSelectChange(selectedRowKeys, selectionRows) {
        this.selectedMainId=selectedRowKeys[0]
        this.selectedRowKeys = selectedRowKeys;
        this.selectionRows = selectionRows;
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
  @import '~@assets/less/common.less'
</style>