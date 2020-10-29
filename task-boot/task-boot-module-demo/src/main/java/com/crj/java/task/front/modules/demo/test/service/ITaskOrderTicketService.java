package com.crj.java.task.front.modules.demo.test.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.crj.java.task.front.modules.demo.test.entity.TaskOrderTicket;

import java.util.List;

/**
 * @Description: 订单机票
 * @Author: Crj
 * @Date:  2019-02-15
 * @Version: V1.0
 */
public interface ITaskOrderTicketService extends IService<TaskOrderTicket> {

	public List<TaskOrderTicket> selectTicketsByMainId(String mainId);
}
