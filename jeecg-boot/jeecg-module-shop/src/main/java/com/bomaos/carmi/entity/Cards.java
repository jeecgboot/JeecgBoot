package com.bomaos.carmi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * 卡密
 * Created by Panyoujie on 2021-03-28 00:33:15
 */
@Data
@ToString
@TableName("sys_cards")
public class Cards implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 自增ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 对应商品id
     */
    private Integer productId;

    /**
     * 卡密
     */
    private String cardInfo;

    /**
     * 卡密状态
     */
    private Integer status;

    /**
     * 售卡类型
     */
    private Integer sellType;

    /**
     * 总数
     */
    private Integer number;

    /**
     * 售出数量
     */
    private Integer sellNumber;

    /**
     * 创建时间
     */
    private Date createdAt;

    /**
     * 更新时间
     */
    private Date updatedAt;

}
