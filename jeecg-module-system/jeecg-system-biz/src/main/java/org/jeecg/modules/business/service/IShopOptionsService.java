package org.jeecg.modules.business.service;

import org.jeecg.modules.business.entity.ShopOptions;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.business.entity.ShopWithOptions;
import org.jeecg.modules.business.vo.OrderBypassStock;

import java.util.List;
import java.util.Map;

/**
 * @Description: 客户选项列表
 * @Author: jeecg-boot
 * @Date:   2025-06-12
 * @Version: V1.0
 */
public interface IShopOptionsService extends IService<ShopOptions> {

    List<ShopOptions> getByInvoiceNumber(String invoiceNumber);

    List<ShopWithOptions> listWithFilters(Integer pageNo, Integer pageSize, List<String> shopIds, String clientId, Boolean showAll, Integer hasOptions, String order);

    int countWithFilters(List<String> shopIds, String clientId, Boolean showAll, Integer hasOptions);

    Map<String, ShopWithOptions> findByClientId(String clientId);

    List<OrderBypassStock> getStockBypassByOrder(List<String> orderIds);
}
