<template>
  <a-modal
    :width="modalWidth"
    :visible="visible"
    :title="title"
    @ok="handleSubmit"
    @cancel="close"
    cancelText="关闭"
    style="margin-top: -70px"
    wrapClassName="ant-modal-cust-warp"
  >
    <a-row :gutter="10" style="background-color: #ececec; padding: 10px; margin: -10px">
      <a-col :md="6" :sm="24">
        <a-card :bordered="false">
          <!--组织机构-->
          <a-directory-tree
            selectable
            :selectedKeys="selectedDepIds"
            :checkStrictly="true"
            @select="onDepSelect"
            :dropdownStyle="{maxHeight:'200px',overflow:'auto'}"
            :treeData="departTree"
          />
        </a-card>
      </a-col>
      <a-col :md="18" :sm="24">
        <a-card :bordered="false">
          用户账号:
          <a-input-search
            :style="{width:'150px',marginBottom:'15px'}"
            placeholder="请输入用户账号"
            v-model="queryParam.username"
            @search="onSearch"
          ></a-input-search>
          <a-button @click="searchReset(1)" style="margin-left: 20px" icon="redo">重置</a-button>
          <!--用户列表-->
          <a-table
            ref="table"
            :scroll="scrollTrigger"
            size="middle"
            rowKey="id"
            :columns="columns"
            :dataSource="dataSource"
            :pagination="ipagination"
            :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange,type: getType}"
            @change="handleTableChange">
          </a-table>
        </a-card>
      </a-col>
    </a-row>
  </a-modal>
</template>

