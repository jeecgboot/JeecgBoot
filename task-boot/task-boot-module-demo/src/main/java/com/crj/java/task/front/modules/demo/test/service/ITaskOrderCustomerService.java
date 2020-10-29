package com.crj.java.task.front.modules.demo.test.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.crj.java.task.front.modules.demo.test.entity.TaskOrderCustomer;

import java.util.List;

/**
 * @Description: 订单客户
 * @Author: Crj
 * @Date:  2019-02-15
 * @Version: V1.0
 */
public interface ITaskOrderCustomerService extends IService<TaskOrderCustomer> {

	public List<TaskOrderCustomer> selectCustomersByMainId(String mainId);
}
