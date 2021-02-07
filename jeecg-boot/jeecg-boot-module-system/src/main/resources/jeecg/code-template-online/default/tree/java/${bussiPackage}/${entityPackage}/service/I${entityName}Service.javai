package ${bussiPackage}.${entityPackage}.service;

import ${bussiPackage}.${entityPackage}.entity.${entityName};
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.exception.JeecgBootException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import java.util.List;

/**
 * @Description: ${tableVo.ftlDescription}
 * @Author: jeecg-boot
 * @Date:   ${.now?string["yyyy-MM-dd"]}
 * @Version: V1.0
 */
public interface I${entityName}Service extends IService<${entityName}> {

	/**根节点父ID的值*/
	public static final String ROOT_PID_VALUE = "0";
	
	/**树节点有子节点状态值*/
	public static final String HASCHILD = "1";
	
	/**树节点无子节点状态值*/
	public static final String NOCHILD = "0";

	/**新增节点*/
	void add${entityName}(${entityName} ${entityName?uncap_first});
	
	/**修改节点*/
	void update${entityName}(${entityName} ${entityName?uncap_first}) throws JeecgBootException;
	
	/**删除节点*/
	void delete${entityName}(String id) throws JeecgBootException;

	/**查询所有数据，无分页*/
    List<${entityName}> queryTreeListNoPage(QueryWrapper<${entityName}> queryWrapper);

}
