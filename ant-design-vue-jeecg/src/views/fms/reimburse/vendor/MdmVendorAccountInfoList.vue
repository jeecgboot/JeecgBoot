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

    <mdmVendorAccountInfo-modal ref="modalForm" @ok="modalFormOk" :mainId="mainId"></mdmVendorAccountInfo-modal>
  </a-card>
</template>

<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import MdmVendorAccountInfoModal from './modules/MdmVendorAccountInfoModal'



  export default {
    name: "MdmVendorAccountInfoList",
    mixins:[JeecgListMixin],
    components: { MdmVendorAccountInfoModal },
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
            this.queryParam['vendorId'] = val
            this.loadData(1);
          }
        }
      }
    },
    data () {
      return {
        description: '主数据供应商信息管理页面',
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
            title:'供应商公司 ID（地市公司组织 ID）',
            align:"center",
            dataIndex: 'vendorCompanyId'
          },
          {
            title:'供应商 ID',
            align:"center",
            dataIndex: 'vendorId'
          },
          {
            title:'供应商编号',
            align:"center",
            dataIndex: 'mdCode'
          },
          {
            title:'是否主账号',
            align:"center",
            dataIndex: 'mainAccountFlag'
          },
          {
            title:'银行名称',
            align:"center",
            dataIndex: 'bankName'
          },
          {
            title:'银行编码',
            align:"center",
            dataIndex: 'bankCode'
          },
          {
            title:'支行名称',
            align:"center",
            dataIndex: 'branchBank'
          },
          {
            title:'支行编码',
            align:"center",
            dataIndex: 'branchCode'
          },
          {
            title:'联行号',
            align:"center",
            dataIndex: 'cnapNumber'
          },
          {
            title:'银行户名',
            align:"center",
            dataIndex: 'accountName'
          },
          {
            title:'银行账号',
            align:"center",
            dataIndex: 'accountNumber'
          },
          {
            title:'账户币种',
            align:"center",
            dataIndex: 'accountCurrency'
          },
          {
            title:'银行账户是否有效',
            align:"center",
            dataIndex: 'validFlag'
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
            title: '操作',
            dataIndex: 'action',
            align:"center",
            scopedSlots: { customRender: 'action' },
          }
        ],
        url: {
          list: "/base/mdmVendorInfo/listMdmVendorAccountInfoByMainId",
          delete: "/base/mdmVendorInfo/deleteMdmVendorAccountInfo",
          deleteBatch: "/base/mdmVendorInfo/deleteBatchMdmVendorAccountInfo"
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
