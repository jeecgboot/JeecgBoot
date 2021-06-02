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
      <a-form-model ref="form" :model="model" :rules="validatorRules">

        <a-form-model-item :labelCol="labelCol"  :wrapperCol="wrapperCol" label="任务类名" prop="jobClassName" hasFeedback >
          <a-input placeholder="请输入任务类名" v-model="model.jobClassName" />
        </a-form-model-item>
        <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="Cron表达式" prop="cronExpression">
          <!-- <j-cron v-model="model.cronExpression"/>-->
          <j-easy-cron v-model="model.cronExpression" />
        </a-form-model-item>
        <a-form-model-item  :labelCol="labelCol" :wrapperCol="wrapperCol" label="参数" prop="parameter" >
          <a-textarea placeholder="请输入参数" :rows="5" v-model="model.parameter" />
        </a-form-model-item>
        <a-form-model-item :labelCol="labelCol"  :wrapperCol="wrapperCol" label="描述" prop="description">
          <a-textarea placeholder="请输入描述" :rows="3" v-model="model.description" />
        </a-form-model-item>
        <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol"  label="状态" prop="status">
          <j-dict-select-tag type="radioButton" v-model="model.status" dictCode="quartz_status"/>
        </a-form-model-item>
      </a-form-model>
    </a-spin>
  </a-modal>
</template>

<script>
  import { httpAction } from '@/api/manage'
  // import JCron from "@/components/jeecg/JCron";
  import cronValidator from "@/components/jeecg/JEasyCron/validator";

  export default {
    name: "QuartzJobModal",
    components: {
      // JCron,
    },
    data () {
      return {
        title:"操作",
        buttonStyle: 'solid',
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
        validatorRules: {
          cronExpression: [
            {required: true, message: '请输入cron表达式!'},
            {validator: cronValidator,}
          ],
          jobClassName: [{required: true, message: '请输入任务类名!'}]
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
      add() {
        // 统一设置默认值
        this.edit({
          cronExpression: '* * * * * ? *',
          status: 0,
        })
      },
      edit (record) {
        this.visible = true;
        this.$nextTick(() => {
          this.$refs.form.resetFields()
          this.model = Object.assign({}, record)
        })
      },
      close () {
        this.$emit('close');
        this.visible = false;
      },
      handleOk () {
        const that = this;
        // 触发表单验证
        this.$refs.form.validate((ok, err) => {
          if (ok) {
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

            console.log('提交参数',this.model)
            httpAction(httpurl,this.model,method).then((res)=>{
              if(res.success){
                that.$message.success(res.message);
                that.$emit('ok');
                that.close();
              }else{
                that.$message.warning(res.message);
              }
            }).finally(() => {
              that.confirmLoading = false;
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