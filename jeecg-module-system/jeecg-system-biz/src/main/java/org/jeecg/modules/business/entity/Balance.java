package org.jeecg.modules.business.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: balance
 * @Author: jeecg-boot
 * @Date:   2023-10-12
 * @Version: V1.0
 */
@Data
@TableName("balance")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="balance对象", description="balance")
public class Balance implements Serializable {
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
	/**client ID*/
	@Excel(name = "client ID", width = 15)
    @ApiModelProperty(value = "client ID")
    private java.lang.String clientId;
	/**currency ID*/
	@Excel(name = "currency ID", width = 15)
    @ApiModelProperty(value = "currency ID")
    private java.lang.String currencyId;
	/**transaction type*/
	@Excel(name = "transaction type", width = 15)
    @ApiModelProperty(value = "transaction type")
    private java.lang.String operationType;
	/**transaction ID*/
	@Excel(name = "transaction ID", width = 15)
    @ApiModelProperty(value = "transaction ID")
    private java.lang.String operationId;
	/**balance amount*/
	@Excel(name = "balance amount", width = 15)
    @ApiModelProperty(value = "balance amount")
    private java.math.BigDecimal amount;

    public Balance() {

    }

    public Balance(String id,
                   String createBy,
                   Date createTime,
                   String updateBy,
                   Date updateTime,
                   String clientId,
                   String currencyId,
                   String operationType,
                   String operationId,
                   BigDecimal amount) {
        this.id = id;
        this.createBy = createBy;
        this.createTime = createTime;
        this.updateBy = updateBy;
        this.updateTime = updateTime;
        this.clientId = clientId;
        this.currencyId = currencyId;
        this.operationType = operationType;
        this.operationId = operationId;
        this.amount = amount;
    }
    public static Balance of (String username,
                              String clientId,
                              String currencyId,
                              String operationType,
                              String operationId,
                              BigDecimal amount) {
        return new Balance(null, username, new Date(), null, null, clientId, currencyId, operationType, operationId, amount);
    }
}
