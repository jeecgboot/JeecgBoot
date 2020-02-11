package org.jeecg.modules.shza.mafk.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 受害人
 * @Author: jeecg-boot
 * @Date:   2020-02-11
 * @Version: V1.0
 */
@Data
@TableName("zz_mafk_person")
public class ZzMafkPerson implements Serializable {
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
	/**命案主表ID*/
	private java.lang.String zzMafkId;
	/**类型*/
	@Excel(name = "类型（1受害人，2嫌疑人）", width = 15)
	private java.lang.Integer type;
	/**姓名*/
	@Excel(name = "姓名", width = 15)
	private java.lang.String name;
	/**性别*/
	@Excel(name = "性别", width = 15)
	private java.lang.String sex;
	/**证件类型*/
	@Excel(name = "证件类型", width = 15)
	private java.lang.String zjlx;
	/**证件号*/
	@Excel(name = "证件号", width = 15)
	private java.lang.String zjh;
	/**国籍*/
	@Excel(name = "国籍", width = 15)
	private java.lang.String gj;
	/**民族*/
	@Excel(name = "民族", width = 15)
	private java.lang.String mz;
	/**生日*/
	@Excel(name = "生日", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	private java.util.Date birthday;
}
