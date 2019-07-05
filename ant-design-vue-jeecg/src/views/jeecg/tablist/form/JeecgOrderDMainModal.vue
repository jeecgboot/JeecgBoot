<template>
  <a-modal
    :title="title"
    :width="1000"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel">

    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
        <!-- 主表单区域 -->
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="订单号"
          hasFeedback>
          <a-input
            placeholder="请输入订单号"
            v-decorator="['orderCode', {rules: [{ required: true, message: '请输入订单号!' }]}]"
          />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="订单类型">
          <a-select placeholder="请输入订单类型" v-decorator="['ctype',{}]">
            <a-select-option value="1">国内订单</a-select-option>
            <a-select-option value="2">国际订单</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="订单日期">
          <a-date-picker showTime format='YYYY-MM-DD HH:mm:ss' v-decorator="[ 'orderDate',{}]"/>
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="订单金额">
          <a-input-number style="width: 200px" v-decorator="[ 'orderMoney', {}]"/>
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="订单备注">
          <a-input placeholder="请输入订单备注" v-decorator="['content', {}]"/>
        </a-form-item>
      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>
  import {httpAction} from '@/api/manage'
  import JDate from '@/components/jeecg/JDate'
  import pick from 'lodash.pick'
  import moment from "moment"

  export default {
    name: "JeecgOrderDMainModal",
    components: {
      JDate
    },
    data() {
      return {
        title: "操作",
        visible: false,
        orderMainModel: {
          jeecgOrderCustomerList: [{}],
          jeecgOrderTicketList: [{}]
        },
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
        validatorRules: {},
        url: {
          add: "/test/order/add",
          edit: "/test/order/edit",
          orderCustomerList: "/test/order/listOrderCustomerByMainId",
          orderTicketList: "/test/order/listOrderTicketByMainId",
        },
      }
    },
    methods: {
      add() {
        this.edit({});
      },
      edit(record) {
        this.form.resetFields();
        this.orderMainModel = Object.assign({}, record);
        //初始化明细表数据
        console.log(this.orderMainModel.id)
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.orderMainModel, 'orderCode', 'ctype', 'orderMoney', 'content'))
          this.form.setFieldsValue({orderDate: this.orderMainModel.orderDate ? moment(this.orderMainModel.orderDate) : null}) //时间格式化
        });
        console.log(this.orderMainModel)
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
            if (!this.orderMainModel.id) {
              httpurl += this.url.add;
              method = 'post';
            } else {
              httpurl += this.url.edit;
              method = 'put';
            }
            let orderMainData = Object.assign(this.orderMainModel, values);
            //时间格式化
            orderMainData.orderDate = orderMainData.orderDate ? orderMainData.orderDate.format('YYYY-MM-DD HH:mm:ss') : null;
            let formData = {
              ...orderMainData
            }

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
      }
    }
  }
</script>

<style scoped>
  .ant-btn {
    padding: 0 10px;
    margin-left: 3px;
  }

  .ant-form-item-control {
    line-height: 0px;
  }

  /** 主表单行间距 */
  .ant-form .ant-form-item {
    margin-bottom: 10px;
  }

  /** Tab页面行间距 */
  .ant-tabs-content .ant-form-item {
    margin-bottom: 0px;
  }
</style>