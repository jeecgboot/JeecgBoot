<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <!-- 搜索区域 -->
      <a-form layout="inline">
        <a-row :gutter="10">
          <a-col :md="10" :sm="12">
            <a-form-item label="用户账号" style="margin-left:8px">
              <a-input placeholder="请输入账号" v-model="queryParam.username"></a-input>
            </a-form-item>
          </a-col>
          <!--<a-col :md="8" :sm="8">-->
          <!--<a-form-item label="用户名称" :labelCol="{span: 5}" :wrapperCol="{span: 18, offset: 1}">-->
          <!--<a-input placeholder="请输入名称查询" v-model="queryParam.realname"></a-input>-->
          <!--</a-form-item>-->
          <!--</a-col>-->
          <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
            <a-col :md="6" :sm="24">
             <a-button type="primary" @click="searchQuery" icon="search" style="margin-left: 18px">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
            </a-col>
          </span>
        </a-row>
      </a-form>
    </div>
    <!-- 操作按钮区域 -->
    <div class="table-operator" :md="24" :sm="24" style="margin-top: -15px">
      <!--<a-button @click="handleEdit" type="primary" icon="edit" style="margin-top: 16px">用户编辑</a-button>-->
      <a-button @click="handleAddUserDepart" type="primary" icon="plus">添加已有用户</a-button>
      <a-button @click="handleAdd" type="primary" icon="plus" style="margin-top: 16px">新建用户</a-button>

      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel">
            <a-icon type="delete"/>
            取消关联
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
            <a class="ant-dropdown-link">
              更多 <a-icon type="down"/>
            </a>
            <a-menu slot="overlay">
                <a-menu-item>
                <a href="javascript:;" @click="handleDeptRole(record)">分配部门角色</a>
              </a-menu-item>

              <a-menu-item>
                <a href="javascript:;" @click="handleDetail(record)">用户详情</a>
              </a-menu-item>

              <a-menu-item>
                <a-popconfirm title="确定取消与选中部门关联吗?" @confirm="() => handleDelete(record.id)">
                  <a>取消关联</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>


      </a-table>
    </div>
    <!-- table区域-end -->

    <!-- 表单区域 -->
    <user-modal ref="modalForm" @ok="modalFormOk"></user-modal>
    <Select-User-Modal ref="selectUserModal" @selectFinished="selectOK"></Select-User-Modal>
    <dept-role-user-modal ref="deptRoleUser"></dept-role-user-modal>
  </a-card>
</template>

