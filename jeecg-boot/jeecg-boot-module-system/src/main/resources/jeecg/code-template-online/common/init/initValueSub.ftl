<#list sub.colums as po>
    <#if po.isShow == 'Y'>
        <#if po.fieldName != 'id'>
            <#if po.defaultVal??>
                <#if po.fieldDbType=="BigDecimal" || po.fieldDbType=="double" || po.fieldDbType=="int">
            ${po.fieldName}:${po.defaultVal},
                <#else>
            ${po.fieldName}:"${po.defaultVal}",
                </#if>
            </#if>
        </#if>
    </#if>
</#list>
