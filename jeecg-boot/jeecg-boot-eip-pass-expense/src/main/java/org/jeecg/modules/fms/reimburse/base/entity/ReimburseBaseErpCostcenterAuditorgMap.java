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
 * @Description: reimburse_base_erp_costcenter_auditorg_map
 * @Author: jeecg-boot
 * @Date:   2020-02-11
 * @Version: V1.0
 */
@Data
@TableName("reimburse_base_erp_costcenter_auditorg_map")
public class ReimburseBaseErpCostcenterAuditorgMap implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
    private java.lang.String id;
	/**审批组织代码*/
	@Excel(name = "审批组织代码", width = 15)
    private java.lang.String auditOrgCode;
	/**审批组织名称*/
	@Excel(name = "审批组织名称", width = 15)
    private java.lang.String auditOrgName;
	/**成本中心代码*/
	@Excel(name = "成本中心代码", width = 15)
    private java.lang.String erpOrgCode;
	/**成本中心名称*/
	@Excel(name = "成本中心名称", width = 15)
    private java.lang.String erpOrgName;
	/**映射是否有效*/
	@Excel(name = "映射是否有效", width = 15)
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
