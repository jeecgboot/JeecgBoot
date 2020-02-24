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
      <a-button type="primary" icon="download" @click="handleExportXls('reimburse_base_biztype_accnt_item_rela')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload>
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
        :rowSelection="{fixed:true,selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        
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

    <reimburseBaseBiztypeAccntItemRela-modal ref="modalForm" @ok="modalFormOk"></reimburseBaseBiztypeAccntItemRela-modal>
  </a-card>
</template>

<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import ReimburseBaseBiztypeAccntItemRelaModal from './modules/ReimburseBaseBiztypeAccntItemRelaModal'

  export default {
    name: "ReimburseBaseBiztypeAccntItemRelaList",
    mixins:[JeecgListMixin],
    components: {
      ReimburseBaseBiztypeAccntItemRelaModal
    },
    data () {
      return {
        description: 'reimburse_base_biztype_accnt_item_rela管理页面',
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
            title:'支出成本类型编号',
            align:"center",
            dataIndex: 'costTypeCode'
          },
          {
            title:'支出成本类型名称',
            align:"center",
            dataIndex: 'costTypeName'
          },
          {
            title:'成本中心类型:1、基层，2、管理部门',
            align:"center",
            dataIndex: 'costcenterType'
          },
          {
            title:'业务大类代码',
            align:"center",
            dataIndex: 'bizTypeCode'
          },
          {
            title:'业务大类名称',
            align:"center",
            dataIndex: 'bizTypeName'
          },
          {
            title:'业务小类代码',
            align:"center",
            dataIndex: 'feeItemCode'
          },
          {
            title:'业务小类名称',
            align:"center",
            dataIndex: 'feeItemName'
          },
          {
            title:'会计科目代码',
            align:"center",
            dataIndex: 'accntCode'
          },
          {
            title:'会计科目名称',
            align:"center",
            dataIndex: 'accntName'
          },
          {
            title:'计提会计科目代码',
            align:"center",
            dataIndex: 'accrualAccntCode'
          },
          {
            title:'计提会计科目名称',
            align:"center",
            dataIndex: 'accrualAccntName'
          },
          {
            title:'负债类科目代码',
            align:"center",
            dataIndex: 'liabilitiesAccntCode'
          },
          {
            title:'负债类科目名称',
            align:"center",
            dataIndex: 'liabilitiesAccntName'
          },
          {
            title:'会计科目映射类别：0-普通，1-预付，2借款，3还款',
            align:"center",
            dataIndex: 'accntMapType'
          },
          {
            title:'reserve1',
            align:"center",
            dataIndex: 'reserve1'
          },
          {
            title:'reserve2',
            align:"center",
            dataIndex: 'reserve2'
          },
          {
            title:'reserve3',
            align:"center",
            dataIndex: 'reserve3'
          },
          {
            title:'reserve4',
            align:"center",
            dataIndex: 'reserve4'
          },
          {
            title:'reserve5',
            align:"center",
            dataIndex: 'reserve5'
          },
          {
            title:'是否有效',
            align:"center",
            dataIndex: 'enableFlag'
          },
          {
            title:'生效开始时间',
            align:"center",
            dataIndex: 'startValidTime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'生效结束时间',
            align:"center",
            dataIndex: 'endValidTime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
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
            scopedSlots: { customRender: 'action' }
          }
        ],
        url: {
          list: "/base/reimburseBaseBiztypeAccntItemRela/list",
          delete: "/base/reimburseBaseBiztypeAccntItemRela/delete",
          deleteBatch: "/base/reimburseBaseBiztypeAccntItemRela/deleteBatch",
          exportXlsUrl: "/base/reimburseBaseBiztypeAccntItemRela/exportXls",
          importExcelUrl: "base/reimburseBaseBiztypeAccntItemRela/importExcel",
        },
        dictOptions:{
        },
      }
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      }
    },
    methods: {
      initDictConfig(){
      }
       
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less'
</style>