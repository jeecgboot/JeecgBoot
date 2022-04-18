package org.jeecg.modules.system.mapper;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.system.entity.SysDataLog;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 系统数据日志Mapper接口
 * @author: jeecg-boot
 */
public interface SysDataLogMapper extends BaseMapper<SysDataLog>{
	/**
	 * 通过表名及数据Id获取最大版本
	 * @param tableName
	 * @param dataId
	 * @return
	 */
	public String queryMaxDataVer(@Param("tableName") String tableName,@Param("dataId") String dataId);
	
}
