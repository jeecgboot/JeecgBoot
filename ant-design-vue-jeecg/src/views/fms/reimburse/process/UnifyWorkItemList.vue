<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="6" :sm="8">
            <a-form-item label="报销单编号">
              <a-input placeholder="请输入报销单编号" v-model="queryParam.applyNo"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="报销事由">
              <a-input placeholder="请输入报销事由" v-model="queryParam.reimbursementItem"></a-input>
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
    <!-- 查询区域-END -->


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
        rowKey="applyNo"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :datasrc="initDataSource"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange, type:'checkbox'}"
        :customRow="clickThenSelect"
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
        //处理超长生成...,并加上提示文字代码
        <div :style="{maxWidth: '180px',whiteSpace: 'nowrap',textOverflow: 'ellipsis',overflow: 'hidden', wordWrap: 'break-word', wordBreak: 'break-all' }" slot="rbmsItem" slot-scope="text,record">
          <a-tooltip placement="left">
            <template slot="title">
              <span>{{record.reimbursementItem}}</span>
            </template>
            {{record.reimbursementItem}}
          </a-tooltip>
        </div>
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
        <span slot="handleWorkItem" slot-scope="text,record">
          <router-link :to="{name:'fms-audit-RmbsBaseAuditView',params:{applyNo:record.applyNo}}">{{record.applyNo}}</router-link>
        </span>
      </a-table>
    </div>
  </a-card>
</template>

<script>
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import { getAction } from '@/api/manage'


  export default {
      name: "UnifyWorkItemList",
      mixins:[JeecgListMixin],
      props:{
        mainId:{
          type:String,
          default:'',
          required:false
        },
        title:''
      },
      data () {
        return {
          description: '待办列表界面',
          disableMixinCreated:true,
          listUrl:'/biz/unifyworkitemList/pagedListWorkItem',
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
              title:'报销单号',
              align:"center",
             // dataIndex: 'applyNo',
              scopedSlots: { customRender: 'handleWorkItem' }
            },
            {
              title:'报销单类别',
              align:"center",
              dataIndex: 'rmbsTemplateName'
            },
            {
              title:'报销事由',
              align:"center",
              dataIndex: 'reimbursementItem',
              scopedSlots: { customRender: 'rbmsItem' }
            },
            {
              title:'起草时间',
              align:"center",
              dataIndex: 'createTime'
            },
            {
              title:'报销部门',
              align:"center",
              dataIndex: 'userDepartName'
            },
            {
              title:'报销人',
              align:"center",
              dataIndex: 'userName'
            },
            {
              title:'当前状态',
              align:"center",
              dataIndex: 'procState'
            },
            {
              title: '操作',
              dataIndex: 'action',
              align:"center",
              scopedSlots: { customRender: 'action' },
            }
          ],
          url: {
            list: "/biz/unifyworkitemList/pagedListWorkItem",
            delete: "/biz/reimburseBizMainInfo/deleteReimburseBizVatDeductionVouchers",
            deleteBatch: "/biz/reimburseBizMainInfo/deleteBatchReimburseBizVatDeductionVouchers"
          },
          dictOptions:{
          },

        }
      },
    computed:{
        initDataSource(){
          this.onClearSelected();
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
        }
      },
      methods: {
        clearList() {
          this.dataSource = []
          this.selectedRowKeys = []
          this.ipagination.current = 1
        },
        clickThenSelect(record) {
          return {
            on: {
              click: () => {
                this.onSelectChange(record.applyNo.split(","), [record]);
              }
            }
          }
        },
        onSelectChange(selectedRowKeys, selectionRows) {
          //this.selectedMainId=selectedRowKeys[0];
          this.selectedMainId = selectionRows[0].applyNo;
          this.selectedRowKeys = selectedRowKeys;
          this.selectionRows = selectionRows;
        },
        loadData(arg) {
          if(!this.url.list){
            this.$message.error("请设置url.list属性!")
            return
          }
          //加载数据 若传入参数1则加载第一页的内容
          if (arg === 1) {
            this.ipagination.current = 1;
          }
          this.onClearSelected()
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
        handleWorkItem(record){
          this.url.list="/base/mdmVendorInfo/list";
          this.loadData(1);
        }
      }
    }
</script>

<style scoped>

</style>