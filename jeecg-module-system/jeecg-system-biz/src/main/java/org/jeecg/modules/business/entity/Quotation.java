package org.jeecg.modules.business.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.ArrayList;

import java.util.Collections;
import java.util.List;

/**
 * @Description: 询单报价表
 * @Author: jeecg-boot
 * @Date:   2026-01-29
 * @Version: V1.0
 */
@Data
@TableName("quotation")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="quotation对象", description="询单报价表")
public class Quotation implements Serializable {
    private static final long serialVersionUID = 1L;

    /**主键*/
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
    /**创建人*/
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
    /**创建日期*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
    /**更新人*/
    @ApiModelProperty(value = "更新人")
    private java.lang.String updateBy;
    /**更新日期*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
    /**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private java.lang.String sysOrgCode;
    /**状态（0=询单中，1=报价完成）*/
    @Excel(name = "状态（0=询单中，1=报价完成）", width = 15)
    @ApiModelProperty(value = "状态（0=询单中，1=报价完成）")
    private java.lang.String status;
    /**产品名称*/
    @Excel(name = "产品名称", width = 15)
    @ApiModelProperty(value = "产品名称")
    private java.lang.String productName;
    /**供货商SKU*/
    @Excel(name = "供货商SKU", width = 15)
    @ApiModelProperty(value = "供货商SKU")
    private java.lang.String supplierSku;
    /**最低起订量*/
    @Excel(name = "最低起订量", width = 15)
    @ApiModelProperty(value = "最低起订量")
    private java.lang.String moq;
    /**图片*/
    @Excel(name = "图片", width = 15)
    @ApiModelProperty(value = "图片")
    private java.lang.String photo;
    /**速卖通链接*/
    @Excel(name = "速卖通链接", width = 15)
    @ApiModelProperty(value = "速卖通链接")
    private java.lang.String customerUrl;
    /**客户售价(€)*/
    @Excel(name = "客户售价(€)", width = 15)
    @ApiModelProperty(value = "客户售价(€)")
    private java.math.BigDecimal customerPrice;
    /**国家*/
    @Excel(name = "国家", width = 15, dictTable = "country", dicText = "name_en", dicCode = "id")
    @Dict(dictTable = "country", dicText = "name_en", dicCode = "id")
    @ApiModelProperty(value = "国家")
    private java.lang.String country;
    /**自定义分类*/
    @Excel(name = "自定义分类", width = 15)
    @ApiModelProperty(value = "自定义分类")
    private java.lang.String category;
    /**物流路线*/
    @Excel(name = "物流路线", width = 15)
    @Dict(dictTable = "logistic_channel", dicText = "zh_name", dicCode = "id")
    @ApiModelProperty(value = "物流路线")
    private java.lang.String logisticChannel;
    /**时效*/
    @Excel(name = "时效", width = 15)
    @ApiModelProperty(value = "时效")
    private java.lang.String livraison;
    /**Prix_d_achat*/
    @Excel(name = "Prix_d_achat", width = 15)
    @ApiModelProperty(value = "Prix_d_achat")
    private java.math.BigDecimal prixAchat;
    /**物流费(€)*/
    @Excel(name = "物流费(€)", width = 15)
    @ApiModelProperty(value = "物流费(€)")
    private java.math.BigDecimal logisticsFee;
    /**总费用(€)*/
    @Excel(name = "总费用(€)", width = 15)
    @ApiModelProperty(value = "总费用(€)")
    private java.math.BigDecimal totalFee;
    /**采购价(¥)*/
    @Excel(name = "采购价(¥)", width = 15)
    @ApiModelProperty(value = "采购价(¥)")
    private java.math.BigDecimal purchasePriceRmb;
    /**尺码范围*/
    @Excel(name = "尺码范围", width = 15)
    @ApiModelProperty(value = "尺码范围")
    private java.lang.String sizeRange;
    /**国内物流(¥)*/
    @Excel(name = "国内物流(¥)", width = 15)
    @ApiModelProperty(value = "国内物流(¥)")
    private java.math.BigDecimal domesticShippingRmb;
    /**成本价 RMB*/
    @Excel(name = "成本价 RMB", width = 15)
    @ApiModelProperty(value = "成本价 RMB")
    private java.math.BigDecimal costRmb;
    /**成本价 EURO*/
    @Excel(name = "成本价 EURO", width = 15)
    @ApiModelProperty(value = "成本价 EURO")
    private java.math.BigDecimal costEur;
    /**售价 RMB*/
    @Excel(name = "售价 RMB", width = 15)
    @ApiModelProperty(value = "售价 RMB")
    private java.math.BigDecimal salePriceRmb;
    /**售价 EURO*/
    @Excel(name = "售价 EURO", width = 15)
    @ApiModelProperty(value = "售价 EURO")
    private java.math.BigDecimal salePriceEur;
    /**合作伙伴售价*/
    @Excel(name = "合作伙伴售价", width = 15)
    @ApiModelProperty(value = "合作伙伴售价")
    private java.lang.Integer partnerSalePrice;
    /**利润 RMB*/
    @Excel(name = "利润 RMB", width = 15)
    @ApiModelProperty(value = "利润 RMB")
    private java.math.BigDecimal profitRmb;
    /**利润 EURO*/
    @Excel(name = "利润 EURO", width = 15)
    @ApiModelProperty(value = "利润 EURO")
    private java.math.BigDecimal profitEur;
    /**利润率*/
    @Excel(name = "利润率", width = 15)
    @ApiModelProperty(value = "利润率")
    private java.math.BigDecimal margin;
    /**快递重量(g)*/
    @Excel(name = "快递重量(g)", width = 15)
    @ApiModelProperty(value = "快递重量(g)")
    private java.lang.Integer expressWeightG;
    /**毛重g*/
    @Excel(name = "毛重g", width = 15)
    @ApiModelProperty(value = "毛重g")
    private java.lang.Integer grossWeightG;
    /**包材重量g*/
    @Excel(name = "包材重量g", width = 15)
    @ApiModelProperty(value = "包材重量g")
    private java.lang.String packWeightG;
    /**链接*/
    @Excel(name = "链接", width = 15)
    @ApiModelProperty(value = "链接")
    private java.lang.String supplierLink;
    /**供货商名*/
    @Excel(name = "供货商名", width = 15)
    @ApiModelProperty(value = "供货商名")
    private java.lang.String supplierName;
    @Excel(name = "供货商价格", width = 15)
    @ApiModelProperty(value = "供货商价格")
    private String supplierPrice;
    /**是否现货*/
    @Excel(name = "是否现货", width = 15, dicCode = "yn")
    @Dict(dicCode = "yn")
    @ApiModelProperty(value = "是否现货")
    private java.lang.Integer inStock;
    /**产品尺码*/
    @Excel(name = "产品尺码", width = 15)
    @ApiModelProperty(value = "产品尺码")
    private java.lang.String productSize;
    /**产品尺码图*/
    @Excel(name = "产品尺码图", width = 15)
    @ApiModelProperty(value = "产品尺码图")
    private java.lang.String productSizeImg;
    /**是否抛重*/
    @Excel(name = "是否抛重", width = 15, dicCode = "yn")
    @Dict(dicCode = "yn")
    @ApiModelProperty(value = "是否抛重")
    private java.math.BigDecimal isVolumetric;
    /**包装长*/
    @Excel(name = "包装长", width = 15)
    @ApiModelProperty(value = "包装长")
    private java.math.BigDecimal packageLength;
    /**包装宽*/
    @Excel(name = "包装宽", width = 15)
    @ApiModelProperty(value = "包装宽")
    private java.math.BigDecimal packageWidth;
    /**包装高*/
    @Excel(name = "包装高", width = 15)
    @ApiModelProperty(value = "包装高")
    private java.math.BigDecimal packageHeight;
    /**规格说明*/
    @Excel(name = "规格说明", width = 15)
    @ApiModelProperty(value = "规格说明")
    private java.lang.String specification;
    /**颜色说明*/
    @Excel(name = "颜色说明", width = 15)
    @ApiModelProperty(value = "颜色说明")
    private java.lang.String colorNote;
    /**颜色图片*/
    @Excel(name = "颜色图片", width = 15)
    @ApiModelProperty(value = "颜色图片")
    private java.lang.String colorImg;
    /**外包装图*/
    @Excel(name = "外包装图", width = 15)
    @ApiModelProperty(value = "外包装图")
    private java.lang.String outerPackageImg;
    /**询单客户*/
    @Excel(name = "询单客户", width = 15, dictTable = "client", dicText = "internal_code", dicCode = "id")
    @Dict(dictTable = "client", dicText = "internal_code", dicCode = "id")
    @ApiModelProperty(value = "询单客户")
    private java.lang.String inquiryClient;
    /**询单销售*/
    @Excel(name = "询单销售", width = 15, dictTable = "sys_user", dicText = "username", dicCode = "id")
    @Dict(dictTable = "sys_user", dicText = "username", dicCode = "id")
    @ApiModelProperty(value = "询单销售")
    private java.lang.String inquirySales;
    /**询单链接*/
    @Excel(name = "询单链接", width = 15)
    @ApiModelProperty(value = "询单链接")
    private java.lang.String inquiryLink;
    /**询单国家*/
    @Excel(name = "询单国家", width = 15, dictTable = "country", dicText = "name_en", dicCode = "id")
    @Dict(dictTable = "country", dicText = "name_en", dicCode = "id")
    @ApiModelProperty(value = "询单国家")
    private java.lang.String inquiryCountry;
    /**预计销量*/
    @Excel(name = "预计销量", width = 15)
    @ApiModelProperty(value = "预计销量")
    private java.lang.Integer expectedSales;
    /**询单图片*/
    @Excel(name = "询单图片", width = 15)
    @ApiModelProperty(value = "询单图片")
    private java.lang.String inquiryPhoto;
    /**询单产品规格*/
    @Excel(name = "询单产品规格", width = 15)
    @ApiModelProperty(value = "询单产品规格")
    private java.lang.String inquirySpec;
    /**询单产品颜色*/
    @Excel(name = "询单产品颜色", width = 15)
    @ApiModelProperty(value = "询单产品颜色")
    private java.lang.String inquiryColor;
    /**附件*/
    @Excel(name = "附件", width = 15)
    @ApiModelProperty(value = "附件")
    private java.lang.String attachments;
    /**询单备注*/
    @Excel(name = "询单备注", width = 30)
    @ApiModelProperty(value = "询单备注")
    private java.lang.String inquiryRemark;

