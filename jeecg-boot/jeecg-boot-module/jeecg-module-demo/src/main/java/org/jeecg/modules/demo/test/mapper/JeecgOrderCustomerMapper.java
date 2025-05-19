package org.jeecg.modules.demo.test.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.demo.test.entity.JeecgOrderCustomer;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 订单客户
 * @Author: jeecg-boot
 * @Date:  2019-02-15
 * @Version: V1.0
 */
public interface JeecgOrderCustomerMapper extends BaseMapper<JeecgOrderCustomer> {
	
	/**
	 *  通过主表外键批量删除客户
	 * @param mainId
	 * @return
	 */
    @Delete("DELETE FROM JEECG_ORDER_CUSTOMER WHERE ORDER_ID = #{mainId}")
	public boolean deleteCustomersByMainId(String mainId);

    /**
     * 通过主表订单外键查询客户
     * @param mainId 订单id
     * @return 订单客户集合
     */
    @Select("SELECT * FROM JEECG_ORDER_CUSTOMER WHERE ORDER_ID = #{mainId}")
	public List<JeecgOrderCustomer> selectCustomersByMainId(String mainId);
}
