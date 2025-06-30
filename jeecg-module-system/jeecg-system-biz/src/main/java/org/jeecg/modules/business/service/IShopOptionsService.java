package org.jeecg.modules.business.service;

import org.jeecg.modules.business.entity.ShopOptions;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 客户选项列表
 * @Author: jeecg-boot
 * @Date:   2025-06-12
 * @Version: V1.0
 */
public interface IShopOptionsService extends IService<ShopOptions> {

    List<ShopOptions> getByInvoiceNumber(String invoiceNumber);
}