    @TableField(exist = false)
    private List<String> inquiryCountryList;

    //support two formats for output: 1) "id1,id2"   2) ["id1","id2"]
    @JsonGetter("inquiryCountryList")
    public List<String> getInquiryCountryAsList() {
        if (inquiryCountry == null || inquiryCountry.trim().isEmpty()) {
            return Collections.emptyList();
        }
        String[] arr = inquiryCountry.split(",");
        List<String> list = new ArrayList<String>(arr.length);
        for (String s : arr) {
            if (s != null) {
                String t = s.trim();
                if (!t.isEmpty()) list.add(t);
            }
        }
        return list;
    }

    /**
     * support two formats for input:
     * 1) ["id1","id2"]
     * 2) "id1,id2"
     * save as "id1,id2"
     */
    @JsonSetter("inquiryCountry")
    public void setInquiryCountryFlexible(JsonNode node) {
        if (node == null || node.isNull()) {
            this.inquiryCountry = null;
            return;
        }
        if (node.isArray()) {
            List<String> list = new ArrayList<String>();
            for (JsonNode n : node) {
                if (n != null && !n.isNull()) {
                    String v = n.asText();
                    if (v != null) {
                        v = v.trim();
                        if (!v.isEmpty()) list.add(v);
                    }
                }
            }
            this.inquiryCountry = list.isEmpty() ? null : joinComma(list);
            return;
        }
        String s = node.asText();
        if (s == null || s.trim().isEmpty()) {
            this.inquiryCountry = null;
            return;
        }
        String[] arr = s.split(",");
        List<String> list = new ArrayList<String>(arr.length);
        for (String item : arr) {
            if (item != null) {
                String t = item.trim();
                if (!t.isEmpty()) list.add(t);
            }
        }
        this.inquiryCountry = list.isEmpty() ? null : joinComma(list);
    }
    private String joinComma(List<String> list) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) sb.append(",");
            sb.append(list.get(i));
        }
        return sb.toString();
    }
}
