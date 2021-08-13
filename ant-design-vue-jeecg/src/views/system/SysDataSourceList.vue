<template>
  <a-card :bordered="false">

    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">

          <a-col :md="6" :sm="8">
            <a-form-item label="数据源名称">
              <a-input placeholder="请输入数据源名称" v-model="queryParam.name"/>
            </a-form-item>
          </a-col>

          <a-col :md="6" :sm="8">
            <a-form-item label="数据库类型">
              <j-dict-select-tag v-model="queryParam.dbType" placeholder="请选择数据库类型" dict-code="database_type"/>
            </a-form-item>
          </a-col>

          <a-col :md="6" :sm="8">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
            </span>
          </a-col>

        </a-row>
      </a-form>
    </div>

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('多数据源管理')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload>
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel">
            <a-icon type="delete"/>
            删除
          </a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作
          <a-icon type="down"/>
        </a-button>
      </a-dropdown>
    </div>

    <!-- table区域-begin -->
    <div>

      <a-alert type="info" showIcon style="margin-bottom: 16px;">
        <template slot="message">
          <span>已选择</span>
          <a style="font-weight: 600;padding: 0 4px;">{{ selectedRowKeys.length }}</a>
          <span>项</span>
          <a style="margin-left: 24px" @click="onClearSelected">清空</a>
        </template>
      </a-alert>

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

        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical"/>
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down"/></a>
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
    <!-- table区域-end -->

    <!-- 表单区域 -->
    <sys-data-source-modal ref="modalForm" @ok="modalFormOk"/>

  </a-card>
</template>

<script>
  import JEllipsis from '@/components/jeecg/JEllipsis'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import SysDataSourceModal from './modules/SysDataSourceModal'

  export default {
    name: 'SysDataSourceList',
    mixins: [JeecgListMixin],
    components: { JEllipsis, SysDataSourceModal },
    data() {
      let ellipsis = (v, l = 20) => (<j-ellipsis value={v} length={l}/>)
      return {
        description: '多数据源管理管理页面',
        // 表头
        columns: [
          {
            title: '#',
            dataIndex: '',
            key: 'rowIndex',
            width: 60,
            align: 'center',
            customRender: (t, r, index) => index + 1
          },
          {
            title: '数据源名称',
            align: 'center',
            dataIndex: 'name'
          },
          {
            title: '数据库类型',
            align: 'center',
            dataIndex: 'dbType_dictText'
          },
          {
            title: '驱动类',
            align: 'center',
            dataIndex: 'dbDriver',
            customRender: (t) => ellipsis(t)
          },
          {
            title: '数据源地址',
            align: 'center',
            dataIndex: 'dbUrl',
            customRender: (t) => ellipsis(t)
          },
          {
            title: '用户名',
            align: 'center',
            dataIndex: 'dbUsername'
          },
          {
            title: '操作',
            dataIndex: 'action',
            align: 'center',
            scopedSlots: { customRender: 'action' },
          }
        ],
        url: {
          list: '/sys/dataSource/list',
          delete: '/sys/dataSource/delete',
          deleteBatch: '/sys/dataSource/deleteBatch',
          exportXlsUrl: 'sys/dataSource/exportXls',
          importExcelUrl: 'sys/dataSource/importExcel',
        },
      }
    },
    computed: {
      importExcelUrl() {
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`
      }
    },
    methods: {}
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>