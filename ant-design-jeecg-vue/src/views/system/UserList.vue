<template>
  <a-card :bordered="false">

    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <a-row :gutter="24">

          <a-col :span="6">
            <a-form-item label="账号">
              <a-input placeholder="请输入账号查询" v-model="queryParam.username"></a-input>
            </a-form-item>
          </a-col>

          <a-col :span="6">
            <a-form-item label="性别">
              <a-select v-model="queryParam.sex" placeholder="请选择性别查询">
                <a-select-option value="">请选择性别查询</a-select-option>
                <a-select-option value="1">男性</a-select-option>
                <a-select-option value="2">女性</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>


          <template v-if="toggleSearchStatus">
            <a-col :span="6">
              <a-form-item label="邮箱">
                <a-input placeholder="请输入邮箱查询" v-model="queryParam.email"></a-input>
              </a-form-item>
            </a-col>

            <a-col :span="6">
              <a-form-item label="手机号码">
                <a-input placeholder="请输入手机号码查询" v-model="queryParam.phone"></a-input>
              </a-form-item>
            </a-col>

            <a-col :span="6">
              <a-form-item label="状态">
                <a-select v-model="queryParam.status" placeholder="请选择用户状态查询">
                  <a-select-option value="">请选择用户状态</a-select-option>
                  <a-select-option value="1">正常</a-select-option>
                  <a-select-option value="2">解冻</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
          </template>

          <a-col :span="6" >
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchByquery" icon="search">查询</a-button>
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
      <a-button @click="handleAdd" type="primary" icon="plus">添加用户</a-button>
      <!--<a-button

        style="margin-left:8px"
        v-if="selectedRowKeys.length > 0"
        ghost
        type="primary"
        icon="delete">批量删除</a-button>-->


      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay" @click="handleMenuClick">
          <a-menu-item key="1"><a-icon type="delete" @click="batchdel"/>删除</a-menu-item>
          <a-menu-item key="2"><a-icon type="lock" @click="batchFrozen('2')"/>冻结</a-menu-item>
          <a-menu-item key="3"><a-icon type="unlock" @click="batchFrozen('1')"/>解冻</a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px">
          批量操作 <a-icon type="down" />
        </a-button>
      </a-dropdown>
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i>
        已选择&nbsp;<a style="font-weight: 600">{{ selectedRowKeys.length }}</a>项&nbsp;&nbsp;
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      <a-table
        ref="table"
        bordered
        size="middle"
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        @change="handleTableChange">

        <template slot="avatarslot" slot-scope="text, record, index">
          <div class="anty-img-wrap">
            <img :src="getAvatarView(record.avatar)"/>
          </div>

        </template>

        <!--<span slot="action" slot-scope="text, record" class="anty-row-operator">
          <a-button type="primary" size="small" icon="setting">角色配置</a-button>
          <a-button type="default" size="small" icon="edit" @click="handleEdit(record)">编辑</a-button>
          <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
            <a-button type="danger" size="small" icon="delete">删除</a-button>
          </a-popconfirm>
        </span>-->

        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">
              更多 <a-icon type="down" />
            </a>
            <a-menu slot="overlay">
              <a-menu-item >
                <a href="javascript:;" @click="handleDetail(record)">详情</a>
              </a-menu-item>

              <a-menu-item >
                <a href="javascript:;" @click="handleChangePassword(record.username)">修改密码</a>
              </a-menu-item>

              <a-menu-item>
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>

              <a-menu-item v-if="record.status==1">
                <a-popconfirm title="确定冻结吗?" @confirm="() => handleFrozen(record.id,2)">
                  <a>冻结</a>
                </a-popconfirm>
              </a-menu-item>

              <a-menu-item v-if="record.status==2">
                <a-popconfirm title="确定解冻吗?" @confirm="() => handleFrozen(record.id,1)">
                  <a>解冻</a>
                </a-popconfirm>
              </a-menu-item>

            </a-menu>
          </a-dropdown>
        </span>


      </a-table>
    </div>
    <!-- table区域-end -->

    <user-modal ref="modal" @ok="modalFormOk"></user-modal>

    <password-modal ref="passwordmodal" @ok="passwordModalOk"></password-modal>

  </a-card>
</template>

