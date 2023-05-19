package org.jeecg.modules.wo.order.service;

import org.jeecg.modules.wo.order.entity.WoOrderDetail;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 订单明细
 * @Author: jeecg-boot
 * @Date:   2023-04-25
 * @Version: V1.0
 */
public interface IWoOrderDetailService extends IService<WoOrderDetail> {

	public List<WoOrderDetail> selectByMainId(String mainId);
}
