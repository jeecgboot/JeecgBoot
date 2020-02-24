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
 * @Description: reimburse_base_biztype_accnt_item_rela
 * @Author: jeecg-boot
 * @Date:   2020-02-10
 * @Version: V1.0
 */
@Data
@TableName("reimburse_base_biztype_accnt_item_rela")
public class ReimburseBaseBiztypeAccntItemRela implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**id*/
	@TableId(type = IdType.ID_WORKER_STR)
    private java.lang.String id;
	/**支出成本类型编号*/
	@Excel(name = "支出成本类型编号", width = 15)
    private java.lang.String costTypeCode;
	/**支出成本类型名称*/
	@Excel(name = "支出成本类型名称", width = 15)
    private java.lang.String costTypeName;
	/**成本中心类型:1、基层，2、管理部门*/
	@Excel(name = "成本中心类型:1、基层，2、管理部门", width = 15)
    private java.lang.String costcenterType;
	/**业务大类代码*/
	@Excel(name = "业务大类代码", width = 15)
    private java.lang.String bizTypeCode;
	/**业务大类名称*/
	@Excel(name = "业务大类名称", width = 15)
    private java.lang.String bizTypeName;
	/**业务小类代码*/
	@Excel(name = "业务小类代码", width = 15)
    private java.lang.String feeItemCode;
	/**业务小类名称*/
	@Excel(name = "业务小类名称", width = 15)
    private java.lang.String feeItemName;
	/**会计科目代码*/
	@Excel(name = "会计科目代码", width = 15)
    private java.lang.String accntCode;
	/**会计科目名称*/
	@Excel(name = "会计科目名称", width = 15)
    private java.lang.String accntName;
	/**计提会计科目代码*/
	@Excel(name = "计提会计科目代码", width = 15)
    private java.lang.String accrualAccntCode;
	/**计提会计科目名称*/
	@Excel(name = "计提会计科目名称", width = 15)
    private java.lang.String accrualAccntName;
	/**负债类科目代码*/
	@Excel(name = "负债类科目代码", width = 15)
    private java.lang.String liabilitiesAccntCode;
	/**负债类科目名称*/
	@Excel(name = "负债类科目名称", width = 15)
    private java.lang.String liabilitiesAccntName;
	/**会计科目映射类别：0-普通，1-预付，2借款，3还款*/
	@Excel(name = "会计科目映射类别：0-普通，1-预付，2借款，3还款", width = 15)
    private java.lang.String accntMapType;
	/**reserve1*/
	@Excel(name = "reserve1", width = 15)
    private java.lang.String reserve1;
	/**reserve2*/
	@Excel(name = "reserve2", width = 15)
    private java.lang.String reserve2;
	/**reserve3*/
	@Excel(name = "reserve3", width = 15)
    private java.lang.String reserve3;
	/**reserve4*/
	@Excel(name = "reserve4", width = 15)
    private java.lang.String reserve4;
	/**reserve5*/
	@Excel(name = "reserve5", width = 15)
    private java.lang.String reserve5;
	/**是否有效*/
	@Excel(name = "是否有效", width = 15)
    private java.lang.String enableFlag;
	/**生效开始时间*/
	@Excel(name = "生效开始时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private java.util.Date startValidTime;
	/**生效结束时间*/
	@Excel(name = "生效结束时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private java.util.Date endValidTime;
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
