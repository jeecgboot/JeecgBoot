package com.crj.java.task.front.modules.demo.test.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import com.crj.java.task.front.modules.demo.test.entity.TaskOrderCustomer;
import com.crj.java.task.front.modules.demo.test.entity.TaskOrderMain;
import com.crj.java.task.front.modules.demo.test.entity.TaskOrderTicket;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 订单
 * @Author: Crj
 * @Date:  2019-02-15
 * @Version: V1.0
 */
public interface ITaskOrderMainService extends IService<TaskOrderMain> {

	/**
	 * 添加一对多
	 *
	 */
	public void saveMain(TaskOrderMain taskOrderMain,List<TaskOrderCustomer> taskOrderCustomerList,List<TaskOrderTicket> taskOrderTicketList) ;

	/**
	 * 修改一对多
	 *
	 */
	public void updateMain(TaskOrderMain taskOrderMain,List<TaskOrderCustomer> taskOrderCustomerList,List<TaskOrderTicket> taskOrderTicketList);

	/**
	 * 删除一对多
	 * @param jformOrderMain
	 */
	public void delMain (String id);

	/**
	 * 批量删除一对多
	 * @param jformOrderMain
	 */
	public void delBatchMain (Collection<? extends Serializable> idList);
}
