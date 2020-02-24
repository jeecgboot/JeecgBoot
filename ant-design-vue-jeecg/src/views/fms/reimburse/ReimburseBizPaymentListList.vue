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

    <reimburseBizPaymentList-modal ref="modalForm" @ok="modalFormOk" :mainId="mainId" :vendorCode="vendorCode" :vendorName="vendorName" :rmbsTemplateCode="rmbsTemplateCode" :rmbsTemplateName="rmbsTemplateName" ></reimburseBizPaymentList-modal>
  </a-card>
</template>

<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import ReimburseBizPaymentListModal from './modules/ReimburseBizPaymentListModal'
  import {currency} from '@/utils/currency'


  export default {
    name: "ReimburseBizPaymentListList",
    mixins:[JeecgListMixin],
    components: { ReimburseBizPaymentListModal },
    filters:{
      currency:currency
    },
      props:{
      mainId:{
        type:String,
        default:'',
        required:false
      },
      vendorCode:{
        type:String,
        default:'',
        required:false
       },
      vendorName:{
        type:String,
        default:'',
        required:false
      },
      rmbsTemplateCode:{
        type:String,
        default:'',
        required:false
      },
      rmbsTemplateName:{
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
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            scopedSlots: { customRender: 'action' },
          }
        ],
        url: {
          list: "/biz/reimburseBizMainInfo/listReimburseBizPaymentListByMainId",
          delete: "/biz/reimburseBizMainInfo/deleteReimburseBizPaymentList",
          deleteBatch: "/biz/reimburseBizMainInfo/deleteBatchReimburseBizPaymentList"
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
  @import '~@assets/less/common.less';
  th.column-money,
  td.column-money {
    text-align: right !important;
  }
</style>
