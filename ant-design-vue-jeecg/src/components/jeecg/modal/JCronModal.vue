<template>
  <a-modal
          title="corn表达式"
          :width="modalWidth"
          :visible="visible"
          :confirmLoading="confirmLoading"
          @ok="handleSubmit"
          @cancel="close"
          cancelText="关闭">
    <div class="card-container">
      <a-tabs type="card">
        <a-tab-pane key="1" type="card">
          <span slot="tab"><a-icon type="schedule" /> 秒</span>
          <a-radio-group v-model="result.second.cronEvery">
            <a-row>
              <a-radio value="1">每一秒钟</a-radio>
            </a-row>
            <a-row>
              <a-radio value="2">每隔
                <a-input-number size="small" v-model="result.second.incrementIncrement" :min="1" :max="59"></a-input-number>
                秒执行 从
                <a-input-number size="small" v-model="result.second.incrementStart" :min="0" :max="59"></a-input-number>
                秒开始
              </a-radio>
            </a-row>
            <a-row>
              <a-radio value="3">具体秒数(可多选)</a-radio>
              <a-select style="width:354px;" size="small" mode="multiple" v-model="result.second.specificSpecific">
                <a-select-option v-for="(val,index) in 60" :key="index" :value="index">{{ index }}</a-select-option>
              </a-select>
            </a-row>
            <a-row>
              <a-radio value="4">周期从
                <a-input-number size="small" v-model="result.second.rangeStart" :min="1" :max="59"></a-input-number>
                到
                <a-input-number size="small" v-model="result.second.rangeEnd" :min="0" :max="59"></a-input-number>
                秒
              </a-radio>
            </a-row>
          </a-radio-group>
        </a-tab-pane>
        <a-tab-pane key="2">
          <span slot="tab"><a-icon type="schedule" />分</span>
          <div class="tabBody">
            <a-radio-group v-model="result.minute.cronEvery">
              <a-row>
                <a-radio value="1">每一分钟</a-radio>
              </a-row>
              <a-row>
                <a-radio value="2">每隔
                  <a-input-number size="small" v-model="result.minute.incrementIncrement" :min="1" :max="60"></a-input-number>
                  分执行 从
                  <a-input-number size="small" v-model="result.minute.incrementStart" :min="0" :max="59"></a-input-number>
                  分开始
                </a-radio>
              </a-row>
              <a-row>
                <a-radio value="3">具体分钟数(可多选)</a-radio>
                <a-select style="width:340px;" size="small" mode="multiple" v-model="result.minute.specificSpecific">
                  <a-select-option v-for="(val,index) in Array(60)" :key="index" :value="index"> {{ index }}</a-select-option>
                </a-select>
              </a-row>
              <a-row>
                <a-radio value="4">周期从
                  <a-input-number size="small" v-model="result.minute.rangeStart" :min="1" :max="60"></a-input-number>
                  到
                  <a-input-number size="small" v-model="result.minute.rangeEnd" :min="0" :max="59"></a-input-number>
                  分
                </a-radio>
              </a-row>
            </a-radio-group>
          </div>
        </a-tab-pane>
        <a-tab-pane key="3">
          <span slot="tab"><a-icon type="schedule" /> 时</span>
          <div class="tabBody">
            <a-radio-group v-model="result.hour.cronEvery">
              <a-row>
                <a-radio value="1">每一小时</a-radio>
              </a-row>
              <a-row>
                <a-radio value="2">每隔
                  <a-input-number size="small" v-model="result.hour.incrementIncrement" :min="0" :max="23"></a-input-number>
                  小时执行 从
                  <a-input-number size="small" v-model="result.hour.incrementStart" :min="0" :max="23"></a-input-number>
                  小时开始
                </a-radio>
              </a-row>
              <a-row>
                <a-radio class="long" value="3">具体小时数(可多选)</a-radio>
                <a-select style="width:340px;" size="small" mode="multiple" v-model="result.hour.specificSpecific">
                  <a-select-option v-for="(val,index) in Array(24)" :key="index" >{{ index }}</a-select-option>
                </a-select>
              </a-row>
              <a-row>
                <a-radio value="4">周期从
                  <a-input-number size="small" v-model="result.hour.rangeStart" :min="0" :max="23"></a-input-number>
                  到
                  <a-input-number size="small" v-model="result.hour.rangeEnd" :min="0" :max="23"></a-input-number>
                  小时
                </a-radio>
              </a-row>
            </a-radio-group>
          </div>
        </a-tab-pane>
        <a-tab-pane key="4">
          <span slot="tab"><a-icon type="schedule" />  天</span>
          <div class="tabBody">
            <a-radio-group v-model="result.day.cronEvery">
              <a-row>
                <a-radio value="1">每一天</a-radio>
              </a-row>
              <a-row>
                <a-radio value="2">每隔
                  <a-input-number size="small" v-model="result.week.incrementIncrement" :min="1" :max="7"></a-input-number>
                  周执行 从
                  <a-select size="small" v-model="result.week.incrementStart">
                    <a-select-option v-for="(val,index) in Array(7)" :key="index" :value="index+1">{{ weekDays[index] }}</a-select-option>
                  </a-select>
                  开始
                </a-radio>
              </a-row>
              <a-row>
                <a-radio value="3">每隔
                  <a-input-number size="small" v-model="result.day.incrementIncrement" :min="1" :max="31"></a-input-number>
                  天执行 从
                  <a-input-number size="small" v-model="result.day.incrementStart" :min="1" :max="31"></a-input-number>
                  天开始
                </a-radio>
              </a-row>
              <a-row>
                <a-radio class="long" value="4">具体星期几(可多选)</a-radio>
                <a-select style="width:340px;" size="small" mode="multiple" v-model="result.week.specificSpecific">
                  <a-select-option v-for="(val,index) in Array(7)" :key="index" :value="index+1">{{ weekDays[index] }}</a-select-option>
                </a-select>
              </a-row>
              <a-row>
                <a-radio class="long" value="5">具体天数(可多选)</a-radio>
                <a-select style="width:354px;" size="small" mode="multiple" v-model="result.day.specificSpecific">
                  <a-select-option v-for="(val,index) in Array(31)" :key="index" :value="index+1">{{ index+1 }}</a-select-option>
                </a-select>
              </a-row>
              <a-row>
                <a-radio value="6">在这个月的最后一天</a-radio>
              </a-row>
              <a-row>
                <a-radio value="7">在这个月的最后一个工作日</a-radio>
              </a-row>
              <a-row>
                <a-radio value="8">在这个月的最后一个
                  <a-select size="small" v-model="result.day.cronLastSpecificDomDay">
                    <a-select-option v-for="(val,index) in Array(7)" :key="index" :value="index+1">{{ weekDays[index] }}</a-select-option>
                  </a-select>
                </a-radio>
              </a-row>
              <a-row>
                <a-radio value="9">
                  在本月底前
                  <a-input-number size="small" v-model="result.day.cronDaysBeforeEomMinus" :min="1" :max="31"></a-input-number>
                  天
                </a-radio>
              </a-row>
              <a-row>
                <a-radio value="10">最近的工作日（周一至周五）至本月
                  <a-input-number size="small" v-model="result.day.cronDaysNearestWeekday" :min="1" :max="31"></a-input-number>
                  日
                </a-radio>
              </a-row>
              <a-row>
                <a-radio value="11">在这个月的第
                  <a-input-number size="small" v-model="result.week.cronNthDayNth" :min="1" :max="5"></a-input-number>
                  个
                  <a-select size="small" v-model="result.week.cronNthDayDay">
                    <a-select-option v-for="(val,index) in Array(7)" :key="index" :value="index+1">{{ weekDays[index] }}</a-select-option>
                  </a-select>

                </a-radio>
              </a-row>
            </a-radio-group>
          </div>
        </a-tab-pane>
        <a-tab-pane key="5">
          <span slot="tab"><a-icon type="schedule" /> 月</span>
          <div class="tabBody">
            <a-radio-group v-model="result.month.cronEvery">
              <a-row>
                <a-radio value="1">每一月</a-radio>
              </a-row>
              <a-row>
                <a-radio value="2">每隔
                  <a-input-number size="small" v-model="result.month.incrementIncrement" :min="0" :max="12"></a-input-number>
                  月执行 从
                  <a-input-number size="small" v-model="result.month.incrementStart" :min="0" :max="12"></a-input-number>
                  月开始
                </a-radio>
              </a-row>
              <a-row>
                <a-radio class="long" value="3">具体月数(可多选)</a-radio>
                <a-select style="width:354px;" size="small" filterable mode="multiple" v-model="result.month.specificSpecific">
                  <a-select-option v-for="(val,index) in Array(12)" :key="index" :value="index+1">{{ index+1 }}</a-select-option>
                </a-select>
              </a-row>
              <a-row>
                <a-radio value="4">从
                  <a-input-number size="small" v-model="result.month.rangeStart" :min="1" :max="12"></a-input-number>
                  到
                  <a-input-number size="small" v-model="result.month.rangeEnd" :min="1" :max="12"></a-input-number>
                  月之间的每个月
                </a-radio>
              </a-row>
            </a-radio-group>
          </div>
        </a-tab-pane>
        <a-tab-pane key="6">
          <span slot="tab"><a-icon type="schedule" /> 年</span>
          <div class="tabBody">
            <a-radio-group v-model="result.year.cronEvery">
              <a-row>
                <a-radio value="1">每一年</a-radio>
              </a-row>
              <a-row>
                <a-radio value="2">每隔
                  <a-input-number size="small" v-model="result.year.incrementIncrement" :min="1" :max="99"></a-input-number>
                  年执行 从
                  <a-input-number size="small" v-model="result.year.incrementStart" :min="2019" :max="2119"></a-input-number>
                  年开始
                </a-radio>
              </a-row>
              <a-row>
                <a-radio class="long" value="3">具体年份(可多选)</a-radio>
                <a-select style="width:354px;" size="small" filterable mode="multiple" v-model="result.year.specificSpecific">
                  <a-select-option v-for="(val,index) in Array(100)" :key="index" :value="2019+index">{{ 2019+index }}</a-select-option>
                </a-select>
              </a-row>
              <a-row>
                <a-radio value="4">从
                  <a-input-number size="small" v-model="result.year.rangeStart" :min="2019" :max="2119"></a-input-number>
                  到
                  <a-input-number size="small" v-model="result.year.rangeEnd" :min="2019" :max="2119"></a-input-number>
                  年之间的每一年
                </a-radio>
              </a-row>
            </a-radio-group>
          </div>
        </a-tab-pane>
      </a-tabs>
      <div class="bottom">
        <span class="value">{{this.cron }}</span>
      </div>
    </div>
  </a-modal>
