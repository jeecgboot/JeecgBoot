package com.crj.java.task.front.modules.demo.test.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.crj.java.task.front.modules.demo.test.entity.TaskOrderTicket;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Description: 订单机票
 * @Author: Crj
 * @Date:  2019-02-15
 * @Version: V1.0
 */
public interface TaskOrderTicketMapper extends BaseMapper<TaskOrderTicket> {

	/**
	 *  通过主表外键批量删除客户
	 * @param mainId
	 * @return
	 */
    @Delete("DELETE FROM TASK_ORDER_TICKET WHERE ORDER_ID = #{mainId}")
	public boolean deleteTicketsByMainId(String mainId);


    @Select("SELECT * FROM TASK_ORDER_TICKET WHERE ORDER_ID = #{mainId}")
	public List<TaskOrderTicket> selectTicketsByMainId(String mainId);
}
