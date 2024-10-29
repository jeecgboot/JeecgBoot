package org.jeecg.config.mybatis;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.binding.MapperMethod.ParamMap;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.dto.LogDTO;
import org.jeecg.common.config.TenantContext;
import org.jeecg.common.constant.TenantConstant;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.base.event.SkuModifiedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * mybatis拦截器，自动注入创建人、创建时间、修改人、修改时间
 * @Author scott
 * @Date  2019-01-19
 *
 */
@Slf4j
@Component
@Intercepts({ @Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class }) })
public class MybatisInterceptor implements Interceptor {
	@Autowired
	private ApplicationEventPublisher eventPublisher;

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
		String sqlId = mappedStatement.getId();
		log.debug("------sqlId------" + sqlId);
		SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
		Object parameter = invocation.getArgs()[1];
		log.debug("------sqlCommandType------" + sqlCommandType);

		if (parameter == null) {
			return invocation.proceed();
		}
		if (SqlCommandType.INSERT == sqlCommandType) {
			LoginUser sysUser = this.getLoginUser();
			Field[] fields = oConvertUtils.getAllFields(parameter);
			for (Field field : fields) {
				log.debug("------field.name------" + field.getName());
				try {
					if ("createBy".equals(field.getName())) {
						field.setAccessible(true);
						Object localCreateBy = field.get(parameter);
						field.setAccessible(false);
						if (localCreateBy == null || "".equals(localCreateBy)) {
							if (sysUser != null) {
								// 登录人账号
								field.setAccessible(true);
								field.set(parameter, sysUser.getUsername());
								field.setAccessible(false);
							}
						}
					}
					// 注入创建时间
					if ("createTime".equals(field.getName())) {
						field.setAccessible(true);
						Object localCreateDate = field.get(parameter);
						field.setAccessible(false);
						if (localCreateDate == null || "".equals(localCreateDate)) {
							field.setAccessible(true);
							field.set(parameter, new Date());
							field.setAccessible(false);
						}
					}
					//注入部门编码
					if ("sysOrgCode".equals(field.getName())) {
						field.setAccessible(true);
						Object localSysOrgCode = field.get(parameter);
						field.setAccessible(false);
						if (localSysOrgCode == null || "".equals(localSysOrgCode)) {
							// 获取登录用户信息
							if (sysUser != null) {
								field.setAccessible(true);
								field.set(parameter, sysUser.getOrgCode());
								field.setAccessible(false);
							}
						}
					}

					//------------------------------------------------------------------------------------------------
					//注入租户ID（是否开启系统管理模块的多租户数据隔离【SAAS多租户模式】）
					if(MybatisPlusSaasConfig.OPEN_SYSTEM_TENANT_CONTROL) {
						if (TenantConstant.TENANT_ID.equals(field.getName())) {
							field.setAccessible(true);
							Object localTenantId = field.get(parameter);
							field.setAccessible(false);
							if (localTenantId == null) {
								field.setAccessible(true);
								field.set(parameter, oConvertUtils.getInt(TenantContext.getTenant(),0));
								field.setAccessible(false);
							}
						}
					}
					//------------------------------------------------------------------------------------------------
					
				} catch (Exception e) {
				}
			}
		}
		if (SqlCommandType.UPDATE == sqlCommandType) {
			LoginUser sysUser = this.getLoginUser();
			Field[] fields = null;
			if (parameter instanceof ParamMap) {
				ParamMap<?> p = (ParamMap<?>) parameter;
				//update-begin-author:scott date:20190729 for:批量更新报错issues/IZA3Q--
                String et = "et";
				if (p.containsKey(et)) {
					parameter = p.get(et);
				} else {
					parameter = p.get("param1");
				}
				//update-end-author:scott date:20190729 for:批量更新报错issues/IZA3Q-

				//update-begin-author:scott date:20190729 for:更新指定字段时报错 issues/#516-
				if (parameter == null) {
					return invocation.proceed();
				}
				//update-end-author:scott date:20190729 for:更新指定字段时报错 issues/#516-

				fields = oConvertUtils.getAllFields(parameter);
			} else {
				fields = oConvertUtils.getAllFields(parameter);
			}

			for (Field field : fields) {
				log.debug("------field.name------" + field.getName());
				try {
					if ("updateBy".equals(field.getName())) {
						//获取登录用户信息
						if (sysUser != null) {
							// 登录账号
							field.setAccessible(true);
							field.set(parameter, sysUser.getUsername());
							field.setAccessible(false);
						}
					}
					if ("updateTime".equals(field.getName())) {
						field.setAccessible(true);
						field.set(parameter, new Date());
						field.setAccessible(false);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		if(sqlId.contains("deleteOne")) {
			if(parameter instanceof Map) {
				Map<String, String> paramMap = (Map<String, String>) parameter;
				String table = paramMap.get("tbname");
				String id = paramMap.get("dataId");
				if(table.equals("sku")) {
					log.info("Publishing an event for table: {} - operation: DELETE - id: {}", table, id);
					eventPublisher.publishEvent(new SkuModifiedEvent(this, id, "DELETE"));
				}
			}
		}
		if(sqlId.contains("saveLog")) {
			if (parameter instanceof Map) {
				Map<String, Object> paramMap = (Map<String, Object>) parameter;
				if(paramMap.get("dto") instanceof LogDTO){
					LogDTO dto = (LogDTO) paramMap.get("dto");
					String table = dto.getLogContent().split(",")[1].substring(3);
					String operationStatus = dto.getLogContent().split(",")[2];
					String requestParam = dto.getRequestParam();
					if(table.equals("sku")) {
						if(operationStatus.equals("添加成功!")) {
							String id = extractIdFromRequestParam(requestParam);
							eventPublisher.publishEvent(new SkuModifiedEvent(this, id, "INSERT"));
						}
						if(operationStatus.equals("修改成功！")) {
							String id = extractIdFromRequestParam(requestParam);
							eventPublisher.publishEvent(new SkuModifiedEvent(this, id, "UPDATE"));
						}
					}
				}
			}
		}
		return invocation.proceed();
	}

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties properties) {
		// TODO Auto-generated method stub
	}

	//update-begin--Author:scott  Date:20191213 for：关于使用Quzrtz 开启线程任务， #465
    /**
     * 获取登录用户
     * @return
     */
	private LoginUser getLoginUser() {
		LoginUser sysUser = null;
		try {
			sysUser = SecurityUtils.getSubject().getPrincipal() != null ? (LoginUser) SecurityUtils.getSubject().getPrincipal() : null;
		} catch (Exception e) {
			//e.printStackTrace();
			sysUser = null;
		}
		return sysUser;
	}
	//update-end--Author:scott  Date:20191213 for：关于使用Quzrtz 开启线程任务， #465

	private String extractSkuId(Object parameter) {
		// Try to extract the skuId from the parameter (assuming it's a Long or part of a Map)
		if (parameter instanceof String) {
			return (String) parameter;
		} else if (parameter instanceof Map) {
			Map<String, Object> paramMap = (Map<String, Object>) parameter;
			return (String) paramMap.get("skuId");
		} else if (parameter.getClass().getName().contains("org.jeecg.modules.business.entity")) {
			Pattern idPattern = Pattern.compile("^.*\\(id=([\\w-]+),.*$");
			Matcher idMatcher = idPattern.matcher(parameter.toString());
			if(idMatcher.matches() &&  idMatcher.groupCount() == 1) {
				return idMatcher.group(1);
			}
		}
		// Add more handling for other cases if necessary
		return null;
	}
	private String extractIdFromRequestParam(String requestParam) {
		// Try to extract the skuId from the parameter (assuming it's a Long or part of a Map)
		Pattern idPattern = Pattern.compile("^.*\"id\":\"(\\w+)\".*$");
		Matcher idMatcher = idPattern.matcher(requestParam);
		if(idMatcher.matches() &&  idMatcher.groupCount() == 1) {
			return idMatcher.group(1);
		}
		return null;
	}

}
