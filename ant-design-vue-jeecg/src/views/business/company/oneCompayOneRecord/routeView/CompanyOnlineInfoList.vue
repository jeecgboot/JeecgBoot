<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="在线设备名称/型号">
              <a-input placeholder="请输入在线设备名称/型号" v-model="queryParam.equipmentName"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="数据状态">
              <j-dict-select-tag placeholder="请选择数据状态" v-model="queryParam.status" dictCode="statue"/>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <div class="table-operator" v-if="operationShow">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>
      </a-dropdown>
      <a-button @click="batchDeclare" type="primary" icon="snippets">申报</a-button>
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;" v-if="operationShow">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
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
        :rowSelection="rowSelection"
        class="j-table-force-nowrap"
        @change="handleTableChange">

        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>
        <template slot="imgSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无图片</span>
          <img v-else :src="getImgView(text)" height="25px" alt="" style="max-width:80px;font-size: 12px;font-style: italic;"/>
        </template>
        <template slot="fileSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无文件</span>
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

        <span slot="action" slot-scope="text, record">
          <!--权限控制查看还是编辑，查看只允许查看不允许修改-->
          <a  @click="handleEdit(record)" v-if="operationShow && (record.status!='1' && record.status!='4')">编辑</a>
           <a-divider v-if="operationShow && (record.status!='1' && record.status!='4')" type="vertical" />
           <a @click="handleview(record)" v-if="!operationShow || (record.status=='1' || record.status=='4')">查看</a>
           <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a v-if="operationShow  && (record.status!='1' && record.status!='4')">删除</a>
          </a-popconfirm>
        </span>

      </a-table>
    </div>

    <companyOnlineInfo-modal ref="modalForm" @ok="modalFormOk" :companyId="companyId"></companyOnlineInfo-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import CompanyOnlineInfoModal from './CompanyOnlineInfoModal'
  import {getAction} from "../../../../../api/manage";

  export default {
    name: "CompanyOnlineInfoList",
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      CompanyOnlineInfoModal
    },
    data () {
      return {
        description: '在线监控验收信息管理页面',
        queryParam: {
          companyId: this.companyId
        },
        // 表头
        columns: [
          {
            title: '序号',
            dataIndex: '',
            key:'rowIndex',
            width:60,
            align:"center",
            customRender:function (t,r,index) {
              return parseInt(index)+1;
            }
          },
          {
            title:'在线设备名称/型号',
            align:"center",
            dataIndex: 'equipmentName'
          },
          {
            title:'设备生产厂家',
            align:"center",
            dataIndex: 'equipmentManufacturers'
          },
          {
            title:'运维单位',
            align:"center",
            dataIndex: 'operationalUnit'
          },
          {
            title:'投入使用日期',
            align:"center",
            dataIndex: 'usedTime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'安装位置',
            align:"center",
            dataIndex: 'installLocation'
          },
          {
            title:'在线监控验收材料',
            align:"center",
            dataIndex: 'files',
            scopedSlots: {customRender: 'fileSlot'}
          },
          {
            title: '数据状态',
            align: "center",
            dataIndex: 'status_dictText'
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            // fixed:"right",
            width:147,
            scopedSlots: { customRender: 'action' }
          }
        ],
        url: {
          list: "/onlineInfo/companyOnlineInfo/list/" + this.listType,
          delete: "/onlineInfo/companyOnlineInfo/delete",
          deleteBatch: "/onlineInfo/companyOnlineInfo/deleteBatch",
          batchDeclare: "/onlineInfo/companyOnlineInfo/batchDeclare",
        },
        dictOptions:{},
      }
    },
    methods: {
      initDictConfig(){
      },
      handleview: function (record) {
        this.$refs.modalForm.edit(record);
        this.$refs.modalForm.title = "查看";
        this.$refs.modalForm.disableSubmit = true;
      },
      batchDeclare: function () {
        if (this.selectedRowKeys.length <= 0) {
          this.$message.warning('请选择一条记录！');
          return;
        } else {
          let ids = "";
          for (var a = 0; a < this.selectedRowKeys.length; a++) {
            ids += this.selectedRowKeys[a] + ",";
          }
          let that = this;
          this.$confirm({
            title: "确认申报",
            content: "是否申报选中数据?",
            onOk: function () {
              that.loading = true;
              getAction(that.url.batchDeclare, {ids: ids}).then((res) => {
                if (res.success) {
                  that.$message.success(res.message);
                  that.loadData();
                  that.onClearSelected();
                } else {
                  that.$message.warning(res.message);
                }
              }).finally(() => {
                that.loading = false;
              });
            }
          });
        }
      },
    },
    props: {
      companyId: "",
      operationShow: "",
      listType: ""
    },
    computed: {
      rowSelection() {
        return {
          getCheckboxProps: record => ({
            props: {
              disabled: record.status == '1',
              name: record.projectName,
            },
          }),
          selectedRowKeys: this.selectedRowKeys,
          onChange: this.onSelectChange
        };
      },

    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>