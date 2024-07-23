package com.shop.common.core.utils;

import com.shop.entity.Orders;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RmbUtil {

    /**
     * 根据查询到订单数量统计订单的总金额
     *
     * @param ordersList
     * @return
     */
    public static Map getRmbCount(List<Orders> ordersList) {
        // 今天的收款金额
        BigDecimal decimal = new BigDecimal("0.00");
        Integer longTotal = 0;  // 今天的成交订单
        for (Orders orders : ordersList) {
            BigDecimal bigDecimal = new BigDecimal(orders.getMoney().toString());
            longTotal++;
            decimal = decimal.add(bigDecimal);
        }

        HashMap<String, Object> map = new HashMap<>();
        map.put("totalSumlong", decimal);
        map.put("longTotal", longTotal);

        return map;
    }

}
