package org.jeecg.modules.business.vo.clientPlatformOrder;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecg.modules.business.entity.ClientPlatformOrderContent;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @Description: Client platform order page list
 * @Author: Wenke
 * @Date:   2021-04-19
 * @Version: V1.0
 */
@Data
@ApiModel(value="Client platform order page object", description="Each row in the page platform order of client")
public class ClientPlatformOrderPage {

	/**主键*/
	@ApiModelProperty(value = "主键")
    private String id;

	/**店铺ID*/
	@Excel(name = "Shop name", width = 15, dictTable = "shop", dicText = "name", dicCode = "id")
    @Dict(dictTable = "shop", dicText = "name", dicCode = "id")
	@ApiModelProperty(value = "店铺ID")
    private String shopId;

	/**平台订单号码*/
	@ApiModelProperty(value = "平台订单号码")
    private String platformOrderId;
	/**平台订单交易号*/
	@Excel(name = "Order number", width = 15)
	@ApiModelProperty(value = "平台订单交易号")
    private String platformOrderNumber;
	/**物流跟踪号*/
	@Excel(name = "Tracking number", width = 15)
	@ApiModelProperty(value = "物流跟踪号")
    private String trackingNumber;
	/**订单交易时间*/
	@Excel(name = "Order placement time", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "订单交易时间")
    private Date orderTime;
	/**订单发货时间*/
	@Excel(name = "Shipping time", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "订单发货时间")
    private Date shippingTime;
	/**订单收件人*/
	@Excel(name = "Recipient", width = 15)
	@ApiModelProperty(value = "订单收件人")
    private String recipient;
	/**订单收件人国家*/
	@Excel(name = "Country", width = 15)
	@ApiModelProperty(value = "订单收件人国家")
    private String country;
	/**订单收件人邮编*/
	@Excel(name = "Postcode", width = 15)
	@ApiModelProperty(value = "订单收件人邮编")
    private String postcode;
	/**物流挂号费*/
	@Excel(name = "FRET fees", width = 15)
	@ApiModelProperty(value = "物流挂号费")
    private java.math.BigDecimal fretFee;
	/**物流发票号*/
	@Excel(name = "Shipping invoice number", width = 15, dictTable = "shipping_invoice", dicText = "invoice_number", dicCode = "id")
    @Dict(dictTable = "shipping_invoice", dicText = "invoice_number", dicCode = "id")
	@ApiModelProperty(value = "物流发票号")
    private String shippingInvoiceNumber;
	/**状态*/
	@Excel(name = "Status", width = 15, dictTable = "sku_status", dicText = "status_text", dicCode = "status_code")
	@Dict(dictTable = "sku_status", dicText = "status_text", dicCode = "status_code")
	@ApiModelProperty(value = "状态")
    private Integer status;

	@ExcelCollection(name="Order content")
	@ApiModelProperty(value = "平台订单内容")
	private List<ClientPlatformOrderContent> platformOrderContentList;

}
