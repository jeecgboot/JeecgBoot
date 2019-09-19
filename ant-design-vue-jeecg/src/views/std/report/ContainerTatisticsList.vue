<template>
  <a-card :bordered="false">

    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <a-row :gutter="24">

          <a-col :md="6" :sm="8">
            <a-form-item label="提单号">
              <a-input placeholder="请输入提单号" @keyup.enter.native="searchQuery" v-model="queryParam.bolcodeQuery"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="箱号（柜号）">
              <a-input placeholder="请输入箱号（柜号）" @keyup.enter.native="searchQuery" v-model="queryParam.containerNoQuery"></a-input>
            </a-form-item>
          </a-col>
          <!--          <a-col :md="6" :sm="8">-->
          <!--            <a-form-item label="海关放行日期">-->
          <!--              <a-input placeholder="请输入海关放行日期" v-model="queryParam.customsReleaseDate"></a-input>-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->

          <a-col :md="5" :sm="10">
            <a-form-item label="放行日期">
              <a-range-picker
                v-model="queryParam.customsReleaseDate"
                :ranges="{ 昨天: [moment().subtract(1,'day'), moment().endOf('day')], '上月': [moment().month(moment().month() - 1).startOf('month'), moment().month(moment().month() - 1).endOf('month') ] }"
                @change="onDateChange"
                :disabledDate="disabledDate"
                :allowClear="false" @keyup.enter.native="searchQuery">
                <a-icon slot="suffixIcon" type="clock-circle" theme="twoTone" v-model="queryParam.customsReleaseDate"/>
              </a-range-picker>
            </a-form-item>
          </a-col>

          <a-col :md="6" :sm="8">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
<!--              <a @click="handleToggleSearch" style="margin-left: 8px">-->
<!--                {{ toggleSearchStatus ? '收起' : '展开' }}-->
<!--                <a-icon :type="toggleSearchStatus ? 'up' : 'down'"/>-->
<!--              </a>-->
            </span>
          </a-col>

        </a-row>
      </a-form>
    </div>

    <!-- 操作按钮区域 -->
    <div class="table-operator">
<!--      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>-->
      <a-button type="primary" icon="download" @click="handleExportXls('集装箱业务统计表')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl"
                @change="handleImportExcel">
<!--        <a-button type="primary" icon="import">导入</a-button>-->
      </a-upload>
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel">
            <a-icon type="delete"/>
            删除
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
        共有
        <a-tag color="#2db7f5" style="font-weight: 700">{{ this.ipagination.total }}</a-tag>
        条集装箱数据 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;


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
        size="middle"
        bordered
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :footer="getFooter"
        :customRow="getCustomRow"
        :rowClassName="getRowClassName"
        @change="handleTableChange">


        <template slot="businessType" slot-scope="text">
          <a-tag v-if="text === '进口'" color="green">{{ text }}</a-tag>
          <a-tag v-else-if="text === '出口'" color="cyan">{{ text }}</a-tag>
        </template>

        <template slot="blNo" slot-scope="text">
          <a-tag color="#2db7f5" style="font-weight: 700">{{ text }}</a-tag>
        </template>





      </a-table>
    </div>
    <!-- table区域-end -->

    <!-- 表单区域 -->
    <containerTatistics-modal ref="modalForm" @ok="modalFormOk"></containerTatistics-modal>
  </a-card>
</template>

