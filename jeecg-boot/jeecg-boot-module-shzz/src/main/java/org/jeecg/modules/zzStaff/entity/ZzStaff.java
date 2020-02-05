package org.jeecg.modules.zzStaff.entity;

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
 * @Description: 员工
 * @Author: jeecg-boot
 * @Date:   2020-02-05
 * @Version: V1.0
 */
@Data
@TableName("zz_staff")
public class ZzStaff implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
    private java.lang.String id;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
    private java.lang.String createBy;
	/**创建日期*/
	@Excel(name = "创建日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private java.util.Date createTime;
	/**更新人*/
	@Excel(name = "更新人", width = 15)
    private java.lang.String updateBy;
	/**更新日期*/
	@Excel(name = "更新日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private java.util.Date updateTime;
	/**所属部门*/
	@Excel(name = "所属部门", width = 15)
    private java.lang.String sysOrgCode;
	/**综治机构*/
	@Excel(name = "综治机构", width = 15)
    private java.lang.String zzOrg;
	/**姓名*/
	@Excel(name = "姓名", width = 15)
    private java.lang.String name;
	/**性别*/
	@Excel(name = "性别", width = 15)
    private java.lang.String sex;
	/**手机号*/
	@Excel(name = "手机号", width = 15)
    private java.lang.String phone;
	/**人员类型*/
	@Excel(name = "人员类型", width = 15)
    private java.lang.String type;
	/**民族*/
	@Excel(name = "民族", width = 15)
    private java.lang.String minZu;
	/**政治面貌*/
	@Excel(name = "政治面貌", width = 15)
    private java.lang.String zzmm;
	/**身份证*/
	@Excel(name = "身份证", width = 15)
    private java.lang.String sfz;
	/**生日*/
	@Excel(name = "生日", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private java.util.Date birthday;
	/**级别*/
	@Excel(name = "级别", width = 15)
    private java.lang.String lv;
	/**特长*/
	@Excel(name = "特长", width = 15)
    private java.lang.String speciality;
	/**职务*/
	@Excel(name = "职务", width = 15)
    private java.lang.String job;
	/**学历*/
	@Excel(name = "学历", width = 15)
    private java.lang.String education;
}
