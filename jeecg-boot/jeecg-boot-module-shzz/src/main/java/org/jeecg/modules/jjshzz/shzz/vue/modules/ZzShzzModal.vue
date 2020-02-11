<template>
  <a-modal
    :title="title"
    :width="width"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭">
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">

        <a-form-item label="统一社会信用代码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'shxyCode', validatorRules.shxyCode]" placeholder="请输入统一社会信用代码"></a-input>
        </a-form-item>
        <a-form-item label="登记证号" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'djCode', validatorRules.djCode]" placeholder="请输入登记证号"></a-input>
        </a-form-item>
        <a-form-item label="社会组织名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'shzzName', validatorRules.shzzName]" placeholder="请输入社会组织名称"></a-input>
        </a-form-item>
        <a-form-item label="登记管理机关代码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'gljgCode', validatorRules.gljgCode]" placeholder="请输入登记管理机关代码"></a-input>
        </a-form-item>
        <a-form-item label="法定代表人姓名" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'fddbrName', validatorRules.fddbrName]" placeholder="请输入法定代表人姓名"></a-input>
        </a-form-item>
        <a-form-item label="住所" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'adress', validatorRules.adress]" placeholder="请输入住所"></a-input>
        </a-form-item>
        <a-form-item label="批准日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择批准日期" v-decorator="[ 'pzDate', validatorRules.pzDate]" :trigger-change="true" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="社会组织类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'shzzType', validatorRules.shzzType]" placeholder="请输入社会组织类型"></a-input>
        </a-form-item>
        <a-form-item label="状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'status', validatorRules.status]" placeholder="请输入状态"></a-input>
        </a-form-item>
        <a-form-item label="负责人证件代码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'fzrDm', validatorRules.fzrDm]" placeholder="请输入负责人证件代码"></a-input>
        </a-form-item>
        <a-form-item label="负责人证件号码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'fzrHm', validatorRules.fzrHm]" placeholder="请输入负责人证件号码"></a-input>
        </a-form-item>
        <a-form-item label="负责人姓名" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'fzrName', validatorRules.fzrName]" placeholder="请输入负责人姓名"></a-input>
        </a-form-item>
        <a-form-item label="负责人联系方式" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'fzrPhone', validatorRules.fzrPhone]" placeholder="请输入负责人联系方式"></a-input>
        </a-form-item>
        <a-form-item label="办公地址" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'bgAdress', validatorRules.bgAdress]" placeholder="请输入办公地址"></a-input>
        </a-form-item>
        <a-form-item label="治保负责人姓名" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'fzfzrName', validatorRules.fzfzrName]" placeholder="请输入治保负责人姓名"></a-input>
        </a-form-item>
        <a-form-item label="治保负责人联系方式" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'zbfzrPhone', validatorRules.zbfzrPhone]" placeholder="请输入治保负责人联系方式"></a-input>
        </a-form-item>
        <a-form-item label="关注程度" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['gzcd', validatorRules.gzcd]" :trigger-change="true" dictCode="gzcd" placeholder="请选择关注程度"/>
        </a-form-item>
        <a-form-item label="是否具备建立中共党组织条件" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['sfjlzz', validatorRules.sfjlzz]" :trigger-change="true" dictCode="yes_no" placeholder="请选择是否具备建立中共党组织条件"/>
        </a-form-item>
        <a-form-item label="是否有中共党组织" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['sfzgdzz', validatorRules.sfzgdzz]" :trigger-change="true" dictCode="yes_no" placeholder="请选择是否有中共党组织"/>
        </a-form-item>
        <a-form-item label="中共党员数量" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'zgdyNum', validatorRules.zgdyNum]" placeholder="请输入中共党员数量" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="是否有工会" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['sfgh', validatorRules.sfgh]" :trigger-change="true" dictCode="yes_no" placeholder="请选择是否有工会"/>
        </a-form-item>
        <a-form-item label="工会会员数量" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'ghhyNum', validatorRules.ghhyNum]" placeholder="请输入工会会员数量" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="是否有共青团组织" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['sfgqtzz', validatorRules.sfgqtzz]" :trigger-change="true" dictCode="yes_no" placeholder="请选择是否有共青团组织"/>
        </a-form-item>
        <a-form-item label="共青团团员数量" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'gqttyNum', validatorRules.gqttyNum]" placeholder="请输入共青团团员数量" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="是否有妇联组织" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['sfflzz', validatorRules.sfflzz]" :trigger-change="true" dictCode="yes_no" placeholder="请选择是否有妇联组织"/>
        </a-form-item>
        <a-form-item label="妇女数量" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'womenNum', validatorRules.womenNum]" placeholder="请输入妇女数量" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="资金来源	" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'moneyLy', validatorRules.moneyLy]" placeholder="请输入资金来源	"></a-input>
        </a-form-item>
        <a-form-item label="是否有境外背景" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['sfjwbj', validatorRules.sfjwbj]" :trigger-change="true" dictCode="yes_no" placeholder="请选择是否有境外背景"/>
        </a-form-item>
        <a-form-item label="经度" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'longitude', validatorRules.longitude]" placeholder="请输入经度"></a-input>
        </a-form-item>
        <a-form-item label="纬度" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'latitude', validatorRules.latitude]" placeholder="请输入纬度"></a-input>
        </a-form-item>

      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>

  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'
  import JDate from '@/components/jeecg/JDate'  
  import JDictSelectTag from "@/components/dict/JDictSelectTag"

  export default {
    name: "ZzShzzModal",
    components: { 
      JDate,
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
          shxyCode: {rules: [
            {required: true, message: '请输入统一社会信用代码!'},
          ]},
          djCode: {rules: [
            {required: true, message: '请输入登记证号!'},
          ]},
          shzzName: {rules: [
            {required: true, message: '请输入社会组织名称!'},
          ]},
          gljgCode: {rules: [
            {required: true, message: '请输入登记管理机关代码!'},
          ]},
          fddbrName: {rules: [
            {required: true, message: '请输入法定代表人姓名!'},
          ]},
          adress: {rules: [
          ]},
          pzDate: {rules: [
          ]},
          shzzType: {rules: [
          ]},
          status: {rules: [
          ]},
          fzrDm: {rules: [
          ]},
          fzrHm: {rules: [
          ]},
          fzrName: {rules: [
          ]},
          fzrPhone: {rules: [
          ]},
          bgAdress: {rules: [
          ]},
          fzfzrName: {rules: [
          ]},
          zbfzrPhone: {rules: [
          ]},
          gzcd: {rules: [
            {required: true, message: '请输入关注程度!'},
          ]},
          sfjlzz: {rules: [
          ]},
          sfzgdzz: {rules: [
          ]},
          zgdyNum: {rules: [
          ]},
          sfgh: {rules: [
          ]},
          ghhyNum: {rules: [
          ]},
          sfgqtzz: {rules: [
          ]},
          gqttyNum: {rules: [
          ]},
          sfflzz: {rules: [
          ]},
          womenNum: {rules: [
          ]},
          moneyLy: {rules: [
          ]},
          sfjwbj: {rules: [
          ]},
          longitude: {rules: [
          ]},
          latitude: {rules: [
          ]},
        },
        url: {
          add: "/shzz/zzShzz/add",
          edit: "/shzz/zzShzz/edit",
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
          this.form.setFieldsValue(pick(this.model,'shxyCode','djCode','shzzName','gljgCode','fddbrName','adress','pzDate','shzzType','status','fzrDm','fzrHm','fzrName','fzrPhone','bgAdress','fzfzrName','zbfzrPhone','gzcd','sfjlzz','sfzgdzz','zgdyNum','sfgh','ghhyNum','sfgqtzz','gqttyNum','sfflzz','womenNum','moneyLy','sfjwbj','longitude','latitude'))
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
        this.form.setFieldsValue(pick(row,'shxyCode','djCode','shzzName','gljgCode','fddbrName','adress','pzDate','shzzType','status','fzrDm','fzrHm','fzrName','fzrPhone','bgAdress','fzfzrName','zbfzrPhone','gzcd','sfjlzz','sfzgdzz','zgdyNum','sfgh','ghhyNum','sfgqtzz','gqttyNum','sfflzz','womenNum','moneyLy','sfjwbj','longitude','latitude'))
      },

      
    }
  }
</script>