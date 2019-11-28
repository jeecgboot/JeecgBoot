<template>

  <div>

    <a-card :bordered="false">

      <!-- 查询区域 -->
      <div class="table-page-search-wrapper">
        <a-form layout="inline" @keyup.enter.native="searchQuery">
          <a-row :gutter="22">

            <a-col :md="4" :sm="5">
              <a-form-item label="提单号">
                <a-input placeholder="请输入提单号查询" v-model="queryParam.bolcodeQuery"
                         @keyup.enter.native="searchQuery"></a-input>
              </a-form-item>
            </a-col>
            <!--          <a-col :md="4" :sm="8">-->
            <!--            <a-form-item label="接单日期">-->
            <!--              <a-range-picker >-->
            <!--                <a-icon slot="suffixIcon" type="clock-circle" theme="twoTone" v-model="queryParam.odate"/>-->
            <!--              </a-range-picker>-->
            <!--            </a-form-item>-->
            <!--          </a-col>-->
            <!--          <a-col :md="4" :sm="8">-->
            <!--            <a-form-item label="放行日期">-->
            <!--              <a-range-picker  :disabledDate="disabledDate" @change="onDateChange" :allowClear="true" >-->
            <!--                <a-icon slot="suffixIcon" type="clock-circle" theme="twoTone" v-model="queryParam.releasedate"/>-->
            <!--              </a-range-picker>-->
            <!--            </a-form-item>-->
            <!--          </a-col>-->
            <a-col :md="5" :sm="10">
              <a-form-item label="放行日期">
                <a-range-picker
                  v-model="queryParam.tmOdate"
                  :ranges="{ 昨天: [moment().subtract(1,'day'), moment().endOf('day')], '本月': [moment().startOf('month'), moment().startOf('day') ],  '上月': [moment().month(moment().month() - 1).startOf('month'), moment().month(moment().month() - 1).endOf('month') ] ,  '上上月': [moment().month(moment().month() - 2).startOf('month'), moment().month(moment().month() - 2).endOf('month') ]}"
                  @change="onDateChange"
                  :disabledDate="disabledDate"
                  :format="dateFormat"
                  :allowClear="false" @keyup.enter.native="searchQuery">
                  <a-icon slot="suffixIcon" type="clock-circle" theme="twoTone" v-model="queryParam.tmOdate"/>
                </a-range-picker>
              </a-form-item>
            </a-col>
            <a-col :md="3" :sm="5">
              <a-form-item label="数据状态">
                <a-select v-model="queryParam.datastatusQuery" placeholder="请选择" :allowClear="true"
                          @keyup.enter.native="searchQuery">
                  <a-select-option value="1">成功</a-select-option>
                  <a-select-option value="2">失败</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <!--<a-col :md="3" :sm="5">
              <a-form-item label="付款方">
                <a-input placeholder="请输入付款方查询" v-model="queryParam.payerQuery" @keyup.enter.native="searchQuery"></a-input>
              </a-form-item>
            </a-col>-->
            <!--          <template v-if="toggleSearchStatus">-->
            <!--            <a-col :md="6" :sm="8">-->
            <!--              <a-form-item label="字典下拉">-->
            <!--                <j-dict-select-tag v-model="queryParam.sex" placeholder="请选择用户名称" dictCode="sex"/>-->
            <!--              </a-form-item>-->
            <!--            </a-col>-->

            <!--            <a-col :md="6" :sm="8">-->
            <!--              <a-form-item label="字典表下拉">-->
            <!--                <j-dict-select-tag v-model="queryParam.realname" placeholder="请选择用户" dictCode="sys_user,realname,id"/>-->
            <!--              </a-form-item>-->
            <!--            </a-col>-->
            <!--          </template>-->
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
            <a-col :md="6" :sm="24">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
              <a-divider type="vertical"/>
              <a-button type="primary" icon="download" @click="handleExportXls('集装箱业务统计表')">导出</a-button>
            </a-col>
          </span>
            <!--<a-col :md="5" :sm="8">

              <template v-if="superQueryFlag">
                <a-tooltip title="已有高级查询条件生效!">
                  <button :disabled="false" class="ant-btn ant-btn-primary" @click="superQuery">
                    <a-icon type="appstore" theme="twoTone" spin="true"></a-icon>
                    <span>高级查询</span>
                  </button>
                </a-tooltip>
              </template>
              <a-button v-else type="primary" @click="superQuery" icon="filter">高级查询</a-button>

                          <a @click="handleToggleSearch" style="margin-left: 8px">
                            {{ toggleSearchStatus ? '收起' : '展开' }}
                            <a-icon :type="toggleSearchStatus ? 'up' : 'down'"/>
                          </a>
            </a-col>-->
          </a-row>
        </a-form>
      </div>

      <!-- 操作按钮区域 -->
      <!--    <div class="table-operator" >-->
      <!--      <a-button type="primary" icon="download" @click="handleExportXls('demo')">导出</a-button>-->
      <!--    </div>-->

      <!-- table区域-begin -->
      <div>
        <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
          当前页有
          <a-tag color="orange" style="font-weight: 700">{{this.connums}}</a-tag>
          个箱量数据
          <a-divider type="vertical"/>&nbsp;
          <a-tag color="purple" style="font-weight: 700">{{this.connumisOk}}</a-tag>
          完成箱量数
          <a-divider type="vertical"/>&nbsp;
          共
          <a-tag color="#2db7f5" style="font-weight: 700">{{ this.successBlnoNum + this.faildBlnoNum }}</a-tag>
          条提单数据
          <a-divider type="vertical"/>&nbsp;

          数据一致数：
          <a-tag color="#5fb5fd" style="font-weight: 700">{{this.successBlnoNum}}</a-tag>
          条提单
          <a-divider type="vertical"/>&nbsp;
          不一致数：
          <a-tag color="pink" style="font-weight: 700">{{this.faildBlnoNum}}</a-tag>
          条提单

          <!--        <a style="margin-left: 24px" @click="onClearSelected">清空</a>-->
          <span style="float:right;">
