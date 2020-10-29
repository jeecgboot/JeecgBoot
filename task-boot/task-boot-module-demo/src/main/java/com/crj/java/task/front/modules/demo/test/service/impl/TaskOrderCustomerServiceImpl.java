package com.crj.java.task.front.modules.demo.test.service.impl;

import java.util.List;

import com.crj.java.task.front.modules.demo.test.entity.TaskOrderCustomer;
import com.crj.java.task.front.modules.demo.test.mapper.TaskOrderCustomerMapper;
import com.crj.java.task.front.modules.demo.test.service.ITaskOrderCustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 订单客户
 * @Author: Crj
 * @Date:  2019-02-15
 * @Version: V1.0
 */
@Service
public class TaskOrderCustomerServiceImpl extends ServiceImpl<TaskOrderCustomerMapper, TaskOrderCustomer> implements ITaskOrderCustomerService {

	@Autowired
	private TaskOrderCustomerMapper taskOrderCustomerMapper;

	@Override
	public List<TaskOrderCustomer> selectCustomersByMainId(String mainId) {
		return taskOrderCustomerMapper.selectCustomersByMainId(mainId);
	}

}
