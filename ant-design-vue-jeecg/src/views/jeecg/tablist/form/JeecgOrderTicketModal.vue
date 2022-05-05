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
      <a-form-model ref="form" :model="model" :rules="validatorRules">

        <a-form-model-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="航班号"
          prop="ticketCode"
          hasFeedback>
          <a-input placeholder="请输入航班号" :readOnly="disableSubmit" v-model="model.ticketCode"></a-input>
        </a-form-model-item>
        <a-form-model-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="航班时间"
          prop="tickectDate"
          hasFeedback>
          <j-date v-model="model.tickectDate"></j-date>
        </a-form-model-item>
        <a-form-model-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="订单号码"
          v-model="this.orderId"
          :hidden="hiding"
          hasFeedback>
          <a-input v-model="model.orderId" disabled="disabled"/>
        </a-form-model-item>
        <a-form-model-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="创建人"
          :hidden="hiding"
          hasFeedback>
          <a-input v-model="model.createBy" :readOnly="disableSubmit"/>
        </a-form-model-item>
        <a-form-model-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="创建时间"
          :hidden="hiding"
          hasFeedback>
          <a-input v-model="model.createTime" :readOnly="disableSubmit"/>
        </a-form-model-item>
      </a-form-model>
    </a-spin>
  </a-modal>
</template>

<script>
  import {httpAction} from '@/api/manage'
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
        validatorRules: {
          ticketCode:[{required : true, message: '请输入航班号!'}],
          tickectDate:[{required : true, message: '请输入航班时间!'}]
        },
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
        this.model = Object.assign({}, record);
        this.visible = true;
      },
      close() {
        this.$emit('close');
        this.visible = false;
        this.$refs.form.resetFields();
      },
      handleOk() {
        const that = this;
        // 触发表单验证
        this.$refs.form.validate(valid => {
          if (valid) {
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
            this.model.mainId = this.model.orderId;
            httpAction(httpurl, this.model, method).then((res) => {
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
          }else{
            return false;
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