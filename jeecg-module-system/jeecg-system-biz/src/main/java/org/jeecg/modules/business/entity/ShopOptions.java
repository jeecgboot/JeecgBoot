package org.jeecg.modules.business.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.jeecg.common.aspect.annotation.Dict;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 客户选项列表
 * @Author: jeecg-boot
 * @Date:   2025-06-12
 * @Version: V1.0
 */
@Data
@TableName("shop_options")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="shop_options对象", description="客户选项列表")
public class ShopOptions implements Serializable {
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
	/**店铺ID*/
    @Excel(name = "shop id", width = 15, dictTable = "shop", dicText = "erp_code", dicCode = "id")
    @ApiModelProperty(value = "店铺ID")
    @Dict(dictTable = "shop", dicText = "erp_code", dicCode = "id")
    private java.lang.String shopId;
	/**是否用余额*/
	@Excel(name = "是否使用余额", width = 15)
    @ApiModelProperty(value = "是否使用余额")
    private java.lang.Boolean useBalance;
	/**是否可发票上显示账户余额*/
	@Excel(name = "是否可发票上显示账户余额", width = 15)
    @ApiModelProperty(value = "是否可发票上显示账户余额")
    private java.lang.Boolean showBalance;
	/**余额阈值 default: -1.00 no limit, 0.00 if balance must be positive*/
	@Excel(name = "余额阈值", width = 15)
    @ApiModelProperty(value = "余额阈值")
    private java.lang.Integer balanceThreshold;
	/**是否定时自动开票*/
	@Excel(name = "是否定时自动开票", width = 15)
    @ApiModelProperty(value = "是否定时自动开票")
    private java.lang.Boolean isAutoInvoice;
	/**是否按订单时间顺序自动开票, only if threshhold >= 0*/
	@Excel(name = "是否按订单时间顺序自动开票", width = 15)
    @ApiModelProperty(value = "是否按订单时间顺序自动开票")
    private java.lang.Boolean isChronologicalOrder;
	/**是否每周五 系统统一开票*/
	@Excel(name = "是否每周五系统统一开票", width = 15)
    @ApiModelProperty(value = "是否每周五系统统一开票")
    private java.lang.Boolean isBreakdownInvoice;
	/**星期五自动开票是否开P+L, default: 0 = no only L,1 = P+L*/
	@Excel(name = "星期五自动开票是否开P+L, default: 0 = no only L,1 = P+L", width = 15)
    @ApiModelProperty(value = "星期五自动开票是否开P+L, default: 0 = no only L,1 = P+L")
    private java.lang.Boolean isCompleteInvoice;
	/**是否客户可以自己开票*/
	@Excel(name = "是否客户可以自己开票", width = 15)
    @ApiModelProperty(value = "是否客户可以自己开票")
    private java.lang.Boolean canSelfInvoice;
	/**是否可以开P票*/
	@Excel(name = "是否可以开P票", width = 15)
    @ApiModelProperty(value = "是否可以开P票")
    private java.lang.Boolean canSelfP;
	/**是否可以开L票*/
	@Excel(name = "是否可以开L票", width = 15)
    @ApiModelProperty(value = "是否可以开L票")
    private java.lang.Boolean canSelfL;
	/**是否可以开P+L票*/
	@Excel(name = "是否可以开P+L票", width = 15)
    @ApiModelProperty(value = "是否可以开P+L票")
    private java.lang.Boolean canSelfPL;
	/**是否忽略库存数(7xxxx)*/
	@Excel(name = "是否忽略库存数(7xxxx)", width = 15)
    @ApiModelProperty(value = "是否忽略库存数(7xxxx)")
    private java.lang.Boolean isSelfIgnoreStock;
	/**是否客户做库存*/
	@Excel(name = "是否客户做库存", width = 15)
    @ApiModelProperty(value = "是否客户做库存")
    private java.lang.Boolean hasStock;
	/**是否开物流票写备注*/
	@Excel(name = "是否开物流票写备注", width = 15)
    @ApiModelProperty(value = "是否开物流票写备注")
    private java.lang.Boolean hasShippingInvoiceRemark;
}
