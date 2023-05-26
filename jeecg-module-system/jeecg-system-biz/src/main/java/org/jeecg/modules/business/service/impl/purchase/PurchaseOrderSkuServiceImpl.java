package org.jeecg.modules.business.service.impl.purchase;

import org.jeecg.modules.business.entity.PurchaseOrderSku;
import org.jeecg.modules.business.mapper.PurchaseOrderContentMapper;
import org.jeecg.modules.business.service.IPurchaseOrderSkuService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 商品采购订单SKU
 * @Author: jeecg-boot
 * @Date:   2021-04-03
 * @Version: V1.0
 */
@Service
public class PurchaseOrderSkuServiceImpl extends ServiceImpl<PurchaseOrderContentMapper, PurchaseOrderSku> implements IPurchaseOrderSkuService {
	
	@Autowired
	private PurchaseOrderContentMapper purchaseOrderContentMapper;
	
	@Override
	public List<PurchaseOrderSku> selectByMainId(String mainId) {
		return purchaseOrderContentMapper.selectByMainId(mainId);
	}
}
