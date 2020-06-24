<template>
  <a-card :bordered="false">
    <a-row>
      <a-col :md="2" :sm="4">
        <a-select defaultValue="POST" style="width: 90px" @change="handleChange" size="large">
          <a-select-option value="POST">POST</a-select-option>
          <!--<a-select-option value="GET">GET</a-select-option>-->
        </a-select>
      </a-col>
      <a-col :md="22" :sm="20">
        <a-input-search
          placeholder="input send url"
          v-model="url"
          @search="onSearch"
          enterButton="Send"
          size="large" />
      </a-col>
    </a-row>

    <a-tabs defaultActiveKey="2">
      <a-tab-pane tab="params" key="2">
        <textarea style="width:100%;font-size: 16px;font-weight:500" :rows="13" @input="changeVal">
        </textarea>
      </a-tab-pane>
    </a-tabs>

    <a-tabs defaultActiveKey="1">
      <a-tab-pane tab="response" key="1">
        <textarea style="width:100%;font-size: 16px;font-weight:500" :rows="10" v-html="resultJson" readOnly>
        </textarea>
      </a-tab-pane>
    </a-tabs>
  </a-card>
</template>
<script>
  import { postAction,getAction } from '@/api/manage'
  import { ACCESS_TOKEN } from "@/store/mutation-types"
  import Vue from 'vue'
  export default {
    name: 'FlowTest',
    data(){
      return {
        url:"",
        paramJson:"",
        resultJson:{},
        requestMethod:"POST"
      }
    },
    methods: {
      onSearch (value) {
        let that = this
        this.resultJson = {};
        if("POST"===this.requestMethod.toUpperCase()){
          postAction(value,this.paramJson).then((res)=>{
            console.log(res)
            this.resultJson = res
          }).catch((err) => {
            that.$message.error("请求异常："+err)
          })
        }else {
          getAction(value,this.paramJson).then((res)=>{
            console.log(res)
            this.resultJson = res;
          }).catch((err) => {
            that.$message.error("请求异常："+err)
          })
        }
      },
      changeVal(e){
        try {
          let json = e.target.value;
          json = json.replace(/\n/g,"");
          json = json.replace(/\s*/g,"");
          if(json.indexOf(",}")>0){
            json = json.replace(",}","}");
          }
          this.paramJson = JSON.parse(json);
        }catch (e) {
          console.log(e);
          this.$message.error("非法的JSON字符串")
        }
      },
      handleChange(value) {
        this.requestMethod = value;
      },
      created () {
        const token = Vue.ls.get(ACCESS_TOKEN);
        this.headers = {"X-Access-Token":token}

      }
    }
  }
</script>