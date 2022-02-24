package org.jeecg.modules.test.seata.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 余额请求对象
 * @author: zyf
 * @date: 2022/01/24
 * @version: V1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReduceBalanceRequest {

    private Long userId;
    private Integer price;
}