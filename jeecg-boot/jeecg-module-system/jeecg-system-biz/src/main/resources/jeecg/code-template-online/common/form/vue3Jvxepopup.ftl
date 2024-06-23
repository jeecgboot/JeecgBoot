<#assign sourceFields = col.dictField?default("")?trim?split(",")/>
<#assign targetFields = col.dictText?default("")?trim?split(",")/>
      type: JVxeTypes.popup,
      popupCode:"${col.dictTable}",
      fieldConfig: [
    <#list sourceFields as fieldName>
        { source: '${fieldName}', target: '${dashedToCamel(targetFields[fieldName_index])}' },
    </#list>
      ],
    <#if col.readonly=='Y'>
        disabled:true,
    </#if>

