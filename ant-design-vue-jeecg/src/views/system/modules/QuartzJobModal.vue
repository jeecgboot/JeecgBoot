<template>
  <a-modal
    :title="title"
    :width="800"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    okText="保存并安排任务"
    cancelText="关闭">

    <a-spin :spinning="confirmLoading">
      <a-form :form="form">

        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="任务类名"
          hasFeedback >
          <a-input placeholder="请输入任务类名" v-decorator="['jobClassName', {rules: [{ required: true, message: '请输入任务类名!' }]}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="cron表达式">
<!--                    <a-input placeholder="请输入cron表达式" v-decorator="['cronExpression', {'initialValue':'0/1 * * * * ?',rules: [{ required: true, message: '请输入任务类名!' }]}]" />-->
<!--                    <a target="_blank" href="http://cron.qqe2.com/">-->
<!--                      <a-icon type="share-alt" />-->
<!--                      在线cron表达式生成-->
<!--                    </a>-->
          <j-cron ref="innerVueCron" v-decorator="['cronExpression', {'initialValue':'0/1 * * * * ?',rules: [{ required: true, message: '请输入cron表达式!' }]}]"  @change="setCorn"></j-cron>
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="参数"
          hasFeedback >
          <a-input placeholder="请输入参数" v-decorator="['parameter', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="描述"
          hasFeedback >
          <a-input placeholder="请输入描述" v-decorator="['description', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="状态">
          <a-radio-group buttonStyle="solid" v-decorator="[ 'status', {'initialValue':0}]">
            <a-radio-button :value="0">正常</a-radio-button>
            <a-radio-button :value="-1">停止</a-radio-button>
          </a-radio-group>
        </a-form-item>

      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>
  import { httpAction } from '@/api/manage'
  import JCron from "@/components/jeecg/JCron.vue";
  import pick from 'lodash.pick'
  import moment from "moment"

  export default {
    name: "QuartzJobModal",
    components: {
      JCron
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
        cron: {
          label: '',
          value: ''
        },
        confirmLoading: false,
        form: this.$form.createForm(this),
        validatorRules: {
          cron: {
            rules: [{
              required: true, message: '请输入cron表达式!'
            }]
          }
        },
        url: {
          add: "/sys/quartzJob/add",
          edit: "/sys/quartzJob/edit",
        },
      }
    },
    created () {
    },
    methods: {
      add () {
        this.edit({});
      },
      edit (record) {
        this.model = Object.assign({},record);
        console.log(this.model)
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'jobClassName','cronExpression','parameter','description','status'));
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
          console.log('values',values)
          if (!err) {
            // if (typeof values.cronExpression == "undefined" || Object.keys(values.cronExpression).length==0 ) {
            //   this.$message.warning('请输入cron表达式!');
            //   return false;
            // }

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

            console.log('提交参数',formData)
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
      setCorn(data){
        console.log('data)',data);
        this.$nextTick(() => {
          this.model.cronExpression = data;
        })

        // console.log(Object.keys(data).length==0);
        // if (Object.keys(data).length==0) {
        //   this.$message.warning('请输入cron表达式!');
        // }
      },
      validateCron(rule, value, callback){
        if(!value){
          callback()
        }else if (Object.keys(value).length==0) {
          callback("请输入cron表达式!");
        }
      },

    }
  }
</script>

<style scoped>

</style>