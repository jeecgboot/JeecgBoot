package org.jeecg.modules.system.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.system.entity.SysDict;
import org.jeecg.modules.system.model.DuplicateCheckVo;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 字典表 Mapper 接口
 * </p>
 *
 * @author zhangweijian
 * @since 2018-12-28
 */
public interface SysDictMapper extends BaseMapper<SysDict> {
	
	/**
	  *  重复检查SQL
	 * @return
	 */
	public Long duplicateCheckCountSql(DuplicateCheckVo duplicateCheckVo);
	public Long duplicateCheckCountSqlNoDataId(DuplicateCheckVo duplicateCheckVo);
	
	public List<Map<String,Object>> queryDictItemsByCode(@Param("code") String code);
	public List<Map<String,Object>> queryTableDictItemsByCode(@Param("table") String table,@Param("text") String text,@Param("code") String code);


	public String queryDictTextByKey(@Param("code") String code,@Param("key") String key);

	public String queryTableDictTextByKey(@Param("table") String table,@Param("text") String text,@Param("code") String code,@Param("key") String key);


}
