package org.jeecg.modules.org.qfqz.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 群防群治组织
 * @Author: jeecg-boot
 * @Date:   2020-02-05
 * @Version: V1.0
 */
@Data
@TableName("zz_qfqz")
public class ZzQfqz implements Serializable {
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
	/**组织名称*/
	@Excel(name = "组织名称", width = 15)
    private java.lang.String orgName;
	/**组织类型*/
	@Excel(name = "组织类型", width = 15)
    private java.lang.String orgType;
	/**组织层级*/
	@Excel(name = "组织层级", width = 15)
    private java.lang.String orgLevel;
	/**业务指导部门*/
	@Excel(name = "业务指导部门", width = 15)
    private java.lang.String ywzdbm;
	/**人员数量*/
	@Excel(name = "人员数量", width = 15)
    private java.lang.Integer ryNum;
	/**主要职能*/
	@Excel(name = "主要职能", width = 15)
    private java.lang.String zyzn;
}
