<template>
  <div>
    <a-form :form="form">
      <a-row>

        <a-col :span="12">
          <a-form-item label="姓名" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input v-decorator="[ 'name', validatorRules.name]" placeholder="请输入姓名"></a-input>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="身份证" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input v-decorator="[ 'sfz', validatorRules.sfz]" placeholder="请输入身份证"></a-input>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="曾用名" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input v-decorator="[ 'oldName', validatorRules.oldName]" placeholder="请输入曾用名"></a-input>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="性别" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <j-dict-select-tag type="radio" v-decorator="['sex']" :trigger-change="true" dictCode="sex" placeholder="请选择性别"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="照片" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <j-upload v-decorator="['photo']" :trigger-change="true"></j-upload>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="出生日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <j-date placeholder="请选择出生日期" v-decorator="[ 'birthday', validatorRules.birthday]" :trigger-change="true" style="width: 100%"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="民族" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <j-dict-select-tag type="list" v-decorator="['zm']" :trigger-change="true" dictCode="minzu" placeholder="请选择民族"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="籍贯" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input v-decorator="[ 'jg', validatorRules.jg]" placeholder="请输入籍贯"></a-input>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="婚姻状况" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <j-dict-select-tag type="list" v-decorator="['hyzk']" :trigger-change="true" dictCode="hyzk" placeholder="请选择婚姻状况"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="政治面貌" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <j-dict-select-tag type="list" v-decorator="['zzmm']" :trigger-change="true" dictCode="zzmm" placeholder="请选择政治面貌"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="学历" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <j-dict-select-tag type="list" v-decorator="['xl']" :trigger-change="true" dictCode="education" placeholder="请选择学历"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="宗教信仰" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input v-decorator="[ 'zjxy', validatorRules.zjxy]" placeholder="请输入宗教信仰"></a-input>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="职业类别" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input v-decorator="[ 'zylb', validatorRules.zylb]" placeholder="请输入职业类别"></a-input>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="职业" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input v-decorator="[ 'zy', validatorRules.zy]" placeholder="请输入职业"></a-input>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="户籍门（楼）祥" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input v-decorator="[ 'hjxq', validatorRules.hjxq]" placeholder="请输入户籍门（楼）祥"></a-input>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="现住门（楼）祥" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input v-decorator="[ 'xzdxq', validatorRules.xzdxq]" placeholder="请输入现住门（楼）祥"></a-input>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="联系方式" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input v-decorator="[ 'lxfs', validatorRules.lxfs]" placeholder="请输入联系方式"></a-input>
          </a-form-item>
        </a-col>
      </a-row>
    </a-form>
  </div>
</template>
<script>
  import pick from 'lodash.pick'
  import { getAction } from '@/api/manage'
  import JDate from '@/components/jeecg/JDate'  
  import JUpload from '@/components/jeecg/JUpload'
  import JDictSelectTag from "@/components/dict/JDictSelectTag"

  export default {
    name: 'ZzPersonaForm',
    components: { 
      JDate,
      JUpload,
      JDictSelectTag,
    },
    data () {
      return {
        form: this.$form.createForm(this),
        model: {},
        labelCol: {
          span: 6
        },
        wrapperCol: {
          span: 16
        },
        labelCol2: {
          span: 3
        },
        wrapperCol2: {
          span: 20
        },
        confirmLoading: false,
        validatorRules:{
          name:{rules: [{ required: true, message: '请输入姓名!' }]},
          sfz:{},
          oldName:{},
          sex:{},
          photo:{},
          birthday:{},
          zm:{},
          jg:{},
          hyzk:{},
          zzmm:{},
          xl:{},
          zjxy:{},
          zylb:{},
          zy:{},
          hjxq:{},
          xzdxq:{},
          lxfs:{},
        },
        
      }
    },
    methods:{
      initFormData(url,id){
        this.clearFormData()
        if(!id){
          this.edit({})
        }else{
          getAction(url,{id:id}).then(res=>{
            if(res.success){
              let records = res.result
              if(records && records.length>0){
                this.edit(records[0])
              }
            }
          })
        }
      },
      edit(record){
        this.model = Object.assign({}, record)
        console.log("ZzPersonaForm-edit",this.model);
        let fieldval = pick(this.model,'name','sfz','oldName','sex','photo','birthday','zm','jg','hyzk','zzmm','xl','zjxy','zylb','zy','hjxq','xzdxq','lxfs')
        this.$nextTick(() => {
          this.form.setFieldsValue(fieldval)
        })
      },
      getFormData(){
        let formdata_arr = []
        this.form.validateFields((err, values) => {
          if (!err) {
            let formdata = Object.assign(this.model, values)
            let isNullObj = true
            Object.keys(formdata).forEach(key=>{
              if(formdata[key]){
                isNullObj = false
              }
            })
            if(!isNullObj){
              formdata_arr.push(formdata)
            }
          }else{
            this.$emit("validateError","综治人口表单校验未通过");
          }
        })
        console.log("综治人口表单数据集",formdata_arr);
        return formdata_arr;
      },
      popupCallback(row){
        this.form.setFieldsValue(pick(row,'name','sfz','oldName','sex','photo','birthday','zm','jg','hyzk','zzmm','xl','zjxy','zylb','zy','hjxq','xzdxq','lxfs'))
      },
      clearFormData(){
        this.form.resetFields()
        this.model={}
      }
    
    }
  }
</script>
