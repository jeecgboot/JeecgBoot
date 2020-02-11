package org.jeecg.modules.jjshzz.fgyzjjzz.entity;

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
 * @Description: 非公有制经济组织
 * @Author: jeecg-boot
 * @Date:   2020-02-11
 * @Version: V1.0
 */
@Data
@TableName("zz_fgyzjjzz")
public class ZzFgyzjjzz implements Serializable {
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
	/**工商执照注册号*/
	@Excel(name = "工商执照注册号", width = 15)
    @ApiModelProperty(value = "工商执照注册号")
    private java.lang.String gszzCode;
	/**企业名称*/
	@Excel(name = "企业名称", width = 15)
    @ApiModelProperty(value = "企业名称")
    private java.lang.String businessName;
	/**企业类别*/
	@Excel(name = "企业类别", width = 15)
    @ApiModelProperty(value = "企业类别")
    private java.lang.String businessType;
	/**企业地址*/
	@Excel(name = "企业地址", width = 15)
    @ApiModelProperty(value = "企业地址")
    private java.lang.String businessAdress;
	/**企业联系方式*/
	@Excel(name = "企业联系方式", width = 15)
    @ApiModelProperty(value = "企业联系方式")
    private java.lang.String businessPhone;
	/**企业员工数*/
	@Excel(name = "企业员工数", width = 15)
    @ApiModelProperty(value = "企业员工数")
    private java.lang.Integer businessYgs;
	/**法定代表人证件代码*/
	@Excel(name = "法定代表人证件代码", width = 15)
    @ApiModelProperty(value = "法定代表人证件代码")
    private java.lang.String fddbrZjdm;
	/**法定代表人证件号码*/
	@Excel(name = "法定代表人证件号码", width = 15)
    @ApiModelProperty(value = "法定代表人证件号码")
    private java.lang.String fddbrZjh;
	/**法定代表人姓名*/
	@Excel(name = "法定代表人姓名", width = 15)
    @ApiModelProperty(value = "法定代表人姓名")
    private java.lang.String fddbrName;
	/**法定代表人联系方式*/
	@Excel(name = "法定代表人联系方式", width = 15)
    @ApiModelProperty(value = "法定代表人联系方式")
    private java.lang.String fddbrPhone;
	/**治保负责人姓名*/
	@Excel(name = "治保负责人姓名", width = 15)
    @ApiModelProperty(value = "治保负责人姓名")
    private java.lang.String zbfzrName;
	/**治保负责人联系方式*/
	@Excel(name = "治保负责人联系方式", width = 15)
    @ApiModelProperty(value = "治保负责人联系方式")
    private java.lang.String zbfzrPhone;
	/**是否危化企业*/
	@Excel(name = "是否危化企业", width = 15)
    @ApiModelProperty(value = "是否危化企业")
    private java.lang.String sfwhqy;
	/**安全隐患类型*/
	@Excel(name = "安全隐患类型", width = 15)
    @ApiModelProperty(value = "安全隐患类型")
    private java.lang.String aqyhType;
	/**关注程度*/
	@Excel(name = "关注程度", width = 15)
    @ApiModelProperty(value = "关注程度")
    private java.lang.String gzcd;
	/**是否具备建立中共党组织条件*/
	@Excel(name = "是否具备建立中共党组织条件", width = 15)
    @ApiModelProperty(value = "是否具备建立中共党组织条件")
    private java.lang.String sfjbjltj;
	/**是否有中共党组织*/
	@Excel(name = "是否有中共党组织", width = 15)
    @ApiModelProperty(value = "是否有中共党组织")
    private java.lang.String sfzgdzz;
	/**中共党员数量*/
	@Excel(name = "中共党员数量", width = 15)
    @ApiModelProperty(value = "中共党员数量")
    private java.lang.Integer zgdyNum;
	/**是否有工会*/
	@Excel(name = "是否有工会", width = 15)
    @ApiModelProperty(value = "是否有工会")
    private java.lang.String sfgh;
	/**工会会员数量*/
	@Excel(name = "工会会员数量", width = 15)
    @ApiModelProperty(value = "工会会员数量")
    private java.lang.Integer ghhyNum;
	/**是否有共青团组织*/
	@Excel(name = "是否有共青团组织", width = 15)
    @ApiModelProperty(value = "是否有共青团组织")
    private java.lang.String sfgqtzz;
	/**共青团团员数量*/
	@Excel(name = "共青团团员数量", width = 15)
    @ApiModelProperty(value = "共青团团员数量")
    private java.lang.Integer gqttyNum;
	/**是否有妇联组织*/
	@Excel(name = "是否有妇联组织", width = 15)
    @ApiModelProperty(value = "是否有妇联组织")
    private java.lang.String sfflzz;
	/**妇女数量*/
	@Excel(name = "妇女数量", width = 15)
    @ApiModelProperty(value = "妇女数量")
    private java.lang.Integer womanNum;
	/**经度*/
	@Excel(name = "经度", width = 15)
    @ApiModelProperty(value = "经度")
    private java.lang.String longitude;
	/**纬度*/
	@Excel(name = "纬度", width = 15)
    @ApiModelProperty(value = "纬度")
    private java.lang.String latitude;
}
