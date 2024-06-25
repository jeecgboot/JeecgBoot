<#list sub.colums as po>
 <#if po.isShow == 'Y' && po.fieldName != 'id'>
  <#if po.fieldDbType=='int' || po.fieldDbType=='double' || po.fieldDbType=='BigDecimal'>
        ${po.fieldName}: <#if po.defaultVal??>${po.defaultVal}<#else>undefined</#if>,
  <#elseif po.fieldDbType=='Blob'>
        ${po.fieldName}String: <#if po.defaultVal??>'${po.defaultVal}'<#else>''</#if>,
  <#else>
        ${po.fieldName}: <#if po.defaultVal??>'${po.defaultVal}'<#else>''</#if>,   
  </#if>
 </#if>
</#list>