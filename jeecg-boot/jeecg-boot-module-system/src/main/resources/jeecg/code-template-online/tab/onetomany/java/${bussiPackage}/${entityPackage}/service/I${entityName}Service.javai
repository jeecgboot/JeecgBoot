package ${bussiPackage}.${entityPackage}.service;

<#list subTables as sub>
import ${bussiPackage}.${entityPackage}.entity.${sub.entityName};
</#list>
import ${bussiPackage}.${entityPackage}.entity.${entityName};
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: ${tableVo.ftlDescription}
 * @Author: jeecg-boot
 * @Date:   ${.now?string["yyyy-MM-dd"]}
 * @Version: V1.0
 */
public interface I${entityName}Service extends IService<${entityName}> {

	/**
	 * 添加一对多
	 * 
	 */
	public void saveMain(${entityName} ${entityName?uncap_first},<#list subTables as sub>List<${sub.entityName}> ${sub.entityName?uncap_first}List<#if sub_has_next>,</#if></#list>) ;
	
	/**
	 * 修改一对多
	 * 
	 */
	public void updateMain(${entityName} ${entityName?uncap_first},<#list subTables as sub>List<${sub.entityName}> ${sub.entityName?uncap_first}List<#if sub_has_next>,</#if></#list>);
	
	/**
	 * 删除一对多
	 */
	public void delMain (String id);
	
	/**
	 * 批量删除一对多
	 */
	public void delBatchMain (Collection<? extends Serializable> idList);
	
}
