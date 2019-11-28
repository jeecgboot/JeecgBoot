<template>
  <a-card :bordered="false">

    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">

          <!--          <a-col :md="6" :sm="8">-->
          <!--            <a-form-item label="放行日期">-->
          <!--              <a-input placeholder="请输入放行日期" v-model="queryParam.customsReleaseDate"></a-input>-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->
          <!--<a-col :md="6" :sm="8">
            <a-form-item label="测试日期">
              <j-date
                v-model="queryParam.blNoDate"
                placeholder="请选择结束时间" >
              </j-date>
            </a-form-item>
          </a-col>-->
          <a-col :md="5" :sm="10">
            <a-form-item label="放行日期">
              <j-range-date v-model="queryParam.customsReleaseDate">

              </j-range-date>
            </a-form-item>
          </a-col>
          <a-col :md="4" :sm="8">
            <a-form-item label="提单号">
              <a-input placeholder="请输入提单号" v-model="queryParam.blNo"></a-input>
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

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <!--      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>-->
      <a-button type="primary" icon="download" @click="handleExportXls('业务接单提单数据')">导出</a-button>
      <!--      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">-->
      <!--        <a-button type="primary" icon="import">导入</a-button>-->
      <!--      </a-upload>-->
      <!--      <a-dropdown v-if="selectedRowKeys.length > 0">-->
      <!--        <a-menu slot="overlay">-->
      <!--          <a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>-->
      <!--        </a-menu>-->
      <!--        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>-->
      <!--      </a-dropdown>-->
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        共有
        <a-tag color="#2db7f5" style="font-weight: 700">{{ this.ipagination.total }}</a-tag>
        条集装箱提单数据 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;


        <!--        <a style="margin-left: 24px" @click="onClearSelected">清空</a>-->
        <span style="float:right;">
          <a @click="loadData()"><a-icon type="sync"/>刷新</a>
          <a-divider type="vertical"/>
          <a-popover title="自定义列" trigger="click" placement="leftBottom">
            <template slot="content">
              <a-checkbox-group @change="onColSettingsChange" v-model="settingColumns" :defaultValue="settingColumns">
                <a-row>
                  <template v-for="(item,index) in defColumns">
                    <template v-if="item.key!='rowIndex'&& item.dataIndex!='action'">
                        <a-col :span="8"><a-checkbox :value="item.dataIndex">{{ item.title }}</a-checkbox></a-col>
                    </template>
                  </template>
                </a-row>
              </a-checkbox-group>
            </template>
            <a><a-icon type="setting" />自定义列</a>
          </a-popover>
        </span>
      </div>



        <a-table
          ref="table"
          size="middle"
          bordered
          :columns="columns"
          :dataSource="dataSource"
          :pagination="ipagination"
          :loading="loading"
          :scroll="{ x: 1800 }"
          @change="handleTableChange">

          <!--        <span slot="action" slot-scope="text, record">-->
          <!--          <a @click="handleEdit(record)">编辑</a>-->

          <!--          <a-divider type="vertical" />-->
          <!--          <a-dropdown>-->
          <!--            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>-->
          <!--            <a-menu slot="overlay">-->
          <!--              <a-menu-item>-->
          <!--                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">-->
          <!--                  <a>删除</a>-->
          <!--                </a-popconfirm>-->
          <!--              </a-menu-item>-->
          <!--            </a-menu>-->
          <!--          </a-dropdown>-->
          <!--        </span>-->
          <template slot="businessType" slot-scope="text">
            <a-tag v-if="text === '进口'" color="green">{{ text }}</a-tag>
            <a-tag v-else-if="text === '出口'" color="cyan">{{ text }}</a-tag>
          </template>

          <template slot="takeBack" slot-scope="text">
            <a-tag v-if="text === '是'" color="#A52A2A">{{ text }}</a-tag>
            <a-tag v-else-if="text === '否'" color="#87d068">{{ text }}</a-tag>
            <span v-else>{{ text }} ..</span>
          </template>

          <template slot="containerNum" slot-scope="text,record">
            <a @click="jump(record)">{{ text }}</a>
          </template>

          <!-- 字符串超长截取省略号显示 -->
          <span slot="toLong" slot-scope="text">
            <j-ellipsis :value="text" :length="5"/>
          </span>

          <!-- 字符串超长截取省略号显示 -->
          <span slot="toLong10" slot-scope="text">
            <j-ellipsis :value="text" :length="10"/>
          </span>

        </a-table>

    </div>
    <!-- table区域-end -->

    <!-- 表单区域 -->
    <billOrderData-modal ref="modalForm" @ok="modalFormOk"></billOrderData-modal>

    <con-list ref="conList"></con-list>

  </a-card>
</template>

