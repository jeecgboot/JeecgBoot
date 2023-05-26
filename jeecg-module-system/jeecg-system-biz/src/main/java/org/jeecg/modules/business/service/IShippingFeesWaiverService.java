package org.jeecg.modules.business.service;

import org.jeecg.modules.business.entity.ShippingFeesWaiverProduct;
import org.jeecg.modules.business.entity.ShippingFeesWaiver;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 采购运费免除
 * @Author: jeecg-boot
 * @Date:   2021-06-02
 * @Version: V1.0
 */
public interface IShippingFeesWaiverService extends IService<ShippingFeesWaiver> {

	/**
	 * 添加一对多
	 * 
	 */
	public void saveMain(ShippingFeesWaiver shippingFeesWaiver,List<ShippingFeesWaiverProduct> shippingFeesWaiverProductList) ;
	
	/**
	 * 修改一对多
	 * 
	 */
	public void updateMain(ShippingFeesWaiver shippingFeesWaiver,List<ShippingFeesWaiverProduct> shippingFeesWaiverProductList);
	
	/**
	 * 删除一对多
	 */
	public void delMain (String id);
	
	/**
	 * 批量删除一对多
	 */
	public void delBatchMain (Collection<? extends Serializable> idList);
	
}
