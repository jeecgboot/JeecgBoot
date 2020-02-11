package org.jeecg.modules.jjshzz.shzz.entity;

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
 * @Description: 社会组织
 * @Author: jeecg-boot
 * @Date:   2020-02-11
 * @Version: V1.0
 */
@Data
@TableName("zz_shzz")
public class ZzShzz implements Serializable {
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
	/**统一社会信用代码*/
	@Excel(name = "统一社会信用代码", width = 15)
    @ApiModelProperty(value = "统一社会信用代码")
    private java.lang.String shxyCode;
	/**登记证号*/
	@Excel(name = "登记证号", width = 15)
    @ApiModelProperty(value = "登记证号")
    private java.lang.String djCode;
	/**社会组织名称*/
	@Excel(name = "社会组织名称", width = 15)
    @ApiModelProperty(value = "社会组织名称")
    private java.lang.String shzzName;
	/**登记管理机关代码*/
	@Excel(name = "登记管理机关代码", width = 15)
    @ApiModelProperty(value = "登记管理机关代码")
    private java.lang.String gljgCode;
	/**法定代表人姓名*/
	@Excel(name = "法定代表人姓名", width = 15)
    @ApiModelProperty(value = "法定代表人姓名")
    private java.lang.String fddbrName;
	/**住所*/
	@Excel(name = "住所", width = 15)
    @ApiModelProperty(value = "住所")
    private java.lang.String adress;
	/**批准日期*/
	@Excel(name = "批准日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "批准日期")
    private java.util.Date pzDate;
	/**社会组织类型*/
	@Excel(name = "社会组织类型", width = 15)
    @ApiModelProperty(value = "社会组织类型")
    private java.lang.String shzzType;
	/**状态*/
	@Excel(name = "状态", width = 15)
    @ApiModelProperty(value = "状态")
    private java.lang.String status;
	/**负责人证件代码*/
	@Excel(name = "负责人证件代码", width = 15)
    @ApiModelProperty(value = "负责人证件代码")
    private java.lang.String fzrDm;
	/**负责人证件号码*/
	@Excel(name = "负责人证件号码", width = 15)
    @ApiModelProperty(value = "负责人证件号码")
    private java.lang.String fzrHm;
	/**负责人姓名*/
	@Excel(name = "负责人姓名", width = 15)
    @ApiModelProperty(value = "负责人姓名")
    private java.lang.String fzrName;
	/**负责人联系方式*/
	@Excel(name = "负责人联系方式", width = 15)
    @ApiModelProperty(value = "负责人联系方式")
    private java.lang.String fzrPhone;
	/**办公地址*/
	@Excel(name = "办公地址", width = 15)
    @ApiModelProperty(value = "办公地址")
    private java.lang.String bgAdress;
	/**治保负责人姓名*/
	@Excel(name = "治保负责人姓名", width = 15)
    @ApiModelProperty(value = "治保负责人姓名")
    private java.lang.String fzfzrName;
	/**治保负责人联系方式*/
	@Excel(name = "治保负责人联系方式", width = 15)
    @ApiModelProperty(value = "治保负责人联系方式")
    private java.lang.String zbfzrPhone;
	/**关注程度*/
	@Excel(name = "关注程度", width = 15)
    @ApiModelProperty(value = "关注程度")
    private java.lang.String gzcd;
	/**是否具备建立中共党组织条件*/
	@Excel(name = "是否具备建立中共党组织条件", width = 15)
    @ApiModelProperty(value = "是否具备建立中共党组织条件")
    private java.lang.String sfjlzz;
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
    private java.lang.Integer womenNum;
	/**资金来源	*/
	@Excel(name = "资金来源	", width = 15)
    @ApiModelProperty(value = "资金来源	")
    private java.lang.String moneyLy;
	/**是否有境外背景*/
	@Excel(name = "是否有境外背景", width = 15)
    @ApiModelProperty(value = "是否有境外背景")
    private java.lang.String sfjwbj;
	/**经度*/
	@Excel(name = "经度", width = 15)
    @ApiModelProperty(value = "经度")
    private java.lang.String longitude;
	/**纬度*/
	@Excel(name = "纬度", width = 15)
    @ApiModelProperty(value = "纬度")
    private java.lang.String latitude;
}
