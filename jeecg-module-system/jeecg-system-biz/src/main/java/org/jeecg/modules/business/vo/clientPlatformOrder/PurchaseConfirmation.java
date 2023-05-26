package org.jeecg.modules.business.vo.clientPlatformOrder;

import lombok.Data;
import org.jeecg.modules.business.entity.OrderContentDetail;
import org.jeecg.modules.business.entity.ShippingFeesWaiver;
import org.jeecg.modules.business.vo.clientPlatformOrder.section.ClientInfo;
import org.jeecg.modules.business.vo.clientPlatformOrder.section.PurchaseDetail;
import org.jeecg.modules.business.vo.clientPlatformOrder.section.OrdersStatisticData;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Confirmation data set of orders
 */
@Data
public class PurchaseConfirmation {
    private final ClientInfo clientInfo;

    private final OrdersStatisticData data;

    private final List<PurchaseDetail> voPurchaseDetails;

    public PurchaseConfirmation(ClientInfo clientInfo, List<OrderContentDetail> details,
                                Map<ShippingFeesWaiver, List<String>> shippingFeesWaiverMap) {
        this.clientInfo = clientInfo;
        this.data = OrdersStatisticData.makeData(details, shippingFeesWaiverMap);
        this.voPurchaseDetails = details.stream().map(
                d -> (new PurchaseDetail(d.getSkuDetail().getSkuId(), d.getSkuDetail().getErpCode(),
                        d.getSkuDetail().getImageSource(), d.getSkuDetail().getNamEn(), d.getSkuDetail().getNameZh(), d.getQuantity(), d.unitPrice()))
        ).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return String.format("Client information: %s, data: %s, purchase detail: %s", clientInfo.toString(), data.toString(), voPurchaseDetails.toString());
    }
}
