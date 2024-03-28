package org.jeecg.modules.business.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.jeecg.modules.business.domain.api.mabang.purDoGetProvider.ProviderData;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: provider
 * @Author: jeecg-boot
 * @Date:   2024-02-14
 * @Version: V1.0
 */
@Data
@TableName("provider")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="provider对象", description="provider")
public class Provider implements Serializable {
    private static final long serialVersionUID = 1L;

    /**id*/
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "id")
    private java.lang.String id;
    /**创建人*/
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
    /**创建日期*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
    /**更新人*/
    @ApiModelProperty(value = "更新人")
    private java.lang.String updateBy;
    /**更新日期*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
    /**ID from Mabang*/
    @Excel(name = "ID from Mabang", width = 15)
    @ApiModelProperty(value = "ID from Mabang")
    private java.lang.String idMabang;
    /**供应商编号*/
    @Excel(name = "供应商编号", width = 15)
    @ApiModelProperty(value = "供应商编号")
    private java.lang.Integer flag;
    /**供应商名称*/
    @Excel(name = "供应商名称", width = 15)
    @ApiModelProperty(value = "供应商名称")
    private java.lang.String provider;
    /**供应商类型: 1 1688 2 淘宝 33 拼多多 38 淘供销 43 淘供销（淘宝天猫） 0 普通 */
    @Excel(name = "供应商类型: 1 1688 2 淘宝 33 拼多多 38 淘供销 43 淘供销（淘宝天猫） 0 普通 ", width = 15)
    @ApiModelProperty(value = "供应商类型: 1 1688 2 淘宝 33 拼多多 38 淘供销 43 淘供销（淘宝天猫） 0 普通 ")
    private java.lang.Integer providerType;
    /**供应商链接*/
    @Excel(name = "供应商链接", width = 15)
    @ApiModelProperty(value = "供应商链接")
    private java.lang.String linkAddress;
    /**联系QQ*/
    @Excel(name = "联系QQ", width = 15)
    @ApiModelProperty(value = "联系QQ")
    private java.lang.String contactQq;
    /**联系旺旺*/
    @Excel(name = "联系旺旺", width = 15)
    @ApiModelProperty(value = "联系旺旺")
    private java.lang.String contactAliw;
    /**联系人*/
    @Excel(name = "联系人", width = 15)
    @ApiModelProperty(value = "联系人")
    private java.lang.String contactPerson;
    /**联系电话*/
    @Excel(name = "联系电话", width = 15)
    @ApiModelProperty(value = "联系电话")
    private java.lang.String contactTel;
    /**采购员编号*/
    @Excel(name = "采购员编号", width = 15)
    @ApiModelProperty(value = "采购员编号")
    private java.lang.String buyerId;
    /**采购员名称*/
    @Excel(name = "采购员名称", width = 15)
    @ApiModelProperty(value = "采购员名称")
    private java.lang.String buyerName;
    /**付款方式:1到付,2分期付款,3现付,4周结,5月结,6账期支付*/
    @Excel(name = "付款方式:1到付,2分期付款,3现付,4周结,5月结,6账期支付", width = 15)
    @ApiModelProperty(value = "付款方式:1到付,2分期付款,3现付,4周结,5月结,6账期支付")
    private java.lang.Integer paymentType;
    /**联系地址(省)*/
    @Excel(name = "联系地址(省)", width = 15)
    @ApiModelProperty(value = "联系地址(省)")
    private java.lang.String contactProvince;
    /**联系地址*/
    @Excel(name = "联系地址", width = 15)
    @ApiModelProperty(value = "联系地址")
    private java.lang.String contactAddress;
    /**收款人*/
    @Excel(name = "收款人", width = 15)
    @ApiModelProperty(value = "收款人")
    private java.lang.String payee;
    /**收款账号*/
    @Excel(name = "收款账号", width = 15)
    @ApiModelProperty(value = "收款账号")
    private java.lang.String receiveAccount;
    /**分销:1是2否*/
    @Excel(name = "分销:1是2否", width = 15)
    @ApiModelProperty(value = "分销:1是2否")
    private java.lang.Integer isDistribution;
    /**财务编码*/
    @Excel(name = "财务编码", width = 15)
    @ApiModelProperty(value = "财务编码")
    private java.lang.String financialcode;

    public Provider providerFromData(ProviderData data) {
        this.createTime = data.getCreateTime();
        this.createBy = "Mabang";
        this.idMabang = data.getId();
        this.flag = data.getFlag();
        this.provider = data.getProvider();
        this.providerType = data.getProviderType();
        this.linkAddress = data.getLinkAddress() == null ? null : data.getLinkAddress().isEmpty() ? null : data.getLinkAddress();
        this.contactQq = data.getContactQq() == null ? null : data.getContactQq().isEmpty() ? null : data.getContactQq();
        this.contactAliw = data.getContactAliw() == null ? null : data.getContactAliw().isEmpty() ? null : data.getContactAliw();
        this.contactPerson = data.getContactPerson() == null ? null : data.getContactPerson().isEmpty() ? null : data.getContactPerson();
        this.contactTel = data.getContactTel() == null ? null : data.getContactTel().isEmpty() ? null : data.getContactTel();
        this.buyerId = data.getBuyerId() == null ? null : data.getBuyerId().isEmpty() ? null : data.getBuyerId();
        this.buyerName = data.getBuyerName() == null ? null : data.getBuyerName().isEmpty() ? null : data.getBuyerName();
        this.paymentType = data.getPaymentType();
        this.contactProvince = data.getContactProvince() == null ? null : data.getContactProvince().isEmpty() ? null : data.getContactProvince();
        this.contactAddress = data.getContactAddress() == null ? null : data.getContactAddress().isEmpty() ? null : data.getContactAddress();
        this.payee = data.getPayee() == null ? null : data.getPayee().isEmpty() ? null : data.getPayee();
        this.receiveAccount = data.getReceiveAccount() == null ? null : data.getReceiveAccount().isEmpty() ? null : data.getReceiveAccount();
        this.isDistribution = data.getIsDistribution();
        this.financialcode = data.getFinancialcode() == null ? null : data.getFinancialcode().isEmpty() ? null : data.getFinancialcode();
        return this;
    }
}
