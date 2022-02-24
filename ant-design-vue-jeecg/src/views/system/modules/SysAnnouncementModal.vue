<template>
  <a-modal
    :title="title"
    :width="900"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    :okButtonProps="{ props: {disabled: disabled} }"
    cancelText="关闭">

    <a-spin :spinning="confirmLoading">
      <a-form-model ref="form" :model="model" :rules="validatorRules">
        <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" prop="msgCategory" label="消息类型">
          <a-radio-group v-model="model.msgCategory" :disabled="disableSubmit">
            <a-radio value="1">通知公告</a-radio>
            <a-radio value="2">系统消息</a-radio>
          </a-radio-group>
        </a-form-model-item>
        <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" prop="titile" label="标题">
          <a-input placeholder="请输入标题" v-model="model.titile" :readOnly="disableSubmit"/>
        </a-form-model-item>
        <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" prop="msgAbstract" label="摘要">
          <a-textarea placeholder="请输入摘要"  v-model="model.msgAbstract" />
        </a-form-model-item>
        <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" prop="endTime" label="截至日期" class="endTime">
          <j-date style="width: 100%" :getCalendarContainer="node => node.parentNode" v-model="model.endTime" placeholder="请选择结束时间" showTime dateFormat="YYYY-MM-DD HH:mm:ss" />
        </a-form-model-item>
        <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" prop="msgType" label="接收用户">
          <a-radio-group v-model="model.msgType" :disabled="disableSubmit" @change="chooseMsgType">
            <a-radio value="USER">指定用户</a-radio>
            <a-radio value="ALL">全体用户</a-radio>
          </a-radio-group>
        </a-form-model-item>
        <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="指定用户" v-if="userType">
          <j-select-multi-user :returnKeys="returnKeys" placeholder="请选择指定用户" v-model="userIds" :trigger-change="true"></j-select-multi-user>
        </a-form-model-item>
        <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="优先级" >
          <a-radio-group v-model="model.priority" placeholder="请选择优先级" :disabled="disableSubmit">
            <a-radio value="L">低</a-radio>
            <a-radio value="M">中</a-radio>
            <a-radio value="H">高</a-radio>
          </a-radio-group>
        </a-form-model-item>
        <a-form-model-item :labelCol="labelColX1" :wrapperCol="wrapperColX1" label="内容" class="j-field-content">
          <j-editor v-model="model.msgContent" />
        </a-form-model-item>
      </a-form-model>
    </a-spin>
  </a-modal>
</template>

