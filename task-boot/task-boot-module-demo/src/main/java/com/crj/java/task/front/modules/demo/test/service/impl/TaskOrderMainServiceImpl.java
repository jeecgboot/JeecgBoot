package com.crj.java.task.front.modules.demo.test.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crj.java.task.front.modules.demo.test.entity.TaskOrderCustomer;
import com.crj.java.task.front.modules.demo.test.entity.TaskOrderMain;
import com.crj.java.task.front.modules.demo.test.entity.TaskOrderTicket;
import com.crj.java.task.front.modules.demo.test.mapper.TaskOrderCustomerMapper;
import com.crj.java.task.front.modules.demo.test.mapper.TaskOrderMainMapper;
import com.crj.java.task.front.modules.demo.test.mapper.TaskOrderTicketMapper;
import com.crj.java.task.front.modules.demo.test.service.ITaskOrderMainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 订单
 * @Author: Crj
 * @Date:  2019-02-15
 * @Version: V1.0
 */
@Service
public class TaskOrderMainServiceImpl extends ServiceImpl<TaskOrderMainMapper, TaskOrderMain> implements ITaskOrderMainService {

    @Autowired
    private TaskOrderMainMapper taskOrderMainMapper;
    @Autowired
    private TaskOrderCustomerMapper taskOrderCustomerMapper;
    @Autowired
    private TaskOrderTicketMapper taskOrderTicketMapper;

    @Override
    @Transactional
    public void saveMain(TaskOrderMain taskOrderMain, List<TaskOrderCustomer> taskOrderCustomerList, List<TaskOrderTicket> taskOrderTicketList) {
        taskOrderMainMapper.insert(taskOrderMain);
        if (taskOrderCustomerList != null) {
            for (TaskOrderCustomer entity : taskOrderCustomerList) {
                entity.setOrderId(taskOrderMain.getId());
                taskOrderCustomerMapper.insert(entity);
            }
        }
        if (taskOrderTicketList != null) {
            for (TaskOrderTicket entity : taskOrderTicketList) {
                entity.setOrderId(taskOrderMain.getId());
                taskOrderTicketMapper.insert(entity);
            }
        }
    }

    @Override
    @Transactional
    public void updateMain(TaskOrderMain taskOrderMain, List<TaskOrderCustomer> taskOrderCustomerList, List<TaskOrderTicket> taskOrderTicketList) {
        taskOrderMainMapper.updateById(taskOrderMain);

        //1.先删除子表数据
        taskOrderTicketMapper.deleteTicketsByMainId(taskOrderMain.getId());
        taskOrderCustomerMapper.deleteCustomersByMainId(taskOrderMain.getId());

        //2.子表数据重新插入
        if (taskOrderCustomerList != null) {
            for (TaskOrderCustomer entity : taskOrderCustomerList) {
                entity.setOrderId(taskOrderMain.getId());
                taskOrderCustomerMapper.insert(entity);
            }
        }
        if (taskOrderTicketList != null) {
            for (TaskOrderTicket entity : taskOrderTicketList) {
                entity.setOrderId(taskOrderMain.getId());
                taskOrderTicketMapper.insert(entity);
            }
        }
    }

	@Override
	@Transactional
	public void delMain(String id) {
		taskOrderMainMapper.deleteById(id);
		taskOrderTicketMapper.deleteTicketsByMainId(id);
		taskOrderCustomerMapper.deleteCustomersByMainId(id);
	}

	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			taskOrderMainMapper.deleteById(id);
			taskOrderTicketMapper.deleteTicketsByMainId(id.toString());
			taskOrderCustomerMapper.deleteCustomersByMainId(id.toString());
		}
	}

}
