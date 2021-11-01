<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="所属部门">
              <j-select-depart placeholder="请选择所属部门" v-model="queryParam.sysOrgCode"/>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="房屋户型">
              <a-input placeholder="请输入房屋户型" v-model="queryParam.houseDoorModel"></a-input>
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="房屋类型">
                <j-dict-select-tag placeholder="请选择房屋类型" v-model="queryParam.houseType" dictCode="house_type"/>
              </a-form-item>
            </a-col>
          </template>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
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
      <a-button type="primary" icon="download" @click="handleExportXls('房屋表')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload>
      <!-- 高级查询区域 -->
      <j-super-query :fieldList="superFieldList" ref="superQueryModal" @handleSuperQuery="handleSuperQuery"></j-super-query>
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
        :scroll="{x:true}"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        class="j-table-force-nowrap"
        @change="handleTableChange">

        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>
        <template slot="imgSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无图片</span>
          <img v-else :src="getImgView(text)" height="25px" alt="" style="max-width:80px;font-size: 12px;font-style: italic;"/>
        </template>
        <template slot="fileSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无文件</span>
          <a-button
            v-else
            :ghost="true"
            type="primary"
            icon="download"
            size="small"
            @click="downloadFile(text)">
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
                <a @click="handleDetail(record)">详情</a>
              </a-menu-item>
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

    <se-house-modal ref="modalForm" @ok="modalFormOk"></se-house-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import SeHouseModal from './modules/SeHouseModal'
  import {filterMultiDictText} from '@/components/dict/JDictSelectUtil'

  export default {
    name: 'SeHouseList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      SeHouseModal
    },
    data () {
      return {
        description: '房屋表管理页面',
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
            title:'所属部门',
            align:"center",
            dataIndex: 'sysOrgCode_dictText'
          },
          {
            title:'房屋编号(门牌号)',
            align:"center",
            dataIndex: 'houseCode'
          },
          {
            title:'房屋楼层',
            align:"center",
            dataIndex: 'houseLayer'
          },
          {
            title:'房屋户型',
            align:"center",
            dataIndex: 'houseDoorModel'
          },
          {
            title:'房屋类型',
            align:"center",
            dataIndex: 'houseType_dictText'
          },
          {
            title:'建筑面积(平方)',
            align:"center",
            dataIndex: 'houseFloorArea'
          },
          {
            title:'室内面积(平方)',
            align:"center",
            dataIndex: 'houseIndoorArea'
          },
          {
            title:'精装修 1表示是 0表示否',
            align:"center",
            dataIndex: 'izFineDecoration_dictText'
          },
          {
            title:'备注',
            align:"center",
            dataIndex: 'houseRemarks'
          },
          {
            title:'小区id',
            align:"center",
            dataIndex: 'commId_dictText'
          },
          {
            title:'楼宇id',
            align:"center",
            dataIndex: 'buildId_dictText'
          },
          {
            title:'单元id',
            align:"center",
            dataIndex: 'unitId_dictText'
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            fixed:"right",
            width:147,
            scopedSlots: { customRender: 'action' }
          }
        ],
        url: {
          list: "/property/seHouse/list",
          delete: "/property/seHouse/delete",
          deleteBatch: "/property/seHouse/deleteBatch",
          exportXlsUrl: "/property/seHouse/exportXls",
          importExcelUrl: "property/seHouse/importExcel",
          
        },
        dictOptions:{},
        superFieldList:[],
      }
    },
    created() {
    this.getSuperFieldList();
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      },
    },
    methods: {
      initDictConfig(){
      },
      getSuperFieldList(){
        let fieldList=[];
        fieldList.push({type:'sel_depart',value:'sysOrgCode',text:'所属部门'})
        fieldList.push({type:'string',value:'houseCode',text:'房屋编号(门牌号)',dictCode:''})
        fieldList.push({type:'string',value:'houseLayer',text:'房屋楼层',dictCode:''})
        fieldList.push({type:'string',value:'houseDoorModel',text:'房屋户型',dictCode:''})
        fieldList.push({type:'string',value:'houseType',text:'房屋类型',dictCode:'house_type'})
        fieldList.push({type:'BigDecimal',value:'houseFloorArea',text:'建筑面积(平方)',dictCode:''})
        fieldList.push({type:'BigDecimal',value:'houseIndoorArea',text:'室内面积(平方)',dictCode:''})
        fieldList.push({type:'BigDecimal',value:'calculationCoefficient',text:'算费系数',dictCode:''})
        fieldList.push({type:'string',value:'izFineDecoration',text:'精装修 1表示是 0表示否',dictCode:'fine_decoration'})
        fieldList.push({type:'string',value:'houseRemarks',text:'备注',dictCode:''})
        fieldList.push({type:'string',value:'commId',text:'小区id',dictCode:'se_community,name,id'})
        fieldList.push({type:'string',value:'buildId',text:'楼宇id',dictCode:'se_building,building_name,id'})
        fieldList.push({type:'string',value:'unitId',text:'单元id',dictCode:'se_unit,unit_code,id'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>