package com.crj.java.task.front.modules.demo.test.service;

import com.crj.java.task.front.common.system.base.service.TaskService;
import com.crj.java.task.front.modules.demo.test.entity.TaskDemo;

import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * @Description: task 测试demo
 * @Author: Crj
 * @Date:  2018-12-29
 * @Version: V1.0
 */
public interface ITaskDemoService extends TaskService<TaskDemo> {

	public void testTran();

	public TaskDemo getByIdCacheable(String id);

	/**
	 * 查询列表数据 在service中获取数据权限sql信息
	 * @param pageSize
	 * @param pageNo
	 * @return
	 */
	IPage<TaskDemo> queryListWithPermission(int pageSize,int pageNo);
}
