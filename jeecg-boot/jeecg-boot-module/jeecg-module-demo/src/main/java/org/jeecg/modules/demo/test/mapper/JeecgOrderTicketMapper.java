package org.jeecg.modules.demo.test.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.demo.test.entity.JeecgOrderTicket;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 订单机票
 * @Author: jeecg-boot
 * @Date:  2019-02-15
 * @Version: V1.0
 */
public interface JeecgOrderTicketMapper extends BaseMapper<JeecgOrderTicket> {

	/**
	 *  通过主表外键批量删除客户
	 * @param mainId
	 * @return
	 */
    @Delete("DELETE FROM JEECG_ORDER_TICKET WHERE ORDER_ID = #{mainId}")
	public boolean deleteTicketsByMainId(String mainId);

    /**
     * 通过主表订单外键查询订单机票
     * @param mainId 订单id
     * @return 返回订单机票集合
     */
    @Select("SELECT * FROM JEECG_ORDER_TICKET WHERE ORDER_ID = #{mainId}")
	public List<JeecgOrderTicket> selectTicketsByMainId(String mainId);
}
