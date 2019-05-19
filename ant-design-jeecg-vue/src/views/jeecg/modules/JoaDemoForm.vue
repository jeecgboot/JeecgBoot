<template>

      <a-form :form="form">
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="请假人">
          <a-input placeholder="请输入请假人" v-decorator="['name', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="请假天数">
          <a-input-number v-decorator="[ 'days', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="开始时间">
          <a-date-picker showTime format='YYYY-MM-DD' v-decorator="[ 'beginDate', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="请假结束时间">
          <a-date-picker showTime format='YYYY-MM-DD' v-decorator="[ 'endDate', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="请假原因">
          <a-input placeholder="请输入请假原因" v-decorator="['reason', {}]" />
        </a-form-item>
      </a-form>
</template>

<script>
  import { httpAction,getAction } from '@/api/manage'
  import moment from "moment"
  import pick from 'lodash.pick'
  import JDate from '@/components/jeecg/JDate.vue'

  export default {
    name: "JoaDemoModal",
    components: { JDate },
    props: ['formData'],
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

        confirmLoading: false,
        form: this.$form.createForm(this),
        validatorRules:{
        name:{},
        days:{},
        beginDate:{},
        endDate:{},
        reason:{},
        bpmStatus:{},
        },
        url: {
          queryById: "/test/joaDemo/queryById",
          add: "/test/joaDemo/add",
          edit: "/test/joaDemo/edit",
        },
      }
    },
    created () {
      console.log("form start");
      console.log("formdata",this.formData);
      if(this.formData.dataId){
        var params = {id:this.formData.dataId};//查询条件
        getAction(this.url.queryById,params).then((res)=>{
          if(res.success){
            console.log("获取表单数据",res);
            this.edit (res.result);
          }
        })
      }
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
          this.form.setFieldsValue(pick(this.model,'name','days','reason','bpmStatus'))
		  //时间格式化
          this.form.setFieldsValue({beginDate:this.model.beginDate?moment(this.model.beginDate):null})
          this.form.setFieldsValue({endDate:this.model.endDate?moment(this.model.endDate):null})
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
            formData.beginDate = formData.beginDate?formData.beginDate.format('YYYY-MM-DD HH:mm:ss'):null;
            formData.endDate = formData.endDate?formData.endDate.format('YYYY-MM-DD HH:mm:ss'):null;
            
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


    }
  }
</script>

<style scoped>

</style>