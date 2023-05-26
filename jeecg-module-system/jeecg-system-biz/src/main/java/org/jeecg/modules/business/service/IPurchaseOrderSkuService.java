package org.jeecg.modules.business.service;

import org.jeecg.modules.business.entity.PurchaseOrderSku;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 商品采购订单SKU
 * @Author: jeecg-boot
 * @Date:   2021-04-03
 * @Version: V1.0
 */
public interface IPurchaseOrderSkuService extends IService<PurchaseOrderSku> {

	public List<PurchaseOrderSku> selectByMainId(String mainId);
}
