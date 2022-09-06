<#if po.isQuery=='Y'>
<#assign query_flag=true>
	<#if query_field_no==2>
          <template v-if="toggleSearchStatus">
	</#if>
	<#assign query_field_dictCode="">
	<#if po.dictTable?default("")?trim?length gt 1>
	    <#assign need_select_tag = true>
	    <#assign query_field_dictCode="${po.dictTable},${po.dictText},${po.dictField}">
	<#elseif po.dictField?default("")?trim?length gt 1>
	    <#assign need_select_tag = true>
	    <#assign query_field_dictCode="${po.dictField}">
	</#if>
	<#if po.queryMode=='single'>
          <#if query_field_no gt 1>  </#if><a-col :lg="8">
            <#if query_field_no gt 1>  </#if><a-form-item label="${po.filedComment}">
            <#if po.classType=='sel_search'>
              <#if query_field_no gt 1>  </#if><j-search-select placeholder="请选择${po.filedComment}" v-model:value="queryParam.${po.fieldName}" dict="${po.dictTable},${po.dictText},${po.dictField}" />
            <#elseif po.classType=='sel_user'>
              <#if query_field_no gt 1>  </#if><j-select-user-by-dept placeholder="请选择${po.filedComment}" v-model:value="queryParam.${po.fieldName}" />
            <#elseif po.classType=='switch'>
              <#if query_field_no gt 1>  </#if><j-switch placeholder="请选择${po.filedComment}" v-model:value="queryParam.${po.fieldName}" <#if po.dictField!= 'is_open'>:options="${po.dictField}"</#if> query />
            <#elseif po.classType=='sel_depart'>
              <#if query_field_no gt 1>  </#if><j-select-dept placeholder="请选择${po.filedComment}" v-model:value="queryParam.${po.fieldName}" checkStrictly />
            <#elseif po.classType=='list_multi'>
              <#if query_field_no gt 1>  </#if><j-select-multiple placeholder="请选择${po.filedComment}" dictCode="${query_field_dictCode?default("")}" v-model:value="queryParam.${po.fieldName}" />
            <#elseif po.classType=='cat_tree'>
              <#if query_field_no gt 1>  </#if><j-category-select placeholder="请选择${po.filedComment}" v-model:value="queryParam.${po.fieldName}" pcode="${po.dictField?default("")}" @change="(value) => handleFormChange('${po.fieldName}', value)" />
            <#elseif po.classType=='date'>
              <#if query_field_no gt 1>  </#if><a-date-picker valueFormat="YYYY-MM-DD" placeholder="请选择${po.filedComment}" v-model:value="queryParam.${po.fieldName}" />
            <#elseif po.classType=='datetime'>
              <#if query_field_no gt 1>  </#if><a-date-picker showTime valueFormat="YYYY-MM-DD HH:mm:ss" placeholder="请选择${po.filedComment}" v-model:value="queryParam.${po.fieldName}" />
            <#elseif po.classType=='pca'>
              <#if query_field_no gt 1>  </#if><j-area-linkage v-model:value="queryParam.${po.fieldName}" placeholder="请选择${po.filedComment}" @change="(value) => handleAreaChange('${po.fieldName}', value)" /> 
            <#elseif po.classType=='sel_tree'>
              <#if query_field_no gt 1>  </#if><j-tree-select v-model:value="queryParam.${po.fieldName}" placeholder="请选择${po.filedComment}" <#if po.dictText??><#if po.dictText?split(',')[2]?? && po.dictText?split(',')[0]??>dict="${po.dictTable},${po.dictText?split(',')[2]},${po.dictText?split(',')[0]}" <#elseif po.dictText?split(',')[1]??>pidField:"${po.dictText?split(',')[1]}", <#elseif po.dictText?split(',')[3]??>hasChildField:"${po.dictText?split(',')[3]}"</#if> </#if>pidValue="${po.dictField}" />
            <#elseif po.classType=='popup'>
              <#assign sourceFields = po.dictField?default("")?trim?split(",")/>
              <#assign targetFields = po.dictText?default("")?trim?split(",")/>
              <#if query_field_no gt 1>  </#if><j-popup
                <#if query_field_no gt 1>  </#if>placeholder="请选择${po.filedComment}" 
                <#if query_field_no gt 1>  </#if>v-model:value="queryParam.${po.fieldName}" 
                <#if query_field_no gt 1>  </#if>code="${po.dictTable}"
                <#if query_field_no gt 1>  </#if>:fieldConfig="[
                <#list sourceFields as fieldName>
                  <#if query_field_no gt 1>  </#if>{ source: '${fieldName}', target: '${targetFields[fieldName_index]}' },
                </#list>
                <#if query_field_no gt 1>  </#if>]"
                <#if query_field_no gt 1>  </#if>:multi="${po.extendParams.popupMulti?c}"
                <#if query_field_no gt 1>  </#if>:setFieldsValue="setFieldsValue" />
            <#elseif po.classType=='list' || po.classType=='radio' || po.classType=='checkbox'>
            <#--  ---------------------------下拉或是单选 判断数据字典是表字典还是普通字典------------------------------- -->
            <#if po.dictTable?default("")?trim?length gt 1>
              <#if query_field_no gt 1>  </#if><j-dict-select-tag placeholder="请选择${po.filedComment}" v-model:value="queryParam.${po.fieldName}" dictCode="${po.dictTable},${po.dictText},${po.dictField}"/>
            <#elseif po.dictField?default("")?trim?length gt 1>
              <#if query_field_no gt 1>  </#if><j-dict-select-tag placeholder="请选择${po.filedComment}" v-model:value="queryParam.${po.fieldName}" dictCode="${po.dictField}"/>
            <#else>
              <#if query_field_no gt 1>  </#if><a-input placeholder="请输入${po.filedComment}" v-model:value="queryParam.${po.fieldName}"></a-input>
         </#if>
      <#else>
              <#if query_field_no gt 1>  </#if><a-input placeholder="请输入${po.filedComment}" v-model:value="queryParam.${po.fieldName}"></a-input>
      </#if>
            <#if query_field_no gt 1>  </#if></a-form-item>
          <#if query_field_no gt 1>  </#if></a-col>
	<#else>
          <#if query_field_no gt 1>  </#if><a-col :lg="8">
            <#if query_field_no gt 1>  </#if><a-form-item label="${po.filedComment}">
      <#if po.classType=='date'>
               <#if query_field_no gt 1>  </#if><a-date-picker value-format="YYYY-MM-DD" placeholder="请选择开始时间" v-model:value="queryParam.${po.fieldName}_begin" class="query-group-cust"/>
               <#if query_field_no gt 1>  </#if><span class="query-group-split-cust">~</span>
               <#if query_field_no gt 1>  </#if><a-date-picker value-format="YYYY-MM-DD" placeholder="请选择结束日期" v-model:value="queryParam.${po.fieldName}_end" class="query-group-cust"/>
      <#elseif po.classType=='datetime'>
               <#if query_field_no gt 1>  </#if><a-date-picker showTime value-format="YYYY-MM-DD HH:mm:ss" placeholder="请选择开始时间" v-model:value="queryParam.${po.fieldName}_begin" class="query-group-cust" />
               <#if query_field_no gt 1>  </#if><span class="query-group-split-cust">~</span>
               <#if query_field_no gt 1>  </#if><a-date-picker showTime value-format="YYYY-MM-DD HH:mm:ss" placeholder="请选择结束时间" v-model:value="queryParam.${po.fieldName}_end" class="query-group-cust" />
      <#else>
               <#if query_field_no gt 1>  </#if><a-input placeholder="请输入最小值" v-model:value="queryParam.${po.fieldName}_begin" class="query-group-cust"></a-input>
               <#if query_field_no gt 1>  </#if><span class="query-group-split-cust">~</span>
               <#if query_field_no gt 1>  </#if><a-input placeholder="请输入最大值" v-model:value="queryParam.${po.fieldName}_end" class="query-group-cust"></a-input>
      </#if>
            <#if query_field_no gt 1>  </#if></a-form-item>
          <#if query_field_no gt 1>  </#if></a-col>
  </#if>
      <#assign query_field_no=query_field_no+1>
  </#if>