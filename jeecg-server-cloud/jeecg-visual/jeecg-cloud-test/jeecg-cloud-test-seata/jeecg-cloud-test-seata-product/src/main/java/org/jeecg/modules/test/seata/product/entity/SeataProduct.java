package org.jeecg.modules.test.seata.product.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
/**
 * @Description: 产品
 * @author: zyf
 * @date: 2022/01/24
 * @version: V1.0
 */
@Data
@Builder
@TableName("product")
public class SeataProduct {

    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 价格
     */
    private BigDecimal price;
    /**
     * 库存
     */
    private Integer stock;

    private Date lastUpdateTime;
}