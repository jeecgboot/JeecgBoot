<#list subTables as subTab>
#segment#${subTab.entityName}ServiceImpl.java
package ${bussiPackage}.${entityPackage}.service.impl;

import ${bussiPackage}.${entityPackage}.entity.${subTab.entityName};
import ${bussiPackage}.${entityPackage}.mapper.${subTab.entityName}Mapper;
import ${bussiPackage}.${entityPackage}.service.I${subTab.entityName}Service;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: ${subTab.ftlDescription}
 * @Author: jeecg-boot
 * @Date:   ${.now?string["yyyy-MM-dd"]}
 * @Version: V1.0
 */
@Service
public class ${subTab.entityName}ServiceImpl extends ServiceImpl<${subTab.entityName}Mapper, ${subTab.entityName}> implements I${subTab.entityName}Service {
	
	@Autowired
	private ${subTab.entityName}Mapper ${subTab.entityName?uncap_first}Mapper;
	
	@Override
	public List<${subTab.entityName}> selectByMainId(String mainId) {
		return ${subTab.entityName?uncap_first}Mapper.selectByMainId(mainId);
	}
}
</#list>