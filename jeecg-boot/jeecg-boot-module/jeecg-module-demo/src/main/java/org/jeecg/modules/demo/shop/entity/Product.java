package org.jeecg.modules.demo.shop.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 商品实体
 * @Author: chenrui
 * @Date: 2025-11-06
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    
    /**
     * 商品ID
     */
    private String id;
    
    /**
     * 商品名称
     */
    private String name;
    
    /**
     * 商品价格
     */
    private BigDecimal price;
    
    /**
     * 商品分类
     */
    private String category;
    
    /**
     * 商品描述
     */
    private String description;
    
    /**
     * 库存数量
     */
    private Integer stock;
}
