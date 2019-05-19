<template>
  <a-modal
    :title="title"
    :width="800"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭">

    <a-spin :spinning="confirmLoading">
      <a-form :form="form">

        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="用户名">
          <a-input placeholder="请输入用户名" v-decorator="['userName', {}]" readOnly="true"/>
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="代理人用户名">
         <a-input-search
            placeholder="请输入代理人用户名"
            v-decorator="['agentUserName', validatorRules.agentUserName]"
            readOnly="true"
            @search="onSearchDepUser"
            size="large">
            <a-button slot="enterButton">选择用户</a-button>
          </a-input-search>
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="代理开始时间">
          <a-date-picker showTime format='YYYY-MM-DD HH:mm:ss' v-decorator="[ 'startTime', validatorRules.startTime]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="代理结束时间">
          <a-date-picker showTime format='YYYY-MM-DD HH:mm:ss' v-decorator="[ 'endTime', validatorRules.endTime]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="状态">
          <a-radio-group class="fontiframe" name="radioGroup" v-decorator="[ 'status', {}]">
            <a-radio class="radioGroup" value="1">有效</a-radio>
            <a-radio class="radioGroup" value="0">无效</a-radio>
          </a-radio-group>
        </a-form-item>

      </a-form>

      <!-- 通过部门筛选，选择人 -->
      <j-search-user-by-dep ref="JSearchUserByDep" @ok="onSearchDepUserCallBack"></j-search-user-by-dep>
    </a-spin>
  </a-modal>
</template>

<script>
  import { httpAction, getAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import moment from "moment"
  import JSearchUserByDep from '@/components/jeecgbiz/JSearchUserByDep'

  export default {
    name: "SysUserAgentModal",
    components: {
      JSearchUserByDep
    },
    data () {
      return {
        title:"操作",
        visible: false,
        model: {},
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        username:"",
        confirmLoading: false,
        form: this.$form.createForm(this),
        validatorRules:{
          agentUserName:{rules: [{ required: true, message: '请输入代理人用户名!' }]},
          startTime:{rules: [{ required: true, message: '请输入代理开始时间!' }]},
          endTime:{rules: [{ required: true, message: '请输入代理结束时间!' }]},
        },
        url: {
          add: "/system/sysUserAgent/add",
          edit: "/system/sysUserAgent/edit",
          queryByUserName:"/system/sysUserAgent/queryByUserName",
        },
      }
    },
    created () {
    },
    methods: {
      agentSettings(username){
        this.username = username;
        this.init();

      },
      init () {
        var params = {userName:this.username};//查询条件
        getAction(this.url.queryByUserName,params).then((res)=>{
          if(res.success){
            console.log("获取流程节点信息",res);
            this.edit (res.result);
          }else{
            this.edit({userName:this.username,status:"0"});
          }
        })
      },
      edit (record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'userName','agentUserName','status'))
		      //时间格式化
          this.form.setFieldsValue({startTime:this.model.startTime?moment(this.model.startTime):null})
          this.form.setFieldsValue({endTime:this.model.endTime?moment(this.model.endTime):null})
        });

      },
      close () {
        this.$emit('close');
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
            //时间格式化
            formData.startTime = formData.startTime?formData.startTime.format('YYYY-MM-DD HH:mm:ss'):null;
            formData.endTime = formData.endTime?formData.endTime.format('YYYY-MM-DD HH:mm:ss'):null;

            console.log(formData)
            httpAction(httpurl,formData,method).then((res)=>{
              if(res.success){
                that.$message.success(res.message);
                //this.init();
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
        this.close()
      },
      //通过组织机构筛选选择用户
      onSearchDepUser() {
        this.$refs.JSearchUserByDep.showModal();
        this.$refs.JSearchUserByDep.title = '根据部门查询用户';
        this.$refs.JSearchUserByDep.type = 'radio'
      },
      onSearchDepUserCallBack(selectedDepUsers) {
        this.form.setFieldsValue({agentUserName:selectedDepUsers});
      }

    }
  }
</script>

<style scoped>

</style>