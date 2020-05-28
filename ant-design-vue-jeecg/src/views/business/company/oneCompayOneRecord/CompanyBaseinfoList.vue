<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="企业简称">
              <a-input placeholder="请输入企业简称" v-model="queryParam.shortName"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="企业类型">
              <a-input placeholder="请输入企业类型" v-model="queryParam.companyType"></a-input>
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="所属行政区">
                <a-input placeholder="请输入所属行政区" v-model="queryParam.administrativeRegion"></a-input>
              </a-form-item>
            </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="所属行业">
                <a-input placeholder="请输入所属行业" v-model="queryParam.industry"></a-input>
              </a-form-item>
            </a-col>
          </template>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
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
    <!-- 查询区域-END -->
    


    <!-- table区域-begin -->
    <div>


      <a-table
        ref="table"
        size="middle"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"

        class="j-table-force-nowrap"
        @change="handleTableChange">

        <a slot="shortName" @click="handleDetail(record)" slot-scope="text, record">{{ text }}</a>


      </a-table>
    </div>

    <companyBaseinfo-modal ref="modalForm" @ok="modalFormOk"></companyBaseinfo-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import store from '@/store/'

  export default {
    name: "CompanyBaseinfoList",
    mixins:[ JeecgListMixin,mixinDevice],
    components: {
    },
    props:{
      list_visible:Boolean
    },
    data () {
      return {
        description: '企业基础信息表管理页面',
        visible :this.list_visible,
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
            title:'所属部门',
            align:"center",
            dataIndex: 'sysOrgCode'
          },
          {
            title:'企业简称',
            align:"center",
            dataIndex: 'shortName',
            key:'shortName',
            scopedSlots: { customRender: 'shortName' }
          },
          {
            title:'统一社会信用代码',
            align:"center",
            dataIndex: 'socialCreditCode'
          },
          {
            title:'企业类型',
            align:"center",
            dataIndex: 'companyType'
          },
          {
            title:'所属行政区',
            align:"center",
            dataIndex: 'administrativeRegion'
          },
          {
            title:'所属行业',
            align:"center",
            dataIndex: 'industry'
          },
          {
            title:'企业地址',
            align:"center",
            dataIndex: 'address'
          }
        ],
        url: {
          list: "/company/companyBaseinfo/listByUserId/"+store.getters.userInfo.id,
        },
        dictOptions:{},
      }
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      },
    },
    methods: {
      initDictConfig(){
      },
      handleDetail(record){
        this.$emit('toDetail',record.companyId);
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>