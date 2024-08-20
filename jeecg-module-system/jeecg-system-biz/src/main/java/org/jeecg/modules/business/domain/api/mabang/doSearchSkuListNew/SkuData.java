package org.jeecg.modules.business.domain.api.mabang.doSearchSkuListNew;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;

@Data
@TableName("sku")
public class SkuData {
    /**
     * Primary key
     */
    @JSONField(deserialize = false)
    @TableId(type = IdType.ASSIGN_ID)
    private String id = IdWorker.getIdStr();

    @JSONField(name="id")
    private Integer stockSkuId;
    @JSONField(name="timeCreated")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String createdTime;
    @JSONField(name="stockSku")
    private String erpCode;
    @JSONField(name="nameCN")
    private String nameCN;
    @JSONField(name="nameEN")
    private String nameEN;
    /**
     * 1.自动创建; 2.待开发; 3.正常; 4.清仓; 5.停止销售
     */
    @JSONField(name="status")
    private Integer status;
    @JSONField(name="originalSku")
    private String originalSku;
    @JSONField(name="salePrice")
    private BigDecimal salePrice;
    @JSONField(name="declareValue")
    private BigDecimal declareValue;
    /**最新采购价*/
    @JSONField(name="purchasePrice")
    private BigDecimal purchasePrice;
    /**默认供应商名称,接口参数传showProvider才返回*/
    @JSONField(name="provider")
    private String provider;
    /**
     * if stockPicture is empty, we use it
     */
    @JSONField(name="stockPicture")
    private String stockPicture;
    /**
     * else we use salePicture
     */
    @JSONField(name="salePicture")
    private String salePicture;
    /**
     *  length, width and height are used in computing volume of item, but volume is now an obsolete field
     */
    @JSONField(name="length")
    private String length;
    @JSONField(name="width")
    private String width;
    @JSONField(name="height")
    private String height;
    @JSONField(name="weight")
    private Double weight;
    /**
     * saleRemark contains the weight
     */
    @JSONField(name="saleRemark")
    private String saleRemark;
    /**
     * 是否含电池:1是;2否
     */
    @JSONField(name="hasBattery")
    private Integer hasBattery;
    /**
     * 带磁:1:是;2:否
     */
    @JSONField(name="magnetic")
    private Integer magnetic ;
    /**
     * 是否赠品1是;2否
     */
    @JSONField(name="isGift")
    private Integer isGift;

    public SkuStatus getStatus() {
        return SkuStatus.fromCode(this.status);
    }
    public int getStatusValue() {
        return this.status;
    }
    public String toString() {
        return "ID : " + this.id +
                "\nStockSkuId : " + this.stockSkuId +
                "\nStockSku : " + this.erpCode +
                "\nStatus : " + this.status +
                "\nEn name : " + this.nameEN +
                "\nZh name : " + this.nameCN +
                "\nDeclared Value : " + this.declareValue +
                "\nSale Price : " + this.salePrice +
                "\nSale Remark (weight): " + this.saleRemark +
                "\nStock Picture : " + this.stockPicture +
                "\nsale Picture : " + this.salePicture +
                "\nBattery : " + this.hasBattery +
                "\nMagnetic : " + this.magnetic
                ;
    }
}
