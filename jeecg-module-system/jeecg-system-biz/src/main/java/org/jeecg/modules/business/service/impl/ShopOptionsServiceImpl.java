package org.jeecg.modules.business.service.impl;

import org.jeecg.modules.business.entity.ShopOptions;
import org.jeecg.modules.business.entity.ShopWithOptions;
import org.jeecg.modules.business.mapper.ShopOptionsMapper;
import org.jeecg.modules.business.service.IShopOptionsService;
import org.jeecg.modules.business.vo.OrderBypassStock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @Description: 客户选项列表
 * @Author: jeecg-boot
 * @Date:   2025-06-12
 * @Version: V1.0
 */
@Service
public class ShopOptionsServiceImpl extends ServiceImpl<ShopOptionsMapper, ShopOptions> implements IShopOptionsService {

    @Autowired
    private ShopOptionsMapper shopOptionsMapper;
    @Override
    public List<ShopOptions> getByInvoiceNumber(String invoiceNumber) {
        return shopOptionsMapper.getByInvoiceNumber(invoiceNumber);
    }

    @Override
    public List<ShopWithOptions> listWithFilters(Integer pageNo, Integer pageSize, List<String> shopIds, String clientId, Boolean showAll, Integer hasOptions, String order) {
        int offset = (pageNo - 1) * pageSize;
        List<Integer> hasOptionsValue = Arrays.asList(hasOptions == 0 ? new Integer[]{0} : hasOptions == 1 ? new Integer[]{1} : new Integer[]{0, 1});
        System.out.println("Listing with filters: " + shopIds + ", clientId: " + clientId + ", showAll: " + showAll + ", hasOptions: " + hasOptionsValue + ", order: " + order);
        return shopOptionsMapper.listWithFilters(offset, pageSize, shopIds, clientId, showAll, hasOptionsValue, order);
    }

    @Override
    public int countWithFilters(List<String> shopIds, String clientId, Boolean showAll, Integer hasOptions) {
        List<Integer> hasOptionsValue = Arrays.asList(hasOptions == 0 ? 0 : hasOptions == 1 ? 1 : 0, 1);
        return shopOptionsMapper.countWithFilters(shopIds, clientId, showAll, hasOptionsValue);
    }

    @Override
    public Map<String, ShopWithOptions> findByClientId(String clientId) {
        return shopOptionsMapper.findByClientId(clientId);
    }

    @Override
    public List<OrderBypassStock> getStockBypassByOrder(List<String> orderIds) {
        if(orderIds == null || orderIds.isEmpty()) {
            return new ArrayList<>();
        }
        return shopOptionsMapper.getStockBypassByOrder(orderIds);
    }

    @Override
    public Boolean findCanSelfInvoiceByClientId(String clientId) {
        List<Boolean> list = shopOptionsMapper.getCanSelfInvoiceByClientId(clientId);
        return list.stream()
                .filter(b-> b == true).findFirst().orElse(false);
    }
}
