package org.jeecg.modules.online.cgreport.mapper;

import java.util.List;
import java.util.Map;

import org.jeecg.modules.online.cgreport.entity.OnlCgreportHead;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 在线报表配置
 * @author: jeecg-boot
 * @date: 2019-03-08
 * @version: V1.0
 */
public interface OnlCgreportHeadMapper extends BaseMapper<OnlCgreportHead> {

	/**
	 * 执行查询SQL语句
	 * 
	 * @param sql
	 * @return
	 */
	List<Map<?, ?>> executeSelete(String sql);
	
	/**
	  * 动态sql查询数据量
	 * @param sql
	 * @return
	 */
	Long queryCountBySql(String sql);

	/**
	 * 通过reportId获取报表主配置信息
	 * @param reportId
	 * @return
	 */
	Map<String, Object> queryCgReportMainConfig(String reportId);
	/**
	 * 通过reportId获取报表字段信息
	 * @param reportCode
	 * @return
	 */
	List<Map<String, Object>> queryCgReportItems(String reportId);
	/**
	 * 通过reportId获取报参数信息
	 * @param reportId
	 * @return
	 */
	List<String> queryCgReportParams(String reportId);

}
