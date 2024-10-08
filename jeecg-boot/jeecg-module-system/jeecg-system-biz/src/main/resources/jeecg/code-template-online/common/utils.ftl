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
<#--下划线转驼峰-->
<#function dashedToCamel(str)>
    <#assign text=""/>
    <#assign strlist = str?split("_")/>
    <#list strlist as v>
        <#assign text=text+v?cap_first/>
    </#list>
    <#return text?uncap_first>
</#function>
<#-- 驼峰转下划线 -->
<#function camelToDashed(str, case='lower')>
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
      || po.fieldValidType != ''
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

<#-- ** 如果Blob就显示 String * -->
<#function autoStringSuffix po>
  <#if  po.fieldDbType=='Blob'>
    <#return "'${po.fieldName}String'">
  <#else>
    <#return "'${po.fieldName}'">
  </#if>
</#function>

<#-- ** 如果Blob就显示model方式 String * -->
<#function autoStringSuffixForModel po>
    <#if  po.fieldDbType=='Blob'>
        <#return "${po.fieldName}String">
    <#else>
        <#return "${po.fieldName}">
    </#if>
</#function>

<#-- ** 高级查询生成 * -->
<#function superQueryFieldList po>
    <#assign superQuery_dictTable="">
    <#assign superQuery_dictText="">
    <#if po.dictTable?default("")?trim?length gt 1>
        <#assign superQuery_dictTable="${po.dictTable}">
    </#if>
    <#if po.dictText?default("")?trim?length gt 1>
        <#assign superQuery_dictText="${po.dictText}">
    </#if>
  <#if po.classType=="popup">
      <#return "{type:'${po.classType}',value:'${po.fieldName}',text:'${po.filedComment}', popup:{code:'${po.dictTable}',field:'${po.dictField?split(',')[0]}',orgFields:'${po.dictField?split(',')[0]}',destFields:'${po.dictText?split(',')[0]}'}}">
  <#elseif po.classType=="sel_user" || po.classType=="sel_depart" || po.classType=="datetime" || po.classType=="date" || po.classType=="pca" || po.classType=="switch">
      <#return "{type:'${po.classType}',value:'${po.fieldName}',text:'${po.filedComment}'}">
  <#else>
      <#if po.classType=="sel_search" || po.classType=="list_multi">
          <#return "{type:'${po.classType}',value:'${po.fieldName}',text:'${po.filedComment}',dictTable:\"${superQuery_dictTable}\", dictText:'${superQuery_dictText}', dictCode:'${po.dictField}'}">
      <#elseif po.dictTable?? && po.dictTable!="" && po.classType!="sel_tree" && po.classType!="cat_tree" && po.classType!="link_down">
          <#return "{type:'${po.fieldDbType}',value:'${po.fieldName}',text:'${po.filedComment}',dictCode:\"${po.dictTable},${po.dictText},${po.dictField}\"}">
      <#elseif po.dictField?? && po.classType!="sel_tree" && po.classType!="cat_tree" && po.classType!="link_down">
          <#return "{type:'${po.fieldDbType}',value:'${po.fieldName}',text:'${po.filedComment}',dictCode:'${po.dictField}'}">
      <#elseif po.fieldDbType=="Text">
          <#return "{type:'string',value:'${po.fieldName}',text:'${po.filedComment}'}">
      <#elseif po.fieldDbType=="Blob">
          <#return "{type:'byte',value:'${po.fieldName}',text:'${po.filedComment}'}">
      <#elseif po.fieldDbType=="BigDecimal" || po.fieldDbType=="double">
          <#return "{type:'number',value:'${po.fieldName}',text:'${po.filedComment}'}">
      <#else>
          <#return "{type:'${po.fieldDbType}',value:'${po.fieldName}',text:'${po.filedComment}'}">
      </#if>
  </#if>
</#function>
<#-- update-begin---author:chenrui ---date:20231228  for:[QQYUN-7527]vue3代码生成默认带上高级查询---------- -->
<#-- ** 高级查询生成(Vue3 * -->
<#function superQueryFieldListForVue3(po,order)>
<#-- 字段展示/DB类型 -->
    <#assign baseAttrs="view: '${po.classType}', type: 'string',">
    <#if po.fieldDbType=='int' || po.fieldDbType=='double' || po.fieldDbType=='BigDecimal'>
        <#assign baseAttrs="view: 'number', type: 'number',">
    </#if>