<script>
  import { filterObj } from '@/utils/util';
  import UserModal from './modules/UserModal'
  import PasswordModal from './modules/PasswordModal'

  import {doMian,getUserList,deleteUser,deleteUserList,frozenBatch} from '@/api/api'

  export default {
    name: "UserList",
    components: {
      UserModal,
      PasswordModal
    },
    data () {
      return {
        description: '这是用户管理页面',
        queryParam: {},
        columns: [
          /*{
            title: '#',
            dataIndex: '',
            key:'rowIndex',
            width:60,
            align:"center",
            customRender:function (t,r,index) {
              return parseInt(index)+1;
            }
          },*/
          {
            title: '用户账号',
            align:"center",
            dataIndex: 'username',
            fixed:'left',
            width:200
          },
          {
            title: '真实姓名',
            align:"center",
            dataIndex: 'realname',
          },
          {
            title: '头像',
            align:"center",
            dataIndex: 'avatar',
            scopedSlots:{customRender:"avatarslot"}
          },

          {
            title: '性别',
            align:"center",
            dataIndex: 'sex',
            customRender:function (text) {
              if(text==1){
                return "男";
              }else if(text==2){
                return "女";
              }else{
                return text;
              }
            }
          },
          {
            title: '生日',
            align:"center",
            dataIndex: 'birthday'
          },
          {
            title: '手机号码',
            align:"center",
            dataIndex: 'phone'
          },
          {
            title: '邮箱',
            align:"center",
            dataIndex: 'email'
          },
          {
            title: '状态',
            align:"center",
            dataIndex: 'status',
            customRender:function (text) {
              if(text==1){
                return "正常";
              }else if(text==2){
                return "冻结";
              }else{
                return text;
              }
            }
          },
          {
            title: '创建时间',
            align:"center",
            dataIndex: 'createTime',
            sorter:true
          },
          {
            title: '操作',
            dataIndex: 'action',
            scopedSlots: { customRender: 'action' },
            fixed:"right",
            align:"center",
            width:150
          }

        ],
        dataSource:[],
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
        toggleSearchStatus:false,
        url: {
          imgerver:doMian+"/sys/common/view"
        },

      }
    },
    created() {
      this.loadData();
    },

    methods: {
      loadData (arg){
        if(arg===1){
          this.ipagination.current = 1;
        }
        let params = this.getQueryParams();//查询条件
        getUserList(params).then((res)=>{
          if(res.success){
            this.dataSource = res.result.records;
            this.ipagination.total = res.result.total;
          }
        })
      },
      getQueryParams(){
        let param = Object.assign({}, this.queryParam,this.isorter);
        param.field = this.getQueryField();
        param.current = this.ipagination.current;
        param.pageSize = this.ipagination.pageSize;
        return filterObj(param);
      },
      getQueryField(){
        let str = "id,";
        for(let a = 0;a<this.columns.length;a++){
          str+=","+this.columns[a].dataIndex;
        }
        return str;
      },
      getAvatarView:function(avatar){
        return this.url.imgerver +"/"+ avatar;
      },
      onSelectChange (selectedRowKeys,selectionRows) {
        this.selectedRowKeys = selectedRowKeys;
        this.selectionRows = selectionRows;
      },
      onClearSelected(){
        this.selectedRowKeys = [];
        this.selectionRows = [];
      },
      searchByquery(){
        this.loadData(1);
      },
      searchReset(){
        let that = this;
        Object.keys(that.queryParam).forEach(function(key){
          that.queryParam[key] = '';
        });
        that.loadData(1);
      },
      batchFrozen:function(status){
        if(this.selectedRowKeys.length<=0){
          this.$message.warning('请选择一条记录！');
          return false;
        }else{
          let ids = "";
          let that = this;
          that.selectedRowKeys.forEach(function(val) {
            ids+=val+",";
          });
          that.$confirm({
            title:"确认操作",
            content:"是否"+(status==1?"解冻":"冻结")+"选中账号?",
            onOk: function(){
              frozenBatch({ids: ids,status:status}).then((res)=>{
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
      batchdel: function(){
        if(this.selectedRowKeys.length<=0){
          this.$message.warning('请选择一条记录！');
          return false;
        }else{
          let ids = "";
          let that = this;
          that.selectedRowKeys.forEach(function(val) {
            ids+=val+",";
          });
          that.$confirm({
            title:"确认删除",
            content:"是否删除选中数据?",
            onOk: function(){
              deleteUserList({ids: ids}).then((res)=>{
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
      handleMenuClick(e){
        if(e.key==1){
          this.batchdel();
        }else if(e.key==2){
          this.batchFrozen(2);
        }else if(e.key==3){
          this.batchFrozen(1);
        }
      },
      handleDelete: function(id){
        let that = this;
        deleteUser({id: id}).then((res)=>{
          if(res.success){
            that.$message.success(res.message);
            that.loadData();
          }else{
            that.$message.warning(res.message);
          }
        });
      },
      handleFrozen: function(id,status){
        let that = this;
        frozenBatch({ids: id,status:status}).then((res)=>{
          if(res.success){
            that.$message.success(res.message);
            that.loadData();
          }else{
            that.$message.warning(res.message);
          }
        });
      },
      handleEdit: function(record){
        this.$refs.modal.edit(record);
        this.$refs.modal.title="编辑";
      },
      handleAdd: function(){
        this.$refs.modal.add();
        this.$refs.modal.title="新增";
      },
      handleDetail:function(record){
        this.$refs.modal.edit(record);
        this.$refs.modal.title="详情";
        this.$refs.modal.disableSubmit = true;
      },
      handleTableChange(pagination, filters, sorter){
        //TODO 筛选
        if (Object.keys(sorter).length>0){
          this.isorter.column = sorter.field;
          this.isorter.order = "ascend"==sorter.order?"asc":"desc"
        }
        this.ipagination = pagination;
        this.loadData();
      },
      handleToggleSearch(){
        this.toggleSearchStatus = !this.toggleSearchStatus;
      },
      handleChangePassword(username){
        this.$refs.passwordmodal.show(username);
      },
      modalFormOk () {
        this.loadData();
      },
      passwordModalOk(){
        //TODO 密码修改完成 不需要刷新页面，可以把datasource中的数据更新一下
      }
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

  .anty-img-wrap{height:25px;position: relative;}
  .anty-img-wrap > img{max-height:100%;}
</style>