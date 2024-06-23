package org.jeecg.modules.test.seata.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * @Description: 库存请求对象
 * @author: zyf
 * @date: 2022/01/24
 * @version: V1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReduceStockRequest {

    private Long productId;
    private Integer amount;
}