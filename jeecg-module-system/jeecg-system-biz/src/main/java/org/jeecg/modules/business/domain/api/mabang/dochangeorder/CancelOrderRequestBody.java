package org.jeecg.modules.business.domain.api.mabang.dochangeorder;

import org.jeecg.modules.business.domain.api.mabang.RequestBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *represents the body of a request to cancel orders
 * using the MaBang API (order-do-change-order).
 */
public class CancelOrderRequestBody implements RequestBody {

    private final List<String> platformOrderIds;
    private final String remark;

    public CancelOrderRequestBody(List<String> platformOrderIds, String remark) {
        this.platformOrderIds = platformOrderIds;
        this.remark = remark;
    }

    @Override
    public String api() { return "order-do-change-order"; }

    @Override
    public Map<String, Object> parameters() {
        Map<String, Object> p = new HashMap<>();
        if (platformOrderIds != null && !platformOrderIds.isEmpty()) {
            p.put("platformOrderId", String.join(",", platformOrderIds));
        }
        p.put("orderStatus", "5");
        if (remark != null && !remark.isEmpty()) p.put("remark", remark);
        return p;
    }
}
