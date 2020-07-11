<#include "/common/utils.ftl">
<#list subTables as sub>
<#if sub.foreignRelationType=='1'>
#segment#${sub.entityName}Form.vue
<template>
  <j-form-container :disabled="disabled">
      <a-form :form="form" slot="detail">
        <a-row>
<#assign form_date = false>
<#assign form_select = false>
<#assign form_select_multi = false>
<#assign form_select_search = false>
<#assign form_popup = false>
<#assign form_sel_depart = false>
<#assign form_sel_user = false>
<#assign form_file = false>
<#assign form_image = false>
<#assign form_editor = false>
<#assign form_cat_tree = false>
<#assign form_cat_back = "">
<#assign form_pca = false>
<#assign form_md = false>
<#assign form_switch=false>
<#assign form_span = 24>
<#if tableVo.fieldRowNum == 2>
  <#assign form_span = 12>
<#elseif tableVo.fieldRowNum==3>
  <#assign form_span = 8>
<#elseif tableVo.fieldRowNum==4>
  <#assign form_span = 6>
</#if>
<#list sub.colums as po>
<#if po.isShow =='Y' && po.fieldName != 'id'>
<#assign form_field_dictCode="">
	<#if po.dictTable?default("")?trim?length gt 1>
		<#assign form_field_dictCode="${po.dictTable},${po.dictText},${po.dictField}">
	<#elseif po.dictField?default("")?trim?length gt 1>
		<#assign form_field_dictCode="${po.dictField}">
	</#if>
	<#if po.classType =='textarea'>
          <a-col :span="24">
            <a-form-item label="${po.filedComment}" :labelCol="labelCol2" :wrapperCol="wrapperCol2">
	<#else>
          <a-col :span="${form_span}">
            <a-form-item label="${po.filedComment}" :labelCol="labelCol" :wrapperCol="wrapperCol">
	</#if>
	<#if po.classType =='date'>
		<#assign form_date=true>
              <j-date placeholder="请选择${po.filedComment}" v-decorator="['${po.fieldName}'${autoWriteRules(po)}]" :trigger-change="true" style="width: 100%"/>
	<#elseif po.classType =='datetime'>
		<#assign form_date=true>
              <j-date placeholder="请选择${po.filedComment}" v-decorator="['${po.fieldName}'${autoWriteRules(po)}]" :trigger-change="true" :show-time="true" date-format="YYYY-MM-DD HH:mm:ss" style="width: 100%"/>
	<#elseif po.classType =='popup'>
		<#assign form_popup=true>
              <j-popup
                v-decorator="['${po.fieldName}'${autoWriteRules(po)}]"
                :trigger-change="true"
                org-fields="${po.dictField}"
                dest-fields="${Format.underlineToHump(po.dictText)}"
                code="${po.dictTable}"
                @callback="popupCallback"/>
    <#elseif po.classType =='sel_depart'>
		<#assign form_sel_depart=true>
              <j-select-depart v-decorator="['${po.fieldName}'${autoWriteRules(po)}]" multi/>
<#elseif po.classType =='switch'>
        <#assign form_switch=true>
              <j-switch v-decorator="['${po.fieldName}'${autoWriteRules(po)}]" <#if po.dictField?default("")?trim?length gt 1>:options="${po.dictField}"</#if>></j-switch>
	<#elseif po.classType =='pca'>
		<#assign form_pca=true>
		      <j-area-linkage type="cascader" v-decorator="['${po.fieldName}'${autoWriteRules(po)}]" placeholder="请输入省市区"/>
	<#elseif po.classType =='markdown'>
	    <#assign form_md=true>
	          <j-markdown-editor v-decorator="['${po.fieldName}']" id="${po.fieldName}"></j-markdown-editor>
    <#elseif po.classType =='password'>
	          <a-input-password v-decorator="['${po.fieldName}'${autoWriteRules(po)}]" placeholder="请输入${po.filedComment}"/>
	<#elseif po.classType =='sel_user'>
		<#assign form_sel_user = true>
              <j-select-user-by-dep v-decorator="['${po.fieldName}'${autoWriteRules(po)}]"/>
	<#elseif po.classType =='textarea'>
              <a-textarea v-decorator="['${po.fieldName}'${autoWriteRules(po)}]" rows="4" placeholder="请输入${po.filedComment}"/>
	<#elseif po.classType=='list' || po.classType=='radio'>
		<#assign form_select = true>
              <j-dict-select-tag type="${po.classType}" v-decorator="['${po.fieldName}'${autoWriteRules(po)}]" :trigger-change="true" dictCode="${form_field_dictCode}" placeholder="请选择${po.filedComment}"/>
	<#elseif po.classType=='list_multi' || po.classType=='checkbox'>
		<#assign form_select_multi = true>
              <j-multi-select-tag type="${po.classType}" v-decorator="['${po.fieldName}'${autoWriteRules(po)}]" :trigger-change="true" dictCode="${form_field_dictCode}" placeholder="请选择${po.filedComment}"/>
	<#elseif po.classType=='sel_search'>
    	<#assign form_select_search = true>
              <j-search-select-tag v-decorator="['${po.fieldName}'${autoWriteRules(po)}]" dict="${form_field_dictCode}" />
    <#elseif po.classType=='cat_tree'>
    	<#assign form_cat_tree = true>
              <j-category-select v-decorator="['${po.fieldName}'${autoWriteRules(po)}]" pcode="${po.dictField?default("")}" placeholder="请选择${po.filedComment}" <#if po.dictText?default("")?trim?length gt 1>back="${po.dictText}" @change="handleCategoryChange"</#if>/>
    	<#if po.dictText?default("")?trim?length gt 1>
    	<#assign form_cat_back = "${po.dictText}">
    	</#if>
	<#elseif po.fieldDbType=='int' || po.fieldDbType=='double' || po.fieldDbType=='BigDecimal'>
              <a-input-number v-decorator="['${po.fieldName}'${autoWriteRules(po)}]" placeholder="请输入${po.filedComment}" style="width: 100%"/>
	<#elseif po.classType=='file'>
		<#assign form_file = true>
              <j-upload v-decorator="['${po.fieldName}'${autoWriteRules(po)}]" :trigger-change="true"></j-upload>
	<#elseif po.classType=='image'>
	    <#assign form_image = true>
              <j-image-upload isMultiple v-decorator="['${po.fieldName}'${autoWriteRules(po)}]"></j-image-upload>
	<#elseif po.classType=='umeditor'>
        <#assign form_editor = true>
              <j-editor v-decorator="['${po.fieldName}',{trigger:'input'}]"/>
    <#elseif po.fieldDbType=='Blob'>
              <a-input v-decorator="['${po.fieldName}String'${autoWriteRules(po)}]" placeholder="请输入${po.filedComment}"></a-input>
	<#else>
              <a-input v-decorator="['${po.fieldName}'${autoWriteRules(po)}]" placeholder="请输入${po.filedComment}"></a-input>
    </#if>
            </a-form-item>
            <#if form_cat_tree && form_cat_back?length gt 1>
            <a-form-item v-show="false">
              <a-input v-decorator="['${form_cat_back}']"></a-input>
            </a-form-item>
            </#if>
          </a-col>
