<template>
  <a-card :bordered="false">

    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <a-row :gutter="24">

          <a-col :span="6">
            <a-form-item label="报表编码">
              <a-input placeholder="请输入报表编码" v-model="queryParam.code"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item label="报表名字">
              <a-input placeholder="请输入报表名字" v-model="queryParam.name"></a-input>
            </a-form-item>
          </a-col>

          <a-col :span="8">
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
      <a-button @click="handleAdd" type="primary" icon="plus">录入</a-button>

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
        <a style="font-weight: 600">
          {{ selectedRowKeys.length }}
        </a>
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

        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical"/>
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down"/></a>
            <a-menu slot="overlay">
              <a-menu-item @click="popReportURL(record.id)">
                配置地址
              </a-menu-item>
              <a-menu-item>
                <a @click="goPageOnline(record.id)">功能测试</a>
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
    <!-- table区域-end -->

    <!-- 表单区域 -->
    <onlCgreportHead-modal ref="modalForm" @ok="modalFormOk"></onlCgreportHead-modal>

    <!-- 提示online报表链接 -->
    <a-modal
      title="报表访问链接"
      :visible="visible"
      @cancel="handleCancel">
      <template slot="footer">
        <a-button @click="handleCancel">关闭</a-button>
        <a-button type="primary" class="copy-this-text" :data-clipboard-text="reportUrlText" @click="onCopyUrl">复制</a-button>
      </template>
      <p>{{ reportUrlText }}</p>
    </a-modal>
  </a-card>
</template>

<script>
  import {JeecgListMixin} from '@/mixins/JeecgListMixin'
  import Clipboard from 'clipboard'
  import { getAction } from '@/api/manage'

  export default {
    name: 'OnlCgreportHeadList',
    mixins: [JeecgListMixin],
    components: {
      Clipboard
    },
    data() {
      return {
        description: '在线报表配置管理页面',
        visible:false,
        reportUrlText:'',
        // 表头
        columns: [
          {
            title: '报表名称',
            align: 'center',
            dataIndex: 'name'
          },
          {
            title: '编码',
            align: 'center',
            dataIndex: 'code'
          },
          {
            title: '查询SQL',
            align: 'center',
            dataIndex: 'cgrSql'
          },
          {
            title: '数据源',
            align: 'center',
            dataIndex: 'dbSource'
          },
          {
            title: '创建时间',
            align: 'center',
            dataIndex: 'createTime'
          },
          {
            title: '描述',
            align: 'center',
            dataIndex: 'content'
          },
          {
            title: '操作',
            dataIndex: 'action',
            align: 'center',
            scopedSlots: { customRender: 'action' }
          }
        ],
        url: {
          list: '/online/cgreport/head/list',
          delete: '/online/cgreport/head/delete',
          deleteBatch: '/online/cgreport/head/deleteBatch',
          getParamsInfo:'/online/cgreport/api/getParamsInfo/'
        }
      }
    },
    methods: {
      initReportUrlText(id){
        getAction(this.url.getParamsInfo+id).then((res) => {
          let textUrl = ""
          if (res.success) {
            if(res.result && res.result.length>0){
              for(let i of res.result){
                textUrl+=i.paramName+"=${"+i.paramName+"}&"
              }
            }
          } else {
            this.$message.warning(res.message)
          }
          if(textUrl.length>0){
            textUrl = textUrl.substring(0,textUrl.length-1)
            this.reportUrlText = `/online/cgreport/${id}?${textUrl}`
          }else{
            this.reportUrlText = `/online/cgreport/${id}`
          }
        })
      },
      goPageOnline(id){
        this.$router.push({path: '/online/cgreport/'+id})
      },
      popReportURL(id){
        this.visible = true;
        this.initReportUrlText(id)
      },
      handleCancel(){
        this.visible = false
        this.reportUrlText = '';
      },
      onCopyUrl(){
        var clipboard = new Clipboard('.copy-this-text')
        clipboard.on('success', () => {
          clipboard.destroy()
          this.$message.success('复制成功')
          this.handleCancel()
        })
        clipboard.on('error', () => {
          this.$message.error('该浏览器不支持自动复制')
          clipboard.destroy()
        })
      }
    }
  }
</script>
<style lang="less" scoped>
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
</style>