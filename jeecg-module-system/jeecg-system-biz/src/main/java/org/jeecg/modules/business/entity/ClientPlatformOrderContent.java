package org.jeecg.modules.business.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.io.Serializable;
import java.util.Objects;

/**
 * @Description: 客户平台订单内容表，列名英文化
 * @Author: Qiuyi LI
 * @Date:   2021-05-20
 * @Version: V1.0
 */
@ApiModel(value="platform_order_content对象", description="平台订单表")
@Data
@TableName("platform_order_content")
public class ClientPlatformOrderContent implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private String id;
	/**SKU ID*/
	@Excel(name = "SKU", width = 15, dictTable = "sku", dicText = "erp_code", dicCode = "id")
	@Dict(dictTable = "sku", dicText = "erp_code", dicCode = "id")
    @ApiModelProperty(value = "SKU ID")
    private String skuId;
	/**SKU数量*/
	@Excel(name = "SKU quantity", width = 15)
    @ApiModelProperty(value = "SKU数量")
    private Integer quantity;
	/**商品采购总费用*/
	@Excel(name = "Total purchasing cost", width = 15)
    @ApiModelProperty(value = "商品采购总费用")
    private java.math.BigDecimal purchaseFee;
	/**物流总费用*/
	@Excel(name = "Total shipping cost", width = 15)
    @ApiModelProperty(value = "物流总费用")
    private java.math.BigDecimal shippingFee;
	/**服务总费用*/
	@Excel(name = "Total service fees", width = 15)
    @ApiModelProperty(value = "服务总费用")
    private java.math.BigDecimal serviceFee;
	/**sku状态*/
    @ApiModelProperty(value = "SKU 状态")
    @Excel(name = "SKU status", width = 15, dictTable = "sku_status", dicText = "status_text", dicCode = "status_code")
    @Dict(dictTable = "sku_status", dicText = "status_text", dicCode = "status_code")
    private Integer status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientPlatformOrderContent content = (ClientPlatformOrderContent) o;
        return id.equals(content.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
