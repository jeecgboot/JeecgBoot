<template>
  <a-range-picker
    :disabled="disabled || readOnly"
    @change="handleDateChange"
    :disabledDate="disabledDate"
    :value="momVal"
    :ranges="{ 昨天: [moment().subtract(1,'day'), moment().endOf('day')], '本月': [moment().startOf('month'), moment().startOf('day') ],  '上月': [moment().month(moment().month() - 1).startOf('month'), moment().month(moment().month() - 1).endOf('month') ] ,  '上上月': [moment().month(moment().month() - 2).startOf('month'), moment().month(moment().month() - 2).endOf('month') ]}"
    :showTime="showTime"
    :format="dateFormat"
    :allowClear="false"
    :getCalendarContainer="getCalendarContainer"
  />
</template>


<script>
  import moment from 'moment'
  export default {
    name: 'JRangeDate',
    props: {
      placeholder:{
        type: String,
        default: '',
        required: false
      },
      value:{
        type: Array,
        default: function () { return [] },
        // default: '',
        required: false
      },
      dateFormat:{
        type: String,
        default: 'YYYY-MM-DD',
        required: false
      },
      //此属性可以被废弃了
      triggerChange:{
        type: Boolean,
        required: false,
        default: false
      },
      readOnly:{
        type: Boolean,
        required: false,
        default: false
      },
      disabled:{
        type: Boolean,
        required: false,
        default: false
      },
      showTime:{
        type: Boolean,
        required: false,
        default: false
      },
      getCalendarContainer: {
        type: Function,
        default: () => document.body
      }
    },
    data () {
      debugger
      let dateStr = this.value;
      console.log(!dateStr);
      return {



        /**   若需要给查询时间控件默认日期 则  将Null 替换为 [moment(moment().startOf('month')), moment(moment().startOf('day'))]  **/
        // momVal: dateStr? [moment(moment().startOf('month')), moment(moment().startOf('day'))]:[moment(dateStr[0]), moment(dateStr[1])]
        momVal: dateStr? null:[moment(dateStr[0]), moment(dateStr[1])]
        // momVal:dateStr
      }
    },
    watch: {
      value (val) {
        debugger
        if(val.length === 0){
          this.momVal = null
        }else{
          // this.momVal = moment(val,this.dateFormat)
          this.momVal = [moment(val[0]), moment(val[1])]
        }
      }
    },
    methods: {
      moment,
      handleDateChange(mom,dateStr){
        // this.momVal = [moment(dateStr[0]), moment(dateStr[1])]
        this.$emit('change', dateStr);
        //[moment(startDate), moment(endDate)];
        // let startDate = moment().startOf('month');
        // let endDate = moment().startOf('day');
        // this.queryParam.customsReleaseDate = [moment(startDate), moment(endDate)];
      },
      disabledDate(current) {
        return current && current > moment().endOf('day');
      },
    },
    //2.2新增 在组件内定义 指定父组件调用时候的传值属性和事件类型 这个牛逼
    model: {
      prop: 'value',
      event: 'change'
    }
  }
</script>
<!--<style scoped>-->

<!--</style>-->