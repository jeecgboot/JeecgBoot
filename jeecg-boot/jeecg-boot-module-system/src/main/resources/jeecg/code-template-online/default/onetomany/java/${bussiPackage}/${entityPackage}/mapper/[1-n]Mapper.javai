<#list subTables as subTab>
#segment#${subTab.entityName}Mapper.java
package ${bussiPackage}.${entityPackage}.mapper;

import java.util.List;
import ${bussiPackage}.${entityPackage}.entity.${subTab.entityName};
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: ${subTab.ftlDescription}
 * @Author: jeecg-boot
 * @Date:   ${.now?string["yyyy-MM-dd"]}
 * @Version: V1.0
 */
public interface ${subTab.entityName}Mapper extends BaseMapper<${subTab.entityName}> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<${subTab.entityName}> selectByMainId(@Param("mainId") String mainId);
}
</#list>