package org.jeecg.modules.business.service.impl;

import org.jeecg.modules.business.entity.ShippingDiscount;
import org.jeecg.modules.business.mapper.ShippingDiscountMapper;
import org.jeecg.modules.business.service.IShippingDiscountService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: SKU物流折扣
 * @Author: jeecg-boot
 * @Date:   2021-04-03
 * @Version: V1.0
 */
@Service
public class ShippingDiscountServiceImpl extends ServiceImpl<ShippingDiscountMapper, ShippingDiscount> implements IShippingDiscountService {
	
	@Autowired
	private ShippingDiscountMapper shippingDiscountMapper;
	
	@Override
	public List<ShippingDiscount> selectByMainId(String mainId) {
		return shippingDiscountMapper.selectByMainId(mainId);
	}
}
