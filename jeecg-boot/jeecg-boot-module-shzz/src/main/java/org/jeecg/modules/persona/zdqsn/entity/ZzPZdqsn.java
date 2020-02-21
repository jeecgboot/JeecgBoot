package org.jeecg.modules.persona.zdqsn.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 重点青少年人员
 * @Author: jeecg-boot
 * @Date:   2020-02-21
 * @Version: V1.0
 */
@Data
@TableName("zz_p_zdqsn")
public class ZzPZdqsn implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
	/**创建时间*/
	@Excel(name = "创建时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
	/**更新人*/
	@Excel(name = "更新人", width = 15)
    @ApiModelProperty(value = "更新人")
    private java.lang.String updateBy;
	/**更新时间*/
	@Excel(name = "更新时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "更新时间")
    private java.util.Date updateTime;
	/**所属部门*/
	@Excel(name = "所属部门", width = 15)
    @ApiModelProperty(value = "所属部门")
    private java.lang.String sysOrgCode;
	/**姓名*/
	@Excel(name = "姓名", width = 15)
    @ApiModelProperty(value = "姓名")
    private java.lang.String name;
	/**身份证*/
	@Excel(name = "身份证", width = 15)
    @ApiModelProperty(value = "身份证")
    private java.lang.String sfz;
	/**曾用名*/
	@Excel(name = "曾用名", width = 15)
    @ApiModelProperty(value = "曾用名")
    private java.lang.String oldName;
	/**性别*/
	@Excel(name = "性别", width = 15)
    @ApiModelProperty(value = "性别")
    private java.lang.String sex;
	/**照片*/
	@Excel(name = "照片", width = 15)
    @ApiModelProperty(value = "照片")
    private java.lang.String photo;
	/**出生日期*/
	@Excel(name = "出生日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "出生日期")
    private java.util.Date birthday;
	/**民族*/
	@Excel(name = "民族", width = 15)
    @ApiModelProperty(value = "民族")
    private java.lang.String zm;
	/**籍贯*/
	@Excel(name = "籍贯", width = 15)
    @ApiModelProperty(value = "籍贯")
    private java.lang.String jg;
	/**婚姻状况*/
	@Excel(name = "婚姻状况", width = 15)
    @ApiModelProperty(value = "婚姻状况")
    private java.lang.String hyzk;
	/**政治面貌*/
	@Excel(name = "政治面貌", width = 15)
    @ApiModelProperty(value = "政治面貌")
    private java.lang.String zzmm;
	/**学历*/
	@Excel(name = "学历", width = 15)
    @ApiModelProperty(value = "学历")
    private java.lang.String xl;
	/**宗教信仰*/
	@Excel(name = "宗教信仰", width = 15)
    @ApiModelProperty(value = "宗教信仰")
    private java.lang.String zjxy;
	/**职业类别*/
	@Excel(name = "职业类别", width = 15)
    @ApiModelProperty(value = "职业类别")
    private java.lang.String zylb;
	/**职业*/
	@Excel(name = "职业", width = 15)
    @ApiModelProperty(value = "职业")
    private java.lang.String zy;
	/**户籍门（楼）祥*/
	@Excel(name = "户籍门（楼）祥", width = 15)
    @ApiModelProperty(value = "户籍门（楼）祥")
    private java.lang.String hjxq;
	/**现住门（楼）祥*/
	@Excel(name = "现住门（楼）祥", width = 15)
    @ApiModelProperty(value = "现住门（楼）祥")
    private java.lang.String xzdxq;
	/**联系方式*/
	@Excel(name = "联系方式", width = 15)
    @ApiModelProperty(value = "联系方式")
    private java.lang.String lxfs;
	/**人员类型*/
	@Excel(name = "人员类型", width = 15)
    @ApiModelProperty(value = "人员类型")
    private java.lang.String rylx;
	/**家庭情况*/
	@Excel(name = "家庭情况", width = 15)
    @ApiModelProperty(value = "家庭情况")
    private java.lang.String jtqk;
	/**监护人身份证*/
	@Excel(name = "监护人身份证", width = 15)
    @ApiModelProperty(value = "监护人身份证")
    private java.lang.String jhrSfz;
	/**监护人姓名*/
	@Excel(name = "监护人姓名", width = 15)
    @ApiModelProperty(value = "监护人姓名")
    private java.lang.String jhrName;
	/**与监护人关系*/
	@Excel(name = "与监护人关系", width = 15)
    @ApiModelProperty(value = "与监护人关系")
    private java.lang.String jhrGx;
	/**监护人电话*/
	@Excel(name = "监护人电话", width = 15)
    @ApiModelProperty(value = "监护人电话")
    private java.lang.String jhrPhone;
	/**监护人住址*/
	@Excel(name = "监护人住址", width = 15)
    @ApiModelProperty(value = "监护人住址")
    private java.lang.String jhrZz;
	/**是否违法犯罪*/
	@Excel(name = "是否违法犯罪", width = 15)
    @ApiModelProperty(value = "是否违法犯罪")
    private java.lang.String sfwffz;
	/**违法犯罪情况*/
	@Excel(name = "违法犯罪情况", width = 15)
    @ApiModelProperty(value = "违法犯罪情况")
    private java.lang.String wffzqk;
	/**帮扶人姓名*/
	@Excel(name = "帮扶人姓名", width = 15)
    @ApiModelProperty(value = "帮扶人姓名")
    private java.lang.String bfrName;
	/**帮扶人电话*/
	@Excel(name = "帮扶人电话", width = 15)
    @ApiModelProperty(value = "帮扶人电话")
    private java.lang.String bfrPhone;
	/**帮扶手段*/
	@Excel(name = "帮扶手段", width = 15)
    @ApiModelProperty(value = "帮扶手段")
    private java.lang.String bfsd;
	/**帮扶情况*/
	@Excel(name = "帮扶情况", width = 15)
    @ApiModelProperty(value = "帮扶情况")
    private java.lang.String bfqk;
}
