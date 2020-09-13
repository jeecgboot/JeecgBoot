package org.jeecg.modules.demo.test.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.common.constant.CacheConstant;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.demo.test.entity.JeecgDemo;
import org.jeecg.modules.demo.test.mapper.JeecgDemoMapper;
import org.jeecg.modules.demo.test.service.IJeecgDemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description: jeecg 测试demo
 * @Author: jeecg-boot
 * @Date:  2018-12-29
 * @Version: V1.0
 */
@Service
public class JeecgDemoServiceImpl extends ServiceImpl<JeecgDemoMapper, JeecgDemo> implements IJeecgDemoService {
	@Autowired
	JeecgDemoMapper jeecgDemoMapper;
	
	/**
	 * 事务控制在service层面
	 * 加上注解：@Transactional，声明的方法就是一个独立的事务（有异常DB操作全部回滚）
	 */
	@Override
	@Transactional
	public void testTran() {
		JeecgDemo pp = new JeecgDemo();
		pp.setAge(1111);
		pp.setName("测试事务  小白兔 1");
		jeecgDemoMapper.insert(pp);
		
		JeecgDemo pp2 = new JeecgDemo();
		pp2.setAge(2222);
		pp2.setName("测试事务  小白兔 2");
		jeecgDemoMapper.insert(pp2);
		
		Integer.parseInt("hello");//自定义异常
		
		JeecgDemo pp3 = new JeecgDemo();
		pp3.setAge(3333);
		pp3.setName("测试事务  小白兔 3");
		jeecgDemoMapper.insert(pp3);
		return ;
	}


	/**
	 * 缓存注解测试： redis
	 */
	@Override
	@Cacheable(cacheNames = CacheConstant.TEST_DEMO_CACHE, key = "#id")
	public JeecgDemo getByIdCacheable(String id) {
		JeecgDemo t = jeecgDemoMapper.selectById(id);
		System.err.println("---未读缓存，读取数据库---");
		System.err.println(t);
		return t;
	}


	@Override
	public IPage<JeecgDemo> queryListWithPermission(int pageSize,int pageNo) {
		Page<JeecgDemo> page = new Page<>(pageNo, pageSize);
		//编程方式，获取当前请求的数据权限规则SQL片段
		String sql = QueryGenerator.installAuthJdbc(JeecgDemo.class);
		return this.baseMapper.queryListWithPermission(page, sql);
	}

}
