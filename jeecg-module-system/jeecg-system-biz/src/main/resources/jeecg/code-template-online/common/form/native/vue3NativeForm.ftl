<#include "/common/utils.ftl">
<#if po.isShow =='Y' && po.fieldName != 'id' && isNotPidField(tableVo, po.fieldDbName)>
<#assign form_field_dictCode="">
	<#if po.dictTable?default("")?trim?length gt 1 && po.dictText?default("")?trim?length gt 1 && po.dictField?default("")?trim?length gt 1>
		<#assign form_field_dictCode="${po.dictTable},${po.dictText},${po.dictField}">
	<#elseif po.dictField?default("")?trim?length gt 1>
		<#assign form_field_dictCode="${po.dictField}">
	</#if>
        <a-col :span="${form_span}">
          <a-form-item label="${po.filedComment}" v-bind="validateInfos.${autoStringSuffixForModel(po)}">
			<#if po.classType =='date'>
		        <a-date-picker placeholder="请选择${po.filedComment}" v-model:value="formData.${po.fieldName}" value-format="YYYY-MM-DD"  style="width: 100%" <#if po.readonly=='Y'>disabled<#else>:disabled="disabled"</#if>/>
			<#elseif po.classType =='datetime'>
		        <a-date-picker placeholder="请选择${po.filedComment}"  v-model:value="formData.${po.fieldName}" showTime value-format="YYYY-MM-DD HH:mm:ss" style="width: 100%" <#if po.readonly=='Y'>disabled<#else>:disabled="disabled"</#if>/>
			<#elseif po.classType =='time'>
			      <#assign need_time = true>
		        <time-picker placeholder="请选择${po.filedComment}" value-format="HH:mm:ss"  v-model:value="formData.${po.fieldName}" style="width: 100%" <#if po.readonly=='Y'>disabled<#else>:disabled="disabled"</#if>/>
			<#elseif po.classType =='popup'>
		        <#assign need_popup = true>
		        <#assign sourceFields = po.dictField?default("")?trim?split(",")/>
		        <#assign targetFields = po.dictText?default("")?trim?split(",")/>
		        <j-popup
		          placeholder="请选择${po.filedComment}"
		          v-model:value="formData.${po.fieldName}"
		          code="${po.dictTable}"
		          :fieldConfig="[
		            <#list sourceFields as fieldName>
		            { source: '${fieldName}', target: '${dashedToCamel(targetFields[fieldName_index])}' },
		            </#list>
		          ]"
		          :multi="${po.extendParams.popupMulti?c}"
		          :setFieldsValue="setFieldsValue"
              <#if po.readonly=='Y'>disabled<#else>:disabled="disabled"</#if><#rt>
		        />
		  <#elseif po.classType =='sel_depart'>
	          <#assign need_dept = true>
	          <j-select-dept v-model:value="formData.${po.fieldName}" :multiple="${po.extendParams.multi?default('true')}" checkStrictly <#if po.readonly=='Y'>disabled<#else>:disabled="disabled"</#if> />
		  <#elseif po.classType =='switch'>
	          <#assign need_switch = true>
	          <j-switch v-model:value="formData.${po.fieldName}" <#if po.dictField != 'is_open'>:options="${po.dictField}"</#if> <#if po.readonly=='Y'>disabled<#else>:disabled="disabled"</#if>></j-switch>
			<#elseif po.classType =='pca'>
	          <#assign need_pca = true>
	          <j-area-linkage v-model:value="formData.${po.fieldName}" placeholder="请输入${po.filedComment}" <#if po.readonly=='Y'>disabled<#else>:disabled="disabled"</#if> />
			<#elseif po.classType =='markdown'>
	          <#assign need_markdown = true>
	          <j-markdown-editor v-model:value="formData.${autoStringSuffixForModel(po)}" id="${po.fieldName}" placeholder="请输入${po.filedComment}" <#if po.readonly=='Y'>disabled<#else>:disabled="disabled"</#if>></j-markdown-editor>
		  <#elseif po.classType =='password'>
	          <a-input-password v-model:value="formData.${po.fieldName}" placeholder="请输入${po.filedComment}" <#if po.readonly=='Y'>disabled<#else>:disabled="disabled"</#if>/>
			<#elseif po.classType =='sel_user'>
	          <#assign need_dept_user = true>
	          <j-select-user-by-dept v-model:value="formData.${po.fieldName}" <#if po.readonly=='Y'>disabled<#else>:disabled="disabled"</#if>/>
			<#elseif po.classType =='textarea'>
	          <a-textarea v-model:value="formData.${autoStringSuffixForModel(po)}" rows="4" placeholder="请输入${po.filedComment}" <#if po.readonly=='Y'>disabled<#else>:disabled="disabled"</#if>/>
			<#elseif po.classType=='radio'>
			 <#assign need_select_tag = true>
            <j-dict-select-tag type='radio' v-model:value="formData.${po.fieldName}" dictCode="${form_field_dictCode}" placeholder="请选择${po.filedComment}" <#if po.readonly=='Y'>disabled<#else>:disabled="disabled"</#if>/>
			<#elseif po.classType=='list'>
	          <#assign need_select_tag = true>
	          <j-dict-select-tag v-model:value="formData.${po.fieldName}" dictCode="${form_field_dictCode}" placeholder="请选择${po.filedComment}" <#if po.readonly=='Y'>disabled<#else>:disabled="disabled"</#if>/>
			<#elseif po.classType=='list_multi'>
	          <#assign need_multi = true>
	          <j-select-multiple type="${po.classType}" v-model:value="formData.${po.fieldName}" dictCode="${form_field_dictCode}" placeholder="请选择${po.filedComment}" <#if po.readonly=='Y'>disabled<#else>:disabled="disabled"</#if> :triggerChange="false"/>
			 <#elseif po.classType=='checkbox'>
					  <#assign need_checkbox = true>
	          <j-checkbox type="${po.classType}" v-model:value="formData.${po.fieldName}" dictCode="${form_field_dictCode}" placeholder="请选择${po.filedComment}" <#if po.readonly=='Y'>disabled<#else>:disabled="disabled"</#if>/>
			<#elseif po.classType=='sel_search'>
	          <#assign need_search = true>
	          <j-search-select v-model:value="formData.${po.fieldName}" dict="${form_field_dictCode}" <#if po.readonly=='Y'>disabled<#else>:disabled="disabled"</#if> />
		    <#elseif po.classType=='cat_tree'>
		      <#assign need_category = true>
	          <j-category-select v-model:value="formData.${po.fieldName}" pcode="${po.dictField?default("")}" placeholder="请选择${po.filedComment}" <#if po.dictText?default("")?trim?length gt 1>back="${dashedToCamel(po.dictText)}"</#if> <#if po.readonly=='Y'>disabled<#else>:disabled="disabled"</#if> @change="(value) => handleFormChange('${po.fieldName}', value)" />
			<#elseif po.fieldDbType=='int' || po.fieldDbType=='double' || po.fieldDbType=='BigDecimal'>
	          <a-input-number v-model:value="formData.${po.fieldName}" placeholder="请输入${po.filedComment}" style="width: 100%" <#if po.readonly=='Y'>disabled<#else>:disabled="disabled"</#if>/>
			<#elseif po.classType=='file'>
		        <#assign need_upload = true>
	          <j-upload v-model:value="formData.${po.fieldName}"  <#if po.readonly=='Y'>disabled<#else>:disabled="disabled"</#if> <#if po.uploadnum??>:maxCount=${po.uploadnum}</#if>></j-upload>
			<#elseif po.classType=='image'>
		        <#assign need_image_upload = true>
	          <j-image-upload <#if po.uploadnum??>:fileMax=${po.uploadnum}</#if> v-model:value="formData.${po.fieldName}" <#if po.readonly=='Y'>disabled<#else>:disabled="disabled"</#if>></j-image-upload>
			<#elseif po.classType=='umeditor'>
		        <#assign need_editor = true>
	          <j-editor v-model:value="formData.${autoStringSuffixForModel(po)}" <#if po.readonly=='Y'>disabled<#else>:disabled="disabled"</#if>/>
		    <#elseif po.fieldDbType=='Blob'>
	          <a-input v-model:value="formData.${autoStringSuffixForModel(po)}" placeholder="请输入${po.filedComment}" <#if po.readonly=='Y'>disabled<#else>:disabled="disabled"</#if>></a-input>
				<#elseif po.classType == 'sel_tree'>
		        <#assign need_select_tree = true>
	          <j-tree-select
	            <#if po.dictText??>
	            <#if po.dictText?split(',')[2]?? && po.dictText?split(',')[0]??>
	            dict="${po.dictTable},${po.dictText?split(',')[2]},${po.dictText?split(',')[0]}"
	            <#elseif po.dictText?split(',')[1]??>
	            pidField="${po.dictText?split(',')[1]}"
	            <#elseif po.dictText?split(',')[3]??>
	            hasChildField="${po.dictText?split(',')[3]}"
	            </#if>
	            </#if>
	            pidValue="${po.dictField}"
	            <#if po.readonly=='Y'>disabled<#else>:disabled="disabled"</#if>
	            v-model:value="formData.${po.fieldName}"
	            @change="(value) => handleFormChange('${po.fieldName}', value)">
	          </j-tree-select>
			<#else>
            <a-input v-model:value="formData.${po.fieldName}" placeholder="请输入${po.filedComment}" <#if po.readonly=='Y'>disabled<#else>:disabled="disabled"</#if>></a-input>
    </#if>
          </a-form-item>
        </a-col>
</#if>