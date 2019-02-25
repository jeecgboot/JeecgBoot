<template>
  <a-card :bordered="false" class="card-area">

    <!-- 查询区域 -->
    <div>
      <!-- 搜索区域 -->
      <a-form layout="horizontal">
        <div class="fold">
          <a-row >
            <a-col :md="12" :sm="24" >
              <a-form-item
                label="名称"
                :labelCol="{span: 5}"
                :wrapperCol="{span: 18, offset: 1}">
                <a-input placeholder="请输入名称查询" v-model="queryParam.roleName"></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="12" :sm="24" >
              <a-form-item
                label="创建时间"
                :labelCol="{span: 5}"
                :wrapperCol="{span: 18, offset: 1}">
                <a-range-picker @change="onChangeDate" v-model="queryParam.createTimeIsRange"/>
              </a-form-item>
            </a-col>
          </a-row>
        </div>
        <span style="float: right; margin-top: 3px;">
          <a-button type="primary" @click="searchQuery">查询</a-button>
          <a-button style="margin-left: 8px" @click="searchReset">重置</a-button>
        </span>
      </a-form>
    </div>

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>

      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px">
          批量操作 <a-icon type="down" />
        </a-button>
      </a-dropdown>
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择&nbsp;<a style="font-weight: 600">{{ selectedRowKeys.length }}</a>项&nbsp;&nbsp;
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
            <a class="ant-dropdown-link">
              更多 <a-icon type="down" />
            </a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handlePerssion(record.id)">授权</a>
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
    <role-modal ref="roleModal" @ok="modalFormOk"></role-modal>
    <user-role-modal ref="modalUserRole"></user-role-modal>
  </a-card>
</template>

<script>
  import RoleModal from './modules/RoleModal'
  import { filterObj } from '@/utils/util'
  import UserRoleModal from './modules/UserRoleModal'
  import {getRoleList,deleteRole,deleteRoleList} from '@/api/api'

  export default {
    name: "RoleList",
    components: {
      RoleModal,
      UserRoleModal
    },
    data () {
      return {
        description: '角色管理页面',
        // 查询条件
        queryParam: {roleName:'',createTimeIsRange:[]},
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
            title: '角色名称',
            align:"center",
            dataIndex: 'roleName'
          },
          {
            title: '角色编码',
            align:"center",
            dataIndex: 'roleCode'
          },
          {
            title: '备注',
            align:"center",
            dataIndex: 'description'
          },
          {
            title: '创建时间',
            dataIndex: 'createTime',
            align:"center",
            sorter: true
          },
          {
            title: '更新时间',
            dataIndex: 'updateTime',
            align:"center",
            sorter: true
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            scopedSlots: { customRender: 'action' },
          }
        ],
        //数据集
        dataSource:[],
        // 分页参数
        ipagination:{
          current: 1,
          pageSize: 10,
          pageSizeOptions: ['10', '20', '30'],
          showTotal: (total, range) => {
            return range[0] + "-" + range[1] + " 共" + total + "条"
          },
          showQuickJumper: true,
          showSizeChanger: true,
          total: 0
        },
        isorter:{
          column: 'createTime',
          order: 'desc',
        },
        loading:false,
        selectedRowKeys: [],
        selectedRows: [],

      }
    },
    created() {
      this.loadData();
    },
    methods: {
      loadData (arg){
        //加载数据 若传入参数1则加载第一页的内容
        if(arg===1){
          this.ipagination.current = 1;
        }
        var params = this.getQueryParams();//查询条件
        getRoleList(params).then((res)=>{
          if(res.success){
            this.dataSource = res.result.records;
            this.ipagination.total = res.result.total;
          }
        })
      },
      getQueryParams(){
        var param = Object.assign({}, this.queryParam,this.isorter);
        param.field = this.getQueryField();
        param.pageNo = this.ipagination.current;
        param.pageSize = this.ipagination.pageSize;
        return filterObj(param);
      },
      getQueryField(){
        //TODO 字段权限控制
        var str = "id,";
        for(var a = 0;a<this.columns.length;a++){
          str+=","+this.columns[a].dataIndex;
        }
        return str;
      },
      onSelectChange (selectedRowKeys,selectionRows) {
        this.selectedRowKeys = selectedRowKeys;
        this.selectionRows = selectionRows;
      },
      onClearSelected(){
        this.selectedRowKeys = [];
        this.selectionRows = [];
      },
      searchQuery(){
        this.loadData(1);
      },
      searchReset(){
        var that = this;
        for(var a in that.queryParam){
          that.queryParam[a] = '';
        }
        that.loadData(1);
      },
      batchDel: function(){
        if(this.selectedRowKeys.length<=0){
          this.$message.warning('请选择一条记录！');
          return ;
        }else{
          var ids = "";
          for(var a =0;a<this.selectedRowKeys.length;a++){
            ids+=this.selectedRowKeys[a]+",";
          }
          var that = this;
          this.$confirm({
            title:"确认删除",
            content:"是否删除选中数据?",
            onOk: function(){
              deleteRoleList({ids: ids}).then((res)=>{
                if(res.success){
                  that.$message.success(res.message);
                  that.loadData();
                  that.onClearSelected();
                }else{
                  that.$message.warning(res.message);
                }
              });
            }
          });
        }
      },
      handleDelete: function(id){
        var that = this;
        deleteRole({id: id}).then((res)=>{
          if(res.success){
            that.$message.success(res.message);
            that.loadData();
          }else{
            that.$message.warning(res.message);
          }
        });
      },
      handleEdit: function(record){
        this.$refs.roleModal.edit(record);
        this.$refs.roleModal.title="编辑";
      },
      handleAdd: function(){
        this.$refs.roleModal.add();
        this.$refs.roleModal.title="新增";
      },
      handlePerssion: function(roleId){
       // alert(roleId);
        this.$refs.modalUserRole.show(roleId);
      },
      handleTableChange(pagination, filters, sorter){
        //分页、排序、筛选变化时触发
        console.log(sorter);
        //TODO 筛选
        if (Object.keys(sorter).length>0){
          this.isorter.column = sorter.field;
          this.isorter.order = "ascend"==sorter.order?"asc":"desc"
        }
        this.ipagination = pagination;
        this.loadData();
      },
      modalFormOk () {
        // 新增/修改 成功时，重载列表
        this.loadData();
      },
      onChangeDate(date, dateString) {
        console.log(date, dateString);
      },
    }
  }
</script>
<style scoped>
  .ant-card-body .table-operator{
    margin-bottom: 18px;
  }
  .ant-layout-content{
    margin:12px 16px 0 !important;
  }
  .ant-table-tbody .ant-table-row td{
    padding-top:15px;
    padding-bottom:15px;
  }
  .anty-row-operator button{margin: 0 5px}
  .ant-btn-danger{background-color: #ffffff}

  .ant-modal-cust-warp{height: 100%}
  .ant-modal-cust-warp .ant-modal-body{height:calc(100% - 110px) !important;overflow-y: auto}
  .ant-modal-cust-warp .ant-modal-content{height:90% !important;overflow-y: hidden}

  @import '../../assets/less/index.less';
</style>