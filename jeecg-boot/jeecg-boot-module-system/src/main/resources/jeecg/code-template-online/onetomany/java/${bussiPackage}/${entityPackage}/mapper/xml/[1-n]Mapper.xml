<#list subTables as subTab>
<#assign originalForeignKeys = subTab.originalForeignKeys>
#segment#${subTab.entityName}Mapper.xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${bussiPackage}.${entityPackage}.mapper.${subTab.entityName}Mapper">

	<delete id="deleteByMainId" parameterType="java.lang.String">
		DELETE 
		FROM  ${subTab.tableName} 
		WHERE
		<#list originalForeignKeys as key>
			 ${key} = ${r'#'}{mainId} <#rt/>
		</#list>
	</delete>
	
	<select id="selectByMainId" parameterType="java.lang.String" resultType="${bussiPackage}.${entityPackage}.entity.${subTab.entityName}">
		SELECT * 
		FROM  ${subTab.tableName}
		WHERE
		<#list originalForeignKeys as key>
			 ${key} = ${r'#'}{mainId} <#rt/>
		</#list>
	</select>
</mapper>
</#list>