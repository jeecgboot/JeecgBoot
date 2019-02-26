<template>
  <a-card :bordered="false">

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button
        @click="batchdel"
        style="margin-left:8px"
        v-if="selectedRowKeys.length > 0"
        ghost
        type="primary"
        icon="delete">批量删除</a-button>
    </div>

    <!-- table区域-begin -->
    <div>

      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i>
        已选择&nbsp;<a style="font-weight: 600">{{ selectedRowKeys.length }}</a>项&nbsp;&nbsp;
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      <a-table
        :columns="columns"
        size="middle"
        :dataSource="dataSource"
        :rowSelection="rowSelection"
      >

        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">
              更多 <a-icon type="down" />
            </a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a href="javascript:;" @click="handleDetail(record)">详情</a>
              </a-menu-item>

              <a-menu-item>
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>

              <!--<a-menu-item v-if="record.status==1">
                <a-popconfirm title="确定冻结吗?" @confirm="() => handleFrozen(record.id,2)">
                  <a>冻结</a>
                </a-popconfirm>
              </a-menu-item>

              <a-menu-item v-if="record.status==2">
                <a-popconfirm title="确定解冻吗?" @confirm="() => handleFrozen(record.id,1)">
                  <a>解冻</a>
                </a-popconfirm>
              </a-menu-item>-->

            </a-menu>
          </a-dropdown>
        </span>

      </a-table>

    </div>
    <!-- table区域-end -->

    <permission-modal ref="permissionmodal" @ok="modalFormOk"></permission-modal>

  </a-card>
</template>

<script>
  import PermissionModal from './modules/PermissionModal'
  import {getPermissionList,deletePermission,deletePermissionList} from '@/api/api'

  const columns = [
   {
      title: '菜单名称',
      dataIndex: 'name',
      key: 'name'
    },{
      title: '菜单类型',
      dataIndex: 'menuType',
      key: 'menuType',
      customRender:function (text) {
        if(text==0){
          return "菜单";
        }else if(text==1){
          return "菜单";
        }else if(text==2){
          return "按钮";
        }else{
          return text;
        }
      }
    },/*{
      title: '权限编码',
      dataIndex: 'perms',
      key: 'permissionCode',
    },*/{
      title: 'icon',
      dataIndex: 'icon',
      key: 'icon',
    },
    {
      title: '组件',
      dataIndex: 'component',
      key: 'component',
    },
    {
      title: '路径',
      dataIndex: 'url',
      key: 'url',
    },
    {
      title: '排序',
      dataIndex: 'sortNo',
      key: 'sortNo',
    },
    {
      title: '操作',
      dataIndex: 'action',
      scopedSlots: { customRender: 'action' },
      fixed:"right",
      align:"center",
      width:150
    }
  ];

  export default {
    name: "PermissionList",
    components: {
      PermissionModal
    },
    data () {
      return {
        description: '这是菜单管理页面',
        //数据集
        dataSource:[],
        // 表头
        columns:columns,
        rowSelection:{
          onChange: (selectedRowKeys) => {
            //console.log(`selectedRowKeys: ${selectedRowKeys}`, 'selectedRows: ', selectedRows);
            this.selectedRowKeys = selectedRowKeys;
          },

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
      loadData (){
        this.dataSource = [];
        getPermissionList().then((res)=>{
          if(res.success){
            console.log( res.result);
            this.dataSource = res.result;

            /*for(let a = 0;a<res.result.length;a++){
              let temp = res.result[a];

              this.dataSource.push(temp);
            }*/
          }
        })
      },
      onClearSelected(){
        this.selectedRowKeys = [];
      },
      handleAdd(){
        this.$refs.permissionmodal.add();
        this.$refs.permissionmodal.title="新增";
      },
      handleEdit: function(record){
        this.$refs.permissionmodal.edit(record);
        this.$refs.permissionmodal.title="编辑";
      },
      handleDetail(record){
        this.$refs.permissionmodal.edit(record);
        this.$refs.permissionmodal.title="详情";
        this.$refs.permissionmodal.disableSubmit = true;
      },
      handleDelete(id){
        var that = this;
        deletePermission({id: id}).then((res)=>{
          if(res.success){
            that.$message.success(res.message);
            that.loadData();
          }else{
            that.$message.warning(res.message);
          }
        });
      },
      batchdel: function(){
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
              deletePermissionList({ids: ids}).then((res)=>{
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
      modalFormOk () {
        // 新增/修改 成功时，重载列表
        this.loadData();
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
</style>