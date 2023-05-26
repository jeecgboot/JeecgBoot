package org.jeecg.modules.business.service;

import org.jeecg.modules.business.entity.Sku;
import org.jeecg.modules.business.entity.Product;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 商品
 * @Author: jeecg-boot
 * @Date:   2021-04-01
 * @Version: V1.0
 */
public interface IProductService extends IService<Product> {

	/**
	 * 添加一对多
	 * 
	 */
	public void saveMain(Product product,List<Sku> skuList) ;
	
	/**
	 * 修改一对多
	 * 
	 */
	public void updateMain(Product product,List<Sku> skuList);
	
	/**
	 * 删除一对多
	 */
	public void delMain (String id);
	
	/**
	 * 批量删除一对多
	 */
	public void delBatchMain (Collection<? extends Serializable> idList);
	
}
