<template>
  <page-layout :title="getTitle" logo="https://gw.alipayobjects.com/zos/rmsportal/nxkuOJlFJuAUhzlMTCEe.png">
    <!-- actions -->
    <template slot="action">
      <a-button-group style="margin-right: 4px;">
        <a-button>关闭</a-button>
        <a-button>打印</a-button>
        <a-button><a-icon type="ellipsis"/></a-button>
      </a-button-group>
      <a-button type="primary" >填写处理意见</a-button>
    </template>
    <detail-list slot="headerContent" size="small" :col="2" class="detail-layout" :datasrc="getRbmsMainInfo">
      <detail-list-item term="报销单编号" >{{this.rmbsBizMainInfo.applyNo}}</detail-list-item>
      <detail-list-item term="创建时间">{{this.rmbsBizMainInfo.createTime}}</detail-list-item>
      <detail-list-item term="员工姓名">{{this.rmbsBizMainInfo.userName}}</detail-list-item>
      <detail-list-item term="员工编号">{{this.rmbsBizMainInfo.userNo}}</detail-list-item>
      <detail-list-item term="部门">{{this.rmbsBizMainInfo.userDepartName}}</detail-list-item>
      <detail-list-item term="员工职务">{{this.rmbsBizMainInfo.userPosition}}</detail-list-item>
      <detail-list-item term="电子邮箱">{{this.rmbsBizMainInfo.userEmail}}</detail-list-item>
      <detail-list-item term="手机号码">{{this.rmbsBizMainInfo.userMobile}}</detail-list-item>
      <detail-list-item term="是否预付">{{this.rmbsBizMainInfo.isPrepay=='Y'?'是':'否'}}</detail-list-item>
      <detail-list-item term="是否含增值税扣税凭证">{{this.rmbsBizMainInfo.existOffsetFlag=='Y'?'是':'否'}}</detail-list-item>
      <detail-list-item term="是否有订单">{{this.rmbsBizMainInfo.existOrder=='Y'?'是':'否'}}</detail-list-item>
      <detail-list-item term="支付方式****" >{{this.rmbsBizMainInfo.paymentType}}<span slot-scope="rmbsBizMainInfo"></span></detail-list-item>
      <detail-list-item term="是否关联交易">{{this.rmbsBizMainInfo.isConntrans=='Y'?'是':'否'}}</detail-list-item>
      <detail-list-item term="供应商编号">{{this.rmbsBizMainInfo.vendorCode}}</detail-list-item>
      <detail-list-item term="供应商名称">{{this.rmbsBizMainInfo.vendorName}}</detail-list-item>
      <detail-list-item term="供应商地点">{{this.rmbsBizMainInfo.vendorAddress}}</detail-list-item>
      <detail-list-item term="供应商资质">{{this.rmbsBizMainInfo.vendorVatFlag}}</detail-list-item>
      <a-row><a-col></a-col></a-row>
      <detail-list-item term="本次发票金额">{{this.rmbsBizMainInfo.invoiceAmount | currency}}</detail-list-item>
      <detail-list-item term="本次付款金额">{{this.rmbsBizMainInfo.paymentAmount | currency}}</detail-list-item>
      <detail-list-item term="报销事由">{{this.rmbsBizMainInfo.reimbursementItem}}</detail-list-item>
      <a-row><a-col></a-col></a-row>
      <detail-list-item term="备注">{{this.rmbsBizMainInfo.remark}}</detail-list-item>
    </detail-list>
    <a-row slot="extra" class="status-list">
      <a-col :xs="12" :sm="12">
        <div class="text">状态</div>
        <div class="heading">{{this.rmbsBizMainInfo.procState}}</div>
      </a-col>
      <a-col :xs="12" :sm="12">
        <div class="text">发票金额</div>
        <div class="heading">{{this.rmbsBizMainInfo.invoiceAmount| currency}}</div>
      </a-col>
    </a-row>


    <a-card :bordered="false" title="流程进度">
      <a-steps :direction="isMobile() && 'vertical' || 'horizontal'" :current="1" progressDot>
        <a-step title="起草">
        </a-step>
        <a-step title="部门初审">
        </a-step>
        <a-step title="财务复核">
        </a-step>
        <a-step title="完成">
        </a-step>
      </a-steps>
    </a-card>

    <a-card style="margin-top: 24px" :bordered="false" title="费用明细" :datasrc="getRbmsDetailInfo">
      <a-table
        ref="table"
        size="middle"
        bordered
        rowKey="id"
        :columns="detailColumns"
        :dataSource="rmbsBizDetailInfo"
        :pagination="paginationDtl"
        :loading="loading"
      >
        <template
          slot="status"
          slot-scope="status">
          <a-badge :status="status | statusTypeFilter" :text="status | statusFilter"/>
        </template>
      </a-table>
    </a-card>
    <a-card style="margin-top: 24px" :bordered="false" title="付款清单" :datasrc="getRbmsPaymentInfo">
      <a-table
        ref="table"
        size="middle"
        bordered
        rowKey="id"
        :columns="paymentListColumns"
        :dataSource="rmbsBizPaymentListInfo"
        :pagination="paginationPayment"
        :loading="loading"
      >
        <template
          slot="status"
          slot-scope="status">
          <a-badge :status="status | statusTypeFilter" :text="status | statusFilter"/>
        </template>
      </a-table>
    </a-card>
    <a-card style="margin-top: 20px" :bordered="false" title="抵扣凭证" :datasrc="getRbmsVoucherInfo" v-if="this.rmbsBizMainInfo.existOffsetFlag=='Y'">
      <a-table
        ref="table"
        size="middle"
        bordered
        rowKey="id"
        :columns="voucherColumns"
        :dataSource="rmbsBizVoucherInfo"
        :pagination="paginationVoucher"
        :loading="loading"
      >
        <template
          slot="status"
          slot-scope="status">
          <a-badge :status="status | statusTypeFilter" :text="status | statusFilter"/>
        </template>
      </a-table>
    </a-card>

  </page-layout>
