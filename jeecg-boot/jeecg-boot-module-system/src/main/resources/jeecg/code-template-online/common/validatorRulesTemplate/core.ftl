<#include "../utils.ftl">
<#if po.isShow == 'Y' && poHasCheck(po)>
    <#if po.fieldName != 'id'>
           ${po.fieldName}: [
        <#assign fieldValidType = po.fieldValidType!''>
    <#-- 非空校验 -->
        <#if po.nullable == 'N' || fieldValidType == '*'>
              { required: true, message: '请输入${po.filedComment}!'},
        <#elseif fieldValidType!=''>
              { required: false},
        </#if>
    <#-- 唯一校验 -->
        <#if fieldValidType == 'only'>
              { validator: (rule, value, callback) => validateDuplicateValue(<#if sub?default("")?trim?length gt 1>'${sub.tableName}'<#else>'${tableName}'</#if>, '${po.fieldDbName}', value, this.model.id, callback)},
        <#-- 6到16位数字 -->
        <#elseif fieldValidType == 'n6-16'>
              { pattern: /^\d{6,16}$/, message: '请输入6到16位数字!'},
        <#-- 6到16位任意字符 -->
        <#elseif fieldValidType == '*6-16'>
              { pattern: /^.{6,16}$/, message: '请输入6到16位任意字符!'},
        <#-- 6到18位字符串 -->
        <#elseif fieldValidType == 's6-18'>
              { pattern: /^.{6,18}$/, message: '请输入6到18位任意字符!'},
        <#-- 网址 -->
        <#elseif fieldValidType == 'url'>
              { pattern: /^((ht|f)tps?):\/\/[\w\-]+(\.[\w\-]+)+([\w\-.,@?^=%&:\/~+#]*[\w\-@?^=%&\/~+#])?$/, message: '请输入正确的网址!'},
        <#-- 电子邮件 -->
        <#elseif fieldValidType == 'e'>
              { pattern: /^([\w]+\.*)([\w]+)@[\w]+\.\w{3}(\.\w{2}|)$/, message: '请输入正确的电子邮件!'},
        <#-- 手机号码 -->
        <#elseif fieldValidType == 'm'>
              { pattern: /^1[3456789]\d{9}$/, message: '请输入正确的手机号码!'},
        <#-- 邮政编码 -->
        <#elseif fieldValidType == 'p'>
              { pattern: /^[1-9]\d{5}$/, message: '请输入正确的邮政编码!'},
        <#-- 字母 -->
        <#elseif fieldValidType == 's'>
              { pattern: /^[A-Z|a-z]+$/, message: '请输入字母!'},
        <#-- 数字 -->
        <#elseif fieldValidType == 'n'>
              { pattern: /^-?\d+\.?\d*$/, message: '请输入数字!'},
        <#-- 整数 -->
        <#elseif fieldValidType == 'z'>
              { pattern: /^-?\d+$/, message: '请输入整数!'},
        <#-- 金额 -->
        <#elseif fieldValidType == 'money'>
              { pattern: /^(([1-9][0-9]*)|([0]\.\d{0,2}|[1-9][0-9]*\.\d{0,2}))$/, message: '请输入正确的金额!'},
        <#-- 正则校验 -->
        <#elseif fieldValidType != '' && fieldValidType != '*'>
              { pattern: '${fieldValidType}', message: '不符合校验规则!'},
        <#-- 无校验 -->
        <#else>
            <#t>
        </#if>
           ],
    </#if>
</#if>