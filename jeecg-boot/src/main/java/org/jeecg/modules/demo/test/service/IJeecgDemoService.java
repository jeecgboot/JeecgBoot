package org.jeecg.modules.demo.test.service;

import org.jeecg.modules.demo.test.entity.JeecgDemo;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: jeecg 测试demo
 * @author： jeecg-boot
 * @date：   2018-12-29
 * @version： V1.0
 */
public interface IJeecgDemoService extends IService<JeecgDemo> {
	public void testTran();
	
	public JeecgDemo getByIdCacheable(String id);
}
