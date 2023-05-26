package org.jeecg.modules.business.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.business.controller.UserException;
import org.jeecg.modules.business.entity.*;
import org.jeecg.modules.business.vo.*;
import org.jeecg.modules.business.vo.inventory.InventoryRecord;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: SKU表
 * @Author: jeecg-boot
 * @Date: 2021-06-28
 * @Version: V1.1
 */
public interface ISkuService extends IService<Sku> {

    List<Sku> selectByMainId(String mainId);

    /**
     * 添加一对多
     */
    public void saveMain(Sku sku,List<SkuPrice> skuPriceList,List<ShippingDiscount> shippingDiscountList,List<SkuDeclaredValue> skuDeclaredValueList) ;

    /**
     * 修改一对多
     */
    public void updateMain(Sku sku,List<SkuPrice> skuPriceList,List<ShippingDiscount> shippingDiscountList,List<SkuDeclaredValue> skuDeclaredValueList);

    /**
     * 删除一对多
     */
    void delMain(String id);

    /**
     * 批量删除一对多
     */
    void delBatchMain(Collection<? extends Serializable> idList);


    void fillPageForCurrentClient(Page<InventoryRecord> page);

    /**
     * Add sku quantity.
     *
     * @param skuQuantities sku quantity
     */
    void addInventory(List<SkuQuantity> skuQuantities);

    /**
     * Search sku quantity in platform orders and reduce them in sku quantity, then add them to inventory.
     * In case of no platform order to reduce, put {@code Collection.emptyList()}.
     *
     * @param skuQuantities
     * @param platformOrderIDs
     */
    void addInventory(List<SkuQuantity> skuQuantities, List<String> platformOrderIDs);

    /**
     * Batch update stock
     * @param list List of stock update
     */
    void batchUpdateSku(List<SkuUpdate> list);


    List<SkuMeasure> measureSku(Collection<String> skuIds);

    List<SkuName> all();

    List<UserSku> findSkuForCurrentUser();

    List<ClientSku> findSkuForUser(String userId);

    List<SkuChannelHistory> findHistoryBySkuId(String skuId) throws UserException;

    List<SkuChannelHistory> findHistoryBySkuIdsAndCountryCode(List<String> skuIds, String countryCode) throws UserException;

    List<Sku> selectByErpCode(Collection<String> erpCodes);
}
