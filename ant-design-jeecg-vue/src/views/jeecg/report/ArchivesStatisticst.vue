<template>
  <a-card :bordered="false">
    <a-tabs defaultActiveKey="1" @change="callback">
      <a-tab-pane tab="柱状图" key="1">
        <a-row :gutter="24">
          <a-col :span="10">
            <a-radio-group :value="barType" @change="statisticst">
              <a-radio-button value="year">按年统计</a-radio-button>
              <a-radio-button value="month">按月统计</a-radio-button>
              <a-radio-button value="category">按类别统计</a-radio-button>
              <a-radio-button value="cabinet">按柜号统计</a-radio-button>
            </a-radio-group>
          </a-col>
          <a-col :span="14">
            <a-form v-if="barType === 'month' && false" layout="inline" style="margin-top: -4px">
              <a-form-item label="月份区间">
                <a-range-picker
                  :placeholder="['开始月份', '结束月份']"
                  format="YYYY-MM"
                  :value="barValue"
                  :mode="barDate"
                  @panelChange="handleBarDate"/>
              </a-form-item>
              <a-button style="margin-top: 2px" type="primary" icon="search" @click="queryDatebar">查询</a-button>
              <a-button style="margin-top: 2px;margin-left: 8px" type="primary" icon="reload" @click="searchReset">重置</a-button>
            </a-form>
          </a-col>
          <bar class="statistic" title="档案统计" :dataSource="countSource" :height="400"/>
        </a-row>
      </a-tab-pane>

      <a-tab-pane tab="饼状图" key="2">
        <a-row :gutter="24">
          <a-col :span="10">
            <a-radio-group :value="pieType" @change="statisticst">
              <a-radio-button value="year">按年统计</a-radio-button>
              <a-radio-button value="month">按月统计</a-radio-button>
              <a-radio-button value="category">按类别统计</a-radio-button>
              <a-radio-button value="cabinet">按柜号统计</a-radio-button>
            </a-radio-group>
          </a-col>
          <a-col :span="14">
            <a-form v-if="pieType === 'month' && false" layout="inline" style="margin-top: -4px">
              <a-row :gutter="24">
                <a-form-item label="月份区间">
                  <a-range-picker
                    :placeholder="['开始月份', '结束月份']"
                    format="YYYY-MM"
                    :value="pieValue"
                    :mode="pieDate"
                    @panelChange="handlePieDate"/>
                </a-form-item>
                <a-button style="margin-top: 2px" type="primary" icon="search" @click="queryDatepie">查询</a-button>
                <a-button style="margin-top: 2px;margin-left: 8px" type="primary" icon="reload" @click="searchReset">重置</a-button>
              </a-row>
            </a-form>
          </a-col>
          <pie class="statistic" title="档案统计" :dataSource="countSource" :height="450"/>
        </a-row>
      </a-tab-pane>
    </a-tabs>
  </a-card>
</template>

