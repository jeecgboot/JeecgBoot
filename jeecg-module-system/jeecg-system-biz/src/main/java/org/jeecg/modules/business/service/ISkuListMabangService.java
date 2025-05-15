package org.jeecg.modules.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.business.domain.api.mabang.doSearchSkuListNew.SkuData;
import org.jeecg.modules.business.entity.Sku;
import org.jeecg.modules.business.entity.SkuWeight;
import org.jeecg.modules.business.vo.ResponsesWithMsg;
import org.jeecg.modules.business.vo.SkuOrderPage;

import java.util.List;
import java.util.Map;

public interface ISkuListMabangService extends IService<SkuData> {
    /**
     * Save skus to DB from mabang api.
     *
     * @param skuDataList skus to save.
     */
    Map<Sku, String> saveSkuFromMabang(List<SkuData> skuDataList);
    Map<Sku, String> updateSkusFromMabang(List<SkuData> skuDataList);
    /**
     * Parse a Sku erpCode into a Product code
     * @param erpCode Sku erpCode in format XXXXXXXX-XX
     * @return product code in format XXXXXXXX
     */
   String parseSkuToProduct(String erpCode) throws Exception;

    /**
     * Parse a Sku erpCode list and return a Product code list
     * @param erpCodeList List of Sku erpCode in format XXXXXXXX-XX
     * @return List of product code in format XXXXXXXX
     */
    List<String> parseSkuListToProductCodeList(List<SkuData> erpCodeList) throws Exception;
    Map<Sku, String> createSkus(List<SkuData> skuDataList);

    ResponsesWithMsg<String> publishSkuToMabang(List<SkuOrderPage> skuList);

    Map<Sku, String> skuSyncUpsert(List<String> erpCodes);
    void updateSkuId();

    void mabangSkuStockUpdate(List<String> erpCodes);

    ResponsesWithMsg<String> mabangSkuWeightUpdate(List<SkuWeight> skuWeights);

    List<SkuData> fetchUnpairedSkus(List<String> stockSkuList);

    List<SkuOrderPage> unpairedSkus(String shopId, List<String> skuNames);

    void compareClientSkusWithMabang(Map<String, Sku> clientSkus);
}
