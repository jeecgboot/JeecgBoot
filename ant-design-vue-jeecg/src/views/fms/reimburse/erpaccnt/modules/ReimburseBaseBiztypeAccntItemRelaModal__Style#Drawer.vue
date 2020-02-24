<template>
  <a-drawer
    :title="title"
    :width="width"
    placement="right"
    :closable="false"
    @close="close"
    :visible="visible">
  
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">

        <a-form-item label="支出成本类型编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'costTypeCode', validatorRules.costTypeCode]" placeholder="请输入支出成本类型编号"></a-input>
        </a-form-item>
        <a-form-item label="支出成本类型名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'costTypeName', validatorRules.costTypeName]" placeholder="请输入支出成本类型名称"></a-input>
        </a-form-item>
        <a-form-item label="成本中心类型:1、基层，2、管理部门" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'costcenterType', validatorRules.costcenterType]" placeholder="请输入成本中心类型:1、基层，2、管理部门"></a-input>
        </a-form-item>
        <a-form-item label="业务大类代码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'bizTypeCode', validatorRules.bizTypeCode]" placeholder="请输入业务大类代码"></a-input>
        </a-form-item>
        <a-form-item label="业务大类名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'bizTypeName', validatorRules.bizTypeName]" placeholder="请输入业务大类名称"></a-input>
        </a-form-item>
        <a-form-item label="业务小类代码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'feeItemCode', validatorRules.feeItemCode]" placeholder="请输入业务小类代码"></a-input>
        </a-form-item>
        <a-form-item label="业务小类名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'feeItemName', validatorRules.feeItemName]" placeholder="请输入业务小类名称"></a-input>
        </a-form-item>
        <a-form-item label="会计科目代码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'accntCode', validatorRules.accntCode]" placeholder="请输入会计科目代码"></a-input>
        </a-form-item>
        <a-form-item label="会计科目名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'accntName', validatorRules.accntName]" placeholder="请输入会计科目名称"></a-input>
        </a-form-item>
        <a-form-item label="计提会计科目代码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'accrualAccntCode', validatorRules.accrualAccntCode]" placeholder="请输入计提会计科目代码"></a-input>
        </a-form-item>
        <a-form-item label="计提会计科目名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'accrualAccntName', validatorRules.accrualAccntName]" placeholder="请输入计提会计科目名称"></a-input>
        </a-form-item>
        <a-form-item label="负债类科目代码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'liabilitiesAccntCode', validatorRules.liabilitiesAccntCode]" placeholder="请输入负债类科目代码"></a-input>
        </a-form-item>
        <a-form-item label="负债类科目名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'liabilitiesAccntName', validatorRules.liabilitiesAccntName]" placeholder="请输入负债类科目名称"></a-input>
        </a-form-item>
        <a-form-item label="会计科目映射类别：0-普通，1-预付，2借款，3还款" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'accntMapType', validatorRules.accntMapType]" placeholder="请输入会计科目映射类别：0-普通，1-预付，2借款，3还款"></a-input>
        </a-form-item>
        <a-form-item label="reserve1" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'reserve1', validatorRules.reserve1]" placeholder="请输入reserve1"></a-input>
        </a-form-item>
        <a-form-item label="reserve2" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'reserve2', validatorRules.reserve2]" placeholder="请输入reserve2"></a-input>
        </a-form-item>
        <a-form-item label="reserve3" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'reserve3', validatorRules.reserve3]" placeholder="请输入reserve3"></a-input>
        </a-form-item>
        <a-form-item label="reserve4" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'reserve4', validatorRules.reserve4]" placeholder="请输入reserve4"></a-input>
        </a-form-item>
        <a-form-item label="reserve5" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'reserve5', validatorRules.reserve5]" placeholder="请输入reserve5"></a-input>
        </a-form-item>
        <a-form-item label="是否有效" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'enableFlag', validatorRules.enableFlag]" placeholder="请输入是否有效"></a-input>
        </a-form-item>
        <a-form-item label="生效开始时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择生效开始时间" v-decorator="[ 'startValidTime', validatorRules.startValidTime]" :trigger-change="true" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="生效结束时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择生效结束时间" v-decorator="[ 'endValidTime', validatorRules.endValidTime]" :trigger-change="true" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="创建时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择创建时间" v-decorator="[ 'createTime', validatorRules.createTime]" :trigger-change="true" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="创建人" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'createBy', validatorRules.createBy]" placeholder="请输入创建人"></a-input>
        </a-form-item>
        <a-form-item label="更新时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择更新时间" v-decorator="[ 'updateTime', validatorRules.updateTime]" :trigger-change="true" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="更新人" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'updateBy', validatorRules.updateBy]" placeholder="请输入更新人"></a-input>
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
  import { validateDuplicateValue } from '@/utils/util'
  import JDate from '@/components/jeecg/JDate'  
  
  export default {
    name: "ReimburseBaseBiztypeAccntItemRelaModal",
    components: { 
      JDate,
    },
    data () {
      return {
        form: this.$form.createForm(this),
        title:"操作",
        width:800,
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
        validatorRules: {
          costTypeCode: {rules: [
          ]},
          costTypeName: {rules: [
          ]},
          costcenterType: {rules: [
          ]},
          bizTypeCode: {rules: [
          ]},
          bizTypeName: {rules: [
          ]},
          feeItemCode: {rules: [
          ]},
          feeItemName: {rules: [
          ]},
          accntCode: {rules: [
          ]},
          accntName: {rules: [
          ]},
          accrualAccntCode: {rules: [
          ]},
          accrualAccntName: {rules: [
          ]},
          liabilitiesAccntCode: {rules: [
          ]},
          liabilitiesAccntName: {rules: [
          ]},
          accntMapType: {rules: [
          ]},
          reserve1: {rules: [
          ]},
          reserve2: {rules: [
          ]},
          reserve3: {rules: [
          ]},
          reserve4: {rules: [
          ]},
          reserve5: {rules: [
          ]},
          enableFlag: {rules: [
          ]},
          startValidTime: {rules: [
          ]},
          endValidTime: {rules: [
          ]},
          createTime: {rules: [
          ]},
          createBy: {rules: [
          ]},
          updateTime: {rules: [
          ]},
          updateBy: {rules: [
          ]},
        },
        url: {
          add: "/base/reimburseBaseBiztypeAccntItemRela/add",
          edit: "/base/reimburseBaseBiztypeAccntItemRela/edit",
        }
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
          this.form.setFieldsValue(pick(this.model,'costTypeCode','costTypeName','costcenterType','bizTypeCode','bizTypeName','feeItemCode','feeItemName','accntCode','accntName','accrualAccntCode','accrualAccntName','liabilitiesAccntCode','liabilitiesAccntName','accntMapType','reserve1','reserve2','reserve3','reserve4','reserve5','enableFlag','startValidTime','endValidTime','createTime','createBy','updateTime','updateBy'))
        })
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
            console.log("表单提交数据",formData)
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
      popupCallback(row){
        this.form.setFieldsValue(pick(row,'costTypeCode','costTypeName','costcenterType','bizTypeCode','bizTypeName','feeItemCode','feeItemName','accntCode','accntName','accrualAccntCode','accrualAccntName','liabilitiesAccntCode','liabilitiesAccntName','accntMapType','reserve1','reserve2','reserve3','reserve4','reserve5','enableFlag','startValidTime','endValidTime','createTime','createBy','updateTime','updateBy'))
      }
      
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