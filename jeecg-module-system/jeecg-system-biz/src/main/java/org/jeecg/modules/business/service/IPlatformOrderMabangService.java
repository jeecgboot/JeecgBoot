package org.jeecg.modules.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.business.domain.api.mabang.getorderlist.Order;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * Services related to operations on {@code Order} entity
 */
@Service
public interface IPlatformOrderMabangService extends IService<Order> {
    /**
     * Save orders to DB from mabang api.
     *
     * @param orders orders to save.
     */
    void saveOrderFromMabang(List<Order> orders);

    /**
     * Update merged platform order date by data from mabang.
     * <p>
     * This function updates both corresponding platform order and its content.
     *
     * @param mergedOrder      order as merge target
     * @param sourceOrderErpId erp IDs of source orders that are merged
     */
    void updateMergedOrderFromMabang(Order mergedOrder, Collection<String> sourceOrderErpId);

}
