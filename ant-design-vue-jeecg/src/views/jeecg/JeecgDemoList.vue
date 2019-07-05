<template>
  <a-card :bordered="false">

    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <a-row :gutter="24">

          <a-col :md="6" :sm="8">
            <a-form-item label="名称">
              <a-input placeholder="请输入名称查询" v-model="queryParam.name"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="年龄">
              <a-input placeholder="请输入名称查询" v-model="queryParam.age"></a-input>
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">
            <a-col :md="6" :sm="8">
              <a-form-item label="字典下拉">
                <j-dict-select-tag v-model="queryParam.sex" placeholder="请选择用户名称" dictCode="sex"/>
              </a-form-item>
            </a-col>

            <a-col :md="6" :sm="8">
              <a-form-item label="字典表下拉">
                <j-dict-select-tag v-model="queryParam.realname" placeholder="请选择用户" dictCode="sys_user,realname,id"/>
              </a-form-item>
            </a-col>
          </template>
          <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
            <a-col :md="6" :sm="24">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
            </a-col>
          </span>
          <a-col :md="6" :sm="24">

            <template v-if="superQueryFlag">
              <a-tooltip title="已有高级查询条件生效!">
                <button :disabled="false" class="ant-btn ant-btn-primary" @click="superQuery">
                  <a-icon type="appstore" theme="twoTone" spin="true"></a-icon>
                  <span>高级查询</span>
                </button>
              </a-tooltip>
            </template>
            <a-button v-else type="primary" @click="superQuery" icon="filter">高级查询</a-button>

            <a @click="handleToggleSearch" style="margin-left: 8px">
              {{ toggleSearchStatus ? '收起' : '展开' }}
              <a-icon :type="toggleSearchStatus ? 'up' : 'down'"/>
            </a>
          </a-col>
        </a-row>
      </a-form>
    </div>

    <!-- 操作按钮区域 -->
    <div class="table-operator" style="margin-top: 5px">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" icon="plus" @click="jump">创建单据</a-button>
      <a-button type="primary" icon="plus" @click="onetomany">一对多</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('demo')">导出</a-button>
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
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{
          selectedRowKeys.length }}</a>项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
        <span style="float:right;">
          <a @click="loadData()"><a-icon type="sync" />刷新</a>
          <a-divider type="vertical" />
          <a-popover title="自定义列" trigger="click" placement="leftBottom">
            <template slot="content">
              <a-checkbox-group @change="onColSettingsChange" v-model="settingColumns" :defaultValue="settingColumns">
                <a-row>
                  <template v-for="(item,index) in defColumns">
                    <template v-if="item.key!='rowIndex'&& item.dataIndex!='action'">
                        <a-col :span="12"><a-checkbox :value="item.dataIndex">{{ item.title }}</a-checkbox></a-col>
                    </template>
                  </template>
                </a-row>
              </a-checkbox-group>
            </template>
            <a><a-icon type="setting" />自定义列</a>
          </a-popover>
        </span>
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

        <div slot="filterDropdown">
          <a-card>
            <a-checkbox-group @change="onColSettingsChange" v-model="settingColumns" :defaultValue="settingColumns">
              <a-row>
                <template v-for="(item,index) in defColumns">
                  <template v-if="item.key!='rowIndex'&& item.dataIndex!='action'">
                    <a-col :span="12"><a-checkbox :value="item.dataIndex">{{ item.title }}</a-checkbox></a-col>
                  </template>
                </template>
              </a-row>
            </a-checkbox-group>
          </a-card>
        </div>
        <a-icon slot="filterIcon" type='setting' :style="{ fontSize:'16px',color:  '#108ee9' }" />

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
    <jeecgDemo-modal ref="modalForm" @ok="modalFormOk"></jeecgDemo-modal>

    <!-- 一对多表单区域 -->
    <JeecgDemoTabsModal ref="jeecgDemoTabsModal" @ok="modalFormOk"></JeecgDemoTabsModal>

    <!-- 高级查询区域 -->
    <j-super-query :fieldList="fieldList" ref="superQueryModal" @handleSuperQuery="handleSuperQuery"></j-super-query>
  </a-card>
</template>

