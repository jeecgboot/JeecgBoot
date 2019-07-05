<template>
  <div class="main">

    <a-form  style="max-width: 500px; margin: 40px auto 0;" :form="form">
      <a-form-item>
        <a-input
          v-decorator="['username',validatorRules.username]"
          size="large"
          type="text"
          autocomplete="false"
          placeholder="请输入用户名或手机号">
          <a-icon slot="prefix" type="lock" :style="{ color: 'rgba(0,0,0,.25)' }"/>
        </a-input>
      </a-form-item>
      <a-row :gutter="0">
        <a-col :span="14">
          <a-form-item>
            <a-input
              v-decorator="['inputCode',validatorRules.inputCode]"
              size="large"
              type="text"
              @change="inputCodeChange"
              placeholder="请输入验证码">
              <a-icon slot="prefix" v-if=" inputCodeContent==verifiedCode " type="smile"
                      :style="{ color: 'rgba(0,0,0,.25)' }"/>
              <a-icon slot="prefix" v-else type="frown" :style="{ color: 'rgba(0,0,0,.25)' }"/>
            </a-input>
          </a-form-item>
        </a-col>
        <a-col :span="10">
          <j-graphic-code @success="generateCode" style="float: right"></j-graphic-code>
        </a-col>
      </a-row>
      <a-form-item :wrapperCol="{span: 19, offset: 5}">
        <a-button type="primary" @click="nextStep">下一步</a-button>
      </a-form-item>
    </a-form>
  </div>
</template>

<script>
  import JGraphicCode from '@/components/jeecg/JGraphicCode'
  import { getAction } from  '@/api/manage'
  import {duplicateCheck } from '@/api/api'
  export default {
    name: "Step1",
    components: {
      JGraphicCode
    },
    data () {
      return {
        form: this.$form.createForm(this),
        inputCodeContent: "",
        inputCodeNull:true,
        verifiedCode:"",
        validatorRules: {
          username:{rules: [{ required: false},{validator: this.validateInputUsername}]},
          inputCode:{rules: [{ required: true, message: '请输入验证码!'},{validator: this.validateInputCode}]},
        },

      }
    },
    methods: {
      nextStep () {
        let that = this
        this.form.validateFields((err, values) => {
          if (!err){
          var params={}
          var reg=/^[1-9]\d*$|^0$/;
          var username=values.username;
          if(reg.test(username)==true) {
            params.phone=username;
          }else{
            params.username=username;
          }
            getAction("/sys/user/querySysUser",params).then((res)=>{
              if(res.success){
            var userList={
              username:res.result.username,
              phone:res.result.phone
            };
             setTimeout(function () {
              that.$emit('nextStep',userList)
            })
          }
          });
        }
    })

      },
      validateInputCode(rule, value, callback){
        if (!value || this.verifiedCode == this.inputCodeContent) {
          callback();
        } else {
          callback(new Error("您输入的验证码不正确!"));
        }
      },
      inputCodeChange(e){
        this.inputCodeContent = e.target.value;
        console.log(this.inputCodeContent)
        if(!e.target.value||0==e.target.value){
          this.inputCodeNull=true
        }else{
          this.inputCodeContent = this.inputCodeContent.toLowerCase()
          this.inputCodeNull=false
        }
      },
      generateCode(value){
        this.verifiedCode = value.toLowerCase();
        console.log(this.verifiedCode);
      },
      validateInputUsername(rule, value, callback){
        console.log(value);
        var reg=/^[0-9]+.?[0-9]*/;
        if(!value){
          callback("请输入用户名和手机号！");
        }

        if(reg.test(value)){
          var params = {
            tableName: 'sys_user',
            fieldName: 'phone',
            fieldVal: value,
            dataId: null
          };
          duplicateCheck(params).then((res) => {
            if (res.success) {
            callback("用户名不存在!")
          } else {
            callback()
          }
        })
        }else{
          var params = {
            tableName: 'sys_user',
            fieldName: 'username',
            fieldVal: value,
            dataId: null
          };
          duplicateCheck(params).then((res) => {
            if (res.success) {
            callback("用户名不存在!")
          } else {
            callback()
          }
        })
        }
      },

    }
  }
</script>

<style scoped>

</style>