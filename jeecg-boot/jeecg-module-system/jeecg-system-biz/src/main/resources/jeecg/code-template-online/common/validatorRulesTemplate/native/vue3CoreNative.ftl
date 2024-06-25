<#assign fieldValidType = po.fieldValidType!''>
<#-- 非空校验 -->
<#if po.nullable == 'N' || fieldValidType == '*'>
{ required: true, message: '请输入${po.filedComment}!'}<#rt>,
<#elseif fieldValidType!=''>
{ required: false}<#rt>,
</#if>
<#-- 唯一校验 -->
<#if fieldValidType == 'only'>
 { validator: ${po.fieldName}Duplicatevalidate }<#rt>
<#-- 6到16位数字 -->
<#elseif fieldValidType == 'n6-16'>
 { pattern: /^\d{6,16}$|^(?=\d+\.\d+)[\d.]{7,17}$/, message: '请输入6到16位数字!'}<#rt>,
<#-- 6到16位任意字符 -->
<#elseif fieldValidType == '*6-16'>
 { pattern: /^.{6,16}$/, message: '请输入6到16位任意字符!'}<#rt>,
<#-- 6到18位字符串 -->
<#elseif fieldValidType == 's6-18'>
 { pattern: /^[a-z|A-Z]{6,18}$/, message: '请输入6到18位字母!'},<#rt>,
<#-- 网址 -->
<#elseif fieldValidType == 'url'>
 { pattern: /^((ht|f)tps?):\/\/[\w\-]+(\.[\w\-]+)+([\w\-.,@?^=%&:\/~+#]*[\w\-@?^=%&\/~+#])?$/, message: '请输入正确的网址!'}<#rt>,
<#-- 电子邮件 -->
<#elseif fieldValidType == 'e'>
 { pattern: /^([\w]+\.*)([\w]+)@[\w]+\.\w{3}(\.\w{2}|)$/, message: '请输入正确的电子邮件!'}<#rt>,
<#-- 手机号码 -->
<#elseif fieldValidType == 'm'>
 { pattern: /^1[3456789]\d{9}$/, message: '请输入正确的手机号码!'}<#rt>,
<#-- 邮政编码 -->
<#elseif fieldValidType == 'p'>
 { pattern: /^[0-9]\d{5}$/, message: '请输入正确的邮政编码!'}<#rt>,
<#-- 字母 -->
<#elseif fieldValidType == 's'>
 { pattern: /^[A-Z|a-z]+$/, message: '请输入字母!'}<#rt>,
<#-- 数字 -->
<#elseif fieldValidType == 'n'>
 { pattern: /^-?\d+\.?\d*$/, message: '请输入数字!'}<#rt>,
<#-- 整数 -->
<#elseif fieldValidType == 'z'>
 { pattern: /^-?\d+$/, message: '请输入整数!'}<#rt>,
<#-- 金额 -->
<#elseif fieldValidType == 'money'>
 { pattern: /^(([1-9][0-9]*)|([0]\.\d{0,2}|[1-9][0-9]*\.\d{0,2}))$/, message: '请输入正确的金额!'}<#rt>,
<#-- 正则校验 -->
<#elseif fieldValidType != '' && fieldValidType != '*'>
 { pattern: '${fieldValidType}', message: '不符合校验规则!'}<#rt>,
<#else>
	<#t>
</#if>