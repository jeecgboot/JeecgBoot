<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="小区名称">
              <a-input placeholder="请输入小区名称" v-model="queryParam.name"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="小区地址">
              <a-input placeholder="请输入小区地址" v-model="queryParam.address"></a-input>
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="城市">
                <j-area-linkage type="cascader" v-model="queryParam.city" placeholder="请选择省市区" />
              </a-form-item>
            </a-col>
          </template>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
              <a @click="handleToggleSearch" style="margin-left: 8px">
                {{ toggleSearchStatus ? '收起' : '展开' }}
                <a-icon :type="toggleSearchStatus ? 'up' : 'down'" />
              </a>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button v-has="'community:add'" @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button v-has="'community:export'" type="primary" icon="download" @click="handleExportXls('小区表')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl"
                @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload>
      <!-- 高级查询区域 -->
      <j-super-query :fieldList="superFieldList" ref="superQueryModal"
                     @handleSuperQuery="handleSuperQuery"></j-super-query>
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel">
            <a-icon type="delete" />
            删除
          </a-menu-item>
        </a-menu>
        <a-button v-has="'community:batch'" style="margin-left: 8px"> 批量操作
          <a-icon v-has="'community:del'" type="down" />
        </a-button>
      </a-dropdown>
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a
        style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
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
          <img v-else :src="getImgView(text)" height="25px" alt=""
               style="max-width:80px;font-size: 12px;font-style: italic;" />
        </template>
        <template slot="pcaSlot" slot-scope="text">
          <div>{{ getPcaText(text) }}</div>
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
          <a v-has="'community:edit'" @click="handleEdit(record)">编辑</a>
          <a-divider type="vertical" />
          <a-dropdown>
            <a v-has="'community:more'" class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handleDetail(record)">详情</a>
              </a-menu-item>
              <a-menu-item>
                <a-popconfirm title="确定数据状态吗?" @confirm="() => freeze(record)">
                  <a v-has="'community:audit'">审核</a>
                </a-popconfirm>
              </a-menu-item>
              <a-menu-item>
                <a-popconfirm title="确定修改数据状态吗?" @confirm="() => freeze(record)">
                  <a v-has="'community:freeze'">冻结</a>
                </a-popconfirm>
              </a-menu-item>
              <a-menu-item>
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a v-has="'community:del'">删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>

    <se-community-modal ref="modalForm" @ok="modalFormOk"></se-community-modal>
  </a-card>
</template>

<script>

import '@/assets/less/TableExpand.less'
import Area from '@/components/_util/Area'
import { mixinDevice } from '@/utils/mixin'
import { httpAction, getAction, putAction } from '@api/manage'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import SeCommunityModal from '../modules/SeCommunityModal'
import { filterMultiDictText } from '@/components/dict/JDictSelectUtil'

export default {
  name: 'SeCommunityList',
  mixins: [JeecgListMixin, mixinDevice],
  components: {
    SeCommunityModal
  },
  data() {
    return {
      description: '小区表管理页面',
      // 表头
      columns: [
        {
          title: '#',
          dataIndex: '',
          key: 'rowIndex',
          width: 60,
          align: 'center',
          customRender: function(t, r, index) {
            return parseInt(index) + 1
          }
        },
        {
          title: '创建人',
          align: 'center',
          dataIndex: 'createBy'
        },
        {
          title: '城市',
          align: 'center',
          dataIndex: 'city',
          scopedSlots: { customRender: 'pcaSlot' }
        },
        {
          title: '小区地址',
          align: 'center',
          dataIndex: 'address'
        },
        {
          title: '小区名称',
          align: 'center',
          sorter: true,
          dataIndex: 'name'
        },

        {
          title: '小区面积',
          align: 'center',
          dataIndex: 'communityArea'
        },
        {
          title: '数据状态',
          align: 'center',
          dataIndex: 'coStatus',
          customRender: text => {
            let communityStatus = this.dictOptions['communityStatus'][parseInt(text)]
            return <a-tag color={communityStatus.tags}>{communityStatus}</a-tag>
          }
        },
        {
          title: '审核状态',
          align: 'center',
          dataIndex: 'verifyStatus',
          customRender: text => {
            let vetting = this.auditStatus['vetting'][parseInt(text)]
            return <a-tag color={vetting.tags}>{vetting}</a-tag>
          }
        },
        {
          title: '操作',
          dataIndex: 'action',
          align: 'center',
          fixed: 'right',
          width: 147,
          scopedSlots: { customRender: 'action' }
        }
      ],
      url: {
        list: '/property/seCommunity/list',
        freeze: '/property/seCommunity/freeze',
        delete: '/property/seCommunity/delete',
        deleteBatch: '/property/seCommunity/deleteBatch',
        exportXlsUrl: '/property/seCommunity/exportXls',
        importExcelUrl: 'property/seCommunity/importExcel'

      },
      dictOptions: {},
      pcaData: '',
      superFieldList: [],
      auditStatus: {}
    }
  },
  created() {
    this.pcaData = new Area()
    this.$set(this.dictOptions, 'communityStatus',
      [
        { text: '正常', value: '0', tags: 'green' },
        { text: '冻结', value: '1', tags: 'red' }
      ])
    this.$set(this.auditStatus, 'vetting', [
      { text: '审核中', value: '0', tags: 'orange' },
      { text: '审核通过', value: '1', tags: 'green' },
      { text: '审核驳回', value: '2', tags: 'red' }])

    this.getSuperFieldList()
  },
  computed: {
    importExcelUrl: function() {
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`
    }
  },
  methods: {
    getPcaText(code) {
      return this.pcaData.getText(code)
    },
    initDictConfig() {
    },
    getSuperFieldList() {
      let fieldList = []
      fieldList.push({ type: 'string', value: 'createBy', text: '创建人', dictCode: '' })
      fieldList.push({ type: 'sel_depart', value: 'sysOrgCode', text: '所属部门' })
      fieldList.push({ type: 'string', value: 'name', text: '小区名称', dictCode: '' })
      fieldList.push({ type: 'string', value: 'address', text: '小区地址', dictCode: '' })
      fieldList.push({ type: 'string', value: 'nearbyLandmarks', text: '地标', dictCode: '' })
      fieldList.push({ type: 'pca', value: 'city', text: '城市' })
      fieldList.push({ type: 'string', value: 'mapX', text: ' x坐标', dictCode: '' })
      fieldList.push({ type: 'string', value: 'mapY', text: ' y坐标', dictCode: '' })
      fieldList.push({ type: 'switch', value: 'coStatus', text: '数据状态' })
      fieldList.push({ type: 'string', value: 'verifyStatus', text: '审核状态', dictCode: 'vetting' })
      fieldList.push({ type: 'string', value: 'communityArea', text: '小区面积', dictCode: '' })
      this.superFieldList = fieldList
    },
    freeze(val) {
      if (val.coStatus === '0') {
        val.coStatus = '1'
      } else {
        val.coStatus = '0'
      }
      putAction(this.url.freeze, val).then((result) => {
        console.log(result)
      })
    }
  }
}
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>