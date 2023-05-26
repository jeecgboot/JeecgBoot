package org.jeecg.modules.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.business.domain.api.mabang.getorderlist.Order;
import org.jeecg.modules.business.domain.api.mabang.getorderlist.OrderItem;
import org.jeecg.modules.business.entity.PlatformOrder;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * Responsible for all mabang platform order persistance operation
 */
@Repository
public interface PlatformOrderMabangMapper extends BaseMapper<Order> {

    /**
     * Update order in a merge operation.
     *
     * @param targetID
     * @param sourceIDs
     */
    void updateMergedOrder(@Param("target") String targetID, @Param("sources") List<String> sourceIDs);

    /**
     * Update orders content in a merge operation.
     *
     * @param targetID
     * @param sourceIDs
     */
    void updateMergedOrderItems(@Param("target") String targetID, @Param("sources") List<String> sourceIDs);


    /**
     * Insert platform order content from mabang side,
     * OrderItem doest not need to provide uuid.
     * sku erp code will be replaced by sku ID.
     *
     * @param orders the order content to insert
     */
    int insertOrdersFromMabang(@Param("orders") List<Order> orders);

    /**
     * Insert platform order content from mabang side,
     * OrderItem doest not need to provide uuid.
     * sku erp code will be replaced by sku ID.
     */
    int insertOrderItemsFromMabang(@Param("items") List<OrderItem> items);

    /**
     * Get platform order ID by erp code.
     *
     * @param platformOrderNumber erp code == platform order number
     */
    String findIdByErpCode(String platformOrderNumber);

    String findIdByErpId(String erpId);

    /**
     * Search orders that exist in DB.
     *
     * @param orderIDs platform order ID of the orders
     * @return image between platform order id and order entity with items as null
     */
    List<PlatformOrder> searchExistence(@Param("orders") Collection<String> orderIDs);

    /**
     * Delete bunch of order contents by their main id.
     *
     * @param mainIDs main identifiers of content to delete
     * @return number of row effected.
     */
    int batchDeleteByMainID(@Param("mainIDs") List<String> mainIDs);

    int batchUpdateById(@Param("orders") List<Order> orders);

    /**
     * Batch update erp status of order contents
     *
     * @param orderIDs   Order IDs
     * @param statusCode Status code
     */
    void batchUpdateErpStatusByMainId(@Param("orderIDs") List<String> orderIDs, @Param("statusCode") String statusCode);
}
