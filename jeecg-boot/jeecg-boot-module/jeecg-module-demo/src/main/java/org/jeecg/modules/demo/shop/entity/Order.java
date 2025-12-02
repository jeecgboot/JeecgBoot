package org.jeecg.modules.demo.shop.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单实体
 * @Author: chenrui
 * @Date: 2025-11-06
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    
    /**
     * 订单ID
     */
    private String id;
    
    /**
     * 商品ID
     */
    private String productId;
    
    /**
     * 商品名称
     */
    private String productName;
    
    /**
     * 购买数量
     */
    private Integer quantity;
    
    /**
     * 单价
     */
    private BigDecimal unitPrice;
    
    /**
     * 总金额
     */
    private BigDecimal totalAmount;
    
    /**
     * 订单状态: pending-待支付, paid-已支付, cancelled-已取消
     */
    private String status;
    
    /**
     * 创建时间
     */
    private Date createTime;
    
    /**
     * 用户信息(可选)
     */
    private String userId;
}