<!--          <a-switch @click="checkeChange()" checkedChildren="分页" unCheckedChildren="分页" defaultChecked />-->
            <!--          <a-divider type="vertical" />-->
          <a @click="loadData()"><a-icon type="sync"/>刷新</a>
          <a-divider type="vertical"/>
          <a-popover title="自定义列" trigger="click" placement="leftBottom">
            <template slot="content">
              <a-checkbox-group @change="onColSettingsChange" v-model="settingColumns" :defaultValue="settingColumns">
                <a-row>
                  <template v-for="(item,index) in defColumns">
                    <template v-if="item.key!='rowIndex'&& item.dataIndex!='action'">
                        <a-col :span="12"><a-checkbox :value="item.dataIndex">{{ item.title }}</a-checkbox></a-col>
                    </template>
                  </template>
                </a-row>
              </a-checkbox-group>
            </template>
            <a><a-icon type="setting"/>自定义列</a>
          </a-popover>
        </span>
        </div>

        <a-table
          ref="table"
          size="small"
          bordered
          :columns="columns"
          :dataSource="dataSource"
          :pagination=false
          :loading="loading"
          :footer="getFooter"
          @change="handleTableChange"
        >


        <span slot="datastatus" slot-scope="text">
          <template v-if=" text == '正常' ">
            <a-badge status="success"/>{{ text }}
          </template>
          <template v-if=" text == null ">
            <a-badge status="error"/>异常
          </template>
        </span>


          <span slot="filterIcon">
          完成量
          <a-tooltip placement="top" title="未完成柜量需要提醒调度及时在TMS系统操作">
            <a-icon type='exclamation-circle' spin></a-icon>
          </a-tooltip>
        </span>

          <template slot="containerNum" slot-scope="text,record">
            <a @click="jump(record)">{{ text }}</a>
          </template>


          <div slot="filterDropdown">
            <a-card>
              <a-checkbox-group @change="onColSettingsChange" v-model="settingColumns" :defaultValue="settingColumns">
                <a-row>
                  <template v-for="(item,index) in defColumns">
                    <template v-if="item.key!='rowIndex'&& item.dataIndex!='action'">
                      <a-col :span="12">
                        <a-checkbox :value="item.dataIndex">{{ item.title }}</a-checkbox>
                      </a-col>
                    </template>
                  </template>
                </a-row>
              </a-checkbox-group>
            </a-card>
          </div>


        </a-table>
      </div>


      <con-list ref="conList"></con-list>

      <!-- table区域-end -->

      <!-- 表单区域 -->
      <!--    <jeecgDemo-modal ref="modalForm" @ok="modalFormOk"></jeecgDemo-modal>-->


      <!-- 一对多表单区域 -->
      <!--    <JeecgDemoTabsModal ref="jeecgDemoTabsModal" @ok="modalFormOk"></JeecgDemoTabsModal>-->

      <!-- 高级查询区域 -->
      <j-super-query :fieldList="fieldList" ref="superQueryModal" @handleSuperQuery="handleSuperQuery"></j-super-query>
    </a-card>


    <div class="components-back-top-demo-custom">
      <a-back-top>
        <div class="ant-back-top-inner">UP</div>
      </a-back-top>

    </div>


  </div>
</template>

