package org.jeecg.modules.order.service;

import org.jeecg.modules.order.entity.ResponseOrder;
import org.jeecg.modules.order.entity.RequestOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: Request Order
 * @Author: jeecg-boot
 * @Date:   2021-05-05
 * @Version: V1.0
 */
public interface IRequestOrderService extends IService<RequestOrder> {

	/**
	 * 添加一对多
	 * 
	 */
	public void saveMain(RequestOrder requestOrder,List<ResponseOrder> responseOrderList) ;
	
	/**
	 * 修改一对多
	 * 
	 */
	public void updateMain(RequestOrder requestOrder,List<ResponseOrder> responseOrderList);
	
	/**
	 * 删除一对多
	 */
	public void delMain (String id);
	
	/**
	 * 批量删除一对多
	 */
	public void delBatchMain (Collection<? extends Serializable> idList);
	
}
