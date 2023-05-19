package org.jeecg.modules.wo.order.service;

import org.jeecg.modules.wo.order.entity.WoOrderDetail;
import org.jeecg.modules.wo.order.entity.WoOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 订单管理
 * @Author: jeecg-boot
 * @Date:   2023-04-25
 * @Version: V1.0
 */
public interface IWoOrderService extends IService<WoOrder> {

	/**
	 * 添加一对多
	 * 
	 */
	public void saveMain(WoOrder woOrder,List<WoOrderDetail> woOrderDetailList) ;
	
	/**
	 * 修改一对多
	 * 
	 */
	public void updateMain(WoOrder woOrder,List<WoOrderDetail> woOrderDetailList);
	
	/**
	 * 删除一对多
	 */
	public void delMain (String id);
	
	/**
	 * 批量删除一对多
	 */
	public void delBatchMain (Collection<? extends Serializable> idList);
	
}
