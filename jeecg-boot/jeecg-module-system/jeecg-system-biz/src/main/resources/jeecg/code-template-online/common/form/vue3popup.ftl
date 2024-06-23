<#assign sourceFields = po.dictField?default("")?trim?split(",")/>
<#assign targetFields = po.dictText?default("")?trim?split(",")/>
    component: 'JPopup',
    componentProps: ({ formActionType }) => {
        const {setFieldsValue} = formActionType;
        return{
            setFieldsValue:setFieldsValue,
            code:"${po.dictTable}",
            fieldConfig: [
                <#list sourceFields as fieldName>
                { source: '${fieldName}', target: '${dashedToCamel(targetFields[fieldName_index])}' },
                </#list>
            ],
            multi:${po.extendParams.popupMulti?c}
        }
    },