<script>
  // import JeecgDemoModal from './modules/JeecgDemoModal'
  import JSuperQuery from '@/components/jeecg/JSuperQuery.vue';
  // import JeecgDemoTabsModal from './modules/JeecgDemoTabsModal'
  import {initDictOptions, filterDictText} from '@/components/dict/JDictSelectUtil'
  import {JeecgListMixin} from '@/mixins/JeecgListMixin'
  import Vue from 'vue'
  import moment from 'moment';
  import {filterObj} from '@/utils/util';
  import conList from './modules/conList';

  //高级查询modal需要参数
  const superQueryFieldList = [{
    type: "date",
    value: "tmOdate",
    text: "放行日期"
  }, {
    type: "string",
    value: "bolcodeQuery",
    text: "提单号"
  }, {
    type: "string",
    value: "payerQuery",
    text: "付款方"
  }]
  export default {
    name: "dataCare",
    mixins: [JeecgListMixin],
    components: {
      // JeecgDemoModal,
      JSuperQuery,
      conList,
      // JeecgDemoTabsModal,
    },
    data() {
      return {
        description: '用户管理页面',
        //字典数组缓存
        sexDictOptions: [],
        /* 排序参数 */
        isorter: {
          column: '',
          order: '',
        },
        //合计箱量
        connums: '',
        //完成箱量
        connumisOk: '',
        //正确提单数
        successBlnoNum: '',
        //是否默认不做初始化加载
        initData: 1,
        //失败提单数
        faildBlnoNum: '',
        //时间初始化
        isDateInit: true,
        // 查询条件
        queryParam: {
          bolcodeQuery: '',
          tmOdate: [moment(moment().startOf('month')), moment(moment().startOf('day'))],
          payerQuery: '',
          datastatusQuery: '',
        },
        importExcelUrl: `${window._CONFIG['domianURL']}/test/jeecgDemo/importExcel`,
        //表头
        columns: [],
        /* 过滤字段集合 */
        // filtersArry:{},
        //列设置
        settingColumns: [],
        //列定义
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
            title: 'OMS订单号',
            align: "center",
            dataIndex: 'ordercode'
          },
          {
            title: '提单号',
            align: "center",
            dataIndex: 'bolcode'
          },
          {
            title: '放行时间',
            align: "center",
            // sorter: true,
            dataIndex: 'tmOdate'
          },
          {
            title: '接单时间',
            align: "center",
            dataIndex: 'odate'
          },
          {
            title: '调度中心',
            align: "center",
            dataIndex: 'sched'
          },
          {
            title: '柜量',
            align: "center",
            // sorter: true,
            dataIndex: 'connum'
          },
          {
            align: "center",
            // sorter: true,
            // slots:{title: 'filterIcon'},
            scopedSlots: {
              customRender: 'containerNum',
              title: 'filterIcon',
            },
            dataIndex: 'containertype',
          },
          {
            title: '数据状态',
            align: "center",
            // sorter: true,
            dataIndex: 'datastatus_dictText',
            scopedSlots: {customRender: 'datastatus'}
          },
          {
            title: '付款方',
            align: "center",
            dataIndex: 'payer'
          },
          {
            title: '业务员',
            align: "center",
            dataIndex: 'salesman'
          },
          {
            title: '业务类型',
            align: "center",
            // filters:[
            //   { text: '进口', value: '进口' },
            //   { text: '出口', value: '出口' },
            //  ],
            // onFilter: (value, record) => record.request.method.includes(value),
            dataIndex: 'salestype'
          },

          {
            title: '码头',
            align: "center",
            dataIndex: 'wharf'
          }
        ],
        url: {
          list: "/dataReport/dataCare/list",
          delete: "/test/jeecgDemo/delete",
          deleteBatch: "/test/jeecgDemo/deleteBatch",
          exportXlsUrl: "dataReport/dataCare/exportXls",
        },
        fieldList: superQueryFieldList
      }
    },
    methods: {
      moment,
      initDictConfig() {
        console.log("--我才是真的方法!--")
        //初始化字典 - 性别
        initDictOptions('sex').then((res) => {
          if (res.success) {
            this.sexDictOptions = res.result;
          }
        });
      },
      // onetomany: function () {
      //   this.$refs.jeecgDemoTabsModal.add();
      //   this.$refs.jeecgDemoTabsModal.title = "编辑";
      // },
      // //跳转单据页面
      // jump() {
      //   this.$router.push({path: '/jeecg/helloworld'})
      // },

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
      //设置默认放行日期，为月初和当前日期查询区间
      defaultValue() {
        // console.log(this.queryParam.tmOdate);
        // let startDate = moment().startOf('month');
        // let endDate = moment().startOf('day');
        // this.queryParam.tmOdate = [moment(startDate), moment(endDate)];
        this.queryParam.tmOdate_begin = this.queryParam.tmOdate[0].format('YYYY-MM-DD');
        this.queryParam.tmOdate_end = this.queryParam.tmOdate[1].format('YYYY-MM-DD');
        console.log(this.queryParam.tmOdate_begin + '&&' + this.queryParam.tmOdate_end)

      },
      disabledDate(current) {
        return current && current > moment().endOf('day');
      },
      lastMonth() {
        return {Today: [moment(), moment()], 'This Month': [moment(), moment().endOf('month')]};
      },
      onDateChange: function (value, dateString) {
        console.log(dateString[0], dateString[1]);
        this.queryParam.tmOdate = value;
        this.queryParam.tmOdate_begin = dateString[0];
        this.queryParam.tmOdate_end = dateString[1];
        console.log(this.queryParam.tmOdate_begin + '&&' + this.queryParam.tmOdate_end)
      },
      getQueryParams() {
        if (this.isDateInit) (
          this.defaultValue()
        )
        var param = Object.assign({}, this.queryParam, this.isorter);
        param.field = this.getQueryField();
        // param.pageNo = this.ipagination.current;
        // param.pageSize = this.ipagination.pageSize;
        delete param.tmOdate; // 时间参数不传递后台
        console.log(this.queryParam.tm_odate_begin + '||' + this.queryParam.tm_odate_end)
        this.isDateInit = false;

        // this.$refs.table.$delete(pagination)

        return filterObj(param);

      },
      getQueryParamsByProcess(){
        if (this.isDateInit) (
          this.defaultValue()
        )
        var param = Object.assign({}, this.queryParam, this.isorter);
        param.field = this.getQueryField();
        // param.pageNo = this.ipagination.current;
        // param.pageSize = this.ipagination.pageSize;
        delete param.tmOdate; // 时间参数不传递后台
        console.log(this.queryParam.tm_odate_begin + '||' + this.queryParam.tm_odate_end)
        this.isDateInit = false;

        // this.$refs.table.$delete(pagination)

        return filterObj(param);

      },

      searchReset() {
        var that = this;
        this.queryParam = {
          bolcodeQuery: '',
          releasedate: [],
          payerQuery: '',
        }; //清空查询区域参数
        this.defaultValue()
        this.loadData(this.ipagination.current);

      },
      //表尾的合计
      getFooter() {
        let connums = 0 //总箱量数
        let connumisOk = 0 //完成箱量总数
        let successBlno = 0 //正常数据量
        let faildBlno = 0 //正常数据量
        this.dataSource.forEach((item, index) => {
          connums += item.connum
          if(!isNaN(parseInt(item.containertype))){
            connumisOk +=parseInt(item.containertype)
          }
          if (item.datastatus == 1) {
            successBlno++
          }
        })
        this.connums = connums
        this.connumisOk = connumisOk
        this.successBlnoNum = successBlno
        faildBlno = this.dataSource.length - successBlno;
        this.faildBlnoNum = faildBlno
        return "当前页的箱量合计：" + connums + "  ,      数据一致数：" + successBlno + "   ,      失败数： " + faildBlno;
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
      // //分页开关
      // checkeChange(checked, event){
      //   debugger
      //   if (checked == true) {
      //     this.$refs.table.pagination = true;
      //   }else{
      //     this.$refs.table.pagination = false;
      //   }
      //
      // }
    },
    created() {
      this.initColumns();
      delete this.initData;//创建完毕后删除initData，以保证用户可以正常查询
    },
    mounted() {

    },
  }
</script>
<style scoped>
  .ant-card-body .table-operator {
    margin-bottom: 18px;
  }

  .ant-table-tbody .ant-table-row td {
    padding-top: 15px;
    padding-bottom: 15px;
  }

  .anty-row-operator button {
    margin: 0 5px
  }

  .ant-btn-danger {
    background-color: #ffffff
  }

  .ant-modal-cust-warp {
    height: 100%
  }

  .ant-modal-cust-warp .ant-modal-body {
    height: calc(100% - 110px) !important;
    overflow-y: auto
  }

  .ant-modal-cust-warp .ant-modal-content {
    height: 90% !important;
    overflow-y: hidden
  }

  /** Button按钮间距 */
  .ant-btn {
    margin-left: 3px
  }

  .components-back-top-demo-custom .ant-back-top {
    bottom: 100px;
  }

  .components-back-top-demo-custom .ant-back-top-inner {
    height: 40px;
    width: 40px;
    line-height: 40px;
    border-radius: 4px;
    background-color: #1088e9;
    color: #fff;
    text-align: center;
    font-size: 20px;
  }
</style>