</template>

<script>
  import { mixinDevice } from '@/utils/mixin.js'
  import PageLayout from '@/components/page/PageLayout'
  import DetailList from '@/components/tools/DetailList'
  import { httpAction ,getAction} from '@/api/manage'
  import ARow from "ant-design-vue/es/grid/Row";
  import ACol from "ant-design-vue/es/grid/Col";

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import {currency} from '@/utils/currency';

  const DetailListItem = DetailList.Item

  export default {
    name: "RmbsBaseAuditView",
    mixins:[JeecgListMixin],
    components: {
      ACol,
      ARow,
      PageLayout,
      DetailList,
      DetailListItem
    },
    props:{


    },
    mixins: [mixinDevice],
    data () {
      return {
        rmbsBizMainInfo:{},
        rmbsBizDetailInfo:[],
        rmbsBizPaymentListInfo:[],
        rmbsBizVoucherInfo:[],
        paginationDtl: {
          current: 1,
          pageSize: 10,
          pageSizeOptions: ['10', '20', '30'],
          showTotal: (total, range) => {
            return range[0] + "-" + range[1] + " 共" + total + "条"
          }
         },
        paginationPayment: {
          current: 1,
          pageSize: 10,
          pageSizeOptions: ['10', '20', '30'],
          showTotal: (total, range) => {
            return range[0] + "-" + range[1] + " 共" + total + "条"
          }
        },
        paginationVoucher: {
          current: 1,
          pageSize: 10,
          pageSizeOptions: ['10', '20', '30'],
          showTotal: (total, range) => {
                return range[0] + "-" + range[1] + " 共" + total + "条"
        },
          showQuickJumper: true,
          showSizeChanger: true,
          total: 0},
        loading: false,
        detailColumns: [
          {
            title:'序号',
            align:"center",
            dataIndex: 'seq'
          },
          {
            title:'业务大类',
            align:"center",
            dataIndex: 'biztypeName'
          },
          {
            title:'业务小类',
            align:"center",
            dataIndex: 'feeItemName',
          },
          {
            title:'发票金额',
            align:"center",
            dataIndex: 'invoiceAmt',
            customRender: (val) => {return currency(val, '￥')}
          },
          {
            title:'发票价额',
            align:"center",
            dataIndex: 'invoicePriceAmt',
            customRender: (val) => {return currency(val, '￥')}
          },
          {
            title:'发票税额',
            align:"center",
            dataIndex: 'invoiceTaxAmt',
            customRender: (val) => {return currency(val, '￥')}
          },
          {
            title:'付款金额',
            align:"center",
            dataIndex: 'paymentAmt',
            customRender: (val) => {return currency(val, '￥')}
          },
          {
            title:'报账单明细说明',
            align:"center",
            dataIndex: 'dtlDesc'
          },
          {
            title:'会计科目名称',
            align:"center",
            dataIndex: 'erpAccountName'
          },
          {
            title:'成本中心名称',
            align:"center",
            dataIndex: 'costcenterName'
          }
        ],
        paymentListColumns:[
          {
            title:'序号',
            align:"center",
            dataIndex: 'seq'
          },
          {
            title:'付款清单说明',
            align:"center",
            dataIndex: 'remark'
          },
          {
            title:'开户行名称',
            align:"center",
            dataIndex: 'bankBranchName'
          },
          {
            title:'收方银行账号',
            align:"center",
            dataIndex: 'bankAccountNo'
          },
          {
            title:'户名',
            align:"center",
            dataIndex: 'bankAccountName'
          },
          {
            title:'账户类别',
            align:"center",
            dataIndex: 'bankAccountType_dictText',
          },
          {
            title:'本次支付金额',
            align:"center",
            dataIndex: 'paymentAmount',
            customRender: (val) => {return currency(val, '￥')}
          }
        ],
        voucherColumns:[
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
            title:'是否需抵扣',
            align:"center",
            dataIndex: 'offsetFlag_dictText'
          },
          {
            title:'增值税扣税凭证类型',
            align:"center",
            dataIndex: 'voucherType_dictText'
          },
          {
            title:'备注',
            align:"center",
            dataIndex: 'remarks'
          }
        ],
      }
    },
    filters:{
      statusFilter(status) {
        const statusMap = {
          'agree': '成功',
          'reject': '驳回'
        }
        return statusMap[status]
      },
      statusTypeFilter(type) {
        const statusTypeMap = {
          'agree': 'success',
          'reject': 'error'
        }
        return statusTypeMap[type]
      },
      currency:currency
    },
    mounted:function(){
           // console.log("applyNo==="+this.$route.params.applyNo);
           // console.log("title==="+JSON.stringify(document.getElementById("initPageOut")));
            //document.getElementById("initPageOut").title=this.$route.params.applyNo;
    },
    computed:{

      getTitle () {
        console.log("报销单编号=["+this.$route.params.applyNo+"]");
        if(this.$route.params.applyNo==undefined){
          return '';
        }
        return "报销单编号："+this.$route.params.applyNo;
      },

      getRbmsMainInfo(){
        let url = "/biz/reimburseBizMainInfo/getRbmsBizMainInfoForAudit";
        if(this.$route.params.applyNo==undefined){
          //this.$message.error("请设置url.list属性!");
          //this.rmbsBizMainInfo ={};
          return;
        }
        let params = {applyNo:this.$route.params.applyNo};
        this.rmbsBizMainInfo ={};
          getAction(url,params).then((res) => {
          if (res.success) {
            //console.log("applyMainInfo=="+JSON.stringify(res.result));
            this.rmbsBizMainInfo = res.result;
          }
        })
      },
      getRbmsDetailInfo(){
        //console.log("applyNo==="+this.$route.params.applyNo);
        if(this.$route.params.applyNo==undefined){
          //this.$message.error("请设置url.list属性!");
          //this.rmbsBizDetailInfo =[];
          return;
        }
        let url = "/biz/reimburseBizMainInfo/listReimburseBizBaseDetailInfoByMainId";
        let params = {applyNo:this.$route.params.applyNo};
        getAction(url,params).then((res) => {
          if (res.success) {
            //console.log("rmbsBizDetailInfo=="+JSON.stringify(res.result));
            this.rmbsBizDetailInfo = res.result.records;
            this.paginationDtl.total= res.result.total;
          }
        })
      },
    getRbmsPaymentInfo(){
      if(this.$route.params.applyNo==undefined){
        //this.$message.error("请设置url.list属性!");
        //this.rmbsBizPaymentListInfo =[];
        return;
      }
      let url = "/biz/reimburseBizMainInfo/listReimburseBizPaymentListByMainId";
      let params = {applyNo:this.$route.params.applyNo};
      getAction(url,params).then((res) => {
        if (res.success) {
          //console.log("rmbsBizDetailInfo=="+JSON.stringify(res.result));
          this.rmbsBizPaymentListInfo = res.result.records;
          this.paginationPayment.total= res.result.total;
        }
      })
    },
    getRbmsVoucherInfo(){
      if(this.$route.params.applyNo==undefined){
        //this.$message.error("请设置url.list属性!");
        //this.rmbsBizVoucherInfo =[];
        return;
      }
      let url = "/biz/reimburseBizMainInfo/listReimburseBizVatDeductionVouchersByMainId";
      let params = {applyNo:this.$route.params.applyNo};
      getAction(url,params).then((res) => {
        if (res.success) {
          //console.log("rmbsBizDetailInfo=="+JSON.stringify(res.result));
          this.rmbsBizVoucherInfo = res.result.records;
          this.paginationVoucher.total= res.result.total;
        }
      })
    }
    },
    methods:{
      getRbmsMainInfoForAudit(applyNo){
        let url = "/biz/reimburseBizMainInf/getRbsBizMainInfoForAudit";
        let params = {applyNo:applyNo};
        getAction(url,this.getQueryParams()).then((res) => {
          if (res.success) {
            console.log("applyMainInfo=="+res.result);
          }
        })
      },
      getQueryParams() {
        //获取查询条件
        let sqp = {}
        if(this.superQueryParams){
          sqp['superQueryParams']=encodeURI(this.superQueryParams)
        }
        var param = Object.assign(sqp, this.queryParam, this.isorter ,this.filters);
        param.field = "applyNo";
        param.pageNo = this.ipagination.current;
        param.pageSize = this.ipagination.pageSize;
        return filterObj(param);
      }

    }

  }
</script>

<style lang="scss" scoped>

  .detail-layout {
    margin-left: 44px;
  }
  .text {
    color: rgba(0, 0, 0, .45);
  }

  .heading {
    color: rgba(0, 0, 0, .85);
    font-size: 20px;
  }

  .no-data {
    color: rgba(0, 0, 0, .25);
    text-align: center;
    line-height: 64px;
    font-size: 16px;

    i {
      font-size: 24px;
      margin-right: 16px;
      position: relative;
      top: 3px;
    }
  }

  .mobile {
    .detail-layout {
      margin-left: unset;
    }
    .text {

    }
    .status-list {
      text-align: left;
    }
  }
</style>