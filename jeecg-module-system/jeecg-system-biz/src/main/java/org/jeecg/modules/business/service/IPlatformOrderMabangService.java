package org.jeecg.modules.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.jeecg.modules.business.domain.api.mabang.getorderlist.Order;
import org.jeecg.modules.business.domain.api.mabang.getorderlist.OrderListRequestBody;
import org.jeecg.modules.business.domain.api.mabang.orderUpdateOrderNewOrder.UpdateResult;
import org.jeecg.modules.business.vo.PlatformOrderOperation;
import org.jeecg.modules.business.vo.Response;
import org.jeecg.modules.business.vo.Responses;
import org.jeecg.modules.business.vo.ResponsesWithMsg;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * Services related to operations on {@code Order} entity
 */
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

    Responses suspendOrder(PlatformOrderOperation orderOperation);

    Responses cancelOrders(PlatformOrderOperation orderOperation);

    List<Order> getOrdersFromMabang(List<OrderListRequestBody> requests, ExecutorService executor);

    void clearLogisticChannel(List<Order> orders, ExecutorService executor);

    String stripAccents(String input);

    JSONObject syncOrdersFromMabang(List<String> platformOrderIds) throws JSONException;

    ResponsesWithMsg<String> editOrdersRemark(String invoiceNumber);

    ResponsesWithMsg<String> deleteOrderRemark(String invoiceNumber);

    Response<List<UpdateResult>, List<UpdateResult>> updateOrderStatusToPreparing(List<String> platformOrderIds);

    ResponsesWithMsg<String> updateReceiverPhone(String platformOrderId, String receiverPhone, String shopId);
}
