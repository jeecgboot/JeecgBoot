package org.jeecg.modules.persona.lsry.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 留守人员附表
 * @Author: jeecg-boot
 * @Date:   2020-02-21
 * @Version: V1.0
 */
@Data
@TableName("zz_p_lsry_persona")
public class ZzPLsryPersona implements Serializable {
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
	/**主表id*/
	private java.lang.String zzPLsryId;
	/**姓名*/
	@Excel(name = "姓名", width = 15)
	private java.lang.String name;
	/**身份证*/
	@Excel(name = "身份证", width = 15)
	private java.lang.String sfz;
	/**健康状况*/
	@Excel(name = "健康状况", width = 15)
	private java.lang.String jkzk;
	/**留守人员关系*/
	@Excel(name = "留守人员关系", width = 15)
	private java.lang.String lsrygx;
	/**联系方式*/
	@Excel(name = "联系方式", width = 15)
	private java.lang.String lxfs;
	/**工作地址*/
	@Excel(name = "工作地址", width = 15)
	private java.lang.String gzdz;
}