<script>
  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { getAction } from '@/api/manage'
  import JDate from '@/components/jeecg/JDate'
  import JEditor from '@/components/jeecg/JEditor'
  import SelectUserListModal from "./SelectUserListModal";
  import moment from 'moment'

  export default {
    components: { JEditor, JDate, SelectUserListModal},
    name: "SysAnnouncementModal",
    data () {
      return {
        title:"操作",
        visible: false,
        disableSubmit:false,
        model: {},
        labelCol: {
          xs: { span: 24 },
          sm: { span: 4 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 18 },
        },
        labelColX1: {
          xs: { span: 24 },
          sm: { span: 4 },
        },
        wrapperColX1: {
          xs: { span: 24 },
          sm: { span: 18 },
        },
        confirmLoading: false,
        validatorRules:{
          titile: [{ required: true, message: '请输入标题!' }],
          msgCategory: [{ required: true, message: '请选择消息类型!' }],
          msgType:[{ required: true, message: '请选择通告对象类型!' }],
          endTime:[{ required: true, message: '请选择结束时间!'} ,{validator: this.endTimeValidate}],
          startTime:[{required: true, message: '请选择开始时间!'},{validator: this.startTimeValidate}],
          msgAbstract: [{ required: true, message: '请输入摘要!' }],
        },
        url: {
          queryByIds: "/sys/user/queryByIds",
          add: "/sys/annountCement/add",
          edit: "/sys/annountCement/edit",
        },
        userType:false,
        userIds:[],
        selectedUser:[],
        disabled:false,
        msgContent:"",
        userList:[],
        returnKeys:['id', 'id'] //用户选择返回字段
      }
    },
    created () {
    },
    methods: {
      add () {
        this.edit({});
      },
      edit (record) {
        this.model = {}
        this.disable = false;
        this.visible = true;
        this.getUser(record);
      },
      getUser(record){
        //update-begin---author:wangshuai ---date:20211227  for：[JTC-191]系统通告参考vue3的来改，为单选按钮附默认值------------
        record.msgCategory = record.msgCategory?record.msgCategory:"1"
        record.msgType = record.msgType?record.msgType:"ALL"
        record.priority = record.priority?record.priority:"H"
        //update-begin---author:wangshuai ---date:20211227  for：[JTC-191]系统通告参考vue3的来改，为单选按钮附默认值------------
        this.model = Object.assign({}, record);
        // 指定用户
        if(record&&record.msgType === "USER"){
          this.userType =  true;
          //update-begin---author:wangshuai ---date:20220104  for：[JTC-304]指定人员不支持分页勾选,换通用的用户组件------------
          this.userIds = record.userIds.substr(0,record.userIds.length-1);
          //update-end---author:wangshuai ---date:20220104  for：[JTC-304]指定人员不支持分页勾选,换通用的用户组件------------
        }
      },
      close () {
        this.$emit('close');
        this.selectedUser = [];
        this.visible = false;
        this.$refs.form.resetFields();
      },
      handleOk () {
        const that = this;
        //当设置指定用户类型，但用户为空时，后台报错
        if(this.userType &&!(this.userIds!=null && this.userIds.length >0)){
            this.$message.warning('指定用户不能为空！')
            return;
          }
        // 触发表单验证
        this.$refs.form.validate(valid => {
          if (valid) {
            that.confirmLoading = true;
            let httpurl = '';
            let method = '';
            if(!this.model.id){
              httpurl+=this.url.add;
              method = 'post';
            }else{
              httpurl+=this.url.edit;
               method = 'put';
            }
            if(this.userType){
              //update-begin---author:wangshuai ---date:20220104  for：[JTC-304]指定人员不支持分页勾选,换通用的用户组件------------
              this.model.userIds =  this.userIds+",";
              //update-end---author:wangshuai ---date:20220104  for：[JTC-304]指定人员不支持分页勾选,换通用的用户组件------------
            }
            httpAction(httpurl,this.model,method).then((res)=>{
              if(res.success){
                that.$message.success(res.message);
                that.$emit('ok');
                that.resetUser();
              }else{
                that.$message.warning(res.message);
              }
            }).finally(() => {
              that.confirmLoading = false;
              that.close();
            })

          }else{
            return false;
          }
        })
      },
      handleCancel () {
        this.visible = false;
        this.$emit('close');
        this.$refs.form.resetFields();
        this.resetUser();
      },
      resetUser (){
        this.userType =  false;
        this.userIds = [];
        this.disabled = false;
        this.$refs.UserListModal.edit(null,null);
      },
      chooseMsgType(e) {
        if("USER" == e.target.value) {
          this.userType = true;
        } else {
          this.userType = false;
          this.userIds = [];
        }
      },
      startTimeValidate(rule,value,callback){
        let endTime = this.model.endTime
        if(!value || !endTime){
          callback()
        }else if(moment(value).isBefore(endTime)){
          callback()
        }else{
          callback("开始时间需小于结束时间")
        }
      },
      endTimeValidate(rule,value,callback){
        let startTime = this.model.startTime;
        if(!value || !startTime){
          callback()
        }else if(moment(startTime).isBefore(value)){
          callback()
        }else{
          callback("结束时间需大于开始时间")
        }
      },
    }
  }
</script>

<style scoped>

</style>