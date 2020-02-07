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

        <a-form-item label="姓名" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'name', validatorRules.name]" placeholder="请输入姓名"></a-input>
        </a-form-item>
        <a-form-item label="身份证" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'sfz', validatorRules.sfz]" placeholder="请输入身份证"></a-input>
        </a-form-item>
        <a-form-item label="性别" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="radio" v-decorator="['sex', validatorRules.sex]" :trigger-change="true" dictCode="sex" placeholder="请选择性别"/>
        </a-form-item>
        <a-form-item label="手机号" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'phone', validatorRules.phone]" placeholder="请输入手机号"></a-input>
        </a-form-item>
        <a-form-item label="矫正人员编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'jzrybh', validatorRules.jzrybh]" placeholder="请输入矫正人员编号"></a-input>
        </a-form-item>
        <a-form-item label="原羁押场所" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'yjycs', validatorRules.yjycs]" placeholder="请输入原羁押场所"></a-input>
        </a-form-item>
        <a-form-item label="矫正类别" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['jzlb', validatorRules.jzlb]" :trigger-change="true" dictCode="jzlb" placeholder="请选择矫正类别"/>
        </a-form-item>
        <a-form-item label="案件类别" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'ajlb', validatorRules.ajlb]" placeholder="请输入案件类别"></a-input>
        </a-form-item>
        <a-form-item label="具体罪名" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'jtzm', validatorRules.jtzm]" placeholder="请输入具体罪名"></a-input>
        </a-form-item>
        <a-form-item label="原判刑期" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'ypxq', validatorRules.ypxq]" placeholder="请输入原判刑期"></a-input>
        </a-form-item>
        <a-form-item label="判刑开始日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择判刑开始日期" v-decorator="[ 'pxksDate', validatorRules.pxksDate]" :trigger-change="true" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="判刑结束日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择判刑结束日期" v-decorator="[ 'pxjsDate', validatorRules.pxjsDate]" :trigger-change="true" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="矫正开始日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择矫正开始日期" v-decorator="[ 'jzksDate', validatorRules.jzksDate]" :trigger-change="true" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="矫正结束日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择矫正结束日期" v-decorator="[ 'jzjsDate', validatorRules.jzjsDate]" :trigger-change="true" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="接受方式" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['jsfs', validatorRules.jsfs]" :trigger-change="true" dictCode="jsfs" placeholder="请选择接受方式"/>
        </a-form-item>
        <a-form-item label="“四史”情况" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['sshqk', validatorRules.sshqk]" :trigger-change="true" dictCode="sshqk" placeholder="请选择“四史”情况"/>
        </a-form-item>
        <a-form-item label="是否累惯犯" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['sflgf', validatorRules.sflgf]" :trigger-change="true" dictCode="yes_no" placeholder="请选择是否累惯犯"/>
        </a-form-item>
        <a-form-item label="三涉情况" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['ssqk', validatorRules.ssqk]" :trigger-change="true" dictCode="ssqk" placeholder="请选择三涉情况"/>
        </a-form-item>
        <a-form-item label="是否建矫正组" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['sfjjzz', validatorRules.sfjjzz]" :trigger-change="true" dictCode="yes_no" placeholder="请选择是否建矫正组"/>
        </a-form-item>
        <a-form-item label="人员组成情况" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['ryzcqk', validatorRules.ryzcqk]" :trigger-change="true" dictCode="ryzcqk" placeholder="请选择人员组成情况"/>
        </a-form-item>
        <a-form-item label="矫正解除类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['jzjclx', validatorRules.jzjclx]" :trigger-change="true" dictCode="jzjclx" placeholder="请选择矫正解除类型"/>
        </a-form-item>
        <a-form-item label="是否有托管" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['sfytg', validatorRules.sfytg]" :trigger-change="true" dictCode="yes_no" placeholder="请选择是否有托管"/>
        </a-form-item>
        <a-form-item label="托管原因" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'tgyy', validatorRules.tgyy]" placeholder="请输入托管原因"></a-input>
        </a-form-item>
        <a-form-item label="检查监督情况" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'tgJcjdqk', validatorRules.tgJcjdqk]" placeholder="请输入检查监督情况"></a-input>
        </a-form-item>
        <a-form-item label="托管纠正情况" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'tgjzqk', validatorRules.tgjzqk]" placeholder="请输入托管纠正情况"></a-input>
        </a-form-item>
        <a-form-item label="是否有漏管" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['sfylg', validatorRules.sfylg]" :trigger-change="true" dictCode="yes_no" placeholder="请选择是否有漏管"/>
        </a-form-item>
        <a-form-item label="漏管原因" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'lgyy', validatorRules.lgyy]" placeholder="请输入漏管原因"></a-input>
        </a-form-item>
        <a-form-item label="检查监督情况" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'lgJcjdqk', validatorRules.lgJcjdqk]" placeholder="请输入检查监督情况"></a-input>
        </a-form-item>
        <a-form-item label="漏管纠正情况" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'lgjzqk', validatorRules.lgjzqk]" placeholder="请输入漏管纠正情况"></a-input>
        </a-form-item>
        <a-form-item label="奖惩情况" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'jcqk', validatorRules.jcqk]" placeholder="请输入奖惩情况"></a-input>
        </a-form-item>
        <a-form-item label="刑罚执行情况" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'xfzxqk', validatorRules.xfzxqk]" placeholder="请输入刑罚执行情况"></a-input>
        </a-form-item>
        <a-form-item label="是否重新犯罪" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" v-decorator="['sfcxfz', validatorRules.sfcxfz]" :trigger-change="true" dictCode="yes_no" placeholder="请选择是否重新犯罪"/>
        </a-form-item>
        <a-form-item label="重新犯罪名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'cxfzmc', validatorRules.cxfzmc]" placeholder="请输入重新犯罪名称"></a-input>
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
    name: "ZzSqjzryModal",
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
          name: {rules: [
            {required: true, message: '请输入姓名!'},
          ]},
          sfz: {rules: [
            {required: true, message: '请输入身份证!'},
          ]},
          sex: {rules: [
            {required: true, message: '请输入性别!'},
          ]},
          phone: {rules: [
            {required: true, message: '请输入手机号!'},
            {pattern:/^1[3456789]\d{9}$/, message: '请输入正确的手机号码!'},
          ]},
          jzrybh: {rules: [
          ]},
          yjycs: {rules: [
          ]},
          jzlb: {rules: [
          ]},
          ajlb: {rules: [
          ]},
          jtzm: {rules: [
          ]},
          ypxq: {rules: [
          ]},
          pxksDate: {rules: [
          ]},
          pxjsDate: {rules: [
          ]},
          jzksDate: {rules: [
          ]},
          jzjsDate: {rules: [
          ]},
          jsfs: {rules: [
          ]},
          sshqk: {rules: [
          ]},
          sflgf: {rules: [
          ]},
          ssqk: {rules: [
          ]},
          sfjjzz: {rules: [
          ]},
          ryzcqk: {rules: [
          ]},
          jzjclx: {rules: [
          ]},
          sfytg: {rules: [
          ]},
          tgyy: {rules: [
          ]},
          tgJcjdqk: {rules: [
          ]},
          tgjzqk: {rules: [
          ]},
          sfylg: {rules: [
          ]},
          lgyy: {rules: [
          ]},
          lgJcjdqk: {rules: [
          ]},
          lgjzqk: {rules: [
          ]},
          jcqk: {rules: [
          ]},
          xfzxqk: {rules: [
          ]},
          sfcxfz: {rules: [
          ]},
          cxfzmc: {rules: [
          ]},
        },
        url: {
          add: "/sqjzry/zzSqjzry/add",
          edit: "/sqjzry/zzSqjzry/edit",
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
          this.form.setFieldsValue(pick(this.model,'name','sfz','sex','phone','jzrybh','yjycs','jzlb','ajlb','jtzm','ypxq','pxksDate','pxjsDate','jzksDate','jzjsDate','jsfs','sshqk','sflgf','ssqk','sfjjzz','ryzcqk','jzjclx','sfytg','tgyy','tgJcjdqk','tgjzqk','sfylg','lgyy','lgJcjdqk','lgjzqk','jcqk','xfzxqk','sfcxfz','cxfzmc'))
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
        this.form.setFieldsValue(pick(row,'name','sfz','sex','phone','jzrybh','yjycs','jzlb','ajlb','jtzm','ypxq','pxksDate','pxjsDate','jzksDate','jzjsDate','jsfs','sshqk','sflgf','ssqk','sfjjzz','ryzcqk','jzjclx','sfytg','tgyy','tgJcjdqk','tgjzqk','sfylg','lgyy','lgJcjdqk','lgjzqk','jcqk','xfzxqk','sfcxfz','cxfzmc'))
      },

      
    }
  }
</script>