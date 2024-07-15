package com.vone.mq.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "pay_goods")
public class PayGoods {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    // 用户名
    private String username;

    // 名称
    private String name;

    // 描述
    private String memo;

    // 图片
    private String picture;

    // 发货内容
    private String sendContent;

    // 价格
    private Double price;

    // 数量
    private Integer number;

    //状态
    private Integer state;

    // 更新时间
    private Date updateDate;

    // 创建时间
    private Date createDate;

}
