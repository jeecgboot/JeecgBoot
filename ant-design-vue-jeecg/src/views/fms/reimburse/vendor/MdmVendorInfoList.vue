<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="6" :sm="8">
            <a-form-item label="供应商名称">
              <a-input placeholder="请输入供应商名称" v-model="queryParam.vendorName"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="供应商编号（主数据编号）">
              <a-input placeholder="请输入供应商编号（主数据编号）" v-model="queryParam.mdCode"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
              <a @click="handleToggleSearch" style="margin-left: 8px">
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
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('主数据供应商信息')">导出</a-button>
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
      <a-tab-pane tab="供应商归属组织" key="1" >
        <mdm-vendor-company-info-list :mainId="selectedMainId" />
      </a-tab-pane>
      <a-tab-pane tab="供应商银行账户" key="2" forceRender>
        <mdm-vendor-account-info-list :mainId="selectedMainId" />
      </a-tab-pane>
      <a-tab-pane tab="供应商联系人" key="3" forceRender>
        <mdm-vendor-contacts-info-list :mainId="selectedMainId" />
      </a-tab-pane>
    </a-tabs>

    <mdmVendorInfo-modal ref="modalForm" @ok="modalFormOk"></mdmVendorInfo-modal>
  </a-card>
</template>

<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import MdmVendorInfoModal from './modules/MdmVendorInfoModal'
  import { getAction } from '@/api/manage'
  import MdmVendorCompanyInfoList from './MdmVendorCompanyInfoList'
  import MdmVendorAccountInfoList from './MdmVendorAccountInfoList'
  import MdmVendorContactsInfoList from './MdmVendorContactsInfoList'

  export default {
    name: "MdmVendorInfoList",
    mixins:[JeecgListMixin],
    components: {
      MdmVendorCompanyInfoList,
      MdmVendorAccountInfoList,
      MdmVendorContactsInfoList,
      MdmVendorInfoModal
    },
    data () {
      return {
        description: '主数据供应商信息管理页面',
        // 表头
        columns: [
          {
            title:'供应商 ID',
            align:"center",
            dataIndex: 'vendorId'
          },
          {
            title:'供应商名称',
            align:"center",
            dataIndex: 'vendorName'
          },
          {
            title:'供应商编号',
            align:"center",
            dataIndex: 'mdCode'
          },
          {
            title:'供应商类型',
            align:"center",
            dataIndex: 'vendorType'
          },
          {
            title:'是否三证合一',
            align:"center",
            dataIndex: 'certificateFlag'
          },
          {
            title:'组织机构代码',
            align:"center",
            dataIndex: 'orgcertNumber'
          },
          {
            title:'统一社会信用代码',
            align:"center",
            dataIndex: 'socIden'
          },
          {
            title:'纳税标识',
            align:"center",
            dataIndex: 'vatFlag'
          },
          {
            title:'纳税登记编号',
            align:"center",
            dataIndex: 'taxRegistrationNumber'
          },
          {
            title:'是否内部关联方',
            align:"center",
            dataIndex: 'innerFlag'
          },
          {
            title:'内部关联方标识（往来段）',
            align:"center",
            dataIndex: 'innerCode'
          },
          {
            title:'是否交易方',
            align:"center",
            dataIndex: 'tradingParty'
          },
          {
            title:'客户编码',
            align:"center",
            dataIndex: 'cuetomerNumber'
          },
          {
            title:'组织机构类型',
            align:"center",
            dataIndex: 'corporationType'
          },
          {
            title:'更新时间',
            align:"center",
            dataIndex: 'lastUpdateDate',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'供应商状态',
            align:"center",
            dataIndex: 'vendorStatus'
          },
          {
            title:'供应商曾用名',
            align:"center",
            dataIndex: 'vendorOldName'
          },
          {
            title:'法定代表人/责任人',
            align:"center",
            dataIndex: 'legalRepresentative'
          },
          {
            title:'税务登记地址',
            align:"center",
            dataIndex: 'taxRegAddress'
          },
          {
            title:'办公地址',
            align:"center",
            dataIndex: 'officeAddress'
          },
          {
            title:'注册地址',
            align:"center",
            dataIndex: 'registryAddress'
          },
          {
            title:'经营范围',
            align:"center",
            dataIndex: 'businessRange'
          },
          {
            title:'公司类型',
            align:"center",
            dataIndex: 'companyType'
          },
          {
            title:'营业期限',
            align:"center",
            dataIndex: 'businessTimelimit'
          },
          {
            title:'注册资金',
            align:"center",
            dataIndex: 'registryFund'
          },
          {
            title:'注册币种',
            align:"center",
            dataIndex: 'registryCurrency'
          },
          {
            title:'供应商所在国家',
            align:"center",
            dataIndex: 'country'
          },
          {
            title:'供应商所在省份',
            align:"center",
            dataIndex: 'province'
          },
          {
            title:'供应商所在城市',
            align:"center",
            dataIndex: 'city'
          },
          {
            title:'来源系统',
            align:"center",
            dataIndex: 'sourceCode'
          },
          {
            title:'是否虚拟供应商',
            align:"center",
            dataIndex: 'virtualVendor'
          },
          {
            title:'虚拟供应商类型',
            align:"center",
            dataIndex: 'virtualType'
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
          list: "/base/mdmVendorInfo/list",
          delete: "/base/mdmVendorInfo/delete",
          deleteBatch: "/base/mdmVendorInfo/deleteBatch",
          exportXlsUrl: "/base/mdmVendorInfo/exportXls",
          importExcelUrl: "base/mdmVendorInfo/importExcel",
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