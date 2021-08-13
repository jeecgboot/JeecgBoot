<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="6" :sm="12">
            <a-form-item label="账号">
              <a-input placeholder="请输入账号查询" v-model="queryParam.username"></a-input>
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
        :scroll="{x:true}"
        bordered
        rowKey="token"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        class="j-table-force-nowrap"
        @change="handleTableChange">

        <template slot="avatarslot" slot-scope="text, record, index">
          <div class="anty-img-wrap">
            <a-avatar shape="square" :src="getAvatarView(record.avatar)" icon="user"/>
          </div>
        </template>

        <span slot="action" slot-scope="text, record">
          <a-popconfirm title="强制退出用户？" @confirm="() => handleForce(record)">
            <a-button type="danger">强退</a-button>
          </a-popconfirm>
        </span>

      </a-table>
    </div>

  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import { forceLogout } from '@/api/login'
  import {filterDictTextByCache} from '@/components/dict/JDictSelectUtil'

  import {getFileAccessHttpUrl} from '@/api/manage';

  export default {
    name: "SysUserOnlineList",
    mixins:[JeecgListMixin, mixinDevice],
    components: {},
    data () {
      return {
        description: '在线用户管理页面',
        queryParam: {
          username: ''
        },
        // 表头
        columns: [
          {
            title:'用户账号',
            align:"center",
            dataIndex: 'username'
          },{
            title:'用户姓名',
            align:"center",
            dataIndex: 'realname'
          },{
            title: '头像',
            align: "center",
            width: 120,
            dataIndex: 'avatar',
            scopedSlots: {customRender: "avatarslot"}
          },{
            title:'生日',
            align:"center",
            dataIndex: 'birthday'
          },{
            title: '性别',
            align: "center",
            dataIndex: 'sex',
            customRender: (text) => {
              //字典值翻译通用方法
              return filterDictTextByCache('sex', text);
            }
          },{
            title:'手机号',
            align:"center",
            dataIndex: 'phone'
          },{
            title: '操作',
            dataIndex: 'action',
            scopedSlots: {customRender: 'action'},
            align: "center",
            width: 170
          }
        ],
        url: {
          list: "/sys/online/list"
        },
        dictOptions:{},
      }
    },
    created() {
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      },
    },
    methods: {
      getAvatarView: function (avatar) {
        return getFileAccessHttpUrl(avatar)
      },
      handleForce(record) {
        let that = this;
        let forceParam = {
          token: record.token
        }
        return forceLogout(forceParam).then((res) => {
          if (res.success) {
            that.loadData();
            this.$message.success('强制退出用户”'+record.realname+'“成功！');
          } else {
            that.$message.warning(res.message);
          }
        })
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>