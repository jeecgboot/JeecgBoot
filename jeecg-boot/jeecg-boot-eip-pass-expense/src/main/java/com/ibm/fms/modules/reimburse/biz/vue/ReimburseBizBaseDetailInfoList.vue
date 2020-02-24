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

    <reimburseBizBaseDetailInfo-modal ref="modalForm" @ok="modalFormOk" :mainId="mainId"></reimburseBizBaseDetailInfo-modal>
  </a-card>
</template>

<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import ReimburseBizBaseDetailInfoModal from './modules/ReimburseBizBaseDetailInfoModal'



  export default {
    name: "ReimburseBizBaseDetailInfoList",
    mixins:[JeecgListMixin],
    components: { ReimburseBizBaseDetailInfoModal },
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
            title:'报账单编号',
            align:"center",
            dataIndex: 'applyNo'
          },
          {
            title:'明细序号',
            align:"center",
            dataIndex: 'seq'
          },
          {
            title:'退单原因代码',
            align:"center",
            dataIndex: 'withdrawCause'
          },
          {
            title:'费用项（业务大类）编码',
            align:"center",
            dataIndex: 'biztypeCode'
          },
          {
            title:'费用项（业务大类）名称',
            align:"center",
            dataIndex: 'biztypeName'
          },
          {
            title:'费用项名称',
            align:"center",
            dataIndex: 'feeItemName_dictText',
          },
          {
            title:'发票金额（价税总额）',
            align:"center",
            dataIndex: 'invoiceAmt'
          },
          {
            title:'发票价额',
            align:"center",
            dataIndex: 'invoicePriceAmt'
          },
          {
            title:'发票税额',
            align:"center",
            dataIndex: 'invoiceTaxAmt'
          },
          {
            title:'付款（支付）金额',
            align:"center",
            dataIndex: 'paymentAmt'
          },
          {
            title:'付款（支付）价额',
            align:"center",
            dataIndex: 'paymentPriceAmt'
          },
          {
            title:'付款（支付）税额',
            align:"center",
            dataIndex: 'paymentTaxAmt'
          },
          {
            title:'税率',
            align:"center",
            dataIndex: 'taxRate_dictText',
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
            title:'付款清单ID',
            align:"center",
            dataIndex: 'paymentListId'
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            scopedSlots: { customRender: 'action' },
          }
        ],
        url: {
          list: "/biz/reimburseBizMainInfo/listReimburseBizBaseDetailInfoByMainId",
          delete: "/biz/reimburseBizMainInfo/deleteReimburseBizBaseDetailInfo",
          deleteBatch: "/biz/reimburseBizMainInfo/deleteBatchReimburseBizBaseDetailInfo"
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
