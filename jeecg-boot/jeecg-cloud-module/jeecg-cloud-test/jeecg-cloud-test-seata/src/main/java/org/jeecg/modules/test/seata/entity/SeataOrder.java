package org.jeecg.modules.test.seata.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;
import org.jeecg.modules.test.seata.enums.OrderStatus;

/**
 * @Description: 订单
 * @author: zyf
 * @date: 2022/01/24
 * @version: V1.0
 */
@Builder
@Data
@TableName("p_order")
public class SeataOrder {

    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 商品ID
     */
    private Long productId;
    /**
     * 订单状态
     */
    private OrderStatus status;
    /**
     * 数量
     */
    private Integer amount;
    /**
     * 总金额
     */
    private Double totalPrice;
}
