package com.crj.java.task.front.modules.demo.test.service.impl;

import java.util.List;

import com.crj.java.task.front.modules.demo.test.entity.TaskOrderTicket;
import com.crj.java.task.front.modules.demo.test.mapper.TaskOrderTicketMapper;
import com.crj.java.task.front.modules.demo.test.service.ITaskOrderTicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 订单机票
 * @Author: Crj
 * @Date:  2019-02-15
 * @Version: V1.0
 */
@Service
public class TaskOrderTicketServiceImpl extends ServiceImpl<TaskOrderTicketMapper, TaskOrderTicket> implements ITaskOrderTicketService {
	@Autowired
	private TaskOrderTicketMapper taskOrderTicketMapper;

	@Override
	public List<TaskOrderTicket> selectTicketsByMainId(String mainId) {
		return taskOrderTicketMapper.selectTicketsByMainId(mainId);
	}

}
