package org.jeecg.modules.test.seata.order.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
/**
 * @Description: 订单请求对象
 * @author: zyf
 * @date: 2022/01/24
 * @version: V1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlaceOrderRequest {

    @NotNull
    private Long userId;

    @NotNull
    private Long productId;

    @NotNull
    private Integer count;
}