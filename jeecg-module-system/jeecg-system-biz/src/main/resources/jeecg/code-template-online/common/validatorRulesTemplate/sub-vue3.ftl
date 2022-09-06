<#include "../utils.ftl">
    <#if col.isShow == 'Y' && poHasCheck(col)>
        validateRules: [
        <#if col.fieldName != 'id'>
            <#assign subFieldValidType = col.fieldValidType!''>
        <#-- 非空校验 -->
            <#if col.nullable == 'N' || subFieldValidType == '*'>
          { required: true, message: '${'$'}{title}不能为空' },
            <#elseif subFieldValidType!=''>
          { required: false},
            </#if>
        <#-- 其他情况下，只要有值就被认为是正则校验 -->
            <#if subFieldValidType?length gt 0>
            <#assign subMessage = '格式不正确'>
            <#if subFieldValidType == 'only' >
                <#assign subMessage = '不能重复'>
            </#if>
          { pattern: "${subFieldValidType}", message: "${'$'}{title}${subMessage}" }
                <#t>
            </#if>
        </#if>
        ],
    </#if>
