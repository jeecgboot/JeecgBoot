<template>
  <a-card :bordered="false">

    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form-model layout="inline" :model="queryParam" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="6" :sm="8">
            <a-form-model-item label="规则名称" prop="ruleName">
              <a-input placeholder="请输入规则名称" v-model="queryParam.ruleName"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-model-item label="规则Code" prop="ruleCode">
              <a-input placeholder="请输入规则Code" v-model="queryParam.ruleCode"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
            </span>
          </a-col>
        </a-row>
      </a-form-model>
    </div>

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('填值规则')">导出</a-button>
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
    <a-alert type="info" showIcon style="margin-bottom: 16px;">
      <template slot="message">
        <span>已选择</span>
        <a style="font-weight: 600;padding: 0 4px;">{{ selectedRowKeys.length }}</a>
        <span>项</span>
        <template v-if="selectedRowKeys.length>0">
          <a-divider type="vertical"/>
          <a @click="onClearSelected">清空</a>
        </template>
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
              <a-menu-item @click="handleTest(record)">
                功能测试
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
    <!-- table区域-end -->

    <!-- 表单区域 -->
    <sys-fill-rule-modal ref="modalForm" @ok="modalFormOk"/>
  </a-card>
</template>

<script>
  import { getAction } from '@/api/manage'
  import SysFillRuleModal from './modules/SysFillRuleModal'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'

  export default {
    name: 'SysFillRuleList',
    mixins: [JeecgListMixin],
    components: { SysFillRuleModal },
    data() {
      return {
        description: '填值规则管理页面',
        // 表头
        columns: [
          {
            title: '#',
            dataIndex: '',
            key: 'rowIndex',
            width: 60,
            align: 'center',
            customRender: (t, r, index) => 1 + index
          },
          {
            title: '规则名称',
            align: 'center',
            dataIndex: 'ruleName'
          },
          {
            title: '规则Code',
            align: 'center',
            dataIndex: 'ruleCode'
          },
          {
            title: '规则实现类',
            align: 'center',
            dataIndex: 'ruleClass'
          },
          {
            title: '规则参数',
            align: 'center',
            dataIndex: 'ruleParams'
          },
          {
            title: '操作',
            dataIndex: 'action',
            align: 'center',
            scopedSlots: { customRender: 'action' },
          }
        ],
        url: {
          list: '/sys/fillRule/list',
          test: '/sys/fillRule/testFillRule',
          delete: '/sys/fillRule/delete',
          deleteBatch: '/sys/fillRule/deleteBatch',
          exportXlsUrl: '/sys/fillRule/exportXls',
          importExcelUrl: '/sys/fillRule/importExcel',
        },
      }
    },
    computed: {
      importExcelUrl() {
        return `${window._CONFIG['domianURL']}${this.url.importExcelUrl}`
      }
    },
    methods: {
      handleTest(record) {
        let closeLoading = this.$message.loading('生成中...', 0)

        getAction(this.url.test, {
          ruleCode: record.ruleCode
        }).then(res => {
          if (res.success) {
            this.$info({
              title: '填值规则功能测试',
              content: '生成结果：' + res.result
            })
          } else {
            this.$message.warn(res.message)
          }
        }).finally(() => {
          closeLoading()
        })
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less'
</style>