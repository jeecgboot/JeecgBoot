<template>
  <a-modal
    :title="title"
    :width="800"
    :visible="visible"
    :okButtonProps="{ props: {disabled: disableSubmit} }"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭">

    <a-spin :spinning="confirmLoading">
      <a-form :form="form">

        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="航班号"
          hasFeedback>
          <a-input
            placeholder="请输入航班号"
            :readOnly="disableSubmit"
            v-decorator="['ticketCode', {rules:[{ required: true,message: '请输入航班号!'}]}]"/>
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="航班时间"
          hasFeedback>
          <j-date v-decorator="['tickectDate',{trigger:'input',rules:[{ required: true,message: '请输入航班号!'}]}]"></j-date>
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="订单号码"
          v-model="this.orderId"
          :hidden="hiding"
          hasFeedback>
          <a-input v-decorator="[ 'orderId', {}]" disabled="disabled"/>
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="创建人"
          :hidden="hiding"
          hasFeedback>
          <a-input v-decorator="[ 'createBy', {}]" :readOnly="disableSubmit"/>
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="创建时间"
          :hidden="hiding"
          hasFeedback>
          <a-input v-decorator="[ 'createTime', {}]" :readOnly="disableSubmit"/>
        </a-form-item>
      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>
  import {httpAction} from '@/api/manage'
  import pick from 'lodash.pick'
  import moment from 'moment'
  import JDate from '@/components/jeecg/JDate'

  export default {
    components: {
      JDate
    },
    name: 'JeecgOrderTicketModal',
    data() {
      return {
        title: '操作',
        visible: false,
        model: {},
        labelCol: {
          xs: {span: 24},
          sm: {span: 5}
        },
        wrapperCol: {
          xs: {span: 24},
          sm: {span: 16}
        },
        moment,
        format: 'YYYY-MM-DD HH:mm:ss',
        disableSubmit: false,
        orderId: '',
        hiding: false,
        confirmLoading: false,
        form: this.$form.createForm(this),
        validatorRules: {},
        url: {
          add: '/test/order/addTicket',
          edit: '/test/order/editTicket'
        }
      }
    },
    created() {
    },
    methods: {
      add(orderId) {
        if (orderId) {
          this.edit({orderId}, '')
        } else {
          this.$message.warning('请选择一条航班数据')
        }
      },
      detail(record) {
        this.edit(record, 'd')
      },
      edit(record, v) {
        if (v == 'e') {
          this.hiding = false;
          this.disableSubmit = false;
        } else if (v == 'd') {
          this.hiding = false;
          this.disableSubmit = true;
        } else {
          this.hiding = true;
          this.disableSubmit = false;
        }
        this.form.resetFields();
        this.orderId = record.orderId;
        this.model = Object.assign({}, record);
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model, 'ticketCode', 'tickectDate', 'orderId', 'createBy', 'createTime', 'updateBy', 'updateTime'))
        })
      },
      close() {
        this.$emit('close');
        this.visible = false;
      },
      handleOk() {
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
            formData.mainId = this.orderId;
            httpAction(httpurl, formData, method).then((res) => {
              if (res.success) {
                that.$message.success(res.message);
                that.$emit('ok')
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
      }
    }
  }
</script>

<style scoped>

</style>