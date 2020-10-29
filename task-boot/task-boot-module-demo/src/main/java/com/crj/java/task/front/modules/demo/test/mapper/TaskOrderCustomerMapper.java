package com.crj.java.task.front.modules.demo.test.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import com.crj.java.task.front.modules.demo.test.entity.TaskOrderCustomer;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 订单客户
 * @Author: Crj
 * @Date:  2019-02-15
 * @Version: V1.0
 */
public interface TaskOrderCustomerMapper extends BaseMapper<TaskOrderCustomer> {

	/**
	 *  通过主表外键批量删除客户
	 * @param mainId
	 * @return
	 */
    @Delete("DELETE FROM TASK_ORDER_CUSTOMER WHERE ORDER_ID = #{mainId}")
	public boolean deleteCustomersByMainId(String mainId);

    @Select("SELECT * FROM TASK_ORDER_CUSTOMER WHERE ORDER_ID = #{mainId}")
	public List<TaskOrderCustomer> selectCustomersByMainId(String mainId);
}