<script>
  import {filterObj} from '@/utils/util'
  import {queryDepartTreeList, getUserList, queryUserByDepId} from '@/api/api'

  export default {
    name: 'JSelectUserByDepModal',
    components: {},
    props: ['modalWidth', 'multi', 'userIds'],
    data() {
      return {
        queryParam: {
          username: "",
        },
        columns: [
          {
            title: '用户账号',
            align: 'center',
            dataIndex: 'username'
          },
          {
            title: '真实姓名',
            align: 'center',
            dataIndex: 'realname'
          },
          {
            title: '角色名称',
            align: 'center',
            dataIndex: 'roleName'
          },
          {
            title: '性别',
            align: 'center',
            dataIndex: 'sex',
            customRender: function (text) {
              if (text === 1) {
                return '男'
              } else if (text === 2) {
                return '女'
              } else {
                return text
              }
            }
          },
          {
            title: '手机号码',
            align: 'center',
            dataIndex: 'phone'
          },
          {
            title: '邮箱',
            align: 'center',
            dataIndex: 'email'
          }
        ],
        scrollTrigger: {},
        dataSource: [],
        selectedRowKeys: [],
        selectUserRows: [],
        selectUserIds: [],
        title: '根据部门选择用户',
        ipagination: {
          current: 1,
          pageSize: 10,
          pageSizeOptions: ['10', '20', '30'],
          showTotal: (total, range) => {
            return range[0] + '-' + range[1] + ' 共' + total + '条'
          },
          showQuickJumper: true,
          showSizeChanger: true,
          total: 0
        },
        isorter: {
          column: 'createTime',
          order: 'desc'
        },
        selectedDepIds: [],
        departTree: [],
        visible: false,
        form: this.$form.createForm(this)
      }
    },
    computed: {
      // 计算属性的 getter
      getType: function () {
        console.log("multi: ", this.multi);
        return this.multi == true ? 'checkbox' : 'radio';
      }
    },
    watch: {
      userIds() {
        this.initUserNames()
      }
    },
    created() {
      // 该方法触发屏幕自适应
      this.resetScreenSize();
      this.loadData().then((res) => {
        this.initUserNames();
      })
    },
    methods: {
      initUserNames() {
        let names = ''
        console.log("props userIds: ", this.userIds)
        if (this.userIds) {
          let currUserIds = this.userIds
          let userIdsArr = currUserIds.split(',');
          for (let item of this.dataSource) {
            if (userIdsArr.includes(item.username)) {
              names += "," + item.realname
            }
          }
          if (names) {
            names = names.substring(1)
          }
          this.$emit("initComp", names)
        }
      },
      async loadData(arg) {
        if (arg === 1) {
          this.ipagination.current = 1;
        }
        let params = this.getQueryParams();//查询条件
        await getUserList(params).then((res) => {
          if (res.success) {
            this.dataSource = res.result.records;
            this.ipagination.total = res.result.total;
          }
        })
      },
      // 触发屏幕自适应
      resetScreenSize() {
        let screenWidth = document.body.clientWidth;
        if (screenWidth < 500) {
          this.scrollTrigger = {x: 800};
        } else {
          this.scrollTrigger = {};
        }
      },
      showModal() {
        this.visible = true;
        this.queryDepartTree();
        this.loadData();
        this.form.resetFields();
      },
      getQueryParams() {
        let param = Object.assign({}, this.queryParam, this.isorter);
        param.field = this.getQueryField();
        param.pageNo = this.ipagination.current;
        param.pageSize = this.ipagination.pageSize;
        return filterObj(param);
      },
      getQueryField() {
        let str = 'id,';
        for (let a = 0; a < this.columns.length; a++) {
          str += ',' + this.columns[a].dataIndex;
        }
        return str;
      },
      searchReset(num) {
        let that = this;
        if (num !== 0) {
          that.queryParam = {};
          that.loadData(1);
        }
        that.selectedRowKeys = [];
        that.selectUserIds = [];
        that.selectedDepIds = [];
      },
      close() {
        this.searchReset(0);
        this.visible = false;
      },
      handleTableChange(pagination, filters, sorter) {
        //TODO 筛选
        if (Object.keys(sorter).length > 0) {
          this.isorter.column = sorter.field;
          this.isorter.order = 'ascend' === sorter.order ? 'asc' : 'desc';
        }
        this.ipagination = pagination;
        this.loadData();
      },
      handleSubmit() {
        let that = this;
        this.getSelectUserRows();
        console.log(that.selectUserRows)
        that.$emit('ok', that.selectUserRows, that.selectUserIds);
        that.searchReset(0)
        that.close();
      },
      //获取选择用户信息
      getSelectUserRows(rowId) {
        let dataSource = this.dataSource;
        let userIds = "";
        this.selectUserRows = [];
        for (let i = 0, len = dataSource.length; i < len; i++) {
          if (this.selectedRowKeys.includes(dataSource[i].id)) {
            this.selectUserRows.push(dataSource[i]);
            userIds = userIds + "," + dataSource[i].username
          }
        }
        this.selectUserIds = userIds.substring(1);
      },
      // 点击树节点,筛选出对应的用户
      onDepSelect(selectedDepIds) {
        if (selectedDepIds[0] != null) {
          this.initQueryUserByDepId(selectedDepIds); // 调用方法根据选选择的id查询用户信息
          if (this.selectedDepIds[0] !== selectedDepIds[0]) {
            this.selectedDepIds = [selectedDepIds[0]];
          }
        }
      },
      onSelectChange(selectedRowKeys, selectionRows) {
        this.selectedRowKeys = selectedRowKeys;
        this.selectionRows = selectionRows;
      },
      onSearch() {
        this.loadData(1);
      },
      // 根据选择的id来查询用户信息
      initQueryUserByDepId(selectedDepIds) {
        queryUserByDepId({id: selectedDepIds.toString()}).then((res) => {
          if (res.success) {
            this.dataSource = res.result;
            this.ipagination.total = res.result.length;
          }
        })
      },
      queryDepartTree() {
        queryDepartTreeList().then((res) => {
          if (res.success) {
            this.departTree = res.result;
          }
        })
      },
      modalFormOk() {
        this.loadData();
      }
    }
  }
</script>

<style scoped>
  .ant-table-tbody .ant-table-row td {
    padding-top: 10px;
    padding-bottom: 10px;
  }

  #components-layout-demo-custom-trigger .trigger {
    font-size: 18px;
    line-height: 64px;
    padding: 0 24px;
    cursor: pointer;
    transition: color .3s;
  }
</style>