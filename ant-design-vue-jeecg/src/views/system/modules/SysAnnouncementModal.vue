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
      <a-form :form="form">
        <a-row class="form-row" :gutter="{ xs: 8, sm: 16, md: 24, lg: 32 }">
          <a-col :lg="12">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="标题">
              <a-input placeholder="请输入标题" v-decorator="['titile', validatorRules.title]" :readOnly="disableSubmit" style="width: 90%"/>
            </a-form-item>
          </a-col>
          <a-col :lg="12">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="消息类型">
              <a-select
                v-decorator="[ 'msgCategory', validatorRules.msgCategory]"
                placeholder="请选择消息类型"
                :disabled="disableSubmit"
                :getPopupContainer = "(target) => target.parentNode"
                style="width: 80%" >
                <a-select-option value="1">通知公告</a-select-option>
                <a-select-option value="2">系统消息</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row class="form-row" :gutter="24">
          <a-col :lg="12">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="开始时间:"
              style="margin-left: 27px">
              <j-date  v-decorator="[ 'startTime', validatorRules.startTime]" placeholder="请选择开始时间" showTime dateFormat="YYYY-MM-DD HH:mm:ss" ></j-date>
            </a-form-item>
          </a-col>
          <a-col :lg="12">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="结束时间"
              class="endTime">
              <j-date  v-decorator="[ 'endTime', validatorRules.endTime]" placeholder="请选择结束时间" showTime dateFormat="YYYY-MM-DD HH:mm:ss"></j-date>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row class="form-row" :gutter="32">
          <a-col :lg="9">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="优先级"
              style="margin-left: 27px">
              <a-select
                v-decorator="[ 'priority', {}]"
                placeholder="请选择优先级"
                :disabled="disableSubmit"
                :getPopupContainer = "(target) => target.parentNode"
                style="margin-left: 5px;width: 135%">
                <a-select-option value="L">低</a-select-option>
                <a-select-option value="M">中</a-select-option>
                <a-select-option value="H">高</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :lg="15" push="3">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="通告对象类型"
              style="margin-left: -14px">
              <a-select
                v-decorator="[ 'msgType', validatorRules.msgType]"
                placeholder="请选择通告对象类型"
                :disabled="disableSubmit"
                @change="chooseMsgType"
                :getPopupContainer = "(target) => target.parentNode"
                style="width: 200px;margin-left: 5px">
                <a-select-option value="USER">指定用户</a-select-option>
                <a-select-option value="ALL">全体用户</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :lg="24" pull="2">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="指定用户"
              v-if="userType">
              <a-select
                mode="multiple"
                placeholder="请选择用户"
                v-model="selectedUser"
                @dropdownVisibleChange="selectUserIds"
                style="width: 119%"
              >
              </a-select>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :lg="24" pull="3">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="内容"
              style="margin-left: 5px">
              <j-editor  style="width: 130%" v-decorator="[ 'msgContent', {} ]" triggerChange></j-editor>
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
    </a-spin>
    <select-user-list-modal ref="UserListModal" @choseUser="choseUser"></select-user-list-modal>
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
          sm: { span: 5 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },

        confirmLoading: false,
        form: this.$form.createForm(this),
        validatorRules:{
          title:{rules: [{ required: true, message: '请输入标题!' }]},
          msgCategory:{rules: [{ required: true, message: '请选择消息类型!' }]},
          msgType:{rules: [{ required: true, message: '请选择通告对象类型!' }]},
          endTime:{rules:[{validator: this.endTimeValidate}]},
          startTime:{rules:[{validator: this.startTimeValidate}]}
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
      }
    },
    created () {
    },
    methods: {
      add () {
        this.edit({});
      },
      edit (record) {
        this.form.resetFields();
        this.model = {}
        this.disable = false;
        this.visible = true;
        this.getUser(record);
      },
      getUser(record){
        this.model = Object.assign({}, record);
        // 指定用户
        if(record&&record.msgType === "USER"){
          this.userType =  true;
          this.userIds = record.userIds;
          getAction(this.url.queryByIds,{userIds:this.userIds}).then((res)=>{
            if(res.success){
              for(var i=0;i<res.result.length;i++){
                this.selectedUser.push(res.result[i].realname);
              }
              this.$refs.UserListModal.edit(res.result,this.userIds);
            }
          });
        }
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'endTime','startTime','titile','msgContent','sender','priority','msgCategory','msgType','sendStatus','delFlag'))
        });
      },
      close () {
        this.$emit('close');
        this.selectedUser = [];
        this.visible = false;
      },
      handleOk () {
        const that = this;
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
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
            let formData = Object.assign(this.model, values);
            if(this.userType){
              formData.userIds =  this.userIds;
            }
            console.log(formData)
            httpAction(httpurl,formData,method).then((res)=>{
              if(res.success){
                that.$message.success(res.message);
                that.$emit('ok');
              }else{
                that.$message.warning(res.message);
              }
            }).finally(() => {
              that.confirmLoading = false;
              that.close();
            })



          }
        })
      },
      handleCancel () {
        this.visible = false;
        this.$emit('close');
        this.resetUser();
      },
      resetUser (){
        this.userType =  false;
        this.userIds = [];
        this.selectedUser = [];
        this.disabled = false;
        this.$refs.UserListModal.edit(null,null);
      },
      selectUserIds() {
        this.$refs.UserListModal.add(this.selectedUser,this.userIds);
      },
      chooseMsgType(value) {
        if("USER" == value) {
          this.userType = true;
        } else {
          this.userType = false;
          this.selectedUser = [];
          this.userIds = [];
        }
      },
      // 子modal回调
      choseUser:function(userList){
        this.selectedUser = [];
        this.userIds = [];
        for(var i=0;i<userList.length;i++){
          this.selectedUser.push(userList[i].realname);
          this.userIds += userList[i].id+","
        }
      },
      startTimeValidate(rule,value,callback){
        let endTime = this.form.getFieldValue("endTime")
        if(!value || !endTime){
          callback()
        }else if(moment(value).isBefore(endTime)){
          callback()
        }else{
          callback("开始时间需小于结束时间")
        }
      },
      endTimeValidate(rule,value,callback){
        let startTime = this.form.getFieldValue("startTime")
        if(!value || !startTime){
          callback()
        }else if(moment(startTime).isBefore(value)){
          callback()
        }else{
          callback("结束时间需大于开始时间")
        }
      }

    }
  }
</script>

<style scoped>

</style>