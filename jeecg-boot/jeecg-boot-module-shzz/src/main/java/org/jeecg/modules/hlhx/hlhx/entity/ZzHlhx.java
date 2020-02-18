package org.jeecg.modules.hlhx.hlhx.entity;

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
 * @Description: 护路护线
 * @Author: jeecg-boot
 * @Date:   2020-02-18
 * @Version: V1.0
 */
@Data
@TableName("zz_hlhx")
public class ZzHlhx implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
	/**创建日期*/
	@Excel(name = "创建日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
	/**更新人*/
	@Excel(name = "更新人", width = 15)
    @ApiModelProperty(value = "更新人")
    private java.lang.String updateBy;
	/**更新日期*/
	@Excel(name = "更新日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
	/**所属部门*/
	@Excel(name = "所属部门", width = 15)
    @ApiModelProperty(value = "所属部门")
    private java.lang.String sysOrgCode;
	/**线路名称*/
	@Excel(name = "线路名称", width = 15)
    @ApiModelProperty(value = "线路名称")
    private java.lang.String roadName;
	/**线路类型*/
	@Excel(name = "线路类型", width = 15)
    @ApiModelProperty(value = "线路类型")
    private java.lang.String roadType;
	/**隶属单位名称*/
	@Excel(name = "隶属单位名称", width = 15)
    @ApiModelProperty(value = "隶属单位名称")
    private java.lang.String lsdwName;
	/**隶属单位地址*/
	@Excel(name = "隶属单位地址", width = 15)
    @ApiModelProperty(value = "隶属单位地址")
    private java.lang.String lsdwAdress;
	/**联系方式*/
	@Excel(name = "联系方式", width = 15)
    @ApiModelProperty(value = "联系方式")
    private java.lang.String lsdwPhone;
	/**隶属单位负责人*/
	@Excel(name = "隶属单位负责人", width = 15)
    @ApiModelProperty(value = "隶属单位负责人")
    private java.lang.String lsdwFzr;
	/**负责人联系方式*/
	@Excel(name = "负责人联系方式", width = 15)
    @ApiModelProperty(value = "负责人联系方式")
    private java.lang.String fzrPhone;
	/**管辖单位名称*/
	@Excel(name = "管辖单位名称", width = 15)
    @ApiModelProperty(value = "管辖单位名称")
    private java.lang.String gxdwName;
	/**管辖单位地址*/
	@Excel(name = "管辖单位地址", width = 15)
    @ApiModelProperty(value = "管辖单位地址")
    private java.lang.String gxdwAdress;
	/**联系方式*/
	@Excel(name = "联系方式", width = 15)
    @ApiModelProperty(value = "联系方式")
    private java.lang.String gxdwPhone;
	/**分管治保负责人*/
	@Excel(name = "分管治保负责人", width = 15)
    @ApiModelProperty(value = "分管治保负责人")
    private java.lang.String fgzbFzr;
	/**联系方式*/
	@Excel(name = "联系方式", width = 15)
    @ApiModelProperty(value = "联系方式")
    private java.lang.String fgzbPhone;
	/**治安隐患等级*/
	@Excel(name = "治安隐患等级", width = 15)
    @ApiModelProperty(value = "治安隐患等级")
    private java.lang.String zayhdj;
	/**治安隐患情况*/
	@Excel(name = "治安隐患情况", width = 15)
    @ApiModelProperty(value = "治安隐患情况")
    private java.lang.String zayhqk;
}
