package org.jeecg.modules.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.business.entity.Sku;
import org.jeecg.modules.business.vo.SkuOrderPage;
import org.jeecg.modules.business.vo.SkuQuantity;
import org.jeecg.modules.business.vo.SkuUpdate;
import org.jeecg.modules.business.vo.inventory.InventoryRecord;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @Description: SKU表
 * @Author: jeecg-boot
 * @Date: 2021-04-01
 * @Version: V1.0
 */
@Repository
public interface SkuMapper extends BaseMapper<Sku> {
    List<InventoryRecord> pageSkuByClientId(String clientId, long offset, long size);

    long countTotal(String clientId);

    /**
     * Increase sku quantity.
     *
     * @param skuQuantities map between sku and its quantity to increase.
     */
    void addSkuQuantity(@Param("quantities") Map<String, Integer> skuQuantities);

    /**
     * Batch update stock
     * @param list List of stock update
     */
    void batchUpdateStock(@Param("list") List<SkuUpdate> list);

    List<Sku> selectByErpCode(Collection<String> erpCodes);

    String searchFirstMissingPriceSku(@Param("skuIds") List<String> skuIds);

    List<String> listErpCodesByIds(@Param("skuIds") List<String> skuIds);

    List<Sku> findMissingSkusInNotShippedOrders(@Param("start") LocalDateTime start);

    List<SkuQuantity> getSkuQuantitiesFromOrderIds(@Param("orderIds") List<String> orderIds);

    Integer countAllSkus();
    Integer countAllSkuWeightsWithFilters(@Param("erpCodes") String erpCodesRegex, @Param("zhNames") String zhNamesRegex, @Param("enNames") String enNamesRegex);
    Integer countAllClientSkus(@Param("clientId") String clientId);
    Integer countAllClientSkusWithFilters(@Param("clientId") String clientId, @Param("erpCodes") String erpCodesRegex, @Param("zhNames") String zhNamesRegex, @Param("enNames") String enNamesRegex);

    List<SkuOrderPage> fetchSkuWeights(@Param("offset") Integer offset, @Param("size") Integer pageSize, @Param("column") String column, @Param("order") String order);
    List<SkuOrderPage> fetchSkusByClient(@Param("clientId") String clientId, @Param("offset") Integer offset, @Param("size") Integer pageSize, @Param("column") String column, @Param("order") String order);
    List<SkuOrderPage> fetchSkuWeightsWithFilters(@Param("offset") Integer offset, @Param("size") Integer pageSize, @Param("column") String column, @Param("order") String order, @Param("erpCodes") String erpCodesRegex, @Param("zhNames") String zhNamesRegex, @Param("enNames") String enNamesRegex);
    List<SkuOrderPage> fetchSkusByClientWithFilters(@Param("clientId") String clientId, @Param("offset") Integer offset, @Param("size") Integer pageSize, @Param("column") String column, @Param("order") String order, @Param("erpCodes") String erpCodesRegex, @Param("zhNames") String zhNamesRegex, @Param("enNames") String enNamesRegex);

    List<SkuOrderPage> listSelectableSkuIdsWithFilters(@Param("clientId") String clientId, @Param("erpCodes") String erpCodeList, @Param("zhNames") String zhNameList, @Param("enNames") String enNameList);
    List<SkuOrderPage> listSelectableSkuIds(@Param("clientId") String clientId);

    String getIdFromErpCode(@Param("erpCode") String erpCode);

    List<Sku> listSkus();

    Sku getByErpCode(@Param("erpCode") String erpCode);

    void updateBatchStockByIds(@Param("skus") List<Sku> skuToUpdate);

    List<SkuOrderPage> getInventoryByInvoiceNumber(@Param("invoiceNumber") String invoiceNumber);

    List<SkuOrderPage> getInventory(@Param("erpCodes") List<String> erpCodes, @Param("invoiceNumber") String invoiceNumber);

    List<Sku> listByClientId(@Param("clientId") String clientId);

    List<SkuOrderPage> searchExistingSkuByKeywords(@Param("keywords") List<String> keywords);
    List<Sku> listImgUrls();

    List<String> fetchAllClientSkuCodes(@Param("clientCode") String clientCode);

    List<String> fetchUnpairedSkus(@Param("shopId") String shopId, @Param("offset") Integer offset, @Param("size") Integer pageSize, @Param("column") String column, @Param("order") String order);

    int countUnpairedSkus(@Param("shopId") String shopId);

    List<String> latestSkuCounter(@Param("userCode") String userCode, @Param("clientCode") String clientCode, @Param("date") String date);

    void setDesynced(@Param("erpCodes") List<String> erpCodes);
}
