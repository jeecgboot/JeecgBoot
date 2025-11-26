package org.jeecg.modules.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * @Description: app系统配置
 * @Author: jeecg-boot
 * @Date:   2021-07-07
 * @Version: V1.0
 * 
 * e3e3NcxzbUiGa53YYVXxWc8ADo5ISgQGx/gaZwERF91oAryDlivjqBv3wqRArgChupi+Y/Gg/swwGEyL0PuVFg==
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description="app系统配置")
public class SysAppVersion implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键")
    private String id;
	/**创建人*/
    @Schema(description = "创建人")
    private String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建日期")
    private java.util.Date createTime;
	/**更新人*/
    @Schema(description = "更新人")
    private String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新日期")
    private java.util.Date updateTime;
	/**所属部门*/
    @Schema(description = "所属部门")
    private String sysOrgCode;
	/**标题*/
	@Excel(name = "标题", width = 15)
    @Schema(description = "标题")
    private String appTitle;
	/**logo*/
	@Excel(name = "logo", width = 15)
    @Schema(description = "logo")
    private String appLogo;
	/**首页轮播图*/
	@Excel(name = "首页轮播图", width = 15)
    @Schema(description = "首页轮播图")
    private String carouselImgJson;
	/**首页菜单图*/
	@Excel(name = "首页菜单图", width = 15)
    @Schema(description = "首页菜单图")
    private String routeImgJson;
    /**app版本*/
    @Schema(description = "版本")
    private String appVersion;
    /**版本编码*/
    @Schema(description = "版本编码")
    private Integer versionNum;
    /**app下载路径*/
    @Schema(description = "app下载路径")
    private String downloadUrl;
    /**热更新路径*/
    @Schema(description = "热更新路径")
    private String wgtUrl;
    /**热更新路径*/
    @Schema(description = "桌面端下载路径")
    private String webDownloadUrl;
    /**更新内容*/
    @Schema(description = "更新内容")
    private String updateNote;
}
