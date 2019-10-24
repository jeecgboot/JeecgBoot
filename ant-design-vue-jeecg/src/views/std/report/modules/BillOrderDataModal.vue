<template>
  <a-drawer
      :title="title"
      :width="screenWidth"
      placement="right"
      :closable="false"
      @close="close"
      :visible="visible"
  >

    <a-spin :spinning="confirmLoading">
      <a-form :form="form">

        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="放行日期">
          <a-date-picker v-decorator="[ 'customsReleaseDate', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="实际到港日期">
          <a-date-picker v-decorator="[ 'actualShipDate', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="预计到港日期">
          <a-date-picker v-decorator="[ 'shippingDate', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="提单号">
          <a-input placeholder="请输入提单号" v-decorator="['blNo', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="货主(付款方)">
          <a-input placeholder="请输入货主(付款方)" v-decorator="['consignor', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="提箱地点">
          <a-input placeholder="请输入提箱地点" v-decorator="['takeContainerAddress', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="拖箱地点">
          <a-input placeholder="请输入拖箱地点" v-decorator="['dragContainerAddress', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="返箱地点">
          <a-input placeholder="请输入返箱地点" v-decorator="['returnContainerAddress', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="船东">
          <a-input placeholder="请输入船东" v-decorator="['shipowner', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="船代">
          <a-input placeholder="请输入船代" v-decorator="['shippingCompanyCode', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="大品名">
          <a-input placeholder="请输入大品名" v-decorator="['bigGoodsType', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="箱量">
          <a-input-number v-decorator="[ 'containerNum', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="箱型箱量">
          <a-input placeholder="请输入箱型箱量" v-decorator="['containerDesc', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="进出口类型">
          <a-input-number v-decorator="[ 'businessType', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="是否退载">
          <a-input-number v-decorator="[ 'takeBack', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="业务员">
          <a-input placeholder="请输入业务员" v-decorator="['servicePersonnel', {}]" />
        </a-form-item>
        <a-form-item
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          label="国家">
          <a-input placeholder="请输入国家" v-decorator="['country', {}]" />
        </a-form-item>

      </a-form>
    </a-spin>
    <a-button type="primary" @click="handleOk">确定</a-button>
    <a-button type="primary" @click="handleCancel">取消</a-button>
  </a-drawer>
</template>

<script>
  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import moment from "moment"

  export default {
    name: "BillOrderDataModal",
    data () {
      return {
        title:"操作",
        visible: false,
        screenWidth: '1800',
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
        },
        url: {
          add: "/org.jeecg.modules.dataReport/billOrderData/add",
          edit: "/org.jeecg.modules.dataReport/billOrderData/edit",
        },
      }
    },
    created () {
      // 当页面初始化时,根据屏幕大小来给抽屉设置宽度
      this.resetScreenSize();
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
          this.form.setFieldsValue(pick(this.model,'shippingDate','blNo','consignor','takeContainerAddress','dragContainerAddress','returnContainerAddress','shipowner','shippingCompanyCode','bigGoodsType','containerNum','containerDesc','businessType','takeBack','servicePersonnel','country'))
		  //时间格式化
          this.form.setFieldsValue({customsReleaseDate:this.model.customsReleaseDate?moment(this.model.customsReleaseDate):null})
          this.form.setFieldsValue({actualShipDate:this.model.actualShipDate?moment(this.model.actualShipDate):null})
          this.form.setFieldsValue({shippingDate:this.model.shippingDate?moment(this.model.shippingDate):null})
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
            formData.customsReleaseDate = formData.customsReleaseDate?formData.customsReleaseDate.format():null;
            formData.actualShipDate = formData.actualShipDate?formData.actualShipDate.format():null;
            formData.shippingDate = formData.shippingDate?formData.shippingDate.format():null;

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
      resetScreenSize() {
        debugger
        let screenWidth = document.body.clientWidth;
        if (screenWidth < 600) {
          this.screenWidth = screenWidth;
        } else {
          // this.screenWidth = screenWidth * 0.85;
          this.screenWidth = 600;
        }
      },


    }
  }
</script>

<style lang="less" scoped>
/** Button按钮间距 */
  .ant-btn {
    margin-left: 30px;
    margin-bottom: 30px;
    float: right;
  }
</style>