</#if>
</#list>
        </a-row>
      </a-form>
  </j-form-container>
</template>
<script>
  import pick from 'lodash.pick'
  import { getAction } from '@/api/manage'
  import { validateDuplicateValue } from '@/utils/util'
  import JFormContainer from '@/components/jeecg/JFormContainer'
  <#if form_date>
  import JDate from '@/components/jeecg/JDate'  
  </#if>
  <#if form_file>
  import JUpload from '@/components/jeecg/JUpload'
  </#if>
  <#if form_image>
  import JImageUpload from '@/components/jeecg/JImageUpload'
  </#if>
  <#if form_sel_depart>
  import JSelectDepart from '@/components/jeecgbiz/JSelectDepart'
  </#if>
  <#if form_sel_user>
  import JSelectUserByDep from '@/components/jeecgbiz/JSelectUserByDep'
  </#if>
  <#if form_select>
  import JDictSelectTag from "@/components/dict/JDictSelectTag"
  </#if>
  <#if form_select_multi>
  import JMultiSelectTag from "@/components/dict/JMultiSelectTag"
  </#if>
  <#if form_select_search>
  import JSearchSelectTag from '@/components/dict/JSearchSelectTag'
  </#if>
  <#if form_editor>
  import JEditor from '@/components/jeecg/JEditor'
  </#if>
  <#if form_cat_tree>
  import JCategorySelect from '@/components/jeecg/JCategorySelect'
  </#if>
  <#if form_pca>
  import JAreaLinkage from '@comp/jeecg/JAreaLinkage'
  </#if>
  <#if form_md>
  import JMarkdownEditor from '@/components/jeecg/JMarkdownEditor/index'
  </#if>
  <#if form_switch==true >
  import JSwitch from '@/components/jeecg/JSwitch'
  </#if>

  export default {
    name: '${sub.entityName}Form',
    components: {
      JFormContainer,
    <#if form_date>
      JDate,
    </#if>
    <#if form_file>
      JUpload,
    </#if>
    <#if form_image>
      JImageUpload,
    </#if>
    <#if form_sel_depart>
      JSelectDepart,
    </#if>
    <#if form_sel_user>
      JSelectUserByDep,
    </#if>
    <#if form_select>
      JDictSelectTag,
    </#if>
    <#if form_select_multi>
      JMultiSelectTag,
    </#if>
    <#if form_select_search>
      JSearchSelectTag,
    </#if>
    <#if form_editor>
      JEditor,
    </#if>
    <#if form_pca>
      JAreaLinkage,
    </#if>
    <#if form_cat_tree>
      JCategorySelect,
    </#if>
    <#if form_md>
      JMarkdownEditor,
    </#if>
    <#if form_switch==true >
      JSwitch,
    </#if>
    },
    props:{
      disabled: {
        type: Boolean,
        default: false,
        required: false
      }
    },
    data () {
      return {
        form: this.$form.createForm(this),
        model: {},
        labelCol: {
          xs: { span: 24 },
          sm: { span: 6 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        labelCol2: {
          xs: { span: 24 },
          sm: { span: 3 },
        },
        wrapperCol2: {
          xs: { span: 24 },
          sm: { span: 20 },
        },
        <#include "/common/validatorRulesTemplate/sub.ftl">
        confirmLoading: false,
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
        console.log("${sub.entityName}Form-edit",this.model);
        let fieldval = pick(this.model<#list sub.colums as po><#if po.fieldName !='id'>,'${po.fieldName}'</#if></#list>)
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
            this.$emit("validateError","${sub.ftlDescription}表单校验未通过");
          }
        })
        console.log("${sub.ftlDescription}表单数据集",formdata_arr);
        return formdata_arr;
      },
      popupCallback(row){
        this.form.setFieldsValue(pick(row<#list sub.colums as po><#if po.fieldName !='id'>,'${po.fieldName}'</#if></#list>))
      },
      clearFormData(){
        this.form.resetFields()
        this.model={}
      },
      <#if form_cat_tree>
      handleCategoryChange(value,backObj){
        this.form.setFieldsValue(backObj);
      }
      </#if>
    }
  }
</script>
</#if>
</#list>
