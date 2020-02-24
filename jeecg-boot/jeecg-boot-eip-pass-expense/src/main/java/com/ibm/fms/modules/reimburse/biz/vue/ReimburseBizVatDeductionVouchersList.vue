<template>
  <a-card :bordered="false" :class="'cust-erp-sub-tab'">
    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button v-if="mainId" @click="handleAdd" type="primary" icon="plus">新增</a-button>
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
          <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
            <a>删除</a>
          </a-popconfirm>
        </span>

      </a-table>
    </div>

    <reimburseBizVatDeductionVouchers-modal ref="modalForm" @ok="modalFormOk" :mainId="mainId"></reimburseBizVatDeductionVouchers-modal>
  </a-card>
</template>

<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import ReimburseBizVatDeductionVouchersModal from './modules/ReimburseBizVatDeductionVouchersModal'



  export default {
    name: "ReimburseBizVatDeductionVouchersList",
    mixins:[JeecgListMixin],
    components: { ReimburseBizVatDeductionVouchersModal },
    props:{
      mainId:{
        type:String,
        default:'',
        required:false
      }
    },
    watch:{
      mainId:{
        immediate: true,
        handler(val) {
          if(!this.mainId){
            this.clearList()
          }else{
            this.queryParam['applyNo'] = val
            this.loadData(1);
          }
        }
      }
    },
    data () {
      return {
        description: '报销单基本信息管理页面',
        disableMixinCreated:true,
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
            title:'报销单编号',
            align:"center",
            dataIndex: 'applyNo'
          },
          {
            title:'发票号码',
            align:"center",
            dataIndex: 'docNum'
          },
          {
            title:'发票代码',
            align:"center",
            dataIndex: 'invoiceCode'
          },
          {
            title:'开票日期',
            align:"center",
            dataIndex: 'docDate',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'是否需抵扣：Y-是/N-否，指发票本身是否需要进行抵扣',
            align:"center",
            dataIndex: 'offsetFlag'
          },
          {
            title:'原发票号码',
            align:"center",
            dataIndex: 'docNumOriginal'
          },
          {
            title:'原发票代码',
            align:"center",
            dataIndex: 'invoiceCodeOriginal'
          },
          {
            title:'增值税扣税凭证类型',
            align:"center",
            dataIndex: 'voucherType'
          },
          {
            title:'增值税扣税凭证类型名称',
            align:"center",
            dataIndex: 'voucherTypeName'
          },
          {
            title:'金额',
            align:"center",
            dataIndex: 'invoiceAmount'
          },
          {
            title:'税额',
            align:"center",
            dataIndex: 'invoiceTaxAmount'
          },
          {
            title:'税率',
            align:"center",
            dataIndex: 'invoiceTaxRate'
          },
          {
            title:'价税合计',
            align:"center",
            dataIndex: 'invoiceTotalAmount'
          },
          {
            title:'购方纳税人识别号',
            align:"center",
            dataIndex: 'buyTaxIdentiNum'
          },
          {
            title:'购方纳税人名称',
            align:"center",
            dataIndex: 'buyTaxIdentiName'
          },
          {
            title:'销方纳税人识别号',
            align:"center",
            dataIndex: 'sellerTaxIdentiNum'
          },
          {
            title:'销方纳税人名称',
            align:"center",
            dataIndex: 'sellerTaxIdentiName'
          },
          {
            title:'发票认证结果:1：认证通过；2：认证不通过；3：未认证; 4：无需认证',
            align:"center",
            dataIndex: 'invoiceResult'
          },
          {
            title:'发票认证结果名称：1：认证通过；2：认证不通过；3：未认证; 4：无需认证',
            align:"center",
            dataIndex: 'invoiceResultName'
          },
          {
            title:'报账单认证结果:1.都通过；2：部分通过；3：都未通过；4：认证异常',
            align:"center",
            dataIndex: 'claimResult'
          },
          {
            title:'认证日期',
            align:"center",
            dataIndex: 'authenticationDate',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'发票签收时间',
            align:"center",
            dataIndex: 'signDate',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'凭证是否退回',
            align:"center",
            dataIndex: 'voucherReturnFlag'
          },
          {
            title:'报账单处理时间',
            align:"center",
            dataIndex: 'processDate',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'是否红字发票：N- 否，Y- 是',
            align:"center",
            dataIndex: 'redInvoiceFlag'
          },
          {
            title:'红字发票通知单编号',
            align:"center",
            dataIndex: 'redInvoiceNum'
          },
          {
            title:'红字发票通知单开具日期',
            align:"center",
            dataIndex: 'redInvoiceDate',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'是否汇总',
            align:"center",
            dataIndex: 'isSum'
          },
          {
            title:'备注',
            align:"center",
            dataIndex: 'remarks'
          },
          {
            title:'红字发票描述',
            align:"center",
            dataIndex: 'redInvoiceDescription'
          },
          {
            title:'供应商编号',
            align:"center",
            dataIndex: 'vendorNum'
          },
          {
            title:'供应商名称',
            align:"center",
            dataIndex: 'vendorName'
          },
          {
            title:'项目属性：Y-动产/N-不动产',
            align:"center",
            dataIndex: 'projectAttr'
          },
          {
            title:'结转税额标识（0、动产项目或成本费用；1、不动产项目并且第一次结转；2、不动产项目并且第二次结转；3、动产转为不动产项目；4、不动产转为动产项目）',
            align:"center",
            dataIndex: 'transferTaxFlag'
          },
          {
            title:'进项税额转出会计期间',
            align:"center",
            dataIndex: 'inputTaxTurnDate',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'不抵扣进项税凭证是否汇总：Y-是，N-否',
            align:"center",
            dataIndex: 'isSumBdk'
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
          list: "/biz/reimburseBizMainInfo/listReimburseBizVatDeductionVouchersByMainId",
          delete: "/biz/reimburseBizMainInfo/deleteReimburseBizVatDeductionVouchers",
          deleteBatch: "/biz/reimburseBizMainInfo/deleteBatchReimburseBizVatDeductionVouchers"
        },
        dictOptions:{
        },

      }
    },
    methods: {
      clearList(){
        this.dataSource=[]
        this.selectedRowKeys=[]
        this.ipagination.current = 1
      },

    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less'
</style>
