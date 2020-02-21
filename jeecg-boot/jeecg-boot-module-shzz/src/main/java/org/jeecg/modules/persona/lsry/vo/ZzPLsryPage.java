package org.jeecg.modules.persona.lsry.vo;

import java.util.List;

import org.jeecg.modules.persona.lsry.entity.ZzPLsryPersona;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Description: 留守人员
 * @Author: jeecg-boot
 * @Date:   2020-02-21
 * @Version: V1.0
 */
@Data
public class ZzPLsryPage {
	
	/**主键*/
	private java.lang.String id;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
	private java.lang.String createBy;
	/**创建日期*/
	@Excel(name = "创建日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	private java.util.Date createTime;
	/**更新人*/
	@Excel(name = "更新人", width = 15)
	private java.lang.String updateBy;
	/**更新日期*/
	@Excel(name = "更新日期", width = 15)
	private java.lang.String updateTime;
	/**所属部门*/
	@Excel(name = "所属部门", width = 15)
	private java.lang.String sysOrgCode;
	/**姓名*/
	@Excel(name = "姓名", width = 15)
	private java.lang.String name;
	/**身份证*/
	@Excel(name = "身份证", width = 15)
	private java.lang.String sfz;
	/**曾用名*/
	@Excel(name = "曾用名", width = 15)
	private java.lang.String oldName;
	/**性别*/
	@Excel(name = "性别", width = 15)
	private java.lang.String sex;
	/**照片*/
	@Excel(name = "照片", width = 15)
	private java.lang.String photo;
	/**出生日期*/
	@Excel(name = "出生日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	private java.util.Date birthday;
	/**民族*/
	@Excel(name = "民族", width = 15)
	private java.lang.String zm;
	/**籍贯*/
	@Excel(name = "籍贯", width = 15)
	private java.lang.String jg;
	/**婚姻状况*/
	@Excel(name = "婚姻状况", width = 15)
	private java.lang.String hyzk;
	/**政治面貌*/
	@Excel(name = "政治面貌", width = 15)
	private java.lang.String zzmm;
	/**学历*/
	@Excel(name = "学历", width = 15)
	private java.lang.String xl;
	/**宗教信仰*/
	@Excel(name = "宗教信仰", width = 15)
	private java.lang.String zjxy;
	/**职业类别*/
	@Excel(name = "职业类别", width = 15)
	private java.lang.String zylb;
	/**职业*/
	@Excel(name = "职业", width = 15)
	private java.lang.String zy;
	/**户籍门（楼）祥*/
	@Excel(name = "户籍门（楼）祥", width = 15)
	private java.lang.String hjxq;
	/**现住门（楼）祥*/
	@Excel(name = "现住门（楼）祥", width = 15)
	private java.lang.String xzdxq;
	/**联系方式*/
	@Excel(name = "联系方式", width = 15)
	private java.lang.String lxfs;
	/**健康状况*/
	@Excel(name = "健康状况", width = 15)
	private java.lang.String jkzk;
	/**个人年收入*/
	@Excel(name = "个人年收入", width = 15)
	private java.lang.String grnsr;
	/**家庭年收入*/
	@Excel(name = "家庭年收入", width = 15)
	private java.lang.String jtnsr;
	/**人户一致标识*/
	@Excel(name = "人户一致标识", width = 15)
	private java.lang.String rhyzbz;
	/**留守人员类型*/
	@Excel(name = "留守人员类型", width = 15)
	private java.lang.String lsrylx;
	/**困难及诉求*/
	@Excel(name = "困难及诉求", width = 15)
	private java.lang.String knjsq;
	/**帮扶情况*/
	@Excel(name = "帮扶情况", width = 15)
	private java.lang.String bfqk;
	
	@ExcelCollection(name="留守人员附表")
	private List<ZzPLsryPersona> zzPLsryPersonaList;	
	
}
