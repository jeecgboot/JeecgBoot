package org.jeecg.config.mybatis;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.Properties;

import org.apache.ibatis.binding.MapperMethod.ParamMap;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.system.entity.SysUser;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * mybatis拦截器，自动注入创建人、创建时间、修改人、修改时间
 * @author scott
 * @date  2019-01-19
 *
 */
@Slf4j
@Component
@Intercepts({ @Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class }) })
public class MybatisInterceptor implements Interceptor {

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
			Field[] fields = parameter.getClass().getDeclaredFields();
			for (Field field : fields) {
				log.debug("------field.name------" + field.getName());
				try {
					if ("createBy".equals(field.getName())) {
						field.setAccessible(true);
						Object local_createBy = field.get(parameter);
						field.setAccessible(false);
						if (local_createBy == null || local_createBy.equals("")) {
							String createBy = "jeecg-boot";
							// 获取登录用户信息
							SysUser sysUser = (SysUser) SecurityUtils.getSubject().getPrincipal();
							if (sysUser != null) {
								// 登录账号
								createBy = sysUser.getUsername();
							}
							if (!oConvertUtils.isEmpty(createBy)) {
								field.setAccessible(true);
								field.set(parameter, createBy);
								field.setAccessible(false);
							}
						}
					}
					// 注入创建时间
					if ("createTime".equals(field.getName())) {
						field.setAccessible(true);
						Object local_createDate = field.get(parameter);
						field.setAccessible(false);
						if (local_createDate == null || local_createDate.equals("")) {
							field.setAccessible(true);
							field.set(parameter, new Date());
							field.setAccessible(false);
						}
					}
				} catch (Exception e) {
				}
			}
		}
		if (SqlCommandType.UPDATE == sqlCommandType) {
			Field[] fields = null;
			if (parameter instanceof ParamMap) {
				ParamMap<?> p = (ParamMap<?>) parameter;
				parameter = p.get("param1");
				fields = parameter.getClass().getDeclaredFields();
			} else {
				fields = parameter.getClass().getDeclaredFields();
			}

			for (Field field : fields) {
				log.debug("------field.name------" + field.getName());
				try {
					if ("updateBy".equals(field.getName())) {
						field.setAccessible(true);
						Object local_updateBy = field.get(parameter);
						field.setAccessible(false);
						if (local_updateBy == null || local_updateBy.equals("")) {
							String updateBy = "jeecg-boot";
							// 获取登录用户信息
							SysUser sysUser = (SysUser) SecurityUtils.getSubject().getPrincipal();
							if (sysUser != null) {
								// 登录账号
								updateBy = sysUser.getUsername();
							}
							if (!oConvertUtils.isEmpty(updateBy)) {
								field.setAccessible(true);
								field.set(parameter, updateBy);
								field.setAccessible(false);
							}
						}
					}
					if ("updateTime".equals(field.getName())) {
						field.setAccessible(true);
						Object local_updateDate = field.get(parameter);
						field.setAccessible(false);
						if (local_updateDate == null || local_updateDate.equals("")) {
							field.setAccessible(true);
							field.set(parameter, new Date());
							field.setAccessible(false);
						}
					}
				} catch (Exception e) {
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

}
