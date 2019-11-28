<template>
  <a-card :bordered="false">

    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <a-row :gutter="24">

          <a-col :md="6" :sm="24">
            <a-form-item label="表名">
              <a-input placeholder="请输入表名" v-model="queryParam.tableName"></a-input>
            </a-form-item>
          </a-col>

          <a-col :md="6" :sm="24">
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
      <a-button @click="doCgformButton" type="primary" icon="highlight" style="margin-left:8px">自定义按钮</a-button>
      <a-button @click="doEnhanceJs" type="primary" icon="strikethrough" style="margin-left:8px">JS增强</a-button>
      <a-button @click="doEnhanceSql" type="primary" icon="filter" style="margin-left:8px">SQL增强</a-button>
      <a-button @click="doEnhanceJava" type="primary" icon="tool" style="margin-left:8px">Java增强</a-button>

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
        <i class="anticon anticon-info-circle ant-alert-icon"></i>
        已选择
        <a style="font-weight: 600">{{ selectedRowKeys.length }}</a>
        项
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

        <template slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical"/>
          <a-dropdown>
            <a class="ant-dropdown-link">更多
              <a-icon type="down"/>
            </a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="goPageOnline(record)">功能测试</a>
              </a-menu-item>

              <a-menu-item>
                <a @click="handleOnlineUrlShow(record)">配置地址</a>
              </a-menu-item>

              <a-menu-item>
                <a @click="handleRemoveRecord(record.id)">移除</a>
              </a-menu-item>

            </a-menu>
          </a-dropdown>
        </template>

        <template slot="dbsync" slot-scope="text">
          <span v-if="text==='Y'" style="color:limegreen">已同步</span>
          <span v-if="text==='N'" style="color:red">未同步</span>
        </template>

      </a-table>
    </div>
    <!-- table区域-end -->

    <!-- 表单区域 -->
    <onl-cgform-head-modal ref="modalForm" @ok="modalFormOk" :action-button="false"></onl-cgform-head-modal>

    <!-- 提示online报表链接 -->
    <a-modal
      :title="onlineUrlTitle"
      :visible="onlineUrlVisible"
      @cancel="handleOnlineUrlClose">
      <template slot="footer">
        <a-button @click="handleOnlineUrlClose">关闭</a-button>
        <a-button type="primary" class="copy-this-text" :data-clipboard-text="onlineUrl" @click="onCopyUrl">复制</a-button>
      </template>
      <p>{{ onlineUrl }}</p>
    </a-modal>

    <enhance-js ref="ehjs"></enhance-js>
    <enhance-sql ref="ehsql"></enhance-sql>
    <enhance-java ref="ehjava"></enhance-java>
    <trans-db2-online ref="transd2o" @ok="transOk"></trans-db2-online>

    <onl-cgform-button-list ref="btnList"></onl-cgform-button-list>
  </a-card>
</template>

