<#assign hasChildrenField = "">
<#list originalColumns as po>
<#if po.fieldDbName == tableVo.extendParams.hasChildren>
<#assign hasChildrenField = po.fieldName>
</#if>
</#list>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${bussiPackage}.${entityPackage}.mapper.${entityName}Mapper">

	<update id="updateTreeNodeStatus" parameterType="java.lang.String">
		update ${tableName} set ${Format.humpToUnderline(hasChildrenField)} = ${r'#'}{status} where id = ${r'#'}{id}
	</update>

</mapper>