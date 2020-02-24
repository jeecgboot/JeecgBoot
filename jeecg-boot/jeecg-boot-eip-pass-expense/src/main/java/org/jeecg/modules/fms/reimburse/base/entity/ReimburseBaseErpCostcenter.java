package org.jeecg.modules.fms.reimburse.base.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;

/**
 * @Description: reimburse_base_erp_costcenter
 * @Author: jeecg-boot
 * @Date:   2020-02-11
 * @Version: V1.0
 */
@Data
@TableName("reimburse_base_erp_costcenter")
public class ReimburseBaseErpCostcenter implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
    private java.lang.String id;
	/**ERP成本中心代码*/
	@Excel(name = "ERP成本中心代码", width = 15)
    private java.lang.String erpOrgCode;
	/**ERP成本中心名称*/
	@Excel(name = "ERP成本中心名称", width = 15)
    private java.lang.String erpOrgName;
	/**ERP成本中心类型代码*/
	@Excel(name = "ERP成本中心类型代码", width = 15)
    private java.lang.Integer costcenterTypeCode;
	/**ERP成本类型名称*/
	@Excel(name = "ERP成本类型名称", width = 15)
    private java.lang.String costcenterTypeName;
	/**成本中心所属公司代码*/
	@Excel(name = "成本中心所属公司代码", width = 15)
    private java.lang.String erpCompanyCode;
	/**成本中心所属公司名称*/
	@Excel(name = "成本中心所属公司名称", width = 15)
    private java.lang.String erpCompanyName;
	/**成本中心所属OU代码*/
	@Excel(name = "成本中心所属OU代码", width = 15)
    private java.lang.String erpOuCode;
	/**成本中心所属OU名称*/
	@Excel(name = "成本中心所属OU名称", width = 15)
    private java.lang.String misOuName;
	/**ERP成本中心是否有效*/
	@Excel(name = "ERP成本中心是否有效", width = 15)
    private java.lang.String validFlag;
	/**创建时间*/
	@Excel(name = "创建时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private java.util.Date createTime;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
    private java.lang.String createBy;
	/**更新时间*/
	@Excel(name = "更新时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private java.util.Date updateTime;
	/**更新人*/
	@Excel(name = "更新人", width = 15)
    private java.lang.String updateBy;
}