<script>
  import BillOrderDataModal from './modules/BillOrderDataModal'
  import {JeecgListMixin} from '@/mixins/JeecgListMixin'
  import JDate from '@/components/jeecg/JDate'
  import JRangeDate from '@/components/jeecg/JRangeDate'
  import Vue from 'vue'
  import JEllipsis from '@/components/jeecg/JEllipsis'
  import {filterObj} from '@/utils/util';
  import conList from './modules/conList'

  export default {
    name: "BillOrderDataList",
    mixins: [JeecgListMixin],
    components: {
      BillOrderDataModal,
      JDate,
      JEllipsis,
      conList,
      JRangeDate
    },
    data() {
      return {
        description: '业务接单提单数据管理页面',
        //列设置
        settingColumns: [],
        /* 排序参数 */
        isorter: {
          column: 'customsReleaseDate',
          order: 'desc',
        },
        //表头
        columns: [],
        //是否默认不做初始化加载
        initData: 1,
        // 表头
        defColumns: [
          {
            title: '#',
            dataIndex: '',
            key: 'rowIndex',
            width: 10,
            align: "center",
            customRender: function (t, r, index) {
              return parseInt(index) + 1;
            }
          },
          {
            title: '放行日期',
            align: "center",
            sorter: true,
            width: 100,
            dataIndex: 'customsReleaseDate'
          },
          {
            title: '实际到港日期',
            align: "center",
            dataIndex: 'actualShipDate'
          },
          {
            title: '预计到港日期',
            align: "center",
            dataIndex: 'shippingDate'
          },
          {
            title: '提单号',
            align: "center",
            dataIndex: 'blNo',


          },
          {
            title: '货主(付款方)',
            align: "center",
            scopedSlots: {customRender: 'toLong'},
            dataIndex: 'consignor'
          },
          {
            title: '提箱地点',
            align: "center",
            dataIndex: 'takeContainerAddress'
          },
          {
            title: '拖箱地点',
            align: "center",
            dataIndex: 'dragContainerAddress'
          },
          {
            title: '返箱地点',
            align: "center",
            dataIndex: 'returnContainerAddress'
          },
          {
            title: '船东',
            align: "center",
            scopedSlots: {customRender: 'toLong'},
            dataIndex: 'shipowner'
          },
          {
            title: '船代',
            align: "center",
            dataIndex: 'shippingCompanyCode'
          },
          {
            title: '大品名',
            align: "center",
            dataIndex: 'bigGoodsType'
          },
          {
            title: '箱量',
            align: "center",
            scopedSlots: {customRender: 'containerNum'},
            dataIndex: 'containerNum'
          },
          {
            title: '箱型箱量',
            align: "center",
            dataIndex: 'containerDesc'
          },
          {
            title: '进出口',
            align: "center",
            dataIndex: 'businessType_dictText',
            filters: [
              {text: '进口', value: '1'},
              {text: '出口', value: '2'},
            ],
            filterMultiple: false,
            scopedSlots: {customRender: 'businessType'},
            // onFilter: (value, record) => record.businessType.includes(value)
          },
          {
            title: '退载自拖',
            align: "center",
            dataIndex: 'takeBack_dictText',
            width: 70,
            filters: [
              {text: '退载', value: '1'}
            ],
            filterMultiple: false,
            scopedSlots: {customRender: 'takeBack'},
          },
          {
            title: '业务员',
            align: "center",
            dataIndex: 'servicePersonnel'
          },
          {
            title: '国家',
            align: "center",
            dataIndex: 'country'
          },
          {
            title: '备注',
            align: "center",
            dataIndex: 'remark',
            scopedSlots: {customRender: 'toLong10'},
          }
        ],
        url: {
          list: "/org.jeecg.modules.dataReport/billOrderData/list",
          delete: "/org.jeecg.modules.dataReport/billOrderData/delete",
          deleteBatch: "/org.jeecg.modules.dataReport/billOrderData/deleteBatch",
          exportXlsUrl: "org.jeecg.modules.dataReport/billOrderData/exportXls",
          importExcelUrl: "org.jeecg.modules.dataReport/billOrderData/importExcel",
        },
      }
    },
    computed: {
      importExcelUrl: function () {
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      }
    },
    created() {
      this.initColumns();
      delete this.initData;//创建完毕后删除initData，以保证用户可以正常查询
      // this.loadData();
      //初始化字典配置 在自己页面定义
      // this.initDictConfig();
    },
    methods: {
      //列设置更改事件
      onColSettingsChange(checkedValues) {
        var key = this.$route.name + ":colsettings";
        Vue.ls.set(key, checkedValues, 7 * 24 * 60 * 60 * 1000)
        this.settingColumns = checkedValues;
        const cols = this.defColumns.filter(item => {
          if (item.key == 'rowIndex' || item.dataIndex == 'action') {
            return true
          }
          if (this.settingColumns.includes(item.dataIndex)) {
            return true
          }
          return false
        })
        this.columns = cols;
      },
      initColumns() {
        //权限过滤（列权限控制时打开，修改第二个参数为授权码前缀）
        //this.defColumns = colAuthFilter(this.defColumns,'testdemo:');

        var key = this.$route.name + ":colsettings";
        let colSettings = Vue.ls.get(key);
        if (colSettings == null || colSettings == undefined) {
          let allSettingColumns = [];
          this.defColumns.forEach(function (item, i, array) {
            allSettingColumns.push(item.dataIndex);
          })
          this.settingColumns = allSettingColumns;
          this.columns = this.defColumns;
        } else {
          this.settingColumns = colSettings;
          const cols = this.defColumns.filter(item => {
            if (item.key == 'rowIndex' || item.dataIndex == 'action') {
              return true;
            }
            if (colSettings.includes(item.dataIndex)) {
              return true;
            }
            return false;
          })
          this.columns = cols;
        }
      },
      //跳转单据页面
      jump(record) {
        debugger
        // alert(record.blNo)
        // this.$router.push({path: '/dataReport/containerStatistics/' , query: {bolcodeQuery: record.blNo}})
        // this.$router.push({name: 'containerStatistics', params: {bolcodeQuery: record.blNo}})
        // this.$router.push({ name: "containerStatistics" , params: {bolcodeQuery: record.blNo}})
        this.$refs.conList.searchChild(record);
        // this.$router.push({name:'test',params: {id:'1'}})
      },


    }
  }
</script>
<style scoped>

  .ant-card-body .table-operator {
    margin-bottom: 10px;
  }


  /** Button按钮间距 */
  .ant-btn {
    margin-left: 5px
  }


  .clickRowStyl {
    background-color: #66ed8d
  }

  .ant-table-tbody > .clickRowStyl:hover > td {
    background-color: #ed9e9b
  }

</style>