</template>
<script>
  export default {
    name:'VueCron',
    props:['data'],
    data(){
      return {
        visible: false,
        confirmLoading:false,
        size:'large',
        weekDays:['天','一','二','三','四','五','六'].map(val=>'星期'+val),
        result: {
          second:{},
          minute:{},
          hour:{},
          day:{},
          week:{},
          month:{},
          year:{}
        },
        defaultValue: {
          second:{
            cronEvery:'',
            incrementStart:3,
            incrementIncrement:5,
            rangeStart:1,
            rangeEnd:0,
            specificSpecific:[],
          },
          minute:{
            cronEvery:'',
            incrementStart:3,
            incrementIncrement:5,
            rangeStart:1,
            rangeEnd:'0',
            specificSpecific:[],
          },
          hour:{
            cronEvery:'',
            incrementStart:3,
            incrementIncrement:5,
            rangeStart:'0',
            rangeEnd:'0',
            specificSpecific:[],
          },
          day:{
            cronEvery:'',
            incrementStart:1,
            incrementIncrement:'1',
            rangeStart:'',
            rangeEnd:'',
            specificSpecific:[],
            cronLastSpecificDomDay:1,
            cronDaysBeforeEomMinus:1,
            cronDaysNearestWeekday:1,
          },
          week:{
            cronEvery:'',
            incrementStart:1,
            incrementIncrement:1,
            specificSpecific:[],
            cronNthDayDay:1,
            cronNthDayNth:1,
          },
          month:{
            cronEvery:'',
            incrementStart:3,
            incrementIncrement:5,
            rangeStart:1,
            rangeEnd:1,
            specificSpecific:[],
          },
          year:{
            cronEvery:'',
            incrementStart:2017,
            incrementIncrement:1,
            rangeStart:2019,
            rangeEnd: 2019,
            specificSpecific:[],
          },
          label:''
        }
      }
    },
    computed: {
      modalWidth(){
        return 608;
      },
      secondsText() {
        let seconds = '';
        let cronEvery=this.result.second.cronEvery||'';
        switch (cronEvery.toString()){
          case '1':
            seconds = '*';
            break;
          case '2':
            seconds = this.result.second.incrementStart+'/'+this.result.second.incrementIncrement;
            break;
          case '3':
            this.result.second.specificSpecific.map(val=> {seconds += val+','});
            seconds = seconds.slice(0, -1);
            break;
          case '4':
            seconds = this.result.second.rangeStart+'-'+this.result.second.rangeEnd;
            break;
        }
        return seconds;
      },
      minutesText() {
        let minutes = '';
        let cronEvery=this.result.minute.cronEvery||'';
        switch (cronEvery.toString()){
          case '1':
            minutes = '*';
            break;
          case '2':
            minutes = this.result.minute.incrementStart+'/'+this.result.minute.incrementIncrement;
            break;
          case '3':
            this.result.minute.specificSpecific.map(val=> {
              minutes += val+','
            });
            minutes = minutes.slice(0, -1);
            break;
          case '4':
            minutes = this.result.minute.rangeStart+'-'+this.result.minute.rangeEnd;
            break;
        }
        return minutes;
      },
      hoursText() {
        let hours = '';
        let cronEvery=this.result.hour.cronEvery||'';
        switch (cronEvery.toString()){
          case '1':
            hours = '*';
            break;
          case '2':
            hours = this.result.hour.incrementStart+'/'+this.result.hour.incrementIncrement;
            break;
          case '3':
            this.result.hour.specificSpecific.map(val=> {
              hours += val+','
            });
            hours = hours.slice(0, -1);
            break;
          case '4':
            hours = this.result.hour.rangeStart+'-'+this.result.hour.rangeEnd;
            break;
        }
        return hours;
      },
      daysText() {
        let days='';
        let cronEvery=this.result.day.cronEvery||'';
        switch (cronEvery.toString()){
          case '1':
            break;
          case '2':
          case '4':
          case '11':
            days = '?';
            break;
          case '3':
            days = this.result.day.incrementStart+'/'+this.result.day.incrementIncrement;
            break;
          case '5':
            this.result.day.specificSpecific.map(val=> {
              days += val+','
            });
            days = days.slice(0, -1);
            break;
          case '6':
            days = "L";
            break;
          case '7':
            days = "LW";
            break;
          case '8':
            days = this.result.day.cronLastSpecificDomDay + 'L';
            break;
          case '9':
            days = 'L-' + this.result.day.cronDaysBeforeEomMinus;
            break;
          case '10':
            days = this.result.day.cronDaysNearestWeekday+"W";
            break
        }
        return days;
      },
      weeksText() {
        let weeks = '';
        let cronEvery=this.result.day.cronEvery||'';
        switch (cronEvery.toString()){
          case '1':
          case '3':
          case '5':
            weeks = '?';
            break;
          case '2':
            weeks = this.result.week.incrementStart+'/'+this.result.week.incrementIncrement;
            break;
          case '4':
            this.result.week.specificSpecific.map(val=> {
              weeks += val+','
            });
            weeks = weeks.slice(0, -1);
            break;
          case '6':
          case '7':
          case '8':
          case '9':
          case '10':
            weeks = "?";
            break;
          case '11':
            weeks = this.result.week.cronNthDayDay+"#"+this.result.week.cronNthDayNth;
            break;
        }
        return weeks;
      },
      monthsText() {
        let months = '';
        let cronEvery=this.result.month.cronEvery||'';
        switch (cronEvery.toString()){
          case '1':
            months = '*';
            break;
          case '2':
            months = this.result.month.incrementStart+'/'+this.result.month.incrementIncrement;
            break;
          case '3':
            this.result.month.specificSpecific.map(val=> {
              months += val+','
            });
            months = months.slice(0, -1);
            break;
          case '4':
            months = this.result.month.rangeStart+'-'+this.result.month.rangeEnd;
            break;
        }
        return months;
      },
      yearsText() {
        let years = '';
        let cronEvery=this.result.year.cronEvery||'';
        switch (cronEvery.toString()){
          case '1':
            years = '*';
            break;
          case '2':
            years = this.result.year.incrementStart+'/'+this.result.year.incrementIncrement;
            break;
          case '3':
            this.result.year.specificSpecific.map(val=> {
              years += val+','
            });
            years = years.slice(0, -1);
            break;
          case '4':
            years = this.result.year.rangeStart+'-'+this.result.year.rangeEnd;
            break;
        }
        return years;
      },
      cron(){
        return `${this.secondsText||'*'} ${this.minutesText||'*'} ${this.hoursText||'*'} ${this.daysText||'*'} ${this.monthsText||'*'} ${this.weeksText||'?'} ${this.yearsText||'*'}`
      },
    },
    watch:{
      visible:{
        handler() {
          // if(this.data){
          //   //this. result = Object.keys(this.data.value).length>0?this.deepCopy(this.data.value):this.deepCopy(this.defaultValue);
          //   //this.result = Object.keys(this.data.value).length>0?clone(this.data.value):clone(this.defaultValue);
          //   //this.result = Object.keys(this.data.value).length>0?clone(JSON.parse(this.data.value)):clone(this.defaultValue);
          //   this.result = Object.keys(this.data.value).length>0?JSON.parse(this.data.value):JSON.parse(JSON.stringify(this.defaultValue));
          // }else{
          //   //this.result = this.deepCopy(this.defaultValue);
          //   //this.result = clone(this.defaultValue);
          //   this.result = JSON.parse(JSON.stringify(this.defaultValue));
          // }
          let label = this.data;
          if(label){
            this.secondsReverseExp(label)
            this.minutesReverseExp(label);
            this.hoursReverseExp(label);
            this.daysReverseExp(label);
            this.daysReverseExp(label);
            this.monthsReverseExp(label);
            this.yearReverseExp(label);
            JSON.parse(JSON.stringify(label));
          }else {
            this.result = JSON.parse(JSON.stringify(this.defaultValue));
          }
        }
      }
    },
    methods: {
      show(){
        this.visible = true;
        // console.log('secondsReverseExp',this.secondsReverseExp(this.data));
        // console.log('minutesReverseExp',this.minutesReverseExp(this.data));
        // console.log('hoursReverseExp',this.hoursReverseExp(this.data));
        // console.log('daysReverseExp',this.daysReverseExp(this.data));
        // console.log('monthsReverseExp',this.monthsReverseExp(this.data));
        // console.log('yearReverseExp',this.yearReverseExp(this.data));

      },
      handleSubmit(){
        this.$emit('ok',this.cron);
        this.close();
        this.visible = false;
      },
      close(){
        this.visible = false;
      },
      secondsReverseExp(seconds) {
        let val =  seconds.split(" ")[0];
        //alert(val);
        let second = {
          cronEvery:'',
          incrementStart:3,
          incrementIncrement:5,
          rangeStart:1,
          rangeEnd:0,
          specificSpecific:[]
        };
        switch (true) {
          case val.includes('*'):
            second.cronEvery = '1';
            break;
          case val.includes('/'):
            second.cronEvery = '2';
            second.incrementStart = val.split('/')[0];
            second.incrementIncrement = val.split('/')[1];
            break;
          case val.includes(','):
            second.cronEvery = '3';
            second.specificSpecific = val.split(',').map(Number).sort();
            break;
          case val.includes('-'):
            second.cronEvery = '4';
            second.rangeStart = val.split('-')[0];
            second.rangeEnd = val.split('-')[1];
            break;
          default:
            second.cronEvery = '1';
        }
        this.result.second = second;
      },
      minutesReverseExp(minutes) {
        let val =  minutes.split(" ")[1];
        let minute = {
          cronEvery:'',
          incrementStart:3,
          incrementIncrement:5,
          rangeStart:1,
          rangeEnd:0,
          specificSpecific:[],
        }
        switch (true) {
          case val.includes('*'):
            minute.cronEvery = '1';
            break;
          case val.includes('/'):
            minute.cronEvery = '2';
            minute.incrementStart = val.split('/')[0];
            minute.incrementIncrement = val.split('/')[1];
            break;
          case val.includes(','):
            minute.cronEvery = '3';
            minute.specificSpecific = val.split(',').map(Number).sort();
            break;
          case val.includes('-'):
            minute.cronEvery = '4';
            minute.rangeStart = val.split('-')[0];
            minute.rangeEnd = val.split('-')[1];
            break;
          default:
            minute.cronEvery = '1';
        }
        this.result.minute = minute;
      },
      hoursReverseExp(hours) {
        let val =  hours.split(" ")[2];
        let hour ={
            cronEvery:'',
            incrementStart:3,
            incrementIncrement:5,
            rangeStart:1,
            rangeEnd:'0',
            specificSpecific:[],
          };
        switch (true) {
          case val.includes('*'):
            hour.cronEvery = '1';
            break;
          case val.includes('/'):
            hour.cronEvery = '2';
            hour.incrementStart = val.split('/')[0];
            hour.incrementIncrement = val.split('/')[1];
            break;
          case val.includes(','):
            hour.cronEvery = '3';
            hour.specificSpecific = val.split(',').map(Number).sort();
            break;
          case val.includes('-'):
            hour.cronEvery = '4';
            hour.rangeStart = val.split('-')[0];
            hour.rangeEnd = val.split('-')[1];
            break;
          default:
            hour.cronEvery = '1';
          }
        this.result.hour = hour;
      },
      daysReverseExp(cron) {
        let days =  cron.split(" ")[3];
        let weeks =  cron.split(" ")[5];
        let day ={
          cronEvery:'',
          incrementStart:1,
          incrementIncrement:1,
          rangeStart:1,
          rangeEnd:1,
          specificSpecific:[],
          cronLastSpecificDomDay:1,
          cronDaysBeforeEomMinus:1,
          cronDaysNearestWeekday:1,
        };
        let week = {
            cronEvery:'',
            incrementStart:1,
            incrementIncrement:1,
            specificSpecific:[],
            cronNthDayDay:1,
            cronNthDayNth:'1',
        };
        if (!days.includes('?')) {
          switch (true) {
            case days.includes('*'):
              day.cronEvery = '1';
              break;
            case days.includes('?'):
              // 2、4、11
              break;
            case days.includes('/'):
              day.cronEvery = '3';
              day.incrementStart = days.split('/')[0];
              day.incrementIncrement = days.split('/')[1];
              break;
            case days.includes(','):
              day.cronEvery = '5';
              day.specificSpecific = days.split(',').map(Number).sort();
              // day.specificSpecific.forEach(function (value, index) {
              //   day.specificSpecific[index] = value -1;
              // });
              break;
            case days.includes('LW'):
              day.cronEvery = '7';
              break;
            case days.includes('L-'):
              day.cronEvery = '9';
              day.cronDaysBeforeEomMinus = days.split('L-')[1];
              break;
            case days.includes('L'):

              //alert(days);
              if(days.len == 1){
                day.cronEvery = '6';
                day.cronLastSpecificDomDay = '1';
              }
              else
              {
                day.cronEvery = '8';
                day.cronLastSpecificDomDay = Number(days.split('L')[0]);
              }
              break;
            case days.includes('W'):
              day.cronEvery = '10';
              day.cronDaysNearestWeekday = days.split('W')[0];
              break;
            default:
              day.cronEvery = '1';
          }
        }else {
          switch (true){
            case weeks.includes('/'):
              day.cronEvery = '2';
              week.incrementStart = weeks.split("/")[0];
              week.incrementIncrement = weeks.split("/")[1];
              break;
            case weeks.includes(','):
              day.cronEvery = '4';
              week.specificSpecific = weeks.split(',').map(Number).sort();
              break;
            case '#':
              day.cronEvery = '11';
              week.cronNthDayDay = weeks.split("#")[0];
              week.cronNthDayNth = weeks.split("#")[1];
              break;
            default:
              day.cronEvery = '1';
              week.cronEvery = '1';
          }
        }
        this.result.day = day;
        this.result.week = week;
      },
      monthsReverseExp(cron) {
        let months = cron.split(" ")[4];
        let month = {
          cronEvery:'',
            incrementStart:3,
            incrementIncrement:5,
            rangeStart:1,
            rangeEnd:1,
            specificSpecific:[],
        };
        switch (true){
          case months.includes('*'):
            month.cronEvery = '1';
            break;
          case months.includes('/'):
            month.cronEvery = '2';
            month.incrementStart = months.split('/')[0];
            month.incrementIncrement = months.split('/')[1];
            break;
          case months.includes(','):
            month.cronEvery = '3';
            month.specificSpecific = months.split(',').map(Number).sort();
            break;
          case months.includes('-'):
            month.cronEvery = '4';
            month.rangeStart = months.split('-')[0];
            month.rangeEnd = months.split('-')[1];
            break;
          default:
            month.cronEvery = '1';
        }
        this.result.month = month;
      },
      yearReverseExp(cron) {
        let years = cron.split(" ")[6];
        let year = {
          cronEvery:'',
          incrementStart:3,
          incrementIncrement:5,
          rangeStart:2019,
          rangeEnd:2019,
          specificSpecific:[],
        };
        switch (true){
          case years.includes('*'):
            year.cronEvery = '1';
            break;
          case years.includes('/'):
            year.cronEvery = '2';
            year.incrementStart = years.split('/')[0];
            year.incrementIncrement = years.split('/')[1];
            break;
          case years.includes(','):
            year.cronEvery = '3';
            year.specificSpecific = years.split(',').map(Number).sort();
            break;
          case years.includes('-'):
            year.cronEvery = '4';
            year.rangeStart = years.split('-')[0];
            year.rangeEnd = years.split('-')[1];
            break;
          default:
            year.cronEvery = '1';
        }
        this.result.year = year;
      }
    }
  }
