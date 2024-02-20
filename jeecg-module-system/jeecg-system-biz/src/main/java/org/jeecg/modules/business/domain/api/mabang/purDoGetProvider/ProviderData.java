package org.jeecg.modules.business.domain.api.mabang.purDoGetProvider;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("provider")
public class ProviderData {
    /**id*/
    @JSONField(name = "id")
    private java.lang.String id;
    /**创建人*/
    @JSONField(name = "createTime")
    private java.util.Date createTime;
    /**供应商编号*/
    @JSONField(name = "flag")
    private java.lang.Integer flag;
    /**供应商名称*/
    @JSONField(name = "provider")
    private java.lang.String provider;
    /**供应商类型: 1 1688 2 淘宝 33 拼多多 38 淘供销 43 淘供销（淘宝天猫） 0 普通 */
    @JSONField(name = "providerType")
    private java.lang.Integer providerType;
    /**供应商链接*/
    @JSONField(name = "linkAddress")
    private java.lang.String linkAddress;
    /**联系QQ*/
    @JSONField(name = "contactQq")
    private java.lang.String contactQq;
    /**联系旺旺*/
    @JSONField(name = "contactAliw")
    private java.lang.String contactAliw;
    /**联系人*/
    @JSONField(name = "contactPerson")
    private java.lang.String contactPerson;
    /**联系电话*/
    @JSONField(name = "contactTel")
    private java.lang.String contactTel;
    /**采购员编号*/
    @JSONField(name = "buyerId")
    private java.lang.String buyerId;
    /**采购员名称*/
    @JSONField(name = "buyerName")
    private java.lang.String buyerName;
    /**付款方式:1到付,2分期付款,3现付,4周结,5月结,6账期支付*/
    @JSONField(name = "paymentType")
    private java.lang.Integer paymentType;
    /**联系地址(省)*/
    @JSONField(name = "contactProvince")
    private java.lang.String contactProvince;
    /**联系地址*/
    @JSONField(name = "contactAddress")
    private java.lang.String contactAddress;
    /**收款人*/
    @JSONField(name = "payee")
    private java.lang.String payee;
    /**收款账号*/
    @JSONField(name = "receiveAccount")
    private java.lang.String receiveAccount;
    /**分销:1是2否*/
    @JSONField(name = "isDistribution")
    private java.lang.Integer isDistribution;
    /**财务编码*/
    @JSONField(name = "financialcode")
    private java.lang.String financialcode;
    public String toString() {
        return "ID : " + this.id +
                "createTime : " + this.createTime +
                "flag : " + this.flag +
                "provider : " + this.provider +
                "providerType : " + this.providerType +
                "linkAddress : " + this.linkAddress +
                "contactQq : " + this.contactQq +
                "contactAliw : " + this.contactAliw +
                "contactPerson : " + this.contactPerson +
                "contactTel : " + this.contactTel +
                "buyerId : " + this.buyerId +
                "buyerName : " + this.buyerName +
                "paymentType : " + this.paymentType +
                "contactProvince : " + this.contactProvince +
                "contactAddress : " + this.contactAddress +
                "payee : " + this.payee +
                "receiveAccount : " + this.receiveAccount +
                "isDistribution : " + this.isDistribution +
                "financialcode : " + this.financialcode
                ;
    }
}