<script>
  import Bar from '@/components/chart/Bar'
  import Pie from '@/components/chart/Pie'
  import ACol from 'ant-design-vue/es/grid/Col'
  import { getAction } from '@/api/manage'

  export default {
    name: 'ArchivesStatisticst',
    components: {
      ACol,
      Bar,
      Pie
    },
    data() {
      return {
        description: '档案统计页面',
        // 查询条件
        queryParam: {},
        // 数据集
        countSource: [],
        // 柱状图
        barType: 'year',
        barDate: ['month', 'month'],
        barValue: [],
        // 饼状图
        pieType: 'year',
        pieDate: ['month', 'month'],
        pieValue: [],
        // 统计图类型
        tabStatus:"bar",
        url: {
          getYearCountInfo: "/api/report/getYearCountInfo",
          getMonthCountInfo:"/api/report/getMonthCountInfo",
          getCntrNoCountInfo:"/api/report/getCntrNoCountInfo",
          getCabinetCountInfo:"/api/report/getCabinetCountInfo",
        },
      }
    },
    created() {
      let url = this.url.getYearCountInfo;
      this.loadDate(url,'year',{});
    },
    methods: {
      loadDate(url,type,param) {
        getAction(url,param,'get').then((res) => {
          if (res.success) {
            this.countSource = [];
            if(type === 'year'){
              this.getYearCountSource(res.result);
            }
            if(type === 'month'){
              this.getMonthCountSource(res.result);
            }
            if(type === 'category'){
              this.getCategoryCountSource(res.result);
            }
            if(type === 'cabinet'){
              this.getCabinetCountSource(res.result);
            }
          }else{
            var that=this;
            that.$message.warning(res.message);
          }
        })
      },
      getYearCountSource(data){
        for (let i = 0; i < data.length; i++) {
          if(this.tabStatus === "bar"){
            this.countSource.push({
              x: `${data[i].year}年`,
              y: data[i].yearcount
            })
          }else{
            this.countSource.push({
              item: `${data[i].year}年`,
              count:data[i].yearcount
            })
          }
        }
      },
      getMonthCountSource(data){
        for (let i = 0; i < data.length; i++) {
          if(this.tabStatus === "bar"){
            this.countSource.push({
              x: data[i].month,
              y: data[i].monthcount
            })
          }else{
            this.countSource.push({
              item: data[i].month,
              count:data[i].monthcount
            })
          }
        }
      },
      getCategoryCountSource(data){
        for (let i = 0; i < data.length; i++) {
          if(this.tabStatus ==="bar"){
            this.countSource.push({
              x: data[i].classifyname,
              y: data[i].cntrnocount
            })
          }else{
            this.countSource.push({
              item: data[i].classifyname,
              count:data[i].cntrnocount
            })
          }
        }
      },
      getCabinetCountSource(data){
        for (let i = 0; i < data.length; i++) {
          if(this.tabStatus === "bar"){
            this.countSource.push({
              x: data[i].cabinetname,
              y: data[i].cabinetcocunt
            })
          }else{
            this.countSource.push({
              item: data[i].cabinetname,
              count:data[i].cabinetcocunt
            })
          }
        }
      },
      // 选择统计图类别
      callback(key) {
        if(key === "1"){
          this.tabStatus = "bar";
          this.queryDatebar();
        }else{
          this.tabStatus = "pie";
          this.queryDatepie();
        }
      },
      // 选择统计类别
      statisticst(e) {
        if(this.tabStatus === "pie"){
          this.pieType = e.target.value;
          this.queryDatepie();
        }else{
          this.barType = e.target.value;
          this.queryDatebar();
        }
      },
      // 按月份查询
      queryDatebar(){
        if(this.barValue.length>0){
          this.getUrl(this.barType,{startTime:this.barValue[0]._d,endTime:this.barValue[1]._d});
        }else{
          this.getUrl(this.barType,{});
        }
      },
      queryDatepie(){
        if(this.pieValue.length>0){
          this.getUrl(this.pieType,{startTime:this.pieValue[0]._d,endTime:this.pieValue[1]._d});
        }else{
          this.getUrl(this.pieType,{});
        }
      },
      searchReset(){
        console.log(this.tabStatus);
        if(this.tabStatus === "pie"){
          this.pieValue = [];
        }else{
          this.barValue = [];
        }
        this.getUrl(this.barType,{});
      },
      // 选择请求url
      getUrl(type,param){
        let url = "";
        if(type === 'year'){
          url = this.url.getYearCountInfo;
        }
        if(type === 'month'){
          url = this.url.getMonthCountInfo;
        }
        if(type === 'category'){
          url = this.url.getCntrNoCountInfo;
        }
        if(type === 'cabinet'){
          url = this.url.getCabinetCountInfo;
        }
        this.loadDate(url,type,param);
      },
      // 选择月份日期
      handleBarDate(value, mode) {
        this.barValue = value
        this.barDate = [
          mode[0] === 'date' ? 'month' : mode[0],
          mode[1] === 'date' ? 'month' : mode[1]
        ]
      },
      handlePieDate(value, mode) {
        this.pieValue = value
        this.pieDate = [
          mode[0] === 'date' ? 'month' : mode[0],
          mode[1] === 'date' ? 'month' : mode[1]
        ]
      },
    }
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

  .statistic {
    padding: 0px !important;
    margin-top: 50px;
  }

  .statistic h4 {
    margin-bottom: 20px;
    text-align: center !important;
    font-size: 24px !important;;
  }

  .statistic #canvas_1 {
    width: 100% !important;
  }
</style>