<script>
  import { deleteAction, postAction,getAction } from '@/api/manage'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import Clipboard from 'clipboard'
  import { filterObj } from '@/utils/util';

  export default {
    name: 'OnlCgformHeadList',
    mixins: [JeecgListMixin],
    components: {
    },
    data() {
      return {
        description: 'Online表单视图',
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
            title: '视图表名',
            align: 'center',
            dataIndex: 'tableName'
          },
          {
            title: '视图表描述',
            align: 'center',
            dataIndex: 'tableTxt'
          },
          {
            title: '原表版本',
            align: 'center',
            dataIndex: 'tableVersion'
          },
          {
            title: '视图版本',
            align: 'center',
            dataIndex: 'copyVersion'
          },
          {
            title: '操作',
            dataIndex: 'action',
            align: 'center',
            scopedSlots: { customRender: 'action' }
          }
        ],
        url: {
          list: '/online/cgform/head/list',
          delete: '/online/cgform/head/delete',
          deleteBatch: '/online/cgform/head/deleteBatch',
          removeRecord: '/online/cgform/head/removeRecord',
        },
        tableTypeDictOptions: [],
        sexDictOptions: [],
        syncModalVisible: false,
        syncFormId: '',
        synMethod: 'normal',
        syncLoading: false,
        onlineUrlTitle: '',
        onlineUrlVisible: false,
        onlineUrl: '',
        selectedRowKeys: [],
        selectedRows: [],
        physicId:""
      }
    },
    watch: {
      '$route'() {
        this.loadData()
      }
    },
    methods: {
      getQueryParams() {
        //获取查询条件
        var param = Object.assign({}, this.queryParam, this.isorter ,this.filters);
        param.field = this.getQueryField();
        param.pageNo = this.ipagination.current;
        param.pageSize = this.ipagination.pageSize;
        param.copyType = 1;
        param.physicId = this.physicId;
        return filterObj(param);
      },

      loadData(arg) {
        if(!this.$route.params.code){
          return false
        }
        this.physicId = this.$route.params.code
        if(!this.url.list){
          this.$message.error("请设置url.list属性!")
          return
        }
        //加载数据 若传入参数1则加载第一页的内容
        if (arg === 1) {
          this.ipagination.current = 1;
        }
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
      },

      goPageOnline(rd) {
        if(rd.isTree=='Y'){
          this.$router.push({ path: '/online/cgformTreeList/' + rd.id })
        }else{
          this.$router.push({ path: '/online/cgformList/' + rd.id })
        }
      },
      handleOnlineUrlClose() {
        this.onlineUrlTitle = ''
        this.onlineUrlVisible = false
      },
      handleOnlineUrlShow(record) {
        if(record.isTree=='Y'){
          this.onlineUrl = `/online/cgformTreeList/${record.id}`
        }else{
          this.onlineUrl = `/online/cgformList/${record.id}`
        }
        this.onlineUrlVisible = true
        this.onlineUrlTitle = '菜单链接[' + record.tableTxt + ']'
      },
      handleRemoveRecord(id) {
        let that = this
        this.$confirm({
          title: '确认要移除此记录?',
          onOk() {
            deleteAction(that.url.removeRecord, { id: id }).then((res) => {
              if (res.success) {
                that.$message.success('移除成功')
                that.loadData()
              } else {
                that.$message.warning(res.message)
              }
            })
          },
          onCancel() {
          }
        })
      },
      doEnhanceJs() {
        if (!this.selectedRowKeys || this.selectedRowKeys.length != 1) {
          this.$message.warning('请先选中一条记录')
          return
        }
        this.$refs.ehjs.show(this.selectedRowKeys[0])
      },
      doEnhanceSql() {
        if (!this.selectedRowKeys || this.selectedRowKeys.length != 1) {
          this.$message.warning('请先选中一条记录')
          return
        }
        this.$refs.ehsql.show(this.selectedRowKeys[0])
      },
      doEnhanceJava() {
        if (!this.selectedRowKeys || this.selectedRowKeys.length != 1) {
          this.$message.warning('请先选中一条记录')
          return
        }
        this.$refs.ehjava.show(this.selectedRowKeys[0])
      },
      doCgformButton() {
        if (!this.selectedRowKeys || this.selectedRowKeys.length != 1) {
          this.$message.warning('请先选中一条记录')
          return
        }
        this.$refs.btnList.show(this.selectedRowKeys[0])

        //this.$router.push({ path: '/online/cgformButton/' + this.selectedRowKeys[0] })
      },
      importOnlineForm() {
        this.$refs.transd2o.show()
      },
      transOk() {
        this.loadData()
      },
      onSelectChange(keys, rows) {
        this.selectedRowKeys = keys
        this.selectedRows = rows
      },
      onCopyUrl(){
        var clipboard = new Clipboard('.copy-this-text')
        clipboard.on('success', () => {
          clipboard.destroy()
          this.$message.success('复制成功')
          this.handleOnlineUrlClose()
        })
        clipboard.on('error', () => {
          this.$message.error('该浏览器不支持自动复制')
          clipboard.destroy()
        })
      },
      showMyCopyInfo(id){
        console.log("查看复制表单的信息",id)
      },
      copyConfig(id){
        postAction(`${this.url.copyOnline}?code=${id}`).then(res=>{
          if(res.success){
            this.$message.success("复制成功")
            this.loadData()
          }else{
            this.$message.error("复制失败>>"+res.message)
          }
        })
      }

    }
  }
</script>
<style lang="less">
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

  .valid-error-cust{
    .ant-select-selection{
      border:2px solid #f5222d;
    }
  }
</style>