package org.jeecg.modules.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.logging.log4j.util.ProcessIdUtil;
import org.jeecg.modules.business.domain.api.mabang.doSearchSkuList.SkuData;
import org.jeecg.modules.business.entity.Product;
import org.jeecg.modules.business.entity.Sku;
import org.jeecg.modules.business.entity.SkuDeclaredValue;
import org.jeecg.modules.business.entity.SkuPrice;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * Responsible for all mabang sku persistance operation
 */
@Repository
public interface SkuListMabangMapper extends BaseMapper<SkuData> {



    /**
     * Search skus that exist in DB.
     *
     * @param skuErpCode
     * @return
     */
    List<Sku> searchExistence(@Param("skuErpCode") Collection<String> skuErpCode);
    List<Product> searchProductExistence(@Param("productCode") Collection<String> productCode);
    String searchProductId(@Param("productCode") String code);
    String searchSkuId(@Param("erpCode") String erpCode);
    String searchSensitiveAttributeId(@Param("enName") String enName);




}