<#-- 特殊类型控件扩展字段 -->
    <#assign extAttrs="">
    <#assign dictCode="">
    <#if po.dictTable?default('')?trim?length gt 1 && po.dictText?default('')?trim?length gt 1 && po.dictField?default("")?trim?length gt 1>
        <#-- update-begin---author:chenrui ---date:20231228  for:fix 带条件字典存在单引号导致js编译错误---------- -->
        <#assign dictCode="dictTable: \"${po.dictTable}\", dictCode: '${po.dictField}', dictText: '${po.dictText}'">
        <#-- update-begin---author:chenrui ---date:20231228  for:fix 带条件字典存在单引号导致js编译错误---------- -->
    <#elseif po.dictField?default("")?trim?length gt 1>
        <#assign dictCode="dictCode: '${po.dictField}'">
    <#else>
        <#assign dictCode="dictCode: ''">
    </#if>

    <#if po.classType=='list' || po.classType=='list_multi' || po.classType=='sel_search' || po.classType=='checkbox' || po.classType=='radio'>
        <#assign extAttrs="${dictCode},">
    <#elseif po.classType=='cat_tree'>
    <#-- 分类字典树 -->
        <#-- update-begin---author:chenrui ---date:20240109  for：[issue/5787]增加非空判断防止代码生成时空指针异常---------- -->
        <#assign extAttrs="pcode: '${po.dictField?default('')}',">
        <#-- update-end---author:chenrui ---date:20240109  for：[issue/5787]增加非空判断防止代码生成时空指针异常---------- -->
    <#elseif po.classType=='sel_tree'>
    <#-- 自定义树 -->
        <#if po.dictText??>
        <#-- dictText示例:id,pid,name,has_child -->
            <#if po.dictText?split(',')[2]?? && po.dictText?split(',')[0]??>
                <#assign extAttrs="dict: '${po.dictTable},${po.dictText?split(',')[2]},${po.dictText?split(',')[0]}'">
            <#elseif po.dictText?split(',')[1]??>
                <#assign extAttrs="pidField: '${po.dictText?split(',')[1]}'">
            <#elseif po.dictText?split(',')[3]??>
                <#assign extAttrs="hasChildField: '${po.dictText?split(',')[3]}'">
            </#if>
        </#if>
        <#-- update-begin---author:chenrui ---date:20240109  for：[issue/5787]增加非空判断防止代码生成时空指针异常---------- -->
        <#assign extAttrs="${extAttrs}, pidValue: '${po.dictField?default('')}',">
        <#-- update-end---author:chenrui ---date:20240109  for：[issue/5787]增加非空判断防止代码生成时空指针异常---------- -->
    <#elseif po.classType=='popup'>
    <#-- popup -->
        <#-- update-begin---author:chenrui ---date:20240109  for：[issue/5787]增加非空判断防止代码生成时空指针异常---------- -->
        <#if po.dictText?default("")?trim?length gt 1 && po.dictText?index_of(',') gt 0 && po.dictField?default("")?trim?length gt 1>
        <#-- update-begin---author:chenrui ---date:20240109  for：[issue/5787]增加非空判断防止代码生成时空指针异常---------- -->
        <#-- 如果有多个回填字段,找到popup字段对应的来源字段 -->
            <#assign orgFieldIx=po.dictText?split(',')?seq_index_of(po.fieldDbName)>
            <#assign orgField=po.dictField?split(',')[orgFieldIx]>
        <#else>
            <#assign orgField=po.dictField?default("")>
        </#if>
        <#assign extAttrs="code: '${po.dictTable?default('')}', orgFields: '${orgField}', destFields: '${po.fieldName}', popupMulti: false,">
    </#if>

    <#return "${po.fieldName}: {title: '${po.filedComment}',order: ${order},${baseAttrs}${extAttrs}}" >
</#function>
<#-- update-end---author:chenrui ---date:20231228  for:[QQYUN-7527]vue3代码生成默认带上高级查询---------- -->


<#-- vue3 获取表单modal的宽度-->
<#function getModalWidth fieldRowNum>
    <#assign modal_width = 800>
    <#if fieldRowNum==2>
        <#assign modal_width = 896>
    <#elseif fieldRowNum==3>
        <#assign modal_width = 1024>
    <#elseif fieldRowNum==4>
        <#assign modal_width = 1280>
    </#if>
    <#return modal_width>
</#function>

<#-- vue3 获取表单 colspan -->
<#function getFormSpan fieldRowNum>
    <#assign form_span = 24>
    <#if fieldRowNum==2>
        <#assign form_span = 12>
    <#elseif fieldRowNum==3>
        <#assign form_span = 8>
    <#elseif fieldRowNum==4>
        <#assign form_span = 6>
    </#if>
    <#return form_span>
</#function>

<#-- vue3 native 判断字段名不是 pidField  -->
<#function isNotPidField(tableVo, fieldDbName) >
  <#assign flag = true>
  <#if tableVo??>
    <#if tableVo.extendParams??>
      <#if tableVo.extendParams.pidField?default("")?trim == fieldDbName>
        <#assign flag = false>
      </#if>
    </#if>
  </#if>
  <#return flag>
</#function>

<#-- vue3 native 获取范围字段 -->
<#function getRangeField(columns) >
  <#assign rangeField = "">
  <#list columns as po>
      <#if po.isQuery=='Y'>
          <#if po.queryMode!='single'>
              <#if po.fieldDbType=='int' || po.fieldDbType=='double' || po.fieldDbType=='BigDecimal' || po.classType=='time' || po.classType=='date' || po.classType=='datetime'>
                  <#assign rangeField = rangeField + "${po.fieldName},">
              </#if>
          </#if>
      </#if>
  </#list>
  <#return rangeField>
</#function>