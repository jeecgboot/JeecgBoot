<#include "../../utils.ftl">
<#list columns as po>
  <#if po.isShow == 'Y' && poHasCheck(po)>
    ${po.fieldName}: [<#include "vue3CoreNative.ftl">],
  </#if>
</#list>