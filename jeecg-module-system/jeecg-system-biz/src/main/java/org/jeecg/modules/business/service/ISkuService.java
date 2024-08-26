package org.jeecg.modules.business.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.business.controller.UserException;
import org.jeecg.modules.business.entity.*;
import org.jeecg.modules.business.vo.*;
import org.jeecg.modules.business.vo.inventory.InventoryRecord;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @Description: SKU表
 * @Author: jeecg-boot
 * @Date: 2021-06-28
 * @Version: V1.1
 */
public interface ISkuService extends IService<Sku> {
    List<Sku> listSkus();
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

    String searchFirstMissingPriceSku(List<String> skuIds);

    List<String> listErpCodesByIds(List<String> skuIds);

    /**
     * Find all sku with missing stock in not shipped orders from self-service clients
     * @return
     */
    List<Sku> findMissingSkusInNotShippedOrders(LocalDateTime start);

    List<SkuQuantity> getSkuQuantitiesFromOrderIds(List<String> orderIds);

    Integer countAllSkus();
    List<SkuOrderPage> fetchSkuWeights(Integer pageNo, Integer pageSize, String parsedColumn, String parsedOrder);

    Integer countAllClientSkus();
    List<SkuOrderPage> fetchSkusByClient(String clientId, Integer pageNo, Integer pageSize, String column, String order);

    Integer countAllSkuWeightsWithFilters(List<String> erpCodeList, List<String> zhNameList, List<String> enNameList);
    List<SkuOrderPage> fetchSkuWeightsWithFilters(Integer pageNo, Integer pageSize, String parsedColumn, String parsedOrder, List<String> erpCodes, List<String> zhNames, List<String> enNames);

    Integer countAllClientSkusWithFilters(String clientId, List<String> erpCodeList, List<String> zhNameList, List<String> enNameList);
    List<SkuOrderPage> fetchSkusByClientWithFilters(String clientId, Integer pageNo, Integer pageSize, String column, String order, List<String> erpCodes, List<String> zhNames, List<String> enNames);

    void addSkuQuantity(Map<String, Integer> quantityPurchased);
    String getIdFromErpCode(String erpCode);
    Sku getByErpCode(String erpCode);
    void updateBatchStockByIds(List<Sku> skuToUpdate);

    List<SkuOrderPage> getInventoryByInvoiceNumber(String invoiceNumber);

    List<Sku> listByClientId(String clientId);

    List<org.jeecg.modules.business.model.Sku> listAsMongoCollection();
}
