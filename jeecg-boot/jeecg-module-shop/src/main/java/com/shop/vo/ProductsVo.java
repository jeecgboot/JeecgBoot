package com.shop.vo;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
public class ProductsVo {

    /**
     * 自增ID
     */
    private Integer id;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 商品名称
     */
    private String classifyName;

    /**
     * 卡密数量
     */
    private Long cardMember;

    /**
     * 出售卡密数量
     */
    private Long sellCardMember;

    /**
     * 商品金额
     */
    private String price;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 商品链接
     */
    private String link;

    /**
     * 商品状态
     */
    private Integer status;

    /**
     * 商品详情
     */
    private String pdInfo;

    /**
     * 创建时间
     */
    private Date createdAt;

    /**
     * 更新时间
     */
    private Date updatedAt;

    /**
     * 删除时间
     */
    private Date deletedAt;

    /**
     * 分类ID
     */
    private Integer classifyId;

    /**
     * 首页截图
     */
    private String indexLogo;

    /**
     * 商品logo
     */
    private String imageLogo;

    /**
     * 批发功能
     */
    private Integer isWholesale;

    /**
     * 批发配置
     */
    private String wholesale;

    /**
     * 限制购买
     */
    private Integer restricts;

    /**
     * 优惠券
     */
    private Integer isCoupon;

    /**
     * 发货类型（0-自动，1-手动）
     */
    private Integer shipType;

    /**
     * 商品库存（人工发货类型生效）
     */
    private Integer inventory;

    /**
     * 销量
     */
    private Integer sales;

    /**
     * 是否开启密码查询
     */
    private Integer isPassword;

    /**
     * 是否开启自定义输入框
     */
    private Integer isCustomize;

    /**
     * 自定义输入框
     */
    private String customizeInput;

    /**
     * 售卡类型
     */
    private Integer sellType;

    /**
     * 是否开启提示
     */
    private Integer componentEnabled;

    /**
     * 提示类型
     */
    private Integer componentType;

    /**
     * 提示语
     */
    private String componentPoint;

    /**
     * 提示链接
     */
    private String componentUrl;
}
