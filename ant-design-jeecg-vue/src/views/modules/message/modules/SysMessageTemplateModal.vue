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
          label="模板CODE">
          <a-input
            :disabled="disable"
            v-decorator="['templateCode', {} ]"
          />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="模板标题">
          <a-input
            placeholder="请输入模板标题"
            v-decorator="['templateName', validatorRules.templateName]"
          />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="模板内容">
          <a-textarea
            placeholder="请输入模板内容"
            v-decorator="['templateContent', validatorRules.templateContent ]"
            :autosize="{ minRows: 8, maxRows: 8 }"
          />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="模板类型">
          <j-dict-select-tag :triggerChange="true" dictCode="msgType" v-decorator="['templateType', validatorRules.templateType ]" placeholder="请选择模板类型">
          </j-dict-select-tag>
        </a-form-item>
      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>
  import {httpAction} from '@/api/manage'
  import pick from 'lodash.pick'

  export default {
    name: "SysMessageTemplateModal",
    data() {
      return {
        title: "操作",
        visible: false,
        disable: true,
        model: {},
        labelCol: {
          xs: {span: 24},
          sm: {span: 5},
        },
        wrapperCol: {
          xs: {span: 24},
          sm: {span: 16},
        },
        confirmLoading: false,
        form: this.$form.createForm(this),
        validatorRules: {
          templateName: {rules: [{required: true, message: '请输入模板标题!'}]},
          templateContent: {rules: [{required: true, message: '请输入模板内容!'}]},
          templateType: {rules: [{required: true, message: '请输入模板类型!'}]},
        },
        url: {
          add: "/message/sysMessageTemplate/add",
          edit: "/message/sysMessageTemplate/edit",
        },
      }
    },
    created() {
    },
    methods: {
      add() {
        this.disable = false;
        this.edit({});
      },
      edit(record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model, 'templateCode', 'templateContent', 'templateName', 'templateTestJson', 'templateType'))
          //时间格式化
        });

      },
      close() {
        this.$emit('close');
        this.visible = false;
        this.disable = true;
      },
      handleOk() {
        this.model.templateType = this.templateType;
        const that = this;
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
            that.confirmLoading = true;
            let httpurl = '';
            let method = '';
            if (!this.model.id) {
              httpurl += this.url.add;
              method = 'post';
            } else {
              httpurl += this.url.edit;
              method = 'put';
            }
            let formData = Object.assign(this.model, values);
            //时间格式化

            console.log(formData)
            httpAction(httpurl, formData, method).then((res) => {
              if (res.success) {
                that.$message.success(res.message);
                that.$emit('ok');
              } else {
                that.$message.warning(res.message);
              }
            }).finally(() => {
              that.confirmLoading = false;
              that.close();
            })


          }
        })
      },
      handleCancel() {
        this.close()
      },


    }
  }
</script>

<style scoped>

</style>