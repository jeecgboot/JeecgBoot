package org.jeecg.modules.quartz.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jeecg.modules.quartz.entity.QuartzJob;

@Slf4j
public class TaskUtil {

		/**
		 * 通过反射调用scheduleJob中定义的方法
		 *
		 * @param scheduleJob
		 */
		@SuppressWarnings("unchecked")
		public static void invokMethod(QuartzJob scheduleJob) {
			Object object = null;
			Class clazz = null;
			boolean flag = true;
			if (StringUtils.isNotBlank(scheduleJob.getSpringId())) {
				object = SpringUtils.getBean(scheduleJob.getSpringId());
			} /*else if (StringUtils.isNotBlank(scheduleJob.getJobClassName())) {
				try {
					clazz = Class.forName(scheduleJob.getJobClassName());
					object = clazz.newInstance();
				} catch (Exception e) {
					flag = false;
					e.printStackTrace();
				}


			}*/
			if (object == null) {
				flag = false;
				log.error("任务spring bean 为空 = [" + scheduleJob.getSpringId() + "]---------------未启动成功，请检查是否配置正确！！！");
				return;
			}
			clazz = object.getClass();
			Method method = null;
			try {
				method = clazz.getDeclaredMethod(scheduleJob.getSpringMethodName(), new Class[] { String.class });
			} catch (NoSuchMethodException e) {
				flag = false;
				log.error("任务执行方法不存在 = [" + scheduleJob.getSpringMethodName() + "]---------------未启动成功，方法名设置错误！！！");

			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (method != null) {
				try {
					method.invoke(object, scheduleJob.getParameter());
				} catch (IllegalAccessException e) {
					flag = false;

					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					flag = false;

					e.printStackTrace();
				} catch (InvocationTargetException e) {
					flag = false;

					e.printStackTrace();
				}
			}
			if(flag){
				log.error("任务 = [" + scheduleJob.getSpringId() +"."+scheduleJob.getSpringMethodName()+ "]----------启动成功");

			}

		}


}
