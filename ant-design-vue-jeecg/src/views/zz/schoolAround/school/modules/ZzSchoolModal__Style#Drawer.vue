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

        <a-form-item label="名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'name', validatorRules.name]" placeholder="请输入名称"></a-input>
        </a-form-item>
        <a-form-item label="地址" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'addr', validatorRules.addr]" placeholder="请输入地址"></a-input>
        </a-form-item>
        <a-form-item label="办学类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['bxlx', validatorRules.bxlx]" :trigger-change="true" dictCode="bxlx" placeholder="请选择办学类型"/>
        </a-form-item>
        <a-form-item label="主管部门" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'zgOrg', validatorRules.zgOrg]" placeholder="请输入主管部门"></a-input>
        </a-form-item>
        <a-form-item label="校长姓名" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'xzName', validatorRules.xzName]" placeholder="请输入校长姓名"></a-input>
        </a-form-item>
        <a-form-item label="联系方式" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'lxfs', validatorRules.lxfs]" placeholder="请输入联系方式"></a-input>
        </a-form-item>
        <a-form-item label="学生人数" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'stuNum', validatorRules.stuNum]" placeholder="请输入学生人数" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="安保人数" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'abNum', validatorRules.abNum]" placeholder="请输入安保人数" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="分管安保责任人" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'fgabZrr', validatorRules.fgabZrr]" placeholder="请输入分管安保责任人"></a-input>
        </a-form-item>
        <a-form-item label="联系方式" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'fgabLxfs', validatorRules.fgabLxfs]" placeholder="请输入联系方式"></a-input>
        </a-form-item>
        <a-form-item label="安保责任人" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'abZrr', validatorRules.abZrr]" placeholder="请输入安保责任人"></a-input>
        </a-form-item>
        <a-form-item label="联系方式" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'abLxfs', validatorRules.abLxfs]" placeholder="请输入联系方式"></a-input>
        </a-form-item>
        <a-form-item label="治安责任人" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'zaZrr', validatorRules.zaZrr]" placeholder="请输入治安责任人"></a-input>
        </a-form-item>
        <a-form-item label="联系方式" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'zaLxfs', validatorRules.zaLxfs]" placeholder="请输入联系方式"></a-input>
        </a-form-item>
        <a-form-item label="经度" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'longitude', validatorRules.longitude]" placeholder="请输入经度"></a-input>
        </a-form-item>
        <a-form-item label="纬度" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'latitude', validatorRules.latitude]" placeholder="请输入纬度"></a-input>
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
  import JDictSelectTag from "@/components/dict/JDictSelectTag"
  
  export default {
    name: "ZzSchoolModal",
    components: { 
      JDictSelectTag,
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
          name: {rules: [
            {required: true, message: '请输入名称!'},
          ]},
          addr: {rules: [
            {required: true, message: '请输入地址!'},
          ]},
          bxlx: {rules: [
            {required: true, message: '请输入办学类型!'},
          ]},
          zgOrg: {rules: [
          ]},
          xzName: {rules: [
          ]},
          lxfs: {rules: [
          ]},
          stuNum: {rules: [
          ]},
          abNum: {rules: [
          ]},
          fgabZrr: {rules: [
          ]},
          fgabLxfs: {rules: [
          ]},
          abZrr: {rules: [
          ]},
          abLxfs: {rules: [
          ]},
          zaZrr: {rules: [
          ]},
          zaLxfs: {rules: [
          ]},
          longitude: {rules: [
          ]},
          latitude: {rules: [
          ]},
        },
        url: {
          add: "/school/zzSchool/add",
          edit: "/school/zzSchool/edit",
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
          this.form.setFieldsValue(pick(this.model,'name','addr','bxlx','zgOrg','xzName','lxfs','stuNum','abNum','fgabZrr','fgabLxfs','abZrr','abLxfs','zaZrr','zaLxfs','longitude','latitude'))
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
        this.form.setFieldsValue(pick(row,'name','addr','bxlx','zgOrg','xzName','lxfs','stuNum','abNum','fgabZrr','fgabLxfs','abZrr','abLxfs','zaZrr','zaLxfs','longitude','latitude'))
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