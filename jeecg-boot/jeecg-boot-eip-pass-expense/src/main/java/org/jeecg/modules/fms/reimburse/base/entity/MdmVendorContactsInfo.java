package org.jeecg.modules.fms.reimburse.base.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import org.jeecg.common.aspect.annotation.Dict;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import java.util.Date;

/**
 * @Description: 主数据供应商联系人
 * @Author: jeecg-boot
 * @Date:   2020-01-17
 * @Version: V1.0
 */
@Data
@TableName("mdm_vendor_contacts_info")
public class MdmVendorContactsInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**编号*/
	@TableId(type = IdType.ID_WORKER_STR)
	private java.lang.String id;
	/**供应商公司 ID（地市公司组织 ID）*/
	@Excel(name = "供应商公司 ID（地市公司组织 ID）", width = 15)
	private java.lang.Integer vendorCompanyId;
	/**供应商 ID*/
	private java.lang.Integer vendorId;
	/**供应商编号（主数据编号）*/
	@Excel(name = "供应商编号（主数据编号）", width = 15)
	private java.lang.String mdCode;
	/**联系人 ID*/
	@Excel(name = "联系人 ID", width = 15)
	private java.lang.Integer vendorContactsId;
	/**联系人姓名*/
	@Excel(name = "联系人姓名", width = 15)
	private java.lang.String contactName;
	/**手机号码*/
	@Excel(name = "手机号码", width = 15)
	private java.lang.String contactCellphone;
	/**电话号码*/
	@Excel(name = "电话号码", width = 15)
	private java.lang.String contactPhone;
	/**传真*/
	@Excel(name = "传真", width = 15)
	private java.lang.String contactFax;
	/**电子邮箱*/
	@Excel(name = "电子邮箱", width = 15)
	private java.lang.String contactMail;
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
