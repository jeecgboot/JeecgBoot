package org.jeecg.modules.business.service.impl;

import org.jeecg.modules.business.entity.ShippingFeesWaiverProduct;
import org.jeecg.modules.business.mapper.ShippingFeesWaiverProductMapper;
import org.jeecg.modules.business.service.IShippingFeesWaiverProductService;
import org.jeecg.modules.business.vo.SkuShippingFeesWaiver;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 采购运费免除产品
 * @Author: jeecg-boot
 * @Date:   2021-06-02
 * @Version: V1.0
 */
@Service
public class ShippingFeesWaiverProductServiceImpl extends ServiceImpl<ShippingFeesWaiverProductMapper, ShippingFeesWaiverProduct> implements IShippingFeesWaiverProductService {
	
	@Autowired
	private ShippingFeesWaiverProductMapper shippingFeesWaiverProductMapper;
	
	@Override
	public List<ShippingFeesWaiverProduct> selectByMainId(String mainId) {
		return shippingFeesWaiverProductMapper.selectByMainId(mainId);
	}

	@Override
	public List<SkuShippingFeesWaiver> selectBySkuIds(List<String> skuIds) {
		return shippingFeesWaiverProductMapper.searchWaiversBySkuIds(skuIds);
	}
}
