<template>
  <a-card :bordered="false">

    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <a-row :gutter="24">

          <a-col :md="6" :sm="8">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="类型">
              <!--          <a-input placeholder="请输入1是关于我们，2友情链接" v-decorator="['type', {}]" />-->
              <a-select
                showSearch
                placeholder="请输入类型"
                v-decorator="['type', {}]">
                <a-select-option v-for="d in typeData" :key="d.value" :value="d.value">{{d.text}}</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="内容">
              <a-input placeholder="请输入内容" v-model="queryParam.content"></a-input>
            </a-form-item>
          </a-col>
        <template v-if="toggleSearchStatus">
        <a-col :md="6" :sm="8">
            <a-form-item label="排序">
              <a-input placeholder="请输入排序" v-model="queryParam.sort"></a-input>
            </a-form-item>
          </a-col>
          </template>
          <a-col :md="6" :sm="8" >
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

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('配置')">导出</a-button>
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
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        @change="handleTableChange">

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
    <!-- table区域-end -->

    <!-- 表单区域 -->
    <config-modal ref="modalForm" @ok="modalFormOk"></config-modal>
  </a-card>
</template>

<script>
  import ConfigModal from './modules/ConfigModal'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import {initDictOptions, filterDictText} from '@/components/dict/JDictSelectUtil'
  import Vue from 'vue'

  export default {
    name: "ConfigList",
    mixins:[JeecgListMixin],
    components: {
      ConfigModal
    },
    data () {
      return {
        description: '配置管理页面',
        sexDictOptions: [],
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
            title: '类型',
            align:"center",
            dataIndex: 'type',
             customRender: (text) => {
               //字典值替换通用方法
               return filterDictText(this.sexDictOptions, text);
             }
           },
		   {
            title: '内容',
            align:"center",
            dataIndex: 'content'
           },
		   {
            title: '排序',
            align:"center",
            dataIndex: 'sort'
           },
		   {
            title: '备注',
            align:"center",
            dataIndex: 'remark'
           },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            scopedSlots: { customRender: 'action' },
          }
        ],
        typeData:[{
          value:1,
          text:'关于我们'
        },{
          value:2,
          text:'友情链接'
        }],
		url: {
          list: "/bjcl/config/list",
          delete: "/bjcl/config/delete",
          deleteBatch: "/bjcl/config/deleteBatch",
          exportXlsUrl: "bjcl/config/exportXls",
          importExcelUrl: "bjcl/config/importExcel",
       },
    }
  },
  computed: {
    importExcelUrl: function(){
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
    }
  },
    created () {
      // this.initDictConfig();
    },
    methods: {
      initDictConfig() {
        //初始化字典 - 性别
        initDictOptions('bjcl_config_type').then((res) => {
          if (res.success) {
            this.sexDictOptions = res.result;
          }
        });
      },
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less'
</style>