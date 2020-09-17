package org.jeecg.modules.demo.test.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.demo.test.service.ITestSpringBean;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TestSpringBean implements ITestSpringBean {
	@Override
	public void test(String msg) {
		log.error("传入消息是==="+msg);
	}
}
