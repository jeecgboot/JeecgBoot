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
          label="标题">
          <a-input placeholder="请输入标题" v-decorator="['titile', validatorRules.title]" :readOnly="disableSubmit" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="内容">
          <!--<a-input placeholder="请输入内容" v-decorator="['msgContent', {}]" />-->
          <a-textarea :rows="5" placeholder="..." v-decorator="[ 'msgContent', {} ]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="开始时间">
          <a-date-picker showTime format="YYYY-MM-DD HH:mm:ss" v-decorator="[ 'startTime', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="结束时间">
          <a-date-picker showTime format="YYYY-MM-DD HH:mm:ss" v-decorator="[ 'endTime', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="优先级">
          <a-select
            v-decorator="[ 'priority', {}]"
            placeholder="请选择优先级"
            :disabled="disableSubmit">
            <a-select-option value="L">低</a-select-option>
            <a-select-option value="M">中</a-select-option>
            <a-select-option value="H">高</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="通告对象类型">
          <a-select
            v-decorator="[ 'msgType', {}]"
            placeholder="请选择通告对象类型"
            :disabled="disableSubmit"
            @change="chooseMsgType">
            <a-select-option value="USER">指定用户</a-select-option>
            <a-select-option value="ALL">全体用户</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="指定用户"
          v-if="userType">
          <a-input placeholder="请选择用户" v-decorator="['userIds',{}]" @click="selectUserIds" disabled="true" />
        </a-form-item>
        <!--<a-modal
          :title="用户选择"
          :width="800"
          :visible="visible"
          :confirmLoading="confirmLoading"
          @ok="handleSubmit"
          @cancel="handleCancel"
          cancelText="关闭"
          wrapClassName="ant-modal-cust-warp"
          style="top:5%;height: 95%;overflow-y: hidden" ref="modelSelectUser"
        >
            123

        </a-modal>-->
      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>
  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import moment from "moment"

  export default {
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
        },
        url: {
          add: "/sys/annountCement/add",
          edit: "/sys/annountCement/edit",
        },
        userType:false,
        visible: false,
        confirmLoading: false,
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
        this.model = Object.assign({}, record);
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'titile','msgContent','priority','msgType'))
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
        this.close()
      },
      chooseMsgType(value) {
        if("USER" == value) {
          this.userType = true;
        } else {
          this.userType = false;
        }
      },
      selectUserIds() {
        console.log(1);
        this.$refs.modalSelectUser.show();
      }

    }
  }
</script>

<style scoped>

</style>