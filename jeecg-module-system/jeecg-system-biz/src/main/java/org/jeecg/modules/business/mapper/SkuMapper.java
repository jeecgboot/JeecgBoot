package org.jeecg.modules.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.business.entity.Sku;
import org.jeecg.modules.business.vo.SkuUpdate;
import org.jeecg.modules.business.vo.inventory.InventoryRecord;
import org.springframework.stereotype.Repository;

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

    boolean deleteByMainId(@Param("mainId") String mainId);

    List<Sku> selectByMainId(@Param("mainId") String mainId);

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
}
