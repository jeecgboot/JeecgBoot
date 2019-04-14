package org.jeecg.modules.online.cgreport.service;

import java.util.List;
import java.util.Map;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.online.cgreport.entity.OnlCgreportHead;
import org.jeecg.modules.online.cgreport.model.OnlCgreportModel;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 在线报表配置
 * @author: jeecg-boot
 * @date: 2019-03-08
 * @version: V1.0
 */
public interface IOnlCgreportHeadService extends IService<OnlCgreportHead> {

	/**
	 * 修改全部项，包括新增、修改、删除
	 * 
	 * @param values
	 * @return
	 */
	Result<?> editAll(OnlCgreportModel values);

	/**
	 * 执行SQL语句
	 * 
	 * @param sql
	 * @return
	 */
	Map<String, Object> executeSelectSql(String sql,Map<String,Object> params);
	
	/**
	 * 动态数据源： 获取SQL解析的字段
	 */
	public List<String> getSqlFields(String sql,String dbKey);

	
	/**
	 * 解析SQL参数
	 */
	public List<String> getSqlParams(String sql);

	Map<String, Object> queryCgReportConfig(String reportCode);
	
	public List<Map<?, ?>> queryByCgReportSql(String sql, Map params,Map paramData,int page, int rows);

}
