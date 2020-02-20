package org.jeecg.modules.persona.hjrk.vo;

import java.util.List;

import org.jeecg.modules.persona.hjrk.entity.ZzPersona;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Description: 户籍人口
 * @Author: jeecg-boot
 * @Date:   2020-02-20
 * @Version: V1.0
 */
@Data
public class ZzPHjrkPage {
	
	/**主键*/
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
	/**人户一致标识*/
	@Excel(name = "人户一致标识", width = 15)
	private java.lang.String rhyz;
	/**户号*/
	@Excel(name = "户号", width = 15)
	private java.lang.String hh;
	/**户主身份证*/
	@Excel(name = "户主身份证", width = 15)
	private java.lang.String hzSfz;
	/**户主姓名*/
	@Excel(name = "户主姓名", width = 15)
	private java.lang.String hzName;
	/**与户主关系*/
	@Excel(name = "与户主关系", width = 15)
	private java.lang.String hzGx;
	/**户主联系方式*/
	@Excel(name = "户主联系方式", width = 15)
	private java.lang.String hzLxfs;
	
	@ExcelCollection(name="综治人口")
	private List<ZzPersona> zzPersonaList;	
	
}
