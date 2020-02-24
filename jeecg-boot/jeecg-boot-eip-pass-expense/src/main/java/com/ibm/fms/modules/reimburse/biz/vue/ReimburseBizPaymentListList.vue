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

    <reimburseBizPaymentList-modal ref="modalForm" @ok="modalFormOk" :mainId="mainId"></reimburseBizPaymentList-modal>
  </a-card>
</template>

<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import ReimburseBizPaymentListModal from './modules/ReimburseBizPaymentListModal'



  export default {
    name: "ReimburseBizPaymentListList",
    mixins:[JeecgListMixin],
    components: { ReimburseBizPaymentListModal },
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
            title:'付款明细行序号',
            align:"center",
            dataIndex: 'seq'
          },
          {
            title:'付款清单说明',
            align:"center",
            dataIndex: 'remark'
          },
          {
            title:'收方行分支机构全称（行名）',
            align:"center",
            dataIndex: 'bankBranchName'
          },
          {
            title:'收方行分支机构联行号',
            align:"center",
            dataIndex: 'bankCnapsCode'
          },
          {
            title:'收方银行账号',
            align:"center",
            dataIndex: 'bankAccountNo'
          },
          {
            title:'收方银行账号户名',
            align:"center",
            dataIndex: 'bankAccountName'
          },
          {
            title:'账户类别',
            align:"center",
            dataIndex: 'bankAccountType_dictText',
          },
          {
            title:'支付结果联系人电话',
            align:"center",
            dataIndex: 'paymentContactorPhone'
          },
          {
            title:'ERP付款账户代码',
            align:"center",
            dataIndex: 'erpAccountComNo'
          },
          {
            title:'ERP付款账户名称',
            align:"center",
            dataIndex: 'erpAccountComRemark'
          },
          {
            title:'付方银行账号',
            align:"center",
            dataIndex: 'paymentAccountNo'
          },
          {
            title:'付方银行账号户名',
            align:"center",
            dataIndex: 'paymentAccountName'
          },
          {
            title:'本次支付金额',
            align:"center",
            dataIndex: 'paymentAmount'
          },
          {
            title:'银企银行类别：可发往不同银行接口',
            align:"center",
            dataIndex: 'bankType_dictText',
          },
          {
            title:'收方行所在省',
            align:"center",
            dataIndex: 'bankLocProvince_dictText',
          },
          {
            title:'收方行所在城市',
            align:"center",
            dataIndex: 'bankLocCity_dictText',
          },
          {
            title:'本次支付状态:0 未付款 1 付款成功  2付款失败 3 付款失败已重提交',
            align:"center",
            dataIndex: 'paymentStatus'
          },
          {
            title:'本次支付状态描述',
            align:"center",
            dataIndex: 'paymentStatusDesc'
          },
          {
            title:'业务分类代码',
            align:"center",
            dataIndex: 'biztypeCode'
          },
          {
            title:'业务分类名称',
            align:"center",
            dataIndex: 'biztypeName'
          },
          {
            title:'费用项代码',
            align:"center",
            dataIndex: 'feeItemCode'
          },
          {
            title:'费用项名称',
            align:"center",
            dataIndex: 'feeItemName'
          },
          {
            title:'本次支付对应ERP付款批模板ID',
            align:"center",
            dataIndex: 'batchPayTplId'
          },
          {
            title:'银行回单ID',
            align:"center",
            dataIndex: 'bankReceiptId'
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
  @import '~@assets/less/common.less'
</style>