<script>
  import {JeecgListMixin} from '@/mixins/JeecgListMixin'
  import {getAction, postAction, deleteAction} from '@/api/manage'
  import SelectUserModal from './SelectUserModal'
  import UserModal from './UserModal'
  import DeptRoleUserModal from './DeptRoleUserModal'

  export default {
    name: "DeptUserInfo",
    mixins: [JeecgListMixin],
    components: {
      DeptRoleUserModal,
      SelectUserModal,
      UserModal
    },
    data() {
      return {
        description: '用户信息',
        currentDeptId: '',
        currentDept: {},
        // 表头
        columns: [{
            title: '用户账号',
            align: "center",
            dataIndex: 'username'
          },
          {
            title: '用户名称',
            align: "center",
            dataIndex: 'realname'
          },
          {
            title: '部门',
            align: "center",
            dataIndex: 'orgCode'
          },
          {
            title: '性别',
            align: "center",
            dataIndex: 'sex_dictText'
          },
          {
            title: '电话',
            align: "center",
            dataIndex: 'phone'
          },
          {
            title: '操作',
            dataIndex: 'action',
            scopedSlots: {customRender: 'action'},
            align: "center",
            width: 150
          }],
        url: {
          list: "/sys/user/departUserList",
          edit: "/sys/user/editSysDepartWithUser",
          delete: "/sys/user/deleteUserInDepart",
          deleteBatch: "/sys/user/deleteUserInDepartBatch",
        }
      }
    },
    created() {
    },

    methods: {
      searchReset() {
        this.queryParam = {}
        this.loadData(1);
      },
      loadData(arg) {
        if (!this.url.list) {
          this.$message.error("请设置url.list属性!")
          return
        }
        //加载数据 若传入参数1则加载第一页的内容
        if (arg === 1) {
          this.ipagination.current = 1;
        }
        //if (this.currentDeptId === '') return;
        let params = this.getQueryParams();//查询条件
        params.depId = this.currentDeptId;
        getAction(this.url.list, params).then((res) => {
          if (res.success && res.result) {
            this.dataSource = res.result.records;
            this.ipagination.total = res.result.total;
          }
        })
      },
      batchDel: function () {

        if (!this.url.deleteBatch) {
          this.$message.error("请设置url.deleteBatch属性!")
          return
        }
        if (!this.currentDeptId) {
          this.$message.error("未选中任何部门，无法取消部门与用户的关联!")
          return
        }

        if (this.selectedRowKeys.length <= 0) {
          this.$message.warning('请选择一条记录！');
          return;
        } else {
          var ids = "";
          for (var a = 0; a < this.selectedRowKeys.length; a++) {
            ids += this.selectedRowKeys[a] + ",";
          }
          var that = this;
          console.log(this.currentDeptId);
          this.$confirm({
            title: "确认取消",
            content: "是否取消用户与选中部门的关联?",
            onOk: function () {
              deleteAction(that.url.deleteBatch, {depId: that.currentDeptId, userIds: ids}).then((res) => {
                if (res.success) {
                  that.$message.success("删除用户与选中部门关系成功！");
                  that.loadData();
                  that.onClearSelected();
                } else {
                  that.$message.warning(res.message);
                }
              });
            }
          });
        }
      },
      handleDelete: function (id) {
        if (!this.url.delete) {
          this.$message.error("请设置url.delete属性!")
          return
        }
        if (!this.currentDeptId) {
          this.$message.error("未选中任何部门，无法取消部门与用户的关联!")
          return
        }

        var that = this;
        deleteAction(that.url.delete, {depId: this.currentDeptId, userId: id}).then((res) => {
          if (res.success) {
            that.$message.success("删除用户与选中部门关系成功！");
            if (this.selectedRowKeys.length>0){
               for(let i =0; i<this.selectedRowKeys.length;i++){
                   if (this.selectedRowKeys[i] == id){
                     this.selectedRowKeys.splice(i,1);
                     break;
                   }
               }
            }
            that.loadData();
          } else {
            that.$message.warning(res.message);
          }
        });
      },
      open(record) {
        //console.log(record);
        this.currentDeptId = record.id;
        this.currentDept = record;
        this.loadData(1);
      },
      clearList() {
        this.currentDeptId = '';
        this.dataSource = [];
      },
      hasSelectDept() {
        if (this.currentDeptId == '') {
          this.$message.error("请选择一个部门!")
          return false;
        }
        return true;
      },
      handleAddUserDepart() {
        if (this.currentDeptId == '' ) {
          this.$message.error("请选择一个部门!")
        } else {
          this.$refs.selectUserModal.visible = true;
        }
      },
      handleEdit: function (record) {
        this.$refs.modalForm.title = "编辑";
        this.$refs.modalForm.departDisabled = true;
        this.$refs.modalForm.disableSubmit = false;
        this.$refs.modalForm.edit(record);
      },
      handleAdd: function () {
        if (this.currentDeptId == '') {
          this.$message.error("请选择一个部门!")
        } else {
          this.$refs.modalForm.departDisabled = true;
          //初始化负责部门
          this.$refs.modalForm.userDepartModel.departIdList = [this.currentDeptId];  //传入一个部门id
          this.$refs.modalForm.add();
          //update-begin---author:liusq   Date:20210223  for：https://gitee.com/jeecg/jeecg-boot/issues/I2SDU1------------
          this.$refs.modalForm.resultDepartOptions=[{key:this.currentDept.key,title:this.currentDept.title}]
          //update-end---author:liusq   Date:20210223  for：https://gitee.com/jeecg/jeecg-boot/issues/I2SDU1------------
          this.$refs.modalForm.title = "新增";
        }
      },
      selectOK(data) {
        let params = {};
        params.depId = this.currentDeptId;
        params.userIdList = [];
        for (var a = 0; a < data.length; a++) {
          params.userIdList.push(data[a]);
        }
        console.log(params);
        postAction(this.url.edit, params).then((res) => {
          if (res.success) {
            this.$message.success(res.message);
            this.loadData();
          } else {
            this.$message.warning(res.message);
          }
        })
      },
      handleDeptRole(record){
        if(this.currentDeptId != ''){
          this.$refs.deptRoleUser.add(record,this.currentDeptId);
          this.$refs.deptRoleUser.title = "部门角色分配";
        }else{
          this.$message.warning("请先选择一个部门!");
        }
      }
    }
  }
</script>
<style scoped>
  /** Button按钮间距 */
  .ant-btn {
    margin-left: 3px
  }

  .ant-card {
    margin-left: -30px;
    margin-right: -30px;
  }

  .table-page-search-wrapper {
    margin-top: -16px;
    margin-bottom: 16px;
  }
</style>