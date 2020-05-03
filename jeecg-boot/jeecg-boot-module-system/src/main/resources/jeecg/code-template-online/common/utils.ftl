<#---->
<#-- freemarker 的一些工具方法 -->
<#---->
<#-- 驼峰转其他字符 -->
<#-- @param str       待转换的文本 -->
<#-- @param character 要转换成的字符 -->
<#-- @param case      转换大小写（normal 不转换，lower 小写，upper 大写） -->
<#function camelToChar(str, character, case='normal')>
  <#assign text=str?replace("([a-z])([A-Z]+)","$1${character}$2","r")/>
  <#if case=="upper">
    <#return text?upper_case>
  <#elseif case=="lower">
    <#return text?lower_case>
  <#else>
    <#return text>
  </#if>
</#function>
<#---->
<#-- 驼峰转下划线 -->
<#function camelToDashed(str, case='normal')>
  <#return camelToChar(str, "_", case)>
</#function>
<#---->
<#-- 驼峰转横线 -->
<#function camelToHorizontal(str, case='normal')>
  <#return camelToChar(str, "-", case)>
</#function>
<#---->
<#-- 获取 v-model 属性 -->
<#function getVModel po,suffix="">
  <#return "v-model=\"queryParam.${po.fieldName}${suffix}\"">
</#function>
<#-- 获取 placeholder 属性 -->
<#function getPlaceholder po,prefix,fillComment=true>
  <#if fillComment>
    <#return "placeholder=\"${prefix}${po.filedComment}\"">
  <#else>
    <#return "placeholder=\"${prefix}\"">
  </#if>
</#function>
<#-- ** 判断某字段是否配置了校验 * -->
<#function poHasCheck po>
  <#if (po.fieldValidType!'')?trim?length gt 0 || po.nullable == 'N'>
    <#if po.fieldName != 'id'>
      <#if po.nullable == 'N'
      || po.fieldValidType == '*'
      || po.fieldValidType == 'only'
      || po.fieldValidType == 'n6-16'
      || po.fieldValidType == '*6-16'
      || po.fieldValidType == 's6-18'
      || po.fieldValidType == 'url'
      || po.fieldValidType == 'e'
      || po.fieldValidType == 'm'
      || po.fieldValidType == 'p'
      || po.fieldValidType == 's'
      || po.fieldValidType == 'n'
      || po.fieldValidType == 'z'
      || po.fieldValidType == 'money'
      >
        <#return true>
      </#if>
    </#if>
  </#if>
  <#return false>
</#function>
<#-- ** 如果配置了校验就显示 validatorRules * -->
<#function autoWriteRules po>
  <#if poHasCheck(po)>
    <#return ", validatorRules.${po.fieldName}">
  <#else>
    <#return "">
  </#if>
</#function>