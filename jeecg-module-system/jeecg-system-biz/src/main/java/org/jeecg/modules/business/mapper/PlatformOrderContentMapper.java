package org.jeecg.modules.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.business.entity.ClientPlatformOrderContent;
import org.jeecg.modules.business.entity.PlatformOrderContent;
import org.jeecg.modules.business.vo.SkuDetail;
import org.jeecg.modules.business.vo.SkuQuantity;
import org.jeecg.modules.business.vo.SkuWeightDiscountServiceFees;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @Description: 平台订单内容
 * @Author: jeecg-boot
 * @Date: 2021-04-08
 * @Version: V1.0
 */
@Repository
public interface PlatformOrderContentMapper extends BaseMapper<PlatformOrderContent> {

    boolean deleteByMainId(@Param("mainId") String mainId);

    List<PlatformOrderContent> selectByMainId(@Param("mainId") String mainId);

    List<ClientPlatformOrderContent> selectClientVersionByMainId(@Param("mainId") String mainId);

    /**
     * Search order contents of a list of order
     *
     * @param orderIDList list of identifiers of orders
     * @return map of sku ID and its quantity
     */
    List<SkuQuantity> searchOrderContent(List<String> orderIDList);

    /**
     * Search order contents of orders
     *
     * @param orderIds Order IDs
     * @return order contents
     */
    List<PlatformOrderContent> fetchOrderContent(List<String> orderIds);

    List<SkuDetail> searchSkuDetail(List<String> skuIDs);

    List<SkuWeightDiscountServiceFees> getAllWeightsDiscountsServiceFees();

    List<PlatformOrderContent> findUninvoicedOrderContents(
            @Param("shopIDs") List<String> shopIds,
            @Param("begin") Date begin,
            @Param("end") Date end
    );

    /**
     * Find all uninvoiced order content for specified shop between order time period with specified status ([1,2] or [1,2,3])
     *
     * @param shopIds
     * @param begin
     * @param end
     * @param erpStatuses
     * @return order contents
     */
    List<PlatformOrderContent> findUninvoicedOrderContentsForShopsAndStatus(
            @Param("shopIDs") List<String> shopIds,
            @Param("begin") Date begin,
            @Param("end") Date end,
            @Param("erpStatuses") List<Integer> erpStatuses
    );

    List<PlatformOrderContent> findUninvoicedShippedOrderContents();
    List<PlatformOrderContent> fetchPlatformOrderContentsToArchive(@Param("orderIDs") List<String> orderIDs);
}
