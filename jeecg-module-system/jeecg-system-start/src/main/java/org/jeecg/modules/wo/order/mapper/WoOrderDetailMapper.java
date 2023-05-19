package org.jeecg.modules.wo.order.mapper;

import java.util.List;
import org.jeecg.modules.wo.order.entity.WoOrderDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 订单明细
 * @Author: jeecg-boot
 * @Date:   2023-04-25
 * @Version: V1.0
 */
public interface WoOrderDetailMapper extends BaseMapper<WoOrderDetail> {

	public boolean deleteByMainId(String mainId);
    
	public List<WoOrderDetail> selectByMainId(String mainId);
}