<script>
  import ContainerTatisticsModal from './modules/ContainerTatisticsModal'
  import {JeecgListMixin} from '@/mixins/JeecgListMixin'
  import Vue from 'vue'
  import moment from 'moment';
  import {filterObj} from '@/utils/util';
  import {getAction} from '@/api/manage'

  export default {
    name: "ContainerTatisticsList",
    mixins: [JeecgListMixin],
    components: {
      ContainerTatisticsModal
    },
    data() {
      return {
        description: '集装箱业务统计表管理页面',
        //表头
        columns: [],
        /* 排序参数 */
        isorter: {
          column: 'customsReleaseDate',
          order: 'desc',
        },
        //初始化合并行
        spanArr: [],
        //鼠标移入变色行
        hoverOrderArr: [],
        //时间初始化
        isDateInit: true,
        //列设置
        settingColumns: [],
        //查询条件
        queryParam: {
          bolcodeQuery: '',
          customsReleaseDate: [],
          containerNoQuery: '',
        },
        // 表头x
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
            title: '提单号',
            align: "center",
            dataIndex: 'blNo',
            customRender: (value, row, index) => {
              const obj = {
                children: value,
                attrs: {},
              };
              debugger
              obj.attrs.rowSpan = this.spanArr[index]
              obj.attrs.colspan = obj.attrs.rowSpan > 0 ? 1 : 0;
              row.containerNums = obj.attrs.rowSpan;
              // obj.children = value +  " | 箱数:" + obj.attrs.rowSpan;
              obj.children = value +  " | 箱数:" + obj.attrs.rowSpan;
              return obj;


            },
            scopedSlots: {customRender: 'blNo'},
          },
          {
            title: '海关放行日期',
            align: "center",
            sorter: true,
            dataIndex: 'customsReleaseDate'
          },
          {
            title: '箱号（柜号）',
            align: "center",
            dataIndex: 'containerNo'
          },
          {
            title: '进出口',
            align: "center",
            dataIndex: 'businessType_dictText',
            filters: [
              {text: '进口', value: '1'},
              {text: '出口', value: '2'},
            ],
            scopedSlots: {customRender: 'businessType'},
            onFilter: (value, record) => record.businessType + '' === value,
          },
          {
            title: '尺寸',
            align: "center",
            dataIndex: 'containerSize'
          },
          {
            title: '规格',
            align: "center",
            dataIndex: 'containerSpec'
          },
          {
            title: '船东',
            align: "center",
            dataIndex: 'shipowner'
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
            title: '详细地址',
            align: "center",
            dataIndex: 'detailAddress'
          },
          {
            title: '接单备注',
            align: "center",
            dataIndex: 'orderRemark'
          }
        ],
        url: {
          list: "/dataReport/containerTatistics/list",
          delete: "/dataReport/containerTatistics/delete",
          deleteBatch: "/dataReport/containerTatistics/deleteBatch",
          exportXlsUrl: "dataReport/containerTatistics/exportXls",
          importExcelUrl: "dataReport/containerTatistics/importExcel",
        },
      }
    },
    computed: {
      importExcelUrl: function () {
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      }
    },
    methods: {
      moment,
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
        let startDate = moment().startOf('month');
        let endDate = moment().startOf('day');
        this.queryParam.customsReleaseDate = [moment(startDate), moment(endDate)];
        this.queryParam.customsReleaseDate_begin = this.queryParam.customsReleaseDate[0].format('YYYY-MM-DD');
        this.queryParam.customsReleaseDate_end = this.queryParam.customsReleaseDate[1].format('YYYY-MM-DD');

      },
      getQueryParams() {
        if (this.isDateInit) (
          this.defaultValue()
        )
        this.spanArr = [];
        var param = Object.assign({}, this.queryParam, this.isorter);
        param.field = this.getQueryField();
        param.pageNo = this.ipagination.current;
        param.pageSize = this.ipagination.pageSize;
        delete param.customsReleaseDate; // 时间参数不传递后台
        this.isDateInit = false;
        return filterObj(param);
      },
      disabledDate(current) {
        return current && current > moment().endOf('day');
      },
      onDateChange: function (value, dateString) {
        this.queryParam.customsReleaseDate = value;
        this.queryParam.customsReleaseDate_begin = dateString[0];
        this.queryParam.customsReleaseDate_end = dateString[1];
      },
      searchReset() {
        var that = this;
        this.queryParam = {
          bolcodeQuery: '',
          customsReleaseDate: [],
          containerNoQuery: ''
        }; //清空查询区域参数
        this.defaultValue()
        this.loadData(this.ipagination.current);
      },
      //表尾的合计
      getFooter() {
        debugger
        let blnoNum = 0 ;//提单数据量
        let data = this.dataSource;
        for (var i = 0; i < data.length; i++) {
          if (i === 0) {
            blnoNum = 1
          }else if (data[i].blNo !=  data[i - 1].blNo) {

            blnoNum++
          }
        }
        return "当前页的箱量合计：" + this.ipagination.total + "  ,      当前页的提单数为：" + blnoNum + "条";
      },
      getCustomRow(record, index) {
        return {
          props: {},
          on: { // 事件
            mouseenter: () => {
              let className = 'background-color : #66ed8d';
              if (index % 2 === 1) className = 'dark-row';
              //console.log("大萨达" + index)


              return 'clickRowStyl'
            },  // 鼠标移入行
          },

        };
      },

      getRowClassName(record, index) {
        let arr = this.spanArr
        for (let i = 0; i < arr.length; i++) {
          if (index == arr[i]) {
            return ''
          }
        }
      },
    },
    created() {
      this.initColumns();
    },
    watch: {
      dataSource() {
        let data = this.dataSource;
        for (var i = 0; i < data.length; i++) {
          if (i === 0) {
            this.spanArr.push(1);
            this.pos = 0
          } else {
            // 判断当前元素与上一个元素是否相同
            if (data[i].blNo === data[i - 1].blNo) {
              this.spanArr[this.pos] += 1;
              this.spanArr.push(0);
            } else {
              this.spanArr.push(1);
              this.pos = i;
            }
          }
        }
      },

    },
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