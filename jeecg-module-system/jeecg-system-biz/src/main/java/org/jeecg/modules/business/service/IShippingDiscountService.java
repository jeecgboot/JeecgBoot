package org.jeecg.modules.business.service;

import org.jeecg.modules.business.entity.ShippingDiscount;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: SKU物流折扣
 * @Author: jeecg-boot
 * @Date:   2021-04-03
 * @Version: V1.0
 */
public interface IShippingDiscountService extends IService<ShippingDiscount> {

	List<ShippingDiscount> selectByMainId(String mainId);
}
