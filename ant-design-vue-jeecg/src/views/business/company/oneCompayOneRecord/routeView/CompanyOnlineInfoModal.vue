<template>
  <j-modal
    :title="title"
    :width="width"
    :visible="visible"
    :confirmLoading="confirmLoading"
    switchFullscreen
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭">
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">

        <a-form-item label="在线设备名称/型号" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['equipmentName', validatorRules.equipmentName]" placeholder="请输入在线设备名称/型号" :disabled="disableSubmit"></a-input>
        </a-form-item>
        <a-form-item label="设备生产厂家" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['equipmentManufacturers']" placeholder="请输入设备生产厂家" :disabled="disableSubmit"></a-input>
        </a-form-item>
        <a-form-item label="运维单位" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['operationalUnit']" placeholder="请输入运维单位" :disabled="disableSubmit"></a-input>
        </a-form-item>
        <a-form-item label="投入使用日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择投入使用日期" v-decorator="['usedTime']" :trigger-change="true" style="width: 100%" :disabled="disableSubmit"/>
        </a-form-item>
        <a-form-item label="安装位置" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="['installLocation']" placeholder="请输入安装位置" :disabled="disableSubmit"></a-input>
        </a-form-item>
        <a-form-item label="在线监控验收材料" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-upload v-decorator="['files']" :trigger-change="true" :disabled="disableSubmit"></j-upload>
        </a-form-item>

      </a-form>
    </a-spin>
    <template slot="footer">
      <a-button type="primary" @click="handleCancel">取消</a-button>
      <a-button type="primary" @click="handleOk"  v-if="!disableSubmit">暂存</a-button>
      <a-button type="primary" @click="handDeclare"  v-if="!disableSubmit">申报</a-button>
    </template>
  </j-modal>
</template>

<script>

  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'
  import JDate from '@/components/jeecg/JDate'  
  import JUpload from '@/components/jeecg/JUpload'


  export default {
    name: "CompanyOnlineInfoModal",
    components: { 
      JDate,
      JUpload,
    },
    data () {
      return {
        form: this.$form.createForm(this),
        disableSubmit: false,
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
          equipmentName: {
            rules: [
              { required: true, message: '请输入在线设备名称/型号!'},
            ]
          },
        },
        url: {
          add: "/onlineInfo/companyOnlineInfo/add",
          edit: "/onlineInfo/companyOnlineInfo/edit",
          declare: "/onlineInfo/companyOnlineInfo/declare",
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
          this.form.setFieldsValue(pick(this.model,'equipmentName','equipmentManufacturers','operationalUnit','usedTime','installLocation','files'))
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
            formData.companyId = this.companyId;
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
      //申报
      handDeclare() {
        const that = this;
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
            that.confirmLoading = true;
            let httpUrl = this.url.declare;
            let method = 'put';
            let formData = Object.assign(this.model, values);
            formData.companyId = this.companyId;
            httpAction(httpUrl, formData, method).then((res) => {
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
      handleCancel () {
        this.close()
      },
      popupCallback(row){
        this.form.setFieldsValue(pick(row,'equipmentName','equipmentManufacturers','operationalUnit','usedTime','installLocation','files'))
      },

      
    },
    props: {
      companyId: ""
    }
  }
</script>