package org.jeecg.modules.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;

/**
 * @Description: 知识库-文档管理
 * @Author: jeecg-boot
 * @Date:   2022-07-21
 * @Version: V1.0
 */
@Data
@TableName("sys_files")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="sys_files对象", description="知识库-文档管理")
public class SysFiles {
    
	/**主键id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键id")
	private String id;
	/**文件名称*/
	@Excel(name = "文件名称", width = 15)
    @ApiModelProperty(value = "文件名称")
	private String fileName;
	/**文件地址*/
	@Excel(name = "文件地址", width = 15)
    @ApiModelProperty(value = "文件地址")
	private String url;
	/**创建人登录名称*/
	@Excel(name = "创建人登录名称", width = 15)
    @Dict(dicCode = "username",dicText = "realname",dictTable = "sys_user")
    @ApiModelProperty(value = "创建人登录名称")
	private String createBy;
	/**创建日期*/
	@Excel(name = "创建日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
	private Date createTime;
	/**更新人登录名称*/
	@Excel(name = "更新人登录名称", width = 15)
    @ApiModelProperty(value = "更新人登录名称")
	private String updateBy;
	/**更新日期*/
	@Excel(name = "更新日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
	private Date updateTime;
	/**文档类型（folder:文件夹 excel:excel doc:word pp:ppt image:图片  archive:其他文档 video:视频）*/
	@Excel(name = "文档类型（folder:文件夹 excel:excel doc:word pp:ppt image:图片  archive:其他文档 video:视频）", width = 15)
    @ApiModelProperty(value = "文档类型（folder:文件夹 excel:excel doc:word pp:ppt image:图片  archive:其他文档 video:视频）")
	private String fileType;
	/**文件上传类型(temp/本地上传(临时文件) manage/知识库 comment)*/
	@Excel(name = "文件上传类型(temp/本地上传(临时文件) manage/知识库 common(通用上传))", width = 15)
    @ApiModelProperty(value = "文件上传类型(temp/本地上传(临时文件) manage/知识库)")
	private String storeType;
	/**父级id*/
	@Excel(name = "父级id", width = 15)
    @ApiModelProperty(value = "父级id")
	private String parentId;
	/**租户id*/
	@Excel(name = "租户id", width = 15)
    @ApiModelProperty(value = "租户id")
	private String tenantId;
	/**文件大小（kb）*/
	@Excel(name = "文件大小（kb）", width = 15)
    @ApiModelProperty(value = "文件大小（kb）")
	private Double fileSize;
	/**是否文件夹(1：是  0：否)*/
	@Excel(name = "是否文件夹(1：是  0：否)", width = 15)
    @ApiModelProperty(value = "是否文件夹(1：是  0：否)")
	private String izFolder;
	/**是否为1级文件夹，允许为空 (1：是 )*/
	@Excel(name = "是否为1级文件夹，允许为空 (1：是 )", width = 15)
    @ApiModelProperty(value = "是否为1级文件夹，允许为空 (1：是 )")
	private String izRootFolder;
	/**是否标星(1：是  0：否)*/
	@Excel(name = "是否标星(1：是  0：否)", width = 15)
    @ApiModelProperty(value = "是否标星(1：是  0：否)")
	private String izStar;
	/**下载次数*/
	@Excel(name = "下载次数", width = 15)
    @ApiModelProperty(value = "下载次数")
	private Integer downCount;
	/**阅读次数*/
	@Excel(name = "阅读次数", width = 15)
    @ApiModelProperty(value = "阅读次数")
	private Integer readCount;
	/**分享链接*/
	@Excel(name = "分享链接", width = 15)
    @ApiModelProperty(value = "分享链接")
	private String shareUrl;
	/**分享权限(1.关闭分享 2.允许所有联系人查看 3.允许任何人查看)*/
	@Excel(name = "分享权限(1.关闭分享 2.允许所有联系人查看 3.允许任何人查看)", width = 15)
    @ApiModelProperty(value = "分享权限(1.关闭分享 2.允许所有联系人查看 3.允许任何人查看)")
	private String sharePerms;
	/**是否允许下载(1：是  0：否)*/
	@Excel(name = "是否允许下载(1：是  0：否)", width = 15)
    @ApiModelProperty(value = "是否允许下载(1：是  0：否)")
	private String enableDown;
	/**是否允许修改(1：是  0：否)*/
	@Excel(name = "是否允许修改(1：是  0：否)", width = 15)
    @ApiModelProperty(value = "是否允许修改(1：是  0：否)")
	private String enableUpdat;
	/**删除状态(0-正常,1-删除至回收站)*/
	@Excel(name = "删除状态(0-正常,1-删除至回收站)", width = 15)
    @ApiModelProperty(value = "删除状态(0-正常,1-删除至回收站)")
	private String delFlag;

    /**
     * 文件表不存在的字段：用户数据集合
     */
	@TableField(exist=false)
    private String userData;

    /**
     * 文件表不存在的字段：用户真实姓名
     */
    @TableField(exist=false)
    private String realname;

    /**
     * 文件表不存在的字段：压缩名称
     */
    @TableField(exist=false)
    private String zipName;
}
