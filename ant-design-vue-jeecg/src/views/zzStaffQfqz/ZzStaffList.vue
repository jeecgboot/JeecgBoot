<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="6" :sm="8">
            <a-form-item label="群防群治组织">
              <j-dict-select-tag placeholder="群防群治组织" v-model="queryParam.zzOrg" dictCode="zz_qfqz,org_name,id"/>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="姓名">
              <a-input placeholder="请输入姓名" v-model="queryParam.name"></a-input>
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">
            <a-col :md="6" :sm="8">
              <a-form-item label="手机号">
                <a-input placeholder="请输入手机号" v-model="queryParam.phone"></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="身份证">
                <a-input placeholder="请输入身份证" v-model="queryParam.sfz"></a-input>
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
    <!-- 查询区域-END -->
    
    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('员工')">导出</a-button>
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

    <zzStaff-modal ref="modalForm" @ok="modalFormOk"></zzStaff-modal>
  </a-card>
</template>

<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import ZzStaffModal from './modules/ZzStaffModal'
  import JDictSelectTag from '@/components/dict/JDictSelectTag.vue'
  import {initDictOptions, filterMultiDictText} from '@/components/dict/JDictSelectUtil'

  export default {
    name: "ZzStaffList",
    mixins:[JeecgListMixin],
    components: {
      JDictSelectTag,
      ZzStaffModal
    },
    data () {
      return {
        description: '员工管理页面',
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
            title:'群防群治组织',
            align:"center",
            dataIndex: 'zzOrg',
            customRender:(text)=>{
              if(!text){
                return ''
              }else{
                return filterMultiDictText(this.dictOptions['zzOrg'], text+"")
              }
            }
          },
          {
            title:'姓名',
            align:"center",
            dataIndex: 'name'
          },
          {
            title:'性别',
            align:"center",
            dataIndex: 'sex',
            customRender:(text)=>{
              if(!text){
                return ''
              }else{
                return filterMultiDictText(this.dictOptions['sex'], text+"")
              }
            }
          },
          {
            title:'手机号',
            align:"center",
            dataIndex: 'phone'
          },
          // {
          //   title:'人员类型',
          //   align:"center",
          //   dataIndex: 'type',
          //   customRender:(text)=>{
          //     if(!text){
          //       return ''
          //     }else{
          //       return filterMultiDictText(this.dictOptions['type'], text+"")
          //     }
          //   }
          // },
          {
            title:'民族',
            align:"center",
            dataIndex: 'minZu',
            customRender:(text)=>{
              if(!text){
                return ''
              }else{
                return filterMultiDictText(this.dictOptions['minZu'], text+"")
              }
            }
          },
          {
            title:'政治面貌',
            align:"center",
            dataIndex: 'zzmm',
            customRender:(text)=>{
              if(!text){
                return ''
              }else{
                return filterMultiDictText(this.dictOptions['zzmm'], text+"")
              }
            }
          },
          {
            title:'身份证',
            align:"center",
            dataIndex: 'sfz'
          },
          {
            title:'生日',
            align:"center",
            dataIndex: 'birthday',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'级别',
            align:"center",
            dataIndex: 'lv',
            customRender:(text)=>{
              if(!text){
                return ''
              }else{
                return filterMultiDictText(this.dictOptions['lv'], text+"")
              }
            }
          },
          {
            title:'特长',
            align:"center",
            dataIndex: 'speciality'
          },
          {
            title:'职务',
            align:"center",
            dataIndex: 'job',
            customRender:(text)=>{
              if(!text){
                return ''
              }else{
                return filterMultiDictText(this.dictOptions['job'], text+"")
              }
            }
          },
          {
            title:'学历',
            align:"center",
            dataIndex: 'education',
            customRender:(text)=>{
              if(!text){
                return ''
              }else{
                return filterMultiDictText(this.dictOptions['education'], text+"")
              }
            }
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            scopedSlots: { customRender: 'action' }
          }
        ],
        url: {
          list: "/zzStaff/zzStaff/list",
          delete: "/zzStaff/zzStaff/delete",
          deleteBatch: "/zzStaff/zzStaff/deleteBatch",
          exportXlsUrl: "/zzStaff/zzStaff/exportXls",
          importExcelUrl: "zzStaff/zzStaff/importExcel",
        },
        dictOptions:{
         zzOrg:[],
         sex:[],
         type:[],
         minZu:[],
         zzmm:[],
         lv:[],
         job:[],
         education:[],
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
        initDictOptions('zz_qfqz,org_name,id').then((res) => {
          if (res.success) {
            this.$set(this.dictOptions, 'zzOrg', res.result)
          }
        })
        initDictOptions('sex').then((res) => {
          if (res.success) {
            this.$set(this.dictOptions, 'sex', res.result)
          }
        })
        initDictOptions('').then((res) => {
          if (res.success) {
            this.$set(this.dictOptions, 'type', res.result)
          }
        })
        initDictOptions('minzu').then((res) => {
          if (res.success) {
            this.$set(this.dictOptions, 'minZu', res.result)
          }
        })
        initDictOptions('zzmm').then((res) => {
          if (res.success) {
            this.$set(this.dictOptions, 'zzmm', res.result)
          }
        })
        initDictOptions('staff_lv').then((res) => {
          if (res.success) {
            this.$set(this.dictOptions, 'lv', res.result)
          }
        })
        initDictOptions('staff_job').then((res) => {
          if (res.success) {
            this.$set(this.dictOptions, 'job', res.result)
          }
        })
        initDictOptions('education').then((res) => {
          if (res.success) {
            this.$set(this.dictOptions, 'education', res.result)
          }
        })
      }
       
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less'
</style>