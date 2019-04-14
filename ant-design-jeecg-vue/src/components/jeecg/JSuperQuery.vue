<template>
  <a-modal
    title="高级查询构造器"
    :width="800"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @cancel="handleCancel"
    :mask="false"
    wrapClassName="ant-modal-cust-warp"
    style="top:5%;max-height: 95%;">
    <template slot="footer">
      <a-button @click="handleCancel">关 闭</a-button>
      <a-button @click="handleReset" style="float: left">重 置</a-button>
      <a-button type="primary" @click="handleOk">查 询</a-button>
    </template>

    <a-spin :spinning="confirmLoading">
      <a-form>
        <div>
          <a-row type="flex" style="margin-bottom:10px" :gutter="16" v-for="(item, index) in queryParamsModel" :key="index">

            <a-col :span="6">
              <a-select placeholder="选择查询字段" v-model="item.field" @select="(val,option)=>handleSelected(option,item)">
                <a-select-option v-for="(f,fIndex) in fieldList" :key=" 'field'+fIndex" :value="f.value" :data-type="f.type">{{ f.text }}</a-select-option>
              </a-select>
            </a-col>

            <a-col :span="6">
              <a-select placeholder="选择匹配规则" v-model="item.rule">
                <a-select-option value="eq">等于</a-select-option>
                <a-select-option value="ne">不等于</a-select-option>
                <a-select-option value="gt">大于</a-select-option>
                <a-select-option value="ge">大于等于</a-select-option>
                <a-select-option value="lt">小于</a-select-option>
                <a-select-option value="le">小于等于</a-select-option>
                <a-select-option value="right_like">以..开始</a-select-option>
                <a-select-option value="left_like">以..结尾</a-select-option>
                <a-select-option value="like">包含</a-select-option>
                <a-select-option value="in">在...中</a-select-option>
              </a-select>
            </a-col>

            <a-col :span="6">
              <j-date v-if=" item.type=='date' " v-model="item.val" placeholder="请选择日期"></j-date>
              <j-date v-else-if=" item.type=='datetime' " v-model="item.val" placeholder="请选择时间" :show-time="true" date-format="YYYY-MM-DD HH:mm:ss"></j-date>
              <a-input-number v-else-if=" item.type=='int'||item.type=='number' " style="width: 100%" placeholder="请输入数值" v-model="item.val"/>
              <a-input v-else v-model="item.val" placeholder="请输入值" />
            </a-col>

            <a-col :span="6">
              <a-button @click="handleAdd" icon="plus"></a-button>&nbsp;
              <a-button @click="handleDel( index )" icon="minus"></a-button>
            </a-col>

          </a-row>
        </div>
      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>
  import ACol from 'ant-design-vue/es/grid/Col'
  import JDate from '@/components/jeecg/JDate.vue';

  export default {
    name: 'JSuperQuery',
    components: {
      ACol,
      JDate
    },
    data(){
      return {
        visible:false,
        confirmLoading:false,
        queryParamsModel:[{}]
      }
    },
    props:{
      /*  fieldList:[{value:'',text:'',type:''}]
      * type:date datetime int number string
      * */
      fieldList:{
        type:Array,
        required:true
      },
      /*
      * 这个回调函数接收一个数组参数 即查询条件
      * */
      callback:{
        type:String,
        required:false,
        default:'handleSuperQuery'
      }
    },
    methods:{
      show(){
        if(!this.queryParamsModel ||this.queryParamsModel.length==0){
          this.queryParamsModel = [{}]
        }
        this.visible = true;
      },
      handleOk(){
        console.log("---高级查询参数--->",this.queryParamsModel)
        if(!this.isNullArray()){
          this.$emit(this.callback, this.queryParamsModel)
        }else{
          this.$emit(this.callback)
        }
      },
      handleCancel(){
        this.close()
      },
      close () {
        this.$emit('close');
        this.visible = false;
      },
      handleAdd () {
        this.queryParamsModel.push({});
      },
      handleDel (index) {

        this.queryParamsModel.splice(index,1);
        this.$message.warning("请关闭后重新打开")
      },
      handleSelected(option,item){
        item['type'] = option.data.attrs['data-type']
      },
      handleReset(){
        this.queryParamsModel=[{}]
        this.$emit(this.callback)
      },
      isNullArray(){
        //判断是不是空数组对象
        if(!this.queryParamsModel || this.queryParamsModel.length==0){
          return true
        }
        if(this.queryParamsModel.length==1){
          let obj = this.queryParamsModel[0]
          if(!obj.field || !obj.val || !obj.rule){
            return true
          }
        }
        return false;
      }
    }
  }
</script>

<style >

</style>