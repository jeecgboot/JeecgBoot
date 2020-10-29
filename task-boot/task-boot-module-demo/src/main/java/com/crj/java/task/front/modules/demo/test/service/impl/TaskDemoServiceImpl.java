package com.crj.java.task.front.modules.demo.test.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crj.java.task.front.common.constant.CacheConstant;
import com.crj.java.task.front.common.system.query.QueryGenerator;
import com.crj.java.task.front.modules.demo.test.entity.TaskDemo;
import com.crj.java.task.front.modules.demo.test.mapper.TaskDemoMapper;
import com.crj.java.task.front.modules.demo.test.service.ITaskDemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description: task 测试demo
 * @Author: Crj
 * @Date:  2018-12-29
 * @Version: V1.0
 */
@Service
public class TaskDemoServiceImpl extends ServiceImpl<TaskDemoMapper, TaskDemo> implements ITaskDemoService {
	@Autowired
	TaskDemoMapper taskDemoMapper;

	/**
	 * 事务控制在service层面
	 * 加上注解：@Transactional，声明的方法就是一个独立的事务（有异常DB操作全部回滚）
	 */
	@Override
	@Transactional
	public void testTran() {
		TaskDemo pp = new TaskDemo();
		pp.setAge(1111);
		pp.setName("测试事务  小白兔 1");
		taskDemoMapper.insert(pp);

		TaskDemo pp2 = new TaskDemo();
		pp2.setAge(2222);
		pp2.setName("测试事务  小白兔 2");
		taskDemoMapper.insert(pp2);

		Integer.parseInt("hello");//自定义异常

		TaskDemo pp3 = new TaskDemo();
		pp3.setAge(3333);
		pp3.setName("测试事务  小白兔 3");
		taskDemoMapper.insert(pp3);
		return ;
	}


	/**
	 * 缓存注解测试： redis
	 */
	@Override
	@Cacheable(cacheNames = CacheConstant.TEST_DEMO_CACHE, key = "#id")
	public TaskDemo getByIdCacheable(String id) {
		TaskDemo t = taskDemoMapper.selectById(id);
		System.err.println("---未读缓存，读取数据库---");
		System.err.println(t);
		return t;
	}


	@Override
	public IPage<TaskDemo> queryListWithPermission(int pageSize,int pageNo) {
		Page<TaskDemo> page = new Page<>(pageNo, pageSize);
		//编程方式，获取当前请求的数据权限规则SQL片段
		String sql = QueryGenerator.installAuthJdbc(TaskDemo.class);
		return this.baseMapper.queryListWithPermission(page, sql);
	}

}