<script>
  import JeecgDemoModal from './modules/JeecgDemoModal'
  import JSuperQuery from '@/components/jeecg/JSuperQuery.vue';
  import JeecgDemoTabsModal from './modules/JeecgDemoTabsModal'
  import {initDictOptions, filterDictText} from '@/components/dict/JDictSelectUtil'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import Vue from 'vue'

  //高级查询modal需要参数
  const superQueryFieldList=[{
    type:"date",
    value:"birthday",
    text:"生日"
  },{
    type:"string",
    value:"name",
    text:"用户名"
  },{
    type:"int",
    value:"age",
    text:"年龄"
  }]
  export default {
    name: "JeecgDemoList",
    mixins:[JeecgListMixin],
    components: {
      JeecgDemoModal,
      JSuperQuery,
      JeecgDemoTabsModal,
    },
    data() {
      return {
        description: '用户管理页面',
        //字典数组缓存
        sexDictOptions: [],
        importExcelUrl:`${window._CONFIG['domianURL']}/test/jeecgDemo/importExcel`,
        //表头
        columns:[],
        //列设置
        settingColumns:[],
        //列定义
        defColumns: [
          {
            title: '#',
            dataIndex: '',
            key: 'rowIndex',
            width: 60,
            align: "center",
            customRender: function (t, r, index) {
              return parseInt(index) + 1;
            }
          },
          {
            title: '姓名',
            align: "center",
            dataIndex: 'name'
          },
          {
            title: '关键词',
            align: "center",
            dataIndex: 'keyWord'
          },
          {
            title: '打卡时间',
            align: "center",
            dataIndex: 'punchTime'
          },
          {
            title: '性别',
            align: "center",
            dataIndex: 'sex',
            customRender: (text) => {
              //字典值替换通用方法
              return filterDictText(this.sexDictOptions, text);
            }
          },
          {
            title: '年龄',
            align: "center",
            dataIndex: 'age'
          },
          {
            title: '生日',
            align: "center",
            dataIndex: 'birthday'
          },
          {
            title: '邮箱',
            align: "center",
            dataIndex: 'email'
          },
          {
            title: '个人简介',
            align: "center",
            dataIndex: 'content'
          },
          {
            title: '操作',
            dataIndex: 'action',
            align: "center",
            scopedSlots: {
              filterDropdown: 'filterDropdown',
              filterIcon: 'filterIcon',
              customRender: 'action'},
          }
        ],
        url: {
          list: "/test/jeecgDemo/list",
          delete: "/test/jeecgDemo/delete",
          deleteBatch: "/test/jeecgDemo/deleteBatch",
          exportXlsUrl: "/test/jeecgDemo/exportXls"
        },
        fieldList:superQueryFieldList
      }
    },
    methods: {
      initDictConfig() {
        console.log("--我才是真的方法!--")
        //初始化字典 - 性别
        initDictOptions('sex').then((res) => {
          if (res.success) {
            this.sexDictOptions = res.result;
          }
        });
      },
      onetomany: function () {
        this.$refs.jeecgDemoTabsModal.add();
        this.$refs.jeecgDemoTabsModal.title = "编辑";
      },
      //跳转单据页面
      jump() {
        this.$router.push({path: '/jeecg/helloworld'})
      },
      //列设置更改事件
      onColSettingsChange (checkedValues) {
        var key = this.$route.name+":colsettings";
        Vue.ls.set(key, checkedValues, 7 * 24 * 60 * 60 * 1000)
        this.settingColumns = checkedValues;
        const cols = this.defColumns.filter(item => {
          if(item.key =='rowIndex'|| item.dataIndex=='action'){
            return true
          }
          if (this.settingColumns.includes(item.dataIndex)) {
            return true
          }
          return false
        })
        this.columns =  cols;
      },
      initColumns(){
        //权限过滤（列权限控制时打开，修改第二个参数为授权码前缀）
        //this.defColumns = colAuthFilter(this.defColumns,'testdemo:');

        var key = this.$route.name+":colsettings";
        let colSettings= Vue.ls.get(key);
        if(colSettings==null||colSettings==undefined){
          let allSettingColumns = [];
          this.defColumns.forEach(function (item,i,array ) {
            allSettingColumns.push(item.dataIndex);
          })
          this.settingColumns = allSettingColumns;
          this.columns = this.defColumns;
        }else{
          this.settingColumns = colSettings;
          const cols = this.defColumns.filter(item => {
            if(item.key =='rowIndex'|| item.dataIndex=='action'){
              return true;
            }
            if (colSettings.includes(item.dataIndex)) {
              return true;
            }
            return false;
          })
          this.columns =  cols;
        }
      }
    },
    created() {
      this.initColumns();
    },
  }
</script>
<style scoped>
  .ant-card-body .table-operator {
    margin-bottom: 18px;
  }

  .ant-table-tbody .ant-table-row td {
    padding-top: 15px;
    padding-bottom: 15px;
  }

  .anty-row-operator button {
    margin: 0 5px
  }

  .ant-btn-danger {
    background-color: #ffffff
  }

  .ant-modal-cust-warp {
    height: 100%
  }

  .ant-modal-cust-warp .ant-modal-body {
    height: calc(100% - 110px) !important;
    overflow-y: auto
  }

  .ant-modal-cust-warp .ant-modal-content {
    height: 90% !important;
    overflow-y: hidden
  }
  /** Button按钮间距 */
  .ant-btn {
    margin-left: 3px
  }
</style>