</script>

<style lang="scss">
  .card-container {
    background: #fff;
    overflow: hidden;
    padding: 12px;
    position: relative;
    width: 100%;
    .ant-tabs{
      border:1px solid #e6ebf5;
      padding: 0;
      .ant-tabs-bar {
        margin: 0;
        outline: none;
        border-bottom: none;
        .ant-tabs-nav-container{
          margin: 0;
          .ant-tabs-tab {
            padding: 0 24px!important;
            background-color: #f5f7fa!important;
            margin-right: 0px!important;
            border-radius: 0;
            line-height: 38px;
            border: 1px solid transparent!important;
            border-bottom: 1px solid #e6ebf5!important;
          }
          .ant-tabs-tab-active.ant-tabs-tab{
            color: #409eff;
            background-color: #fff!important;
            border-right:1px solid #e6ebf5!important;
            border-left:1px solid #e6ebf5!important;
            border-bottom:1px solid #fff!important;
            font-weight: normal;
            transition:none!important;
          }
        }
      }
      .ant-tabs-tabpane{
        padding: 15px;
        .ant-row{
          margin: 10px 0;
        }
        .ant-select,.ant-input-number{
          width: 100px;
        }
      }
    }
  }
</style>
<style lang="scss" scoped>
  .container-widthEn{
    width: 755px;
  }
  .container-widthCn{
    width: 608px;
  }
  .language{
    text-align: center;
    position: absolute;
    right: 13px;
    top: 13px;
    border: 1px solid transparent;
    height: 40px;
    line-height: 38px;
    font-size: 16px;
    color: #409eff;
    z-index: 1;
    background: #f5f7fa;
    outline: none;
    width: 47px;
    border-bottom: 1px solid #e6ebf5;
    border-radius: 0;
  }
  .card-container{
    .bottom{
      display: flex;
      justify-content: center;
      padding: 10px 0 0 0;
      .cronButton{
        margin: 0 10px;
        line-height: 40px;
      }
    }
  }
  .tabBody{
    .a-row{
      margin: 10px 0;
      .long{
        .a-select{
          width:354px;
        }
      }
      .a-input-number{
        width: 110px;
      }
    }
  }
</style>
